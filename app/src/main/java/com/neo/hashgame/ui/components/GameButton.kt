package com.neo.hashgame.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape

@Composable
fun GameButton(
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) = OutlinedButton(
    onClick = onClick,
    shape = RectangleShape,
    border = BorderStroke(
        ButtonDefaults.OutlinedBorderSize,
        MaterialTheme.colors.primary
    ),
    content = content
)