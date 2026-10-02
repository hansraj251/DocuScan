package com.docu.scan.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DocuScanColors = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary
)

@Composable
fun DocuScanTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DocuScanColors,
        content = content
    )
}
