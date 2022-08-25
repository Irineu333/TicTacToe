package com.neo.hash.exceptions

import com.neo.hash.model.Hash
import com.neo.hash.model.Player

class HardFailureException(
    phone: Player.Phone,
    person: Player.Person,
    log: List<Hash.Block>
) : Throwable("AI - ${phone.symbol}, " +
        "${person.name} - ${person.symbol} : " +
        log.joinToString(", ") { "$it" })