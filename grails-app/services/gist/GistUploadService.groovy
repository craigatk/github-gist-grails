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

import org.eclipse.egit.github.core.Gist
import org.eclipse.egit.github.core.GistFile
import org.eclipse.egit.github.core.client.RequestException
import org.eclipse.egit.github.core.service.GistService

class GistUploadService {
  static transactional = false
  
  def gistRemoteService

  GistFileEntry uploadNewGist(GistFileEntry gistFileEntry, GitHubCredentials gitHubCredentials) {
    GistService gistService = gistRemoteService.createGistService(gitHubCredentials)
    
    Gist gist = createGistObject(gistFileEntry)

    gist = gistService.createGist(gist)
    
    gistFileEntry.id = gist.id
    gistFileEntry.htmlUrl = gist.htmlUrl

    return gistFileEntry
  }
  
  GistFileEntry updateGistContent(GistFileEntry gistFileEntry, GitHubCredentials gitHubCredentials) {
    GistService gistService = gistRemoteService.createGistService(gitHubCredentials)
    
    Gist gist = createGistObject(gistFileEntry)
    
    gist = gistService.updateGist(gist)

    gistFileEntry.htmlUrl = gist.htmlUrl

    return gistFileEntry
  }
  
  protected Gist createGistObject(GistFileEntry gistFileEntry) {
    GistFile gistFile = new GistFile()
    gistFile.content = gistFileEntry.content
    gistFile.filename = gistFileEntry.file.name

    Gist gist = new Gist()
    gist.public = gistFileEntry.isPublic
    gist.setFiles([(gistFileEntry.file.name): gistFile])
    
    if (gistFileEntry.id) {
      gist.id = gistFileEntry.id
    }

    return gist
  }

  boolean gistContentIsUpdated(GistFileEntry gistFileEntry, GitHubCredentials gitHubCredentials) {
    GistService gistService = gistRemoteService.createGistService(gitHubCredentials)
    
    Gist gist = gistService.getGist(gistFileEntry.id)
    
    GistFile gistFile = gist.getFiles()[(gistFileEntry.file.name)]
    
    String gistContent = gistFile.content
    String gistFileEntryContent = gistFileEntry.content

    return (gistContent != gistFileEntryContent)
  }
  
  void deleteGist(String gistId, GitHubCredentials gitHubCredentials) {
    GistService gistService = gistRemoteService.createGistService(gitHubCredentials)
    
    gistService.deleteGist(gistId)
  }

  boolean gistExists(GistFileEntry gistFileEntry, GitHubCredentials gitHubCredentials) {
    GistService gistService = gistRemoteService.createGistService(gitHubCredentials)

    try {
      gistService.getGist(gistFileEntry.id)

      return true
    } catch (RequestException requestException) {
      return false
    }
  }

  boolean validateCredentials(GitHubCredentials gitHubCredentials) {
    GistService gistService = gistRemoteService.createGistService(gitHubCredentials)

    try {
      gistService.starredGists

      return true
    } catch (RequestException re) {
      return false
    }
  }
}
