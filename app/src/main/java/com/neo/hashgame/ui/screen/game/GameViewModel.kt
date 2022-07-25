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

        _uiState.update { state ->

            val playerTurn = state.playerTurn ?: return

            val newHash = state.hash.copy().apply {
                set(playerTurn.symbol, row, column)
            }

            val newPlayerTurn = state.players.find {
                it != playerTurn
            }

            state.copy(
                hash = newHash,
                playerTurn = newPlayerTurn
            )
        }

        verifyWin()
    }

    fun start(player1: Player.Person, player2: Player) {

        val players = listOf(
            player1,
            player2
        )

        _uiState.update {
            it.copy(
                players = players,
                playerTurn = players.random(),
                isPlaying = true
            )
        }
    }

    private fun verifyWin() {
        val state = uiState.value
        val hash = state.hash

        val partialRange = 2..3

        fun getRowWinner(row: Int): Hash.Symbol? {
            val symbol = hash.get(row, 1)

            return symbol.takeIf {
                partialRange.all {
                    hash.get(row, it) == symbol
                }
            }
        }

        fun getColumnWinner(column: Int): Hash.Symbol? {
            val symbol = hash.get(1, column)

            return symbol.takeIf {
                partialRange.all { hash.get(it, column) == symbol }
            }
        }

        fun getDiagonalWinner(): Hash.Symbol? {
            val symbol = hash.get(1, 1)

            return symbol.takeIf {
                partialRange.all { hash.get(it, it) == symbol }
            }
        }

        fun getInvertedDiagonalWinner(): Hash.Symbol? {
            val symbol = hash.get(1, 3)

            return symbol.takeIf {
                partialRange.all {
                    hash.get(it, 4.minus(it)) == symbol
                }
            }
        }

        fun getWinner(): Pair<Player, Hash.Winner>? {
            for (row in Hash.KEY_RANGE) {
                val rowWinner = getRowWinner(row)

                if (rowWinner != null) {
                    return state.players.first {
                        it.symbol == rowWinner
                    } to Hash.Winner.Row(row)
                }
            }

            for (column in Hash.KEY_RANGE) {
                val columnWinner = getColumnWinner(column)

                if (columnWinner != null) {
                    return state.players.first {
                        it.symbol == columnWinner
                    } to Hash.Winner.Column(column)
                }
            }

            val diagonalWinner = getDiagonalWinner()

            if (diagonalWinner != null) {
                return state.players.first {
                    it.symbol == diagonalWinner
                } to Hash.Winner.Diagonal.Normal
            }

            val invertedDiagonalWinner = getInvertedDiagonalWinner()

            if (invertedDiagonalWinner != null) {
                return state.players.first {
                    it.symbol == invertedDiagonalWinner
                } to Hash.Winner.Diagonal.Inverted
            }

            return null
        }

        val winner = getWinner()

        if (winner != null) {
            _uiState.update {
                it.copy(
                    playerWinner = winner.first.apply {
                        windsCount++
                    },
                    hash = it.hash.copy(
                        winner = winner.second
                    ),
                    isPlaying = false,
                    playerTurn = null
                )
            }
            return
        }

        val aTie = Hash.KEY_RANGE.all { row ->
            Hash.KEY_RANGE.all { column ->
                hash.get(row, column) != null
            }
        }

        if (aTie) {
            _uiState.update {
                it.copy(
                    tied = it.tied + 1,
                    isPlaying = false,
                    playerTurn = null
                )
            }
            return
        }
    }

    fun canClick(row: Int, column: Int): Boolean {
        val state = uiState.value
        val symbol = state.hash.get(row, column)
        val playing = state.playerTurn

        return symbol == null && playing is Player.Person
    }

    fun clear() {
        _uiState.update {
            it.copy(
                hash = Hash(),
                playerWinner = null,
                playerTurn = it.playerWinner ?: it.players.random(),
                isPlaying = true
            )
        }
    }
}
