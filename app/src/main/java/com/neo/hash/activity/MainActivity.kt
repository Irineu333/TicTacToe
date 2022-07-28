package com.neo.hash.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private var interstitialAd: InterstitialAd? = null

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
                        showInterstitial = {
                            interstitialAd?.show(this)
                        }
                    )
                    UpdateScreen(
                        openPlayStore = this::openPlayStore
                    )
                }
            }
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

    private fun loadInterstitial() {
        InterstitialAd.load(
            this,
            BuildConfig.INTERSTITIAL_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    this@MainActivity.interstitialAd = interstitialAd

                    configCallback()
                }


                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    Timber.e(adError.message)
                }
            }
        )
    }

    private fun configCallback() {
        interstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
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
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.

            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.

            }
        }
    }
}