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

package org.safris.commons.xml.dom;

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
    final StringBuffer buffer = new StringBuffer();
    domToString(buffer, element, 0, style);
    return buffer.toString();
  }

  private static void domToString(final StringBuffer stringBuffer, final Node node, int depth, final DOMStyle style) {
    if (node == null)
      return;

    final String nodeName;
    if (style.isIgnoreNamespaces())
      nodeName = node.getLocalName();
    else
      nodeName = node.getNodeName();

    final String nodeValue = node.getNodeValue();
    final int type = node.getNodeType();
    if (Node.ELEMENT_NODE == type) {
      if (style.isIndent() && stringBuffer.length() > 1 && stringBuffer.charAt(stringBuffer.length() - 1) == '>') {
        stringBuffer.append("\n");
        for (int i = 0; i < depth; i++) {
          stringBuffer.append("  ");
        }
      }

      stringBuffer.append("<");
      stringBuffer.append(nodeName);
      attributesToString(stringBuffer, node, depth + 1, style);
      if (node.hasChildNodes()) {
        stringBuffer.append(">");
        final NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
          domToString(stringBuffer, nodeList.item(i), depth + 1, style);
        }

        if (style.isIndent() && stringBuffer.length() > 1 && stringBuffer.charAt(stringBuffer.length() - 1) == '>') {
          stringBuffer.append("\n");
          for (int i = 0; i < depth; i++) {
            stringBuffer.append("  ");
          }
        }

        stringBuffer.append("</").append(nodeName).append(">");
      }
      else {
        stringBuffer.append("/>");
      }
    }
    else if (Node.TEXT_NODE == type && nodeValue != null && nodeValue.length() != 0) {
      // Note: DOM expands entity references to their Unicode equivalent.
      // '&amp;' becomes simply '&'. Since the string being constructed
      // here is intended to be used as XML text, we have to reconstruct
      // the standard entity references
      entityConvert(stringBuffer, nodeValue);
    }
  }

  private static void attributesToString(final StringBuffer stringBuffer, final Node node, int depth, final DOMStyle style) {
    final NamedNodeMap attributes;
    if ((attributes = node.getAttributes()) == null)
      return;

    for (int i = 0; i < attributes.getLength(); i++) {
      final Node attribute = attributes.item(i);
      final String nodeName = attribute.getNodeName();
      if (nodeName.startsWith("xmlns") && style.isIgnoreNamespaces())
        continue;

      if (style.isIndentAttributes()) {
        stringBuffer.append("\n");
        for (int j = 0; j < depth; j++) {
          stringBuffer.append("  ");
        }
      }
      else {
        stringBuffer.append(" ");
      }

      stringBuffer.append(nodeName);
      stringBuffer.append("=\"");
      entityConvert(stringBuffer, attribute.getNodeValue());
      stringBuffer.append("\"");
    }
  }

  /**
   * Convert the invalid XML characters in a string to character entities.
   *
   * @param textToConvert
   *          the String containing invalid entities.
   * @return String with expanded entities.
   */
  private static void entityConvert(final StringBuffer stringBuffer, String entity) {
    if (entity == null)
      return;

    entity = entity.trim();
    for (int i = 0; i < entity.length(); i++) {
      switch (entity.charAt(i)) {
        case '&':
          stringBuffer.append("&amp;");
          break;
        case '>':
          stringBuffer.append("&gt;");
          break;
        case '<':
          stringBuffer.append("&lt;");
          break;
        default:
          stringBuffer.append(entity.substring(i, i + 1));
          break;
      }
    }
  }

  private DOMs() {
  }
}