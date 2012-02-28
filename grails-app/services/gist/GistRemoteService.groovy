package gist

import org.eclipse.egit.github.core.service.GistService


class GistRemoteService {
  static transactional = false

  def grailsApplication

  GistService createGistService(GitHubCredentials gitHubCredentials) {
    GistService gistService = new GistService()

    gistService.getClient().setCredentials(gitHubCredentials.username, gitHubCredentials.password)

    return gistService
  }

  String getGitHubPassword() {
    grailsApplication.config.gist.github.password
  }

  String getGitHubUsername() {
    grailsApplication.config.gist.github.username
  }
}
