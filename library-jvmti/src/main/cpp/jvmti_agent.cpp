//
// Created by Chaos on 2020/12/23.
//
#include <jni.h>
#include <string>
#include <android/log.h>
#include "jvmti_agent.h"

#define LOG_TAG "jvmti-agent"

#define ALOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jlong JNICALL nativeGetObjectSize(
        JNIEnv *env, jclass clazz, jobject object) {
    jlong size;

    if (gJvmtiEnv == nullptr) {
        ALOGI("Failure running GetObjectSize: global_jvmti_env is null");
        return -1;
    }

    jvmtiError result = gJvmtiEnv->GetObjectSize(object, &size);
    if (result != JVMTI_ERROR_NONE) {
        char *err;
        gJvmtiEnv->GetErrorName(result, &err);
        ALOGI("Failure running GetObjectSize: %s", err);
        gJvmtiEnv->Deallocate(reinterpret_cast<unsigned char *>(err));
        return -1;
    }

    return size;
}

void SetAllCapabilities(jvmtiEnv *jvmti) {
    jvmtiCapabilities caps;
    jvmtiError error;
    error = jvmti->GetPotentialCapabilities(&caps);
    error = jvmti->AddCapabilities(&caps);
}

jvmtiEnv *CreateJvmtiEnv(JavaVM *vm) {
    jvmtiEnv *jvmti_env;
    jint result = vm->GetEnv((void **) &jvmti_env, JVMTI_VERSION_1_2);
    if (result != JNI_OK) {
        return nullptr;
    }

    return jvmti_env;
}

void SetEventNotification(jvmtiEnv *jvmti, jvmtiEventMode mode,
                          jvmtiEvent event_type) {
    jvmtiError err = jvmti->SetEventNotificationMode(mode, event_type, nullptr);
}

void JNICALL onNativeMethodBind(jvmtiEnv *jvmti_env, JNIEnv *jni_env, jthread thread, jmethodID method,
                                void *address, void **new_address_ptr) {
    jclass clazz = jni_env->FindClass(jvmtiJavaClazz);
    jmethodID getObjectSizeMethod = jni_env->GetStaticMethodID(clazz, jvmtiGetObjectSizeMethod,
                                                               jvmtiGetObjectSizeMethodSignature);
    if (method == getObjectSizeMethod) {
        *new_address_ptr = reinterpret_cast<void *>(&nativeGetObjectSize);
    }
}

extern "C" JNIEXPORT jint JNICALL Agent_OnAttach(JavaVM *vm, char *options,
                                                 void *reserved) {
    jvmtiEnv *jvmti_env = CreateJvmtiEnv(vm);

    if (jvmti_env == nullptr) {
        ALOGI("Failure to getting jvmtiEnv");
        return JNI_ERR;
    }

    gJvmtiEnv = jvmti_env;

    SetAllCapabilities(jvmti_env);

    jvmtiEventCallbacks callbacks;
    memset(&callbacks, 0, sizeof(callbacks));

    // For holding jvmtiEnv instance
    callbacks.NativeMethodBind = &onNativeMethodBind;

    jvmtiError error = jvmti_env->SetEventCallbacks(&callbacks, sizeof(callbacks));

    SetEventNotification(jvmti_env, JVMTI_ENABLE, JVMTI_EVENT_NATIVE_METHOD_BIND);

    ALOGI("The jvmti agent attached");
    return JNI_OK;
}

extern "C" JNIEXPORT void JNICALL Agent_OnUnload(JavaVM *vm) {
    gJvmtiEnv = nullptr;
    ALOGI("The jvmti agent unloaded");
}

extern "C" JNIEXPORT jlong JNICALL nativeGetObjectSizeStub(
        JNIEnv *env, jclass clazz, jobject object) {
    // Stub method for getObjectSize
    return JNI_OK;
}

static JNINativeMethod methods[] = {
        {jvmtiGetObjectSizeMethod, jvmtiGetObjectSizeMethodSignature, reinterpret_cast<void *>(&nativeGetObjectSizeStub)},
};

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    jclass clazz = env->FindClass(jvmtiJavaClazz);
    env->RegisterNatives(clazz, methods, sizeof(methods) / sizeof(methods[0]));
    return JNI_VERSION_1_6;
}