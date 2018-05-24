# TeXPrinter

> **Current stable version:** 3.0.0 - Yummy pastéis  
> [![build status](https://gitlab.com/ben.frank/texprinter/badges/master/pipeline.svg)](https://gitlab.com/ben.frank/texprinter/commits/master)

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

## Highlights of Yummy pastéis (v3.0.0)

* Huge performance boost due to upgrade of the PDF library
* Completely rewritten in Kotlin for better maintenance and user experience.
* Polished UI using JavaFX
* New minimum system requirement: Java 8

## License

This application is licensed under the [New BSD License](http://www.opensource.org/licenses/bsd-license.php).
I want to call your attention to the fact that the New BSD License has been verified as a GPL-compatible 
free software license by the [Free Software Foundation](http://www.fsf.org/), and has been vetted as an 
open source license by the [Open Source Initiative](http://www.opensource.org/).

## Building the application

TeXPrinter uses Gradle, so with any Java 9/Java 10 configuration and Gradle you are good to go.
Please note that as JavaFX is used you are required to have it included in your JDK.

If you're using Java 8 you need to run something like

    mv src/main/kotlin/module-info.java src/main/kotlin/module-info.java_NOTSOURCE

before executing gradle.
