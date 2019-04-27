/* Copyright (c) 2016 OpenJAX
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

package org.openjax.ext.xml.sax;

import java.net.URL;

import javax.xml.namespace.QName;

public class XMLDocument {
  private final URL url;
  private final XMLCatalog catalog;
  private final boolean isXSD;
  private final QName rootElement;
  private final URL schemaLocation;
  private final boolean referencesLocalOnly;

  public XMLDocument(final URL url, final XMLCatalog catalog, final QName rootElement, final boolean isXSD, final boolean referencesLocalOnly) {
    this.url = url;
    this.catalog = catalog;
    this.rootElement = rootElement;

    final SchemaLocation schemaLocation = catalog.getSchemaLocation(rootElement.getNamespaceURI());
    this.schemaLocation = schemaLocation == null ? null : schemaLocation.getDirectory().get(rootElement.getNamespaceURI());
    this.isXSD = isXSD;
    this.referencesLocalOnly = referencesLocalOnly;
  }

  public URL getURL() {
    return this.url;
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

  public boolean referencesLocalOnly() {
    return referencesLocalOnly;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof XMLDocument))
      return false;

    final XMLDocument that = (XMLDocument)obj;
    return isXSD == that.isXSD && referencesLocalOnly == that.referencesLocalOnly && (url != null ? url.equals(that.url) : that.url == null) && (catalog != null ? catalog.equals(that.catalog) : that.catalog == null) && (rootElement != null ? rootElement.equals(that.rootElement) : that.rootElement == null);
  }

  @Override
  public int hashCode() {
    int hashCode = 17;
    hashCode = 31 * hashCode + (isXSD ? 1 : 0);
    hashCode = 31 * hashCode + (referencesLocalOnly ? 1 : 0);
    hashCode = 31 * hashCode + url.hashCode();
    hashCode = 31 * hashCode + catalog.hashCode();
    hashCode = 31 * hashCode + rootElement.hashCode();
    return hashCode;
  }

  @Override
  public String toString() {
    return "URL: " + url.toExternalForm() + "\nIsXSD: " + isXSD + "\nReferencesOnlyLocal: " + referencesLocalOnly + "\nCatalog:\n" + catalog.toTR9401();
  }
}