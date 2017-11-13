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

package org.lib4j.xml.sax;

import com.sun.org.apache.xerces.internal.impl.Constants;

public final class SAXProperty {
  /**
   * Declaration handler.
   */
  public static final SAXProperty DECLARATION_HANDLER = new SAXProperty(Constants.SAX_PROPERTY_PREFIX + Constants.DECLARATION_HANDLER_PROPERTY);

  /**
   * Lexical handler.
   */
  public static final SAXProperty LEXICAL_HANDLER = new SAXProperty(Constants.SAX_PROPERTY_PREFIX + Constants.LEXICAL_HANDLER_PROPERTY);

  /**
   * DOM node.
   */
  public static final SAXProperty DOM_NODE = new SAXProperty(Constants.SAX_PROPERTY_PREFIX + Constants.DOM_NODE_PROPERTY);

  /**
   * XML string.
   */
  public static final SAXProperty XML_STRING = new SAXProperty(Constants.SAX_PROPERTY_PREFIX + Constants.XML_STRING_PROPERTY);

  /**
   * Secure.
   */
  public static final SAXProperty FEATURE_SECURE = new SAXProperty(Constants.SAX_PROPERTY_PREFIX + Constants.FEATURE_SECURE_PROCESSING);

  /**
   * Document XML version.
   */
  public static final SAXProperty DOCUMENT_XML_VERSION = new SAXProperty(Constants.SAX_PROPERTY_PREFIX + Constants.DOCUMENT_XML_VERSION_PROPERTY);

  /**
   * JAXP schemaSource: when used internally may include DTD sources (DOM).
   */
  public static final SAXProperty SCHEMA_SOURCE = new SAXProperty(Constants.JAXP_PROPERTY_PREFIX + Constants.SCHEMA_SOURCE);

  /**
   * JAXP schemaSource language: when used internally may include DTD namespace (DOM).
   */
  public static final SAXProperty SCHEMA_LANGUAGE = new SAXProperty(Constants.JAXP_PROPERTY_PREFIX + Constants.SCHEMA_LANGUAGE);

  /**
   * Element attribute limit.
   */
  //public static final SAXProperty SYSTEM_PROPERTY_ELEMENT_ATTRIBUTE_LIMIT = new SAXProperty(Constants.JAXP_PROPERTY_PREFIX + Constants.SYSTEM_PROPERTY_ELEMENT_ATTRIBUTE_LIMIT);

  /**
   * Symbol table.
   */
  public static final SAXProperty SYMBOL_TABLE = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY);

  /**
   * Error reporter.
   */
  public static final SAXProperty ERROR_REPORTER = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY);

  /**
   * Error handler.
   */
  public static final SAXProperty ERROR_HANDLER = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_HANDLER_PROPERTY);

  /**
   * XInclude handler.
   */
  public static final SAXProperty XINCLUDE_HANDLER = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.XINCLUDE_HANDLER_PROPERTY);

  /**
   * Entity manager.
   */
  public static final SAXProperty ENTITY_MANAGER = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_MANAGER_PROPERTY);

  /**
   * Input buffer size.
   */
  public static final SAXProperty BUFFER_SIZE = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.BUFFER_SIZE_PROPERTY);

  /**
   * Security manager.
   */
  public static final SAXProperty SECURITY_MANAGER = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.SECURITY_MANAGER_PROPERTY);

  /**
   * Entity resolver.
   */
  public static final SAXProperty ENTITY_RESOLVER = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_RESOLVER_PROPERTY);

  /**
   * Grammar pool.
   */
  public static final SAXProperty XMLGRAMMAR_POOL = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.XMLGRAMMAR_POOL_PROPERTY);

  /**
   * Datatype validator.
   */
  public static final SAXProperty DATATYPE_VALIDATOR_FACTORY = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.DATATYPE_VALIDATOR_FACTORY_PROPERTY);

  /**
   * Document scanner.
   */
  public static final SAXProperty DOCUMENT_SCANNER = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.DOCUMENT_SCANNER_PROPERTY);

  /**
   * DTD scanner.
   */
  public static final SAXProperty DTD_SCANNER = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.DTD_SCANNER_PROPERTY);

  /**
   * DTD processor.
   */
  public static final SAXProperty DTD_PROCESSOR = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.DTD_PROCESSOR_PROPERTY);

  /**
   * Validator.
   */
  public static final SAXProperty VALIDATOR = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.VALIDATOR_PROPERTY);

  /**
   * DTD Validator.
   */
  public static final SAXProperty DTD_VALIDATOR = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.DTD_VALIDATOR_PROPERTY);

  /**
   * Schema Validator.
   */
  public static final SAXProperty SCHEMA_VALIDATOR = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.SCHEMA_VALIDATOR_PROPERTY);

  /**
   * No namespace schema location.
   */
  public static final SAXProperty SCHEMA_LOCATION = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.SCHEMA_LOCATION);

  /**
   * Schema location.
   */
  public static final SAXProperty SCHEMA_NONS_LOCATION = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.SCHEMA_NONS_LOCATION);

  /**
   * Namespace binder.
   */
  public static final SAXProperty NAMESPACE_BINDER = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.NAMESPACE_BINDER_PROPERTY);

  /**
   * Namespace context.
   */
  public static final SAXProperty NAMESPACE_CONTEXT = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.NAMESPACE_CONTEXT_PROPERTY);

  /**
   * Validation manager.
   */
  public static final SAXProperty VALIDATION_MANAGER = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.VALIDATION_MANAGER_PROPERTY);

  /**
   * XPointer Schema.
   */
  public static final SAXProperty XPOINTER_SCHEMA = new SAXProperty(Constants.XERCES_PROPERTY_PREFIX + Constants.XPOINTER_SCHEMA_PROPERTY);

  private final String property;

  private SAXProperty(final String property) {
    this.property = property;
  }

  public String getProperty() {
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

    if (!(obj instanceof SAXProperty))
      return false;

    final SAXProperty saxProperty = (SAXProperty)obj;
    return property != null ? property.equals(saxProperty.property) : saxProperty.property == null;
  }
}