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
import org.lib4j.xml.binding.Year;

public class YearTest {
  @Test
  public void testYear() {
    try {
      Year.parseYear(null);
      Assert.fail("Expected a NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      Year.parseYear("");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Year.parseYear("--010");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Year.parseYear("010");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Year.parseYear("10");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Year.parseYear("100");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Year.parseYear("AAA");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Year.parseYear("2227-15:00");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Year.parseYear("2227+14:60");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Year.parseYear("2227+14:60.9");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    final String[] years = new String[] {"2500Z", "1400Z", "0003Z", "0020Z", "0310Z", "1001Z", "2007+01:00", "3017-01:00", "4027Z", "1302+12:00", "1112-12:30"};
    for (final String year : years)
      Assert.assertEquals(year, Year.parseYear(year).toString());
  }
}