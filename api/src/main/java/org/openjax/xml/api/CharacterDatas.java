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

package org.openjax.xml.api;

/**
 * Utility functions for operations pertaining to XML character data.
 */
public final class CharacterDatas {
  static <T> T assertNotNull(final T obj, final String message) {
    if (obj == null)
      throw new IllegalArgumentException(message);

    return obj;
  }

  private static void assertQuote(final char quote) {
    if (quote != '"' && quote != '\'')
      throw new IllegalArgumentException("Illegal quote character: " + quote);
  }

  private static void assertRange(final int off, final int len, final CharSequence str) {
    assertNotNull(str, "str is null");
    assertRange(off, len, str.length(), "str.length()");
  }

  private static void assertRange(final int off, final int len, final char[] chars) {
    assertNotNull(chars, "chars is null");
    assertRange(off, len, chars.length, "chars.length");
  }

  private static void assertRange(final int off, final int len, final int length, final String lenError) {
    if (off < 0)
      throw new IndexOutOfBoundsException("off (" + off + ") must be non-negative");

    if (len < 0)
      throw new IndexOutOfBoundsException("len (" + len + ") must be non-negative");

    if (off + len > length)
      throw new IndexOutOfBoundsException("off (" + off + ") + len (" + len + ") > " + lenError + " (" + length + ")");
  }

  private static StringBuilder escape(final StringBuilder out, final CharSequence str, final char quote, final int off, final int len) {
    for (int i = off, length = off + len; i < length; ++i) {
      final char ch = str.charAt(i);
      if (ch == '&')
        out.append("&amp;");
      else if (ch == '>')
        out.append("&gt;");
      else if (ch == '<')
        out.append("&lt;");
      else if (quote != ch)
        out.append(ch);
      else if (ch == '\'')
        out.append("&apos;");
      else if (ch == '"')
        out.append("&quot;");
      else
        throw new IllegalArgumentException("Illegal quote character: '" + quote + "'");
    }

    return out;
  }

  private static StringBuilder escape(final StringBuilder out, final char[] chars, final char quote, final int off, final int len) {
    for (int i = off, length = off + len; i < length; ++i) {
      final char ch = chars[i];
      if (ch == '&')
        out.append("&amp;");
      else if (ch == '>')
        out.append("&gt;");
      else if (ch == '<')
        out.append("&lt;");
      else if (quote != ch)
        out.append(ch);
      else if (ch == '\'')
        out.append("&apos;");
      else if (ch == '"')
        out.append("&quot;");
      else
        throw new IllegalArgumentException("Illegal quote character: '" + quote + "'");
    }

    return out;
  }

