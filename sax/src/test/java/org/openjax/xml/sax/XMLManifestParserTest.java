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
  private static XmlPreview test(final String fileName, final boolean expectXsd) throws IOException {
    final URL url = ClassLoader.getSystemClassLoader().getResource(fileName);
//    try {
      final XmlPreview preview = XmlPreviewParser.parse(url);
      assertEquals(expectXsd, preview.isSchema());
//      assertEquals('<', reader.read());
//      final char[] chars = new char[2048];
//      reader.read(chars);
//      System.err.println("<" + new String(chars));
//      System.err.println("\n\n\n");
//      assertEquals('!', reader.read());
      return preview;
//    }
  }

  private static XmlPreview testXsd(final String fileName) throws IOException {
    return test(fileName, true);
  }

  private static XmlPreview testXml(final String fileName) throws IOException {
    return test(fileName, false);
  }

  @Test
  public void testEmptyXsd() throws IOException {
    assertNull(testXsd("empty.xsd").getTargetNamespace());
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
    assertNull(testXsd("noNamespace.xsd").getTargetNamespace());
  }

  @Test
  public void testNoNamespaceXml() throws IOException {
    final XmlPreview preview = testXml("invalid.xml");
    assertEquals("test.xsd", URLs.getName(preview.getImports().get(preview.getRootElement().getNamespaceURI())));
  }

  @Test
  public void testRemoteXml() throws IOException {
    final XmlPreview preview = testXml("remote.xml");
    assertEquals("remote.xsd", URLs.getName(preview.getImports().get(preview.getRootElement().getNamespaceURI())));
  }

  @Test
  public void testValidXml() throws IOException {
    final XmlPreview preview = testXml("valid.xml");
    assertEquals("test.xsd", URLs.getName(preview.getImports().get(preview.getRootElement().getNamespaceURI())));
  }

  @Test
  public void testEmptyXml() throws IOException {
    final XmlPreview preview = testXml("empty.xml");
    assertNull(preview.getImports());
  }

  @Test
  public void testDoctypeXml() throws Exception {
    final URL url = ClassLoader.getSystemClassLoader().getResource("doctype.xml");
//    try (final ReplayReader reader = new ReplayReader(new InputStreamReader(url.openStream()))) {
      XmlPreviewParser.parse(url);

//      final char[] chars = new char[19];
//      assertEquals(chars.length, reader.read(chars));
//      assertEquals("<!DOCTYPE catalog [", new String(chars));
//    }
  }
}