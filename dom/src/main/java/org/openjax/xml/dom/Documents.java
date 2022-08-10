/* Copyright (c) 2020 OpenJAX
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

import java.util.LinkedList;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Utility functions for operations pertaining to {@link Document}.
 */
public final class Documents {
  /**
   * Set the specified namespace URI to each node of the provided document, which includes all child elements, and conditionally
   * their attributes if {@code attributeFormQualified} is {@code true}.
   *
   * @param document The {@link Document}.
   * @param namespaceURI The namespace URI.
   * @param attributeFormQualified If {@code true}, the {@code namespaceURI} will be set for attribute nodes; otherwise attribute
   *          nodes will be left as is.
   */
  public static void setNamespaceURI(final Document document, final String namespaceURI, final boolean attributeFormQualified) {
    final LinkedList<Node> nodes = new LinkedList<>();
    nodes.push(document.getDocumentElement());

    while (!nodes.isEmpty()) {
      Node node = nodes.pop();
      switch (node.getNodeType()) {
        case Node.ATTRIBUTE_NODE:
        case Node.ELEMENT_NODE:
          if (namespaceURI != null) {
            // the reassignment to node is very important. as per javadoc renameNode will
            // try to modify node (first parameter) in place. If that is not possible it
            // will replace that node for a new created one and return it to the caller.
            // if we did not reassign node we will get no childs in the loop below.
            node = document.renameNode(node, namespaceURI, node.getNodeName());
          }

          break;
      }

      if (attributeFormQualified) {
        // for attributes of this node
        final NamedNodeMap attributes = node.getAttributes();
        if (attributes != null && attributes.getLength() != 0) {
          for (int i = 0, i$ = attributes.getLength(); i < i$; ++i) { // [RA]
            final Node attribute = attributes.item(i);
            if (attribute != null)
              nodes.push(attribute);
          }
        }
      }

      // for child nodes of this node
      final NodeList childNodes = node.getChildNodes();
      if (childNodes != null && childNodes.getLength() != 0) {
        for (int i = 0, i$ = childNodes.getLength(); i < i$; ++i) { // [RA]
          final Node childNode = childNodes.item(i);
          if (childNode != null)
            nodes.push(childNode);
        }
      }
    }
  }

  private Documents() {
  }
}