package gist

import org.eclipse.egit.github.core.Gist
import org.eclipse.egit.github.core.GistFile
import org.eclipse.egit.github.core.service.GistService

class GistUploadService {
  static transactional = false
  
  def grailsApplication
  def gistRemoteService

  GistFileEntry uploadNewGist(GistFileEntry gistFileEntry, String password) {
    GistFile gistFile = new GistFile()
    gistFile.content = gistFileEntry.content
    gistFile.filename = gistFileEntry.file.name
    
    Gist gist = new Gist()
    gist.public = gistFileEntry.isPublic
    gist.setFiles([(gistFileEntry.file.name): gistFile])

    GistService gistService = gistRemoteService.createGistService(password)

    gist = gistService.createGist(gist)
    
    gistFileEntry.id = gist.id

    return gistFileEntry
  }
}
