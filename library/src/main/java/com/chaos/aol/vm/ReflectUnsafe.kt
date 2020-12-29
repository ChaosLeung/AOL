package com.chaos.aol.vm

import com.chaos.aol.utils.ArrayUtils
import java.lang.reflect.Field

internal class ReflectUnsafe {
    companion object : VirtualMachine {

        private val OBJECT_SIZE_FIELD =
            Class::class.java.getDeclaredField("objectSize").apply { isAccessible = true }

        private val HEADER_SIZE: Int = sizeOfObject(Any()).toInt()

        override fun sizeOfObject(obj: Any): Long {
            val clazz = obj::class.java
            if (clazz.isArray) {
                return sizeOfArrayObject(obj)
            }
            return OBJECT_SIZE_FIELD.getInt(clazz).toLong()
        }

        override fun sizeOfArrayObject(obj: Any): Long {
            val clazz = obj::class.java
            if (!clazz.isArray) {
                throw IllegalArgumentException("class '${clazz.name}' of object '$obj' should be an array class")
            }

            val headerSize = Unsafe.arrayBaseOffset(clazz)
            val contentSize = Unsafe.arrayIndexScale(clazz).toLong() * ArrayUtils.getArrayLength(obj)
            return headerSize + contentSize
        }

        override fun sizeOfField(field: Field): Int = sizeOfType(field.type)

        private fun sizeOfType(clazz: Class<*>): Int {
            // code from ComponentSize method which in art/libdexfile/dex/primitive.h
            return when (clazz) {
                Void::class.java -> 0
                Byte::class.java, Boolean::class.java -> 1
                Char::class.java, Short::class.java -> 2
                Int::class.java, Float::class.java -> 4
                Long::class.java, Double::class.java -> 8
                // static constexpr size_t kObjectReferenceSize = 4;
                else -> 4
            }
        }

        override fun fieldOffset(field: Field): Long = Unsafe.objectFieldOffset(field)

        override fun arrayBaseOffset(clazz: Class<*>): Int = Unsafe.arrayBaseOffset(clazz)

        override fun arrayIndexScale(clazz: Class<*>): Int = Unsafe.arrayIndexScale(clazz)

        override fun objectHeaderSize(): Int = HEADER_SIZE

        override fun arrayHeaderSize(clazz: Class<*>): Int {
            if (!clazz.isArray) {
                throw IllegalArgumentException("${clazz.name} should be an array class")
            }
            return Unsafe.arrayBaseOffset(clazz)
        }

        fun guessArrayObjectSize(obj: Any): Long {
            val clazz = obj::class.java
            if (!clazz.isArray) {
                throw IllegalArgumentException("${clazz.name} should be an array class")
            }

            val arrayLength = ArrayUtils.getArrayLength(obj)

            val headerSize = guessArrayHeaderSize(clazz)
            val contentSize = arrayLength.toLong() * sizeOfType(clazz.componentType!!)
            return headerSize + contentSize
        }

        fun guessArrayHeaderSize(clazz: Class<*>): Int {
            if (!clazz.isArray) {
                throw IllegalArgumentException("${clazz.name} should be an array class")
            }
            // code from ComputeArraySize method which in art/runtime/mirror/array-alloc-inl.h
            // code from GetRawData method which in art/runtime/mirror/array.h

            return roundUp(HEADER_SIZE + 4/* int32_t length_ */, sizeOfType(clazz.componentType!!))
        }

        private fun roundUp(x: Int, n: Int): Int {
            return roundDown(x + n - 1, n)
        }

        private fun roundDown(x: Int, n: Int): Int {
            return x and -n
        }
    }
}