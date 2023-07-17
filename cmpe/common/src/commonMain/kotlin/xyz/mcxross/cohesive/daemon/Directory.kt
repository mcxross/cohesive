package xyz.mcxross.cohesive.daemon

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.mcxross.cohesive.utils.Log
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchKey

class DirectoryListener(private val directoryPath: String) {
  private var running: Boolean = false
  private var onCreateCallback: ((String) -> Unit)? = null
  private var onDeleteCallback: ((String) -> Unit)? = null
  private var onModifyCallback: ((String) -> Unit)? = null

  fun onCreate(callback: (String) -> Unit) {
    onCreateCallback = callback
  }

  fun onDelete(callback: (String) -> Unit) {
    onDeleteCallback = callback
  }

  fun onModify(callback: (String) -> Unit) {
    onModifyCallback = callback
  }

  @Suppress("NewApi")
  suspend fun startListening() = coroutineScope {
    val directory = Paths.get(directoryPath)

    if (!Files.exists(directory) || !Files.isDirectory(directory)) {
      throw IllegalArgumentException("Invalid directory path: $directoryPath")
    }

    running = true

    Log.d { "Directory listener started. Listening for new files in $directoryPath and child dir(s)" }

    watchDirectory(directory)

  }

  @OptIn(DelicateCoroutinesApi::class)
  private fun watchDirectory(directory: Path) {
    val watchService = FileSystems.getDefault().newWatchService()
    directory.register(
      watchService,
      StandardWatchEventKinds.ENTRY_CREATE,
      StandardWatchEventKinds.ENTRY_MODIFY,
      StandardWatchEventKinds.ENTRY_DELETE,
    )

    val subDirs = Files.newDirectoryStream(directory) {
      Files.isDirectory(it)
    }

    subDirs.forEach {
      GlobalScope.launch {
        watchDirectory(it)

        while (running) {
          val key: WatchKey = withContext(Dispatchers.IO) {
            watchService.take()
          }

          for (event in key.pollEvents()) {
            val kind = event.kind()
            val fileName = event.context() as Path

            when (kind) {
              StandardWatchEventKinds.ENTRY_CREATE -> {
                val filePath = directory.resolve(fileName)
                onCreateCallback?.invoke(filePath.toString())
              }

              StandardWatchEventKinds.ENTRY_DELETE -> {
                val filePath = directory.resolve(fileName)
                onDeleteCallback?.invoke(filePath.toString())
              }

              StandardWatchEventKinds.ENTRY_MODIFY -> {
                val filePath = directory.resolve(fileName)
                onModifyCallback?.invoke(filePath.toString())
              }
            }
          }

          key.reset()
        }
      }
    }
  }

  fun stopListening() {
    running = false
  }
}

fun createCohesiveProjectListener(
  directoryPath: String,
  block: DirectoryListener.() -> Unit
): DirectoryListener {
  val listener = DirectoryListener(directoryPath)
  listener.block()
  return listener
}
