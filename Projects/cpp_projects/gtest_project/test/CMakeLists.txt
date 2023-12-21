option(ENABLE_UNIT_TESTS "Enable unit tests" ON)
message(STATUS "Enable testing: ${ENABLE_UNIT_TESTS}")

if (ENABLE_UNIT_TESTS)
    # the following code to fetch googletest
    # is inspired by and adapted after:
    #   - https://cmake.org/cmake/help/v3.11/module/FetchContent.html
    include(FetchContent)

    FetchContent_Declare(
            googletest
            GIT_REPOSITORY https://gitee.com/antmoc/googletest.git
            GIT_TAG v1.14.0
    )

    FetchContent_GetProperties(googletest)

    if (NOT googletest_POPULATED)
        FetchContent_Populate(googletest)

        # Prevent GoogleTest from overriding our compiler/linker options
        # when building with Visual Studio
        set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)
        # Prevent GoogleTest from using PThreads
        set(gtest_disable_pthreads ON CACHE BOOL "" FORCE)

        # adds the targers: gtest, gtest_main, gmock, gmock_main
        add_subdirectory(${googletest_SOURCE_DIR} ${googletest_BINARY_DIR} EXCLUDE_FROM_ALL)
    endif ()

    aux_source_directory(. test_files)
    #list(REMOVE_ITEM test_files "./test_my_add.cpp")
    #set(test_files test_main.cpp test_my_add.cpp)
    set(test_name test_${PROJECT_NAME})
    add_executable(${test_name} "")
    target_sources(${test_name} PRIVATE ${test_files})
    target_link_libraries(${test_name} PRIVATE ${src_lib} gtest_main gmock_main)

    enable_testing()

    add_test(NAME ${test_name} COMMAND $<TARGET_FILE:${test_name}>)
endif ()

