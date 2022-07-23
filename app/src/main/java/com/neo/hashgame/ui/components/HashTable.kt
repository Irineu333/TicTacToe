package com.neo.hashgame.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.neo.hashgame.model.Hash
import kotlin.math.min

typealias OnBlockClick = (Hash.Block) -> Unit

private val Density.DEFAULT_LINE_WIDTH
    get() = 4.dp.toPx()

@Composable
fun HashTable(
    modifier: Modifier = Modifier,
    onBlockClick: OnBlockClick? = null,
    lineHashColor: Color = MaterialTheme.colors.onBackground,
    linePlayersColors: Color = MaterialTheme.colors.primary,
    hash: Hash
) = Box(modifier = modifier) {
    DrawBackground(lineHashColor)
    DrawForeground(
        onBlockClick = onBlockClick,
        linePlayersColors = linePlayersColors,
        hash = hash
    )
}

@Composable
private fun DrawForeground(
    onBlockClick: OnBlockClick? = null,
    linePlayersColors: Color,
    hash: Hash
) = BoxWithConstraints {

    val rowsSize = remember { maxHeight / 3 }
    val columnsSize = remember { maxWidth / 3 }

    for (row in Hash.KEY_RANGE) {
        for (column in Hash.KEY_RANGE) {

            Block(
                block = Hash.Block(
                    row = row,
                    column = column,
                    player = hash.get(row, column)
                ),
                linePlayersColors = linePlayersColors,
                onClick = onBlockClick,
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
    block: Hash.Block,
    onClick: OnBlockClick? = null,
    modifier: Modifier,
    linePlayersColors: Color
) = Box(
    modifier = onClick?.let {
        modifier.clickable {
            it(block)
        }
    } ?: modifier,
    contentAlignment = Alignment.Center
) {
    if (block.player != null) {
        Player(
            player = block.player,
            linePlayersColors = linePlayersColors,
            modifier = Modifier.fillMaxSize(
                fraction = 0.5f
            )
        )
    }
}

@Composable
private fun Player(
    player: Hash.Player,
    modifier: Modifier,
    linePlayersColors: Color
) = Canvas(modifier = modifier) {
    when (player) {
        Hash.Player.O -> {

            val radius = min(size.height, size.width) / 2f

            drawRoundedDrawCircle(
                color = linePlayersColors,
                radius = radius,
                center = center
            )
        }
        Hash.Player.X -> {

            fun drawLine(
                start: Offset,
                end: Offset
            ) = drawRoundedLine(
                color = linePlayersColors,
                start = start,
                end = end
            )

            drawLine(
                start = Offset(
                    x = 0f,
                    y = 0f
                ),
                end = Offset(
                    x = size.width,
                    y = size.height
                )
            )

            drawLine(
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
    }
}

@Composable
private fun DrawBackground(
    lineHashColor: Color
) = Canvas(
    modifier = Modifier.fillMaxSize()
) {

    val rowsSize = size.height / 3
    val columnsSize = size.width / 3

    fun drawLine(
        start: Offset,
        end: Offset
    ) = drawRoundedLine(
        color = lineHashColor,
        start = start,
        end = end,
    )

    fun drawRow(position: Int) = drawLine(
        start = Offset(0f, rowsSize * position),
        end = Offset(size.width, rowsSize * position),
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

fun DrawScope.drawRoundedLine(
    color: Color,
    start: Offset,
    end: Offset
) = drawLine(
    color = color,
    start = start,
    end = end,
    strokeWidth = DEFAULT_LINE_WIDTH,
    cap = StrokeCap.Round
)

fun DrawScope.drawRoundedDrawCircle(
    color: Color,
    center: Offset,
    radius: Float,
) = drawCircle(
    color = color,
    radius = radius,
    center = center,
    style = Stroke(
        width = DEFAULT_LINE_WIDTH,
        cap = StrokeCap.Round
    )
)

@Preview(showBackground = true)
@Composable
fun HashTablePreview() {
    SquareBox {
        HashTable(
            hash = Hash().apply {
                set(Hash.Player.X, 2, 2)
                set(Hash.Player.O, 1, 1)
                set(Hash.Player.O, 3, 3)
            }
        )
    }
}