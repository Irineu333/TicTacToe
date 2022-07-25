package com.neo.hashgame.ui.screen.game

import com.neo.hashgame.model.Hash
import com.neo.hashgame.model.Player

data class GameUiState(
    val players: List<Player> = listOf(),
    val playerTurn: Player? = null,
    val playerWinner: Player? = null,
    val tied: Int = 0,
    val isPlaying: Boolean = false,
    val hash: Hash = Hash()
)