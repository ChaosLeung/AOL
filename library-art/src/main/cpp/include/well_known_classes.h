#ifndef AOL_WELL_KNOWN_CLASSES_H_
#define AOL_WELL_KNOWN_CLASSES_H_

#include "art_define.h"

namespace aol {

struct WellKnownClasses {
public:
    static void Init(JNIEnv* env);

    static jclass java_lang_Class;
    static jmethodID java_lang_Class_getName;
    static jmethodID java_lang_Class_getSuperClass;
    static jfieldID java_lang_Class_sFields;
    static jfieldID java_lang_Class_iFields;

    // Android 21 & 22
    static jclass java_lang_reflect_ArtField;
    static jmethodID java_lang_reflect_ArtField_getOffset;
    static jmethodID java_lang_reflect_ArtField_getName;
    static jmethodID java_lang_reflect_ArtField_getType;

    static jclass libcore_reflect_InternalNames;
    static jmethodID libcore_reflect_InternalNames_getInternalName;

    // Android 23
    static jfieldID java_lang_Class_numStaticFields;
    static jfieldID java_lang_Class_numInstanceFields;

    static jclass java_util_ArrayList;
    static jmethodID java_util_ArrayList_init;
    static jmethodID java_util_ArrayList_add;

    static jclass com_chaos_aol_FieldData;
    static jmethodID com_chaos_aol_FieldData_create;
};

}
#endif //AOL_WELL_KNOWN_CLASSES_H_
