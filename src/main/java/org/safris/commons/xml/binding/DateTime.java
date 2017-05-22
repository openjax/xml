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
import java.util.TimeZone;

public final class DateTime {
  public static DateTime parseDateTime(String string) {
    if (string == null)
      throw new NullPointerException("string == null");

    string = string.trim();
    if (string.length() < Year.YEAR_FRAG_MIN_LENGTH + 1 + Month.MONTH_FRAG_MIN_LENGTH + 1 + Day.DAY_FRAG_MIN_LENGTH + 1 + Time.HOUR_FRAG_MIN_LENGTH + 1 + Time.MINUTE_FRAG_MIN_LENGTH + 1 + Time.SECOND_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException(string);

    final Date date = Date.parseDateFrag(string);
    final int index = string.indexOf("T", Date.DATE_FRAG_MIN_LENGTH);
    if (index == -1)
      throw new IllegalArgumentException("dateTime == " + string);

    final Time time = Time.parseTime(string.substring(index + 1));
    return new DateTime(date, time);
  }

  protected static final TimeZone GMT = TimeZone.getTimeZone("GMT");

  private final Date date;
  private final Time time;
  private final long epochTime;

  protected DateTime(final Date date, final Time time) {
    if (date == null)
      throw new NullPointerException("date == null");

    if (time == null)
      throw new NullPointerException("time == null");

    this.date = date;
    this.time = time;
    this.epochTime = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDay(), time.getHour(), time.getMinute(), (int)time.getSecond(), (int)((time.getSecond() - (int)time.getSecond()) * 1000000000)).toEpochSecond(ZoneOffset.ofTotalSeconds(getTimeZone().getRawOffset() / 1000));
  }

  public DateTime(final int year, final int month, int day, final int hour, int minute, final float second, final TimeZone timeZone) {
    this(new Date(year, month, day, timeZone), new Time(hour, minute, second, timeZone));
  }

  public DateTime(final int year, final int month, int day, final int hour, int minute, final float second) {
    this(year, month, day, hour, minute, second, null);
  }

  public DateTime(final long time, final TimeZone timeZone) {
    this(new Date(time, timeZone), new Time(time, timeZone));
  }

  public DateTime(final long time) {
    this(new Date(time), new Time(time));
  }

  public DateTime() {
    this(System.currentTimeMillis());
  }

  public int getYear() {
    return date.getYear();
  }

  public int getMonth() {
    return date.getMonth();
  }

  public int getDay() {
    return date.getDay();
  }

  public int getHour() {
    return time.getHour();
  }

  public int getMinute() {
    return time.getMinute();
  }

  public float getSecond() {
    return time.getSecond();
  }

  public TimeZone getTimeZone() {
    return time.getTimeZone();
  }

  public long getTime() {
    return epochTime;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof DateTime))
      return false;

    final DateTime that = (DateTime)obj;
    return (date != null ? date.equals(that.date) : that.date == null) && (time != null ? time.equals(that.time) : that.time == null);
  }

  @Override
  public int hashCode() {
    return (date != null ? date.hashCode() : -1) + (time != null ? time.hashCode() : -1);
  }

  protected String toEmbededString() {
    final StringBuffer buffer = new StringBuffer();
    if (date != null)
      buffer.append(date.toEmbededString());

    buffer.append("T");
    if (time != null)
      buffer.append(time.toEmbededString());

    return buffer.toString();
  }

  @Override
  public String toString() {
    return new StringBuffer(toEmbededString()).append(Time.formatTimeZone(getTimeZone())).toString();
  }
}