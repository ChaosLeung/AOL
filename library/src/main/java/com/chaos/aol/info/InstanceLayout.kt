package com.chaos.aol.info

import com.chaos.aol.extensions.getSafeName
import com.chaos.aol.utils.ObjectUtils
import com.chaos.aol.vm.VirtualMachine
import com.chaos.aol.vm.Vm
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.ref.WeakReference
import java.util.*
import kotlin.math.max

class InstanceLayout private constructor(
    private val instanceRef: WeakReference<Any?>,
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
            " %6s %5s %${maxTypeLen}s %-${maxDescLen}s %s%n",
            "OFFSET",
            "SIZE",
            "TYPE",
            "DESCRIPTION",
            "VALUE"
        )

        val vm = Vm.get()

        val instance = instanceRef.get()

        if (instance == null) {
            pw.printf(
                " %6d %5d %${maxTypeLen}s %-${maxDescLen}s %s%n",
                0,
                classData.headerSize,
                "",
                "(object header)",
                "N/A"
            )
        } else {
            for (offset in 0 until classData.headerSize.toLong() step 4) {
                val word = vm.getInt(instance, offset)
                pw.printf(
                    " %6d %5d %${+maxTypeLen}s %-${maxDescLen}s %s%n", offset, 4, "", "(object header)",
                    toHex(word shr 0 and 0xFF) +
                            " ${toHex(word shr 8 and 0xFF)}" +
                            " ${toHex(word shr 16 and 0xFF)}" +
                            " ${toHex(word shr 24 and 0xFF)}" +
                            " (${toBinary(word shr 0 and 0xFF)}" +
                            " ${toBinary(word shr 8 and 0xFF)}" +
                            " ${toBinary(word shr 16 and 0xFF)}" +
                            " ${toBinary(word shr 24 and 0xFF)})" +
                            " ($word)"
                )
            }
        }

        var nextFree = classData.headerSize.toLong()

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
                if (instance == null || f.ref == null)
                    "N/A"
                else
                    ObjectUtils.valueString(ObjectUtils.value(instance, f.ref))
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

    private fun toBinary(x: Int): String {
        var s = Integer.toBinaryString(x)
        val deficit = 8 - s.length
        for (c in 0 until deficit) {
            s = "0$s"
        }
        return s
    }

    private fun toHex(x: Int): String {
        var s = Integer.toHexString(x)
        val deficit = 2 - s.length
        for (c in 0 until deficit) {
            s = "0$s"
        }
        return s
    }

    companion object {

        fun parseInstance(instance: Any): InstanceLayout {
            val vm = Vm.get()

            val ref = WeakReference(instance)
            val classData = ClassData.parseInstance(instance)
            val fields = TreeSet(classData.fields)

            if (classData.isArray) {
                val clazz = instance.javaClass

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