#include "build.h"
#include "bit_utils.h"
#include "dex_file.h"
#include "dex_cache.h"
#include "class.h"
#include "art_field.h"
#include "vector"
#include "length_prefixed_array.h"
#include "well_known_classes.h"
#include "java_objects.h"
#include "jni_utils.h"
#include "art_utils.h"
#include "log.h"

#define LOG_TAG "AOL_NATIVE"

using std::vector;
using std::string;
using std::shared_ptr;
using namespace aol;

jobject GetFieldsList(JNIEnv *env, vector<JField> *fields) {
    jobject list = env->NewObject(WellKnownClasses::java_util_ArrayList, WellKnownClasses::java_util_ArrayList_init);

    for (auto &f : *fields) {
        jstring name = env->NewStringUTF(f.name.c_str());
        jstring type = env->NewStringUTF(GetClassName(f.type).c_str());
        jstring declaring_class = env->NewStringUTF(f.declaringClass.c_str());
        jobject obj = env->CallStaticObjectMethod(WellKnownClasses::com_chaos_aol_JField,
                                                  WellKnownClasses::com_chaos_aol_JField_create,
                                                  name,
                                                  type,
                                                  declaring_class,
                                                  (jint) f.offset,
                                                  (jint) f.size);
        env->CallBooleanMethod(list, WellKnownClasses::java_util_ArrayList_add, obj);
    }
    return list;
}

void GetFieldsInternal(const shared_ptr<JClass> &klass, bool is_static, vector<JField> *out_fields) {
    if (klass->GetSuper() != nullptr) {
        GetFieldsInternal(klass->GetSuper(), is_static, out_fields);
    }
    auto fields = is_static ? klass->GetSFields() : klass->GetIFields();
    out_fields->insert(out_fields->end(), fields.begin(), fields.end());
}

void GetObjectFields(JNIEnv *env, jclass target, vector<JField> *out_fields) {
    auto klass = JClass::Create(env, target);
    GetFieldsInternal(klass, false, out_fields);
}

void GetClassFields(JNIEnv *env, jclass target, vector<JField> *out_fields) {
    auto java_lang_class = JClass::Create(env, WellKnownClasses::java_lang_Class);
    // add all instance fields of java.lang.Class
    GetFieldsInternal(java_lang_class, false, out_fields);
    auto klass = JClass::Create(env, target);
    GetFieldsInternal(klass, true, out_fields);
}

extern "C" {

jobject NativeGetFields(JNIEnv *env, jclass clazz, jclass target, jboolean is_static) {
    vector<JField> fields;

    if (is_static) {
        GetClassFields(env, target, &fields);
    } else {
        GetObjectFields(env, target, &fields);
    }

    return GetFieldsList(env, &fields);
}

void NativeInit(JNIEnv *env, jclass clazz) {
    gSdkVersion = GetIntProp("ro.build.version.sdk");
    gPreviewSdkVersion = GetIntProp("ro.build.version.preview_sdk");

    WellKnownClasses::Init(env);
}

jint NativeGetClassSize(JNIEnv *env, jclass clazz, jclass target) {
    return env->GetIntField(target, WellKnownClasses::java_lang_Class_classSize);
}

}

static JNINativeMethod methods[] = {
    {
        "nativeInit",
        "()V",
        reinterpret_cast<void *>(NativeInit)
    },
    {
        "nativeGetFields",
        "(Ljava/lang/Class;Z)Ljava/util/List;",
        reinterpret_cast<void *>(NativeGetFields)
    },
    {
        "getClassSize",
        "(Ljava/lang/Class;)I",
        reinterpret_cast<void *>(NativeGetClassSize)
    }
};

inline jint RegisterNatives(JNIEnv *env) {
    jclass clz = env->FindClass("com/chaos/aol/external/ArtNative");
    if (clz != nullptr) {
        return env->RegisterNatives(clz, methods, ARRAY_SIZE(methods));
    }
    return JNI_ERR;
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = nullptr;

    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        LOGW("Failed to get 1.6 JNIEnv");
        return JNI_ERR;
    }

    if (RegisterNatives(env) != JNI_OK) {
        LOGW("Failed to register native methods");
        return JNI_ERR;
    }

    LOGD("JNI_OnLoad End");

    return JNI_VERSION_1_6;
}