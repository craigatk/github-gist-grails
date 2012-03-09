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
  void shouldProcessNewAndExistingGists() {
    def newGist1 = new GistFileEntry()
    def newGist2 = new GistFileEntry()
    def updatedGist = new GistFileEntry(id: "updated")
    def existingGist = new GistFileEntry(id: "existing")

    gistFinder.findGistsInDir(new File("dir1")).returns([newGist1])
    gistFinder.findGistsInDir(new File("dir2")).returns([newGist2])
    gistFinder.findGistsInDir(new File("dir3")).returns([updatedGist, existingGist])

    gistUploadService.uploadNewGist(newGist1, gitHubCredentials).returns(newGist1)
    gistFileUpdater.updateGistFileEntry(newGist1)

    gistUploadService.uploadNewGist(newGist2, gitHubCredentials).returns(newGist2)
    gistFileUpdater.updateGistFileEntry(newGist2)

    gistUploadService.gistExists(updatedGist, gitHubCredentials).returns(true)
    gistUploadService.gistContentIsUpdated(updatedGist, gitHubCredentials).returns(true)
    gistUploadService.updateGistContent(updatedGist, gitHubCredentials).returns(updatedGist)

    gistUploadService.gistExists(existingGist, gitHubCredentials).returns(true)
    gistUploadService.gistContentIsUpdated(existingGist, gitHubCredentials).returns(false)

    play {
      gistFileService.processGistsInDirectories(["dir1", "dir2", "dir3"], username, password)
    }
  }

  @Test
  void whenGistIdNoLongerExistsShouldCreateNewGist() {
    def missingGist = new GistFileEntry(id: "missing")

    gistFinder.findGistsInDir(new File("dir1")).returns([missingGist])

    gistUploadService.gistExists(missingGist, gitHubCredentials).returns(false)

    gistUploadService.uploadNewGist(missingGist, gitHubCredentials).returns(missingGist)
    gistFileUpdater.updateGistFileEntry(missingGist)

    play {
      gistFileService.processGistsInDirectories(["dir1"], username, password)
    }
  }
}
