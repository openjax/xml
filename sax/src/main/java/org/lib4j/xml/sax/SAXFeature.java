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

public enum SAXFeature {
  /**
   * Namespace awareness.
   */
  NAMESPACES(Constants.SAX_FEATURE_PREFIX, Constants.NAMESPACES_FEATURE),

  /**
   * Namespace prefix awareness.
   */
  NAMESPACE_PREFIXES(Constants.SAX_FEATURE_PREFIX, Constants.NAMESPACE_PREFIXES_FEATURE),

  /**
   * Intern strings.
   */
  STRING_INTERNING(Constants.SAX_FEATURE_PREFIX, Constants.STRING_INTERNING_FEATURE),

  /**
   * Validate.
   */
  VALIDATION(Constants.SAX_FEATURE_PREFIX, Constants.VALIDATION_FEATURE),

  /**
   * External general entities.
   */
  EXTERNAL_GENERAL_ENTITIES(Constants.SAX_FEATURE_PREFIX, Constants.EXTERNAL_GENERAL_ENTITIES_FEATURE),

  /**
   * External parameter entities.
   */
  EXTERNAL_PARAMETER_ENTITIES(Constants.SAX_FEATURE_PREFIX, Constants.EXTERNAL_PARAMETER_ENTITIES_FEATURE),

  /**
   * Lexically handle parameter entities.
   */
  LEXICAL_HANDLER_PARAMETER_ENTITIES(Constants.SAX_FEATURE_PREFIX, Constants.LEXICAL_HANDLER_PARAMETER_ENTITIES_FEATURE),

  /**
   * Set is standalone.
   */
  IS_STANDALONE(Constants.SAX_FEATURE_PREFIX, Constants.IS_STANDALONE_FEATURE),

  /**
   * Resolve DTD URIs.
   */
  RESOLVE_DTD_URIS(Constants.SAX_FEATURE_PREFIX, Constants.RESOLVE_DTD_URIS_FEATURE),

  /**
   * Use Attributes2.
   */
  USE_ATTRIBUTES2(Constants.SAX_FEATURE_PREFIX, Constants.USE_ATTRIBUTES2_FEATURE),

  /**
   * Use Locator2.
   */
  USE_LOCATOR2(Constants.SAX_FEATURE_PREFIX, Constants.USE_LOCATOR2_FEATURE),

  /**
   * Use EntityResolver2.
   */
  USE_ENTITY_RESOLVER2(Constants.SAX_FEATURE_PREFIX, Constants.USE_ENTITY_RESOLVER2_FEATURE),

  /**
   * Unicode normalization checking.
   */
  UNICODE_NORMALIZATION_CHECKING(Constants.SAX_FEATURE_PREFIX, Constants.UNICODE_NORMALIZATION_CHECKING_FEATURE),

  /**
   * xmlns URIs.
   */
  XMLNS_URIS(Constants.SAX_FEATURE_PREFIX, Constants.XMLNS_URIS_FEATURE),

  /**
   * XML 1.1.
   */
  XML_11(Constants.SAX_FEATURE_PREFIX, Constants.XML_11_FEATURE),

  /**
   * Allow unparsed entity and notation declaration events to be sent after the end DTD event.
   */
  ALLOW_DTD_EVENTS_AFTER_ENDDTD(Constants.SAX_FEATURE_PREFIX, Constants.ALLOW_DTD_EVENTS_AFTER_ENDDTD_FEATURE),

  // Xerces Features

  /**
   * Schema validation.
   */
  SCHEMA_VALIDATION(Constants.XERCES_FEATURE_PREFIX, Constants.SCHEMA_VALIDATION_FEATURE),

  /**
   * Expose schema normalized.
   */
  SCHEMA_NORMALIZED_VALUE(Constants.XERCES_FEATURE_PREFIX, Constants.SCHEMA_NORMALIZED_VALUE),

  /**
   * Send schema default value via characters().
   */
  SCHEMA_ELEMENT_DEFAULT(Constants.XERCES_FEATURE_PREFIX, Constants.SCHEMA_ELEMENT_DEFAULT),

  /**
   * Schema full constraint checking.
   */
  SCHEMA_FULL_CHECKING(Constants.XERCES_FEATURE_PREFIX, Constants.SCHEMA_FULL_CHECKING),

  /**
   * Augment Post-Schema-Validation-Infoset.
   */
  SCHEMA_AUGMENT_PSVI(Constants.XERCES_FEATURE_PREFIX, Constants.SCHEMA_AUGMENT_PSVI),

  /**
   * Dynamic validation.
   */
  DYNAMIC_VALIDATION(Constants.XERCES_FEATURE_PREFIX, Constants.DYNAMIC_VALIDATION_FEATURE),

