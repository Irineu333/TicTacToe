package com.neo.hash.ui.screen.gameScreen.viewModel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.neo.hash.BuildConfig
import com.neo.hash.dataStoreRepository
import com.neo.hash.exceptions.HardFailureException
import com.neo.hash.model.Difficulty
import com.neo.hash.model.Hash
import com.neo.hash.model.Player
import com.neo.hash.singleton.Coclew
import com.neo.hash.singleton.GlobalFlow
import com.neo.hash.util.extensions.findType
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import timber.log.Timber

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

    private var referenceCode: String = ""

    private val isCoclewMode
        get() = referenceCode.isNotEmpty() &&
                Coclew.enabled.value == true

    init {
        setupListener()
    }

    private fun setupListener() = viewModelScope.launch {
        dataStoreRepository.referenceCodeFlow.collectLatest {
            referenceCode = it
        }
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
        if (state.playerWinner != null) return
        if (state.hash.get(row, column) != null) return

        val newHash = state.hash.copy().apply {
            set(playerTurn.symbol, row, column)
        }

        getWinner(newHash)?.let { winner ->
            _uiState.update { state ->

                val playerWinner = when (val first = winner.first) {
                    is Player.Person -> first.copy(
                        windsCount = first.windsCount + 1
                    )
                    is Player.Phone -> first.copy(
                        windsCount = first.windsCount + 1
                    )
                }

                val players = state.players.map {
                    if (it == winner.first) playerWinner else it
                }

                val hashWinner = winner.second

                state.copy(
                    players = players,
                    playerWinner = playerWinner,
                    hash = newHash,
                    winner = hashWinner,
                    playerTurn = null
                )
            }

            state.players.findType<Player.Phone>()?.let { phone ->
                if (winner.first is Player.Person) {
                    // count points
                    if (isCoclewMode) {

                        Timber.i("reference code: $referenceCode")

                        viewModelScope.launch {
                            GlobalFlow.addPoints(phone.difficulty)
                        }
                    }

                    // report hard mode failure
                    if (phone.difficulty == Difficulty.HARD) {

                        val hardFailureException = HardFailureException(
                            phone,
                            winner.first as Player.Person,
                            newHash.log
                        )

                        Timber.e(hardFailureException)
                    }
                }

                phoneFinishEvent(
                    phone = phone,
                    phoneWin = winner.first is Player.Phone
                )
            }
            return
        }

        if (newHash.isTie()) {
            _uiState.update {

                it.players.findType<Player.Phone>()?.let { phone ->
                    phoneFinishEvent(phone = phone, null)
                }

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
                    val delay = launch { delay(500) }

                    with(state.playerTurn) {
                        when (difficulty) {
                            Difficulty.EASY -> intelligent.easy(state.hash)
                            Difficulty.MEDIUM -> if (isCoclewMode)
                                intelligent.mediumCoclew(state.hash) else
                                intelligent.medium(state.hash)
                            Difficulty.HARD -> intelligent.hard(state.hash)
                        }
                    }.also {
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

        newGameEvent(players)
    }

    private fun newGameEvent(players: List<Player>) {
        Firebase.analytics.logEvent(
            "new_game${
                players.findType<Player.Phone>()?.let { phone ->
                    "_intelligent_${
                        when (phone.difficulty) {
                            Difficulty.EASY -> "easy"
                            Difficulty.MEDIUM -> "medium"
                            Difficulty.HARD -> "hard"
                        }
                    }${if (isCoclewMode) "_coclew" else ""}"
                } ?: ""
            }", Bundle.EMPTY
        )
    }

    private fun phoneFinishEvent(phone: Player.Phone, phoneWin: Boolean?) {
        Firebase.analytics.logEvent(
            "${
                when (phoneWin) {
                    true -> "win_phone"
                    false -> "win_person"
                    null -> "pie"
                }
            }_${
                when (phone.difficulty) {
                    Difficulty.EASY -> "easy"
                    Difficulty.MEDIUM -> "medium"
                    Difficulty.HARD -> "hard"
                }
            }${if (isCoclewMode) "_coclew" else ""}", Bundle.EMPTY
        )
    }

    fun canClick(row: Int, column: Int): Boolean {
        val state = uiState.value
        val symbol = state.hash.get(row, column)
        val playing = state.playerTurn
        val winner = state.playerWinner

        val disabledAi = { playing is Player.Phone && !playing.isEnabled }
        val playingIsPerson = { playing is Player.Person }

        return symbol == null && winner == null && (playingIsPerson() || disabledAi())
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
                playerTurn = lastStatedPlayer,
                winner = null
            )
        }

        playIntelligent()

        newGameEvent(uiState.value.players)
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
                        windsCount = player.windsCount
                    )

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

                val newPhone = player.copy(
                    isEnabled = !player.isEnabled
                )

                _uiState.update { state ->

                    val newPlayers = state.players.map {
                        if (player == it) newPhone else it
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

                if (newPhone.isEnabled) {
                    playIntelligent()
                }
            }
        }
    }
}
