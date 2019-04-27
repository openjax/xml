/* Copyright (c) 2008 OpenJAX
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

package org.openjax.ext.xml.dom;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility functions for operations pertaining to classes in the
 * {@code org.w3c.dom} package.
 */
public final class DOMs {
  /**
   * Returns a string representation of a {@link Node}. This method handles all
   * child nodes recursively.
   * <p>
   * Note: this only handles elements, attributes and text nodes. It will not
   * handle processing instructions, comments, CDATA or anything else.
   *
   * @param node Node to convert.
   * @param styles An array of {@link DOMStyle} style preferences.
   * @return A string representation of a {@link Node}.
   */
  public static String domToString(final Node node, final DOMStyle ... styles) {
    return domToString(node, null, null, styles);
  }

  /**
   * Returns a string representation of a {@link Node}. This method handles all
   * child nodes recursively.
   * <p>
   * Note: this only handles elements, attributes and text nodes. It will not
   * handle processing instructions, comments, CDATA or anything else.
   *
   * @param node Node to convert.
   * @param namespaceToPrefix Map of namespace-to-prefix assignments.
   * @param schemaLocations Map of namespace-to-schemaLocation assignments.
   * @param styles An array of {@link DOMStyle} style preferences.
   * @return A string representation of a {@link Node}.
   */
  public static String domToString(final Node node, final Map<String,String> namespaceToPrefix, final Map<String,String> schemaLocations, final DOMStyle ... styles) {
    final DOMStyle style = DOMStyle.merge(styles);
    final Set<String> namespaces = style.isIgnoreNamespaces() || schemaLocations == null ? null : new HashSet<>();
    final StringBuilder builder = domToString(new StringBuilder(), namespaces, namespaceToPrefix, node, 0, style);
    if (schemaLocations == null || schemaLocations.size() == 0 || namespaces.size() == 0)
      return builder.toString();

    final StringBuilder locations = new StringBuilder();
    for (final String namespace : namespaces) {
      final String location = schemaLocations.get(namespace);
      if (location != null)
        locations.append(namespace).append(' ').append(location);
    }

    if (locations.length() == 0)
      return builder.toString();

    int index = builder.indexOf(">");
    if (builder.charAt(index - 1) == '/')
      --index;

    locations.append('"');
    locations.insert(0, " xsi:schemaLocation=\"");
    if (builder.lastIndexOf("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", index) == -1)
      locations.insert(0, " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");

    builder.insert(index, locations);
    return builder.toString();
  }

