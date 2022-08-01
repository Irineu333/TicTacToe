package com.neo.hash.singleton

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object Coclew {

    private val coclewRef = FirebaseDatabase
        .getInstance()
        .getReference("coclew")

    val enabled = MutableStateFlow(value = true).apply {
        coclewRef.child("enabled").addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    value = snapshot.getValue<Boolean>() ?: return
                }

                override fun onCancelled(error: DatabaseError) = Unit
            }
        )
    }.asStateFlow()

    val interstitialSkip = MutableStateFlow(value = 0L).apply {
        coclewRef
            .child("interstitial_skip")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    value = snapshot.getValue<Long>() ?: return
                }

                override fun onCancelled(error: DatabaseError) = Unit
            })
    }.asStateFlow()
}