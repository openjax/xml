/* Copyright (c) 2008 lib4j
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

package org.lib4j.xml.sax;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.xml.XMLConstants;

import org.lib4j.lang.Resources;
import org.lib4j.net.CachedURL;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class SchemaLocationResolver implements LSResourceResolver {
  private static URL xmlSchemaXsd = null;
  private static URL xmlXsd = null;

  private final XMLCatalog catalog;

  public SchemaLocationResolver(final XMLCatalog catalog) {
    this.catalog = catalog;
  }

  @Override
  public LSInput resolveResource(final String type, final String namespaceURI, final String publicId, String systemId, final String baseURI) {
    if (namespaceURI == null && systemId == null)
      return new LSInputImpl(systemId, publicId, baseURI);

    if (systemId == null)
      systemId = namespaceURI;
    else if (baseURI != null)
      systemId = SchemaLocationHandler.getPath(baseURI, systemId);

    try {
      SchemaLocation schemaLocation = catalog.get(namespaceURI);
      final Map<String,CachedURL> directory;
      if (schemaLocation == null) {
        if (namespaceURI == null) {
          SchemaLocation nullLocation = catalog.get(null);
          if (nullLocation == null)
            catalog.put(null, nullLocation = new SchemaLocation(null));

          directory = nullLocation.getDirectory();
        }
        else if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(namespaceURI)) {
          if (xmlSchemaXsd == null)
            xmlSchemaXsd = Resources.getResource("xmlschema/XMLSchema.xsd").getURL();

          catalog.put(XMLConstants.W3C_XML_SCHEMA_NS_URI, schemaLocation = new SchemaLocation(namespaceURI));
          schemaLocation.getDirectory().put(namespaceURI, new CachedURL(xmlSchemaXsd));
          directory = schemaLocation.getDirectory();
        }
        else if (XMLConstants.XML_NS_URI.equals(namespaceURI) && "http://www.w3.org/2001/xml.xsd".equals(systemId)) {
          if (xmlXsd == null)
            xmlXsd = Resources.getResource("xmlschema/xml.xsd").getURL();

          catalog.put(namespaceURI, schemaLocation = new SchemaLocation(systemId));
          schemaLocation.getDirectory().put(systemId, new CachedURL(xmlXsd));
          directory = schemaLocation.getDirectory();
        }
        else {
          return new LSInputImpl(systemId, publicId, baseURI);
        }
      }
      else {
        directory = schemaLocation.getDirectory();
      }

      CachedURL url = directory.get(systemId);
      if (url == null) {
        if (namespaceURI == null) {
          if ("http://www.w3.org/2001/XMLSchema.dtd".equals(systemId)) {
            directory.put(systemId, url = new CachedURL(Thread.currentThread().getContextClassLoader().getResource("xmlschema/XMLSchema.dtd")));
          }
          else if ("http://www.w3.org/2001/datatypes.dtd".equals(systemId)) {
            directory.put(systemId, url = new CachedURL(Thread.currentThread().getContextClassLoader().getResource("xmlschema/datatypes.dtd")));
          }
          else {
            return new LSInputImpl(systemId, publicId, baseURI);
          }
        }
        else {
          return new LSInputImpl(systemId, publicId, baseURI);
        }
      }

      final LSInput input = new LSInputImpl(systemId, publicId, baseURI);
      input.setByteStream(url.openStream());
      return input;
    }
    catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}