# XML

[![Build Status](https://github.com/openjax/xml/actions/workflows/build.yml/badge.svg)](https://github.com/openjax/xml/actions/workflows/build.yml)
[![Coverage Status](https://coveralls.io/repos/github/openjax/xml/badge.svg)](https://coveralls.io/github/openjax/xml)
[![Javadocs](https://www.javadoc.io/badge/org.openjax.xml/xml-maven-plugin.svg)](https://www.javadoc.io/doc/org.openjax.xml/xml-maven-plugin)
[![Released Version](https://img.shields.io/maven-central/v/org.openjax.xml/xml-maven-plugin.svg)](https://mvnrepository.com/artifact/org.openjax.xml/xml-maven-plugin)
![Snapshot Version](https://img.shields.io/nexus/s/org.openjax.xml/xml-maven-plugin?label=maven-snapshot&server=https%3A%2F%2Foss.sonatype.org)

## Introduction

Modules that provide convenient APIs related to XML.

## Modules

* **[api][api]**: An API for common functions and abstractions related to XML.
* **[datatype][datatype]**: Java bindings to XML data types as specified in [http://www.w3.org/TR/xmlschema11-2/][xml11-2].
* **[dom][dom]**: Utility functions and convenience patterns specific to Java's XML DOM libraries.
* **[sax][sax]**: Utility functions and convenience patterns specific to Java's XML SAX libraries.
* **[transform][transform]**: Utility functions and convenience patterns specializing in XML stylesheet transformations.
* **[xml-maven-plugin][xml-maven-plugin]**: Maven Plugin for general tasks related to XML, XSD and XSLT -- such as validation and transformation -- supporting [XML Schema 1.1][xml11-1].

## Contributing

Pull requests are welcome. For major changes, please [open an issue](../../issues) first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[api]: /api
[datatype]: /datatype
[dom]: /dom
[sax]: /sax
[transform]: /transform
[xml-maven-plugin]: /xml-maven-plugin
[xml11-1]: https://www.w3.org/TR/xmlschema11-1/
[xml11-2]: http://www.w3.org/TR/xmlschema11-2/