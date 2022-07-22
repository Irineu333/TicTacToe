package com.neo.hashgame.model

class Hash {

    private val data = mutableMapOf<String, Player>()

    fun set(
        player: Player,
        row: Int,
        column: Int
    ) = data.put(key(row, column), player)

    fun get(
        row: Int,
        column: Int
    ) = data[key(row, column)]

    private fun key(row: Int, column: Int) : String {

        require(KEY_RANGE.contains(row)) { "invalid row $row" }
        require(KEY_RANGE.contains(column)) { "invalid column $column" }

        return "($row,$column)"
    }

    enum class Player {
        O,
        X
    }

    companion object {
         val KEY_RANGE = 1..3
    }
}