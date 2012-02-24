package gist


public enum GistStatus {
  NEW(0),
  UPLOADED(1),
  UPLOAD_ERROR(2)

  final Long id
  
  GistStatus(Long id) {
    this.id = id
  }
}