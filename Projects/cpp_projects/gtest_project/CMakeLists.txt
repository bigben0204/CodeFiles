cmake_minimum_required(VERSION 3.16)
project(gtest_project)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_EXPORT_COMPILE_COMMANDS on)
add_compile_options(-Wall -Wextra -Wpedantic)

enable_testing()
add_subdirectory(src)
add_subdirectory(test)
