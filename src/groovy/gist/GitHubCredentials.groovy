package gist


class GitHubCredentials {
  String password
  String username

  boolean equals(Object o) {
    (this.username == o.username) && (this.password == o.password)
  }
}
