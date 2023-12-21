add_subdirectory(googletest-1.14.0 EXCLUDE_FROM_ALL)

set(test_name test_${PROJECT_NAME})

aux_source_directory(. test_files)
#list(REMOVE_ITEM test_files "./test_my_add.cpp")
#set(test_files test_main.cpp test_my_add.cpp)

add_executable(${test_name} ${test_files})
target_link_libraries(${test_name} ${src_lib} gtest_main gmock_main)

add_test(NAME ${test_name} COMMAND ${test_name})
