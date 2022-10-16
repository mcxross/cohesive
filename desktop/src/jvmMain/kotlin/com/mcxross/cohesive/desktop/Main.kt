package com.mcxross.cohesive.desktop

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.window.*
import com.mcxross.cohesive.common.Cohesive
import com.mcxross.cohesive.common.frontend.model.Local.LocalContext
import com.mcxross.cohesive.common.frontend.model.onnet.Descriptor
import com.mcxross.cohesive.common.frontend.ui.view.splash.SplashScreen
import com.mcxross.cohesive.common.frontend.utils.*
import com.mcxross.cohesive.common.frontend.utils.WindowState
import com.mcxross.cohesive.common.utils.Log
import com.mcxross.cohesive.mellow.PlatformDropTargetModifier
import kotlinx.coroutines.delay
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

@Composable
fun BrewContextCompositionLocal(
    windowScope: WindowScope,
    environment: com.mcxross.cohesive.common.frontend.model.Environment,
    platformDropTargetModifier: PlatformDropTargetModifier,
    content: @Composable () -> Unit,
) {
    val context = com.mcxross.cohesive.common.frontend.model.Context()
    context.windowScope = windowScope
    context.environment = environment
    context.platformDropTargetModifier = platformDropTargetModifier
    LocalContext = compositionLocalOf { context }
    CompositionLocalProvider(LocalContext provides context) {
        content()
    }
}

fun main() = Cohesive.run {

    /*val init by rememberUpdatedState {
       runBlocking {
           StatesHolder.init {
               println("Scheduler initialized")
           }
       }
   }

   var isLoadingPlugins by remember { mutableStateOf(true) }
   var isLoadingConfig by remember { mutableStateOf(true) }
   var isLoadingResources by remember { mutableStateOf(true) }

   var environment by remember { mutableStateOf(com.mcxross.cohesive.common.frontend.model.Environment) }

   WindowState.state = rememberWindowState(
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

           *//*loadStartPlugins({}) {
                pluginManager = it
                isLoadingPlugins = false
            }*//*

        }

        Log.i { "Resources loaded in $timeLoadingResources ms" }

    }

    LaunchedEffect(true) {
        delay(3000)
        loadResources()
    }

    isLoadingResources = isLoadingConfig//isLoadingPlugins &&
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

        if (WindowState.isPreAvail) {

            if (WindowState.isMainWindowOpen) {

                Window(
                    onCloseRequest = ::exitApplication,
                    undecorated = true,
                    state = WindowState.state,
                    icon = BitmapPainter(useResource("ic_launcher.png", ::loadImageBitmap)),
                ) {
                    val density = LocalDensity.current.density
                    val dropParent = remember(density) {
                        PlatformDropTargetModifier(
                            density = density,
                            window = window,
                        )
                    }

                    *//*pluginManager.getExtensions(IMain::class.java).forEach {
                        BrewContextCompositionLocal(
                            windowScope = this,
                            environment = environment,
                            platformDropTargetModifier = dropParent,
                        ) {
                            it.Compose()
                        }

                    }*//*
                }

            }
        }

        if (WindowState.isDelayClose) {

            if (WindowState.isStoreWindowOpen) {

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
                    val density = LocalDensity.current.density
                    val dropParent = remember(density) {
                        PlatformDropTargetModifier(
                            density = density,
                            window = window,
                        )
                    }
                    //val iStore = pluginManager.getExtensions(IStore::class.java)[0]
                    if (content.isContentReady()) {
                        environment.plugins = content.getPlugins()
                        BrewContextCompositionLocal(
                            windowScope = this,
                            environment = environment,
                            platformDropTargetModifier = dropParent,
                        ) {
                            // iStore.Compose()
                        }

                    } else {
                        BrewContextCompositionLocal(
                            windowScope = this,
                            environment = environment,
                            platformDropTargetModifier = dropParent,
                        ) {
                            //iStore.Compose()
                        }
                    }
                }

            }
        }

    }*/

}
