#ifndef AOL_OBJECT_H
#define AOL_OBJECT_H

#include "art_define.h"

class MANAGED Object {
public:
    uint32_t klass_;
    uint32_t monitor_;
};

#endif //AOL_OBJECT_H
