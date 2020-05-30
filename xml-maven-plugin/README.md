# XML Maven Plugin

[![Build Status](https://travis-ci.org/openjax/xml.svg?1)](https://travis-ci.org/openjax/xml)
[![Coverage Status](https://coveralls.io/repos/github/openjax/xml/badge.svg?1)](https://coveralls.io/github/openjax/xml)
[![Javadocs](https://www.javadoc.io/badge/org.openjax.xml/xml-maven-plugin.svg?1)](https://www.javadoc.io/doc/org.openjax.xml/xml-maven-plugin)
[![Released Version](https://img.shields.io/maven-central/v/org.openjax.xml/xml-maven-plugin.svg?1)](https://mvnrepository.com/artifact/org.openjax.xml/xml-maven-plugin)
![Snapshot Version](https://img.shields.io/nexus/s/org.openjax.xml/xml-maven-plugin?label=maven-snapshot&server=https%3A%2F%2Foss.sonatype.org)

## Introduction

The `xml-maven-plugin` plugin is for general tasks related to XML, XSD, and XSLT.

This plugin supports [XML Schema 1.1][xml11].

## Goals Overview

* [`xml:validate`](#xmlvalidate) validates XML or XSD documents against schema(s) specified in the `xsi:schemaLocation` attribute.
* [`xml:transform`](#xmltransform) transforms XML documents by applying XSLT stylesheets.

## Usage

### `xml:validate`

The `xml:validate` goal is bound to the `compile` phase, and is used to validate XML documents against schema(s) specified in the `xsi:schemaLocation` attribute. The validator uses a SAX parser and supports [XML Schema 1.1][xml11].

#### Example 1

Execution with `includes` directive.

```xml
<plugin>
  <groupId>org.openjax.xml</groupId>
  <artifactId>xml-maven-plugin</artifactId>
  <version>0.9.4</version>
  <executions>
    <execution>
      <goals>
        <goal>validate</goal>
      </goals>
      <configuration>
        <includes>
          <include>**/*.xsd</include>
          <include>**/*.xml</include>
        </includes>
      </configuration>
    </execution>
  </executions>
</plugin>
```

#### Example 2

Execution with `includes` and `excludes` directives.

```xml
<plugin>
  <groupId>org.openjax.xml</groupId>
  <artifactId>xml-maven-plugin</artifactId>
  <version>0.9.4</version>
  <executions>
    <execution>
      <goals>
        <goal>validate</goal>
      </goals>
      <configuration>
        <includes>
          <include>**/*.xml</include>
          <include>**/*.xsd</include>
        </includes>
        <excludes>
          <exclude>**/willfail.xml</exclude>
        </excludes>
      </configuration>
    </execution>
  </executions>
</plugin>
```

#### Example 3

Execution with `includes`, `excludes`, and `resources` directives.

```xml
<plugin>
  <groupId>org.openjax.xml</groupId>
  <artifactId>xml-maven-plugin</artifactId>
  <version>0.9.4</version>
  <executions>
    <execution>
      <goals>
        <goal>validate</goal>
      </goals>
      <configuration>
        <includes>
          <include>**/*.xml</include>
          <include>**/*.xsd</include>
        </includes>
        <excludes>
          <exclude>**/willfail.xml</exclude>
        </excludes>
        <resources>
          <resource>i-am-on-the-classpath.xml</resource>
          <resource>META-INF/maven/plugin.xml</resource>
        </resources>
      </configuration>
    </execution>
  </executions>
</plugin>
```

### Configuration Parameters

| Name                              | Type    | Use      | Description                                                  |
|:----------------------------------|:--------|:---------|:-------------------------------------------------------------|
| <samp>/skip¹</samp>               | Boolean | Optional | Skip executioin. **Default:** `false`.                       |
| <samp>/includes¹</samp>           | List    | Optional | List of `include` patterns. **Default:** `null`.           |
| <samp>/includes/includeⁿ</samp>   | String  | Optional | [Pattern][pattern] of files to include. **Default:** `null`. |
| <samp>/excludes¹</samp>           | List    | Optional | List of `exclude` patterns. **Default:** `null`.           |
| <samp>/excludes/excludeⁿ</samp>   | String  | Optional | [Pattern][pattern] of files to exclude. **Default:** `null`. |
| <samp>/resources¹</samp>          | List    | Optional | List of `resource` names. **Default:** `null`.          |
| <samp>/resources/resourceⁿ</samp> | String  | Optional | Resource name on the classpath. **Default:** `null`.         |

### Execution Options

1. Running Maven in offline mode (`mvn -o`) will cause `xml:validate` to silently pass validation of XML files with remote `xsi:schemalocations`. For these files, the validator will only test whether the file is well formed.

### `xml:transform`

The `xml:transform` goal is bound to the `generate-resources` phase, and is used to transform XML documents with a XML Stylesheet Transformer (XSLT). The XSL Transformer supports [XSLT 2.0][xsl2].

#### Example 1

Execution with `includes` directive.

```xml
<plugin>
  <groupId>org.openjax.xml</groupId>
  <artifactId>xml-maven-plugin</artifactId>
  <version>0.9.4</version>
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

### Configuration Parameters

| Name                              | Type             | Use      | Description                                                                                                                       |
|:----------------------------------|:-----------------|:---------|:----------------------------------------------------------------------------------------------------------------------------------|
| <samp>/skip¹</samp>               | Boolean          | Optional | Skip executioin. **Default:** `false`.                                                                                            |
| <samp>/destDir¹</samp>            | String           | Required | Destination directory of transformed files.                                                                                       |
| <samp>/rename¹</samp><br>&nbsp;   | String<br>&nbsp; | Optional<br>&nbsp; | Regex pattern to rename input file to output file:<br>&nbsp;&nbsp;&nbsp;&nbsp;`/<input>/<output>/` **Default:** `null`. |
| <samp>/includes¹</samp>           | List             | Optional | List of `include` directives. **Default:** `null`.                                                                                |
| <samp>/includes/includeⁿ</samp>   | String           | Optional | [Pattern][pattern] of files to include. **Default:** `null`.                                                                      |
| <samp>/excludes¹</samp>           | List             | Optional | List of `exclude` directives. **Default:** `null`.                                                                                |
| <samp>/excludes/excludeⁿ</samp>   | String           | Optional | [Pattern][pattern] of files to exclude. **Default:** `null`.                                                                      |
| <samp>/resources¹</samp>          | List             | Optional | List of `resource` names. **Default:** `null`.                                                                                    |
| <samp>/resources/resourceⁿ</samp> | String           | Optional | Resource name on the classpath. **Default:** `null`.                                                                              |

## Contributing

Pull requests are welcome. For major changes, please [open an issue](../../issues) first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[pattern]: https://ant.apache.org/manual/dirtasks.html
[xml11]: https://www.w3.org/TR/xmlschema11-1/
[xsl2]: https://www.w3.org/TR/xslt20/