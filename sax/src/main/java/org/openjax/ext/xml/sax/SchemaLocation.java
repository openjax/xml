/* Copyright (c) 2016 OpenJAX
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

package org.openjax.ext.xml.sax;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openjax.ext.util.Paths;

public class SchemaLocation {
  private final String namespace;
  private final Map<String,URL> directory;

  public SchemaLocation(final String namespace) {
    this.namespace = namespace;
    this.directory = new HashMap<>();
  }

  public SchemaLocation(final String namespace, final URL location) {
    this(namespace);
    this.directory.put(namespace, location);
    this.directory.put(Paths.canonicalize(location.toExternalForm()), location);
  }

  public String getNamespace() {
    return namespace;
  }

  public Map<String,URL> getDirectory() {
    return directory;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof SchemaLocation))
      return false;

    final SchemaLocation that = (SchemaLocation)obj;
    return (namespace != null ? namespace.equals(that.namespace) : that.namespace == null) && directory.equals(that.directory);
  }

  @Override
  public int hashCode() {
    return (namespace != null ? namespace.hashCode() : 0) + directory.hashCode();
  }

  @Override
  public String toString() {
    return "{" + namespace + ", " + directory + "}";
  }
}