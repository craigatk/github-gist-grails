# Grails GitHub Gist Plugin #

## Installation and Usage

See [atkinsondev.com/open-source/grails/github-gist-plugin.html](http://atkinsondev.com/open-source/grails/github-gist-plugin.html) for installation and usage instructions.

## Development

### Running Tests

The plugin comes with unit, integration and system tests. The system tests verify the plugin works with Github by creating, updating, and removing live Gists. 

To run the system tests, first add the following config parameters with your Github username and password to ~/.grails/gists-config.groovy:

    gist.github.username = "<username>"
    gist.github.password = "<password>"
    
Then run the system tests with
 
    grails test-app system:

## Version History ##

* 0.2: Updated to Grails v2.2.4 and GitHub API v2.1.5 to work correctly with GitHub
* 0.1: Initial release