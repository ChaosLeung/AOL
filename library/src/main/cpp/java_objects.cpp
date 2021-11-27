#include "java_objects.h"
#include "build.h"
#include "well_known_classes.h"
#include "art_field.h"
#include "length_prefixed_array.h"
#include "jni_utils.h"
#include "art_utils.h"
#include "log.h"

#define LOG_TAG "JClass"

using namespace aol;
using std::vector;
using std::string;
using std::sort;

inline jfieldID GetFieldsFieldId(bool is_static) {
    return is_static ? WellKnownClasses::java_lang_Class_sFields : WellKnownClasses::java_lang_Class_iFields;
}

typedef void (*GetFieldsFunc)(JNIEnv *env, jclass clazz, string &class_name, bool is_static, vector<JField> *out_fields);

inline void GetFieldsApi21(JNIEnv *env, jclass clazz, string &class_name, bool is_static, vector<JField> *out_fields) {
    auto fields = reinterpret_cast<jobjectArray>(env->GetObjectField(clazz, GetFieldsFieldId(is_static)));
    if (fields == nullptr) {
        return;
    }

    jsize length = env->GetArrayLength(fields);
    if (length == 0) {
        return;
    }

    for (int i = 0; i < length; i++) {
        jobject f = env->GetObjectArrayElement(fields, i);
        string name = GetStdString(env, reinterpret_cast<jstring>(env->CallObjectMethod(f, WellKnownClasses::java_lang_reflect_ArtField_getName)));
        auto type_class = reinterpret_cast<jclass>(env->CallObjectMethod(f, WellKnownClasses::java_lang_reflect_ArtField_getType));
        string type = GetStdString(env,
                                   reinterpret_cast<jstring>(env->CallStaticObjectMethod(WellKnownClasses::libcore_reflect_InternalNames,
                                                                                         WellKnownClasses::libcore_reflect_InternalNames_getInternalName,
                                                                                         type_class)));
//        string type_class_name = GetClassName(env, type_class);
//        string type = GetTypeDescriptor(type_class_name);
        uint32_t offset = env->CallIntMethod(f, WellKnownClasses::java_lang_reflect_ArtField_getOffset);
        size_t size = GetTypeSize(type.c_str());
        out_fields->push_back(JField(name, type, class_name, offset, size));
    }
}

inline void GetFieldsApi23(JNIEnv *env, jclass clazz, string &class_name, bool is_static, vector<JField> *out_fields) {
    jfieldID fields_length_field = is_static ? WellKnownClasses::java_lang_Class_numStaticFields : WellKnownClasses::java_lang_Class_numInstanceFields;
    jint fields_length = env->GetIntField(clazz, fields_length_field);
    if (fields_length <= 0) {
        return;
    }

    jlong fields_ptr = env->GetLongField(clazz, GetFieldsFieldId(is_static));
    size_t size = ArtField::ObjectSize();
    auto begin = StrideIterator<ArtField>(reinterpret_cast<ArtField *>(fields_ptr), size);
    auto end = StrideIterator<ArtField>(reinterpret_cast<ArtField *>(fields_ptr + size * fields_length), size);

    for (auto f = begin; f != end; f++) {
        out_fields->push_back(JField(f->GetName(), f->GetTypeDescriptor(), class_name, f->GetOffset(), f->Size()));
    }
}

inline void GetFieldsApi24(JNIEnv *env, jclass clazz, string &class_name, bool is_static, vector<JField> *out_fields) {
    jlong fields_ptr = env->GetLongField(clazz, GetFieldsFieldId(is_static));
    auto fields = reinterpret_cast<LengthPrefixedArray<ArtField> *>(fields_ptr);
    if (fields == nullptr) {
        return;
    }

    size_t size = ArtField::ObjectSize();
    size_t align = ArtField::Alignment();
    for (auto f = fields->begin(size, align); f != fields->end(size, align); f++) {
        out_fields->push_back(JField(f->GetName(), f->GetTypeDescriptor(), class_name, f->GetOffset(), f->Size()));
    }
}

bool FieldComparator(JField &f1, JField &f2) {
    return f1.offset < f2.offset;
}

std::shared_ptr<JClass> JClass::Create(JNIEnv *env, jclass clazz) {
    GetFieldsFunc get_fields_func;
    if (isApiGe24()) {
        get_fields_func = GetFieldsApi24;
    } else if (isApiGe23()) {
        get_fields_func = GetFieldsApi23;
    } else {
        get_fields_func = GetFieldsApi21;
    }

    std::shared_ptr<JClass> super = nullptr;
    jclass super_clazz = env->GetSuperclass(clazz);
    if (super_clazz != nullptr) {
        super = Create(env, super_clazz);
    }

    auto class_name = GetClassName(env, clazz);

    vector<JField> sfields;
    vector<JField> ifields;

    get_fields_func(env, clazz, class_name, true, &sfields);
    get_fields_func(env, clazz, class_name, false, &ifields);

    sort(sfields.begin(), sfields.end(), FieldComparator);
    sort(ifields.begin(), ifields.end(), FieldComparator);

    return std::shared_ptr<JClass>(new JClass(clazz, super, sfields, ifields));
}
