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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.libj.io.ReplayReader;
import org.libj.net.URLs;
import org.xml.sax.InputSource;

/**
 * Parser for XML documents that produces {@link XmlDigest} objects.
 */
public final class XmlDigestParser {
  /**
   * Parses an XML document at the specified {@link URL}.
   *
   * @param url The {@link URL}.
   * @return A {@link XmlDigest} containing the {@link XmlCatalog} and
   *         manifest information for the XML document represented by the
   *         specified {@link URL}.
   * @throws IOException If the stream does not support
   *           {@link Reader#mark(int)}, or if some other I/O error has
   *           occurred.
   * @throws NullPointerException If the specified {@link URL} is null.
   */
  public static XmlDigest parse(final URL url) throws IOException {
    try (final Reader in = new ReplayReader(new InputStreamReader(url.openStream()))) {
      return parse(url, new LSInputImpl(null, url.toString(), null, in));
    }
  }

  private static XmlCatalog getCatalog(final URL location, final InputSource inputSource) {
    return new XmlCatalog.Tree(location, inputSource);
  }

  /**
   * Parses an XML document at the specified {@link InputSource}.
   *
   * @param url The {@link URL}.
   * @param inputSource The {@link InputSource}.
   * @return A {@link XmlDigest} containing the {@link XmlCatalog} and manifest
   *         information for the XML document represented by the specified
   *         {@link InputSource}.
   * @throws IOException If the stream does not support
   *           {@link Reader#mark(int)}, or if some other I/O error has
   *           occurred.
   * @throws NullPointerException If the specified {@link InputSource} is null.
   */
  public static XmlDigest parse(final URL url, final InputSource inputSource) throws IOException {
    inputSource.setCharacterStream(SAXUtil.getReader(inputSource));
    final XmlCatalog catalog = getCatalog(url, inputSource);
    final XmlDigest digest = new XmlDigest(inputSource, catalog);

    FastSAXParser.parse(SAXUtil.getReader(inputSource), digest);
    final Set<URL> visited = new HashSet<>();

    if (digest.getIncludes() != null)
      includes(catalog, digest.getNamespaceURIs(), visited, digest.getTargetNamespace(), digest.getIncludes());

    if (digest.getImports() != null)
      imports(catalog, digest.getNamespaceURIs(), visited, digest.getImports());

    return digest;
  }

  private static void imports(final XmlCatalog catalog, final Set<String> namespaceURIs, final Set<URL> visited, final Map<String,URL> schemaLocations) throws IOException {
    for (final Map.Entry<String,URL> entry : schemaLocations.entrySet()) {
      final URL url = entry.getValue();
      if (visited.contains(url))
        continue;

      visited.add(url);
      if (catalog.matchURI(entry.getKey()) == null) {
        try (final Reader in = new ReplayReader(new InputStreamReader(url.openStream()))) {
          final LSInputImpl inputSource = new LSInputImpl(null, url.toString(), null, in);
          final XmlCatalog nextCatalog = getCatalog(url, inputSource);
          final XmlDigest digest = new XmlDigest(inputSource, nextCatalog);

          FastSAXParser.parse(in, digest);
          catalog.putEntity(entry.getKey(), nextCatalog);

          if (digest.getImports() != null)
            namespaceURIs.addAll(digest.getImports().keySet());

          if (digest.getIncludes() != null)
            includes(nextCatalog, namespaceURIs, visited, entry.getKey(), digest.getIncludes());

          namespaceURIs.remove(entry.getKey());
          if (namespaceURIs.isEmpty())
            break;

          if (digest.getImports() != null)
            imports(nextCatalog, namespaceURIs, visited, digest.getImports());
        }
        catch (final IOException e) {
          if (!Validator.isRemoteAccessException(e) || URLs.isLocal(url))
            throw e;
        }
      }
    }
  }

  private static void includes(final XmlCatalog catalog, final Set<String> namespaceURIs, final Set<URL> visited, final String namespaceURI, final Map<String,URL> includes) throws IOException {
    for (final Map.Entry<String,URL> entry : includes.entrySet()) {
      final URL url = entry.getValue();
      if (visited.contains(url))
        continue;

      visited.add(url);
      try (final Reader in = new ReplayReader(new InputStreamReader(url.openStream()))) {
        final LSInputImpl inputSource = new LSInputImpl(null, url.toString(), null, in);
        final XmlDigest digest = new XmlDigest(inputSource, catalog);

        FastSAXParser.parse(in, digest);
        catalog.putEntity(entry.getKey(), new XmlEntity(url, inputSource));

        if (digest.getIncludes() != null)
          includes(catalog, namespaceURIs, visited, namespaceURI, digest.getIncludes());

        if (digest.getImports() != null)
          imports(catalog, namespaceURIs, visited, digest.getImports());
      }
      catch (final IOException e) {
        if (!Validator.isRemoteAccessException(e) || URLs.isLocal(url))
          throw e;
      }
    }
  }

  private XmlDigestParser() {
  }
}