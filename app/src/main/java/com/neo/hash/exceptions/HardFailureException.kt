package com.neo.hash.exceptions

import androidx.annotation.Keep
import com.neo.hash.model.Hash
import com.neo.hash.model.Player

@Keep
class HardFailureException(
    phone: Player.Phone,
    person: Player.Person,
    log: List<Hash.Block>
) : Exception("AI - ${phone.symbol}, " +
        "${person.name} - ${person.symbol} : " +
        log.joinToString(", ") { "$it" })