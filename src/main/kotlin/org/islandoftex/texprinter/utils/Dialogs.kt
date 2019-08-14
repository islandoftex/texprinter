// SPDX-License-Identifier: BSD-3-Clause

package org.islandoftex.texprinter.utils

import javafx.application.HostServices
import javafx.application.Platform
import javafx.scene.control.ButtonType
import mu.KotlinLogging
import org.controlsfx.dialog.ExceptionDialog
import org.islandoftex.texprinter.AppMain
import java.net.URL
import kotlin.system.exitProcess

/**
 * Provides message features to the other classes.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.0
 * @since 1.0
 */
object Dialogs {
  private val logger = KotlinLogging.logger { }

  /**
   * Displays the showExceptionWindow.
   */
  fun showExceptionWindow(e: Throwable) {
    // if it is GUI mode
    if (!AppMain.isConsoleApplication) {
      try {
        Platform.runLater {
          val exwin = ExceptionDialog(e)
          exwin.dialogPane.buttonTypes.setAll(ButtonType.YES, ButtonType.NO)
          exwin.headerText = "Houston, we have a problem."
          exwin.contentText = "Unfortunately, TeXPrinter raised an exception. " +
                              "It might be a bug, or simply a temporary technical dificulty.\n\n" +
                              "We would be pleased if you could open an issue on GitLab.\n" +
                              "Dou you want us to take you to the issue tracker?"
          if (exwin.showAndWait().get() == ButtonType.YES) {
            // TODO: use HostServices.showDocument()
            // Desktop.getDesktop().browse(URL("https://gitlab.com/islandoftex/texprinter/issues").toURI())
          }
        }
      } catch (_: Exception) {
      } finally {
        exitProcess(0)
      }
    } else {
      logger.error {
        "Unfortunately, TeXPrinter raised an exception. " +
        "It might be a bug, or simply a temporary technical dificulty.\n" +
        "MESSAGE: ${AppUtils.printStackTrace(Exception(e))}"
      }
      // exit the application
      exitProcess(0)
    }
  }
}
