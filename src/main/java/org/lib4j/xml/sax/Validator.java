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
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.lib4j.net.CachedURL;
import org.lib4j.net.URLs;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public final class Validator {
  private static final String XML_11_URI = "http://www.w3.org/XML/XMLSchema/v1.1";
  private static final SchemaFactory factory = SchemaFactory.newInstance(XML_11_URI);

  public static CachedURL validate(final URL url, final boolean offline) throws IOException, SAXException {
    return validate(url, offline, new LoggingErrorHandler());
  }

  public static CachedURL validate(final URL url, final boolean offline, final ErrorHandler errorHandler) throws IOException, SAXException {
    final CachedURL cachedURL = new CachedURL(url);
    final XMLDocument xmlDocument = XMLDocuments.parse(cachedURL.toURL(), offline, true);
    final Map<String,SchemaLocation> schemaReferences = xmlDocument.getSchemaReferences();
    if (offline && !xmlDocument.referencesOnlyLocal()) {
      errorHandler.warning(new SAXParseException("Offline execution not checking remote schemas.", URLs.toExternalForm(url), null, 0, 0));
      return cachedURL;
    }

    if (schemaReferences.isEmpty() && !xmlDocument.isXSD()) {
      errorHandler.warning(new SAXParseException("There is no schema or DTD associated with the document.", URLs.toExternalForm(url), null, 0, 0));
      return cachedURL;
    }

    final ValidatorErrorHandler validatorErrorHandler = new ValidatorErrorHandler(errorHandler);
    final SchemaLocationResolver schemaLocationResolver = new SchemaLocationResolver(schemaReferences);
    if (xmlDocument.isXSD()) {
      final SchemaFactory factory = SchemaFactory.newInstance(XML_11_URI);
      factory.setResourceResolver(schemaLocationResolver);
      factory.setErrorHandler(validatorErrorHandler);

      try (final InputStream in = cachedURL.toURL().openStream()) {
        factory.newSchema(new StreamSource(in, cachedURL.toString()));
      }
    }
    else {
      final javax.xml.validation.Validator validator = factory.newSchema().newValidator();
      validator.setResourceResolver(schemaLocationResolver);
      validator.setErrorHandler(validatorErrorHandler);

      try (final InputStream in = url.openStream()) {
        validator.validate(new StreamSource(in, cachedURL.toString()));
      }
    }

    for (final Map.Entry<String,SchemaLocation> schemaLocation : schemaReferences.entrySet()) {
      final Map<String,CachedURL> locations = schemaLocation.getValue().getLocation();
      for (final CachedURL cachedUrl : locations.values())
        cachedUrl.destroy();
    }

    if (validatorErrorHandler.getErrors() != null) {
      final Iterator<SAXParseException> iterator = validatorErrorHandler.getErrors().iterator();
      final SAXParseException firstException = iterator.next();
      final SAXException exception = new SAXException(firstException);
      exception.setStackTrace(firstException.getStackTrace());
      while (iterator.hasNext())
        exception.addSuppressed(iterator.next());

      throw exception;
    }

    return cachedURL;
  }

  private Validator() {
  }
}