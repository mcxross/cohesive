package com.mcxross.cohesive.cps

import java.util.function.BooleanSupplier

data class PluginLoaderContainer(val loader: PluginLoader, val condition: BooleanSupplier = BooleanSupplier { true })
