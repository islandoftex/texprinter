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
}

group = "com.gitlab.cereda"
version = "3.0.0"
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
  implementation(files("/usr/lib/jvm/java-8-openjdk/jre/lib/ext/jfxrt.jar"))
  implementation(kotlin("stdlib", kotlin_version))
  implementation(kotlin("stdlib-jdk8", kotlin_version))
  implementation(kotlin("runtime", kotlin_version))
  implementation(kotlin("reflect", kotlin_version))
  implementation("com.itextpdf:itext7-core:7.1.2")
  implementation("com.itextpdf:html2pdf:2.0.2")
  implementation("org.jsoup:jsoup:1.11.3")
  implementation("io.github.microutils:kotlin-logging:1.5.4")
  implementation("org.slf4j:slf4j-simple:1.8.0-beta2") // needed for kotlin-logging
  implementation("org.controlsfx:controlsfx:8.40.14")
  testImplementation("junit:junit:4.12")
}

application {
  mainClassName = "$group.texprinter.TeXPrinter"
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_10
  targetCompatibility = sourceCompatibility
  sourceSets {
    "main" {
      java.srcDirs(
          "src/main/kotlin"
      )
    }
    "resources" {
      java.srcDirs(
          "src/main/resources"
      )
    }
  }
}

tasks.withType<ProcessResources> {
  this.outputs.upToDateWhen { false } // always reprocess
  filesMatching(listOf("**/*.html","**/*.properties")) {
    println("Processing file: " + this.file.absolutePath)
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

val compileKotlin: KotlinCompile by tasks
val compileTestKotlin: KotlinCompile by tasks
val compileJava: JavaCompile by tasks
tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
  // should set it for compileKotlin and compileTestKotlin
}

tasks {
  "compileKotlin" {
    println("Compiling for Kotlin JVM target ${compileKotlin.kotlinOptions.jvmTarget}")
    println("Compiling with source and target compatibility: ${java.sourceCompatibility}")
  }
}