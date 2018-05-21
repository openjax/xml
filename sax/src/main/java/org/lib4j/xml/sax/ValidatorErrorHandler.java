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

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ValidatorErrorHandler implements ErrorHandler {
  private final ErrorHandler errorHandler;
  private List<SAXParseException> errors;

  public ValidatorErrorHandler(final ErrorHandler handler) {
    this.errorHandler = handler;
  }

  public List<SAXParseException> getErrors() {
    return errors;
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
  public void fatalError(final SAXParseException e) throws SAXException {
    if (errorHandler != null)
      errorHandler.fatalError(e);
  }

  @Override
  public void warning(final SAXParseException e) throws SAXException {
    if (errorHandler != null)
      errorHandler.warning(e);
  }
}