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

import org.libj.io.ReplayReader;
import org.libj.net.URLs;
import org.xml.sax.InputSource;

/**
 * Parser for XML documents that produces {@link XMLManifest} objects.
 */
public class XMLManifestParser {
  /**
   * Parses an XML document at the specified {@link URL}.
   *
   * @param url The {@link URL}.
   * @return A {@link XMLManifest} containing the {@link XMLCatalog} and
   *         manifest information for the XML document represented by the
   *         specified {@link URL}.
   * @throws IOException If the stream does not support
   *           {@link Reader#mark(int)}, or if some other I/O error has
   *           occurred.
   * @throws NullPointerException If the specified {@link URL} is null.
   */
  public static XMLManifest parse(final URL url) throws IOException {
    try (final Reader in = new ReplayReader(new InputStreamReader(url.openStream()))) {
      return parse(null, url.toString(), in, url);
    }
  }

  /**
   * Parses an XML document at the specified {@link InputSource}.
   *
   * @param inputSource The {@link InputSource}.
   * @return A {@link XMLManifest} containing the {@link XMLCatalog} and
   *         manifest information for the XML document represented by the
   *         specified {@link InputSource}.
   * @throws IOException If the stream does not support
   *           {@link Reader#mark(int)}, or if some other I/O error has
   *           occurred.
   * @throws NullPointerException If the specified {@link InputSource} is null.
   */
  public static XMLManifest parse(final InputSource inputSource) throws IOException {
    return parse(null, inputSource.getSystemId(), SAXUtil.getReader(inputSource), new URL(inputSource.getSystemId()));
  }

  static XMLManifest parse(final String publicId, final String systemId, final Reader in, final URL url) throws IOException {
    final XMLCatalog catalog = new XMLCatalog();
    final XMLManifest manifest = new XMLManifest(publicId, systemId, in, catalog);
    FastSAXParser.parse(in, manifest);
    if (manifest.isSchema())
      catalog.putSchemaLocation(manifest.getTargetNamespace(), new SchemaLocation(manifest.getTargetNamespace(), url));

    if (manifest.getImports() != null)
      imports(catalog, manifest.getNamespaceURIs(), manifest.getImports());

    if (manifest.getIncludes() != null)
      includes(catalog, manifest.getTargetNamespace(), manifest.getIncludes());

    return manifest;
  }

  private static void imports(final XMLCatalog catalog, final Set<String> namespaceURIs, final Map<String,URL> schemaLocations) throws IOException {
    for (final Map.Entry<String,URL> schemaLocation : schemaLocations.entrySet()) {
      if (!catalog.hasSchemaLocation(schemaLocation.getKey())) {
        final URL url = schemaLocation.getValue();
        try (final Reader in = new ReplayReader(new InputStreamReader(url.openStream()))) {
          final XMLManifest manifest = new XMLManifest(null, schemaLocation.getValue().toString(), in, catalog);
          FastSAXParser.parse(in, manifest);
          catalog.putSchemaLocation(schemaLocation.getKey(), new SchemaLocation(schemaLocation.getKey(), schemaLocation.getValue()));
          if (manifest.getImports() != null)
            namespaceURIs.addAll(manifest.getImports().keySet());

          namespaceURIs.remove(schemaLocation.getKey());
          if (namespaceURIs.isEmpty())
            break;

          if (manifest.getImports() != null)
            imports(catalog, namespaceURIs, manifest.getImports());

          if (manifest.getIncludes() != null)
            includes(catalog, schemaLocation.getKey(), manifest.getIncludes());
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
        final XMLManifest manifest = new XMLManifest(null, url.toString(), in, catalog);
        FastSAXParser.parse(in, manifest);
        catalog.getSchemaLocation(namespaceURI).getDirectory().put(entry.getKey(), url);
        if (manifest.getIncludes() != null)
          includes(catalog, namespaceURI, manifest.getIncludes());
      }
      catch (final IOException e) {
        if (!Validator.isRemoteAccessException(e) || URLs.isLocal(url))
          throw e;
      }
    }
  }
}