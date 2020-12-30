package com.chaos.aol.info

import com.chaos.aol.vm.Vm
import java.lang.reflect.Modifier

open class ClassData internal constructor(
    val name: String,
    fields: List<FieldData> = emptyList(),
    val headerSize: Int = Vm.get().objectHeaderSize(),
    val isArray: Boolean = false,
    val superClass: ClassData?,
) {

    val fields = fields
        get() = field.toList()

    companion object {

        fun parseInstance(instance: Any): ClassData {
            return parse(instance.javaClass)
        }

        private fun <T> parse(clazz: Class<T>): ClassData {
            val vm = Vm.get()

            val isArray = clazz.isArray

            val name = clazz.name
            val superClass = if (clazz.superclass != null) {
                parse(clazz.superclass!!)
            } else {
                null
            }

            val headerSize = if (isArray) {
                vm.arrayHeaderSize(clazz)
            } else {
                vm.objectHeaderSize()
            }

            val fields = mutableListOf<FieldData>()

            var klass: Class<in T>? = clazz
            do {
                fields.addAll(klass!!.declaredFields.filter {
                    !Modifier.isStatic(it.modifiers)
                }.map {
                    FieldData.parse(it)
                })
                klass = klass.superclass
            } while (klass != null)

            return ClassData(name, fields.toList(), headerSize, isArray, superClass)
        }
    }
}