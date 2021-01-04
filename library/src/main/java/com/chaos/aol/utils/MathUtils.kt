package com.chaos.aol.utils

internal object MathUtils {

    fun roundUp(x: Int, n: Int): Int {
        return roundDown(x + n - 1, n)
    }

    fun roundDown(x: Int, n: Int): Int {
        return x and -n
    }
}