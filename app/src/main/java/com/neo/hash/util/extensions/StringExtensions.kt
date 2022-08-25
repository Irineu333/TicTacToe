package com.neo.hash.util.extensions

fun String?.isUid(): Boolean {
    return !isNullOrEmpty() && length == 28
}