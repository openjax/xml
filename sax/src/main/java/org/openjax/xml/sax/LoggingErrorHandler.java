/* Copyright (c) 2016 OpenJAX
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

package org.openjax.xml.sax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * Implementation of the {@link ErrorHandler} interface that submits log events to a SLF4J {@link Logger} corresponding to the
 * {@link LoggingErrorHandler} class.
 */
public class LoggingErrorHandler implements ErrorHandler {
  private static final Logger logger = LoggerFactory.getLogger(LoggingErrorHandler.class);

  @Override
  public void warning(final SAXParseException exception) throws SAXParseException {
    final String message = exception.getMessage() + " (" + exception.getLineNumber() + "," + exception.getColumnNumber() + ")";
    if (exception.getMessage() != null && exception.getMessage().startsWith("schema_reference.4")) {
      if (logger.isErrorEnabled()) logger.error(message);
      throw exception;
    }

    if (logger.isWarnEnabled()) logger.warn(message);
  }

  @Override
  public void error(final SAXParseException exception) {
    if (logger.isErrorEnabled()) logger.error(exception.getMessage() + " (" + exception.getLineNumber() + "," + exception.getColumnNumber() + ")");
  }

  @Override
  public void fatalError(final SAXParseException exception) {
    if (logger.isErrorEnabled()) logger.error(exception.getMessage() + " (" + exception.getLineNumber() + "," + exception.getColumnNumber() + ")");
  }
}