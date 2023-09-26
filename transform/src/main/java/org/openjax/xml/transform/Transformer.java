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

package org.openjax.xml.transform;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

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

  public static String transform(final URL stylesheet, final String in, final String systemId) throws IOException, TransformerException {
    return transform(stylesheet, in, systemId, (Map<String,String>)null);
  }

  public static String transform(final URL stylesheet, final String in, final String systemId, final Map<String,String> parameters) throws IOException, TransformerException {
    return transform(stylesheet, new ByteArrayInputStream(in.getBytes()), systemId, parameters);
  }

  public static void transform(final URL stylesheet, final String in, final String systemId, final File out) throws IOException, TransformerException {
    transform(stylesheet, in, systemId, out, (Map<String,String>)null);
  }

  public static void transform(final URL stylesheet, final String in, final String systemId, final File out, final Map<String,String> parameters) throws IOException, TransformerException {
    transform(stylesheet, new ByteArrayInputStream(in.getBytes()), systemId, out, parameters);
  }

  public static void transform(final URL stylesheet, final String in, final String systemId, final OutputStream out) throws IOException, TransformerException {
    transform(stylesheet, in, systemId, out, (Map<String,String>)null);
  }

  public static void transform(final URL stylesheet, final String in, final String systemId, final OutputStream out, final Map<String,String> parameters) throws IOException, TransformerException {
    transform(stylesheet, new ByteArrayInputStream(in.getBytes()), systemId, out, parameters);
  }

  public static String transform(final URL stylesheet, final InputStream in, final String systemId) throws IOException, TransformerException {
    return transform(stylesheet, in, systemId, (Map<String,String>)null);
  }

  public static String transform(final URL stylesheet, final InputStream in, final String systemId, final Map<String,String> parameters) throws IOException, TransformerException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    transform(stylesheet, new StreamSource(in, systemId), new StreamResult(out), parameters);
    return new String(out.toByteArray());
  }

  public static void transform(final URL stylesheet, final InputStream in, final String systemId, final File out) throws IOException, TransformerException {
    transform(stylesheet, in, systemId, out, (Map<String,String>)null);
  }

  public static void transform(final URL stylesheet, final InputStream in, final String systemId, final File out, final Map<String,String> parameters) throws IOException, TransformerException {
    out.createNewFile();
    transform(stylesheet, new StreamSource(in, systemId), new StreamResult(out), parameters);
  }

  public static void transform(final URL stylesheet, final InputStream in, final String systemId, final OutputStream out) throws IOException, TransformerException {
    transform(stylesheet, in, systemId, out, (Map<String,String>)null);
  }

  public static void transform(final URL stylesheet, final InputStream in, final String systemId, final OutputStream out, final Map<String,String> parameters) throws IOException, TransformerException {
    transform(stylesheet, new StreamSource(in, systemId), new StreamResult(out), parameters);
  }

  public static String transform(final URL stylesheet, final URL in) throws IOException, TransformerException {
    return transform(stylesheet, in, (Map<String,String>)null);
  }

  public static String transform(final URL stylesheet, final URL in, final Map<String,String> parameters) throws IOException, TransformerException {
    try (final InputStream input = in.openStream()) {
      final ByteArrayOutputStream out = new ByteArrayOutputStream();
      transform(stylesheet, new StreamSource(input, in.toString()), new StreamResult(out), parameters);
      return new String(out.toByteArray());
    }
  }

  public static void transform(final URL stylesheet, final URL in, final File out) throws IOException, TransformerException {
    transform(stylesheet, in, out, (Map<String,String>)null);
  }

  public static void transform(final URL stylesheet, final URL in, final File out, final Map<String,String> parameters) throws IOException, TransformerException {
    try (final InputStream input = in.openStream()) {
      transform(stylesheet, new StreamSource(input, in.toString()), new StreamResult(out), parameters);
    }
  }

  public static void transform(final URL stylesheet, final URLConnection in, final File out) throws IOException, TransformerException {
    transform(stylesheet, in, out, (Map<String,String>)null);
  }

  public static void transform(final URL stylesheet, final URLConnection in, final File out, final Map<String,String> parameters) throws IOException, TransformerException {
    try (final InputStream input = in.getInputStream()) {
      transform(stylesheet, new StreamSource(input, in.toString()), new StreamResult(out), parameters);
    }
  }

  public static void transform(final URL stylesheet, final URL in, final OutputStream out) throws IOException, TransformerException {
    transform(stylesheet, in, out, (Map<String,String>)null);
  }

  public static void transform(final URL stylesheet, final URL in, final OutputStream out, final Map<String,String> parameters) throws IOException, TransformerException {
    try (final InputStream input = in.openStream()) {
      transform(stylesheet, new StreamSource(input, in.toString()), new StreamResult(out), parameters);
    }
  }

  public static void transform(final URL stylesheet, final URLConnection in, final OutputStream out) throws IOException, TransformerException {
    transform(stylesheet, in, out, (Map<String,String>)null);
  }

  public static void transform(final URL stylesheet, final URLConnection in, final OutputStream out, final Map<String,String> parameters) throws IOException, TransformerException {
    try (final InputStream input = in.getInputStream()) {
      transform(stylesheet, new StreamSource(input, in.toString()), new StreamResult(out), parameters);
    }
  }

  private static void transform(final URL stylesheet, final StreamSource in, final StreamResult out, final Map<String,String> parameters) throws IOException, TransformerException {
    try (final InputStream stylesheetIn = stylesheet.openStream()) {
      final StreamSource streamSource = new StreamSource(stylesheetIn, stylesheet.toString());
      final javax.xml.transform.Transformer transformer = factory.newTransformer(streamSource);
      if (parameters != null && parameters.size() > 0)
        for (final Map.Entry<String,String> entry : parameters.entrySet()) // [S]
          transformer.setParameter(entry.getKey(), entry.getValue());

      transformer.transform(in, out);
    }
  }

  private Transformer() {
  }
}