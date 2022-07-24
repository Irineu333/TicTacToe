package com.neo.hashgame.ui.screen.game

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neo.hashgame.HashGameBackground
import com.neo.hashgame.model.Hash
import com.neo.hashgame.model.Player
import com.neo.hashgame.ui.components.GameButton
import com.neo.hashgame.ui.components.GameDialog
import com.neo.hashgame.ui.components.HashTable
import com.neo.hashgame.ui.components.Players
import com.neo.hashgame.ui.components.SquareBox
import com.neo.hashgame.ui.theme.HashGameTheme
import kotlin.random.Random

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit = {},
    isPhone: Boolean = false,
    viewModel: GameViewModel = viewModel()
) = Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {

    val state = viewModel.uiState.collectAsState().value
    Players(
        players = state.players,
        modifier = Modifier
            .fillMaxWidth(),
        playing = state.playing
    )

    Spacer(modifier = Modifier.height(16.dp))

    SquareBox(
        modifier = Modifier.padding(16.dp)
    ) {
        HashTable(
            hash = state.hash,
            onBlockClick = {
                viewModel.select(it.row, it.column)
            },
            canClick = {
                viewModel.canClick(it.row, it.column)
            }
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    GameButton(onClick = onHomeClick) {
        Text(text = "Home".uppercase())
    }

    var finishing by remember { mutableStateOf(false) }

    if (state.players.isEmpty() && !finishing) {
        InsertPlayersDialog(
            viewModel = viewModel,
            vsPhone = isPhone,
            onDismissRequest = {
                finishing = true
                onHomeClick()
            }
        )
    }
}

@Composable
fun InsertPlayersDialog(
    viewModel: GameViewModel,
    vsPhone: Boolean = false,
    onDismissRequest: () -> Unit
) {

    var person1 by remember { mutableStateOf("") }
    var person2 by remember { mutableStateOf(if (vsPhone) "Smartphone" else "") }

    val isNotBlank = person1.isNotBlank() && person2.isNotBlank()
    val isError = person1 == person2 && isNotBlank

    val context = LocalContext.current

    fun confirm() {
        if (!isNotBlank) {
            Toast.makeText(context, "Preencha todos os campos", Toast.LENGTH_LONG).show()
            return
        }

        if (isError) {
            Toast.makeText(context, "Nomes não podem ser iguais", Toast.LENGTH_LONG).show()
            return
        }

        val symbol1 = if (Random.nextBoolean())
            Hash.Symbol.O else Hash.Symbol.X

        val symbol2 = when (symbol1) {
            Hash.Symbol.O -> Hash.Symbol.X
            Hash.Symbol.X -> Hash.Symbol.O
        }

        viewModel.start(
            Player.Person(
                name = person1,
                symbol = symbol1
            ),
            if (vsPhone) {
                Player.Phone(
                    symbol = symbol2
                )
            } else {
                Player.Person(
                    name = person2,
                    symbol = symbol2
                )
            }
        )
    }

    GameDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = "Players".uppercase(),
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary
            )
        },
        buttons = {
            GameButton(
                onClick = {
                    confirm()
                },
                enabled = isNotBlank && !isError
            ) {
                Text(text = "Confirmar".uppercase())
            }
        }
    ) {
        OutlinedTextField(
            value = person1,
            onValueChange = {
                person1 = it
            },
            isError = isError,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                imeAction = if (vsPhone)
                    ImeAction.Done else ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    confirm()
                }
            ),
            singleLine = true,
            label = {
                Text(text = "Player 1")
            }
        )

        OutlinedTextField(
            value = person2,
            onValueChange = {
                person2 = it
            },
            isError = isError,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    confirm()
                }
            ),
            singleLine = true,
            readOnly = vsPhone,
            label = {
                Text(text = "Player 2")
            }
        )
    }

}


@Preview(showBackground = true)
@Composable
private fun GameScreenPreview() {
    HashGameTheme {
        HashGameBackground {
            GameScreen(
                viewModel = GameViewModel().apply {
                    start(
                        Player.Person("Test1", Hash.Symbol.X),
                        Player.Person("Test2", Hash.Symbol.O)
                    )
                }
            )
        }
    }
}