package com.neo.hash.util.extension


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