package gist

import spock.lang.Specification

class GistFileUpdaterIntegrationSpec extends Specification {
  GistFileUpdater gistFileUpdater = new GistFileUpdater()
  GistCommentFinder gistCommentFinder = new GistCommentFinder()

  final String gistLine1 = "Gist line 1"
  
  void whenNewGistShouldUpdateIdInFile() {
    given:
    String id = "123"
    
    File file = new File("target/idUpdate.txt")
    
    file.text = createGistText()
    
    GistFileEntry gistFileEntry = gistCommentFinder.findGistsInFile(file)[0]
    gistFileEntry.id = id

    when:
    gistFileUpdater.updateGistFileEntry(gistFileEntry)

    then:
    assert file.text.contains("""<gist id="123">""")
  }
  
  void whenTwoNewGistsShouldUpdateIdsInFile() {
    given:
    final String id1 = "123"
    final String id2 = "456"
    
    File file = new File("target/idUpdate.txt")

    file.text = createGistText() + createGistText()   
    
    def gistFileEntries = gistCommentFinder.findGistsInFile(file)
    assert gistFileEntries?.size() == 2
    
    gistFileEntries[0].id = id1
    gistFileEntries[1].id = id2

    when:
    gistFileUpdater.updateGistFileEntry(gistFileEntries[0])

    then:
    assert file.text.contains("""<gist id="${id1}">""")
    assert !file.text.contains("""<gist id="${id2}">""")

    when:
    gistFileUpdater.updateGistFileEntry(gistFileEntries[1])

    then:
    assert file.text.contains("""<gist id="${id1}">""")
    assert file.text.contains("""<gist id="${id2}">""")
  }
  
  void whenGistHasNewIdShouldUpdateId() {
    given:
    File file = new File("target/newId.txt")
    
    file.text = """
<gist id="oldId">
content
</gist>
    """
    
    def gistFileEntries = gistCommentFinder.findGistsInFile(file)
    assert gistFileEntries.size() == 1
    
    gistFileEntries[0].id = "newId"

    when:
    gistFileUpdater.updateGistFileEntry(gistFileEntries[0])

    then:
    assert file.text.contains("""<gist id="newId">""")
    assert !file.text.contains("""<gist id="old">""")
  }
  
  void whenGistIsNotPublicShouldKeepPublicGistAttribute() {
    given:
    File file = new File("target/private.txt")

    file.text = """
<gist id="theId" public="false">
content
</gist>
"""

    def gistFileEntries = gistCommentFinder.findGistsInFile(file)
    assert gistFileEntries.size() == 1

    assert !gistFileEntries[0].isPublic

    when:
    gistFileUpdater.updateGistFileEntry(gistFileEntries[0])

    then:
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
