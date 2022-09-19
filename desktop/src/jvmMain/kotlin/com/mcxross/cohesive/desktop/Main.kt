package com.mcxross.cohesive.desktop

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.*
import com.mcxross.cohesive.common.frontend.model.Local.LocalContext
import com.mcxross.cohesive.common.frontend.model.onnet.Descriptor
import com.mcxross.cohesive.common.frontend.openapi.ui.screen.IMain
import com.mcxross.cohesive.common.frontend.openapi.ui.screen.IStore
import com.mcxross.cohesive.common.frontend.ui.view.splash.SplashScreen
import com.mcxross.cohesive.common.frontend.utils.*
import com.mcxross.cohesive.common.utils.Log
import com.mcxross.cohesive.desktop.utils.loadPluginsAsync
import kotlinx.coroutines.delay
import org.pf4j.PluginManager
import kotlin.system.measureTimeMillis

inline fun loadConfig(onLoaded: (environment: com.mcxross.cohesive.common.frontend.model.Environment) -> Unit) {
    Log.i { "Loading config" }
    var configuration: com.mcxross.cohesive.common.frontend.model.Configuration
    val timeInMillis = measureTimeMillis {
        configuration = load(readFileToStr("config.toml"))
    }
    val chains = ArrayList<String>()
    val chainPaths = ArrayList<String>()
    configuration.chain.clusterKey.forEachIndexed { index, clusterKey ->
        chains.add(clusterKey.toString())
        chainPaths.add(configuration.chain.clusterValue[index])
    }
    Log.i { "Config loaded in $timeInMillis ms" }
    onLoaded(com.mcxross.cohesive.common.frontend.model.Environment.create(chains = chains, chainPaths = chainPaths))
}

inline fun loadStartPlugins(crossinline onLoaded: () -> Unit, crossinline onStarted: (it: PluginManager) -> Unit) {
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

@Composable
fun BrewContextCompositionLocal(
    windowScope: WindowScope,
    environment: com.mcxross.cohesive.common.frontend.model.Environment,
    content: @Composable () -> Unit,
) {
    val context = com.mcxross.cohesive.common.frontend.model.Context()
    context.windowScope = windowScope
    context.environment = environment
    LocalContext = compositionLocalOf { context }
    CompositionLocalProvider(LocalContext provides context) {
        content()
    }
}

fun main() = application {

    /*Must initialize logging*/
    Log.init()

    var isLoadingPlugins by remember { mutableStateOf(true) }
    var isLoadingConfig by remember { mutableStateOf(true) }
    var isLoadingResources by remember { mutableStateOf(true) }

    //monitor this value
    var pluginManager: PluginManager = DefaultAsyncPluginManager()
    var environment by remember { mutableStateOf(com.mcxross.cohesive.common.frontend.model.Environment) }

    WindowStateHolder.state = rememberWindowState(
        placement = WindowPlacement.Floating,
        position = WindowPosition.Aligned(Alignment.Center),
        size = getPreferredWindowSize(800, 1000)
    )

    val loadResources by rememberUpdatedState {

        Log.i { "Loading resources" }

        val timeLoadingResources = measureTimeMillis {

            loadConfig {
                environment = it
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
                    icon = BitmapPainter(useResource("ic_launcher.png", ::loadImageBitmap)),
                ) {
                    pluginManager.getExtensions(IMain::class.java).forEach {
                        BrewContextCompositionLocal(
                                windowScope = this,
                                environment = environment,
                            ) {
                                it.Compose()
                            }

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
                        environment.plugins = content.getPlugins()
                        BrewContextCompositionLocal(
                            windowScope = this,
                            environment = environment,
                        ) {
                            iStore.Compose()
                        }

                    } else {
                        BrewContextCompositionLocal(
                            windowScope = this,
                            environment = environment,
                        ) {
                            iStore.Compose()
                        }
                    }
                }

            }
        }

    }
}
