package com.mcxross.cohesive.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext

object Daemon {
    @OptIn(DelicateCoroutinesApi::class)
    val scope = CoroutineScope(newSingleThreadContext("Daemon"))
}