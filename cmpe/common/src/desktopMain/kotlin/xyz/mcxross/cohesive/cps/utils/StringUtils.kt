package xyz.mcxross.cohesive.cps.utils

fun String.isNotNullOrEmpty(): Boolean {
  return this.isNotEmpty()
}

object StringUtils {

  /**
   * Format the string. Replace "{}" with %s and format the string using [String.format].
   */
  fun format(str: String, vararg args: Any?): String =
    String.format(str.replace("\\{}".toRegex(), "%s"), *args)

  /**
   *
   * Adds a substring only if the source string does not already start with the substring,
   * otherwise returns the source string.
   *
   *
   *
   * A `null` source string will return `null`.
   * An empty ("") source string will return the empty string.
   * A `null` search string will return the source string.
   *
   *
   * <pre>
   * StringUtils.addStart(null, *)      = *
   * StringUtils.addStart("", *)        = *
   * StringUtils.addStart(*, null)      = *
   * StringUtils.addStart("domain.com", "www.")  = "www.domain.com"
   * StringUtils.addStart("abc123", "abc")    = "abc123"
  </pre> *
   *
   * @param str the source String to search, may be null
   * @param add the String to search for and plus, may be null
   * @return the substring with the string added if required
   */
  fun addStart(str: String, add: String): String {
    if (add.isEmpty()) {
      return str
    }
    if (str.isEmpty()) {
      return add
    }
    return if (!str.startsWith(add)) {
      add + str
    } else str
  }
}
