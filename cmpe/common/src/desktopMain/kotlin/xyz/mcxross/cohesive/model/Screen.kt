package xyz.mcxross.cohesive.model

import androidx.compose.ui.window.WindowScope
import xyz.mcxross.cohesive.designsystem.mellow.PlatformDropTargetModifier

data class Screen(var scope: WindowScope? = null, var pdtm: PlatformDropTargetModifier)
