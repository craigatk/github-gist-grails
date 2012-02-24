includeTargets << grailsScript('_GrailsBootstrap')

target(main: "Extract new Gists from files and upload them to GitHub") {
  depends(configureProxy, packageApp, classpath, loadApp, configureApp)

  def gistFileService = appCtx.getBean('gistFileService')
  gistFileService.serviceMethod()
}

setDefaultTarget(main)
