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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.neo.hash.BuildConfig
import com.neo.hash.R
import com.neo.hash.activity.viewModel.MainViewModel
import com.neo.hash.model.Screen
import com.neo.hash.singleton.Coclew
import com.neo.hash.ui.components.ErrorDialog
import com.neo.hash.ui.screen.HomeScreen
import com.neo.hash.ui.screen.gameScreen.GameScreen
import com.neo.hash.ui.screen.gameScreen.viewModel.GameViewModel
import com.neo.hash.ui.screen.gameScreen.viewModel.Match
import com.neo.hash.ui.theme.Coclew
import com.neo.hash.ui.theme.CoclewDark
import com.neo.hash.util.extensions.*

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
    showInterstitial: ((() -> Unit)?) -> Unit = { }
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

    var showMaintenance by rememberSaveable { mutableStateOf(false) }

    val primaryColor by animateColorAsState(
        if (referenceCode.isUid())
            Color.Coclew else colors.primary
    )

    val primaryVariantColor by animateColorAsState(
        if (referenceCode.isUid())
            Color.CoclewDark else colors.primaryVariant
    )

    MaterialTheme(
        colors.copy(
            primary = primaryColor,
            primaryVariant = primaryVariantColor
        )
    ) {

        val showFeature = !referenceCode.isNullOrEmpty() ||
                isHomeScreenCurrent &&
                coclewEnabled == true

        AnimatedVisibility(coclewEnabled != null && showFeature) {
            CoclewIdentifier(
                modifier = modifier.padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
                referenceCode = referenceCode ?: "",
                isMaintenance = coclewEnabled == false &&
                        !referenceCode.isNullOrEmpty(),
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


        if (showMaintenance) {
            ErrorDialog(
                onDismiss = {
                    showMaintenance = false
                },
                title = stringResource(R.string.title_warning),
                message = stringResource(R.string.text_maintenance)
            )
        }

        val isCoclewMode = referenceCode.isUid() && coclewEnabled == true

        var matchState by rememberSaveable { mutableStateOf<Match?>(null) }

        AnimatedNavHost(
            navController = controller,
            startDestination = Screen.HomeScreen.route,
            modifier = Modifier
                .weight(weight = 1f)
                .verticalScroll(rememberScrollState())
        ) {

            fun mustShowInterstitial(
                ignoreSkip: Boolean,
                onSuccess: () -> Unit
            ) {
                when {
                    !referenceCode!!.isUid() || coclewEnabled == null -> {
                        onSuccess()
                        showInterstitial(null)
                    }
                    coclewEnabled == false -> showMaintenance = true
                    ignoreSkip -> showInterstitial(onSuccess)
                    viewModel.isSkipInterstitial() -> onSuccess()
                    else -> showInterstitial(onSuccess)
                }
            }

            composable(
                route = Screen.HomeScreen.route,
                enterTransition = {
                    fadeIn()
                },
                exitTransition = { exitToLeftTransition },
                popEnterTransition = { enterToRightTransition }
            ) { backStackEntry ->

                HomeScreen(
                    onStartMatch = { match, onSuccess ->
                        mustShowInterstitial(ignoreSkip = true) {
                            if (controller isCurrent backStackEntry) {
                                matchState = match
                                controller.navigate(Screen.GameScreen.route)
                                onSuccess()
                            }
                        }
                    },
                    isCoclewMode = isCoclewMode
                )
            }

            composable(
                route = Screen.GameScreen.route,
                enterTransition = { enterToLeftTransition },
                popExitTransition = { exitToRightTransition }
            ) { backStackEntry ->

                GameScreen(
                    onHomeClick = {
                        if (controller isCurrent backStackEntry) {
                            controller.popBackStack()
                        }
                    },
                    isCoclewMode = !referenceCode.isNullOrEmpty(),
                    showInterstitial = { onSuccess ->
                        mustShowInterstitial(
                            ignoreSkip = false,
                            onSuccess
                        )
                    },
                    viewModel = viewModel(
                        factory = GameViewModel.Factory(
                            match = matchState ?: error("invalid match")
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
}
