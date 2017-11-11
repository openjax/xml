/* Copyright (c) 2006 lib4j
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

package org.lib4j.xml;

import org.junit.Assert;
import org.junit.Test;

public class NamespaceBindingTest {
  private static void assertEquals(final String expected, final String uri) {
    final NamespaceBinding namespaceBinding = NamespaceBinding.parseNamespace(uri);
    final NamespaceBinding decodedBinding = NamespaceBinding.parseClassName(namespaceBinding.getClassName());
//    System.err.println(namespaceBinding.getClassName());
    Assert.assertEquals("Explicit", expected, namespaceBinding.getClassName());
    Assert.assertEquals("Diff", uri, decodedBinding.getNamespaceUri().toString());
  }

  @Test
  public void testNamespaceToPackage() {
    assertEquals("org.w3.www._2001.XMLSchema.hF8l3SXA", "http://www.w3.org/2001/XMLSchema");
    assertEquals("org._3w.www._2001.XMLSchema.hF8l3yXA", "http://www.3w.org/2001/XMLSchema");
    assertEquals("org._3w.www._2001.XMLSchema.xERkZfJd8lwA", "ssh://www.3w.org/2001/XMLSchema");
    assertEquals("org._3w.www._2001.XMLSchema.xGZ4eHM6Ly_cS7cS4", "xxs://www.3w.org/2001/XMLSchema");
    assertEquals("com.sun.java.xml.ns.j2ee.hF8l3yXA", "http://java.sun.com/xml/ns/j2ee");
    assertEquals("org.openuri.nameworld.hF8lw", "http://openuri.org/nameworld");
    assertEquals("org.xmlsoap.schemas.soap.envelope.hLjgl2eCXdQJe", "http://schemas.xmlsoap.org/soap/envelope/");
    assertEquals("com.safris.www.schema.test.hHuJd6JcA", "http://www.safris.com/schema/test");
    assertEquals("org.w3.www._1999.xhtml.hF8l3SXA", "http://www.w3.org/1999/xhtml");
    assertEquals("org.w3.www._1999.xlink.hF8l3SXA", "http://www.w3.org/1999/xlink");
    assertEquals("org.w3.www._2000._09.xmldsig.hLjgl2KCXdQJG", "http://www.w3.org/2000/09/xmldsig#");
    assertEquals("org.w3.www._2001._04.xmlenc.hLjgl2KCXdAJG", "http://www.w3.org/2001/04/xmlenc#");
    assertEquals("org.w3.www._2001._10.xmlexcc14n.hLjgl2KCXcQJbjAluQCRg", "http://www.w3.org/2001/10/xml-exc-c14n#");
    assertEquals("org.w3.www.XML._1998.namespace.hF8l3SXA", "http://www.w3.org/XML/1998/namespace");
    assertEquals("com.safris.www.schema.testtwo.hHuJd6JcA", "http://www.safris.com/schema/testtwo");
    assertEquals("org.safris.xml.schema.binding.test.unit.attributes.HHuJd6JcA", "http://xml.safris.org/schema/binding/test/unit/attributes.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.complexTypes.HHuJd6JcA", "http://xml.safris.org/schema/binding/test/unit/complexTypes.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.everything.HHuJd6JcA", "http://xml.safris.org/schema/binding/test/unit/everything.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.lists.HHuJd6JcA", "http://xml.safris.org/schema/binding/test/unit/lists.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.mixed.HHuJd6JcA", "http://xml.safris.org/schema/binding/test/unit/mixed.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.namespace.HHuJd6JcA", "http://xml.safris.org/schema/binding/test/unit/namespace.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.simpleTypes.HHuJd6JcA", "http://xml.safris.org/schema/binding/test/unit/simpleTypes.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.types.HHuJd6JcA", "http://xml.safris.org/schema/binding/test/unit/types.xsd");
    assertEquals("org.safris.xml.schema.binding.dbb.HHuJd6JcA", "http://xml.safris.org/schema/binding/dbb.xsd");
    assertEquals("org.safris.xml.toolkit.binding.manifest.HHuJd6JcA", "http://xml.safris.org/toolkit/binding/manifest.xsd");
    assertEquals("org.safris.xml.toolkit.binding.test.maven.HHuJd6JcA", "http://xml.safris.org/toolkit/binding/test/maven.xsd");
    assertEquals("org.safris.xml.toolkit.binding.tutorial.invoice.HHuJd6JcA", "http://xml.safris.org/toolkit/binding/tutorial/invoice.xsd");
    assertEquals("org.safris.xml.toolkit.sample.binding.any.HHuJd6JcA", "http://xml.safris.org/toolkit/sample/binding/any.xsd");
    assertEquals("org.safris.xml.toolkit.sample.binding.enums.HHuJd6JcA", "http://xml.safris.org/toolkit/sample/binding/enums.xsd");
    assertEquals("org.safris.xml.toolkit.sample.binding.simple.HHuJd6JcA", "http://xml.safris.org/toolkit/sample/binding/simple.xsd");
    assertEquals("org.safris.xml.toolkit.sample.binding.xsitype.HHuJd6JcA", "http://xml.safris.org/toolkit/sample/binding/xsitype.xsd");
    assertEquals("org.safris.xml.toolkit.tutorial.binding.beginner.invoice.HHuJd6JcA", "http://xml.safris.org/toolkit/tutorial/binding/beginner/invoice.xsd");
    assertEquals("test_namespace_targetNamespace.xJpCW8wlo", "test-namespace-targetNamespace");
    assertEquals("aol_liberty_config.nHuJ1$J0A", "urn:aol:liberty:config");
    assertEquals("aol_liberty_config.nHuJ1$J14J0A", "urn:aol:liberty:config:");
    assertEquals("berkeley_safris_game_chess.nJxCdbQnWkJ0A", "urn:berkeley:safris:game:chess");
    assertEquals("liberty_ac_2003_08.nH$J1qJ1yJaA", "urn:liberty:ac:2003-08");
    assertEquals("liberty_ac_2004_12.nH$J1qJ1yJaA", "urn:liberty:ac:2004-12");
    assertEquals("liberty_disco_2003_08.nH$J12J1yJaA", "urn:liberty:disco:2003-08");
    assertEquals("liberty_id_sis_pp_2003_08.nH$J1qJbuJbqJ1yJa", "urn:liberty:id-sis-pp:2003-08");
    assertEquals("liberty_iff_2003_08.nH$J1uJ1yJaA", "urn:liberty:iff:2003-08");
    assertEquals("liberty_metadata_2003_08.nJvCdcQnWkJaA", "urn:liberty:metadata:2003-08");
    assertEquals("liberty_sb_2003_08.nH$J1qJ1yJaA", "urn:liberty:sb:2003-08");
    assertEquals("oasis_names_tc_SAML_1_0_assertion.nH2J12J1qJ1yJ1mJdmJ0A", "urn:oasis:names:tc:SAML:1.0:assertion");
    assertEquals("oasis_names_tc_SAML_1_0_protocol.nH2J12J1qJ1yJ1mJdmJ0A", "urn:oasis:names:tc:SAML:1.0:protocol");
  }
}