package com.neo.hash.ui.screen.gameScreen.viewModel

import com.neo.hash.model.Hash
import com.neo.hash.model.Player

data class GameUiState(
    val match: Match,
    val playerTurn: Player? = null,
    val playerWinner: Player? = null,
    val tied: Int = 0,
    val winner: Hash.Winner? = null,
    val hash: Hash = Hash()
)

data class Match(
    val players: List<Player>
) {
    companion object {
        var match: Match? = null
    }
}