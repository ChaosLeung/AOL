#include <leb128.h>
#include "dex_file.h"
#include "build.h"

#define DEXFILE_MEMBER(t, f) \
    isApiGe28() ? reinterpret_cast<const DexFile28 *>(t)->f : reinterpret_cast<const DexFile21 *>(t)->f

const uint8_t *DexFile::DataBegin() const {
    // The dex file on the Android 28 platform may be a CompatDexFile, so use data_begin_
    return isApiGe28() ? reinterpret_cast<const DexFile28 *>(this)->data_begin_ : reinterpret_cast<const DexFile21 *>(this)->begin_;
}

const FieldId &DexFile::GetFieldId(uint32_t idx) const {
    const FieldId *const ids = DEXFILE_MEMBER(this, field_ids_);
    return ids[idx];
}

const TypeId &DexFile::GetTypeId(uint16_t idx) const {
    const TypeId *const ids = DEXFILE_MEMBER(this, type_ids_);
    return ids[idx];
}

const char *DexFile::GetFieldName(const FieldId &field_id) const {
    return StringDataByIdx(field_id.name_idx_);
}

const char *DexFile::GetTypeDescriptor(const TypeId &type_id) const {
    return StringDataByIdx(type_id.descriptor_idx_);
}

const char *DexFile::GetFieldTypeDescriptor(const FieldId &field_id) const {
    const TypeId &type_id = GetTypeId(field_id.type_idx_);
    return GetTypeDescriptor(type_id);
}

const StringId &DexFile::GetStringId(uint32_t idx) const {
    const StringId *const ids = DEXFILE_MEMBER(this, string_ids_);
    return ids[idx];
}

const char *DexFile::StringDataByIdx(uint32_t idx) const {
    uint32_t unicode_length;
    return StringDataAndUtf16LengthByIdx(idx, &unicode_length);
}

const char *DexFile::StringDataAndUtf16LengthByIdx(uint32_t idx, uint32_t *utf16_length) const {
    const StringId &string_id = GetStringId(idx);
    return GetStringDataAndUtf16Length(string_id, utf16_length);
}

const char *DexFile::GetStringDataAndUtf16Length(const StringId &string_id,
                                                 uint32_t *utf16_length) const {
    const uint8_t *ptr = DataBegin() + string_id.string_data_off_;
    *utf16_length = DecodeUnsignedLeb128(&ptr);
    return reinterpret_cast<const char *>(ptr);
}