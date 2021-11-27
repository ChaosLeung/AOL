#ifndef AOL_BUILD_H
#define AOL_BUILD_H

#include "art_define.h"
#include "string"

extern int gSdkVersion;
extern int gPreviewSdkVersion;

static int GetIntProp(const char *prop) {
    FILE *fp = popen(std::string("getprop ").append(prop).c_str(), "r");
    if (!fp) return -1;
    char chs[16];
    fgets(chs, sizeof(chs), fp);
    pclose(fp);
    return atoi(chs);
}

#define IS_API_DEFINE(sdk) \
inline bool isApi##sdk() { \
    return gSdkVersion == sdk || (gSdkVersion == sdk - 1 && gPreviewSdkVersion > 0); \
}

#define IS_GE_API_DEFINE(sdk) \
inline bool isApiGe##sdk() { \
    return gSdkVersion > sdk - 1 || (gSdkVersion == sdk - 1 && gPreviewSdkVersion > 0); \
}

#define API_DEFINE(sdk) \
IS_API_DEFINE(sdk) \
IS_GE_API_DEFINE(sdk)

API_DEFINE(21)

API_DEFINE(22)

API_DEFINE(23)

API_DEFINE(24)

API_DEFINE(25)

API_DEFINE(26)

API_DEFINE(27)

API_DEFINE(28)

API_DEFINE(29)

API_DEFINE(30)

API_DEFINE(31)

#endif //AOL_BUILD_H
