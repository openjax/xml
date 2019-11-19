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

import javax.xml.XMLConstants;

import org.libj.util.Paths;
import org.libj.util.function.Throwing;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

class XmlCatalogResolver implements LSResourceResolver {
  static String getPath(final String referrer, final String location) {
    return Paths.isAbsolute(location) ? location : Paths.newPath(Paths.getCanonicalParent(referrer), location);
  }

  enum W3C {
    SCHEMA_XSD(XMLConstants.W3C_XML_SCHEMA_NS_URI, "xmlschema/XMLSchema.xsd"),
    XML_XSD(XMLConstants.XML_NS_URI, "xmlschema/xml.xsd");

    private XmlEntity in;

    XmlEntity getEntity() {
      if (in != null)
        return in;

      final LSInputImpl inputSource = new LSInputImpl(null, null, namespaceURI);
      return in = new XmlEntity(Thread.currentThread().getContextClassLoader().getResource(resourceName), inputSource);
    }

    private final String namespaceURI;
    private final String resourceName;

    W3C(final String namespaceURI, final String resourceName) {
      this.namespaceURI = namespaceURI;
      this.resourceName = resourceName;
    }
  }

  private final XmlCatalog catalog;

  XmlCatalogResolver(final XmlCatalog catalog) {
    this.catalog = catalog;
  }

  @Override
  public LSInput resolveResource(final String type, final String namespaceURI, final String publicId, String systemId, final String baseURI) {
//    System.err.println("resolveResource(\"" + type + "\", \"" + namespaceURI + "\", \"" + publicId + "\", \"" + systemId + "\", \"" + baseURI + "\")");
    if (namespaceURI == null && systemId == null)
      return new LSInputImpl(publicId, systemId, baseURI);

    if (systemId == null)
      systemId = namespaceURI;
    else if (baseURI != null)
      systemId = getPath(baseURI, systemId);

    try {
      URL url = catalog.matchURI(systemId);
      if (url == null) {
        if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(namespaceURI)) {
          final XmlEntity entity = W3C.SCHEMA_XSD.getEntity();
          catalog.putEntity(namespaceURI, entity);
          return (LSInputImpl)entity.getInputSource();
        }

        if (XMLConstants.XML_NS_URI.equals(namespaceURI)) {
          final XmlEntity entity = W3C.XML_XSD.getEntity();
          catalog.putEntity(namespaceURI, entity);
          return (LSInputImpl)entity.getInputSource();
        }
      }

//      if (url == null) {
//        if ("http://www.w3.org/2001/XMLSchema.dtd".equals(systemId)) {
//          directory.put(systemId, url = Thread.currentThread().getContextClassLoader().getResource("xmlschema/XMLSchema.dtd"));
//        }
//        else if ("http://www.w3.org/2001/datatypes.dtd".equals(systemId)) {
//          directory.put(systemId, url = Thread.currentThread().getContextClassLoader().getResource("xmlschema/datatypes.dtd"));
//        }
//        else if (Paths.isAbsolute(systemId)) {
//          directory.put(systemId, url = new URL(systemId));
//        }
//      }

      final LSInputImpl input = new LSInputImpl(publicId, systemId, baseURI);
      if (url != null)
        input.setByteStream(url.openStream());

//      System.out.println(url);
      return input;
    }
    catch (final IOException e) {
      Throwing.rethrow(e);
      throw new Error("Will never get here");
    }
  }
}