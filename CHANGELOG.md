# Changelog

## 3.x series

### WIP: 3.0.4

* Update dependencies to support Java 11

### 3.0.3

* UTF-8 encoding is now enforced (even on non-UTF-8 platforms)
* HTML to TeX conversion is a bit more robust (especially image handling)

### 3.0.2

* Move to the new home on the Island of TeX (Gitlab group)

### 3.0.1

* Adapt to changes in SX page model
* Fix smaller annoyances with JavaFX on JDK 11

### 3.0.0

* Huge performance boost due to upgrade of the PDF library
* Polished and modern cross-platform UI using JavaFX
* Adapted to the latest SX data model.
* New minimum system requirement: Java 8
* Completely rewritten in Kotlin for better maintenance and user experience.
* Use of Gradle as build system

## 2.x series

### 2.1

+ Lots of classes added, renamed and removed.
+ `iText` PDF library updated to the last stable version.
+ Fixed several GUI font rendering issues.
+ New interface design, cross-platform look and feel, help messages.
+ Added support for Java 5, 6, 7 and OpenJDK 6.
+ GUI redesign for a better user experience.

### 2.0

+ `Question` was completely rewritten. Fixed bugs related to migrated questions and answers. Fixed bugs related to community wiki questions and answers.
+ Fixed minor annoyances.
+ Renamed `net.sf.texprinter.conf` to `net.sf.texprinter.config`. Renamed `ConfigurationRetriever` to `Configuration`. Added `changelog.html`.
+ Renamed `MessagesHelper` to `Dialogs`. Renamed `DownloadHelper` to `Downloader`. Renamed `StringHelper` to `StringUtils`. Added `ExecutionLogging`. Added `LoopSaver`. Added `OutputController`. Added `Reporter`. Added `StringHandler`. Added `UIUtils`. Added `VersionChecker`.
+ Added `net.sf.texprinter.ui.images`. Added images.
+ Added `net.sf.texprinter.ui`. Added `AboutPanel`. Added `ChangelogPanel`. Added `ExceptionPanel`. Added `InputPanel`.

## 1.x series

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
