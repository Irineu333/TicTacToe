package com.neo.hash.model

import kotlin.math.abs

data class Hash(
    private val symbols: Array<Array<Symbol?>> = Array(KEY_RANGE.last) {
        Array(KEY_RANGE.last) { null }
    },
    val log: MutableList<Block> = mutableListOf()
) {
    fun set(
        symbol: Symbol,
        row: Int,
        column: Int
    ) {
        log.add(Block(row, column, symbol))
        symbols[row - 1][column - 1] = symbol
    }

    fun get(
        row: Int,
        column: Int
    ) = symbols[row - 1][column - 1]

    override fun equals(other: Any?): Boolean {

        if (other !is Hash) return false

        for (row in KEY_RANGE) {
            for (column in KEY_RANGE) {
                if (other.get(row, column) != get(row, column)) {
                    return false
                }
            }
        }

        return true
    }

    override fun hashCode(): Int {
        return symbols.contentDeepHashCode()
    }

    fun isTie() = KEY_RANGE.all { row ->
        KEY_RANGE.all { column ->
            get(row, column) != null
        }
    }

    fun isEmpty() = KEY_RANGE.all { row ->
        KEY_RANGE.all { column ->
            get(row, column) == null
        }
    }

    fun getAllSymbols() = buildList {
        for (row in KEY_RANGE) {
            for (column in KEY_RANGE) {
                val symbol = get(row, column)
                if (symbol != null) {
                    add(
                        Block(
                            row,
                            column,
                            symbol
                        )
                    )
                }
            }
        }
    }

    fun getAllEmpty() = buildList {
        for (row in KEY_RANGE) {
            for (column in KEY_RANGE) {
                val symbol = get(row, column)
                if (symbol == null) {
                    add(
                        Block(
                            row,
                            column
                        )
                    )
                }
            }
        }
    }

    data class Block(
        val row: Int,
        val column: Int,
        val symbol: Symbol? = null
    ) {
        fun isSide(center: Block) =
            abs(center.row - row) < 2 &&
                    abs(center.column - column) < 2
    }

    sealed class Winner {

        class Row(
            val row: Int,
        ) : Winner()

        class Column(
            val column: Int,
        ) : Winner()

        sealed class Diagonal : Winner() {
            object Normal : Diagonal()
            object Inverted : Diagonal()
        }
    }

    enum class Symbol {
        O,
        X
    }

    companion object {
        val KEY_RANGE = 1..3
    }
}