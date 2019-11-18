/* Copyright (c) 2018 OpenJAX
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

package org.openjax.xml.sax;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.libj.io.ReplayReader;
import org.xml.sax.InputSource;

/**
 * Utility functions for operations pertaining to the {@code org.xml.sax}
 * package.
 */
final class SAXUtil {
  /**
   * Returns a string encoding of the specified {@code attributes}. The encoding
   * is of the form:
   *
   * <pre>name = "value"</pre>
   *
   * @param attributes The map of attributes.
   * @return A string encoding of the specified {@code attributes}.
   */
  static String toString(final Map<QName,String> attributes) {
    if (attributes == null)
      return null;

    if (attributes.size() == 0)
      return "";

    final StringBuilder builder = new StringBuilder();
    final Iterator<Map.Entry<QName,String>> iterator = attributes.entrySet().iterator();
    for (int i = 0; iterator.hasNext(); ++i) {
      if (i > 0)
        builder.append(' ');

      final Map.Entry<QName,String> entry = iterator.next();
      final QName name = entry.getKey();
      if (name.getPrefix().length() > 0)
        builder.append(name.getPrefix()).append(':');

      builder.append(name.getLocalPart());
      builder.append("=\"").append(entry.getValue()).append('"');
    }

    return builder.toString();
  }

  static ReplayReader getReader(final InputSource inputSource) {
    if (inputSource.getCharacterStream() != null)
      return new ReplayReader(inputSource.getCharacterStream());

    if (inputSource.getByteStream() != null)
      // FIXME: Determine the encoding from the element declaration
      return new ReplayReader(new InputStreamReader(inputSource.getByteStream()));

    throw new IllegalArgumentException("InputSource has null CharacterStream and ByteStream");
  }

  static InputSource toInputSource(final String systemId, final InputStream in) {
    final InputSource inputSource = new InputSource(systemId);
    inputSource.setCharacterStream(new ReplayReader(new InputStreamReader(in)));
    return inputSource;
  }

  private SAXUtil() {
  }
}