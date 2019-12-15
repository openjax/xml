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

import java.io.Serializable;
import java.util.TimeZone;

/**
 * http://www.w3.org/TR/xmlschema11-2/#gDay
 */
public class Day extends TemporalType implements Serializable {
  private static final long serialVersionUID = -2605382792284795205L;

  public static String print(final Day day) {
    return day == null ? null : day.toString();
  }

  public static Day parse(String string) {
    if (string == null)
      return null;

    string = string.trim();
    if (!string.startsWith(PAD_FRAG) || string.length() < PAD_FRAG.length() + DAY_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException("day == " + string);

    final int day = parseDayFrag(string.substring(PAD_FRAG.length()));
    final TimeZone timeZone = Time.parseTimeZoneFrag(string.substring(PAD_FRAG.length() + DAY_FRAG_MIN_LENGTH));
    return new Day(day, timeZone);
  }

  protected static int parseDayFrag(final String string) {
    if (string.length() < DAY_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException("day == " + string);

    final char ch = string.charAt(0);
    final char ch2 = string.charAt(1);
    if (ch == '0') {
      if (ch2 < '1' || '9' < ch2)
        throw new IllegalArgumentException("day == " + string);
    }
    else if (ch == '1' || ch == '2') {
      if (ch2 < '0' || '9' < ch2)
        throw new IllegalArgumentException("day == " + string);
    }
    else if (ch == '3') {
      if (ch2 < '0' || '1' < ch2)
        throw new IllegalArgumentException("day == " + string);
    }
    else {
      throw new IllegalArgumentException("day == " + string);
    }

    try {
      return Integer.parseInt("" + ch + ch2);
    }
    catch (final NumberFormatException e) {
      throw new IllegalArgumentException(string, e);
    }
  }

  protected static final int DAY_FRAG_MIN_LENGTH = 2;
  private static final String PAD_FRAG = "---";

  private final int day;

  public Day(final int day, final TimeZone timeZone) {
    super(timeZone);
    this.day = day;
    if (day < 0 || 31 < day)
      throw new IllegalArgumentException("day == " + day);
  }

  public Day(final int day) {
    this(day, null);
  }

  @SuppressWarnings("deprecation")
  public Day(final long time) {
    super(TimeZone.getDefault());
    final java.util.Date date = new java.util.Date(time);
    this.day = date.getDate();
  }

  public Day() {
    this(System.currentTimeMillis());
  }

  public int getDay() {
    return day;
  }

  @Override
  protected String toEmbededString() {
    return (day < 10 ? "---0" : "---") + day;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Day))
      return false;

    final Day that = (Day)obj;
    return super.equals(obj) && this.day == that.day;
  }

  @Override
  public int hashCode() {
    return 31 * super.hashCode() + day;
  }
}