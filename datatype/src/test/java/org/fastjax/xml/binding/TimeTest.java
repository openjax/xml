/* Copyright (c) 2008 FastJAX
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

package org.fastjax.xml.binding;

import static org.junit.Assert.*;

import java.util.TimeZone;

import org.fastjax.xml.datatype.Time;
import org.junit.Test;

public class TimeTest {
  @Test
  public void testTime() {
    assertNull(Time.parse(null));

    try {
      Time.parse("25:30:10Z");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Time.parse("22:60:10");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Time.parse("22:59:60");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Time.parse("22:59:60.0000Z");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Time.parse("2:59:59.99999Z");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Time.parse("23:9:59.99999Z");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Time.parse("23:59:9.99999Z");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Time.parse("23:59:59.99999-15:00");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Time.parse("23:59:59.99999+14:60");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Time.parse("23:59:59.99999+14:60.9");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    assertEquals(new Time(9, 30, 10, TimeZone.getTimeZone("GMT")), Time.parse("09:30:10Z"));
    assertEquals(new Time(1, 23, 45), Time.parse("01:23:45"));
    assertEquals(new Time(21, 23, 45.678f), Time.parse("21:23:45.678"));
    assertEquals(new Time(21, 23, 45.09999f), Time.parse("21:23:45.09999"));
    assertEquals(new Time(24, 0, 0), Time.parse("24:00:00.00000"));
    assertEquals(new Time(21, 23, 45.09999f, TimeZone.getTimeZone("GMT")), Time.parse("21:23:45.09999Z"));
    assertEquals(new Time(21, 23, 45.09999f, TimeZone.getTimeZone("GMT+2:30")), Time.parse("21:23:45.09999+02:30"));
    assertEquals(new Time(21, 23, 45.09999f, TimeZone.getTimeZone("GMT-14:45")), Time.parse("21:23:45.09999-14:45"));
  }
}