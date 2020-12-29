package com.chaos.aol.vm

class Vm {
    companion object {
        private val INSTANCE: VirtualMachine by lazy {
            ReflectUnsafe
        }

        @JvmStatic
        fun get(): VirtualMachine {
            return INSTANCE
        }
    }
}