package com.mcxross.cohesive.web

import kotlinx.browser.document
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.HTMLElement

fun main() {
    val rootElement = document.getElementById("root") as HTMLElement

    renderComposable(root = rootElement) {
        Div {
            Text("Cohesive...")
        }
    }
}
