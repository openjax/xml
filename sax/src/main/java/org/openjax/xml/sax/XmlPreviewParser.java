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

import static org.libj.lang.Assertions.*;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.libj.net.URLs;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

/**
 * Parser for XML documents that produces {@link XmlPreview} objects.
 */
public final class XmlPreviewParser {
  /**
   * Parses an XML document at the specified {@link URL}.
   *
   * @param url The {@link URL}.
   * @return A {@link XmlPreview} containing the {@link XmlCatalog} and manifest information for the XML document represented by the
   *         specified {@link URL}.
   * @throws IOException If the stream does not support {@link Reader#mark(int)}, or if some other I/O error has occurred.
   * @throws SAXParseException If provided XML document cannot be parsed.
   * @throws IllegalArgumentException If {@code url} is null.
   */
  public static XmlPreview parse(final URL url) throws IOException, SAXParseException {
    try (final CachedInputSource inputSource = new CachedInputSource(null, assertNotNull(url).toString(), null, url.openStream())) {
      return parse(url, inputSource);
    }
  }

  /**
   * Parses an XML document at the specified {@link InputSource}.
   *
   * @param url The {@link URL}.
   * @param inputSource The {@link InputSource}.
   * @return A {@link XmlPreview} containing the {@link XmlCatalog} and manifest information for the XML document represented by the
   *         specified {@link InputSource}.
   * @throws IOException If the stream does not support {@link Reader#mark(int)}, or if some other I/O error has occurred.
   * @throws SAXParseException If provided XML document cannot be parsed.
   * @throws IllegalArgumentException If {@code inputSource} is null.
   */
  static XmlPreview parse(final URL url, final CachedInputSource inputSource) throws IOException, SAXParseException {
    final XmlPreviewHandler previewHandler = new XmlPreviewHandler(new XmlCatalog(url, inputSource));
    FastSAXParser.parse(inputSource.getCharacterStream(), previewHandler);

    final XmlPreview preview = previewHandler.toXmlPreview();
    process(previewHandler, url.toString(), true);
    return preview;
  }

  private static boolean process(final XmlPreviewHandler previewHandler, final String uri, final boolean isImport) throws IOException, SAXParseException {
    final HashMap<String,URL> includes = previewHandler.getIncludes() == null ? null : new HashMap<>(previewHandler.getIncludes());
    final HashMap<String,URL> imports = previewHandler.getImports() == null ? null : new HashMap<>(previewHandler.getImports());

    if (imports != null && imports.size() > 0)
      previewHandler.getVisitedURIs().addAll(imports.keySet());

    if (includes != null && includes.size() > 0)
      traverse(previewHandler, includes, false);

    if (isImport) {
      previewHandler.getVisitedURIs().remove(uri);
      if (previewHandler.getVisitedURIs().isEmpty()) {
        return false;
      }
    }

    if (imports != null && imports.size() > 0)
      traverse(previewHandler, imports, true);

    return true;
  }

  private static void traverse(final XmlPreviewHandler previewHandler, final Map<String,URL> schemaLocations, final boolean isImport) throws IOException, SAXParseException {
    if (schemaLocations.size() > 0) {
      for (final Map.Entry<String,URL> entry : schemaLocations.entrySet()) { // [S]
        final URL location = entry.getValue();
        if (!previewHandler.getVisitedURLs().add(location))
          continue;

        final String uri = entry.getKey();
        final XmlCatalog catalog = previewHandler.getCatalog();
        if (catalog.getEntity(uri) == null) {
          try {
            final CachedInputSource inputSource = new CachedInputSource(null, location.toString(), previewHandler.getSystemId(), location.openStream());

            final XmlEntity entity;
            if (isImport) {
              final XmlCatalog nextCatalog = new XmlCatalog(location, inputSource);
              previewHandler.reset(nextCatalog);
              entity = nextCatalog;
            }
            else {
              entity = new XmlEntity(location, inputSource);
            }

            FastSAXParser.parse(inputSource.getCharacterStream(), previewHandler);
            catalog.putEntity(uri, entity);
          }
          catch (final IOException e) {
            if (!Validator.isRemoteAccessException(e) || URLs.isLocal(location))
              throw e;
          }

          if (!process(previewHandler, uri, isImport))
            break;
        }
      }
    }
  }

  private XmlPreviewParser() {
  }
}