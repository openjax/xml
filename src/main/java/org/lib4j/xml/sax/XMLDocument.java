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

import javax.xml.namespace.QName;

import org.lib4j.net.CachedURL;

public class XMLDocument {
  private final CachedURL cachedUrl;
  private final XMLCatalog catalog;
  private final boolean isXSD;
  private final QName rootElement;
  private final URL schemaLocation;
  private final boolean referencesOnlyLocal;

  public XMLDocument(final CachedURL cachedUrl, final XMLCatalog catalog, final QName rootElement, final boolean isXSD, final boolean referencesOnlyLocal) {
    this.cachedUrl = cachedUrl;
    this.catalog = catalog;
    this.rootElement = rootElement;
    final SchemaLocation schemaLocation = catalog.getSchemaLocation(rootElement.getNamespaceURI());
    this.schemaLocation = schemaLocation == null ? null : schemaLocation.getDirectory().get(rootElement.getNamespaceURI()).toURL();
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

  public XMLCatalog getCatalog() {
    return catalog;
  }

  public boolean isXsd() {
    return isXSD;
  }

  public boolean referencesOnlyLocal() {
    return referencesOnlyLocal;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof XMLDocument))
      return false;

    final XMLDocument that = (XMLDocument)obj;
    return isXSD == that.isXSD && referencesOnlyLocal == that.referencesOnlyLocal && (cachedUrl != null ? cachedUrl.equals(that.cachedUrl) : that.cachedUrl == null) && (catalog != null ? catalog.equals(that.catalog) : that.catalog == null) && (rootElement != null ? rootElement.equals(that.rootElement) : that.rootElement == null);
  }

  @Override
  public int hashCode() {
    int hashCode = 17;
    hashCode = 31 * hashCode + (isXSD ? 1 : 0);
    hashCode = 31 * hashCode + (referencesOnlyLocal ? 1 : 0);
    hashCode = 31 * hashCode + cachedUrl.hashCode();
    hashCode = 31 * hashCode + catalog.hashCode();
    hashCode = 31 * hashCode + rootElement.hashCode();
    return hashCode;
  }

  @Override
  public String toString() {
    return "URL: " + cachedUrl.toExternalForm() + "\nIsXSD: " + isXSD + "\nReferencesOnlyLocal: " + referencesOnlyLocal + "\nCatalog:\n" + catalog.toTR9401();
  }
}