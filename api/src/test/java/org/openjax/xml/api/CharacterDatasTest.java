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

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterDatasTest {
  private static final String[] escaped = {"+?&lt;2&amp;v1d6KjT", "qNT|&amp;H/6th.", "foo &amp; bar", "&lt; foo bar", "foo bar &gt;", "&amp;apos;foo\"'bar&amp;apos;", "&amp;quot;foo bar&amp;quot;", "&amp;amp;amp&amp;amp"};
  private static final String[] unescaped = {"+?<2&v1d6KjT", "qNT|&H/6th.", "foo & bar", "< foo bar", "foo bar >", "&apos;foo\"'bar&apos;", "&quot;foo bar&quot;", "&amp;amp&amp"};

  private static void testEscapeElem(final int message, final String expected, final String in) {
    assertEquals(String.valueOf(message), expected, CharacterDatas.escapeForElem(new StringBuilder(), in).toString());
    assertEquals(String.valueOf(message), expected, CharacterDatas.escapeForElem(new StringBuilder(), in.toCharArray()).toString());
  }

  private static void testEscapeAttr(final String expected, final String in, final char quote) {
    assertEquals(expected, CharacterDatas.escapeForAttr(new StringBuilder(), in, quote).toString());
    assertEquals(expected, CharacterDatas.escapeForAttr(new StringBuilder(), in.toCharArray(), quote).toString());
  }

  private static void testUnescapeAttr(final String expected, final String in, final char quote) {
    assertEquals(expected, CharacterDatas.unescapeFromAttr(new StringBuilder(), in, quote).toString());
    assertEquals(expected, CharacterDatas.unescapeFromAttr(new StringBuilder(), in.toCharArray(), quote).toString());
  }

  private static void testUnescapeElem(final int message, final String expected, final String in) {
    assertEquals(String.valueOf(message), expected, CharacterDatas.unescapeFromElem(new StringBuilder(), in).toString());
    assertEquals(String.valueOf(message), expected, CharacterDatas.unescapeFromElem(new StringBuilder(), in.toCharArray()).toString());
  }

  @Test
  public void testEscape() {
    for (int i = 0; i < escaped.length; ++i) // [A]
      testEscapeElem(i, escaped[i], unescaped[i]);

    testEscapeAttr("&quot;foo &lt; ' &gt; &amp; bar&quot;", "\"foo < ' > & bar\"", '"');
    testEscapeAttr("\"foo &lt; &apos; &gt; &amp; bar\"", "\"foo < ' > & bar\"", '\'');
  }

  @Test
  public void testUnescape() {
    for (int i = 0; i < unescaped.length; ++i) // [A]
      testUnescapeElem(i, unescaped[i], escaped[i]);

    testUnescapeAttr("\"foo < &apos; > & bar\"", "&quot;foo &lt; &apos; &gt; &amp; bar&quot;", '"');
    testUnescapeAttr("&quot;foo < ' > & bar&quot;", "&quot;foo &lt; &apos; &gt; &amp; bar&quot;", '\'');
  }
}