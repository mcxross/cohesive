package com.mcxross.cohesive.common.frontend.ui.view

import androidx.compose.runtime.Composable
import com.mcxross.cohesive.common.frontend.openapi.ui.view.CohesiveView
import com.mcxross.cohesive.csp.annotation.Cohesive
import com.mcxross.cohesive.csp.annotation.Net

/**
 * Default implementation of [CohesiveView] that is used when no other implementation is provided.
 *
 * It implements all abstract methods of [CohesiveView] with "generic" UIs.
 * @since 0.1.0
 */
@Cohesive(
  platform = "Cohesive",
  version = "0.1.0",
  nets = [
    Net(
      k = "Mainnet",
      v = "https://mainnet.cohesive.network",
    ),
    Net(
      k = "Testnet",
      v = "https://testnet.cohesive.network",
    ),
    Net(
      k = "Localhost",
      v = "http://localhost:3998",
    ),
  ],
)
open class DefaultCohesiveView : CohesiveView {

  @Composable
  override fun Explorer() {
    com.mcxross.cohesive.common.frontend.ui.view.explorer.Explorer()
  }

  @Composable
  override fun Wallet() {
    com.mcxross.cohesive.common.frontend.ui.view.wallet.Wallet()
  }
}
