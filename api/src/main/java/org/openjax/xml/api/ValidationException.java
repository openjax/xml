/* Copyright (c) 2008 OpenJAX
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

package org.openjax.xml.api;

import org.xml.sax.SAXException;

/**
 * A {@link SAXException} that signifies a validation exception.
 */
public class ValidationException extends SAXException {
  private static final long serialVersionUID = -5730844996681538725L;

  /**
   * Creates a new {@link ValidationException}.
   */
  public ValidationException() {
    super();
  }

  /**
   * Creates a new {@link ValidationException} with the specified detail
   * message.
   *
   * @param message The detail message.
   */
  public ValidationException(final String message) {
    super(message);
  }

  /**
   * Creates a new {@link ValidationException} with the specified cause.
   *
   * @param cause The cause.
   */
  public ValidationException(final Exception cause) {
    super(cause);
  }

  /**
   * Creates a new {@link ValidationException} with the specified detail message
   * and cause.
   *
   * @param message The detail message.
   * @param cause The cause.
   */
  public ValidationException(final String message, final Exception cause) {
    super(message, cause);
  }
}