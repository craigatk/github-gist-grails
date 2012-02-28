package gist

import org.junit.Before
import org.junit.Test

class GistUploadServiceSystemTests {
  GistUploadService gistUploadService
  
  GistRemoteService gistRemoteService

  GitHubCredentials gitHubCredentials

  @Before
  void setUp() {
    String username = gistRemoteService.gitHubUsername
    String password = gistRemoteService.gitHubPassword

    gitHubCredentials = new GitHubCredentials(username: username, password: password)
  }

  @Test
  void shouldUploadNewGist() {
    GistFileEntry gistFileEntry = new GistFileEntry(
        contentLines: ["Some test content", "Line 2"],
        file: new File("HelloNew.groovy")
    )

    def updatedGistFileEntry = gistUploadService.uploadNewGist(gistFileEntry, gitHubCredentials)

    assert updatedGistFileEntry.id
  }

  @Test
  void shouldUpdateExistingGist() {
    GistFileEntry gistFileEntry = new GistFileEntry(
        contentLines: ["Some test content", "Line 2"],
        file: new File("HelloUpdate.groovy")
    )

    def gistAfterCreate = gistUploadService.uploadNewGist(gistFileEntry, gitHubCredentials)
    assert gistAfterCreate.id

    gistAfterCreate.contentLines << "New line 3"

    def gistAfterUpdate = gistUploadService.updateGistContent(gistAfterCreate, gitHubCredentials)

    assert gistAfterUpdate.id
  }
}
