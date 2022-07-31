@file:OptIn(ExperimentalAnimationApi::class)

package com.neo.hash.ui.screen.mainScreen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.neo.hash.BuildConfig
import com.neo.hash.activity.viewModel.MainViewModel
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
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
    showInterstitial: (() -> Unit) -> Unit = { }
) = Column(
    modifier = modifier
        .fillMaxSize(),
    verticalArrangement = Arrangement.SpaceBetween
) {
    val controller = rememberAnimatedNavController()
    val route = controller.currentBackStackEntryAsState().value?.destination?.route
    val referenceCode = viewModel.referenceCodeFlow.collectAsState(initial = "").value

    AnimatedVisibility(
        route == Screen.HomeScreen.route ||
                referenceCode.isNotEmpty()
    ) {
        CoclewIdentifier(
            modifier = modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
            referenceCode = referenceCode,
            onAddReferenceCode = { code ->
                viewModel.setReferenceCode(code)
            },
            onRemoveReferenceCode = {
                viewModel.clearReferenceCode()
            }
        )
    }

    AnimatedNavHost(
        navController = controller,
        startDestination = Screen.HomeScreen.route,
        modifier = Modifier
            .weight(1f)
            .verticalScroll(rememberScrollState())
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
                        controller.navigate(Screen.GameScreen.getRoute(vsPhone))
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
                againstIntelligent = backStackEntry.arguments!!.getBoolean(
                    Screen.GameScreen.isPhone
                ),
                showInterstitial = { ignoreSkip, onSuccess ->
                    when {
                        referenceCode.isEmpty() -> onSuccess()
                        ignoreSkip -> showInterstitial(onSuccess)
                        viewModel.isSkipInterstitial() -> onSuccess()
                        else -> showInterstitial(onSuccess)
                    }
                }
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
            }
        }
    )
}