  /**
   * Warn on duplicate attribute declaration.
   */
  WARN_ON_DUPLICATE_ATTDEF(Constants.XERCES_FEATURE_PREFIX, Constants.WARN_ON_DUPLICATE_ATTDEF_FEATURE),

  /**
   * Warn on undeclared element.
   */
  WARN_ON_UNDECLARED_ELEMDEF(Constants.XERCES_FEATURE_PREFIX, Constants.WARN_ON_UNDECLARED_ELEMDEF_FEATURE),

  /**
   * Warn on duplicate entity declaration.
   */
  WARN_ON_DUPLICATE_ENTITYDEF(Constants.XERCES_FEATURE_PREFIX, Constants.WARN_ON_DUPLICATE_ENTITYDEF_FEATURE),

  /**
   * Allow Java encoding names.
   */
  ALLOW_JAVA_ENCODINGS(Constants.XERCES_FEATURE_PREFIX, Constants.ALLOW_JAVA_ENCODINGS_FEATURE),

  /**
   * Disallow DOCTYPE declaration.
   */
  DISALLOW_DOCTYPE_DECL(Constants.XERCES_FEATURE_PREFIX, Constants.DISALLOW_DOCTYPE_DECL_FEATURE),

  /**
   * Continue after fatal error.
   */
  CONTINUE_AFTER_FATAL_ERROR(Constants.XERCES_FEATURE_PREFIX, Constants.CONTINUE_AFTER_FATAL_ERROR_FEATURE),

  /**
   * Load dtd grammar when nonvalidating.
   */
  LOAD_DTD_GRAMMAR(Constants.XERCES_FEATURE_PREFIX, Constants.LOAD_DTD_GRAMMAR_FEATURE),

  /**
   * Load external dtd when nonvalidating.
   */
  LOAD_EXTERNAL_DTD(Constants.XERCES_FEATURE_PREFIX, Constants.LOAD_EXTERNAL_DTD_FEATURE),

  /**
   * Create entity reference nodes.
   */
  CREATE_ENTITY_REF_NODES(Constants.XERCES_FEATURE_PREFIX, Constants.CREATE_ENTITY_REF_NODES_FEATURE),

  /**
   * Default attribute values.
   */
  DEFAULT_ATTRIBUTE_VALUES(Constants.XERCES_FEATURE_PREFIX, Constants.DEFAULT_ATTRIBUTE_VALUES_FEATURE),

  /**
   * Validate content models.
   */
  VALIDATE_CONTENT_MODELS(Constants.XERCES_FEATURE_PREFIX, Constants.VALIDATE_CONTENT_MODELS_FEATURE),

  /**
   * Validate datatypes.
   */
  VALIDATE_DATATYPES(Constants.XERCES_FEATURE_PREFIX, Constants.VALIDATE_DATATYPES_FEATURE),

  /**
   * Notify character references.
   */
  NOTIFY_CHAR_REFS(Constants.XERCES_FEATURE_PREFIX, Constants.NOTIFY_CHAR_REFS_FEATURE),

  /**
   * Notify built-in (&amp,amp;, etc.) references.
   */
  NOTIFY_BUILTIN_REFS(Constants.XERCES_FEATURE_PREFIX, Constants.NOTIFY_BUILTIN_REFS_FEATURE),

  /**
   * Standard URI conformance.
   */
  STANDARD_URI_CONFORMANT(Constants.XERCES_FEATURE_PREFIX, Constants.STANDARD_URI_CONFORMANT_FEATURE),

  /**
   * Internal performance related feature:
   * false - the parser settings (features/properties) have not changed between 2 parses
   * true - the parser settings have changed between 2 parses
   * NOTE: this feature should only be set by the parser configuration.
   */
  PARSER_SETTINGS(Constants.XERCES_FEATURE_PREFIX, Constants.PARSER_SETTINGS),

  /**
   * For some reason, this feature messes up stuff dealing with xsi:type!!!
   */
  HONOUR_ALL_SCHEMALOCATIONS(Constants.XERCES_FEATURE_PREFIX, Constants.HONOUR_ALL_SCHEMALOCATIONS_FEATURE),

  GENERATE_SYNTHETIC_ANNOTATIONS(Constants.XERCES_FEATURE_PREFIX, Constants.GENERATE_SYNTHETIC_ANNOTATIONS_FEATURE),

  VALIDATE_ANNOTATIONS(Constants.XERCES_FEATURE_PREFIX, Constants.VALIDATE_ANNOTATIONS_FEATURE);

  private final String feature;

  private SAXFeature(final String prefix, final String feature) {
    this.feature = prefix + feature;
  }

  @Override
  public String toString() {
    return feature;
  }
}