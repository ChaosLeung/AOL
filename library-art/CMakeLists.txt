cmake_minimum_required(VERSION 3.10.2)

project("AOL")

include_directories(src/main/cpp/include)

file(GLOB SOURCE_FILES CONFIGURE_DEPENDS "src/main/cpp/*.cpp")
set(PROTECTION_SOURCES ${SOURCE_FILES})

add_library(aol-art
        SHARED
        ${PROTECTION_SOURCES}
        src/main/cpp/well_known_classes.cpp)

find_library(android-log log)

target_link_libraries(aol-art ${android-log})