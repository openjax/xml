/* Copyright (c) 2019 JAX-SB
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

package org.apache.xerces.jaxp.datatype;

public class Duration extends DurationImpl {
  public static String print(final Duration duration) {
    return duration == null ? null : duration.toString();
  }

  public static Duration parse(final String string) {
    return string == null ? null : new Duration(string);
  }

  protected Duration() {
    this(false, 0, 0, 0, 0, 0, 0);
  }

  protected Duration(final long durationInMilliSeconds) {
    super(durationInMilliSeconds);
  }

  protected Duration(final String lexicalRepresentation) {
    super(lexicalRepresentation);
  }

  public Duration(final boolean isPositive, final int years) {
    super(isPositive, years, 0, 0, 0, 0, 0);
  }

  public Duration(final boolean isPositive, final int years, final int months) {
    super(isPositive, years, months, 0, 0, 0, 0);
  }

  public Duration(final boolean isPositive, final int years, final int months, final int days) {
    super(isPositive, years, months, days, 0, 0, 0);
  }

  public Duration(final boolean isPositive, final int years, final int months, final int days, final int hours) {
    super(isPositive, years, months, days, hours, 0, 0);
  }

  public Duration(final boolean isPositive, final int years, final int months, final int days, final int hours, final int minutes) {
    super(isPositive, years, months, days, hours, minutes, 0);
  }

  public Duration(final boolean isPositive, final int years, final int months, final int days, final int hours, final int minutes, final int seconds) {
    super(isPositive, years, months, days, hours, minutes, seconds);
  }
}