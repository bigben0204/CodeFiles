cmake_minimum_required(VERSION 3.16)
project(TestProject)

set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_EXTENSIONS OFF)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_subdirectory(deps)

add_executable(TestProject main.cpp)
target_link_libraries(TestProject
        PRIVATE
        say_hello
        )
# add_dependencies(TestProject say_hello_target) 如果不增加依赖，则say_hello_target不会执行

