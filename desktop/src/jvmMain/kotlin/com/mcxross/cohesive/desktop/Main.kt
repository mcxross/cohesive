package com.mcxross.cohesive.desktop

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.*
import com.mcxross.cohesive.common.model.onnet.Descriptor
import com.mcxross.cohesive.common.openapi.ui.view.IMain
import com.mcxross.cohesive.common.openapi.ui.view.IStore
import com.mcxross.cohesive.common.ui.view.splash.SplashScreen
import com.mcxross.cohesive.common.utils.*
import com.mcxross.cohesive.desktop.utils.loadPluginsAsync
import kotlinx.coroutines.delay
import org.pf4j.PluginManager
import kotlin.system.measureTimeMillis

fun loadConfig(onLoaded: () -> Unit) {
    Log.i { "Loading config" }
    val timeInMillis = measureTimeMillis {
        Log.d { load(readFileToStr("config.toml")).chain.clusterKey.toString() }
    }
    Log.i { "Config loaded in $timeInMillis ms" }
    onLoaded()
}

fun loadStartPlugins(onLoaded: () -> Unit, onStarted: (it: PluginManager) -> Unit) {
    Log.i { "Loading plugins" }
    val timeLoadingPlugins = measureTimeMillis {
        loadPluginsAsync({
            Log.i { "Plugins Loaded" }
            onLoaded()
        }) {
            it.loadPlugins()
            Log.i { "Start plugins" }
            val timeStartingPlugins = measureTimeMillis {
                it.startPlugins()
                onStarted(it)
            }
            Log.i { "Plugins Started in $timeStartingPlugins ms" }
        }
    }
    Log.i { "Plugins loaded, and started in $timeLoadingPlugins ms" }
}

fun main() = application {

    Log.init()

    var isLoadingPlugins by remember { mutableStateOf(true) }
    var isLoadingConfig by remember { mutableStateOf(true) }
    var isLoadingResources by remember { mutableStateOf(true) }

    //monitor this value
    var pluginManager: PluginManager = DefaultAsyncPluginManager()

    val state = rememberWindowState(
        placement = WindowPlacement.Floating,
        position = WindowPosition.Aligned(Alignment.Center),
        size = getPreferredWindowSize(800, 1000)
    )

    WindowStateHolder.state = state

    val loadResources by rememberUpdatedState {

        Log.i { "Loading resources" }

        val timeLoadingResources = measureTimeMillis {

            loadConfig {
                isLoadingConfig = false
            }

            loadStartPlugins({}) {
                pluginManager = it
                isLoadingPlugins = false
            }

        }

        Log.i { "Resources loaded in $timeLoadingResources ms" }

    }

    LaunchedEffect(true) {
        delay(3000)
        loadResources()
    }

    isLoadingResources = isLoadingPlugins && isLoadingConfig

    if (isLoadingResources) {

        Window(
            onCloseRequest = ::exitApplication,
            undecorated = true,
            resizable = false,
            state = WindowState(
                position = WindowPosition.Aligned(Alignment.Center),
                size = getPreferredWindowSize(400, 300),
            ),
        ) {
            SplashScreen()
        }

    } else {

        if (WindowStateHolder.isPreAvail) {

            if (WindowStateHolder.isMainWindowOpen) {

                Window(
                    onCloseRequest = ::exitApplication,
                    undecorated = true,
                    state = WindowStateHolder.state,
                    alwaysOnTop = true,
                    icon = BitmapPainter(useResource("ic_launcher.png", ::loadImageBitmap)),
                ) {
                    pluginManager.getExtensions(IMain::class.java).forEach {
                        it.Show(this)
                    }
                }

            }
        }

        if (WindowStateHolder.isDelayClose) {

            if (WindowStateHolder.isStoreWindowOpen) {

                val content = remember {
                    Descriptor.run()
                }

                Window(
                    onCloseRequest = ::exitApplication,
                    undecorated = true,
                    resizable = false,
                    state = WindowState(
                        position = WindowPosition.Aligned(Alignment.Center),
                    ),
                    icon = BitmapPainter(useResource("ic_launcher.png", ::loadImageBitmap)),
                ) {
                    val iStore = pluginManager.getExtensions(IStore::class.java)[0]
                    if (content.isContentReady()) {

                        iStore.Emit(windowScope = this, plugins = content.getPlugins())

                    } else {
                        iStore.Emit(windowScope = this, plugins = emptyList())
                    }
                }

            }
        }

    }
}
