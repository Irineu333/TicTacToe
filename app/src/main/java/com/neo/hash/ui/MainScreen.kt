@file:OptIn(ExperimentalAnimationApi::class)

package com.neo.hash.ui

import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.neo.hash.BuildConfig
import com.neo.hash.model.Screen
import com.neo.hash.ui.screen.HomeScreen
import com.neo.hash.ui.screen.gameScreen.GameScreen
import com.neo.hash.util.extensions.enterToLeftTransition
import com.neo.hash.util.extensions.enterToRightTransition
import com.neo.hash.util.extensions.exitToLeftTransition
import com.neo.hash.util.extensions.exitToRightTransition
import com.neo.hash.util.extensions.isCurrent

@Composable
fun MainScreen(
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween
) {
    val controller = rememberAnimatedNavController()

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        factory = { context ->
            AdView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
            }
        },
        update = { view ->
            view.adUnitId = BuildConfig.BANNER_ID

            view.setAdSize(AdSize.BANNER)
            view.loadAd(AdRequest.Builder().build())
        }
    )

    AnimatedNavHost(
        navController = controller,
        startDestination = Screen.HomeScreen.route
    ) {
        composable(
            route = Screen.HomeScreen.route,
            enterTransition = {
                fadeIn()
            },
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
            arguments = listOf(
                navArgument(Screen.GameScreen.isPhone) {
                    type = NavType.BoolType
                }
            ),
            enterTransition = { enterToLeftTransition },
            popExitTransition = { exitToRightTransition }
        ) { backStackEntry ->

            GameScreen(
                onHomeClick = {
                    if (controller isCurrent backStackEntry) {
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

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen()
}