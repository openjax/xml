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

import static org.junit.Assert.*;

import org.junit.Test;

public class DateTimeTest {
  @Test
  public void testDateTime() {
    assertNull(DateTime.parse(null));

    try {
      DateTime.parse("");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("010");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("10");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("100");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("AAA");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-1");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-1Z");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-10-1");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-10-00Z");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-10-1Z");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-13-08-11:00");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-12-08-15:00");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-01-08+14:60");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-00-08+10:60");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-02-08+14:60.9");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-01-32+12:60");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-02-30+10:60");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-04-31+12:60.9");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }
    try {
      DateTime.parse("2227-12-08T12:30:61-11:00");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-12-08T26:50:31-12:00");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-12-08T13:50:31-15:00");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-01-08T10:90:01+12:60");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-01-08T00:00:00+10:60");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-02-08T12:12:12:22+14:60");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-01-32T13:13:13+12:60");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-02-30T05:01:01+10:60");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      DateTime.parse("2227-04-31TP04:20:00+12:60.9");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    final String[] dateTimes = new String[] {"2500-01-01T04:20:00Z", "1400-02-02T12:30:45.678Z", "0003-03-03T12:34:56.789Z", "0020-04-04T01:23:45.678Z", "0310-05-05T23:46:57.890Z", "1001-06-06T04:20:00Z", "2007-07-07T12:30:45.678+01:00", "3017-08-08T12:34:56.789-01:00", "4027-09-09T01:23:45.678Z", "1302-10-10T23:46:57.890+12:00", "1112-01-11T12:34:56.789-12:30"};
    for (final String dateTime : dateTimes)
      assertEquals(dateTime, DateTime.parse(dateTime).toString());
  }
}