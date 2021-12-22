package com.chaos.aol.cli.ops

import java.lang.reflect.Array

abstract class ClassArgOp : Op {
    override fun run(vararg args: String) {
        val str = args[0].trim()
        if (str.lastIndexOf("[]") == str.length - 2) {
            runWithClass(Array.newInstance(Class.forName(str.substring(0, str.length - 2)), 0)::class.java)
        } else {
            runWithClass(Class.forName(args[0]))
        }
    }

    abstract fun runWithClass(clazz: Class<*>)
}