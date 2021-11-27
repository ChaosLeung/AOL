#ifndef AOL_LENGTH_PREFIXED_ARRAY_H
#define AOL_LENGTH_PREFIXED_ARRAY_H

#include "art_define.h"
#include "stride_iterator.h"
#include "bit_utils.h"

/**
 * Copy from Android 24 art/runtime/base/length_prefixed_array.h
 */
template<typename T>
class LengthPrefixedArray {
public:
    explicit LengthPrefixedArray(size_t length)
        : size_(length) {}

    T& At(size_t index, size_t element_size = sizeof(T), size_t alignment = alignof(T)) {
        return AtUnchecked(index, element_size, alignment);
    }

    const T& At(size_t index, size_t element_size = sizeof(T), size_t alignment = alignof(T)) const {
        return AtUnchecked(index, element_size, alignment);
    }

    StrideIterator<T> begin(size_t element_size = sizeof(T), size_t alignment = alignof(T)) {
        return StrideIterator<T>(&AtUnchecked(0, element_size, alignment), element_size);
    }

    StrideIterator<const T> begin(size_t element_size = sizeof(T),
                                  size_t alignment = alignof(T)) const {
        return StrideIterator<const T>(&AtUnchecked(0, element_size, alignment), element_size);
    }

    StrideIterator<T> end(size_t element_size = sizeof(T), size_t alignment = alignof(T)) {
        return StrideIterator<T>(&AtUnchecked(size_, element_size, alignment), element_size);
    }

    StrideIterator<const T> end(size_t element_size = sizeof(T),
                                size_t alignment = alignof(T)) const {
        return StrideIterator<const T>(&AtUnchecked(size_, element_size, alignment), element_size);
    }

    static size_t OffsetOfElement(size_t index,
                                  size_t element_size = sizeof(T),
                                  size_t alignment = alignof(T)) {
        return RoundUp(offsetof(LengthPrefixedArray<T>, data), alignment) + index * element_size;
    }

    static size_t ComputeSize(size_t num_elements,
                              size_t element_size = sizeof(T),
                              size_t alignment = alignof(T)) {
        size_t result = OffsetOfElement(num_elements, element_size, alignment);
        return result;
    }

    size_t size() const {
        return size_;
    }

    // Update the length but does not reallocate storage.
    void SetSize(size_t length) {
        size_ = length;
    }

    // Clear the potentially uninitialized padding between the size_ and actual data.
    void ClearPadding(size_t element_size = sizeof(T), size_t alignment = alignof(T)) {
        size_t gap_offset = offsetof(LengthPrefixedArray<T>, data);
        size_t gap_size = OffsetOfElement(0, element_size, alignment) - gap_offset;
        memset(reinterpret_cast<uint8_t*>(this) + gap_offset, 0, gap_size);
    }

private:
    T& AtUnchecked(size_t index, size_t element_size, size_t alignment) {
        return *reinterpret_cast<T*>(
            reinterpret_cast<uintptr_t>(this) + OffsetOfElement(index, element_size, alignment));
    }

    const T& AtUnchecked(size_t index, size_t element_size, size_t alignment) const {
        return *reinterpret_cast<T*>(
            reinterpret_cast<uintptr_t>(this) + OffsetOfElement(index, element_size, alignment));
    }

    uint32_t size_;
    uint8_t data[0];
};
#endif //AOL_LENGTH_PREFIXED_ARRAY_H
