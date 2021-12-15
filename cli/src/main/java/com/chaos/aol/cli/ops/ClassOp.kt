package com.chaos.aol.cli.ops

import com.chaos.aol.info.ObjectLayout

class ClassOp : ClassArgOp() {
    override fun name(): String = "class"

    override fun description(): String = "Show the class internals: static field layout"

    override fun runWithClass(clazz: Class<*>) {
        println(ObjectLayout.parse(clazz).toReadableString(true))
    }
}