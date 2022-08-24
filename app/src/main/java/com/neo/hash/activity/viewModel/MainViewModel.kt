package com.neo.hash.activity.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.neo.hash.BuildConfig
import com.neo.hash.data.response.Points
import com.neo.hash.dataStoreRepository
import com.neo.hash.model.Difficulty
import com.neo.hash.singleton.Coclew
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


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
        if (Coclew.enabled.value != true) return

        if (Firebase.auth.currentUser == null) {

            Firebase.auth.signInAnonymously()
                .addOnSuccessListener {
                    addPoints(difficulty)
                }

            return
        }

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
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val value = snapshot.getValue<Points>() ?: return

                        val addPoints = when (difficulty) {
                            Difficulty.EASY -> value.easy
                            Difficulty.MEDIUM -> value.medium
                            Difficulty.HARD -> value.hard
                        }

                        userValueRef.setValue(ServerValue.increment(addPoints))

                        coclewRef
                            .child("timestamp")
                            .getTimesTamp { timestamp ->
                                coclewRef.child("historic")
                                    .child(timestamp.getDateFormatted()).apply {
                                        child("total")
                                            .setValue(ServerValue.increment(addPoints))
                                    }
                                    .child("users")
                                    .child(referenceCode)
                                    .apply {
                                        child("value")
                                            .setValue(ServerValue.increment(addPoints))
                                    }
                                    .child("transactions")
                                    .push()
                                    .setValue(
                                        mapOf(
                                            "points" to addPoints,
                                            "version" to BuildConfig.VERSION_NAME,
                                            "date" to timestamp.getDateTimeFormatted(),
                                            "difficulty" to difficulty.name
                                        )
                                    )
                            }
                    }

                    override fun onCancelled(error: DatabaseError) = Unit
                }
            )
    }
}

fun Long.getDateFormatted(): String {
    return SimpleDateFormat(
        "dd-MM-yyyy",
        Locale("pt", "BR")
    ).format(Date(this))
}

fun Long.getDateTimeFormatted(): String {
    return SimpleDateFormat(
        "HH:mm dd-MM-yyyy",
        Locale("pt", "BR")
    ).format(Date(this))
}

fun DatabaseReference.getTimesTamp(function: (Long) -> Unit) {
    setValue(ServerValue.TIMESTAMP)
        .addOnSuccessListener {
            addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        function(snapshot.getValue<Long>() ?: System.currentTimeMillis())
                    }

                    override fun onCancelled(error: DatabaseError) {
                        function(System.currentTimeMillis())
                    }
                }
            )
        }
}
