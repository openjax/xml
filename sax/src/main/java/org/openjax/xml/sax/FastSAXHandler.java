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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.xml.namespace.QName;

/**
 * Fast handler for parsing events from {@link FastSAXParser}, which declares
 * callback methods that provide actual string names and values.
 *
 * @see FastSAXParser
 * @see FasterSAXHandler
 */
public abstract class FastSAXHandler implements FasterSAXHandler {
  private static final class Element {
    private final String prefix;
    private final String localName;

    private Element(final String prefix, final String localName) {
      this.prefix = prefix;
      this.localName = localName;
    }

    private String namespace;
    private QName name;

    private Map<String,String> prefixToNamespace;

    private Map<String,String> prefixToNamespace() {
      return prefixToNamespace == null ? prefixToNamespace = new HashMap<>() : prefixToNamespace;
    }

    private ArrayList<String[]> attributes;

    private ArrayList<String[]> attributes() {
      return attributes == null ? attributes = new ArrayList<>() : attributes;
    }
  }

  protected Reader reader;
  private ArrayList<Element> stack;

  /**
   * Creates a new {@link FastSAXHandler} with the specified input stream.
   *
   * @param reader The input stream.
   * @throws NullPointerException If the specified input stream is null.
   */
  public FastSAXHandler(final Reader reader) {
    this.reader = Objects.requireNonNull(reader);
  }

  /**
   * Creates a new {@link FastSAXHandler} with a null input stream.
   */
  protected FastSAXHandler() {
  }

  private static final int DEFAULT_BUFFER_SIZE = 64;
  private static final double RESIZE_FACTOR = 1.5;
  private char[] buf = new char[DEFAULT_BUFFER_SIZE];

  @SuppressWarnings("unused")
  private String read(final Reader in, final int len) throws IOException {
    if (len >= buf.length) {
      final char[] resized = new char[RESIZE_FACTOR < 0 ? len - (int)RESIZE_FACTOR : (int)((len + 1) * RESIZE_FACTOR)];
      System.arraycopy(buf, 0, resized, 0, buf.length);
      buf = resized;
    }

    in.read(buf, 0, len);
    return new String(buf, 0, len);
  }

  private String lookupNamespace(final String prefix) {
    if (stack != null) {
      for (int i = stack.size() - 1; i >= 0; --i) {
        final Element element = stack.get(i);
        final String namespace;
        if (element.prefixToNamespace != null && (namespace = element.prefixToNamespace.get(prefix)) != null)
          return namespace;
      }
    }

    return null;
  }

  private boolean inDeclaration;

  @Override
  public final boolean startDeclaration(final int nameLen) throws IOException {
    inDeclaration = true;
    return true;
  }

  @Override
  public final boolean endDeclaration() throws IOException {
    inDeclaration = false;
    return true;
  }

  @Override
  public final boolean attribute(final int prefixLen, final int localPartLen, final int skip, final int valueLen) throws IOException {
    if (inDeclaration)
      return true;

    final String prefix;
    if (prefixLen > 0) {
      prefix = read(reader, prefixLen - 1);
      reader.read();
    }
    else {
      prefix = null;
    }

    final String localName = read(reader, localPartLen);
    reader.skip(skip);
    final String value = read(reader, valueLen);
    final Element element = stack.get(stack.size() - 1);
    if (prefixLen == 0 && "xmlns".equals(localName)) {
      element.prefixToNamespace().put("", value);
    }
    else if ("xmlns".equals(prefix)) {
      element.prefixToNamespace().put(localName, value);
    }

    element.attributes().add(new String[] {prefix != null ? prefix : "", localName, value});
    return true;
  }

  @Override
  public final boolean startElement(final int prefixLen, final int localPartLen) throws IOException {
    final String prefix;
    if (prefixLen > 0) {
      prefix = read(reader, prefixLen - 1);
      reader.read();
    }
    else {
      prefix = "";
    }

    if (stack == null)
      stack = new ArrayList<>();

    stack.add(new Element(prefix, read(reader, localPartLen)));
    return true;
  }

  @Override
  public final boolean startElement() throws IOException {
    final Element element = stack.get(stack.size() - 1);
    if (element.namespace == null)
      element.namespace = lookupNamespace(element.prefix);

    final Map<QName,String> attributes;
    if (element.attributes == null) {
      attributes = null;
    }
    else {
      attributes = new HashMap<>();
      for (final String[] attribute : element.attributes) {
        final QName key = new QName(lookupNamespace(attribute[0]), attribute[1], attribute[0]);
        attributes.put(key, attribute[2]);
      }
    }

    element.name = new QName(element.namespace, element.localName, element.prefix);
    return startElement(element.name, attributes);
  }

  @Override
  public final boolean endElement() throws IOException {
    return endElement(stack.remove(stack.size() - 1).name);
  }

  /**
   * Callback method for element start tags.
   *
   * @param name The qualified name of the element.
   * @param attributes A map of the attribute names to values, or {@code null}
   *          if the element does not have attributes.
   * @return Whether parsing should continue.
   * @throws IOException If an I/O error has occurred.
   */
  public abstract boolean startElement(QName name, Map<QName,String> attributes) throws IOException;

  /**
   * Callback method for element end tags.
   *
   * @param name The qualified name of the element.
   * @return Whether parsing should continue.
   * @throws IOException If an I/O error has occurred.
   */
  public boolean endElement(final QName name) throws IOException {
    return true;
  }

  /**
   * Resets the local variables in this handler, so it can be used in another
   * parsing invocation.
   */
  public void reset() {
    if (this.stack != null)
      this.stack.clear();

    this.inDeclaration = false;
  }
}