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

package org.openjax.classic.xml.sax;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openjax.classic.xml.api.OfflineValidationException;
import org.xml.sax.SAXException;

public class ValidatorTest {
  @Test
  public void testValidate() throws Exception {
    Validator.validate(Thread.currentThread().getContextClassLoader().getResource("valid.xml"), true);

    try {
      Validator.validate(Thread.currentThread().getContextClassLoader().getResource("remote.xml"), true);
      fail("Expected OfflineValidationException");
    }
    catch (final OfflineValidationException e) {
    }

    try {
      Validator.validate(Thread.currentThread().getContextClassLoader().getResource("invalid.xml"), true);
      fail("Should have failed");
    }
    catch (final SAXException e) {
      if (!e.getMessage().startsWith("cvc-datatype-valid.1.2.1: 'a' is not a valid value for 'integer'."))
        fail(e.getMessage());
    }

    try {
      Validator.validate(Thread.currentThread().getContextClassLoader().getResource("test.xsd"), true);
    }
    catch (final SAXException e) {
      if (e.getMessage() == null || !e.getMessage().startsWith("schema_reference.4: Failed to read schema document 'http://www.w3.org/2001/"))
        throw e;
    }
  }
}