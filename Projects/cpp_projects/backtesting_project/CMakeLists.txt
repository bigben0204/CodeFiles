cmake_minimum_required(VERSION 3.16)
project(backtesting_project)

set(CMAKE_CXX_STANDARD 17)

enable_testing()
add_subdirectory(src)
add_subdirectory(test)
