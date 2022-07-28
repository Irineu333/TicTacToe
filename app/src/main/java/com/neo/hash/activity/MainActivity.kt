package com.neo.hash.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.neo.hash.BuildConfig
import com.neo.hash.ui.MainScreen
import com.neo.hash.ui.screen.updateScreen.UpdateScreen
import com.neo.hash.ui.theme.HashBackground
import com.neo.hash.ui.theme.HashTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var interstitialAd: InterstitialAd? = null
    private val snackbarHostState = SnackbarHostState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        loadInterstitial()

        setContent {
            HashTheme {
                HashBackground(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MainScreen(
                        showInterstitial = this::showInterstitial
                    )

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                        ) {

                            Snackbar(
                                snackbarData = it
                            )
                        }
                    }

                    UpdateScreen(
                        openPlayStore = this::openPlayStore
                    )
                }
            }
        }
    }

    private fun showInterstitial(
        onSuccess: () -> Unit
    ) {
        if (interstitialAd == null) {
            loadInterstitial(
                onSuccess = onSuccess
            )
        } else {
            interstitialAd!!.show(this)
            interstitialAd!!.fullScreenContentCallback = AdCallback(
                onSuccess = onSuccess
            )
        }
    }

    private fun openPlayStore() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$packageName")
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }

    private fun loadInterstitial(
        onSuccess: (() -> Unit)? = null
    ) {

        if (onSuccess != null) {
            showSnackbar(
                message = "Carregando anúncio..."
            )
        }

        InterstitialAd.load(
            this,
            BuildConfig.INTERSTITIAL_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    this@MainActivity.interstitialAd = interstitialAd
                    onSuccess?.invoke()
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    showSnackbar("Erro ao carregar anúncio")
                }
            }
        )
    }

    private fun showSnackbar(
        message: String
    ) = lifecycleScope.launch {
        snackbarHostState.showSnackbar(message)
    }

    inner class AdCallback(
        private val onSuccess: () -> Unit
    ) : FullScreenContentCallback() {
        override fun onAdClicked() {
            // Called when a click is recorded for an ad.

        }

        override fun onAdDismissedFullScreenContent() {
            // Called when ad is dismissed.

            interstitialAd = null
            loadInterstitial()
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            // Called when ad fails to show.

            interstitialAd = null
            loadInterstitial()

            showSnackbar("Erro ao exibir anúncio")
        }

        override fun onAdImpression() {
            // Called when an impression is recorded for an ad.

        }

        override fun onAdShowedFullScreenContent() {
            // Called when ad is shown.
            onSuccess()
        }
    }
}