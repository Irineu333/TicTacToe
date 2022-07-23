package com.neo.hashgame.ui.screen.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.neo.hashgame.HashGameBackground
import com.neo.hashgame.model.Hash
import com.neo.hashgame.ui.components.GameButton
import com.neo.hashgame.ui.components.HashTable
import com.neo.hashgame.ui.components.Players
import com.neo.hashgame.ui.components.SquareBox
import com.neo.hashgame.ui.theme.HashGameTheme

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    onHomeClick : () -> Unit = {},
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
        modifier = Modifier.padding(36.dp)
    ) {
        HashTable(
            onBlockClick = {
                viewModel.select(it.row, it.column)
            },
            hash = state.hash
        )
    }

    GameButton(onClick = onHomeClick) {
        Text(text = "Home")
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