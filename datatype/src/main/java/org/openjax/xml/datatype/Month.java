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

package org.openjax.xml.datatype;

import java.util.TimeZone;

/**
 * http://www.w3.org/TR/xmlschema11-2/#gMonth
 */
public class Month extends TemporalType {
  public static String print(final Month month) {
    return month == null ? null : month.toString();
  }

  public static Month parse(String string) {
    if (string == null)
      return null;

    string = string.trim();
    if (!string.startsWith(PAD_FRAG) || string.length() < PAD_FRAG.length() + MONTH_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException(string);

    final int month = parseMonthFrag(string.substring(PAD_FRAG.length()));
    final TimeZone timeZone = Time.parseTimeZoneFrag(string.substring(PAD_FRAG.length() + MONTH_FRAG_MIN_LENGTH));
    return new Month(month, timeZone);
  }

  protected static int parseMonthFrag(final String string) {
    if (string.length() < MONTH_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException("month == " + string);

    final char ch = string.charAt(0);
    final char ch2 = string.charAt(1);
    if (ch == '0') {
      if (ch2 < '1' || '9' < ch2)
        throw new IllegalArgumentException("month == " + string);
    }
    else if (ch == '1') {
      if (ch2 < '0' || '2' < ch2)
        throw new IllegalArgumentException("month == " + string);
    }
    else {
      throw new IllegalArgumentException("month == " + string);
    }

    try {
      return Integer.parseInt("" + ch + ch2);
    }
    catch (final NumberFormatException e) {
      throw new IllegalArgumentException("month == " + string, e);
    }
  }

  protected static final int MONTH_FRAG_MIN_LENGTH = 2;
  private static final String PAD_FRAG = "--";

  private final int month;

  public Month(final int month, final TimeZone timeZone) {
    super(timeZone);
    this.month = month;
    if (month < 0 || 12 < month)
      throw new IllegalArgumentException("month == " + month);
  }

  public Month(final int month) {
    this(month, null);
  }

  @SuppressWarnings("deprecation")
  public Month(final long time) {
    super(TimeZone.getDefault());
    final java.util.Date date = new java.util.Date(time);
    this.month = date.getMonth() + 1;
  }

  public Month() {
    this(System.currentTimeMillis());
  }

  public int getMonth() {
    return month;
  }

  @Override
  protected String toEmbeddedString() {
    return (month < 10 ? "--0" : "--") + month;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof Month))
      return false;

    final Month that = (Month)obj;
    return super.equals(obj) && this.month == that.month;
  }

  @Override
  public int hashCode() {
    return 31 * super.hashCode() + month;
  }
}