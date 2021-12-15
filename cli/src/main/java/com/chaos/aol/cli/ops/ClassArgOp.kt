package com.chaos.aol.cli.ops

abstract class ClassArgOp : Op {
    override fun run(vararg args: String) {
        runWithClass(Class.forName(args[0]))
    }

    abstract fun runWithClass(clazz: Class<*>)
}