#ifndef AOL_ART_DEFINE_H
#define AOL_ART_DEFINE_H

#include "jni.h"

#define PACKED(x) __attribute__ ((__aligned__(x), __packed__))
#define MANAGED PACKED(4)

#define ARRAY_SIZE(arr)     (sizeof(arr) / sizeof((arr)[0]))

#endif //AOL_ART_DEFINE_H
