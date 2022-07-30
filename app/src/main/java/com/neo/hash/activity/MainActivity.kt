package com.neo.hash.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.neo.hash.ui.screen.mainScreen.MainScreen
import com.neo.hash.ui.screen.updateScreen.UpdateScreen
import com.neo.hash.ui.theme.HashBackground
import com.neo.hash.ui.theme.HashTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
                    UpdateScreen()
                }
            }
        }
    }
}