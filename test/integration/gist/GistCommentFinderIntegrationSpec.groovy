package gist

import spock.lang.Specification

class GistCommentFinderIntegrationSpec extends Specification {
  static String oneLineGist = "Some gist content"
  static String twoLineGist = oneLineGist + "\n" + oneLineGist

  GistCommentFinder gistCommentFinder = new GistCommentFinder()
  
  File gistFile
  
  def setup() {
    gistFile = new File("target/gistCommentFinder.txt")
    gistFile.text = ""
  }
  
  void 'when one-line Gist in file should get Gist'() {
    given:
    gistFile.text = createGistText(oneLineGist)

    when:
    def gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)

    then:
    assert gistsFromFile.size() == 1
    
    assert gistsFromFile[0].contentLines == [oneLineGist]
    assert gistsFromFile[0].isPublic
  }

  void 'when two-line Gist in file should get Gist'() {
    given:
    gistFile.text = createGistText(twoLineGist)

    when:
    List<GistFileEntry> gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)

    then:
    assert gistsFromFile.size() == 1

    assert gistsFromFile[0].contentLines == [oneLineGist, oneLineGist]
  }
  
  void 'when full file Gist should get Gist'() {
    given:
    gistFile.text = """// <gist>
class TestService {

  def serviceMethod() {
    println "In service method"
  }
}
// </gist>
"""

    when:
    List<GistFileEntry> gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)

    then:
    assert gistsFromFile.size() == 1

    assert gistsFromFile[0].contentLines.size() == 6
    assert gistsFromFile[0].contentLines[5] == "}"
  }

  void 'should get Gist ID from file'() {
    given:
    final String id = "123"

    gistFile.text = createGistText("Some text", [id: id])

    when:
    List<GistFileEntry> gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)

    then:
    assert gistsFromFile.size() == 1

    assert gistsFromFile[0].id == id
  }

  void 'when not public should get public from file'() {
    given:
    gistFile.text = createGistText("Some text", ['public': 'false'])

    when:
    List<GistFileEntry> gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)

    then:
    assert gistsFromFile.size() == 1

    assert !gistsFromFile[0].isPublic
  }

  void 'when public should get public from file'() {
    given:
    gistFile.text = createGistText("Some text", ['public': 'true'])

    when:
    List<GistFileEntry> gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)

    then:
    assert gistsFromFile.size() == 1

    assert gistsFromFile[0].isPublic
  }

  void 'should get Gist starting line from file'() {
    given:
    gistFile.text = createGistText(oneLineGist)

    when:
    List<GistFileEntry> gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)

    then:
    assert gistsFromFile.size() == 1

    assert gistsFromFile[0].gistStartLineIndex == 1
  }

  private String createGistText(String innerText, Map attributes = null) {
    String gistAttributes = (attributes) ? " " + attributes?.collect { key, value -> " ${key}=\"${value}\"" }.join(" ") : ""
    
    """
// <gist${gistAttributes}>
${innerText}
// </gist>"""
  }
}
