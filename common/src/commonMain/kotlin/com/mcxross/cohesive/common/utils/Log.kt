package com.mcxross.cohesive.common.utils

import io.github.aakira.napier.Napier


object Log {

  private const val TAG = "Cohesive"

  fun init() {
    Napier.base(DebugAntilog(TAG))
  }

  fun v(message: () -> String) {
    Napier.v(tag = TAG) { message() }
  }

  fun i(message: () -> String) {
    Napier.i(tag = TAG) { message() }
  }

  fun d(message: () -> String) {
    Napier.d(tag = TAG) { message() }
  }

  fun w(message: () -> String) {
    Napier.w(tag = TAG) { message() }
  }

  fun e(message: () -> String) {
    Napier.e(tag = TAG) { message() }
  }

  fun wtf(message: () -> String) {
    Napier.wtf(tag = TAG) { message() }
  }
}
