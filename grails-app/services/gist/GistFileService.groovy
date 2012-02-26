package gist

class GistFileService {
  static transactional = false

  GistFinder gistFinder = new GistFinder()
  GistUploadService gistUploadService

  def serviceMethod(String password) {
    println("Running service method with password [${password}]")
  }

  def processNewGistsInDirectories(List<String> dirNames) {
    dirNames.each { dirName ->
      def gists = gistFinder.findGistsInDir(new File(dirName))
      
      def newGists = gists.findAll { it.status == GistStatus.NEW }
      
      newGists.each { newGist ->
        gistUploadService.uploadNewGist(newGist)
      }
    }
  }
}
