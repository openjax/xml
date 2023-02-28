/* Copyright (c) 2018 OpenJAX
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

package org.openjax.xml.api;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class XmlElementTest {
  @Test
  public void test() {
    try {
      new XmlElement(null);
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      new XmlElement("a", Collections.singletonMap(null, "c")).toString();
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      new XmlElement("a", Collections.singletonMap("b", null)).toString();
      fail("Expected NullPointerException");
    }
    catch (final NullPointerException e) {
    }

    try {
      new XmlElement("");
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      new XmlElement("a", Collections.singletonMap("", "c")).toString();
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      new XmlElement("123");
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      new XmlElement("a", Collections.singletonMap("123", "c")).toString();
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      new XmlElement("123");
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    try {
      new XmlElement("a").toString(-1);
      fail("Expected IllegalArgumentException");
    }
    catch (final IllegalArgumentException e) {
    }

    assertEquals("<a/>", new XmlElement("a").toString());
    assertEquals("<a b=\"null\"/>", new XmlElement("a", Collections.singletonMap("b", String.valueOf((Object)null))).toString());
    assertEquals("<a b=\"&amp;\"/>", new XmlElement("a", Collections.singletonMap("b", "&")).toString());

    assertEquals("<a>null</a>", new XmlElement("a", Collections.singletonList(String.valueOf((Object)null))).toString());
    assertEquals("<a>hello</a>", new XmlElement("a", Collections.singletonList("hello")).toString());

    final Map<String,Object> attrs = Collections.singletonMap("x", "y");
    final XmlElement b = new XmlElement("b:b", attrs);
    final XmlElement a = new XmlElement("a", Collections.singletonList(b));
    assertEquals("<a><b:b x=\"y\"/></a>", a.toString());
    assertEquals("<a>\n  <b:b x=\"y\"/>\n</a>", a.toString(2));

    final XmlElement a2 = a.clone();
    assertNotEquals("", a2);
    assertNotSame(a, a2);
    assertEquals(a, a2);
    assertEquals(a.hashCode(), a2.hashCode());
    assertEquals(b, a.getElements().iterator().next());
    assertEquals(attrs, b.getAttributes());
    assertEquals("a", a.getName());

    final XmlElement c = new XmlElement("c:c", attrs);
    b.setElements(new ArrayList<>());
    b.getElements().add(c);
    assertEquals("<a>\n  <b:b x=\"y\">\n    <c:c x=\"y\"/>\n  </b:b>\n</a>", a.toString(2));
    c.setAttributes(Collections.singletonMap("s", "t"));
    assertEquals("<a>\n  <b:b x=\"y\">\n    <c:c s=\"t\"/>\n  </b:b>\n</a>", a.toString(2));
    b.getElements().add("after");
    assertEquals("<a><b:b x=\"y\"><c:c s=\"t\"/>after</b:b></a>", a.toString());
    assertEquals("<a>\n  <b:b x=\"y\">\n    <c:c s=\"t\"/>\n    after\n  </b:b>\n</a>", a.toString(2));
    ((List)b.getElements()).add(0, "before");
    assertEquals("<a><b:b x=\"y\">before<c:c s=\"t\"/>after</b:b></a>", a.toString());
    assertEquals("<a>\n  <b:b x=\"y\">\n    before\n    <c:c s=\"t\"/>\n    after\n  </b:b>\n</a>", a.toString(2));
  }
}