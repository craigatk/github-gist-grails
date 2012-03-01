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

class GistCommentFinder {
  List<GistFileEntry> findGistsInFile(File file) {
    def gists = []
    
    GistFileEntry gistFileEntry = null

    file.eachLine(0) { String line, index ->
      if (lineHasStartingTag(line)) {
        String gistId = getGistAttributeFromLine(line, "id")
        
        String isPublicAttribute = getGistAttributeFromLine(line, "public")
        Boolean isPublic = (isPublicAttribute) ? Boolean.parseBoolean(isPublicAttribute) : true
        
        gistFileEntry = new GistFileEntry(
            file: file,
            gistStartLineIndex: index,
            isPublic: isPublic
        )
        
        if (gistId) {
          gistFileEntry.id = gistId
        }
      } else if (lineHasEndingTag(line)) {
        gists << gistFileEntry
        
        gistFileEntry = null
      } else if (gistFileEntry) {
        gistFileEntry.contentLines << line
      }
    }
    
    return gists
  }

  boolean lineHasStartingTag(String line) {
    line =~ /<gist.*>/
  }
  
  String getGistAttributeFromLine(String line, String attribute) {
    String massagedLine = line.substring(line.indexOf('<')) + "</gist>"

    new XmlSlurper().parseText(massagedLine).@"${attribute}"
  }

  boolean lineHasEndingTag(String line) {
    line =~ /<\/gist>/
  }
}
