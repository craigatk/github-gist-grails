package gist
import org.eclipse.egit.github.core.Gist
import org.eclipse.egit.github.core.service.GistService
import spock.lang.Specification

class GistUploadServiceSpec extends Specification {
  GistUploadService gistUploadService
  
  GistRemoteService gistRemoteService
  GistService gistService

  final def gitHubCredentials = new GitHubCredentials(username: "username", password: "password")

  final String gistFileName = "gistFile.txt"

  final GistFileEntry gistFileEntry = new GistFileEntry(
      contentLines: ["content line 1"],
      file: new File(gistFileName)
  )
  
  void setup() {
    gistUploadService = new GistUploadService()
    
    gistRemoteService = Mock()
    gistUploadService.gistRemoteService = gistRemoteService

    gistService = Mock()
  }
  
  def "should upload new Gist"() {
    given:
    Gist updatedGist = new Gist(
        id: "1234"
    )

    when:
    gistUploadService.uploadNewGist(gistFileEntry, gitHubCredentials)

    then:
    1 * gistRemoteService.createGistService(gitHubCredentials) >> gistService
    1 * gistService.createGist(_ as Gist) >> updatedGist
    0 * _

    and:
    assert gistFileEntry.id == "1234"
  }
  
  void "should update Gist"() {
    given:
    Gist updatedGist = new Gist(
        id: "1234"
    )

    when:
    gistUploadService.updateGistContent(gistFileEntry, gitHubCredentials)

    then:
    1 * gistRemoteService.createGistService(gitHubCredentials) >> gistService
    1 * gistService.updateGist(_ as Gist) >> updatedGist
    0 * _
  }
  
  void "when Gist is public should create Gist object"() {
    given:
    gistFileEntry.isPublic = true

    when:
    Gist gist = gistUploadService.createGistObject(gistFileEntry)

    then:
    assert gist.isPublic()
    assert !gist.id

    def gistFile = gist.files[(gistFileName)]
    assert gistFile

    assert gistFile.content == gistFileEntry.content
    assert gistFile.filename == gistFileName
  }
  
  void "when Gist is private should create Gist object"() {
    given:
    gistFileEntry.isPublic = false

    when:
    Gist gist = gistUploadService.createGistObject(gistFileEntry)

    then:
    assert !gist.isPublic()
  }
  
  void "when Gist has ID should set ID on Gist object"() {
    given:
    gistFileEntry.id = "1234"

    when:
    Gist gist = gistUploadService.createGistObject(gistFileEntry)

    then:
    assert gist.id == gistFileEntry.id
  }
}
