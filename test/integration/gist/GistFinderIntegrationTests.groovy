package gist

import org.apache.commons.io.FileUtils
import org.junit.Before
import org.junit.Test

class GistFinderIntegrationTests {
  final String testDirPath = "target/gists"
  final String testSubDirPath = "${testDirPath}/subdir"
  
  GistFinder gistFinder = new GistFinder()

  final String gistLine1 = "Gist line 1"
  final String gistLine2 = "Gist line 2"
  
  @Before
  void createTestDirs() {
    File testDir = new File(testDirPath)
    FileUtils.deleteDirectory(testDir)
    testDir.mkdirs()
    
    File testSubDir = new File(testSubDirPath)
    FileUtils.deleteDirectory(testSubDir)
    testSubDir.mkdirs()
  }
  
  @Test
  void whenOneFileWithOneGistShouldFindGist() {
    File fileWithOneGist = new File(testDirPath, "oneGist.txt")

    writeGistToFile(fileWithOneGist)
    
    def gists = gistFinder.findGistsInDir(new File(testDirPath))
    
    assert gists?.size() == 1
    
    def gist = gists[0]
    
    assert gist.contentLines == [gistLine1, gistLine2]
  }
  
  @Test
  void whenOneFileInSubDirWithOneGistShouldFindGist() {
    File fileWithOneGist = new File(testSubDirPath, "oneGist.txt")

    writeGistToFile(fileWithOneGist)

    def gists = gistFinder.findGistsInDir(new File(testDirPath))

    assert gists?.size() == 1

    def gist = gists[0]

    assert gist.contentLines == [gistLine1, gistLine2]
  }

  private void writeGistToFile(File file) {
    String fileText = """
Some text
// <gist>
${gistLine1}
${gistLine2}
// </gist>
"""

    file.text = fileText
  }
}
