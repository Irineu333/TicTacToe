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

            val player = state.playing ?: return

            state.copy(
                hash = state.hash.copy().apply {
                    set(player.symbol, row, column)
                },
                playing = state.players.find {
                    it != player
                }
            )
        }

        verifyWin()
    }

    private fun verifyWin() {
        val state = uiState.value
        val hash = state.hash

        val partialRange = 2..3

        fun getRowWinner(row: Int): Hash.Symbol? {
            val symbol = hash.get(row, 1)
            return symbol.takeIf {
                partialRange.all { hash.get(row, it) == symbol }
            }
        }

        fun columnWinner(column: Int): Hash.Symbol? {
            val symbol = hash.get(1, column)
            return symbol.takeIf {
                partialRange.all { hash.get(it, column) == symbol }
            }
        }

        fun diagonalWinner(): Hash.Symbol? {
            val symbol = hash.get(1, 1)
            return symbol.takeIf {
                partialRange.all { hash.get(it, it) == symbol }
            }
        }

        fun invertedDiagonalWinner(): Hash.Symbol? {
            val symbol = hash.get(1, 3)

            return symbol.takeIf {
                partialRange.all {
                    hash.get(it, 4.minus(it)) == symbol
                }
            }
        }

        fun getWinner(): Player? {
            for (row in Hash.KEY_RANGE) {
                val rowWinner = getRowWinner(row)
                if (rowWinner != null) {
                    return state.players.find {
                        it.symbol == rowWinner
                    }
                }
            }

            for (column in Hash.KEY_RANGE) {
                val columnWinner = columnWinner(column)

                if (columnWinner != null) {
                    return state.players.find {
                        it.symbol == columnWinner
                    }
                }
            }

            val diagonalWinner = diagonalWinner()

            if (diagonalWinner != null) {
                return state.players.find {
                    it.symbol == diagonalWinner
                }
            }

            val invertedDiagonalWinner = invertedDiagonalWinner()

            if (invertedDiagonalWinner != null) {
                return state.players.find {
                    it.symbol == invertedDiagonalWinner
                }
            }

            return null
        }

        val winner = getWinner()

        if (winner != null) {
            _uiState.update {
                it.copy(
                    winner = when(winner) {
                        is Player.Person -> winner.copy(
                            windsCount = winner.windsCount + 1
                        )
                        is Player.Phone -> winner.copy(
                            windsCount = winner.windsCount + 1
                        )
                    }
                )
            }
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
