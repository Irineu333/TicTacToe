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
import com.neo.hash.ui.MainScreen
import com.neo.hash.ui.screen.updateScreen.UpdateScreen
import com.neo.hash.ui.theme.HashBackground
import com.neo.hash.ui.theme.HashTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            HashTheme {
                HashBackground(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    MainScreen()
                    UpdateScreen(
                        openPlayStore = ::openPlayStore
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
}