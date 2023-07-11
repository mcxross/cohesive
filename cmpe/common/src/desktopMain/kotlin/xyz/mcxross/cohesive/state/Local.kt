package xyz.mcxross.cohesive.state

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import xyz.mcxross.cohesive.model.Screen

object Local {
  var LocalScreen: ProvidableCompositionLocal<Screen> = staticCompositionLocalOf {
    error("No Screen provided")
  }
}
