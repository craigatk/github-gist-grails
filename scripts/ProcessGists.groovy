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
import grails.build.logging.GrailsConsole

includeTargets << grailsScript('_GrailsBootstrap')

target(main: "Extract new Gists from files and upload them to GitHub") {
  depends(configureProxy, packageApp, classpath, loadApp, configureApp)
  
  def gistConfigService = appCtx.getBean('gistConfigService')
  
  String username = gistConfigService.gitHubUsername

  if (!username) {
    username = GrailsConsole.getInstance().reader.readLine("GitHub username: ")
  }
  
  String password = gistConfigService.gitHubPassword

  if (!password) {
    password = GrailsConsole.getInstance().reader.readLine("GitHub password: ", '*'.toCharacter())  
  }

  def gistFileService = appCtx.getBean('gistFileService')
  gistFileService.processGists(username, password)
}

setDefaultTarget(main)
