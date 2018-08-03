import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.text.SimpleDateFormat
import java.util.Date

buildscript {
  repositories {
    mavenCentral()
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
ext["releasename"] = "Yummy past\u00e9is"
ext["authorname"] = "Paulo Roberto Massa Cereda and Ben Frank"

repositories {
  mavenCentral()
}

plugins {
  kotlin("jvm") version "1.2.51"
  application
}

val kotlinVersion = plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion
dependencies {
  implementation(kotlin("stdlib", kotlinVersion))
  implementation(kotlin("stdlib-jdk8", kotlinVersion))
  implementation("com.itextpdf:itext7-core:7.1.2")
  implementation("com.itextpdf:html2pdf:2.0.2")
  implementation("org.jsoup:jsoup:1.11.3")
  implementation("io.github.microutils:kotlin-logging:1.5.8")
  implementation("org.slf4j:slf4j-simple:1.8.0-beta2") // needed for kotlin-logging
  if (JavaVersion.current() >= JavaVersion.VERSION_1_9) {
    implementation("org.controlsfx:controlsfx:9.0.0")
  } else {
    implementation("org.controlsfx:controlsfx:8.40.14")
  }
  testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.8")
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
      java.setSrcDirs(listOf("src/main/kotlin"))
    }
    "resources" {
      java.setSrcDirs(listOf("src/main/resources"))
    }
    "test" {
      java.setSrcDirs(listOf("src/test/kotlin"))
    }
  }
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ProcessResources> {
  outputs.upToDateWhen { false } // always reprocess
  filesMatching(listOf("**/*.md", "**/AboutWindow.fxml", "**/*.properties")) {
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

task<Jar>("uberJar") {
  dependsOn(":jar")
  classifier = "with-deps"
  duplicatesStrategy = DuplicatesStrategy.EXCLUDE
  configurations["compileClasspath"].forEach { file: File ->
    from(zipTree(file.absoluteFile))
  }
  from(compileKotlin.destinationDir)
  from(java.sourceSets["resources"].allSource)
}

tasks.withType<Jar> {
  doFirst {
    manifest {
      attributes["Implementation-Title"] = ext["projectDisplayName"]
      attributes["Implementation-Version"] = version
      attributes["Main-Class"] = ext["mainClassName"]
    }
  }
}

tasks {
  "compileKotlin" {
    doFirst {
      println("# BUILD Compiling for Kotlin JVM target ${compileKotlin.kotlinOptions.jvmTarget}")
      println("# BUILD Compiling with source and target compatibility: ${java.sourceCompatibility}")
    }
  }
  "compileJava" {
    dependsOn(":compileKotlin")
    if (JavaVersion.current() >= JavaVersion.VERSION_1_9) {
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
            "--add-exports=javafx.base/com.sun.javafx.runtime=ALL-UNNAMED",
            "--add-exports=javafx.web/com.sun.webkit=ALL-UNNAMED"
        )
        java.sourceSets["main"].compileClasspath = files()
      }
    }
  }
  "run" {
    doFirst { println("# BUILD Starting to run ${ext["moduleName"]}") }
    if (JavaVersion.current() >= JavaVersion.VERSION_1_9)
      inputs.property("moduleName", ext["moduleName"])
  }
  "jar" {
    doFirst { println("# BUILD Packaging application") }
  }
  "uberJar" {
    doFirst { println("# BUILD Packaging jar with dependencies") }
  }
  "assembleDist" {
    dependsOn(":uberJar")
  }
}
