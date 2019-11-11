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

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

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
   * <pre>
   * name = "value"
   * </pre>
   *
   * @param attributes The {@code Attributes}.
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

  static ReplayReader getReader(final StreamSource streamSource) {
    if (streamSource.getReader() != null)
      return new ReplayReader(streamSource.getReader());

    if (streamSource.getInputStream() != null)
      // TODO: Determine the encoding from the element declaration
      return new ReplayReader(new InputStreamReader(streamSource.getInputStream()));

    throw new IllegalArgumentException("StreamSource has null Reader and InputStream");
  }

  static InputSource getInputSource(final URL url) throws IOException {
    final InputSource inputSource = new InputSource(url.toString());
    inputSource.setCharacterStream(new ReplayReader(new InputStreamReader(url.openStream())));
    return inputSource;
  }

  static InputSource getInputSource(final StreamSource streamSource) {
    final InputSource inputSource = new InputSource(streamSource.getSystemId());
    inputSource.setCharacterStream(SAXUtil.getReader(streamSource));
    return inputSource;
  }

  private SAXUtil() {
  }
}