package org.islandoftex.texprinter.ui

import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.App

/**
 * The GUI
 *
 * @author Ben Frank
 * @version 3.1
 * @since 3.1
 */
class GUI : App(MainWindowLayout::class) {
  override fun start(stage: Stage) {
    stage.maxHeight = 375.0
    stage.minWidth = 600.0
    stage.icons += Image("/org/islandoftex/texprinter/images/printer.png")
    super.start(stage)
  }
}