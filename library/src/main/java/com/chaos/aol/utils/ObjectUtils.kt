package com.chaos.aol.utils

import java.lang.reflect.Field

object ObjectUtils {

    fun arrayLength(obj: Any): Int {
        val clazz = obj.javaClass
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

    fun valueString(o: Any?): String {
        if (o == null) return "null"
        if (o.javaClass.isArray) {
            return when (o.javaClass.componentType) {
                Boolean::class.javaPrimitiveType -> (o as BooleanArray).contentToString()
                Byte::class.javaPrimitiveType -> (o as ByteArray).contentToString()
                Short::class.javaPrimitiveType -> (o as ShortArray).contentToString()
                Char::class.javaPrimitiveType -> (o as CharArray).contentToString()
                Int::class.javaPrimitiveType -> (o as IntArray).contentToString()
                Float::class.javaPrimitiveType -> (o as FloatArray).contentToString()
                Long::class.javaPrimitiveType -> (o as LongArray).contentToString()
                Double::class.javaPrimitiveType -> (o as DoubleArray).contentToString()
                else -> (o as Array<*>)
                    .map {
                        if (it == null) "null" else valueString(it)
                    }
                    .toTypedArray()
                    .contentToString()
            }
        }
        if (o.javaClass.isPrimitive) {
            return o.toString()
        }
        return when (o.javaClass) {
            Boolean::class.javaObjectType -> o.toString()
            Byte::class.javaObjectType -> o.toString()
            Short::class.javaObjectType -> o.toString()
            Char::class.javaObjectType -> o.toString()
            Int::class.javaObjectType -> o.toString()
            Float::class.javaObjectType -> o.toString()
            Long::class.javaObjectType -> o.toString()
            Double::class.javaObjectType -> o.toString()
            else -> "(object)"
        }
    }

    fun value(o: Any, f: Field): Any? {
        val t: Class<*> = f.type
        f.isAccessible = true
        if (!t.isPrimitive) {
            return f.get(o)
        }
        return when (t) {
            Boolean::class.javaPrimitiveType -> f.getBoolean(o)
            Byte::class.javaPrimitiveType -> f.getByte(o)
            Char::class.javaPrimitiveType -> f.getChar(o)
            Short::class.javaPrimitiveType -> f.getShort(o)
            Int::class.javaPrimitiveType -> f.getInt(o)
            Float::class.javaPrimitiveType -> f.getFloat(o)
            Long::class.javaPrimitiveType -> f.getLong(o)
            Double::class.javaPrimitiveType -> f.getDouble(o)
            else -> throw IllegalStateException("Unhandled primitive: $t")
        }
    }
}