/* Copyright (c) 2014 OpenJAX
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

import java.io.InputStream;
import java.io.Reader;

import org.w3c.dom.ls.LSInput;
import org.xml.sax.InputSource;

/**
 * Simple implementation of the the {@link LSInput} interface for the input
 * source of XML data.
 */
public class LSInputImpl extends InputSource implements LSInput {
  private String stringData;
  private String baseURI;
  private boolean certifiedText;

  /**
   * Creates a new {@link LSInputImpl} with the specified parameters.
   *
   * @param publicId The public identifier for this input source.
   * @param systemId The system identifier, a URI reference
   *          [<a href='http://www.ietf.org/rfc/rfc2396.txt'>IETF RFC 2396</a>],
   *          for this input source.
   * @param baseURI The base URI to be used (see section 5.1.4 in
   *          [<a href='http://www.ietf.org/rfc/rfc2396.txt'>IETF RFC 2396</a>])
   *          for resolving a relative <code>systemId</code> to an absolute URI.
   * @param in The the byte stream for this input source.
   */
  public LSInputImpl(final String publicId, final String systemId, final String baseURI, final InputStream in) {
    setPublicId(publicId);
    setSystemId(systemId);
    setBaseURI(baseURI);
    setByteStream(in);
  }

  /**
   * Creates a new {@link LSInputImpl} with the specified parameters.
   *
   * @param publicId The public identifier for this input source.
   * @param systemId The system identifier, a URI reference
   *          [<a href='http://www.ietf.org/rfc/rfc2396.txt'>IETF RFC 2396</a>],
   *          for this input source.
   * @param baseURI The base URI to be used (see section 5.1.4 in
   *          [<a href='http://www.ietf.org/rfc/rfc2396.txt'>IETF RFC 2396</a>])
   *          for resolving a relative <code>systemId</code> to an absolute URI.
   * @param reader The the character stream for this input source.
   */
  public LSInputImpl(final String publicId, final String systemId, final String baseURI, final Reader reader) {
    setPublicId(publicId);
    setSystemId(systemId);
    setBaseURI(baseURI);
    setCharacterStream(reader);
  }

  /**
   * Creates a new {@link LSInputImpl} with the specified parameters.
   *
   * @param publicId The public identifier for this input source.
   * @param systemId The system identifier, a URI reference
   *          [<a href='http://www.ietf.org/rfc/rfc2396.txt'>IETF RFC 2396</a>],
   *          for this input source.
   * @param baseURI The base URI to be used (see section 5.1.4 in
   *          [<a href='http://www.ietf.org/rfc/rfc2396.txt'>IETF RFC 2396</a>])
   *          for resolving a relative <code>systemId</code> to an absolute URI.
   */
  public LSInputImpl(final String publicId, final String systemId, final String baseURI) {
    setPublicId(publicId);
    setSystemId(systemId);
    setBaseURI(baseURI);
  }

  @Override
  public String getStringData() {
    return stringData;
  }

  @Override
  public void setStringData(final String stringData) {
    this.stringData = stringData;
  }

  @Override
  public String getBaseURI() {
    return baseURI;
  }

  @Override
  public void setBaseURI(final String baseURI) {
    this.baseURI = baseURI;
  }

  @Override
  public boolean getCertifiedText() {
    return certifiedText;
  }

  @Override
  public void setCertifiedText(final boolean certifiedText) {
    this.certifiedText = certifiedText;
  }
}