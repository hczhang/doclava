# Narrative Documentation #
## -htmldir and .jd files ##

If -htmldir is given on the commandline, then Doclava reads all files in the given directory, including subdirectories, and depending on the extension of the file, takes one of the following actions:

#### _filename_.jd ####

A .jd file consists of a set of Clearsilver hdf definitions, followed by "@jd:body", and then the body text of the document.

The standard Doclava templates use the hdf node page.title to set the html `<title>` tag.

The body text of the document has the following operations performed on it:
  * It is run through the javadoc processor.  All inline javadoc tags work (like {@link}).  No out-of-line javadoc tags work (what would @return do anyway in this context?).
  * It is wrapped with the standard formatting of the documentation pages, that comes from the Clearsilver templates.

The output is then saved to _filename_.html.

```
page.title=Android SDK
@jd:body
<h1>Android SDK </h1>
<p>Welcome to Android!</p>
<p>This is a link to a class called {@link android.view.View}.</p>
```


#### _filename_.cs ####

The file is processed as a Clearsilver template file and saved to _filename_.html in the output directory.  Javadoc processing is not performed as it would be with .jd files.

#### Otherwise ####

The file is copied to the output directory unchanged.