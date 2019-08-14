# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Changed

* CLI has been simplified

## [3.0.4] – 2019-08-13

### Added

* Update dependencies to support Java 11

## [3.0.3] – 2019-03-04

### Fixed

* UTF-8 encoding is now enforced (even on non-UTF-8 platforms)
* HTML to TeX conversion is a bit more robust (especially image handling)

## [3.0.2] – 2019-02-21

### Changed

* Move to the new home on the Island of TeX (Gitlab group)

## [3.0.1] – 2019-01-18

### Fixed

* Adapt to changes in SX page model
* Fix smaller annoyances with JavaFX on JDK 11

## [3.0.0] (Yummy Pastéis) – 2018-08-27

### Changed

* Huge performance boost due to upgrade of the PDF library
* Polished and modern cross-platform UI using JavaFX
* Adapted to the latest SX data model.
* New minimum system requirement: Java 8
* Completely rewritten in Kotlin for better maintenance and user experience.
* Use of Gradle as build system

## [2.1] – 2012-03-21

### Added

* New interface design, cross-platform look and feel, help messages.
* Added support for Java 5, 6, 7 and OpenJDK 6.

### Changed

* Lots of classes added, renamed and removed.
* `iText` PDF library updated to the last stable version.

### Fixed

* Fixed several GUI font rendering issues.
* GUI redesign for a better user experience.

## [2.0] (Awesome Tiramisù) – 2011-10-31

### Added

* `changelog.html`
* Support for images
* User interface for the changelog, about window, exceptions

### Changed

* `Question` was completely rewritten. Fixed bugs related to migrated questions and answers. Fixed bugs related to community wiki questions and answers.
* Fixed minor annoyances.
* Refactored internal structure of the configuration
* Refactored many class names

## [1.1] – 2011-09-08

### Added

* Added `PostComparator` class to sort answers according to their votes and acceptance. Just to be sure of top answers being displayed first.
* Added test suite for the new sorting class.
* Added number of votes to both `PDFGenerator` and `TeXGenerator` classes. Now questions and answers *do* have their corresponding votes for easy conference.

### Changed

* Added version number and name control instead of hard-coding it.

## 1.0.2

### Fixed

* Fixed a critical bug in the `PDFGenerator` when dealing with invalid image references. If an invalid reference is found, a replacement image is used.

## 1.0.1

### Fixed

* Fixed a critical bug in the `StringHelper` class when the resource downloader could not find a proper image file. If an invalid reference is found, a replacement image is used.

## 1.0

* First release.

[Unreleased]: https://gitlab.com/islandoftex/texprinter/compare/v3.0.4...master
[3.0.3]: https://gitlab.com/islandoftex/texprinter/compare/v3.0.2...v3.0.4
[3.0.3]: https://gitlab.com/islandoftex/texprinter/compare/v3.0.2...v3.0.3
[3.0.2]: https://gitlab.com/islandoftex/texprinter/compare/v3.0.1...v3.0.2
[3.0.1]: https://gitlab.com/islandoftex/texprinter/compare/v3.0.0...v3.0.1
[3.0.0]: https://gitlab.com/islandoftex/texprinter/compare/v2.1...v3.0.0
[2.1]: https://gitlab.com/islandoftex/texprinter/compare/v2.0...v2.1
[2.0]: https://gitlab.com/islandoftex/texprinter/compare/v1.1...v2.0
[1.1]: https://gitlab.com/islandoftex/texprinter/commits/v1.1