  /**
   * Returns the XML-escaped {@code str} to be used in an XML attribute. The
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
   * @param str The string to escape.
   * @param quote The quote character to be used to delimit the attribute's
   *          value in the XML document (either {@code '"'} or {@code '\''}).
   * @return A new {@link StringBuilder} with the escaped representation of
   *         {@code str}.
   * @throws IllegalArgumentException If {@code str} is null, or if
   *           {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder escapeForAttr(final CharSequence str, final char quote) {
    assertNotNull(str, "str is null");
    assertQuote(quote);
    return escape(new StringBuilder(), str, quote, 0, str.length());
  }

  /**
   * Returns the XML-escaped {@code str} to be used in an XML attribute. The
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
   * @param out The {@link StringBuilder} to which the escaped contents of
   *          {@code str} are to be appended.
   * @param str The string to escape.
   * @param quote The quote character to be used to delimit the attribute's
   *          value in the XML document (either {@code '"'} or {@code '\''}).
   * @return The provided {@link StringBuilder} with the escaped representation
   *         of {@code str}.
   * @throws IllegalArgumentException If {@code out} or {@code str} is null, or
   *           if {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder escapeForAttr(final StringBuilder out, final CharSequence str, final char quote) {
    assertNotNull(out, "out is null");
    assertNotNull(str, "str is null");
    assertQuote(quote);
    return escape(out, str, quote, 0, str.length());
  }

  /**
   * Returns the XML-escaped {@code chars} to be used in an XML attribute. The
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
   * @param chars The {@code char[]} to escape.
   * @param quote The quote character to be used to delimit the attribute's
   *          value in the XML document (either {@code '"'} or {@code '\''}).
   * @return A new {@link StringBuilder} with the escaped representation of
   *         {@code chars}.
   * @throws IllegalArgumentException If {@code chars} is null, or if
   *           {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder escapeForAttr(final char[] chars, final char quote) {
    assertNotNull(chars, "chars is null");
    assertQuote(quote);
    return escape(new StringBuilder(), chars, quote, 0, chars.length);
  }

  /**
   * Returns the XML-escaped {@code chars} to be used in an XML attribute. The
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
   * @param out The {@link StringBuilder} to which the escaped contents of
   *          {@code chars} are to be appended.
   * @param chars The {@code char[]} to escape.
   * @param quote The quote character to be used to delimit the attribute's
   *          value in the XML document (either {@code '"'} or {@code '\''}).
   * @return The provided {@link StringBuilder} with the escaped representation
   *         of {@code chars}.
   * @throws IllegalArgumentException If {@code out} or {@code chars} is null,
   *           or if {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder escapeForAttr(final StringBuilder out, final char[] chars, final char quote) {
    assertNotNull(out, "out is null");
    assertNotNull(chars, "chars is null");
    assertQuote(quote);
    return escape(out, chars, quote, 0, chars.length);
  }

  /**
   * Returns the XML-escaped {@code str} to be used in an XML attribute. The
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
   * @param str The string to escape.
   * @param quote The quote character to be used to delimit the attribute's
   *          value in the XML document (either {@code '"'} or {@code '\''}).
   * @param off Start index from which to escape characters.
   * @param len Number of characters to escape.
   * @return A new {@link StringBuilder} with the escaped representation of
   *         {@code str}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code str} is null, or if
   *           {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder escapeForAttr(final CharSequence str, final char quote, final int off, final int len) {
    assertRange(off, len, str);
    assertQuote(quote);
    return escape(new StringBuilder(), str, quote, off, len);
  }

  /**
   * Returns the XML-escaped {@code str} to be used in an XML attribute. The
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
   * @param out The {@link StringBuilder} to which the escaped contents of
   *          {@code str} are to be appended.
   * @param str The string to escape.
   * @param quote The quote character to be used to delimit the attribute's
   *          value in the XML document (either {@code '"'} or {@code '\''}).
   * @param off Start index from which to escape characters.
   * @param len Number of characters to escape.
   * @return The provided {@link StringBuilder} with the escaped representation
   *         of {@code str}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code out} or {@code str} is null, or
   *           if {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder escapeForAttr(final StringBuilder out, final CharSequence str, final char quote, final int off, final int len) {
    assertNotNull(out, "out is null");
    assertRange(off, len, str);
    assertQuote(quote);
    return escape(out, str, quote, off, len);
  }

  /**
   * Returns the XML-escaped {@code chars} to be used in an XML attribute. The
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
   * @param chars The {@code char[]} to escape.
   * @param quote The quote character to be used to delimit the attribute's
   *          value in the XML document (either {@code '"'} or {@code '\''}).
   * @param off Start index from which to escape characters.
   * @param len Number of characters to escape.
   * @return A new {@link StringBuilder} with the escaped representation of
   *         {@code chars}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code chars} is null, or if
   *           {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder escapeForAttr(final char[] chars, final char quote, final int off, final int len) {
    assertRange(off, len, chars);
    assertQuote(quote);
    return escape(new StringBuilder(), chars, quote, off, len);
  }

  /**
   * Returns the XML-escaped {@code chars} to be used in an XML attribute. The
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
   * @param out The {@link StringBuilder} to which the escaped contents of
   *          {@code chars} are to be appended.
   * @param chars The {@code char[]} to escape.
   * @param quote The quote character to be used to delimit the attribute's
   *          value in the XML document (either {@code '"'} or {@code '\''}).
   * @param off Start index from which to escape characters.
   * @param len Number of characters to escape.
   * @return The provided {@link StringBuilder} with the escaped representation
   *         of {@code chars}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code out} or {@code chars} is null,
   *           or if {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder escapeForAttr(final StringBuilder out, final char[] chars, final char quote, final int off, final int len) {
    assertNotNull(out, "out is null");
    assertRange(off, len, chars);
    assertQuote(quote);
    return escape(out, chars, quote, off, len);
  }

  /**
   * Returns the XML-escaped {@code str} to be used in an XML element. The
   * escaped characters are:
   *
   * <pre>
   * {@code From |  To
   * -------------
   *   &  | &amp;
   *   >  | &gt;
   *   <  | &lt;}
   * </pre>
   *
   * @param str The string to escape.
   * @return A new {@link StringBuilder} with the escaped representation of
   *         {@code str}.
   * @throws IllegalArgumentException If {@code str} is null.
   */
  public static StringBuilder escapeForElem(final CharSequence str) {
    assertNotNull(str, "str is null");
    return escape(new StringBuilder(), str, '\0', 0, str.length());
  }

