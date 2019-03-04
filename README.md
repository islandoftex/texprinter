# TeXPrinter

> **Current stable version:**
> [![current version](https://img.shields.io/badge/dynamic/json.svg?style=for-the-badge&color=lightgray&label=Yummy%20pastéis&query=%24.0.name&url=https%3A%2F%2Fgitlab.com%2Fapi%2Fv4%2Fprojects%2F6431709%2Frepository%2Ftags)](https://gitlab.com/islandoftex/texprinter/tags) – [Download TeXPrinter](https://gitlab.com/islandoftex/texprinter/tags)  
> **Development version:** [![build status](https://img.shields.io/gitlab/pipeline/islandoftex/texprinter.svg?style=for-the-badge)](https://gitlab.com/islandoftex/texprinter/commits/master)

## The application

**TeXPrinter** is a Kotlin application designed for the sole purpose of printing threads from 
[TeX.SX](http://tex.stackexchange.com/), a free, community driven Q&A for users of TeX, LaTeX, 
ConTeXt, and related typesetting systems. [TeX.SX](http://tex.stackexchange.com/) is a part of
the [Stack Exchange network of _Q&A_ websites](http://stackexchange.com/sites).

TeXPrint currently can print threads to the following formats: PDF and (La)TeX.

The PDF output is provided by the _iText_ library. It's a quick option if you don't intend to 
customize the output. If the thread has images, they are embedded in the final result.

The TeX option is recommended if you want to format the code the way you like it. It basically uses
the `article` style, the `listings` package and a pretty straightforward approach. If the thread
has images, they are downloaded to the current directory and correctly referenced in the document.
Of course, you need to compile it.

Please note that only the main [TeX.SX](http://tex.stackexchange.com/) website is supported.

## Highlights of Yummy pastéis (v3.0)

* Huge performance boost due to upgrade of the PDF library
* Completely rewritten in Kotlin for better maintenance and user experience.
* Polished UI using JavaFX
* New minimum system requirement: Java 8

## System requirements and Installation

TeXPrinter ships as ZIP file (JAR and a library folder with dependencies) and as a JAR with dependencies.
If you are using Java 8 you are good to go. On Linux you might need to install the `openjfx` package of
your distribution.

Should you be one of the Java 11 users please note that you will be required to invoke the JAR file with
additional arguments as done in our build file. We strongly recommend to use the stable Java 8 build for now.
Hence, we do not provide the (expert) guide for executing TeXPrinter on Java 11.

TeXPrinter does not need to be installed. Simply [download the JAR file](https://gitlab.com/islandoftex/texprinter/tags)
and execute it.

## License

This application is licensed under the [New BSD License](https://opensource.org/licenses/BSD-3-Clause).
I want to call your attention to the fact that the New BSD License has been verified as a GPL-compatible 
free software license by the [Free Software Foundation](http://www.fsf.org/), and has been vetted as an 
open source license by the [Open Source Initiative](http://www.opensource.org/).

## Building the application

TeXPrinter uses Gradle, so with any Java 9/Java 10 configuration and Gradle you are good to go.
Please note that as JavaFX is used you are required to have it included in your JDK.

Please note that you might need adapt the source compatibility in the Gradle file to Java 9 if you are
running anything above JDK 8. This enables the `module-info.java` file, disables a manifest workaround
and uses the appropriate library versions, if available.

Our typical test cases involve posts 101, 1319 and 57141.
