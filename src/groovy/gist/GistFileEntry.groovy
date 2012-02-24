package gist

class GistFileEntry {
  String id
  List<String> contentLines = []
  Integer gistStartLineIndex
  File file
  GistStatus status

  String getContent() {
    contentLines.join("\n")
  }
  
  String toString() {
    content
  }
}
