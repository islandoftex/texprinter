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
package org.islandoftex.texprinter.utils

import org.islandoftex.texprinter.TeXPrinter
import javafx.application.Platform
import javafx.scene.control.ButtonType
import mu.KotlinLogging
import org.controlsfx.dialog.ExceptionDialog

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
    if (!TeXPrinter.isConsoleApplication) {
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
            //Desktop.getDesktop().browse(URL("https://gitlab.com/islandoftex/texprinter/issues").toURI())
          }
        }
      } catch (_: Exception) {
      } finally {
        System.exit(0)
      }
    } else {
      logger.error {
        "Unfortunately, TeXPrinter raised an exception. " +
        "It might be a bug, or simply a temporary technical dificulty.\n" +
        "MESSAGE: ${AppUtils.printStackTrace(Exception(e))}"
      }
      // exit the application
      System.exit(0)
    }
  }
}
