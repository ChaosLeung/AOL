package com.chaos.aol.vm

import java.lang.reflect.Field

interface VirtualMachine {

    /**
     * Returns the shallow size of the given object.
     * @param obj object
     * @return shallow size
     */
    fun sizeOfInstance(obj: Any): Int

    /**
     * Returns the shallow size of an instance of the given class.
     * Use for regular object class.
     * @param objClazz instance class
     * @return shallow size of instance
     */
    fun sizeOfRegularObject(objClazz: Class<*>): Int

    /**
     * Returns the size of a field holding the type.
     * @param clazz field type
     * @return slot size
     */
    fun sizeOfField(clazz: Class<*>): Int

    /**
     * Returns the field offset
     * @param field field
     * @return offset
     */
    fun fieldOffset(field: Field): Long

    /**
     * Returns the array base offset for an array.
     * @param clazz component type
     * @return base offset
     */
    fun arrayBaseOffset(clazz: Class<*>): Int

    /**
     * Returns the array index scale for an array.
     * @param clazz component type
     * @return index scale
     */
    fun arrayIndexScale(clazz: Class<*>): Int

    /**
     * Returns the object header size.
     * @return header size
     */
    fun objectHeaderSize(): Int

    /**
     * Returns the array header size.
     * This includes the array length pseudofield.
     * @return array header size
     */
    fun arrayHeaderSize(clazz: Class<*>): Int

    /**
     * Reads an int off the object at given offset.
     * @param obj instance
     * @param offset offset
     * @return the int
     */
    fun getInt(obj: Any, offset: Long): Int

    companion object {
        const val UNKNOWN_SIZE: Int = -1
    }
}