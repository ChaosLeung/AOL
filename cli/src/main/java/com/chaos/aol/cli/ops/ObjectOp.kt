package com.chaos.aol.cli.ops

import com.chaos.aol.info.ObjectLayout

class ObjectOp : ClassArgOp() {
    override fun name(): String = "instance"

    override fun description(): String = "Show the object internals: instance field layout"

    override fun runWithClass(clazz: Class<*>) {
        println(ObjectLayout.parse(clazz).toReadableString(false))
    }
}