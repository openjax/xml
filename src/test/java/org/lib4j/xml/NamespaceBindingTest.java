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
    final NamespaceBinding decodedBinding = NamespaceBinding.parsePackageName(namespaceBinding.getClassName());
    Assert.assertEquals("Explicit", expected, namespaceBinding.getClassName());
    Assert.assertEquals("Diff", uri, decodedBinding.getNamespaceUri().toString());
  }

  @Test
  public void testNamespaceToPackage() {
    assertEquals("org.w3.www._2001.XMLSchema.xIEcGGcJdlCXaI_A", "http://www.w3.org/2001/XMLSchema");
    assertEquals("com.sun.java.xml.ns.j2ee.xERwYfJd8lwA", "http://java.sun.com/xml/ns/j2ee");
    assertEquals("org.openuri.nameworld.xERwYfJcA", "http://openuri.org/nameworld");
    assertEquals("org.xmlsoap.schemas.soap.envelope.xKCODDHBLs8Eu6gS8", "http://schemas.xmlsoap.org/soap/envelope/");
    assertEquals("com.safris.www.schema.test.xGI4MNxLvRLg", "http://www.safris.com/schema/test");
    assertEquals("org.w3.www._1999.xhtml.xIEcGGcJdlCXaI9A", "http://www.w3.org/1999/xhtml");
    assertEquals("org.w3.www._1999.xlink.xIEcGGcJdlCXaI9A", "http://www.w3.org/1999/xlink");
    assertEquals("org.w3.www._2000._09.xmldsig.xIEcGGcJdlCXaI6o9AkY", "http://www.w3.org/2000/09/xmldsig#");
    assertEquals("org.w3.www._2001._04.xmlenc.xIEcGGcJdlCXaI6o8gkY", "http://www.w3.org/2001/04/xmlenc#");
    assertEquals("org.w3.www._2001._10.xmlexcc14n.xGI4MNxLtRLuJ6nwS3YS3gSM", "http://www.w3.org/2001/10/xml-exc-c14n#");
    assertEquals("org.w3.www.XML._1998.namespace.xIEcGGcJdlCXcI_A", "http://www.w3.org/XML/1998/namespace");
    assertEquals("org.safris.xml.schema.binding.test.unit.attributes.xMBHBhhwJdjQJdzghc8ObIA", "http://xml.safris.org/schema/binding/test/unit/attributes.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.complexTypes.xMBHBhhwJdjQJd0ghc8ObIA", "http://xml.safris.org/schema/binding/test/unit/complexTypes.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.everything.xMBHBhhwJdjQJdzghc8ObIA", "http://xml.safris.org/schema/binding/test/unit/everything.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.lists.xMBHBhhwJdjQJdxAhc8ObIA", "http://xml.safris.org/schema/binding/test/unit/lists.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.mixed.xMBHBhhwJdjQJdxAhc8ObIA", "http://xml.safris.org/schema/binding/test/unit/mixed.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.namespace.xMBHBhhwJdjQJdzAhc8ObIA", "http://xml.safris.org/schema/binding/test/unit/namespace.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.simpleTypes.xMBHBhhwJdjQJd0Ahc8ObIA", "http://xml.safris.org/schema/binding/test/unit/simpleTypes.xsd");
    assertEquals("org.safris.xml.schema.binding.test.unit.types.xMBHBhhwJdjQJdxAhc8ObIA", "http://xml.safris.org/schema/binding/test/unit/types.xsd");
    assertEquals("org.safris.xml.schema.binding.dbb.xKCODDHBLs0Eu7BC54c2Q", "http://xml.safris.org/schema/binding/dbb.xsd");
    assertEquals("org.safris.xml.toolkit.binding.manifest.xKCODDHBLs0Eu$BC54c2Q", "http://xml.safris.org/toolkit/binding/manifest.xsd");
    assertEquals("org.safris.xml.toolkit.binding.test.maven.xKCODDHBLs0Eu_BC54c2Q", "http://xml.safris.org/toolkit/binding/test/maven.xsd");
    assertEquals("org.safris.xml.toolkit.binding.tutorial.invoice.xMBHBhhwJdjQJdyAhc8ObIA", "http://xml.safris.org/toolkit/binding/tutorial/invoice.xsd");
    assertEquals("org.safris.xml.toolkit.sample.binding.any.xKCODDHBLs0Eu_BC54c2Q", "http://xml.safris.org/toolkit/sample/binding/any.xsd");
    assertEquals("org.safris.xml.toolkit.sample.binding.enums.xMBHBhhwJdjQJdwAhc8ObIA", "http://xml.safris.org/toolkit/sample/binding/enums.xsd");
    assertEquals("org.safris.xml.toolkit.sample.binding.simple.xMBHBhhwJdjQJdwghc8ObIA", "http://xml.safris.org/toolkit/sample/binding/simple.xsd");
    assertEquals("org.safris.xml.toolkit.sample.binding.xsitype.xMBHBhhwJdjQJdxAhc8ObIA", "http://xml.safris.org/toolkit/sample/binding/xsitype.xsd");
    assertEquals("org.safris.xml.toolkit.tutorial.binding.beginner.invoice.xMBHBhhwJdjQJd2ghc8ObIA", "http://xml.safris.org/toolkit/tutorial/binding/beginner/invoice.xsd");
    assertEquals("test_namespace_targetNamespace.xJpCW8wlo", "test-namespace-targetNamespace");
    assertEquals("aol_liberty_config.xGR1cm463E6_E6A", "urn:aol:liberty:config");
    assertEquals("aol_liberty_config.xGR1cm463E6_E68E6", "urn:aol:liberty:config:");
    assertEquals("berkeley_safris_game_chess.xII6uTcdcQnW0J1pCdA", "urn:berkeley:safris:game:chess");
    assertEquals("liberty_ac_2003_08.xGR1cm46_E61E65Et", "urn:liberty:ac:2003-08");
    assertEquals("liberty_ac_2004_12.xGR1cm46_E61E65Et", "urn:liberty:ac:2004-12");
    assertEquals("liberty_disco_2003_08.xGR1cm46_E67E65Et", "urn:liberty:disco:2003-08");
    assertEquals("liberty_id_sis_pp_2003_08.xGR1cm46_E61Et3Et1E65EtA", "urn:liberty:id-sis-pp:2003-08");
    assertEquals("liberty_iff_2003_08.xGR1cm46_E63E65Et", "urn:liberty:iff:2003-08");
    assertEquals("liberty_metadata_2003_08.xII6uTcdbwnXEJ1pCWg", "urn:liberty:metadata:2003-08");
    assertEquals("liberty_sb_2003_08.xGR1cm46_E61E65Et", "urn:liberty:sb:2003-08");
    assertEquals("oasis_names_tc_SAML_1_0_assertion.xGR1cm467E67E61E65E6zEuzE6A", "urn:oasis:names:tc:SAML:1.0:assertion");
    assertEquals("oasis_names_tc_SAML_1_0_protocol.xGR1cm467E67E61E65E6zEuzE6A", "urn:oasis:names:tc:SAML:1.0:protocol");
    assertEquals("com.safris.www.schema.testtwo.xGI4MNxLvRLg", "http://www.safris.com/schema/testtwo");
  }
}