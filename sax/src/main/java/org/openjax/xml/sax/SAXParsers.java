/* Copyright (c) 2019 OpenJAX
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

package org.openjax.xml.sax;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * Utility class providing functions related to SAX parsers.
 */
public final class SAXParsers {
  private static final Logger logger = LoggerFactory.getLogger(SAXParsers.class);
  private static final SAXParserFactory[] factory = new SAXParserFactory[2];

  private static SAXParserFactory newFactory(final boolean validating) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
    SAXParserFactory factory;
    try {
      factory = SAXParserFactory.newInstance("org.apache.xerces.jaxp.SAXParserFactoryImpl", null);
    }
    catch (final FactoryConfigurationError e) {
      factory = SAXParserFactory.newInstance();
      logger.warn("Unable to create SAXParserFactory of type org.apache.xerces.jaxp.SAXParserFactoryImpl. Factory of " + factory.getClass().getName() + " created instead.", e);
    }

    factory.setNamespaceAware(true);
    factory.setXIncludeAware(true);
    factory.setValidating(validating);
    if (!factory.getFeature("http://xml.org/sax/features/xml-1.1"))
      throw new FactoryConfigurationError("Expected XML 1.1");

    // The next 2 lines are necessary to turn off DTD validation, which
    // takes over if the XML document has a DOCTYPE and a xsi:schemaLocation
    factory.setFeature("http://xml.org/sax/features/validation", false);
    factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

    factory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
    factory.setFeature("http://apache.org/xml/features/validation/schema-full-checking", validating);
    factory.setFeature("http://apache.org/xml/features/honour-all-schemaLocations", true);
    factory.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true);
    return factory;
  }

  private static SAXParserFactory getFactory(final boolean validating) throws ParserConfigurationException, SAXNotRecognizedException, SAXNotSupportedException {
    final int index = validating ? 0 : 1;
    if (factory[index] == null)
      factory[index] = newFactory(validating);

    return factory[index];
  }

  /**
   * Creates a new {@link SAXParser} instance that supports:
   * <ol>
   * <li>Validation, if the specified {@code validating} argument is
   * {@code true}.</li>
   * <li>Namespace prefixes.</li>
   * <li>Schema full checking, if the specified {@code validating} argument is
   * {@code true}.</li>
   * <li>Honor all schemaLocation(s).</li>
   * <li>Continue after fatal error.</li>
   * </ol>
   *
   * @return A new {@link SAXParser}.
   * @param validating If the new {@link SAXParser} should support validation.
   * @throws SAXException If a SAX exception has occurred.
   */
  public static SAXParser newParser(final boolean validating) throws SAXException {
    try {
      return getFactory(validating).newSAXParser();
    }
    catch (final ParserConfigurationException e) {
      throw new SAXException(e);
    }
  }

  private SAXParsers() {
  }
}