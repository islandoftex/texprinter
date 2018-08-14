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
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ChangeListener
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.ProgressBar
import javafx.stage.Screen
import javafx.util.Duration
import org.controlsfx.control.Notifications
import tornadofx.Controller

/**
 * Provides the JavaFX controller for the main window.
 *
 * @author Ben Frank
 * @version 3.0
 * @since 3.0
 */
class MainWindowController : Controller() {
  enum class InputType { URL, ID }
  enum class OutputFormat { PDF, TEX }

  var inputType: InputType = InputType.URL
  var outputFormat: OutputFormat = OutputFormat.PDF
  val processing = SimpleBooleanProperty(false)
  val urlOrId = SimpleStringProperty("")
  val status = SimpleStringProperty("TeXPrinter is waiting for your input…")
  val progress = SimpleDoubleProperty(0.0) // later ProgressBar.INDETERMINATE_PROGRESS

  val digitListener = ChangeListener<String> { _, _, newValue ->
    if (!newValue.matches("\\d*".toRegex()))
      urlOrId.value = newValue.replace("[^\\d]".toRegex(), "")
  }

  fun fetchIt() {
    val questionID = urlOrId.value
    if (questionID.isBlank()) {
      Alert(Alert.AlertType.ERROR, "You didn't provide enough information to fetch a post.", ButtonType.OK).showAndWait()
      return
    }

    fun executeWhenDone() {
      Platform.runLater {
        status.value = ""
        progress.value = 0.0
        processing.set(false)
      }
    }

    Thread {
      Platform.runLater {
        processing.set(true)
        progress.value = ProgressBar.INDETERMINATE_PROGRESS
        status.value = "I'm fetching the question…"
      }

      val url = if (inputType == InputType.URL) {
        val regex = "https://tex\\.stackexchange\\.com/([aq]|questions)/([\\d]*).*".toRegex()
        val id = regex.matchEntire(questionID.trim())?.groups?.get(2)?.value
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
        "http://tex.stackexchange.com/questions/" + questionID.trim()
      }
      val q = Question(url)
      var filename = url.substringAfterLast("/").trim()

      Platform.runLater {
        status.value = "TeXPrinter is printing your output file…"
      }
      filename = "$filename.${outputFormat.name.toLowerCase()}"
      when (outputFormat) {
        OutputFormat.PDF -> PDFGenerator.generate(q, filename)
        OutputFormat.TEX -> TeXGenerator.generate(q, filename)
      }

      Platform.runLater {
        try {
          Notifications.create().apply {
            title("Success")
            owner(Screen.getPrimary())
            hideAfter(Duration.seconds(60.0))
            text("Finished fetching and saving the requested question and answers.\n" +
                 "If you have chosen TeX output, don't forget to compile.")
            showInformation()
          }
        } catch (_: Exception) {
          // ignored on purpose, because:
          // TODO: doesn't work if window is not focused
        }
        executeWhenDone()
      }
    }.start()
  }
}
