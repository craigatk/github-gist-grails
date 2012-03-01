package gist

import org.junit.After
import org.junit.Before
import org.junit.Test

class GistUploadServiceSystemTests {
  GistUploadService gistUploadService
  
  GistRemoteService gistRemoteService

  GitHubCredentials gitHubCredentials
  
  String gistIdToDelete

  @Before
  void setUp() {
    String username = gistRemoteService.gitHubUsername
    String password = gistRemoteService.gitHubPassword

    gitHubCredentials = new GitHubCredentials(username: username, password: password)
  }

  @After
  void deleteGist() {
    if (gistIdToDelete) {
      gistUploadService.deleteGist(gistIdToDelete, gitHubCredentials)
    }
  }

  @Test
  void shouldUploadNewGist() {
    GistFileEntry gistFileEntry = new GistFileEntry(
        contentLines: ["Some test content", "Line 2"],
        file: new File("HelloNew.groovy")
    )

    def updatedGistFileEntry = gistUploadService.uploadNewGist(gistFileEntry, gitHubCredentials)

    assert updatedGistFileEntry.id

    gistIdToDelete = updatedGistFileEntry.id
  }

  @Test
  void shouldUpdateExistingGist() {
    GistFileEntry gistFileEntry = new GistFileEntry(
        contentLines: ["Some test content", "Line 2"],
        file: new File("HelloUpdate.groovy")
    )

    def gistAfterCreate = gistUploadService.uploadNewGist(gistFileEntry, gitHubCredentials)
    assert gistAfterCreate.id

    gistIdToDelete = gistAfterCreate.id

    gistAfterCreate.contentLines << "New line 3"

    def gistAfterUpdate = gistUploadService.updateGistContent(gistAfterCreate, gitHubCredentials)

    assert gistAfterUpdate.id
  }
  
  @Test
  void whenGistContentNotChangedShouldNotBeUpdated() {
    GistFileEntry gistFileEntry = new GistFileEntry(
        contentLines: ["Some test content", "Line 2"],
        file: new File("HelloUpdate.groovy")
    )

    def gistAfterCreate = gistUploadService.uploadNewGist(gistFileEntry, gitHubCredentials)
    assert gistAfterCreate.id

    gistIdToDelete = gistAfterCreate.id
    
    assert !gistUploadService.gistContentIsUpdated(gistAfterCreate, gitHubCredentials)
  }

  @Test
  void whenGistContentChangedShouldBeUpdated() {
    GistFileEntry gistFileEntry = new GistFileEntry(
        contentLines: ["Some test content", "Line 2"],
        file: new File("HelloUpdate.groovy")
    )

    def gistAfterCreate = gistUploadService.uploadNewGist(gistFileEntry, gitHubCredentials)
    assert gistAfterCreate.id

    gistIdToDelete = gistAfterCreate.id

    gistAfterCreate.contentLines << "New line 3"

    assert gistUploadService.gistContentIsUpdated(gistAfterCreate, gitHubCredentials)
  }
}
