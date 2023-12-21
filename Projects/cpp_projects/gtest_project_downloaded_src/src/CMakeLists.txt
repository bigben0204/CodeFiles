aux_source_directory(. src_files)
list(REMOVE_ITEM src_files "./main.cpp")

set(src_lib src_lib CACHE STRING INTERNAL)
add_library(${src_lib} OBJECT ${src_files})
target_include_directories(${src_lib} PUBLIC ${PROJECT_SOURCE_DIR}/header)

set(src_exe_name main_${PROJECT_NAME})
add_executable(${src_exe_name} main.cpp)
target_link_libraries(${src_exe_name} ${src_lib} pthread)
