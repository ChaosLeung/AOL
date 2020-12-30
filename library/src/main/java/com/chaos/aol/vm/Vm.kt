package com.chaos.aol.vm

object Vm {
    private val INSTANCE: VirtualMachine by lazy { Art }

    fun get(): VirtualMachine {
        return INSTANCE
    }
}