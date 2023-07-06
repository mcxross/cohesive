package xyz.mcxross.cohesive.common.frontend.impl.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPlacement
import xyz.mcxross.cohesive.common.Local
import xyz.mcxross.cohesive.common.ds.tree.TreeNodeIterators
import xyz.mcxross.cohesive.common.ds.tree.tree
import xyz.mcxross.cohesive.common.frontend.api.ui.view.CohesiveView
import xyz.mcxross.cohesive.common.frontend.impl.ui.view.View
import xyz.mcxross.cohesive.common.frontend.utils.WindowState
import xyz.mcxross.cohesive.common.frontend.utils.isDirectory
import xyz.mcxross.cohesive.common.project.loadDirAsProject
import xyz.mcxross.cohesive.common.utils.isBinaryFile
import xyz.mcxross.cohesive.csp.annotation.Cohesive
import xyz.mcxross.cohesive.mellow.Dialog
import xyz.mcxross.cohesive.mellow.EditorComposite
import xyz.mcxross.cohesive.mellow.EditorSimple
import xyz.mcxross.cohesive.mellow.File
import xyz.mcxross.cohesive.mellow.FileTree
import xyz.mcxross.cohesive.mellow.FileTreeModel
import xyz.mcxross.cohesive.mellow.HomeFolder
import xyz.mcxross.cohesive.mellow.Menu
import xyz.mcxross.cohesive.mellow.MenuContainer
import xyz.mcxross.cohesive.mellow.MenuInterface
import xyz.mcxross.cohesive.mellow.MenuItem
import xyz.mcxross.cohesive.mellow.TabbedButtonIcon
import xyz.mcxross.cohesive.mellow.TopBar
import xyz.mcxross.cohesive.mellow.WindowScaffold
import java.util.*

open class MainScreen {

  private lateinit var cohesiveView: CohesiveView
  private lateinit var cohesive: Cohesive

  @Composable
  protected fun mainMenu(): MenuContainer =
    MenuContainer(
      icon = painterResource("listMenu_dark.svg"),
      contentDescription = "Main Menu",
      onClick = {},
      menuTree = tree(MenuItem(text = "Root"), TreeNodeIterators.LevelOrder) {
        child(MenuItem(text = "New")) {
          child(MenuItem(text = "Project")) {
            child(MenuItem(text = "Java"))
            child(MenuItem(text = "Kotlin"))
          }
          child(MenuItem(text = "Wallet"))
        }
        child(
          MenuItem(
            icon = painterResource("menu-open_dark.svg"),
            text = "Open",
            menuInterface =
            object : MenuInterface {
              override fun onClick() {
                WindowState.isOpenDialogOpen = true
              }

              override fun onHover(onEnter: Boolean) {}
            },
          ),
        )
        child(MenuItem(text = "Save"))
        child(MenuItem(text = "Save As"))
        child(MenuItem(text = "Restart"))
        child(
          MenuItem(
            text = "Exit",
            menuInterface = object : MenuInterface {
              override fun onClick() {
                WindowState.isMainWindowOpen = false
              }

              override fun onHover(onEnter: Boolean) {
                TODO("Not yet implemented")
              }
            },
          ),
        )
      },
    )

  @Composable
  fun menus(): List<MenuContainer> = listOf(mainMenu())

  @Composable
  protected fun TitleMenuBar() {
    Column(
      modifier = Modifier.fillMaxWidth().wrapContentHeight(),
    ) {
      var restore by remember { mutableStateOf(true) }
      Local.LocalScreen.current.scope?.TopBar(
        onClose = { WindowState.isMainWindowOpen = false },
        onRestore = {
          if (restore) {
            WindowState.state.placement = WindowPlacement.Maximized
            restore = false
          } else {
            WindowState.state.placement = WindowPlacement.Floating
            restore = true
          }
        },
        onMinimize = { WindowState.state.isMinimized = !WindowState.state.isMinimized },
        icon = painterResource("ic_launcher.png"),
        menuContent = {
          Box(
            modifier = Modifier.offset(x = 5.dp).align(Alignment.CenterVertically),
          ) {
            menus().forEachIndexed { index, menu ->
              Menu(index = index, menuContainer = menu) {}
            }
          }
        },
        restoreIcon =
        if (restore) painterResource("maximize_dark.svg")
        else painterResource("restore_dark.svg"),
      ) {
        Text(
          "${cohesive.platform}  —",
          fontSize = 11.sp,
          modifier = Modifier.padding(end = 10.dp).align(Alignment.CenterVertically),
        )
        ClusterMenu()
        SwitchView()
      }

      Divider()
    }
  }

