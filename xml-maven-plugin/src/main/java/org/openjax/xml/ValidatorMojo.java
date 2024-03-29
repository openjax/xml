/* Copyright (c) 2008 OpenJAX
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashSet;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.libj.net.URIs;
import org.libj.net.URLConnections;
import org.libj.util.Dates;
import org.openjax.xml.sax.CachedInputSource;
import org.openjax.xml.sax.Validator;
import org.xml.sax.SAXException;

@Mojo(name = "validate", defaultPhase = LifecyclePhase.COMPILE)
@Execute(goal = "validate")
public class ValidatorMojo extends XmlMojo {
  @Override
  public void execute(final LinkedHashSet<URI> uris) throws MojoExecutionException, MojoFailureException {
    final File recordDir = new File(getProject().getBuild().getDirectory(), "validator");
    recordDir.mkdirs();

    try {
      if (uris.size() > 0) {
        for (final URI uri : uris) { // [S]
          final File recordFile = new File(recordDir, URIs.getName(uri));
          final String filePath = URIs.isLocalFile(uri) ? CWD.relativize(new File(uri).getAbsoluteFile().toPath()).toString() : uri.toString();
          long lastModifiedSource = -1;
          final long lastModifiedTarget;
          final URL url = uri.toURL();
          final URLConnection connection = URLConnections.checkFollowRedirect(url.openConnection());
          if (recordFile.exists() && (lastModifiedSource = connection.getLastModified()) <= (lastModifiedTarget = recordFile.lastModified()) && lastModifiedTarget < lastModifiedSource + Dates.MILLISECONDS_IN_DAY) {
            getLog().info("Pre-validated: " + filePath);
            continue;
          }

          try {
            getLog().info("   Validating: " + filePath);
            Validator.validate(url, new CachedInputSource(null, url.toString(), null, connection));
          }
          catch (final FileNotFoundException | SAXException e) {
            if (!getOffline() || !(e instanceof SAXException) || !Validator.isRemoteAccessException((SAXException)e)) {
              final String message = e instanceof FileNotFoundException ? e.getClass().getSimpleName() + e.getMessage() : e.getMessage();
              final StringBuilder b = new StringBuilder("\nURL: ").append(uri.toString());
              b.append("\nReason: ").append(message).append('\n');
              for (final Throwable t : e.getSuppressed()) // [A]
                b.append("        ").append(t.getMessage()).append('\n');

              final MojoFailureException exception = new MojoFailureException("Failed to validate xml.", "", b.toString());
              exception.initCause(e);
              throw exception;
            }
          }

          if (!recordFile.createNewFile()) {
            recordFile.setLastModified(lastModifiedSource > 0 ? lastModifiedSource : System.currentTimeMillis());
          }
        }
      }
    }
    catch (final IOException e) {
      throw new MojoExecutionException(e.getClass().getSimpleName() + ": " + e.getMessage(), e);
    }
  }
}