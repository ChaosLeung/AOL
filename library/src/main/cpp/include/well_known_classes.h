#ifndef AOL_WELL_KNOWN_CLASSES_H_
#define AOL_WELL_KNOWN_CLASSES_H_

#include "art_define.h"

namespace aol {

struct WellKnownClasses {
public:
    static void Init(JNIEnv *env);

    static jclass java_lang_Class;
    static jmethodID java_lang_Class_getName;
    static jfieldID java_lang_Class_sFields;
    static jfieldID java_lang_Class_iFields;
    static jfieldID java_lang_Class_numStaticFields;    // Android 23
    static jfieldID java_lang_Class_numInstanceFields;  // Android 23
    static jfieldID java_lang_Class_classSize;

    // Android 21 & 22
    static jclass java_lang_reflect_ArtField;
    static jmethodID java_lang_reflect_ArtField_getOffset;
    static jmethodID java_lang_reflect_ArtField_getName;
    static jmethodID java_lang_reflect_ArtField_getType;

    static jclass libcore_reflect_InternalNames;
    static jmethodID libcore_reflect_InternalNames_getInternalName;

    static jclass java_util_ArrayList;
    static jmethodID java_util_ArrayList_init;
    static jmethodID java_util_ArrayList_add;

    static jclass com_chaos_aol_JField;
    static jmethodID com_chaos_aol_JField_create;
};

}
#endif //AOL_WELL_KNOWN_CLASSES_H_
