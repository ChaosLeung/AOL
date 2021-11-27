package com.chaos.aol.external

import android.content.Context
import com.chaos.aol.info.JField
import me.weishu.reflection.Reflection

object ArtNative {
    fun init(context: Context?) {
        Reflection.unseal(context)
        nativeInit()
    }

    fun getStaticFields(clz: Class<*>): List<JField> {
        return nativeGetFields(clz, true)
    }

    fun getInstanceFields(clz: Class<*>): MutableList<JField> {
        return nativeGetFields(clz, false)
    }

    private external fun nativeInit()
    private external fun nativeGetFields(clz: Class<*>, isStatic: Boolean): MutableList<JField>
    external fun getClassSize(clz: Class<*>): Int

    init {
        System.loadLibrary("aol-art")
    }
}