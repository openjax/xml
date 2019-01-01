/* Copyright (c) 2008 OpenJAX
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

import org.junit.Assert;
import org.junit.Test;

public class HexBinaryTest {
  private static void assertEquals(final String data, final String base64) {
    final HexBinary hexBinary = new HexBinary(data.getBytes());
    final String hexString = hexBinary.toString();
    Assert.assertEquals(base64, hexString);
    final HexBinary unmarshalled = HexBinary.parse(hexString);
    Assert.assertEquals(data, new String(unmarshalled.getBytes()));
  }

  @Test
  public void testHexBinary() {
    assertEquals("Bonjour", "426F6E6A6F7572");
    assertEquals("Hello World", "48656C6C6F20576F726C64");
    assertEquals("The quick brown fox jumps over the lazy dog", "54686520717569636B2062726F776E20666F78206A756D7073206F76657220746865206C617A7920646F67");
  }
}