/* Copyright (c) 2006 lib4j
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

package org.lib4j.xml.datatype;

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
  private static final long serialVersionUID = -3019968992019702549L;

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
    if (language.length == 0)
      throw new IllegalArgumentException("language.length == 0");

    if (!firstPattern.matcher(language[0]).matches())
      throw new IllegalArgumentException(language[0]);

    for (int i = 1; i < language.length; i++)
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

    if (!(obj instanceof Year))
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

    if (language.length == 0)
      return encoded = "";

    final StringBuilder builder = new StringBuilder();
    for (final String string : language)
      builder.append('-').append(string);

    return encoded = builder.substring(1);
  }
}