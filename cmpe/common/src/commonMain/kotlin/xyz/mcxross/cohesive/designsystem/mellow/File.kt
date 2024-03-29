package xyz.mcxross.cohesive.designsystem.mellow

import kotlinx.coroutines.CoroutineScope

expect val HomeFolder: File

interface File {
  val name: String
  val path: String
  val absolutePath: String
  val canonicalPath: String
  val isDirectory: Boolean
  val children: List<File>
  val hasChildren: Boolean
  fun readLines(scope: CoroutineScope): TextLines
}
