package com.chaos.aol.vm

import com.chaos.aol.utils.ObjectUtils
import java.lang.reflect.Field

internal class ReflectUnsafe {
    companion object : VirtualMachine {

        private val OBJECT_SIZE_FIELD =
            Class::class.java.getDeclaredField("objectSize").apply { isAccessible = true }

        private val HEADER_SIZE: Int = sizeOfObject(Any()).toInt()

        override fun sizeOfObject(obj: Any): Long {
            val clazz = obj.javaClass
            if (clazz.isArray) {
                return sizeOfArrayObject(obj)
            }
            return OBJECT_SIZE_FIELD.getInt(clazz).toLong()
        }

        override fun sizeOfArrayObject(obj: Any): Long {
            val clazz = obj.javaClass
            if (!clazz.isArray) {
                throw IllegalArgumentException("class '${clazz.name}' of object '$obj' should be an array class")
            }

            val headerSize = Unsafe.arrayBaseOffset(clazz)
            val contentSize = Unsafe.arrayIndexScale(clazz).toLong() * ObjectUtils.arrayLength(obj)
            return headerSize + contentSize
        }

        override fun sizeOfField(field: Field): Int = sizeOfType(field.type)

        private fun sizeOfType(clazz: Class<*>): Int {
            // code from ComponentSize method which in art/libdexfile/dex/primitive.h
            return when (clazz) {
                Void::class.javaPrimitiveType -> 0
                Byte::class.javaPrimitiveType, Boolean::class.javaPrimitiveType -> 1
                Char::class.javaPrimitiveType, Short::class.javaPrimitiveType -> 2
                Int::class.javaPrimitiveType, Float::class.javaPrimitiveType -> 4
                Long::class.javaPrimitiveType, Double::class.javaPrimitiveType -> 8
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

        override fun getInt(obj: Any, offset: Long): Int = Unsafe.getInt(obj, offset)

        fun guessArrayObjectSize(obj: Any): Long {
            val clazz = obj.javaClass
            if (!clazz.isArray) {
                throw IllegalArgumentException("${clazz.name} should be an array class")
            }

            val arrayLength = ObjectUtils.arrayLength(obj)

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