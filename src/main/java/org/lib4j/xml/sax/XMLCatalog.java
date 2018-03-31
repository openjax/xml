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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.lib4j.net.CachedURL;

public class XMLCatalog {
  private final Map<String,SchemaLocation> schemaLocations = new LinkedHashMap<String,SchemaLocation>();

  public SchemaLocation get(final String namespaceURI) {
    return schemaLocations.get(namespaceURI);
  }

  public void put(final String key, final SchemaLocation schemaLocation) {
    schemaLocations.put(key, schemaLocation);
  }

  public boolean containsKey(final String key) {
    return schemaLocations.containsKey(key);
  }

  public boolean isEmpty() {
    return schemaLocations.isEmpty();
  }

  public Set<Map.Entry<String,SchemaLocation>> entrySet() {
    return schemaLocations.entrySet();
  }

  public String toTR9401() {
    final StringBuilder builder = new StringBuilder();
    boolean fistLine = true;
    for (final Map.Entry<String,SchemaLocation> locationEntry : schemaLocations.entrySet()) {
      for (final Map.Entry<String,CachedURL> directoryEntry : locationEntry.getValue().getDirectory().entrySet()) {
        if (fistLine)
          fistLine = false;
        else
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