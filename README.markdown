# TeXPrinter

> **Current stable version:** 1.1 - Apple Pie

## The application

**TeXPrinter** is a Java application designed for the sole purpose of printing threads from [TeX.SX](http://tex.stackexchange.com/), a free, community driven Q&A for users of TeX, LaTeX, ConTeXt, and related typesetting systems. [TeX.SX](http://tex.stackexchange.com/) is a part of the [Stack Exchange network of _Q&A_ websites](http://stackexchange.com/sites).

TeXPrint currently can print threads to the following formats: PDF and TeX.

The PDF output is provided by the _iText_ library. It's a quick option if you don't intend to customize the output. If the thread has images, they are embedded in the final result.

The TeX option is recommended if you want to format the code the way you like it. It basically uses the `article` style, the `listings` package and a pretty straightforward approach. If the thread has images, they are downloaded to the current directory and correctly referenced in the document. Of course, you need to compile it.

Please note that only the main [TeX.SX](http://tex.stackexchange.com/) website is supported.

## License

This application is licensed under the [New BSD License](http://www.opensource.org/licenses/bsd-license.php). I want to call your attention to the fact that the New BSD License has been verified as a GPL-compatible free software license by the [Free Software Foundation](http://www.fsf.org/), and has been vetted as an open source license by the [Open Source Initiative](http://www.opensource.org/).

## Changelog

### 1.1

+ Added `PostComparator` class to sort answers according to their votes and acceptance. Just to be sure of top answers being displayed first.

+ Added test suite for the new sorting class.

+ Added version number and name control instead of hard-coding it.

+ Added number of votes to both `PDFGenerator` and `TeXGenerator` classes. Now questions and answers *do* have their corresponding votes for easy conference.

### 1.0.2

+ Fixed a critical bug in the `PDFGenerator` when dealing with invalid image references. If an invalid reference is found, a replacement image is used.

### 1.0.1

+ Fixed a critical bug in the `StringHelper` class when the resource downloader could not find a proper image file. If an invalid reference is found, a replacement image is used.

### 1.0

+ First release.
