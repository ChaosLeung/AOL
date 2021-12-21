package com.chaos.aol.info

import android.util.Log
import com.chaos.aol.vm.VirtualMachine
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.math.max

class ObjectLayout private constructor(
    private val klass: JClass,
) {

    fun toReadableString(isClassInfo: Boolean = false): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)

        dump(pw, isClassInfo)

        pw.close()

        return sw.toString()
    }

    fun dumpToLog(priority: Int = Log.DEBUG, tag: String = TAG, classInfo: Boolean = false) {
        toReadableString(classInfo).split('\n').forEach {
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

    fun dump(pw: PrintWriter, isClassInfo: Boolean = false) {
        val paddingDesc = "(alignment/padding gap)"
        val nextGapDesc = "(loss due to the next object alignment)"

        var maxTypeLen = "TYPE".length
        var maxClassLen = "DECLARING_CLASS".length
        val fields = if (isClassInfo) klass.staticFields else klass.instanceFields
        for (f in fields) {
            maxTypeLen = max(f.type.length, maxTypeLen)
            maxClassLen = max(f.declaringClass.length, maxClassLen)
        }
        maxTypeLen += 2
        maxClassLen += 2

        if (isClassInfo) {
            pw.println("${klass.name} class internals:")
        } else {
            pw.println("${klass.name} object internals:")
        }
        pw.printf(
            " %6s %5s %${maxClassLen}s %${maxTypeLen}s  %s%n",
            "OFFSET",
            "SIZE",
            "DECLARING_CLASS",
            "TYPE",
            "DESCRIPTION"
        )

        val commonFormat = " %6d %5d %${maxClassLen}s %${maxTypeLen}s  %s%n"

        var nextFree = klass.headerSize

        var interLoss = 0
        var exterLoss = 0

        for (f in fields) {
            if (f.offset > nextFree) {
                pw.printf(
                    commonFormat,
                    nextFree,
                    f.offset - nextFree,
                    "",
                    "",
                    paddingDesc,
                )
                interLoss += f.offset - nextFree
            }
            pw.printf(
                commonFormat,
                f.offset,
                f.size,
                f.declaringClass,
                f.type,
                f.name,
            )
            nextFree = f.offset + f.size
        }

        val sizeOf = if (isClassInfo) klass.classSize else klass.objectSize
        if (sizeOf != nextFree) {
            exterLoss = sizeOf - nextFree
            pw.printf(
                " %6d %5s %${maxClassLen}s %${maxTypeLen}s  %s%n",
                nextFree,
                exterLoss,
                "",
                "",
                nextGapDesc
            )
        }

        if (isClassInfo) {
            pw.printf("Class size: ")
        } else {
            pw.printf("Instance size: ")
        }
        if (sizeOf == VirtualMachine.UNKNOWN_SIZE) {
            pw.printf("<Unknown>")
        } else {
            pw.printf("%d bytes", sizeOf)
        }

        if (interLoss != 0 || exterLoss != 0) {
            pw.println()
            pw.printf(
                "Space losses: %d bytes internal + %d bytes external = %d bytes total",
                interLoss,
                exterLoss,
                interLoss + exterLoss
            )
        }
    }

    companion object {

        private const val TAG = "InstanceLayout"

        fun parse(obj: Any): ObjectLayout {
            return if (obj is Class<*>) {
                parse(null, obj)
            } else {
                parse(obj, obj.javaClass)
            }
        }

        private fun parse(instance: Any?, clazz: Class<*>): ObjectLayout {
            return ObjectLayout(JClass.parse(instance, clazz))
        }
    }
}