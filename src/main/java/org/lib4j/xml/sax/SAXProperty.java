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

import org.apache.xerces.impl.Constants;

public enum SAXProperty {
  /**
   * Declaration handler.
   */
  DECLARATION_HANDLER(Constants.SAX_PROPERTY_PREFIX, Constants.DECLARATION_HANDLER_PROPERTY),

  /**
   * Lexical handler.
   */
  LEXICAL_HANDLER(Constants.SAX_PROPERTY_PREFIX, Constants.LEXICAL_HANDLER_PROPERTY),

  /**
   * DOM node.
   */
  DOM_NODE(Constants.SAX_PROPERTY_PREFIX, Constants.DOM_NODE_PROPERTY),

  /**
   * XML string.
   */
  XML_STRING(Constants.SAX_PROPERTY_PREFIX, Constants.XML_STRING_PROPERTY),

  /**
   * Document XML version.
   */
  DOCUMENT_XML_VERSION(Constants.SAX_PROPERTY_PREFIX, Constants.DOCUMENT_XML_VERSION_PROPERTY),

  /**
   * JAXP schemaSource: when used internally may include DTD sources (DOM).
   */
  SCHEMA_SOURCE(Constants.JAXP_PROPERTY_PREFIX, Constants.SCHEMA_SOURCE),

  /**
   * JAXP schemaSource language: when used internally may include DTD namespace (DOM).
   */
  SCHEMA_LANGUAGE(Constants.JAXP_PROPERTY_PREFIX, Constants.SCHEMA_LANGUAGE),

  /**
   * Element attribute limit.
   */
  //SYSTEM_PROPERTY_ELEMENT_ATTRIBUTE_LIMIT(Constants.JAXP_PROPERTY_PREFIX, Constants.SYSTEM_PROPERTY_ELEMENT_ATTRIBUTE_LIMIT),

  /**
   * Symbol table.
   */
  SYMBOL_TABLE(Constants.XERCES_PROPERTY_PREFIX, Constants.SYMBOL_TABLE_PROPERTY),

  /**
   * Error reporter.
   */
  ERROR_REPORTER(Constants.XERCES_PROPERTY_PREFIX, Constants.ERROR_REPORTER_PROPERTY),

  /**
   * Error handler.
   */
  ERROR_HANDLER(Constants.XERCES_PROPERTY_PREFIX, Constants.ERROR_HANDLER_PROPERTY),

  /**
   * XInclude handler.
   */
  XINCLUDE_HANDLER(Constants.XERCES_PROPERTY_PREFIX, Constants.XINCLUDE_HANDLER_PROPERTY),

  /**
   * Entity manager.
   */
  ENTITY_MANAGER(Constants.XERCES_PROPERTY_PREFIX, Constants.ENTITY_MANAGER_PROPERTY),

  /**
   * Input buffer size.
   */
  BUFFER_SIZE(Constants.XERCES_PROPERTY_PREFIX, Constants.BUFFER_SIZE_PROPERTY),

  /**
   * Security manager.
   */
  SECURITY_MANAGER(Constants.XERCES_PROPERTY_PREFIX, Constants.SECURITY_MANAGER_PROPERTY),

  /**
   * Entity resolver.
   */
  ENTITY_RESOLVER(Constants.XERCES_PROPERTY_PREFIX, Constants.ENTITY_RESOLVER_PROPERTY),

  /**
   * Grammar pool.
   */
  XMLGRAMMAR_POOL(Constants.XERCES_PROPERTY_PREFIX, Constants.XMLGRAMMAR_POOL_PROPERTY),

  /**
   * Datatype validator.
   */
  DATATYPE_VALIDATOR_FACTORY(Constants.XERCES_PROPERTY_PREFIX, Constants.DATATYPE_VALIDATOR_FACTORY_PROPERTY),

  /**
   * Document scanner.
   */
  DOCUMENT_SCANNER(Constants.XERCES_PROPERTY_PREFIX, Constants.DOCUMENT_SCANNER_PROPERTY),

  /**
   * DTD scanner.
   */
  DTD_SCANNER(Constants.XERCES_PROPERTY_PREFIX, Constants.DTD_SCANNER_PROPERTY),

  /**
   * DTD processor.
   */
  DTD_PROCESSOR(Constants.XERCES_PROPERTY_PREFIX, Constants.DTD_PROCESSOR_PROPERTY),

  /**
   * Validator.
   */
  VALIDATOR(Constants.XERCES_PROPERTY_PREFIX, Constants.VALIDATOR_PROPERTY),

  /**
   * DTD Validator.
   */
  DTD_VALIDATOR(Constants.XERCES_PROPERTY_PREFIX, Constants.DTD_VALIDATOR_PROPERTY),

  /**
   * Schema Validator.
   */
  SCHEMA_VALIDATOR(Constants.XERCES_PROPERTY_PREFIX, Constants.SCHEMA_VALIDATOR_PROPERTY),

  /**
   * No namespace schema location.
   */
  SCHEMA_LOCATION(Constants.XERCES_PROPERTY_PREFIX, Constants.SCHEMA_LOCATION),

  /**
   * Schema location.
   */
  SCHEMA_NONS_LOCATION(Constants.XERCES_PROPERTY_PREFIX, Constants.SCHEMA_NONS_LOCATION),

  /**
   * Namespace binder.
   */
  NAMESPACE_BINDER(Constants.XERCES_PROPERTY_PREFIX, Constants.NAMESPACE_BINDER_PROPERTY),

  /**
   * Namespace context.
   */
  NAMESPACE_CONTEXT(Constants.XERCES_PROPERTY_PREFIX, Constants.NAMESPACE_CONTEXT_PROPERTY),

  /**
   * Validation manager.
   */
  VALIDATION_MANAGER(Constants.XERCES_PROPERTY_PREFIX, Constants.VALIDATION_MANAGER_PROPERTY);

  private final String property;

  private SAXProperty(final String prefix, final String property) {
    this.property = prefix + property;
  }

  @Override
  public String toString() {
    return property;
  }
}