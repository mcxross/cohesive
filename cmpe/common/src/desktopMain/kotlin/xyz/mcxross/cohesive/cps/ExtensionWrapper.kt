package xyz.mcxross.cohesive.cps

/** A wrapper over extension instance. */
class ExtensionWrapper<T>(
  val descriptor: ExtensionDescriptor,
  private val extensionFactory: ExtensionFactory
) : Comparable<ExtensionWrapper<T>?> {

  var extension: T? = null // cache extension instance
    get() {
      if (field == null) {
        field = extensionFactory.create(descriptor.extensionClass) as T?
      }
      return field
    }
    private set
  val ordinal: Int
    get() = descriptor.ordinal

  override operator fun compareTo(other: ExtensionWrapper<T>?): Int {
    return ordinal - other!!.ordinal
  }
}
