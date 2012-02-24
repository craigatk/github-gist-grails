package gist

class GistFileService {
  static transactional = false

  GistFinder gistFinder = new GistFinder()
  GistUploadService gistUploadService

  def serviceMethod() {
    println("Running service method")
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
