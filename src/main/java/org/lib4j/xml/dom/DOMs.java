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
    final DOMStyle style = DOMStyle.consolidate(styles);
    final StringBuilder builder = new StringBuilder();
    domToString(builder, element, 0, style);
    return builder.toString();
  }

  private static void domToString(final StringBuilder string, final Node node, int depth, final DOMStyle style) {
    if (node == null)
      return;

    final String nodeName = style.isIgnoreNamespaces() ? node.getLocalName() : node.getNodeName();
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
      attributesToString(string, node, depth + 1, style);
      if (node.hasChildNodes()) {
        string.append(">");
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
          domToString(string, nodeList.item(i), depth + 1, style);

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
  }

  private static void attributesToString(final StringBuilder string, final Node node, int depth, final DOMStyle style) {
    final NamedNodeMap attributes = node.getAttributes();
    if (attributes == null)
      return;

    for (int i = 0; i < attributes.getLength(); i++) {
      final Node attribute = attributes.item(i);
      final String nodeName = attribute.getNodeName();
      if (nodeName.startsWith("xmlns") && style.isIgnoreNamespaces())
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