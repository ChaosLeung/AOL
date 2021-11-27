#ifndef AOL_ART_UTILS_H_
#define AOL_ART_UTILS_H_

#include "jni.h"
#include "string"

inline size_t GetTypeSize(const char *type) {
    switch (type[0]) {
        case 'Z':
        case 'B':
        case 'C':return 1;
        case 'S':return 2;
        case 'I':
        case 'F':return 4;
        case 'J':
        case 'D':return 8;
        case 'L':
        case '[':return 4;
        default:return 0;
    }
}

inline std::string GetTypeDescriptor(std::string &class_name) {
    int idx = class_name.rfind("[]");
    bool is_array = idx > 0;
    if (is_array) {
        std::string element_clazz = class_name.substr(0, idx);
        return '[' + GetTypeDescriptor(element_clazz);
    } else if (class_name == "boolean") {
        return "Z";
    } else if (class_name == "byte") {
        return "B";
    } else if (class_name == "short") {
        return "S";
    } else if (class_name == "int") {
        return "I";
    } else if (class_name == "long") {
        return "J";
    } else if (class_name == "float") {
        return "F";
    } else if (class_name == "double") {
        return "D";
    } else if (class_name == "char") {
        return "C";
    } else if (class_name == "void") {
        return "V";
    } else {
        std::string copy;
        std::replace_copy(class_name.begin(), class_name.end(), copy.begin(), '.', '/');
        return 'L' + copy + ';';
    }
}

inline std::string GetClassName(std::string &type_desc) {
    if (type_desc.find("[") == 0) {
        std::string type = type_desc.substr(1, type_desc.length() - 1);
        return GetClassName(type) + "[]";
    } else if (type_desc == "Z") {
        return "boolean";
    } else if (type_desc == "B") {
        return "byte";
    } else if (type_desc == "S") {
        return "short";
    } else if (type_desc == "I") {
        return "int";
    } else if (type_desc == "J") {
        return "long";
    } else if (type_desc == "F") {
        return "float";
    } else if (type_desc == "D") {
        return "double";
    } else if (type_desc == "C") {
        return "char";
    } else if (type_desc == "V") {
        return "void";
    } else {
        std::string type = type_desc.substr(1, type_desc.length() - 2);
        std::replace(type.begin(), type.end(), '/', '.');
        return type;
    }
}

#endif //AOL_ART_UTILS_H_
