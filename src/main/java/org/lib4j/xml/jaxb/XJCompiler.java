/* Copyright (c) 2017 lib4j
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

package org.lib4j.xml.jaxb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

import org.lib4j.io.Streams;
import org.lib4j.net.URLs;
import org.lib4j.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.tools.xjc.XJCFacade;

public class XJCompiler {
  public static class Command {
    // Corresponding XJC parameter: mark-generated.
    // This feature causes all of the generated code to have @Generated annotation.
    private boolean addGeneratedAnnotation = false;

    // Corresponding XJC parameter: catalog.
    // Specify catalog files to resolve external entity references. Supports
    // TR9401, XCatalog, and OASIS XML Catalog format.
    private File catalog;

    // Removes all files from the output directory before running XJC.
    private boolean overwrite = true;

    // Corresponding XJC parameter: enableIntrospection.
    // Enable correct generation of Boolean getters/setters to enable Bean
    // Introspection APIs.
    private boolean enableIntrospection = true;

    // Defines the encoding used by XJC (for generating Java Source files) and
    // schemagen (for generating XSDs). The corresponding argument parameter for
    // XJC and SchemaGen is: encoding.
    //
    // The algorithm for finding the encoding to use is as follows (where the
    // first non-null value found is used for encoding):
    //
    // 1. If the configuration property is explicitly given within the plugin's
    // configuration, use that value.
    // 2. If the Maven property project.build.sourceEncoding is defined, use its
    // value.
    // 3. Otherwise use the value from the system property file.encoding.
    private String encoding;

    // Corresponding XJC parameter: extension.
    //
    // By default, the XJC binding compiler strictly enforces the rules outlined
    // in the Compatibility chapter of the JAXB Specification. Appendix E.2
    // defines a set of W3C XML Schema features that are not completely supported
    // by JAXB v1.0. In some cases, you may be allowed to use them in the
    // '-extension' mode enabled by this switch. In the default (strict) mode,
    // you are also limited to using only the binding customizations defined in
    // the specification.
    private boolean extension = false;

    // Corresponding XJC parameter: episode.
    //
    // Generate an episode file from this compilation, so that other schemas that
    // rely on this schema can be compiled later and rely on classes that are
    // generated from this compilation. The generated episode file is really just
    // a JAXB customization file (but with vendor extensions.)
    //
    // If this parameter is true, the episode file generated is called
    // META-INF/sun-jaxb.episode, and included in the artifact.
    private boolean generateEpisode = false;

    // Corresponding XJC parameter: nv.
    //
    // By default, the XJC binding compiler performs strict validation of the
    // source schema before processing it. Use this option to disable strict
    // schema validation. This does not mean that the binding compiler will not
    // perform any validation, it simply means that it will perform less-strict
    // validation.
    private boolean laxSchemaValidation = false;

    // Corresponding XJC parameter: no-header.
    //
    // Suppress the generation of a file header comment that includes some note
    // and timestamp. Using this makes the generated code more diff-friendly.
    private boolean noGeneratedHeaderComments = false;

    // Corresponding XJC parameter: npa.
    //
    // Suppress the generation of package level annotations into
    // package-info.java. Using this switch causes the generated code to
    // internalize those annotations into the other generated classes.
    private boolean noPackageLevelAnnotations = false;

    // Corresponding XJC parameter: d.
    //
    // The working directory where the generated Java source files are created.
    // Required: Yes
    private File destDir;

    // Corresponding XJC parameter: p.
    //
    // The package under which the source files will be generated. Quoting the
    // XJC documentation: 'Specifying a target package via this command-line
    // option overrides any binding customization for package name and the
    // default package name algorithm defined in the specification'.
    private String packageName;

    // Corresponding XJC parameter: quiet.
    // Suppress compiler output, such as progress information and warnings.
    private boolean quiet = false;

    // Parameter holding List of XSD paths to files and/or directories which
    // should be recursively searched for XSD files. Only files or directories
    // that actually exist will be included (in the case of files) or recursively
    // searched for XSD files to include (in the case of directories). Configure
    // using standard Maven structure for Lists:
    //
    // <configuration>
    // ...
    // <schemas>
    // <schema>some/explicit/relative/file.xsd</schema>
    // <schema>/another/absolute/path/to/a/specification.xsd</schema>
    // <schema>a/directory/holding/xsds</schema>
    // </schemas>
    // </configuration>
    private LinkedHashSet<URL> schemas;

    public static enum SourceType {
      DTD("dtd"), WSDL("wsdl"), XMLSCHEMA("xmlschema");

      private final String type;

      private SourceType(final String type) {
        this.type = type;
      }

      public static SourceType fromString(final String sourceType) {
        return valueOf(sourceType.toUpperCase());
      }
    }

    // Defines the content type of sources for the XJC. To simplify usage of the
    // JAXB2 maven plugin, all source files are assumed to have the same type of
    // content.
    //
    // This parameter replaces the previous multiple-choice boolean configuration
    // options for the jaxb2-maven-plugin (i.e. dtd, xmlschema, wsdl), and
    // corresponds to setting one of those flags as an XJC argument.
    private SourceType sourceType;

    public static enum TargetVersion {
      _2_0("2.0"), _2_1("2.1");

      private final String version;

      private TargetVersion(final String type) {
        this.version = type;
      }

      public static TargetVersion fromString(final String version) {
        return valueOf("_" + version.replace('.', '_'));
      }
    }

    // Corresponding XJC parameter: target.
    //
    // Permitted values: '2.0' and '2.1'. Avoid generating code that relies on
    // JAXB newer than the version given. This will allow the generated code to
    // run with JAXB 2.0 runtime (such as JavaSE 6.).
    private TargetVersion targetVersion;

    // Corresponding XJC parameter: verbose.
    //
    // Tells XJC to be extra verbose, such as printing informational messages or
    // displaying stack traces.
    // User property: xjc.verbose
    private boolean verbose = false;

    // Parameter holding List of XJB Files and/or directories which should be
    // recursively searched for XJB files. Only files or directories that
    // actually exist will be included (in the case of files) or recursively
    // searched for XJB files to include (in the case of directories). JAXB
    // binding files are used to configure parts of the Java source generation.
    // Supply the configuration using the standard Maven structure for
    // configuring plugin Lists:
    //
    // <configuration>
    // ...
    // <xjbSources>
    // <xjbSource>bindings/aBindingConfiguration.xjb</xjbSource>
    // <xjbSource>bindings/config/directory</xjbSource>
    // </xjbSources>
    // </configuration>
    private LinkedHashSet<URL> xjbs;

    public boolean isAddGeneratedAnnotation() {
      return this.addGeneratedAnnotation;
    }

    public void setAddGeneratedAnnotation(final boolean addGeneratedAnnotation) {
      this.addGeneratedAnnotation = addGeneratedAnnotation;
    }

    public File getCatalog() {
      return this.catalog;
    }

    public void setCatalog(final File catalog) {
      this.catalog = catalog;
    }

    public boolean isOverwrite() {
      return this.overwrite;
    }

    public void setOverwrite(final boolean overwrite) {
      this.overwrite = overwrite;
    }

    public boolean isEnableIntrospection() {
      return this.enableIntrospection;
    }

    public void setEnableIntrospection(final boolean enableIntrospection) {
      this.enableIntrospection = enableIntrospection;
    }

    public String getEncoding() {
      return this.encoding;
    }

    public void setEncoding(final String encoding) {
      this.encoding = encoding;
    }

    public boolean isExtension() {
      return this.extension;
    }

    public void setExtension(final boolean extension) {
      this.extension = extension;
    }

    public boolean isGenerateEpisode() {
      return this.generateEpisode;
    }

    public void setGenerateEpisode(final boolean generateEpisode) {
      this.generateEpisode = generateEpisode;
    }

    public boolean isLaxSchemaValidation() {
      return this.laxSchemaValidation;
    }

    public void setLaxSchemaValidation(final boolean laxSchemaValidation) {
      this.laxSchemaValidation = laxSchemaValidation;
    }

    public boolean isNoGeneratedHeaderComments() {
      return this.noGeneratedHeaderComments;
    }

    public void setNoGeneratedHeaderComments(final boolean noGeneratedHeaderComments) {
      this.noGeneratedHeaderComments = noGeneratedHeaderComments;
    }

    public boolean isNoPackageLevelAnnotations() {
      return this.noPackageLevelAnnotations;
    }

    public void setNoPackageLevelAnnotations(final boolean noPackageLevelAnnotations) {
      this.noPackageLevelAnnotations = noPackageLevelAnnotations;
    }

    public File getDestDir() {
      return this.destDir;
    }

    public void setDestDir(final File destDir) {
      this.destDir = destDir;
    }

    public String getPackageName() {
      return this.packageName;
    }

    public void setPackageName(final String packageName) {
      this.packageName = packageName;
    }

    public boolean isQuiet() {
      return this.quiet;
    }

    public void setQuiet(final boolean quiet) {
      this.quiet = quiet;
    }

    public LinkedHashSet<URL> getSchemas() {
      return this.schemas;
    }

    public void setSchemas(final LinkedHashSet<URL> schemas) {
      this.schemas = schemas;
    }

    public SourceType getSourceType() {
      return this.sourceType;
    }

    public void setSourceType(final SourceType sourceType) {
      this.sourceType = sourceType;
    }

    public TargetVersion getTargetVersion() {
      return this.targetVersion;
    }

    public void setTargetVersion(final TargetVersion targetVersion) {
      this.targetVersion = targetVersion;
    }

    public boolean isVerbose() {
      return this.verbose;
    }

    public void setVerbose(final boolean verbose) {
      this.verbose = verbose;
    }

    public LinkedHashSet<URL> getXJBs() {
      return this.xjbs;
    }

    public void setXJBs(final LinkedHashSet<URL> xjbs) {
      this.xjbs = xjbs;
    }
  }

  private static final Logger logger = LoggerFactory.getLogger(XJCompiler.class);

  public static void compile(final Command command, final File ... classpath) throws JAXBException {
    if (command.getSchemas() == null || command.getSchemas().size() == 0)
      return;

    final List<String> args = new ArrayList<String>();
    args.add("java");
    if (classpath.length > 0) {
      args.add("-cp");
      final StringBuilder cp = new StringBuilder();
      for (final File classpathFile : classpath)
        cp.append(File.pathSeparator).append(classpathFile.getAbsolutePath());

      args.add(cp.substring(1));
    }

    args.add(XJCFacade.class.getName());
    args.add("-Xannotate");
    if (command.isAddGeneratedAnnotation())
      args.add("-mark-generated");

    if (command.getCatalog() != null) {
      args.add("-catalog");
      args.add(command.getCatalog().getAbsolutePath());
    }

    if (command.isEnableIntrospection())
      args.add("-enableIntrospection");

    if (command.isExtension())
      args.add("-extension");

    if (command.isLaxSchemaValidation())
      args.add("-nv");

    if (command.isNoGeneratedHeaderComments())
      args.add("-no-header");

    if (command.isNoPackageLevelAnnotations())
      args.add("-npa");

    if (command.isQuiet())
      args.add("-quiet");

    if (command.getTargetVersion() != null) {
      args.add("-target");
      args.add(command.getTargetVersion().version);
    }

    if (command.isVerbose())
      args.add("-verbose");

    if (command.getSourceType() != null)
      args.add("-" + command.getSourceType().type);

    if (command.getEncoding() != null) {
      args.add("-encoding");
      args.add(command.getEncoding());
    }

    if (command.getPackageName() != null) {
      args.add("-p");
      args.add(command.getPackageName());
    }

    try {
      if (command.getDestDir() != null) {
        args.add("-d");
        args.add(command.getDestDir().getAbsolutePath());

        if (!command.getDestDir().exists() && !command.getDestDir().mkdirs()) {
          throw new JAXBException("Unable to create output directory " + command.getDestDir().getAbsolutePath());
        }
        // FIXME: This does not work because the files that are written are only known by xjc, so I cannot
        // FIXME: stop this generator from overwriting them if owerwrite=false
//        else if (command.isOverwrite()) {
//          for (final File file : command.getDestDir().listFiles())
//            Files.walk(file.toPath()).map(Path::toFile).filter(a -> a.getName().endsWith(".java")).sorted((o1, o2) -> o2.compareTo(o1)).forEach(File::delete);
//        }
      }

      for (final URL schema : command.getSchemas()) {
        if (URLs.isFile(schema)) {
          final File file = new File(schema.getFile());
          if (!file.exists())
            throw new FileNotFoundException(file.getAbsolutePath());

          args.add(file.getAbsolutePath());
        }
        else {
          try (final InputStream in = schema.openStream()) {
            final File file = File.createTempFile(URLs.getName(schema), "");
            args.add(file.getAbsolutePath());
            file.deleteOnExit();
            Files.write(file.toPath(), Streams.readBytes(in));
          }
        }

        args.add(schema.getFile());
      }

      if (command.getXJBs() != null) {
        for (final URL xjb : command.getXJBs()) {
          args.add("-b");
          if (URLs.isFile(xjb)) {
            args.add(xjb.getFile());
          }
          else {
            try (final InputStream in = xjb.openStream()) {
              final File file = File.createTempFile(URLs.getName(xjb), "");
              args.add(file.getAbsolutePath());
              file.deleteOnExit();
              Files.write(file.toPath(), Streams.readBytes(in));
            }
          }
        }
      }

      if (command.isGenerateEpisode()) {
        final File metaInfDir = new File(command.getDestDir(), "META-INF" + File.separator + "sun-jaxb.episode");
        if (!metaInfDir.getParentFile().mkdirs())
          throw new JAXBException("Unable to create output directory META-INF" + metaInfDir.getParentFile().getAbsolutePath());

        args.add("-episode");
        args.add(metaInfDir.getAbsolutePath());
      }

      final Process process = new ProcessBuilder(args).inheritIO().redirectErrorStream(true).start();
      try (final Scanner scanner = new Scanner(process.getInputStream())) {
        while (scanner.hasNextLine())
          log(scanner.nextLine().trim());

        final StringBuilder lastLine = new StringBuilder();
        while (scanner.hasNextByte())
          lastLine.append(scanner.nextByte());

        if (lastLine.length() > 0)
          log(lastLine.toString());
      }

      final int exitCode = process.waitFor();
      if (exitCode != 0)
        throw new JAXBException("xjc finished with code: " + exitCode + "\n" + Collections.toString(args, " "));
    }
    catch (final InterruptedException | IOException e) {
      throw new JAXBException(e.getMessage(), e);
    }
  }

  private static void log(final String line) {
    if (line.startsWith("[ERROR] "))
      logger.error(line.substring(8));
    else if (line.startsWith("[WARNING] "))
      logger.warn(line.substring(10));
    else
      logger.info(line);
  }
}