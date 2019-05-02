/* Copyright (c) 2015 OpenJAX
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

package org.openjax.xml.datatype;

import static org.junit.Assert.*;

import org.junit.Test;

public class LanguageTest {
  @Test
  public void testLanguage() {
    assertNull(Language.parse(null));

    try {
      Language.parse("");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Language.parse("11");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Language.parse("superlong");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Language.parse("witha1");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Language.parse("witha-another#");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Language.parse("witha-and-another#");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      Language.parse("721-fhajdo-f1h");
      fail("Expected a IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    final String[] languages = new String[] {"art-lojban", "az-Arab", "az-Cyrl", "az-Latn", "be-Latn", "bs-Cyrl", "bs-Latn", "cel-gaulish", "de-1901", "de-1996", "de-AT-1901", "de-AT-1996", "de-CH-1901", "de-CH-1996", "de-DE-1901", "de-DE-1996", "en-boont", "en-GB-oed", "en-scouse", "es-419", "i-ami", "i-bnn", "i-default", "i-enochian", "i-hak", "i-klingon", "i-lux", "i-mingo", "i-navajo", "i-pwn", "i-tao", "i-tay", "i-tsu", "iu-Cans", "iu-Latn", "mn-Cyrl", "mn-Mong", "no-bok", "no-nyn", "sgn-BE-fr", "sgn-BE-nl", "sgn-BR", "sgn-CH-de", "sgn-CO", "sgn-DE", "sgn-DK", "sgn-ES", "sgn-FR", "sgn-GB", "sgn-GR", "sgn-IE", "sgn-IT", "sgn-JP", "sgn-MX", "sgn-NL", "sgn-NO", "sgn-PT", "sgn-SE", "sgn-US", "sgn-ZA", "sl-rozaj", "sr-Cyrl", "sr-Latn", "tg-Arab", "tg-Cyrl", "uz-Cyrl", "uz-Latn", "yi-latn", "zh-cmn", "zh-cmn-Hans", "zh-cmn-Hant", "zh-gan", "zh-guoyu", "zh-hakka", "zh-Hans", "zh-Hans-CN", "zh-Hans-HK", "zh-Hans-MO", "zh-Hans-SG", "zh-Hans-TW", "zh-Hant", "zh-Hant-CN", "zh-Hant-HK", "zh-Hant-MO", "zh-Hant-SG", "zh-Hant-TW", "zh-min", "zh-min-nan", "zh-wuu", "zh-xiang", "zh-yue"};
    for (final String language : languages)
      assertEquals(language, Language.parse(language).toString());
  }
}