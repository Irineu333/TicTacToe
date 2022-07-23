package com.neo.hashgame.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.min

@Composable
fun SquareBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) = BoxWithConstraints {

    val size = min(maxHeight, maxWidth)

    Box(
        modifier = modifier.size(size),
        content = content
    )
}