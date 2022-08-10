/* Copyright (c) 2018 OpenJAX
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
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The {@link XmlCatalog} class represents a catalog for XML entities as defined by
 * <a href= "https://www.oasis-open.org/committees/download.php/14809/xml-catalogs.html"> XML Catalogs, OASIS Standard V1.1, 7
 * October 2005</a>.
 * <p>
 * The {@link XmlCatalog} contains identifier-to-location mappings, and can be presented in
 * <a href="https://www.oasis-open.org/specs/tr9401.html">TR9401 format</a>.
 */
public class XmlCatalog extends XmlEntity {
  private LinkedHashMap<String,XmlEntity> uriToEntity;

  /**
   * Creates a new {@link XmlCatalog} with the specified {@link URL} and {@link CachedInputSource}.
   *
   * @param location The {@link URL}.
   * @param inputSource The {@link CachedInputSource}.
   * @throws IllegalArgumentException If the specified {@link URL} or {@link CachedInputSource} is null.
   */
  public XmlCatalog(final URL location, final CachedInputSource inputSource) {
    super(location, assertNotNull(inputSource));
  }

  private Map<String,XmlEntity> uriToSystemId() {
    return uriToEntity == null ? uriToEntity = new LinkedHashMap<>() : uriToEntity;
  }

  /**
   * Associates the specified {@link XmlEntity} to the namespace URI.
   *
   * @param uri The URI key.
   * @param entity The schema location value.
   * @return The previous value associated with key, or {@code null} if there was no mapping for key.
   * @throws IllegalArgumentException If the specified {@link XmlEntity} is null.
   */
  public XmlEntity putEntity(final String uri, final XmlEntity entity) {
    return uriToSystemId().put(uri, assertNotNull(entity));
  }

  /**
   * Returns the schema location associated with the specified namespace URI.
   *
   * @param uri The URI.
   * @return The schema location associated with the specified URI.
   * @throws IOException If an I/O exception has occurred.
   */
  public XmlEntity getEntity(final String uri) throws IOException {
    if (uri.equals(location.toString()))
      return this;

    if (uriToEntity == null)
      return null;

    XmlEntity entity = uriToEntity.get(uri);
    if (entity != null)
      return entity;

    for (final XmlEntity catalog : uriToEntity.values()) { // [C]
      if (catalog instanceof XmlCatalog && catalog != this) {
        entity = ((XmlCatalog)catalog).getEntity(uri);
        if (entity != null)
          return entity;
      }
    }

    return null;
  }

  /**
   * Returns {@code true} if this map contains no entities.
   *
   * @return {@code true} if this map contains no entities.
   */
  public boolean isEmpty() {
    return uriToEntity == null || uriToEntity.isEmpty();
  }

  /**
   * Returns a string representation of this {@link XmlCatalog} in <a href="https://www.oasis-open.org/specs/tr9401.html">TR9401
   * format</a>.
   *
   * @return A string representation of this {@link XmlCatalog} in <a href="https://www.oasis-open.org/specs/tr9401.html">TR9401
   *         format</a>.
   */
  public String toTR9401() {
    final StringBuilder builder = new StringBuilder();
    toTR9401(new HashSet<>(), builder);
    return builder.toString();
  }

  private void toTR9401(final Set<String> uris, final StringBuilder builder) {
    int i = 0;
    if (uriToEntity == null)
      return;

    for (final Map.Entry<String,XmlEntity> entry : uriToEntity.entrySet()) { // [S]
      if (uris.contains(entry.getKey()))
        continue;

      uris.add(entry.getKey());
      if (i++ > 0)
        builder.append('\n');

      final XmlEntity entity = entry.getValue();
      final String line = "\"" + entry.getKey() + "\" \"" + entity.getLocation() + "\"";
      // FIXME: What's going on here?
      builder.append(entry.getKey().equals(entry.getKey()) ? "PUBLIC " : "SYSTEM ").append(line);
      builder.append("\nREWRITE_SYSTEM ").append(line);
      if (entity instanceof XmlCatalog && entity != this) {
        ((XmlCatalog)entity).toTR9401(uris, builder);
      }
    }
  }

  /**
   * Closes this {@link XmlCatalog}. This method calls {@link XmlEntity#close()} on each {@link XmlEntity} instance referenced in
   * this {@link XmlCatalog}.
   *
   * @throws IOException If an I/O error has occurred.
   */
  @Override
  public void close() throws IOException {
    super.close();
    if (uriToEntity != null)
      for (final XmlEntity entity : uriToEntity.values()) // [C]
        if (entity != this)
          entity.close();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof XmlCatalog) || !super.equals(obj))
      return false;

    return Objects.equals(uriToEntity, ((XmlCatalog)obj).uriToEntity);
  }

  @Override
  public int hashCode() {
    int hashCode = super.hashCode();
    if (uriToEntity != null)
      hashCode = 31 * hashCode + uriToEntity.hashCode();

    return hashCode;
  }
}