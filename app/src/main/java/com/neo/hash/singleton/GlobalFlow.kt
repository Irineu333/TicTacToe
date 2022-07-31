package com.neo.hash.singleton

import com.neo.hash.model.Difficulty
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object GlobalFlow {
    private val mDifficulty = Channel<Difficulty>()
    val difficulty get() = mDifficulty.receiveAsFlow()


    private val mInterstitial = Channel<Unit>()
    val interstitial get() = mInterstitial.receiveAsFlow()

    suspend fun addPoints(difficulty: Difficulty) {
        mDifficulty.send(difficulty)
    }

    suspend fun showInterstitial() {
        mInterstitial.send(Unit)
    }
}
