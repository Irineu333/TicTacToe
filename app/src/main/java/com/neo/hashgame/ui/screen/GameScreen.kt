package com.neo.hashgame.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.neo.hashgame.HashGameBackground
import com.neo.hashgame.model.Hash
import com.neo.hashgame.ui.components.HashTable
import kotlin.random.Random

@Composable
fun GameScreen(
    modifier: Modifier = Modifier
) = Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {

    var hash by remember { mutableStateOf(Hash()) }

    HashTable(
        onBlockClick = {
            hash = hash.update {
                val player = if (Random.nextBoolean()) Hash.Player.X else Hash.Player.O
                set(player, it.row, it.column)
            }
        },
        modifier = Modifier.size(200.dp),
        hash = hash
    )
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    HashGameBackground {
        GameScreen()
    }
}