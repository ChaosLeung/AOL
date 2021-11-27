package com.chaos.aol.info

import com.chaos.aol.extensions.getSafeName
import com.chaos.aol.vm.Vm
import java.lang.reflect.Field

class JField private constructor(
    val name: String,
    val type: String,
    val declaringClass: String,
    val offset: Int,
    val size: Int = -1
) : Comparable<JField> {

    fun getShortDeclaringClass(): String {
        val idx = declaringClass.lastIndexOf(".")
        return if (idx != -1) {
            declaringClass.substring(idx + 1)
        } else {
            declaringClass
        }
    }

    override fun compareTo(other: JField): Int {
        return offset.compareTo(other.offset)
    }

    override fun toString(): String {
        return "$name: $type"
    }

    companion object {
        fun parse(field: Field): JField {
            return JField(
                field.name,
                field.type.getSafeName(),
                field.declaringClass.getSafeName(),
                Vm.get().fieldOffset(field).toInt(),
                Vm.get().sizeOfField(field.type)
            )
        }

        @JvmStatic
        fun create(fieldName: String, fieldType: String, declaringClass: String, offset: Int, size: Int): JField {
            return JField(fieldName, fieldType, declaringClass, offset, size)
        }
    }
}