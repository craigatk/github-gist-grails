import org.codehaus.groovy.grails.test.support.GrailsTestMode
import org.codehaus.groovy.grails.test.junit4.JUnit4GrailsTestType

eventAllTestsStart = {
  phasesToRun << "system"
}

def systemTestName = "system"
def systemTestDirectory = "system"
def systemTestMode = new GrailsTestMode(autowire: true, wrapInTransaction: true, wrapInRequestEnvironment: false)
def systemTestType = new JUnit4GrailsTestType(systemTestName, systemTestDirectory, systemTestMode)

systemTests = [systemTestType]

systemTestPhasePreparation = {
  integrationTestPhasePreparation()
}
systemTestPhaseCleanUp = {
  integrationTestPhaseCleanUp()
}