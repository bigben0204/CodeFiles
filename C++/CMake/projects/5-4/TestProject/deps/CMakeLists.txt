set(say_hello_sources
        ${CMAKE_CURRENT_BINARY_DIR}/say_hello/foo.h
        ${CMAKE_CURRENT_BINARY_DIR}/say_hello/foo.cpp
        )

add_custom_target(say_hello_target
        WORKING_DIRECTORY
        ${CMAKE_CURRENT_BINARY_DIR}
        DEPENDS
        ${say_hello_sources}
        COMMENT
        "Intermediate say_hello target"
        VERBATIM
        )

add_custom_command(
        OUTPUT
        ${say_hello_sources}
        COMMAND
        ${CMAKE_COMMAND} -E tar xzf ${CMAKE_CURRENT_SOURCE_DIR}/say_hello.tar.gz
        WORKING_DIRECTORY
        ${CMAKE_CURRENT_BINARY_DIR}
        DEPENDS
        ${CMAKE_CURRENT_SOURCE_DIR}/say_hello.tar.gz
        COMMENT
        "Unpacking C++ say_hello headers and sources"
)

add_library(say_hello "")
target_sources(say_hello
        PRIVATE
        ${say_hello_sources}
        )
target_include_directories(say_hello
        INTERFACE
        ${CMAKE_CURRENT_BINARY_DIR}/say_hello
        )

