## Simple Customizations ##

### Add a title ###

You can set a title for your project by passing an [HDF](http://en.wikipedia.org/wiki/Hierarchical_Data_Format) variable as an argument to doclava. Add the argument:

` -hdf project.name "Your Project's Name" `

### Add an overview page ###
The standard Javadoc tool supports an [overview page](http://download-llnw.oracle.com/javase/1.3/docs/tooldocs/solaris/javadoc.html#overviewtags), configured as: ` -overview <yourpage.html> `. This is a good place to let users know which classes and methods to look at to get started with your project.

### Change the style ###

By default, Doclava includes an empty "assets/customizations.css" file as the last included css file. By overriding this file, you can modify the look and feel of your page.

To include your own customizations, add an argument:

` -templatedir my_template_dir `

By specifying this directory, you can override any of the built-in assets and templates . An example custom template directory might have the following structure:

```
  my_template_dir/
      assets/
         customizations.css
         customizations.js
      components/
         api_filter.cs
         left_nav.cs
         masthead.cs
         search_box.cs
      customizations.cs
      footer.cs
```

## Advanced Customizations ##

### Embedded docs ###
[Click here](EmbeddedDocs.md) for information on embedding your documentation in a larger project page.

### Custom code ###
Doclava is open source, so feel free to customize it to fit your needs! If you do something you think others will enjoy, be sure to let us know.