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

fun <T> List<T>.containsAny(
    emptyBlocks: MutableList<T>
) = any { block ->
    emptyBlocks.any {
        block == it
    }
}

fun <T> List<T>.recurring(
    predicate: (T) -> T = { it }
) = groupBy(predicate)
    .filterValues {
        it.size > 1
    }.keys.toList()

fun <E> List<E>.tryRecurring() =
    recurring().ifEmpty { this }

fun <T> List<T>.tryFilter(predicate: (T) -> Boolean) =
    filter(predicate).ifEmpty { this }

inline fun <reified T> List<Any>.findType() = find { it is T } as? T