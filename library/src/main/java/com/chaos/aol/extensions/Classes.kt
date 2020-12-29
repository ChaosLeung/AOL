package com.chaos.aol.extensions

fun <T> Class<T>.getSafeName(): String {
    var n = canonicalName
    if (n == null) {
        n = name
    }
    return n!!
}