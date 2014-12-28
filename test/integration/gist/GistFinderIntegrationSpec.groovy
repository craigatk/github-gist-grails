package gist
import org.apache.commons.io.FileUtils
import spock.lang.Specification

class GistFinderIntegrationSpec extends Specification {
  final String testDirPath = "target/gists"
  final String testSubDirPath = "${testDirPath}/subdir"
  
  GistFinder gistFinder = new GistFinder()

  final String gistLine1 = "Gist line 1"
  final String gistLine2 = "Gist line 2"
  
  def setup() {
    File testDir = new File(testDirPath)
    FileUtils.deleteDirectory(testDir)
    testDir.mkdirs()
    
    File testSubDir = new File(testSubDirPath)
    FileUtils.deleteDirectory(testSubDir)
    testSubDir.mkdirs()
  }
  
  void whenOneFileWithOneGistShouldFindGist() {
    given:
    File fileWithOneGist = new File(testDirPath, "oneGist.txt")

    writeGistToFile(fileWithOneGist)

    when:
    def gists = gistFinder.findGistsInDir(new File(testDirPath))


    then:
    assert gists?.size() == 1
    
    def gist = gists[0]
    
    assert gist.contentLines == [gistLine1, gistLine2]
  }
  
  void whenOneFileInSubDirWithOneGistShouldFindGist() {
    given:
    File fileWithOneGist = new File(testSubDirPath, "oneGist.txt")

    writeGistToFile(fileWithOneGist)

    when:
    def gists = gistFinder.findGistsInDir(new File(testDirPath))


    then:
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
