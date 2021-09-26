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

package org.openjax.xml.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Lightweight encapsulation of an XML element, supporting attributes, content,
 * and child elements.
 * <p>
 * Attributes are represented with a raw-type {@link Map}, and
 * {@code key.toString()} and {@code value.toString()} are used to marshal to
 * string.
 * <p>
 * Child elements are represented with a raw-type {@link Collection}, and
 * {@code element.toString()} is used to marshal to string.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class XmlElement implements Cloneable, Serializable {
  private static final Pattern qName = Pattern.compile("^[a-zA-Z_][\\w.-]*(:[a-zA-Z_][\\w.-]*)?$");

  /**
   * Asserts the specified string is a valid <a href=
   * "https://www.w3.org/TR/1999/REC-xml-names-19990114/#dt-qname">xs:qName</a>.
   *
   * @param name The string.
   * @return The specified string.
   * @throws IllegalArgumentException If {@code name} is null, or if
   *           {@code name} is not a valid <a href=
   *           "https://www.w3.org/TR/1999/REC-xml-names-19990114/#dt-qname">xs:qName</a>.
   */
  private static String requireQName(final Object name) {
    CharacterDatas.assertNotNull(name, "name is null");
    final String string = String.valueOf(name);
    if (string.length() == 0 || !qName.matcher(string).matches())
      throw new IllegalArgumentException(string + " is not a valid xs:QName");

    return string;
  }

  private final String name;
  private Map attributes;
  private Collection elements;

  /**
   * Creates a new {@link XmlElement} with the specified name, map of
   * attributes, and collection of child elements.
   *
   * @param name The name.
   * @param attributes The attributes.
   * @param elements The child elements.
   * @throws IllegalArgumentException If {@code name} is null, or if
   *           {@code name} is not a valid <a href=
   *           "https://www.w3.org/TR/1999/REC-xml-names-19990114/#dt-qname">xs:qName</a>.
   */
  public XmlElement(final String name, final Map attributes, final Collection elements) {
    this.name = requireQName(name);
    this.attributes = attributes;
    this.elements = elements;
  }

  /**
   * Creates a new {@link XmlElement} with the specified name and map of
   * attributes.
   *
   * @param name The name.
   * @param attributes The attributes.
   * @throws IllegalArgumentException If {@code name} is null, or if
   *           {@code name} is not a valid <a href=
   *           "https://www.w3.org/TR/1999/REC-xml-names-19990114/#dt-qname">xs:qName</a>.
   */
  public XmlElement(final String name, final Map attributes) {
    this(name, attributes, null);
  }

  /**
   * Creates a new {@link XmlElement} with the specified name and collection of
   * child elements.
   *
   * @param name The name.
   * @param elements The child elements.
   * @throws IllegalArgumentException If {@code name} is null, or if
   *           {@code name} is not a valid <a href=
   *           "https://www.w3.org/TR/1999/REC-xml-names-19990114/#dt-qname">xs:qName</a>.
   */
  public XmlElement(final String name, final Collection elements) {
    this(name, null, elements);
  }

  /**
   * Creates a new {@link XmlElement} with the specified name.
   *
   * @param name The name.
   * @throws IllegalArgumentException If {@code name} is null., or if
   *           {@code name} is not a valid <a href=
   *           "https://www.w3.org/TR/1999/REC-xml-names-19990114/#dt-qname">xs:qName</a>.
   */
  public XmlElement(final String name) {
    this(name, null, null);
  }

  /**
   * Returns the name of this element.
   *
   * @return The name of this element.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the attributes for this element.
   *
   * @param attributes The attributes.
   */
  public void setAttributes(final Map attributes) {
    this.attributes = attributes;
  }

  /**
   * Returns the attributes of this element.
   *
   * @return The attributes of this element.
   */
  public Map getAttributes() {
    return this.attributes;
  }

  /**
   * Sets the child elements for this element.
   *
   * @param elements The child elements.
   */
  public void setElements(final Collection elements) {
    this.elements = elements;
  }

  /**
   * Returns the child elements of this element.
   *
   * @return The child elements of this element.
   */
  public Collection getElements() {
    return this.elements;
  }

  @Override
  public XmlElement clone() {
    try {
      final XmlElement clone = (XmlElement)super.clone();
      if (attributes != null)
        clone.attributes = new HashMap(attributes);

      if (elements != null)
        clone.elements = new ArrayList(elements);

      return clone;
    }
    catch (final CloneNotSupportedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof XmlElement))
      return false;

    final XmlElement that = (XmlElement)obj;
    return name.equals(that.name) && Objects.equals(attributes, that.attributes) && (elements == null ? that.elements == null : that.elements != null && elements.size() == that.elements.size() && elements.equals(that.elements));
  }

  @Override
  public int hashCode() {
    int hashCode = 31 + name.hashCode();
    if (attributes != null)
      hashCode = 31 * hashCode + attributes.hashCode();

    if (elements != null)
      hashCode = 31 * hashCode + elements.hashCode();

    return hashCode;
  }

  /**
   * Returns an XML string representation of this element with the specified
   * number of spaces to indent child elements.
   *
   * @param indent Number of spaces to indent child elements. If the specified
   *          indent value is greater than {@code 0}, child elements are
   *          indented and placed on a new line. If the indent value is
   *          {@code 0}, child elements are not indented, nor placed on a new
   *          line.
   * @return An XML string representation of this element.
   * @throws IllegalArgumentException If a child element is null, or the name or
   *           value of an attribute is null, or if the name of an attribute is
   *           not a valid <a href=
   *           "https://www.w3.org/TR/1999/REC-xml-names-19990114/#dt-qname">xs:qName</a>,
   *           or if {@code indent} is negative.
   * @throws StackOverflowError If the graph of child elements has cycles.
   */
  public String toString(final int indent) {
    if (indent < 0)
      throw new IllegalArgumentException("indent (" + indent + ") must be non-negative");

    final StringBuilder builder = new StringBuilder("<");
    builder.append(name);
    if (attributes != null && attributes.size() > 0) {
      final StringBuilder value = new StringBuilder();
      for (final Map.Entry entry : (Set<Map.Entry>)attributes.entrySet()) {
        final String name = requireQName(entry.getKey());
        value.append(CharacterDatas.assertNotNull(entry.getValue(), "name is null"));
        builder.append(' ').append(name).append("=\"");
        CharacterDatas.escapeForAttr(builder, value, '"');
        value.setLength(0);
        builder.append('"');
      }
    }

    if (elements == null || elements.size() == 0)
      return builder.append("/>").toString();

    builder.append('>');
    if (indent == 0) {
      for (final Object element : elements) {
        builder.append(element.toString());
      }
    }
    else {
      final char[] chars = new char[indent + 1];
      Arrays.fill(chars, 1, chars.length, ' ');
      chars[0] = '\n';
      final String delim = new String(chars);
      for (final Object element : elements) {
        final String string = element instanceof XmlElement ? ((XmlElement)element).toString(indent) : element.toString();
        builder.append(chars).append(string.replace("\n", delim));
      }

      builder.append('\n');
    }

    return builder.append("</").append(name).append('>').toString();
  }

  /**
   * Returns an XML string representation of this element with no indentation.
   *
   * @return An XML string representation of this element.
   * @throws IllegalArgumentException If the name of an attribute is null, or if
   *           the name of an attribute is not a valid <a href=
   *           "https://www.w3.org/TR/1999/REC-xml-names-19990114/#dt-qname">xs:qName</a>.
   * @throws StackOverflowError If the graph of child elements has cycles.
   */
  @Override
  public String toString() {
    return toString(0);
  }
}