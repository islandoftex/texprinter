/******************************************************************************
 * Copyright 2012-2018 Paulo Roberto Massa Cereda                             *
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
package org.islandoftex.texprinter

import javafx.scene.image.Image
import javafx.stage.Stage
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.islandoftex.texprinter.config.Configuration
import org.islandoftex.texprinter.generators.PDFGenerator
import org.islandoftex.texprinter.generators.TeXGenerator
import org.islandoftex.texprinter.model.Question
import org.islandoftex.texprinter.ui.MainWindowLayout
import org.islandoftex.texprinter.utils.Dialogs
import tornadofx.App
import tornadofx.launch
import java.time.LocalDate

/**
 * The main class.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.0
 * @since 1.0
 */
class TeXPrinter : App(MainWindowLayout::class) {
  override fun start(stage: Stage) {
    stage.isResizable = false
    stage.icons += Image("/org/islandoftex/texprinter/images/printer.png")
    super.start(stage)
  }

  companion object {
    var isConsoleApplication: Boolean = false
    val config = Json.parse(Configuration.serializer(),
        this::class.java
            .getResource("/org/islandoftex/texprinter/config/texprinter.json")
            .readText())

    private const val DEBUG: Boolean = false
    private val logger = KotlinLogging.logger { }

    /**
     * The main method.
     *
     * @param args The command line arguments.
     */
    @JvmStatic
    fun main(args: Array<String>) {
      // configure the logger
      System.setProperty("org.slf4j.simpleLogger.defaultLogLevel",
          if (DEBUG) "DEBUG" else "ERROR")

      // the question id
      var questionId = ""

      // command line parser
      if (args.isNotEmpty()) {
        // assume command-line use
        // parse the arguments
        var outputFormat = ""

        if ("version" in args) {
          println("TeXPrinter " + config.appVersionNumber + " - " + config.appVersionName)
          System.exit(0)
        } else if ("help" in args) {
          println("texprinter [ --question-id ID --output EXT | --version | --help ]")
        }

        args.forEach {
          val tmp = it.removePrefix("--").split("=")
          if (tmp.size == 2) {
            when (tmp[0]) {
              "question-id" -> {
                questionId = tmp[1]
              }
              "output" -> {
                outputFormat = if (tmp[1].equals("pdf", ignoreCase = true) ||
                                   tmp[1].equals("tex", ignoreCase = true)) {
                  tmp[1]
                } else {
                  ""
                }
              }
            }
          }
        }

        if (questionId.isNotBlank() && outputFormat.isNotBlank()) {
          // set the flag
          isConsoleApplication = true
          println("\u001b[1mTeXPrinter v${config.appVersionNumber} - A TeX.SX question printer\u001b[0m\n")
          println("\u001b[2mCopyright (c) 2012-${LocalDate.now().year}, Paulo Roberto Massa Cereda\u001B[0m")
          println("\u001B[2mCopyright (c) 2018-${LocalDate.now().year}, Ben Frank\u001B[0m")
          println("\u001B[2mAll rights reserved.\u001B[0m\n")

          // fetch the question
          val q = Question("http://tex.stackexchange.com/questions/" + questionId.trim())
          // set the filename
          var filename = questionId.trim()

          // check the result
          if (outputFormat == "pdf") {
            // set the filename to PDF
            filename = "$filename.pdf"
            // and generate it
            PDFGenerator.generate(q, filename)
          } else {
            // set the filename to TeX
            filename = "$filename.tex"
            // and generate it
            TeXGenerator.generate(q, filename)
          }

          logger.info { "Done! The new file was generated successfully! Have fun!" }
          System.exit(0)
        }
      }
      try {
        launch<TeXPrinter>(args)
      } catch (ex: Exception) {
        if (DEBUG) {
          ex.printStackTrace()
        }
        Dialogs.showExceptionWindow(ex)
      }
    }
  }
}
