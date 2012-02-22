package gist

import grails.plugin.spock.UnitSpec

class GistCommentFinderSpec extends UnitSpec {
  GistCommentFinder gistCommentFinder = new GistCommentFinder()

  def "should find start tag in line"() {
    expect:
    gistCommentFinder.lineHasStartingTag(line) == hasStartingTag

    where:
    line                          | hasStartingTag
    ""                            | false
    "<gist>"                      | true
    "// <gist>"                   | true
    "gist"                        | false
    """<gist id="1885253">"""     | true
    """// <gist id="1885253">"""  | true
  }

  def "should find end tag in line"() {
    expect:
    gistCommentFinder.lineHasEndingTag(line) == hasEndingTag

    where:
    line      | hasEndingTag
    ""        | false
    "</gist>" | true
    "/gist"   | false
    "<gist>"  | false
  }

  def "should get gist id from starting tag in line"() {
    expect:
    gistCommentFinder.getGistIdFromLine(line) == id

    where:
    line                      | id
    """<gist id="1234">"""    | "1234"
    """// <gist id="123">"""  | "123"
    "<gist id='12'>"          | "12"
  }
}
