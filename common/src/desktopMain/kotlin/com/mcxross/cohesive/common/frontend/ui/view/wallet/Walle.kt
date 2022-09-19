package com.mcxross.cohesive.common.frontend.ui.view.wallet

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.mcxross.cohesive.common.frontend.openapi.ui.view.IWallet
import com.mcxross.cohesive.mellow.Dialog

class Walle : IWallet {
    @Composable
    override fun CreateAccountDialog(
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
    ) = Dialog(
        onClose = onClose,
        text = text,
        negativeText = negativeText,
        neutralText = neutralText,
        positiveText = positiveText,
        onNegative = onNegative,
        onNeutral = onNeutral,
        onPositive = onPositive,
        negativeEnable = negativeEnable,
        neutralEnable = neutralEnable,
        positiveEnable = positiveEnable,
        width = width,
        height = height,
        content = content,
    )

    @Composable
    override fun ImportAccountDialog(
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
    ) = Dialog(
        onClose = onClose,
        text = text,
        negativeText = negativeText,
        neutralText = neutralText,
        positiveText = positiveText,
        onNegative = onNegative,
        onNeutral = onNeutral,
        onPositive = onPositive,
        negativeEnable = negativeEnable,
        neutralEnable = neutralEnable,
        positiveEnable = positiveEnable,
        width = width,
        height = height,
        content = content,
    )

    @Composable
    override fun Compose() {

    }
}