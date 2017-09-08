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

import java.io.File;
import java.io.IOException;
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
  private static final SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/XML/XMLSchema/v1.1");

  public static void validate(final File file, final boolean offline) throws IOException, SAXException {
    validate(file, offline, new LoggingErrorHandler());
  }

  public static void validate(final File file, final boolean offline, final ErrorHandler errorHandler) throws IOException, SAXException {
    final XMLDocument xmlDocument = XMLDocuments.parse(file.toURI().toURL(), offline, true);
    final Map<String,SchemaLocation> schemaReferences = xmlDocument.getSchemaReferences();
    if (offline && !xmlDocument.referencesOnlyLocal()) {
      errorHandler.warning(new SAXParseException("Offline execution not checking remote schemas.", URLs.toExternalForm(file.toURI().toURL()), null, 0, 0));
      return;
    }

    if (schemaReferences.isEmpty() && !xmlDocument.isXSD()) {
      errorHandler.warning(new SAXParseException("There is no schema or DTD associated with the document.", URLs.toExternalForm(file.toURI().toURL()), null, 0, 0));
      return;
    }

    final ValidationHandler handler = new ValidationHandler(schemaReferences, errorHandler);
    if (xmlDocument.isXSD()) {
      final SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/XML/XMLSchema/v1.1");
      factory.setResourceResolver(handler);
      factory.setErrorHandler(handler);

      factory.newSchema(new StreamSource(file));
    }
    else {
      final javax.xml.validation.Validator validator = factory.newSchema().newValidator();
      validator.setResourceResolver(handler);
      validator.setErrorHandler(handler);

      validator.validate(new StreamSource(file));
    }

    for (final Map.Entry<String,SchemaLocation> schemaLocation : schemaReferences.entrySet()) {
      final Map<String,CachedURL> locations = schemaLocation.getValue().getLocation();
      for (final Map.Entry<String,CachedURL> location : locations.entrySet())
        location.getValue().destroy();
    }

    if (handler.getErrors() != null) {
      final Iterator<SAXParseException> iterator = handler.getErrors().iterator();
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