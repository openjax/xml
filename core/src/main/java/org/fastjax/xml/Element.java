/* Copyright (c) 2018 FastJAX
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

package org.fastjax.xml;

import java.util.Collection;
import java.util.Map;

public class Element {
  private final String name;
  private Map<String,String> attributes;
  private Collection<Element> elements;

  public Element(final String name, final Map<String,String> attributes, final Collection<Element> elements) {
    this.name = name;
    if (name == null || name.length() == 0)
      throw new IllegalArgumentException("name == " + name);

    this.attributes = attributes;
    this.elements = elements;
  }

  public String getName() {
    return this.name;
  }

  public void setAttributes(final Map<String,String> attributes) {
    this.attributes = attributes;
  }

  public Map<String,String> getAttributes() {
    return this.attributes;
  }

  public void setElements(final Collection<Element> elements) {
    this.elements = elements;
  }

  public Collection<Element> getElements() {
    return this.elements;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Element))
      return false;

    final Element that = (Element)obj;
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

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder("<");
    builder.append(name);
    if (attributes != null && attributes.size() > 0)
      for (final Map.Entry<String,String> entry : attributes.entrySet())
        builder.append(' ').append(entry.getKey()).append("=\"").append(CharacterDatas.escape(entry.getValue())).append('"');

    if (elements == null || elements.size() == 0)
      return builder.append("/>").toString();

    builder.append('>');
    for (final Element element : elements)
      builder.append("\n  ").append(element.toString().replaceAll("\n", "\n  "));

    return builder.append("\n</").append(name).append('>').toString();
  }
}