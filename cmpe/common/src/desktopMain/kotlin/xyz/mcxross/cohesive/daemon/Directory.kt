package xyz.mcxross.cohesive.daemon

import kotlinx.coroutines.coroutineScope
import xyz.mcxross.cohesive.common.utils.Log
import java.nio.file.*

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

    val watchService = FileSystems.getDefault().newWatchService()
    directory.register(
      watchService,
      StandardWatchEventKinds.ENTRY_CREATE,
      StandardWatchEventKinds.ENTRY_DELETE,
      StandardWatchEventKinds.ENTRY_MODIFY,
    )

    running = true

    Log.d { "Directory listener started. Listening for new files in $directoryPath" }

    while (running) {
      val key: WatchKey = watchService.take()

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
