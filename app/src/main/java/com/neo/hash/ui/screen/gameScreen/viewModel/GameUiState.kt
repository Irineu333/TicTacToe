package com.neo.hash.ui.screen.gameScreen.viewModel

import com.neo.hash.model.Difficulty
import com.neo.hash.model.Hash
import com.neo.hash.model.Player

data class GameUiState(
    val players: List<Player>,
    val playerTurn: Player? = null,
    val playerWinner: Player? = null,
    val tied: Int = 0,
    val winner: Hash.Winner? = null,
    val hash: Hash = Hash()
)

data class Match(
    val players: List<Player>
) {
    data class Player(
        val name: String,
        val symbol: Hash.Symbol,
        val type: Type,
        val difficulty: Difficulty? = null
    ) {
        enum class Type {
            PERSON,
            PHONE
        }
    }
}

fun Match.Player.toModel() = when (type) {
    Match.Player.Type.PERSON -> Player.Person(
        symbol = symbol,
        name = name
    )
    Match.Player.Type.PHONE -> Player.Phone(
        symbol = symbol,
        difficulty = difficulty ?: error("invalid difficulty")
    )
}