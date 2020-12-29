package com.chaos.aol.utils

class ArrayUtils {
    companion object {
        fun getArrayLength(obj: Any): Int {
            val clazz = obj::class.java
            if (!clazz.isArray) {
                throw IllegalArgumentException("${clazz.name} should be an array class")
            }
            return when (clazz) {
                ByteArray::class.java -> (obj as ByteArray).size
                BooleanArray::class.java -> (obj as BooleanArray).size
                CharArray::class.java -> (obj as CharArray).size
                ShortArray::class.java -> (obj as ShortArray).size
                IntArray::class.java -> (obj as IntArray).size
                FloatArray::class.java -> (obj as FloatArray).size
                LongArray::class.java -> (obj as LongArray).size
                DoubleArray::class.java -> (obj as DoubleArray).size
                else -> (obj as Array<*>).size
            }
        }
    }
}