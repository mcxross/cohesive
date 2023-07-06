package xyz.mcxross.cohesive.common.project

import xyz.mcxross.cohesive.daemon.createCohesiveProjectListener
import java.io.File

internal class Project(
  val parentDir: File,
) {

  val COHESIVE_PROJECT = ".cohesive"

  var projectLoaded: Boolean = false

  init {
    require(parentDir.isDirectory)
    if (!isCohesiveProject()) {
      makeCohesiveProject()
    } else {
      loadCohesiveProject()
    }

    createCohesiveProjectListener(parentDir.absolutePath) {
      onCreate {
        //TODO handle new file created
      }
      // TODO This runs on the main thread. Use coroutines to dispatch it
      startListening()
    }
    projectLoaded = true
  }

  fun isLoaded(): Boolean {
    return projectLoaded
  }

  private fun isCohesiveProject(): Boolean {
    val cohesiveDir = File(parentDir, COHESIVE_PROJECT)
    return cohesiveDir.exists() && cohesiveDir.isDirectory
  }

  private fun makeCohesiveProject(): Boolean {
    return File(parentDir, COHESIVE_PROJECT).mkdir()
  }

  fun loadCohesiveProject() {

  }

}

fun loadDirAsProject(file: File): Boolean {
  return if (file.isDirectory) {
    Project(file).isLoaded()
  } else {
    false
  }
}
