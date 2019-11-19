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

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;
import org.libj.net.URLs;

public class XMLManifestParserTest {
  private static XmlDigest test(final String fileName, final boolean expectXsd) throws IOException {
    final URL url = ClassLoader.getSystemClassLoader().getResource(fileName);
//    try {
      final XmlDigest digest = XmlDigestParser.parse(url);
      assertEquals(expectXsd, digest.isSchema());
//      assertEquals('<', reader.read());
//      final char[] chars = new char[2048];
//      reader.read(chars);
//      System.err.println("<" + new String(chars));
//      System.err.println("\n\n\n");
//      assertEquals('!', reader.read());
      return digest;
//    }
  }

  private static XmlDigest testXsd(final String fileName) throws IOException {
    return test(fileName, true);
  }

  private static XmlDigest testXml(final String fileName) throws IOException {
    return test(fileName, false);
  }

  @Test
  public void testEmptyXsd() throws IOException {
    assertEquals(null, testXsd("empty.xsd").getTargetNamespace());
  }

  @Test
  public void testTestXsd() throws IOException {
    assertEquals("http://www.openjax.org/xml/test.xsd", testXsd("test.xsd").getTargetNamespace());
  }

  @Test
  public void testLocalXsd() throws IOException {
    assertEquals("http://www.openjax.org/xml/local.xsd", testXsd("local.xsd").getTargetNamespace());
  }

  @Test
  public void testRemoteXsd() throws IOException {
    assertEquals("http://www.openjax.org/xml/remote.xsd", testXsd("remote.xsd").getTargetNamespace());
  }

  @Test
  public void testNoNamespaceXsd() throws IOException {
    assertEquals(null, testXsd("noNamespace.xsd").getTargetNamespace());
  }

  @Test
  public void testNoNamespaceXml() throws IOException {
    final XmlDigest digest = testXml("invalid.xml");
    assertEquals("test.xsd", URLs.getName(digest.getImports().get(digest.getRootElement().getNamespaceURI())));
  }

  @Test
  public void testRemoteXml() throws IOException {
    final XmlDigest digest = testXml("remote.xml");
    assertEquals("remote.xsd", URLs.getName(digest.getImports().get(digest.getRootElement().getNamespaceURI())));
  }

  @Test
  public void testValidXml() throws IOException {
    final XmlDigest digest = testXml("valid.xml");
    assertEquals("test.xsd", URLs.getName(digest.getImports().get(digest.getRootElement().getNamespaceURI())));
  }

  @Test
  public void testEmptyXml() throws IOException {
    final XmlDigest digest = testXml("empty.xml");
    assertNull(digest.getImports());
  }

  @Test
  public void testDoctypeXml() throws Exception {
    final URL url = ClassLoader.getSystemClassLoader().getResource("doctype.xml");
//    try (final ReplayReader reader = new ReplayReader(new InputStreamReader(url.openStream()))) {
      XmlDigestParser.parse(url);

//      final char[] chars = new char[19];
//      assertEquals(chars.length, reader.read(chars));
//      assertEquals("<!DOCTYPE catalog [", new String(chars));
//    }
  }
}