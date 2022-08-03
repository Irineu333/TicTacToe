@file:OptIn(ExperimentalFoundationApi::class)

package com.neo.hash.ui.screen.gameScreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neo.hash.BuildConfig
import com.neo.hash.R
import com.neo.hash.model.Difficulty
import com.neo.hash.model.Hash
import com.neo.hash.model.Player
import com.neo.hash.ui.components.Symbol

@Composable
fun Players(
    players: List<Player>,
    modifier: Modifier = Modifier,
    playing: Player? = null,
    onDebugClick: (Player) -> Unit = {}
) = Row(
    modifier = modifier
        .padding(horizontal = 8.dp),
) {
    for (player in players) {
        PlayerCard(
            player = player,
            playing = playing == player,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f)
                .fillMaxWidth(),
            onDebugClick = onDebugClick
        )
    }
}

@Composable
private fun PlayerCard(
    player: Player,
    modifier: Modifier,
    playing: Boolean = false,
    onDebugClick: (Player) -> Unit = {}
) {

    val isPhone = player is Player.Phone

    Card(
        modifier = if (BuildConfig.DEBUG) {
            modifier.combinedClickable(
                onClick = {},
                onLongClick = {
                    onDebugClick(player)
                }
            )
        } else modifier,
        backgroundColor = when {
            isPhone && !(player as Player.Phone).isEnabled -> Color.Red
            playing -> Color(0xFFEEEEEE)
            else -> colors.surface
        },
    ) {
        Row(
            modifier = Modifier
                .padding(
                    vertical = 4.dp,
                    horizontal = 8.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Symbol(
                    symbol = player.symbol,
                    modifier = Modifier.height(20.dp)
                )

                if (isPhone && playing && (player as Player.Phone).isEnabled) {
                    CircularProgressIndicator(
                        strokeWidth = 2.dp,
                        modifier = Modifier.matchParentSize(),
                        color = colors.primary.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.padding(4.dp))

            val name = buildAnnotatedString {
                when (player) {
                    is Player.Person -> {
                        append(player.name.uppercase())
                    }
                    is Player.Phone -> {

                        append("${stringResource(id = R.string.text_artificial_intelligence)}:")

                        withStyle(SpanStyle(colors.primary)) {
                            append(player.difficulty.name)
                        }
                    }
                }
            }

            Text(text = name, fontSize = 16.sp)

            Spacer(modifier = Modifier.weight(weight = 1f))

            Text(text = "${player.windsCount}", fontSize = 16.sp)
        }
    }
}

@Preview
@Composable
private fun PlayersPreview() {
    val players = listOf(
        Player.Phone(
            Hash.Symbol.O,
            difficulty = Difficulty.HARD
        ),
        Player.Person(
            Hash.Symbol.X,
            "Irineu"
        )
    )
    Players(
        players = players,
        playing = players[0],
        modifier = Modifier.fillMaxWidth()
    )
}