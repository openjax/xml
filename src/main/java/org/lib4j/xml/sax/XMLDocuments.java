/* Copyright (c) 2008 lib4j
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

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.lib4j.net.CachedURL;
import org.lib4j.net.Sockets;
import org.lib4j.net.URLs;
import org.xml.sax.SAXException;

public final class XMLDocuments {
  private static SAXParserFactory factory;

  static {
    try {
      factory = SAXParserFactory.newInstance("org.apache.xerces.jaxp.SAXParserFactoryImpl", null);
    }
    catch (final FactoryConfigurationError e) {
      factory = SAXParserFactory.newInstance();
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

  public static XMLDocument parse(final URL url, final boolean offline, final boolean validating) throws IOException, SAXException {
    return parse(url, null, offline, validating);
  }

  public static XMLDocument parse(final URL url, final DocumentHandler documentHandler, final boolean offline, final boolean validating) throws IOException, SAXException {
    final CachedURL cachedURL = new CachedURL(url);
    final SchemaLocationHandler handler = new SchemaLocationHandler(cachedURL, validating);
    if (offline)
      Sockets.disableNetwork();

    final SAXParser parser = newParser();
    parser.parse(cachedURL.openStream(), handler);
    parser.reset();
    final Map<String,SchemaLocation> references = new LinkedHashMap<String,SchemaLocation>();
    if (handler.isXSD())
      references.put(handler.getTargetNamespace(), new SchemaLocation(handler.getTargetNamespace(), cachedURL));

    boolean referencesOnlyLocal = imports(parser, documentHandler, offline, references, handler.getNamespaceURIs(), handler.getImports());
    if (handler.isXSD())
      referencesOnlyLocal = includes(parser, documentHandler, offline, references, handler.getTargetNamespace(), handler.getIncludes()) && referencesOnlyLocal;

    final XMLDocument xmlDocument = new XMLDocument(references, handler.getRootElement(), handler.isXSD(), handler.referencesOnlyLocal() && referencesOnlyLocal);
    if (offline)
      Sockets.enableNetwork();

    return xmlDocument;
  }

  private static boolean imports(final SAXParser parser, final DocumentHandler documentHandler, final boolean offline, final Map<String,SchemaLocation> references, final Set<String> namespaceURIs, final Map<String,CachedURL> schemaLocations) throws IOException, SAXException {
    boolean referencesOnlyLocal = true;
    for (final Map.Entry<String,CachedURL> schemaLocation : schemaLocations.entrySet()) {
      if (!references.containsKey(schemaLocation.getKey())) {
        if (!offline || (referencesOnlyLocal = schemaLocation.getValue().isLocal() && referencesOnlyLocal)) {
          final SchemaLocationHandler handler = new SchemaLocationHandler(schemaLocation.getValue(), false);
          try {
            if (documentHandler != null)
              documentHandler.schemaLocation(schemaLocation.getValue().openConnection());

            parser.reset();
            parser.parse(schemaLocation.getValue().openStream(), handler);
          }
          catch (final SAXInterruptException e) {
            schemaLocation.getValue().reset();
          }

          references.put(schemaLocation.getKey(), new SchemaLocation(schemaLocation.getKey(), schemaLocation.getValue()));
          for (final String location : handler.getImports().keySet())
            if (!references.containsKey(location))
              namespaceURIs.add(location);

          namespaceURIs.remove(schemaLocation.getKey());
          if (namespaceURIs.isEmpty())
            break;

          referencesOnlyLocal = imports(parser, documentHandler, offline, references, namespaceURIs, handler.getImports()) && referencesOnlyLocal;
          referencesOnlyLocal = includes(parser, documentHandler, offline, references, schemaLocation.getKey(), handler.getIncludes()) && referencesOnlyLocal;
        }
      }
    }

    return referencesOnlyLocal;
  }

  private static boolean includes(final SAXParser parser, final DocumentHandler documentHandler, final boolean offline, final Map<String,SchemaLocation> references, final String namespaceURI, final Set<CachedURL> includes) throws IOException, SAXException {
    boolean referencesOnlyLocal = false;
    for (final CachedURL include : includes) {
      if (!offline || (referencesOnlyLocal = include.isLocal() && referencesOnlyLocal)) {
        final SchemaLocationHandler handler = new SchemaLocationHandler(include, false);
        try {
          if (documentHandler != null)
            documentHandler.schemaLocation(include.openConnection());

          parser.reset();
          parser.parse(include.openStream(), handler);
        }
        catch (final SAXInterruptException e) {
          include.reset();
        }

        references.get(namespaceURI).getLocation().put(URLs.toExternalForm(include), include);
        referencesOnlyLocal = includes(parser, documentHandler, offline, references, namespaceURI, handler.getIncludes()) && referencesOnlyLocal;
      }
    }

    return referencesOnlyLocal;
  }
}