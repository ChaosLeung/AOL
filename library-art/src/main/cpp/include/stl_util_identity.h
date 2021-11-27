#ifndef AOL_STL_UTIL_IDENTITY_H
#define AOL_STL_UTIL_IDENTITY_H

// Use to suppress type deduction for a function argument.
// See std::identity<> for more background:
// http://www.open-std.org/jtc1/sc22/wg21/docs/papers/2005/n1856.html#20.2.2 - move/forward helpers
//
// e.g. "template <typename X> void bar(identity<X>::type foo);
//     bar(5); // compilation error
//     bar<int>(5); // ok
// or "template <typename T> void foo(T* x, typename Identity<T*>::type y);
//     Base b;
//     Derived d;
//     foo(&b, &d);  // Use implicit Derived* -> Base* conversion.
// If T was deduced from both &b and &d, there would be a mismatch, i.e. deduction failure.
template <typename T>
struct Identity {
    using type = T;
};

#endif //AOL_STL_UTIL_IDENTITY_H
