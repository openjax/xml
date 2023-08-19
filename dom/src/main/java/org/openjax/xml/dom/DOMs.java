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

package org.openjax.xml.dom;

import java.util.HashSet;
import java.util.Map;

import javax.xml.XMLConstants;

import org.libj.lang.Strings;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility functions for operations pertaining to classes in the {@code org.w3c.dom} package.
 */
public final class DOMs {
  /**
   * Returns a string representation of a {@link Node}, or an empty string if {@code node} is null. This method handles all child
   * nodes recursively.
   *
   * @implNote Only elements, attributes and text nodes and considered. Other facets like processing instructions, comments and
   *           CDATA are not considered.
   * @param node The {@link Node} to convert.
   * @param styles An array of {@link DOMStyle} style preferences.
   * @return A string representation of a {@link Node}.
   */
  public static String domToString(final Node node, final DOMStyle ... styles) {
    return domToString(node, null, null, styles);
  }

  /**
   * Returns a string representation of a {@link Node}, or an empty string if {@code node} is null. This method handles all child
   * nodes recursively.
   *
   * @implNote Only elements, attributes and text nodes and considered. Other facets like processing instructions, comments and
   *           CDATA are not considered.
   * @param node The {@link Node} to convert.
   * @param namespaceToPrefix Map of namespace-to-prefix assignments.
   * @param schemaLocations Map of namespace-to-schemaLocation assignments.
   * @param styles An array of {@link DOMStyle} style preferences.
   * @return A string representation of a {@link Node}.
   */
  public static String domToString(final Node node, final Map<String,String> namespaceToPrefix, final Map<String,String> schemaLocations, final DOMStyle ... styles) {
    if (node == null)
      return "";

    final boolean indent = DOMStyle.isIndent(styles);
    final boolean indentAttributes = DOMStyle.isIndentAttributes(styles);
    final boolean omitNamespaces = DOMStyle.isOmitNamespaces(styles);
    final HashSet<String> namespaces = omitNamespaces || schemaLocations == null ? null : new HashSet<>();
    final StringBuilder xml = domToString(new StringBuilder(), namespaces, namespaceToPrefix, node, 0, indent, indentAttributes, omitNamespaces);
    if (schemaLocations == null || schemaLocations.size() == 0 || namespaces == null || namespaces.size() == 0)
      return xml.toString();

    final StringBuilder locations = new StringBuilder();
    for (final String namespace : namespaces) { // [S]
      final String location = schemaLocations.get(namespace);
      if (location != null)
        locations.append(namespace).append(' ').append(location);
    }

    if (locations.length() == 0)
      return xml.toString();

    int index = Strings.indexOf(xml, '>');
    if (xml.charAt(index - 1) == '/')
      --index;

    locations.append('"');
    locations.insert(0, " xsi:schemaLocation=\"");
    if (xml.lastIndexOf("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", index) == -1)
      locations.insert(0, " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");

    xml.insert(index, locations);
    return xml.toString();
  }

