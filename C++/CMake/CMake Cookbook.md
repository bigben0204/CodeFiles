# [CMake Cookbook](https://www.bookstack.cn/read/CMake-Cookbook/content-preface-preface-chinese.md)

源码下载<https://github.com/dev-cafe/cmake-cookbook>

# 第1章 从可执行文件到库

## [1.1 将单个源文件编译为可执行文件](https://www.bookstack.cn/read/CMake-Cookbook/content-chapter1-1.1-chinese.md)

```cpp
cmake_minimum_required(VERSION 3.14 FATAL_ERROR)

project(recipe-01 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 20)

aux_source_directory(. DIR_SRCS)

add_executable(hello-world ${DIR_SRCS})  # 生成可执行文件名称与工程名字可任意指定，没有相关联关系
```

在`build`目录：

```cpp
$ cmake ..
$ cmake --build .  # 与make一样效果。--build <dir> = Build a CMake-generated project binary tree. 可通过cmake -help查看帮助
$ cmake --build . --target clean  # 与make clean效果一样
```

**NOTE**:*CMake语言不区分大小写，但是参数区分大小写。*

**TIPS**:*CMake中，C++是默认的编程语言。不过，我们还是建议使用`LANGUAGES`选项在`project`命令中显式地声明项目的语言。*

要配置项目并生成构建器，我们必须通过命令行界面(CLI)运行CMake。CMake CLI提供了许多选项，`cmake -help`将输出以显示列出所有可用选项的完整帮助信息，我们将在书中对这些选项进行更多地了解。正如您将从`cmake -help`的输出中显示的内容，它们中的大多数选项会让你您访问CMake手册，查看详细信息。通过下列命令生成构建器：

```
$ mkdir -p build
$ cd build$ cmake ..
```

这里，我们创建了一个目录`build`(生成构建器的位置)，进入`build`目录，并通过指定`CMakeLists.txt`的位置(本例中位于父目录中)来调用CMake。可以使用以下命令行来实现相同的效果：

```
$ cmake -H. -Bbuild  # 如果在build目录，则命令为cmake -H.. -B.
```

该命令是跨平台的，使用了`-H`和`-B`为CLI选项。`-H`表示当前目录中搜索根`CMakeLists.txt`文件。`-Bbuild`告诉CMake在一个名为`build`的目录中生成所有的文件。

**NOTE**:*`cmake -H. -Bbuild`也属于CMake标准使用方式: https://cmake.org/pipermail/cmake-developers/2018-January/030520.html 。不过，我们将在本书中使用传统方法(创建一个构建目录，进入其中，并通过将CMake指向`CMakeLists.txt`的位置来配置项目)。*

运行`cmake`命令会输出一系列状态消息，显示配置信息：

```
$ cmake ..
-- The CXX compiler identification is GNU 10.2.0
-- Check for working CXX compiler: /usr/bin/c++.exe
-- Check for working CXX compiler: /usr/bin/c++.exe - works
-- Detecting CXX compiler ABI info
-- Detecting CXX compiler ABI info - done
-- Detecting CXX compile features
-- Detecting CXX compile features - done
-- Configuring done
-- Generating done
-- Build files have been written to: /cygdrive/d/Program Files/JetBrains/CppProject/TestProject/build
```

**NOTE**:*在与`CMakeLists.txt`相同的目录中执行`cmake .`，原则上足以配置一个项目。然而，CMake会将所有生成的文件写到项目的根目录中。这将是一个源代码内构建，通常是不推荐的，因为这会混合源代码和项目的目录树。我们首选的是源外构建。*

CMake是一个构建系统生成器。将描述构建系统(如：Unix Makefile、Ninja、Visual Studio等)应当如何操作才能编译代码。然后，CMake为所选的构建系统生成相应的指令。默认情况下，在GNU/Linux和macOS系统上，CMake使用Unix Makefile生成器。Windows上，Visual Studio是默认的生成器。在下一个示例中，我们将进一步研究生成器，并在第13章中重新讨论生成器。

GNU/Linux上，CMake默认生成Unix Makefile来构建项目：

- `Makefile`: `make`将运行指令来构建项目。
- `CMakefile`：包含临时文件的目录，CMake用于检测操作系统、编译器等。此外，根据所选的生成器，它还包含特定的文件。
- `cmake_install.cmake`：处理安装规则的CMake脚本，在项目安装时使用。
- `CMakeCache.txt`：如文件名所示，CMake缓存。CMake在重新运行配置时使用这个文件。

要构建示例项目，我们运行以下命令：

```
$ cmake --build .
```

最后，CMake不强制指定构建目录执行名称或位置，我们完全可以把它放在项目路径之外。这样做同样有效：

```
$ mkdir -p /tmp/someplace
$ cd /tmp/someplace
$ cmake /path/to/source
$ cmake --build .
```

官方文档 https://cmake.org/runningcmake/ 给出了运行CMake的简要概述。由CMake生成的构建系统，即上面给出的示例中的Makefile，将包含为给定项目构建目标文件、可执行文件和库的目标及规则。`hello-world`可执行文件是在当前示例中的唯一目标，运行以下命令：

```
$ cmake --build . --target help  # 和make help效果一样
The following are some of the valid targets for this Makefile:
... all (the default if no target is provided)
... clean
... depend
... edit_cache
... rebuild_cache
... hello-world
... main.o
... main.i
... main.s
```

CMake生成的目标比构建可执行文件的目标要多。可以使用`cmake --build . --target <target-name>`语法，实现如下功能：

- **all**(或Visual Studio generator中的ALL_BUILD)是默认目标，将在项目中构建所有目标。
- **clean**，删除所有生成的文件。
- **rebuild_cache**，将调用CMake为源文件生成依赖(如果有的话)。
- **edit_cache**，这个目标允许直接编辑缓存。

对于更复杂的项目，通过测试阶段和安装规则，CMake将生成额外的目标：

- **test**(或Visual Studio generator中的**RUN_TESTS**)将在CTest的帮助下运行测试套件。我们将在第4章中详细讨论测试和CTest。
- **install**，将执行项目安装规则。我们将在第10章中讨论安装规则。
- **package**，此目标将调用CPack为项目生成可分发的包。打包和CPack将在第11章中讨论。

## [1.2 切换生成器](https://www.bookstack.cn/read/CMake-Cookbook/content-chapter1-1.2-chinese.md)

CMake针对不同平台支持本地构建工具列表。同时支持命令行工具(如Unix Makefile和Ninja)和集成开发环境(IDE)工具。用以下命令，可在平台上找到生成器名单，以及已安装的CMake版本：

```
$ cmake --help
```

这个命令的输出，将列出CMake命令行界面上所有的选项，您会找到可用生成器的列表。例如，安装了CMake 3.11.2的GNU/Linux机器上的输出：

```
Generators

The following generators are available on this platform (* marks default):
* Unix Makefiles               = Generates standard UNIX makefiles.
  Ninja                        = Generates build.ninja files.
  Ninja Multi-Config           = Generates build-<Config>.ninja files.
  CodeBlocks - Ninja           = Generates CodeBlocks project files.
  CodeBlocks - Unix Makefiles  = Generates CodeBlocks project files.
  CodeLite - Ninja             = Generates CodeLite project files.
  CodeLite - Unix Makefiles    = Generates CodeLite project files.
  Sublime Text 2 - Ninja       = Generates Sublime Text 2 project files.
  Sublime Text 2 - Unix Makefiles
                               = Generates Sublime Text 2 project files.
  Kate - Ninja                 = Generates Kate project files.
  Kate - Unix Makefiles        = Generates Kate project files.
  Eclipse CDT4 - Ninja         = Generates Eclipse CDT 4.0 project files.
  Eclipse CDT4 - Unix Makefiles= Generates Eclipse CDT 4.0 project files.
```

**具体实施**

我们将重用前一节示例中的`hello-world.cpp`和`CMakeLists.txt`。惟一的区别在使用CMake时，因为现在必须显式地使用命令行方式，用`-G`切换生成器。

1. 首先，使用以下步骤配置项目:

   ```
   ben@ben-virtual-machine:~/Softwares/JetBrains/CppProjects/TestProject/build$ cmake -G Ninja ..
   -- The C compiler identification is GNU 7.5.0
   -- The CXX compiler identification is GNU 7.5.0
   -- Check for working C compiler: /usr/bin/cc
   -- Check for working C compiler: /usr/bin/cc - works
   -- Detecting C compiler ABI info
   -- Detecting C compiler ABI info - done
   -- Detecting C compile features
   -- Detecting C compile features - done
   -- Check for working CXX compiler: /usr/bin/c++
   -- Check for working CXX compiler: /usr/bin/c++ - works
   -- Detecting CXX compiler ABI info
   -- Detecting CXX compiler ABI info - done
   -- Detecting CXX compile features
   -- Detecting CXX compile features - done
   -- Configuring done
   -- Generating done
   -- Build files have been written to: /home/ben/Softwares/JetBrains/CppProjects/TestProject/build
   ```

2. 第二步，构建项目：

   ```
   $ cmake --build .  # 这里也可以使用命令ninja来进行构建
   [2/2] Linking CXX executable hello-world
   ```

**如何工作**

与前一个配置相比，每一步的输出没什么变化。每个生成器都有自己的文件集，所以编译步骤的输出和构建目录的内容是不同的：

- `build.ninja`和`rules.ninja`：包含Ninja的所有的构建语句和构建规则。
- `CMakeCache.txt`：CMake会在这个文件中进行缓存，与生成器无关。
- `CMakeFiles`：包含由CMake在配置期间生成的临时文件。
- `cmake_install.cmake`：CMake脚本处理安装规则，并在安装时使用。

`cmake --build .`将`ninja`命令封装在一个跨平台的接口中。

## [1.3 构建和链接静态库和动态库](https://www.bookstack.cn/read/CMake-Cookbook/content-chapter1-1.3-chinese.md)

**准备工作**

回看第一个例子，这里并不再为可执行文件提供单个源文件，我们现在将引入一个类，用来包装要打印到屏幕上的消息。更新一下的`hello-world.cpp`:

```c++
#include "Message.h"

#include <cstdlib>
#include <iostream>

int main() {
    Message say_hello("Hello, CMake World!");
    std::cout << say_hello << std::endl;
    Message say_goodbye("Goodbye, CMake World");
    std::cout << say_goodbye << std::endl;
    return EXIT_SUCCESS;
}
```

`Message`类包装了一个字符串，并提供重载过的`<<`操作，并且包括两个源码文件：`Message.hpp`头文件与`Message.cpp`源文件。`Message.hpp`中的接口包含以下内容：

```c++
#ifndef RECIPE_01_MESSAGE_H
#define RECIPE_01_MESSAGE_H
#pragma once

#include <iosfwd>
#include <string>

class Message {
public:
    Message(const std::string& m) : message_(m) {}

    friend std::ostream& operator<<(std::ostream& os, Message& obj) {
        return obj.printObject(os);
    }

private:
    std::string message_;

    std::ostream& printObject(std::ostream& os);
};

#endif //RECIPE_01_MESSAGE_H

```

