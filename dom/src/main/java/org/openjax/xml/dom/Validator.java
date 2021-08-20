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

import java.io.IOException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.openjax.xml.api.ValidationException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class Validator {
  private static final QName XSI = new QName(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "xsi", XMLConstants.XMLNS_ATTRIBUTE);
  private static final QName XMLNS = new QName(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, XMLConstants.XMLNS_ATTRIBUTE);

  public final void validate(final Element element) throws IOException, ValidationException {
    // only do validation on the root element of the document
    if (element != element.getOwnerDocument().getDocumentElement())
      return;

    final NamedNodeMap attributes = element.getAttributes();
    final StringBuilder namespaceLocations = new StringBuilder();
    for (int i = 0, len = attributes.getLength(); i < len; ++i) {
      final Node attribute = attributes.item(i);
      final String namespaceURI = attribute.getNodeValue();
      if (attribute.getNodeName().startsWith(XMLNS.getLocalPart()) && namespaceURI != null && namespaceURI.length() != 0 && !XSI.getNamespaceURI().equals(namespaceURI)) {
        final URL schemaLocation = getSchemaLocation(namespaceURI);
        if (schemaLocation != null) {
          if (i > 0)
            namespaceLocations.append(' ');

          namespaceLocations.append(namespaceURI).append(' ').append(schemaLocation);
        }
      }
    }

    element.setAttributeNS(XMLNS.getNamespaceURI(), XSI.getPrefix() + ":" + XSI.getLocalPart(), XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
    element.setAttributeNS(XSI.getNamespaceURI(), "xsi:schemaLocation", namespaceLocations.toString());

    System.out.println(DOMs.domToString(element));

    parse(element);
  }

  protected abstract URL lookupSchemaLocation(String namespaceURI);

  /**
   * Returns the schemaLocation {@link URL} of the declaring namespaceURI.
   *
   * @param namespaceURI The namespaceURI that is defined at the schemaLocation.
   * @return The schemaLocation {@link URL} of the declaring namespaceURI.
   */
  protected abstract URL getSchemaLocation(String namespaceURI);

  protected abstract void parse(Element element) throws IOException, ValidationException;
}