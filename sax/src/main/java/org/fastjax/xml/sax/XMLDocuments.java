/* Copyright (c) 2008 FastJAX
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

package org.fastjax.xml.sax;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.fastjax.net.URLs;
import org.fastjax.net.WrappedURLConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public final class XMLDocuments {
  private static final Logger logger = LoggerFactory.getLogger(SchemaLocationHandler.class);
  private static SAXParserFactory factory;

  static {
    try {
      factory = SAXParserFactory.newInstance("org.apache.xerces.jaxp.SAXParserFactoryImpl", null);
    }
    catch (final FactoryConfigurationError e) {
      factory = SAXParserFactory.newInstance();
      logger.warn("Unable to create SAXParserFactory of type org.apache.xerces.jaxp.SAXParserFactoryImpl. Factory of " + factory.getClass().getName() + " created instead.", e);
    }
  }

  private static SAXParser newParser() throws SAXException {
    factory.setNamespaceAware(true);
    factory.setValidating(true);
    try {
      factory.setFeature("http://xml.org/sax/features/validation", true);
      factory.setFeature("http://apache.org/xml/features/validation/schema", true);
      factory.setFeature("http://apache.org/xml/features/validation/dynamic", false);
      factory.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
      factory.setFeature("http://apache.org/xml/features/honour-all-schemaLocations", true);
      factory.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true);
      return factory.newSAXParser();
    }
    catch (final ParserConfigurationException e) {
      throw new SAXException(e);
    }
  }

  public static XMLDocument parse(final URL url, final boolean localOnly, final boolean validating) throws IOException, SAXException {
    return parse(url, null, localOnly, validating);
  }

  protected static URL disableHttp(final URL url, final boolean localOnly) throws MalformedURLException {
    return localOnly && url.getProtocol().startsWith("http") ? new URL(url, "", new URLStreamHandler() {
      @Override
      protected URLConnection openConnection(final URL u) throws IOException {
        return openConnection(u, null);
      }

      @Override
      protected URLConnection openConnection(final URL u, final Proxy proxy) throws IOException {
        return new WrappedURLConnection(proxy != null ? url.openConnection(proxy) : url.openConnection()) {
          @Override
          public InputStream getInputStream() throws IOException {
            return new InputStream() {
              @Override
              public int read() throws IOException {
                throw new IOException();
              }
            };
          }

          @Override
          public OutputStream getOutputStream() throws IOException {
            return new OutputStream() {
              @Override
              public void write(final int b) throws IOException {
                throw new IOException();
              }
            };
          }
        };
      }
    }) : url;
  }

  public static XMLDocument parse(URL url, final DocumentHandler documentHandler, final boolean localOnly, final boolean validating) throws IOException, SAXException {
    url = disableHttp(url, localOnly);

    final SchemaLocationHandler handler = new SchemaLocationHandler(url, localOnly, validating);

    final SAXParser parser = newParser();
    parser.parse(url.openStream(), handler);
    parser.reset();
    final XMLCatalog catalog = new XMLCatalog();
    if (handler.isXSD())
      catalog.putSchemaLocation(handler.getTargetNamespace(), new SchemaLocation(handler.getTargetNamespace(), url));

    boolean referencesOnlyLocal = imports(parser, documentHandler, localOnly, catalog, handler.getNamespaceURIs(), handler.getImports());
    if (handler.isXSD())
      referencesOnlyLocal = includes(parser, documentHandler, localOnly, catalog, handler.getTargetNamespace(), handler.getIncludes()) && referencesOnlyLocal;

    return new XMLDocument(url, catalog, handler.getRootElement(), handler.isXSD(), handler.referencesOnlyLocal() && referencesOnlyLocal);
  }

  private static boolean imports(final SAXParser parser, final DocumentHandler documentHandler, final boolean localOnly, final XMLCatalog catalog, final Set<String> namespaceURIs, final Map<String,URL> schemaLocations) throws IOException, SAXException {
    boolean referencesLocalOnly = true;
    for (final Map.Entry<String,URL> schemaLocation : schemaLocations.entrySet()) {
      if (!catalog.hasSchemaLocation(schemaLocation.getKey())) {
        if (!localOnly || (referencesLocalOnly = URLs.isLocal(schemaLocation.getValue()) && referencesLocalOnly)) {
          final SchemaLocationHandler handler = new SchemaLocationHandler(schemaLocation.getValue(), localOnly, false);
          if (documentHandler != null)
            documentHandler.schemaLocation(schemaLocation.getValue().openConnection());

          parser.reset();
          try (final InputStream in = schemaLocation.getValue().openStream()) {
            parser.parse(in, handler);
          }
          catch (final SAXInterruptException e) {
            logger.debug("Caught " + SAXInterruptException.class.getName());
          }

          catalog.putSchemaLocation(schemaLocation.getKey(), new SchemaLocation(schemaLocation.getKey(), schemaLocation.getValue()));
          for (final String location : handler.getImports().keySet())
            namespaceURIs.add(location);

          namespaceURIs.remove(schemaLocation.getKey());
          if (namespaceURIs.isEmpty())
            break;

          referencesLocalOnly = imports(parser, documentHandler, localOnly, catalog, namespaceURIs, handler.getImports()) && referencesLocalOnly;
          referencesLocalOnly = includes(parser, documentHandler, localOnly, catalog, schemaLocation.getKey(), handler.getIncludes()) && referencesLocalOnly;
        }
      }
    }

    return referencesLocalOnly;
  }

  private static boolean includes(final SAXParser parser, final DocumentHandler documentHandler, final boolean localOnly, final XMLCatalog references, final String namespaceURI, final Map<String,URL> includes) throws IOException, SAXException {
    boolean referencesLocalOnly = true;
    for (final Map.Entry<String,URL> entry : includes.entrySet()) {
      final URL include = entry.getValue();
      if (!localOnly || (referencesLocalOnly = URLs.isLocal(include) && referencesLocalOnly)) {
        final SchemaLocationHandler handler = new SchemaLocationHandler(include, localOnly, false);
        if (documentHandler != null)
          documentHandler.schemaLocation(include.openConnection());

        parser.reset();
        try (final InputStream in = include.openStream()) {
          parser.parse(in, handler);
        }
        catch (final SAXInterruptException e) {
          logger.debug("Caught " + SAXInterruptException.class.getName());
        }

        references.getSchemaLocation(namespaceURI).getDirectory().put(entry.getKey(), include);
        referencesLocalOnly = includes(parser, documentHandler, localOnly, references, namespaceURI, handler.getIncludes()) && referencesLocalOnly;
      }
    }

    return referencesLocalOnly;
  }
}