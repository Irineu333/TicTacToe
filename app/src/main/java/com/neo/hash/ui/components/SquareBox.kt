package com.neo.hash.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.min

@Composable
fun SquareBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) = Box(modifier = modifier) {
    BoxWithConstraints {

        val size = remember { min(maxHeight, maxWidth) }

        Box(
            modifier = Modifier.size(size),
            content = content
        )
    }
}
