/******************************************************************************
 * Copyright 2012-2018 Paulo Roberto Massa Cereda and Ben Frank               *
 *                                                                            *
 * Redistribution and use in source and binary forms, with or                 *
 *  without modification, are permitted provided that the following           *
 *  conditions are met:                                                       *
 *                                                                            *
 * 1. Redistributions of source code must retain the above copyright          *
 * notice, this list of conditions and the following disclaimer.              *
 *                                                                            *
 * 2. Redistributions in binary form must reproduce the above copyright       *
 * notice, this list of conditions and the following disclaimer in the        *
 * documentation and/or other materials provided with the distribution.       *
 *                                                                            *
 * 3. Neither the name of the copyright holder nor the names of it            *
 *  contributors may be used to endorse or promote products derived           *
 *  from this software without specific prior written permission.             *
 *                                                                            *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS       *
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT         *
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS         *
 *  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE            *
 *  COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,      *
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,      *
 *  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS     *
 *  OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND    *
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR     *
 *  TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE    *
 *  USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *
 ******************************************************************************/
package com.gitlab.cereda.texprinter.ui

import com.gitlab.cereda.texprinter.generators.PDFGenerator
import com.gitlab.cereda.texprinter.generators.TeXGenerator
import com.gitlab.cereda.texprinter.model.Question
import javafx.application.Platform
import javafx.beans.value.ChangeListener
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.input.MouseEvent
import javafx.scene.layout.HBox
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import javafx.stage.Modality
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.util.Duration
import org.controlsfx.control.Notifications
import org.controlsfx.control.PopOver
import org.controlsfx.control.SegmentedButton
import java.net.URL
import java.util.*

/**
 * Provides the JavaFX controller for the main window.
 *
 * @author Ben Frank
 * @version 3.0
 * @since 3.0
 */
class MainWindowController : Initializable {
  @FXML
  private lateinit var outputFormat: ToggleGroup
  @FXML
  private lateinit var questionID: TextField
  @FXML
  private lateinit var inputType: SegmentedButton
  @FXML
  private lateinit var tpOut: TitledPane
  @FXML
  private lateinit var texOutput: HBox
  @FXML
  private lateinit var pdfOutput: HBox
  @FXML
  private lateinit var progress: ProgressBar
  @FXML
  private lateinit var statusLabel: Label
  @FXML
  private lateinit var start: Button

  private val digitListener = ChangeListener<String> { _, _, newValue ->
    if (!newValue.matches("\\d*".toRegex()))
      questionID.text = newValue.replace("[^\\d]".toRegex(), "")
  }

  private fun addPopOver(e: Node, s: String) {
    val tf = TextFlow(Text(s))
    tf.prefWidth = 350.0
    tf.padding = Insets(7.0)
    val pop = PopOver(tf)
    pop.isDetachable = false
    pop.title = "About the output format"
    pop.arrowLocation = PopOver.ArrowLocation.TOP_CENTER
    e.setOnMouseReleased {
      pop.show(e)
    }
  }

  override fun initialize(p0: URL?, p1: ResourceBundle?) {
    // validate user input
    inputType.toggleGroup.selectedToggleProperty().addListener { _, _, newToggle ->
      if (newToggle.userData == "id") {
        if (questionID.text.contains("[^\\d]".toRegex()))
          questionID.text = ""
        questionID.textProperty().addListener(digitListener)
        questionID.promptText = "ID (only numbers)"
      } else {
        questionID.textProperty().removeListener(digitListener)
        questionID.promptText = "URL (pointing to a TeX.SX post)"
      }
    }
    inputType.styleClass.addAll(SegmentedButton.STYLE_CLASS_DARK)

    // inform about output
    addPopOver(texOutput, "For TeX output, TeXPrinter organizes everything inside an article document class, with " +
                          "the listings package providing syntax highlighting. There are some TeX documents that won’t " +
                          "compile at first, possibly because of a bad formatting in the HTML. TeXPrinter tries to guess the " +
                          "correct syntax most of the time, but errors might happen. If your document doesn't compile at first, " +
                          "fix it – it won't be difficult – and there you go, a nice PDF output. If the thread has images, they " +
                          "are automatically downloaded to the current folder.")
    addPopOver(pdfOutput, "TeXPrinter can generate two output formats: PDF and TeX. When choosing PDF, TeXPrinter relies on " +
                          "iText, a PDF library. All elements are disposed in a very basic layout and then the output is " +
                          "generated. This is the best choice if you don't have a TeX distro. There are cases in which the PDF " +
                          "library can raise an exception; if that happens, please try again with the TeX output.")
  }

  @FXML
  fun fetchIt(actionEvent: ActionEvent) {
    if (questionID.text.isBlank() || outputFormat.selectedToggle.userData.toString().isBlank()) {
      Alert(Alert.AlertType.ERROR, "You didn't provide enough information to fetch a post.", ButtonType.OK).showAndWait()
      return
    }

    fun executeWhenDone() {
      Platform.runLater {
        statusLabel.text = ""
        progress.progress = 0.0
        listOf(inputType, questionID, tpOut, start).forEach {
          it.isDisable = false
        }
      }
    }

    Thread {
      Platform.runLater {
        listOf(inputType, questionID, tpOut, start).forEach {
          it.isDisable = true
        }
        progress.progress = ProgressBar.INDETERMINATE_PROGRESS
        statusLabel.text = "I'm fetching the question…"
      }

      val url = if (inputType.toggleGroup.selectedToggle.userData == "url") {
        val regex = "https://tex\\.stackexchange\\.com/([aq]|questions)/([\\d]*).*".toRegex()
        val id = regex.matchEntire(questionID.text.trim())?.groups?.get(2)?.value
        if (id.isNullOrBlank()) {
          Platform.runLater {
            Alert(Alert.AlertType.ERROR, "ID is invalid!").showAndWait()
            executeWhenDone()
          }
          return@Thread
        } else {
          "http://tex.stackexchange.com/questions/" + id.toString()
        }
      } else {
        "http://tex.stackexchange.com/questions/" + questionID.text.trim()
      }
      val q = Question(url)
      var filename = url.substringAfterLast("/").trim()

      Platform.runLater {
        statusLabel.text = "TeXPrinter is printing your output file…"
      }
      when (outputFormat.selectedToggle.userData) {
        "pdf" -> {
          filename = "$filename.pdf"
          PDFGenerator.generate(q, filename)
        }
        "tex" -> {
          filename = "$filename.tex"
          TeXGenerator.generate(q, filename)
        }
      }

      Platform.runLater {
        // TODO: doesn't work if window is not focused
        Notifications.create().apply {
          title("Success")
          owner(Screen.getPrimary())
          hideAfter(Duration.seconds(60.0))
          text("Finished fetching and saving the requested question and answers.\n" +
               "If you have chosen TeX output, don't forget to compile.")
          showInformation()
        }
        executeWhenDone()
      }
    }.start()
  }

  fun showHelp(mouseEvent: MouseEvent) {
    val newStage = Stage()
    val root = FXMLLoader.load<Parent>(javaClass.getResource("/com/gitlab/cereda/texprinter/fxml/AboutWindow.fxml"))
    newStage.icons.setAll(Image(javaClass.getResourceAsStream("/com/gitlab/cereda/texprinter/images/printer.png")))
    newStage.initModality(Modality.APPLICATION_MODAL)
    newStage.scene = Scene(root, 400.0, 300.0)
    newStage.title = "About TeXPrinter"
    newStage.isResizable = false
    newStage.showAndWait()
  }
}
