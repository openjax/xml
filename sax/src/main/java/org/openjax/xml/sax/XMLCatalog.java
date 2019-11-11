/* Copyright (c) 2018 OpenJAX
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.libj.io.ReplayReader;
import org.xml.sax.InputSource;

public class XMLCatalog {
  /**
   * Parses an XML document at the specified {@link URL}.
   *
   * @param url The {@link URL}.
   * @return A {@link XMLCatalog} for the XML document represented by the
   *         specified {@link URL}.
   * @throws IOException If the stream does not support
   *           {@link Reader#mark(int)}, or if some other I/O error has
   *           occurred.
   */
  public static XMLCatalog parse(final URL url) throws IOException {
    try (final Reader in = new ReplayReader(new InputStreamReader(url.openStream()))) {
      return XMLManifestParser.parse(url.toString(), in, url).getCatalog();
    }
  }

  /**
   * Parses an XML document at the specified {@link InputSource}.
   *
   * @param inputSource The {@link InputSource}.
   * @return A {@link XMLCatalog} for the XML document represented by the
   *         specified {@link InputSource}.
   * @throws IOException If the stream does not support
   *           {@link Reader#mark(int)}, or if some other I/O error has
   *           occurred.
   */
  public static XMLCatalog parse(final InputSource inputSource) throws IOException {
    return XMLManifestParser.parse(inputSource.getSystemId(), SAXUtil.getReader(inputSource), new URL(inputSource.getSystemId())).getCatalog();
  }

  private final Map<String,SchemaLocation> schemaLocations = new LinkedHashMap<>();

  public void putSchemaLocation(final String key, final SchemaLocation schemaLocation) {
    schemaLocations.put(key, schemaLocation);
  }

  public SchemaLocation getSchemaLocation(final String namespaceURI) {
    return schemaLocations.get(namespaceURI);
  }

  public boolean hasSchemaLocation(final String key) {
    return schemaLocations.containsKey(key);
  }

  public boolean isEmpty() {
    return schemaLocations.isEmpty();
  }

  public String toTR9401() {
    final StringBuilder builder = new StringBuilder();
    final Iterator<Map.Entry<String,SchemaLocation>> entryIterator = schemaLocations.entrySet().iterator();
    for (int i = 0; entryIterator.hasNext();) {
      final Map.Entry<String,SchemaLocation> locationEntry = entryIterator.next();
      final Iterator<Map.Entry<String,URL>> locationIterator = locationEntry.getValue().getDirectory().entrySet().iterator();
      for (; locationIterator.hasNext(); ++i) {
        final Map.Entry<String,URL> directoryEntry = locationIterator.next();
        if (i > 0)
          builder.append('\n');

        final String line = "\"" + directoryEntry.getKey() + "\" \"" + directoryEntry.getValue() + "\"";
        builder.append(directoryEntry.getKey().equals(locationEntry.getKey()) ? "PUBLIC " : "SYSTEM ").append(line);
        builder.append("\nREWRITE_SYSTEM ").append(line);
      }
    }

    return builder.toString();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof XMLCatalog))
      return false;

    final XMLCatalog that = (XMLCatalog)obj;
    return schemaLocations.equals(that.schemaLocations);
  }

  @Override
  public int hashCode() {
    return schemaLocations.hashCode();
  }

  @Override
  public String toString() {
    return schemaLocations.toString() + "\n" + toTR9401();
  }
}