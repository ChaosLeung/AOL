package com.chaos.aol.vm

import android.content.Context
import com.chaos.aol.external.ArtNative

object Vm {
    private val INSTANCE: VirtualMachine by lazy { Art }

    fun init(context: Context) {
        ArtNative.init(context)
    }

    fun get(): VirtualMachine {
        return INSTANCE
    }
}