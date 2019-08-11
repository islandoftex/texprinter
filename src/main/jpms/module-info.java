// SPDX-License-Identifier: BSD-3-Clause

module org.islandoftex.texprinter {
  // Kotlin compatibility
  requires kotlin.stdlib;
  requires kotlinx.serialization.runtime;

  // logging
  requires kotlin.logging;
  requires org.slf4j;

  // resources and JavaFX interface
  requires javafx.base;
  requires javafx.controls;
  requires javafx.graphics;
  requires javafx.media;
  requires javafx.web;
  requires org.controlsfx.controls;
  requires tornadofx;

  // post analysis
  requires org.jsoup;
  requires java.xml;

  // PDF export
  requires layout;
  requires html2pdf;
  requires kernel;

  // module
  exports org.islandoftex.texprinter;
}
