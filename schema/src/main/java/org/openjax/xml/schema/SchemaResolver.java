/* Copyright (c) 2020 OpenJAX
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

package org.openjax.xml.schema;

import java.net.URL;

import javax.xml.XMLConstants;

public final class SchemaResolver {
  private static final Entry[] entries = new Entry[2];

  private static class Entry {
    private final String publicId;
    private final String systemId;
    private final String resourcePath;
    private URL url;

    private Entry(final String publicId, final String systemId, final String resourcePath) {
      this.publicId = publicId;
      this.systemId = systemId;
      this.resourcePath = resourcePath;
    }
  }

  private static Entry getEntry(final String publicId, final String systemId) {
    if (publicId == null)
      return null;

    for (int i = 0; i < entries.length; ++i) {
      final Entry entry = entries[i];
      if (publicId.equals(entry.publicId) && (systemId == null || systemId.equals(entry.systemId)))
        return entry;
    }

    return null;
  }

  static {
    entries[0] = new Entry(XMLConstants.W3C_XML_SCHEMA_NS_URI, null, "xmlschema/XMLSchema.xsd");
    entries[1] = new Entry(XMLConstants.XML_NS_URI, "http://www.w3.org/2001/xml.xsd", "xmlschema/xml.xsd");
  }

  public static URL resolve(final String publicId, final String systemId) {
    if (publicId == null)
      return null;

    final Entry entry = getEntry(publicId, systemId);
    if (entry == null)
      return null;

    if (entry.url != null)
      return entry.url;

    final URL url = SchemaResolver.class.getClassLoader().getResource(entry.resourcePath);
    if (url == null)
      throw new IllegalStateException();

    return entry.url = url;
  }

  private SchemaResolver() {
  }
}