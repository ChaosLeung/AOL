#include "dex_cache.h"
#include "build.h"

#define DEXCACHE_MEMBER(t, f) \
    isApiGe24() ? reinterpret_cast<const DexCache24 *>(t)->f : reinterpret_cast<const DexCache23 *>(t)->f

const DexFile *DexCache::GetDexFile() const {
    uint64_t dex_file = DEXCACHE_MEMBER(this, dex_file_);
    return reinterpret_cast<DexFile *>(dex_file);
}