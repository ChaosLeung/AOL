cmake_minimum_required(VERSION 3.4.1)

add_library(aol-jvmti-agent SHARED jvmti_agent.cpp)

find_library(log-lib log)

target_link_libraries(aol-jvmti-agent ${log-lib})