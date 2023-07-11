package xyz.mcxross.cohesive

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
import xyz.mcxross.cohesive.state.Platform
import xyz.mcxross.cohesive.cps.Descriptor
import xyz.mcxross.cohesive.state.WindowState
import xyz.mcxross.cohesive.utils.getPreferredWindowSize
import xyz.mcxross.cohesive.utils.load
import xyz.mcxross.cohesive.utils.readFileToStr
import xyz.mcxross.cohesive.utils.runBlocking
import xyz.mcxross.cohesive.utils.Log
import xyz.mcxross.cohesive.cps.pluginManager
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.mcxross.cohesive.state.Configuration
import xyz.mcxross.cohesive.state.ConfigurationContainer
import xyz.mcxross.cohesive.state.Context

fun runDescriptor() {
  // Run descriptor and mutate obj
  Log.d { "Working on Descriptor..." }
  val time = measureTimeMillis {
    Context.descriptor = Descriptor.run()
    Context.secondaryPlugins = Context.descriptor.getPlugins()
  }
  Log.d { "Done! Took $time ms" }
}

fun runConfig() {
  // Load configs from file and mutate context obj
  fun loadConfig(): Pair<Boolean, Platform> {
    Log.d { "Loading Config" }
    val timeInMillis = measureTimeMillis {
      val configurationContainer: ConfigurationContainer = load(readFileToStr("config.toml"))
      Configuration.asSetFor = configurationContainer
    }
    Log.d { "Loaded in $timeInMillis ms" }

    return Pair(
      true,
      Platform.create(
        chains = Configuration.asSetFor?.platform?.k!!,
        chainPaths = Configuration.asSetFor?.platform?.v!!,
      )
    )
  }

  Log.d { "Working on Config..." }
  val time = measureTimeMillis {
    val config = loadConfig()
    if (config.first) {
      Context.platform = config.second
      Context.isLoadingConfig = false
    }
  }
  Log.d { "Done! Took $time ms" }
}

fun runPlugins() {
  // Load and start plugins and mutate context obj
  Log.d { "Working on Plugins" }
  val time = measureTimeMillis {
    Context.pluginManager = pluginManager {
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
        Context.isLoadingResource = false
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
