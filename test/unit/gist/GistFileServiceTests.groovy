package gist

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test

@WithGMock
class GistFileServiceTests {
  GistFileService gistFileService

  GistFileUpdater gistFileUpdater
  GistFinder gistFinder
  GistUploadService gistUploadService

  final String username = "username"
  final String password = "password"

  final def gitHubCredentials = new GitHubCredentials(
      username: username,
      password: password
  )

  @Before
  void setUp() {
    gistFileService = new GistFileService()

    gistFileUpdater = mock(GistFileUpdater)
    gistFileService.gistFileUpdater = gistFileUpdater
    
    gistFinder = mock(GistFinder)
    gistFileService.gistFinder = gistFinder

    gistUploadService = mock(GistUploadService)
    gistFileService.gistUploadService = gistUploadService
  }

  @Test
  void shouldProcessNewGists() {
    def newGist1 = new GistFileEntry()
    def newGist2 = new GistFileEntry()
    def uploadedGist = new GistFileEntry(id: "1234")

    gistFinder.findGistsInDir(new File("dir1")).returns([newGist1])
    gistFinder.findGistsInDir(new File("dir2")).returns([newGist2])
    gistFinder.findGistsInDir(new File("dir3")).returns([uploadedGist])

    gistUploadService.uploadNewGist(newGist1, gitHubCredentials).returns(newGist1)
    gistFileUpdater.updateGistFileEntry(newGist1)

    gistUploadService.uploadNewGist(newGist2, gitHubCredentials).returns(newGist2)
    gistFileUpdater.updateGistFileEntry(newGist2)

    gistUploadService.updateGistContent(uploadedGist, gitHubCredentials).returns(uploadedGist)
    
    play {
      gistFileService.processNewGistsInDirectories(["dir1", "dir2", "dir3"], username, password)
    }
  }
}
