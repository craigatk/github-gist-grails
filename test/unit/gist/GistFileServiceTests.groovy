package gist

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test

@WithGMock
class GistFileServiceTests {
  GistFileService gistFileService

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
    
    gistFinder = mock(GistFinder)
    gistFileService.gistFinder = gistFinder

    gistUploadService = mock(GistUploadService)
    gistFileService.gistUploadService = gistUploadService
  }

  @Test
  void shouldProcessNewGists() {
    def newGist1 = new GistFileEntry(status: GistStatus.NEW)
    def newGist2 = new GistFileEntry(status: GistStatus.NEW)
    def uploadedGist = new GistFileEntry(status:  GistStatus.UPLOADED)

    gistFinder.findGistsInDir(new File("dir1")).returns([newGist1])
    gistFinder.findGistsInDir(new File("dir2")).returns([newGist2])
    gistFinder.findGistsInDir(new File("dir3")).returns([uploadedGist])

    gistUploadService.uploadNewGist(newGist1, gitHubCredentials)
    gistUploadService.uploadNewGist(newGist2, gitHubCredentials)
    gistUploadService.uploadNewGist(uploadedGist, gitHubCredentials).never()
    
    play {
      gistFileService.processNewGistsInDirectories(["dir1", "dir2", "dir3"], username, password)
    }
  }
}
