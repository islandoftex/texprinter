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

  if (JavaVersion.current() < JavaVersion.VERSION_1_8)
    throw Exception("Incompatible JAVA version")
  else if (JavaVersion.current() >= JavaVersion.VERSION_11)
    println("# BUILD WARNING: JDK 11 may miss JavaFX capabilities")
}

group = "com.gitlab.cereda"
version = "3.0.0"
ext["projectDisplayName"] = "TeXPrinter"
ext["projectName"] = ext["projectDisplayName"].toString().toLowerCase()
ext["moduleName"] = "$group.${ext["projectName"]}"
ext["mainClassName"] = "${ext["moduleName"]}.${ext["projectDisplayName"]}"
ext["releasename"] = "Yummy pastéis"
ext["authorname"] = "Paulo Roberto Massa Cereda and Ben Frank"

repositories {
  mavenCentral()
  maven("https://kotlin.bintray.com/kotlinx")
}

plugins {
  kotlin("jvm") version "1.2.61"
  application
  id("com.github.johnrengelman.shadow") version "2.0.4" // Apache 2.0
  id("kotlinx-serialization") version "0.6.1" // Apache 2.0
}

application {
  applicationName = ext["projectDisplayName"].toString()
  mainClassName = ext["mainClassName"].toString()
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_8
  targetCompatibility = sourceCompatibility
  sourceSets {
    "main" {
      java {
        setSrcDirs(listOf("src/main/kotlin"))
        if (sourceCompatibility >= JavaVersion.VERSION_1_9) {
          srcDir("src/main/jpms")
        }
      }
    }
    "resources" {
      java.setSrcDirs(listOf("src/main/resources"))
    }
    "test" {
      java.setSrcDirs(listOf("src/test/kotlin"))
    }
  }
}

val kotlinVersion = plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion
dependencies {
  implementation(kotlin("stdlib", kotlinVersion)) // Apache 2.0
  implementation(kotlin("stdlib-jdk8", kotlinVersion)) // Apache 2.0
  implementation("com.itextpdf:itext7-core:7.1.2") // AGPL 3.0
  implementation("com.itextpdf:html2pdf:2.0.2") // AGPL 3.0
  implementation("org.jsoup:jsoup:1.11.3") // MIT
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.6.1") // Apache 2.0
  implementation("io.github.microutils:kotlin-logging:1.5.9") // Apache 2.0
  implementation("org.slf4j:slf4j-simple:1.8.0-beta2") // MIT
  implementation("no.tornado:tornadofx:1.7.17") // Apache 2.0
  if (java.sourceCompatibility >= JavaVersion.VERSION_1_9) {
    implementation("org.controlsfx:controlsfx:9.0.0") // BSD 3-clause
  } else {
    implementation("org.controlsfx:controlsfx:8.40.14") // BSD 3-clause
  }
  testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.9") // Apache 2.0
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
        "application_releasename" to ext["releasename"],
        "application_authorname" to ext["authorname"],
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

val compileKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
compileJava.destinationDir = compileKotlin.destinationDir

if (JavaVersion.current() >= JavaVersion.VERSION_1_9) {
  task<Exec>("createStandaloneRuntime") {
    dependsOn(":uberJar")
    workingDir("build")
    commandLine(
        "jlink --module-path libs:${org.gradle.internal.jvm.Jvm.current().javaHome}/jmods:" +
        "${java.sourceSets["main"].compileClasspath.asPath} " +
        "--add-modules ${ext["moduleName"]} " +
        "--launcher ${ext["moduleName"]}=${ext["moduleName"]}/${ext["mainClassName"]} " +
        "--output dist --strip-debug --compress 2 --no-header-files --no-man-pages")
  }
}

val mainManifest: Manifest = DefaultManifest((project as ProjectInternal).fileResolver)
    .apply {
      attributes["Implementation-Title"] = ext["projectDisplayName"]
      attributes["Implementation-Version"] = version
      attributes["Main-Class"] = ext["mainClassName"]
      if (java.sourceCompatibility < JavaVersion.VERSION_1_9) {
        attributes["Automatic-Module-Name"] = ext["moduleName"]
      }
    }
tasks.withType<Jar> {
  doFirst { manifest.attributes.putAll(mainManifest.attributes) }
  appendix = "jdk" + java.targetCompatibility.majorVersion
}
tasks.withType<ShadowJar> {
  doFirst { manifest.attributes.putAll(mainManifest.attributes) }
  appendix = "jdk" + java.targetCompatibility.majorVersion + "-with-deps"
  classifier = ""
}
tasks.getByName("assembleDist").dependsOn("shadowJar")

tasks {
  "compileKotlin" {
    doFirst {
      println("# BUILD Compiling for Kotlin JVM target ${compileKotlin.kotlinOptions.jvmTarget}")
      println("# BUILD Compiling with source and target compatibility: ${java.sourceCompatibility}")
    }
  }
  "compileJava" {
    dependsOn(":compileKotlin")
    if (java.sourceCompatibility >= JavaVersion.VERSION_1_9) {
      inputs.property("moduleName", ext["moduleName"])
      doFirst {
        println("# BUILD Compiling module ${ext["moduleName"]}")
        compileJava.options.compilerArgs = listOf(
            // include Gradle dependencies as modules
            "--module-path", java.sourceSets["main"].compileClasspath.asPath,
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
        java.sourceSets["main"].compileClasspath = files()
      }
    }
  }
  "run" {
    doFirst { println("# BUILD Starting to run ${ext["moduleName"]}") }
    if (java.sourceCompatibility >= JavaVersion.VERSION_1_9)
      inputs.property("moduleName", ext["moduleName"])
  }
}
