package com.chaos.aol.info

import android.util.Log
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
    private val instanceSize: Int
) {

    fun toReadableString(): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)

        dump(pw)

        pw.close()

        return sw.toString()
    }

    fun dumpToLog(priority: Int = Log.DEBUG, tag: String = TAG) {
        toReadableString().split('\n').forEach {
            when (priority) {
                Log.VERBOSE -> Log.v(tag, it)
                Log.DEBUG -> Log.d(tag, it)
                Log.INFO -> Log.i(tag, it)
                Log.WARN -> Log.w(tag, it)
                Log.ERROR -> Log.e(tag, it)
                else -> Log.d(tag, it)
            }
        }
    }

    fun dump(pw: PrintWriter) {
        val paddingDesc = "(alignment/padding gap)"
        val nextGapDesc = "(loss due to the next object alignment)"
        val objHeaderDesc = "(object header)"

        var maxTypeLen = "TYPE".length
        for (f in fields) {
            maxTypeLen = max(f.type.length, maxTypeLen)
        }
        maxTypeLen += 2

        var maxDescLen = nextGapDesc.length
        for (f in fields) {
            maxDescLen = max(f.name.length, maxDescLen)
        }
        maxDescLen += 2

        pw.println("${classData.name} object internals:")
        pw.printf(
            " %6s %5s %${maxTypeLen}s  %-${maxDescLen}s %s%n",
            "OFFSET",
            "SIZE",
            "TYPE",
            "DESCRIPTION",
            "VALUE"
        )

        val vm = Vm.get()
        val instance = instanceRef.get()

        val commonFormat = " %6d %5d %${maxTypeLen}s  %-${maxDescLen}s %s%n"

        if (instance == null) {
            pw.printf(
                commonFormat,
                0,
                classData.headerSize,
                "",
                objHeaderDesc,
                "N/A"
            )
        } else {
            for (offset in 0 until classData.headerSize step Int.SIZE_BYTES) {
                val word = vm.getInt(instance, offset.toLong())
                pw.printf(
                    commonFormat,
                    offset,
                    Int.SIZE_BYTES,
                    "",
                    objHeaderDesc,
                    "${word.toHexString()} (${word.toBinaryString()}) (${word})"
                )
            }
        }

        var nextFree = classData.headerSize

        var interLoss = 0
        var exterLoss = 0

        for (f in fields) {
            if (f.offset > nextFree) {
                pw.printf(
                    commonFormat,
                    nextFree,
                    f.offset - nextFree,
                    "",
                    paddingDesc,
                    ""
                )
                interLoss += f.offset - nextFree
            }
            pw.printf(
                commonFormat,
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

        val sizeOf = instanceSize
        if (sizeOf != nextFree) {
            exterLoss = sizeOf - nextFree
            pw.printf(
                " %6d %5s %${maxTypeLen}s  %s%n",
                nextFree,
                exterLoss,
                "",
                nextGapDesc
            )
        }

        pw.printf("Instance size: ")
        if (sizeOf == VirtualMachine.UNKNOWN_SIZE) {
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
    }

    private fun Int.toBinaryString(): String {
        val step = Int.SIZE_BITS / Int.SIZE_BYTES
        return Integer.toBinaryString(this).padStart(Int.SIZE_BITS, '0').insert(step, step, ' ')
    }

    private fun Int.toHexString(): String {
        return Integer.toHexString(this).padStart(Int.SIZE_BITS / Int.SIZE_BYTES, '0').insert(2, 2, ' ')
    }

    private fun String.insert(start: Int = 0, step: Int = 1, ch: Char = ' '): String {
        if (start < 0 || step < 1) {
            return this
        }
        val sb = StringBuilder(this)
        val counts: Int = (length - start) / step
        for (i in 0 until counts) {
            sb.insert(i + start + i * step, ch)
        }
        return sb.toString()
    }

    companion object {

        private const val TAG = "InstanceLayout"

        fun parseInstance(instance: Any): InstanceLayout {
            val vm = Vm.get()

            val ref = WeakReference(instance)
            val classData = ClassData.parseInstance(instance)
            val fields = TreeSet(classData.fields)

            if (classData.isArray) {
                val clazz = instance.javaClass

                val instanceSize = vm.sizeOfObject(instance)

                fields.add(
                    FieldData.create(
                        "<elements>",
                        clazz.componentType!!.getSafeName(),
                        classData.name,
                        vm.arrayBaseOffset(clazz),
                        instanceSize - classData.headerSize
                    )
                )
                return InstanceLayout(ref, classData, fields.toList(), instanceSize)
            }

            return InstanceLayout(ref, classData, fields.toList(), vm.sizeOfObject(instance))
        }
    }
}