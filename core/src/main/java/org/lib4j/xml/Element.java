/* Copyright (c) 2018 lib4j
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

package org.lib4j.xml;

import java.util.Collection;

public class Element {
  private final String name;
  private final Collection<Attribute> attributes;
  private final Collection<Element> elements;

  public Element(final String name, final Collection<Attribute> attributes, final Collection<Element> elements) {
    this.name = name;
    if (name == null || name.length() == 0)
      throw new IllegalArgumentException("name == " + name);

    this.attributes = attributes;
    this.elements = elements;
  }

  public String getName() {
    return this.name;
  }

  public Collection<Attribute> getAttributes() {
    return this.attributes;
  }

  public Collection<Element> getElements() {
    return this.elements;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Attribute))
      return false;

    final Element that = (Element)obj;
    return name.equals(that.name) && (attributes == null ? that.attributes == null : that.attributes != null && attributes.size() == that.attributes.size() && attributes.containsAll(that.attributes)) && (elements == null ? that.elements == null : that.elements != null && elements.size() == that.elements.size() && elements.containsAll(that.elements));
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
    for (final Attribute attribute : attributes)
      builder.append(' ').append(attribute);

    if (elements == null || elements.size() == 0)
      return builder.append("/>").toString();

    builder.append('>');
    for (final Element element : elements)
      builder.append("\n  ").append(element.toString().replaceAll("\n", "\n  "));

    return builder.append("\n</").append(name).append('>').toString();
  }
}