  /**
   * Returns the XML-escaped {@code str} to be used in an XML element. The
   * escaped characters are:
   *
   * <pre>
   * {@code From |  To
   * -------------
   *   &  | &amp;
   *   >  | &gt;
   *   <  | &lt;}
   * </pre>
   *
   * @param out The {@link StringBuilder} to which the escaped contents of
   *          {@code str} are to be appended.
   * @param str The string to escape.
   * @return The provided {@link StringBuilder} with the escaped representation
   *         of {@code str}.
   * @throws IllegalArgumentException If {@code out} or {@code str} is null.
   */
  public static StringBuilder escapeForElem(final StringBuilder out, final CharSequence str) {
    assertNotNull(out, "out is null");
    assertNotNull(str, "str is null");
    return escape(out, str, '\0', 0, str.length());
  }

  /**
   * Returns the XML-escaped {@code chars} to be used in an XML element. The
   * escaped characters are:
   *
   * <pre>
   * {@code From |  To
   * -------------
   *   &  | &amp;
   *   >  | &gt;
   *   <  | &lt;}
   * </pre>
   *
   * @param chars The {@code char[]} to escape.
   * @return A new {@link StringBuilder} with the escaped representation of
   *         {@code chars}.
   * @throws IllegalArgumentException If {@code chars} is null.
   */
  public static StringBuilder escapeForElem(final char[] chars) {
    assertNotNull(chars, "chars is null");
    return escape(new StringBuilder(), chars, '\0', 0, chars.length);
  }

  /**
   * Returns the XML-escaped {@code chars} to be used in an XML element. The
   * escaped characters are:
   *
   * <pre>
   * {@code From |  To
   * -------------
   *   &  | &amp;
   *   >  | &gt;
   *   <  | &lt;}
   * </pre>
   *
   * @param out The {@link StringBuilder} to which the escaped contents of
   *          {@code chars} are to be appended.
   * @param chars The {@code char[]} to escape.
   * @return The provided {@link StringBuilder} with the escaped representation
   *         of {@code chars}.
   * @throws IllegalArgumentException If {@code out} or {@code chars} is null.
   */
  public static StringBuilder escapeForElem(final StringBuilder out, final char[] chars) {
    assertNotNull(out, "out is null");
    assertNotNull(chars, "chars is null");
    return escape(out, chars, '\0', 0, chars.length);
  }

  /**
   * Returns the XML-escaped {@code str} to be used in an XML element. The
   * escaped characters are:
   *
   * <pre>
   * {@code From |  To
   * -------------
   *   &  | &amp;
   *   >  | &gt;
   *   <  | &lt;}
   * </pre>
   *
   * @param str The string to escape.
   * @param off Start index from which to escape characters.
   * @param len Number of characters to escape.
   * @return A new {@link StringBuilder} with the escaped representation of
   *         {@code str}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code str} is null.
   */
  public static StringBuilder escapeForElem(final CharSequence str, final int off, final int len) {
    assertRange(off, len, str);
    return escape(new StringBuilder(), str, '\0', off, len);
  }

