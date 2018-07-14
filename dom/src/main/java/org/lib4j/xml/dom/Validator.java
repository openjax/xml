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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.lib4j.xml.ValidationException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public abstract class Validator {
  private static final QName XSI = new QName(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "xsi", "xmlns");
  private static final QName XMLNS = new QName(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns");

  protected abstract URL lookupSchemaLocation(final String namespaceURI);

  public final void validate(final Element element) throws ValidationException {
    // only do validation on the root element of the document
    if (element != element.getOwnerDocument().getDocumentElement())
      return;

    final NamedNodeMap attributes = element.getAttributes();
    Node node = null;
    final Collection<String> namespaceURIs = new ArrayList<>(attributes.getLength());
    for (int i = 0; i < attributes.getLength(); i++) {
      node = attributes.item(i);
      if (node.getNodeName().startsWith(XMLNS.getLocalPart()))
        namespaceURIs.add(node.getNodeValue());
    }

    String namespaceLocations = "";
    for (final String namespaceURI : namespaceURIs) {
      if (namespaceURI == null || namespaceURI.length() == 0 || XSI.getNamespaceURI().equals(namespaceURI))
        continue;

      final URL schemaLocation = getSchemaLocation(namespaceURI);
      if (schemaLocation != null)
        namespaceLocations += " " + namespaceURI + " " + schemaLocation.toExternalForm();
    }

    element.setAttributeNS(XMLNS.getNamespaceURI(), XSI.getPrefix() + ":" + XSI.getLocalPart(), XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
    element.setAttributeNS(XSI.getNamespaceURI(), "xsi:schemaLocation", namespaceLocations.substring(1));

    try {
      parse(element);
    }
    catch (final IOException e) {
      throw new ValidationException(e);
    }
  }

  /**
   * This method allows a caller to get the schemaLocation <code>URL</code>
   * of the declaring namespaceURI.
   *
   * @param namespaceURI The namespaceURI that is defined at the
   * schemaLocation.
   *
   * @return The schemaLocation <code>URL</code>.
   */
  protected abstract URL getSchemaLocation(final String namespaceURI);

  protected abstract void parse(final Element element) throws IOException, ValidationException;
}