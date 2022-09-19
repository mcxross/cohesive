package com.mcxross.cohesive.common.frontend.utils

import okio.FileSystem
import okio.Path.Companion.toPath
import okio.buffer

actual fun readFileToStr(
    path: String,
): String {
    val file = FileSystem.SYSTEM.source(path.toPath())
    return file.buffer().readUtf8()
}