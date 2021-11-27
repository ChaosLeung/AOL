#ifndef AOL_CLASS_H_
#define AOL_CLASS_H_

#include "art_define.h"
#include "object.h"
#include "dex_cache.h"
#include "art_field.h"

class ArtField;

class MANAGED Class21 final : Object {
public:
    uint32_t class_loader_;
    uint32_t component_type_;
    uint32_t dex_cache_;

    uint32_t direct_methods_;
    uint32_t ifields_;
    uint32_t iftable_;
    uint32_t imtable_;
    uint32_t name_;
    uint32_t sfields_;
    uint32_t super_class_;
    uint32_t verify_error_class_;
    uint32_t virtual_methods_;
    uint32_t vtable_;

    uint32_t access_flags_;
    uint32_t class_size_;
    pid_t clinit_thread_id_;
    int32_t dex_class_def_idx_;
    int32_t dex_type_idx_;
    uint32_t num_reference_instance_fields_;
    uint32_t num_reference_static_fields_;
    uint32_t object_size_;
    uint32_t primitive_type_;
    uint32_t reference_instance_offsets_;
    uint32_t reference_static_offsets_;
    uint32_t status_;
};

class MANAGED Class22 final : Object {
public:
    uint32_t class_loader_;
    uint32_t component_type_;
    uint32_t dex_cache_;

    uint32_t dex_cache_strings_;
    uint32_t direct_methods_;
    uint32_t ifields_;
    uint32_t iftable_;
    uint32_t name_;
    uint32_t sfields_;
    uint32_t super_class_;
    uint32_t verify_error_class_;
    uint32_t virtual_methods_;
    uint32_t vtable_;

    uint32_t access_flags_;
    uint32_t class_size_;
    pid_t clinit_thread_id_;
    int32_t dex_class_def_idx_;
    int32_t dex_type_idx_;
    uint32_t num_reference_instance_fields_;
    uint32_t num_reference_static_fields_;
    uint32_t object_size_;
    uint32_t primitive_type_;
    uint32_t reference_instance_offsets_;
    uint32_t reference_static_offsets_;
    uint32_t status_;
};

class MANAGED Class23 final : Object {
public:
    uint32_t class_loader_;
    uint32_t component_type_;
    uint32_t dex_cache_;

    uint32_t dex_cache_strings_;
    uint32_t iftable_;
    uint32_t name_;
    uint32_t super_class_;
    uint32_t verify_error_class_;
    uint32_t vtable_;

    uint32_t access_flags_;
    uint64_t direct_methods_;
    uint64_t ifields_;
    uint64_t sfields_;
    uint64_t virtual_methods_;
    uint32_t class_size_;
    int32_t clinit_thread_id_;
    int32_t dex_class_def_idx_;
    int32_t dex_type_idx_;
    uint32_t num_direct_methods_;
    uint32_t num_instance_fields_;
    uint32_t num_reference_instance_fields_;
    uint32_t num_reference_static_fields_;
    uint32_t num_static_fields_;
    uint32_t num_virtual_methods_;
    uint32_t object_size_;
    uint32_t primitive_type_;
    uint32_t reference_instance_offsets_;
    uint32_t status_;
};

class MANAGED Class24 final : Object {
public:
    uint32_t annotation_type_;
    uint32_t class_loader_;
    uint32_t component_type_;
    uint32_t dex_cache_;

    uint32_t iftable_;
    uint32_t name_;
    uint32_t super_class_;
    uint32_t verify_error_;
    uint32_t vtable_;

    uint32_t access_flags_;
    uint64_t dex_cache_strings_;
    uint64_t ifields_;
    uint64_t methods_;
    uint64_t sfields_;
    uint32_t class_flags_;
    uint32_t class_size_;
    int32_t clinit_thread_id_;
    int32_t dex_class_def_idx_;
    int32_t dex_type_idx_;
    uint32_t num_reference_instance_fields_;
    uint32_t num_reference_static_fields_;
    uint32_t object_size_;
    uint32_t primitive_type_;
    uint32_t reference_instance_offsets_;
    uint32_t status_;
    uint16_t copied_methods_offset_;
    uint16_t virtual_methods_offset_;
};

class MANAGED Class26 final : Object {
public:
    uint32_t class_loader_;
    uint32_t component_type_;
    uint32_t dex_cache_;

    uint32_t ext_data_;
    uint32_t iftable_;
    uint32_t name_;
    uint32_t super_class_;
    uint32_t vtable_;

    uint64_t ifields_;
    uint64_t methods_;
    uint64_t sfields_;
    uint32_t access_flags_;
    uint32_t class_flags_;
    uint32_t class_size_;
    int32_t clinit_thread_id_;
    int32_t dex_class_def_idx_;
    int32_t dex_type_idx_;
    uint32_t num_reference_instance_fields_;
    uint32_t num_reference_static_fields_;
    uint32_t object_size_;
    uint32_t object_size_alloc_fast_path_;
    uint32_t primitive_type_;
    uint32_t reference_instance_offsets_;
    uint32_t status_;
    uint16_t copied_methods_offset_;
    uint16_t virtual_methods_offset_;
};

class Class {
public:
    DexCache* GetDexCache();
    uintptr_t GetSFieldsPtr();
    uintptr_t GetIFieldsPtr();
    uint32_t GetSFieldsLength();
    uint32_t GetIFieldsLength();
};

#endif //AOL_CLASS_H_
