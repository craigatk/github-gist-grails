package gist

class GistFileEntry {
  String id
  List<String> contentLines = []
  
  String getContent() {
    contentLines.join("\n")
  }
}
