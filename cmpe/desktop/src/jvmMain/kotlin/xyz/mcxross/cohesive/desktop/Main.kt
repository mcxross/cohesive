package xyz.mcxross.cohesive.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowScope
import xyz.mcxross.cohesive.common.Cohesive
import xyz.mcxross.cohesive.common.Local.LocalScreen
import xyz.mcxross.cohesive.common.Screen
import xyz.mcxross.cohesive.common.frontend.impl.ui.screen.MainScreen
import xyz.mcxross.cohesive.common.frontend.impl.ui.screen.StoreScreen
import xyz.mcxross.cohesive.common.frontend.impl.ui.view.splash.SplashScreen
import xyz.mcxross.cohesive.common.frontend.utils.WindowState
import xyz.mcxross.cohesive.common.frontend.utils.getPreferredWindowSize
import xyz.mcxross.cohesive.common.utils.Log.d
import xyz.mcxross.cohesive.common.utils.splashScreenSize
import xyz.mcxross.cohesive.designsystem.mellow.PlatformDropTargetModifier

@Composable
fun BrewScreenCompositionLocal(
  windowScope: WindowScope,
  pdtm: PlatformDropTargetModifier,
  content: @Composable () -> Unit,
) {
  val screen = Screen(windowScope, pdtm)
  screen.pdtm = pdtm
  LocalScreen = compositionLocalOf { screen }
  CompositionLocalProvider(LocalScreen provides screen) { content() }
}

/** This is where the magic happens, the main function is the entry point to the application. */
fun main() =
  Cohesive.run {
    if (xyz.mcxross.cohesive.common.Context.isLoadingResource) {
      Window(
        onCloseRequest = ::exitApplication,
        undecorated = true,
        resizable = false,
        icon = BitmapPainter(useResource("ic_launcher.png", ::loadImageBitmap)),
        state =
          androidx.compose.ui.window.WindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            size = getPreferredWindowSize(splashScreenSize().width, splashScreenSize().height),
          ),
      ) {
        SplashScreen()
      }
    } else {

      if (WindowState.isPreAvail) {

        if (WindowState.isMainWindowOpen) {

          Window(
            onCloseRequest = ::exitApplication,
            undecorated = true,
            state = WindowState.state,
            icon = BitmapPainter(useResource("ic_launcher.png", ::loadImageBitmap)),
          ) {
            val density = LocalDensity.current.density
            val dropParent =
              remember(density) {
                PlatformDropTargetModifier(
                  density = density,
                  window = window,
                )
              }
            xyz.mcxross.cohesive.common.Context.pluginManager.getCohesiveView().let { View ->
              BrewScreenCompositionLocal(
                windowScope = this,
                pdtm = dropParent,
              ) {
                if (View != null) {
                  MainScreen() Ingest View
                } else {
                  d {
                    "View is null. Ensure you've defined View and built with CSP. \n Defaulting to EditorView"
                  }
                }
              }
            }
          }
        }
      }

      if (WindowState.isDelayClose) {

        if (WindowState.isStoreWindowOpen) {

          Window(
            onCloseRequest = ::exitApplication,
            undecorated = true,
            resizable = false,
            state =
            androidx.compose.ui.window.WindowState(
                position = WindowPosition.Aligned(Alignment.Center),
              ),
            icon = BitmapPainter(useResource("ic_launcher.png", ::loadImageBitmap)),
          ) {
            val density = LocalDensity.current.density
            val dropParent =
              remember(density) {
                PlatformDropTargetModifier(
                  density = density,
                  window = window,
                )
              }
            BrewScreenCompositionLocal(
              windowScope = this,
              pdtm = dropParent,
            ) {
              StoreScreen()
            }
          }
        }
      }
    }
  }
