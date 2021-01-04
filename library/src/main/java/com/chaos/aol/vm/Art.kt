package com.chaos.aol.vm

import java.lang.reflect.Field

internal object Art : VirtualMachine {

    private val sizeCalculator = SizeCalculatorProvider.get()

    override fun sizeOfObject(obj: Any): Int {
        if (obj == Class::class.java) {
            return sizeCalculator.sizeOfClassObject(obj as Class<*>)
        }
        val clazz = obj.javaClass
        if (clazz.isArray) {
            return sizeCalculator.sizeOfArrayObject(obj)
        } else if (clazz == String::class.java) {
            return sizeCalculator.sizeOfStringObject(obj as String)
        }
        return sizeCalculator.sizeOfRegularObject(obj)
    }

    override fun sizeOfField(field: Field): Int = sizeCalculator.sizeOfField(field)

    override fun fieldOffset(field: Field): Long = Unsafe.objectFieldOffset(field)

    override fun arrayBaseOffset(clazz: Class<*>): Int = Unsafe.arrayBaseOffset(clazz)

    override fun arrayIndexScale(clazz: Class<*>): Int = Unsafe.arrayIndexScale(clazz)

    override fun objectHeaderSize(): Int = sizeCalculator.objectHeaderSize()

    override fun arrayHeaderSize(clazz: Class<*>): Int = sizeCalculator.arrayHeaderSize(clazz)

    override fun getInt(obj: Any, offset: Long): Int = Unsafe.getInt(obj, offset)
}