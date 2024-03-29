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

package org.openjax.xml;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.openjax.maven.mojo.FilterParameter;
import org.openjax.maven.mojo.FilterType;
import org.openjax.maven.mojo.PatternSetMojo;

@Mojo(name = "xml", requiresDependencyResolution = ResolutionScope.TEST)
public abstract class XmlMojo extends PatternSetMojo {
  static final Path CWD = new File("").getAbsoluteFile().toPath();
  private static final String delimeter = "://";

  @Parameter(defaultValue = "${httpProxy}", readonly = true)
  private String httpProxy;

  @FilterParameter(FilterType.RESOURCE)
  @Parameter(property = "resources")
  private List<String> resources;

  @FilterParameter(FilterType.FILE)
  @Parameter(property = "files")
  private List<File> files;

  protected final void setHttpProxy() throws MojoFailureException {
    if (httpProxy == null)
      return;

    final String scheme;
    if (httpProxy.startsWith("https" + delimeter))
      scheme = "https";
    else if (httpProxy.startsWith("http" + delimeter))
      scheme = "http";
    else
      throw new MojoFailureException("Invalid proxy: " + httpProxy + " no http or http scheme");

    final int portIndex = httpProxy.indexOf(':', scheme.length() + delimeter.length());
    final String port = portIndex != -1 ? httpProxy.substring(portIndex + 1) : "80";

    System.setProperty(scheme + ".proxyHost", httpProxy.substring(scheme.length() + delimeter.length(), portIndex));
    System.setProperty(scheme + ".proxyPort", port);
  }

  @Override
  public final void execute(final Configuration configuration) throws MojoExecutionException, MojoFailureException {
    setHttpProxy();
    final LinkedHashSet<URI> fileSets = new LinkedHashSet<>(configuration.getFileSets());
    if (resources != null && resources.size() > 0)
      for (final String resource : new LinkedHashSet<>(resources)) // [S]
        fileSets.add(URI.create(resource));

    if (files != null && files.size() > 0)
      for (final File file : new LinkedHashSet<>(files)) // [S]
        fileSets.add(file.toURI());

    if (fileSets.isEmpty()) {
      if (configuration.getFailOnNoOp())
        throw new MojoExecutionException("Empty input parameters (failOnNoOp=true)");

      getLog().info("Skipping for empty input parameters.");
      return;
    }

    execute(fileSets);
  }

  public abstract void execute(LinkedHashSet<URI> urls) throws MojoExecutionException, MojoFailureException;
}