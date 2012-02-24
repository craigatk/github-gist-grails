package gist

class GistCommentFinder {
  List<GistFileEntry> findGistsInFile(File file) {
    def gists = []
    
    GistFileEntry gistFileEntry = null

    file.eachLine(0) { String line, index ->
      if (lineHasStartingTag(line)) {
        String gistId = getGistIdFromLine(line)
        
        gistFileEntry = new GistFileEntry(
            file: file,
            gistStartLineIndex: index)
        
        if (gistId) {
          gistFileEntry.id = gistId
          gistFileEntry.status = GistStatus.UPLOADED
        } else {
          gistFileEntry.status = GistStatus.NEW
        }
      } else if (lineHasEndingTag(line)) {
        gists << gistFileEntry
        
        gistFileEntry = null
      } else if (gistFileEntry) {
        gistFileEntry.contentLines << line
      }
    }
    
    return gists
  }

  boolean lineHasStartingTag(String line) {
    line =~ /<gist.*>/
  }
  
  String getGistIdFromLine(String line) {
    String massagedLine = line.substring(line.indexOf('<')) + "</gist>"
    
    new XmlSlurper().parseText(massagedLine).@id
  }

  boolean lineHasEndingTag(String line) {
    line =~ /<\/gist>/
  }
}
