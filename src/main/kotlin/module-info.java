module com.gitlab.cereda.texprinter {
  // Kotlin compatibility
  requires kotlin.stdlib;
  // logging
  requires kotlin.logging;
  requires org.slf4j;
  requires org.slf4j.simple;
  // resources and JavaFX interface
  requires java.base;
  requires javafx.controls;
  requires javafx.graphics;
  requires javafx.fxml;
  requires javafx.web;
  requires controlsfx;
  // post analysis
  requires org.jsoup;
  requires java.xml;
  // PDF export
  requires layout;
  requires html2pdf;
  requires kernel;
  // module
  exports com.gitlab.cereda.texprinter;
  exports com.gitlab.cereda.texprinter.ui to javafx.fxml;
  opens com.gitlab.cereda.texprinter.ui to javafx.fxml;
}
