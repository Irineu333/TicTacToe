package com.neo.hash.activity.viewModel

sealed class MainUiEffect {
    class Toast(
        val message: String
    ) : MainUiEffect()

    class Error(
        val error: String
    ) : MainUiEffect()

    sealed class Points : MainUiEffect() {
        data class Added(
            val points: Int
        ) : Points()

        object Error : Points()
    }
}
