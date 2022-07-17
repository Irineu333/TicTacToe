package com.neo.hashgame.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.neo.hashgame.model.Scores
import com.neo.hashgame.model.Screen
import com.neo.hashgame.ui.screen.HomeScreen
import com.neo.hashgame.ui.theme.HashGameTheme
import com.neo.hashgame.util.extensions.enterToLeftTransition
import com.neo.hashgame.util.extensions.enterToRightTransition
import com.neo.hashgame.util.extensions.exitToLeftTransition
import com.neo.hashgame.util.extensions.exitToRightTransition

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val controller = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = controller,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(
            route = Screen.HomeScreen.route,
            exitTransition = { exitToLeftTransition },
            popEnterTransition = { enterToRightTransition }
        ) {
            HomeScreen(
                // TODO: mock
                scores = Scores(
                    plays = 3,
                    hasWon = 2,
                    lost = 1
                ),
                onPlayClick = {
                    controller.navigate(Screen.GameScreen.route)
                }
            )
        }

        composable(
            route = Screen.GameScreen.route,
            enterTransition = { enterToLeftTransition },
            popExitTransition = { exitToRightTransition }
        ) {
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