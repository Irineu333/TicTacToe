package com.neo.hash.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.neo.hash.ui.screen.mainScreen.MainScreen
import com.neo.hash.ui.screen.updateScreen.UpdateScreen
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