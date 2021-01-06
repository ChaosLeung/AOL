//
// Created by Chaos on 2020/12/23.
//

#ifndef MY_APPLICATION_JVMTI_AGENT_H
#define MY_APPLICATION_JVMTI_AGENT_H

#include "jvmti.h"

static jvmtiEnv *gJvmtiEnv;

const char *jvmtiJavaClazz = "com/chaos/library/aol/jvmti/JvmtiImpl";
const char *jvmtiGetObjectSizeMethod = "nativeGetObjectSize";
const char *jvmtiGetObjectSizeMethodSignature = "(Ljava/lang/Object;)J";

extern "C" JNIEXPORT jlong JNICALL nativeGetObjectSize(JNIEnv *env, jclass klass, jobject object);

extern "C" JNIEXPORT jint JNICALL Agent_OnAttach(JavaVM *vm, char *options, void *reserved);

extern "C" JNIEXPORT void JNICALL Agent_OnUnload(JavaVM *vm);

#endif //MY_APPLICATION_JVMTI_AGENT_H
