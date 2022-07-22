package com.neo.hashgame.model

data class Hash(
    private val data: MutableMap<String, Player> = mutableMapOf()
) {
    fun set(
        player: Player,
        row: Int,
        column: Int
    ) = data.set(key(row, column), player)

    fun set(
        block: Block
    ) {
        requireNotNull(block.player) { "player cannot be null" }
        set(block.player, block.row, block.column)
    }

    fun get(
        row: Int,
        column: Int
    ) = data[key(row, column)]

    private fun key(row: Int, column: Int): String {

        require(KEY_RANGE.contains(row)) { "invalid row $row" }
        require(KEY_RANGE.contains(column)) { "invalid column $column" }

        return "($row,$column)"
    }

    fun update(function: Hash.() -> Unit) = copy().apply(function)

    override fun equals(
        other: Any?
    ) = if (other is Hash) {
        other === this
    } else false

    data class Block(
        val player: Player? = null,
        val row: Int,
        val column: Int
    )

    enum class Player {
        O,
        X
    }

    companion object {
        val KEY_RANGE = 1..3
    }
}