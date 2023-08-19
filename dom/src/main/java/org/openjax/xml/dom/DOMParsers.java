/* Copyright (c) 2006 OpenJAX
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

package org.openjax.xml.dom;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.libj.net.URLConnections;
import org.libj.util.StringPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public final class DOMParsers {
  private static final Logger logger = LoggerFactory.getLogger(DOMParsers.class);

  private static final ErrorHandler errorHandler = new ErrorHandler() {
    // ignore fatal errors (an exception is guaranteed)
    @Override
    public void fatalError(final SAXParseException exception) {
    }

    // treat validation errors as fatal
    @Override
    public void error(final SAXParseException e) throws SAXParseException {
      if (logger.isErrorEnabled()) logger.error("[" + e.getLineNumber() + "," + e.getColumnNumber() + "]" + (e.getSystemId() != null ? " systemId=\"" + e.getSystemId() + "\"" : ""));
      throw e;
    }

    // dump warnings too
    @Override
    public void warning(final SAXParseException e) {
      final String message = e.getMessage() != null ? " " + e.getMessage() : "";
      if (logger.isWarnEnabled()) logger.warn("[" + e.getLineNumber() + "," + e.getColumnNumber() + "] systemId=\"" + e.getSystemId() + "\"" + message);
    }
  };

  public static DocumentBuilder newDocumentBuilder() {
    final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    documentBuilderFactory.setNamespaceAware(true);
    documentBuilderFactory.setIgnoringComments(true);
    documentBuilderFactory.setIgnoringElementContentWhitespace(true);
    documentBuilderFactory.setValidating(false);

    try {
      final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
      documentBuilder.setErrorHandler(errorHandler);
      documentBuilder.setEntityResolver(new EntityResolver() {
        private String prevPath;

        private InputStream getInputStream(final String systemId) throws IOException {
          System.err.println(systemId);
          return URLConnections.checkFollowRedirect(new URL(systemId).openConnection()).getInputStream();
        }

        @Override
        public InputSource resolveEntity(final String publicId, final String systemId) throws SAXException, IOException {
          if (systemId == null)
            return null;

          if (!StringPaths.isAbsoluteLocal(systemId))
            prevPath = StringPaths.getCanonicalParent(systemId);

          try {
            return new InputSource(getInputStream(systemId));
          }
          catch (final IOException e) {
            if (prevPath == null || !StringPaths.isAbsoluteLocal(systemId))
              throw e;

            return new InputSource(getInputStream(prevPath + StringPaths.getName(systemId)));
          }
        }
      });
      return documentBuilder;
    }
    catch (final ParserConfigurationException e) {
      throw new IllegalStateException(e);
    }
  }

  private DOMParsers() {
  }
}