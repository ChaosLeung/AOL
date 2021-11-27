#include "well_known_classes.h"
#include "build.h"

namespace aol {

jclass WellKnownClasses::java_lang_Class;
jmethodID WellKnownClasses::java_lang_Class_getName;
jmethodID WellKnownClasses::java_lang_Class_getSuperClass;
jfieldID WellKnownClasses::java_lang_Class_sFields;
jfieldID WellKnownClasses::java_lang_Class_iFields;

jclass WellKnownClasses::java_lang_reflect_ArtField = nullptr;
jmethodID WellKnownClasses::java_lang_reflect_ArtField_getOffset = nullptr;
jmethodID WellKnownClasses::java_lang_reflect_ArtField_getName = nullptr;
jmethodID WellKnownClasses::java_lang_reflect_ArtField_getType = nullptr;

jclass WellKnownClasses::libcore_reflect_InternalNames = nullptr;
jmethodID WellKnownClasses::libcore_reflect_InternalNames_getInternalName = nullptr;

jfieldID WellKnownClasses::java_lang_Class_numStaticFields = nullptr;
jfieldID WellKnownClasses::java_lang_Class_numInstanceFields = nullptr;

jclass WellKnownClasses::java_util_ArrayList;
jmethodID WellKnownClasses::java_util_ArrayList_init;
jmethodID WellKnownClasses::java_util_ArrayList_add;

jclass WellKnownClasses::com_chaos_aol_FieldData;
jmethodID WellKnownClasses::com_chaos_aol_FieldData_create;

void WellKnownClasses::Init(JNIEnv *env) {
    java_lang_Class = reinterpret_cast<jclass>(env->NewGlobalRef(env->FindClass("java/lang/Class")));
    java_lang_Class_getName = env->GetMethodID(java_lang_Class, "getName", "()Ljava/lang/String;");
    java_lang_Class_getSuperClass = env->GetMethodID(java_lang_Class, "getSuperclass", "()Ljava/lang/Class;");

    if (isApiGe23()) {
        java_lang_Class_iFields = env->GetFieldID(java_lang_Class, "iFields", "J");
        java_lang_Class_sFields = env->GetFieldID(java_lang_Class, "sFields", "J");
    } else {
        java_lang_Class_iFields = env->GetFieldID(java_lang_Class, "iFields", "[Ljava/lang/reflect/ArtField;");
        java_lang_Class_sFields = env->GetFieldID(java_lang_Class, "sFields", "[Ljava/lang/reflect/ArtField;");
    }

    if (isApi21() || isApi22()) {
        java_lang_reflect_ArtField = reinterpret_cast<jclass>(env->NewGlobalRef(env->FindClass("java/lang/reflect/ArtField")));
        java_lang_reflect_ArtField_getOffset = env->GetMethodID(java_lang_reflect_ArtField, "getOffset", "()I");
        java_lang_reflect_ArtField_getName = env->GetMethodID(java_lang_reflect_ArtField, "getName", "()Ljava/lang/String;");
        java_lang_reflect_ArtField_getType = env->GetMethodID(java_lang_reflect_ArtField, "getType", "()Ljava/lang/Class;");

        libcore_reflect_InternalNames = reinterpret_cast<jclass>(env->NewGlobalRef(env->FindClass("libcore/reflect/InternalNames")));
        libcore_reflect_InternalNames_getInternalName =
            env->GetStaticMethodID(libcore_reflect_InternalNames, "getInternalName", "(Ljava/lang/Class;)Ljava/lang/String;");
    }

    if (isApi23()) {
        java_lang_Class_numInstanceFields = env->GetFieldID(java_lang_Class, "numInstanceFields", "I");
        java_lang_Class_numStaticFields = env->GetFieldID(java_lang_Class, "numStaticFields", "I");
    }

    java_util_ArrayList = reinterpret_cast<jclass>(env->NewGlobalRef(env->FindClass("java/util/ArrayList")));
    java_util_ArrayList_init = env->GetMethodID(java_util_ArrayList, "<init>", "()V");
    java_util_ArrayList_add = env->GetMethodID(java_util_ArrayList, "add", "(Ljava/lang/Object;)Z");

    com_chaos_aol_FieldData = reinterpret_cast<jclass>(env->NewGlobalRef(env->FindClass("com/chaos/aol/info/FieldData")));
    com_chaos_aol_FieldData_create = env->GetStaticMethodID(com_chaos_aol_FieldData, "create",
                                                            "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;II)Lcom/chaos/aol/info/FieldData;");
}

}
