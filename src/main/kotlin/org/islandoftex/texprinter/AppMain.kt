package org.islandoftex.texprinter

import com.github.ajalt.clikt.parameters.options.versionOption
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.islandoftex.texprinter.config.Configuration

/**
 * The main class.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 3.1
 * @since 1.0
 */
object AppMain {
  const val DEBUG: Boolean = false
  var isConsoleApplication: Boolean = false
  val config = Json.decodeFromString<Configuration>(this::class.java
          .getResource("/org/islandoftex/texprinter/config/texprinter.json")
          .readText())

  /**
   * The main method.
   *
   * @param args The command line arguments.
   */
  @JvmStatic
  fun main(args: Array<String>) {
    // configure the logger
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel",
        if (DEBUG) "DEBUG" else "ERROR")
    TeXPrinter()
        .versionOption(version = "${config.appVersionNumber} – " +
                                 config.appVersionName)
        .main(args)
  }
}