package com.neo.hash.model

sealed class Player {

    abstract val symbol: Hash.Symbol
    abstract val windsCount: Int

    data class Phone(
        override val symbol: Hash.Symbol,
        override val windsCount: Int = 0,
        val ai : Intelligent = Intelligent(symbol),
        val isEnabled : Boolean = true
    ) : Player()

    data class Person(
        override val symbol: Hash.Symbol,
        val name: String,
        override val windsCount: Int = 0
    ) : Player()
}