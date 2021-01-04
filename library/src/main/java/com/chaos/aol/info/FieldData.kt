package com.chaos.aol.info

import com.chaos.aol.extensions.getSafeName
import com.chaos.aol.vm.Vm
import java.lang.reflect.Field

class FieldData private constructor(
    val ref: Field?,
    name: String,
    val type: String,
    declaringClass: String,
    val offset: Int,
    val size: Int = -1
) : Comparable<FieldData> {

    val name: String

    init {
        val isArray = declaringClass.startsWith("[")
        this.name = if (isArray) {
            name
        } else {
            val idx = declaringClass.lastIndexOf(".")
            if (idx != -1) {
                "${declaringClass.substring(idx + 1)}.$name"
            } else {
                "$declaringClass.$name"
            }
        }
    }

    override fun compareTo(other: FieldData): Int {
        return offset.compareTo(other.offset)
    }

    override fun toString(): String {
        return "$name: $type"
    }

    companion object {
        fun parse(field: Field): FieldData {
            return FieldData(
                field,
                field.name,
                field.type.getSafeName(),
                field.declaringClass.getSafeName(),
                Vm.get().fieldOffset(field).toInt(),
                Vm.get().sizeOfField(field)
            )
        }

        fun create(fieldName: String, fieldType: String, declaringClass: String, offset: Int, size: Int): FieldData {
            return FieldData(null, fieldName, fieldType, declaringClass, offset, size)
        }
    }
}