package com.mcxross.cohesive.desktop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.*
import com.mcxross.cohesive.common.Cohesive
import com.mcxross.cohesive.common.Context
import com.mcxross.cohesive.common.Local.LocalScreen
import com.mcxross.cohesive.common.Screen
import com.mcxross.cohesive.common.frontend.impl.ui.screen.MainScreen
import com.mcxross.cohesive.common.frontend.impl.ui.screen.StoreScreen
import com.mcxross.cohesive.common.frontend.impl.ui.view.splash.SplashScreen
import com.mcxross.cohesive.common.frontend.utils.WindowState
import com.mcxross.cohesive.common.frontend.utils.getPreferredWindowSize
import com.mcxross.cohesive.common.utils.Log.e
import com.mcxross.cohesive.mellow.PlatformDropTargetModifier

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
    if (Context.isLoadingResource) {

      Window(
        onCloseRequest = ::exitApplication,
        undecorated = true,
        resizable = false,
        state =
          WindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            size = getPreferredWindowSize(400, 300),
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

            Context.pluginManager.getCohesiveView().let { View ->
              BrewScreenCompositionLocal(
                windowScope = this,
                pdtm = dropParent,
              ) {
                if (View != null) {
                  MainScreen() Ingest View
                } else {
                  e { "View is null. Ensure you've defined View and built with CSP" }
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
              WindowState(
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
