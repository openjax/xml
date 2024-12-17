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

import java.util.Arrays;
import java.util.TimeZone;

/**
 * http://www.w3.org/TR/xmlschema11-2/#gMonthDay
 */
public class MonthDay extends TemporalType {
  public static String print(final MonthDay monthDay) {
    return monthDay == null ? null : monthDay.toString();
  }

  public static MonthDay parse(String string) {
    if (string == null)
      return null;

    string = string.trim();
    if (!string.startsWith(PAD_FRAG) || string.length() < PAD_FRAG.length() + MONTH_DAY_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException("month-day == " + string);

    final MonthDay monthDay = parseMonthDayFrag(string = string.substring(PAD_FRAG.length()));
    if (MONTH_DAY_FRAG_MIN_LENGTH >= string.length())
      return monthDay;

    final TimeZone timeZone = Time.parseTimeZoneFrag(string.substring(MONTH_DAY_FRAG_MIN_LENGTH));
    return new MonthDay(monthDay.getMonth(), monthDay.getDay(), timeZone);
  }

  protected static MonthDay parseMonthDayFrag(String string) {
    if (string.length() < MONTH_DAY_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException("month-day == " + string);

    final int month = Month.parseMonthFrag(string);
    final int day = Day.parseDayFrag(string.substring(Month.MONTH_FRAG_MIN_LENGTH + 1));
    if (month == 2 && 29 < day)
      throw new IllegalArgumentException("month == " + month + " day == " + day);

    if (Arrays.binarySearch(LONG_MONTHS, month) < 0 && 30 < day)
      throw new IllegalArgumentException("month == " + month + " day == " + day);

    return new MonthDay(month, day);
  }

  protected static final int MONTH_DAY_FRAG_MIN_LENGTH = Month.MONTH_FRAG_MIN_LENGTH + 1 + Day.DAY_FRAG_MIN_LENGTH;
  private static final int[] LONG_MONTHS = {1, 3, 5, 7, 8, 10, 12};
  private static final String PAD_FRAG = "--";

  private final Month month;
  private final Day day;

  public MonthDay(final int month, final int day, final TimeZone timeZone) {
    this(new Month(month), new Day(day), timeZone);
  }

  protected MonthDay(final Month month, final Day day, final TimeZone timeZone) {
    super(timeZone);
    this.month = month;
    this.day = day;
    if (month.getMonth() == 2 && 29 < day.getDay())
      throw new IllegalArgumentException("month == " + month + " day == " + day);

    if (Arrays.binarySearch(LONG_MONTHS, month.getMonth()) < 0 && 30 < day.getDay())
      throw new IllegalArgumentException("month == " + month + " day == " + day);
  }

  public MonthDay(final int month, final int day) {
    this(month, day, null);
  }

  public MonthDay(final long timeMs) {
    this(new Month(timeMs), new Day(timeMs), null);
  }

  public MonthDay() {
    this(System.currentTimeMillis());
  }

  public int getMonth() {
    return month.getMonth();
  }

  public int getDay() {
    return day.getDay();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof MonthDay && super.equals(obj)))
      return false;

    final MonthDay that = (MonthDay)obj;
    return month.equals(that.month) && day.equals(that.day);
  }

  @Override
  public int hashCode() {
    int hashCode = super.hashCode();
    hashCode = 31 * hashCode + month.hashCode();
    hashCode = 31 * hashCode + day.hashCode();
    return hashCode;
  }

  @Override
  protected StringBuilder toEmbeddedString(final StringBuilder b) {
    month.toEmbeddedString(b).append('-');
    final int day = getDay();
    if (day < 10)
      b.append('0');

    b.append(day);
    return b;
  }
}