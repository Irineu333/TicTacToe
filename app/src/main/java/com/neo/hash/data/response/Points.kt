package com.neo.hash.data.response

import com.google.firebase.database.PropertyName

class Points {
    @PropertyName("hard")
    var hard : Long = 0
    @PropertyName("medium")
    var medium : Long = 0
    @PropertyName("easy")
    var easy : Long = 0
}
