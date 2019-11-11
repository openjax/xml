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

/**
 * Faster handler for parsing events from {@link FastSAXParser}, which declares
 * callback methods that provide data lengths (to be manually dereferenced to
 * actual names and values).
 * <p>
 * The callback methods in this handler are length values, which are to be
 * re-read from the source {@link java.io.Reader} providing the stream of data
 * that is being parsed. Implementations of this interface are expected to
 * possess a reference to the source {@link java.io.Reader} to be able to
 * re-read the contents.
 *
 * @see FastSAXParser
 * @see FastSAXHandler
 */
public interface FasterSAXHandler {
  /**
   * Callback method for DOCTYPE blocks (i.e. {@code <!DOCTYPE [ ]>}).
   *
   * @param doctypeLen The length of the {@code DOCTYPE [ ]} string, sans
   *          {@code <!} and {@code >}.
   * @return Whether parsing should continue.
   * @throws IOException If an I/O error has occurred.
   */
  default boolean doctype(int doctypeLen) throws IOException {
    return true;
  };

  /**
   * Callback method for comment blocks (i.e. {@code <!-- COMMENT -->}).
   *
   * @param commentLen The length of the {@code COMMENT} string, sans
   *          {@code <!--} and {@code -->}.
   * @return Whether parsing should continue.
   * @throws IOException If an I/O error has occurred.
   */
  default boolean comment(int commentLen) throws IOException {
    return true;
  };

  /**
   * Callback method for attribute occurrences.
   *
   * @param prefixLen The length of the prefix part of the attribute name, which
   *          is {@code == 0} if the attribute name does not have a prefix, and
   *          {@code >= 2} if the attribute name does not has a prefix, as this
   *          includes the {@code ':'} character.
   * @param localPartLen The length of the local part of the attribute name.
   * @param skip The length of the data to skip, which matches the regex:
   *          {@code "[ \n\r\t]*=[ \n\r\t]*\""}.
   * @param valueLen The length of the attribute value (does not include the
   *          surrounding quotes).
   * @return Whether parsing should continue.
   * @throws IOException If an I/O error has occurred.
   */
  default boolean attribute(int prefixLen, int localPartLen, int skip, int valueLen) throws IOException {
    return true;
  };

  /**
   * Called when an element's "start tag" is opened with a {@code '<'} character.
   *
   * @param prefixLen The length of the prefix part of the element name
   *          (including the {@code ':'} character).
   * @param localPartLen The length of the local part of the element name.
   * @return Whether parsing should continue.
   * @throws IOException If an I/O error has occurred.
   */
  default boolean startElement(int prefixLen, int localPartLen) throws IOException {
    return true;
  };

  /**
   * Called when an element's "start tag" is closed with a {@code '>'} character.
   *
   * @return Whether parsing should continue.
   * @throws IOException If an I/O error has occurred.
   */
  default boolean startElement() throws IOException {
    return true;
  };

  /**
   * Called when an element's "end tag" is encountered.
   * <p>
   * The "end tag" is either a dedicated tag that resembles {@code "</ELEMENT>"}
   * for elements that may have child elements, or can be the "start tag" that
   * ends with {@code "/>"} for elements that have no child elements.
   *
   * @return Whether parsing should continue.
   * @throws IOException If an I/O error has occurred.
   */
  default boolean endElement() throws IOException {
    return true;
  };
}