  /**
   * Returns the XML-escaped {@code str} to be used in an XML element. The
   * escaped characters are:
   *
   * <pre>
   * {@code From |  To
   * -------------
   *   &  | &amp;
   *   >  | &gt;
   *   <  | &lt;}
   * </pre>
   *
   * @param out The {@link StringBuilder} to which the escaped contents of
   *          {@code str} are to be appended.
   * @param str The string to escape.
   * @param off Start index from which to escape characters.
   * @param len Number of characters to escape.
   * @return The provided {@link StringBuilder} with the escaped representation
   *         of {@code str}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code out} or {@code str} is null.
   */
  public static StringBuilder escapeForElem(final StringBuilder out, final CharSequence str, final int off, final int len) {
    assertNotNull(out, "out is null");
    assertRange(off, len, str);
    return escape(out, str, '\0', off, len);
  }

  /**
   * Returns the XML-escaped {@code chars} to be used in an XML element. The
   * escaped characters are:
   *
   * <pre>
   * {@code From |  To
   * -------------
   *   &  | &amp;
   *   >  | &gt;
   *   <  | &lt;}
   * </pre>
   *
   * @param chars The {@code char[]} to escape.
   * @param off Start index from which to escape characters.
   * @param len Number of characters to escape.
   * @return A new {@link StringBuilder} with the escaped representation of
   *         {@code chars}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code chars} is null.
   */
  public static StringBuilder escapeForElem(final char[] chars, final int off, final int len) {
    assertRange(off, len, chars);
    return escape(new StringBuilder(), chars, '\0', off, len);
  }

  /**
   * Returns the XML-escaped {@code chars} to be used in an XML element. The
   * escaped characters are:
   *
   * <pre>
   * {@code From |  To
   * -------------
   *   &  | &amp;
   *   >  | &gt;
   *   <  | &lt;}
   * </pre>
   *
   * @param out The {@link StringBuilder} to which the escaped contents of
   *          {@code chars} are to be appended.
   * @param chars The {@code char[]} to escape.
   * @param off Start index from which to escape characters.
   * @param len Number of characters to escape.
   * @return The provided {@link StringBuilder} with the escaped representation
   *         of {@code chars}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code out} or {@code chars} is null.
   */
  public static StringBuilder escapeForElem(final StringBuilder out, final char[] chars, final int off, final int len) {
    assertNotNull(out, "out is null");
    assertRange(off, len, chars);
    return escape(out, chars, '\0', off, len);
  }

  private static int check(final char c5, final char c4, final char c3, final char c2, final char c1, final char c0, final StringBuilder out, final int i) {
    if (i >= 0)
      return i;

    if (c5 != '\0')
      out.append(c5);

    if (c4 != '\0')
      out.append(c4);

    if (c3 != '\0')
      out.append(c3);

    if (c2 != '\0')
      out.append(c2);

    if (c1 != '\0')
      out.append(c1);

    if (c0 != '\0')
      out.append(c0);

    return -i;
  }

