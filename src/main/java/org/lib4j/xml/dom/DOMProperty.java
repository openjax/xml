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

import com.sun.org.apache.xerces.internal.impl.Constants;

@SuppressWarnings("restriction")
public final class DOMProperty {
  /**
   * DOM node.
   */
  public static final DOMProperty DOM_NODE = new DOMProperty(Constants.SAX_PROPERTY_PREFIX + Constants.DOM_NODE_PROPERTY);

  /**
   * JAXP schemaSource: when used internally may include DTD sources (DOM).
   */
  public static final DOMProperty SCHEMA_SOURCE = new DOMProperty(Constants.JAXP_PROPERTY_PREFIX + Constants.SCHEMA_SOURCE);

  /**
   * JAXP schemaSource language: when used internally may include DTD namespace (DOM).
   */
  public static final DOMProperty SCHEMA_LANGUAGE = new DOMProperty(Constants.JAXP_PROPERTY_PREFIX + Constants.SCHEMA_LANGUAGE);

  /**
   * Resource resolver.
   */
  public static final DOMFeature DOM_RESOURCE_RESOLVER = new DOMFeature(Constants.SAX_PROPERTY_PREFIX + Constants.DOM_RESOURCE_RESOLVER);

  /**
   * Error handler.
   */
  public static final DOMFeature DOM_ERROR_HANDLER = new DOMFeature(Constants.SAX_PROPERTY_PREFIX + Constants.DOM_ERROR_HANDLER);

  /**
   * Schema type.
   */
  public static final DOMFeature DOM_SCHEMA_TYPE = new DOMFeature(Constants.SAX_PROPERTY_PREFIX + Constants.DOM_SCHEMA_TYPE);

  /**
   * Schema Location.
   */
  public static final DOMFeature DOM_SCHEMA_LOCATION = new DOMFeature(Constants.SAX_PROPERTY_PREFIX + Constants.DOM_SCHEMA_LOCATION);

  /**
   * XSModel.
   */
  public static final DOMFeature DOM_PSVI = new DOMFeature(Constants.SAX_PROPERTY_PREFIX + Constants.DOM_PSVI);

  /**
   * Current element node.
   */
  public static final DOMProperty CURRENT_ELEMENT_NODE = new DOMProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.CURRENT_ELEMENT_NODE_PROPERTY);

  /**
   * Document final class name.
   */
  public static final DOMProperty DOCUMENT_CLASS_NAME = new DOMProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.DOCUMENT_CLASS_NAME_PROPERTY);

  private final String property;

  protected DOMProperty(final String property) {
    this.property = property;
  }

  protected String getProperty() {
    return property;
  }

  @Override
  public int hashCode() {
    return property.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof DOMProperty))
      return false;

    final DOMProperty domProperty = (DOMProperty)obj;
    return property != null ? property.equals(domProperty.property) : domProperty.property == null;
  }
}