package com.neo.hash.ui.screen.gameScreen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neo.hash.BuildConfig
import com.neo.hash.model.Hash
import com.neo.hash.model.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel : ViewModel() {

    private var _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()

    private var lastStatedPlayer: Player? = null
    private var aiJob: Job? = null

    private val canRunIntelligent: Boolean
        get() = uiState.value.let {
            it.playerTurn is Player.Phone
                    && it.playerTurn.isEnabled
        }

    fun select(row: Int, column: Int) {
        val state = uiState.value

        val playerTurn = state.playerTurn ?: return

        when (playerTurn) {
            is Player.Person -> {
                internalSelect(row, column)
            }
            is Player.Phone -> {
                if (BuildConfig.DEBUG && !playerTurn.isEnabled) {
                    internalSelect(row, column)
                }
            }
        }
    }

    private fun internalSelect(row: Int, column: Int) {
        val state = uiState.value

        val playerTurn = state.playerTurn ?: return

        val newHash = state.hash.copy().apply {
            check(get(row, column) == null) { "invalid selection" }
            set(playerTurn.symbol, row, column)
        }

        getWinner(newHash)?.let { winner ->
            _uiState.update {
                it.copy(
                    playerWinner = winner.first.apply {
                        windsCount++
                    },
                    hash = newHash,
                    winner = winner.second,
                    playerTurn = null
                )
            }
            return
        }

        if (newHash.isTie()) {
            _uiState.update {
                it.copy(
                    tied = it.tied + 1,
                    hash = newHash,
                    playerTurn = null
                )
            }
            return
        }

        val newPlayerTurn = state.players.first {
            it != playerTurn
        }

        _uiState.update {
            it.copy(
                hash = newHash,
                playerTurn = newPlayerTurn
            )
        }

        playIntelligent()
    }

    private fun playIntelligent() {
        if (!canRunIntelligent) return

        aiJob?.cancel()
        aiJob = viewModelScope.launch {

            val state = uiState.value

            if (state.playerTurn is Player.Phone) {
                val (row, column) = withContext(Dispatchers.Default) {
                    val delay = launch { delay(1000) }

                    state.playerTurn.ai.hard(state.hash).also {
                        delay.join()
                    }
                }

                if (canRunIntelligent) {
                    internalSelect(row, column)
                }
            }
        }
    }

    private fun getWinner(hash: Hash): Pair<Player, Hash.Winner>? {
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

        val players = uiState.value.players

        for (row in Hash.KEY_RANGE) {
            val rowWinner = getRowWinner(row)

            if (rowWinner != null) {
                return players.first {
                    it.symbol == rowWinner
                } to Hash.Winner.Row(row)
            }
        }

        for (column in Hash.KEY_RANGE) {
            val columnWinner = getColumnWinner(column)

            if (columnWinner != null) {
                return players.first {
                    it.symbol == columnWinner
                } to Hash.Winner.Column(column)
            }
        }

        val diagonalWinner = getDiagonalWinner()

        if (diagonalWinner != null) {
            return players.first {
                it.symbol == diagonalWinner
            } to Hash.Winner.Diagonal.Normal
        }

        val invertedDiagonalWinner = getInvertedDiagonalWinner()

        if (invertedDiagonalWinner != null) {
            return players.first {
                it.symbol == invertedDiagonalWinner
            } to Hash.Winner.Diagonal.Inverted
        }

        return null
    }

    fun start(player1: Player.Person, player2: Player) {

        val players = listOf(
            player1,
            player2
        )


        _uiState.update {
            lastStatedPlayer = players.random()

            it.copy(
                players = players,
                playerTurn = lastStatedPlayer
            )
        }

        playIntelligent()
    }

    fun canClick(row: Int, column: Int): Boolean {
        val state = uiState.value
        val symbol = state.hash.get(row, column)
        val playing = state.playerTurn

        val disabledAi = { playing is Player.Phone && !playing.isEnabled }

        return symbol == null && (playing is Player.Person || disabledAi())
    }

    fun clear() {

        aiJob?.cancel()

        _uiState.update { state ->

            lastStatedPlayer = state.players.first {
                it != lastStatedPlayer
            }

            state.copy(
                hash = Hash(),
                playerWinner = null,
                playerTurn = state.playerWinner ?: lastStatedPlayer,
                winner = null
            )
        }

        playIntelligent()
    }

    fun onDebug(player: Player) {

        if (!BuildConfig.DEBUG) {
            error("function only available in debug")
        }

        when (player) {
            is Player.Person -> {
                _uiState.update { state ->
                    val newPlayerPhone = Player.Phone(
                        symbol = player.symbol,
                    ).apply {
                        windsCount = player.windsCount
                    }

                    val newPlayers = state.players.map {
                        if (player == it) newPlayerPhone else it
                    }

                    state.copy(
                        players = newPlayers,
                        playerTurn = state.playerTurn?.let { turn ->
                            newPlayers.first { it.symbol == turn.symbol }
                        },
                        playerWinner = state.playerWinner?.let { winner ->
                            newPlayers.first { it.symbol == winner.symbol }
                        }
                    )
                }

                playIntelligent()
            }
            is Player.Phone -> {

                player.isEnabled = !player.isEnabled

                _uiState.update {
                    it.copy()
                }

                if (player.isEnabled) {
                    playIntelligent()
                }
            }
        }
    }
}
