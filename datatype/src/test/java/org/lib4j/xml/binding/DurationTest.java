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
import org.lib4j.xml.datatype.Duration;

public class DurationTest {
  @Test
  public void testDuration() {
    Assert.assertNull(Duration.parse(null));

    try {
      Duration.parse("");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("X1347Y");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("PTT347Y");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P16349286492843693864932864932864293864Y3M5DT7H10M3.3S");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1.Y3M5DT7H10M3.3S");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1Y3M5D3.3S");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1Y3M5DT7H10M3.3S3.3S");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1Y3M5DT7H10M3S3S");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1Y3M5DT7H10M10M3S");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1Y3M5DT7H3S10M");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1Y3M5DT7H7H10M3S");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1Y3M5DT10M7H3S");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1Y3M5DT3S7H10M");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1Y3M5DT7H10M3Y");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1Y3Y3M5D");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P3M1Y5D");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P3D1Y");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1Y3M3M5D");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1Y5D3M");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1Y3M3D3D");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1Y3M3D7H");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Duration.parse("P1Y3M3D7S");
      Assert.fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    final String[] durations = new String[] {"P3Y", "-P1Y", "P10M", "-P8M", "P7D", "-P2D", "PT7H", "-PT9H", "PT8M", "-PT1M", "PT5S", "-PT4S", "PT5.555S", "-PT4.332S", "P3Y4M", "-P13Y34M", "P1Y3M5DT7H10M3.3S", "P1M", "P1D"};
    for (final String duration : durations)
      Assert.assertEquals(duration, Duration.parse(duration).toString());
  }
}