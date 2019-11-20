/* Copyright (c) 2019 OpenJAX
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
import java.io.Reader;

/**
 * Fast SAX parser for XML data.
 * <p>
 * This parser performs as few operations as necessary to parse well-formed XML
 * documents.
 * <p>
 * <i><b>Note:</b> This parser does not perform any validation.</i>
 *
 * @see FasterSAXHandler
 */
public final class FastSAXParser {
  private static final int DEFAULT_READ_LIMIT = 8192;

  /**
   * Parse the data provided by the input stream, and handle parse events with
   * the specified {@link FasterSAXHandler}.
   * <p>
   * <i><b>Note:</b> The provided input stream must support
   * {@link Reader#mark(int)}.</i>
   *
   * @param in The {@link Reader} input stream.
   * @param handler The {@link FasterSAXHandler}.
   * @throws IOException If the input stream does not support
   *           {@link Reader#mark(int)}, or if some other I/O error has
   *           occurred.
   * @throws NullPointerException If the specified {@link Reader} or
   *           {@link FasterSAXHandler} is null.
   * @see java.io.BufferedInputStream
   * @see org.libj.io.ReplayReader
   */
  public static void parse(final Reader in, final FasterSAXHandler handler) throws IOException {
    char skipToNext = '\0';
    boolean inElement = false;
    int startElem = -1;
    int startAttr = -1;
    int startValue = -1;
    boolean inComment = false;
    boolean inQuote = false;
    boolean inDeclaration = false;
    boolean inDoctype = false;
    int prefixLen = 0;
    int attrNameLen = 0;
    in.mark(DEFAULT_READ_LIMIT);
    handler.startDocument();
    for (int ch0, ch1 = '\0', ch2 = '\0', i = 0; (ch0 = in.read()) != -1; ++i) {
      if (skipToNext != '\0') {
        if (ch0 == skipToNext)
          skipToNext = '\0';
      }
      else if (inComment) {
        if (ch0 == '>' && (ch1 == ']' || ch1 == '-' && ch2 == '-')) {
          in.reset();
          final int len;
          if (inDoctype) {
            in.read();
            if (!handler.doctype((len = i - startElem) - 2))
              return;
          }
          else {
            in.read();
            in.read();
            in.read();
            if (!handler.comment((len = i - startElem) - 6))
              return;
          }

          in.reset();
          in.skip(len);
          inComment = false;
          inDoctype = false;
        }
        else if (ch2 == '<' && ch1 == '!' && ch0 == 'D') {
          inDoctype = true;
        }
      }
      else if (ch1 == '<') {
        if (ch0 == '/') {
          if (!handler.endElement())
            return;

          skipToNext = '>';
          inElement = false;
        }
        else if (ch0 == '!') {
          inElement = false;
          inComment = true;
        }
        else if (ch0 == '?') {
          inDeclaration = true;
          startElem = i;
          in.mark(DEFAULT_READ_LIMIT);
        }
      }
      else if (inElement) {
        final boolean isWs = !inQuote && Character.isWhitespace(ch0);
        if (ch0 == ':' && !inQuote) {
          if (startElem != -1)
            prefixLen = i - startElem;
          else if (startAttr != -1)
            prefixLen = i - startAttr;
          else
            throw new IllegalStateException();
        }
        else if (ch0 == '>' || ch0 == '=' || ch0 == '"' || isWs) {
          if (startElem != -1) {
            in.reset();
            final int localName = i - startElem - prefixLen - 1;
            if (inDeclaration ? !handler.startDeclaration(localName) : !handler.startElement(prefixLen, localName))
              return;

            in.reset();
            in.skip(prefixLen + localName + 1);
            startElem = -1;
            prefixLen = 0;
          }
          else if (startAttr != -1 && startAttr != i - 1 && ch1 != '/' && (!inDeclaration || ch1 != '?')) {
            if (attrNameLen == 0) {
              attrNameLen = i - startAttr - prefixLen - 1;
            }
            else if (ch0 == '"') {
              if (!inQuote && startValue == -1) {
                startValue = i;
              }
              else {
                in.reset();
                final int skip = startValue - startAttr - prefixLen - attrNameLen;
                final int value = i - startValue - 1;
                if (!handler.attribute(prefixLen, attrNameLen, skip, value))
                  return;

                final int len = prefixLen + attrNameLen + skip + value;
                in.reset();
                in.skip(len + 1);
                startElem = -1;
                prefixLen = 0;
                attrNameLen = 0;
                startAttr = -1;
                startValue = -1;
              }
            }
          }

          if (ch0 == '>') {
            inElement = false;
            startAttr = -1;
            // FIXME: Should we assert that ch1 == '?' here?
            // FIXME: Or are we guaranteed that inDeclaration is enough knowledge?
            if (inDeclaration) {
              inDeclaration = false;
              if (!handler.endDeclaration())
                return;
            }
            else if (!handler.startElement()) {
              return;
            }

            if (ch1 == '/' && !handler.endElement())
              return;
          }

          if (attrNameLen == 0 && isWs) {
            startAttr = i;
            in.mark(DEFAULT_READ_LIMIT);
          }
        }
      }
      else if (ch0 == '<') {
        inElement = true;
        startElem = i;
        in.mark(DEFAULT_READ_LIMIT);
      }

      ch2 = ch1;
      ch1 = ch0;
      if (!inComment && ch0 == '"')
        inQuote = !inQuote;

//      System.err.print((char)ch0);
    }

    handler.endDocument();
  }

  private FastSAXParser() {
  }
}