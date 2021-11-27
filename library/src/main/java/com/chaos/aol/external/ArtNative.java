package com.chaos.aol.external;

import android.content.Context;

import com.chaos.aol.info.FieldData;

import java.util.List;

import me.weishu.reflection.Reflection;

public class ArtNative {

    static {
        System.loadLibrary("aol-art");
    }

    public static void init(Context context) {
        Reflection.unseal(context);
        nativeInit();
    }

    public static native void nativeInit();

    public static native List<FieldData> nativeGetFields(Class<?> clz, boolean isStatic);

    public static native int getClassSize(Class<?> clz);
}
