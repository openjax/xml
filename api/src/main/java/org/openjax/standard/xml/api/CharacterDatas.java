/* Copyright (c) 2017 OpenJAX
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

package org.openjax.standard.xml.api;

/**
 * Utility functions for operations pertaining to XML character data.
 */
public final class CharacterDatas {
  private static void assertRange(final int fromIndex, final int toIndex, final CharSequence string) {
    if (fromIndex < 0)
      throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);

    if (fromIndex > toIndex)
      throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");

    if (toIndex > string.length())
      throw new IndexOutOfBoundsException("toIndex = " + toIndex);
  }

  private static StringBuilder escape(final StringBuilder string, final char quote, final int fromIndex, final int toIndex) {
    for (int i = toIndex - 1; i >= fromIndex; --i) {
      final char ch = string.charAt(i);
      if (ch == '&')
        string.replace(i, i + 1, "&amp;");
      else if (ch == '>')
        string.replace(i, i + 1, "&gt;");
      else if (ch == '<')
        string.replace(i, i + 1, "&lt;");
      if (quote == '\0' || quote == ch) {
        if (ch == '\'')
          string.replace(i, i + 1, "&apos;");
        else if (ch == '"')
          string.replace(i, i + 1, "&quot;");
      }
    }

    return string;
  }

  /**
   * Returns the XML-escaped {@code string} to be used in an XML attribute. The
   * specified {@code quote} refers to the character to be used to delimit the
   * attribute's value in the XML document (either {@code '"'} or {@code '\''}).
   * The escaped characters are:
   *
   * <pre>
   * {@code From |  To
   * -------------
   *   &  | &amp;
   *   >  | &gt;
   *   <  | &lt;
   * -------------
   *   '  | &apos;
   *   "  | &quot;}
   * </pre>
   *
   * @param string The string to escape.
   * @param quote The quote character to be used to delimit the attribute's
   *          value in the XML document (either {@code '"'} or {@code '\''}).
   * @return The XML-escaped {@code string}.
   * @throws IllegalArgumentException If {@code quote} is not {@code '"'} or
   *           {@code '\''}.
   * @throws NullPointerException If {@code string} is null.
   */
  public static String escapeForAttr(final CharSequence string, final char quote) {
    if (quote != '"' && quote != '\'')
      throw new IllegalArgumentException("Illegal quote character: " + quote);

    return escape(string instanceof StringBuilder ? (StringBuilder)string : new StringBuilder(string), quote, 0, string.length()).toString();
  }

  /**
   * Returns the XML-escaped {@code string} to be used in an XML attribute. The
   * specified {@code quote} refers to the character to be used to delimit the
   * attribute's value in the XML document (either {@code '"'} or {@code '\''}).
   * The escaped characters are:
   *
   * <pre>
   * {@code From |  To
   * -------------
   *   &  | &amp;
   *   >  | &gt;
   *   <  | &lt;
   * -------------
   *   '  | &apos;
   *   "  | &quot;}
   * </pre>
   *
   * @param string The string to escape.
   * @param quote The quote character to be used to delimit the attribute's
   *          value in the XML document (either {@code '"'} or {@code '\''}).
   * @param fromIndex Start index from which to escape characters.
   * @param toIndex End index to which to escape characters.
   * @return The XML-escaped {@code string}.
   * @throws IllegalArgumentException If {@code quote} is not {@code '"'} or
   *           {@code '\''}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code fromIndex < 0 || toIndex > size ||
   *         fromIndex > toIndex}).
   * @throws NullPointerException If {@code string} is null.
   */
  public static String escapeForAttr(final CharSequence string, final char quote, final int fromIndex, final int toIndex) {
    if (quote != '"' && quote != '\'')
      throw new IllegalArgumentException("Illegal quote character: " + quote);

    assertRange(fromIndex, toIndex, string);
    return escape(string instanceof StringBuilder ? (StringBuilder)string : new StringBuilder(string), quote, fromIndex, toIndex).toString();
  }

  /**
   * Returns the XML-escaped {@code string} to be used in an XML element. The
   * escaped characters are:
   *
   * <pre>
   * {@code From |  To
   * -------------
   *   &  | &amp;
   *   >  | &gt;
   *   <  | &lt;}
   * </pre>
   * @param string The string to escape.
   * @return The XML-escaped {@code string}.
   * @throws NullPointerException If {@code string} is null.
   */
  public static String escapeForElem(final CharSequence string) {
    return escape(string instanceof StringBuilder ? (StringBuilder)string : new StringBuilder(string), '\0', 0, string.length()).toString();
  }

  /**
   * Returns the XML-escaped {@code string} to be used in an XML element. The
   * escaped characters are:
   *
   * <pre>
   * {@code From |  To
   * -------------
   *   &  | &amp;
   *   >  | &gt;
   *   <  | &lt;}
   * </pre>
   * @param string The string to escape.
   * @param fromIndex Start index from which to escape characters.
   * @param toIndex End index to which to escape characters.
   * @return The XML-escaped {@code string}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code fromIndex < 0 || toIndex > size ||
   *         fromIndex > toIndex}).
   * @throws NullPointerException If {@code string} is null.
   */
  public static String escapeForElem(final CharSequence string, final int fromIndex, final int toIndex) {
    assertRange(fromIndex, toIndex, string);
    return escape(string instanceof StringBuilder ? (StringBuilder)string : new StringBuilder(string), '\0', fromIndex, toIndex).toString();
  }

  private static StringBuilder unescape(final StringBuilder string, final char quote, final int fromIndex, final int toIndex) {
    for (int i = toIndex - 1, pos = 0, start = -1; i >= fromIndex; --i) {
      final char ch = string.charAt(i);
      if (start >= 0) {
        if (pos == 0) {
          if (ch != 'p' && ch != 't' && (quote != '\0' && quote != '\'' || ch != 's'))
            start = -1;
          else
            ++pos;
        }
        else if (pos == 1) {
          if (ch != 'm' && ch != 'g' && ch != 'l' && (quote != '\0' && quote != '"' && quote != '\'' || ch != 'o'))
            start = -1;
          else
            ++pos;
        }
        else if (pos == 2) {
          if (ch == '&') {
            string.replace(i, start + 1, string.charAt(i + 1) == 'g' ? ">" : "<");
            start = -1;
          }
          else if (ch != 'a' && ch != 'p' && (quote != '\0' && quote != '"' || ch != 'u')) {
            start = -1;
          }
          else {
            ++pos;
          }
        }
        else if (pos == 3) {
          if (ch == '&') {
            string.replace(i, start + 1, "&");
            start = -1;
          }
          else if (quote != '\0' && (quote != '\'' || ch != 'a') && (quote != '"' || ch != 'q')) {
            start = -1;
          }
          else {
            ++pos;
          }
        }
        else if (pos == 4) {
          if (ch == '&')
            string.replace(i, start + 1, string.charAt(i + 1) == 'a' ? "'" : "\"");

          start = -1;
        }
      }
      else if (ch == ';') {
        start = i;
        pos = 0;
      }
    }

    return string;
  }

  /**
   * Returns the XML-unescaped {@code string} as from an XML attribute. The
   * specified {@code quote} refers to the character used to delimit the
   * attribute's value in the XML document (either {@code '"'} or {@code '\''}).
   * The unescaped characters are:
   *
   * <pre>
   * {@code From   | To
   * ------------
   * &amp;  | &
   * &gt;   | >
   * &lt;   | <
   * ------------
   * &apos; | '
   * &quot; | "}
   * </pre>
   *
   * @param string The string to unescape.
   * @param quote The quote character used to delimit the attribute's value in
   *          the XML document (either {@code '"'} or {@code '\''}).
   * @return The XML-unescaped {@code string}.
   * @throws IllegalArgumentException If {@code quote} is not {@code '"'} or
   *           {@code '\''}.
   * @throws NullPointerException If {@code string} is null.
   */
  public static String unescapeFromAttr(final CharSequence string, final char quote) {
    return unescape(string instanceof StringBuilder ? (StringBuilder)string : new StringBuilder(string), quote, 0, string.length()).toString();
  }

  /**
   * Returns the XML-unescaped {@code string} as from an XML attribute. The
   * specified {@code quote} refers to the character used to delimit the
   * attribute's value in the XML document (either {@code '"'} or {@code '\''}).
   * The unescaped characters are:
   *
   * <pre>
   * {@code From   | To
   * ------------
   * &amp;  | &
   * &gt;   | >
   * &lt;   | <
   * ------------
   * &apos; | '
   * &quot; | "}
   * </pre>
   *
   * @param string The string to unescape.
   * @param quote The quote character used to delimit the attribute's value in
   *          the XML document (either {@code '"'} or {@code '\''}).
   * @param fromIndex Start index from which to unescape characters.
   * @param toIndex End index to which to unescape characters.
   * @return The XML-unescaped {@code string}.
   * @throws IllegalArgumentException If {@code quote} is not {@code '"'} or
   *           {@code '\''}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code fromIndex < 0 || toIndex > size ||
   *         fromIndex > toIndex}).
   * @throws NullPointerException If {@code string} is null.
   */
  public static String unescapeFromAttr(final CharSequence string, final char quote, final int fromIndex, final int toIndex) {
    assertRange(fromIndex, toIndex, string);
    return unescape(string instanceof StringBuilder ? (StringBuilder)string : new StringBuilder(string), quote, fromIndex, toIndex).toString();
  }

  /**
   * Returns the XML-unescaped {@code string} as from an XML element. The
   * unescaped characters are:
   *
   * <pre>
   * {@code From   | To
   * ------------
   * &amp;  | &
   * &gt;   | >
   * &lt;   | <}
   * </pre>
   *
   * @param string The string to unescape.
   * @return The XML-unescaped {@code string}.
   * @throws NullPointerException If {@code string} is null.
   */
  public static String unescapeFromElem(final CharSequence string) {
    return unescape(string instanceof StringBuilder ? (StringBuilder)string : new StringBuilder(string), '\0', 0, string.length()).toString();
  }

  /**
   * Returns the XML-unescaped {@code string} as from an XML element. The
   * unescaped characters are:
   *
   * <pre>
   * {@code From   | To
   * ------------
   * &amp;  | &
   * &gt;   | >
   * &lt;   | <}
   * </pre>
   *
   * @param string The string to unescape.
   * @param fromIndex Start index from which to unescape characters.
   * @param toIndex End index to which to unescape characters.
   * @return The XML-unescaped {@code string}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code fromIndex < 0 || toIndex > size ||
   *         fromIndex > toIndex}).
   * @throws NullPointerException If {@code string} is null.
   */
  public static String unescapeFromElem(final CharSequence string, final int fromIndex, final int toIndex) {
    assertRange(fromIndex, toIndex, string);
    return unescape(string instanceof StringBuilder ? (StringBuilder)string : new StringBuilder(string), '\0', fromIndex, toIndex).toString();
  }

  private CharacterDatas() {
  }
}