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

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.libj.net.URLs;
import org.libj.util.StringPaths;
import org.openjax.xml.schema.SchemaResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 * A {@link FastSAXHandler} that collects catalog information and metadata from an XML document.
 * <p>
 * This handler dereferences external references to imported or included documents and schemas, in order to comprise a complete
 * catalog.
 * <p>
 * One {@link XmlPreviewHandler} instance is created for <b>all</b> XML documents.
 * <p>
 * One {@link XmlCatalog} instance is created for <b>all</b> XML documents.
 */
class XmlPreviewHandler extends FastSAXHandler {
  private static final Logger logger = LoggerFactory.getLogger(XmlPreviewHandler.class);

  /**
   * Returns a string encoding of the specified {@code attributes}. The encoding is of the form:
   *
   * <pre>
   * {@code
   * name = "value"
   * }
   * </pre>
   *
   * @param attributes The map of attributes.
   * @return A string encoding of the specified {@code attributes}.
   */
  private static String toString(final Map<QName,String> attributes) {
    if (attributes == null)
      return null;

    if (attributes.size() == 0)
      return "";

    final StringBuilder b = new StringBuilder();
    final Iterator<Map.Entry<QName,String>> iterator = attributes.entrySet().iterator();
    for (int i = 0; iterator.hasNext(); ++i) { // [I]
      if (i > 0)
        b.append(' ');

      final Map.Entry<QName,String> entry = iterator.next();
      final QName name = entry.getKey();
      if (name.getPrefix().length() > 0)
        b.append(name.getPrefix()).append(':');

      b.append(name.getLocalPart());
      b.append("=\"").append(entry.getValue()).append('"');
    }

    return b.toString();
  }

  private String systemId;
  private XmlCatalog catalog;
  private final HashSet<String> visitedURIs = new HashSet<>();
  private final HashSet<URL> visitedURLs = new HashSet<>();
  private final LinkedHashMap<String,URL> absoluteIncludes = new LinkedHashMap<>();
  private boolean isLocal = true;
  private String targetNamespace;

  /**
   * Creates a new {@link XmlPreviewHandler} to be initialized with the specified {@link XmlCatalog}.
   *
   * @param catalog The {@link XmlCatalog}.
   * @throws NullPointerException If the specified {@link XmlCatalog} is null.
   * @throws IllegalArgumentException If the {@link InputSource} in the specified {@link XmlCatalog} does not have a byte stream or
   *           character stream.
   */
  XmlPreviewHandler(final XmlCatalog catalog) {
    init(catalog);
  }

  /**
   * Initializes the specified {@link XmlCatalog} in this {@link XmlPreviewHandler}.
   *
   * @param catalog The {@link XmlCatalog}.
   * @throws NullPointerException If the specified {@link XmlCatalog} is null.
   * @throws IllegalArgumentException If the {@link InputSource} in the specified {@link XmlCatalog} does not have a byte stream or
   *           character stream.
   */
  private void init(final XmlCatalog catalog) {
    super.reader = catalog.getInputSource().getCharacterStream();
    this.systemId = catalog.getInputSource().getSystemId();
    this.catalog = catalog;
  }

  /**
   * Resets the local variables in this handler and initializes it for the specified {@link XmlCatalog}.
   *
   * @param catalog The {@link XmlCatalog}.
   * @see #reset()
   */
  void reset(final XmlCatalog catalog) {
    reset();
    init(catalog);
  }

  /**
   * Returns the system identifier (URI reference).
   *
   * @return The system identifier (URI reference).
   * @see <a href="http://www.ietf.org/rfc/rfc2396.txt">IETF RFC 2396</a>
   */
  String getSystemId() {
    return systemId;
  }

  /**
   * Returns the catalog instance.
   *
   * @return The catalog instance.
   */
  XmlCatalog getCatalog() {
    return catalog;
  }

  /**
   * Returns a set of the URIs visited throughout the lifecycle of this {@link XmlPreviewHandler} instance.
   *
   * @return A set of the URIs visited throughout the lifecycle of this {@link XmlPreviewHandler} instance.
   */
  Set<String> getVisitedURIs() {
    return visitedURIs;
  }

  /**
   * Returns a set of the URLs visited throughout the lifecycle of this {@link XmlPreviewHandler} instance.
   *
   * @return A set of the URLs visited throughout the lifecycle of this {@link XmlPreviewHandler} instance.
   */
  Set<URL> getVisitedURLs() {
    return visitedURLs;
  }

  private boolean isSchema;

