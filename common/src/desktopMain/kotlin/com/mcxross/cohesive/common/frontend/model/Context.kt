package com.mcxross.cohesive.common.frontend.model

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.WindowScope
import com.mcxross.cohesive.mellow.PlatformDropTargetModifier

object Local {
  var LocalContext: ProvidableCompositionLocal<Context> =
    staticCompositionLocalOf { error("No Context provided") }
}

actual class Context {

  var windowScope: WindowScope? = null
  var environment: Environment? = null
  var platformDropTargetModifier: PlatformDropTargetModifier? = null
}
