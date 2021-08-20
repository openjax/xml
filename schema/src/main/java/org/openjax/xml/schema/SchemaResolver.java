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

/**
 * Utility class that resolves the schema location of schemas identified by the
 * following {@code publicId} and {@code systemId}:
 *
 * <pre>
 * http://www.w3.org/2001/XMLSchema
 * http://www.w3.org/XML/1998/namespace http://www.w3.org/2001/xml.xsd
 * </pre>
 */
public final class SchemaResolver {
  private static final Schema[] schemas = new Schema[] {
    new Schema(XMLConstants.W3C_XML_SCHEMA_NS_URI, null, "xmlschema/XMLSchema.xsd"),
    new Schema(XMLConstants.XML_NS_URI, "http://www.w3.org/2001/xml.xsd", "xmlschema/xml.xsd")
  };

  private static class Schema {
    private final String publicId;
    private final String systemId;
    private final String resourcePath;
    private URL url;

    private Schema(final String publicId, final String systemId, final String resourcePath) {
      this.publicId = publicId;
      this.systemId = systemId;
      this.resourcePath = resourcePath;
    }

    @Override
    public String toString() {
      return "{" + publicId + "}" + systemId;
    }
  }

  /**
   * Returns the schema location {@link URL} for the specified {@code publicId}
   * and {@code systemId}, or {@code null} if the provided {@code publicId} and
   * {@code systemId} do not match one of:
   *
   * <pre>
   * http://www.w3.org/2001/XMLSchema
   * http://www.w3.org/XML/1998/namespace http://www.w3.org/2001/xml.xsd
   * </pre>
   *
   * @param publicId The Public ID.
   * @param systemId The System ID.
   * @return The schema location {@link URL} for the specified {@code publicId}
   *         and {@code systemId}.
   */
  public static URL resolve(final String publicId, final String systemId) {
    if (publicId == null)
      return null;

    for (final Schema schema : schemas) {
      if (publicId.equals(schema.publicId) && (systemId == null || systemId.equals(schema.systemId))) {
        if (schema.url != null)
          return schema.url;

        final URL url = Thread.currentThread().getContextClassLoader().getResource(schema.resourcePath);
        if (url == null)
          throw new IllegalStateException("Unable to find location of schema: " + schema);

        return schema.url = url;
      }
    }

    return null;
  }

  private SchemaResolver() {
  }
}