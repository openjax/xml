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

package org.safris.commons.xml.binding;

import java.util.TimeZone;

/**
 * http://www.w3.org/TR/xmlschema11-2/#gDay
 */
public final class Day {
  public static Day parseDay(String string) {
    if (string == null)
      throw new NullPointerException("string == null");

    string = string.trim();
    if (!string.startsWith(PAD_FRAG) || string.length() < PAD_FRAG.length() + DAY_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException("day == " + string);

    final int day = parseDayFrag(string.substring(PAD_FRAG.length()));
    final TimeZone timeZone = Time.parseTimeZoneFrag(string.substring(PAD_FRAG.length() + DAY_FRAG_MIN_LENGTH));
    return new Day(day, timeZone);
  }

  protected static int parseDayFrag(final String string) {
    if (string == null)
      throw new NullPointerException("string == null");

    if (string.length() < DAY_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException("day == " + string);

    int index = 0;
    final char ch = string.charAt(index);
    final char ch2 = string.charAt(++index);
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
    else
      throw new IllegalArgumentException("day == " + string);

    final String dayString = "" + ch + ch2;
    int day;
    try {
      day = Integer.parseInt(dayString);
    }
    catch (final NumberFormatException e) {
      throw new IllegalArgumentException(string, e);
    }

    return day;
  }

  protected static final int DAY_FRAG_MIN_LENGTH = 2;
  private static final String PAD_FRAG = "---";

  private final int day;
  private final TimeZone timeZone;

  public Day(final int day, final TimeZone timeZone) {
    this.day = day;
    if (day < 0 || 31 < day)
      throw new IllegalArgumentException("day == " + day);

    this.timeZone = timeZone != null ? timeZone : TimeZone.getDefault();
  }

  public Day(final int day) {
    this(day, null);
  }

  @SuppressWarnings("deprecation")
  public Day(final long time) {
    final java.util.Date date = new java.util.Date(time);
    this.day = date.getDate();
    this.timeZone = TimeZone.getDefault();
  }

  public Day() {
    this(System.currentTimeMillis());
  }

  public int getDay() {
    return day;
  }

  public TimeZone getTimeZone() {
    return timeZone;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Day))
      return false;

    final Day that = (Day)obj;
    return this.day == that.day && (timeZone != null ? timeZone.equals(that.timeZone) : that.timeZone == null);
  }

  @Override
  public int hashCode() {
    return day ^ 17 + (timeZone != null ? timeZone.hashCode() : -1);
  }

  protected String toEmbededString() {
    final StringBuffer string = new StringBuffer();
    if (day < 10)
      string.append("---0").append(day);
    else
      string.append("---").append(day);

    return string.toString();
  }

  @Override
  public String toString() {
    return new StringBuffer(toEmbededString()).append(Time.formatTimeZone(timeZone)).toString();
  }
}