`Message.cpp`实现如下：

```c++
#include "Message.h"
#include <iostream>
#include <string>

std::ostream& Message::printObject(std::ostream& os) {
    os << "This is my very nice message: " << std::endl;
    os << message_;
    return os;
}
```

**具体实施**

这里有两个文件需要编译，所以`CMakeLists.txt`必须进行修改。本例中，先把它们编译成一个库，而不是直接编译成可执行文件:

1. 创建目标——静态库。库的名称和源码文件名相同，具体代码如下：

   ```
   add_library(message STATIC Message.h Message.cpp)
   ```

2. 创建`hello-world`可执行文件的目标部分不需要修改：

   ```
   add_executable(hello-world hello-world.cpp)
   ```

3. 最后，将目标库链接到可执行目标：

   ```
   target_link_libraries(hello-world message)
   ```

   ```
   cmake_minimum_required(VERSION 3.14 FATAL_ERROR)
   
   project(recipe-03 LANGUAGES CXX)
   
   set(CMAKE_CXX_STANDARD 20)
   
   add_library(message STATIC Message.h Message.cpp)
   
   add_executable(hello-world hello-world.cpp)
   
   target_link_libraries(hello-world message)
   ```

4. 对项目进行配置和构建。库编译完成后，将连接到`hello-world`可执行文件中：

   ```
   $ mkdir -p build
   $ cd build
   $ cmake ..
   $ cmake --build .
   ```

   ```
   Scanning dependencies of target message
   [ 25%] Building CXX object CMakeFiles/message.dir/Message.cpp.o
   [ 50%] Linking CXX static library libmessage.a
   [ 50%] Built target message
   Scanning dependencies of target hello-world
   [ 75%] Building CXX object CMakeFiles/hello-world.dir/hello-world.cpp.o
   [100%] Linking CXX executable hello-world.exe
   [100%] Built target hello-world
   
   # 在build目录可以看到内容有libmessage.a静态库：
   $ ls -l
   总用量 224
   -rwxrw-r--+ 1 Ben Ben   1429 11月 27 21:54 cmake_install.cmake
   -rwxrw-r--+ 1 Ben Ben  13549 11月 27 21:54 CMakeCache.txt
   drwxrwxr-x+ 1 Ben Ben      0 11月 27 21:54 CMakeFiles
   -rwxrwxr-x+ 1 Ben Ben 189656 11月 27 21:54 hello-world.exe
   -rwxrw-r--+ 1 Ben Ben   2948 11月 27 21:54 libmessage.a
   -rwxrw-r--+ 1 Ben Ben   6677 11月 27 21:54 Makefile
   ```

   ```c++
   $ ./hello-world.exe
   This is my very nice message:
   Hello, CMake World!
   This is my very nice message:
   Goodbye, CMake World
   ```

**工作原理**

本节引入了两个新命令：

- `add_library(message STATIC Message.hpp Message.cpp)`：生成必要的构建指令，将指定的源码编译到库中。`add_library`的第一个参数是目标名。整个`CMakeLists.txt`中，可使用相同的名称来引用库。生成的库的实际名称将由CMake通过在前面添加前缀`lib`和适当的扩展名作为后缀来形成。生成库是根据第二个参数(`STATIC`或`SHARED`)和操作系统确定的。
- `target_link_libraries(hello-world message)`: 将库链接到可执行文件。此命令还确保`hello-world`可执行文件可以正确地依赖于消息库。因此，在消息库链接到`hello-world`可执行文件之前，需要完成消息库的构建。

编译成功后，构建目录包含`libmessage.a`一个静态库(在GNU/Linux上)和`hello-world`可执行文件。

- **STATIC**：用于创建静态库，即编译文件的打包存档，以便在链接其他目标时使用，例如：可执行文件。
- **SHARED**：用于创建动态库，即可以动态链接，并在运行时加载的库。可以在`CMakeLists.txt`中使用`add_library(message SHARED Message.hpp Message.cpp)`从静态库切换到动态共享对象(DSO)。
- **OBJECT**：可将给定`add_library`的列表中的源码编译到目标文件，不将它们归档到静态库中，也不能将它们链接到共享对象中。如果需要一次性创建静态库和动态库，那么使用对象库尤其有用。我们将在本示例中演示。
- **MODULE**：又为DSO组。与`SHARED`库不同，它们不链接到项目中的任何目标，不过可以进行动态加载。该参数可以用于构建运行时插件。

--------------

本地验证：

如果修改为`add_library(message SHARED Message.hpp Message.cpp)`，则生成动态库`cygmessage.dll`和`libmessage.dll.a`，如果删除该`cygmessage.dll`，则运行`./hello_world.exe`则报错：

```
$ ./hello-world.exe
D:/Program Files/JetBrains/CppProject/TestProject/build/hello-world.exe: error while loading shared libraries: cygmessag
e.dll: cannot open shared object file: No such file or directory
```

如果修改为`add_library(message OBJECT Message.h Message.cpp)`，则不会生成`.dll`和`.a`文件，只有一个`hello_world.exe`

------

CMake还能够生成特殊类型的库，这不会在构建系统中产生输出，但是对于组织目标之间的依赖关系，和构建需求非常有用：

- **IMPORTED**：此类库目标表示位于项目外部的库。此类库的主要用途是，对现有依赖项进行构建。因此，`IMPORTED`库将被视为不可变的。我们将在本书的其他章节演示使用`IMPORTED`库的示例。参见: https://cmake.org/cmake/help/latest/manual/cmakebuildsystem.7.html#imported-targets
- **INTERFACE**：与`IMPORTED`库类似。不过，该类型库可变，没有位置信息。它主要用于项目之外的目标构建使用。我们将在本章第5节中演示`INTERFACE`库的示例。参见: https://cmake.org/cmake/help/latest/manual/cmake-buildsystem.7.html#interface-libraries
- **ALIAS**：顾名思义，这种库为项目中已存在的库目标定义别名。不过，不能为`IMPORTED`库选择别名。参见: https://cmake.org/cmake/help/latest/manual/cmake-buildsystem.7.html#alias-libraries

本例中，我们使用`add_library`直接集合了源代码。后面的章节中，我们将使用`target_sources`汇集源码，特别是在第7章。请参见Craig Scott的这篇精彩博文: https://crascit.com/2016/01/31/enhanced-source-file-handling-with-target_sources/ ，其中有对`target_sources`命令的具体使用。

**更多信息**

现在展示`OBJECT`库的使用，修改`CMakeLists.txt`，如下：

```
cmake_minimum_required(VERSION 3.14 FATAL_ERROR)

project(recipe-03 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 20)

add_library(message-objs
        OBJECT
        Message.h
        Message.cpp
        )
# this is only needed for older compilers
# but doesn't hurt either to have it
set_target_properties(message-objs
        PROPERTIES
        POSITION_INDEPENDENT_CODE 1
        )
add_library(message-shared
        SHARED
        $<TARGET_OBJECTS:message-objs>
        )
add_library(message-static
        STATIC
        $<TARGET_OBJECTS:message-objs>
        )
add_executable(hello-world hello-world.cpp)
target_link_libraries(hello-world message-static)

编译时可以看到扫描了四个对象：message-objs/message-static/hello-world/message-shared：
$ cmake --build .
Scanning dependencies of target message-objs
[ 20%] Building CXX object CMakeFiles/message-objs.dir/Message.cpp.o
[ 20%] Built target message-objs
Scanning dependencies of target message-static
[ 40%] Linking CXX static library libmessage-static.a
[ 40%] Built target message-static
Scanning dependencies of target hello-world
[ 60%] Building CXX object CMakeFiles/hello-world.dir/hello-world.cpp.o
[ 80%] Linking CXX executable hello-world.exe
[ 80%] Built target hello-world
Scanning dependencies of target message-shared
[100%] Linking CXX shared library cygmessage-shared.dll
[100%] Built target message-shared
```

首先，`add_library`改为`add_library(Message-objs OBJECT Message.hpp Message.cpp)`。此外，需要保证编译的目标文件与生成位置无关。可以通过使用`set_target_properties`命令，设置`message-objs`目标的相应属性来实现。

**NOTE**: *可能在某些平台和/或使用较老的编译器上，需要显式地为目标设置`POSITION_INDEPENDENT_CODE`属性。*

现在，可以使用这个对象库来获取静态库(`message-static`)和动态库(`message-shared`)。要注意引用对象库的生成器表达式语法:`$<TARGET_OBJECTS:message-objs>`。生成器表达式是CMake在生成时(即配置之后)构造，用于生成特定于配置的构建输出。参见: https://cmake.org/cmake/help/latest/manual/cmake-generator-expressions.7.html 。我们将在第5章中深入研究生成器表达式。最后，将`hello-world`可执行文件链接到消息库的静态版本。

-------------------------------

本地试验：

修改CMakeLists.txt如下：

```
cmake_minimum_required(VERSION 3.14 FATAL_ERROR)

project(recipe-03 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 20)

set(SRC_MESSAGE Message.h Message.cpp)

add_library(message-shared
        SHARED
        ${SRC_MESSAGE}
        )
add_library(message-static
        STATIC
        ${SRC_MESSAGE}
        )
add_executable(hello-world hello-world.cpp)
target_link_libraries(hello-world message-static)

编译时可以看到扫描了三个对象：message-static/hello-world/message-shared：
$ cmake --build .
Scanning dependencies of target message-static
[ 16%] Building CXX object CMakeFiles/message-static.dir/Message.cpp.o
[ 33%] Linking CXX static library libmessage-static.a
[ 33%] Built target message-static
Scanning dependencies of target hello-world
[ 50%] Building CXX object CMakeFiles/hello-world.dir/hello-world.cpp.o
[ 66%] Linking CXX executable hello-world.exe
[ 66%] Built target hello-world
Scanning dependencies of target message-shared
[ 83%] Building CXX object CMakeFiles/message-shared.dir/Message.cpp.o
[100%] Linking CXX shared library cygmessage-shared.dll
[100%] Built target message-shared
```

---------------

是否可以让CMake生成同名的两个库？换句话说，它们都可以被称为`message`，而不是`message-static`和`message-share`d吗？我们需要修改这两个目标的属性：

