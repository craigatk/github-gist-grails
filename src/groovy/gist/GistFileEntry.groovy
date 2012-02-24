package gist

class GistFileEntry {
  String id
  List<String> contentLines = []
  File file
  Integer gistStartLineNumber

  String getContent() {
    contentLines.join("\n")
  }
  
  String toString() {
    content
  }
}
