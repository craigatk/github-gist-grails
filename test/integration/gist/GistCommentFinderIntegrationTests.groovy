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
    assert gistsFromFile[0].status == GistStatus.NEW
  }

  @Test
  void whenTwoLineGistInFileShouldGetGist() {
    gistFile.text = createGistText(twoLineGist)

    def gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)
    assert gistsFromFile.size() == 1

    assert gistsFromFile[0].contentLines == [oneLineGist, oneLineGist]
  }

  @Test
  void shouldGetGistIdFromFile() {
    final String id = "123"

    gistFile.text = createGistWithId(id)

    def gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)
    assert gistsFromFile.size() == 1

    assert gistsFromFile[0].id == id
    assert gistsFromFile[0].status == GistStatus.UPLOADED
  }

  @Test
  void shouldGetGistStartingLineFromFile() {
    gistFile.text = createGistText(oneLineGist)

    def gistsFromFile = gistCommentFinder.findGistsInFile(gistFile)
    assert gistsFromFile.size() == 1

    assert gistsFromFile[0].gistStartLineIndex == 1
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
