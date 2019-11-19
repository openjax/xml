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

import java.net.URL;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.xml.sax.InputSource;

/**
 * The {@link XmlCatalog} class represents a catalog for XML entities as defined
 * by <a href=
 * "https://www.oasis-open.org/committees/download.php/14809/xml-catalogs.html">
 * XML Catalogs, OASIS Standard V1.1, 7 October 2005</a>.
 * <p>
 * The {@link XmlCatalog} contains identifier-to-location mappings, and can be
 * presented in <a href="https://www.oasis-open.org/specs/tr9401.html">TR9401
 * format</a>.
 */
public abstract class XmlCatalog extends XmlEntity {
  private static final long serialVersionUID = -4854713465553698524L;

  public XmlCatalog(final URL location, final InputSource inputSource) {
    super(location, inputSource);
  }

  /**
   * Associates the specified schema location to the namespace URI.
   *
   * @param uri The URI key.
   * @param entity The schema location value.
   */
  public abstract void putEntity(String uri, XmlEntity entity);

  public abstract URL matchURI(String uri);

  /**
   * Returns {@code true} if this map contains no entities.
   *
   * @return {@code true} if this map contains no entities.
   */
  public abstract boolean isEmpty();

  /**
   * Returns a string representation of this {@link XmlCatalog} in
   * <a href="https://www.oasis-open.org/specs/tr9401.html">TR9401 format</a>.
   *
   * @return A string representation of this {@link XmlCatalog} in
   *         <a href="https://www.oasis-open.org/specs/tr9401.html">TR9401
   *         format</a>.
   */
  public abstract String toTR9401();

  public static class Tree extends XmlCatalog {
    private static final long serialVersionUID = -4854713465553698524L;

    private LinkedHashMap<String,XmlEntity> uriToEntity;

    public Tree(final URL location, final InputSource inputSource) {
      super(location, inputSource);
    }

    private Map<String,XmlEntity> uriToSystemId() {
      return uriToEntity == null ? uriToEntity = new LinkedHashMap<>() : uriToEntity;
    }

    @Override
    public void putEntity(final String uri, final XmlEntity entity) {
      uriToSystemId().put(uri, entity);
    }

    @Override
    public URL matchURI(final String uri) {
      final XmlEntity entity = getEntity(uri);
      return entity == null ? null : entity.getLocation();
    }

    /**
     * Returns the schema location associated with the specified namespace URI.
     *
     * @param uri The URI.
     * @return The schema location associated with the specified URI.
     */
    private XmlEntity getEntity(final String uri) {
      if (uriToEntity == null)
        return null;

      XmlEntity entity = uriToEntity.get(uri);
      if (entity != null)
        return entity;

      for (final XmlEntity catalog : uriToEntity.values()) {
        if (catalog instanceof Tree) {
          entity = ((Tree)catalog).getEntity(uri);
          if (entity != null)
            return entity;
        }
      }

      return null;
    }

    @Override
    public boolean isEmpty() {
      return uriToEntity == null || uriToEntity.isEmpty();
    }

    @Override
    public String toTR9401() {
      final StringBuilder builder = new StringBuilder();
      toTR9401(new HashSet<>(), builder);
      return builder.toString();
    }

    private void toTR9401(final Set<String> uris, final StringBuilder builder) {
      int i = 0;
      if (uriToEntity != null) {
        for (final Map.Entry<String,XmlEntity> entry : uriToEntity.entrySet()) {
          if (uris.contains(entry.getKey()))
            continue;

          uris.add(entry.getKey());
          if (i++ > 0)
            builder.append('\n');

          final XmlEntity systemId = entry.getValue();
          final String line = "\"" + entry.getKey() + "\" \"" + systemId.getLocation() + "\"";
          builder.append(entry.getKey().equals(entry.getKey()) ? "PUBLIC " : "SYSTEM ").append(line);
          builder.append("\nREWRITE_SYSTEM ").append(line);
          if (systemId instanceof XmlCatalog) {
            ((Tree)systemId).toTR9401(uris, builder);
          }
        }
      }
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;

      if (!(obj instanceof Tree))
        return false;

      final Tree that = (Tree)obj;
      return uriToEntity == null ? that.uriToEntity == null : uriToEntity.equals(that.uriToEntity);
    }

    @Override
    public int hashCode() {
      return uriToEntity == null ? 733 : uriToEntity.hashCode();
    }
  }
}