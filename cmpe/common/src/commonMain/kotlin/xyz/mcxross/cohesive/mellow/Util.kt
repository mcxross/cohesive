package xyz.mcxross.cohesive.mellow

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

/**
 * Iterates through a [List] using the index and calls [action] for each item.
 * This does not allocate an iterator like [Iterable.forEachIndexed].
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> List<T>.fastForEachIndexed(action: (Int, T) -> Unit) {
  contract { callsInPlace(action) }
  for (index in indices) {
    val item = get(index)
    action(index, item)
  }
}

@OptIn(ExperimentalContracts::class)
inline fun <T> List<T>.fastFirstOrNull(predicate: (T) -> Boolean): T? {
  contract { callsInPlace(predicate) }
  forEach { if (predicate(it)) return it }
  return null
}

/**
 * Returns a list containing the results of applying the given [transform] function
 * to each element in the original collection.
 */
@OptIn(ExperimentalContracts::class)
inline fun <T, R> List<T>.fastMap(transform: (T) -> R): List<R> {
  contract { callsInPlace(transform) }
  val target = ArrayList<R>(size)
  fastForEach {
    target += transform(it)
  }
  return target
}

/**
 * Accumulates value starting with [initial] value and applying [operation] from left to right
 * to current accumulator value and each element.
 *
 * Returns the specified [initial] value if the collection is empty.
 *
 * @param [operation] function that takes current accumulator value and an element, and calculates the next accumulator value.
 */
@OptIn(ExperimentalContracts::class)
internal inline fun <T, R> List<T>.fastFold(initial: R, operation: (acc: R, T) -> R): R {
  contract { callsInPlace(operation) }
  var accumulator = initial
  fastForEach { e ->
    accumulator = operation(accumulator, e)
  }
  return accumulator
}

/**
 * Returns `true` if all elements match the given [predicate].
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> List<T>.fastAll(predicate: (T) -> Boolean): Boolean {
  contract { callsInPlace(predicate) }
  fastForEach { if (!predicate(it)) return false }
  return true
}

/**
 * Returns `true` if at least one element matches the given [predicate].
 */
@OptIn(ExperimentalContracts::class)
inline fun <T> List<T>.fastAny(predicate: (T) -> Boolean): Boolean {
  contract { callsInPlace(predicate) }
  fastForEach { if (predicate(it)) return true }
  return false
}

/**
 * Returns a list containing the results of applying the given [transform] function
 * to each element in the original collection.
 */
@OptIn(ExperimentalContracts::class)
internal inline fun <T, R> List<T>.fastMapIndexedNotNull(
  transform: (index: Int, T) -> R?
): List<R> {
  contract { callsInPlace(transform) }
  val target = ArrayList<R>(size)
  fastForEachIndexed { index, e ->
    transform(index, e)?.let { target += it }
  }
  return target
}
