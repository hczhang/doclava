# Getting Started #

## Building Doclava ##
The Doclava source comes bundled with an [ant](http://ant.apache.org/) script to build the doclet. The "jar" task will build a jar containing Doclava and all necessary dependencies.

## Using Doclava with Javadoc ##
Doclava is implented as a Doclet, and can be used anywhere [Javadoc](http://www.oracle.com/technetwork/java/javase/documentation/index-jsp-135444.html) is used.

The command line arguments to pass to Javadoc to use Doclava are:
```
  -doclet com.google.doclava.Doclava
  -docletpath ${jar.file}
```

Be sure to see the list of Doclava's [command line arguments](CommandLineArguments.md).

## Using Doclava with Ant ##
Ant's built-in [javadoc task](http://ant.apache.org/manual/Tasks/javadoc.html) can be configured to use third-party doclets. The following is an example of how Doclava builds its own documentation:

```
<project>
  ...
  <target name="doclava" depends="jar">
    <javadoc packagenames="com.google.*"
           destdir="build/docs"
           sourcepath="src"
           docletpath="${jar.file}"
           bootclasspath="${javahome}/jre/lib/rt.jar"
           >
      <doclet name="com.google.doclava.Doclava">
        <param name="-stubs" value="build/stubs" />
        <param name="-hdf"/> <param name="project.name"/> <param name="Doclava"/>
        <!-- versioning -->
        <param name="-since"/> <param name="doclava/previous.xml"/> <param name="v1" />
        <param name="-apiversion" value="v2"/>
        <!-- federation -->
        <param name="-federate" /><param name="JDK"/>
        <param name="http://download.oracle.com/javase/6/docs/api/index.html?"/>
        <param name="-federationxml"/><param name="JDK"/>
        <param name="http://doclava.googlecode.com/svn/static/api/openjdk-6.xml"/>
      </doclet>
    </javadoc>
  </target>
</project>
```

Note that some [command line arguments](CommandLineArguments.md) take more than one value, and so cannot be specified using `<param name="-foo" value="bar"/>` elements. Instead, list each value in its own element, as in `<param name="-foo"/> <param name="val1"/> <param name="val2"/>`.


## Using Doclava with Maven ##
See [Maven's guide for using alternate doclets.](http://maven.apache.org/plugins/maven-javadoc-plugin/examples/alternate-doclet.html) Use the doclet "com.google.doclava.Doclava", and you can refer to Doclava via our deployed artifact in Maven's central repository. An example of using Doclava with Maven:
```
<project>
  ...
  <build>
    <plugins>
      ...
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <version>2.7</version>
        <configuration>
          <docletArtifact>
            <groupId>com.google.doclava</groupId>
            <artifactId>doclava</artifactId>
            <version>1.0.5</version>
          </docletArtifact>
          <doclet>com.google.doclava.Doclava</doclet>
          <!--
            | bootclasspath required by Sun's JVM 
          -->
          <bootclasspath>${sun.boot.class.path}</bootclasspath>
          <additionalparam>
             -quiet
             -federate JDK http://download.oracle.com/javase/6/docs/api/index.html?
             -federationxml JDK http://doclava.googlecode.com/svn/static/api/openjdk-6.xml
             -hdf project.name "${project.name}"
             -d ${project.build.directory}/apidocs
           </additionalparam>
          <useStandardDocletOptions>false</useStandardDocletOptions>
          <!--
            | Apple's JVM sometimes requires more memory
          -->
          <additionalJOption>-J-Xmx1024m</additionalJOption>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```

The use of `bootclasspath` parameter is strongly recommended if users compile their javadoc with Sun's JVM, it's not required on Apple's JVM that ignores it. If you intend the API docs be generated across multiple JVMs, we suggest to put it as shown above.

You can build your documentation with the `mvn javadoc:javadoc` or `mvn site` goal.

### Warning ###

Current release of Doclava (1.0.5) requires JDK1.6 to be executed, if you're testing your application in a retro-compatible JVM we strongly suggest to put Doclava in a proper profile (to avoid break your build), activated when Java1.6 is detected:

```
  <profile>
    <id>doclava</id>
    <activation>
      <jdk>1.6</jdk>
    </activation>
    <build>
      <plugins>
        <plugin>
          <artifactId>maven-javadoc-plugin</artifactId>
          <configuration>
            <bootclasspath>${sun.boot.class.path}</bootclasspath>
            <doclet>com.google.doclava.Doclava</doclet>
            <useStandardDocletOptions>false</useStandardDocletOptions>
            <docletArtifact>
              <groupId>com.google.doclava</groupId>
              <artifactId>doclava</artifactId>
              <version>1.0.5</version>
            </docletArtifact>
            <additionalparam>
             -hdf project.name "${project.name}"
            -d ${project.build.directory}/apidocs
            -quiet
            </additionalparam>
          </configuration>
        </plugin>
      </plugins>
    </build>
  </profile>
```