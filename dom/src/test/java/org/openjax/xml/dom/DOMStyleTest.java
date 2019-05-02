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

package org.openjax.xml.dom;

import static org.junit.Assert.*;

import org.junit.Test;

public class DOMStyleTest {
  @Test
  public void testMerge() {
    assertNull(DOMStyle.merge((DOMStyle[])null));

    // Condition: default
    DOMStyle option = DOMStyle.merge();
    assertFalse(option.isIndent());
    assertFalse(option.isIgnoreNamespaces());

    // Condition: indent
    option = DOMStyle.merge(DOMStyle.INDENT);
    assertTrue(option.isIndent());
    assertFalse(option.isIgnoreNamespaces());

    // Condition: ignoreNamespases
    option = DOMStyle.merge(DOMStyle.IGNORE_NAMESPACES);
    assertTrue(option.isIgnoreNamespaces());
    assertFalse(option.isIndent());

    // Condition: indent & ignoreNamespases
    option = DOMStyle.merge(DOMStyle.INDENT, DOMStyle.IGNORE_NAMESPACES);
    assertTrue(option.isIgnoreNamespaces());
    assertTrue(option.isIndent());
  }
}