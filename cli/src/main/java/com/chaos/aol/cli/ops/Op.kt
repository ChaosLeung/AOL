package com.chaos.aol.cli.ops

interface Op {
    fun name(): String

    fun description(): String

    fun run(vararg args: String)
}