/* Copyright (c) 2019 OpenJAX
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

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * An {@link ErrorHandler} that produces no output.
 */
public class SilentErrorHandler implements ErrorHandler {
  /**
   * Silently consumes notification of a warning.
   */
  @Override
  public void warning(final SAXParseException exception) throws SAXException {
  }

  /**
   * Silently consumes notification of a recoverable error.
   */
  @Override
  public void error(final SAXParseException exception) throws SAXException {
  }

  /**
   * Silently consumes notification of an unrecoverable error.
   */
  @Override
  public void fatalError(final SAXParseException exception) throws SAXException {
  }
}