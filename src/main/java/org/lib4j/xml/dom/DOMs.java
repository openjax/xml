/* Copyright (c) 2008 lib4j
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

package org.lib4j.xml.dom;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.XMLConstants;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class DOMs {
  /**
   * Converts a DOM document to a XML string. It handles all children recursively.
   *
   * Note: this only handles elements, attributes and text nodes. It will not handle processing instructions, comments, CDATA or anything else.
   *
   * @param element
   *          element to convert.
   */
  public static String domToString(final Element element, final DOMStyle ... styles) {
    return domToString(element, null, styles);
  }

  /**
   * Converts a DOM document to a XML string. It handles all children recursively.
   *
   * Note: this only handles elements, attributes and text nodes. It will not handle processing instructions, comments, CDATA or anything else.
   *
   * @param element
   *          element to convert.
   */
  public static String domToString(final Element element, final Map<String,String> schemaLocations, final DOMStyle ... styles) {
    final DOMStyle style = DOMStyle.consolidate(styles);
    final Set<String> namespaces = style.isIgnoreNamespaces() || schemaLocations == null ? null : new HashSet<String>();
    final StringBuilder string = domToString(new StringBuilder(), namespaces, element, 0, style);
    if (schemaLocations == null || schemaLocations.size() == 0 || namespaces.size() == 0)
      return string.toString();

    final StringBuilder locations = new StringBuilder();
    for (final String namespace : namespaces) {
      final String location = schemaLocations.get(namespace);
      if (location != null)
        locations.append(namespace).append(" ").append(location);
    }

    if (locations.length() == 0)
      return string.toString();

    int index = string.indexOf(">");
    if (string.charAt(index - 1) == '/')
      --index;

    locations.append('"');
    locations.insert(0, " xsi:schemaLocation=\"");
    if (string.lastIndexOf("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"", index) == -1)
      locations.insert(0, " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");

    string.insert(index, locations);
    return string.toString();
  }

  private static final boolean validNamespaceURI(final String namespaceURI) {
    return namespaceURI != null && !XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(namespaceURI) && !XMLConstants.XML_NS_URI.equals(namespaceURI);
  }

  private static StringBuilder domToString(final StringBuilder string, final Set<String> namespaces, final Node node, int depth, final DOMStyle style) {
    if (node == null)
      return string;

    final String nodeName;
    if (style.isIgnoreNamespaces()) {
      nodeName = node.getLocalName();
    }
    else {
      if (namespaces != null && validNamespaceURI(node.getNamespaceURI()))
        namespaces.add(node.getNamespaceURI());

      nodeName = node.getNodeName();
    }

    final String nodeValue = node.getNodeValue();
    final int type = node.getNodeType();
    if (Node.ELEMENT_NODE == type) {
      if (style.isIndent() && string.length() > 1 && string.charAt(string.length() - 1) == '>') {
        string.append("\n");
        for (int i = 0; i < depth; i++)
          string.append("  ");
      }

      string.append("<");
      string.append(nodeName);
      attributesToString(string, namespaces, node, depth + 1, style);
      if (node.hasChildNodes()) {
        string.append(">");
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
          domToString(string, namespaces, nodeList.item(i), depth + 1, style);

        if (style.isIndent() && string.length() > 1 && string.charAt(string.length() - 1) == '>') {
          string.append("\n");
          for (int i = 0; i < depth; i++)
            string.append("  ");
        }

        string.append("</").append(nodeName).append(">");
      }
      else {
        string.append("/>");
      }
    }
    else if (Node.TEXT_NODE == type && nodeValue != null && nodeValue.length() != 0) {
      // Note: DOM expands entity references to their Unicode equivalent.
      // '&amp;' becomes simply '&'. Since the string being constructed
      // here is intended to be used as XML text, we have to reconstruct
      // the standard entity references
      entityConvert(string, nodeValue);
    }

    return string;
  }

  private static void attributesToString(final StringBuilder string, final Set<String> namespaces, final Node node, int depth, final DOMStyle style) {
    final NamedNodeMap attributes = node.getAttributes();
    if (attributes == null)
      return;

    for (int i = 0; i < attributes.getLength(); i++) {
      final Node attribute = attributes.item(i);
      final String nodeName = attribute.getNodeName();
      if (!style.isIgnoreNamespaces()) {
        if (namespaces != null && validNamespaceURI(node.getNamespaceURI()))
          namespaces.add(attribute.getNamespaceURI());
      }
      else if (nodeName.startsWith("xmlns"))
        continue;

      if (style.isIndentAttributes()) {
        string.append("\n");
        for (int j = 0; j < depth; j++)
          string.append("  ");
      }
      else {
        string.append(" ");
      }

      string.append(nodeName);
      string.append("=\"");
      entityConvert(string, attribute.getNodeValue());
      string.append("\"");
    }
  }

  /**
   * Convert the invalid XML characters in a string to character entities.
   *
   * @param textToConvert
   *          the String containing invalid entities.
   * @return String with expanded entities.
   */
  private static void entityConvert(final StringBuilder string, String entity) {
    if (entity == null)
      return;

    entity = entity.trim();
    for (int i = 0; i < entity.length(); i++) {
      final char ch = entity.charAt(i);
      switch (ch) {
        case '&':
          string.append("&amp;");
          break;
        case '>':
          string.append("&gt;");
          break;
        case '<':
          string.append("&lt;");
          break;
        case '\'':
          string.append("&apos;");
          break;
        case '"':
          string.append("&quot;");
          break;
        default:
          string.append(ch);
          break;
      }
    }
  }

  private DOMs() {
  }
}