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

  static String oneLineGist = "Some gist content"
  static String twoLineGist = oneLineGist + "\n" + oneLineGist

  def "should get gists content from text"() {
    expect:
    gistCommentFinder.findGistsInText(text)*.contentLines == gists

    where:
    text                          | gists
    createGistText(oneLineGist)   | [[oneLineGist]]
    createGistText(twoLineGist)   | [[oneLineGist, oneLineGist]]
  }

  def "should get gist id from text"() {
    expect:
    gistCommentFinder.findGistsInText(text)*.id == gistIds

    where:
    text                  | gistIds
    createGistWithId("123") | ["123"]
  }

  private String createGistText(String innerText) {
    """
// <gist>
${innerText}
// </gist>"""
  }

  private String createGistWithId(String id) {
    """
// <gist id="${id}">
Some text
// </gist>"""
  }
}
