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

class GistFileService {
  static transactional = false

  GistFileUpdater gistFileUpdater = new GistFileUpdater()
  GistFinder gistFinder = new GistFinder()
  GistUploadService gistUploadService

  def processGistsInDirectories(List<String> dirNames, String username, String password) {
    def gitHubCredentials = new GitHubCredentials(
        username: username,
        password: password
    )
    
    int numGistsProcessed = 0

    dirNames.each { dirName ->
      def gistFileEntries = gistFinder.findGistsInDir(new File(dirName))
      
      numGistsProcessed += gistFileEntries.size()
      
      gistFileEntries.each { gistFileEntry ->
        if (gistFileEntry.id) {
          if (gistUploadService.gistExists(gistFileEntry, gitHubCredentials)) {
            if (gistUploadService.gistContentIsUpdated(gistFileEntry, gitHubCredentials)) {
              gistFileEntry = gistUploadService.updateGistContent(gistFileEntry, gitHubCredentials)
  
              println "Updated Gist with ID ${gistFileEntry.id} at URL ${gistFileEntry.htmlUrl}"  
            } else {
              println "Skipped unchanged Gist with ID ${gistFileEntry.id}"
            }
          } else {
            gistFileEntry = gistUploadService.uploadNewGist(gistFileEntry, gitHubCredentials)
            gistFileUpdater.updateGistFileEntry(gistFileEntry)
            
            println "Re-uploaded missing Gist with new ID ${gistFileEntry.id}"
          }
        } else {
          gistFileEntry = gistUploadService.uploadNewGist(gistFileEntry, gitHubCredentials)
          gistFileUpdater.updateGistFileEntry(gistFileEntry)

          println "Created Gist with ID ${gistFileEntry.id} at URL ${gistFileEntry.htmlUrl}"
        }
      }
    }
    
    println "Processed ${numGistsProcessed} Gists"
  }
}
