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
import java.io.Reader;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.libj.net.URLs;
import org.libj.util.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link FastSAXHandler} that collects catalog information and metadata from
 * an XML document.
 * <p>
 * This handler dereferences external references to imported or included
 * documents and schemas, in order to comprise a complete catalog.
 * <p>
 * One {@link XMLManifest} instance is created for <b>each</b> XML
 * document.
 * <p>
 * One {@link XMLCatalog} instance is created for <b>all</b> XML documents.
 */
public class XMLManifest extends FastSAXHandler {
  private static final Logger logger = LoggerFactory.getLogger(XMLManifest.class);

  private final String publicId;
  private final String systemId;
  private final XMLCatalog catalog;
  private final Set<String> namespaceURIs = new HashSet<>();
  private final Map<String,URL> absoluteIncludes = new LinkedHashMap<>();
  private boolean referencesOnlyLocal = true;
  private String targetNamespace;

  /**
   * Creates a new {@link XMLManifest} with the specified parameters.
   *
   * @param publicId The public identifier for this input source.
   * @param systemId The system identifier, a URI reference
   *          [<a href='http://www.ietf.org/rfc/rfc2396.txt'>IETF RFC 2396</a>],
   *          for this input source.
   * @param in The input stream for XML data.
   * @param catalog The {@link XMLCatalog}.
   * @throws IOException If an I/O error has occurred.
   */
  protected XMLManifest(final String publicId, final String systemId, final Reader in, final XMLCatalog catalog) throws IOException {
    super(in);
    this.publicId = publicId;
    this.systemId = systemId;
    this.catalog = catalog;
  }

  public String getPublicId() {
    return this.publicId;
  }

  public String getSystemId() {
    return this.systemId;
  }

  /**
   * Returns the catalog instance.
   *
   * @return The catalog instance.
   */
  public XMLCatalog getCatalog() {
    return this.catalog;
  }

  /**
   * Returns a set of the namespace URIs referenced in the XML document
   * represented by this {@link XMLManifest} instance.
   *
   * @return A set of the namespace URIs referenced in the XML document
   *         represented by this {@link XMLManifest} instance.
   */
  public Set<String> getNamespaceURIs() {
    return namespaceURIs;
  }

  private boolean isSchema;

  /**
   * Returns whether the XML document represented by this {@link XMLManifest}
   * instance is an XML Schema Document.
   *
   * @return Whether the XML document represented by this {@link XMLManifest}
   *         instance is an XML Schema Document.
   * @throws IllegalStateException If this method is called before the XML
   *           document represented by this {@link XMLManifest} instance is
   *           parsed.
   */
  public boolean isSchema() {
    if (rootElement == null)
      throw new IllegalStateException("Parsing has not been performed");

    return isSchema;
  }

  private QName rootElement;

  /**
   * Returns the {@link QName} of the root element of the XML document
   * represented by this {@link XMLManifest} instance.
   *
   * @return The {@link QName} of the root element of the XML document
   *         represented by this {@link XMLManifest} instance.
   * @throws IllegalStateException If this method is called before the XML
   *           document represented by this {@link XMLManifest} instance is
   *           parsed.
   */
  public QName getRootElement() {
    if (rootElement == null)
      throw new IllegalStateException("Parsing has not been performed");

    return rootElement;
  }

  /**
   * Returns the "targetNamespace" attribute of the XML document represented by
   * this {@link XMLManifest} instance. This method is only useful for XML
   * Schema Documents (i.e. when {@link #isSchema()} is {@code true}).
   *
   * @return The "targetNamespace" attribute of the XML document represented by
   *         this {@link XMLManifest} instance.
   */
  public String getTargetNamespace() {
    return this.targetNamespace;
  }

//  public boolean referencesOnlyLocal() {
//    return referencesOnlyLocal;
//  }

  private Map<String,URL> imports;

  private Map<String,URL> imports() {
    return imports == null ? imports = new LinkedHashMap<>() : imports;
  }

  /**
   * Returns the map of namespace-to-URL entries of "import" references for the
   * XML document represented by this {@link XMLManifest} instance.
   * <ul>
   * <li>If {@link #isSchema()} is {@code true}, this method represents the
   * {@code <xs:import/>} elements of an XML Schema Document.</li>
   * <li>If {@link #isSchema()} is {@code false}, this method represents the
   * {@code xsi:schemaLocation} attribute of an XML Document.</li>
   * </ul>
   *
   * @return The map of namespace-to-URL entries of "import" references for the
   *         XML document represented by this {@link XMLManifest} instance.
   */
  public Map<String,URL> getImports() {
    return imports;
  }

