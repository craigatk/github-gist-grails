package gist


class GistFinder {
  GistCommentFinder gistCommentFinder = new GistCommentFinder()
  
  List<GistFileEntry> findGistsInDir(File dir) {
    List<GistFileEntry> gists = []
    
    def filesInDir = dir.listFiles()
    
    filesInDir.each { file ->
      if (!file.isDirectory()) {
        gists.addAll(gistCommentFinder.findGistsInText(file.text, file))
      } else {
        gists.addAll(findGistsInDir(file))
      } 
    }
    
    return gists
  }
}
