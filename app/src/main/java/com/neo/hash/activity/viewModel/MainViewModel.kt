package com.neo.hash.activity.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.neo.hash.data.response.Points
import com.neo.hash.dataStoreRepository
import com.neo.hash.model.Difficulty
import com.neo.hash.singleton.Coclew
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val referenceCodeFlow = dataStoreRepository.referenceCodeFlow

    private var interstitialSkippedCount = 0L
    private var referenceCode = ""

    init {
        setupListeners()
    }

    private fun setupListeners() {
        viewModelScope.launch {
            launch {
                dataStoreRepository.interstitialSkippedFlow.collectLatest {
                    interstitialSkippedCount = it
                }
            }

            launch {
                dataStoreRepository.referenceCodeFlow.collectLatest {
                    referenceCode = it
                }
            }
        }
    }

    fun setReferenceCode(code: String) = viewModelScope.launch {
        dataStoreRepository.setReferenceCode(code)
    }

    fun clearReferenceCode() = viewModelScope.launch {
        dataStoreRepository.setReferenceCode("")
    }

    fun isSkipInterstitial(): Boolean {

        val value = Coclew.interstitialSkip.value
        if (interstitialSkippedCount >= value) {
            //don't skip
            return false
        }

        interstitialSkipped()

        //skip
        return true
    }

    private fun interstitialSkipped() = viewModelScope.launch {
        dataStoreRepository.interstitialSkipped()
    }

    fun interstitialSkipReset() = viewModelScope.launch {
        dataStoreRepository.interstitialSkipReset()
    }

    fun addPoints(difficulty: Difficulty) {
        if (referenceCode.isEmpty()) return

        viewModelScope.launch {

            val coclewRef = FirebaseDatabase
                .getInstance()
                .getReference("coclew")

            val userRef = coclewRef
                .child("users")
                .child(referenceCode)

            val userValueRef = userRef
                .child("value")

            coclewRef
                .child("points")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val value = snapshot.getValue<Points>() ?: return

                        val addPoints = when (difficulty) {
                            Difficulty.EASY -> value.easy
                            Difficulty.MEDIUM -> value.medium
                            Difficulty.HARD -> value.hard
                        }

                        userRef
                            .child("value")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {

                                    val oldPoints = snapshot.getValue<Int>() ?: 0

                                    val newPoints = oldPoints + addPoints

                                    userValueRef.setValue(newPoints)
                                }

                                override fun onCancelled(error: DatabaseError) = Unit
                            })
                    }

                    override fun onCancelled(error: DatabaseError) = Unit
                })
        }
    }
}
