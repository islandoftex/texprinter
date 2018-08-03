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
package com.gitlab.cereda.texprinter

import com.gitlab.cereda.texprinter.config.Configuration
import com.gitlab.cereda.texprinter.generators.PDFGenerator
import com.gitlab.cereda.texprinter.generators.TeXGenerator
import com.gitlab.cereda.texprinter.model.Question
import com.gitlab.cereda.texprinter.utils.Dialogs
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import mu.KLogger
import mu.KotlinLogging
import java.text.SimpleDateFormat
import java.util.*

/**
 * The main class.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.0
 * @since 1.0
 */
class TeXPrinter : Application() {
  companion object {
    var isConsoleApplication: Boolean = false
    private const val DEBUG: Boolean = true
    private val config = Configuration()
    private lateinit var logger: KLogger
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
      logger = KotlinLogging.logger { }

      // the question id
      var questionId = ""

      // command line parser
      if (args.isNotEmpty()) {
        // assume command-line use
        // parse the arguments
        var outputFormat = ""

        if ("version" in args) {
          val config = Configuration()
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
          println("\u001b[2mCopyright (c) 2012-${SimpleDateFormat("yyyy").format(Date())}, Paulo Roberto Massa Cereda\u001B[0m")
          println("\u001B[2mCopyright (c) 2018-${SimpleDateFormat("yyyy").format(Date())}, Ben Frank\u001B[0m")
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
        Application.launch(TeXPrinter::class.java)
      } catch (ex: Exception) {
        Dialogs.showExceptionWindow(ex)
      }
    }
  }

  override fun start(primaryStage: Stage) {
    val root = FXMLLoader.load<Parent>(javaClass.getResource("/com/gitlab/cereda/texprinter/fxml/MainWindow.fxml"))
    primaryStage.icons.setAll(Image(javaClass.getResourceAsStream("/com/gitlab/cereda/texprinter/images/printer.png")))
    primaryStage.scene = Scene(root, 600.0, 350.0)
    primaryStage.title = "TeXPrinter"
    primaryStage.isResizable = false
    primaryStage.show()
  }
}
