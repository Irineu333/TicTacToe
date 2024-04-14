package com.neo.hash.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun GameButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) = OutlinedButton(
    onClick = onClick,
    modifier = modifier,
    enabled = enabled,
    shape = RectangleShape,
    border = BorderStroke(
        ButtonDefaults.OutlinedBorderSize,
        if (enabled) MaterialTheme.colors.primary else
            MaterialTheme.colors.primary.copy(
                alpha = ButtonDefaults.OutlinedBorderOpacity
            )
    ),
    content = content
)

@Preview
@Composable
private fun GameButtonPreview() {
    GameButton(onClick = {}) {
        Text(text = "Test")
    }
}