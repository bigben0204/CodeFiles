cmake_minimum_required(VERSION 3.17)
project(TestProject)

set(CMAKE_CXX_STANDARD 14)

set(say_hello_sources
        ${CMAKE_CURRENT_BINARY_DIR}/say_hello/foo.h
        ${CMAKE_CURRENT_BINARY_DIR}/say_hello/foo.cpp
        )

add_custom_command(
        OUTPUT
        ${say_hello_sources}
        COMMAND
        ${CMAKE_COMMAND} -E tar xzf ${CMAKE_CURRENT_SOURCE_DIR}/say_hello.tar.gz
        COMMAND
        ${CMAKE_COMMAND} -E touch ${say_hello_sources}
        WORKING_DIRECTORY
        ${CMAKE_CURRENT_BINARY_DIR}
        DEPENDS
        ${CMAKE_CURRENT_SOURCE_DIR}/say_hello.tar.gz
        COMMENT
        "Unpacking C++ say_hello headers and sources"
        VERBATIM
)

add_library(say_hello "")
# 这里的PUBLIC指定的h文件，可以不用指定；但cpp文件必须指定
target_sources(say_hello
        PRIVATE
        ${CMAKE_CURRENT_BINARY_DIR}/say_hello/foo.cpp
        PUBLIC
        ${CMAKE_CURRENT_BINARY_DIR}/say_hello/foo.h
        )
# 这里的包含目录必须指定为INTERFACE或PUBLIC，一是给say_hello库使用，二是给依赖say_hello的库使用
target_include_directories(say_hello
        INTERFACE
        ${CMAKE_CURRENT_BINARY_DIR}/say_hello
        )

add_executable(TestProject main.cpp)
target_link_libraries(TestProject
        PRIVATE
        say_hello
        )