  private static int unescape(final StringBuilder out, final CharSequence str, int i, final char c4, final char c3, final char c2, final char c1, final int depth, final char quote, final int len) {
    if (i == len)
      return -i;

    final char c0 = str.charAt(i);
    if (c0 == '&')
      return check(c4, c3, c2, c1, c0, ++i == len ? '\0' : str.charAt(i), out, unescape(out, str, i, c3, c2, c1, c0, 1, quote, len));

    if (depth == 1) {
      if (c0 == 'a' || c0 == 'g' || c0 == 'l' || c0 == 'q')
        return check(c4, c3, c2, c1, c0, ++i == len ? '\0' : str.charAt(i), out, unescape(out, str, i, c3, c2, c1, c0, 2, quote, len));

      return -i;
    }
    else if (depth == 2) {
      if (c0 == 'm' || c0 == 'p' || c0 == 't' || c0 == 'u')
        return check(c4, c3, c2, c1, c0, ++i == len ? '\0' : str.charAt(i), out, unescape(out, str, i, c3, c2, c1, c0, 3, quote, len));

      return -i;
    }
    else if (depth == 3) {
      if (c0 == 'p' || c0 == 'o')
        return check(c4, c3, c2, c1, c0, ++i == len ? '\0' : str.charAt(i), out, unescape(out, str, i, c3, c2, c1, c0, 4, quote, len));

      if (c0 == ';' && c1 == 't') {
        if (c2 == 'g') {
          out.append('>');
          return i;
        }

        if (c2 == 'l') {
          out.append('<');
          return i;
        }
      }

      return -i;
    }
    else if (depth == 4) {
      if (c0 == 's')
        return quote != '\'' ? -i : check(c4, c3, c2, c1, c0, ++i == len ? '\0' : str.charAt(i), out, unescape(out, str, i, c3, c2, c1, c0, 5, quote, len));

      if (c0 == 't')
        return quote != '"' ? -i : check(c4, c3, c2, c1, c0, ++i == len ? '\0' : str.charAt(i), out, unescape(out, str, i, c3, c2, c1, c0, 5, quote, len));

      if (c0 == ';' && c1 == 'p') {
        out.append('&');
        return i;
      }

      return -i;
    }
    else if (depth == 5 && c0 == ';') {
      if (c1 == 's') {
        out.append('\'');
        return i;
      }

      if (c1 == 't') {
        out.append('"');
        return i;
      }

      return -i;
    }

    out.append(c0);
    return i;
  }

  private static int unescape(final StringBuilder out, final char[] chars, int i, final char c4, final char c3, final char c2, final char c1, final int depth, final char quote, final int len) {
    if (i == len)
      return -i;

    final char c0 = chars[i];
    if (c0 == '&')
      return check(c4, c3, c2, c1, c0, ++i == len ? '\0' : chars[i], out, unescape(out, chars, i, c3, c2, c1, c0, 1, quote, len));

    if (depth == 1 && (c0 == 'a' || c0 == 'g' || c0 == 'l' || c0 == 'q'))
      return check(c4, c3, c2, c1, c0, ++i == len ? '\0' : chars[i], out, unescape(out, chars, i, c3, c2, c1, c0, 2, quote, len));

    if (depth == 2 && (c0 == 'm' || c0 == 'p' || c0 == 't' || c0 == 'u'))
      return check(c4, c3, c2, c1, c0, ++i == len ? '\0' : chars[i], out, unescape(out, chars, i, c3, c2, c1, c0, 3, quote, len));

    if (depth == 3) {
      if (c0 == 'p' || c0 == 'o')
        return check(c4, c3, c2, c1, c0, ++i == len ? '\0' : chars[i], out, unescape(out, chars, i, c3, c2, c1, c0, 4, quote, len));

      if (c0 == ';' && c1 == 't') {
        if (c2 == 'g') {
          out.append('>');
          return i;
        }

        if (c2 == 'l') {
          out.append('<');
          return i;
        }

        return -i;
      }
    }
    else if (depth == 4) {
      if (c0 == 's')
        return quote != '\'' ? -i : check(c4, c3, c2, c1, c0, ++i == len ? '\0' : chars[i], out, unescape(out, chars, i, c3, c2, c1, c0, 5, quote, len));

      if (c0 == 't')
        return quote != '"' ? -i : check(c4, c3, c2, c1, c0, ++i == len ? '\0' : chars[i], out, unescape(out, chars, i, c3, c2, c1, c0, 5, quote, len));

      if (c0 == ';' && c1 == 'p') {
        out.append('&');
        return i;
      }

      return -i;
    }
    else if (depth == 5 && c0 == ';') {
      if (c1 == 's') {
        out.append('\'');
        return i;
      }

      if (c1 == 't') {
        out.append('"');
        return i;
      }

      return -i;
    }

    out.append(c0);
    return i;
  }

  private static StringBuilder unescape(final StringBuilder out, final CharSequence str, final char quote, final int off, final int len) {
    for (int i = off, length = off + len; i < length; ++i)
      i = unescape(out, str, i, '\0', '\0', '\0', '\0', 0, quote, length);

    return out;
  }

