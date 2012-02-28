package gist

import org.junit.Test

class GistUploadServiceSystemTests {
  GistUploadService gistUploadService
  
  GistRemoteService gistRemoteService

  @Test
  void shouldUploadNewGist() {
    GistFileEntry gistFileEntry = new GistFileEntry(
        contentLines: ["Some test content", "Line 2"],
        file: new File("Hello.groovy")
    )
    
    String username = gistRemoteService.gitHubUsername
    String password = gistRemoteService.gitHubPassword
    
    def gitHubCredentials = new GitHubCredentials(username: username, password: password)

    def updatedGistFileEntry = gistUploadService.uploadNewGist(gistFileEntry, gitHubCredentials)

    assert updatedGistFileEntry.id
  }
}
