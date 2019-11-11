/* Copyright (c) 2019 OpenJAX
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
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.stream.StreamSource;

import org.libj.io.ReplayReader;
import org.libj.net.URLs;
import org.xml.sax.InputSource;

/**
 * Parser for XML documents that produces {@link XMLCatalog} and
 * {@link XMLCatalogHandler} objects.
 */
public class XMLCatalogParser {
  /**
   * Parses an XML document at the specified {@link URL}.
   *
   * @param url The {@link URL}.
   * @return A {@link XMLCatalogHandler} containing the {@link XMLCatalog} and
   *         metadata information for the XML document represented by the
   *         specified {@link URL}.
   * @throws IOException If the stream does not support
   *           {@link Reader#mark(int)}, or if some other I/O error has
   *           occurred.
   */
  public static XMLCatalogHandler parse(final URL url) throws IOException {
    return parse(SAXUtil.getInputSource(url), url);
  }

  /**
   * Parses an XML document at the specified {@link StreamSource}.
   *
   * @param streamSource The {@link StreamSource}.
   * @return A {@link XMLCatalogHandler} containing the {@link XMLCatalog} and
   *         metadata information for the XML document represented by the
   *         specified {@link StreamSource}.
   * @throws IOException If the stream does not support
   *           {@link Reader#mark(int)}, or if some other I/O error has
   *           occurred.
   */
  public static XMLCatalogHandler parse(final StreamSource streamSource) throws IOException {
    return parse(SAXUtil.getInputSource(streamSource));
  }

  /**
   * Parses an XML document at the specified {@link InputSource}.
   *
   * @param inputSource The {@link InputSource}.
   * @return A {@link XMLCatalogHandler} containing the {@link XMLCatalog} and
   *         metadata information for the XML document represented by the
   *         specified {@link InputSource}.
   * @throws IOException If the stream does not support
   *           {@link Reader#mark(int)}, or if some other I/O error has
   *           occurred.
   */
  public static XMLCatalogHandler parse(final InputSource inputSource) throws IOException {
    return parse(inputSource, new URL(inputSource.getSystemId()));
  }

  static XMLCatalogHandler parse(final InputSource inputSource, final URL url) throws IOException {
    final XMLCatalog catalog = new XMLCatalog();
    final XMLCatalogHandler handler = new XMLCatalogHandler(inputSource.getSystemId(), inputSource.getCharacterStream(), catalog);
    FastSAXParser.parse(inputSource.getCharacterStream(), handler);
    if (handler.isSchema())
      catalog.putSchemaLocation(handler.getTargetNamespace(), new SchemaLocation(handler.getTargetNamespace(), url));

    if (handler.getImports() != null)
      imports(catalog, handler.getNamespaceURIs(), handler.getImports());

    if (handler.getIncludes() != null)
      includes(catalog, handler.getTargetNamespace(), handler.getIncludes());

    return handler;
  }

  private static void imports(final XMLCatalog catalog, final Set<String> namespaceURIs, final Map<String,URL> schemaLocations) throws IOException {
    for (final Map.Entry<String,URL> schemaLocation : schemaLocations.entrySet()) {
      if (!catalog.hasSchemaLocation(schemaLocation.getKey())) {
        final URL url = schemaLocation.getValue();
        try (final Reader in = new ReplayReader(new InputStreamReader(url.openStream()))) {
          final XMLCatalogHandler handler = new XMLCatalogHandler(schemaLocation.getValue().toString(), in, catalog);
          FastSAXParser.parse(in, handler);
          catalog.putSchemaLocation(schemaLocation.getKey(), new SchemaLocation(schemaLocation.getKey(), schemaLocation.getValue()));
          if (handler.getImports() != null)
            namespaceURIs.addAll(handler.getImports().keySet());

          namespaceURIs.remove(schemaLocation.getKey());
          if (namespaceURIs.isEmpty())
            break;

          if (handler.getImports() != null)
            imports(catalog, namespaceURIs, handler.getImports());

          if (handler.getIncludes() != null)
            includes(catalog, schemaLocation.getKey(), handler.getIncludes());
        }
        catch (final IOException e) {
          if (!Validator.isRemoteAccessException(e) || URLs.isLocal(url))
            throw e;
        }
      }
    }
  }

  private static void includes(final XMLCatalog catalog, final String namespaceURI, final Map<String,URL> includes) throws IOException {
    for (final Map.Entry<String,URL> entry : includes.entrySet()) {
      final URL url = entry.getValue();
      try (final Reader in = new ReplayReader(new InputStreamReader(url.openStream()))) {
        final XMLCatalogHandler handler = new XMLCatalogHandler(url.toString(), in, catalog);
        FastSAXParser.parse(in, handler);
        catalog.getSchemaLocation(namespaceURI).getDirectory().put(entry.getKey(), url);
        if (handler.getIncludes() != null)
          includes(catalog, namespaceURI, handler.getIncludes());
      }
      catch (final IOException e) {
        if (!Validator.isRemoteAccessException(e) || URLs.isLocal(url))
          throw e;
      }
    }
  }
}