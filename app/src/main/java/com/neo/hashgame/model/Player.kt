package com.neo.hashgame.model

sealed class Player {

    abstract val symbol: Hash.Symbol
    abstract val name: String
    abstract var windsCount: Int

    data class Phone(
        override val symbol: Hash.Symbol
    ) : Player() {
        override val name: String = "Smartphone"
        override var windsCount: Int = 0
    }

    data class Person(
        override val name: String,
        override val symbol: Hash.Symbol,
    ) : Player() {
        override var windsCount: Int = 0
    }
}