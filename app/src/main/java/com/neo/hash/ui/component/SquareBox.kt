package com.neo.hash.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.min

@Composable
fun BoxWithConstraintsScope.SquareBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) = Box(
    modifier = modifier
        .wrapContentSize()
        .align(Alignment.Center)
) {

    val size = min(
        this@SquareBox.maxHeight,
        this@SquareBox.maxWidth
    )

    Box(
        modifier = Modifier.size(size),
        content = content
    )
}
