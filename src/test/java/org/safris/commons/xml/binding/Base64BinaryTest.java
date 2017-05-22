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

package org.safris.commons.xml.binding;

import org.junit.Assert;
import org.junit.Test;

public class Base64BinaryTest {
  private static void assertEquals(final String data, final String base64) {
    final Base64Binary base64Binary = new Base64Binary(data.getBytes());
    final String base64String = base64Binary.toString();
    Assert.assertEquals(base64, base64String);
    final Base64Binary unmarshalled = Base64Binary.parseBase64Binary(base64String);
    Assert.assertEquals(data, new String(unmarshalled.getBytes()));
  }

  @Test
  public void testBase64Binary() {
    assertEquals("Bonjour", "Qm9uam91cg==");
    assertEquals("Hello World", "SGVsbG8gV29ybGQ=");
    assertEquals("The quick brown fox jumps over the lazy dog", "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZw==");
  }
}