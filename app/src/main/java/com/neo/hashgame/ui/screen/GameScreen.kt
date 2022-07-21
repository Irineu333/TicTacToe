package com.neo.hashgame.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neo.hashgame.HashGameBackground

@Composable
fun GameScreen(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = Modifier.size(100.dp)) {

        val rowsSize = size.height / 3
        val columnsSize = size.width / 3

        fun drawRow(position: Int) = drawLine(
            color = Color.Red,
            start = Offset(0f, rowsSize * position),
            end = Offset(size.height, rowsSize * position)
        )

        fun drawColumn(position: Int) = drawLine(
            color = Color.Red,
            start = Offset(columnsSize * position, 0f),
            end = Offset(columnsSize * position, size.height)
        )

        for (index in 1..3) {
            drawRow(index)
            drawColumn(index)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    HashGameBackground {
        GameScreen()
    }
}