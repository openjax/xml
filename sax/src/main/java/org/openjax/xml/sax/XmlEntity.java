/* Copyright (c) 2019 OpenJAX
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

import static org.libj.lang.Assertions.*;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

/**
 * The {@link XmlEntity} represents an XML entity, which associates a {@link CachedInputSource} to an {@link URL}.
 */
public class XmlEntity implements AutoCloseable, Serializable {
  protected final URL location;
  protected final CachedInputSource inputSource;

  /**
   * Creates a new {@link XmlEntity} with the specified {@link URL} and {@link CachedInputSource}.
   *
   * @param location The {@link URL}.
   * @param inputSource The {@link CachedInputSource}.
   * @throws IllegalArgumentException If the specified {@link URL} or {@link CachedInputSource} is null.
   */
  public XmlEntity(final URL location, final CachedInputSource inputSource) {
    this.location = assertNotNull(location);
    this.inputSource = assertNotNull(inputSource);
  }

  /**
   * Returns the {@link URL}.
   *
   * @return The {@link URL}.
   */
  public final URL getLocation() {
    return location;
  }

  /**
   * Returns the {@link CachedInputSource}.
   *
   * @return The {@link CachedInputSource}.
   */
  public CachedInputSource getInputSource() {
    return this.inputSource;
  }

  /**
   * Closes this {@link XmlEntity}. This method calls {@link CachedInputSource#close() close()} on the underlying
   * {@link CachedInputSource}.
   *
   * @throws IOException If an I/O error has occurred.
   */
  @Override
  public void close() throws IOException {
    this.inputSource.close();
  }
}