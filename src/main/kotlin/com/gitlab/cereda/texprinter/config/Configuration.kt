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

package com.gitlab.cereda.texprinter.config

import com.gitlab.cereda.texprinter.utils.AppUtils
import mu.KotlinLogging
import java.util.*

/**
 * Retrieves the application properties.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.0
 * @since 1.1
 */
class Configuration {
  companion object {
    // the application logger
    private val logger = KotlinLogging.logger { }
  }

  // the properties
  private val properties: Properties = Properties()

  /**
   * Gets the author names.
   *
   * @return The author names.
   */
  val appAuthor: String
    get() = properties.getProperty("AppAuthor")
        .toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8)

  /**
   * Gets the version number.
   *
   * @return The version number.
   */
  val appVersionNumber: String
    get() = properties.getProperty("AppVersionNumber")
        .toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8)

  /**
   * Gets the version name.
   *
   * @return The version name.
   */
  val appVersionName: String
    get() = properties.getProperty("AppVersionName")
        .toByteArray(Charsets.ISO_8859_1).toString(Charsets.UTF_8)

  /**
   * Gets the version URL used to check for newer versions.
   *
   * @return The version URL.
   */
  val appVersionURL: String
    get() = properties.getProperty("AppVersionURL")

  /**
   * Default constructor.
   */
  init {
    // lets try to load the configuration
    try {
      // get the configuration file
      javaClass.getResourceAsStream("/com/gitlab/cereda/texprinter/config/texprinter.properties").use {
        properties.load(it)
      }
    } catch (exception: Exception) {
      // something bad happened
      logger.error {
        "No configuration properties file was found. Probably a typo or " +
        "wrong path? MESSAGE: ${AppUtils.printStackTrace(exception)}"
      }
      // set a dummy app author
      properties.setProperty("AppAuthor", "Deep thought")
      // set a dummy app version number
      properties.setProperty("AppVersionNumber", "0.0")
      // and a dummy app version name
      properties.setProperty("AppVersionName", "Lazy developer")
      // set a dummy app version URL to fetch
      properties.setProperty("AppVersionURL", "Dummy URL")
    }
  }
}
