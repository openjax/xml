/* Copyright (c) 2016 lib4j
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
import java.util.Map;

import javax.xml.namespace.QName;

import org.lib4j.net.CachedURL;

public class XMLDocument {
  private final CachedURL cachedUrl;
  private final Map<String,SchemaLocation> schemaReferences;
  private final boolean isXSD;
  private final QName rootElement;
  private final URL schemaLocation;
  private final boolean referencesOnlyLocal;

  public XMLDocument(final CachedURL cachedUrl, final Map<String,SchemaLocation> schemaReferences, final QName rootElement, final boolean isXSD, final boolean referencesOnlyLocal) {
    this.cachedUrl = cachedUrl;
    this.schemaReferences = schemaReferences;
    this.rootElement = rootElement;
    final SchemaLocation schemaLocation = schemaReferences.get(rootElement.getNamespaceURI());
    this.schemaLocation = schemaLocation == null ? null : schemaLocation.getLocation().get(rootElement.getNamespaceURI()).toURL();
    this.isXSD = isXSD;
    this.referencesOnlyLocal = referencesOnlyLocal;
  }

  public CachedURL getURL() {
    return this.cachedUrl;
  }

  public QName getRootElement() {
    return this.rootElement;
  }

  public URL getSchemaLocation() {
    return schemaLocation;
  }

  public Map<String,SchemaLocation> getSchemaReferences() {
    return schemaReferences;
  }

  public boolean isXSD() {
    return isXSD;
  }

  public boolean referencesOnlyLocal() {
    return referencesOnlyLocal;
  }
}