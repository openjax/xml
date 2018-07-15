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

package org.lib4j.xml.datatype;

import java.io.Serializable;
import java.util.TimeZone;

/**
 * http://www.w3.org/TR/xmlschema11-2/#gYearMonth
 */
public class YearMonth extends TemporalType implements Serializable {
  private static final long serialVersionUID = -5629415172932276877L;

  public static String print(final YearMonth yearMonth) {
    return yearMonth == null ? null : yearMonth.toString();
  }

  public static YearMonth parse(String string) {
    if (string == null)
      return null;

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

    final TimeZone timeZone = index == -1 ? null : Time.parseTimeZoneFrag(string.substring(index));
    return new YearMonth(year, month, timeZone);
  }

  protected static final int YEAR_MONTH_FRAG_MIN_LENGTH = Year.YEAR_FRAG_MIN_LENGTH + 1 + Month.MONTH_FRAG_MIN_LENGTH;

  private final Year year;
  private final Month month;
  private final long epochTime;

  @SuppressWarnings("deprecation")
  protected YearMonth(final Year year, final Month month, final TimeZone timeZone) {
    super(timeZone);
    if (year == null)
      throw new IllegalArgumentException("year == null");

    if (month == null)
      throw new IllegalArgumentException("month == null");

    this.year = year;
    this.month = month;
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

  public long getTime() {
    return epochTime;
  }

  @Override
  protected String toEmbededString() {
    final StringBuilder builder = new StringBuilder();
    builder.append(year.toEmbededString()).append('-');
    if (getMonth() < 10)
      builder.append('0');

    builder.append(getMonth());
    return builder.toString();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof YearMonth))
      return false;

    final YearMonth that = (YearMonth)obj;
    return super.equals(obj) && (year != null ? year.equals(that.year) : that.year == null) && (month != null ? month.equals(that.month) : that.month == null);
  }

  @Override
  public int hashCode() {
    return super.hashCode() + (year != null ? year.hashCode() : -1) + (month != null ? month.hashCode() : -1);
  }
}