```
cmake_minimum_required(VERSION 3.14 FATAL_ERROR)

project(recipe-03 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 20)

add_library(message-objs
        OBJECT
        Message.h
        Message.cpp
        )
# this is only needed for older compilers
# but doesn't hurt either to have it
set_target_properties(message-objs
        PROPERTIES
        POSITION_INDEPENDENT_CODE 1
        )
add_library(message-shared
        SHARED
        $<TARGET_OBJECTS:message-objs>
        )
set_target_properties(message-shared
        PROPERTIES
        OUTPUT_NAME "message"
        )
add_library(message-static
        STATIC
        $<TARGET_OBJECTS:message-objs>
        )
set_target_properties(message-static
        PROPERTIES
        OUTPUT_NAME "message"
        )
add_executable(hello-world hello-world.cpp)
target_link_libraries(hello-world message-static)

编译：
$ cmake --build .
Scanning dependencies of target message-objs
[ 20%] Building CXX object CMakeFiles/message-objs.dir/Message.cpp.o
[ 20%] Built target message-objs
Scanning dependencies of target message-static
[ 40%] Linking CXX static library libmessage.a
[ 40%] Built target message-static
Scanning dependencies of target hello-world
[ 60%] Building CXX object CMakeFiles/hello-world.dir/hello-world.cpp.o
[ 80%] Linking CXX executable hello-world.exe
[ 80%] Built target hello-world
Scanning dependencies of target message-shared
[100%] Linking CXX shared library cygmessage.dll
[100%] Built target message-shared

$ ls -l
总用量 364
-rwxrw-r--+ 1 Ben Ben   1429 11月 27 22:37 cmake_install.cmake
-rwxrw-r--+ 1 Ben Ben  13549 11月 27 22:37 CMakeCache.txt
drwxrwxr-x+ 1 Ben Ben      0 11月 27 22:38 CMakeFiles
-rwxrwxr-x+ 1 Ben Ben 137251 11月 27 22:38 cygmessage.dll
-rwxrwxr-x+ 1 Ben Ben 189656 11月 27 22:38 hello-world.exe
-rwxrw-r--+ 1 Ben Ben   2948 11月 27 22:37 libmessage.a
-rwxrw-r--+ 1 Ben Ben   2828 11月 27 22:38 libmessage.dll.a
-rwxrw-r--+ 1 Ben Ben   7738 11月 27 22:37 Makefile
```

我们可以链接到DSO吗？这取决于操作系统和编译器：

1. GNU/Linux和macOS上，不管选择什么编译器，它都可以工作。
2. Windows上，不能与Visual Studio兼容，但可以与MinGW和MSYS2兼容。

这是为什么呢？生成好的DSO组需要程序员限制符号的可见性。需要在编译器的帮助下实现，但不同的操作系统和编译器上，约定不同。CMake有一个机制来处理这个问题，我们将在第10章中解释它如何工作。

## 1.4 用条件句控制编译

**具体实施**

从与上一个示例的的源代码开始，我们希望能够在不同的两种行为之间进行切换：

1. 将`Message.hpp`和`Message.cpp`构建成一个库(静态或动态)，然后将生成库链接到`hello-world`可执行文件中。
2. 将`Message.hpp`，`Message.cpp`和`hello-world.cpp`构建成一个可执行文件，但不生成任何一个库。

让我们来看看如何使用`CMakeLists.txt`来实现：

1. 首先，定义最低CMake版本、项目名称和支持的语言：

   ```
   cmake_minimum_required(VERSION 3.5 FATAL_ERROR)
   project(recipe-04 LANGUAGES CXX)
   ```

2. 我们引入了一个新变量`USE_LIBRARY`，这是一个逻辑变量，值为`OFF`。我们还打印了它的值：

   ```
   set(USE_LIBRARY OFF)
   message(STATUS "Compile sources into a library? ${USE_LIBRARY}")  # 加了status，以-- Compile source into a library? OFF显示；不加status，无前缀--
   ```

3. CMake中定义`BUILD_SHARED_LIBS`全局变量，并设置为`OFF`。调用`add_library`并省略第二个参数，将构建一个静态库：

   ```
   set(BUILD_SHARED_LIBS OFF)
   ```

4. 然后，引入一个变量`_sources`，包括`Message.hpp`和`Message.cpp`：

   ```
   list(APPEND _sources Message.hpp Message.cpp)
   ```

5. 然后，引入一个基于`USE_LIBRARY`值的`if-else`语句。如果逻辑为真，则`Message.hpp`和`Message.cpp`将打包成一个库：

   ```
   if(USE_LIBRARY)
       # add_library will create a static library
       # since BUILD_SHARED_LIBS is OFF
       add_library(message ${_sources})
       add_executable(hello-world hello-world.cpp)
       target_link_libraries(hello-world message)
   else()
       add_executable(hello-world hello-world.cpp ${_sources})
   endif()
   ```

6. 我们可以再次使用相同的命令集进行构建。由于`USE_LIBRARY`为`OFF`, `hello-world`可执行文件将使用所有源文件来编译。可以通过在GNU/Linux上，运行`objdump -x`命令进行验证。

   ```c++
   cmake_minimum_required(VERSION 3.14 FATAL_ERROR)
   
   project(recipe-04 LANGUAGES CXX)
   
   set(CMAKE_CXX_STANDARD 20)
   
   # introduce a toggle for using a library
   set(USE_LIBRARY ON)
   
   message(STATUS "Compile sources into a library? ${USE_LIBRARY}")  # 加了status，以-- Compile source into a library? OFF显示；不加status，无前缀--
   
   # BUILD_SHARED_LIBS is a global flag offered by CMake
   # to toggle the behavior of add_library
   set(BUILD_SHARED_LIBS OFF)  # 这里设置为ON，则add_library会默认生成动态库
   
   # list sources
   list(APPEND _sources Message.h Message.cpp)
   
   if (USE_LIBRARY)
       # add_library will create a static library
       # since BUILD_SHARED_LIBS is OFF
       add_library(message ${_sources})  # 如果没有BUILD_SHARED_LIBS变量，则默认生成STATIC库
   
       add_executable(hello-world hello-world.cpp)
   
       target_link_libraries(hello-world message)
   else ()
       add_executable(hello-world hello-world.cpp ${_sources})
   endif ()
   ```

**工作原理**

我们介绍了两个变量：`USE_LIBRARY`和`BUILD_SHARED_LIBS`。这两个变量都设置为`OFF`。如CMake语言文档中描述，逻辑真或假可以用多种方式表示：

- 如果将逻辑变量设置为以下任意一种：`1`、`ON`、`YES`、`true`、`Y`或非零数，则逻辑变量为`true`。
- 如果将逻辑变量设置为以下任意一种：`0`、`OFF`、`NO`、`false`、`N`、`IGNORE、NOTFOUND`、空字符串，或者以`-NOTFOUND`为后缀，则逻辑变量为`false`。

`USE_LIBRARY`变量将在第一个和第二个行为之间切换。`BUILD_SHARED_LIBS`是CMake的一个全局标志。因为CMake内部要查询`BUILD_SHARED_LIBS`全局变量，所以`add_library`命令可以在不传递`STATIC/SHARED/OBJECT`参数的情况下调用；如果为`false`或未定义，将生成一个静态库。

这个例子说明，可以引入条件来控制CMake中的执行流。但是，当前的设置不允许从外部切换，不需要手动修改`CMakeLists.txt`。原则上，我们希望能够向用户开放所有设置，这样就可以在不修改构建代码的情况下调整配置，稍后将展示如何做到这一点。

**NOTE**:*`else()`和`endif()`中的`()`，可能会让刚开始学习CMake代码的同学感到惊讶。其历史原因是，因为其能够指出指令的作用范围。例如，可以使用`if(USE_LIBRARY)…else(USE_LIBRARY)…endif(USE_LIBIRAY)`。这个格式并不唯一，可以根据个人喜好来决定使用哪种格式。*

**TIPS**:*`_sources`变量是一个局部变量，不应该在当前范围之外使用，可以在名称前加下划线。*

## 1.5 向用户显示选项

前面的配置中，我们引入了条件句：通过硬编码的方式给定逻辑变量值。不过，这会影响用户修改这些变量。CMake代码没有向读者传达，该值可以从外部进行修改。推荐在`CMakeLists.txt`中使用`option()`命令，以选项的形式显示逻辑开关，用于外部设置，从而切换构建系统的生成行为。本节的示例将向您展示，如何使用这个命令。

**具体实施**

看一下前面示例中的静态/动态库示例。与其硬编码`USE_LIBRARY`为`ON`或`OFF`，现在为其设置一个默认值，同时也可以从外部进行更改：

1. 用一个选项替换上一个示例的`set(USE_LIBRARY OFF)`命令。该选项将修改`USE_LIBRARY`的值，并设置其默认值为`OFF`：

   ```
   option(USE_LIBRARY "Compile sources into a library" OFF)  # 默认值为OFF
   ```

2. 现在，可以通过CMake的`-D`CLI选项，将信息传递给CMake来切换库的行为：

   ```c++
   $ mkdir -p build
   $ cd build
   $ cmake -D USE_LIBRARY=ON ..
   -- ...
   -- Compile sources into a library? ON
   -- ...
   
   $ cmake --build .
   Scanning dependencies of target message
   [ 25%] Building CXX object CMakeFiles/message.dir/Message.cpp.o
   [ 50%] Linking CXX static library libmessage.a
   [ 50%] Built target message
   Scanning dependencies of target hello-world
   [ 75%] Building CXX object CMakeFiles/hello-world.dir/hello-world.cpp.o
   [100%] Linking CXX executable hello-world
   [100%] Built target hello-world
   ```

`-D`开关用于为CMake设置任何类型的变量：逻辑变量、路径等等。

**工作原理**

`option`可接受三个参数：

```
option(<option_variable> "help string" [initial value])
```

- `<option_variable>`表示该选项的变量的名称。
- `"help string"`记录选项的字符串，在CMake的终端或图形用户界面中可见。
- `[initial value]`选项的默认值，可以是`ON`或`OFF`。

**更多信息**

有时选项之间会有依赖的情况。示例中，我们提供生成静态库或动态库的选项。但是，如果没有将`USE_LIBRARY`逻辑设置为`ON`，则此选项没有任何意义。CMake提供`cmake_dependent_option()`命令用来定义依赖于其他选项的选项：

```c++
include(CMakeDependentOption)
cmake_dependent_option(MAKE_STATIC_LIBRARY "Compile sources into a static library" OFF
        "USE_LIBRARY" ON
        )
cmake_dependent_option(MAKE_SHARED_LIBRARY "Compile sources into a shared library" ON
        "USE_LIBRARY" ON
        )
```

如果`USE_LIBRARY`为`ON`，`MAKE_STATIC_LIBRARY`默认值为`OFF`，否则`MAKE_STATIC_LIBRARY`默认值为`ON`。可以这样运行：

```
$ cmake -D USE_LIBRARY=OFF -D MAKE_SHARED_LIBRARY=ON ..
```

这仍然不会构建库，因为`USE_LIBRARY`仍然为`OFF`。

CMake有适当的机制，通过包含模块来扩展其语法和功能，这些模块要么是CMake自带的，要么是定制的。本例中，包含了一个名为`CMakeDependentOption`的模块。如果没有`include`这个模块，`cmake_dependent_option()`命令将不可用。参见 https://cmake.org/cmake/help/latest/module/CMakeDependentOption.html

**TIPS**:*手册中的任何模块都可以以命令行的方式使用`cmake --help-module <name-of-module>`。例如，`cmake --help-module CMakeDependentOption`将打印刚才讨论的模块的手册页(帮助页面)。*

完整CMakeLists.txt如下：

