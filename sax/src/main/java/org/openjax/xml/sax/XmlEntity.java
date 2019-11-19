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

import java.io.Serializable;
import java.net.URL;

import org.xml.sax.InputSource;

/**
 * The {@link XmlEntity} represents an XML entity
 *
 */
public class XmlEntity implements Serializable {
  private static final long serialVersionUID = 259999207686667185L;

  protected final URL location;
  protected final InputSource inputSource;

  public XmlEntity(final URL location, final InputSource inputSource) {
    this.location = location;
    this.inputSource = inputSource;
  }

  public final URL getLocation() {
    return location;
  }

  public InputSource getInputSource() {
    return this.inputSource;
  }
}