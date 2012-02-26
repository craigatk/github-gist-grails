import org.codehaus.groovy.grails.cli.CommandLineHelper
import grails.build.logging.GrailsConsole

includeTargets << grailsScript('_GrailsBootstrap')

target(main: "Extract new Gists from files and upload them to GitHub") {
  depends(configureProxy, packageApp, classpath, loadApp, configureApp)

  def password = GrailsConsole.getInstance().reader.readLine("GitHub password: ", '*'.toCharacter())

  def gistFileService = appCtx.getBean('gistFileService')
  gistFileService.serviceMethod(password)
}

setDefaultTarget(main)
