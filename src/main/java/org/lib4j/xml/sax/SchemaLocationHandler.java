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

import java.net.MalformedURLException;
import java.net.URI;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.lib4j.lang.Paths;
import org.lib4j.net.CachedURL;
import org.lib4j.net.URLs;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SchemaLocationHandler extends DefaultHandler {
  protected static String getPath(final String referrer, final String location) {
    return URLs.isAbsolute(location) ? location : Paths.newPath(Paths.getCanonicalParent(referrer), location);
  }

  protected static String getPath(final URI referrer, final String location) {
    return getPath(referrer.toASCIIString(), location);
  }

  private final Set<String> namespaceURIs = new HashSet<String>();
  private final Map<String,CachedURL> absoluteIncludes = new LinkedHashMap<String,CachedURL>();
  private final Map<String,CachedURL> imports = new LinkedHashMap<String,CachedURL>();
  private final Map<String,CachedURL> includes = new LinkedHashMap<String,CachedURL>();
  private boolean referencesOnlyLocal = true;
  private String targetNamespace = null;

  private final boolean validating;
  protected final CachedURL url;

  protected SchemaLocationHandler(final CachedURL url, final boolean validating) {
    this.url = url;
    this.validating = validating;
  }

  public Set<String> getNamespaceURIs() {
    return namespaceURIs;
  }

  public Map<String,CachedURL> getImports() {
    return imports;
  }

  public Map<String,CachedURL> getIncludes() {
    return includes;
  }

  private Boolean isXSD = null;

  public boolean isXSD() {
    if (isXSD == null)
      throw new IllegalStateException("Parsing has not been performed");

    return isXSD;
  }

  private QName rootElement;

  public QName getRootElement() {
    if (rootElement == null)
      throw new IllegalStateException("Parsing has not been performed");

    return rootElement;
  }

  public String getTargetNamespace() {
    return this.targetNamespace;
  }

  public boolean referencesOnlyLocal() {
    return referencesOnlyLocal;
  }

  @Override
  public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
    if (isXSD == null) {
      isXSD = XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(uri);
      rootElement = new QName(uri, localName);
    }

    if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(uri)) {
      if ("schema".equals(localName)) {
        for (int i = 0; i < attributes.getLength(); i++) {
          final String attributeName = attributes.getLocalName(i);
          if ("targetNamespace".equals(attributeName)) {
            targetNamespace = attributes.getValue(i);
            break;
          }
        }
      }
      else if ("import".equals(localName)) {
        String namespace = null;
        String schemaLocation = null;
        for (int i = 0; i < attributes.getLength(); i++) {
          final String attributeName = attributes.getLocalName(i);
          if ("namespace".equals(attributeName)) {
            namespace = attributes.getValue(i);
            if (schemaLocation != null)
              break;
          }
          else if ("schemaLocation".equals(attributeName)) {
            schemaLocation = attributes.getValue(i);
            if (namespace != null)
              break;
          }
        }

        try {
          final String path = getPath(url.toExternalForm(), schemaLocation);
          referencesOnlyLocal = Paths.isLocal(path) && referencesOnlyLocal;
          namespaceURIs.add(namespace);
          if (!imports.containsKey(namespace))
            imports.put(namespace, new CachedURL(path));
        }
        catch (final MalformedURLException e) {
          throw new SAXException(e);
        }
      }
      else if ("include".equals(localName)) {
        for (int i = 0; i < attributes.getLength(); i++) {
          if ("schemaLocation".equals(attributes.getLocalName(i))) {
            final String schemaLocation = attributes.getValue(i);
            try {
              final String absolutePath = getPath(URLs.toExternalForm(url), schemaLocation);
              referencesOnlyLocal = Paths.isLocal(absolutePath) && referencesOnlyLocal;
              CachedURL cachedURL = absoluteIncludes.get(absolutePath);
              if (cachedURL == null)
                absoluteIncludes.put(absolutePath, cachedURL = new CachedURL(absolutePath));

              includes.put(schemaLocation, cachedURL);
            }
            catch (final MalformedURLException e) {
              throw new SAXException(e);
            }
          }
        }
      }
      else if (!validating && !"schema".equals(localName) && !"annotation".equals(localName) && !"redefine".equals(localName)) {
        throw new SAXInterruptException();
      }
    }
    else {
      for (int i = 0; i < attributes.getLength(); i++) {
        final String namespaceURI = attributes.getURI(i);
        if (XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI.equals(namespaceURI)) {
          if ("schemaLocation".equals(attributes.getLocalName(i))) {
            final String value = attributes.getValue(i);
            final StringTokenizer tokenizer = new StringTokenizer(value);
            while (tokenizer.hasMoreTokens()) {
              final String schemaNamespaceURI = tokenizer.nextToken();
              if (tokenizer.hasMoreTokens()) {
                final String location = tokenizer.nextToken();
                try {
                  final String path = getPath(URLs.toExternalForm(url), location);
                  referencesOnlyLocal = Paths.isLocal(path) && referencesOnlyLocal;
                  if (!imports.containsKey(schemaNamespaceURI))
                    imports.put(schemaNamespaceURI, new CachedURL(path));
                }
                catch (final MalformedURLException e) {
                  throw new SAXException(e);
                }
              }
            }
          }
        }
        else if (namespaceURI.length() != 0) {
          namespaceURIs.add(namespaceURI);
        }
      }

      if (!XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI.equals(uri) && uri.length() != 0)
        namespaceURIs.add(uri);
    }
  }
}