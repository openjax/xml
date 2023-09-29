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

import org.libj.util.StringPaths;
import org.libj.util.function.Throwing;
import org.openjax.xml.schema.SchemaResolver;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

class XmlCatalogResolver implements LSResourceResolver {
  static String getPath(final String referrer, final String location) {
    return StringPaths.isAbsolute(location) ? location : StringPaths.newPath(StringPaths.getCanonicalParent(referrer), location);
  }

  enum W3C {
    SCHEMA_XSD(XMLConstants.W3C_XML_SCHEMA_NS_URI),
    XML_XSD(XMLConstants.XML_NS_URI);

    XmlEntity getEntity() throws IOException {
      final URL resource = SchemaResolver.resolve(namespaceURI, null);
      if (resource == null)
        throw new IllegalStateException("Unable to find resource for namespace=\"" + namespaceURI + "\"");

      // FIXME: Why can't I cache this?
      return new XmlEntity(resource, new CachedInputSource(null, namespaceURI, null, resource.openConnection()));
    }

    private final String namespaceURI;

    W3C(final String namespaceURI) {
      this.namespaceURI = namespaceURI;
    }
  }

  private final XmlCatalog catalog;

  XmlCatalogResolver(final XmlCatalog catalog) {
    this.catalog = catalog;
  }

  @Override
  public LSInput resolveResource(final String type, final String namespaceURI, final String publicId, String systemId, final String baseURI) {
    if (namespaceURI == null && systemId == null)
      return null;

    if (systemId == null)
      systemId = namespaceURI;
    else if (baseURI != null)
      systemId = getPath(baseURI, systemId);

    try {
      XmlEntity entity = catalog.getEntity(systemId);
      if (entity == null) {
        if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(namespaceURI)) {
          catalog.putEntity(namespaceURI, entity = W3C.SCHEMA_XSD.getEntity());
        }
        else if (XMLConstants.XML_NS_URI.equals(namespaceURI)) {
          catalog.putEntity(namespaceURI, entity = W3C.XML_XSD.getEntity());
        }
      }

      if (entity == null) {
        try {
          return new CachedInputSource(publicId, systemId, baseURI, new URL(systemId).openConnection());
        }
        catch (final IOException e) {
          return null;
        }
      }

      final CachedInputSource inputSource = entity.getInputSource();
      inputSource.getCharacterStream().close();
      inputSource.setBaseURI(baseURI);
      return inputSource;
    }
    catch (final IOException e) {
      Throwing.rethrow(e);
      throw new Error("Will never get here");
    }
  }
}