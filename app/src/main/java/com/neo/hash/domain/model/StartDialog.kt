package com.neo.hash.domain.model

sealed class StartDialog {
    object VsIntelligent : StartDialog()
    object VsPerson : StartDialog()
    object Multiplayer : StartDialog()
}