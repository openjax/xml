/* Copyright (c) 2017 FastJAX
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

package org.fastjax.xml;

public final class CharacterDatas {
  public static StringBuilder escape(final StringBuilder string) {
    for (int i = 0; i < string.length(); i++) {
      final char ch = string.charAt(i);
      if (ch == '&')
        string.replace(i, i + 1, "&amp;");
      else if (ch == '\'')
        string.replace(i, i + 1, "&apos;");
      else if (ch == '>')
        string.replace(i, i + 1, "&gt;");
      else if (ch == '<')
        string.replace(i, i + 1, "&lt;");
      else if (ch == '"')
        string.replace(i, i + 1, "&quot;");
    }

    return string;
  }

  public static StringBuilder unescape(final StringBuilder string) {
    char firstChar = '\0';
    int start = -1;
    int pos = 0;
    for (int i = 0; i < string.length(); i++) {
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
          if (ch == ';') {
            string.replace(start, i + 1, firstChar == 'a' ? "'" : "\"");
            start = -1;
          }
          else {
            start = -1;
          }
        }
      }
      else if (ch == '&') {
        start = i;
        pos = 0;
      }
    }

    return string;
  }

  public static String escape(final String string) {
    return escape(new StringBuilder(string)).toString();
  }

  public static String unescape(final String string) {
    return unescape(new StringBuilder(string)).toString();
  }

  private CharacterDatas() {
  }
}