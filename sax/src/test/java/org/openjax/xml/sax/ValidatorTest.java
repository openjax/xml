/* Copyright (c) 2008 OpenJAX
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

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.libj.net.URLs;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ValidatorTest {
  static {
    URLs.disableRemote();
  }

  private static void testNoDeclaration(final String fileName) throws IOException, SAXException {
    final AtomicBoolean sawWarning = new AtomicBoolean();
    Validator.validate(ClassLoader.getSystemClassLoader().getResource(fileName), new ErrorHandler() {
      @Override
      public void warning(final SAXParseException exception) throws SAXException {
        assertEquals("There is no schema or DTD associated with the document", exception.getMessage());
        sawWarning.set(true);
      }

      @Override
      public void fatalError(final SAXParseException exception) throws SAXException {
      }

      @Override
      public void error(final SAXParseException exception) throws SAXException {
      }
    });
    assertTrue(sawWarning.get());
  }

  @Test
  public void testEmptyXml() throws IOException, SAXException {
    testNoDeclaration("empty.xml");
  }

  @Test
  public void testCurrencyXml() throws IOException, SAXException {
    testNoDeclaration("currency.xml");
  }

  @Test
  public void testTestXsd() throws IOException, SAXException {
    Validator.validate(ClassLoader.getSystemClassLoader().getResource("test.xsd"));
  }

  @Test
  public void testNoNamespaceXsd() throws IOException, SAXException {
    Validator.validate(ClassLoader.getSystemClassLoader().getResource("noNamespace.xsd"));
  }

  @Test
  public void testValidXml() throws IOException, SAXException {
    Validator.validate(ClassLoader.getSystemClassLoader().getResource("valid.xml"));
  }

  @Test
  public void testXmlXsd() throws IOException, SAXException {
    Validator.validate(ClassLoader.getSystemClassLoader().getResource("xmlschema/xml.xsd"));
  }

  @Test
  public void testOffline() throws IOException {
    try {
      Validator.validate(ClassLoader.getSystemClassLoader().getResource("remote.xml"));
      fail("Expected SAXException");
    }
    catch (final SAXException e) {
      assertTrue(Validator.isRemoteAccessException(e));
    }
  }

  @Test
  public void testOverride() throws IOException, SAXException {
    Validator.validate(ClassLoader.getSystemClassLoader().getResource("override.xml"));
  }

  @Test
  public void testXInclude() throws IOException, SAXException {
    Validator.validate(ClassLoader.getSystemClassLoader().getResource("xinclude.xml"));
  }

  @Test
  public void testInvalid() throws IOException {
    try {
      Validator.validate(ClassLoader.getSystemClassLoader().getResource("invalid.xml"));
      fail("Expected SAXException");
    }
    catch (final SAXException e) {
      assertFalse(Validator.isRemoteAccessException(e));
      if (!e.getMessage().startsWith("cvc-datatype-valid.1.2.1: 'a' is not a valid value for 'integer'."))
        fail(e.getMessage());
    }
  }
}