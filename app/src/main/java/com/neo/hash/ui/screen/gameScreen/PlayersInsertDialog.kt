package com.neo.hash.ui.screen.gameScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SyncAlt
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.neo.hash.R
import com.neo.hash.model.Difficulty
import com.neo.hash.model.Hash
import com.neo.hash.model.Player
import com.neo.hash.model.StartDialog
import com.neo.hash.singleton.Coclew
import com.neo.hash.ui.components.GameButton
import com.neo.hash.ui.components.GameDialog
import com.neo.hash.ui.components.Symbol
import com.neo.hash.ui.screen.gameScreen.viewModel.Match
import kotlin.random.Random

@Composable
fun PlayersInsertDialog(
    startDialog: StartDialog,
    onStartMatch: (Match, () -> Unit) -> Unit = { _, _ -> },
    isCoclewMode: Boolean = false,
    onDismissRequest: () -> Unit = {}
) = when (startDialog) {
    StartDialog.Multiplayer -> TODO()
    StartDialog.VsIntelligent, StartDialog.VsPerson -> {
        OfflineMatch(
            isVsIntelligent = startDialog is StartDialog.VsIntelligent,
            onStartMatch = onStartMatch,
            onDismissRequest = onDismissRequest,
            isCoclewMode = isCoclewMode
        )
    }
}

@Composable
private fun OfflineMatch(
    isVsIntelligent: Boolean,
    onStartMatch: (Match, () -> Unit) -> Unit,
    onDismissRequest: () -> Unit,
    isCoclewMode: Boolean
) {
    val textSmartphone = stringResource(R.string.text_artificial_intelligence)

    var player1 by rememberSaveable { mutableStateOf("") }
    var player2 by rememberSaveable { mutableStateOf(if (isVsIntelligent) textSmartphone else "") }

    val isNotBlank = player1.isNotBlank() && player2.isNotBlank()
    val isError = player1 == player2 && isNotBlank

    var difficultySelection by rememberSaveable {
        mutableStateOf(Difficulty.values().random())
    }

    var symbols by rememberSaveable {
        val symbol1 = if (Random.nextBoolean())
            Hash.Symbol.O else Hash.Symbol.X

        val symbol2 = when (symbol1) {
            Hash.Symbol.O -> Hash.Symbol.X
            Hash.Symbol.X -> Hash.Symbol.O
        }

        mutableStateOf(listOf(symbol1, symbol2))
    }

    fun confirm() {
        onStartMatch(
            Match(
                listOf(
                    Player.Person(
                        name = player1,
                        symbol = symbols[0]
                    ),
                    if (isVsIntelligent) {
                        Player.Phone(
                            symbol = symbols[1],
                            difficulty = difficultySelection
                        )
                    } else {
                        Player.Person(
                            name = player2,
                            symbol = symbols[1]
                        )
                    }
                )
            ),
            onDismissRequest
        )
    }

    GameDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = false
        ),
        title = {
            Text(
                text = stringResource(R.string.text_players),
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                color = colors.primary
            )
        },
        content = {
            Box {
                Column {
                    OutlinedTextField(
                        value = player1,
                        onValueChange = {
                            player1 = it.trim()
                        },
                        isError = isError,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        label = {
                            Text(text = stringResource(R.string.text_player, 1))
                        },
                        trailingIcon = {
                            Symbol(symbol = symbols[0])
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = player2,
                        onValueChange = {
                            player2 = it.trim()
                        },
                        isError = isError,
                        singleLine = true,
                        readOnly = isVsIntelligent,
                        label = {
                            Text(text = stringResource(R.string.text_player, 2))
                        },
                        trailingIcon = {
                            Symbol(symbol = symbols[1])
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                IconButton(
                    onClick = {
                        symbols = symbols.asReversed()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SyncAlt,
                        contentDescription = null,
                        modifier = Modifier.rotate(90f),
                        tint = contentColorFor(colors.background)
                    )
                }
            }

            if (isVsIntelligent) {
                val points by Coclew.points.collectAsState()

                LazyRow(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    val difficulties = Difficulty.values()
                    val lastIndex = difficulties.size - 1

                    for ((index, difficulty) in difficulties.withIndex()) {
                        item {
                            OutlinedButton(
                                onClick = {
                                    difficultySelection = difficulty
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = if (difficulty == difficultySelection)
                                        contentColorFor(colors.primary)
                                    else colors.primary,
                                    backgroundColor = if (difficulty == difficultySelection)
                                        colors.primary
                                    else
                                        colors.background

                                )
                            ) {
                                Text(
                                    text = when (difficulty) {
                                        Difficulty.EASY -> stringResource(R.string.text_easy)
                                        Difficulty.MEDIUM -> stringResource(R.string.text_medium)
                                        Difficulty.HARD -> stringResource(R.string.text_hard)
                                    }
                                )

                                if (isCoclewMode && points != null) {
                                    Text(
                                        text = ":" + when (difficulty) {
                                            Difficulty.EASY -> points!!.easy
                                            Difficulty.MEDIUM -> points!!.medium
                                            Difficulty.HARD -> points!!.hard
                                        }
                                    )
                                }
                            }
                        }

                        if (index < lastIndex) {
                            item { Spacer(modifier = Modifier.width(8.dp)) }
                        }
                    }
                }

                Divider(modifier = Modifier.padding(top = 8.dp))
            }
        },
        buttons = {

            GameButton(
                onClick = onDismissRequest
            ) {
                Text(text = stringResource(R.string.btn_cancel))
            }

            Spacer(modifier = Modifier.weight(1f))

            GameButton(
                onClick = ::confirm,
                enabled = isNotBlank && !isError
            ) {
                Text(text = stringResource(R.string.btn_confirm))
            }
        }
    )
}

@Preview
@Composable
private fun PlayersInsertDialogPreview() {
    PlayersInsertDialog(
        onDismissRequest = {},
        startDialog = StartDialog.VsIntelligent
    )
}