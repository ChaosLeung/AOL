package com.chaos.aol.vm

import com.chaos.aol.utils.ObjectUtils
import java.lang.reflect.Field

internal object Art : VirtualMachine {

    private val OBJECT_SIZE_FIELD =
        Class::class.java.getDeclaredField("objectSize").apply { isAccessible = true }
    private val ACCESS_FLAGS_FIELD =
        Class::class.java.getDeclaredField("clinitThreadId").apply { isAccessible = true }

    /**
     * define order:
     * uint32_t class_size_;
     * pid_t clinit_thread_id_;
     * static_assert(sizeof(pid_t) == sizeof(int32_t), "java.lang.Class.clinitThreadId size check");
     */
    private val CLASS_SIZE_FIELD_OFFSET = Unsafe.objectFieldOffset(ACCESS_FLAGS_FIELD) - 4

    private val HEADER_SIZE: Int = sizeOfObject(Any())

    override fun sizeOfObject(obj: Any): Int {
        if (obj == Class::class.java) {
            return Unsafe.getInt(obj, CLASS_SIZE_FIELD_OFFSET)
        }
        val clazz = obj.javaClass
        if (clazz.isArray) {
            return sizeOfArrayObject(obj)
        } else if (clazz == String::class.java) {
            return sizeOfStringObject(obj as String)
        }
        return OBJECT_SIZE_FIELD.getInt(clazz)
    }

    private fun sizeOfArrayObject(obj: Any): Int {
        val clazz = obj.javaClass
        if (!clazz.isArray) {
            throw IllegalArgumentException("class '${clazz.name}' of object '$obj' should be an array class")
        }

        val headerSize = Unsafe.arrayBaseOffset(clazz)
        val contentSize = Unsafe.arrayIndexScale(clazz) * ObjectUtils.arrayLength(obj)
        return headerSize + contentSize
    }

    private fun sizeOfStringObject(str: String): Int {
        // https://android.googlesource.com/platform/art/+/refs/heads/android11-release/runtime/mirror/string-inl.h#88
        val allAscii = str.all {
            // https://android.googlesource.com/platform/art/+/refs/heads/android11-release/runtime/mirror/string.h#241
            it.toInt() - 1 < 0x7f
        }
        var size = HEADER_SIZE + 8
        size += str.length * if (allAscii) 1 else 2
        return roundUp(size, 8)
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

    private fun roundUp(x: Int, n: Int): Int {
        return roundDown(x + n - 1, n)
    }

    private fun roundDown(x: Int, n: Int): Int {
        return x and -n
    }
}