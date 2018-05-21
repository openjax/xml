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

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.lib4j.net.CachedURL;
import org.lib4j.net.URLs;
import org.lib4j.xml.OfflineValidationException;
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
    final CachedURL cachedUrl = new CachedURL(url);
    final XMLDocument xmlDocument = XMLDocuments.parse(cachedUrl, offline, true);
    final XMLCatalog catalog = xmlDocument.getCatalog();
    if (offline && !xmlDocument.referencesOnlyLocal()) {
      final SAXParseException parseException = new SAXParseException("Offline execution not checking remote schemas.", URLs.toExternalForm(url), null, 0, 0);
      errorHandler.warning(parseException);
      throw new OfflineValidationException(parseException);
    }

    if (catalog.isEmpty() && !xmlDocument.isXsd()) {
      errorHandler.warning(new SAXParseException("There is no schema or DTD associated with the document.", URLs.toExternalForm(url), null, 0, 0));
      return cachedUrl;
    }

    try (final InputStream in = cachedUrl.openStream()) {
      validate(new StreamSource(in, url.toString()), catalog, xmlDocument.isXsd(), errorHandler);
    }

    catalog.destroy();
    return cachedUrl;
  }

  public static CachedURL validate(final URL url, final XMLCatalog catalog, final boolean isXsd, final ErrorHandler errorHandler) throws IOException, SAXException {
    final CachedURL cachedUrl = new CachedURL(url);
    try (final InputStream in = cachedUrl.openStream()) {
      validate(new StreamSource(in, url.toString()), catalog, isXsd, errorHandler);
    }

    return cachedUrl;
  }

  public static void validate(final StreamSource streamSource, final XMLCatalog catalog, final boolean isXsd, final ErrorHandler errorHandler) throws IOException, SAXException {
    final ValidatorErrorHandler validatorErrorHandler = new ValidatorErrorHandler(errorHandler);
    final SchemaLocationResolver schemaLocationResolver = new SchemaLocationResolver(catalog);
    if (isXsd) {
      final SchemaFactory factory = SchemaFactory.newInstance(XML_11_URI);
      factory.setResourceResolver(schemaLocationResolver);
      factory.setErrorHandler(validatorErrorHandler);

      factory.newSchema();
    }
    else {
      final javax.xml.validation.Validator validator = factory.newSchema().newValidator();
      validator.setResourceResolver(schemaLocationResolver);
      validator.setErrorHandler(validatorErrorHandler);

      System.err.println(streamSource.getSystemId());
      validator.validate(streamSource);
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
  }

  private Validator() {
  }
}