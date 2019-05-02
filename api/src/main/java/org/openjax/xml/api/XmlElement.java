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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Lightweight encapsulation of an XML element.
 */
public class XmlElement implements Cloneable, Serializable {
  private static final long serialVersionUID = 3766554839993594188L;
  private final String name;
  private Map<String,Object> attributes;
  private Collection<XmlElement> elements;

  /**
   * Creates a new {@code XmlElement} with the specified parameters.
   *
   * @param name The name.
   * @param attributes The attributes.
   * @param elements The child elements.
   */
  public XmlElement(final String name, final Map<String,Object> attributes, final Collection<XmlElement> elements) {
    this.name = name;
    if (name == null || name.length() == 0)
      throw new IllegalArgumentException("name == " + name);

    this.attributes = attributes;
    this.elements = elements;
  }

  /**
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
  public void setAttributes(final Map<String,Object> attributes) {
    this.attributes = attributes;
  }

  /**
   * @return The attributes of this element.
   */
  public Map<String,Object> getAttributes() {
    return this.attributes;
  }

  /**
   * Sets the child elements for this element.
   *
   * @param elements The child elements.
   */
  public void setElements(final Collection<XmlElement> elements) {
    this.elements = elements;
  }

  /**
   * @return The child elements of this element.
   */
  public Collection<XmlElement> getElements() {
    return this.elements;
  }

  @Override
  public XmlElement clone() {
    try {
      final XmlElement clone = (XmlElement)super.clone();
      if (attributes != null)
        clone.attributes = new HashMap<>(attributes);

      if (elements != null)
        clone.elements = new ArrayList<>(elements);

      return clone;
    }
    catch (final CloneNotSupportedException e) {
      throw new UnsupportedOperationException(e);
    }
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof XmlElement))
      return false;

    final XmlElement that = (XmlElement)obj;
    return name.equals(that.name) && (attributes == null ? that.attributes == null : attributes.equals(that.attributes)) && (elements == null ? that.elements == null : that.elements != null && elements.size() == that.elements.size() && elements.containsAll(that.elements));
  }

  @Override
  public int hashCode() {
    int hashCode = 9;
    hashCode ^= 31 * name.hashCode();
    if (attributes != null)
      hashCode ^= 31 * attributes.hashCode();

    if (elements != null)
      hashCode ^= 31 * elements.hashCode();

    return hashCode;
  }

  /**
   * Returns an XML string representation of this element.
   *
   * @return An XML string representation of this element.
   * @throws StackOverflowError If the graph of child elements has cycles.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder("<");
    builder.append(name);
    if (attributes != null && attributes.size() > 0) {
      for (final Map.Entry<String,Object> entry : attributes.entrySet()) {
        builder.append(' ').append(entry.getKey()).append("=\"");
        if (entry.getValue() != null)
          builder.append(CharacterDatas.escapeForAttr(new StringBuilder(String.valueOf(entry.getValue())), '"'));
        else
          builder.append("null");

        builder.append('"');
      }
    }

    if (elements == null || elements.size() == 0)
      return builder.append("/>").toString();

    builder.append('>');
    for (final XmlElement element : elements)
      builder.append("\n  ").append(element.toString().replaceAll("\n", "\n  "));

    return builder.append("\n</").append(name).append('>').toString();
  }
}