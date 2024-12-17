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

import java.util.Calendar;
import java.util.TimeZone;

/**
 * http://www.w3.org/TR/xmlschema11-2/#gYear
 */
public class Year extends TemporalType {
  public static String print(final Year year) {
    return year == null ? null : year.toString();
  }

  public static Year parse(String string) {
    if (string == null)
      return null;

    string = string.trim();
    if (string.length() < YEAR_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException("year == " + string);

    final int year = parseYearFrag(string);
    int index = string.indexOf('Z', YEAR_FRAG_MIN_LENGTH);
    if (index == -1)
      index = string.indexOf('-', YEAR_FRAG_MIN_LENGTH);

    if (index == -1)
      index = string.indexOf('+', YEAR_FRAG_MIN_LENGTH);

    final TimeZone timeZone = index == -1 ? null : Time.parseTimeZoneFrag(string.substring(index));
    return new Year(year, timeZone);
  }

  protected static int parseYearFrag(String string) {
    if (string.length() == 0)
      throw new IllegalArgumentException(string);

    int index = string.indexOf('Z', YEAR_FRAG_MIN_LENGTH);
    if (index != -1)
      string = string.substring(0, index);

    index = string.indexOf('-', YEAR_FRAG_MIN_LENGTH);
    if (index != -1)
      string = string.substring(0, index);

    index = string.indexOf('+', YEAR_FRAG_MIN_LENGTH);
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
  private final long epochTime;

  @SuppressWarnings("deprecation")
  public Year(final int year, final TimeZone timeZone) {
    super(timeZone);
    this.year = year;
    epochTime = java.util.Date.UTC(year - 1900, Calendar.JANUARY, 1, 0, 0, 0) - getTimeZone().getRawOffset() - getTimeZone().getDSTSavings();
  }

  public Year(final int year) {
    this(year, null);
  }

  public Year(final long timeMs, final TimeZone timeZone) {
    this(Time.newCalendar(timeMs, timeZone).get(Calendar.YEAR), null);
  }

  public Year(final long timeMs) {
    this(Time.newCalendar(timeMs).get(Calendar.YEAR), null);
  }

  public Year() {
    this(System.currentTimeMillis());
  }

  public int getYear() {
    return year;
  }

  public long getTime() {
    return epochTime;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Year && super.equals(obj)))
      return false;

    final Year that = (Year)obj;
    return this.year == that.year;
  }

  @Override
  public int hashCode() {
    return 31 * super.hashCode() + year;
  }

  @Override
  protected StringBuilder toEmbeddedString(final StringBuilder b) {
    if (year < 10)
      b.append("000").append(year);
    else if (year < 100)
      b.append("00").append(year);
    else if (year < 1000)
      b.append('0').append(year);
    else
      b.append(year);

    return b;
  }
}