/* Copyright (c) 2006 lib4j
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

import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.w3c.dom.Node;

public final class NamespaceURI {
  // subjectively chosen
  public static final String W3C_XML_SCHEMA_PREFIX = "xs";
  public static final String W3C_XML_SCHEMA_INSTANCE_PREFIX = "xsi";

  public static final QName XS = new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "", W3C_XML_SCHEMA_PREFIX);
  public static final QName XSI = new QName(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "", W3C_XML_SCHEMA_INSTANCE_PREFIX);

  // statically defined
  public static final QName XML = new QName(XMLConstants.XML_NS_URI, "", XMLConstants.XML_NS_PREFIX);
  public static final QName XMLNS = new QName(XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "", XMLConstants.XMLNS_ATTRIBUTE);

  private static final Map<String,NamespaceURI> instances = new HashMap<String,NamespaceURI>();

  public static NamespaceURI getInstance(final String namespaceURI) {
    if (namespaceURI == null)
      throw new NullPointerException("namespaceURI == null");

    NamespaceURI value = instances.get(namespaceURI);
    if (value == null)
      instances.put(namespaceURI, value = new NamespaceURI(namespaceURI));

    return value;
  }

  public static String lookupNamespaceURI(final Node parent, final String prefix) {
    if (XML.getPrefix().equals(prefix))
      return XML.getNamespaceURI();

    if (XMLNS.getPrefix().equals(prefix))
      return XMLNS.getNamespaceURI();

    return parent.lookupNamespaceURI(prefix);
  }

  private final String namespaceURI;
  private final NamespaceBinding namespaceBinding;

  private NamespaceURI(final String namespaceURI) {
    if (namespaceURI == null)
      throw new NullPointerException("namespaceURI == null");

    this.namespaceURI = namespaceURI.intern();
    this.namespaceBinding = NamespaceBinding.parseNamespace(namespaceURI);
  }

  public NamespaceBinding getNamespaceBinding() {
    return namespaceBinding;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof NamespaceURI))
      return false;

    return namespaceURI.equals(((NamespaceURI)obj).namespaceURI);
  }

  @Override
  public int hashCode() {
    return namespaceURI.hashCode();
  }

  @Override
  public String toString() {
    return namespaceURI;
  }
}