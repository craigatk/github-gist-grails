package gist

class GistCommentFinder {
  List<GistFileEntry> findGistsInText(String text, File file = null) {
    def gists = []
    
    GistFileEntry gistFileEntry = null

    def lines = text.readLines()
    
    lines.eachWithIndex { line, index ->
      if (lineHasStartingTag(line)) {
        String gistId = getGistIdFromLine(line)
        
        gistFileEntry = new GistFileEntry(file: file, gistStartLineNumber: index)
        
        if (gistId) {
          gistFileEntry.id = gistId
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
