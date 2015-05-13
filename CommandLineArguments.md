# Command Line Arguments #

### -d _dir_ ###

Specify where to put the output files.


### -templatedir _dir_ ###

Specify the directory of clearsilver template files.  You can have as many of these as you need


### -hdf _name_ _value_ ###

Set a value in the Clearsilver hdf namespace.  See the [Clearsilver docs](http://www.clearsilver.net/docs/) for more info.


### -htmldir _dir_ ###

Specify the source directory for html/jd files.  If you use this flag, you should have a file in that directory called _index.jd_.  It will turn that into the index.html, and add that as the "Main Page."  If you don't specify this flag, the packages list page will become the index.html.

You can also put files, for example, images, in this directory and they will be copied to the output directory.

See "Narrative Documentation: .jd files" below for more information on what goes in this directory.


### -title _string_ ###

Specify a string to use as the html page title.


### -werror ###

Set this if you want anything that would be a warning to be an error.  You can use _-error_, _-warning_ or _-hide_ for finer-grained control.  See below.


### -error _number_, -warning _number_, -hide _number_ ###

Specify whether a given error _number_ is an error, warning, or whether to hide it altogether.  These are common documentation mistakes that can easily be caught by the tool and increase the general quality and reduce the amount of proofreading necessary.  None of these default to error because the Sun doclet allows most of these errors, and defaulting to errors would make most Java code not produce docs by default.  The best practice is to turn the warnings into errors one at a time once you have eliminated them.

| **Error Number** | **Default** | **Description** |
|:-----------------|:------------|:----------------|
| 101              | warning   | Unresolved @link or @see tag. |
| 102              | warning   | Bad {@sample } or {@include } tag. |
| 103              | warning   | Unknown tag.  Check for misspellings like @returns instead of @return, @throw instead of @throws, etc. |
| 104              | warning   | Bad @param tag name.  An @param tag references a parameter name that doesn't exist in the given method. |
| 105              | hidden    | Undocumented Parameter.  There is a parameter that doesn't have a corresponding @param tag.  This is really really common, so it's hidden by default |

Click [here](http://code.google.com/p/doclava/source/browse/trunk/src/com/google/doclava/Errors.java#115) for a full list of errors.
### -proofread _file_ ###

Specify a proofread file.  A proofread file contains all of the text content of the javadocs concatenated into one file, suitable for spell-checking and other goodness.


### -todo _file_ ###

Specify a todo file.  A todo file lists the program elements that are missing documentation.  At some point, this might be improved to show more warnings.


### -public, -protected, -package, -private, -hidden ###

Specify the level of visibility to show.  -public shows only public classes, members; -protected only protected, etc.  -hidden shows everything, even program elements that have been hidden with @hide.

### -since _version.xml_ _name_ ###
Provide information about when API features were added to the project.

### -apiversion _version_ ###
The current version name of the project. Used when generating versioned documentation using the "-since" argument.

### -federate _name_ _site_ ###
Link against an external documentation site. For more information, see [FederatedDocs](FederatedDocs.md)

### -federationxml _name_ _file_ ###
You can specify an alternative xml file when federating against an external site. This can be useful for federating against sites that use the same layout for reference documentation, but do not provide a current.xml file.

### -paresecomments ###
If Doclava is being run for a purpose other than generating documentation, comments will not be parsed unless this flag is set. Not parsing comments can speed up builds for large projects, but will result in errors in those comments not being reported.

### -assetsdir _dir_ ###
Puts the assets directory in _dir_, a relative path to the documentation output directory.

### -generatesources ###
Embeds your source code within your generated documentation. Added in version 1.0.5.

### -toroot _path_ ###
Prepends the given _path_ to any generated relative URL or fragment. Useful for specifying an absolute path.

### -yaml _filename.ext_ ###
Generates a [YAML](http://wikipedia.org/wiki/YAML) file named _filename.ext_ that contains a list of the package, class, interface and exception names with links to their files.  You can convert this to a navigation menu or table of contents for the reference documentation. The output YAML file is created in the assets folder.  You normally set _.ext_ to the .yaml extension.