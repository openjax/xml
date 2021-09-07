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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;

import org.libj.io.ReplayReader;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.InputSource;

/**
 * An {@link InputSource} and {@link LSInput} representation of an XML entity
 * that allows character stream data to be re-read multiple time, for optimized
 * performance reading external XML entities.
 */
public class CachedInputSource extends InputSource implements AutoCloseable, LSInput, Serializable {
  private static final long serialVersionUID = 4371604845681155607L;

  private static final class CachedReader extends ReplayReader {
    private CachedReader(final Reader in) {
      super(in);
    }

    @Override
    @SuppressWarnings("sync-override")
    public void close() {
      buffer.reset(0);
    }

    public void destroy() throws IOException {
      super.close();
    }
  }

  /**
   * Returns a {@link ReplayReader} for the byte or character data in the
   * specified {@link InputSource}.
   *
   * @param inputSource The {@link InputSource} containing the byte data as an
   *          {@link InputStream} or character data as a {@link java.io.Reader}.
   * @return A {@link ReplayReader} for the byte or character data in the
   *         specified {@link InputSource}.
   * @throws IllegalArgumentException If the specified {@link InputSource} is null.
   * @throws IllegalArgumentException If the specified {@link InputSource} does
   *           not have a byte stream or character stream.
   */
  @SuppressWarnings("resource")
  private static ReplayReader getReader(final InputSource inputSource) {
    if (inputSource.getCharacterStream() instanceof ReplayReader)
      return (ReplayReader)inputSource.getCharacterStream();

    if (inputSource.getCharacterStream() != null)
      return new ReplayReader(inputSource.getCharacterStream());

    if (inputSource.getByteStream() != null)
      // FIXME: Determine the encoding from the element declaration
      return new ReplayReader(new InputStreamReader(inputSource.getByteStream()));

    throw new IllegalArgumentException("InputSource has null CharacterStream and ByteStream");
  }

  /**
   * Creates a new {@link CachedInputSource} with the specified
   * {@code publicId}, {@code systemId}, {@code baseURI}, and
   * {@link InputStream}.
   *
   * @param publicId The public identifier.
   * @param systemId The system identifier (URI reference).
   * @param baseURI The base URI to be used for resolving a relative
   *          {@code systemId} to an absolute URI.
   * @param in A byte stream containing an XML document or other entity.
   * @throws IllegalArgumentException If the specified {@link InputStream} is null.
   * @see <a href="http://www.ietf.org/rfc/rfc2396.txt">IETF RFC 2396</a>
   */
  public CachedInputSource(final String publicId, final String systemId, final String baseURI, final InputStream in) {
    this(publicId, systemId, baseURI);
    setByteStream(in);
  }

  /**
   * Creates a new {@link CachedInputSource} with the specified
   * {@code publicId}, {@code systemId}, {@code baseURI}, and {@link Reader}.
   *
   * @param publicId The public identifier.
   * @param systemId The system identifier (URI reference).
   * @param baseURI The base URI to be used for resolving a relative
   *          {@code systemId} to an absolute URI.
   * @param reader The the character stream.
   * @throws IllegalArgumentException If the specified {@link Reader} is null.
   * @see <a href="http://www.ietf.org/rfc/rfc2396.txt">IETF RFC 2396</a>
   */
  public CachedInputSource(final String publicId, final String systemId, final String baseURI, final Reader reader) {
    this(publicId, systemId, baseURI);
    setCharacterStream(reader instanceof CachedReader ? reader : new CachedReader(reader));
  }

  /**
   * Creates a new {@link CachedInputSource} with the specified
   * {@code publicId}, {@code systemId}, and {@code baseURI}.
   *
   * @param publicId The public identifier.
   * @param systemId The system identifier (URI reference).
   * @param baseURI The base URI to be used for resolving a relative
   *          {@code systemId} to an absolute URI.
   * @see <a href="http://www.ietf.org/rfc/rfc2396.txt">IETF RFC 2396</a>
   */
  private CachedInputSource(final String publicId, final String systemId, final String baseURI) {
    setPublicId(publicId);
    setSystemId(systemId);
    setBaseURI(baseURI);
  }

  /**
   * Creates a new {@link CachedInputSource} with the {@code publicId},
   * {@code systemId}, and character stream from the specified
   * {@link InputSource}.
   *
   * @param inputSource The {@link InputSource} from which the {@code publicId},
   *          {@code systemId}, and character streams are to be copied.
   * @throws IllegalArgumentException If the specified {@link InputSource} is null.
   * @throws IllegalArgumentException If the specified {@link InputSource} does
   *           not have a byte stream or character stream.
   */
  public CachedInputSource(final InputSource inputSource) {
    this(inputSource.getPublicId(), inputSource.getSystemId(), null);
    setCharacterStream(getReader(inputSource));
  }

  /**
   * {@inheritDoc}
   *
   * @implNote This method has been modified use the specified
   *           {@link InputStream} as the source of a {@link CachedReader} via
   *           an {@link InputStreamReader} that is thereafter provided to
   *           {@link #setCharacterStream(Reader)}.
   */
  @Override
  public void setByteStream(final InputStream byteStream) {
    super.setCharacterStream(new CachedReader(new InputStreamReader(byteStream)));
  }

  @Override
  public String getStringData() {
    return null;
  }

  @Override
  public void setStringData(final String stringData) {
    throw new UnsupportedOperationException();
  }

  private String baseURI;

  @Override
  public String getBaseURI() {
    return baseURI;
  }

  @Override
  public void setBaseURI(final String baseURI) {
    this.baseURI = baseURI;
  }

  private boolean certifiedText;

  @Override
  public boolean getCertifiedText() {
    return certifiedText;
  }

  @Override
  public void setCertifiedText(final boolean certifiedText) {
    this.certifiedText = certifiedText;
  }

  /**
   * Close this {@link CachedInputSource}, relinquishing any underlying
   * resources.
   *
   * @throws IOException If an I/O error has occurred.
   */
  @Override
  public void close() throws IOException {
    ((CachedReader)getCharacterStream()).destroy();
  }
}