  /**
   * Specifies whether the XML document represented by the {@link XmlCatalog} in this {@link XmlPreviewHandler} instance is an XML
   * Schema Document.
   *
   * @return Whether the XML document represented by the {@link XmlCatalog} in this {@link XmlPreviewHandler} instance is an XML
   *         Schema Document.
   * @throws IllegalStateException If this method is called before the XML document represented by the {@link XmlCatalog} in this
   *           {@link XmlPreviewHandler} instance is parsed.
   */
  boolean isSchema() {
    if (rootElement == null)
      throw new IllegalStateException("Parsing has not been performed");

    return isSchema;
  }

  private QName rootElement;

  /**
   * Returns the {@link QName} of the root element of the XML document represented by the {@link XmlCatalog} in this
   * {@link XmlPreviewHandler} instance.
   *
   * @return The {@link QName} of the root element of the XML document represented by the {@link XmlCatalog} in this
   *         {@link XmlPreviewHandler} instance.
   * @throws IllegalStateException If this method is called before the XML document represented by the {@link XmlCatalog} in this
   *           {@link XmlPreviewHandler} instance is parsed.
   */
  QName getRootElement() {
    if (rootElement == null)
      throw new IllegalStateException("Parsing has not been performed");

    return rootElement;
  }

  /**
   * Returns the "targetNamespace" attribute of the XML document represented by the {@link XmlCatalog} in this
   * {@link XmlPreviewHandler} instance. This method is only useful for XML Schema Documents (i.e. when {@link #isSchema()} is
   * {@code true}).
   *
   * @return The "targetNamespace" attribute of the XML document represented by the {@link XmlCatalog} in this
   *         {@link XmlPreviewHandler} instance.
   */
  String getTargetNamespace() {
    return targetNamespace;
  }

  private Map<String,URL> imports;

  private Map<String,URL> imports() {
    return imports == null ? imports = new LinkedHashMap<String,URL>() {
      @Override
      public URL put(final String key, final URL value) {
        final URL schemaUrl = SchemaResolver.resolve(key, value.toString());
        return schemaUrl != null ? super.put(key, schemaUrl) : super.put(key, value);
      }
    } : imports;
  }

  /**
   * Returns the map of namespace-to-URL entries of "import" references for the XML document represented by the {@link XmlCatalog} in
   * this {@link XmlPreviewHandler} instance.
   * <ul>
   * <li>If {@link #isSchema()} is {@code true}, this method represents the {@code <xs:import/>} elements of an XML Schema
   * Document.</li>
   * <li>If {@link #isSchema()} is {@code false}, this method represents the {@code xsi:schemaLocation} attribute of an XML
   * Document.</li>
   * </ul>
   *
   * @return The map of namespace-to-URL entries of "import" references for the XML document represented by the {@link XmlCatalog} in
   *         this {@link XmlPreviewHandler} instance.
   */
  Map<String,URL> getImports() {
    return imports;
  }

  private void addImport(final String namespace, final String location) {
    final String path = XmlCatalogResolver.getPath(systemId, location);
    isLocal &= StringPaths.isAbsoluteLocal(path);
    if (!imports().containsKey(namespace))
      imports.put(namespace, StringPaths.getProtocol(path) == null ? URLs.create("file:" + path) : URLs.create(path));
  }

  private Map<String,URL> includes;

  private Map<String,URL> includes() {
    return includes == null ? includes = new LinkedHashMap<>() : includes;
  }

  /**
   * Returns the map of {@link String}-to-{@link URL} entries of "include" references for the XML document represented by the
   * {@link XmlCatalog} in this {@link XmlPreviewHandler} instance. The key and value of each entry in this map represents the same
   * logical string, differing only in class type.
   * <ul>
   * <li>If {@link #isSchema()} is {@code true}, this method represents the {@code <xs:include/>} elements of an XML Schema
   * Document.</li>
   * <li>If {@link #isSchema()} is {@code false}, this method represents the {@code xsi:noNamespaceSchemaLocation} and
   * {@code <xi:include href>} attributes in an XML Document.</li>
   * </ul>
   *
   * @return The map of {@link String}-to-{@link URL} entries of "include" references for the XML document represented by the
   *         {@link XmlCatalog} in this {@link XmlPreviewHandler} instance.
   */
  Map<String,URL> getIncludes() {
    return includes;
  }

  private void addInclude(final String schemaLocation) {
    final String path = XmlCatalogResolver.getPath(systemId, schemaLocation);
    isLocal &= StringPaths.isAbsoluteLocal(path);
    URL url = absoluteIncludes.get(path);
    if (url == null)
      absoluteIncludes.put(path, url = URLs.create(path));

    includes().put(schemaLocation, url);
  }

