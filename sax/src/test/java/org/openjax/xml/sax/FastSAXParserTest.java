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
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;
import org.libj.io.ReplayReader;
import org.xml.sax.SAXParseException;

public class FastSAXParserTest {
  private static String read(final Reader in, final int len) throws IOException {
    final char[] chars = new char[len];
    in.read(chars);
    return new String(chars);
  }

  enum Type {
    DOST,
    DOEN,
    DCST,
    DCEN,
    CMNT,
    DOCT,
    ELOP,
    ELCL,
    ELEN,
    ATTR
  }

  private static final class Event {
    private final Type type;
    private final String[] fields;

    private Event(final Type type, final String[] fields) {
      this.type = type;
      this.fields = fields;
    }

    @Override
    public String toString() {
      return Arrays.toString(fields);
    }
  }

  private static final class FasterTestHandler implements FasterSAXHandler {
    private final Iterator<Event> iterator;
    private final Reader in;

    private FasterTestHandler(final Iterator<Event> iterator, final Reader in) {
      this.iterator = iterator;
      this.in = in;
    }

    @Override
    public boolean startDocument() throws IOException {
      assertEvent(iterator, in, Type.DOST);
      return true;
    }

    @Override
    public boolean endDocument() throws IOException {
      assertEvent(iterator, in, Type.DOEN);
      return true;
    }

    @Override
    public boolean startDeclaration(int nameLen) throws IOException {
      assertEvent(iterator, in, Type.DCST);
      return true;
    }

    @Override
    public boolean endDeclaration() throws IOException {
      assertEvent(iterator, in, Type.DCEN);
      return true;
    }

    @Override
    public boolean doctype(final int len) throws IOException {
      assertEvent(iterator, in, Type.DOCT, len);
      return true;
    }

    @Override
    public boolean comment(final int len) throws IOException {
      assertEvent(iterator, in, Type.CMNT, len);
      return true;
    }

    @Override
    public boolean startElement(final int prefix, final int localName) throws IOException {
      assertEvent(iterator, in, Type.ELOP, prefix, localName);
      return true;
    }

    @Override
    public boolean startElement() throws IOException {
      assertEvent(iterator, in, Type.ELCL);
      return true;
    }

    @Override
    public boolean endElement() throws IOException {
      assertEvent(iterator, in, Type.ELEN);
      return true;
    }

    @Override
    public boolean attribute(final int prefix, final int localName, final int skip, final int value) throws IOException {
      assertEvent(iterator, in, Type.ATTR, prefix, localName, skip, value);
      return true;
    }
  };

  private static void add(final ArrayList<? super Event> events, final Type type, final String ... fields) {
    events.add(new Event(type, fields));
  }

  private static void assertEvent(final Iterator<Event> iterator, final Reader in, final Type type, final int ... indices) throws IOException {
    final String[] fields = new String[indices.length];
    for (int i = 0, i$ = indices.length; i < i$; ++i) { // [A]
      assertTrue(String.valueOf(indices[i]), indices[i] > -1);
      fields[i] = read(in, indices[i]);
    }

    assertTrue("Expected " + type + ": " + Arrays.toString(fields), iterator.hasNext());
    final Event event = iterator.next();
    assertEquals(event.toString(), event.type, type);
    assertEquals(event.fields.length, indices.length);
    for (int i = 0, i$ = indices.length; i < i$; ++i) // [A]
      assertEquals(event.fields[i], fields[i]);
  }

  private static void test(final ArrayList<Event> events, final URL url) throws IOException, SAXParseException {
    final Iterator<Event> iterator = events.iterator();
    try (final Reader in = new ReplayReader(new InputStreamReader(url.openStream()))) {
      FastSAXParser.parse(in, new FasterTestHandler(iterator, in));
    }
  }

