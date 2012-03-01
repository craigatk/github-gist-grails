package gist

class GistFileService {
  static transactional = false

  GistFileUpdater gistFileUpdater = new GistFileUpdater()
  GistFinder gistFinder = new GistFinder()
  GistUploadService gistUploadService

  def processNewGistsInDirectories(List<String> dirNames, String username, String password) {
    def gitHubCredentials = new GitHubCredentials(
        username: username,
        password: password
    )

    dirNames.each { dirName ->
      def gistFileEntries = gistFinder.findGistsInDir(new File(dirName))
      
      gistFileEntries.each { gistFileEntry ->
        if (gistFileEntry.id) {
          if (gistUploadService.gistContentIsUpdated(gistFileEntry, gitHubCredentials)) {
            gistFileEntry = gistUploadService.updateGistContent(gistFileEntry, gitHubCredentials)

            println "Updated Gist with ID ${gistFileEntry.id} at URL ${gistFileEntry.htmlUrl}"  
          } else {
            println "Skipped unchanged Gist with ID ${gistFileEntry.id}"
          }
        } else {
          gistFileEntry = gistUploadService.uploadNewGist(gistFileEntry, gitHubCredentials)
          gistFileUpdater.updateGistFileEntry(gistFileEntry)

          println "Created Gist with ID ${gistFileEntry.id} at URL ${gistFileEntry.htmlUrl}"
        }
      }
    }
  }
}
