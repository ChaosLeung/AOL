package com.chaos.aol.info

import com.chaos.aol.extensions.getSafeName
import com.chaos.aol.vm.Vm
import java.lang.reflect.Field

class FieldData private constructor(
    val name: String,
    val type: String,
    val declaringClass: String,
    val offset: Long,
    val size: Long = -1
) : Comparable<FieldData> {

    override fun compareTo(other: FieldData): Int {
        return offset.compareTo(other.offset)
    }

    override fun toString(): String {
        return "$name: $type"
    }

    companion object {
        fun parse(field: Field): FieldData {
            return FieldData(
                field.name,
                field.type.getSafeName(),
                field.declaringClass.getSafeName(),
                Vm.get().fieldOffset(field),
                Vm.get().sizeOfField(field).toLong()
            )
        }

        fun create(fieldName: String, fieldType: String, declaringClass: String, offset: Long, size: Long): FieldData {
            return FieldData(fieldName, fieldType, declaringClass, offset, size)
        }
    }
}