package com.neo.hash.ui.feature.game.viewModel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.neo.hash.domain.model.Difficulty
import com.neo.hash.domain.model.Hash
import com.neo.hash.domain.model.Player

data class GameUiState(
    val players: List<Player>,
    val playerTurn: Player? = null,
    val playerWinner: Player? = null,
    val tied: Int = 0,
    val winner: Hash.Winner? = null,
    val hash: Hash = Hash()
)

data class Match(
    @SerializedName("players")
    val players: List<Player>
) {
    data class Player(
        @SerializedName("name")
        val name: String,
        @SerializedName("symbol")
        val symbol: Hash.Symbol,
        @SerializedName("type")
        val type: Type,
        @SerializedName("difficulty")
        val difficulty: Difficulty? = null
    ) {

        @Keep
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