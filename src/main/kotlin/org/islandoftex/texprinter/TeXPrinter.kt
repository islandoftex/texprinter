// SPDX-License-Identifier: BSD-3-Clause

package org.islandoftex.texprinter

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import org.islandoftex.texprinter.AppMain.config
import org.islandoftex.texprinter.generators.PDFGenerator
import org.islandoftex.texprinter.generators.TeXGenerator
import org.islandoftex.texprinter.model.Question
import org.islandoftex.texprinter.ui.GUI
import tornadofx.launch
import java.io.File
import java.time.LocalDate
import kotlin.system.exitProcess

/**
 * The CLI.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.1
 * @since 3.1
 */
class TeXPrinter : CliktCommand() {
  private val cli by option(help = "Run the application in CLI mode without GUI.")
      .flag("--gui", default = false)
  private val questionId by option(help = "The integer identifier of the question to print.")
      .int()
      .default(Int.MIN_VALUE)
  private val output by option(help = "The output format of the print run.")
      .choice("pdf", "tex")
  private val filename by option(help = "The file to save to.")
      .file(mustExist = false, canBeDir = false)

  override fun run() {
    AppMain.isConsoleApplication = cli
    if (questionId != Int.MIN_VALUE && output != null) {
      val file = filename ?: File("$questionId.$output")

      echo("\u001b[1mTeXPrinter v${config.appVersionNumber} - A TeX.SX question printer\u001b[0m\n")
      echo("\u001b[2mCopyright (c) 2012-${LocalDate.now().year}, Paulo Roberto Massa Cereda\u001B[0m")
      echo("\u001B[2mCopyright (c) 2018-${LocalDate.now().year}, Ben Frank\u001B[0m")
      echo("\u001B[2mAll rights reserved.\u001B[0m\n")

      // fetch the question
      val q = Question("http://tex.stackexchange.com/questions/$questionId")

      // check the result
      if (output == "pdf") {
        PDFGenerator.generate(q, file.name)
      } else {
        TeXGenerator.generate(q, file.name)
      }

      echo("Done! The new file was generated successfully! Have fun!")
      exitProcess(0)
    } else if (cli) {
      echo("No parameters provided!", err = true)
    }
    if (!cli) {
      try {
        launch<GUI>()
      } catch (ex: Exception) {
        if (AppMain.DEBUG) {
          ex.printStackTrace()
        }
      }
    }
  }
}
