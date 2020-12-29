package com.chaos.aol.info

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

        val paddingDesc = "(alignment/padding gap)"
        val nextGapDesc = "(loss due to the next object alignment)"

        var maxTypeLen = "TYPE".length
        for (f in fields) {
            maxTypeLen = max(f.type.length, maxTypeLen)
        }
        maxTypeLen += 2

        var maxDescLen = max(paddingDesc.length, nextGapDesc.length)
        for (f in fields) {
            maxDescLen = max(f.name.length, maxDescLen)
        }
        maxDescLen += 2

        pw.println(classData.name + " object internals:")
        pw.printf(
            " %6s %5s %" + maxTypeLen + "s %-" + maxDescLen + "s %s%n",
            "OFFSET",
            "SIZE",
            "TYPE",
            "DESCRIPTION",
            "VALUE"
        )
        pw.printf(
            " %6d %5d %" + maxTypeLen + "s %-" + maxDescLen + "s %s%n",
            0,
            classData.headerSize,
            "",
            "(object header)",
            "N/A"
        )

        var nextFree: Long = classData.headerSize.toLong()

        var interLoss: Long = 0
        var exterLoss: Long = 0

        for (f in fields) {
            if (f.offset > nextFree) {
                pw.printf(
                    " %6d %5d %${maxTypeLen}s %-${maxDescLen}s%n",
                    nextFree,
                    f.offset - nextFree,
                    "",
                    paddingDesc
                )
                interLoss += f.offset - nextFree
            }
            pw.printf(
                " %6d %5d %${maxTypeLen}s %-${maxDescLen}s %s%n",
                f.offset,
                f.size,
                f.type,
                f.name,
                "N/A"
            )
            nextFree = f.offset + f.size
        }

        val sizeOf: Long = instanceSize
        if (sizeOf != nextFree) {
            exterLoss = sizeOf - nextFree
            pw.printf(" %6d %5s %${maxTypeLen}s %s%n", nextFree, exterLoss, "", nextGapDesc)
        }

        pw.printf("Instance size: ")
        if (sizeOf == VirtualMachine.UNKNOWN_SIZE.toLong()) {
            pw.printf("<Unknown>")
        } else {
            pw.printf("%d bytes", sizeOf)
        }
        pw.println()
        pw.printf(
            "Space losses: %d bytes internal + %d bytes external = %d bytes total%n",
            interLoss,
            exterLoss,
            interLoss + exterLoss
        )

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