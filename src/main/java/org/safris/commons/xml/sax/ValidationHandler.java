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

package org.safris.commons.xml.sax;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;

import org.safris.commons.lang.Resources;
import org.safris.commons.net.CachedURL;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public final class ValidationHandler extends DefaultHandler implements LSResourceResolver {
  private final Map<String,SchemaLocation> schemaLocations;
  private final ErrorHandler errorHandler;

  public ValidationHandler(final Map<String,SchemaLocation> schemaLocations, final ErrorHandler errorHandler) {
    this.schemaLocations = schemaLocations;
    this.errorHandler = errorHandler;
  }

  private List<SAXParseException> errors;

  @Override
  public void fatalError(final SAXParseException e) throws SAXException {
    if (errorHandler != null)
      errorHandler.fatalError(e);
  }

  @Override
  public void error(final SAXParseException e) throws SAXException {
    if (errors == null)
      errors = new ArrayList<SAXParseException>();

    errors.add(e);
    if (errorHandler != null)
      errorHandler.error(e);
  }

  @Override
  public void warning(final SAXParseException e) throws SAXException {
    if (errorHandler != null)
      errorHandler.warning(e);
  }

  public List<SAXParseException> getErrors() {
    return errors;
  }

  @Override
  public LSInput resolveResource(final String type, final String namespaceURI, final String publicId, String systemId, final String baseURI) {
    if (namespaceURI == null && systemId == null)
      return new LSInputImpl(systemId, publicId, baseURI);

    if (systemId == null)
      systemId = namespaceURI;
    else if (baseURI != null)
      systemId = XMLHandler.getPath(baseURI, systemId);

    try {
      SchemaLocation schemaLocation = schemaLocations.get(namespaceURI);
      final Map<String,CachedURL> locations;
      if (schemaLocation == null) {
        if (namespaceURI == null) {
          SchemaLocation nullLocation = schemaLocations.get(null);
          if (nullLocation == null)
            schemaLocations.put(null, nullLocation = new SchemaLocation(null));

          locations = nullLocation.getLocation();
        }
        else if (XMLConstants.W3C_XML_SCHEMA_NS_URI.equals(namespaceURI)) {
          schemaLocations.put(XMLConstants.W3C_XML_SCHEMA_NS_URI, schemaLocation = new SchemaLocation(namespaceURI));
          schemaLocation.getLocation().put(namespaceURI, new CachedURL(Resources.getResource("xmlschema/XMLSchema.xsd").getURL()));
          locations = schemaLocation.getLocation();
        }
        else if ("http://www.w3.org/XML/1998/namespace".equals(namespaceURI) && "http://www.w3.org/2001/xml.xsd".equals(systemId)) {
          schemaLocations.put(namespaceURI, schemaLocation = new SchemaLocation(systemId));
          schemaLocation.getLocation().put(systemId, new CachedURL(Resources.getResource("xmlschema/xml.xsd").getURL()));
          locations = schemaLocation.getLocation();
        }
        else {
          return new LSInputImpl(systemId, publicId, baseURI);
        }
      }
      else {
        locations = schemaLocation.getLocation();
      }

      CachedURL url = locations.get(systemId);
      if (url == null) {
        if (namespaceURI == null) {
          if ("http://www.w3.org/2001/XMLSchema.dtd".equals(systemId)) {
            locations.put(systemId, url = new CachedURL(Resources.getResource("xmlschema/XMLSchema.dtd").getURL()));
          }
          else if ("http://www.w3.org/2001/datatypes.dtd".equals(systemId)) {
            locations.put(systemId, url = new CachedURL(Resources.getResource("xmlschema/datatypes.dtd").getURL()));
          }
          else {
            return new LSInputImpl(systemId, publicId, baseURI);
          }
        }
        else {
          return new LSInputImpl(systemId, publicId, baseURI);
        }
      }

      final LSInput input = new LSInputImpl(systemId, publicId, baseURI);
      input.setByteStream(url.openStream());
      return input;
    }
    catch (final IOException e) {
      throw new UnsupportedOperationException(e);
    }
  }
}