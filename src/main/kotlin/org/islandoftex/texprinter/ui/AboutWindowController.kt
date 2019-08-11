// SPDX-License-Identifier: BSD-3-Clause

package org.islandoftex.texprinter.ui

import javafx.scene.Node
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import tornadofx.Controller
import tornadofx.observableListOf

/**
 * Provides the JavaFX controller for the about/changelog window.
 *
 * @author Ben Frank
 * @version 3.0
 * @since 3.0
 */
class AboutWindowController : Controller() {
  val changelogItems = observableListOf<Node>()
  init {
    val changeLogMD = javaClass
        .getResource("/org/islandoftex/texprinter/config/changelog.md")
        .readText()
    changeLogMD.split("\n").forEach {
      if (it.startsWith("#")) {
        changelogItems.add(TextFlow().apply {
          children.add(Text(it.replace("#", "").trim()).apply {
            font = Font.font(18.0)
            underlineProperty().set(true)
          })
        })
      } else if (it.isNotBlank()) {
        changelogItems.add(TextFlow().apply {
          children.add(Text(it.replace("*", "").trim()))
        })
      }
    }
  }
}
