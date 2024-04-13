package com.neo.hash.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.neo.hash.ui.feature.main.MainScreen
import com.neo.hash.ui.feature.update.UpdateScreen
import com.neo.hash.ui.theme.HashBackground
import com.neo.hash.ui.theme.HashTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            HashTheme {
                HashBackground(Modifier.fillMaxSize()) {
                    MainScreen()
                    UpdateScreen()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (Firebase.auth.currentUser == null) {

            //anonymous firebase login
            Firebase.auth.signInAnonymously()
        }
    }
}