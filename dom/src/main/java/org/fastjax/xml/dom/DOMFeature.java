/* Copyright (c) 2008 FastJAX
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

package org.fastjax.xml.dom;

import org.apache.xerces.impl.Constants;

public enum DOMFeature {
  /**
   * Comments.
   */
  INCLUDE_COMMENTS(Constants.SAX_FEATURE_PREFIX, Constants.INCLUDE_COMMENTS_FEATURE),

  /**
   * Create cdata nodes.
   */
  CREATE_CDATA_NODES(Constants.SAX_FEATURE_PREFIX, Constants.CREATE_CDATA_NODES_FEATURE),

  /**
   * Load as infoset.
   */
  LOAD_AS_INFOSET(Constants.SAX_FEATURE_PREFIX, Constants.LOAD_AS_INFOSET),

  /**
   * Canonical form.
   */
  DOM_CANONICAL_FORM(Constants.SAX_FEATURE_PREFIX, Constants.DOM_CANONICAL_FORM),

  /**
   * Support CDATA sections.
   */
  DOM_CDATA_SECTIONS(Constants.SAX_FEATURE_PREFIX, Constants.DOM_CDATA_SECTIONS),

  /**
   * Support comments.
   */
  DOM_COMMENTS(Constants.SAX_FEATURE_PREFIX, Constants.DOM_COMMENTS),

  /**
   * Charset overrides xml encoding. REVISIT: this feature seems to have no effect for Xerces.
   */
  DOM_CHARSET_OVERRIDES_XML_ENCODING(Constants.SAX_FEATURE_PREFIX, Constants.DOM_CHARSET_OVERRIDES_XML_ENCODING),

  /**
   * Data normailzation.
   */
  DOM_DATATYPE_NORMALIZATION(Constants.SAX_FEATURE_PREFIX, Constants.DOM_DATATYPE_NORMALIZATION),

  /**
   * Support entities.
   */
  DOM_ENTITIES(Constants.SAX_FEATURE_PREFIX, Constants.DOM_ENTITIES),

  /**
   * Support infoset.
   */
  DOM_INFOSET(Constants.SAX_FEATURE_PREFIX, Constants.DOM_INFOSET),

  /**
   * Support namespaces.
   */
  DOM_NAMESPACES(Constants.SAX_FEATURE_PREFIX, Constants.DOM_NAMESPACES),

  /**
   * Support namespace declarations.
   */
  DOM_NAMESPACE_DECLARATIONS(Constants.SAX_FEATURE_PREFIX, Constants.DOM_NAMESPACE_DECLARATIONS),

  /**
   * Supported mediatypes only.
   */
  DOM_SUPPORTED_MEDIATYPES_ONLY(Constants.SAX_FEATURE_PREFIX, Constants.DOM_SUPPORTED_MEDIATYPES_ONLY),

  /**
   * Validate if schema.
   */
  DOM_VALIDATE_IF_SCHEMA(Constants.SAX_FEATURE_PREFIX, Constants.DOM_VALIDATE_IF_SCHEMA),

  /**
   * Validate.
   */
  DOM_VALIDATE(Constants.SAX_FEATURE_PREFIX, Constants.DOM_VALIDATE),

  /**
   * Element content whitespace.
   */
  DOM_ELEMENT_CONTENT_WHITESPACE(Constants.SAX_FEATURE_PREFIX, Constants.DOM_ELEMENT_CONTENT_WHITESPACE),

  /**
   * Discard default content.
   */
  DOM_DISCARD_DEFAULT_CONTENT(Constants.SAX_FEATURE_PREFIX, Constants.DOM_DISCARD_DEFAULT_CONTENT),

  /**
   * Normalize characters.
   */
  DOM_NORMALIZE_CHARACTERS(Constants.SAX_FEATURE_PREFIX, Constants.DOM_NORMALIZE_CHARACTERS),

  /**
   * Check char normalization.
   */
  DOM_CHECK_CHAR_NORMALIZATION(Constants.SAX_FEATURE_PREFIX, Constants.DOM_CHECK_CHAR_NORMALIZATION),

  /**
   * Check wellformed.
   */
  DOM_WELLFORMED(Constants.SAX_FEATURE_PREFIX, Constants.DOM_WELLFORMED),

  /**
   * Split CDATA.
   */
  DOM_SPLIT_CDATA(Constants.SAX_FEATURE_PREFIX, Constants.DOM_SPLIT_CDATA),

  /**
   * Format pretty print.
   */
  DOM_FORMAT_PRETTY_PRINT(Constants.SAX_FEATURE_PREFIX, Constants.DOM_FORMAT_PRETTY_PRINT),

  /**
   * XMLDECL.
   */
  DOM_XMLDECL(Constants.SAX_FEATURE_PREFIX, Constants.DOM_XMLDECL),

  /**
   * Unknown chars.
   */
  DOM_UNKNOWNCHARS(Constants.SAX_FEATURE_PREFIX, Constants.DOM_UNKNOWNCHARS),

  /**
   * Certified.
   */
  DOM_CERTIFIED(Constants.SAX_FEATURE_PREFIX, Constants.DOM_CERTIFIED),

  /**
   * Disallow DOCTYPE.
   */
  DOM_DISALLOW_DOCTYPE(Constants.SAX_FEATURE_PREFIX, Constants.DOM_DISALLOW_DOCTYPE),

  /**
   * Ignore unknown character denormalizations.
   */
  DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS(Constants.SAX_FEATURE_PREFIX, Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS),

  /**
   * Defer node expansion.
   */
  DEFER_NODE_EXPANSION(Constants.XERCES_FEATURE_PREFIX, Constants.DEFER_NODE_EXPANSION_FEATURE),

  /**
   * Include ignorable whitespace .
   */
  INCLUDE_IGNORABLE_WHITESPACE(Constants.XERCES_FEATURE_PREFIX, Constants.INCLUDE_IGNORABLE_WHITESPACE),

  /**
   * Resource resolver.
   */
  DOM_RESOURCE_RESOLVER(Constants.SAX_PROPERTY_PREFIX, Constants.DOM_RESOURCE_RESOLVER),

  /**
   * Error handler.
   */
  DOM_ERROR_HANDLER(Constants.SAX_PROPERTY_PREFIX, Constants.DOM_ERROR_HANDLER),

  /**
   * Schema type.
   */
  DOM_SCHEMA_TYPE(Constants.SAX_PROPERTY_PREFIX, Constants.DOM_SCHEMA_TYPE),

  /**
   * Schema Location.
   */
  DOM_SCHEMA_LOCATION(Constants.SAX_PROPERTY_PREFIX, Constants.DOM_SCHEMA_LOCATION),

  /**
   * XSModel.
   */
  DOM_PSVI(Constants.SAX_PROPERTY_PREFIX, Constants.DOM_PSVI);

  private final String feature;

  private DOMFeature(final String prefix, final String feature) {
    this.feature = prefix + feature;
  }

  @Override
  public String toString() {
    return feature;
  }
}