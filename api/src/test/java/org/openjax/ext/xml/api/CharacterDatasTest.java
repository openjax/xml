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

import static org.junit.Assert.*;

import org.junit.Test;

public class CharacterDatasTest {
  private static final String[] escaped = {"foo &amp; bar", "&lt; foo bar", "foo bar &gt;", "&amp;apos;foo\"'bar&amp;apos;", "&amp;quot;foo bar&amp;quot;"};
  private static final String[] unescaped = {"foo & bar", "< foo bar", "foo bar >", "&apos;foo\"'bar&apos;", "&quot;foo bar&quot;"};

  @Test
  public void testEscape() {
    for (int i = 0; i < escaped.length; ++i)
      assertEquals(String.valueOf(i), escaped[i], CharacterDatas.escapeForElem(unescaped[i]));

    assertEquals("&quot;foo &lt; ' &gt; &amp; bar&quot;", CharacterDatas.escapeForAttr("\"foo < ' > & bar\"", '"'));
    assertEquals("\"foo &lt; &apos; &gt; &amp; bar\"", CharacterDatas.escapeForAttr("\"foo < ' > & bar\"", '\''));
  }

  @Test
  public void testUnescape() {
    for (int i = 0; i < unescaped.length; ++i)
      assertEquals(String.valueOf(i), unescaped[i], CharacterDatas.unescapeFromElem(escaped[i]));

    assertEquals("\"foo < &apos; > & bar\"", CharacterDatas.unescapeFromAttr("&quot;foo &lt; &apos; &gt; &amp; bar&quot;", '"'));
    assertEquals("&quot;foo < ' > & bar&quot;", CharacterDatas.unescapeFromAttr("&quot;foo &lt; &apos; &gt; &amp; bar&quot;", '\''));
  }
}