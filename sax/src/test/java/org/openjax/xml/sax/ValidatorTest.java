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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.libj.net.URLs;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ValidatorTest {
  private static final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

  static {
    URLs.disableRemote();
  }

  private static void testNoDeclaration(final String fileName) throws IOException, SAXException {
    final AtomicBoolean hasWarning = new AtomicBoolean();
    Validator.validate(classLoader.getResource(fileName), new ErrorHandler() {
      @Override
      public void warning(final SAXParseException exception) throws SAXException {
        assertEquals("There is no schema or DTD associated with the document", exception.getMessage());
        hasWarning.set(true);
      }

      @Override
      public void fatalError(final SAXParseException exception) throws SAXException {
      }

      @Override
      public void error(final SAXParseException exception) throws SAXException {
      }
    });
    assertTrue(hasWarning.get());
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
    Validator.validate(classLoader.getResource("test.xsd"));
  }

  @Test
  public void testNoNamespaceXsd() throws IOException, SAXException {
    Validator.validate(classLoader.getResource("noNamespace.xsd"));
  }

  @Test
  public void testValidXml() throws IOException, SAXException {
    Validator.validate(classLoader.getResource("valid.xml"));
  }

  @Test
  public void testXmlXsd() throws IOException, SAXException {
    Validator.validate(classLoader.getResource("xmlschema/xml.xsd"));
  }

  @Test
  public void testOffline() throws IOException {
    try {
      Validator.validate(classLoader.getResource("remote.xml"));
      fail("Expected SAXException");
    }
    catch (final SAXException e) {
      assertTrue(Validator.isRemoteAccessException(e));
    }
  }

  @Test
  public void testOverride() throws IOException, SAXException {
    Validator.validate(classLoader.getResource("override.xml"));
  }

  @Test
  public void testXInclude() throws IOException, SAXException {
    Validator.validate(classLoader.getResource("xinclude.xml"));
  }

  @Test
  public void testInvalid() throws IOException {
    try {
      Validator.validate(classLoader.getResource("invalid.xml"));
      fail("Expected SAXException");
    }
    catch (final SAXException e) {
      assertFalse(Validator.isRemoteAccessException(e));
      if (!e.getMessage().startsWith("cvc-datatype-valid.1.2.1: 'a' is not a valid value for 'integer'."))
        fail(e.getMessage());
    }
  }

  @Test
  public void testMainUrl() throws IOException, SAXException {
    final URL url = classLoader.getResource("override.xml");
    Validator.main(new String[] {url.toString()});
  }

  @Test
  public void testMainAbsolute() throws IOException, SAXException {
    final URL url = classLoader.getResource("override.xml");
    Validator.main(new String[] {url.getFile()});
  }

  @Test
  public void testMainRelative() throws IOException, SAXException {
    final URL url = classLoader.getResource("override.xml");
    final String cwd = new File("").getAbsolutePath();
    final String path = url.getFile().substring(cwd.length() + 1);
    Validator.main(new String[] {path});
  }
}