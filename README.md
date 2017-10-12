# ph-javacc-maven-plugin
An updated version of the [javacc-maven-plugin](https://github.com/mojohaus/javacc-maven-plugin) using JavaCC 6.1.3.
The parameters etc. where not touched, so it should be a drop-in replacement for the old javacc-maven-plugin 2.6.
It requires Java 1.6 or higher and Apache Maven 2.x or higher. It is licensed under the Apache 2 license.

I'm using it e.g. in [ph-css](https://github.com/phax/ph-css) for CSS parsing.

# News and noteworthy

  * v3.0.0 - work in progress
    * Changed minimum requirement to JDK 8
    * Requires Maven 3 for execution
    * Using JavaCC 7.0.2
    * Updated to JTB 1.4.12
    * Removed deprecated classes
    * Requires https://github.com/javacc/javacc/pull/28 to be fixed
  * v2.8.2 - 2016-11-19
    * Fixing https://github.com/javacc/javacc/issues/2 locally
  * v2.8.1 - 2016-07-13
    * Fixed a problem with the code generation for "modern" Java template

# Maven usage
Example with 3 executions (two javacc and one jjtree-javacc):

```xml
    <build>
      ...
      <plugin>
        <groupId>com.helger.maven</groupId>
        <artifactId>ph-javacc-maven-plugin</artifactId>
        <version>2.8.2</version>
        <executions>
          <execution>
            <id>jjc1</id>
            <phase>generate-sources</phase>
            <goals>
              <goal>javacc</goal>
            </goals>
            <configuration>
              <jdkVersion>1.5</jdkVersion>
              <javadocFriendlyComments>true</javadocFriendlyComments>
              <packageName>org.javacc.parser</packageName>
              <sourceDirectory>src/main/java/org/javacc/parser</sourceDirectory>
              <outputDirectory>${project.build.directory}/generated-sources/javacc1</outputDirectory>
            </configuration>
          </execution>
          <execution>
            <id>jjt2</id>
            <phase>generate-sources</phase>
            <goals>
              <goal>javacc</goal>
            </goals>
            <configuration>
              <jdkVersion>1.5</jdkVersion>
              <javadocFriendlyComments>true</javadocFriendlyComments>
              <packageName>org.javacc.utils</packageName>
              <sourceDirectory>src/main/java/org/javacc/utils</sourceDirectory>
              <outputDirectory>${project.build.directory}/generated-sources/javacc2</outputDirectory>
            </configuration>
          </execution>
          <execution>
            <id>jjt1</id>
            <phase>generate-sources</phase>
            <goals>
              <goal>jjtree-javacc</goal>
            </goals>
            <configuration>
              <jdkVersion>1.5</jdkVersion>
              <javadocFriendlyComments>true</javadocFriendlyComments>
              <packageName>org.javacc.parser</packageName>
              <sourceDirectory>src/main/java/org/javacc/jjtree</sourceDirectory>
              <excludes>
                <exclude>**/parser/**</exclude>
              </excludes>
              <outputDirectory>${project.build.directory}/generated-sources/jjtree1</outputDirectory>
            </configuration>
          </execution>
        </executions>
      </plugin>
      ...
    </build>
```

Supported goals with the respective parameters are:
  * `javacc` - parses a JavaCC grammar file (`*.jj`) and transforms it to Java source files. Detailed information about the JavaCC options can be found on the [JavaCC website](https://javacc.org/)
    * `File` **sourceDirectory** - The directory where the JavaCC grammar files (`*.jj`) are located.
      Defaults to `${basedir}/src/main/javacc`.
    * `File` **outputDirectory** - The directory where the parser files generated by JavaCC will be stored. The directory will be registered as a compile source root of the project such that the generated files will participate in later build phases like compiling and packaging.
      Defaults to `${project.build.directory}/generated-sources/javacc`
    * `int` **staleMillis** - The granularity in milliseconds of the last modification date for testing whether a source needs recompilation.
      Defaults to `0`
    * `String[]` **includes** - A set of Ant-like inclusion patterns used to select files from the source directory for processing. By default, the patterns `**/*.jj` and `**/*.JJ` are used to select grammar files.
    * `String[]` **excludes** - A set of Ant-like exclusion patterns used to prevent certain files from being processed. By default, this set is empty such that no files are excluded.
  * `jjdoc` - [JJDoc](https://javacc.org/doc/JJDoc.html) takes a JavaCC parser specification and produces documentation for the BNF grammar. This mojo will search the source directory for all `*.jj` files and run JJDoc once for each file it finds. Each of these output files, along with an `index.html` file will be placed in the site directory (`target/site/jjdoc`), and a link will be created in the "Project Reports" menu of the generated site.
  * `jjtree-javacc` - preprocesses decorated grammar files (`*.jjt`) with JJTree and passes the output to JavaCC in order to finally generate a parser with parse tree actions.
  * `jjtree` -  parses a JJTree grammar file (`*.jjt`) and transforms it to Java source files and a JavaCC grammar file. Please see the [JJTree Reference Documentation](https://javacc.org/doc/JJTree.html) for more information.
  * `jtb-javacc` - preprocesses ordinary grammar files (`*.jtb`) with JTB and passes the output to JavaCC in order to finally generate a parser with parse tree actions.
  * `jtb` - parses a JTB file and transforms it into source files for an AST and a JavaCC grammar file which automatically builds the AST.

# Integration tests

  * To run the integration tests use the following commandline `mvn clean install -Dit=true`
  * If you have a proxy server in place, edit `src/it/settings.xml` and add it there

# External links

  * Java CC sources are finally on GitHub at https://github.com/javacc/javacc

---

My personal [Coding Styleguide](https://github.com/phax/meta/blob/master/CodeingStyleguide.md) |
On Twitter: <a href="https://twitter.com/philiphelger">@philiphelger</a>
