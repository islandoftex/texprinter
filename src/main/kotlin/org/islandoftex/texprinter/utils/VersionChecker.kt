// SPDX-License-Identifier: BSD-3-Clause

package org.islandoftex.texprinter.utils

import org.islandoftex.texprinter.TeXPrinter
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
