@file:OptIn(ExperimentalAnimationApi::class)

package com.neo.hash.ui.screen.mainScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.neo.hash.singleton.Coclew
import com.neo.hash.ui.components.ErrorDialog
import com.neo.hash.ui.screen.HomeScreen
import com.neo.hash.ui.screen.gameScreen.GameScreen
import com.neo.hash.ui.theme.Coclew
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

    val currentDestination = controller.currentBackStackEntryAsState().value?.destination
    val isHomeScreenCurrent = currentDestination?.route == Screen.HomeScreen.route

    val referenceCode = viewModel.referenceCodeFlow.collectAsState(initial = null).value

    val coclewEnabled = Coclew.enabled.collectAsState().value

    var showMaintenance by remember { mutableStateOf(false) }

    val primaryColor by animateColorAsState(
        if (!referenceCode.isNullOrEmpty() && coclewEnabled)
            Color.Coclew else colors.primary
    )

    MaterialTheme(colors.copy(primaryColor)) {
        if (referenceCode != null) {
            AnimatedVisibility(
                referenceCode.isNotEmpty() ||
                        isHomeScreenCurrent &&
                        coclewEnabled
            ) {
                CoclewIdentifier(
                    modifier = modifier.padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                    referenceCode = referenceCode,
                    isMaintenance = !coclewEnabled,
                    showMaintenance = {
                        showMaintenance = true
                    },
                    onAddReferenceCode = { code ->
                        viewModel.setReferenceCode(code)
                    },
                    onRemoveReferenceCode = {
                        viewModel.clearReferenceCode()
                    }
                )
            }
        }

        if (showMaintenance) {
            ErrorDialog(
                onDismiss = {
                    showMaintenance = false
                },
                title = "Aviso",
                message = "Estamos em manutenção."
            )
        }

        AnimatedNavHost(
            navController = controller,
            startDestination = Screen.HomeScreen.route,
            modifier = Modifier
                .weight(weight = 1f)
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
                            referenceCode!!.isEmpty() -> onSuccess()
                            !coclewEnabled -> showMaintenance = true
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
}