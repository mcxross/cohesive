package xyz.mcxross.cohesive.csp.annotation

/**
 * Annotation for specifying a platform's network information for a Cohesive extension.
 *
 * @param k The key for the network.
 * @param v The value for the network.
 */
@MustBeDocumented
annotation class Net(
  val k: String,
  val v: String,
)
