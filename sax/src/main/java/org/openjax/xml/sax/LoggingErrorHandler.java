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
import org.slf4j.event.Level;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class LoggingErrorHandler implements ErrorHandler {
  private static final Logger logger = LoggerFactory.getLogger(LoggingErrorHandler.class);

  private static void log(final String message, final SAXParseException e, final Level level) {
    if (level == null)
      return;

    if (level == Level.TRACE)
      logger.trace(message, e);
    else if (level == Level.DEBUG)
      logger.debug(message, e);
    else if (level == Level.INFO)
      logger.info(message, e);
    else if (level == Level.WARN)
      logger.warn(message, e);
    else if (level == Level.ERROR)
      logger.error(message, e);
    else
      throw new UnsupportedOperationException("Unsupported log level: " + level);
  }

  private final Level warn;
  private final Level error;
  private final Level fatalError;

  public LoggingErrorHandler(final Level warn, final Level error, final Level fatalError) {
    this.warn = warn;
    this.error = error;
    this.fatalError = fatalError;
  }

  public LoggingErrorHandler() {
    this(Level.WARN, Level.ERROR, Level.ERROR);
  }

  @Override
  public void warning(final SAXParseException exception) throws SAXException {
    final String message = exception.getMessage() + " (" + exception.getLineNumber() + "," + exception.getColumnNumber() + ")";
    if (exception.getMessage() != null && exception.getMessage().startsWith("schema_reference.4")) {
      log(message, null, error);
      throw exception;
    }

    log(message, null, warn);
  }

  @Override
  public void error(final SAXParseException exception) throws SAXException {
    log(exception.getMessage() + " (" + exception.getLineNumber() + "," + exception.getColumnNumber() + ")", null, error);
  }

  @Override
  public void fatalError(final SAXParseException exception) throws SAXException {
    log(exception.getMessage() + " (" + exception.getLineNumber() + "," + exception.getColumnNumber() + ")", null, fatalError);
  }
}