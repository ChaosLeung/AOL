#include "class.h"
#include "build.h"

#define CLASS_MEMBER(t, f) \
    isApiGe26() ? reinterpret_cast<const Class26 *>(t)->f : \
    isApiGe24() ? reinterpret_cast<const Class24 *>(t)->f : \
    isApiGe23() ? reinterpret_cast<const Class23 *>(t)->f : \
    isApiGe22() ? reinterpret_cast<const Class22 *>(t)->f : \
    reinterpret_cast<const Class21 *>(t)->f

DexCache *Class::GetDexCache() {
    return reinterpret_cast<DexCache *>(CLASS_MEMBER(this, dex_cache_));
}

uintptr_t Class::GetSFieldsPtr() {
    return static_cast<uintptr_t>(CLASS_MEMBER(this, sfields_));
}

uintptr_t Class::GetIFieldsPtr() {
    return static_cast<uintptr_t>(CLASS_MEMBER(this, ifields_));
}

uint32_t Class::GetSFieldsLength() {
    if (isApiGe23()) {
        return reinterpret_cast<const Class23 *>(this)->num_static_fields_;
    }
    return *reinterpret_cast<uint32_t *>(GetSFieldsPtr());
}

uint32_t Class::GetIFieldsLength() {
    if (isApiGe23()) {
        return reinterpret_cast<const Class23 *>(this)->num_instance_fields_;
    }
    return *reinterpret_cast<uint32_t *>(GetIFieldsPtr());
}
