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

/**
 * Simple implementation of the the {@link LSInput} interface for the input
 * source of XML data.
 */
public class LSInputImpl implements LSInput {
  private Reader reader;
  private InputStream inputStream;
  private String stringData;
  private String systemId;
  private String publicId;
  private String baseURI;
  private String encoding;
  private boolean certifiedText;

  /**
   * Creates a new {@link LSInputImpl} with the specified parameters.
   *
   * @param systemId The system identifier, a URI reference
   *          [<a href='http://www.ietf.org/rfc/rfc2396.txt'>IETF RFC 2396</a>],
   *          for this input source.
   * @param publicId The public identifier for this input source.
   * @param baseURI The base URI to be used (see section 5.1.4 in
   *          [<a href='http://www.ietf.org/rfc/rfc2396.txt'>IETF RFC 2396</a>])
   *          for resolving a relative <code>systemId</code> to an absolute URI.
   */
  public LSInputImpl(final String systemId, final String publicId, final String baseURI) {
    this.systemId = systemId;
    this.publicId = publicId;
    this.baseURI = baseURI;
  }

  @Override
  public Reader getCharacterStream() {
    return this.reader;
  }

  @Override
  public void setCharacterStream(final Reader characterStream) {
    this.reader = characterStream;
  }

  @Override
  public InputStream getByteStream() {
    return inputStream;
  }

  @Override
  public void setByteStream(final InputStream byteStream) {
    this.inputStream = byteStream;
  }

  @Override
  public String getStringData() {
    return this.stringData;
  }

  @Override
  public void setStringData(final String stringData) {
    this.stringData = stringData;
  }

  @Override
  public String getSystemId() {
    return this.systemId;
  }

  @Override
  public void setSystemId(final String systemId) {
    this.systemId = systemId;
  }

  @Override
  public String getPublicId() {
    return this.publicId;
  }

  @Override
  public void setPublicId(final String publicId) {
    this.publicId = publicId;
  }

  @Override
  public String getBaseURI() {
    return this.baseURI;
  }

  @Override
  public void setBaseURI(final String baseURI) {
    this.baseURI = baseURI;
  }

  @Override
  public String getEncoding() {
    return this.encoding;
  }

  @Override
  public void setEncoding(final String encoding) {
    this.encoding = encoding;
  }

  @Override
  public boolean getCertifiedText() {
    return this.certifiedText;
  }

  @Override
  public void setCertifiedText(final boolean certifiedText) {
    this.certifiedText = certifiedText;
  }
}