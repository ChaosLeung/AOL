#ifndef AOL_JAVA_OBJECTS_H_
#define AOL_JAVA_OBJECTS_H_

#include "art_define.h"
#include "vector"
#include "string"

namespace aol {

using std::vector;
using std::string;
using std::shared_ptr;

class JField {
public:
    JField(string name, string type, string declaringClass, const size_t offset, const size_t size) :
        name(name), type(type), declaringClass(declaringClass), offset(offset), size(size) {
    }

    JField(const char *name, const char *type, string declaringClass, const size_t offset, const size_t size) :
        name(name), type(type), declaringClass(declaringClass), offset(offset), size(size) {
    }

    string name;
    string type;
    string declaringClass;
    size_t offset;
    size_t size;
};

class JClass {
public:
    static shared_ptr<JClass> Create(JNIEnv *env, jclass clazz);

private:
    JClass(jclass clazz = nullptr,
           shared_ptr<JClass> super = nullptr,
           vector<JField> sfields = vector<JField>(),
           vector<JField> ifields = vector<JField>())
        : clazz_(clazz),
          super_(super),
          sfields_(sfields),
          ifields_(ifields) {
    }

public:
    shared_ptr<JClass> GetSuper() {
        return super_;
    }

    const vector<JField> &GetSFields() {
        return sfields_;
    }

    const vector<JField> &GetIFields() {
        return ifields_;
    }

private:
    jclass clazz_;
    shared_ptr<JClass> super_;
    vector<JField> sfields_;
    vector<JField> ifields_;
};

}
#endif //AOL_JAVA_OBJECTS_H_
