/* Copyright (c) 2006 lib4j
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

package org.lib4j.xml.binding;

import java.io.Serializable;
import java.util.TimeZone;

/**
 * http://www.w3.org/TR/xmlschema11-2/#gMonth
 */
public final class Month implements Serializable {
  private static final long serialVersionUID = 9191134240521332696L;

  public static Month parseMonth(String string) {
    if (string == null)
      throw new NullPointerException("string == null");

    string = string.trim();
    if (!string.startsWith(PAD_FRAG) || string.length() < PAD_FRAG.length() + MONTH_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException(string);

    final int month = parseMonthFrag(string.substring(PAD_FRAG.length()));
    final TimeZone timeZone = Time.parseTimeZoneFrag(string.substring(PAD_FRAG.length() + MONTH_FRAG_MIN_LENGTH));
    return new Month(month, timeZone);
  }

  protected static int parseMonthFrag(final String string) {
    if (string == null)
      throw new NullPointerException("string == null");

    if (string.length() < MONTH_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException("month == " + string);

    int index = 0;
    final char ch = string.charAt(index);
    final char ch2 = string.charAt(++index);
    if (ch == '0') {
      if (ch2 < '1' || '9' < ch2)
        throw new IllegalArgumentException("month == " + string);
    }
    else if (ch == '1') {
      if (ch2 < '0' || '2' < ch2)
        throw new IllegalArgumentException("month == " + string);
    }
    else
      throw new IllegalArgumentException("month == " + string);


    final String monthString = "" + ch + ch2;
    int month;
    try {
      month = Integer.parseInt(monthString);
    }
    catch (final NumberFormatException e) {
      throw new IllegalArgumentException("month == " + string, e);
    }

    return month;
  }

  protected static final int MONTH_FRAG_MIN_LENGTH = 2;
  private static final String PAD_FRAG = "--";

  private final int month;
  private final TimeZone timeZone;

  public Month(final int month, final TimeZone timeZone) {
    this.month = month;
    if (month < 0 || 12 < month)
      throw new IllegalArgumentException("month == " + month);

    this.timeZone = timeZone != null ? timeZone : TimeZone.getDefault();
  }

  public Month(final int month) {
    this(month, null);
  }

  @SuppressWarnings("deprecation")
  public Month(final long time) {
    final java.util.Date date = new java.util.Date(time);
    this.month = date.getMonth() + 1;
    this.timeZone = TimeZone.getDefault();
  }

  public Month() {
    this(System.currentTimeMillis());
  }

  public int getMonth() {
    return month;
  }

  public TimeZone getTimeZone() {
    return timeZone;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof Month))
      return false;

    final Month that = (Month)obj;
    return this.month == that.month && (timeZone != null ? timeZone.equals(that.timeZone) : that.timeZone == null);
  }

  @Override
  public int hashCode() {
    return month ^ 13 + (timeZone != null ? timeZone.hashCode() : -1);
  }

  protected String toEmbededString() {
    final StringBuffer buffer = new StringBuffer();
    if (month < 10)
      buffer.append("--0").append(month);
    else
      buffer.append("--").append(month);

    return buffer.toString();
  }

  @Override
  public String toString() {
    return new StringBuffer(toEmbededString()).append(Time.formatTimeZone(timeZone)).toString();
  }
}