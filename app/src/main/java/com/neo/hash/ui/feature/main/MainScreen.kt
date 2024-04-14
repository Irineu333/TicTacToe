@file:OptIn(ExperimentalAnimationApi::class)

package com.neo.hash.ui.feature.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.gson.Gson
import com.neo.hash.BuildConfig
import com.neo.hash.domain.model.Screen
import com.neo.hash.ui.feature.home.HomeScreen
import com.neo.hash.ui.feature.game.GameScreen
import com.neo.hash.ui.feature.game.viewModel.GameViewModel
import com.neo.hash.ui.feature.game.viewModel.Match
import com.neo.hash.util.extension.enterToLeftTransition
import com.neo.hash.util.extension.enterToRightTransition
import com.neo.hash.util.extension.exitToLeftTransition
import com.neo.hash.util.extension.exitToRightTransition
import com.neo.hash.util.extension.isCurrent

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier
        .fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween
) {
    val controller = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = controller,
        startDestination = Screen.HomeScreen.route,
        modifier = Modifier
            .weight(weight = 1f)
            .verticalScroll(rememberScrollState())
    ) {

        composable(
            route = Screen.HomeScreen.route,
            enterTransition = { fadeIn() },
            arguments = listOf(navArgument("match") { type = NavType.StringType }),
            exitTransition = { exitToLeftTransition },
            popEnterTransition = { enterToRightTransition }
        ) { backStackEntry ->

            HomeScreen(
                onStartMatch = { match ->
                    if (controller isCurrent backStackEntry) {
                        controller.navigate(Screen.GameScreen.route(match))
                    }
                },
            )
        }

        composable(
            route = Screen.GameScreen.route,
            enterTransition = { enterToLeftTransition },
            popExitTransition = { exitToRightTransition }
        ) { backStackEntry ->

            val match = backStackEntry.arguments!!.getString("match")
                .let { Gson().fromJson(it, Match::class.java) }

            GameScreen(
                onHomeClick = {
                    if (controller isCurrent backStackEntry) {
                        controller.popBackStack()
                    }
                },
                viewModel = viewModel(
                    factory = GameViewModel.Factory(
                        match = match
                    )
                )
            )
        }
    }

    val adRequest = remember { AdRequest.Builder().build() }

    AndroidView(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                adUnitId = BuildConfig.BANNER_ID
                setAdSize(AdSize.BANNER)
                loadAd(adRequest)
                adListener = object : AdListener() {
                    override fun onAdLoaded() {

                    }
                }
            }
        }
    )
}
