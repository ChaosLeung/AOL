#ifndef AOL_DEX_CACHE_H
#define AOL_DEX_CACHE_H

#include "art_define.h"
#include "object.h"
#include "dex_file.h"

class MANAGED DexCache23 final : public Object {
public:
    uint32_t reserve0;
    uint32_t reserve1;
    uint32_t reserve2;
    uint32_t reserve3;
    uint32_t reserve4;
    uint32_t reserve5;

    uint64_t dex_file_;
};

class MANAGED DexCache24 final : public Object {
public:
    uint32_t reserve0;
    uint32_t reserve1;

    uint64_t dex_file_;
};

class DexCache {
public:
    const DexFile* GetDexFile() const;
};
#endif //AOL_DEX_CACHE_H
