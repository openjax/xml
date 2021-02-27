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

package org.openjax.xml.sax;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.xml.parsers.SAXParser;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.SchemaFactory;

import org.apache.xerces.impl.Constants;
import org.libj.net.MemoryURLStreamHandler;
import org.libj.net.URLs;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;

/**
 * A SAX-based validator for XML documents and XML Schema Definition documents
 * that conform to the <a href="https://www.w3.org/TR/xmlschema11-1/">XML Schema
 * v1.1</a> standard.
 */
public final class Validator {
  private static final ErrorHandler DEFAULT_ERROR_HANDLER = new LoggingErrorHandler();
  private static final String dynamicXmlRoot = "n892fn298n9w8nds9v";
  private static final String dynamicXmlError = "cvc-elt.1.a: Cannot find the declaration of element '" + dynamicXmlRoot + "'.";
  private static final SchemaFactory factory;

  static {
    try {
      factory = newSchemaFactory();
    }
    catch (final SAXNotRecognizedException | SAXNotSupportedException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  private static SchemaFactory newSchemaFactory() throws SAXNotRecognizedException, SAXNotSupportedException {
    final SchemaFactory factory = SchemaFactory.newInstance(Constants.W3C_XML_SCHEMA11_NS_URI);
    factory.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
    factory.setFeature("http://apache.org/xml/features/validation/schema/augment-psvi", true);
    factory.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true);
    // factory.setFeature("http://apache.org/xml/features/allow-java-encodings",
    // true);
    factory.setFeature("http://apache.org/xml/features/standard-uri-conformant", true);
    // factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl",
    // true);
    // factory.setFeature("http://apache.org/xml/features/generate-synthetic-annotations",
    // true);
    factory.setFeature("http://apache.org/xml/features/validate-annotations", true);
    factory.setFeature("http://apache.org/xml/features/honour-all-schemaLocations", true);
    // factory.setFeature("http://apache.org/xml/features/namespace-growth",
    // true);
    factory.setFeature("http://apache.org/xml/features/internal/tolerate-duplicates", true);
    factory.setFeature("http://apache.org/xml/features/validation/cta-full-xpath-checking", true);
    // factory.setFeature("http://apache.org/xml/features/validation/assert-comments-and-pi-checking",
    // true);

    // factory.setProperty("http://apache.org/xml/properties/internal/entity-manager",
    // ???);
    // factory.setProperty("http://apache.org/xml/properties/internal/symbol-table",
    // ???);
    // factory.setProperty("http://apache.org/xml/properties/internal/error-reporter",
    // ???);
    // factory.setProperty("http://apache.org/xml/properties/internal/error-handler",
    // ???);
    // factory.setProperty("http://apache.org/xml/properties/internal/entity-resolver",
    // ???);
    // factory.setProperty("http://apache.org/xml/properties/internal/grammar-pool",
    // ???);
    // factory.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation",
    // ???);
    // factory.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
    // ???);
    // factory.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource",
    // ???);
    // factory.setProperty("http://apache.org/xml/properties/security-manager",
    // ???);
    // factory.setProperty("http://apache.org/xml/properties/locale", ???);
    // factory.setProperty("http://apache.org/xml/properties/internal/validation/schema/dv-factory",
    // ???);
    // factory.setProperty("http://apache.org/xml/properties/validation/schema/version",
    // ???);
    // factory.setProperty("http://apache.org/xml/properties/validation/schema/datatype-xml-version",
    // ???);
    return factory;
  }

  /**
   * Validates the XML document contained in the specified string.
   *
   * @param xml The XML document to validate.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If the {@link ErrorHandler} throws a
   *           {@link SAXException}, if a fatal error is found and the
   *           {@link ErrorHandler} returns normally, or if any SAX errors occur
   *           during processing.
   * @throws NullPointerException If the specified string is null.
   */
  public static void validate(final String xml) throws IOException, SAXException {
    validate(xml, DEFAULT_ERROR_HANDLER);
  }

  /**
   * Validates the XML document contained in the specified string.
   *
   * @param xml The XML document to validate.
   * @param errorHandler The {@link ErrorHandler} for parsing and validation
   *          errors.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If the {@link ErrorHandler} throws a
   *           {@link SAXException}, if a fatal error is found and the
   *           {@link ErrorHandler} returns normally, or if any SAX errors occur
   *           during processing.
   * @throws NullPointerException If the specified string is null.
   */
  public static void validate(final String xml, final ErrorHandler errorHandler) throws IOException, SAXException {
    validate(MemoryURLStreamHandler.createURL(xml.getBytes()), errorHandler);
  }

  /**
   * Validates the XML document provided by the source in the specified
   * {@link URL}.
   *
   * @param url The {@link URL} providing the location for the XML document to
   *          validate.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If the {@link ErrorHandler} throws a
   *           {@link SAXException}, if a fatal error is found and the
   *           {@link ErrorHandler} returns normally, or if any SAX errors occur
   *           during processing.
   * @throws NullPointerException If the specified {@link URL} is null.
   */
  public static void validate(final URL url) throws IOException, SAXException {
    validate(url, DEFAULT_ERROR_HANDLER);
  }

  /**
   * Validates the XML document provided by the source in the specified
   * {@link URL}.
   *
   * @param url The {@link URL} providing the location for the XML document to
   *          validate.
   * @param errorHandler The {@link ErrorHandler} for parsing and validation
   *          errors.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If the {@link ErrorHandler} throws a
   *           {@link SAXException}, if a fatal error is found and the
   *           {@link ErrorHandler} returns normally, or if any SAX errors occur
   *           during processing.
   * @throws NullPointerException If the specified {@link URL} is null.
   */
  public static void validate(final URL url, final ErrorHandler errorHandler) throws IOException, SAXException {
    try (final InputStream in = url.openStream()) {
      validate(url, new CachedInputSource(null, url.toString(), null, in), null, errorHandler);
    }
  }

  /**
   * Validates the XML document provided by the source in the specified
   * {@link InputSource}.
   *
   * @param inputSource The {@link InputSource} providing the source for the XML
   *          document to validate.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If the {@link ErrorHandler} throws a
   *           {@link SAXException}, if a fatal error is found and the
   *           {@link ErrorHandler} returns normally, or if any SAX errors occur
   *           during processing.
   * @throws NullPointerException If the specified {@link InputSource} is null.
   */
  public static void validate(final InputSource inputSource) throws IOException, SAXException {
    validate(inputSource, DEFAULT_ERROR_HANDLER);
  }

  /**
   * Validates the XML document provided by the source in the specified
   * {@link InputSource}.
   *
   * @param inputSource The {@link InputSource} providing the source for the XML
   *          document to validate.
   * @param errorHandler The {@link ErrorHandler} for parsing and validation
   *          errors.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If the {@link ErrorHandler} throws a
   *           {@link SAXException}, if a fatal error is found and the
   *           {@link ErrorHandler} returns normally, or if any SAX errors occur
   *           during processing.
   * @throws NullPointerException If the specified {@link InputSource} is null.
   */
  public static void validate(final InputSource inputSource, final ErrorHandler errorHandler) throws IOException, SAXException {
    validate(null, inputSource, null, errorHandler);
  }

  /**
   * Validates the XML document provided by the source in the specified
   * {@link InputSource}.
   *
   * @param inputSource The {@link InputSource} providing the source for the XML
   *          document to validate.
   * @param previewHandler The {@link XmlPreviewHandler} for the document to
   *          validate (can be {@code null}).
   * @param errorHandler The {@link ErrorHandler} for parsing and validation
   *          errors.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If the {@link ErrorHandler} throws a
   *           {@link SAXException}, if a fatal error is found and the
   *           {@link ErrorHandler} returns normally, or if any SAX errors occur
   *           during processing.
   * @throws NullPointerException If the specified {@link InputSource} is null.
   */
  public static void validate(final InputSource inputSource, final XmlPreviewHandler previewHandler, final ErrorHandler errorHandler) throws IOException, SAXException {
    validate(null, inputSource, previewHandler, errorHandler);
  }

  /**
   * Validates the XML document provided by the source in the specified
   * {@link InputSource}.
   *
   * @param inputSource The {@link InputSource} providing the source for the XML
   *          document to validate.
   * @param preview The {@link XmlPreview} for the document to validate.
   * @param errorHandler The {@link ErrorHandler} for parsing and validation
   *          errors.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If the {@link ErrorHandler} throws a
   *           {@link SAXException}, if a fatal error is found and the
   *           {@link ErrorHandler} returns normally, or if any SAX errors occur
   *           during processing.
   * @throws NullPointerException If the specified {@link InputSource} or
   *           {@link XmlPreview} is null.
   */
  public static void validate(final InputSource inputSource, final XmlPreview preview, final ErrorHandler errorHandler) throws IOException, SAXException {
    validate(inputSource instanceof CachedInputSource ? (CachedInputSource)inputSource : new CachedInputSource(inputSource), preview, errorHandler);
  }

  /**
   * Validates the XML document provided by the stream of data in the specified
   * {@link Reader}.
   *
   * @param url The {@link URL} specifying the location of the document to
   *          validate.
   * @param inputSource The {@link InputSource} providing the source for the XML
   *          document to validate.
   * @param previewHandler The {@link XmlPreviewHandler} for the document to
   *          validate (can be {@code null}).
   * @param errorHandler The {@link ErrorHandler} for parsing and validation
   *          errors.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If the {@link ErrorHandler} throws a
   *           {@link SAXException}, if a fatal error is found and the
   *           {@link ErrorHandler} returns normally, or if any SAX errors occur
   *           during processing.
   * @throws NullPointerException If the specified {@link Reader} is null.
   */
  private static void validate(final URL url, final InputSource inputSource, final XmlPreviewHandler previewHandler, final ErrorHandler errorHandler) throws IOException, SAXException {
    final CachedInputSource cachedInputSource = inputSource instanceof CachedInputSource ? (CachedInputSource)inputSource : new CachedInputSource(inputSource);
    final XmlPreview preview = initInputSource(url, cachedInputSource, previewHandler);
    validate(cachedInputSource, preview, errorHandler);
  }

  private static XmlPreview initInputSource(final URL url, final CachedInputSource inputSource, final XmlPreviewHandler previewHandler) throws IOException, SAXParseException {
    if (previewHandler == null) {
      final XmlPreview preview = XmlPreviewParser.parse(url != null ? url : new URL(inputSource.getSystemId()), inputSource);
      inputSource.getCharacterStream().close();
      return preview;
    }

    final XmlPreview preview = previewHandler.toXmlPreview();
    previewHandler.reset();
    return preview;
  }

  /**
   * Validates the XML document provided by the stream of data in the specified
   * {@link Reader}.
   *
   * @param inputSource The {@link CachedInputSource} providing the source for
   *          the XML document to validate.
   * @param preview The {@link XmlPreview} for the document to validate .
   * @param errorHandler The {@link ErrorHandler} for parsing and validation
   *          errors.
   * @throws IOException If an I/O error has occurred.
   * @throws SAXException If the {@link ErrorHandler} throws a
   *           {@link SAXException}, if a fatal error is found and the
   *           {@link ErrorHandler} returns normally, or if any SAX errors occur
   *           during processing.
   * @throws NullPointerException If the specified {@link InputSource} or
   *           {@link XmlPreview} is null.
   */
  private static void validate(final CachedInputSource inputSource, final XmlPreview preview, final ErrorHandler errorHandler) throws IOException, SAXException {
    try {
      final SAXParser parser = SAXParsers.newParser(false);
      final SAXSource saxSource;
      if (preview.isSchema()) {
        final StringBuilder xml = new StringBuilder();
        xml.append('<').append(dynamicXmlRoot);
        xml.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        if (preview.getTargetNamespace() != null && preview.getTargetNamespace().length() > 0) {
          xml.append(" xmlns=\"").append(preview.getTargetNamespace()).append('"');
          xml.append(" xsi:schemaLocation=\"").append(preview.getTargetNamespace()).append(' ').append(inputSource.getSystemId()).append('"');
        }
        else {
          xml.append(" xsi:noNamespaceSchemaLocation=\"").append(inputSource.getSystemId()).append('"');
        }

        xml.append("/>");
        saxSource = new SAXSource(parser.getXMLReader(), new InputSource(new ByteArrayInputStream(xml.toString().getBytes())));
        saxSource.setSystemId("dynamic:" + inputSource.getSystemId());
      }
      else {
        saxSource = new SAXSource(parser.getXMLReader(), inputSource);
        saxSource.setSystemId(inputSource.getSystemId());
      }

      final javax.xml.validation.Validator validator = factory.newSchema().newValidator();
      validator.setResourceResolver(new XmlCatalogResolver(preview.getCatalog()));

      final ValidatorErrorHandler validatorErrorHandler = new ValidatorErrorHandler(errorHandler, inputSource, preview.isSchema() || preview.getImports() != null || preview.getIncludes() != null);
      validator.setErrorHandler(validatorErrorHandler);

      try {
        validator.validate(saxSource);
      }
      catch (final IOException | SAXException e) {
        checkException(validatorErrorHandler, e);
      }

      checkException(validatorErrorHandler, null);
    }
    finally {
      if (preview != null)
        preview.getCatalog().close();
    }
  }

  @SuppressWarnings("unchecked")
  private static <E extends Exception>void checkException(final ValidatorErrorHandler validatorErrorHandler, final Throwable suppressor) throws E, SAXParseException {
    if (validatorErrorHandler.errors != null) {
      final Iterator<SAXParseException> iterator = validatorErrorHandler.errors.iterator();
      final SAXParseException exception = iterator.next();
      while (iterator.hasNext())
        exception.addSuppressed(iterator.next());

      if (suppressor == null)
        throw exception;

      suppressor.addSuppressed(exception);
      throw (E)suppressor;
    }
    else if (suppressor != null) {
      throw (E)suppressor;
    }
  }

  private static final class ValidatorErrorHandler extends DelegateErrorHandler {
    private final InputSource inputSource;
    private final boolean hasSchema;
    private List<SAXParseException> errors;

    private ValidatorErrorHandler(final ErrorHandler handler, final InputSource inputSource, final boolean hasSchema) {
      super(handler);
      this.inputSource = Objects.requireNonNull(inputSource);
      this.hasSchema = hasSchema;
    }

    @Override
    public void warning(final SAXParseException e) throws SAXException {
      if (!e.getMessage().startsWith("schema_reference.4: Failed to read schema document '") || hasSchema)
        super.warning(e);
    }

    @Override
    public void error(final SAXParseException e) throws SAXException {
      if (dynamicXmlError.equals(e.getMessage()))
        return;

      if (e.getMessage().startsWith("cvc-elt.1.a: Cannot find the declaration of element ") && !hasSchema) {
        warning(new SAXParseException("There is no schema or DTD associated with the document", inputSource.getPublicId(), inputSource.getSystemId(), 0, 0));
      }
      else {
        if (errors == null)
          errors = new ArrayList<>();

        errors.add(e);
        super.error(e);
      }
    }
  }

  /**
   * Specifies whether the provided exception could be the result of the JVM
   * being offline.
   *
   * @param exception The {@link IOException} to test.
   * @return Whether the provided exception could be the result of the JVM being
   *         offline.
   * @throws NullPointerException If the provided {@link IOException} is null.
   */
  public static boolean isRemoteAccessException(final IOException exception) {
    final String methodName = exception.getStackTrace()[0].getMethodName();
    return "openConnection".equals(methodName) || "connect".equals(methodName) || "socketConnect".equals(methodName);
  }

  /**
   * Specifies whether the provided exception could be the result of the JVM
   * being offline.
   *
   * @param exception The {@link SAXException} to test.
   * @return Whether the provided exception could be the result of the JVM being
   *         offline.
   * @throws NullPointerException If the provided {@link SAXException} is null.
   */
  public static boolean isRemoteAccessException(final SAXException exception) {
    final String message = exception.getMessage();
    final int start = message.indexOf("Failed to read schema document '");
    if (start == -1)
      return false;

    final int end = message.indexOf("', because", start + 32);
    final String failedDocument = message.substring(start + 32, end);

    try {
      return !URLs.isLocal(new URL(failedDocument));
    }
    catch (final MalformedURLException e) {
      return false;
    }
  }

  private Validator() {
  }
}