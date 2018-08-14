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
package com.gitlab.cereda.texprinter.utils

import com.gitlab.cereda.texprinter.TeXPrinter
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Provides a version checker for TeXPrinter. This class will try to
 * connect to the project's official homepage and check for a certain
 * file, read it, check the value against the built-in one and then
 * prompt if the values don't match.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.0
 * @since 2.0
 */
// TODO: gitlab file and URL
class VersionChecker : Thread() {

  /**
   * Checks for newer versions. This method will connect to the server,
   * read a certain file, get the current version value, check it against
   * the built-in value and then update the JLabel component accordingly.
   */
  override fun run() {
    // TODO: display wait message?

    // lets try
    try {
      // create a new configuration
      val config = TeXPrinter.config
      // create a new URL from config
      val url = URL(config.appVersionURL)
      // open stream
      url.openStream().use {
        // get the document builder factory
        val dbf = DocumentBuilderFactory.newInstance()
        // create a new document
        val db = dbf.newDocumentBuilder()
        // parse the input stream
        val doc = db.parse(it)

        // normalize it
        doc.documentElement.normalize()
        // get the version number
        val versionNumber = doc.getElementsByTagName("version").item(0).textContent
        // get the version name
        val versionName = doc.getElementsByTagName("name").item(0).textContent

        // if it os different
        if (versionName != config.appVersionName || versionNumber != config.appVersionNumber) {
          // there is a new version available
          Alert(Alert.AlertType.INFORMATION).apply {
            buttonTypes.setAll(ButtonType.YES, ButtonType.NO)
            headerText = "Update available"
            contentText = "Your current version of TeXPrinter is outdated. The latest release is $versionNumber.\n" +
                "Do you want to update your installation?"
            if (showAndWait().get() == ButtonType.YES) {
              // TODO: update
            }
          }
        } else {
          // current version is the latest
          // TODO: do we really want this?
          Alert(Alert.AlertType.INFORMATION).apply {
            buttonTypes.setAll(ButtonType.CLOSE)
            headerText = "No updates available"
            contentText = "Congratulations. You are already running the latest version"
            showAndWait()
          }
        }
      }
    } catch (e: Exception) {
      // TODO: display error?
    }
  }
}
