package com.chaos.aol.vm

import java.lang.reflect.Field

interface VirtualMachine {

    fun sizeOfObject(obj: Any): Int

    fun sizeOfField(field: Field): Int

    fun fieldOffset(field: Field): Long

    fun arrayBaseOffset(clazz: Class<*>): Int

    fun arrayIndexScale(clazz: Class<*>): Int

    fun objectHeaderSize(): Int

    fun arrayHeaderSize(clazz: Class<*>): Int

    fun getInt(obj: Any, offset: Long): Int

    companion object {
        const val UNKNOWN_SIZE: Int = -1
    }
}