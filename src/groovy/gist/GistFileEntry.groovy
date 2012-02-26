package gist

class GistFileEntry {
  String id
  List<String> contentLines = []
  File file
  Integer gistStartLineIndex
  Boolean isPublic = true
  GistStatus status = GistStatus.NEW

  String getContent() {
    contentLines.join("\n")
  }
  
  String toString() {
    content
  }
}
