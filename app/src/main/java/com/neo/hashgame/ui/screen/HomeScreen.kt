package com.neo.hashgame.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neo.hashgame.model.Score
import com.neo.hashgame.ui.components.ScoreComponent
import com.neo.hashgame.ui.theme.HashGameTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    score: Score,
    onPlayClick : () -> Unit
) = Column(
    modifier = modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
) {
    ScoreComponent(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        score = score
    )

    Button(onClick = onPlayClick) {
        Text(text = "JOGAR")
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HashGameTheme {
        HomeScreen(
            score = Score(
                plays = 3,
                hasWon = 2,
                lost = 1
            )
        ) {

        }
    }
}