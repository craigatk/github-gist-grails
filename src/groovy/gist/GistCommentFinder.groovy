package gist

class GistCommentFinder {
  List<GistFileEntry> findGistsInText(String text) {
    def gists = []
    
    GistFileEntry gistFileEntry = null

    def lines = text.readLines()
    
    lines.each { line ->
      if (lineHasStartingTag(line)) {
        String gistId = getGistIdFromLine(line)
        
        gistFileEntry = new GistFileEntry() 
        
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