  private static StringBuilder unescape(final StringBuilder out, final char[] chars, final char quote, final int off, final int len) {
    for (int i = off, length = off + len; i < length; ++i)
      i = unescape(out, chars, i, '\0', '\0', '\0', '\0', 0, quote, length);

    return out;
  }

  /**
   * Returns the XML-unescaped {@code str} as from an XML attribute. The
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
   * @param str The string to unescape.
   * @param quote The quote character used to delimit the attribute's value in
   *          the XML document (either {@code '"'} or {@code '\''}).
   * @return A new {@link StringBuilder} with the unescaped representation of
   *         {@code str}.
   * @throws IllegalArgumentException If {@code str} is null, or if
   *           {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder unescapeFromAttr(final CharSequence str, final char quote) {
    assertNotNull(str, "str is null");
    assertQuote(quote);
    return unescape(new StringBuilder(), str, quote, 0, str.length());
  }

  /**
   * Returns the XML-unescaped {@code str} as from an XML attribute. The
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
   * @param out The {@link StringBuilder} to which the unescaped contents of
   *          {@code str} are to be appended.
   * @param str The string to unescape.
   * @param quote The quote character used to delimit the attribute's value in
   *          the XML document (either {@code '"'} or {@code '\''}).
   * @return The provided {@link StringBuilder} with the unescaped
   *         representation of {@code str}.
   * @throws IllegalArgumentException If {@code out} or {@code str} is null, or
   *           if {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder unescapeFromAttr(final StringBuilder out, final CharSequence str, final char quote) {
    assertNotNull(out, "out is null");
    assertNotNull(str, "str is null");
    assertQuote(quote);
    return unescape(out, str, quote, 0, str.length());
  }

  /**
   * Returns the XML-unescaped {@code chars} as from an XML attribute. The
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
   * @param chars The {@code char[]} to unescape.
   * @param quote The quote character used to delimit the attribute's value in
   *          the XML document (either {@code '"'} or {@code '\''}).
   * @return A new {@link StringBuilder} with the unescaped representation of
   *         {@code chars}.
   * @throws IllegalArgumentException If {@code chars} is null, or if
   *           {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder unescapeFromAttr(final char[] chars, final char quote) {
    assertNotNull(chars, "chars is null");
    assertQuote(quote);
    return unescape(new StringBuilder(), chars, quote, 0, chars.length);
  }

  /**
   * Returns the XML-unescaped {@code chars} as from an XML attribute. The
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
   * @param out The {@link StringBuilder} to which the unescaped contents of
   *          {@code chars} are to be appended.
   * @param chars The {@code char[]} to unescape.
   * @param quote The quote character used to delimit the attribute's value in
   *          the XML document (either {@code '"'} or {@code '\''}).
   * @return The provided {@link StringBuilder} with the unescaped
   *         representation of {@code chars}.
   * @throws IllegalArgumentException If {@code out} or {@code chars} is null,
   *           or if {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder unescapeFromAttr(final StringBuilder out, final char[] chars, final char quote) {
    assertNotNull(out, "out is null");
    assertNotNull(chars, "chars is null");
    assertQuote(quote);
    return unescape(out, chars, quote, 0, chars.length);
  }

  /**
   * Returns the XML-unescaped {@code str} as from an XML attribute. The
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
   * @param str The string to unescape.
   * @param quote The quote character used to delimit the attribute's value in
   *          the XML document (either {@code '"'} or {@code '\''}).
   * @param off Start index from which to unescape characters.
   * @param len Number of characters to escape.
   * @return A new {@link StringBuilder} with the unescaped representation of
   *         {@code str}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code str} is null, or if
   *           {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder unescapeFromAttr(final CharSequence str, final char quote, final int off, final int len) {
    assertRange(off, len, str);
    return unescape(new StringBuilder(), str, quote, off, len);
  }

  /**
   * Returns the XML-unescaped {@code str} as from an XML attribute. The
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
   * @param out The {@link StringBuilder} to which the unescaped contents of
   *          {@code str} are to be appended.
   * @param str The string to unescape.
   * @param quote The quote character used to delimit the attribute's value in
   *          the XML document (either {@code '"'} or {@code '\''}).
   * @param off Start index from which to unescape characters.
   * @param len Number of characters to escape.
   * @return The provided {@link StringBuilder} with the unescaped
   *         representation of {@code str}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code out} or {@code str} is null, or
   *           if {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder unescapeFromAttr(final StringBuilder out, final CharSequence str, final char quote, final int off, final int len) {
    assertNotNull(out, "out is null");
    assertRange(off, len, str);
    assertQuote(quote);
    return unescape(out, str, quote, off, len);
  }

  /**
   * Returns the XML-unescaped {@code chars} as from an XML attribute. The
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
   * @param chars The {@code char[]} to unescape.
   * @param quote The quote character used to delimit the attribute's value in
   *          the XML document (either {@code '"'} or {@code '\''}).
   * @param off Start index from which to unescape characters.
   * @param len Number of characters to escape.
   * @return A new {@link StringBuilder} with the unescaped representation of
   *         {@code chars}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code chars} is null, or if
   *           {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder unescapeFromAttr(final char[] chars, final char quote, final int off, final int len) {
    assertRange(off, len, chars);
    assertQuote(quote);
    return unescape(new StringBuilder(), chars, quote, off, len);
  }

  /**
   * Returns the XML-unescaped {@code chars} as from an XML attribute. The
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
   * @param out The {@link StringBuilder} to which the unescaped contents of
   *          {@code chars} are to be appended.
   * @param chars The {@code char[]} to unescape.
   * @param quote The quote character used to delimit the attribute's value in
   *          the XML document (either {@code '"'} or {@code '\''}).
   * @param off Start index from which to unescape characters.
   * @param len Number of characters to escape.
   * @return The provided {@link StringBuilder} with the unescaped
   *         representation of {@code chars}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code out} or {@code chars} is null,
   *           or if {@code quote} is not {@code '"'} or {@code '\''}.
   */
  public static StringBuilder unescapeFromAttr(final StringBuilder out, final char[] chars, final char quote, final int off, final int len) {
    assertNotNull(out, "out is null");
    assertRange(off, len, chars);
    assertQuote(quote);
    return unescape(out, chars, quote, off, len);
  }

