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

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public final class SAXParser {
  private final XMLReader xmlReader;

  protected SAXParser(final XMLReader xmlReader) {
    this.xmlReader = xmlReader;
  }

  public void setFeature(final SAXFeature feature, final boolean value) {
    try {
      xmlReader.setFeature(feature.getFeature(), value);
    }
    catch (final SAXNotRecognizedException e) {
      // FIXME: Remove this!
      throw new Error(e);
    }
    catch (final SAXNotSupportedException e) {
      // FIXME: Remove this!
      throw new Error(e);
    }
  }

  public boolean getFeature(final SAXFeature feature) {
    try {
      return xmlReader.getFeature(feature.getFeature());
    }
    catch (final SAXNotRecognizedException e) {
      // FIXME: Remove this!
      throw new Error(e);
    }
    catch (final SAXNotSupportedException e) {
      // FIXME: Remove this!
      throw new Error(e);
    }
  }

  public void setProptery(final SAXProperty property, final Object value) {
    try {
      xmlReader.setProperty(property.getProperty(), value);
    }
    catch (final SAXNotRecognizedException e) {
      // FIXME: Remove this!
      throw new Error(e);
    }
    catch (final SAXNotSupportedException e) {
      // FIXME: Remove this!
      throw new Error(e);
    }
  }

  public Object getProperty(final SAXProperty property) {
    try {
      return xmlReader.getProperty(property.getProperty());
    }
    catch (final SAXNotRecognizedException e) {
      // FIXME: Remove this!
      throw new Error(e);
    }
    catch (final SAXNotSupportedException e) {
      // FIXME: Remove this!
      throw new Error(e);
    }
  }

  public void setEntityResolver(final EntityResolver resolver) {
    xmlReader.setEntityResolver(resolver);
  }

  public EntityResolver getEntityResolver() {
    return xmlReader.getEntityResolver();
  }

  public void setDTDHandler(final DTDHandler handler) {
    xmlReader.setDTDHandler(handler);
  }

  public DTDHandler getDTDHandler() {
    return xmlReader.getDTDHandler();
  }

  public void setContentHandler(final ContentHandler handler) {
    xmlReader.setContentHandler(handler);
  }

  public ContentHandler getContentHandler() {
    return xmlReader.getContentHandler();
  }

  public void setErrorHandler(final ErrorHandler handler) {
    xmlReader.setErrorHandler(handler);
  }

  public ErrorHandler getErrorHandler() {
    return xmlReader.getErrorHandler();
  }

  public void parse(final InputSource input) throws IOException, SAXException {
    xmlReader.parse(input);
  }

  public void parse(final String systemId) throws IOException, SAXException {
    xmlReader.parse(systemId);
  }
}