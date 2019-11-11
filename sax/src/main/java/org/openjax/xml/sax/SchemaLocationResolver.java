/* Copyright (c) 2008 OpenJAX
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.openjax.xml.sax;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.xml.XMLConstants;

import org.libj.util.Paths;
import org.libj.util.function.Throwing;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

class SchemaLocationResolver implements LSResourceResolver {
  static String getPath(final String referrer, final String location) {
    return Paths.isAbsolute(location) ? location : Paths.newPath(Paths.getCanonicalParent(referrer), location);
  }

  private static URL xmlSchemaXsd;
  private static URL xmlXsd;

  private final XMLCatalog catalog;
  private final String rootPath;

  SchemaLocationResolver(final XMLCatalog catalog, final String rootPath) {
    this.catalog = catalog;
    this.rootPath = rootPath;
  }

  @Override
  public LSInput resolveResource(final String type, final String namespaceURI, final String publicId, String systemId, final String baseURI) {
//    System.err.println("resolveResource(\"" + type + "\", \"" + namespaceURI + "\", \"" + publicId + "\", \"" + systemId + "\", \"" + baseURI + "\")");
    if (namespaceURI == null && systemId == null)
      return new LSInputImpl(systemId, publicId, baseURI);

    if (systemId == null)
      systemId = namespaceURI;
    else if (baseURI != null)
      systemId = getPath(baseURI, systemId);

    try {
      SchemaLocation schemaLocation = catalog.getSchemaLocation(namespaceURI);
      final Map<String,URL> directory;
      if (schemaLocation == null) {
        if (namespaceURI == null) {
          schemaLocation = catalog.getSchemaLocation(null);
          if (schemaLocation == null)
            catalog.putSchemaLocation(null, schemaLocation = new SchemaLocation(null));
        }
        else {
          if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(namespaceURI)) {
            if (xmlSchemaXsd == null)
              xmlSchemaXsd = Thread.currentThread().getContextClassLoader().getResource("xmlschema/XMLSchema.xsd");

            catalog.putSchemaLocation(XMLConstants.W3C_XML_SCHEMA_NS_URI, schemaLocation = new SchemaLocation(namespaceURI, xmlSchemaXsd));
          }
          else if (XMLConstants.XML_NS_URI.equals(namespaceURI)) {
            if (xmlXsd == null)
              xmlXsd = Thread.currentThread().getContextClassLoader().getResource("xmlschema/xml.xsd");

            catalog.putSchemaLocation(namespaceURI, schemaLocation = new SchemaLocation(systemId, xmlXsd));
          }
          else {
            final String path = getPath(baseURI != null ? baseURI : rootPath, systemId);
            schemaLocation = new SchemaLocation(namespaceURI, new URL(path));
          }
        }
      }

      directory = schemaLocation.getDirectory();
      URL url = directory.get(systemId);
      if (url == null) {
        if ("http://www.w3.org/2001/XMLSchema.dtd".equals(systemId)) {
          directory.put(systemId, url = Thread.currentThread().getContextClassLoader().getResource("xmlschema/XMLSchema.dtd"));
        }
        else if ("http://www.w3.org/2001/datatypes.dtd".equals(systemId)) {
          directory.put(systemId, url = Thread.currentThread().getContextClassLoader().getResource("xmlschema/datatypes.dtd"));
        }
        else if (Paths.isAbsolute(systemId)) {
          directory.put(systemId, url = new URL(systemId));
        }
      }

      final LSInput input = new LSInputImpl(systemId, publicId, baseURI);
      if (url != null)
        input.setByteStream(url.openStream());

//      System.out.println(url);
      return input;
    }
    catch (final IOException e) {
      Throwing.rethrow(e);
      throw new Error("Should never get here");
    }
  }
}