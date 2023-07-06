package xyz.mcxross.cohesive.common.utils

import java.io.File
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

fun isBinaryFile(file: File): Boolean {
  val BUFFER_SIZE = 1024 * 1024 // 1 MB buffer size
  val MAX_CHECK_SIZE = 1024 * 1024 // Limit the check size to 1 MB

  val channel = FileChannel.open(file.toPath())

  // Read the file content up to the maximum check size
  val fileSize = channel.size()
  val bufferSize = BUFFER_SIZE.coerceAtMost(fileSize.toInt())
  val buffer = ByteBuffer.allocate(bufferSize)
  channel.read(buffer)
  buffer.flip()

  // Check if any non-printable characters are present in the content
  for (i in 0 until bufferSize) {
    val byteValue: Int = buffer[i].toInt() and 0xFF
    if (byteValue < 0x09 || byteValue == 0x0B || (byteValue in 0x0E..0x1A) ||
      (byteValue in 0x7F..0x9F)
    ) {
      channel.close()
      return true
    }
  }

  channel.close()
  return false
}
