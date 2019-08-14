rootProject.name = "texprinter"

pluginManagement {
  resolutionStrategy {
    eachPlugin {
      if (requested.id.id.startsWith("kotlinx-serialization")) {
        useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
      }
    }
  }
}