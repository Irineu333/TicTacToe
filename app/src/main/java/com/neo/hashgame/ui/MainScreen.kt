package com.neo.hashgame.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neo.hashgame.model.Score
import com.neo.hashgame.model.Screen
import com.neo.hashgame.ui.screen.HomeScreen
import com.neo.hashgame.ui.theme.HashGameTheme

@Composable
fun MainScreen() {
    val controller = rememberNavController()

    NavHost(
        navController = controller,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(
                // TODO: mock
                score = Score(
                    plays = 3,
                    hasWon = 2,
                    lost = 1
                ),
                onPlayClick = {
                    controller.navigate(Screen.GameScreen.route)
                }
            )
        }

        composable(Screen.GameScreen.route) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Game Screen")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    HashGameTheme {
        MainScreen()
    }
}