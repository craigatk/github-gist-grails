package gist


class GistFileUpdater {
  def updateGistFileEntry(GistFileEntry gistFileEntry) {
    File file = gistFileEntry.file
    
    String fileText = file.text
    
    file.withWriter {writer ->
      fileText.eachLine(0) { String line, lineNumber ->
        if (lineNumber == gistFileEntry.gistStartLineNumber) {
          writer.writeLine(line.replace("""<gist>""", """<gist id="${gistFileEntry.id}">"""))
        } else {
          writer.writeLine(line)
        }
      }
    }
  }
}
