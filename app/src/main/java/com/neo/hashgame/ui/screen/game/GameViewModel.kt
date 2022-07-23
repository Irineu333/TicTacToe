package com.neo.hashgame.ui.screen.game

import androidx.lifecycle.ViewModel
import com.neo.hashgame.model.Hash
import com.neo.hashgame.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private var _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()

    fun select(row: Int, column: Int) {
        _uiState.update { state ->

            val player = state.player ?: error("invalid state")

            state.copy(
                hash = state.hash.copy().apply {
                    set(player.symbol, row, column)
                },
                player = state.players.findLast { it != player }
            )
        }
    }

    fun start(person: Player.Person, player: Player) {
        _uiState.update {
            it.copy(
                players = listOf(
                    person,
                    player
                ),
                player = if (Random.nextBoolean())
                    person
                else
                    player
            )
        }
    }
}

data class GameUiState(
    val players: List<Player> = listOf(),
    val player: Player? = null,
    val hash: Hash = Hash()
)