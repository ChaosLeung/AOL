#ifndef AOL_DEX_FILE_H
#define AOL_DEX_FILE_H

#include "art_define.h"
#include "dex_file_structs.h"
#include "string"

class DexFile21 {
public:
    virtual ~DexFile21();

    // The base address of the memory mapping.
    const uint8_t *const begin_;

    // The size of the underlying memory allocation in bytes.
    const size_t size_;

    // Typically the dex file name when available, alternatively some identifying string.
    //
    // The ClassLinker will use this to match DexFiles the boot class
    // path to DexCache::GetLocation when loading from an image.
    const std::string location_;

    const uint32_t location_checksum_;

    // Manages the underlying memory allocation.
    uintptr_t *mem_map_;

    // Points to the header section.
    const Header *const header_;

    // Points to the base of the string identifier list.
    const StringId *const string_ids_;

    // Points to the base of the type identifier list.
    const TypeId *const type_ids_;

    // Points to the base of the field identifier list.
    const FieldId *const field_ids_;

    // Points to the base of the method identifier list.
    const MethodId *const method_ids_;

    // Points to the base of the prototype identifier list.
    const ProtoId *const proto_ids_;

    // Points to the base of the class definition list.
    const ClassDef *const class_defs_;
};

class DexFile28 {
public:
    virtual ~DexFile28();

    // The base address of the memory mapping.
    const uint8_t *const begin_;

    // The size of the underlying memory allocation in bytes.
    const size_t size_;

    // The base address of the data section (same as Begin() for standard dex).
    const uint8_t *const data_begin_;

    // The size of the data section.
    const size_t data_size_;

    // Typically the dex file name when available, alternatively some identifying string.
    //
    // The ClassLinker will use this to match DexFiles the boot class
    // path to DexCache::GetLocation when loading from an image.
    const std::string location_;

    const uint32_t location_checksum_;

    // Points to the header section.
    const Header *const header_;

    // Points to the base of the string identifier list.
    const StringId *const string_ids_;

    // Points to the base of the type identifier list.
    const TypeId *const type_ids_;

    // Points to the base of the field identifier list.
    const FieldId *const field_ids_;

    // Points to the base of the method identifier list.
    const MethodId *const method_ids_;

    // Points to the base of the prototype identifier list.
    const ProtoId *const proto_ids_;

    // Points to the base of the class definition list.
    const ClassDef *const class_defs_;
};

class DexFile {
public:
    const uint8_t *DataBegin() const;

    const FieldId &GetFieldId(uint32_t idx) const;

    const TypeId& GetTypeId(uint16_t idx) const;

    const char *GetFieldName(const FieldId &field_id) const;

    const char* GetTypeDescriptor(const TypeId& type_id) const;

    const char* GetFieldTypeDescriptor(const FieldId& field_id) const;

    const StringId &GetStringId(uint32_t idx) const;

    const char *StringDataByIdx(uint32_t idx) const;

    const char *StringDataAndUtf16LengthByIdx(uint32_t idx, uint32_t *utf16_length) const;

    const char *GetStringDataAndUtf16Length(const StringId &string_id,
                                            uint32_t *utf16_length) const;
};

#endif //AOL_DEX_FILE_H