`````c++
cmake_minimum_required(VERSION 3.14 FATAL_ERROR)

project(recipe-04 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 20)

option(USE_LIBRARY OFF)  # Default value is OFF

message(STATUS "Compile sources into a library? ${USE_LIBRARY}")

include(CMakeDependentOption)
# 如果USE_LIBRARY为true，则MAKE_STATIC_LIBRARY值为OFF，否则为ON
cmake_dependent_option(MAKE_STATIC_LIBRARY "Compile sources into a static library" OFF
        "USE_LIBRARY" ON
        )
cmake_dependent_option(MAKE_SHARED_LIBRARY "Compile sources into a shared library" ON
        "USE_LIBRARY" ON
        )

# list sources
list(APPEND _sources Message.cpp)

message(STATUS "Compile sources into a STATIC library? ${MAKE_STATIC_LIBRARY}")
message(STATUS "Compile sources into a SHARED library? ${MAKE_SHARED_LIBRARY}")
if (USE_LIBRARY)
    if (MAKE_SHARED_LIBRARY)
        add_library(message SHARED ${_sources})
    else ()
        add_library(message STATIC ${_sources})
    endif ()

    add_executable(hello-world hello-world.cpp)
    target_link_libraries(hello-world message)
else ()
    add_executable(hello-world hello-world.cpp ${_sources})
endif ()
`````

## 1.6 指定编译器

目前为止，我们还没有过多考虑如何选择编译器。CMake可以根据平台和生成器选择编译器，还能将编译器标志设置为默认值。然而，我们通常控制编译器的选择。在后面的示例中，我们还将考虑构建类型的选择，并展示如何控制编译器标志。

**具体实施**

如何选择一个特定的编译器？例如，如果想使用Intel或Portland Group编译器怎么办？CMake将语言的编译器存储在`CMAKE_<LANG>_COMPILER`变量中，其中`<LANG>`是受支持的任何一种语言，对于我们的目的是`CXX`、`C`或`Fortran`。用户可以通过以下两种方式之一设置此变量：

1. 使用CLI中的`-D`选项，例如：

   ```
   $ cmake -D CMAKE_CXX_COMPILER=clang++ ..
   ```

2. 通过导出环境变量`CXX`(C++编译器)、`CC`(C编译器)和`FC`(Fortran编译器)。例如，使用这个命令使用`clang++`作为`C++`编译器：

   ```
   $ env CXX=clang++ cmake ..
   ```

到目前为止讨论的示例，都可以通过传递适当的选项，配置合适的编译器。

**NOTE**:*CMake了解运行环境，可以通过其CLI的`-D`开关或环境变量设置许多选项。前一种机制覆盖后一种机制，但是我们建议使用`-D`显式设置选项。显式优于隐式，因为环境变量可能被设置为不适合(当前项目)的值。*

我们在这里假设，其他编译器在标准路径中可用，CMake在标准路径中执行查找编译器。如果不是这样，用户将需要将完整的编译器可执行文件或包装器路径传递给CMake。

**TIPS**:*我们建议使用`-D CMAKE_<LANG>_COMPILER`CLI选项设置编译器，而不是导出`CXX`、`CC`和`FC`。这是确保跨平台并与非POSIX兼容的唯一方法。为了避免变量污染环境，这些变量可能会影响与项目一起构建的外部库环境。*

**工作原理**

配置时，CMake会进行一系列平台测试，以确定哪些编译器可用，以及它们是否适合当前的项目。一个合适的编译器不仅取决于我们所使用的平台，还取决于我们想要使用的生成器。CMake执行的第一个测试基于项目语言的编译器的名称。例如，`cc`是一个工作的`C`编译器，那么它将用作`C`项目的默认编译器。GNU/Linux上，使用Unix Makefile或Ninja时, GCC家族中的编译器很可能是`C++`、`C`和`Fortran`的默认选择。Microsoft Windows上，将选择Visual Studio中的`C++`和`C`编译器(前提是Visual Studio是生成器)。如果选择MinGW或MSYS Makefile作为生成器，则默认使用MinGW编译器。

**更多信息**

我们的平台上的CMake，在哪里可以找到可用的编译器和编译器标志？CMake提供`--system-information`标志，它将把关于系统的所有信息转储到屏幕或文件中。要查看这个信息，请尝试以下操作：

```
$ cmake --system-information information.txt
```

文件中(本例中是`information.txt`)可以看到`CMAKE_CXX_COMPILER`、`CMAKE_C_COMPILER`和`CMAKE_Fortran_COMPILER`的默认值，以及默认标志。我们将在下一个示例中看到相关的标志。

CMake提供了额外的变量来与编译器交互：

- `CMAKE_<LANG>_COMPILER_LOADED`:如果为项目启用了语言`<LANG>`，则将设置为`TRUE`。
- `CMAKE_<LANG>_COMPILER_ID`:编译器标识字符串，编译器供应商所特有。例如，`GCC`用于GNU编译器集合，`AppleClang`用于macOS上的Clang, `MSVC`用于Microsoft Visual Studio编译器。注意，不能保证为所有编译器或语言定义此变量。
- `CMAKE_COMPILER_IS_GNU<LANG>`:如果语言`<LANG>`是GNU编译器集合的一部分，则将此逻辑变量设置为`TRUE`。注意变量名的`<LANG>`部分遵循GNU约定：C语言为`CC`, C++语言为`CXX`, Fortran语言为`G77`。
- `CMAKE_<LANG>_COMPILER_VERSION`:此变量包含一个字符串，该字符串给定语言的编译器版本。版本信息在`major[.minor[.patch[.tweak]]]`中给出。但是，对于`CMAKE_<LANG>_COMPILER_ID`，不能保证所有编译器或语言都定义了此变量。

我们可以尝试使用不同的编译器，配置下面的示例`CMakeLists.txt`。这个例子中，我们将使用CMake变量来探索已使用的编译器(及版本)：

```
cmake_minimum_required(VERSION 3.14 FATAL_ERROR)

project(recipe-06 LANGUAGES CXX)

message(STATUS "Is the C++ compiler loaded? ${CMAKE_CXX_COMPILER_LOADED}")
if (CMAKE_CXX_COMPILER_LOADED)
    message(STATUS "The C++ compiler ID is: ${CMAKE_CXX_COMPILER_ID}")
    message(STATUS "Is the C++ from GNU? ${CMAKE_COMPILER_IS_GNUCXX}")
    message(STATUS "The C++ compiler version is: ${CMAKE_CXX_COMPILER_VERSION}")
endif ()
message(STATUS "Is the C compiler loaded? ${CMAKE_C_COMPILER_LOADED}")
if (CMAKE_C_COMPILER_LOADED)
    message(STATUS "The C compiler ID is: ${CMAKE_C_COMPILER_ID}")
    message(STATUS "Is the C from GNU? ${CMAKE_COMPILER_IS_GNUCC}")
    message(STATUS "The C compiler version is: ${CMAKE_C_COMPILER_VERSION}")
endif ()
```

注意，这个例子不包含任何目标，没有要构建的东西，我们只关注配置步骤:

```
$ cmake ..

...
-- Is the C++ compiler loaded? 1
-- The C++ compiler ID is: GNU
-- Is the C++ from GNU? 1
-- The C++ compiler version is: 8.1.0
-- Is the C compiler loaded? 1
-- The C compiler ID is: GNU
-- Is the C from GNU? 1
-- The C compiler version is: 8.1.0
...
```

当然，输出将取决于可用和已选择的编译器(及版本)。

## 1.7 切换构建类型

CMake可以配置构建类型，例如：Debug、Release等。配置时，可以为Debug或Release构建设置相关的选项或属性，例如：编译器和链接器标志。控制生成构建系统使用的配置变量是`CMAKE_BUILD_TYPE`。该变量默认为空，CMake识别的值为:

1. **Debug**：用于在没有优化的情况下，使用带有调试符号构建库或可执行文件。
2. **Release**：用于构建的优化的库或可执行文件，不包含调试符号。
3. **RelWithDebInfo**：用于构建较少的优化库或可执行文件，包含调试符号。
4. **MinSizeRel**：用于不增加目标代码大小的优化方式，来构建库或可执行文件。

**具体实施**

示例中，我们将展示如何为项目设置构建类型：

1. 首先，定义最低CMake版本、项目名称和支持的语言：

   ```
   cmake_minimum_required(VERSION 3.5 FATAL_ERROR)project(recipe-07 LANGUAGES C CXX)
   ```

2. 然后，设置一个默认的构建类型(本例中是Release)，并打印一条消息。要注意的是，该变量被设置为缓存变量，可以通过缓存进行编辑：

   ```
   if (NOT CMAKE_BUILD_TYPE)
       set(CMAKE_BUILD_TYPE Release CACHE STRING "Build type" FORCE)
   endif()
   message(STATUS "Build type: ${CMAKE_BUILD_TYPE}")
   ```

3. 最后，打印出CMake设置的相应编译标志：

   ```
   message(STATUS "C flags, Debug configuration: ${CMAKE_C_FLAGS_DEBUG}")
   message(STATUS "C flags, Release configuration: ${CMAKE_C_FLAGS_RELEASE}")
   message(STATUS "C flags, Release configuration with Debug info: ${CMAKE_C_FLAGS_RELWITHDEBINFO}")
   message(STATUS "C flags, minimal Release configuration: ${CMAKE_C_FLAGS_MINSIZEREL}")
   message(STATUS "C++ flags, Debug configuration: ${CMAKE_CXX_FLAGS_DEBUG}")
   message(STATUS "C++ flags, Release configuration: ${CMAKE_CXX_FLAGS_RELEASE}")
   message(STATUS "C++ flags, Release configuration with Debug info: ${CMAKE_CXX_FLAGS_RELWITHDEBINFO}")
   message(STATUS "C++ flags, minimal Release configuration: ${CMAKE_CXX_FLAGS_MINSIZEREL}")
   ```

4. 验证配置的输出:

   ```
   $ cmake ..
   ...
   -- Build type: Release
   -- C flags, Debug configuration: -g
   -- C flags, Release configuration: -O3 -DNDEBUG
   -- C flags, Release configuration with Debug info: -O2 -g -DNDEBUG
   -- C flags, minimal Release configuration: -Os -DNDEBUG
   -- C++ flags, Debug configuration: -g
   -- C++ flags, Release configuration: -O3 -DNDEBUG
   -- C++ flags, Release configuration with Debug info: -O2 -g -DNDEBUG
   -- C++ flags, minimal Release configuration: -Os -DNDEBUG
   ```

5. 切换构建类型:

   ```
   $ cmake -D CMAKE_BUILD_TYPE=Debug ..
   -- Build type: Debug
   -- C flags, Debug configuration: -g
   -- C flags, Release configuration: -O3 -DNDEBUG
   -- C flags, Release configuration with Debug info: -O2 -g -DNDEBUG
   -- C flags, minimal Release configuration: -Os -DNDEBUG
   -- C++ flags, Debug configuration: -g
   -- C++ flags, Release configuration: -O3 -DNDEBUG
   -- C++ flags, Release configuration with Debug info: -O2 -g -DNDEBUG
   -- C++ flags, minimal Release configuration: -Os -DNDEBUG
   ```

