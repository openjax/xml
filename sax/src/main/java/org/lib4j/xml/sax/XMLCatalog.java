/* Copyright (c) 2018 lib4j
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

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class XMLCatalog {
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
    for (final Map.Entry<String,SchemaLocation> locationEntry : schemaLocations.entrySet()) {
      final Iterator<Map.Entry<String,URL>> iterator = locationEntry.getValue().getDirectory().entrySet().iterator();
      for (int i = 0; iterator.hasNext(); i++) {
        final Map.Entry<String,URL> directoryEntry = iterator.next();
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