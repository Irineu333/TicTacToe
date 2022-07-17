package com.neo.hashgame.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neo.hashgame.model.Scores

@Composable
fun ScoresTable(
    modifier: Modifier = Modifier,
    scores: Scores
) = Column(
    modifier = modifier
        .border(
            width = 1.dp,
            color = MaterialTheme.colors.primary,
            shape = RoundedCornerShape(4.dp)
        )
) {
    Text(
        text = "Score",
        modifier = Modifier.fillMaxWidth(),
        fontSize = 18.sp,
        textAlign = TextAlign.Center,
    )

    Divider(
        color = MaterialTheme.colors.primary
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val scoresText = remember {
            listOf(
                "Jogou" to scores.plays,
                "Ganhou" to scores.hasWon,
                "Perdeu" to scores.lost
            )
        }

        scoresText.forEach { score ->
            ScoreText(
                score = score.first,
                score.second
            )
        }
    }
}

@Composable
fun ScoreText(
    score: String,
    value: Int
) = Text(
    text = buildAnnotatedString {
        withStyle(
            SpanStyle(
                color = Color.Black
            )
        ) {
            append(score)
        }

        append(":")

        withStyle(
            SpanStyle(
                color = Color.Blue
            )
        ) {
            append("$value")
        }
    }
)

@Preview(showBackground = true)
@Composable
private fun ScoresTablesPreview() {
    ScoresTable(
        scores = Scores(
            plays = 4,
            hasWon = 2,
            lost = 2
        )
    )
}