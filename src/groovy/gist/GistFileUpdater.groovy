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

class GistFileUpdater {
  def updateGistFileEntry(GistFileEntry gistFileEntry) {
    File file = gistFileEntry.file
    
    String fileText = file.text
    
    file.withWriter {writer ->
      fileText.eachLine(0) { String line, lineNumber ->
        if (lineNumber == gistFileEntry.gistStartLineIndex) {
          String newGistLine = "<gist"
          newGistLine += " id=\"${gistFileEntry.id}\""
          if (!gistFileEntry.isPublic) {
            newGistLine += " public=\"false\""
          }
          newGistLine += ">"
          
          writer.writeLine(line.replaceFirst("<gist.*>", newGistLine))
        } else {
          writer.writeLine(line)
        }
      }
    }
  }
}
