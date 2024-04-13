package com.neo.hash.exception

import com.neo.hash.domain.model.Hash
import com.neo.hash.domain.model.Player

class HardFailureException(
    phone: Player.Phone,
    person: Player.Person,
    log: List<Hash.Block>
) : Throwable("AI - ${phone.symbol}, " +
        "${person.name} - ${person.symbol} : " +
        log.joinToString(", ") { "$it" })