package gist

import org.eclipse.egit.github.core.Gist
import org.eclipse.egit.github.core.service.GistService
import org.gmock.WithGMock
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Test

@WithGMock
class GistUploadServiceTests {
  GistUploadService gistUploadService
  
  GistRemoteService gistRemoteService

  final def gitHubCredentials = new GitHubCredentials(username: "username", password: "password")

  final String gistFileName = "gistFile.txt"

  final GistFileEntry gistFileEntry = new GistFileEntry(
      contentLines: ["content line 1"],
      file: new File(gistFileName)
  )
  
  @Before
  void setUp() {
    gistUploadService = new GistUploadService()
    
    gistRemoteService = mock(GistRemoteService)
    gistUploadService.gistRemoteService = gistRemoteService
  }
  
  @Test
  void shouldUploadNewGist() {
    GistService gistService = mock(GistService)

    gistRemoteService.createGistService(gitHubCredentials).returns(gistService)

    Gist updatedGist = new Gist(
        id: "1234"
    )
    
    gistService.createGist(CoreMatchers.any(Gist)).returns(updatedGist)
    
    play {
      gistUploadService.uploadNewGist(gistFileEntry, gitHubCredentials)  
    }

    assert gistFileEntry.id == "1234"
  }
  
  @Test
  void shouldUpdateGist() {
    GistService gistService = mock(GistService)

    gistRemoteService.createGistService(gitHubCredentials).returns(gistService)

    Gist updatedGist = new Gist(
        id: "1234"
    )
    
    gistService.updateGist(CoreMatchers.any(Gist)).returns(updatedGist)

    play {
      gistUploadService.updateGistContent(gistFileEntry, gitHubCredentials)
    }
  }
  
  @Test
  void whenGistIsPublicShouldCreateGistObject() {
    gistFileEntry.isPublic = true
    
    Gist gist = gistUploadService.createGistObject(gistFileEntry)
    
    assert gist.isPublic()
    assert !gist.id

    def gistFile = gist.files[(gistFileName)]
    assert gistFile

    assert gistFile.content == gistFileEntry.content
    assert gistFile.filename == gistFileName
  }
  
  @Test
  void whenGistIsPrivateShouldCreateGistObject() {
    gistFileEntry.isPublic = false

    Gist gist = gistUploadService.createGistObject(gistFileEntry)

    assert !gist.isPublic()
  }
  
  @Test
  void whenGistHasIdShouldSetIdOnGistObject() {
    gistFileEntry.id = "1234"
    
    Gist gist = gistUploadService.createGistObject(gistFileEntry)
    
    assert gist.id == gistFileEntry.id
  }
}
