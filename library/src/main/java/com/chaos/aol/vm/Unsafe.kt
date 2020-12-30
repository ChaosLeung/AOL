package com.chaos.aol.vm

import java.lang.reflect.Field
import java.lang.reflect.Method

internal class Unsafe {
    companion object {
        private val THE_ONE: Any

        private val objectFieldOffsetMethod: Method
        private val arrayBaseOffsetMethod: Method
        private val arrayIndexScaleMethod: Method

        private val getIntMethod: Method

        init {
            val unsafeClass = Class.forName("sun.misc.Unsafe")
            val instanceField = unsafeClass.getDeclaredField("THE_ONE").apply { isAccessible = true }
            THE_ONE = instanceField.get(null)!!

            objectFieldOffsetMethod = unsafeClass.getDeclaredMethod("objectFieldOffset", Field::class.java)
            arrayBaseOffsetMethod = unsafeClass.getDeclaredMethod("arrayBaseOffset", Class::class.java)
            arrayIndexScaleMethod = unsafeClass.getDeclaredMethod("arrayIndexScale", Class::class.java)

            getIntMethod = unsafeClass.getDeclaredMethod("getInt", Object::class.java, Long::class.java)
        }

        fun objectFieldOffset(field: Field): Long {
            return objectFieldOffsetMethod.invoke(THE_ONE, field) as Long
        }

        fun arrayBaseOffset(clazz: Class<*>): Int {
            return arrayBaseOffsetMethod.invoke(THE_ONE, clazz) as Int
        }

        fun arrayIndexScale(clazz: Class<*>): Int {
            return arrayIndexScaleMethod.invoke(THE_ONE, clazz) as Int
        }

        fun getInt(obj: Any, offset: Long): Int {
            return getIntMethod.invoke(THE_ONE, obj, offset) as Int
        }
    }
}