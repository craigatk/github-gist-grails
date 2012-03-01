/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
