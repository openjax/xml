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

package org.openjax.classic.xml.api;

/**
 * Utility functions for operations pertaining to XML character data.
 */
public final class CharacterDatas {
  /**
   * Returns the XML-escaped {@code string}. The escaped characters are:
   *
   * <pre>
   * {@code From |  To
   * -------------
   *   &  | &amp;
   *   '  | &apos;
   *   "  | &quot;
   *   >  | &gt;
   *   <  | &lt;}
   * </pre>
   *
   * @param string The string to escape.
   * @return The XML-escaped {@code string}.
   */
  public static StringBuilder escape(final StringBuilder string) {
    for (int i = 0; i < string.length(); ++i) {
      final char ch = string.charAt(i);
      if (ch == '&')
        string.replace(i, i + 1, "&amp;");
      else if (ch == '\'')
        string.replace(i, i + 1, "&apos;");
      else if (ch == '"')
        string.replace(i, i + 1, "&quot;");
      else if (ch == '>')
        string.replace(i, i + 1, "&gt;");
      else if (ch == '<')
        string.replace(i, i + 1, "&lt;");
    }

    return string;
  }

  /**
   * Returns the XML-unescaped {@code string}. The unescaped characters are:
   *
   * <pre>
   * {@code From   | To
   * ------------
   * &amp;  | &
   * &apos; | '
   * &quot; | "
   * &gt;   | >
   * &lt;   | <}
   * </pre>
   *
   * @param string The string to unescape.
   * @return The XML-unescaped {@code string}.
   */
  public static StringBuilder unescape(final StringBuilder string) {
    char firstChar = '\0';
    for (int i = 0, pos = 0, start = -1; i < string.length(); ++i) {
      final char ch = string.charAt(i);
      if (start >= 0) {
        if (pos == 0) {
          if (ch != 'a' && ch != 'g' && ch != 'l' && ch != 'q') {
            start = -1;
          }
          else {
            ++pos;
            firstChar = ch;
          }
        }
        else if (pos == 1) {
          if (ch != 'm' && ch != 'p' && ch != 'g' && ch != 't' && ch != 'u')
            start = -1;
          else
            ++pos;
        }
        else if (pos == 2) {
          if (ch == ';') {
            string.replace(start, i + 1, firstChar == 'g' ? ">" : "<");
            start = -1;
          }
          else if (ch != 'p' && ch != 'o') {
            start = -1;
          }
          else {
            ++pos;
          }
        }
        else if (pos == 3) {
          if (ch == ';') {
            string.replace(start, i + 1, "&");
            start = -1;
          }
          else if (ch != 's' && ch != 't') {
            start = -1;
          }
          else {
            ++pos;
          }
        }
        else if (pos == 4) {
          if (ch == ';')
            string.replace(start, i + 1, firstChar == 'a' ? "'" : "\"");

          start = -1;
        }
      }
      else if (ch == '&') {
        start = i;
        pos = 0;
      }
    }

    return string;
  }

  /**
   * Returns the XML-escaped {@code string}. The escaped characters are:
   *
   * <pre>
   * {@code From |  To
   * -------------
   *   &  | &amp;
   *   '  | &apos;
   *   "  | &quot;
   *   >  | &gt;
   *   <  | &lt;}
   * </pre>
   *
   * @param string The string to escape.
   * @return The XML-escaped {@code string}.
   */
  public static String escape(final String string) {
    return escape(new StringBuilder(string)).toString();
  }

  /**
   * Returns the XML-unescaped {@code string}. The unescaped characters are:
   *
   * <pre>
   * {@code From   | To
   * ------------
   * &amp;  | &
   * &apos; | '
   * &quot; | "
   * &gt;   | >
   * &lt;   | <}
   * </pre>
   *
   * @param string The string to unescape.
   * @return The XML-unescaped {@code string}.
   */
  public static String unescape(final String string) {
    return unescape(new StringBuilder(string)).toString();
  }

  private CharacterDatas() {
  }
}