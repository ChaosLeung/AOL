package com.chaos.aol.external;

import android.content.Context;

import com.chaos.aol.info.FieldData;

import java.util.List;

import me.weishu.reflection.Reflection;

public class ArtObjectLayout {

    static {
        System.loadLibrary("aol-art");
    }

    public static void init(Context context) {
        Reflection.unseal(context);
        nativeInit();
    }

    public static native void nativeInit();

    public static native void dumpObjectLayout(Class<?> clz);

    public static native void dumpClassLayout(Class<?> clz);

    public static native List<FieldData> getFields(Class<?> clz, boolean isStatic);
}
