package com.neo.hashgame.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neo.hashgame.HashGameBackground
import com.neo.hashgame.model.Hash
import kotlin.math.min

@Composable
fun GameScreen(
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    HashTable(
        modifier = Modifier.size(200.dp),
        hash = Hash().apply {
            set(Hash.Player.O, 1, 1)
            set(Hash.Player.X, 3, 3)
        }
    )
}

@Composable
private fun HashTable(
    modifier: Modifier,
    hash: Hash
) = Canvas(modifier = modifier) {

    val rowsSize = size.height / 3
    val columnsSize = size.width / 3

    fun drawLine(
        start: Offset,
        end: Offset
    ) = drawLine(
        color = Color.Red,
        start = start,
        strokeWidth = 2.dp.toPx(),
        end = end,
        cap = StrokeCap.Round
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

    val rowRadius = rowsSize / 2
    val columnRadius = columnsSize / 2

    fun drawPlayer(
        player: Hash.Player,
        row: Int,
        column: Int
    ) {

        val center = Offset(
            x = columnsSize * column - columnRadius,
            y = rowsSize * row - rowRadius
        )

        when(player) {
            Hash.Player.O -> {
                drawCircle(
                    color = Color.Blue,
                    radius = min(rowRadius, columnRadius) / 2f,
                    center = center,
                    style = Stroke(
                        width = 2.dp.toPx()
                    )
                )
            }
            Hash.Player.X -> {
                drawLine(
                    color = Color.Blue,
                    start = Offset(
                        x = center.x - columnRadius / 2f,
                        y = center.y - rowRadius / 2f
                    ),
                    end = Offset(
                        x = center.x + columnRadius / 2f,
                        y = center.y + rowRadius / 2f
                    ),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )

                drawLine(
                    color = Color.Blue,
                    start = Offset(
                        x = center.x + columnRadius / 2f,
                        y = center.y - rowRadius / 2f
                    ),
                    end = Offset(
                        x = center.x - columnRadius / 2f,
                        y = center.y + rowRadius / 2f
                    ),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
        }
    }

    for (row in Hash.KEY_RANGE) {
        for (column in Hash.KEY_RANGE) {

            val player = hash.get(row, column) ?: continue

            drawPlayer(
                player,
                row,
                column
            )
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