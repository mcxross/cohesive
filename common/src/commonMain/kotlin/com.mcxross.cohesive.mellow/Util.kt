package com.mcxross.cohesive.mellow

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

internal fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + ((stop - start) * fraction)
}

@OptIn(ExperimentalContracts::class)
inline fun <T> List<T>.fastForEach(action: (T) -> Unit) {
    contract {
        callsInPlace(action)
    }
    for (index in indices) {
        val item = get(index)
        action(item)
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <T> List<T>.fastFirstOrNull(predicate: (T) -> Boolean): T? {
    contract { callsInPlace(predicate) }
    forEach { if (predicate(it)) return it }
    return null
}
