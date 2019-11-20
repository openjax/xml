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
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.libj.net.URLs;
import org.xml.sax.InputSource;

/**
 * Parser for XML documents that produces {@link XmlAudit} objects.
 */
public final class XmlAuditParser {
  /**
   * Parses an XML document at the specified {@link URL}.
   *
   * @param url The {@link URL}.
   * @return A {@link XmlAudit} containing the {@link XmlCatalog} and
   *         manifest information for the XML document represented by the
   *         specified {@link URL}.
   * @throws IOException If the stream does not support
   *           {@link Reader#mark(int)}, or if some other I/O error has
   *           occurred.
   * @throws NullPointerException If the specified {@link URL} is null.
   */
  public static XmlAudit parse(final URL url) throws IOException {
    try (final CachedInputSource inputSource = new CachedInputSource(null, url.toString(), null, url.openStream())) {
      return parse(url, inputSource);
    }
  }

  /**
   * Parses an XML document at the specified {@link InputSource}.
   *
   * @param url The {@link URL}.
   * @param inputSource The {@link InputSource}.
   * @return A {@link XmlAudit} containing the {@link XmlCatalog} and manifest
   *         information for the XML document represented by the specified
   *         {@link InputSource}.
   * @throws IOException If the stream does not support
   *           {@link Reader#mark(int)}, or if some other I/O error has
   *           occurred.
   * @throws NullPointerException If the specified {@link InputSource} is null.
   */
  static XmlAudit parse(final URL url, final CachedInputSource inputSource) throws IOException {
    final XmlAuditHandler auditHandler = new XmlAuditHandler(new XmlCatalog(url, inputSource));
    FastSAXParser.parse(inputSource.getCharacterStream(), auditHandler);

    final XmlAudit xmlAudit = auditHandler.toXmlAudit();
    process(auditHandler, url.toString(), true);
    return xmlAudit;
  }

  private static boolean process(final XmlAuditHandler auditHandler, final String uri, final boolean isImport) throws IOException {
    final HashMap<String,URL> includes = auditHandler.getIncludes() == null ? null : new HashMap<>(auditHandler.getIncludes());
    final HashMap<String,URL> imports = auditHandler.getImports() == null ? null : new HashMap<>(auditHandler.getImports());

    if (imports != null && imports.size() > 0)
      auditHandler.getVisitedURIs().addAll(imports.keySet());

    if (includes != null && includes.size() > 0)
      traverse(auditHandler, includes, false);

    if (isImport) {
      auditHandler.getVisitedURIs().remove(uri);
      if (auditHandler.getVisitedURIs().isEmpty()) {
        return false;
      }
    }

    if (imports != null && imports.size() > 0)
      traverse(auditHandler, imports, true);

    return true;
  }

  private static void traverse(final XmlAuditHandler auditHandler, final Map<String,URL> schemaLocations, final boolean isImport) throws IOException {
    for (final Map.Entry<String,URL> entry : schemaLocations.entrySet()) {
      final URL location = entry.getValue();
      if (!auditHandler.getVisitedURLs().add(location))
        continue;

      final String uri = entry.getKey();
      final XmlCatalog catalog = auditHandler.getCatalog();
      if (catalog.getEntity(uri) == null) {
        try {
          final CachedInputSource inputSource = new CachedInputSource(null, location.toString(), auditHandler.getSystemId(), location.openStream());

          final XmlEntity entity;
          if (isImport) {
            final XmlCatalog nextCatalog = new XmlCatalog(location, inputSource);
            auditHandler.reset(nextCatalog);
            entity = nextCatalog;
          }
          else {
            entity = new XmlEntity(location, inputSource);
          }

          FastSAXParser.parse(inputSource.getCharacterStream(), auditHandler);
          catalog.putEntity(uri, entity);
        }
        catch (final IOException e) {
          if (!Validator.isRemoteAccessException(e) || URLs.isLocal(location))
            throw e;
        }

        if (!process(auditHandler, uri, isImport))
          break;
      }
    }
  }

  private XmlAuditParser() {
  }
}