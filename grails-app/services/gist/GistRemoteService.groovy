package gist

import org.eclipse.egit.github.core.service.GistService


class GistRemoteService {
  static transactional = false

  def grailsApplication

  GistService createGistService(String password) {
    if (!password) {
      password = grailsApplication.config.gist.github.password
    }

    GistService gistService = new GistService()

    gistService.getClient().setCredentials(gitHubUsername, password)

    return gistService
  }

  String getGitHubUsername() {
    grailsApplication.config.gist.github.username
  }
}
