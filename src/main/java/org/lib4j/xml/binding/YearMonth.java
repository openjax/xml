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
 * http://www.w3.org/TR/xmlschema11-2/#gYearMonth
 */
public final class YearMonth implements Serializable {
  private static final long serialVersionUID = -5629415172932276877L;

  public static YearMonth parseYearMonth(String string) {
    if (string == null)
      throw new NullPointerException("string == null");

    string = string.trim();
    if (string.length() < YEAR_MONTH_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException("year-month == " + string);

    final int year = Year.parseYearFrag(string);
    int index = string.indexOf("-", Year.YEAR_FRAG_MIN_LENGTH);
    final int month = Month.parseMonthFrag(string.substring(index + 1));
    index = string.indexOf("Z", YEAR_MONTH_FRAG_MIN_LENGTH);
    if (index == -1)
      index = string.indexOf("-", YEAR_MONTH_FRAG_MIN_LENGTH);

    if (index == -1)
      index = string.indexOf("+", YEAR_MONTH_FRAG_MIN_LENGTH);

    final TimeZone timeZone;
    if (index != -1)
      timeZone = Time.parseTimeZoneFrag(string.substring(index));
    else
      timeZone = null;

    return new YearMonth(year, month, timeZone);
  }

  protected static final int YEAR_MONTH_FRAG_MIN_LENGTH = Year.YEAR_FRAG_MIN_LENGTH + 1 + Month.MONTH_FRAG_MIN_LENGTH;

  private final Year year;
  private final Month month;
  private final TimeZone timeZone;
  private final long epochTime;

  @SuppressWarnings("deprecation")
  protected YearMonth(final Year year, final Month month, final TimeZone timeZone) {
    if (year == null)
      throw new NullPointerException("year == null");

    if (month == null)
      throw new NullPointerException("month == null");

    this.year = year;
    this.month = month;
    this.timeZone = timeZone != null ? timeZone : TimeZone.getDefault();
    epochTime = java.util.Date.UTC(year.getYear() - 1900, month.getMonth() - 1, 1, 0, 0, 0) - getTimeZone().getRawOffset() - getTimeZone().getDSTSavings();
  }

  public YearMonth(final int year, final int month, final TimeZone timeZone) {
    this(new Year(year, timeZone), new Month(month, timeZone), timeZone);
  }

  public YearMonth(final int year, final int month) {
    this(year, month, null);
  }

  public YearMonth(final long time, final TimeZone timeZone) {
    this(new Year(time, timeZone), new Month(time), timeZone);
  }

  public YearMonth(final long time) {
    this(new Year(time), new Month(time), null);
  }

  public YearMonth() {
    this(System.currentTimeMillis());
  }

  public int getYear() {
    return year.getYear();
  }

  public int getMonth() {
    return month.getMonth();
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

    if (!(obj instanceof YearMonth))
      return false;

    final YearMonth that = (YearMonth)obj;
    return (year != null ? year.equals(that.year) : that.year == null) && (month != null ? month.equals(that.month) : that.month == null) && (timeZone != null ? timeZone.equals(that.timeZone) : that.timeZone == null);
  }

  @Override
  public int hashCode() {
    return (year != null ? year.hashCode() : -1) + (month != null ? month.hashCode() : -1) + (timeZone != null ? timeZone.hashCode() : -1);
  }

  protected String toEmbededString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append(year.toEmbededString());
    buffer.append("-");
    if (getMonth() < 10)
      buffer.append("0").append(getMonth());
    else
      buffer.append(getMonth());

    return buffer.toString();
  }

  @Override
  public String toString() {
    return new StringBuffer(toEmbededString()).append(Time.formatTimeZone(timeZone)).toString();
  }
}