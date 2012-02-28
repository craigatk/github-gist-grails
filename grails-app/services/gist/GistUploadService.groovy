package gist

import org.eclipse.egit.github.core.Gist
import org.eclipse.egit.github.core.GistFile
import org.eclipse.egit.github.core.service.GistService

class GistUploadService {
  static transactional = false
  
  def gistRemoteService

  GistFileEntry uploadNewGist(GistFileEntry gistFileEntry, GitHubCredentials gitHubCredentials) {
    GistService gistService = gistRemoteService.createGistService(gitHubCredentials)
    
    Gist gist = createGistObject(gistFileEntry)

    gist = gistService.createGist(gist)
    
    gistFileEntry.id = gist.id

    return gistFileEntry
  }
  
  GistFileEntry updateGistContent(GistFileEntry gistFileEntry, GitHubCredentials gitHubCredentials) {
    GistService gistService = gistRemoteService.createGistService(gitHubCredentials)
    
    Gist gist = createGistObject(gistFileEntry)
    
    gistService.updateGist(gist)

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
}
