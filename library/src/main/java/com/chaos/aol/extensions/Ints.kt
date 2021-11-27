package com.chaos.aol.extensions

fun Int.toBinaryString(): String {
    val step = Int.SIZE_BITS / Int.SIZE_BYTES
    return Integer.toBinaryString(this).padStart(Int.SIZE_BITS, '0').insert(step, step, ' ')
}

fun Int.toHexString(): String {
    return Integer.toHexString(this).padStart(Int.SIZE_BITS / Int.SIZE_BYTES, '0').insert(2, 2, ' ')
}
