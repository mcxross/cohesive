package com.mcxross.cohesive.common.frontend.model

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.WindowScope

object Local {
    var LocalContext: ProvidableCompositionLocal<com.mcxross.cohesive.common.frontend.model.Context> = staticCompositionLocalOf { error("No Context provided") }
}

actual class Context {

    var windowScope: WindowScope? = null
    var environment: com.mcxross.cohesive.common.frontend.model.Environment? = null
}