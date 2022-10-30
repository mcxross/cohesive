package com.mcxross.cohesive.common

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
import com.mcxross.cohesive.common.frontend.model.Platform
import com.mcxross.cohesive.common.frontend.model.onnet.Descriptor
import com.mcxross.cohesive.common.frontend.utils.WindowState
import com.mcxross.cohesive.common.frontend.utils.getPreferredWindowSize
import com.mcxross.cohesive.common.frontend.utils.load
import com.mcxross.cohesive.common.frontend.utils.readFileToStr
import com.mcxross.cohesive.common.frontend.utils.runBlocking
import com.mcxross.cohesive.common.utils.Log
import com.mcxross.cohesive.cps.pluginManager
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun runDescriptor() {
  // Run descriptor and mutate obj
  Log.d { "Working on descriptor..." }
  val time = measureTimeMillis {
    Context.descriptor = Descriptor.run()
    Context.secondaryPlugin = Context.descriptor.getPlugins()
  }
  Log.d { "Done! Took $time ms" }
}

fun runConfig() {

  // Load configs from file and mutate context obj

  fun loadConfig(): Pair<Boolean, Platform> {
    Log.i { "Loading config" }
    var configuration: com.mcxross.cohesive.common.frontend.model.Configuration
    val timeInMillis = measureTimeMillis { configuration = load(readFileToStr("config.toml")) }
    val chains = ArrayList<String>()
    val chainPaths = ArrayList<String>()
    configuration.chain.clusterKey.forEachIndexed { index, clusterKey ->
      chains.add(clusterKey.toString())
      chainPaths.add(configuration.chain.clusterValue[index])
    }
    Log.i { "Config loaded in $timeInMillis ms" }

    return Pair(
      true,
      Platform.create(
        chains = chains,
        chainPaths = chainPaths,
      )
    )
  }

  Log.d { "Working on config..." }
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
  Log.d { "Working on plugins" }
  val time = measureTimeMillis {
    Context.pluginManager.loadPlugins()
    Context.pluginManager.startPlugins()
  }
  Log.d { "Done Took $time ms" }
}

object Cohesive {

  private val pluginManager = pluginManager {}
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
    Context.pluginManager = pluginManager

    // The MainWindow State Must be remembered
    WindowState.state =
      rememberWindowState(
        placement = WindowPlacement.Floating,
        position = WindowPosition.Aligned(Alignment.Center),
        size = getPreferredWindowSize(800, 1000),
      )

    // This is an expensive op in terms of time, and compute and therefore must run once
    val loadResources by rememberUpdatedState {
      Log.i { "Loading resources" }
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