  private void addImport(final String namespace, final String location) {
    final String path = SchemaLocationResolver.getPath(systemId, location);
    referencesOnlyLocal &= Paths.isAbsoluteLocal(path);
    if (!imports().containsKey(namespace))
      imports.put(namespace, Paths.getProtocol(path) == null ? URLs.create("file:" + path) : URLs.create(path));
  }

  private Map<String,URL> includes;

  private Map<String,URL> includes() {
    return includes == null ? includes = new LinkedHashMap<>() : includes;
  }

  /**
   * Returns the map of {@link String}-to-{@link URL} entries of "include"
   * references for the XML document represented by this {@link XMLManifest}
   * instance. The key and value of each entry in this map represents the same
   * logical string, differing only in class type.
   * <ul>
   * <li>If {@link #isSchema()} is {@code true}, this method represents the
   * {@code <xs:include/>} elements of an XML Schema Document.</li>
   * <li>If {@link #isSchema()} is {@code false}, this method represents the
   * {@code xsi:noNamespaceSchemaLocation} and {@code <xi:include href>}
   * attributes in an XML Document.</li>
   * </ul>
   *
   * @return The map of {@link String}-to-{@link URL} entries of "include"
   *         references for the XML document represented by this
   *         {@link XMLManifest} instance.
   */
  public Map<String,URL> getIncludes() {
    return includes;
  }

  private void addInclude(final String schemaLocation) {
    final String path = SchemaLocationResolver.getPath(systemId, schemaLocation);
    referencesOnlyLocal &= Paths.isAbsoluteLocal(path);
    URL url = absoluteIncludes.get(path);
    if (url == null)
      absoluteIncludes.put(path, url = URLs.create(path));

    includes().put(schemaLocation, url);
  }

  @Override
  public boolean startElement(final QName name, final Map<QName,String> attributes) throws IOException {
    if (logger.isDebugEnabled()) {
      final String attrs = SAXUtil.toString(attributes);
      logger.debug("<" + name.getLocalPart() + " xmlns=\"" + name.getNamespaceURI() + "\"" + (attrs != null ? " " + attrs + ">" : ">"));
    }

    if (rootElement == null) {
      rootElement = name;
      isSchema = XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(name.getNamespaceURI()) && "schema".equals(rootElement.getLocalPart());
    }

    if (isSchema) {
      if ("schema".equals(name.getLocalPart())) {
        for (final Map.Entry<QName,String> entry : attributes.entrySet()) {
          final String attributeName = entry.getKey().getLocalPart();
          if ("targetNamespace".equals(attributeName)) {
            targetNamespace = entry.getValue();
            break;
          }
        }
      }
      else if ("import".equals(name.getLocalPart())) {
        String namespace = null;
        String schemaLocation = null;
        for (final Map.Entry<QName,String> entry : attributes.entrySet()) {
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

        final String path = SchemaLocationResolver.getPath(systemId, schemaLocation);
        referencesOnlyLocal &= Paths.isAbsoluteLocal(path);
        namespaceURIs.add(namespace);
        if (!imports().containsKey(namespace))
          imports.put(namespace, URLs.create(path));
      }
      else if ("include".equals(name.getLocalPart())) {
        for (final Map.Entry<QName,String> entry : attributes.entrySet()) {
          if ("schemaLocation".equals(entry.getKey().getLocalPart())) {
            addInclude(entry.getValue());
          }
        }
      }
      else if (!"schema".equals(name.getLocalPart()) && !"annotation".equals(name.getLocalPart()) && !"redefine".equals(name.getLocalPart())) {
        return false;
      }
    }
    else {
      if ("include".equals(name.getLocalPart())) {
        for (final Map.Entry<QName,String> entry : attributes.entrySet()) {
          final String namespaceURI = entry.getKey().getNamespaceURI();
          if ("http://www.w3.org/2001/XInclude".equals(namespaceURI)) {
            if ("href".equals(entry.getKey().getLocalPart())) {
              addInclude(entry.getValue());
            }
          }
        }
      }
      else {
        if (attributes != null) {
          for (final Map.Entry<QName,String> entry : attributes.entrySet()) {
            final String namespaceURI = entry.getKey().getNamespaceURI();
            if (XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI.equals(namespaceURI)) {
              if ("noNamespaceSchemaLocation".equals(entry.getKey().getLocalPart())) {
                addInclude(entry.getValue());
              }
              else if ("schemaLocation".equals(entry.getKey().getLocalPart())) {
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
            else if (namespaceURI.length() != 0) {
              namespaceURIs.add(namespaceURI);
            }
          }
        }
      }

      if (!XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI.equals(name.getNamespaceURI()) && name.getNamespaceURI().length() != 0)
        namespaceURIs.add(name.getNamespaceURI());
    }

    return true;
  }
}