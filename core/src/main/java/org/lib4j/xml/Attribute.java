/* Copyright (c) 2018 lib4j
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

package org.lib4j.xml;

public class Attribute {
  private final String name;
  private final String value;

  public Attribute(final String name, final String value) {
    this.name = name;
    if (name == null || name.length() == 0)
      throw new IllegalArgumentException("name == " + name);

    this.value = value;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Attribute))
      return false;

    final Attribute that = (Attribute)obj;
    return name.equals(that.name) && (value != null ? value.equals(that.value) : that.value == null);
  }

  @Override
  public int hashCode() {
    int hashCode = 9;
    hashCode ^= 31 * name.hashCode();
    hashCode ^= 31 * value.hashCode();
    return hashCode;
  }

  @Override
  public String toString() {
    return name + "=\"" + CharacterDatas.escape(value) + "\"";
  }
}