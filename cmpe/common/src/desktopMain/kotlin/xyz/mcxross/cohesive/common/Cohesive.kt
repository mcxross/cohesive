package xyz.mcxross.cohesive.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import xyz.mcxross.cohesive.common.frontend.model.Platform
import xyz.mcxross.cohesive.common.frontend.model.onnet.Descriptor
import xyz.mcxross.cohesive.common.frontend.utils.WindowState
import xyz.mcxross.cohesive.common.frontend.utils.getPreferredWindowSize
import xyz.mcxross.cohesive.common.frontend.utils.load
import xyz.mcxross.cohesive.common.frontend.utils.readFileToStr
import xyz.mcxross.cohesive.common.frontend.utils.runBlocking
import xyz.mcxross.cohesive.common.utils.Log
import xyz.mcxross.cohesive.cps.pluginManager
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun runDescriptor() {
  // Run descriptor and mutate obj
  Log.d { "Working on Descriptor..." }
  val time = measureTimeMillis {
    xyz.mcxross.cohesive.common.Context.descriptor = Descriptor.run()
    xyz.mcxross.cohesive.common.Context.secondaryPlugins = xyz.mcxross.cohesive.common.Context.descriptor.getPlugins()
  }
  Log.d { "Done! Took $time ms" }
}

fun runConfig() {
  // Load configs from file and mutate context obj
  fun loadConfig(): Pair<Boolean, Platform> {
    Log.d { "Loading Config" }
    val timeInMillis = measureTimeMillis {
      val configurationContainer: ConfigurationContainer = load(readFileToStr("config.toml"))
      xyz.mcxross.cohesive.common.Context.configuration.asSetFor = configurationContainer
    }
    Log.d { "Loaded in $timeInMillis ms" }

    return Pair(
      true,
      Platform.create(
        chains = xyz.mcxross.cohesive.common.Context.configuration.asSetFor?.platform?.k!!,
        chainPaths = xyz.mcxross.cohesive.common.Context.configuration.asSetFor?.platform?.v!!,
      )
    )
  }

  Log.d { "Working on Config..." }
  val time = measureTimeMillis {
    val config = loadConfig()
    if (config.first) {
      xyz.mcxross.cohesive.common.Context.platform = config.second
      xyz.mcxross.cohesive.common.Context.isLoadingConfig = false
    }
  }
  Log.d { "Done! Took $time ms" }
}

fun runPlugins() {
  // Load and start plugins and mutate context obj
  Log.d { "Working on Plugins" }
  val time = measureTimeMillis {
    xyz.mcxross.cohesive.common.Context.pluginManager = pluginManager {
      loadPlugins()
      startPlugins()
    }
  }
  Log.d { "Done! Took $time ms" }
}

object Cohesive {

  private lateinit var applicationScope: ApplicationScope

  init {
    // Must initialize logging before anything else
    Log.init()

    Log.i { "Cohesive starting..." }
  }

  /** Builds [Context] that's available for global access */
  fun run(
    content: @Composable (ApplicationScope.() -> Unit),
  ) = application {
    applicationScope = this

    // The MainWindow State Must be remembered
    WindowState.state =
      rememberWindowState(
        placement = WindowPlacement.Floating,
        position = WindowPosition.Aligned(Alignment.Center),
        size = getPreferredWindowSize(800, 1000),
      )

    // This is an expensive op in terms of time, and compute and therefore must run once
    val loadResources by rememberUpdatedState {
      Log.i { "Loading Resources" }
      val timeLoadingResources = measureTimeMillis {
        val runs = listOf(::runDescriptor, ::runConfig, ::runPlugins)
        runBlocking {
          for (i in 0..2) {
            launch { runs[i].call() }
          }
        }
        xyz.mcxross.cohesive.common.Context.isLoadingResource = false
      }
      Log.i { "Resources loaded in $timeLoadingResources ms" }
    }

    // Defer loading res' and continue
    LaunchedEffect(true) {
      delay(3000)
      loadResources()
    }
    content()
  }
}
