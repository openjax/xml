/* Copyright (c) 2008 lib4j
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

import org.junit.Assert;
import org.junit.Test;
import org.lib4j.xml.binding.YearMonth;

public class YearMonthTest {
  @Test
  public void testYearMonth() {
    try {
      YearMonth.parseYearMonth(null);
      Assert.fail("Expected a NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      YearMonth.parseYearMonth("");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      YearMonth.parseYearMonth("010");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      YearMonth.parseYearMonth("10");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      YearMonth.parseYearMonth("100");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      YearMonth.parseYearMonth("AAA");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      YearMonth.parseYearMonth("2227-1");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      YearMonth.parseYearMonth("2227-1Z");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      YearMonth.parseYearMonth("2227-13-11:00");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      YearMonth.parseYearMonth("2227-12-15:00");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      YearMonth.parseYearMonth("2227-01+14:60");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      YearMonth.parseYearMonth("2227-00+10:60");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      YearMonth.parseYearMonth("2227-02+14:60.9");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    final String[] yearMonths = new String[] {"2500-01Z", "1400-02Z", "0003-03Z", "0020-04Z", "0310-05Z", "1001-06Z", "2007-07+01:00", "3017-08-01:00", "4027-09Z", "1302-10+12:00", "1112-11-12:30"};
    for (final String yearMonth : yearMonths)
      Assert.assertEquals(yearMonth, YearMonth.parseYearMonth(yearMonth).toString());
  }
}