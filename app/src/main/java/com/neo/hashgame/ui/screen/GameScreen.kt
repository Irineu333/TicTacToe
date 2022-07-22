package com.neo.hashgame.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
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
) = Box(modifier = modifier) {
    DrawBackground(modifier)
    DrawForeground(
        onClick = {

        },
        modifier = modifier,
        hash = hash
    )
}

@Composable
private fun DrawForeground(
    onClick: (Hash.Block) -> Unit,
    modifier: Modifier,
    hash: Hash
) = BoxWithConstraints(modifier = modifier) {

    val rowsSize = maxHeight / 3
    val columnsSize = maxWidth / 3

    for (row in Hash.KEY_RANGE) {
        for (column in Hash.KEY_RANGE) {

            Block(
                onClick = onClick,
                block = Hash.Block(
                    row = row,
                    column = column,
                    player = hash.get(row, column)
                ),
                modifier = Modifier
                    .size(
                        width = columnsSize,
                        height = rowsSize
                    )
                    .offset(
                        x = columnsSize * column.dec(),
                        y = rowsSize * row.dec(),
                    )
            )
        }
    }
}

@Composable
private fun Block(
    onClick: (Hash.Block) -> Unit,
    modifier: Modifier,
    block: Hash.Block
) = IconButton(
    onClick = {
        onClick(block)
    },
    modifier = modifier
) {
    val player = block.player ?: return@IconButton

    Player(
        player = player,
        modifier = Modifier.fillMaxSize(
            fraction = 0.6f
        )
    )
}

@Composable
fun Player(
    player: Hash.Player,
    modifier: Modifier
) = Canvas(modifier = modifier) {
    when (player) {
        Hash.Player.O -> {

            val radius = min(size.height, size.height) / 2f

            roundedDrawPlayer1(
                color = Color.Blue,
                radius = radius,
                center = center
            )
        }
        Hash.Player.X -> {
            roundedDrawPlayer2(
                color = Color.Blue
            )
        }
    }
}

@Composable
private fun DrawBackground(
    modifier: Modifier
) = Canvas(modifier = modifier) {

    val rowsSize = size.height / 3
    val columnsSize = size.width / 3

    fun drawLine(
        start: Offset,
        end: Offset
    ) = roundedDrawLine(
        color = Color.Red,
        start = start,
        end = end,
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

fun DrawScope.roundedDrawLine(
    color: Color,
    start: Offset,
    end: Offset
) = drawLine(
    color = color,
    start = start,
    end = end,
    strokeWidth = 2.dp.toPx(),
    cap = StrokeCap.Round
)

fun DrawScope.roundedDrawPlayer1(
    color: Color,
    center: Offset,
    radius: Float,
) = drawCircle(
    color = color,
    radius = radius,
    center = center,
    style = Stroke(
        width = 2.dp.toPx(),
        cap = StrokeCap.Round
    )
)

private fun DrawScope.roundedDrawPlayer2(color: Color) {

    fun drawPlayer2(
        start: Offset,
        end: Offset
    ) = drawLine(
        color = color,
        start = start,
        end = end,
        strokeWidth = 2.dp.toPx(),
        cap = StrokeCap.Round
    )

    drawPlayer2(
        start = Offset(
            x = 0f,
            y = 0f
        ),
        end = Offset(
            x = size.width,
            y = size.height
        )
    )

    drawPlayer2(
        start = Offset(
            x = size.width,
            y = 0f
        ),
        end = Offset(
            x = 0f,
            y = size.height
        )
    )
}