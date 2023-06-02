package xyz.mcxross.cohesive.common.frontend.api.ui.screen

import androidx.compose.runtime.Composable
import xyz.mcxross.cohesive.common.frontend.api.ui.view.IView

interface IMain : IView {

  @Composable
  fun WindowListMenuButton()

  @Composable
  fun SwitchView()

  @Composable
  fun ClusterMenu()

  @Composable
  fun TitleMenuBar()

  @Composable
  fun ExplorerView()

  @Composable
  fun WalletView()

  @Composable
  fun EditorView()
}
