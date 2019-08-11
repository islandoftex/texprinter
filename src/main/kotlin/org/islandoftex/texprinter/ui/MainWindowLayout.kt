// SPDX-License-Identifier: BSD-3-Clause

package org.islandoftex.texprinter.ui

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.TextField
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import javafx.scene.image.Image
import javafx.scene.layout.Border
import javafx.scene.layout.Priority
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import org.controlsfx.control.PopOver
import org.controlsfx.control.SegmentedButton
import org.controlsfx.glyphfont.Glyph
import tornadofx.*

class MainWindowLayout : View("TeXPrinter") {
  private fun addPopOver(e: Node, s: String) {
    val tf = TextFlow(Text(s))
    tf.prefWidth = 350.0
    tf.padding = Insets(7.0)
    val pop = PopOver(tf)
    pop.isDetachable = false
    pop.title = "About the output format"
    pop.arrowLocation = PopOver.ArrowLocation.TOP_CENTER
    e.setOnMouseReleased { pop.show(e) }
  }

  val controller: MainWindowController by inject()
  val outputFormat = ToggleGroup().apply {
    selectedToggleProperty().addListener { _, _, newToggle ->
      controller.outputFormat =
          (if (newToggle.userData.toString() == "pdf") MainWindowController.OutputFormat.PDF
          else MainWindowController.OutputFormat.TEX)
    }
  }
  var urlOrId: TextField by singleAssign()
  override val root = vbox {
    prefHeight = 350.0
    prefWidth = 600.0
    spacing = 10.0
    padding = Insets(5.0, 10.0, 5.0, 10.0)
    hbox {
      label {
        prefHeight = 30.0
        graphic = Glyph("FontAwesome", "COMMENTS").apply {
          fontSize = 30.0
        }
      }
      pane { hgrow = Priority.ALWAYS }
      label {
        prefHeight = 30.0
        text = "Welcome to TeXPrinter!"
        style {
          fontSize = Dimension(30.0, Dimension.LinearUnits.px)
          fontWeight = FontWeight.BOLD
        }
      }
      pane { hgrow = Priority.ALWAYS }
    }
    vbox {
      spacing = 5.0
      hbox {
        label {
          prefHeight = 26.0
          text = "Please enter the question's ID:"
          alignment = Pos.BASELINE_CENTER
          style {
            fontWeight = FontWeight.BOLD
          }
        }
        pane { hgrow = Priority.ALWAYS }
        add(SegmentedButton().apply {
          prefHeight = 26.0
          styleClass.add(SegmentedButton.STYLE_CLASS_DARK)
          buttons.setAll(ToggleButton("By URL").apply {
            userData = "url"
            prefHeight = 23.0
            isSelected = true
          }, ToggleButton("By ID (number)").apply {
            userData = "id"
            prefHeight = 23.0
          })
          toggleGroup.selectedToggleProperty().addListener { _, _, newToggle ->
            if (newToggle.userData == "id") {
              if (urlOrId.text.contains("[^\\d]".toRegex()))
                urlOrId.text = ""
              urlOrId.textProperty().addListener(controller.digitListener)
              urlOrId.promptText = "ID (only numbers)"
              controller.inputType = MainWindowController.InputType.ID
            } else {
              urlOrId.textProperty().removeListener(controller.digitListener)
              urlOrId.promptText = "URL (pointing to a TeX.SX post)"
              controller.inputType = MainWindowController.InputType.URL
            }
          }
          disableProperty().bind(controller.processing)
        })
      }
      urlOrId = textfield {
        promptText = "URL (pointing to a TeX.SX post)"
        textProperty().bindBidirectional(controller.urlOrId)
        disableProperty().bind(controller.processing)
      }
      titledpane("Please select the output format") {
        style {
          border = Border.EMPTY
        }
        isCollapsible = false
        disableProperty().bind(controller.processing)
        vbox {
          hbox {
            spacing = 5.0
            id = "pdfOutput"
            radiobutton {
              userData = "pdf"
              isSelected = true
              toggleGroup = outputFormat
            }
            label {
              padding = Insets(0.0, 5.0, 5.0, 2.0)
              graphic = Glyph("FontAwesome", "FILE_PDF_ALT").apply {
                fontSize = 38.0
              }
            }
            textflow {
              hgrow = Priority.ALWAYS
              text("Generate a PDF output from the provided question " +
                   "ID. All resources (e.g. images) will be embedded in the " +
                   "resulting PDF file. The document will be ready for viewing " +
                   "and printing.")
            }
            addPopOver(this,
                "TeXPrinter can generate two output formats: PDF and TeX. When choosing PDF, TeXPrinter relies on " +
                "iText, a PDF library. All elements are disposed in a very basic layout and then the output is " +
                "generated. This is the best choice if you don't have a TeX distro. There are cases in which the PDF " +
                "library can raise an exception; if that happens, please try again with the TeX output.")
          }
          hbox {
            spacing = 5.0
            id = "texOutput"
            radiobutton {
              userData = "tex"
              toggleGroup = outputFormat
            }
            label {
              padding = Insets(0.0, 5.0, 5.0, 2.0)
              graphic = Glyph("FontAwesome", "FILE_TEXT_ALT").apply {
                fontSize = 38.0
              }
            }
            textflow {
              hgrow = Priority.ALWAYS
              text("Generate a source TeX output from the provided " +
                   "question ID. All resources(e.g.images) will be downloaded " +
                   "to the current directory. You'll need to compile the " +
                   "TeX document.")
            }
            addPopOver(this,
                "For TeX output, TeXPrinter organizes everything inside an article document class, with " +
                "the listings package providing syntax highlighting. There are some TeX documents that won’t " +
                "compile at first, possibly because of a bad formatting in the HTML. TeXPrinter tries to guess the " +
                "correct syntax most of the time, but errors might happen. If your document doesn't compile at first, " +
                "fix it – it won't be difficult – and there you go, a nice PDF output. If the thread has images, they " +
                "are automatically downloaded to the current folder.")
          }
        }
      }
      hbox {
        spacing = 5.0
        alignment = Pos.BOTTOM_CENTER
        progressbar {
          prefWidth = 200.0
          prefHeight = 15.0
          progressProperty().bind(controller.progress)
        }
        label {
          textProperty().bind(controller.status)
          prefWidth = 275.0
          prefHeight = 17.0
        }
        pane { hgrow = Priority.ALWAYS }
        button {
          disableProperty().bind(controller.processing)
          prefWidth = 75.0
          text = "Fetch it!"
          setOnAction {
            controller.fetchIt()
          }
        }
      }
      pane { vgrow = Priority.ALWAYS }
      hbox {
        spacing = 10.0
        alignment = Pos.BOTTOM_CENTER
        padding = Insets(7.5, 0.0, 0.0, 0.0)
        imageview(Image("/org/islandoftex/texprinter/images/license.png")) {
          isPreserveRatio = true
          fitHeight = 22.0
        }
        textflow {
          prefHeight = 22.0
          padding = Insets(0.0, 5.0, 0.0, 0.0)
          text("All user contributions licensed under cc-wiki with attribution required.")
        }
        pane { hgrow = Priority.ALWAYS }
        label {
          prefHeight = 22.0
          graphic = Glyph("FontAwesome", "QUESTION_CIRCLE").apply {
            fontSize = 22.0
          }
          setOnMouseClicked { AboutWindowLayout().openModal() }
        }
      }
    }
  }
}