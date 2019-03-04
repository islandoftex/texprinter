import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.java.archives.internal.DefaultManifest
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.text.SimpleDateFormat
import java.util.Date

buildscript {
  repositories {
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx")
  }

  if (JavaVersion.current() < JavaVersion.VERSION_1_8) {
    throw Exception("Incompatible JAVA version")
  } else if (JavaVersion.current() >= JavaVersion.VERSION_11) {
    logger.warn("# BUILD WARNING: JDK 11 uses external JavaFX components!")
    logger.info("# BUILD INFO: We are expecting a JDK 11 build to happen on " +
                "a JDK that does not include the JavaFX components. If they " +
                "are present in your classpath the behavior may be undefined.")
  }

}

repositories {
  mavenCentral()
  maven("https://kotlin.bintray.com/kotlinx")
}

plugins {
  val kotlinVersion = "1.3.21"
  kotlin("jvm") version kotlinVersion
  application
  id("com.github.johnrengelman.shadow") version "4.0.3" // Apache 2.0
  id("kotlinx-serialization") version kotlinVersion     // Apache 2.0
}

val kotlinVersion = plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion
val javafxVersion = "11.0.1"
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
val javafxModules = file("src/main/jpms/module-info.java")
    .readLines().asSequence().filter {
      it.contains("javafx.")
    }.map {
      it.trim().substringBeforeLast(";").substringAfterLast("requires ")
    }.toList()

dependencies {
  if (JavaVersion.current() >= JavaVersion.VERSION_11) {
    logger.info("Using external JavaFX components")
    javafxModules.forEach {
      implementation("org.openjfx:${it.replace(".", "-")}:$javafxVersion:$javafxPlatform")
    }
  }
  implementation(kotlin("stdlib", kotlinVersion)) // Apache 2.0
  implementation(kotlin("stdlib-jdk8", kotlinVersion)) // Apache 2.0
  implementation(kotlin("reflect", kotlinVersion)) // Apache 2.0
  implementation("com.itextpdf:itext7-core:7.1.5") // AGPL 3.0
  implementation("com.itextpdf:html2pdf:2.1.2") // AGPL 3.0
  implementation("org.jsoup:jsoup:1.11.3") // MIT
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.10.0") // Apache 2.0
  implementation("io.github.microutils:kotlin-logging:1.6.25") // Apache 2.0
  implementation("org.slf4j:slf4j-simple:1.8.0-beta2") // MIT
  implementation("no.tornado:tornadofx:1.7.18") // Apache 2.0
  if (JavaVersion.current() >= JavaVersion.VERSION_1_9) {
    implementation("org.controlsfx:controlsfx:9.0.0") // BSD 3-clause
  } else {
    implementation("org.controlsfx:controlsfx:8.40.14") // BSD 3-clause
  }
  testImplementation("io.kotlintest:kotlintest-runner-junit5:3.2.1") // Apache 2.0
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
  sourceCompatibility = if (JavaVersion.current() > JavaVersion.VERSION_1_8)
    JavaVersion.VERSION_1_9
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
  kotlinOptions.jvmTarget = "1.8"
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

if (JavaVersion.current() >= JavaVersion.VERSION_1_9) {
  task<Exec>("createStandaloneRuntime") {
    dependsOn(":uberJar")
    workingDir("build")
    commandLine(
        "jlink --module-path libs:${org.gradle.internal.jvm.Jvm.current().javaHome}/jmods:" +
        "${sourceSets["main"].compileClasspath.asPath} " +
        "--add-modules $moduleName " +
        "--launcher $moduleName=$moduleName/$mainClass " +
        "--output dist --strip-debug --compress 2 --no-header-files --no-man-pages")
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
    main = mainClass // TODO: why?
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
