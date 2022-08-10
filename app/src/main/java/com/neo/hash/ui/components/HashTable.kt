package com.neo.hash.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.neo.hash.model.Hash

private val Density.DEFAULT_LINE_WIDTH
    get() = 4.dp.toPx()

@Composable
fun HashTable(
    hash: Hash,
    modifier: Modifier = Modifier,
    winner: Hash.Winner? = null,
    canClick: (Hash.Block) -> Boolean = { winner != null },
    onBlockClick: ((Hash.Block) -> Unit)? = null,
    hashColor: Color = MaterialTheme.colors.onBackground,
    symbolsColor: Color = MaterialTheme.colors.primary,
    animSymbols : Boolean = false,
    winnerLineColor: Color = symbolsColor.copy(alpha = 0.4f)
) = Box(modifier = modifier) {
    DrawBackground(hashColor)
    DrawForeground(
        hash = hash,
        winner = winner,
        symbolsColor = symbolsColor,
        animSymbols = animSymbols,
        winnerLineColor = winnerLineColor,
        canClick = canClick,
        onBlockClick = onBlockClick
    )
}

@Composable
private fun BoxScope.DrawForeground(
    hash: Hash,
    winner: Hash.Winner?,
    symbolsColor: Color,
    winnerLineColor: Color,
    animSymbols : Boolean = false,
    canClick: (Hash.Block) -> Boolean = { true },
    onBlockClick: ((Hash.Block) -> Unit)? = null
) = Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier.matchParentSize()
) {
    DrawSymbols(
        hash = hash,
        lineSymbolsColors = symbolsColor,
        animSymbols = animSymbols,
        canClick = canClick,
        onBlockClick = onBlockClick
    )

    if (winner != null) {
        DrawWinner(
            winner = winner,
            lineColor = winnerLineColor,
        )
    }
}

@Composable
fun BoxScope.DrawWinner(
    winner: Hash.Winner,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    val linesSize = remember { Animatable(0f) }

    LaunchedEffect(key1 = winner) {
        linesSize.animateTo(1f)
    }

    Canvas(modifier = modifier.matchParentSize()) {

        val columnSize = size.width / 3f
        val columnRadius = columnSize / 2f

        val rowSize = size.height / 3f
        val rowRadius = rowSize / 2f

        fun drawRoundedLine(
            start: Offset,
            end: Offset
        ) = drawRoundedLine(
            color = lineColor,
            start = start,
            end = end,
            width = 10.dp.toPx()
        )

        when (winner) {
            is Hash.Winner.Column -> {

                val rowPadding = rowRadius / 2f

                val x = columnSize * winner.column.dec() + columnRadius

                val height = size.height - rowPadding * 2f

                val aHeight = height * linesSize.value

                drawRoundedLine(
                    start = Offset(
                        x = x,
                        y = rowPadding
                    ),
                    end = Offset(
                        x = x,
                        y = aHeight + rowPadding
                    )
                )
            }
            is Hash.Winner.Row -> {

                val columnPadding = columnRadius / 2f

                val y = rowSize * winner.row.dec() + rowRadius

                val width = size.width - columnPadding * 2f

                val aWidth = width * linesSize.value

                drawRoundedLine(
                    start = Offset(
                        x = columnPadding,
                        y = y
                    ),
                    end = Offset(
                        x = aWidth + columnPadding,
                        y = y
                    )
                )
            }
            Hash.Winner.Diagonal.Normal -> {

                val rowPadding = rowRadius / 2f
                val columnPadding = columnRadius / 2f

                val width = size.width - columnPadding * 2f
                val height = size.height - rowPadding * 2f

                val aWidth = width * linesSize.value
                val aHeight = height * linesSize.value

                drawRoundedLine(
                    start = Offset(
                        x = columnPadding,
                        y = rowPadding
                    ),
                    end = Offset(
                        x = aWidth + columnPadding,
                        y = aHeight + rowPadding
                    )
                )
            }
            Hash.Winner.Diagonal.Inverted -> {

                val rowPadding = rowRadius / 2f
                val columnPadding = columnRadius / 2f

                val width = size.width - columnPadding * 2f
                val height = size.height - rowPadding * 2f

                val aWidth = width * linesSize.value
                val aHeight = height * linesSize.value

                drawRoundedLine(
                    start = Offset(
                        x = columnPadding,
                        y = height + rowPadding
                    ),
                    end = Offset(
                        x = aWidth + columnPadding,
                        y = (height - aHeight) + rowPadding
                    )
                )
            }
        }
    }
}

