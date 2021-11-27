package com.chaos.aol.vm

import android.annotation.TargetApi
import android.os.Build
import com.chaos.aol.external.ArtNative
import com.chaos.aol.utils.MathUtils
import com.chaos.aol.utils.ObjectUtils

internal interface SizeCalculator {
    fun sizeOfClassObject(clazz: Class<*>): Int
    fun sizeOfArrayObject(obj: Any): Int
    fun sizeOfStringObject(str: String): Int
    fun sizeOfRegularObject(obj: Any): Int
    fun sizeOfRegularObject(objClazz: Class<*>): Int

    fun sizeOfField(fieldType: Class<*>): Int

    fun objectHeaderSize(): Int
    fun arrayHeaderSize(clazz: Class<*>): Int
}

internal object SizeCalculatorProvider {
    private val calculator: SizeCalculator by lazy {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                SizeCalculatorV26()
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                SizeCalculatorV24()
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                SizeCalculatorV23()
            }
            else -> {
                SizeCalculatorV19()
            }
        }
    }

    fun get(): SizeCalculator = calculator
}

private open class SizeCalculatorV19 : SizeCalculator {
    private val objectSizeField =
        Class::class.java.getDeclaredField("objectSize").apply { isAccessible = true }
    private val clinitThreadIdField =
        Class::class.java.getDeclaredField("clinitThreadId").apply { isAccessible = true }

    /**
     * define order:
     * uint32_t class_size_;
     * pid_t clinit_thread_id_;
     * static_assert(sizeof(pid_t) == sizeof(int32_t), "java.lang.Class.clinitThreadId size check");
     */
    private val classSizeFieldOffset = Unsafe.objectFieldOffset(clinitThreadIdField) - 4/* int32_t */

    protected val objectHeaderSize: Int = objectSizeField.getInt(Any::class.java)

    override fun sizeOfClassObject(clazz: Class<*>): Int = ArtNative.getClassSize(clazz)

    override fun sizeOfArrayObject(obj: Any): Int {
        val clazz = obj.javaClass
        if (!clazz.isArray) {
            throw IllegalArgumentException("class '${clazz.name}' of object '$obj' should be an array class")
        }

        val headerSize = Unsafe.arrayBaseOffset(clazz)
        val contentSize = Unsafe.arrayIndexScale(clazz) * ObjectUtils.arrayLength(obj)
        return headerSize + contentSize
    }

    override fun sizeOfStringObject(str: String): Int {
        // String Fields:
        //
        // CharArray* array_; // 32-bit if below 5.0, pointer take up 4 bytes
        // int32_t count_;
        // uint32_t hash_code_;
        // int32_t offset_;
        return objectHeaderSize + 16 /* String Fields */ + str.length * 2 /* unit16_t */
    }

    override fun sizeOfRegularObject(obj: Any): Int = sizeOfRegularObject(obj.javaClass)

    override fun sizeOfRegularObject(objClazz: Class<*>): Int = objectSizeField.getInt(objClazz)

    override fun sizeOfField(fieldType: Class<*>): Int {
        return when (fieldType) {
            Void::class.javaPrimitiveType -> 0
            Byte::class.javaPrimitiveType, Boolean::class.javaPrimitiveType -> 1
            Char::class.javaPrimitiveType, Short::class.javaPrimitiveType -> 2
            Int::class.javaPrimitiveType, Float::class.javaPrimitiveType -> 4
            Long::class.javaPrimitiveType, Double::class.javaPrimitiveType -> 8
            // Below 5.0, system is 32-bit and pointer take up 4 bytes.
            // In 5.0, 5.1 platform, reference pointer hold by HeapReference, and it defined as uint32_t
            // Start from 6.0, reference size always 4
            else -> 4
        }
    }

    override fun objectHeaderSize(): Int = objectHeaderSize

    override fun arrayHeaderSize(clazz: Class<*>): Int {
        if (!clazz.isArray) {
            throw IllegalArgumentException("${clazz.name} should be an array class")
        }
        return Unsafe.arrayBaseOffset(clazz)
    }
}

@TargetApi(Build.VERSION_CODES.M)
private open class SizeCalculatorV23 : SizeCalculatorV19() {
    override fun sizeOfStringObject(str: String): Int {
        // String Fields:
        //
        // int32_t count_;
        // uint32_t hash_code_;
        // uint16_t value_[0];
        return objectHeaderSize + 8 /* String Fields */ + str.length * 2 /* unit16_t */
    }
}

@TargetApi(Build.VERSION_CODES.N)
private open class SizeCalculatorV24 : SizeCalculatorV23() {
    override fun sizeOfStringObject(str: String): Int {
        // https://android.googlesource.com/platform/art/+/refs/heads/nougat-mr1.8-release/runtime/mirror/string-inl.h#167
        return MathUtils.roundUp(super.sizeOfStringObject(str), 8)
    }
}

@TargetApi(Build.VERSION_CODES.O)
private open class SizeCalculatorV26 : SizeCalculatorV24() {
    override fun sizeOfStringObject(str: String): Int {
        // String Fields:
        //
        // int32_t count_;
        // uint32_t hash_code_;
        // // Compression of all-ASCII into 8-bit memory leads to usage one of these fields
        // union {
        //   uint16_t value_[0];
        //   uint8_t value_compressed_[0];
        // };

        // https://android.googlesource.com/platform/art/+/refs/heads/android11-release/runtime/mirror/string-inl.h#88
        val allAscii = str.all {
            // https://android.googlesource.com/platform/art/+/refs/heads/android11-release/runtime/mirror/string.h#241
            it.toInt() - 1 < 0x7f
        }
        var size = objectHeaderSize + 8
        size += str.length * if (allAscii) 1 else 2
        // https://android.googlesource.com/platform/art/+/refs/heads/android11-release/runtime/mirror/string-alloc-inl.h#174
        return MathUtils.roundUp(size, 8)
    }
}