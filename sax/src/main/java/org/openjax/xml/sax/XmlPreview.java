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

package org.openjax.xml.sax;

import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;

/**
 * A {@link FastSAXHandler} that collects catalog information and metadata from an XML document.
 * <p>
 * This handler dereferences external references to imported or included documents and schemas, in order to comprise a complete
 * catalog.
 * <p>
 * One {@link XmlPreview} instance is created for <b>each</b> XML document.
 * <p>
 * One {@link XmlCatalog} instance is created for <b>all</b> XML documents.
 */
public class XmlPreview {
  private final boolean isLocal;
  private final boolean isSchema;
  private final XmlCatalog catalog;
  private final QName rootElement;
  private final String targetNamespace;
  private final Map<String,URL> imports;
  private final Map<String,URL> includes;

  public XmlPreview(final XmlCatalog catalog, final boolean isLocal, final boolean isSchema, final QName rootElement, final String targetNamespace, final Map<String,URL> imports, final Map<String,URL> includes) {
    this.catalog = catalog;
    this.isLocal = isLocal;
    this.isSchema = isSchema;
    this.rootElement = rootElement;
    this.targetNamespace = targetNamespace;
    this.imports = imports;
    this.includes = includes;
  }

  /**
   * Returns the {@link XmlCatalog} instance.
   *
   * @return The {@link XmlCatalog} instance.
   */
  public XmlCatalog getCatalog() {
    return this.catalog;
  }

  /**
   * Specifies whether the XML document represented by this {@link XmlPreview} instance only contains references that locally
   * accessible via the {@code file:} protocol.
   *
   * @return Whether the XML document represented by this {@link XmlPreview} instance only contains references that locally accessible
   *         via the {@code file:} protocol.
   */
  public boolean isLocal() {
    return isLocal;
  }

  /**
   * Specifies whether the XML document represented by this {@link XmlPreview} instance is an XML Schema Document.
   *
   * @return Whether the XML document represented by this {@link XmlPreview} instance is an XML Schema Document.
   */
  public boolean isSchema() {
    return isSchema;
  }

  /**
   * Returns the {@link QName} of the root element of the XML document represented by this {@link XmlPreview} instance.
   *
   * @return The {@link QName} of the root element of the XML document represented by this {@link XmlPreview} instance.
   */
  public QName getRootElement() {
    return rootElement;
  }

  /**
   * Returns the "targetNamespace" attribute of the XML document represented by this {@link XmlPreview} instance. This method is only
   * useful for XML Schema Documents (i.e. when {@link #isSchema()} is {@code true}).
   *
   * @return The "targetNamespace" attribute of the XML document represented by this {@link XmlPreview} instance.
   */
  public String getTargetNamespace() {
    return this.targetNamespace;
  }

  /**
   * Returns the map of namespace-to-URL entries of "import" references for the XML document represented by this {@link XmlPreview}
   * instance.
   * <ul>
   * <li>If {@link #isSchema()} is {@code true}, this method represents the {@code <xs:import/>} elements of an XML Schema
   * Document.</li>
   * <li>If {@link #isSchema()} is {@code false}, this method represents the {@code xsi:schemaLocation} attribute of an XML
   * Document.</li>
   * </ul>
   *
   * @return The map of namespace-to-URL entries of "import" references for the XML document represented by this {@link XmlPreview}
   *         instance.
   */
  public Map<String,URL> getImports() {
    return imports;
  }

  /**
   * Returns the map of {@link String}-to-{@link URL} entries of "include" references for the XML document represented by this
   * {@link XmlPreview} instance. The key and value of each entry in this map represents the same logical string, differing only in
   * class type.
   * <ul>
   * <li>If {@link #isSchema()} is {@code true}, this method represents the {@code <xs:include/>} elements of an XML Schema
   * Document.</li>
   * <li>If {@link #isSchema()} is {@code false}, this method represents the {@code xsi:noNamespaceSchemaLocation} and
   * {@code <xi:include href>} attributes in an XML Document.</li>
   * </ul>
   *
   * @return The map of {@link String}-to-{@link URL} entries of "include" references for the XML document represented by this
   *         {@link XmlPreview} instance.
   */
  public Map<String,URL> getIncludes() {
    return includes;
  }
}