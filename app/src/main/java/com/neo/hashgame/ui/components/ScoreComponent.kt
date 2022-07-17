package com.neo.hashgame.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neo.hashgame.model.Score
import com.neo.hashgame.util.extensions.joinToAnnotatedString


@Composable
fun ScoreComponent(
    modifier: Modifier = Modifier,
    score: Score
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val texts = """
            Jogou: ${score.plays}
            Ganhou: ${score.hasWon}
            Perdeu: ${score.lost}
        """.trimIndent()

        val regex = Regex("\\w+:\\s?\\d+")

        val results = regex.findAll(texts).toList()

        val result = results.joinToAnnotatedString(
            separator = "\n"
        ) {

            val value = it.value

            withStyle(
                SpanStyle(
                    color = Color.Black
                )
            ) {
                append(value.substringBefore(":"))
            }

            append(":")

            withStyle(
                SpanStyle(
                    color = Color.Blue
                )
            ) {
                append(value.substringAfter(":"))
            }
        }

        Text(
            text = result,
            fontSize = 16.sp,
            lineHeight = 20.sp,
        )
    }
}
