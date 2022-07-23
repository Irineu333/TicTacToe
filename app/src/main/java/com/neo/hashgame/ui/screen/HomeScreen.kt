package com.neo.hashgame.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neo.hashgame.model.Hash
import com.neo.hashgame.ui.components.GameButton
import com.neo.hashgame.ui.components.HashTable
import com.neo.hashgame.ui.components.SquareBox
import com.neo.hashgame.ui.theme.HashGameTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier
        .padding(32.dp)
        .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
) {

    SquareBox {
        HashTable(
            hash = Hash().apply {
                set(Hash.Player.X, 1, 3)
                set(Hash.Player.X, 2, 2)
                set(Hash.Player.X, 3, 1)

                set(Hash.Player.O, 1, 2)
                set(Hash.Player.O, 2, 1)
                set(Hash.Player.O, 3, 3)
            }
        )
    }

    Spacer(modifier = Modifier.padding(16.dp))

    GameButton(onClick = {}) {
        Row {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
            )

            Text(text = "vs", fontSize = 16.sp)

            Icon(
                imageVector = Icons.Default.PhoneAndroid,
                contentDescription = null
            )
        }
    }

    GameButton(onClick = {}) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
        )

        Text(text = "vs", fontSize = 16.sp)

        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HashGameTheme {
        HomeScreen()
    }
}