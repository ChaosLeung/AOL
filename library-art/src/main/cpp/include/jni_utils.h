#ifndef AOL_JNI_UTILS_H_
#define AOL_JNI_UTILS_H_

#include "art_define.h"
#include "well_known_classes.h"

using std::string;

inline string GetStdString(JNIEnv *env, jstring str) {
    const char *chs = env->GetStringUTFChars(str, JNI_FALSE);
    string result(chs);
    env->ReleaseStringUTFChars(str, chs);
    return result;
}

inline std::string GetClassName(JNIEnv *env, jclass clazz) {
    return GetStdString(env, reinterpret_cast<jstring>(env->CallObjectMethod(clazz, aol::WellKnownClasses::java_lang_Class_getName)));
}

#endif //AOL_JNI_UTILS_H_
