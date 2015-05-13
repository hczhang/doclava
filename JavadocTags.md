# Javadoc Tags in Doclava #

Doclava adds support for a number of Javadoc tags, and does not handle others that the standard doclet does.

## New or Improved Tags ##

### @hide ###

When applied to a package, class, method or field, @hide removes that node and all of its children from the documentation.

### @deprecated ###

Puts a warning message that you shouldn't use this particular class, method or field.  The warning is a little different from Sun's though:
  * In the summary listing, they also list the brief description of the element.  We only put the warning in the summary list, and make the user scroll down to the full description to get any info about what it does.  This is to further discourage use of deprecated program elements.
  * When a class inherits from a deprecated class or overrides a deprecated method, the deprecated warning automatically propagates into that documentation.  (This is not done when a class inherits from a deprecated interface).  You can add @undeprecate to the comments for the subclass / overridden method if you want to suppress this behavior.

### @undeprecate ###

Don't inherit @deprecated from your superclass or overridden method.  See @deprecated above.

### {@more} ###

The Sun javadoc always ends the brief description of a program element at the first period.  If you have more to say in the brief description, or have markup that has periods, put {@more} where you want the break to be.  Everything after that will be in the full description.

### {@sample} and {@include} ###

These tags copy sample text from an arbitrary file into the output javadoc html.

The @include tag copies the text verbatim from the given file.

The @sample tag copies the text from the given file and
  * strips leading and trailing whitespace
  * reduces the indent level of the text to the indent level of the first non-whitespace line
  * escapes all <, >; and & characters for html
  * drops all lines containing either BEGIN\_INCLUDE or END\_INCLUDE so sample code can be nested

Both tags accept either a filename and an id or just a filename.  If no id is provided, the entire file is copied.  If an id is provided, the lines in the given file between the first two lines containing BEGIN\_INCLUDE(id) and END\_INCLUDE(id), for the given id, are copied.  The id may be only letters, numbers and underscore (_)._

Four examples:
```
{@include samples/SampleCode/src/com/google/app/Notification1.java}
{@sample samples/SampleCode/src/com/google/app/Notification1.java}
{@include samples/SampleCode/src/com/google/app/Notification1.java Bleh}
{@sample samples/SampleCode/src/com/google/app/Notification1.java Bleh}
```

### @attr ###

The @attr tag is used for generating the docs on XML Attributes.

  * @attr name _name_ -- declares an xml attribute.  The comment here can come from an @attr description tag.  _name_ should be what you want the developer to see.  (In android, this tag is added by aapt to the R files.)
  * @attr ref _field_ -- references a field that has an @attr name tag on it.
  * @attr description _more\_tags_ -- defines the docs that are pulled into the XML Attributes section.

### prettyprint ###
Although not a tag, Doclava allows you to print a block of formatted code as follows:
```
/**
 * An example code snippet:
 * <pre class="prettyprint">
 * public class MyClass {
 *    public void myMethod() {}
 * }
 * </pre>
 */
```
## Tags that are not supported ##

### @author, @version, @serial ###

We just haven't gotten to this yet.

### @since ###
Again, we just haven't gotten there, but versioning is also supported across the entire api through versioned xml files. See [CommandLineArguments](CommandLineArguments.md).