  @Override
  public boolean startElement(final QName name, final Map<QName,String> attributes) throws IOException {
    final String localPart = name.getLocalPart();
    final String namespaceURI = name.getNamespaceURI();
    if (logger.isTraceEnabled()) {
      final String attrs = toString(attributes);
      logger.trace("<" + localPart + " xmlns=\"" + namespaceURI + "\"" + (attrs != null ? " " + attrs + ">" : ">"));
    }

    if (rootElement == null) {
      rootElement = name;
      isSchema = XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(namespaceURI) && "schema".equals(rootElement.getLocalPart());
    }

    if (isSchema) {
      if ("schema".equals(localPart)) {
        if (attributes.size() > 0) {
          for (final Map.Entry<QName,String> entry : attributes.entrySet()) { // [S]
            final String attributeName = entry.getKey().getLocalPart();
            if ("targetNamespace".equals(attributeName)) {
              targetNamespace = entry.getValue();
              break;
            }
          }
        }
      }
      else if ("import".equals(localPart)) {
        String namespace = null;
        String schemaLocation = null;
        if (attributes.size() > 0) {
          for (final Map.Entry<QName,String> entry : attributes.entrySet()) { // [S]
            final String attributeName = entry.getKey().getLocalPart();
            if ("namespace".equals(attributeName)) {
              namespace = entry.getValue();
              if (schemaLocation != null)
                break;
            }
            else if ("schemaLocation".equals(attributeName)) {
              schemaLocation = entry.getValue();
              if (namespace != null)
                break;
            }
          }
        }

        final String path = XmlCatalogResolver.getPath(systemId, schemaLocation);
        isLocal &= StringPaths.isAbsoluteLocal(path);
        visitedURIs.add(namespace);
        if (!imports().containsKey(namespace))
          imports.put(namespace, URLs.create(path));
      }
      else if ("include".equals(localPart)) {
        if (attributes.size() > 0) {
          for (final Map.Entry<QName,String> entry : attributes.entrySet()) { // [S]
            if ("schemaLocation".equals(entry.getKey().getLocalPart())) {
              addInclude(entry.getValue());
            }
          }
        }
      }
      else if (!"schema".equals(localPart) && !"annotation".equals(localPart) && !"redefine".equals(localPart)) {
        return false;
      }
    }
    else {
      if ("include".equals(localPart)) {
        if (attributes.size() > 0) {
          for (final Map.Entry<QName,String> entry : attributes.entrySet()) { // [S]
            final QName key = entry.getKey();
            if ("http://www.w3.org/2001/XInclude".equals(key.getNamespaceURI()) && "href".equals(key.getLocalPart())) {
              addInclude(entry.getValue());
            }
          }
        }
      }
      else {
        if (attributes != null && attributes.size() > 0) {
          for (final Map.Entry<QName,String> entry : attributes.entrySet()) { // [S]
            final QName key = entry.getKey();
            final String keyNamespace = key.getNamespaceURI();
            if (XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI.equals(keyNamespace)) {
              final String keyLocalPart = key.getLocalPart();
              if ("noNamespaceSchemaLocation".equals(keyLocalPart)) {
                addInclude(entry.getValue());
              }
              else if ("schemaLocation".equals(keyLocalPart)) {
                final String value = entry.getValue();
                final StringTokenizer tokenizer = new StringTokenizer(value);
                while (tokenizer.hasMoreTokens()) {
                  final String namespace = tokenizer.nextToken();
                  if (tokenizer.hasMoreTokens()) {
                    final String location = tokenizer.nextToken();
                    addImport(namespace, location);
                  }
                }
              }
            }
            else if (keyNamespace.length() != 0) {
              visitedURIs.add(keyNamespace);
            }
          }
        }
      }

      if (!XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI.equals(namespaceURI) && namespaceURI.length() != 0)
        visitedURIs.add(namespaceURI);
    }

    return true;
  }

  /**
   * Resets the local variables in this handler, so it can be used in another parsing invocation.
   */
  @Override
  public void reset() {
    super.reset();
    this.isSchema = false;
    this.rootElement = null;
    this.targetNamespace = null;
    this.isLocal = true;
    if (includes != null)
      includes.clear();

    if (imports != null)
      imports.clear();
  }

  /**
   * Returns an {@link XmlPreview} representation of this {@link XmlPreviewHandler}.
   *
   * @return An {@link XmlPreview} representation of this {@link XmlPreviewHandler}.
   */
  public XmlPreview toXmlPreview() {
    return new XmlPreview(catalog, isLocal, isSchema, rootElement, targetNamespace, imports == null ? null : new HashMap<>(imports), includes == null ? null : new HashMap<>(includes));
  }
}