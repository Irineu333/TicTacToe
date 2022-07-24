package com.neo.hashgame.ui.screen.game

import androidx.lifecycle.ViewModel
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

            val player = state.playing ?: error("invalid state")

            state.copy(
                hash = state.hash.copy().apply {
                    set(player.symbol, row, column)
                },
                playing = state.players.findLast { it != player }
            )
        }
    }

    fun start(player1: Player.Person, player2: Player) {
        _uiState.update {
            it.copy(
                players = listOf(
                    player1,
                    player2
                ),
                playing = if (Random.nextBoolean())
                    player1
                else
                    player2
            )
        }
    }

    fun canClick(row: Int, column: Int): Boolean {
        val state = uiState.value
        val symbol = state.hash.get(row, column)
        val playing = state.playing

        return symbol == null && playing is Player.Person
    }
}
