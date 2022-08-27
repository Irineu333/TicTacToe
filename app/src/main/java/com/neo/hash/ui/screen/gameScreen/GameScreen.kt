package com.neo.hash.ui.screen.gameScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neo.hash.R
import com.neo.hash.model.Difficulty
import com.neo.hash.model.Hash
import com.neo.hash.model.Player
import com.neo.hash.ui.components.GameButton
import com.neo.hash.ui.components.HashTable
import com.neo.hash.ui.components.SquareBox
import com.neo.hash.ui.screen.gameScreen.viewModel.GameViewModel
import com.neo.hash.ui.screen.gameScreen.viewModel.Match
import com.neo.hash.ui.theme.HashTheme

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    isCoclewMode: Boolean = false,
    onHomeClick: () -> Unit = {},
    showInterstitial: (Boolean, () -> Unit) -> Unit = { _, _ -> },
    viewModel: GameViewModel
) = Column(
    modifier = modifier
        .padding(top = 16.dp)
        .fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {

    val state = viewModel.uiState.collectAsState().value

    AnimatedVisibility(visible = state.tied > 0) {
        Card(modifier = Modifier.padding(bottom = 16.dp)) {
            Row(
                modifier = Modifier
                    .padding(
                        vertical = 4.dp,
                        horizontal = 16.dp
                    )
                    .height(IntrinsicSize.Min)
            ) {
                Text(
                    text = stringResource(
                        R.string.text_tired
                    ).uppercase(),
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${state.tied}",
                    fontSize = 16.sp
                )
            }
        }
    }

    val players = state.match.players

    AnimatedVisibility(
        visible = players.isNotEmpty(),
        modifier = Modifier
            .padding(bottom = 16.dp)
    ) {
        Players(
            players = players,
            playing = state.playerTurn,
            onDebugClick = {
                if (it is Player.Phone) {
                    viewModel.onDebug(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
        )
    }

    BoxWithConstraints {
        SquareBox {
            HashTable(
                hash = state.hash,
                winner = state.winner,
                onBlockClick = {
                    viewModel.select(it.row, it.column)
                },
                canClick = {
                    viewModel.canClick(it.row, it.column)
                },
                animSymbols = true,
                modifier = Modifier
                    .padding(16.dp)
                    .matchParentSize()
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    AnimatedVisibility(visible = players.isNotEmpty()) {
        Row {
            AnimatedVisibility(
                visible = !isCoclewMode || state.playerTurn == null
            ) {
                GameButton(
                    onClick = {
                        showInterstitial(false) {
                            viewModel.clear()
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.btn_clean).uppercase())
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            GameButton(onClick = onHomeClick) {
                Text(text = stringResource(R.string.btn_start).uppercase())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameScreenPreview() {
    HashTheme {
        GameScreen(
            viewModel = viewModel(
                factory = GameViewModel.Factory(
                    Match(
                        players = listOf(
                            Player.Phone(Hash.Symbol.O),
                            Player.Person(Hash.Symbol.X, "Irineu")
                        )
                    )
                )
            )
        )
    }
}