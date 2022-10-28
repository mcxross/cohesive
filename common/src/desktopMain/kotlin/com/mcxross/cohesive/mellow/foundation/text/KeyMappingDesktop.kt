/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mcxross.cohesive.mellow.foundation.text

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import com.mcxross.cohesive.mellow.Platform
import java.awt.event.KeyEvent as AwtKeyEvent

internal val platformDefaultKeyMapping: KeyMapping =
  when (Platform.Current) {
    Platform.MacOS -> {
      val common = commonKeyMapping(KeyEvent::isMetaPressed)
      object : KeyMapping {
        override fun map(event: KeyEvent): KeyCommand? {
          return when {
            event.isMetaPressed && event.isCtrlPressed ->
              when (event.key) {
                MappedKeys.Space -> KeyCommand.CHARACTER_PALETTE
                else -> null
              }

            event.isShiftPressed && event.isAltPressed ->
              when (event.key) {
                MappedKeys.DirectionLeft -> KeyCommand.SELECT_LEFT_WORD
                MappedKeys.DirectionRight -> KeyCommand.SELECT_RIGHT_WORD
                MappedKeys.DirectionUp -> KeyCommand.SELECT_PREV_PARAGRAPH
                MappedKeys.DirectionDown -> KeyCommand.SELECT_NEXT_PARAGRAPH
                else -> null
              }

            event.isShiftPressed && event.isMetaPressed ->
              when (event.key) {
                MappedKeys.DirectionLeft -> KeyCommand.SELECT_LINE_LEFT
                MappedKeys.DirectionRight -> KeyCommand.SELECT_LINE_RIGHT
                MappedKeys.DirectionUp -> KeyCommand.SELECT_HOME
                MappedKeys.DirectionDown -> KeyCommand.SELECT_END
                else -> null
              }

            event.isMetaPressed ->
              when (event.key) {
                MappedKeys.DirectionLeft -> KeyCommand.LINE_LEFT
                MappedKeys.DirectionRight -> KeyCommand.LINE_RIGHT
                MappedKeys.DirectionUp -> KeyCommand.HOME
                MappedKeys.DirectionDown -> KeyCommand.END
                MappedKeys.Backspace -> KeyCommand.DELETE_FROM_LINE_START
                else -> null
              }

            // Emacs-like shortcuts
            event.isCtrlPressed && event.isShiftPressed && event.isAltPressed -> {
              when (event.key) {
                MappedKeys.F -> KeyCommand.SELECT_RIGHT_WORD
                MappedKeys.B -> KeyCommand.SELECT_LEFT_WORD
                else -> null
              }
            }

            event.isCtrlPressed && event.isAltPressed -> {
              when (event.key) {
                MappedKeys.F -> KeyCommand.RIGHT_WORD
                MappedKeys.B -> KeyCommand.LEFT_WORD
                else -> null
              }
            }

            event.isCtrlPressed && event.isShiftPressed -> {
              when (event.key) {
                MappedKeys.F -> KeyCommand.SELECT_RIGHT_CHAR
                MappedKeys.B -> KeyCommand.SELECT_LEFT_CHAR
                MappedKeys.P -> KeyCommand.SELECT_UP
                MappedKeys.N -> KeyCommand.SELECT_DOWN
                MappedKeys.A -> KeyCommand.SELECT_LINE_START
                MappedKeys.E -> KeyCommand.SELECT_LINE_END
                else -> null
              }
            }

            event.isCtrlPressed -> {
              when (event.key) {
                MappedKeys.F -> KeyCommand.LEFT_CHAR
                MappedKeys.B -> KeyCommand.RIGHT_CHAR
                MappedKeys.P -> KeyCommand.UP
                MappedKeys.N -> KeyCommand.DOWN
                MappedKeys.A -> KeyCommand.LINE_START
                MappedKeys.E -> KeyCommand.LINE_END
                MappedKeys.H -> KeyCommand.DELETE_PREV_CHAR
                MappedKeys.D -> KeyCommand.DELETE_NEXT_CHAR
                MappedKeys.K -> KeyCommand.DELETE_TO_LINE_END
                MappedKeys.O -> KeyCommand.NEW_LINE
                else -> null
              }
            }
            // end of emacs-like shortcuts

            event.isShiftPressed ->
              when (event.key) {
                MappedKeys.MoveHome -> KeyCommand.SELECT_HOME
                MappedKeys.MoveEnd -> KeyCommand.SELECT_END
                else -> null
              }

            event.isAltPressed ->
              when (event.key) {
                MappedKeys.DirectionLeft -> KeyCommand.LEFT_WORD
                MappedKeys.DirectionRight -> KeyCommand.RIGHT_WORD
                MappedKeys.DirectionUp -> KeyCommand.PREV_PARAGRAPH
                MappedKeys.DirectionDown -> KeyCommand.NEXT_PARAGRAPH
                MappedKeys.Delete -> KeyCommand.DELETE_NEXT_WORD
                MappedKeys.Backspace -> KeyCommand.DELETE_PREV_WORD
                else -> null
              }

            else -> null
          } ?: common.map(event)
        }
      }
    }

    else -> defaultKeyMapping
  }

internal object MappedKeys {
  val A: Key = Key(AwtKeyEvent.VK_A)
  val B: Key = Key(AwtKeyEvent.VK_B)
  val D: Key = Key(AwtKeyEvent.VK_D)
  val C: Key = Key(AwtKeyEvent.VK_C)
  val E: Key = Key(AwtKeyEvent.VK_E)
  val F: Key = Key(AwtKeyEvent.VK_F)
  val H: Key = Key(AwtKeyEvent.VK_H)
  val K: Key = Key(AwtKeyEvent.VK_K)
  val N: Key = Key(AwtKeyEvent.VK_N)
  val O: Key = Key(AwtKeyEvent.VK_O)
  val P: Key = Key(AwtKeyEvent.VK_P)
  val V: Key = Key(AwtKeyEvent.VK_V)
  val X: Key = Key(AwtKeyEvent.VK_X)
  val Z: Key = Key(AwtKeyEvent.VK_Z)
  val Backslash: Key = Key(AwtKeyEvent.VK_BACK_SLASH)
  val DirectionLeft: Key = Key(AwtKeyEvent.VK_LEFT)
  val DirectionRight: Key = Key(AwtKeyEvent.VK_RIGHT)
  val DirectionUp: Key = Key(AwtKeyEvent.VK_UP)
  val DirectionDown: Key = Key(AwtKeyEvent.VK_DOWN)
  val PageUp: Key = Key(AwtKeyEvent.VK_PAGE_UP)
  val PageDown: Key = Key(AwtKeyEvent.VK_PAGE_DOWN)
  val MoveHome: Key = Key(AwtKeyEvent.VK_HOME)
  val MoveEnd: Key = Key(AwtKeyEvent.VK_END)
  val Insert: Key = Key(AwtKeyEvent.VK_INSERT)
  val Enter: Key = Key(AwtKeyEvent.VK_ENTER)
  val Backspace: Key = Key(AwtKeyEvent.VK_BACK_SPACE)
  val Delete: Key = Key(AwtKeyEvent.VK_DELETE)
  val Paste: Key = Key(AwtKeyEvent.VK_PASTE)
  val Cut: Key = Key(AwtKeyEvent.VK_CUT)
  val Copy: Key = Key(AwtKeyEvent.VK_COPY)
  val Tab: Key = Key(AwtKeyEvent.VK_TAB)
  val Space: Key = Key(AwtKeyEvent.VK_SPACE)
}
