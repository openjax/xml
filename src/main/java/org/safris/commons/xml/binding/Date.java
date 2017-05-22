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

import java.time.LocalDateTime;
import java.time.ZoneOffset;
/**
 * http://www.w3.org/TR/xmlschema11-2/#date
 */
import java.util.TimeZone;

public final class Date {
  public static Date parseDate(String string) {
    if (string == null)
      throw new NullPointerException("string == null");

    string = string.trim();
    if (string.length() < DATE_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException("date == " + string);

    final Date date = parseDateFrag(string);
    int index = string.indexOf("Z", DATE_FRAG_MIN_LENGTH);
    if (index == -1)
      index = string.indexOf("-", DATE_FRAG_MIN_LENGTH);

    if (index == -1)
      index = string.indexOf("+", DATE_FRAG_MIN_LENGTH);

    final TimeZone timeZone;
    if (index != -1)
      timeZone = Time.parseTimeZoneFrag(string.substring(index));
    else
      timeZone = null;

    return new Date(date.getYear(), date.getMonth(), date.getDay(), timeZone);
  }

  protected static Date parseDateFrag(String string) {
    if (string == null)
      throw new NullPointerException("string == null");

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
  private final TimeZone timeZone;
  private final long epochTime;

  protected Date(final YearMonth yearMonth, final Day day, final TimeZone timeZone) {
    if (yearMonth == null)
      throw new NullPointerException("yearMonth == null");

    if (day == null)
      throw new NullPointerException("day == null");

    this.yearMonth = yearMonth;
    this.day = day;
    this.timeZone = timeZone != null ? timeZone : TimeZone.getDefault();

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

    if (!(obj instanceof Date))
      return false;

    final Date that = (Date)obj;
    return (yearMonth != null ? yearMonth.equals(that.yearMonth) : that.yearMonth == null) && (day != null ? day.equals(that.day) : that.day == null) && (timeZone != null ? timeZone.equals(that.timeZone) : that.timeZone == null);
  }

  @Override
  public int hashCode() {
    return (yearMonth != null ? yearMonth.hashCode() : -1) + (day != null ? day.hashCode() : -1) + (timeZone != null ? timeZone.hashCode() : -1);
  }

  protected String toEmbededString() {
    final StringBuffer buffer = new StringBuffer();
    buffer.append(yearMonth.toEmbededString());
    buffer.append("-");
    if (getDay() < 10)
      buffer.append("0").append(getDay());
    else
      buffer.append(getDay());

    return buffer.toString();
  }

  @Override
  public String toString() {
    return new StringBuffer(toEmbededString()).append(Time.formatTimeZone(timeZone)).toString();
  }
}