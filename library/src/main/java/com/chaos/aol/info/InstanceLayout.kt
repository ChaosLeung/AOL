package com.chaos.aol.info

import android.util.Log
import com.chaos.aol.extensions.getSafeName
import com.chaos.aol.vm.VirtualMachine
import com.chaos.aol.vm.Vm
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import kotlin.math.max

class InstanceLayout private constructor(
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

        pw.println("${classData.name} object internals:")
        pw.printf(
            " %6s %5s %${maxTypeLen}s  %s%n",
            "OFFSET",
            "SIZE",
            "TYPE",
            "DESCRIPTION"
        )

        val commonFormat = " %6d %5d %${maxTypeLen}s  %s%n"

        pw.printf(
            commonFormat,
            0,
            classData.headerSize,
            "",
            objHeaderDesc,
        )

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
                )
                interLoss += f.offset - nextFree
            }
            pw.printf(
                commonFormat,
                f.offset,
                f.size,
                f.type,
                f.name,
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

    companion object {

        private const val TAG = "InstanceLayout"

        fun parseInstance(instance: Any): InstanceLayout = parse(instance, instance.javaClass)

        fun parseClass(clazz: Class<*>): InstanceLayout = parse(null, clazz)

        private fun parse(instance: Any?, clazz: Class<*>): InstanceLayout {
            val vm = Vm.get()

            val classData = ClassData.parse(clazz)
            val fields = TreeSet(classData.fields)

            val instanceSize = when {
                instance != null -> vm.sizeOfInstance(instance)
                classData.isArray -> vm.arrayHeaderSize(clazz)
                else -> vm.sizeOfRegularObject(clazz)
            }

            if (classData.isArray) {
                fields.add(
                    FieldData.create(
                        "<elements>",
                        clazz.componentType!!.getSafeName(),
                        classData.name,
                        vm.arrayBaseOffset(clazz),
                        instanceSize - classData.headerSize
                    )
                )
            }
            return InstanceLayout(classData, fields.toList(), instanceSize)
        }
    }
}