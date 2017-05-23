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
public final class DOMFeature {
  /**
   * Comments.
   */
  public static final DOMFeature INCLUDE_COMMENTS = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.INCLUDE_COMMENTS_FEATURE);

  /**
   * Create cdata nodes.
   */
  public static final DOMFeature CREATE_CDATA_NODES = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.CREATE_CDATA_NODES_FEATURE);

  /**
   * Load as infoset.
   */
  public static final DOMFeature LOAD_AS_INFOSET = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.LOAD_AS_INFOSET);

  /**
   * Canonical form.
   */
  public static final DOMFeature DOM_CANONICAL_FORM = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_CANONICAL_FORM);

  /**
   * Support CDATA sections.
   */
  public static final DOMFeature DOM_CDATA_SECTIONS = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_CDATA_SECTIONS);

  /**
   * Support comments.
   */
  public static final DOMFeature DOM_COMMENTS = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_COMMENTS);

  /**
   * Charset overrides xml encoding. REVISIT: this feature seems to have no effect for Xerces.
   */
  public static final DOMFeature DOM_CHARSET_OVERRIDES_XML_ENCODING = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_CHARSET_OVERRIDES_XML_ENCODING);

  /**
   * Data normailzation.
   */
  public static final DOMFeature DOM_DATATYPE_NORMALIZATION = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_DATATYPE_NORMALIZATION);

  /**
   * Support entities.
   */
  public static final DOMFeature DOM_ENTITIES = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_ENTITIES);

  /**
   * Support infoset.
   */
  public static final DOMFeature DOM_INFOSET = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_INFOSET);

  /**
   * Support namespaces.
   */
  public static final DOMFeature DOM_NAMESPACES = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_NAMESPACES);

  /**
   * Support namespace declarations.
   */
  public static final DOMFeature DOM_NAMESPACE_DECLARATIONS = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_NAMESPACE_DECLARATIONS);

  /**
   * Supported mediatypes only.
   */
  public static final DOMFeature DOM_SUPPORTED_MEDIATYPES_ONLY = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_SUPPORTED_MEDIATYPES_ONLY);

  /**
   * Validate if schema.
   */
  public static final DOMFeature DOM_VALIDATE_IF_SCHEMA = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_VALIDATE_IF_SCHEMA);

  /**
   * Validate.
   */
  public static final DOMFeature DOM_VALIDATE = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_VALIDATE);

  /**
   * Element content whitespace.
   */
  public static final DOMFeature DOM_ELEMENT_CONTENT_WHITESPACE = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_ELEMENT_CONTENT_WHITESPACE);

  /**
   * Discard default content.
   */
  public static final DOMFeature DOM_DISCARD_DEFAULT_CONTENT = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_DISCARD_DEFAULT_CONTENT);

  /**
   * Normalize characters.
   */
  public static final DOMFeature DOM_NORMALIZE_CHARACTERS = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_NORMALIZE_CHARACTERS);

  /**
   * Check char normalization.
   */
  public static final DOMFeature DOM_CHECK_CHAR_NORMALIZATION = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_CHECK_CHAR_NORMALIZATION);

  /**
   * Check wellformed.
   */
  public static final DOMFeature DOM_WELLFORMED = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_WELLFORMED);

  /**
   * Split CDATA.
   */
  public static final DOMFeature DOM_SPLIT_CDATA = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_SPLIT_CDATA);

  /**
   * Format pretty print.
   */
  public static final DOMFeature DOM_FORMAT_PRETTY_PRINT = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_FORMAT_PRETTY_PRINT);

  /**
   * XMLDECL.
   */
  public static final DOMFeature DOM_XMLDECL = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_XMLDECL);

  /**
   * Unknown chars.
   */
  public static final DOMFeature DOM_UNKNOWNCHARS = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_UNKNOWNCHARS);

  /**
   * Certified.
   */
  public static final DOMFeature DOM_CERTIFIED = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_CERTIFIED);

  /**
   * Disallow DOCTYPE.
   */
  public static final DOMFeature DOM_DISALLOW_DOCTYPE = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_DISALLOW_DOCTYPE);

  /**
   * Ignore unknown character denormalizations.
   */
  public static final DOMFeature DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS = new DOMFeature(Constants.SAX_FEATURE_PREFIX + Constants.DOM_IGNORE_UNKNOWN_CHARACTER_DENORMALIZATIONS);

  /**
   * Defer node expansion.
   */
  public static final DOMFeature DEFER_NODE_EXPANSION = new DOMFeature(Constants.XERCES_FEATURE_PREFIX + Constants.DEFER_NODE_EXPANSION_FEATURE);

  /**
   * Include ignorable whitespace .
   */
  public static final DOMFeature INCLUDE_IGNORABLE_WHITESPACE = new DOMFeature(Constants.XERCES_FEATURE_PREFIX + Constants.INCLUDE_IGNORABLE_WHITESPACE);

  private final String feature;

  public DOMFeature(final String feature) {
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

    if (!(obj instanceof DOMFeature))
      return false;

    final DOMFeature domFeature = (DOMFeature)obj;
    return feature != null ? feature.equals(domFeature.feature) : domFeature.feature == null;
  }
}