package com.chaos.aol.info

import com.chaos.aol.extensions.getSafeName
import com.chaos.aol.external.ArtNative
import com.chaos.aol.vm.Vm

internal class JClass internal constructor(
    val name: String,
    val headerSize: Int = Vm.get().objectHeaderSize(),
    val objectSize: Int,
    val classSize: Int,
    val staticFields: List<JField> = emptyList(),
    val instanceFields: List<JField> = emptyList()
) {

    companion object {

        fun parse(instance: Any?, clazz: Class<*>): JClass {
            val vm = Vm.get()

            val isArray = clazz.isArray

            val name = clazz.name

            val headerSize = if (isArray) {
                vm.arrayHeaderSize(clazz)
            } else {
                vm.objectHeaderSize()
            }

            val objectSize = when {
                instance != null -> vm.sizeOfInstance(instance)
                isArray -> vm.arrayHeaderSize(clazz)
                else -> vm.sizeOfRegularObject(clazz)
            }

            val classSize = ArtNative.getClassSize(clazz)

            val staticFields = ArtNative.getStaticFields(clazz)
            val instanceFields = ArtNative.getInstanceFields(clazz)

            if (isArray) {
                instanceFields.add(
                    JField.create(
                        "<elements>",
                        clazz.componentType!!.getSafeName(),
                        name,
                        vm.arrayBaseOffset(clazz),
                        objectSize - headerSize
                    )
                )
            }
            return JClass(name, headerSize, objectSize, classSize, staticFields, instanceFields)
        }
    }
}