/* Copyright (c) 2016 lib4j
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

package org.safris.commons.xml.sax;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lib4j.net.CachedURL;
import org.lib4j.net.URLs;
import org.safris.commons.lang.Paths;

public class SchemaLocation {
  private final String namespace;
  private final Map<String,CachedURL> location;

  public SchemaLocation(final String namespace) {
    this.namespace = namespace;
    this.location = new HashMap<String,CachedURL>();
  }

  public SchemaLocation(final String namespace, final CachedURL location) throws IOException {
    this(namespace);
    if (location == null)
      throw new NullPointerException("location == null");

    this.location.put(namespace, location);
    this.location.put(Paths.canonicalize(URLs.toExternalForm(location)), location);
  }

  public String getNamespace() {
    return namespace;
  }

  public Map<String,CachedURL> getLocation() {
    return location;
  }
}