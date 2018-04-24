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

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class ValidatorTest {
  private static final Logger logger = LoggerFactory.getLogger(ValidatorTest.class);

  @Test
  public void testValidate() throws Exception {
    Validator.validate(new File("src/test/resources/valid.xml").toURI().toURL(), true);
    try {
      Validator.validate(new File("src/test/resources/invalid.xml").toURI().toURL(), true);
      Assert.fail("Should have failed.");
    }
    catch (final SAXException e) {
      if (!e.getMessage().startsWith("cvc-datatype-valid.1.2.1: 'a' is not a valid value for 'integer'."))
        Assert.fail(e.getMessage());
    }

    try {
      Validator.validate(new File("src/test/resources/test.xsd").toURI().toURL(), true);
    }
    catch (final SAXException e) {
      System.err.println(e.getMessage());
      if (e.getMessage() != null && e.getMessage().startsWith("schema_reference.4: Failed to read schema document 'http://www.w3.org/2001/"))
        logger.info(e.getMessage());
      else
        throw e;
    }
  }
}