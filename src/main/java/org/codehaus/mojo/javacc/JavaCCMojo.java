package org.codehaus.mojo.javacc;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Parses a JavaCC grammar file (<code>*.jj</code>) and transforms it to Java
 * source files. Detailed information about the JavaCC options can be found on
 * the <a href="https://javacc.dev.java.net/">JavaCC website</a>.
 *
 * @goal javacc
 * @phase generate-sources
 * @since 2.0
 * @author jruiz@exist.com
 * @author jesse <jesse.mcconnell@gmail.com>
 * @version $Id: JavaCCMojo.java 8156 2008-11-26 18:20:19Z bentmann $
 */
public class JavaCCMojo extends AbstractJavaCCMojo
{
  /**
   * The directory where the JavaCC grammar files (<code>*.jj</code>) are
   * located.
   *
   * @parameter property=sourceDirectory
   *            default-value="${basedir}/src/main/javacc"
   */
  private File sourceDirectory;

  /**
   * The directory where the parser files generated by JavaCC will be stored.
   * The directory will be registered as a compile source root of the project
   * such that the generated files will participate in later build phases like
   * compiling and packaging.
   *
   * @parameter property=outputDirectory
   *            default-value="${project.build.directory}/generated-sources/javacc"
   */
  private File outputDirectory;

  /**
   * The granularity in milliseconds of the last modification date for testing
   * whether a source needs recompilation.
   *
   * @parameter property=lastModGranularityMs default-value="0"
   */
  private int staleMillis;

  /**
   * A set of Ant-like inclusion patterns used to select files from the source
   * directory for processing. By default, the patterns <code>**&#47;*.jj</code>
   * and <code>**&#47;*.JJ</code> are used to select grammar files.
   *
   * @parameter
   */
  private String [] includes;

  /**
   * A set of Ant-like exclusion patterns used to prevent certain files from
   * being processed. By default, this set is empty such that no files are
   * excluded.
   *
   * @parameter
   */
  private String [] excludes;

  /**
   * {@inheritDoc}
   */
  @Override
  protected File getSourceDirectory ()
  {
    return this.sourceDirectory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String [] getIncludes ()
  {
    if (this.includes != null)
      return this.includes;
    return new String [] { "**/*.jj", "**/*.JJ" };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String [] getExcludes ()
  {
    return this.excludes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected File getOutputDirectory ()
  {
    return this.outputDirectory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected int getStaleMillis ()
  {
    return this.staleMillis;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected File [] getCompileSourceRoots ()
  {
    return new File [] { getOutputDirectory () };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void processGrammar (final GrammarInfo grammarInfo) throws MojoExecutionException, MojoFailureException
  {
    final File jjFile = grammarInfo.getGrammarFile ();
    final File jjDirectory = jjFile.getParentFile ();

    final File tempDirectory = getTempDirectory ();

    // setup output directory of parser file (*.java) generated by JavaCC
    final File parserDirectory = new File (tempDirectory, "parser");

    // generate parser files
    final JavaCC javacc = newJavaCC ();
    javacc.setInputFile (jjFile);
    javacc.setOutputDirectory (parserDirectory);
    javacc.run ();

    // copy parser files from JavaCC
    copyGrammarOutput (getOutputDirectory (),
                       grammarInfo.getParserPackage (),
                       parserDirectory,
                       grammarInfo.getParserName () + "*");

    // copy source files which are next to grammar unless the grammar resides in
    // an ordinary source root
    // (legacy support for custom sources)
    if (!isSourceRoot (grammarInfo.getSourceDirectory ()))
    {
      copyGrammarOutput (getOutputDirectory (), grammarInfo.getParserPackage (), jjDirectory, "*");
    }

    deleteTempDirectory (tempDirectory);
  }

}
