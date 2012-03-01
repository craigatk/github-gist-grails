package gist

class GistFileService {
  static transactional = false

  GistFinder gistFinder = new GistFinder()
  GistUploadService gistUploadService

  def serviceMethod(String username, String password) {
    println("Running service method with username [${username}] and password [${password}]")
  }

  def processNewGistsInDirectories(List<String> dirNames, String username, String password) {
    def gitHubCredentials = new GitHubCredentials(
        username: username,
        password: password
    )

    dirNames.each { dirName ->
      def gistFileEntries = gistFinder.findGistsInDir(new File(dirName))
      
      gistFileEntries.each { gistFileEntry ->
        if (gistFileEntry.id) {
          gistUploadService.updateGistContent(gistFileEntry, gitHubCredentials)
        } else {
          gistUploadService.uploadNewGist(gistFileEntry, gitHubCredentials)
        }
      }
    }
  }
}
