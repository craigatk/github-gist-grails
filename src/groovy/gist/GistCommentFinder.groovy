package gist

class GistCommentFinder {
  List<GistFileEntry> findGistsInFile(File file) {
    def gists = []
    
    GistFileEntry gistFileEntry = null

    file.eachLine(0) { String line, index ->
      if (lineHasStartingTag(line)) {
        String gistId = getGistAttributeFromLine(line, "id")
        
        String isPublicAttribute = getGistAttributeFromLine(line, "public")
        Boolean isPublic = (isPublicAttribute) ? Boolean.parseBoolean(isPublicAttribute) : true
        
        gistFileEntry = new GistFileEntry(
            file: file,
            gistStartLineIndex: index,
            isPublic: isPublic
        )
        
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
  
  String getGistAttributeFromLine(String line, String attribute) {
    String massagedLine = line.substring(line.indexOf('<')) + "</gist>"

    new XmlSlurper().parseText(massagedLine).@"${attribute}"
  }

  boolean lineHasEndingTag(String line) {
    line =~ /<\/gist>/
  }
}
