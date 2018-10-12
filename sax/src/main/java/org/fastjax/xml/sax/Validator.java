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
import java.net.URL;
import java.util.Iterator;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.apache.xerces.impl.Constants;
import org.fastjax.io.Streams;
import org.fastjax.util.MemoryURLStreamHandler;
import org.fastjax.xml.OfflineValidationException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public final class Validator {
  private static final SchemaFactory factory = SchemaFactory.newInstance(Constants.W3C_XML_SCHEMA11_NS_URI);

  public static void validate(final String xml, final boolean localOnly) throws IOException, SAXException {
    validate(MemoryURLStreamHandler.createURL(xml.getBytes()), localOnly, new LoggingErrorHandler());
  }

  public static void validate(final InputStream in, final boolean localOnly) throws IOException, SAXException {
    validate(MemoryURLStreamHandler.createURL(Streams.readBytes(in)), localOnly, new LoggingErrorHandler());
  }

  public static void validate(final URL url, final boolean localOnly) throws IOException, SAXException {
    validate(url, localOnly, new LoggingErrorHandler());
  }

  public static void validate(final URL url, final boolean localOnly, final ErrorHandler errorHandler) throws IOException, SAXException {
    final XMLDocument xmlDocument = XMLDocuments.parse(url, localOnly, true);
    final XMLCatalog catalog = xmlDocument.getCatalog();
    if (localOnly && !xmlDocument.referencesLocalOnly()) {
      final SAXParseException parseException = new SAXParseException("Offline execution not checking remote schemas", url.toString(), null, 0, 0);
      errorHandler.warning(parseException);
      throw new OfflineValidationException(parseException);
    }

    if (catalog.isEmpty() && !xmlDocument.isXsd()) {
      errorHandler.warning(new SAXParseException("There is no schema or DTD associated with the document", url.toString(), null, 0, 0));
      return;
    }

    try (final InputStream in = url.openStream()) {
      validate(new StreamSource(in, url.toString()), catalog, xmlDocument.isXsd(), errorHandler);
    }
  }

  public static URL validate(final URL url, final XMLCatalog catalog, final boolean isXsd, final ErrorHandler errorHandler) throws IOException, SAXException {
    try (final InputStream in = url.openStream()) {
      validate(new StreamSource(in, url.toString()), catalog, isXsd, errorHandler);
    }

    return url;
  }

  public static void validate(final StreamSource streamSource, final XMLCatalog catalog, final boolean isXsd, final ErrorHandler errorHandler) throws IOException, SAXException {
    final ValidatorErrorHandler validatorErrorHandler = new ValidatorErrorHandler(errorHandler);
    final SchemaLocationResolver schemaLocationResolver = new SchemaLocationResolver(catalog);
    if (isXsd) {
      final SchemaFactory factory = SchemaFactory.newInstance(Constants.W3C_XML_SCHEMA11_NS_URI);
      factory.setResourceResolver(schemaLocationResolver);
      factory.setErrorHandler(validatorErrorHandler);

      factory.newSchema();
    }
    else {
      final javax.xml.validation.Validator validator = factory.newSchema().newValidator();
      validator.setResourceResolver(schemaLocationResolver);
      validator.setErrorHandler(validatorErrorHandler);

      validator.validate(streamSource);
    }

    if (validatorErrorHandler.getErrors() != null) {
      final Iterator<SAXParseException> iterator = validatorErrorHandler.getErrors().iterator();
      final SAXParseException exception = iterator.next();
      while (iterator.hasNext())
        exception.addSuppressed(iterator.next());

      throw exception;
    }
  }

  private Validator() {
  }
}