import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.java.archives.internal.DefaultManifest
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

buildscript {
  repositories {
    jcenter()
    maven("https://kotlin.bintray.com/kotlinx")
  }

  logger.lifecycle("Checking JDK compatibility")
  // everything older than Java 8 is unsupported
  if (JavaVersion.current() < JavaVersion.VERSION_1_8) {
    throw Exception("Incompatible Java version")
  }
  // Java 8 is supported and does not need special attention, let's look at the
  // more recent versions where currently only Java 11 is supported
  else if (JavaVersion.current() == JavaVersion.VERSION_11) {
    logger.warn("# BUILD WARNING: JDK 11 may use external JavaFX components!")
  }
  // check non-LTS versions
  else if (JavaVersion.current() == JavaVersion.VERSION_1_9 ||
           JavaVersion.current() == JavaVersion.VERSION_1_10 ||
           JavaVersion.current() > JavaVersion.VERSION_11) {
    logger.warn("# BUILD WARNING: You are using an intermediate Java version. " +
                "This build script will silently fail on unexpected behavior. " +
                "Make sure you have JavaFX set up properly.")
  }
}

repositories {
  jcenter()
  maven("https://kotlin.bintray.com/kotlinx")
}

plugins {
  val kotlinVersion = "1.3.30"
  kotlin("jvm") version kotlinVersion
  application
  id("com.github.johnrengelman.shadow") version "5.0.0"  // Apache 2.0
  id("kotlinx-serialization") version kotlinVersion      // Apache 2.0
}

val kotlinVersion = plugins.getPlugin(KotlinPluginWrapper::class).kotlinPluginVersion
val externalJFXVersion = "11.0.2"

val javafxModules = file("src/main/jpms/module-info.java")
    .readLines().asSequence().filter {
      it.contains("javafx.")
    }.map {
      it.trim().substringBeforeLast(";").substringAfterLast("requires ")
    }.toList()
val useExternalJFX: Boolean = if (JavaVersion.current() < JavaVersion.VERSION_11)
  false
else {
  val modules = ByteArrayOutputStream().use {
    logger.info("Fetching available java modules")
    exec {
      commandLine("java", "--list-modules")
      standardOutput = it
      errorOutput = it
      isIgnoreExitValue = true
    }
    it.toString().trim()
  }
  var external = false
  javafxModules.forEach {
    if (!modules.contains(it)) external = true
  }
  logger.info("Resorting to external JFX modules: $external")
  external
}
val javafxPlatform = org.gradle.internal.os.OperatingSystem.current().let {
  when {
    it.isWindows -> "win"
    it.isLinux -> "linux"
    it.isMacOsX -> "mac"
    else -> {// hope the best…
      logger.warn("OS not recognized")
      "linux"
    }
  }
}

dependencies {
  if (useExternalJFX) {
    logger.warn("# BUILD WARNING: Using external JavaFX components")
    javafxModules.forEach {
      implementation("org.openjfx:${it
          .replace(".", "-")}:$externalJFXVersion:$javafxPlatform")
    }
  }
  implementation(kotlin("stdlib", kotlinVersion))                              // Apache 2.0
  implementation(kotlin("stdlib-jdk8", kotlinVersion))                         // Apache 2.0
  implementation(kotlin("reflect", kotlinVersion))                             // Apache 2.0
  implementation("com.itextpdf:itext7-core:7.1.5")                             // AGPL 3.0
  implementation("com.itextpdf:html2pdf:2.1.2")                                // AGPL 3.0
  implementation("org.jsoup:jsoup:1.11.3")                                     // MIT
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.11.0") // Apache 2.0
  implementation("io.github.microutils:kotlin-logging:1.6.26")                 // Apache 2.0
  implementation("org.slf4j:slf4j-simple:1.8.0-beta4")                         // MIT
  implementation("no.tornado:tornadofx:1.7.18")                                // Apache 2.0
  implementation("org.controlsfx:controlsfx:${                                 // BSD 3-clause
  if (JavaVersion.current() >= JavaVersion.VERSION_11) "11.0.0"
  else "8.40.15"
  }")
  testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")           // Apache 2.0
}

group = "org.islandoftex"
version = "3.0.3"
val projectDisplayName = "TeXPrinter"
val projectName = projectDisplayName.toLowerCase()
val moduleName = "$group.$projectName"
val mainClass = "$moduleName.$projectDisplayName"
val releasename = "Yummy pastéis"
val authorname = "Paulo Roberto Massa Cereda and Ben Frank"

application {
  applicationName = projectDisplayName
  mainClassName = mainClass
}

