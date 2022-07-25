package com.neo.hashgame.ui.screen.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neo.hashgame.HashGameBackground
import com.neo.hashgame.model.Hash
import com.neo.hashgame.model.Player
import com.neo.hashgame.ui.components.GameButton
import com.neo.hashgame.ui.components.HashTable
import com.neo.hashgame.ui.components.Players
import com.neo.hashgame.ui.components.SquareBox
import com.neo.hashgame.ui.screen.PlayersInsertDialog
import com.neo.hashgame.ui.theme.HashGameTheme

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

    Card(modifier = Modifier.alpha(if (state.tied > 0) 1f else 0f)) {
        Row(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 8.dp)
                .height(IntrinsicSize.Min)
        ) {
            Text(text = "Velha", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "${state.tied}", fontSize = 16.sp)
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Players(
        players = state.players,
        modifier = Modifier
            .fillMaxWidth(),
        playing = state.playerTurn
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

    GameButton(
        onClick = {
            viewModel.clear()
        }
    ) {
        Text(text = "Limpar".uppercase())
    }

    var finishing by remember { mutableStateOf(false) }

    if (state.players.isEmpty() && !finishing) {
        PlayersInsertDialog(
            viewModel = viewModel,
            vsPhone = isPhone,
            onDismissRequest = {
                finishing = true
                onHomeClick()
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