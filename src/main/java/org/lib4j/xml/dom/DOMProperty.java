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

import org.apache.xerces.impl.Constants;

public enum DOMProperty {
  /**
   * DOM node.
   */
  DOM_NODE(Constants.SAX_PROPERTY_PREFIX, Constants.DOM_NODE_PROPERTY),

  /**
   * JAXP schemaSource: when used internally may include DTD sources (DOM).
   */
  SCHEMA_SOURCE(Constants.JAXP_PROPERTY_PREFIX, Constants.SCHEMA_SOURCE),

  /**
   * JAXP schemaSource language: when used internally may include DTD namespace (DOM).
   */
  SCHEMA_LANGUAGE(Constants.JAXP_PROPERTY_PREFIX, Constants.SCHEMA_LANGUAGE),

  /**
   * Current element node.
   */
  CURRENT_ELEMENT_NODE(Constants.XERCES_PROPERTY_PREFIX, Constants.CURRENT_ELEMENT_NODE_PROPERTY),

  /**
   * Document final class name.
   */
  DOCUMENT_CLASS_NAME(Constants.XERCES_PROPERTY_PREFIX, Constants.DOCUMENT_CLASS_NAME_PROPERTY);

  private final String property;

  private DOMProperty(final String prefix, String property) {
    this.property = prefix + property;
  }

  @Override
  public String toString() {
    return property;
  }
}