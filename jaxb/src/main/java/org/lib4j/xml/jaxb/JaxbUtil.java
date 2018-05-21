/* Copyright (c) 2017 lib4j
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

package org.lib4j.xml.jaxb;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.lib4j.net.CachedURL;
import org.lib4j.xml.sax.LoggingErrorHandler;
import org.lib4j.xml.sax.Validator;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

public final class JaxbUtil {
  private static final String DEFAULT = "##default";

  @SuppressWarnings("unchecked")
  public static <T>String toXMLString(final T binding) throws JAXBException {
    final StringWriter stringWriter = new StringWriter();
    final JAXBContext jaxbContext = JAXBContext.newInstance(binding.getClass());
    final Marshaller marshaller = jaxbContext.createMarshaller();

    marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

    final XmlRootElement xmlRootElement = binding.getClass().getAnnotation(XmlRootElement.class);
    if (xmlRootElement != null) {
      marshaller.marshal(binding, stringWriter);
      return stringWriter.toString();
    }

    final XmlType xmlType = binding.getClass().getAnnotation(XmlType.class);
    final String localName = DEFAULT.equals(xmlType.name()) ? binding.getClass().getSimpleName() : xmlType.name();
    final String namespace;
    if (DEFAULT.equals(xmlType.namespace())) {
      final XmlSchema xmlSchema = binding.getClass().getPackage().getAnnotation(XmlSchema.class);
      namespace = xmlSchema != null ? xmlSchema.namespace() : DEFAULT;
    }
    else {
      namespace = xmlType.namespace();
    }

    final QName qName = new QName(namespace, localName);
    final JAXBElement<T> element = new JAXBElement<T>(qName, (Class<T>)binding.getClass(), binding);
    marshaller.marshal(element, stringWriter);
    return stringWriter.toString();
  }

  public static <T>T parse(final Class<T> cls, final URL url) throws IOException, SAXException {
    return parse(cls, Thread.currentThread().getContextClassLoader(), url, new LoggingErrorHandler(), true);
  }

  public static <T>T parse(final Class<T> cls, final ClassLoader classLoader, final URL url) throws IOException, SAXException {
    return parse(cls, classLoader, url, new LoggingErrorHandler(), true);
  }

  public static <T>T parse(final Class<T> cls, final URL url, final boolean validate) throws IOException, SAXException {
    return parse(cls, Thread.currentThread().getContextClassLoader(), url, new LoggingErrorHandler(), validate);
  }

  public static <T>T parse(final Class<T> cls, final ClassLoader classLoader, final URL url, final boolean validate) throws IOException, SAXException {
    return parse(cls, classLoader, url, new LoggingErrorHandler(), validate);
  }

  public static <T>T parse(final Class<T> cls, final URL url, final ErrorHandler errorHandler, final boolean validate) throws IOException, SAXException {
    return parse(cls, Thread.currentThread().getContextClassLoader(), url, errorHandler, validate);
  }

  public static <T>T parse(final Class<T> cls, final ClassLoader classLoader, final URL url, final ErrorHandler errorHandler, final boolean validate) throws IOException, SAXException {
    final CachedURL cachedURL = validate ? Validator.validate(url, false, errorHandler) : new CachedURL(url);

    try {
      final Unmarshaller unmarshaller = JAXBContext.newInstance(cls.getPackageName(), classLoader).createUnmarshaller();
      try (final InputStream in = cachedURL.openStream()) {
        final JAXBElement<T> element = unmarshaller.unmarshal(XMLInputFactory.newInstance().createXMLStreamReader(in), cls);
        return element.getValue();
      }
    }
    catch (final FactoryConfigurationError | JAXBException e) {
      throw new UnsupportedOperationException(e);
    }
    catch (final XMLStreamException e) {
      throw new SAXException(e);
    }
  }

  private JaxbUtil() {
  }
}