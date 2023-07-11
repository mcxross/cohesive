package xyz.mcxross.cohesive.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.WindowState
import xyz.mcxross.cohesive.model.View
import xyz.mcxross.cohesive.designsystem.mellow.HomeFolder
import kotlinx.serialization.Serializable
import xyz.mcxross.cohesive.project.Project

typealias WindowState = StatesHolder.Window

@Serializable
class StatesHolder {
  object Window {
    var state: WindowState = WindowState()
    var view: View by mutableStateOf(View.EXPLORER)
    var isMainWindowOpen: Boolean by mutableStateOf(true)
    var isImportAccountOpen: Boolean by mutableStateOf(false)
    var isCreateAccountOpen: Boolean by mutableStateOf(false)
    var isOpenDialogOpen: Boolean by mutableStateOf(false)
    var isStoreWindowOpen: Boolean by mutableStateOf(true)
    var isPreAvail: Boolean by mutableStateOf(false)
    var isDelayClose: Boolean by mutableStateOf(true)
    var currentProjectFile: Any? by mutableStateOf(HomeFolder.absolutePath)
    var currentProject: Project? by mutableStateOf(null)
  }
}