  private static boolean validNamespaceURI(final String namespaceURI) {
    return namespaceURI != null && !XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI) && !XMLConstants.XML_NS_URI.equals(namespaceURI);
  }

  private static StringBuilder domToString(final StringBuilder b, final HashSet<String> namespaces, final Map<String,String> namespaceToPrefix, final Node node, final int depth, final boolean indent, final boolean indentAttributes, final boolean omitNamespaces) {
    if (node == null)
      return b;

    if (node instanceof Attr)
      return attributeToString(b, namespaces, namespaceToPrefix, (Attr)node, depth, indentAttributes, omitNamespaces);

    final String prefix;
    final String namespaceURI = node.getNamespaceURI();
    if (omitNamespaces) {
      prefix = "";
    }
    else if ((namespaces != null || namespaceToPrefix != null) && validNamespaceURI(namespaceURI)) {
      prefix = namespaceToPrefix == null ? null : namespaceToPrefix.get(namespaceURI);
      if (namespaces != null)
        namespaces.add(namespaceURI);
    }
    else {
      prefix = null;
    }

    final String nodeName = prefix == null ? node.getNodeName() : prefix.length() > 0 ? prefix + ":" + node.getLocalName() : node.getLocalName();
    final String nodeValue = node.getNodeValue();
    final int type = node.getNodeType();
    if (type == Node.ELEMENT_NODE) {
      int length;
      if (indent && (length = b.length()) > 1 && b.charAt(length - 1) == '>') {
        b.append('\n');
        for (int i = 0; i < depth; ++i) // [N]
          b.append("  ");
      }

      b.append('<').append(nodeName);
      attributesToString(b, namespaces, namespaceToPrefix, node, depth + 1, indentAttributes, omitNamespaces);
      if (node.hasChildNodes()) {
        b.append('>');
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0, i$ = nodeList.getLength(); i < i$; ++i) // [RA]
          domToString(b, namespaces, namespaceToPrefix, nodeList.item(i), depth + 1, indent, indentAttributes, omitNamespaces);

        if (indent && (length = b.length()) > 1 && b.charAt(length - 1) == '>') {
          b.append('\n');
          for (int i = 0; i < depth; ++i) // [N]
            b.append("  ");
        }

        b.append("</").append(nodeName).append('>');
      }
      else {
        b.append("/>");
      }
    }
    else if (type == Node.TEXT_NODE && nodeValue != null && nodeValue.length() != 0) {
      // Note: DOM expands entity references to their Unicode equivalent. '&amp;' becomes simply '&'. Since the string being
      // constructed here is intended to be used as XML text, we have to reconstruct the standard entity references.
      appendText(b, nodeValue);
    }

    return b;
  }

  private static StringBuilder attributeToString(final StringBuilder b, final HashSet<String> namespaces, final Map<String,String> namespaceToPrefix, final Attr attribute, final int depth, final boolean indentAttributes, final boolean omitNamespaces) {
    if (indentAttributes) {
      b.append('\n');
      for (int i = 0; i < depth; ++i) // [N]
        b.append("  ");
    }
    else {
      b.append(' ');
    }

    final String prefix;
    final String localName;
    final String attrLocalName = attribute.getLocalName();
    if (!omitNamespaces && (namespaces != null || namespaceToPrefix != null)) {
      final String namespaceURI = attribute.getNamespaceURI();
      if (validNamespaceURI(namespaceURI)) {
        prefix = namespaceToPrefix == null ? null : namespaceToPrefix.get(namespaceURI);
        localName = attrLocalName;
        if (namespaces != null)
          namespaces.add(namespaceURI);
      }
      else if (namespaceToPrefix != null && "xmlns".equals(attribute.getPrefix())) {
        final String localNamespaceURI = attribute.lookupNamespaceURI(attrLocalName);
        final String name = localNamespaceURI == null ? null : namespaceToPrefix.get(localNamespaceURI);
        localName = name != null ? name : attrLocalName;
        prefix = "xmlns";
      }
      else {
        prefix = null;
        localName = attrLocalName;
      }
    }
    else {
      prefix = null;
      localName = attrLocalName;
    }

    final String nodeName = prefix == null ? attribute.getNodeName() : prefix.length() == 0 ? localName : localName.length() > 0 ? prefix + ":" + localName : prefix;
    b.append(nodeName).append("=\"");
    final String value;
    final String nodeValue = attribute.getNodeValue();
    if (namespaceToPrefix != null && "xsi:type".equals(attribute.getName())) {
      final int colon = nodeValue.indexOf(':');
      if (colon != -1) {
        final String valueNamespaceURI = attribute.lookupNamespaceURI(nodeValue.substring(0, colon));
        final String valuePrefix = namespaceToPrefix.get(valueNamespaceURI);
        value = valuePrefix == null ? nodeValue : valuePrefix.length() > 0 ? valuePrefix + ":" + nodeValue.substring(colon + 1) : nodeValue.substring(colon + 1);
      }
      else {
        value = nodeValue;
      }
    }
    else {
      value = nodeValue;
    }

    appendText(b, value);
    b.append('"');
    return b;
  }

  private static StringBuilder attributesToString(final StringBuilder builder, final HashSet<String> namespaces, final Map<String,String> namespaceToPrefix, final Node node, final int depth, final boolean indentAttributes, final boolean omitNamespaces) {
    final NamedNodeMap attributes = node.getAttributes();
    if (attributes == null)
      return builder;

    for (int i = 0, i$ = attributes.getLength(); i < i$; ++i) { // [RA]
      final Attr attribute = (Attr)attributes.item(i);
      if (!omitNamespaces || !attribute.getNodeName().startsWith("xmlns"))
        attributeToString(builder, namespaces, namespaceToPrefix, attribute, depth, indentAttributes, omitNamespaces);
    }

    return builder;
  }

  /**
   * Append the specified {@code text} string to {@code builder}, ensuring characters are properly escaped. The escaped characters
   * are:
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
   * Note: This method removes any leading and trailing whitespace from {@code text}.
   *
   * @param builder The {@link StringBuilder} to which to append.
   * @param text The text string to append.
   * @throws NullPointerException If {@code builder} or {@code text} is null.
   */
  private static void appendText(final StringBuilder builder, final String text) {
    for (int i = 0, i$ = text.length(); i < i$; ++i) { // [N]
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