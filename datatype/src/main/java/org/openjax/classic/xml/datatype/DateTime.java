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

package org.openjax.classic.xml.datatype;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

public class DateTime extends TemporalType implements Serializable {
  private static final long serialVersionUID = 7756729079060501414L;

  public static String print(final DateTime dateTime) {
    return dateTime == null ? null : dateTime.toString();
  }

  public static DateTime parse(String string) {
    if (string == null)
      return null;

    string = string.trim();
    if (string.length() < Year.YEAR_FRAG_MIN_LENGTH + 1 + Month.MONTH_FRAG_MIN_LENGTH + 1 + Day.DAY_FRAG_MIN_LENGTH + 1 + Time.HOUR_FRAG_MIN_LENGTH + 1 + Time.MINUTE_FRAG_MIN_LENGTH + 1 + Time.SECOND_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException(string);

    final int index = string.indexOf("T", Date.DATE_FRAG_MIN_LENGTH);
    if (index == -1)
      throw new IllegalArgumentException("dateTime == " + string);

    return new DateTime(Date.parseDateFrag(string), Time.parse(string.substring(index + 1)));
  }

  protected static final TimeZone GMT = TimeZone.getTimeZone("GMT");

  private final Date date;
  private final Time time;
  private final long epochTime;

  protected DateTime(final Date date, final Time time) {
    super(time.getTimeZone());
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

  public long getTime() {
    return epochTime;
  }

  @Override
  protected String toEmbededString() {
    final StringBuilder builder = new StringBuilder();
    if (date != null)
      builder.append(date.toEmbededString());

    builder.append('T');
    if (time != null)
      builder.append(time.toEmbededString());

    return builder.toString();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof DateTime))
      return false;

    final DateTime that = (DateTime)obj;
    return date.equals(that.date) && time.equals(that.time);
  }

  @Override
  public int hashCode() {
    return date.hashCode() ^ 3 + time.hashCode() ^ 7;
  }
}