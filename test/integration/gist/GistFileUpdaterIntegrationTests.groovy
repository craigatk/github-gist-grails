package gist

import org.junit.Test


class GistFileUpdaterIntegrationTests {
  GistFileUpdater gistFileUpdater = new GistFileUpdater()
  GistCommentFinder gistCommentFinder = new GistCommentFinder()

  final String gistLine1 = "Gist line 1"
  
  @Test
  void whenNewGistShouldUpdateIdInFile() {
    String id = "123"
    
    File file = new File("target/idUpdate.txt")
    
    file.text = createGistText()
    
    GistFileEntry gistFileEntry = gistCommentFinder.findGistsInText(file.text, file)[0]
    gistFileEntry.id = id
    
    gistFileUpdater.updateGistFileEntry(gistFileEntry)
    
    assert file.text.contains("""<gist id="123">""")
  }
  
  @Test
  void whenTwoNewGistsShouldUpdateIdsInFile() {
    final String id1 = "123"
    final String id2 = "456"
    
    File file = new File("target/idUpdate.txt")

    file.text = createGistText() + createGistText()   
    
    def gistFileEntries = gistCommentFinder.findGistsInText(file.text, file)
    assert gistFileEntries?.size() == 2
    
    gistFileEntries[0].id = id1
    gistFileEntries[1].id = id2
    
    gistFileUpdater.updateGistFileEntry(gistFileEntries[0])
    assert file.text.contains("""<gist id="${id1}">""")
    assert !file.text.contains("""<gist id="${id2}">""")

    gistFileUpdater.updateGistFileEntry(gistFileEntries[1])
    assert file.text.contains("""<gist id="${id1}">""")
    assert file.text.contains("""<gist id="${id2}">""")
  }
  
  private String createGistText() {
    """
Some text
// <gist>
${gistLine1}
// </gist>
"""
  }
}
