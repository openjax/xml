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

package org.openjax.xml.dom;

public enum DOMStyle {
  /** Indent each element with 2 spaces. */
  INDENT(true, false, false),
  /** Indent each attribute with a newline. */
  INDENT_ATTRS(false, true, false),
  /** Omit namespace declaration. */
  OMIT_NAMESPACES(false, false, true);

  private final boolean indentElements;
  private final boolean indentAttributes;
  private final boolean omitNamespaces;

  private DOMStyle(final boolean indentElements, final boolean indentAttributes, final boolean omitNamespaces) {
    this.indentElements = indentElements;
    this.indentAttributes = indentAttributes;
    this.omitNamespaces = omitNamespaces;
  }

  public boolean isIndentAttributes() {
    return indentAttributes;
  }

  public boolean isIndent() {
    return indentElements;
  }

  public boolean isOmitNamespaces() {
    return omitNamespaces;
  }

  public static boolean isIndentAttributes(final DOMStyle ... styles) {
    if (styles == null)
      return false;

    for (int i = 0; i < styles.length; ++i)
      if (styles[i].isIndentAttributes())
        return true;

    return false;
  }

  public static boolean isIndent(final DOMStyle ... styles) {
    if (styles == null)
      return false;

    for (int i = 0; i < styles.length; ++i)
      if (styles[i].isIndent())
        return true;

    return false;
  }

  public static boolean isOmitNamespaces(final DOMStyle ... styles) {
    if (styles == null)
      return false;

    for (int i = 0; i < styles.length; ++i)
      if (styles[i].isOmitNamespaces())
        return true;

    return false;
  }

  private static int combinations = -1;

  public static int combinations() {
    return combinations == -1 ? combinations = 1 << values().length : combinations;
  }

  public static int combination(final DOMStyle ... styles) {
    if (styles == null)
      return 0;

    boolean a = false;
    boolean b = false;
    boolean c = false;
    for (final DOMStyle style : styles) {
      switch (style.ordinal()) {
        case 0:
          a = true;
          break;

        case 1:
          b = true;
          break;

        case 2:
          c = true;
          break;

        default:
          throw new UnsupportedOperationException("Unsupported DOMStyle: " + style);
      }
    }

    return a ? (b ? (c ? 7 : 6) : (c ? 5 : 4)) : (b ? (c ? 3 : 2) : (c ? 1 : 0));
  }
}