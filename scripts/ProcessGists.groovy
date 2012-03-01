import grails.build.logging.GrailsConsole

includeTargets << grailsScript('_GrailsBootstrap')

target(main: "Extract new Gists from files and upload them to GitHub") {
  depends(configureProxy, packageApp, classpath, loadApp, configureApp)
  
  def gistRemoteService = appCtx.getBean('gistRemoteService')
  
  String username = gistRemoteService.gitHubUsername

  if (!username || username == "{}") {
    username = GrailsConsole.getInstance().reader.readLine("GitHub username: ")
  }
  
  String password = gistRemoteService.gitHubPassword

  if (!password || password == "{}") {
    password = GrailsConsole.getInstance().reader.readLine("GitHub password: ", '*'.toCharacter())  
  }

  def gistFileService = appCtx.getBean('gistFileService')
  gistFileService.processNewGistsInDirectories(["grails-app", "src", "test"], username, password)
}

setDefaultTarget(main)