  /**
   * Returns the XML-unescaped {@code str} as from an XML element. The unescaped
   * characters are:
   *
   * <pre>
   * {@code From   | To
   * ------------
   * &amp;  | &
   * &gt;   | >
   * &lt;   | <}
   * </pre>
   *
   * @param str The string to unescape.
   * @return A new {@link StringBuilder} with the unescaped representation of
   *         {@code str}.
   * @throws IllegalArgumentException If {@code str} is null.
   */
  public static StringBuilder unescapeFromElem(final CharSequence str) {
    assertNotNull(str, "str is null");
    return unescape(new StringBuilder(), str, '\0', 0, str.length());
  }

  /**
   * Returns the XML-unescaped {@code str} as from an XML element. The unescaped
   * characters are:
   *
   * <pre>
   * {@code From   | To
   * ------------
   * &amp;  | &
   * &gt;   | >
   * &lt;   | <}
   * </pre>
   *
   * @param out The {@link StringBuilder} to which the unescaped contents of
   *          {@code str} are to be appended.
   * @param str The string to unescape.
   * @return The provided {@link StringBuilder} with the unescaped
   *         representation of {@code str}.
   * @throws IllegalArgumentException If {@code out} or {@code str} is null.
   */
  public static StringBuilder unescapeFromElem(final StringBuilder out, final CharSequence str) {
    assertNotNull(out, "out is null");
    assertNotNull(str, "str is null");
    return unescape(out, str, '\0', 0, str.length());
  }

