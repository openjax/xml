# OpenJAX Standard XML Maven Plugin

> Maven Plugin for XML validation and transformation

[![Build Status](https://travis-ci.org/openjax/standard-xml.png)](https://travis-ci.org/openjax/standard-xml)
[![Coverage Status](https://coveralls.io/repos/github/openjax/standard-xml/badge.svg)](https://coveralls.io/github/openjax/standard-xml)

### Introduction

The `xml-maven-plugin` plugin is used for general XML-related goals.

### Goals Overview

* [`xml:validate`](#xmlvalidate) validates XML files.
* [`xml:transform`](#xmltransform) transform XML files with XSL stylesheets.

### Usage

#### `xml:validate`

The `xml:validate` goal is bound to the `compile` phase, and is used to validate XML documents of types specified in the plugin's `configuration`. The validator uses a SAX parser and supports [XML Schema 1.1][xml11].

##### Example 1

Execution with `includes` directive.

```xml
<plugin>
  <groupId>org.openjax.standard.xml</groupId>
  <artifactId>xml-maven-plugin</artifactId>
  <version>0.9.2-SNAPSHOT</version>
  <executions>
    <execution>
      <goals>
        <goal>validate</goal>
      </goals>
      <configuration>
        <includes>
          <include>**/*.ddlx</include>
          <include>**/*.sqlx</include>
          <include>**/*.jsonx</include>
          <include>**/*.xsd</include>
          <include>**/*.xml</include>
        </includes>
      </configuration>
    </execution>
  </executions>
</plugin>
```

##### Example 2

Execution with `includes` and `excludes` directives.

```xml
<plugin>
  <groupId>org.openjax.standard.xml</groupId>
  <artifactId>xml-maven-plugin</artifactId>
  <version>0.9.2-SNAPSHOT</version>
  <executions>
    <execution>
      <goals>
        <goal>validate</goal>
      </goals>
      <configuration>
        <includes>
          <include>**/*.ddlx</include>
          <include>**/*.sqlx</include>
          <include>**/*.jsonx</include>
          <include>**/*.xsd</include>
          <include>**/*.xml</include>
        </includes>
        <excludes>
          <exclude>**/willfail.xml</exclude>
        </excludes>
      </configuration>
    </execution>
  </executions>
</plugin>
```

#### Configuration Parameters

| Name                | Type    | Use      | Description                                               |
|:--------------------|:--------|:---------|:----------------------------------------------------------|
| `/skip`             | Boolean | Optional | Skip executioin. **Default:** `false`.                    |
| `/includes`         | Set     | Optional | Set of `include` directives. **Default:** `null`.         |
| `/includes/include` | String  | Optional | Fileset pattern of files to include. **Default:** `null`. |
| `/excludes`         | Set     | Optional | Set of `exclude` directives. **Default:** `null`.         |
| `/excludes/exclude` | String  | Optional | Fileset pattern of files to exclude. **Default:** `null`. |

#### Execution Options

1. Executing Maven in offline mode (`mvn -o`) will cause `xml:validate` to silently pass validation of XML files with remote `xsi:schemalocations`, thus avoiding remote calls.

#### `xml:transform`

The `xml:transform` goal is bound to the `generate-resources` phase, and is used to transform XML documents with a XML Stylesheet (XSL). The XSL Transformer supports [XSLT 2.0][xsl2].

##### Example 1

Execution with `includes` directive.

```xml
<plugin>
  <groupId>org.openjax.standard.xml</groupId>
  <artifactId>xml-maven-plugin</artifactId>
  <version>0.9.2-SNAPSHOT</version>
  <executions>
    <execution>
      <goals>
        <goal>transform</goal>
      </goals>
      <configuration>
        <destDir>${project.build.directory}/generated-resources</destDir>
        <rename>/\.\S+$/.txt/</rename>
        <stylesheet>src/main/resources/stylesheet.xsl</stylesheet>
        <includes>
          <include>**/*.xml</include>
        </includes>
      </configuration>
    </execution>
  </executions>
</plugin>
```

#### Configuration Parameters

| Name                | Type    | Use      | Description                                                                               |
|:--------------------|:--------|:---------|:------------------------------------------------------------------------------------------|
| `/skip`             | Boolean | Optional | Skip executioin. **Default:** `false`.                                                    |
| `/destDir`          | String  | Required | Destination directory of transformed files.                                               |
| `/rename`           | String  | Optional | Regex pattern used to rename output files as: `/<search>/<replace>/` **Default:** `null`. |
| `/includes`         | Set     | Optional | Set of `include` directives. **Default:** `null`.                                         |
| `/includes/include` | String  | Optional | Fileset pattern of files to include. **Default:** `null`.                                 |
| `/excludes`         | Set     | Optional | Set of `exclude` directives. **Default:** `null`.                                         |
| `/excludes/exclude` | String  | Optional | Fileset pattern of files to exclude. **Default:** `null`.                                 |

### JavaDocs

JavaDocs are available [here](https://standard.openjax.org/xml/apidocs/).

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[mvn-plugin]: https://img.shields.io/badge/mvn-plugin-lightgrey.svg
[xml11]: https://www.w3.org/TR/xmlschema11-1/
[xsl2]: https://www.w3.org/TR/xslt20/