/* Copyright (c) 2006 lib4j
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

import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

/**
 * http://www.w3.org/TR/xmlschema11-2/#base64Binary
 */
public final class Base64Binary {
  public static Base64Binary parseBase64Binary(final String string) {
    return string == null ? null : new Base64Binary(DatatypeConverter.parseBase64Binary(string));
  }

  private final byte[] bytes;
  private String encoded = null;

  public Base64Binary(final byte[] bytes) {
    this.bytes = bytes;
  }

  public byte[] getBytes() {
    return bytes;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof Base64Binary))
      return false;

    final Base64Binary that = (Base64Binary)obj;
    return bytes != null ? Arrays.equals(bytes, that.bytes) : that.bytes == null;
  }

  @Override
  public int hashCode() {
    return bytes != null ? Arrays.hashCode(bytes) : -1;
  }

  /**
   * Returns the base64 string representation of this object's byte[] data.
   *
   * @return  The base64 string.
   */
  @Override
  public String toString() {
    return encoded == null ? encoded = DatatypeConverter.printBase64Binary(bytes) : encoded;
  }
}