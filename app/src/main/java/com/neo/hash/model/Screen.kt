package com.neo.hash.model

import android.net.Uri
import com.neo.hash.ui.screen.gameScreen.viewModel.Match

sealed class Screen {

    object HomeScreen : Screen() {
        const val route = "home_screen"
    }

    object GameScreen : Screen() {
        const val route = "game_screen"
    }
}
