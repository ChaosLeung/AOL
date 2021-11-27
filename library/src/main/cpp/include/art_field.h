#ifndef AOL_DEX_FIELD_H
#define AOL_DEX_FIELD_H

#include "art_define.h"
#include "object.h"
#include "dex_cache.h"
#include "class.h"

class Class;

class MANAGED ArtField21 final : public Object {
public:
    uint32_t declaring_class_;
    uint32_t access_flags_;
    uint32_t field_dex_idx_;
    uint32_t offset_;
};

class ArtField23 final {
public:
    uint32_t declaring_class_;
    uint32_t access_flags_;
    uint32_t field_dex_idx_;
    uint32_t offset_;
};

class ArtField {
public:
    static size_t ObjectSize();

    static size_t Alignment();

    Class *GetDeclaringClass();

    uint32_t GetDexFieldIndex();

    const char *GetName();

    const char *GetTypeDescriptor();

    const DexCache *GetDexCache();

    const DexFile *GetDexFile();

    uint32_t GetOffset();

    size_t Size();
};

#endif //AOL_DEX_FIELD_H
