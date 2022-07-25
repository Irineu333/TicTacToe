package com.neo.hashgame.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
    hash: Hash,
    modifier: Modifier = Modifier,
    canClick: (Hash.Block) -> Boolean = { true },
    onBlockClick: OnBlockClick? = null,
    hashColor: Color = MaterialTheme.colors.onBackground,
    symbolsColor: Color = MaterialTheme.colors.primary,
    winnerLineColor: Color = symbolsColor
) = Box(modifier = modifier) {
    DrawBackground(hashColor)
    DrawForeground(
        hash = hash,
        canClick = canClick,
        onBlockClick = onBlockClick,
        symbolsColor = symbolsColor,
        winnerLineColor = winnerLineColor
    )
}

@Composable
private fun DrawForeground(
    hash: Hash,
    symbolsColor: Color,
    winnerLineColor: Color,
    canClick: (Hash.Block) -> Boolean = { true },
    onBlockClick: OnBlockClick? = null
) = Box {
    DrawSymbols(
        hash = hash,
        lineSymbolsColors = symbolsColor,
        canClick = {
            if (hash.winner == null)
                canClick(it)
            else
                false
        },
        onBlockClick = onBlockClick
    )

    if (hash.winner != null) {
        DrawWinner(
            winner = hash.winner,
            lineColor = winnerLineColor
        )
    }
}

@Composable
fun DrawWinner(
    winner: Hash.Winner,
    lineColor: Color,
    modifier: Modifier = Modifier
) = Canvas(modifier = modifier.fillMaxSize()) {

    val columnSize = size.width / 3
    val columnRadius = columnSize / 2

    val rowSize = size.height / 3
    val rowRadius = rowSize / 2

    fun drawRoundedLine(
        start: Offset,
        end: Offset
    ) = drawRoundedLine(
        color = lineColor,
        start = start,
        end = end,
        width = 8.dp.toPx()
    )

    when (winner) {
        is Hash.Winner.Column -> {

            val rowPadding = rowRadius / 2f

            val x = columnSize * winner.column.dec() + columnRadius
            val height = size.height - rowPadding

            drawRoundedLine(
                start = Offset(x = x, y = rowPadding),
                end = Offset(x = x, y = height)
            )
        }
        is Hash.Winner.Row -> {

            val columnPadding = columnRadius / 2f

            val y = rowSize * winner.row.dec() + rowRadius
            val width = size.width - columnPadding

            drawRoundedLine(
                start = Offset(x = columnPadding, y = y),
                end = Offset(x = width, y = y)
            )
        }
        Hash.Winner.Diagonal.Normal -> {

            val rowPadding = rowRadius / 2f
            val columnPadding = columnRadius / 2f

            val width = size.width - rowPadding
            val height = size.height - columnPadding

            drawRoundedLine(
                start = Offset(x = rowPadding, y = columnPadding),
                end = Offset(x = width, y = height)
            )
        }
        Hash.Winner.Diagonal.Inverted -> {

            val rowPadding = rowRadius / 2f
            val columnPadding = columnRadius / 2f

            val width = size.width - rowPadding
            val height = size.height - columnPadding

            drawRoundedLine(
                start = Offset(x = rowPadding, y = height),
                end = Offset(x = width, y = columnPadding)
            )
        }
    }
}

@Composable
private fun DrawSymbols(
    hash: Hash,
    lineSymbolsColors: Color,
    modifier: Modifier = Modifier,
    canClick: (Hash.Block) -> Boolean = { true },
    onBlockClick: OnBlockClick? = null
) = BoxWithConstraints(modifier = modifier) {

    val rowsSize = remember { maxHeight / 3 }
    val columnsSize = remember { maxWidth / 3 }

    for (row in Hash.KEY_RANGE) {
        for (column in Hash.KEY_RANGE) {
            Block(
                block = Hash.Block(
                    row = row,
                    column = column,
                    symbol = hash.get(row, column)
                ),
                linePlayersColors = lineSymbolsColors,
                canClick = canClick,
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
    modifier: Modifier,
    linePlayersColors: Color,
    canClick: (Hash.Block) -> Boolean,
    onClick: OnBlockClick? = null
) = Box(
    modifier = onClick?.let onClick@{
        if (!canClick(block)) return@onClick modifier
        modifier.clickable { it(block) }
    } ?: modifier,
    contentAlignment = Alignment.Center
) {
    if (block.symbol != null) {
        Player(
            symbol = block.symbol,
            linePlayersColors = linePlayersColors,
            modifier = Modifier.fillMaxSize(
                fraction = 0.5f
            )
        )
    }
}

@Composable
private fun Player(
    symbol: Hash.Symbol,
    modifier: Modifier,
    linePlayersColors: Color
) = Canvas(modifier = modifier) {
    when (symbol) {
        Hash.Symbol.O -> {

            val radius = min(size.height, size.width) / 2f

            drawRoundedCircle(
                color = linePlayersColors,
                radius = radius,
                center = center
            )
        }
        Hash.Symbol.X -> {

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
    end: Offset,
    width : Float = DEFAULT_LINE_WIDTH
) = drawLine(
    color = color,
    start = start,
    end = end,
    strokeWidth = width,
    cap = StrokeCap.Round
)

fun DrawScope.drawRoundedCircle(
    color: Color,
    center: Offset,
    radius: Float,
    width : Float = DEFAULT_LINE_WIDTH
) = drawCircle(
    color = color,
    radius = radius,
    center = center,
    style = Stroke(
        width = width,
        cap = StrokeCap.Round
    )
)

@Preview(showBackground = true)
@Composable
fun HashTablePreview() {
    SquareBox {
        HashTable(
            hash = Hash(
                winner = Hash.Winner.Diagonal.Normal
            ).apply {
                set(Hash.Symbol.O, 1, 1)
                set(Hash.Symbol.O, 2, 2)
                set(Hash.Symbol.O, 3, 3)
            }
        )
    }
}