package gist

import org.junit.Before
import org.junit.Test

class GistCommentFinderIntegrationTests {
  static String oneLineGist = "Some gist content"
  static String twoLineGist = oneLineGist + "\n" + oneLineGist

  GistCommentFinder gistCommentFinder = new GistCommentFinder()
  
  File gistFile
  
  @Before
  void createFile() {
    gistFile = new File("target/gistCommentFinder.txt")
    gistFile.text = ""
  }
  
  @Test
  void whenOneLineGistInFileShouldGetGist() {
    gistFile.text = createGistText(oneLineGist)
    
    def gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)
    assert gistsFromFile.size() == 1
    
    assert gistsFromFile[0].contentLines == [oneLineGist]
    assert gistsFromFile[0].isPublic
  }

  @Test
  void whenTwoLineGistInFileShouldGetGist() {
    gistFile.text = createGistText(twoLineGist)

    def gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)
    assert gistsFromFile.size() == 1

    assert gistsFromFile[0].contentLines == [oneLineGist, oneLineGist]
  }
  
  @Test
  void whenFullFileGistShouldGetGist() {
    gistFile.text = """// <gist>
class TestService {

  def serviceMethod() {
    println "In service method"
  }
}
// </gist>
"""
    
    def gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)
    assert gistsFromFile.size() == 1

    assert gistsFromFile[0].contentLines.size() == 6
    assert gistsFromFile[0].contentLines[5] == "}"
  }

  @Test
  void shouldGetGistIdFromFile() {
    final String id = "123"

    gistFile.text = createGistText("Some text", [id: id])

    def gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)
    assert gistsFromFile.size() == 1

    assert gistsFromFile[0].id == id
  }

  @Test
  void whenNotPublicShouldGetPublicFromFile() {
    gistFile.text = createGistText("Some text", ['public': 'false'])

    def gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)
    assert gistsFromFile.size() == 1

    assert !gistsFromFile[0].isPublic
  }

  @Test
  void whenPublicShouldGetPublicFromFile() {
    gistFile.text = createGistText("Some text", ['public': 'true'])

    def gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)
    assert gistsFromFile.size() == 1

    assert gistsFromFile[0].isPublic
  }

  @Test
  void shouldGetGistStartingLineFromFile() {
    gistFile.text = createGistText(oneLineGist)

    def gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)
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
