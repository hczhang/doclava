Doclava (rhymes with [baklava](http://en.wikipedia.org/wiki/Baklava)) is a custom [Javadoc](http://www.oracle.com/technetwork/java/javase/documentation/index-jsp-135444.html) doclet used to generate documentation. Key differences between Doclava and the standard doclet include:
  * Refreshed look and feel, including search capabilities.
  * Embeds versioning information in the documentation.
  * Uses a templating engine for user customizations.
  * Throw build errors for things that can easily be caught, like @param tags that don't match the parameter names.
  * Ability to include snippets of code from real source code
  * Federate documentation between multiple sites.
  * Ability to embed javadocs in a larger web page.

Doclava uses [JSilver](http://code.google.com/p/jsilver/) as its templating engine, a pure-Java implementation of [Clearsilver](http://www.clearsilver.net/).

## Using Doclava ##
Doclava can be used as a standard Doclet, and can be run through the Ant or Maven build systems.

For more information on how to use Doclava, see:
  * [Getting Started](GettingStarted.md)
  * [List of supported tags](JavadocTags.md)
  * [Supported arguments](CommandLineArguments.md)
  * [Embedding documentation in your project page](EmbeddedDocs.md)
  * [Customizing your documentation](CustomDocs.md)

### Contributing to Doclava ###
If you are interested in contributing to Doclava, email a project owner for commit access.