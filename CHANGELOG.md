# Changes by Version

## [v1.0.0-SNAPSHOT](https://github.com/libj/util/compare/d0c67d4e541efec6c78816639664513db64488dd..HEAD)

## [v0.9.5](https://github.com/openjax/xml/compare/cb61e8271d217eb106e6d62b972e0562fb20697c..d0c67d4e541efec6c78816639664513db64488dd) (2023-09-20)
* #12 EntityResolver does not follow redirects
* #10 Rewrite CharacterDatas
* #9 Support standalone execution
* #8 Add base64BinaryNonEmpty to datatypes-0.9.xsd
* #7 Null pointer exception when validating XML with no targetNamespace defined in XSD
* #6 Transition to GitHub Actions
* #5 Add Documents class for operations pertaining to Document
* #4 Upgrade Maven dependencies
* #2 Properly set line/column for new SAXParseException in FastSAXParser.parse(...)
* #1 FastSAXParser should throw SAXParseException instead of IllegalStateException in case of malformed XML

## [v0.9.4](https://github.com/openjax/xml/compare/cc44b4706d3802ab7a6737f11bcc84341c7fe1de..cb61e8271d217eb106e6d62b972e0562fb20697c) (2020-05-23)
* Add `CachedInputSource`.
* Add `DelegateErrorHandler`.
* Add `FastSAXHandler`.
* Add `FastSAXParser`.
* Add `FasterSAXHandler`.
* Move `Parsers` to `SAXParsers`.
* Support parsing and validation with XInclude.
* Rewrite `SchemaResolver`.
* Rewrite `XmlCatalog`.
* Add `XmlCatalogResolver`, `XmlEntity`, `XmlPreview`, and `XmlPreviewParser`.
* Upgrade `net.sf.saxon:Saxon-HE` from `v9.9.1-4` to `v9.9.1-5`.
* Improve tests.
* Improve javadocs.

## [v0.9.3](https://github.com/openjax/xml/compare/d2b32b14b98199fa90721eb9b345c4abaa7447c2..cc44b4706d3802ab7a6737f11bcc84341c7fe1de) (2019-07-21)
* Improve date regex in `datatypes-0.8.xsd`.
* Add `SilentErrorHandler`.
* Add configurable logging levels to `LoggingErrorHandler`.
* Refactor `FileSetMojo` to `PatternSetMojo`.
* Fix concurrency issue in `Parsers`.
* Add basic and extended iso8601 formats to `datatypes-0.9.xsd`.
* Upgrade `http://www.openjax.org/xml/datatypes-0.8.xsd` to `http://www.openjax.org/xml/datatypes-0.9.xsd`.
* Upgrade `net.sf.saxon:Saxon-HE:9.9.1-2` to `9.9.1-4`.
* Upgrade `org.libj:net:0.5.0` to `0.5.1`.

## [v0.9.2](https://github.com/entinae/pom/compare/bc0f34c466b5947f934f105af188f13d7b9ebaa8..d2b32b14b98199fa90721eb9b345c4abaa7447c2) (2019-05-13)
* Initial public release.