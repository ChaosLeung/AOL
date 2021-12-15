package com.chaos.aol.cli

import com.chaos.aol.cli.ops.ClassOp
import com.chaos.aol.cli.ops.ObjectOp
import com.chaos.aol.vm.Vm
import java.io.PrintStream
import kotlin.system.exitProcess

val ops = listOf(
    ClassOp(),
    ObjectOp()
).map { it.name() to it }.toMap()

fun main(vararg args: String) {
    val options = if (args.isNotEmpty()) args[0] else "help"

    Vm.init(null)

    val op = ops[options]
    when {
        op != null -> {
            op.run(*args.copyOfRange(1, args.size))
        }
        options != "help" -> {
            System.err.println("Unknown option: $options")
            System.err.println()
            printHelp(System.err)
            exitProcess(1)
        }
        else -> {
            printHelp(System.out)
            exitProcess(0)
        }
    }
}

private fun printHelp(pw: PrintStream) {
    pw.println("Usage: aol <option> <class>")
    pw.println()
    pw.println("Options: ")
    for (lop in ops.values) {
        pw.printf("  %8s: %s%n", lop.name(), lop.description())
    }
}