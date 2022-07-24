package com.neo.hashgame.model

sealed class Player {

    abstract val symbol: Hash.Symbol
    abstract val name: String
    abstract val windsCount : Int

    data class Phone(
        override val symbol: Hash.Symbol,
        override val windsCount: Int = 0
    ) : Player() {
        override val name: String = "Smartphone"
    }

    data class Person(
        override val name: String,
        override val symbol: Hash.Symbol,
        override val windsCount: Int = 0
    ) : Player()
}