package com.neo.hash.singleton

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object Coclew {

    private val coclewRef = FirebaseDatabase
        .getInstance()
        .getReference("coclew")

    private val coroutine = CoroutineScope(Dispatchers.Main)

    val enabled by lazy {
        Channel<Boolean>().apply {
            coclewRef.child("enabled").addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val enabled = snapshot.getValue<Boolean>() ?: return

                        coroutine.launch {
                            send(enabled)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) = Unit
                }
            )
        }.receiveAsFlow()
    }
}