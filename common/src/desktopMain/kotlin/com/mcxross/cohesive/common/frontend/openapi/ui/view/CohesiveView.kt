package com.mcxross.cohesive.common.frontend.openapi.ui.view

import androidx.compose.runtime.Composable
import com.mcxross.cohesive.csp.annotation.ExtensionPoint

/**
 * This is the main entry point for the Cohesive UI.
 *
 * Extend this class to create your own declarative UI implementation of the Cohesive View UI.
 * The Cohesive Plugin System will instantiate this class and inject the various implementations
 * defaulting to System implementations otherwise.
 *
 * Note: This class doesn't support overriding the Cohesive Screen(s), it's for Views only.
 *
 * @property Explorer is the `Explorer` abstract.
 * @property Wallet is the `Wallet` abstract.
 * @property SimpleEditor is the `SimpleEditor` abstract. A bare minimum lightweight editor for editing.
 * @property CompositeEditor is the `IDE` abstract. A more advanced editor with tooling for editing and development.
 * @since 0.1.0
 */
interface CohesiveView : ExtensionPoint {

    @Composable
    fun Explorer() {}

    @Composable
    fun Wallet() {}

    @Composable
    fun SimpleEditor() {}

    @Composable
    fun CompositeEditor() {}
}