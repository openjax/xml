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

@SuppressWarnings("restriction")
public final class SAXFeature {
  // SAX Features

  /**
   * Namespace awareness.
   */
  public static final SAXFeature NAMESPACES = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.NAMESPACES_FEATURE);

  /**
   * Namespace prefix awareness.
   */
  public static final SAXFeature NAMESPACE_PREFIXES = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.NAMESPACE_PREFIXES_FEATURE);

  /**
   * Intern strings.
   */
  public static final SAXFeature STRING_INTERNING = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.STRING_INTERNING_FEATURE);

  /**
   * Validate.
   */
  public static final SAXFeature VALIDATION = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.VALIDATION_FEATURE);

  /**
   * External general entities.
   */
  public static final SAXFeature EXTERNAL_GENERAL_ENTITIES = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.EXTERNAL_GENERAL_ENTITIES_FEATURE);

  /**
   * External parameter entities.
   */
  public static final SAXFeature EXTERNAL_PARAMETER_ENTITIES = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.EXTERNAL_PARAMETER_ENTITIES_FEATURE);

  /**
   * Lexically handle parameter entities.
   */
  public static final SAXFeature LEXICAL_HANDLER_PARAMETER_ENTITIES = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.LEXICAL_HANDLER_PARAMETER_ENTITIES_FEATURE);

  /**
   * Set is standalone.
   */
  public static final SAXFeature IS_STANDALONE = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.IS_STANDALONE_FEATURE);

  /**
   * Resolve DTD URIs.
   */
  public static final SAXFeature RESOLVE_DTD_URIS = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.RESOLVE_DTD_URIS_FEATURE);

  /**
   * Use Attributes2.
   */
  public static final SAXFeature USE_ATTRIBUTES2 = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.USE_ATTRIBUTES2_FEATURE);

  /**
   * Use Locator2.
   */
  public static final SAXFeature USE_LOCATOR2 = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.USE_LOCATOR2_FEATURE);

  /**
   * Use EntityResolver2.
   */
  public static final SAXFeature USE_ENTITY_RESOLVER2 = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.USE_ENTITY_RESOLVER2_FEATURE);

  /**
   * Unicode normalization checking.
   */
  public static final SAXFeature UNICODE_NORMALIZATION_CHECKING = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.UNICODE_NORMALIZATION_CHECKING_FEATURE);

  /**
   * xmlns URIs.
   */
  public static final SAXFeature XMLNS_URIS = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.XMLNS_URIS_FEATURE);

  /**
   * XML 1.1.
   */
  public static final SAXFeature XML_11 = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.XML_11_FEATURE);

  /**
   * Allow unparsed entity and notation declaration events to be sent after the end DTD event.
   */
  public static final SAXFeature ALLOW_DTD_EVENTS_AFTER_ENDDTD = new SAXFeature(Constants.SAX_FEATURE_PREFIX + Constants.ALLOW_DTD_EVENTS_AFTER_ENDDTD_FEATURE);

  // Xerces Features

  /**
   * Schema validation.
   */
  public static final SAXFeature SCHEMA_VALIDATION = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_VALIDATION_FEATURE);

  /**
   * Expose schema normalized.
   */
  public static final SAXFeature SCHEMA_NORMALIZED_VALUE = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_NORMALIZED_VALUE);

  /**
   * Send schema default value via characters().
   */
  public static final SAXFeature SCHEMA_ELEMENT_DEFAULT = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_ELEMENT_DEFAULT);

  /**
   * Schema full constraint checking.
   */
  public static final SAXFeature SCHEMA_FULL_CHECKING = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_FULL_CHECKING);

  /**
   * Augment Post-Schema-Validation-Infoset.
   */
  public static final SAXFeature SCHEMA_AUGMENT_PSVI = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.SCHEMA_AUGMENT_PSVI);

  /**
   * Dynamic validation.
   */
  public static final SAXFeature DYNAMIC_VALIDATION = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.DYNAMIC_VALIDATION_FEATURE);

  /**
   * Warn on duplicate attribute declaration.
   */
  public static final SAXFeature WARN_ON_DUPLICATE_ATTDEF = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.WARN_ON_DUPLICATE_ATTDEF_FEATURE);

  /**
   * Warn on undeclared element.
   */
  public static final SAXFeature WARN_ON_UNDECLARED_ELEMDEF = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.WARN_ON_UNDECLARED_ELEMDEF_FEATURE);

  /**
   * Warn on duplicate entity declaration.
   */
  public static final SAXFeature WARN_ON_DUPLICATE_ENTITYDEF = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.WARN_ON_DUPLICATE_ENTITYDEF_FEATURE);

  /**
   * Allow Java encoding names.
   */
  public static final SAXFeature ALLOW_JAVA_ENCODINGS = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.ALLOW_JAVA_ENCODINGS_FEATURE);

  /**
   * Disallow DOCTYPE declaration.
   */
  public static final SAXFeature DISALLOW_DOCTYPE_DECL = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.DISALLOW_DOCTYPE_DECL_FEATURE);

  /**
   * Continue after fatal error.
   */
  public static final SAXFeature CONTINUE_AFTER_FATAL_ERROR = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.CONTINUE_AFTER_FATAL_ERROR_FEATURE);

  /**
   * Load dtd grammar when nonvalidating.
   */
  public static final SAXFeature LOAD_DTD_GRAMMAR = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.LOAD_DTD_GRAMMAR_FEATURE);

  /**
   * Load external dtd when nonvalidating.
   */
  public static final SAXFeature LOAD_EXTERNAL_DTD = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.LOAD_EXTERNAL_DTD_FEATURE);

  /**
   * Create entity reference nodes.
   */
  public static final SAXFeature CREATE_ENTITY_REF_NODES = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.CREATE_ENTITY_REF_NODES_FEATURE);

  /**
   * Default attribute values.
   */
  public static final SAXFeature DEFAULT_ATTRIBUTE_VALUES = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.DEFAULT_ATTRIBUTE_VALUES_FEATURE);

  /**
   * Validate content models.
   */
  public static final SAXFeature VALIDATE_CONTENT_MODELS = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.VALIDATE_CONTENT_MODELS_FEATURE);

  /**
   * Validate datatypes.
   */
  public static final SAXFeature VALIDATE_DATATYPES = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.VALIDATE_DATATYPES_FEATURE);

  /**
   * Notify character references.
   */
  public static final SAXFeature NOTIFY_CHAR_REFS = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.NOTIFY_CHAR_REFS_FEATURE);

  /**
   * Notify built-in (&amp;amp;, etc.) references.
   */
  public static final SAXFeature NOTIFY_BUILTIN_REFS = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.NOTIFY_BUILTIN_REFS_FEATURE);

  /**
   * Standard URI conformance.
   */
  public static final SAXFeature STANDARD_URI_CONFORMANT = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.STANDARD_URI_CONFORMANT_FEATURE);

  /**
   * Internal performance related feature:
   * false - the parser settings (features/properties) have not changed between 2 parses
   * true - the parser settings have changed between 2 parses
   * NOTE: this feature should only be set by the parser configuration.
   */
  public static final SAXFeature PARSER_SETTINGS = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.PARSER_SETTINGS);

  /**
   * Make XML Processor XInclude Aware.
   */
  public static final SAXFeature XINCLUDE_AWARE = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.XINCLUDE_AWARE);

  /**
   * Ignore xsi:schemaLocation and xsi:noNamespaceSchemaLocation.
   */
  public static final SAXFeature IGNORE_SCHEMA_LOCATION_HINTS = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.IGNORE_SCHEMA_LOCATION_HINTS);

  /**
   * When true, the schema processor will change characters events
   * to ignorableWhitespaces events, when characters are expected to
   * only contain ignorable whitespaces.
   */
  public static final SAXFeature CHANGE_IGNORABLE_CHARACTERS_INTO_IGNORABLE_WHITESPACES = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.CHANGE_IGNORABLE_CHARACTERS_INTO_IGNORABLE_WHITESPACES);

  /**
   * For some reason, this feature messes up stuff dealing with xsi:type!!!
   */
//  public static final SAXFeature HONOUR_ALL_SCHEMALOCATIONS = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.HONOUR_ALL_SCHEMALOCATIONS_FEATURE);
//  public static final SAXFeature GENERATE_SYNTHETIC_ANNOTATIONS = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.GENERATE_SYNTHETIC_ANNOTATIONS_FEATURE);
//  public static final SAXFeature VALIDATE_ANNOTATIONS = new SAXFeature(Constants.XERCES_FEATURE_PREFIX + Constants.VALIDATE_ANNOTATIONS_FEATURE);

  private final String feature;

  public SAXFeature(final String feature) {
    this.feature = feature;
  }

  protected String getFeature() {
    return feature;
  }

  @Override
  public int hashCode() {
    return feature.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof SAXFeature))
      return false;

    final SAXFeature saxFeature = (SAXFeature)obj;
    return feature != null ? feature.equals(saxFeature.feature) : saxFeature.feature == null;
  }

  @Override
  public String toString() {
    return feature;
  }
}