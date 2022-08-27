package com.neo.hash.model

sealed class StartDialog {
    object VsIntelligent : StartDialog()
    object VsPerson : StartDialog()
    object Multiplayer : StartDialog()
}