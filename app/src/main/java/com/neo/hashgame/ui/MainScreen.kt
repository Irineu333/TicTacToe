@file:OptIn(ExperimentalAnimationApi::class)

package com.neo.hashgame.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
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
        ) { backStackEntry ->
            HomeScreen(
                onPlayClick = { vsPhone ->
                    if (controller isCurrent backStackEntry) {
                        controller.navigate(Screen.GameScreen.route(vsPhone))
                    }
                }
            )
        }

        composable(
            route = Screen.GameScreen.route,
            arguments = listOf(navArgument(Screen.GameScreen.isPhone) { type = NavType.BoolType }),
            enterTransition = { enterToLeftTransition },
            popExitTransition = { exitToRightTransition }
        ) { backStackEntry ->

            GameScreen(
                onHomeClick = {
                    if (controller isCurrent  backStackEntry) {
                        controller.popBackStack()
                    }
                },
                isPhone = backStackEntry.arguments!!.getBoolean(
                    Screen.GameScreen.isPhone
                )
            )
        }
    }
}

infix fun NavHostController.isCurrent(
    stack: NavBackStackEntry
) = currentDestination?.id == stack.destination.id