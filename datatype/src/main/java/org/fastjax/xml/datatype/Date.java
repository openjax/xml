/* Copyright (c) 2006 FastJAX
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

package org.fastjax.xml.datatype;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

/**
 * http://www.w3.org/TR/xmlschema11-2/#date
 */
public class Date extends TemporalType implements Serializable {
  private static final long serialVersionUID = -9016233681424543761L;

  public static String print(final Date date) {
    return date == null ? null : date.toString();
  }

  public static Date parse(String string) {
    if (string == null)
      return null;

    string = string.trim();
    if (string.length() < DATE_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException("date == " + string);

    final Date date = parseDateFrag(string);
    int index = string.indexOf("Z", DATE_FRAG_MIN_LENGTH);
    if (index == -1)
      index = string.indexOf("-", DATE_FRAG_MIN_LENGTH);

    if (index == -1)
      index = string.indexOf("+", DATE_FRAG_MIN_LENGTH);

    final TimeZone timeZone = index == -1 ? null : Time.parseTimeZoneFrag(string.substring(index));
    return new Date(date.getYear(), date.getMonth(), date.getDay(), timeZone);
  }

  protected static Date parseDateFrag(String string) {
    if (string.length() < DATE_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException("date == " + string);

    final int year = Year.parseYearFrag(string);
    final int index = string.indexOf("-", Year.YEAR_FRAG_MIN_LENGTH);
    final MonthDay monthDay = MonthDay.parseMonthDayFrag(string = string.substring(index + 1));
    if (year % 4 != 0 && monthDay.getMonth() == 2 && monthDay.getDay() == 29)
      throw new IllegalArgumentException("year == " + year + " month == " + monthDay.getMonth() + " day == " + monthDay.getDay());

    return new Date(year, monthDay.getMonth(), monthDay.getDay());
  }

  protected static final int DATE_FRAG_MIN_LENGTH = Year.YEAR_FRAG_MIN_LENGTH + 1 + Month.MONTH_FRAG_MIN_LENGTH + 1 + Day.DAY_FRAG_MIN_LENGTH;
  private final YearMonth yearMonth;
  private final Day day;
  private final long epochTime;

  protected Date(final YearMonth yearMonth, final Day day, final TimeZone timeZone) {
    super(timeZone);
    this.yearMonth = yearMonth;
    this.day = day;
    this.epochTime = LocalDateTime.of(yearMonth.getYear(), yearMonth.getMonth(), day.getDay(), 0, 0, 0).toEpochSecond(ZoneOffset.ofTotalSeconds(getTimeZone().getRawOffset() / 1000));
  }

  public Date(final int year, final int month, int day, final TimeZone timeZone) {
    this(new YearMonth(year, month), new Day(day), timeZone);
  }

  public Date(final int year, final int month, final int day) {
    this(year, month, day, null);
  }

  public Date(final long time, final TimeZone timeZone) {
    this(new YearMonth(time, timeZone), new Day(time), null);
  }

  public Date(final long time) {
    this(new YearMonth(time), new Day(time), null);
  }

  public Date() {
    this(System.currentTimeMillis());
  }

  public int getYear() {
    return yearMonth.getYear();
  }

  public int getMonth() {
    return yearMonth.getMonth();
  }

  public int getDay() {
    return day.getDay();
  }

  public long getTime() {
    return epochTime;
  }

  @Override
  protected String toEmbededString() {
    final StringBuilder builder = new StringBuilder();
    builder.append(yearMonth.toEmbededString()).append('-');
    if (getDay() < 10)
      builder.append('0').append(getDay());
    else
      builder.append(getDay());

    return builder.toString();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Date))
      return false;

    final Date that = (Date)obj;
    return super.equals(obj) && yearMonth.equals(that.yearMonth) && day.equals(that.day);
  }

  @Override
  public int hashCode() {
    return super.hashCode() + yearMonth.hashCode() ^ 3 + day.hashCode() ^ 7;
  }
}