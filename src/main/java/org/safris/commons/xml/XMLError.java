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

package org.safris.commons.xml;

import java.lang.reflect.InvocationTargetException;

import org.safris.commons.lang.Throwables;

public class XMLError extends Error {
  private static final long serialVersionUID = 2679754040056450837L;

  public XMLError() {
    super();
  }

  public XMLError(final String message) {
    super(message);
  }

  public XMLError(final Throwable cause) {
    if (cause instanceof InvocationTargetException)
      Throwables.set(this, cause.getCause().getMessage(), cause.getCause());
    else
      Throwables.set(this, cause.getMessage(), cause);
  }

  public XMLError(final String message, final Throwable cause) {
    if (cause instanceof InvocationTargetException)
      Throwables.set(this, message != null ? message : cause.getCause().getMessage(), cause.getCause());
    else
      Throwables.set(this, cause.getMessage(), cause);
  }
}