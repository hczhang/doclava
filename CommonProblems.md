### `com.sun.tools.javac.code.Symbol$CompletionFailure: class file for sun.util.resources.OpenListResourceBundle not found` ###

You may need to add the Java runtime to Javadoc's bootclasspath. On the command line, if the JAVA\_HOME environment variable is set correctly, try:
```
  -bootclasspath $JAVA_HOME/jre/lib/rt.jar
```


### `java.lang.ClassCastException: com.sun.tools.javadoc.ClassDocImpl cannot be cast to com.sun.javadoc.AnnotationTypeDoc` ###

Try adding your project's binary to Javadoc's classpath:
```
  -classpath path/to/project.jar
```