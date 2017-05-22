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

import java.util.Calendar;
import java.util.TimeZone;

import org.safris.commons.util.CalendarUtil;

/**
 * http://www.w3.org/TR/xmlschema11-2/#gYear
 */
public final class Year {
  public static Year parseYear(String string) {
    if (string == null)
      throw new NullPointerException("string == null");

    string = string.trim();
    if (string.length() < YEAR_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException("year == " + string);

    final int year = parseYearFrag(string);
    int index = string.indexOf("Z", YEAR_FRAG_MIN_LENGTH);
    if (index == -1)
      index = string.indexOf("-", YEAR_FRAG_MIN_LENGTH);

    if (index == -1)
      index = string.indexOf("+", YEAR_FRAG_MIN_LENGTH);

    final TimeZone timeZone;
    if (index != -1)
      timeZone = Time.parseTimeZoneFrag(string.substring(index));
    else
      timeZone = null;

    return new Year(year, timeZone);
  }

  protected static int parseYearFrag(String string) {
    if (string == null)
      throw new NullPointerException("string == null");

    if (string.length() == 0)
      throw new IllegalArgumentException(string);

    int index = string.indexOf("Z", YEAR_FRAG_MIN_LENGTH);
    if (index != -1)
      string = string.substring(0, index);

    index = string.indexOf("-", YEAR_FRAG_MIN_LENGTH);
    if (index != -1)
      string = string.substring(0, index);

    index = string.indexOf("+", YEAR_FRAG_MIN_LENGTH);
    if (index != -1)
      string = string.substring(0, index);

    if (string.length() < YEAR_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException(string);

    try {
      return Integer.parseInt(string);
    }
    catch (final NumberFormatException e) {
      throw new IllegalArgumentException(string, e);
    }
  }

  protected static final int YEAR_FRAG_MIN_LENGTH = 4;

  private final int year;
  private final TimeZone timeZone;
  private final long epochTime;

  @SuppressWarnings("deprecation")
  public Year(final int year, final TimeZone timeZone) {
    this.year = year;
    this.timeZone = timeZone != null ? timeZone : TimeZone.getDefault();
    epochTime = java.util.Date.UTC(year - 1900, 0, 1, 0, 0, 0) - getTimeZone().getRawOffset() - getTimeZone().getDSTSavings();
  }

  public Year(final int year) {
    this(year, null);
  }

  public Year(final long time, final TimeZone timeZone) {
    this(CalendarUtil.newCalendar(time, timeZone).get(Calendar.YEAR), null);
  }

  public Year(final long time) {
    this(CalendarUtil.newCalendar(time).get(Calendar.YEAR), null);
  }

  public Year() {
    this(System.currentTimeMillis());
  }

  public int getYear() {
    return year;
  }

  public TimeZone getTimeZone() {
    return timeZone;
  }

  public long getTime() {
    return epochTime;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Year))
      return false;

    final Year that = (Year)obj;
    return this.year == that.year && (timeZone != null ? timeZone.equals(that.timeZone) : that.timeZone == null);
  }

  @Override
  public int hashCode() {
    return year ^ 5 + (timeZone != null ? timeZone.hashCode() : -1);
  }

  protected String toEmbededString() {
    final StringBuffer buffer = new StringBuffer();
    if (year < 10)
      buffer.append("000").append(year);
    else if (year < 100)
      buffer.append("00").append(year);
    else if (year < 1000)
      buffer.append("0").append(year);
    else
      buffer.append(year);

    return buffer.toString();
  }

  @Override
  public String toString() {
    return new StringBuffer(toEmbededString()).append(Time.formatTimeZone(timeZone)).toString();
  }
}