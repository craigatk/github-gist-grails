package gist

class GistCommentFinder {
  List<GistFileEntry> findGistsInText(String text) {
    def gists = []

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
