#ifndef AOL_BIT_UTILS_H
#define AOL_BIT_UTILS_H

#include "stl_util_identity.h"
#include <type_traits>
#include "stdio.h"

template<typename T>
constexpr T RoundDown(T x, typename Identity<T>::type n) {
    return (x & -n);
}

template<typename T>
constexpr T RoundUp(T x, typename std::remove_reference<T>::type n) {
    return RoundDown(x + n - 1, n);
}

template<typename T>
constexpr int CLZ(T x) {
    static_assert(std::is_integral<T>::value, "T must be integral");
    static_assert(std::is_unsigned<T>::value, "T must be unsigned");
    static_assert(std::numeric_limits<T>::radix == 2, "Unexpected radix!");
    static_assert(sizeof(T) == sizeof(uint64_t) || sizeof(T) <= sizeof(uint32_t),
                  "Unsupported sizeof(T)");
    constexpr bool is_64_bit = (sizeof(T) == sizeof(uint64_t));
    constexpr size_t adjustment =
            is_64_bit ? 0u : std::numeric_limits<uint32_t>::digits - std::numeric_limits<T>::digits;
    return is_64_bit ? __builtin_clzll(x) : __builtin_clz(x) - adjustment;
}

template<typename T>
constexpr ssize_t MostSignificantBit(T value) {
    static_assert(std::is_integral<T>::value, "T must be integral");
    static_assert(std::is_unsigned<T>::value, "T must be unsigned");
    static_assert(std::numeric_limits<T>::radix == 2, "Unexpected radix!");
    return (value == 0) ? -1 : std::numeric_limits<T>::digits - 1 - CLZ(value);
}

template<typename T>
constexpr size_t MinimumBitsToStore(T value) {
    return static_cast<size_t>(MostSignificantBit(value) + 1);
}

#endif //AOL_BIT_UTILS_H
