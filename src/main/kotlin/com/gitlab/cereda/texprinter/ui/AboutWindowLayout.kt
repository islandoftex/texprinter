/******************************************************************************
 * Copyright 2012-2018 Paulo Roberto Massa Cereda and Ben Frank               *
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