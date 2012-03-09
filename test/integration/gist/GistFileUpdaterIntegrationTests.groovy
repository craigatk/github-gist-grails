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
    
    GistFileEntry gistFileEntry = gistCommentFinder.findGistsInFile(file)[0]
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
    
    def gistFileEntries = gistCommentFinder.findGistsInFile(file)
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
  
  @Test
  void whenGistHasNewIdShouldUpdateId() {
    File file = new File("target/newId.txt")
    
    file.text = """
<gist id="oldId">
content
</gist>
    """
    
    def gistFileEntries = gistCommentFinder.findGistsInFile(file)
    assert gistFileEntries.size() == 1
    
    gistFileEntries[0].id = "newId"

    gistFileUpdater.updateGistFileEntry(gistFileEntries[0])
    assert file.text.contains("""<gist id="newId">""")
    assert !file.text.contains("""<gist id="old">""")
  }
  
  @Test
  void whenGistIsNotPublicShouldKeepPublicGistAttribute() {
    File file = new File("target/private.txt")

    file.text = """
<gist id="theId" public="false">
content
</gist>
"""

    def gistFileEntries = gistCommentFinder.findGistsInFile(file)
    assert gistFileEntries.size() == 1

    assert !gistFileEntries[0].isPublic

    gistFileUpdater.updateGistFileEntry(gistFileEntries[0])
    assert file.text.contains("""<gist id="theId" public="false">""")
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