  private static boolean validNamespaceURI(final String namespaceURI) {
    return namespaceURI != null && !XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI) && !XMLConstants.XML_NS_URI.equals(namespaceURI);
  }

  private static StringBuilder domToString(final StringBuilder builder, final Set<String> namespaces, final Map<String,String> namespaceToPrefix, final Node node, int depth, final DOMStyle style) {
    if (node == null)
      return builder;

    if (node instanceof Attr)
      return attributeToString(builder, namespaces, namespaceToPrefix, (Attr)node, depth, style);

    final String prefix;
    if (!style.isIgnoreNamespaces() && (namespaces != null || namespaceToPrefix != null) && validNamespaceURI(node.getNamespaceURI())) {
      prefix = namespaceToPrefix == null ? null : namespaceToPrefix.get(node.getNamespaceURI());
      if (namespaces != null)
        namespaces.add(node.getNamespaceURI());
    }
    else {
      prefix = null;
    }

    final String nodeName = prefix == null ? node.getNodeName() : prefix.length() > 0 ? prefix + ":" + node.getLocalName() : node.getLocalName();
    final String nodeValue = node.getNodeValue();
    final int type = node.getNodeType();
    if (type == Node.ELEMENT_NODE) {
      if (style.isIndent() && builder.length() > 1 && builder.charAt(builder.length() - 1) == '>') {
        builder.append('\n');
        for (int i = 0; i < depth; ++i)
          builder.append("  ");
      }

      builder.append('<');
      builder.append(nodeName);
      attributesToString(builder, namespaces, namespaceToPrefix, node, depth + 1, style);
      if (node.hasChildNodes()) {
        builder.append('>');
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); ++i)
          domToString(builder, namespaces, namespaceToPrefix, nodeList.item(i), depth + 1, style);

        if (style.isIndent() && builder.length() > 1 && builder.charAt(builder.length() - 1) == '>') {
          builder.append('\n');
          for (int i = 0; i < depth; ++i)
            builder.append("  ");
        }

        builder.append("</").append(nodeName).append('>');
      }
      else {
        builder.append("/>");
      }
    }
    else if (type == Node.TEXT_NODE && nodeValue != null && nodeValue.length() != 0) {
      // Note: DOM expands entity references to their Unicode equivalent.
      // '&amp;' becomes simply '&'. Since the string being constructed
      // here is intended to be used as XML text, we have to reconstruct
      // the standard entity references
      appendText(builder, nodeValue);
    }

    return builder;
  }

  private static StringBuilder attributeToString(final StringBuilder builder, final Set<String> namespaces, final Map<String,String> namespaceToPrefix, final Attr attribute, int depth, final DOMStyle style) {
    if (style.isIndentAttributes()) {
      builder.append('\n');
      for (int i = 0; i < depth; ++i)
        builder.append("  ");
    }
    else {
      builder.append(' ');
    }

    final String prefix;
    final String localName;
    if (!style.isIgnoreNamespaces() && (namespaces != null || namespaceToPrefix != null)) {
      if (validNamespaceURI(attribute.getNamespaceURI())) {
        prefix = namespaceToPrefix == null ? null : namespaceToPrefix.get(attribute.getNamespaceURI());
        localName = attribute.getLocalName();
        if (namespaces != null)
          namespaces.add(attribute.getNamespaceURI());
      }
      else if (namespaceToPrefix != null && "xmlns".equals(attribute.getPrefix())) {
        final String localNamespaceURI = attribute.lookupNamespaceURI(attribute.getLocalName());
        final String name = localNamespaceURI == null ? null : namespaceToPrefix.get(localNamespaceURI);
        localName = name != null ? name : attribute.getLocalName();
        prefix = "xmlns";
      }
      else {
        prefix = null;
        localName = attribute.getLocalName();
      }
    }
    else {
      prefix = null;
      localName = attribute.getLocalName();
    }

    final String nodeName = prefix == null ? attribute.getNodeName() : prefix.length() == 0 ? localName : localName.length() > 0 ? prefix + ":" + localName : prefix;
    builder.append(nodeName);
    builder.append("=\"");
    final String value;
    if (namespaceToPrefix != null && "xsi:type".equals(attribute.getName())) {
      final int colon = attribute.getNodeValue().indexOf(':');
      if (colon != -1) {
        final String valueNamespaceURI = attribute.lookupNamespaceURI(attribute.getNodeValue().substring(0, colon));
        final String valuePrefix = namespaceToPrefix.get(valueNamespaceURI);
        value = valuePrefix == null ? attribute.getNodeValue() : valuePrefix.length() > 0 ? valuePrefix + ":" + attribute.getNodeValue().substring(colon + 1) : attribute.getNodeValue().substring(colon + 1);
      }
      else {
        value = attribute.getNodeValue();
      }
    }
    else {
      value = attribute.getNodeValue();
    }

    appendText(builder, value);
    builder.append('"');
    return builder;
  }

  private static StringBuilder attributesToString(final StringBuilder builder, final Set<String> namespaces, final Map<String,String> namespaceToPrefix, final Node node, int depth, final DOMStyle style) {
    final NamedNodeMap attributes = node.getAttributes();
    if (attributes == null)
      return builder;

    for (int i = 0; i < attributes.getLength(); ++i) {
      final Attr attribute = (Attr)attributes.item(i);
      if (!style.isIgnoreNamespaces() || !attribute.getNodeName().startsWith("xmlns"))
        attributeToString(builder, namespaces, namespaceToPrefix, attribute, depth, style);
    }

    return builder;
  }

  /**
   * Append the specified {@code text} string to {@code builder}, ensuring
   * characters are properly escaped. The escaped characters are:
   *
   * <pre>
   * {@code From |  To
   * -------------
   *   &  | &amp;
   *   '  | &apos;
   *   "  | &quot;
   *   >  | &gt;
   *   <  | &lt;}
   * </pre>
   *
   * Note: This method removes any leading and trailing whitespace from
   * {@code text}.
   *
   * @param builder The {@link StringBuilder} to which to append.
   * @param text The text string to append.
   */
  private static void appendText(final StringBuilder builder, String text) {
    text = text.trim();
    for (int i = 0; i < text.length(); ++i) {
      final char ch = text.charAt(i);
      if (ch == '&')
        builder.append("&amp;");
      else if (ch == '>')
        builder.append("&gt;");
      else if (ch == '<')
        builder.append("&lt;");
      else if (ch == '\'')
        builder.append("&apos;");
      else if (ch == '"')
        builder.append("&quot;");
      else
        builder.append(ch);
    }
  }

  private DOMs() {
  }
}