**工作原理**

我们演示了如何设置默认构建类型，以及如何(从命令行)覆盖它。这样，就可以控制项目，是使用优化，还是关闭优化启用调试。我们还看到了不同配置使用了哪些标志，这主要取决于选择的编译器。需要在运行CMake时显式地打印标志，也可以仔细阅读运行`CMake --system-information`的输出，以了解当前平台、默认编译器和语言的默认组合是什么。下一个示例中，我们将讨论如何为不同的编译器和不同的构建类型，扩展或调整编译器标志。

**更多信息**

我们展示了变量`CMAKE_BUILD_TYPE`，如何切换生成构建系统的配置(这个链接中有说明: https://cmake.org/cmake/help/v3.5/variable/CMAKE_BUILD_TYPE.html )。Release和Debug配置中构建项目通常很有用，例如：评估编译器优化级别的效果。对于单配置生成器，如Unix Makefile、MSYS Makefile或Ninja，因为要对项目重新配置，这里需要运行CMake两次。不过，CMake也支持复合配置生成器。这些通常是集成开发环境提供的项目文件，最显著的是Visual Studio和Xcode，它们可以同时处理多个配置。可以使用`CMAKE_CONFIGURATION_TYPES`变量可以对这些生成器的可用配置类型进行调整，该变量将接受一个值列表(可从这个链接获得文档:https://cmake.org/cmake/help/v3.5/variable/CMAKE_CONFIGURATION_TYPES.html)。

下面是对Visual Studio的CMake调用:

```
$ mkdir -p build
$ cd build
$ cmake .. -G"Visual Studio 12 2017 Win64" -D CMAKE_CONFIGURATION_TYPES="Release;Debug"
```

将为Release和Debug配置生成一个构建树。然后，您可以使`--config`标志来决定构建这两个中的哪一个:

```
$ cmake --build . --config Release
```

**NOTE**:*当使用单配置生成器开发代码时，为Release版和Debug创建单独的构建目录，两者使用相同的源代码。这样，就可以在两者之间切换，而不用重新配置和编译。*

## 1.8 设置编译器选项

前面的示例展示了如何探测CMake，从而获得关于编译器的信息，以及如何切换项目中的编译器。后一个任务是控制项目的编译器标志。CMake为调整或扩展编译器标志提供了很大的灵活性，您可以选择下面两种方法:

- CMake将编译选项视为目标属性。因此，可以根据每个目标设置编译选项，而不需要覆盖CMake默认值。
- 可以使用`-D`CLI标志直接修改`CMAKE_<LANG>_FLAGS_<CONFIG>`变量。这将影响项目中的所有目标，并覆盖或扩展CMake默认值。

本示例中，我们将展示这两种方法。

**准备工作**

编写一个示例程序，计算不同几何形状的面积，`computer_area.cpp`：

```c++
#include "geometry_circle.hpp"
#include "geometry_polygon.hpp"
#include "geometry_rhombus.hpp"
#include "geometry_square.hpp"
#include <cstdlib>
#include <iostream>

int main() {
    using namespace geometry;
    double radius = 2.5293;
    double A_circle = area::circle(radius);
    std::cout << "A circle of radius " << radius << " has an area of " << A_circle
              << std::endl;
    int nSides = 19;
    double side = 1.29312;
    double A_polygon = area::polygon(nSides, side);
    std::cout << "A regular polygon of " << nSides << " sides of length " << side
              << " has an area of " << A_polygon << std::endl;
    double d1 = 5.0;
    double d2 = 7.8912;
    double A_rhombus = area::rhombus(d1, d2);
    std::cout << "A rhombus of major diagonal " << d1 << " and minor diagonal " << d2
              << " has an area of " << A_rhombus << std::endl;
    double l = 10.0;
    double A_square = area::square(l);
    std::cout << "A square of side " << l << " has an area of " << A_square
              << std::endl;
    return EXIT_SUCCESS;
}
```

函数的各种实现分布在不同的文件中，每个几何形状都有一个头文件和源文件。总共有4个头文件和5个源文件要编译：

```
.
├─ CMakeLists.txt
├─ compute-areas.cpp
├─ geometry_circle.cpp
├─ geometry_circle.hpp
├─ geometry_polygon.cpp
├─ geometry_polygon.hpp
├─ geometry_rhombus.cpp
├─ geometry_rhombus.hpp
├─ geometry_square.cpp
└─ geometry_square.hpp
```

**具体实施**

现在已经有了源代码，我们的目标是配置项目，并使用编译器标示进行实验:

1. 设置CMake的最低版本:

   ```
   cmake_minimum_required(VERSION 3.5 FATAL_ERROR)
   ```

2. 声明项目名称和语言:

   ```
   project(recipe-08 LANGUAGES CXX)
   ```

3. 然后，打印当前编译器标志。CMake将对所有C++目标使用这些:

   ```
   message("C++ compiler flags: ${CMAKE_CXX_FLAGS}")
   ```

4. 为目标准备了标志列表，其中一些将无法在Windows上使用:

   ```
   list(APPEND flags "-fPIC" "-Wall")if(NOT WIN32)  list(APPEND flags "-Wextra" "-Wpedantic")endif()
   ```

5. 添加了一个新的目标——`geometry`库，并列出它的源依赖关系:

   ```
   add_library(geometry
     STATIC
       geometry_circle.cpp
       geometry_circle.hpp
       geometry_polygon.cpp
       geometry_polygon.hpp
       geometry_rhombus.cpp
       geometry_rhombus.hpp
       geometry_square.cpp
       geometry_square.hpp
     )
   ```

6. 为这个库目标设置了编译选项:

   ```
   target_compile_options(geometry
     PRIVATE
       ${flags}
     )
   ```

7. 然后，将生成`compute-areas`可执行文件作为一个目标:

   ```
   add_executable(compute-areas compute-areas.cpp)
   ```

8. 还为可执行目标设置了编译选项:

   ```
   target_compile_options(compute-areas
     PRIVATE
       "-fPIC"
     )
   ```

9. 最后，将可执行文件链接到geometry库:

   ```
   target_link_libraries(compute-areas geometry)
   ```

完整CMakeLists.txt：

```c++
cmake_minimum_required(VERSION 3.14 FATAL_ERROR)

project(recipe-08 LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 20)

message("C++ compiler flags: ${CMAKE_CXX_FLAGS}")

list(APPEND flags "-fPIC" "-Wall")
if (NOT WIN32)
    list(APPEND flags "-Wextra" "-Wpedantic")
endif ()

add_library(geometry
        STATIC
        geometry_circle.cpp
        geometry_circle.hpp
        geometry_polygon.cpp
        geometry_polygon.hpp
        geometry_rhombus.cpp
        geometry_rhombus.hpp
        geometry_square.cpp
        geometry_square.hpp
        )

target_compile_options(geometry
        PRIVATE
        ${flags}
        )

add_executable(compute-areas compute_areas.cpp)

target_compile_options(compute-areas
        PRIVATE
        "-fPIC")

target_link_libraries(compute-areas geometry)
```

--------------

[**target_compile_definitions和target_compile_options中第二个参数的含义**](https://blog.csdn.net/qq_34369618/article/details/96358204)

Libs can define their compile flags as PRIVATE, PUBLIC or INTERFACE.

- PRIVATE basically means the flags are only applied to this library.
- PUBLIC and INTERFACE flags are also applied to any target which links to it (i.e. its dependees);
- the difference between these two being that INTERFACE flags aren’t applied to the library itself - only its dependees

PRIVATE只把flag加入我这个库
PUBLIC和INTERFACE会加入任何链接我的目标的编译属性
INTERFACE不把flag加入我这个库，而指使别人怎么做

---------------

**如何工作**

本例中，警告标志有`-Wall`、`-Wextra`和`-Wpedantic`，将这些标示添加到`geometry`目标的编译选项中； `compute-areas`和 `geometry`目标都将使用`-fPIC`标志。编译选项可以添加三个级别的可见性：`INTERFACE`、`PUBLIC`和`PRIVATE`。

可见性的含义如下:

- **PRIVATE**，编译选项会应用于给定的目标，不会传递给与目标相关的目标。我们的示例中， 即使`compute-areas`将链接到`geometry`库，`compute-areas`也不会继承`geometry`目标上设置的编译器选项。
- **INTERFACE**，给定的编译选项将只应用于指定目标，并传递给与目标相关的目标。
- **PUBLIC**，编译选项将应用于指定目标和使用它的目标。

目标属性的可见性CMake的核心，我们将在本书中经常讨论这个话题。以这种方式添加编译选项，不会影响全局CMake变量`CMAKE_<LANG>_FLAGS_<CONFIG>`，并能更细粒度控制在哪些目标上使用哪些选项。

我们如何验证，这些标志是否按照我们的意图正确使用呢？或者换句话说，如何确定项目在CMake构建时，实际使用了哪些编译标志？一种方法是，使用CMake将额外的参数传递给本地构建工具。本例中会设置环境变量`VERBOSE=1`：

```c++
$ mkdir -p build
$ cd build
$ cmake ..
$ cmake --build . -- VERBOSE=1
    
... lots of output ...
    
[ 14%] Building CXX object CMakeFiles/geometry.dir/geometry_circle.cpp.o
/usr/bin/c++ -fPIC -Wall -Wextra -Wpedantic -o CMakeFiles/geometry.dir/geometry_circle.cpp.o -c /home/bast/tmp/cmake-cookbook/chapter-01/recipe-08/cxx-example/geometry_circle.cpp
[ 28%] Building CXX object CMakeFiles/geometry.dir/geometry_polygon.cpp.o
/usr/bin/c++ -fPIC -Wall -Wextra -Wpedantic -o CMakeFiles/geometry.dir/geometry_polygon.cpp.o -c /home/bast/tmp/cmake-cookbook/chapter-01/recipe-08/cxx-example/geometry_polygon.cpp
[ 42%] Building CXX object CMakeFiles/geometry.dir/geometry_rhombus.cpp.o
/usr/bin/c++ -fPIC -Wall -Wextra -Wpedantic -o CMakeFiles/geometry.dir/geometry_rhombus.cpp.o -c /home/bast/tmp/cmake-cookbook/chapter-01/recipe-08/cxx-example/geometry_rhombus.cpp
[ 57%] Building CXX object CMakeFiles/geometry.dir/geometry_square.cpp.o
/usr/bin/c++ -fPIC -Wall -Wextra -Wpedantic -o CMakeFiles/geometry.dir/geometry_square.cpp.o -c /home/bast/tmp/cmake-cookbook/chapter-01/recipe-08/cxx-example/geometry_square.cpp
    
... more output ...

[ 85%] Building CXX object CMakeFiles/compute-areas.dir/compute-areas.cpp.o
/usr/bin/c++ -fPIC -o CMakeFiles/compute-areas.dir/compute-areas.cpp.o -c /home/bast/tmp/cmake-cookbook/chapter-01/recipe-08/cxx-example/compute-areas.cpp

... more output ...
```

输出确认编译标志，确认指令设置正确。

--------------

修改geometry的编译选项为INTERFACE：

```c++
target_compile_options(geometry
        INTERFACE
        ${flags}
        )
可以看到如下：
[ 57%] Building CXX object CMakeFiles/geometry.dir/geometry_square.cpp.o 
/usr/bin/c++    -std=gnu++1z -o CMakeFiles/geometry.dir/geometry_square.cpp.o -c /home/ben/Softwares/JetBrains/CppProjects/TestProject/geometry_square.cpp
...
[ 85%] Building CXX object CMakeFiles/compute-areas.dir/compute_areas.cpp.o
/usr/bin/c++    -fPIC -Wall -Wextra -Wpedantic -std=gnu++1z -o CMakeFiles/compute-areas.dir/compute_areas.cpp.o -c /home/ben/Softwares/JetBrains/CppProjects/TestProject/compute_areas.cpp
```

再修改compute-areas编译选项为INTERFACE，则看到编译内容和上面一样：

```c++
target_compile_options(compute-areas
        INTERFACE
        "-fPIC")
```

----------------

控制编译器标志的第二种方法，不用对`CMakeLists.txt`进行修改。如果想在这个项目中修改`geometry`和`compute-areas`目标的编译器选项，可以使用CMake参数进行配置：

```
$ cmake -D CMAKE_CXX_FLAGS="-fno-exceptions -fno-rtti" ..
```

这将使用`-fno-rtti - fpic - wall - Wextra - wpedantic`配置`geometry`目标，同时使用`-fno exception -fno-rtti - fpic`配置`compute-areas`。

**NOTE**:*本书中，我们推荐为每个目标设置编译器标志。使用`target_compile_options()`不仅允许对编译选项进行细粒度控制，而且还可以更好地与CMake的更高级特性进行集成。*

---------------

查看配置了在cmake命令中增加`CMAKE_CXX_FLAGS`效果：

```c++
$ cmake -D CMAKE_CXX_FLAGS="-fno-exceptions -fno-rtti" ..
-- The CXX compiler identification is GNU 7.5.0
-- Check for working CXX compiler: /usr/bin/c++
-- Check for working CXX compiler: /usr/bin/c++ - works
-- Detecting CXX compiler ABI info
-- Detecting CXX compiler ABI info - done
-- Detecting CXX compile features
-- Detecting CXX compile features - done
C++ compiler flags: -fno-exceptions -fno-rtti
-- Configuring done
-- Generating done

$ cmake --build . -- VERBOSE=1  
[ 57%] Building CXX object CMakeFiles/geometry.dir/geometry_square.cpp.o
/usr/bin/c++    -fno-exceptions -fno-rtti   -fPIC -Wall -Wextra -Wpedantic -std=gnu++1z -o CMakeFiles/geometry.dir/geometry_square.cpp.o -c /home/ben/Softwares/JetBrains/CppProjects/TestProject/geometry_square.cpp
...
[ 85%] Building CXX object CMakeFiles/compute-areas.dir/compute_areas.cpp.o
/usr/bin/c++    -fno-exceptions -fno-rtti   -fPIC -std=gnu++1z -o CMakeFiles/compute-areas.dir/compute_areas.cpp.o -c /home/ben/Softwares/JetBrains/CppProjects/TestProject/compute_areas.cpp
```

--------------

**更多信息**

大多数时候，编译器有特性标示。当前的例子只适用于`GCC`和`Clang`；其他供应商的编译器不确定是否会理解(如果不是全部)这些标志。如果项目是真正跨平台，那么这个问题就必须得到解决，有三种方法可以解决这个问题。

最典型的方法是将所需编译器标志列表附加到每个配置类型CMake变量`CMAKE_<LANG>_FLAGS_<CONFIG>`。标志确定设置为给定编译器有效的标志，因此将包含在`if-endif`子句中，用于检查`CMAKE_<LANG>_COMPILER_ID`变量，例如：

```c++
if(CMAKE_CXX_COMPILER_ID MATCHES GNU)
  list(APPEND CMAKE_CXX_FLAGS "-fno-rtti" "-fno-exceptions")
  list(APPEND CMAKE_CXX_FLAGS_DEBUG "-Wsuggest-final-types" "-Wsuggest-final-methods" "-Wsuggest-override")
  list(APPEND CMAKE_CXX_FLAGS_RELEASE "-O3" "-Wno-unused")
endif()
if(CMAKE_CXX_COMPILER_ID MATCHES Clang)
  list(APPEND CMAKE_CXX_FLAGS "-fno-rtti" "-fno-exceptions" "-Qunused-arguments" "-fcolor-diagnostics")
  list(APPEND CMAKE_CXX_FLAGS_DEBUG "-Wdocumentation")
  list(APPEND CMAKE_CXX_FLAGS_RELEASE "-O3" "-Wno-unused")
endif()
```

更细粒度的方法是，不修改`CMAKE_<LANG>_FLAGS_<CONFIG>`变量，而是定义特定的标志列表：

```
set(COMPILER_FLAGS)
set(COMPILER_FLAGS_DEBUG)
set(COMPILER_FLAGS_RELEASE)

if(CMAKE_CXX_COMPILER_ID MATCHES GNU)
  list(APPEND CXX_FLAGS "-fno-rtti" "-fno-exceptions")
  list(APPEND CXX_FLAGS_DEBUG "-Wsuggest-final-types" "-Wsuggest-final-methods" "-Wsuggest-override")
  list(APPEND CXX_FLAGS_RELEASE "-O3" "-Wno-unused")
endif()
if(CMAKE_CXX_COMPILER_ID MATCHES Clang)
  list(APPEND CXX_FLAGS "-fno-rtti" "-fno-exceptions" "-Qunused-arguments" "-fcolor-diagnostics")
  list(APPEND CXX_FLAGS_DEBUG "-Wdocumentation")
  list(APPEND CXX_FLAGS_RELEASE "-O3" "-Wno-unused")
endif()
```

稍后，使用**生成器表达式**来设置编译器标志的基础上，为每个配置和每个目标生成构建系统:

```
target_compile_options(compute-areas
        PRIVATE
        ${CXX_FLAGS}
        "$<$<CONFIG:Debug>:${CXX_FLAGS_DEBUG}>"
        "$<$<CONFIG:Release>:${CXX_FLAGS_RELEASE}>"
        )
```

当前示例中展示了这两种方法，我们推荐后者(特定于项目的变量和`target_compile_options`)。

两种方法都有效，并在许多项目中得到广泛应用。不过，每种方式都有缺点。`CMAKE_<LANG>_COMPILER_ID`不能保证为所有编译器都定义。此外，一些标志可能会被弃用，或者在编译器的较晚版本中引入。与`CMAKE_<LANG>_COMPILER_ID`类似，`CMAKE_<LANG>_COMPILER_VERSION`变量不能保证为所有语言和供应商都提供定义。尽管检查这些变量的方式非常流行，但我们认为更健壮的替代方法是检查所需的标志集是否与给定的编译器一起工作，这样项目中实际上只使用有效的标志。结合特定于项目的变量、`target_compile_options`和生成器表达式，会让解决方案变得非常强大。我们将在第7章的第3节中展示，如何使用`check-and-set`模式。

-----------

使用上面的生成器表达式方式验证：

```c++
$ cmake -DCMAKE_BUILD_TYPE=Release ..
$ cmake --build . -- VERBOSE=1

# 其中-O3 -DNDEBUG是Release模式时的默认选项
[ 85%] Building CXX object CMakeFiles/compute-areas.dir/compute_areas.cpp.o
/usr/bin/c++    -O3 -DNDEBUG   -fno-rtti -fno-exceptions -O3 -Wno-unused -std=gnu++1z -o CMakeFiles/compute-areas.dir/compute_areas.cpp.o -c /home/ben/Softwares/JetBrains/CppProjects/TestProject/compute_areas.cpp

###################
$ cmake -DCMAKE_BUILD_TYPE=Debug ..
$ cmake --build . -- VERBOSE=1
    
# 其中-g是Debug模式时的默认选项
[ 85%] Building CXX object CMakeFiles/compute-areas.dir/compute_areas.cpp.o
/usr/bin/c++    -g   -fno-rtti -fno-exceptions -Wsuggest-final-types -Wsuggest-final-methods -Wsuggest-override -std=gnu++1z -o CMakeFiles/compute-areas.dir/compute_areas.cpp.o -c /home/ben/Softwares/JetBrains/CppProjects/TestProject/compute_areas.cpp

###################
$ cmake ..
$ cmake --build . -- VERBOSE=1
    
# 如果不指定Release或Debug，则不会有对应的选项，也没有${CXX_FLAGS_DEBUG}和${CXX_FLAGS_RELEASE}
[ 85%] Building CXX object CMakeFiles/compute-areas.dir/compute_areas.cpp.o
/usr/bin/c++    -fno-rtti -fno-exceptions -std=gnu++1z -o CMakeFiles/compute-areas.dir/compute_areas.cpp.o -c /home/ben/Softwares/JetBrains/CppProjects/TestProject/compute_areas.cpp
```

--------------

## 1.9 为语言设定标准

编程语言有不同的标准，即提供改进的语言版本。启用新标准是通过设置适当的编译器标志来实现的。前面的示例中，我们已经展示了如何为每个目标或全局进行配置。3.1版本中，CMake引入了一个独立于平台和编译器的机制，用于为`C++`和`C`设置语言标准：为目标设置`<LANG>_STANDARD`属性。

**具体实施**

将逐步构建`CMakeLists.txt`，并展示如何设置语言标准(本例中是`C++14`):

1. 声明最低要求的CMake版本，项目名称和语言:

   ```
   cmake_minimum_required(VERSION 3.5 FATAL_ERROR)
   project(recipe-09 LANGUAGES CXX)
   ```

2. 要求在Windows上导出所有库符号:

   ```
   set(CMAKE_WINDOWS_EXPORT_ALL_SYMBOLS ON)
   ```

3. 需要为库添加一个目标，这将编译源代码为一个动态库:

   ```
   add_library(animals
     SHARED
       Animal.cpp
       Animal.hpp
       Cat.cpp
       Cat.hpp
       Dog.cpp
       Dog.hpp
       Factory.hpp
     )
   ```

4. 现在，为目标设置了`CXX_STANDARD`、`CXX_EXTENSIONS`和`CXX_STANDARD_REQUIRED`属性。还设置了`position_independent_code`属性，以避免在使用一些编译器构建DSO时出现问题:

   ```
   set_target_properties(animals
     PROPERTIES
       CXX_STANDARD 14
       CXX_EXTENSIONS OFF
       CXX_STANDARD_REQUIRED ON
       POSITION_INDEPENDENT_CODE 1
   )
   ```

5. 然后，为”动物农场”的可执行文件添加一个新目标，并设置它的属性:

   ```
   add_executable(animal-farm animal-farm.cpp)
   set_target_properties(animal-farm
     PROPERTIES
       CXX_STANDARD 14
       CXX_EXTENSIONS OFF
       CXX_STANDARD_REQUIRED ON
     )
   ```

6. 最后，将可执行文件链接到库:

   ```
   target_link_libraries(animal-farm animals)
   ```

7. 现在，来看看猫和狗都说了什么:

   ```
   $ mkdir -p build
   $ cd build
   $ cmake ..
   $ cmake --build .
   $ ./animal-farm
   
   I'm Simon the cat!
   I'm Marlowe the dog!
   ```

完整CMakeLists.txt

```c++
# set minimum cmake version
cmake_minimum_required(VERSION 3.5 FATAL_ERROR)

# project name and language
project(recipe-09 LANGUAGES CXX)

set(CMAKE_WINDOWS_EXPORT_ALL_SYMBOLS ON)

add_library(animals
        SHARED
        Animal.cpp
        Animal.hpp
        Cat.cpp
        Cat.hpp
        Dog.cpp
        Dog.hpp
        Factory.hpp
        )

set_target_properties(animals
        PROPERTIES
        CXX_STANDARD 14
        CXX_EXTENSIONS OFF
        CXX_STANDARD_REQUIRED ON
        POSITION_INDEPENDENT_CODE 1
        )

add_executable(animal-farm animal-farm.cpp)

set_target_properties(animal-farm
        PROPERTIES
        CXX_STANDARD 14
        CXX_EXTENSIONS OFF
        CXX_STANDARD_REQUIRED ON
        )

target_link_libraries(animal-farm animals)
```

**工作原理**

步骤4和步骤5中，我们为动物和动物农场目标设置了一些属性:

- **CXX_STANDARD**会设置我们想要的标准。
- **CXX_EXTENSIONS**告诉CMake，只启用`ISO C++`标准的编译器标志，而不使用特定编译器的扩展。
- **CXX_STANDARD_REQUIRED**指定所选标准的版本。如果这个版本不可用，CMake将停止配置并出现错误。当这个属性被设置为`OFF`时，CMake将寻找下一个标准的最新版本，直到一个合适的标志。这意味着，首先查找`C++14`，然后是`C++11`，然后是`C++98`。（译者注：目前会从`C++20`或`C++17`开始查找）

**TIPS**:*如果语言标准是所有目标共享的全局属性，那么可以将`CMAKE_<LANG>_STANDARD`、`CMAKE_<LANG>_EXTENSIONS`和`CMAKE_<LANG>_STANDARD_REQUIRED`变量设置为相应的值。所有目标上的对应属性都将使用这些设置。*

**更多信息**

通过引入编译特性，CMake对语言标准提供了更精细的控制。这些是语言标准引入的特性，比如`C++11`中的可变参数模板和`Lambda`表达式，以及`C++14`中的自动返回类型推断。可以使用`target_compile_features()`命令要求为特定的目标提供特定的特性，CMake将自动为标准设置正确的编译器标志。也可以让CMake为可选编译器特性，生成兼容头文件。

**TIPS**:*我们建议阅读CMake在线文档，全面了解`cmake-compile-features`和如何处理编译特性和语言标准: https://cmake.org/cmake/help/latest/manual/cmake-compile-features.7.html 。*

## 1.10 使用控制流



# 第3章 检测外部库和程序

本章中主要内容有:

- 检测Python解释器
- 检测Python库
- 检测Python模块和包
- 检测BLAS和LAPACK数学库
- 检测OpenMP并行环境
- 检测MPI并行环境
- 检测Eigen库
- 检测Boost库
- 检测外部库:Ⅰ. 使用pkg-config
- 检测外部库:Ⅱ. 书写find模块

我们的项目常常会依赖于其他项目和库。本章将演示，如何检测外部库、框架和项目，以及如何链接到这些库。CMake有一组预打包模块，用于检测常用库和程序，例如：Python和Boost。可以使用`cmake --help-module-list`获得现有模块的列表。但是，不是所有的库和程序都包含在其中，有时必须自己编写检测脚本。本章将讨论相应的工具，了解CMake的`find`族命令:

- **find_file**：在相应路径下查找命名文件
- **find_library**：查找一个库文件
- **find_package**：从外部项目查找和加载设置
- **find_path**：查找包含指定文件的目录
- **find_program**：找到一个可执行程序

**NOTE**:*可以使用`--help-command`命令行显示CMake内置命令的打印文档。*

## 3.1 检测Python解释器

Python是一种非常流行的语言。许多项目用Python编写的工具，从而将主程序和库打包在一起，或者在配置或构建过程中使用Python脚本。这种情况下，确保运行时对Python解释器的依赖也需要得到满足。本示例将展示如何检测和使用Python解释器。

我们将介绍`find_package`命令，这个命令将贯穿本章。

**具体实施**

我们将逐步建立`CMakeLists.txt`文件:

1. 首先，定义CMake最低版本和项目名称。注意，这里不需要任何语言支持:

   ```
   cmake_minimum_required(VERSION 3.5 FATAL_ERROR)
   project(recipe-01 LANGUAGES NONE)
   ```

2. 然后，使用`find_package`命令找到Python解释器:

   ```
   find_package(PythonInterp REQUIRED)
   ```

3. 然后，执行Python命令并捕获它的输出和返回值:

   ```
   execute_process(
     COMMAND
         ${PYTHON_EXECUTABLE} "-c" "print('Hello, world!')"
     RESULT_VARIABLE _status
     OUTPUT_VARIABLE _hello_world
     ERROR_QUIET
     OUTPUT_STRIP_TRAILING_WHITESPACE
     )
   ```

4. 最后，打印Python命令的返回值和输出:

   ```
   message(STATUS "RESULT_VARIABLE is: ${_status}")
   message(STATUS "OUTPUT_VARIABLE is: ${_hello_world}")
   ```

5. 配置项目:

   ```
   $ mkdir -p build
   $ cd build
   $ cmake ..
   -- Found PythonInterp: /usr/bin/python (found version "3.6.5")
   -- RESULT_VARIABLE is: 0
   -- OUTPUT_VARIABLE is: Hello, world!
   -- Configuring done
   -- Generating done
   -- Build files have been written to: /home/user/cmake-cookbook/chapter-03/recipe-01/example/build
   ```

完整CMakeLists.txt如下：

```
cmake_minimum_required(VERSION 3.14 FATAL_ERROR)

project(recipe-01 LANGUAGES NONE)

set(CMAKE_CXX_STANDARD 20)

#find_package(PythonInterp REQUIRED)  # python
#find_package(PythonInterp 3.5 REQUIRED)  # -- Found PythonInterp: /usr/bin/python3 (found suitable version "3.8.5", minimum required is "3.5")
find_package(Python3 COMPONENTS Interpreter)  # python3

execute_process(
        COMMAND
        ${Python3_EXECUTABLE} "-c" "print('Hello, world!')"  #${Python3_EXECUTABLE}, 参考https://cmake.org/cmake/help/v3.12/module/FindPython3.html
        RESULT_VARIABLE _status
        OUTPUT_VARIABLE _output
        ERROR_QUIET
        OUTPUT_STRIP_TRAILING_WHITESPACE
)

message(STATUS "RESULT_VARIABLE is: ${_status}")
message(STATUS "OUTPUT_VARIABLE is: ${_output}")

# 输出：
ben@ben-virtual-machine:~/Softwares/JetBrains/CppProjects/TestProject/cmake-build-debug$ cmake ..
-- Found Python3: /usr/bin/python3.8 (found version "3.8.5") found components: Interpreter 
-- RESULT_VARIABLE is: 0
-- OUTPUT_VARIABLE is: Hello, world!
-- Configuring done
-- Generating done
-- Build files have been written to: /home/ben/Softwares/JetBrains/CppProjects/TestProject/cmake-build-debug
```

**工作原理**

`find_package`是用于发现和设置包的CMake模块的命令。这些模块包含CMake命令，用于标识系统标准位置中的包。CMake模块文件称为`Find<name>.cmake`，当调用`find_package(<name>)`时，模块中的命令将会运行。

除了在系统上实际查找包模块之外，查找模块还会设置了一些有用的变量，反映实际找到了什么，也可以在自己的`CMakeLists.txt`中使用这些变量。对于Python解释器，相关模块为`FindPythonInterp.cmake`附带的设置了一些CMake变量:

- **PYTHONINTERP_FOUND**：是否找到解释器
- **PYTHON_EXECUTABLE**：Python解释器到可执行文件的路径
- **PYTHON_VERSION_STRING**：Python解释器的完整版本信息
- **PYTHON_VERSION_MAJOR**：Python解释器的主要版本号
- **PYTHON_VERSION_MINOR** ：Python解释器的次要版本号
- **PYTHON_VERSION_PATCH**：Python解释器的补丁版本号

可以强制CMake，查找特定版本的包。例如，要求Python解释器的版本大于或等于2.7：`find_package(PythonInterp 2.7)`

可以强制满足依赖关系:

```
find_package(PythonInterp REQUIRED)
```

如果在查找位置中没有找到适合Python解释器的可执行文件，CMake将中止配置。

**TIPS**:*CMake有很多查找软件包的模块。我们建议在CMake在线文档中查询`Find<package>.cmake`模块，并在使用它们之前详细阅读它们的文档。`find_package`命令的文档可以参考 https://cmake.org/cmake/help/v3.5/command/find_package.html 。在线文档的一个很好的替代方法是浏览 https://github.com/Kitware/CMake/tree/master/Modules 中的CMake模块源代码——它们记录了模块使用的变量，以及模块可以在`CMakeLists.txt`中使用的变量。*

-------------

可以在CMake目录找到对应的cmake文件：

`/home/ben/Softwares/cmake-3.17.0-Linux-x86_64/share/cmake-3.17/Modules/FindPython3.cmake`

该cmake文件有说明会输出哪些变量：

```
...
This module will set the following variables in your project
(see :ref:`Standard Variable Names <CMake Developer Standard Variable Names>`):

``Python3_FOUND``
  System has the Python 3 requested components.
``Python3_Interpreter_FOUND``
  System has the Python 3 interpreter.
``Python3_EXECUTABLE``
  Path to the Python 3 interpreter.
``Python3_INTERPRETER_ID``
  A short string unique to the interpreter. Possible values include:
    * Python
    * ActivePython
    * Anaconda
    * Canopy
    * IronPython
...
```

-----------------

**更多信息**

软件包没有安装在标准位置时，CMake无法正确定位它们。用户可以使用CLI的`-D`参数传递相应的选项，告诉CMake查看特定的位置。Python解释器可以使用以下配置:

```
$ cmake -D PYTHON_EXECUTABLE=/custom/location/python ..
```

这将指定非标准`/custom/location/python`安装目录中的Python可执行文件。

**NOTE**:*每个包都是不同的，`Find<package>.cmake`模块试图提供统一的检测接口。当CMake无法找到模块包时，我们建议您阅读相应检测模块的文档，以了解如何正确地使用CMake模块。可以在终端中直接浏览文档，本例中可使用`cmake --help-module FindPythonInterp`查看。*

除了检测包之外，我们还想提到一个便于打印变量的helper模块。本示例中，我们使用了以下方法:

```
message(STATUS "RESULT_VARIABLE is: ${_status}")
message(STATUS "OUTPUT_VARIABLE is: ${_hello_world}")
```

使用以下工具进行调试:

```
include(CMakePrintHelpers)
cmake_print_variables(_status _output)
```

将产生以下输出:

```
-- _status="0" ; _output="Hello, world!"
```

有关打印属性和变量的更多信息，请参考 https://cmake.org/cmake/help/v3.5/module/CMakePrintHelpers.html 。

## 3.2 检测Python库

可以使用Python工具来分析和操作程序的输出。然而，还有更强大的方法可以将解释语言(如Python)与编译语言(如C或C++)组合在一起使用。一种是扩展Python，通过编译成共享库的C或C++模块在这些类型上提供新类型和新功能，这是第9章的主题。另一种是将Python解释器嵌入到C或C++程序中。两种方法都需要下列条件:

- Python解释器的工作版本
- Python头文件Python.h的可用性
- Python运行时库libpython

三个组件所使用的Python版本必须相同。我们已经演示了如何找到Python解释器；本示例中，我们将展示另外两种方式。

**准备工作**

我们将一个简单的Python代码，嵌入到C程序中，可以在Python文档页面上找到。源文件称为`hello-embedded-python.c`:

```
#include <Python.h>
int main(int argc, char *argv[]) {
  Py_SetProgramName(argv[0]); /* optional but recommended */
  Py_Initialize();
  PyRun_SimpleString("from time import time,ctime\n"
                     "print 'Today is',ctime(time())\n");
  Py_Finalize();
  return 0;
}
```

此代码将在程序中初始化Python解释器的实例，并使用Python的`time`模块，打印日期。

**NOTE**:*嵌入代码可以在Python文档页面的 https://docs.python.org/2/extending/embedding.html 和 https://docs.python.org/3/extending/embedding.html 中找到。*

## 具体实施

以下是`CMakeLists.txt`中的步骤:

1. 包含CMake最低版本、项目名称和所需语言:

   ```
   cmake_minimum_required(VERSION 3.5 FATAL_ERROR)
   project(recipe-02 LANGUAGES C)
   ```

2. 制使用C99标准，这不严格要求与Python链接，但有时你可能需要对Python进行连接:

   ```
   set(CMAKE_C_STANDARD 99)
   set(CMAKE_C_EXTENSIONS OFF)
   set(CMAKE_C_STANDARD_REQUIRED ON)
   ```

3. 找到Python解释器。这是一个`REQUIRED`依赖:

   ```
   find_package(PythonInterp REQUIRED)
   ```

4. 找到Python头文件和库的模块，称为`FindPythonLibs.cmake`:

   ```
   find_package(PythonLibs ${PYTHON_VERSION_MAJOR}.${PYTHON_VERSION_MINOR} EXACT REQUIRED)
   ```

5. 使用`hello-embedded-python.c`源文件，添加一个可执行目标:

   ```
   add_executable(hello-embedded-python hello-embedded-python.c)
   ```

6. 可执行文件包含`Python.h`头文件。因此，这个目标的`include`目录必须包含Python的`include`目录，可以通过`PYTHON_INCLUDE_DIRS`变量进行指定:

   ```
   target_include_directories(hello-embedded-python
     PRIVATE
         ${PYTHON_INCLUDE_DIRS}
       )
   ```

7. 最后，将可执行文件链接到Python库，通过`PYTHON_LIBRARIES`变量访问:

   ```
   target_link_libraries(hello-embedded-python
     PRIVATE
         ${PYTHON_LIBRARIES}
       )
   ```

8. 现在，进行构建:

   ```
   $ mkdir -p build
   $ cd build
   $ cmake ..
   ...
   -- Found PythonInterp: /usr/bin/python (found version "3.6.5")
   -- Found PythonLibs: /usr/lib/libpython3.6m.so (found suitable exact version "3.6.5")
   ```

9. 最后，执行构建，并运行可执行文件:

   ```
   $ cmake --build .
   $ ./hello-embedded-python
   Today is Thu Jun 7 22:26:02 2018
   ```

**工作原理**

`FindPythonLibs.cmake`模块将查找Python头文件和库的标准位置。由于，我们的项目需要这些依赖项，如果没有找到这些依赖项，将停止配置，并报出错误。

注意，我们显式地要求CMake检测安装的Python可执行文件。这是为了确保可执行文件、头文件和库都有一个匹配的版本。这对于不同版本，可能在运行时导致崩溃。我们通过`FindPythonInterp.cmake`中定义的`PYTHON_VERSION_MAJOR`和`PYTHON_VERSION_MINOR`来实现:

```
find_package(PythonInterp REQUIRED)
find_package(PythonLibs ${PYTHON_VERSION_MAJOR}.${PYTHON_VERSION_MINOR} EXACT REQUIRED)
```

使用`EXACT`关键字，限制CMake检测特定的版本，在本例中是匹配的相应Python版本的包括文件和库。我们可以使用`PYTHON_VERSION_STRING`变量，进行更接近的匹配:

```
find_package(PythonInterp REQUIRED)
find_package(PythonLibs ${PYTHON_VERSION_STRING} EXACT REQUIRED)
```

## 更多信息

当Python不在标准安装目录中，我们如何确定Python头文件和库的位置是正确的？对于Python解释器，可以通过CLI的`-D`选项传递`PYTHON_LIBRARY`和`PYTHON_INCLUDE_DIR`选项来强制CMake查找特定的目录。这些选项指定了以下内容:

- **PYTHON_LIBRARY**：指向Python库的路径
- **PYTHON_INCLUDE_DIR**：Python.h所在的路径

这样，就能获得所需的Python版本。

**TIPS**:*有时需要将`-D PYTHON_EXECUTABLE`、`-D PYTHON_LIBRARY`和`-D PYTHON_INCLUDE_DIR`传递给CMake CLI，以便找到及定位相应的版本的组件。*

要将Python解释器及其开发组件匹配为完全相同的版本可能非常困难，对于那些将它们安装在非标准位置或系统上安装了多个版本的情况尤其如此。CMake 3.12版本中增加了新的Python检测模块，旨在解决这个棘手的问题。我们`CMakeLists.txt`的检测部分也将简化为:

```
find_package(Python COMPONENTS Interpreter Development REQUIRED)
```

我们建议您阅读新模块的文档，地址是: https://cmake.org/cmake/help/v3.12/module/FindPython.html

## 3.3 检测Python模块和包



## [cmake教程(find_package使用)](https://cloud.tencent.com/developer/article/1338349)

下面以工程demo为示例, 项目目录结构如下：

```javascript
├── cmake
│   └── FindDEMOLIB.cmake
├── CMakeLists.txt
├── demo.cpp
├── demo.h
└── demoMain.cpp
```

其中demo.h和demo.cpp生成lib，demoMain.cpp链接对应的lib生成可执行文件

文件内容如下:

```c++
//=========================demo.h===================
#ifndef DEMO_H
#define DEMO_H

namespace demo {
    void printDemo();
}

#endif //RECIPE_02_DEMO_H



//======================demo.cpp==================
#include "demo.h"
#include <iostream>

namespace demo {
    void printDemo() {
        std::cout << "this is demo" << std::endl;
    }
}

//======================demoMain.cpp==================
#include "demo.h"

int main() {
    demo::printDemo();
    return 0;
}
```

首先我们使用demo.h和demo.cpp 生成静态lib，并安装：

```javascript
ben@ben-virtual-machine:~/Softwares/JetBrains/CppProjects/TestProject/cmake-build-debug$ make install
[ 50%] Built target demo_lib
[100%] Built target demoMain
Install the project...
-- Install configuration: ""
-- Installing: /home/ben/Softwares/JetBrains/CppProjects/TestProject/install/lib/libdemo_lib.a
-- Up-to-date: /home/ben/Softwares/JetBrains/CppProjects/TestProject/install/include/demo.h
```

FindDEMOLIB.cmake的内容如下（精简版本）：

```c++
message(STATUS "now using FindDEMOLIB.cmake find demo lib")

find_path(DEMOLIB_INCLUDE_DIR demo.h ${PROJECT_SOURCE_DIR}/install/include)
message(STATUS "./h dir ${DEMOLIB_INCLUDE_DIR}")

find_library(DEMOLIB_LIBRARY libdemo_lib.a ${PROJECT_SOURCE_DIR}/install/lib)
message(STATUS "lib dir: ${DEMOLIB_LIBRARY}")

if (DEMOLIB_INCLUDE_DIR AND DEMOLIB_LIBRARY)
    set(DEMOLIB_FOUND TRUE)
endif(DEMOLIB_INCLUDE_DIR AND DEMOLIB_LIBRARY)
```

主CMakeLists.txt内容如下：

```c++
cmake_minimum_required(VERSION 3.14 FATAL_ERROR)

project(demo LANGUAGES CXX)

set(SRC_LIB demo.cpp)
add_library(demo_lib STATIC ${SRC_LIB})

install(TARGETS demo_lib DESTINATION ${PROJECT_SOURCE_DIR}/install/lib)
install(FILES demo.h DESTINATION ${PROJECT_SOURCE_DIR}/install/include)

set(SRC_EXE demoMain.cpp)

list(APPEND CMAKE_MODULE_PATH ${PROJECT_SOURCE_DIR}/cmake)
message(STATUS "cmake_module_path: ${CMAKE_INCLUDE_PATH}")
find_package(DEMOLIB)

if (DEMOLIB_FOUND)
    add_executable(demoMain ${SRC_EXE})
    message(STATUS "found demo ${DEMOLIB_INCLUDE_DIR} ${DEMOLIB_LIBRARY}")
    include_directories(${DEMOLIB_INCLUDE_DIR})
    target_link_libraries(demoMain ${DEMOLIB_LIBRARY})
else()
    message(STATUS "not found DEMOLIB_FOUND")
endif(DEMOLIB_FOUND)
```

编译输出信息如下：

```
ben@ben-virtual-machine:~/Softwares/JetBrains/CppProjects/TestProject/cmake-build-debug$ cmake ..
-- cmake_module_path: 
-- now using FindDEMOLIB.cmake find demo lib
-- ./h dir /home/ben/Softwares/JetBrains/CppProjects/TestProject/install/include
-- lib dir: /home/ben/Softwares/JetBrains/CppProjects/TestProject/install/lib/libdemo_lib.a
-- found demo /home/ben/Softwares/JetBrains/CppProjects/TestProject/install/include /home/ben/Softwares/JetBrains/CppProjects/TestProject/install/lib/libdemo_lib.a
-- Configuring done
-- Generating done
-- Build files have been written to: /home/ben/Softwares/JetBrains/CppProjects/TestProject/cmake-build-debug
```

make：

```
ben@ben-virtual-machine:~/Softwares/JetBrains/CppProjects/TestProject/cmake-build-debug$ make
[ 50%] Built target demo_lib
[100%] Built target demoMain
```

运行：

```
ben@ben-virtual-machine:~/Softwares/JetBrains/CppProjects/TestProject/cmake-build-debug$ ./demoMain 
this is demo
```





