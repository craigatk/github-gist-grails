# Grails GitHub Gist Plugin #

## What is a GitHub Gist? ##
GitHub has this handy code snippet sharing system called [Gists](https://gist.github.com/) that make it easy to display formatted code on blogs, etc.
You can easily embed Gists into your blog (Wordpress, etc.) with a simple JavaScript script tag.

## Plugin Description ##
Creates GitHub Gists from sections of a Grails project so you can use compiled and tested code snippets written from the comfort of your IDE in blog articles, etc.
And when you update the code in your Grails project you can easily keep the corresponding Gists in sync.

The plugin uses the [GitHub Java API](https://github.com/eclipse/egit-github/tree/master/org.eclipse.egit.github.core) to create and update the Gists.

## Installation ##
Install the plugin with the 'grails install-plugin' command:

    grails install-plugin github-gist

Or add the plugin definition in BuildConfig.groovy:

    compile(":github-gist:0.1")

## Usage ##
Mark the sections of code you want to upload to create a Gist with starting and ending <gist></gist> XML tags, usually in comments.
And type of comment doesn't matter (block /* */ vs. line //).

    // <gist>
    void myGistMethod() {
      // Magic happens here
    }
    // </gist>

Then run 'grails process-gists', enter your GitHub username and password, and voila! the Gists are created.
And the <gist> XML tags are updated with the ID of the newly created Gists, which you'll probably need to share the Gists.

    > grails process-gists
    GitHub username: myusername
    GitHub password: **********

And when you later want to update your Gists, run the same command 'grails process-gists'.

### Config parameters ###

If you do want to type in your GitHub username and password (or if you want to create/update Gists in an automated build), you can specify your GitHub credentials in Config.groovy parameters:

    gist.github.username="myusername"
    gist.github.password="mypassword"

### Public/Private Gists ##

By default, the Gists created are public (for easy sharing on blogs, etc.), but if you so desire you can mark Gists as private by:

    // <gist public="false">
    ...
    // </gist>

## Version History ##

0.1: Initial release