package com.neo.hash.domain.model

sealed class Player {

    abstract val symbol: Hash.Symbol
    abstract val windsCount: Int

    data class Phone(
        override val symbol: Hash.Symbol,
        override val windsCount: Int = 0,
        val intelligent: Intelligent = Intelligent(symbol),
        val isEnabled: Boolean = true,
        val difficulty: Difficulty = Difficulty.HARD
    ) : Player()

    data class Person(
        override val symbol: Hash.Symbol,
        val name: String,
        override val windsCount: Int = 0
    ) : Player()
}