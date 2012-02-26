package gist

import org.junit.Test

class GistUploadServiceSystemTests {
  GistUploadService gistUploadService

  @Test
  void shouldUploadNewGist() {
    GistFileEntry gistFileEntry = new GistFileEntry(
        contentLines: ["Some test content", "Line 2"],
        file: new File("Hello.groovy")
    )

    def updatedGistFileEntry = gistUploadService.uploadNewGist(gistFileEntry, null)

    assert updatedGistFileEntry.id
  }
}
