package com.chaos.aol.extensions

fun String.insert(start: Int = 0, step: Int = 1, ch: Char = ' '): String {
    if (start < 0 || step < 1) {
        return this
    }
    val sb = StringBuilder(this)
    val counts: Int = (length - start) / step
    for (i in 0 until counts) {
        sb.insert(i + start + i * step, ch)
    }
    return sb.toString()
}