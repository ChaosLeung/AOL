package com.chaos.aol.sample

open class Primitive {
    private val str = "default"
    private val int = 12
    private val short: Short = 1
    private val long = 10L
    private val ch = 'c'
    private val byte: Byte = 2
    private val boolean = true
    private val float = 1.2f
    private val double: Double = 2.2
}

class PrimitiveArray : Primitive() {
    private val intArray = intArrayOf()
    private val shortArray = shortArrayOf()
    private val longArray = longArrayOf()
    private val charArray = charArrayOf()
    private val byteArray = byteArrayOf()
    private val boolArray = booleanArrayOf()
    private val floatArray = floatArrayOf()
    private val doubleArray = doubleArrayOf()
}

open class Boxed {
    private val int = Integer.valueOf(12)
    private val short = java.lang.Short.valueOf(1)
    private val long = java.lang.Long.valueOf(10)
    private val ch = Character.valueOf('c')
    private val byte = java.lang.Byte.valueOf(2)
    private val boolean = java.lang.Boolean.valueOf(true)
    private val float = java.lang.Float.valueOf(1.2f)
    private val double = java.lang.Double.valueOf(2.2)
}

class BoxedArray : Boxed() {
    private val intArray: Array<Int>? = null
    private val shortArray: Array<Short>? = null
    private val longArray: Array<Long>? = null
    private val charArray: Array<Char>? = null
    private val byteArray: Array<Byte>? = null
    private val boolArray: Array<Boolean>? = null
    private val floatArray: Array<Float>? = null
    private val doubleArray: Array<Double>? = null
}