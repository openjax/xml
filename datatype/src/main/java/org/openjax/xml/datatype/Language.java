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

package org.openjax.xml.datatype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * http://www.w3.org/TR/xmlschema11-2/#language
 */
public class Language implements CharSequence, Serializable {
  public static String print(final Language language) {
    return language == null ? null : language.toString();
  }

  public static Language parse(String string) {
    if (string == null)
      return null;

    string = string.trim();
    if (string.length() < LANGUAGE_FRAG_MIN_LENGTH)
      throw new IllegalArgumentException(string);

    final StringTokenizer tokenizer = new StringTokenizer(string, "-");
    final Collection<String> languages = new ArrayList<>();
    while (tokenizer.hasMoreTokens())
      languages.add(tokenizer.nextToken());

    return new Language(languages);
  }

  private static final Pattern firstPattern = Pattern.compile("[a-zA-Z]{1,8}");
  private static final Pattern otherPattern = Pattern.compile("[a-zA-Z0-9]{1,8}");
  private static final int LANGUAGE_FRAG_MIN_LENGTH = 1;
  private final String[] language;
  private String encoded;

  public Language(final String ... language) {
    final int len = language.length;
    if (len == 0)
      throw new IllegalArgumentException("language.length == 0");

    final String langudage0 = language[0];
    if (!firstPattern.matcher(langudage0).matches())
      throw new IllegalArgumentException(langudage0);

    for (int i = 1; i < len; ++i) // [A]
      if (!otherPattern.matcher(language[i]).matches())
        throw new IllegalArgumentException(language[i]);

    this.language = language;
  }

  public Language(final Collection<String> language) {
    this(language.toArray(new String[language.size()]));
  }

  public String[] getLanguage() {
    return language;
  }

  @Override
  public int length() {
    return toString().length();
  }

  @Override
  public char charAt(final int index) {
    return toString().charAt(index);
  }

  @Override
  public CharSequence subSequence(final int start, final int end) {
    return toString().subSequence(start, end);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Language))
      return false;

    final Language that = (Language)obj;
    return Arrays.equals(this.language, that.language);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(language);
  }

  @Override
  public String toString() {
    if (encoded != null)
      return encoded;

    final int i$ = language.length;
    if (i$ == 0)
      return encoded = "";

    final StringBuilder b = new StringBuilder();
    for (int i = 0; i < i$; ++i) { // [A]
      if (i > 0)
        b.append('-');

      b.append(language[i]);
    }

    return encoded = b.toString();
  }
}