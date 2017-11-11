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
import org.lib4j.util.BiMap;
import org.lib4j.util.Diff;
import org.lib4j.util.HashBiMap;
import org.lib4j.util.JavaIdentifiers;

/**
 * This class models the binding between an XML namespace URI and a Java
 * package name. This model asserts that a unique package exists for every
 * different XML namespace.
 * @see NamespaceBinding#parseNamespace(URI)
 * @see NamespaceBinding#parseClassName(String)
 */
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
    builder.insert(0, uri.substring(0, start));
    builder.append(uri.substring(end));
    if (replaceServicePort)
      addPrefixForDigits(builder);
    else
      removePrefixForDigits(builder);

    return builder.toString();
  }

  private static final BiMap<String,Character> schemeToPrefix = new HashBiMap<String,Character>();
  private static final char defaultPrefix = 'x';

  static {
    schemeToPrefix.put("http", 'h');
    schemeToPrefix.put("https", 's');
    schemeToPrefix.put("file", 'f');
    schemeToPrefix.put("data", 'd');
    schemeToPrefix.put("urn", 'u');
  }

  private static String getDiff(final String packageName, final String namespaceURI) {
    int start = namespaceURI.indexOf(":");
    char prefix;
    final StringBuilder namespaceForDiff = new StringBuilder();
    if (start > -1) {
      if (start + 1 < namespaceURI.length() && namespaceURI.charAt(start + 1) == '/') {
        final String scheme = namespaceURI.substring(0, start);
        while (namespaceURI.charAt(++start) == '/');
        prefix = schemeToPrefix.getOrDefault(scheme, defaultPrefix);
        if (prefix == defaultPrefix) {
          final Service service = Services.getService(scheme);
          if (service != null)
            namespaceForDiff.append(service.getPort());
          else
            namespaceForDiff.append(namespaceURI.substring(0, start));
        }
      }
      else if ("urn".equals(namespaceURI.substring(0, start))) {
        ++start;
        prefix = 'n';
      }
      else {
        prefix = defaultPrefix;
      }
    }
    else {
      prefix = defaultPrefix;
    }

    if (namespaceURI.endsWith(".xsd")) {
      prefix = Character.toUpperCase(prefix);
      namespaceForDiff.append(start > -1 ? namespaceURI.substring(start, namespaceURI.length() - 4) : namespaceURI.substring(0, namespaceURI.length() - 4));
    }
    else {
      namespaceForDiff.append(start > -1 ? namespaceURI.substring(start) : namespaceURI);
    }

    final String packageNameForDiff = packageName.replace('.', '/');
    final Diff diff = new Diff(packageNameForDiff, namespaceForDiff.toString());
    final byte[] bytes = diff.toBytes();
    return prefix + base64Encoder.encodeToString(bytes).replace('+', '$').replace('/', '_');
  }

  private static StringBuilder addPrefixForDigits(final StringBuilder builder) {
    boolean match = false;
    for (int i = builder.length() - 1; i >= 0; --i) {
      final char ch = builder.charAt(i);
      if (match && ch == '/')
        builder.insert(i + 1, '_');

      match = Character.isDigit(ch);
    }

    return builder;
  }

  private static StringBuilder removePrefixForDigits(final StringBuilder builder) {
    boolean match = false;
    for (int i = builder.length() - 1; i >= 0; --i) {
      final char ch = builder.charAt(i);
      if (match && (ch == '/' || ch == '.'))
        builder.delete(i + 1, i + 2);

      match = ch == '_';
    }

    return builder;
  }

  /**
   * Create a <code>NamespaceBinding</code> from a fully qualified class name.
   * This method is intended for class names created by
   * <code>NamespaceBinding.getClassName()<code>, which contain a simple class
   * name as a Base64-encoded diff between the package name and the original
   * namespace URI. If such a class name is inputted to this method, the
   * resulting <code>NamespaceBinding</code> is guaranteed to the unique
   * binding between that class name and the namespace URI from which it
   * originated.
   * @param className The fully qualified class name previously encoded by
   *        <code>NamespaceBinding.getClassName()<code>.
   * @return A guaranteed unique <code>NamespaceBinding</code> for the package
   *         name.
   * @see NamespaceBinding#getClassName()
   * @see NamespaceBinding#parseNamespace(URI)
   */
  public static NamespaceBinding parseClassName(final String className) {
    final int index = className.lastIndexOf('.');
    char prefix = className.charAt(index + 1);
    String suffix = "";
    if (Character.isUpperCase(prefix)) {
      prefix = Character.toLowerCase(prefix);
      suffix = ".xsd";
    }

    final String scheme = prefix == defaultPrefix ? null : schemeToPrefix.inverse().get(prefix);
    final String simpleClassName = className.substring(index + 2);
    final byte[] diffBytes = Base64.getDecoder().decode(simpleClassName.replace('$', '+').replace('_', '/'));
    final Diff diff = Diff.decode(diffBytes);
    final String source = className.substring(0, index).replace('.', '/');

    final String decodedUri = diff.patch(source);
    if (scheme != null)
      return new NamespaceBinding(URIs.makeURI(flipNamespaceURI(scheme + "://" + decodedUri, false) + suffix), className, simpleClassName);

    if (!Character.isDigit(decodedUri.charAt(0)))
      return new NamespaceBinding(URIs.makeURI(flipNamespaceURI((prefix == 'n' ? "urn:" : "") + decodedUri, false) + suffix), className, simpleClassName);

    final StringBuilder port = new StringBuilder().append(decodedUri.charAt(0));
    int i = 1;
    while (Character.isDigit(decodedUri.charAt(i)))
      port.append(decodedUri.charAt(i++));

    final Service service = Services.getService(Integer.parseInt(port.toString()));
    return new NamespaceBinding(URIs.makeURI(service == null ? decodedUri : flipNamespaceURI(service.getName() + "://" + decodedUri.substring(i), false) + suffix), className, simpleClassName);
  }

  /**
   * Create a <code>NamespaceBinding</code> from a <code>URI</code>. This
   * method guarantees that a unique package name will be created for each
   * unique <code>URI</code>. Examples of namespaces that would otherwise
   * seem to result in the same package name are:
   * <p>
   * <code>http://www.foo.com/bar.xsd</code>
   * <p>
   * <code>http://www.foo.com/bar</code>
   * <p>
   * <code>https://www.foo.com/bar.xsd</code>
   * <p>
   * <code>https://www.foo.com/bar</code>
   * <p>
   * <code>file://www.foo.com/bar.xsd</code>
   * <p>
   * The resulting package name for each of these namespaces is:
   * <p>
   * <code>com.foo.www.bar</code>
   * <p>
   * The simple class name is based on a Base64 string representation of a diff
   * between the original namespace URI and the resulting package name. The
   * package name carries enough information within itself to be able to
   * translate directly back to the unique namespace URI from which it was
   * generated.
   * @param uri The namespace URI.
   * @return A guaranteed unique <code>NamespaceBinding</code> to the uri.
   */
  public static NamespaceBinding parseNamespace(final URI uri) {
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

  /**
   * Create a <code>NamespaceBinding</code> from a <code>String</code> uri.
   * This method guarantees that a unique package name will be created for each
   * unique <code>URI</code>. Examples of namespaces that would otherwise
   * seem to result in the same package name are:
   * <p>
   * <code>http://www.foo.com/bar.xsd</code>
   * <p>
   * <code>http://www.foo.com/bar</code>
   * <p>
   * <code>https://www.foo.com/bar.xsd</code>
   * <p>
   * <code>https://www.foo.com/bar</code>
   * <p>
   * <code>file://www.foo.com/bar.xsd</code>
   * <p>
   * The resulting package name for each of these namespaces is:
   * <p>
   * <code>com.foo.www.bar</code>
   * <p>
   * The simple class name is based on a Base64 string representation of a diff
   * between the original namespace URI and the resulting package name. The
   * package name carries enough information within itself to be able to
   * translate directly back to the unique namespace URI from which it was
   * generated.
   * @param uri The namespace URI.
   * @return A guaranteed unique <code>NamespaceBinding</code> to the uri.
   * @see NamespaceBinding#parseNamespace(URI)
   */
  public static NamespaceBinding parseNamespace(final String uri) {
    return uri == null ? null : parseNamespace(URIs.makeURI(uri));
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
    this(namespaceUri, packageName, getDiff(packageName, flipNamespaceURI(namespaceUri.toString(), true)));
  }

  /**
   * @return The namespace URI for this binding.
   */
  public URI getNamespaceUri() {
    return this.namespaceUri;
  }

  /**
   * @return The package name for this binding.
   */
  public String getPackageName() {
    return this.packageName;
  }

  /**
   * @return The simple class name (i.e. class name without the package name)
   * for this binding.
   */
  public String getSimpleClassName() {
    return this.simpleClassName;
  }

  /**
   * @return The fully qualified class name for this binding.
   */
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