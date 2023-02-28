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

import java.io.Serializable;
import java.util.Arrays;

/**
 * http://www.w3.org/TR/xmlschema11-2/#hexBinary
 */
public class HexBinary implements Serializable {
  public static String print(final HexBinary hexBinary) {
    return hexBinary == null ? null : hexBinary.toString();
  }

  public static HexBinary parse(final String string) {
    if (string == null)
      return null;

    final int i$ = string.length();
    if (i$ % 2 != 0)
      throw new IllegalArgumentException("Odd length of hex string: " + string.length());

    final int j$ = i$ / 2;
    final byte[] bytes = new byte[j$];
    for (int i = 0, j = 0; j < j$; ++j) { // [N]
      final char c0 = string.charAt(i++);
      final char c1 = string.charAt(i++);
      byte b = 0;
      if ('0' <= c0 && c0 <= '9')
        b += (c0 - '0') * 16;
      else if ('a' <= c0 && c0 <= 'f')
        b += (c0 - 'a' + 10) * 16;
      else if ('A' <= c0 && c0 <= 'F')
        b += (c0 - 'A' + 10) * 16;
      else
        throw new IllegalArgumentException("Bad character in hex string: " + c0);

      if ('0' <= c1 && c1 <= '9')
        b += c1 - '0';
      else if ('a' <= c1 && c1 <= 'f')
        b += c1 - 'a' + 10;
      else if ('A' <= c1 && c1 <= 'F')
        b += c1 - 'A' + 10;
      else
        throw new IllegalArgumentException("Bad character in hex string: " + c1);

      bytes[j] = b;
    }

    return new HexBinary(bytes);
  }

  private static char convertDigit(int value) {
    value &= 0x0f;
    return value >= 10 ? (char)(value - 10 + 'A') : (char)(value + '0');
  }

  private final byte[] bytes;
  private String encoded;

  public HexBinary(final byte[] bytes) {
    this.bytes = bytes;
    if (bytes == null)
      throw new IllegalArgumentException("bytes == null");
  }

  public byte[] getBytes() {
    return bytes;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof HexBinary))
      return false;

    final HexBinary that = (HexBinary)obj;
    return Arrays.equals(bytes, that.bytes);
  }

  @Override
  public int hashCode() {
    return 31 + Arrays.hashCode(bytes);
  }

  /**
   * Returns the hex string representation of this object's {@code byte[]} data.
   *
   * @return The hex string representation of this object's {@code byte[]} data.
   */
  @Override
  public String toString() {
    if (encoded != null)
      return encoded;

    final StringBuilder str = new StringBuilder(bytes.length * 2);
    for (int i = 0, i$ = bytes.length; i < i$; ++i) { // [A]
      final byte b = bytes[i];
      str.append(convertDigit(b >> 4)).append(convertDigit(b & 0x0f));
    }

    return encoded = str.toString();
  }
}