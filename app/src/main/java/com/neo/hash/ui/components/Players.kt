package com.neo.hash.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neo.hash.R
import com.neo.hash.model.Hash
import com.neo.hash.model.Player

@Composable
fun Players(
    players: List<Player>,
    modifier: Modifier = Modifier,
    playing: Player? = null
) = Row(
    modifier = modifier.padding(horizontal = 8.dp),
) {
    for (player in players) {
        PlayerCard(
            player = player,
            playing = playing == player,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun PlayerCard(
    player: Player,
    modifier: Modifier,
    playing: Boolean = false
) = Card(
    modifier = modifier,
    backgroundColor = if (playing)
        Color(0xFFEEEEEE) else
        MaterialTheme.colors.surface
) {
    Row(
        modifier = Modifier
            .padding(
                vertical = 4.dp,
                horizontal = 8.dp
            ).height(IntrinsicSize.Min)
    ) {
        Box {
            Symbol(symbol = player.symbol)
            if (player is Player.Phone && playing) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.matchParentSize()
                )
            }
        }

        Spacer(modifier = Modifier.padding(4.dp))

        val name = when (player) {
            is Player.Person -> player.name
            is Player.Phone -> stringResource(R.string.text_smartphone)
        }

        Text(text = name.uppercase(), fontSize = 16.sp)

        Spacer(modifier = Modifier.weight(1f))

        Text(text = "${player.windsCount}", fontSize = 16.sp)
    }
}

@Composable
fun Symbol(
    symbol: Hash.Symbol,
    color: Color = MaterialTheme.colors.primary
) {

    when (symbol) {
        Hash.Symbol.O -> {
            Icon(
                imageVector = Icons.Outlined.Circle,
                tint = color,
                contentDescription = stringResource(R.string.text_circle),
                modifier = Modifier.padding(2.dp)
            )
        }
        Hash.Symbol.X -> {
            Icon(
                imageVector = Icons.Outlined.Close,
                tint = color,
                contentDescription = "x"
            )
        }
    }
}


@Preview
@Composable
private fun PlayersPreview() {
    val players = listOf(
        Player.Phone(
            Hash.Symbol.O
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