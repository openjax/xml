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
import java.util.Base64;
import java.util.Objects;

/**
 * http://www.w3.org/TR/xmlschema11-2/#base64Binary
 */
public class Base64Binary implements Serializable {
  public static String print(final Base64Binary base64Binary) {
    return base64Binary == null ? null : base64Binary.toString();
  }

  public static Base64Binary parse(final String encoded) {
    return encoded == null ? null : new Base64Binary(Base64.getDecoder().decode(encoded));
  }

  private final byte[] bytes;
  private String encoded;

  public Base64Binary(final byte[] bytes) {
    this.bytes = Objects.requireNonNull(bytes);
  }

  public Base64Binary(final String encoded) {
    this.encoded = encoded;
    this.bytes = Base64.getDecoder().decode(encoded);
  }

  public byte[] getBytes() {
    return bytes;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Base64Binary))
      return false;

    final Base64Binary that = (Base64Binary)obj;
    return Arrays.equals(bytes, that.bytes);
  }

  @Override
  public int hashCode() {
    return 31 + Arrays.hashCode(bytes);
  }

  /**
   * Returns the Base64 string representation of this object's {@code byte[]} data.
   *
   * @return The Base64 string.
   */
  @Override
  public String toString() {
    return encoded == null ? encoded = new String(Base64.getEncoder().encode(bytes)) : encoded;
  }
}