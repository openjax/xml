package org.safris.commons.xml.transform;

import java.io.File;
import java.io.IOException;
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
    try {
      final StreamSource stylesheetSource = new StreamSource(stylesheet.openStream(), stylesheet.toURI().toASCIIString());
      final javax.xml.transform.Transformer transformer = factory.newTransformer(stylesheetSource);
      transformer.transform(new StreamSource(in.openStream(), in.toURI().toASCIIString()), new StreamResult(out));
    }
    catch (final URISyntaxException e) {
      throw new TransformerException(e);
    }
  }

  private Transformer() {
  }
}