package com.neo.hash.model

import com.google.gson.Gson
import com.neo.hash.ui.screen.gameScreen.viewModel.Match

sealed class Screen {

    object HomeScreen : Screen() {
        const val route = "home_screen"
    }

    object GameScreen : Screen() {

        fun route(match: Match) = "game_screen/${Gson().toJson(match)}"

        const val route = "game_screen/{match}"
    }
}
