package com.neo.hash.data.response

import com.google.firebase.database.PropertyName

class Points {
    @PropertyName("hard")
    var hard : Int = 0
    @PropertyName("medium")
    var medium : Int = 0
    @PropertyName("easy")
    var easy : Int = 0
}
