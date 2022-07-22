package com.neo.hashgame.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neo.hashgame.HashGameBackground

@Composable
fun GameScreen(
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    HashTable(
        modifier = Modifier.size(200.dp)
    )
}

@Composable
private fun HashTable(
    modifier: Modifier
) = Canvas(modifier = modifier) {

    val rowsSize = size.height / 3
    val columnsSize = size.width / 3

    fun drawLine(
        start: Offset,
        end: Offset
    ) = drawLine(
        color = Color.Red,
        strokeWidth = 2.dp.toPx(),
        start = start,
        end = end
    )

    fun drawRow(position: Int) = drawLine(
        start = Offset(0f, rowsSize * position),
        end = Offset(size.height, rowsSize * position),
    )

    fun drawColumn(position: Int) = drawLine(
        start = Offset(columnsSize * position, 0f),
        end = Offset(columnsSize * position, size.height),
    )

    for (index in 1..2) {
        drawRow(index)
        drawColumn(index)
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    HashGameBackground {
        GameScreen()
    }
}