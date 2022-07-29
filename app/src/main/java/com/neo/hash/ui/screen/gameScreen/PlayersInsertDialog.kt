package com.neo.hash.ui.screen.gameScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.neo.hash.R
import com.neo.hash.model.Hash
import com.neo.hash.model.Player
import com.neo.hash.ui.components.GameButton
import com.neo.hash.ui.components.GameDialog
import com.neo.hash.ui.screen.gameScreen.viewModel.GameViewModel
import kotlin.random.Random

@Composable
fun PlayersInsertDialog(
    viewModel: GameViewModel,
    vsPhone: Boolean = false,
    showInterstitial: (() -> Unit) -> Unit = { },
    onDismissRequest: () -> Unit = {}
) {
    val textSmartphone = stringResource(R.string.text_smartphone)

    var player1 by remember { mutableStateOf("") }
    var player2 by remember { mutableStateOf(if (vsPhone) textSmartphone else "") }

    val isNotBlank = player1.isNotBlank() && player2.isNotBlank()
    val isError = player1 == player2 && isNotBlank

    GameDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = false
        ),
        title = {
            Text(
                text = stringResource(R.string.text_players).uppercase(),
                modifier = Modifier
                    .align(Alignment.Center),
                textAlign = TextAlign.Center,
                color = colors.primary
            )
        },
        content = {
            OutlinedTextField(
                value = player1,
                onValueChange = {
                    player1 = it.uppercase().trim()
                },
                isError = isError,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                label = {
                    Text(text = stringResource(R.string.text_player, 1))
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = player2,
                onValueChange = {
                    player2 = it.uppercase().trim()
                },
                isError = isError,
                singleLine = true,
                readOnly = vsPhone,
                label = {
                    Text(text = stringResource(R.string.text_player, 2))
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        buttons = {
            GameButton(
                onClick = {
                    showInterstitial {

                        val symbol1 = if (Random.nextBoolean())
                            Hash.Symbol.O else Hash.Symbol.X

                        val symbol2 = when (symbol1) {
                            Hash.Symbol.O -> Hash.Symbol.X
                            Hash.Symbol.X -> Hash.Symbol.O
                        }

                        viewModel.start(
                            Player.Person(
                                name = player1,
                                symbol = symbol1
                            ),
                            if (vsPhone) {
                                Player.Phone(
                                    symbol = symbol2
                                )
                            } else {
                                Player.Person(
                                    name = player2,
                                    symbol = symbol2
                                )
                            }
                        )
                    }
                },
                enabled = isNotBlank && !isError
            ) {
                Text(text = stringResource(R.string.btn_confirm).uppercase())
            }
        }
    )
}

@Preview
@Composable
private fun PlayersInsertDialogPreview() {
    PlayersInsertDialog(
        viewModel = GameViewModel(),
        onDismissRequest = {}
    )
}