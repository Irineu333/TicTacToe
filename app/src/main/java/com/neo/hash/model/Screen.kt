package com.neo.hash.model

sealed class Screen() {

    object HomeScreen : Screen() {
        const val route = "home_screen"
    }

    object GameScreen : Screen() {

        const val isPhone = "vsPhone"
        const val route = "game_screen/{$isPhone}"

        fun route(vsPhone: Boolean) = "game_screen/${vsPhone}"
    }
}
