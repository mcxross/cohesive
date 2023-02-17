package com.mcxross.cohesive.common.frontend.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.WindowState
import com.mcxross.cohesive.common.Daemon
import com.mcxross.cohesive.common.frontend.impl.ui.view.View
import com.mcxross.cohesive.common.utils.Log
import com.mcxross.cohesive.mellow.HomeFolder
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.concurrent.fixedRateTimer

typealias WindowState = StatesHolder.Window

@Serializable
class StatesHolder {

  companion object {
    /*private val windowPOJOKStore: KStore<WindowPOJO> =
      storeOf(System.getProperty("user.home") + "/AppData/Local/McXross/Cohesive")*/

    suspend fun init(schedule: () -> Unit) {
      /*val windowPOJO = deserialize()
      Window.state = windowPOJO?.state ?: WindowState()
      Window.view = windowPOJO?.view ?: View.EXPLORER
      Window.isMainWindowOpen = windowPOJO?.isMainWindowOpen ?: true
      Window.isImportAccountOpen = windowPOJO?.isImportAccountOpen ?: false
      Window.isCreateAccountOpen = windowPOJO?.isCreateAccountOpen ?: false
      Window.isOpenDialogOpen = windowPOJO?.isOpenDialogOpen ?: false
      Window.isStoreWindowOpen = windowPOJO?.isStoreWindowOpen ?: true
      Window.isPreAvail = windowPOJO?.isPreAvail ?: false
      Window.isDelayClose = windowPOJO?.isDelayClose ?: true
      Window.currentProjectFile = windowPOJO?.currentProjectFile ?: ""*/
      fixedRateTimer("windowPOJOKStore", false, 0, 30000) {
        Daemon.scope.launch {
          serialize()
          Log.d {
            "windowPOJOKStore saved"
          }
        }
      }

    }

    suspend fun serialize() {
      /*windowPOJOKStore.set(
        WindowPOJO(
          Window.state,
          Window.view,
          Window.isMainWindowOpen,
          Window.isImportAccountOpen,
          Window.isCreateAccountOpen,
          Window.isOpenDialogOpen,
          Window.isStoreWindowOpen,
          Window.isPreAvail,
          Window.isDelayClose,
          Window.currentProjectFile.toString(),
        ),
      )*/
    }

    fun serializeStash() {}
    /*private suspend fun deserialize(): WindowPOJO {
      return windowPOJOKStore.get()
    }*/

    fun deserializeUnStash() {

    }
  }

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
  }
}

@Serializable
data class WindowPOJO(
  val state: WindowState,
  val view: View,
  val isMainWindowOpen: Boolean,
  val isImportAccountOpen: Boolean,
  val isCreateAccountOpen: Boolean,
  val isOpenDialogOpen: Boolean,
  val isStoreWindowOpen: Boolean,
  val isPreAvail: Boolean,
  val isDelayClose: Boolean,
  val currentProjectFile: String,
)
