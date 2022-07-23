package com.neo.hashgame.model

data class Hash(
    private val symbols: Array<Array<Symbol?>> = Array(KEY_RANGE.last) {
        Array(KEY_RANGE.last) { null }
    }
) {
    fun set(
        symbol: Symbol,
        row: Int,
        column: Int
    ) {
        symbols[row - 1][column - 1] = symbol
    }

    fun get(
        row: Int,
        column: Int
    ) = symbols[row - 1][column - 1]

    override fun equals(other: Any?): Boolean {
        if (other is Hash) {
            for (row in KEY_RANGE) {
                for (column in KEY_RANGE) {
                    if (other.get(row, column) != get(row, column)) {
                        return false
                    }
                }
            }
        }

        return false
    }

    override fun hashCode(): Int {
        return symbols.contentDeepHashCode()
    }

    data class Block(
        val symbol: Symbol? = null,
        val row: Int,
        val column: Int
    )

    enum class Symbol {
        O,
        X
    }

    companion object {
        val KEY_RANGE = 1..3
    }
}