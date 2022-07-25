package com.neo.hash.util.extensions

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString


fun <T> List<T>.joinToAnnotatedString(
    separator: String,
    transformation: AnnotatedString.Builder.(T) -> Unit
) = buildAnnotatedString {
    val sequence = this@joinToAnnotatedString
    val lastIndex = sequence.lastIndex

    for ((index, item) in sequence.withIndex()) {
        transformation(this, item)
        if (index < lastIndex) {
            append(separator)
        }
    }
}