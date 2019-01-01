/* Copyright (c) 2017 OpenJAX
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

package org.openjax.classic.xml.transform;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public final class Transformer {
  private static TransformerFactory factory;

  static {
    try {
      factory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", null);
    }
    catch (final TransformerFactoryConfigurationError e) {
      factory = TransformerFactory.newInstance();
    }
  }

  public static void transform(final URL stylesheet, final URL in, final File out) throws IOException, TransformerException {
    try (final InputStream input = in.openStream()) {
      final StreamSource streamSource = new StreamSource(stylesheet.openStream(), stylesheet.toURI().toString());
      final javax.xml.transform.Transformer transformer = factory.newTransformer(streamSource);
      transformer.transform(new StreamSource(input, in.toURI().toString()), new StreamResult(out));
    }
    catch (final URISyntaxException e) {
      throw new TransformerException(e);
    }
  }

  private Transformer() {
  }
}