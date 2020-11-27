# [CMake Cookbook](https://www.bookstack.cn/read/CMake-Cookbook/content-preface-preface-chinese.md)

源码下载<https://github.com/dev-cafe/cmake-cookbook>

## 第1章 从可执行文件到库

### [1.1 将单个源文件编译为可执行文件](https://www.bookstack.cn/read/CMake-Cookbook/content-chapter1-1.1-chinese.md)

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

### [1.2 切换生成器](https://www.bookstack.cn/read/CMake-Cookbook/content-chapter1-1.2-chinese.md)

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

### [1.3 构建和链接静态库和动态库](https://www.bookstack.cn/read/CMake-Cookbook/content-chapter1-1.3-chinese.md)

**NOTE**: *这个示例代码可以在 https://github.com/dev-cafe/cmake-cookbook/tree/v1.0/chapter-01/recipe-03 找到，其中有C++和Fortran示例。该配置在CMake 3.5版(或更高版本)测试有效的，并且已经在GNU/Linux、macOS和Windows上进行了测试。*

项目中会有单个源文件构建的多个可执行文件的可能。项目中有多个源文件，通常分布在不同子目录中。这种实践有助于项目的源代码结构，而且支持模块化、代码重用和关注点分离。同时，这种分离可以简化并加速项目的重新编译。本示例中，我们将展示如何将源代码编译到库中，以及如何链接这些库。

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

### 1.4 用条件句控制编译

































































