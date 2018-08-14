package com.gitlab.cereda.texprinter.ui

import com.gitlab.cereda.texprinter.TeXPrinter
import javafx.geometry.Insets
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import tornadofx.*
import java.time.LocalDate

class AboutWindowLayout : View("About") {
  override val root = vbox {
    prefHeight = 300.0
    prefWidth = 400.0
    tabpane {
      tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
      vgrow = Priority.ALWAYS

      tab<AboutTab>()
      tab<ChangelogTab>()
    }
  }

  class AboutTab : View("About TeXPrinter") {
    override val root = vbox {
      spacing = 10.0
      padding = Insets(5.0)
      textflow {
        text("Version ${TeXPrinter.config.appVersionNumber} " +
             "(${TeXPrinter.config.appVersionName})\n")
        text("Copyright 2012-${LocalDate.now().year} " +
             "${TeXPrinter.config.appAuthor}. All rights reserved.")
      }
      textflow {
        text("This application is licensed under the New BSD License. " +
             "I want to call your attention to the fact that the New BSD License " +
             "has been verified as a GPL-compatible free software license by the " +
             "Free Software Foundation, and has been vetted as an open source " +
             "license by the Open Source Initiative.")
      }
    }
  }

  class ChangelogTab : View("Changelog") {
    val controller: AboutWindowController by inject()
    override val root = scrollpane {
      isFitToHeight = true
      isFitToWidth = true
      vbox {
        spacing = 10.0
        padding = Insets(5.0)
        children.addAll(controller.changelogItems)
      }
    }
  }
}