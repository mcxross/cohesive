package com.mcxross.cohesive.common.frontend.utils

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

actual fun <T> runBlocking(
  context: CoroutineContext,
  block: suspend CoroutineScope.() -> T,
): T {
  return kotlinx.coroutines.runBlocking(context, block)
}
