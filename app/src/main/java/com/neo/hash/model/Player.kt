package com.neo.hash.model

sealed class Player {

    abstract val symbol: Hash.Symbol
    abstract var windsCount: Int

    data class Phone(
        override val symbol: Hash.Symbol,
        val ai : Intelligent = Intelligent(symbol),
    ) : Player() {
        override var windsCount: Int = 0
        var isEnabled : Boolean = true
    }

    data class Person(
        override val symbol: Hash.Symbol,
        val name: String,
    ) : Player() {
        override var windsCount: Int = 0
    }
}