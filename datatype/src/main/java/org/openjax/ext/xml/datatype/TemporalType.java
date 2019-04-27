/* Copyright (c) 2018 OpenJAX
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

package org.openjax.ext.xml.datatype;

import java.util.TimeZone;

public abstract class TemporalType {
  protected final TimeZone timeZone;

  protected TemporalType(final TimeZone timeZone) {
    this.timeZone = timeZone == null ? TimeZone.getDefault() : timeZone;
  }

  protected abstract String toEmbededString();

  public final TimeZone getTimeZone() {
    return timeZone;
  }

  @Override
  public final String toString() {
    return toEmbededString() + Time.formatTimeZone(timeZone);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof TemporalType))
      return false;

    final TemporalType that = (TemporalType)obj;
    return timeZone != null ? timeZone.equals(that.timeZone) : that.timeZone == null;
  }

  @Override
  public int hashCode() {
    return timeZone.hashCode();
  }
}