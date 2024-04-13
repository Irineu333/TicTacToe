package com.neo.hash.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PhoneAndroid
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.neo.hash.BuildConfig
import com.neo.hash.model.Hash
import com.neo.hash.model.StartDialog
import com.neo.hash.ui.components.GameButton
import com.neo.hash.ui.components.HashTable
import com.neo.hash.ui.components.SquareBox
import com.neo.hash.ui.screen.gameScreen.PlayersInsertDialog
import com.neo.hash.ui.screen.gameScreen.viewModel.Match
import com.neo.hash.ui.theme.HashBackground
import com.neo.hash.ui.theme.HashTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onStartMatch: (Match) -> Unit = { _ -> },
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = modifier.fillMaxSize()
) {

    var startDialog by remember { mutableStateOf<StartDialog?>(null) }

    if (startDialog != null) {
        PlayersInsertDialog(
            onStartMatch = onStartMatch,
            startDialog = startDialog!!,
            onDismissRequest = {
                startDialog = null
            },
        )
    }

    BoxWithConstraints(Modifier.fillMaxWidth(0.5f)) {
        SquareBox(
            modifier = Modifier.border(
                width = 2.dp,
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(8.dp)
            )
        ) {
            HashTable(
                hash = remember {
                    Hash().apply {
                        set(Hash.Symbol.X, 1, 3)
                        set(Hash.Symbol.X, 2, 2)
                        set(Hash.Symbol.X, 3, 1)

                        set(Hash.Symbol.O, 1, 2)
                        set(Hash.Symbol.O, 2, 1)
                        set(Hash.Symbol.O, 3, 3)
                    }
                },
                winner = Hash.Winner.Diagonal.Inverted,
                modifier = Modifier
                    .padding(16.dp)
                    .matchParentSize()
            )
        }
    }

    Text(
        text = BuildConfig.VERSION_NAME,
        modifier = Modifier
            .padding(vertical = 4.dp)
    )

    Spacer(modifier = Modifier.height(16.dp))

    GameButton(
        onClick = { startDialog = StartDialog.VsIntelligent }
    ) {
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

    GameButton(
        onClick = { startDialog = StartDialog.VsPerson }
    ) {
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

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HashTheme {
        HashBackground {
            HomeScreen()
        }
    }
}