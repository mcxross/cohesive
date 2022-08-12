package com.mcxross.cohesive.desktop.utils

import com.mcxross.cohesive.desktop.AsyncPluginManager
import com.mcxross.cohesive.desktop.DefaultAsyncPluginManager
import org.pf4j.DefaultExtensionFinder
import org.pf4j.ExtensionFinder
import org.pf4j.PluginManager
import java.util.concurrent.CompletableFuture

fun loadPluginsAsync(onLoaded: () -> Unit, onStarted: (plugin: PluginManager) -> Unit) {

    val asyncPluginManager: AsyncPluginManager = object : DefaultAsyncPluginManager() {

        override fun createExtensionFinder(): ExtensionFinder {
            val extensionFinder = super.createExtensionFinder() as DefaultExtensionFinder
            extensionFinder.addServiceProviderExtensionFinder()
            return extensionFinder
        }
    }

    val feature: CompletableFuture<Void> = asyncPluginManager.loadPluginsAsync()
    feature.thenRun {
        println("Plugins loaded...")
        onLoaded()
    }

    feature.thenCompose { v: Void? -> asyncPluginManager.startPluginsAsync() }
    feature.thenRun {
        println("Plugins started...")
        onStarted(asyncPluginManager)
    }

    feature.get()

}