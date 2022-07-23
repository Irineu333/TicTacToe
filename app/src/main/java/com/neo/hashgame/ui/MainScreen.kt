@file:OptIn(ExperimentalAnimationApi::class)

package com.neo.hashgame.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.neo.hashgame.model.Screen
import com.neo.hashgame.ui.screen.HomeScreen
import com.neo.hashgame.ui.screen.game.GameScreen
import com.neo.hashgame.util.extensions.enterToLeftTransition
import com.neo.hashgame.util.extensions.enterToRightTransition
import com.neo.hashgame.util.extensions.exitToLeftTransition
import com.neo.hashgame.util.extensions.exitToRightTransition

@Composable
fun MainScreen() {
    val controller = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = controller,
        startDestination = Screen.HomeScreen.route,
    ) {
        composable(
            route = Screen.HomeScreen.route,
            exitTransition = { exitToLeftTransition },
            popEnterTransition = { enterToRightTransition }
        ) {
            HomeScreen(
                onPlayPeople = { 
                    controller.navigate(Screen.GameScreen.route)
                }
            )
        }

        composable(
            route = Screen.GameScreen.route,
            enterTransition = { enterToLeftTransition },
            popExitTransition = { exitToRightTransition }
        ) {
            GameScreen(
                onHomeClick = {
                    controller.popBackStack()
                }
            )
        }
    }
}