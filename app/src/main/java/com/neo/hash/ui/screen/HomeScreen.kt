package com.neo.hash.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neo.hash.BuildConfig
import com.neo.hash.model.Hash
import com.neo.hash.ui.components.GameButton
import com.neo.hash.ui.components.HashTable
import com.neo.hash.ui.components.SquareBox
import com.neo.hash.ui.theme.HashBackground
import com.neo.hash.ui.theme.HashTheme

@Composable
fun HomeScreen(
    onPlayClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier
        .fillMaxSize()
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.align(Alignment.Center)
    ) {
        SquareBox(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            HashTable(
                hash = Hash().apply {
                    set(Hash.Symbol.X, 1, 3)
                    set(Hash.Symbol.X, 2, 2)
                    set(Hash.Symbol.X, 3, 1)

                    set(Hash.Symbol.O, 1, 2)
                    set(Hash.Symbol.O, 2, 1)
                    set(Hash.Symbol.O, 3, 3)
                },
                winner = Hash.Winner.Diagonal.Inverted,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        GameButton(onClick = {
            onPlayClick(true)
        }) {
            Row {
                Icon(
                    imageVector = Icons.Rounded.Person,
                    contentDescription = null,
                )

                Text(text = "vs", fontSize = 16.sp)

                Icon(
                    imageVector = Icons.Rounded.PhoneAndroid,
                    contentDescription = null
                )
            }
        }

        GameButton(onClick = {
            onPlayClick(false)
        }) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
            )

            Text(text = "vs", fontSize = 16.sp)

            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
            )
        }
    }

    Text(
        text = BuildConfig.VERSION_NAME,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HashTheme {
        HashBackground {
            HomeScreen(onPlayClick = {})
        }
    }
}