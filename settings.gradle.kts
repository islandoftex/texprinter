rootProject.name = "TeXPrinter"

pluginManagement {
  repositories {
    gradlePluginPortal()
    maven("https://kotlin.bintray.com/kotlinx")
  }
  resolutionStrategy {
    eachPlugin {
      if (requested.id.id.startsWith("kotlinx-serialization")) {
        useModule("org.jetbrains.kotlinx:kotlinx-gradle-serialization-plugin:${requested.version}")
      }
    }
  }
}