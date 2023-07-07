package xyz.mcxross.cohesive.common.project

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import xyz.mcxross.cohesive.common.utils.Log
import xyz.mcxross.cohesive.daemon.createCohesiveProjectListener
import java.io.File

@OptIn(DelicateCoroutinesApi::class)
internal class Project(
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
        //TODO handle new file created
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
