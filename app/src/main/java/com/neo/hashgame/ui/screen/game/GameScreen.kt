package com.neo.hashgame.ui.screen.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    smartphone: Boolean = false,
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
            .fillMaxWidth()
    )

    SquareBox(
        modifier = Modifier.padding(16.dp)
    ) {
        HashTable(
            onBlockClick = {
                viewModel.select(it.row, it.column)
            },
            hash = state.hash
        )
    }

    GameButton(onClick = onHomeClick) {
        Text(text = "Home".uppercase())
    }

    if (state.players.isEmpty()) {
        InsertPlayers(
            viewModel = viewModel,
            smartphone = smartphone
        )
    }
}

@Composable
fun InsertPlayers(
    viewModel: GameViewModel,
    smartphone: Boolean = false
) {

    var person1 by remember { mutableStateOf("") }
    var person2 by remember { mutableStateOf(if (smartphone) "Smartphone" else "") }

    val isNotEmpty = person1.isNotBlank() && person2.isNotBlank()
    val isError = person1 == person2 && isNotEmpty


    GameDialog(
        onDismissRequest = {},
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
                        if (smartphone) {
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
                },
                enabled = isNotEmpty && !isError
            ) {
                Text(text = "CONFIRMAR".uppercase())
            }
        }
    ) {
        OutlinedTextField(
            value = person1,
            onValueChange = {
                person1 = it
            },
            isError = isError,
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
            readOnly = smartphone,
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
            GameScreen()
        }
    }
}