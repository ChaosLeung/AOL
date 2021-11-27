#include "art_field.h"
#include "build.h"
#include "jni.h"
#include "art_utils.h"

#define ART_FIELD_MEMBER(t, f) \
    isApiGe23() ? reinterpret_cast<const ArtField23 *>(t)->f : reinterpret_cast<const ArtField21 *>(t)->f

uint32_t ArtField::GetDexFieldIndex() {
    return ART_FIELD_MEMBER(this, field_dex_idx_);
}

Class *ArtField::GetDeclaringClass() {
    return reinterpret_cast<Class *>(ART_FIELD_MEMBER(this, declaring_class_));
}

const char *ArtField::GetName() {
    uint32_t field_index = GetDexFieldIndex();
    const DexFile *dex_file = GetDexFile();
    return dex_file->GetFieldName(dex_file->GetFieldId(field_index));
}

const char *ArtField::GetTypeDescriptor() {
    uint32_t field_index = GetDexFieldIndex();
    const DexFile *dex_file = GetDexFile();
    const FieldId &field_id = dex_file->GetFieldId(field_index);
    return dex_file->GetFieldTypeDescriptor(field_id);
}

const DexCache *ArtField::GetDexCache() {
    Class *klass = GetDeclaringClass();
    return klass->GetDexCache();
}

const DexFile *ArtField::GetDexFile() {
    return GetDexCache()->GetDexFile();
}

uint32_t ArtField::GetOffset() {
    return ART_FIELD_MEMBER(this, offset_);
}

size_t ArtField::Size() {
    return GetTypeSize(GetTypeDescriptor());
}

size_t ArtField::Alignment() {
    return isApiGe23() ? alignof(ArtField23) : alignof(ArtField21);
}

size_t ArtField::ObjectSize() {
    return isApiGe23() ? sizeof(ArtField23) : sizeof(ArtField21);
}