  @Test
  public void testTestXsd() throws IOException, SAXParseException {
    final ArrayList<Event> events = new ArrayList<>();
    add(events, Type.DOST);
    add(events, Type.CMNT, "\n  Copyright (c) 2006 OpenJAX\n\n  Permission is hereby granted, free of charge, to any person obtaining a copy\n  of this software and associated documentation files (the \"Software\"), to deal\n  in the Software without restriction, including without limitation the rights\n  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n  copies of the Software, and to permit persons to whom the Software is\n  furnished to do so, subject to the following conditions:\n\n  The above copyright notice and this permission notice shall be included in\n  all copies or substantial portions of the Software.\n\n  You should have received a copy of The MIT License (MIT) along with this\n  program. If not, see <http://opensource.org/licenses/MIT/>.\n");
    add(events, Type.ELOP, "xs:", "schema");
    add(events, Type.ATTR, "", "elementFormDefault", "=\"", "qualified");
    add(events, Type.ATTR, "", "targetNamespace", "=\"", "http://openuri.org/testNumerals");
    add(events, Type.ATTR, "xmlns:", "xs", "=\"", "http://www.w3.org/2001/XMLSchema");
    add(events, Type.ELCL);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "doc");
    add(events, Type.ELCL);
    add(events, Type.ELOP, "xs:", "complexType");
    add(events, Type.ELCL);
    add(events, Type.ELOP, "xs:", "sequence");
    add(events, Type.ELCL);
    add(events, Type.ELOP, "xs:", "choice");
    add(events, Type.ATTR, "", "minOccurs", "=\"", "0");
    add(events, Type.ATTR, "", "maxOccurs", "=\"", "unbounded");
    add(events, Type.ELCL);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "int");
    add(events, Type.ATTR, "", "type", "=\"", "xs:int");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "short");
    add(events, Type.ATTR, "", "type", "=\"", "xs:short");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "byte");
    add(events, Type.ATTR, "", "type", "=\"", "xs:byte");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "double");
    add(events, Type.ATTR, "", "type", "=\"", "xs:double");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "float");
    add(events, Type.ATTR, "", "type", "=\"", "xs:float");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "boolean");
    add(events, Type.ATTR, "", "type", "=\"", "xs:boolean");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "string");
    add(events, Type.ATTR, "", "type", "=\"", "xs:string");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "decimal");
    add(events, Type.ATTR, "", "type", "=\"", "xs:decimal");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "integer");
    add(events, Type.ATTR, "", "type", "=\"", "xs:integer");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "long");
    add(events, Type.ATTR, "", "type", "=\"", "xs:long");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "hexBinary");
    add(events, Type.ATTR, "", "type", "=\"", "xs:hexBinary");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "base64Binary");
    add(events, Type.ATTR, "", "type", "=\"", "xs:base64Binary");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "date");
    add(events, Type.ATTR, "", "type", "=\"", "xs:date");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "dateTime");
    add(events, Type.ATTR, "", "type", "=\"", "xs:dateTime");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "gYearMonth");
    add(events, Type.ATTR, "", "type", "=\"", "xs:gYearMonth");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "duration");
    add(events, Type.ATTR, "", "type", "=\"", "xs:duration");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "QName");
    add(events, Type.ATTR, "", "type", "=\"", "xs:QName");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELEN);
    add(events, Type.ELEN);
    add(events, Type.ELEN);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "attribute");
    add(events, Type.ATTR, "", "name", "=\"", "price");
    add(events, Type.ATTR, "", "type", "=\"", "xs:float");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xs:", "attribute");
    add(events, Type.ATTR, "", "name", "=\"", "quant");
    add(events, Type.ATTR, "", "type", "=\"", "xs:byte");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELEN);
    add(events, Type.DOEN);
    test(events, ClassLoader.getSystemClassLoader().getResource("numerals.xsd"));
  }

  @Test
  public void testNumeralsXml() throws IOException, SAXParseException {
    final ArrayList<Event> events = new ArrayList<>();
    add(events, Type.DOST);
    add(events, Type.CMNT, "\n  Copyright (c) 2008 OpenJAX\n\n  Permission is hereby granted, free of charge, to any person obtaining a copy\n  of this software and associated documentation files (the \"Software\"), to deal\n  in the Software without restriction, including without limitation the rights\n  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n  copies of the Software, and to permit persons to whom the Software is\n  furnished to do so, subject to the following conditions:\n\n  The above copyright notice and this permission notice shall be included in\n  all copies or substantial portions of the Software.\n\n  You should have received a copy of The MIT License (MIT) along with this\n  program. If not, see <http://opensource.org/licenses/MIT/>.\n");
    add(events, Type.ELOP, "xs:", "schema");
    add(events, Type.ATTR, "", "elementFormDefault", "=\"", "qualified");
    add(events, Type.ATTR, "", "targetNamespace", "=\"", "http://www.openjax.org/xml/test.xsd");
    add(events, Type.ATTR, "xmlns:", "xs", "=\"", "http://www.w3.org/2001/XMLSchema");
    add(events, Type.ELCL);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "parent");
    add(events, Type.ELCL);
    add(events, Type.ELOP, "xs:", "complexType");
    add(events, Type.ELCL);
    add(events, Type.ELOP, "xs:", "sequence");
    add(events, Type.ELCL);
    add(events, Type.ELOP, "xs:", "element");
    add(events, Type.ATTR, "", "name", "=\"", "child");
    add(events, Type.ELCL);
    add(events, Type.ELOP, "xs:", "complexType");
    add(events, Type.ATTR, "", "mixed", "=\"", "true");
    add(events, Type.ELCL);
    add(events, Type.ELOP, "xs:", "simpleContent");
    add(events, Type.ELCL);
    add(events, Type.ELOP, "xs:", "extension");
    add(events, Type.ATTR, "", "base", "=\"", "xs:integer");
    add(events, Type.ELCL);
    add(events, Type.ELOP, "xs:", "anyAttribute");
    add(events, Type.ATTR, "", "processContents", "=\"", "skip");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELEN);
    add(events, Type.ELEN);
    add(events, Type.ELEN);
    add(events, Type.ELEN);
    add(events, Type.ELEN);
    add(events, Type.ELEN);
    add(events, Type.ELEN);
    add(events, Type.ELEN);
    add(events, Type.DOEN);
    test(events, ClassLoader.getSystemClassLoader().getResource("test.xsd"));
  }

  @Test
  public void testDoctypeXml() throws IOException, SAXParseException {
    final ArrayList<Event> events = new ArrayList<>();
    add(events, Type.DOST);
    add(events, Type.CMNT, "\n  Copyright (c) 2019 OpenJAX\n\n  Permission is hereby granted, free of charge, to any person obtaining a copy\n  of this software and associated documentation files (the \"Software\"), to deal\n  in the Software without restriction, including without limitation the rights\n  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n  copies of the Software, and to permit persons to whom the Software is\n  furnished to do so, subject to the following conditions:\n\n  The above copyright notice and this permission notice shall be included in\n  all copies or substantial portions of the Software.\n\n  You should have received a copy of The MIT License (MIT) along with this\n  program. If not, see <http://opensource.org/licenses/MIT/>.\n");
    add(events, Type.DOCT, "DOCTYPE catalog [\n  <!NOTATION jpeg SYSTEM \"JPG\">\n  <!ENTITY prod557 SYSTEM \"prod557.jpg\" NDATA jpeg>\n  <!ENTITY prod563 SYSTEM \"prod563.jpg\" NDATA jpeg>\n]");
    add(events, Type.ELOP, "", "attribute");
    add(events, Type.ATTR, "xml:", "base", "=\"", ".");
    add(events, Type.ELCL);
    add(events, Type.ELOP, "xml:", "local");
    add(events, Type.ATTR, "", "localAnyURI", "=\"", "http://example.com?");
    add(events, Type.ATTR, "xml:", "localBase64Binary", " = \"", " aGVsbG8K ");
    add(events, Type.ATTR, "xml:", "localDateTime", "\n    =\n    \"", "2019-08-24T17:58:49Z");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELOP, "xml:", "ref");
    add(events, Type.ATTR, "", "localBoolean", "=\"", "true");
    add(events, Type.ATTR, "xml:", "localByte", "\n    =\"", "127");
    add(events, Type.ATTR, "", "localDate", "=\n    \"", "2019-08-24");
    add(events, Type.ELCL);
    add(events, Type.ELEN);
    add(events, Type.ELEN);
    add(events, Type.DOEN);
    test(events, ClassLoader.getSystemClassLoader().getResource("doctype.xml"));
  }
}