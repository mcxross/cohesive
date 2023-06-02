package xyz.mcxross.cohesive.common.frontend.api.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp

interface IWallet : IView {

  @Composable
  fun CreateAccountDialog(
    onClose: () -> Unit,
    text: String,
    negativeText: String,
    neutralText: String,
    positiveText: String,
    onNegative: () -> Unit,
    onNeutral: () -> Unit,
    onPositive: () -> Unit,
    negativeEnable: Boolean,
    neutralEnable: Boolean,
    positiveEnable: Boolean,
    width: Dp,
    height: Dp,
    content: @Composable () -> Unit,
  )

  @Composable
  fun ImportAccountDialog(
    onClose: () -> Unit,
    text: String,
    negativeText: String,
    neutralText: String,
    positiveText: String,
    onNegative: () -> Unit,
    onNeutral: () -> Unit,
    onPositive: () -> Unit,
    negativeEnable: Boolean,
    neutralEnable: Boolean,
    positiveEnable: Boolean,
    width: Dp,
    height: Dp,
    content: @Composable () -> Unit
  )
}
