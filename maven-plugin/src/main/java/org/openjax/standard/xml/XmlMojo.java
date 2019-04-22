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

package org.openjax.standard.xml;

import java.net.URL;
import java.util.LinkedHashSet;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.openjax.standard.maven.mojo.FileSetMojo;

@Mojo(name="xml", requiresDependencyResolution=ResolutionScope.TEST)
public abstract class XmlMojo extends FileSetMojo {
  private static final String delimeter = "://";

  @Parameter(defaultValue="${httpProxy}", readonly=true)
  private String httpProxy;

  @Parameter(defaultValue="${project.build.directory}", required=true, readonly=true)
  protected String directory = null;

  @Parameter(defaultValue="${settings.offline}", required=true, readonly=true)
  protected boolean offline;

  protected final void setHttpProxy() throws MojoFailureException {
    if (offline || httpProxy == null)
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
  public final void execute(final LinkedHashSet<URL> urls) throws MojoExecutionException, MojoFailureException {
    setHttpProxy();
    executeMojo(urls);
  }

  public abstract void executeMojo(LinkedHashSet<URL> urls) throws MojoExecutionException, MojoFailureException;
}