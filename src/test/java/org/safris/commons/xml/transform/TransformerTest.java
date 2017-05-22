/* Copyright (c) 2016 lib4j
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

package org.safris.commons.xml.transform;

import java.io.File;
import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.junit.Assert;
import org.junit.Test;
import org.safris.commons.io.Files;

public class TransformerTest {
  @Test
  public void testTransform() throws TransformerException, IOException {
    final File destFile = new File("target/generated-test-resources/test.txt");
    if (destFile.exists())
      destFile.delete();

    Transformer.transform(new File("src/test/resources/test.xsl").toURI().toURL(), new File("src/test/resources/test.xml").toURI().toURL(), destFile);
    final String string = new String(Files.getBytes(destFile));
    Assert.assertEquals("<HTML>\n   <HEAD>\n      <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n      <TITLE></TITLE>\n   </HEAD>\n   <BODY>\n      <H1>Hello, World!</H1>\n      <DIV>from <I>An XSLT Programmer</I></DIV>\n   </BODY>\n</HTML>", string);
  }
}