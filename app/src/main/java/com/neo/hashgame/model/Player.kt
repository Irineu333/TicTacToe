package com.neo.hashgame.model

sealed class Player {

    abstract val symbol: Hash.Symbol
    abstract val name: String
    abstract var windsCount : Int

    class Phone(
        override val symbol: Hash.Symbol,
        override var windsCount: Int = 0
    ) : Player() {
        override val name: String = "Smartphone"
    }

    class Person(
        override val name: String,
        override val symbol: Hash.Symbol,
        override var windsCount: Int = 0
    ) : Player()
}