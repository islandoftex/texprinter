import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.text.SimpleDateFormat
import java.util.Date

buildscript {
  var kotlin_version: String by extra
  kotlin_version = "1.2.41"

  repositories {
    mavenCentral()
  }
  dependencies {
    classpath(kotlin("gradle-plugin", kotlin_version))
  }

  if (JavaVersion.current() < JavaVersion.VERSION_1_8) {
    throw Exception("Incompatible JAVA version")
  }
}

group = "com.gitlab.cereda"
version = "3.0.0"
ext["projectDisplayName"] = "TeXPrinter"
ext["projectName"] = ext["projectDisplayName"].toString().toLowerCase()
ext["moduleName"] = "$group.${ext["projectName"]}"
ext["mainClassName"] = "${ext["moduleName"]}.${ext["projectDisplayName"]}"
ext["releasename"] = "Yummy past\u00e9is"
ext["authorname"] = "Paulo Roberto Massa Cereda and Ben Frank"

repositories {
  mavenCentral()
}

plugins {
  kotlin("jvm") version "1.2.41" // TODO: automate this variable
  application
}

val kotlin_version: String by extra

dependencies {
  implementation(kotlin("stdlib", kotlin_version))
  implementation(kotlin("stdlib-jdk8", kotlin_version))
  implementation("com.itextpdf:itext7-core:7.1.2")
  implementation("com.itextpdf:html2pdf:2.0.2")
  implementation("org.jsoup:jsoup:1.11.3")
  implementation("io.github.microutils:kotlin-logging:1.5.4")
  implementation("org.slf4j:slf4j-simple:1.8.0-beta2") // needed for kotlin-logging
  if (JavaVersion.current() >= JavaVersion.VERSION_1_9) {
    implementation("org.controlsfx:controlsfx:9.0.0")
  } else {
    implementation("org.controlsfx:controlsfx:8.40.14")
  }
  testImplementation("junit:junit:4.12")
}

application {
  applicationName = ext["projectDisplayName"].toString()
  mainClassName = ext["mainClassName"].toString()
}

java {
  sourceCompatibility = JavaVersion.current()
  targetCompatibility = sourceCompatibility
  sourceSets {
    "main" {
      java.srcDirs("src/main/kotlin")
    }
    "resources" {
      java.srcDirs("src/main/resources")
    }
  }
}

tasks.withType<ProcessResources> {
  outputs.upToDateWhen { false } // always reprocess
  filesMatching(listOf("**/*.html", "**/*.properties")) {
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

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
  // should set it for compileKotlin and compileTestKotlin
}

val jar: Jar by tasks
jar.manifest {
  attributes["Implementation-Title"] = ext["projectDisplayName"]
  attributes["Implementation-Version"] = version
  attributes["Main-Class"] = ext["mainClassName"]
}

val compileKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
compileJava.destinationDir = compileKotlin.destinationDir

tasks {
  "compileKotlin" {
    println("# BUILD Compiling for Kotlin JVM target ${compileKotlin.kotlinOptions.jvmTarget}")
    println("# BUILD Compiling with source and target compatibility: ${java.sourceCompatibility}")
  }
  "compileJava" {
    dependsOn(":compileKotlin")
    println("# BUILD Compiling module ${ext["moduleName"]}")
    if (JavaVersion.current() >= JavaVersion.VERSION_1_9) {
      inputs.property("moduleName", ext["moduleName"])
      doFirst {
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
            "--add-exports=javafx.base/com.sun.javafx.runtime=ALL-UNNAMED",
            "--add-exports=javafx.web/com.sun.webkit=ALL-UNNAMED"
        )
        java.sourceSets["main"].compileClasspath = files()
      }
    }
  }
  "run" {
    println("# BUILD Starting to run ${ext["moduleName"]}")
    if (JavaVersion.current() >= JavaVersion.VERSION_1_9)
      inputs.property("moduleName", ext["moduleName"])
  }
}