  @Composable
  protected fun ClusterMenu() {
    var expanded by remember { mutableStateOf(false) }
    val suggestions =
      cohesive.nets.map {
        it.k.replaceFirstChar { c ->
          if (c.isLowerCase()) c.titlecase(Locale.ROOT) else c.toString()
        }
      }
    var setCluster by remember { mutableStateOf(suggestions[0]) }
    Box {
      Button(
        modifier = Modifier.defaultMinSize(minWidth = 150.dp),
        border = BorderStroke(1.dp, MaterialTheme.colors.surface),
        colors =
        ButtonDefaults.buttonColors(
          backgroundColor = Color.Transparent,
          contentColor = contentColorFor(MaterialTheme.colors.surface),
        ),
        onClick = { expanded = !expanded },
      ) {
        Icon(
          painterResource("clusterConnectedDot_dark.svg"),
          contentDescription = "",
          modifier = Modifier.align(Alignment.CenterVertically),
          tint = Color.Green,
        )
        Text(setCluster, fontSize = 11.sp)
        Icon(
          imageVector = Icons.Filled.ArrowDropDown,
          contentDescription = null,
        )
      }
      DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier =
        Modifier.defaultMinSize(minWidth = 150.dp)
          .border(border = BorderStroke(2.dp, Color(0xFF4D5051)), shape = RectangleShape),
      ) {
        suggestions.forEach { label ->
          DropdownMenuItem(
            modifier = Modifier.height(24.dp),
            onClick = {
              if (label !== setCluster) {
                setCluster = label
              }
              expanded = false
            },
          ) {
            Text(text = label, fontSize = 12.sp)
          }
        }
      }
    }
  }

  @Composable
  protected fun SwitchView() {

    val (selected, setSelected) = remember {
      mutableStateOf(0)
    }

    when (selected) {
      0 -> {
        WindowState.view = View.EXPLORER
      }

      1 -> {
        WindowState.view = View.WALLET
      }

      2 -> {
        WindowState.view = View.EDITOR
      }
    }

    Box(modifier = Modifier.offset(x = 10.dp).padding(top = 2.dp, bottom = 1.dp)) {
      TabbedButtonIcon(
        items = listOf(
          Pair("icon/boxes.svg", "Explorer"),
          Pair("icon/wallet.svg", "Wallet"),
          Pair("icon/code.svg", "Editor"),
        ),
        selectedItemIndex = selected,
        onClick = setSelected,
      )
    }

  }

  @Composable
  protected fun Editor() {
    if ((WindowState.currentProjectFile as File).isDirectory) {
      if (loadDirAsProject(java.io.File((WindowState.currentProjectFile as File).absolutePath))) {
        EditorComposite(
          file = WindowState.currentProjectFile as File,
        )
      } else {
        // TODO Handle dir not loaded
      }
    } else {
      if (!isBinaryFile(java.io.File((WindowState.currentProjectFile as File).absolutePath))) {
        EditorSimple(
          file = WindowState.currentProjectFile as File,
        )
      }
    }
    /*EditorEmpty(text = "Drag and Drop Files to Open")*/
  }

  @Composable
  protected fun Composition() {
    Crossfade(WindowState.view) { view ->
      when (view) {
        View.EXPLORER -> cohesiveView.Explorer()
        View.WALLET -> cohesiveView.Wallet()
        View.EDITOR -> Editor()
        else -> {}
      }

      if (WindowState.isOpenDialogOpen) {
        FileExplorerDialog()
      }
    }
  }

  @Composable
  protected fun FileExplorerDialog() {
    var file by remember { mutableStateOf(HomeFolder) }
    Dialog(
      onClose = { WindowState.isOpenDialogOpen = !WindowState.isOpenDialogOpen },
      text = "Open File or Project",
      negativeText = "Cancel",
      onNegative = { WindowState.isOpenDialogOpen = !WindowState.isOpenDialogOpen },
      positiveText = "Ok",
      onPositive = {
        WindowState.currentProjectFile = file
        WindowState.view = View.EDITOR
        WindowState.isOpenDialogOpen = !WindowState.isOpenDialogOpen
      },
      width = 450.dp,
      height = 450.dp,
    ) {
      FileTree(model = FileTreeModel(root = HomeFolder)) { file = it }
    }
  }

  @Composable
  infix fun Ingest(cohesiveView: CohesiveView) {
    this.cohesiveView = cohesiveView
    this.cohesiveView::class.annotations.forEach {
      when (it) {
        is Cohesive -> {
          this.cohesive = it
        }
      }
    }
    WindowScaffold(
      topBar = { TitleMenuBar() },
      onDragStarted = { uris, _ -> uris.isNotEmpty() },
      onDragEntered = {},
      onDropped = { uris, _ ->
        if (isDirectory(uris[0].path)) {
          WindowState.currentProjectFile = uris[0].path
          WindowState.view = View.EDITOR
        }
        true
      },
    ) {
      Composition()
    }
  }
}
