package com.neo.hashgame.ui.screen.game

import com.neo.hashgame.model.Hash
import com.neo.hashgame.model.Player

data class GameUiState(
    val players: List<Player> = listOf(),
    val playing: Player? = null,
    val hash: Hash = Hash()
)