  /**
   * Returns the XML-unescaped {@code chars} as from an XML element. The
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
   * @param chars The {@code char[]} to unescape.
   * @return A new {@link StringBuilder} with the unescaped representation of
   *         {@code chars}.
   * @throws IllegalArgumentException If {@code chars} is null.
   */
  public static StringBuilder unescapeFromElem(final char[] chars) {
    assertNotNull(chars, "chars is null");
    return unescape(new StringBuilder(), chars, '\0', 0, chars.length);
  }

  /**
   * Returns the XML-unescaped {@code chars} as from an XML element. The
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
   * @param out The {@link StringBuilder} to which the unescaped contents of
   *          {@code chars} are to be appended.
   * @param chars The {@code char[]} to unescape.
   * @return The provided {@link StringBuilder} with the unescaped
   *         representation of {@code chars}.
   * @throws IllegalArgumentException If {@code out} or {@code chars} is null.
   */
  public static StringBuilder unescapeFromElem(final StringBuilder out, final char[] chars) {
    assertNotNull(out, "out is null");
    assertNotNull(chars, "chars is null");
    return unescape(out, chars, '\0', 0, chars.length);
  }

  /**
   * Returns the XML-unescaped {@code str} as from an XML element. The unescaped
   * characters are:
   *
   * <pre>
   * {@code From   | To
   * ------------
   * &amp;  | &
   * &gt;   | >
   * &lt;   | <}
   * </pre>
   *
   * @param str The string to unescape.
   * @param off Start index from which to unescape characters.
   * @param len Number of characters to escape.
   * @return A new {@link StringBuilder} with the unescaped representation of
   *         {@code str}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code str} is null.
   */
  public static StringBuilder unescapeFromElem(final CharSequence str, final int off, final int len) {
    assertRange(off, len, str);
    return unescape(new StringBuilder(), str, '\0', off, len);
  }

  /**
   * Returns the XML-unescaped {@code str} as from an XML element. The unescaped
   * characters are:
   *
   * <pre>
   * {@code From   | To
   * ------------
   * &amp;  | &
   * &gt;   | >
   * &lt;   | <}
   * </pre>
   *
   * @param out The {@link StringBuilder} to which the unescaped contents of
   *          {@code str} are to be appended.
   * @param str The string to unescape.
   * @param off Start index from which to unescape characters.
   * @param len Number of characters to escape.
   * @return The provided {@link StringBuilder} with the unescaped
   *         representation of {@code str}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code out} or {@code str} is null.
   */
  public static StringBuilder unescapeFromElem(final StringBuilder out, final CharSequence str, final int off, final int len) {
    assertNotNull(out, "out is null");
    assertRange(off, len, str);
    return unescape(out, str, '\0', off, len);
  }

  /**
   * Returns the XML-unescaped {@code chars} as from an XML element. The
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
   * @param chars The {@code char[]} to unescape.
   * @param off Start index from which to unescape characters.
   * @param len Number of characters to escape.
   * @return A new {@link StringBuilder} with the unescaped representation of
   *         {@code chars}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code chars} is null.
   */
  public static StringBuilder unescapeFromElem(final char[] chars, final int off, final int len) {
    assertRange(off, len, chars);
    return unescape(new StringBuilder(), chars, '\0', off, len);
  }

  /**
   * Returns the XML-unescaped {@code chars} as from an XML element. The
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
   * @param out The {@link StringBuilder} to which the unescaped contents of
   *          {@code chars} are to be appended.
   * @param chars The {@code char[]} to unescape.
   * @param off Start index from which to unescape characters.
   * @param len Number of characters to escape.
   * @return The provided {@link StringBuilder} with the unescaped
   *         representation of {@code chars}.
   * @throws IndexOutOfBoundsException For range parameters that are
   *           out-of-bounds ({@code off < 0 || len < 0 ||
   *         off + len >= str.length()}).
   * @throws IllegalArgumentException If {@code out} or {@code chars} is null.
   */
  public static StringBuilder unescapeFromElem(final StringBuilder out, final char[] chars, final int off, final int len) {
    assertNotNull(out, "out is null");
    assertRange(off, len, chars);
    return unescape(out, chars, '\0', off, len);
  }

  private CharacterDatas() {
  }
}