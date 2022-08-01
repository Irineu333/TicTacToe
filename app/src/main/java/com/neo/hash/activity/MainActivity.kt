package com.neo.hash.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.neo.hash.BuildConfig
import com.neo.hash.activity.viewModel.MainViewModel
import com.neo.hash.singleton.GlobalFlow
import com.neo.hash.ui.components.ErrorDialog
import com.neo.hash.ui.screen.mainScreen.MainScreen
import com.neo.hash.ui.screen.updateScreen.UpdateScreen
import com.neo.hash.ui.theme.HashBackground
import com.neo.hash.ui.theme.HashTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var interstitial: InterstitialAd? = null
    private var adRequest = AdRequest.Builder().build()

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        loadInterstitial()

        setupListeners()

        setContent {
            HashTheme {
                HashBackground(Modifier.fillMaxSize()) {

                    var error by remember { mutableStateOf("") }

                    if (error.isNotEmpty()) {
                        ErrorDialog(
                            message = error,
                            onDismiss = {
                                error = ""
                            }
                        )
                    }

                    MainScreen(
                        viewModel = viewModel,
                        showInterstitial = { onSuccess ->
                            showInterstitial(
                                onSuccess = onSuccess,
                                onError = {
                                    error = it
                                }
                            )
                        }
                    )

                    UpdateScreen()
                }
            }
        }
    }

    private fun setupListeners() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                GlobalFlow.difficulty.collectLatest { difficulty ->
                    showInterstitial(
                        onSuccess = {
                            viewModel.addPoints(difficulty)
                        }
                    )
                }
            }
        }
    }

    private fun loadInterstitial(
        onError: ((String) -> Unit)? = null,
        onSuccess: (() -> Unit)? = null,
    ) {
        if (onSuccess != null) {
            Toast.makeText(
                this,
                "Carregando anúncio",
                Toast.LENGTH_SHORT
            ).show()
        }

        InterstitialAd.load(
            this,
            BuildConfig.INTERSTITIAL_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitial: InterstitialAd) {
                    this@MainActivity.interstitial = interstitial
                    onSuccess?.invoke()
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitial = null
                    onError?.invoke("Não foi possivel carregar o anúncio.")
                }
            }
        )
    }

    private fun showInterstitial(
        onError: ((String) -> Unit)? = null,
        onSuccess: (() -> Unit)? = null,
    ) {
        if (interstitial == null) {
            loadInterstitial(
                onSuccess = {
                    showInterstitial(
                        onSuccess = onSuccess,
                        onError = onError
                    )
                },
                onError = onError
            )
            return
        }

        interstitial!!.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                interstitial = null
                loadInterstitial()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when ad fails to show.
                interstitial = null
                loadInterstitial()
                onError?.invoke("Não foi possivel exibir o anúncio.")
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
                onSuccess?.invoke()
                viewModel.interstitialSkipReset()
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
            }
        }

        interstitial!!.show(this)
    }
}