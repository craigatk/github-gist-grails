package gist

import spock.lang.Specification

class GistFileServiceSpec extends Specification {
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

  def setup() {
    gistFileService = new GistFileService()

    gistFileUpdater = Mock()
    gistFileService.gistFileUpdater = gistFileUpdater

    gistFinder = Mock()
    gistFileService.gistFinder = gistFinder

    gistUploadService = Mock()
    gistFileService.gistUploadService = gistUploadService
  }

  def "should process new and existing Gists"() {
    given:
    def newGist1 = new GistFileEntry()
    def newGist2 = new GistFileEntry()
    def updatedGist = new GistFileEntry(id: "updated")
    def existingGist = new GistFileEntry(id: "existing")


    when:
    gistFileService.processGistsInDirectories(["dir1", "dir2", "dir3"], gitHubCredentials)

    then:
    1 *  gistFinder.findGistsInDir(new File("dir1")) >> [newGist1]
    1 * gistFinder.findGistsInDir(new File("dir2")) >> [newGist2]
    1 * gistFinder.findGistsInDir(new File("dir3")) >> [updatedGist, existingGist]

    1 * gistUploadService.uploadNewGist(newGist1, gitHubCredentials) >> newGist1
    1 * gistFileUpdater.updateGistFileEntry(newGist1)

    1 * gistUploadService.uploadNewGist(newGist2, gitHubCredentials) >> newGist2
    1 * gistFileUpdater.updateGistFileEntry(newGist2)

    1 * gistUploadService.gistExists(updatedGist, gitHubCredentials) >> true
    1 * gistUploadService.gistContentIsUpdated(updatedGist, gitHubCredentials) >> true
    1 * gistUploadService.updateGistContent(updatedGist, gitHubCredentials) >> updatedGist

    1 * gistUploadService.gistExists(existingGist, gitHubCredentials) >> true
    1 * gistUploadService.gistContentIsUpdated(existingGist, gitHubCredentials) >> false

    0 * _
  }

  def "when Gist ID no longer exists should create new Gist"() {
    given:
    def missingGist = new GistFileEntry(id: "missing")

    when:
    gistFileService.processGistsInDirectories(["dir1"], gitHubCredentials)

    then:
    1 * gistFinder.findGistsInDir(new File("dir1")) >> [missingGist]

    1 * gistUploadService.gistExists(missingGist, gitHubCredentials) >> false

    1 * gistUploadService.uploadNewGist(missingGist, gitHubCredentials) >> missingGist
    1 * gistFileUpdater.updateGistFileEntry(missingGist)

    0 * _
  }
}
