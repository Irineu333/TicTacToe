package com.neo.hashgame.ui.screen.game

import androidx.lifecycle.ViewModel
import com.neo.hashgame.model.Hash
import com.neo.hashgame.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private var _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()

    fun select(row: Int, column: Int) {
        _uiState.update {
            it.copy(
                hash = it.hash.copy().apply {
                    set(Hash.Symbol.X, row, column)
                }
            )
        }
    }
}

data class GameUiState(
    val players: List<Player> = listOf(
        Player.Phone(
            Hash.Symbol.O
        ),
        Player.Person(
            "Irineu",
            Hash.Symbol.X
        )
    ),
    val player: Player? = null,
    val hash: Hash = Hash()
)