package com.chaos.aol.info

import android.os.Build
import com.chaos.aol.extensions.getSafeName
import com.chaos.aol.vm.VirtualMachine
import com.chaos.aol.vm.Vm
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.max

class InstanceLayout private constructor(
    private val instance: WeakReference<Any?>,
    private val classData: ClassData,
    private val fields: List<FieldData>,
    private val instanceSize: Long
) {

    fun toPrintable(): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)

        val MSG_HEADER = "(object header)"

        var maxTypeLen = "TYPE".length
        for (f in fields) {
            maxTypeLen = max(f.type.length, maxTypeLen)
        }
        maxTypeLen += 2

        var maxDescrLen = MSG_HEADER.length
        for (f in fields) {
            maxDescrLen = max(f.name.length, maxDescrLen)
        }
        maxDescrLen += 2

        pw.println("Android " + Build.VERSION.SDK_INT)
        pw.println(classData.name + " object internals:")
        pw.printf(
            " %6s %5s %" + maxTypeLen + "s %-" + maxDescrLen + "s %s%n",
            "OFFSET",
            "SIZE",
            "TYPE",
            "DESCRIPTION",
            "VALUE"
        )
        pw.printf(
            " %6d %5d %" + maxTypeLen + "s %-" + maxDescrLen + "s %s%n",
            0,
            classData.headerSize,
            "",
            "(object header)",
            "N/A"
        )

        for (f in fields) {
            pw.printf(
                " %6d %5d %" + maxTypeLen + "s %-" + maxDescrLen + "s %s%n",
                f.offset,
                f.size,
                f.type,
                f.name,
                "N/A"
            )
        }

        val sizeOf: Long = instanceSize
        if (sizeOf == VirtualMachine.UNKNOWN_SIZE.toLong()) {
            pw.printf("Instance size: <Unknown>")
        } else {
            pw.printf("Instance size: %d bytes%n", sizeOf)
        }

        pw.close()

        return sw.toString()
    }

    companion object {

        fun parseInstance(instance: Any): InstanceLayout {
            val vm = Vm.get()

            val ref = WeakReference(instance)
            val classData = ClassData.parseInstance(instance)
            val fields = TreeSet(classData.fields)

            if (classData.isArray) {
                val clazz = instance::class.java

                val instanceSize = vm.sizeOfArrayObject(instance)

                fields.add(
                    FieldData.create(
                        "<elements>",
                        clazz.componentType!!.getSafeName(),
                        classData.name,
                        vm.arrayBaseOffset(clazz).toLong(),
                        instanceSize - classData.headerSize
                    )
                )
                return InstanceLayout(ref, classData, fields.toList(), instanceSize)
            }

            return InstanceLayout(ref, classData, fields.toList(), vm.sizeOfObject(instance))
        }
    }
}