java {
  sourceCompatibility = if (JavaVersion.current() == JavaVersion.VERSION_11)
    JavaVersion.VERSION_11
  else
    JavaVersion.VERSION_1_8
  targetCompatibility = sourceCompatibility
}

sourceSets {
  named("main").configure {
    java {
      setSrcDirs(listOf("src/main/kotlin"))
      if (project.java.sourceCompatibility >= JavaVersion.VERSION_1_9) {
        srcDir("src/main/jpms")
      }
    }
    resources {
      setSrcDirs(listOf("src/main/resources"))
    }
  }
  named("test").configure {
    java {
      setSrcDirs(listOf("src/test/kotlin"))
    }
  }
}

tasks.withType<KotlinCompile> {
  kotlinOptions.apply {
    apiVersion = "1.3"
    languageVersion = "1.3"
    jvmTarget = if (JavaVersion.current() == JavaVersion.VERSION_11)
      "11"
    else
      "1.8"
  }
}

tasks.withType<ProcessResources> {
  outputs.upToDateWhen { false } // always reprocess
  filesMatching(listOf("**/*.md", "**/*.json")) {
    println("# BUILD Processing file: " + this.file.absolutePath)
    expand(mapOf(
        "application_name" to project.name,
        "application_version" to project.version,
        "application_releasename" to releasename,
        "application_authorname" to authorname,
        "last_change" to SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()),
        "current_year" to SimpleDateFormat("yyyy").format(Date())
    ))
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
  }
}

val mainManifest: Manifest = DefaultManifest((project as ProjectInternal).fileResolver)
    .apply {
      attributes["Implementation-Title"] = projectDisplayName
      attributes["Implementation-Version"] = version
      attributes["Main-Class"] = mainClass
      if (java.sourceCompatibility < JavaVersion.VERSION_1_9) {
        attributes["Automatic-Module-Name"] = moduleName
      }
    }

val compileKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
compileKotlin.apply {
  doFirst {
    println("# BUILD Compiling for Kotlin JVM target ${compileKotlin.kotlinOptions.jvmTarget}")
    println("# BUILD Compiling with source and target compatibility: ${java.sourceCompatibility}")
  }
}
compileJava.apply {
  destinationDir = compileKotlin.destinationDir
  if (java.sourceCompatibility > JavaVersion.VERSION_1_8) {
    inputs.property("moduleName", moduleName)
    options.compilerArgs = listOf(
        // include Gradle dependencies as modules
        "--module-path", sourceSets["main"].compileClasspath.asPath,
        // various patches as long as controlsfx is not properly JDK 9 compatible
        // from https://bitbucket.org/controlsfx/controlsfx/src/<commit>/build.gradle?at=9.0.0
        "--add-exports=javafx.graphics/com.sun.javafx.scene.traversal=ALL-UNNAMED",
        "--add-exports=javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED",
        "--add-exports=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED",
        "--add-exports=javafx.controls/com.sun.javafx.scene.control.inputmap=ALL-UNNAMED",
        "--add-exports=javafx.base/com.sun.javafx.event=ALL-UNNAMED",
        "--add-exports=javafx.base/com.sun.javafx.collections=ALL-UNNAMED",
        "--add-exports=javafx.base/com.sun.javafx.runtime=ALL-UNNAMED"
    )
    if (JavaVersion.current() >= JavaVersion.VERSION_11) {
      options.compilerArgs = listOf(*options.compilerArgs.toTypedArray(),
          "--add-modules", javafxModules.joinToString(",")
      )
    }
  }
}

tasks {
  named<Jar>("jar") {
    manifest.attributes.putAll(mainManifest.attributes)
    archiveAppendix.set("jdk" + java.targetCompatibility.majorVersion)
  }
  named<ShadowJar>("shadowJar") {
    manifest.attributes.putAll(mainManifest.attributes)
    archiveAppendix.set("jdk" + java.targetCompatibility.majorVersion + "-with-deps")
    archiveClassifier.set("")
  }
  named<JavaExec>("run") {
    if (JavaVersion.current() >= JavaVersion.VERSION_11) {
      doFirst {
        logger.info("Appending JVM arguments for external JavaFX")
        jvmArgs = listOf(
            "--module-path", classpath.asPath,
            "--add-modules", javafxModules.joinToString(","),
            "--add-exports=javafx.base/com.sun.javafx.runtime=ALL-UNNAMED",
            "--add-exports=javafx.graphics/com.sun.javafx.css=ALL-UNNAMED",
            "--add-exports=javafx.graphics/com.sun.javafx.geom=ALL-UNNAMED"
        )
      }
    }
  }
}
tasks.named<Task>("assembleDist").configure { dependsOn("shadowJar") }
