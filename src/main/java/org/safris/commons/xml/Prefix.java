/* Copyright (c) 2006 lib4j
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
// * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.safris.commons.xml;

import java.util.HashMap;
import java.util.Map;

public final class Prefix {
  private static final Map<String,Prefix> instances = new HashMap<String,Prefix>();

  public static final Prefix EMPTY = getInstance("");

  public static Prefix getInstance(final String prefix) {
    Prefix value = instances.get(prefix);
    if (value == null)
      instances.put(prefix, value = new Prefix(prefix));

    return value;
  }

  private final String prefix;
//  private final String title;
//  private final String lower;

  private Prefix(final String prefix) {
    this.prefix = prefix;
    /*if (prefix.equals(prefix.toUpperCase())) {
      title = prefix;
      lower = prefix.toLowerCase();
    }
    else if (64 < prefix.charAt(0) && prefix.charAt(0) < 91) {
      title = prefix;
      lower = title.substring(0, 1).toLowerCase() + title.substring(1);
    }
    else {
      title = prefix.substring(0, 1).toUpperCase() + prefix.substring(1);
      lower = prefix;
    }*/
  }

  @Override
  public boolean equals(final Object obj) {
    if (!(obj instanceof Prefix))
      return false;

    return prefix.equals(((Prefix)obj).prefix);
  }

  @Override
  public int hashCode() {
    return prefix.hashCode();
  }

  @Override
  public String toString() {
    return prefix;
  }

//  public String toStringTitleCase() {
//      return title;
//  }
//
//  public String toStringLowerCase() {
//      return lower;
//  }
}