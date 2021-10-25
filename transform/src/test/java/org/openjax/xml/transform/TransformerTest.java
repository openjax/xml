/* Copyright (c) 2016 OpenJAX
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

package org.openjax.xml.transform;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.transform.TransformerException;

import org.junit.Test;

public class TransformerTest {
  @Test
  public void testTransform() throws TransformerException, IOException {
    final File destFile = new File("target/generated-test-resources/test.txt");
    if (destFile.exists())
      destFile.delete();
    else
      destFile.getParentFile().mkdirs();

    Transformer.transform(ClassLoader.getSystemClassLoader().getResource("test.xsl"), ClassLoader.getSystemClassLoader().getResource("test.xml"), destFile);
    final String string = new String(Files.readAllBytes(destFile.toPath()));
    assertEquals("<!DOCTYPE HTML><HTML>\n   <HEAD>\n      <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n      <TITLE></TITLE>\n   </HEAD>\n   <BODY>\n      <H1>Hello, World!</H1>\n      <DIV>from <I>An XSLT Programmer</I></DIV>\n   </BODY>\n</HTML>", string);
  }
}