@Composable
private fun BoxScope.DrawSymbols(
    hash: Hash,
    lineSymbolsColors: Color,
    modifier: Modifier = Modifier,
    animSymbols : Boolean = false,
    canClick: (Hash.Block) -> Boolean = { true },
    onBlockClick: ((Hash.Block) -> Unit)? = null
) = BoxWithConstraints(
    modifier = modifier.matchParentSize()
) {

    val rowsSize = maxHeight / 3f
    val columnsSize = maxWidth / 3f

    for (row in Hash.KEY_RANGE) {
        for (column in Hash.KEY_RANGE) {
            Block(
                block = Hash.Block(
                    row = row,
                    column = column,
                    symbol = hash.get(row, column)
                ),
                linePlayersColors = lineSymbolsColors,
                animSymbol = animSymbols,
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
    animSymbol: Boolean = false,
    canClick: (Hash.Block) -> Boolean,
    onClick: ((Hash.Block) -> Unit)? = null
) = Box(
    modifier = onClick?.let {
        if (!canClick(block)) {
            modifier
        } else {
            modifier.clickable { it(block) }
        }
    } ?: modifier,
    contentAlignment = Alignment.Center
) {
    if (block.symbol != null) {

        val drawAnim = remember { Animatable(initialValue = if (animSymbol) 0f else 2f) }

        LaunchedEffect(key1 = block.symbol) {
            drawAnim.animateTo(
                targetValue = 2f
            )
        }

        Player(
            symbol = block.symbol,
            linePlayersColors = linePlayersColors,
            drawAnim = drawAnim.value,
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
    linePlayersColors: Color,
    drawAnim: Float
) = Canvas(modifier = modifier) {
    when (symbol) {
        Hash.Symbol.O -> {

            val size = Size(size.height, size.width)

            drawRoundedCircle(
                color = linePlayersColors,
                size = size,
                sweepAngle = 360f * drawAnim / 2f
            )
        }
        Hash.Symbol.X -> {

            val width1 = when (drawAnim) {
                in 0f..1f -> size.width * drawAnim
                else -> size.width
            }

            val height1 = when (drawAnim) {
                in 0f..1f -> size.height * drawAnim
                else -> size.height
            }

            val width2 = when (drawAnim) {
                in 1f..2f -> size.width * drawAnim.dec()
                else -> 0f
            }

            val height2 = when (drawAnim) {
                in 1f..2f -> size.height * drawAnim.dec()
                else -> 0f
            }

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
                    x = width1,
                    y = height1
                )
            )

            drawLine(
                start = Offset(
                    x = width2,
                    y = size.height - height2
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
private fun BoxScope.DrawBackground(
    lineHashColor: Color
) = Canvas(
    modifier = Modifier.matchParentSize()
) {

    val rowsSize = size.height / 3f
    val columnsSize = size.width / 3f

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
    width: Float = DEFAULT_LINE_WIDTH
) = drawLine(
    color = color,
    start = start,
    end = end,
    strokeWidth = width,
    cap = StrokeCap.Round
)

fun DrawScope.drawRoundedCircle(
    color: Color,
    size: Size,
    startAngle: Float = 0f,
    sweepAngle: Float = 360f,
    width: Float = DEFAULT_LINE_WIDTH
) = drawArc(
    color = color,
    startAngle = startAngle,
    sweepAngle = sweepAngle,
    size = size,
    useCenter = false,
    style = Stroke(
        width = width,
        cap = StrokeCap.Round
    )
)

@Preview(showBackground = true)
@Composable
fun HashTablePreview() {
    BoxWithConstraints {
        SquareBox {
            HashTable(
                hash = Hash().apply {
                    set(Hash.Symbol.X, 1, 1)
                    set(Hash.Symbol.O, 2, 2)
                    set(Hash.Symbol.X, 3, 3)
                },
                winner = Hash.Winner.Diagonal.Normal,
                modifier = Modifier.matchParentSize()
            )
        }
    }
}