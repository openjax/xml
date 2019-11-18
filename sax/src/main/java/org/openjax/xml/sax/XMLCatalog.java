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
import java.io.Serializable;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.libj.io.ReplayReader;
import org.xml.sax.InputSource;

/**
 * The {@link XMLCatalog} class represents an entity catalog as defined by
 * <a href=
 * "https://www.oasis-open.org/committees/download.php/14809/xml-catalogs.html">
 * XML Catalogs, OASIS Standard V1.1, 7 October 2005</a>.
 * <p>
 * The {@link XMLCatalog} contains namespaceURI-to-schemaLocation mappings, and
 * can be presented in
 * <a href="https://www.oasis-open.org/specs/tr9401.html">TR9401 format</a>.
 */
public class XMLCatalog implements Serializable {
  private static final long serialVersionUID = -4854713465553698524L;

  /**
   * Parses an XML document at the specified {@link URL}.
   *
   * @param url The {@link URL}.
   * @return A {@link XMLCatalog} for the XML document represented by the
   *         specified {@link URL}.
   * @throws IOException If the stream does not support
   *           {@link Reader#mark(int)}, or if some other I/O error has
   *           occurred.
   * @throws NullPointerException If the specified {@link URL} is null.
   */
  public static XMLCatalog parse(final URL url) throws IOException {
    try (final Reader in = new ReplayReader(new InputStreamReader(url.openStream()))) {
      return XMLManifestParser.parse(null, url.toString(), in, url).getCatalog();
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
   * @throws NullPointerException If the specified {@link InputSource} is null.
   */
  public static XMLCatalog parse(final InputSource inputSource) throws IOException {
    return XMLManifestParser.parse(inputSource).getCatalog();
  }

  private Map<String,SchemaLocation> schemaLocations;

  private Map<String,SchemaLocation> schemaLocations() {
    return schemaLocations == null ? schemaLocations = new LinkedHashMap<>() : schemaLocations;
  }

  /**
   * Associates the specified schema location to the namespace URI.
   *
   * @param namespaceURI The namespace URI key.
   * @param schemaLocation The schema location value.
   */
  public void putSchemaLocation(final String namespaceURI, final SchemaLocation schemaLocation) {
    schemaLocations().put(namespaceURI, schemaLocation);
  }

  /**
   * Returns the schema location associated with the specified namespace URI.
   *
   * @param namespaceURI The namespace URI.
   * @return The schema location associated with the specified namespace URI.
   */
  public SchemaLocation getSchemaLocation(final String namespaceURI) {
    return schemaLocations == null ? null : schemaLocations.get(namespaceURI);
  }

  /**
   * Returns whether this {@link XMLCatalog} contains a schema location
   * associate to the specified namespace URI.
   *
   * @param namespaceURI The namespace URI.
   * @return Whether this {@link XMLCatalog} contains a schema location
   *         associate to the specified namespace URI.
   */
  public boolean hasSchemaLocation(final String namespaceURI) {
    return schemaLocations != null && schemaLocations.containsKey(namespaceURI);
  }

  /**
   * Returns {@code true} if this map contains no namespaceURI-schemaLocation
   * mappings.
   *
   * @return {@code true} if this map contains no namespaceURI-schemaLocation
   *         mappings.
   */
  public boolean isEmpty() {
    return schemaLocations == null || schemaLocations.isEmpty();
  }

  /**
   * Returns a string representation of this {@link XMLCatalog} in
   * <a href="https://www.oasis-open.org/specs/tr9401.html">TR9401 format</a>.
   *
   * @return A string representation of this {@link XMLCatalog} in
   *         <a href="https://www.oasis-open.org/specs/tr9401.html">TR9401
   *         format</a>.
   */
  public String toTR9401() {
    if (schemaLocations == null)
      return "";

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
    return schemaLocations == null ? that.schemaLocations == null : schemaLocations.equals(that.schemaLocations);
  }

  @Override
  public int hashCode() {
    return schemaLocations == null ? 733 : schemaLocations.hashCode();
  }

  /**
   * Returns a string representation of this {@link XMLCatalog} in
   * <a href="https://www.oasis-open.org/specs/tr9401.html">TR9401 format</a>.
   *
   * @return A string representation of this {@link XMLCatalog} in
   *         <a href="https://www.oasis-open.org/specs/tr9401.html">TR9401
   *         format</a>.
   */
  @Override
  public String toString() {
    return toTR9401();
  }
}