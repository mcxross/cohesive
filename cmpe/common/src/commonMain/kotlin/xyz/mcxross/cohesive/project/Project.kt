package xyz.mcxross.cohesive.project

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import xyz.mcxross.cohesive.daemon.createCohesiveProjectListener
import xyz.mcxross.cohesive.designsystem.mellow.File
import xyz.mcxross.cohesive.utils.Log

@OptIn(DelicateCoroutinesApi::class)
class Project(
  private val parentDir: File,
) {
  val name: String
    get() = parentDir.name
  val root: File
    get() = parentDir

  private val COHESIVE_PROJECT = ".cohesive"

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
        //TODO handle modify file
      }
      onModify {
        //TODO handle modify file
      }
      onDelete {
        //TODO handle delete file
      }
      // TODO Use Structured Concurrency
      GlobalScope.launch {
        startListening()
      }
    }
    projectLoaded = true
  }

  fun isLoaded(): Boolean {
    return projectLoaded
  }

  private fun isCohesiveProject(): Boolean {
    val cohesiveDir = java.io.File(parentDir.absolutePath, COHESIVE_PROJECT)
    return cohesiveDir.exists() && cohesiveDir.isDirectory
  }

  private fun makeCohesiveProject(): Boolean {
    return java.io.File(parentDir.absolutePath, COHESIVE_PROJECT).mkdir()
  }

  fun loadCohesiveProject() {

  }

}

fun loadDirAsProject(file: File): Pair<Boolean, Project?> {
  return if (file.isDirectory) {
    val project = Project(file)
    project.isLoaded() to project
  } else {
    false to null
  }
}
