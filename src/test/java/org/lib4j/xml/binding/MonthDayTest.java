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
import org.lib4j.xml.binding.MonthDay;

public class MonthDayTest {
  @Test
  public void testMonthDay() {
    try {
      MonthDay.parseMonthDay(null);
      Assert.fail("Expected a NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      MonthDay.parseMonthDay("");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("---5");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("-5-30");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("--A");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("--00");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("--13");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("--4");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("--04-32");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("--04-00");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("--04-7");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("--02-30");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("--04-31");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("--06-31");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("--09-31");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("--11Z-");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("--12-30-15:00");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("--07-12+14:60");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      MonthDay.parseMonthDay("--02-01+14:60.9");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    final String[] monthDays = new String[] {"--12-31Z", "--04-30Z", "--03-31Z", "--02-29Z", "--01-31Z", "--01-12Z", "--07-02+01:00", "--09-12-01:00", "--10-11Z", "--11-15+12:00", "--12-17-12:30"};
    for (final String monthDay : monthDays)
      Assert.assertEquals(monthDay, MonthDay.parseMonthDay(monthDay).toString());
  }
}