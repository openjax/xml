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
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.lib4j.xml;

import java.net.URI;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

import org.lib4j.net.Service;
import org.lib4j.net.Services;
import org.lib4j.net.URIs;
import org.lib4j.util.Diff;
import org.lib4j.util.JavaIdentifiers;

public final class NamespaceBinding {
  private static StringBuilder buildHost(final StringBuilder builder, final String host) {
    if (host == null)
      return builder;

    int end = host.length();
    int start = host.lastIndexOf('.');
    do {
      final String word = host.substring(start + 1, end);
      builder.append('.').append(JavaIdentifiers.toIdentifier(word, '_'));

      end = start;
      start = host.lastIndexOf('.', start - 1);
    }
    while (end > -1);
    return builder;
  }

  private static String formatFileName(final String name) {
    final int index = name.lastIndexOf('.');
    return index == -1 ? name : name.substring(0, index);
  }

  private static StringBuilder buildPath(final StringBuilder builder, final String path) {
    if (path == null)
      return builder;

    int start = path.charAt(0) == '/' ? 0 : -1;
    int end = path.indexOf('/', start == 0 ? 1 : 0);
    final int len = path.length();
    do {
      final String word = end == -1 ? (start == len - 1 ? null : path.substring(start + 1)) : start != end ? path.substring(start + 1, end) : null;
      if (word != null)
        builder.append('.').append(JavaIdentifiers.toIdentifier(end == -1 ? formatFileName(word) : word));

      start = end;
      end = path.indexOf('/', start + 1);
    }
    while (start > -1);
    return builder;
  }

  private static final Map<Character,String> defaultUrnSub = Collections.singletonMap(null, "_");

  private static StringBuilder buildUrn(final StringBuilder builder, final String urn) {
    int start = urn.charAt(0) == ':' ? 0 : -1;
    int end = urn.indexOf(':', start == 0 ? 1 : 0);
    final int len = urn.length();
    do {
      if (end != -1 || start != len - 1) {
        final String word = JavaIdentifiers.toIdentifier(end == -1 ? urn.substring(start + 1) : urn.substring(start + 1, end), '\0', defaultUrnSub);
        if (start > 0 || !"urn".equals(word))
          builder.append('_').append(word);
      }

      start = end;
      end = urn.indexOf(':', start + 1);
    }
    while (start > -1);
    return builder;
  }

  public static NamespaceBinding parseNamespace(final String uri) {
    return uri == null ? null : getPackageFromNamespace(URIs.makeURI(uri));
  }

  private static final Base64.Encoder base64Encoder = Base64.getEncoder().withoutPadding();

  private static String flipNamespaceURI(final String uri, final boolean replaceServicePort) {
    final int colon = uri.indexOf(":/");
    if (colon == -1)
      return uri;

    int start = colon;
    while (uri.charAt(++start) == '/');
    final int end = uri.indexOf('/', start + 1);
    final StringBuilder builder = new StringBuilder();
    buildHost(builder, uri.substring(start, end));
    builder.delete(0, 1);
    final Service service = replaceServicePort ? Services.getService(uri.substring(0, colon)) : null;
    if (service == null)
      builder.insert(0, uri.substring(0, start));
    else
      builder.insert(0, service.getPort());

    builder.append(uri.substring(end));
    return builder.toString();
  }

  private static String getDiff(final String packageName, final String namespaceURI) {
    final String packageNameForDiff = packageName.replace('.', '/');

    final Diff diff = new Diff(packageNameForDiff, namespaceURI);
    final byte[] bytes = diff.toBytes();
    return base64Encoder.encodeToString(bytes).replace('+', '$').replace('/', '_');
  }

  public static NamespaceBinding parsePackageName(final String packageName) {
    final int index = packageName.lastIndexOf('.');
    final String simpleClassName = packageName.substring(index + 2);
    final byte[] diffBytes = Base64.getDecoder().decode(simpleClassName.replace('$', '+').replace('_', '/'));
    final Diff diff = Diff.decode(diffBytes);
    final String source = packageName.substring(0, index).replace('.', '/');

    String decodedUri = diff.patch(source);
    if (!Character.isDigit(decodedUri.charAt(0)))
      return new NamespaceBinding(URIs.makeURI(decodedUri), packageName, simpleClassName);

    final StringBuilder port = new StringBuilder().append(decodedUri.charAt(0));
    int i = 1;
    while (Character.isDigit(decodedUri.charAt(i)))
      port.append(decodedUri.charAt(i++));

    final Service service = Services.getService(Integer.parseInt(port.toString()));
    return new NamespaceBinding(URIs.makeURI(service == null ? decodedUri : flipNamespaceURI(service.getName() + "://" + decodedUri.substring(i), false)), packageName, simpleClassName);
  }

  public static NamespaceBinding getPackageFromNamespace(final URI uri) {
    if (uri == null)
      return null;

    final StringBuilder builder = new StringBuilder(uri.toString().length());
    final String packageName;
    if (uri.getHost() == null) {
      buildUrn(builder, uri.toString());
      packageName = builder.length() == 0 ? "" : builder.charAt(0) == '_' ? builder.substring(1) : builder.toString();
    }
    else {
      buildHost(builder, uri.getHost());
      buildPath(builder, uri.getPath());
      packageName = builder.length() == 0 ? "" : builder.charAt(0) == '.' ? builder.substring(1) : builder.toString();
    }

    return new NamespaceBinding(uri, packageName);
  }

  private final URI namespaceUri;
  private final String packageName;
  private final String simpleClassName;
  private final String className;

  private NamespaceBinding(final URI namespaceUri, final String packageName, final String simpleClassName) {
    this.namespaceUri = namespaceUri;
    this.packageName = packageName;
    this.simpleClassName = simpleClassName;
    this.className = packageName + "." + simpleClassName;
  }

  private NamespaceBinding(final URI namespaceUri, final String packageName) {
    this(namespaceUri, packageName, "x" + getDiff(packageName, flipNamespaceURI(namespaceUri.toString(), true)));
  }

  public URI getNamespaceUri() {
    return this.namespaceUri;
  }

  public String getPackageName() {
    return this.packageName;
  }

  public String getSimpleClassName() {
    return this.simpleClassName;
  }

  public String getClassName() {
    return this.className;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof NamespaceBinding))
      return false;

    final NamespaceBinding that = (NamespaceBinding)obj;
    return namespaceUri.equals(that.namespaceUri) && packageName.equals(that.packageName) && simpleClassName.equals(that.simpleClassName);
  }

  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + namespaceUri.hashCode();
    hashCode = 31 * hashCode + packageName.hashCode();
    hashCode = 31 * hashCode + simpleClassName.hashCode();
    return hashCode;
  }

  @Override
  public String toString() {
    return "{\n  namespaceUri: \"" + namespaceUri + "\",\n  packageName: \"" + packageName + "\",\n  simpleClassName: \"" + simpleClassName + "\"\n}";
  }
}