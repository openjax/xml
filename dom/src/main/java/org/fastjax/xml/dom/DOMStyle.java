/* Copyright (c) 2006 FastJAX
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

package org.fastjax.xml.dom;

public final class DOMStyle {
  protected static DOMStyle merge(final DOMStyle ... styles) {
    if (styles == null)
      return null;

    if (styles.length == 0)
      return DEFAULT;

    if (styles.length == 1)
      return styles[0];

    final DOMStyle merged = new DOMStyle(DEFAULT_MASK);
    for (final DOMStyle style : styles)
      merged.mask = merged.mask | style.mask;

    return merged;
  }

  private static final int DEFAULT_MASK = 0x000;
  private static final int INDENT_MASK = 0x001;
  private static final int INDENT_ATTRS_MASK = 0x010;
  private static final int IGNORE_NAMESPACES_MASK = 0x100;

  private static final DOMStyle DEFAULT = new DOMStyle(DEFAULT_MASK);
  public static final DOMStyle INDENT = new DOMStyle(INDENT_MASK);
  public static final DOMStyle INDENT_ATTRS = new DOMStyle(INDENT_ATTRS_MASK);
  public static final DOMStyle IGNORE_NAMESPACES = new DOMStyle(IGNORE_NAMESPACES_MASK);

  private int mask = 0;

  private DOMStyle(final int mask) {
    this.mask = mask;
  }

  protected boolean isIndentAttributes() {
    return (mask & INDENT_ATTRS_MASK) == INDENT_ATTRS_MASK;
  }

  protected boolean isIndent() {
    return (mask & INDENT_MASK) == INDENT_MASK;
  }

  protected boolean isIgnoreNamespaces() {
    return (mask & IGNORE_NAMESPACES_MASK) == IGNORE_NAMESPACES_MASK;
  }

  @Override
  public int hashCode() {
    return mask;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof DOMStyle))
      return false;

    return ((DOMStyle)obj).mask == mask;
  }
}