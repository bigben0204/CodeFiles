# [《Effective Modern C++》](https://www.kancloud.cn/kangdandan/book/169968)

# 简介

```c++
void someFunc(Widget w);        // someFunc的参数w是以值传送

Widget wid;                     // wid是个Widget的对象

someFunc(wid);                  // 在这个someFunc调用里面，w是通过
                                // 拷贝构造函数生成wid的一个拷贝

someFunc(std::move(wid));       // 在这个someFunc调用里面，w是通过
                                // 移动构造函数生成wid的一个拷贝
```

在一个函数调用里面，在函数的调用方的表达式是函数的实参。这些表达式被用来初始化函数的形参。在上面的代码中的第一次调用`someFunc`，实参是`wid`。在第二次调用的地方，实参是 `std::move(wid)`。两次调用的形参都是`w`。实参和形参的区别是很重要的，**因为形参只能是左值**，但是给他们初始化的实参即有可能是右值也有可能是左值。这和完美转发的过程是密切相关的，在完美转发中一个传递给一个函数的实参再传递给第二个函数，以此来保证原始的参数的右值特性或者左值特性被保留。（完美转发的细节在条款30中）。

本地试验看下例：

```c++
void f2(int&& i) {
    cout << i << endl;
}

int main() {
    int i = 10;
    i = 20;
    f2(i);  // 编译错误，error: cannot bind rvalue reference of type 'int&&' to lvalue of type 'int'

    int&& i2 = 10;  // 右值引用对象在声明后就是一个正常的左值类型对象
    i2 = 20;  // 可以对左值类型对象正常赋值，就像i = 20;一样
    f2(i2);  // 编译错误，error: cannot bind rvalue reference of type 'int&&' to lvalue of type 'int'

    f2(10);
    return 0;
}
```

**通过lambda表达式创造的函数对象通常称之为闭包（closure）**。通常很少区分lambda表达式和它产生的闭包，我通常用lambdas来代指它们。类似的，我很少区分函数模板（生成函数的模板）和模板函数（利用函数模板生成的函数）。对于类模板和模板类也是如此。

```c++
extern int x;                   // 对象声明

class Widgets;                  // class声明

bool func(const Widget& w);     // 函数声明

enum class Color;               // 被作用域包裹的enum声明（参考条款10）
```

我指一个**函数的签名是由函数的参数和返回值确定的**。函数和参数的名字并不是函数签名的一部分。在上述代码中，`func`的签名是`bool(const Widget&)`。函数声明的组成部分除了他的参数和返回值（比如如果有`noexcept`或者`constexpr`）都被排除在外。（`noexcept`和`constexpr`在条款14和条款15中被讨论）。正式的“签名”的定义和我的略有出入。但对于这本书来说，我的定义会非常有用。（正式的定义会排除返回值类型）。

我把**直接从new返回的原始指针叫做内建指针**。**一个原始指针的反义词就是智能指针**。智能指针通常重载了指针取值运算符（`operator->`和`operator*`），在条款20里面会解释`std::weak_ptr`是个特殊情况。

在源码注释里面，我通常把“构造函数”简称为ctor，“析构函数”简称为dtor。

# 第一章 类型推导

C++98只有一种类型推导规则：函数模板。C++11修改了一点规则样本，并且添加额外的两条规则，一条是`auto`，另一个是`decltype`。C++14继续扩展了`auto`和`decltype`的使用情况。

在类型推导过程中有太多的上下文判断，在大多数情况，`auto`出现在调用函数模板时，在`decltype`表达式里面，和在C++14中，神秘的`decltype(auto)`构造。

## 条款一：理解模板类型推导

一段函数模板看起来会是这样：

```c++
template<typename T>
void f(ParamType param);
```

`T`的类型不仅和`expr`的类型独立，而且还和`ParamType`的形式独立。下面是三个例子：

- `ParamType`是一个指针或者是一个引用类型，但并不是一个通用的引用类型（通用的引用类型的内容在条款24。此时，你要知道例外情况会出现的，他们的类型并不和左值应用或者右值引用）。
- `ParamType`是一个通用的引用
- `ParamType`既不是指针也不是引用

这样的话，我们就有了三种类型需要检查的类型推导场景。每一种都是基于我们队模板的通用的调用封装：

```c++
template<typename T>
void f(ParamType param);

f(expr);					// 从expr推导出T和ParamType的类型
```

### **第一种情况**：`ParamType`是个非通用的引用或者是一个指针

最简单的情况是**当`ParamType`是一个引用类型或者是一个指针**，但并非是通用的引用。在这种情况下，类型推导的过程如下：

1. **如果`expr`的类型是个引用，忽略引用的部分**。（或如果expr的类型是个指针，忽略指针的部分。）
2. 然后利用`expr`的类型和`ParamType`对比去判断`T`的类型。

举一个例子，如果这个是我们的模板，

```c++
template<typename T>
void f(T& param);           // param是一个引用类型
```

我们有这样的代码变量声明：

```c++
int x = 27;                 // x是一个int
const int cx = x;           // cx是一个const int
const int& rx = x;          // rx是const int的引用
```

`param`和`T`在不同的调用下面的类型推导如下：

```c++
f(x);                       // T是int，param的类型时int&
f(cx);                      // T是const int，
                            // param的类型是const int&
f(rx);                      // T是const int
                            // param的类型时const int&
```

本地试验，和期望不太一致：

```c++
#include <iostream>

using namespace std;

template<typename T>
void f(T& param) {
    cout << "is const: " << is_const<decltype(param)>::value << endl;
    cout << "is_reference: " << is_reference<decltype(param)>::value << endl;
}

int main() {
    cout << boolalpha;

    int x = 27;
    const int cx = x;
    const int& rx = x;
    f(x);
    f(cx);
    f(rx);

    // cout << is_const<decltype(cx)>::value << endl;  // true
    // cout << is_const<decltype(rx)>::value << endl;  // false
    return 0;
}
// 输出：
is const: false
is_reference: true
is const: false
is_reference: true
is const: false
is_reference: true
```

在第二和第三部分的调用，注意`cx`和`rx`由于被指定为`const`类型变量，`T`被推导成`const int`，这也就导致了参数的类型被推导为`const int&`。这对调用者非常重要。当传递一个`const`对象给一个引用参数，他们期望对象会保留常量特性，也就是说，参数变成了`const`的引用。这也就是为什么给一个以`T&`为参数的模板传递一个`const`对象是安全的：**对象的`const`特性是`T`类型推导的一部分**。

在第三个例子中，注意尽管`rx`的类型是一个引用，`T`仍然被推导成了一个非引用的。这是因为`rx`的引用特性会被类型推导所忽略。

这些例子展示了左值引用参数的处理方式，但是类型推导在右值引用上也是如此。当然，**右值参数只可能传递给右值引用参数**，但是这个限制和类型推导没有关系。

`f(10); // 编译错误，error: cannot bind non-const lvalue reference of type 'int&' to an rvalue of type 'int'`

如果我们把`f`的参数类型从`T&`变成`const T&`，情况就会发生变化，但是并不会令人惊讶。由于`param`的声明是`const`引用的，`cx`和`rx`的`const`特性会被保留，这样的话`T`的`const`特性就没有必要了。

```c++
template<typename T>
void f(const T& param);     // param现在是const的引用

int x = 27;                 // 和之前一样
const int cx = x;           // 和之前一样
const int& rx = x;          // 和之前一样

f(x);                       // T是int，param的类型是const int&
f(cx);                      // T是int，param的类型是const int&
f(rx);                      // T是int，param的类型是const int&
```

和之前一样，`rx`的引用特性在类型推导的过程中会被忽略。

```c++
template<typename T>
void f(T* param);           // param是一个指针

int x = 27;                 // 和之前一样
const int *px = &x;         // px是一个指向const int x的指针
const int* const cpx = &x;  // cpx是一个指向const int x的指针，且cpx不能修改

f(&x);                      // T是int，param的类型是int*

f(px);                      // T是const int
                            // param的类型是const int*
f(cpx);						// param的类型还是const int*，原cpx的常量性不会带到类型推导里
```

本地试验：

```c++
int main() {
    int x = 27;
    const int* px = &x;  // 指针所指内容不能修改，指针地址可以修改
    *px = 28;  // 编译错误，指针所指内容不能修改，error: assignment of read-only location '* px'

    int y = 30;
    px = &y;

    const int* const p = &x;  // 指针所指内容不能修改，指针地址也不能修改
    p = &y;  // 编译错误，指针地址不能修改，error: assignment of read-only variable 'p'
    return 0;
}
```

再试验：

```c++
#include <iostream>

using namespace std;

template<typename T>
void f(T* param) {
    *param = 28;
    int i = 10;
    param = &i;
}

int main() {
    int x = 27;
    const int* px = &x;
    f(&x);
    f(px);  // 编译错误，*param = 28; error: assignment of read-only location '* param'

    int* const cpx = &x;
    f(cpx);  // 可以正常编译，可以在f中对param内存内容重新赋值，也可以对param重新赋值，说明param是int*
    return 0;
}
```

C++在引用和指针上的类型推导法则是如此的自然。

### **第二种情况**：`ParamType`是个通用的引用（Universal Reference）

对于通用的引用参数，情况就变得不是那么明显了。这些参数被声明成右值引用（也就是函数模板使用一个类型参数`T`，一个通用的引用参数的申明类型是`T&&`），但是当传递进去右值参数情况变得不一样。完整的讨论请参考条款24，这里是先行版本。

- **如果`expr`是一个左值**，**`T`和`ParamType`都会被推导成左值引用**。这有些不同寻常。第一，这是模板类型`T`被推导成一个引用的唯一情况。第二，尽管`ParamType`利用右值引用的语法来进行推导，但是他最终推导出来的类型是左值引用。
- 如果`expr`是一个右值，那么就执行“普通”的法则（第一种情况）

举个例子：

```c++
template<typename T>
void f(T&& param);			// param现在是一个通用的引用

int x = 27;                 // 和之前一样
const int cx = x;           // 和之前一样
const int& rx = x;          // 和之前一样

f(x);						// x是左值，所以T是int&
							// param的类型也是int&

f(cx);						// cx是左值，所以T是const int&
							// param的类型也是const int&

f(rx);						// rx是左值，所以T是const int&
							// param的类型也是const int&

f(27);						// 27是右值，所以T是int
							// 所以param的类型是int&&

// 与模板通用引用&&类型推导不同的是非模板函数的右值引用形参类型：
void f2(int&& num);
f2(27);  // 可以编译通过，27是右值
f2(x);  // 编译失败，x是左值，无法绑定到右值引用入参
```

条款23解释了这个例子推导的原因。关键的地方在于通用引用的类型推导法则和左值引用或者右值引用的法则大不相同。特殊的情况下，当使用了通用的引用，左值参数和右值参数的类型推导大不相同。这在非通用的类型推到上面绝对不会发生。

### 第三种情况：`ParamType`既不是指针也不是引用

当`ParamType`既不是指针也不是引用，我们把它处理成pass-by-value：

```c++
template<typename T>
void f(T param);			// param现在是pass-by-value
```

这就意味着`param`就是完全传给他的参数的一份拷贝——一个完全新的对象。基于这个事实可以从`expr`给出推导的法则：

1. 和之前一样，如果`expr`的类型是个引用，将会忽略引用的部分。
2. 如果在忽略`expr`的引用特性，`expr`是个`const`的，也要忽略掉`const`。如果是`volatile`，照样也要忽略掉（`volatile`对象并不常见。它们常常被用在实现设备驱动上面。查看更多的细节，请参考条款40。）

这样的话：

```c++
int x = 27;                 // 和之前一样
const int cx = x;           // 和之前一样
const int& rx = x;          // 和之前一样

f(x);                       // T和param的类型都是int
f(cx);                      // T和param的类型也都是int
f(rx);                      // T和param的类型还都是int
```

注意尽管`cx`和`rx`都是`const`类型，`param`却不是`const`的。这是有道理的。`param`是一个和`cx`和`rx`独立的对象——一个`cx`和`rx`的拷贝。`cx`和`rx`不能被修改和`param`能不能被修改是没有关系的。这就是为什么`expr`的常量特性（或者是易变性）（在很多的C++书籍上面`const`特性和`volatile`特性被称之为CV特性——译者注）在推导`param`的类型的时候被忽略掉了：`expr`不能被修改并不意味着它的一份拷贝不能被修改。

认识到`const`（和`volatile`）在按值传递参数的时候会被忽略掉。正如我们所见，引用的`const`或者是指针指向`const`，`expr`的`const`特性在类型推导的过程中会被保留。但是考虑到`expr`是一个`const`的指针指向一个`const`对象，而且`expr`被通过按值传递传递给`param`：

```c++
template<typename T>
void f(T param);            // param仍然是按值传递的（pass by value）

const char* const ptr =     // ptr是一个const指针，指向一个const对象
  "Fun with pointers";

f(ptr);                     // 给参数传递的是一个const char * const类型
```

这里，位于星号右边的`const`是表明指针是常量`const`的：`ptr`不能被修改指向另外一个不同的地址，并且也不能置成`null`。（星号左边的`const`表明`ptr`指向的——字符串——是`const`的，也就是说字符串不能被修改。）当这个`ptr`传递给`f`，组成这个指针的内存bit被拷贝给`param`。这样的话，指针自己（`ptr`）本身是被按值传递的。按照按值传递的类型推导法则，`ptr`的`const`特性会被忽略，这样`param`的推导出来的类型就是`const char*`，也就是一个可以被修改的指针，指向一个`const`的字符串。**`ptr`指向的东西的`const`特性被加以保留，但是`ptr`自己本身的`const`特性会被忽略**，因为它要被重新复制一份而创建了一个新的指针`param`。

数组参数

这主要出现在mainstream的模板类型推导里面，但是有一种情况需要特别加以注意。就是数组类型和指针类型是不一样的，尽管它们通常看起来是可以替换的。一个最基本的幻觉就是在很多的情况下，一个数组会被退化成一个指向其第一个元素的指针。这个退化的代码常常如此：

```
const char name[] = "J. P. Briggs";     // name的类型是const char[13]

const char * ptrToName = name;          // 数组被退化成指针
```

在这里，`const char*`指针`ptrToName`使用`name`初始化，实际的`name`的类型是`const char[13]`。这些类型（`const char*`和`const char[13]`）是不一样的，但是因为数组到指针的退化规则，代码会被正常编译。

但是如果一个数组传递给一个安置传递的模板参数里面情况会如何？会发生什么呢？

```
template<typename T>
void f(T param);            // 模板拥有一个按值传递的参数

f(name);                    // T和param的类型会被推到成什么呢？
```

我们从一个没有模板参数的函数开始。是的，是的，语法是合法的，

```
void myFunc(int param[]);       // 和上面的函数相同
```

但是以数组声明，但是还是把它当成一个指针声明，也就是说`myFunc`可以和下面的声明等价：

```
void myFunc(int* param);        // 和上面的函数是一样的
```

这样的数组和指针等价的声明经常会在以C语言为基础的C++里面出现，这也就导致了数组和指针是等价的错觉。

因为数组参数声明会被当做指针参数，传递给模板函数的按值传递的数组参数会被退化成指针类型。这就意味着在模板`f`的调用中，模板参数`T`被推导成`const char*`：

```
f(name);                    // name是个数组，但是T被推导成const char*
```

但是来一个特例。尽管函数不能被真正的定义成参数为数组，但是可以声明参数是数组的引用！所以如果我们修改模板`f`的参数成引用，

```
template<typename T>
void f(T& param);           // 引用参数的模板
```

然后传一个数组给他

```
f(name);                    // 传递数组给f
```

`T`最后推导出来的实际的类型就是数组！类型推导包括了数组的长度，所以在这个例子里面，`T`被推导成了`const char [13]`，函数`f`的参数（数组的引用）被推导成了`const char (&)[13]`。是的，语法看起来怪怪的，但是理解了这些可以升华你的精神（原文knowing it will score you mondo points with those few souls who care涉及到了几个宗教词汇——译者注）。

本地试验：

```c++
#include <iostream>

using namespace std;

template<typename T>
void f1(T param) {
    cout << sizeof(param) << endl;
}

template<typename T>
void f2(T& param) {
    cout << sizeof(param) << endl;
}

int main() {
    const char name[] = "Hello, J. P. Briggs";
    f1(name);  // T推导为const char*
    f2(name);  // T推导为const char (&)[20]

    const char* p1 = "abc";
    cout << sizeof(p1) << endl;  // 指针大小：8

    const char p2[] = "abc";  // 字符总字节数：4
    cout << sizeof(p2) << endl;
    return 0;
}
// 输出：
8
20
8
4
```

有趣的是，声明数组的引用可以使的创造出一个推导出一个数组包含的元素长度的模板：

```c++
// 在编译的时候返回数组的长度（数组参数没有名字，
// 因为只关心数组包含的元素的个数）
template<typename T, std::size_t N>
constexpr std::size_t arraySize(T (&)[N]) noexcept
{
    return N;                   // constexpr和noexcept在随后的条款中介绍
}

int main() {
    int nums[10] = {1, 2, 3};
    int num2[arraySize(nums)] = {};  // 数据长度需要为编译期常量，声明长度为10的数组
    cout << sizeof(num2) << endl;  // 40
    return 0;
}
```

（`constexpr`是一种比`const`更加严格的常量定义，`noexcept`是说明函数永远都不会抛出异常——译者注）

正如条款15所述，定义为`constexpr`说明函数可以在编译的时候得到其返回值。这就使得创建一个和一个数组长度相同的一个数组，其长度可以从括号初始化：

```c++
int keyVals[] = { 1, 3, 7, 9, 11, 22, 35 };     // keyVals有7个元素
int mappedVals[arraySize(keyVals)];             // mappedVals长度也是7
```

当然，作为一个现代的C++开发者，应该优先选择内建的`std::array`：

```c++
std::array<int, arraySize(keyVals)> mappedVals; // mappedVals长度是7
```

由于`arraySize`被声明称`noexcept`，这会帮助编译器生成更加优化的代码。可以从条款14查看更多详情。

本地试验array：<https://blog.csdn.net/fengbingchun/article/details/72809699>

```c++
#include <iostream>
#include <array>
#include <iterator>
#include <algorithm>

int main() {
    constexpr int i = 10;
    // 必须常量值初始化
    array<int, i> intArray;  // 元素值未定义
    array<int, i> intArray2{intArray};  // 使用另一个array对象初始化，则必须类型和大小都相等

    array<string, 10> strArray;  // 默认构造生成元素对象
    strArray.fill("hello");  // 填充值
    
    array<string, 10> strArray1{"hello", "world", "nice", "day"};
    copy(strArray1.begin(), strArray1.end(), ostream_iterator<string>(cout, " "));  // hello world nice day       
    cout << endl;
    reverse_copy(strArray1.begin(), strArray1.end(), ostream_iterator<string>(cout, " "));  //       day nice world hello 
    return 0;
}
```

数组并不是C++唯一可以退化成指针的东西。函数类型可以被退化成函数指针，和我们之前讨论的数组的推导类似，函数可以被推导成函数指针：

```c++
void someFunc(int， double);    // someFunc是一个函数
                                // 类型是void(int, double)

template<typename T>
void f1(T param);               // 在f1中 参数直接按值传递

template<typename T>
void f2(T& param);              // 在f2中 参数是按照引用传递

f1(someFunc);                   // param被推导成函数指针
                                // 类型是void(*)(int, double)

f2(someFunc);                   // param被推导成函数指针
                                // 类型时void(&)(int, double)
```

这在实践中极少有不同，如果你知道数组到指针的退化，或许你也就会就知道函数到函数指针的退化。

所以你现在知道如下：`auto`相关的模板推导法则。我把最重要的部分单独在下面列出来。在通用引用中对待左值的处理有一点混乱，但是数组退化成指针和函数退化成函数指针的做法更加混乱呢。有时候你要对你的编译器和需求大吼一声，“告诉我到底类型推导成啥了啊！”当这种情况发生的时候，去参考条款4，因为它致力于让编译器告诉你是如何处理的。

| 要记住的东西                                                 |
| :----------------------------------------------------------- |
| 在模板类型推导的时候，有引用特性的参数的引用特性会被忽略     |
| 在推导通用引用参数的时候，左值会被特殊处理                   |
| 在推导按值传递的参数时候，`const`和/或`volatile`参数会被视为非`const`和非`volatile` |
| 在模板类型推导的时候，参数如果是数组或者函数名称，他们会被退化成指针，除非是用在初始化引用类型 |

## 条款二：理解`auto`类型推导

如果你已经阅读了条款1关于模板相关的类型推导，你就已经知道了机会所有关于`auto`的类型推导，因为除了一个例外，`auto`类型推导就是模板类型推导。

模板类型推导和`auto`类型推导是有一个直接的映射。有一个书面上的从一种情况转换成另外一种情况的算法。

在条款1，模板类型推导是使用下面的通用模板函数来解释的：

```
template<typename T>
void f(ParamType param);
```

在这里通常调用：

```c++
f(expr);                    // 使用一些表达式来当做调用f的参数
```

在调用`f`的地方，编译器使用`expr`来推导`T`和`ParamType`的类型。

当一个变量被声明为`auto`，`auto`相当于模板中的`T`，而对变量做的相关的类型限定就像`ParamType`。这用代码说明比直接解释更加容易理解，所以看下面的这个例子：

```
auto x = 27;
```

这里，对`x`的类型定义就仅仅是`auto`本身。从另一方面，在这个声明中：

```
const auto cx = x;
```

类型被声明成`const auto`，在这儿：

```
const auto& rx = x;
```

类型被声明称`const auto&`。在这些例子中推导`x`，`cx`，`rx`的类型的时候，编译器处理每个声明的时候就和处理对应的表达式初始化的模板：

```c++
template<typename T>                // 推导x的类型的
void func_for_x(T param);           // 概念上的模板

func_for_x(27);                     // 概念上的调用：
                                    // param的类型就是x的类型

template<typename T>
void func_for_cx(const T param);    // 推导cx的概念上的模板

func_for_cx(x);                     // 概念调用：param的推导类型就是cx的类型

template<typename T>
void func_for_rx(const T& param);   // 推导rx概念上的模板

func_for_rx(x);                     // 概念调用：param的推导类型就是rx的类型
```

正如我所说，对`auto`的类型推导只存在一种情况的例外（这个后面就会讨论），其他的就和模板类型推导完全一样了。

正如我所说，对`auto`的类型推导只存在一种情况的例外（这个后面就会讨论），其他的就和模板类型推导完全一样了。

条款1把模板类型推导划分成三部分，基于在通用的函数模板的`ParamType`的特性和`param`的类型声明。在一个用`auto`声明的变量上，类型声明代替了`ParamType`的作用，所以也有三种情况：

- 情况1：类型声明是一个指针或者是一个引用，但不是一个通用的引用
- 情况2：类型声明是一个通用引用
- 情况3：类型声明既不是一个指针也不是一个引用

我们已经看了情况1和情况3的例子：

```c++
auto x = 27;                        // 情况3（x既不是指针也不是引用）
const auto cx = x;                  // 情况3（cx二者都不是）
const auto& rx = x;                 // 情况1（rx是一个非通用的引用）
```

情况2正如你期待的那样：

```c++
auto&& uref1 = x;                   // x是int并且是左值
                                    // 所以uref1的类型是int&
auto&& uref2 = cx;                  // cx是int并且是左值
                                    // 所以uref2的类型是const int&
auto&& uref3 = 27;                  // 27是int并且是右值
                                    // 所以uref3的类型是int&&
```

条款1讲解了在非引用类型声明里，数组和函数名称如何退化成指针。这在`auto`类型推导上面也是一样：

```c++
const char name[] =                 // name的类型是const char[13] 
    "R. N. Briggs";

auto arr1 = name;                   // arr1的类型是const char*

auto& arr2 = name;                  // arr2的类型是const char (&)[13]

void someFunc(int, double);         // someFunc是一个函数，类型是
                                    // void (*)(int, double)

auto& func2 = someFunc;             // func1的类型是
                                    // void (&)(int, double)
```

正如你所见，`auto`类型推导和模板类型推导工作很类似。它们就像一枚硬币的两面。

除了有一种情况是不一样的。我们从如果你想声明一个用27初始化的`int`， C++98你有两种语法选择：

```c++
int x1 = 27;
int x2(27);
```

C++11，通过标准支持的统一初始化（使用花括号初始化——译者注），可以添加下面的代码：

```c++
int x3 = { 27 };
int x4{ 27 };
```

综上四种语法，都会生成一种结果：一个拥有27数值的`int`。

但是正如条款5所解释的，使用`auto`来声明变量比使用固定的类型更好，所以在上述的声明中把`int`换成`auto`更好。最直白的写法就如下面的代码：

```c++
auto x1 = 27;
auto x2(27);
auto x3 = {27};
auto x4{ 27 };
```

上面的所有声明都可以编译，但是他们和被替换的相对应的语句的意义并不一样。头两个的确是一样的，声明一个初始化值为27的`int`。然而后面两个，声明了一个类型为`std::intializer_list`的变量，这个变量包含了一个单一的元素27！

```c++
auto x1 = 27;                       // 类型时int，值是27

auto x2(27);                        // 同上

auto x3 = { 27 };                   // 类型是std::intializer_list<int>
                                    // 值是{ 27 }

auto x4{ 27 };                      // 同上
```

这和`auto`的一种特殊类型推导有关系。当使用一对花括号来初始化一个`auto`类型的变量的时候，推导的类型是`std::intializer_list`。如果这种类型无法被推导（比如在花括号中的变量拥有不同的类型），代码会编译错误。

```c++
auto x5 = { 1, 2, 3.0 };            // 错误！ 不能讲T推导成
                                    // std::intializer_list<T>
```

正如注释中所说的，在这种情况，类型推导会失败，但是认识到这里实际上是有两种类型推导是非常重要的。一种是`auto: x5`的类型被推导。因为`x5`的初始化是在花括号里面，`x5`必须被推导成`std::intializer_list`。但是`std::intializer_list`是一个模板。实例是对一些`T`实例化成`std::intializer_list`，这就意味着`T`的类型必须被推导出来。类型推导就在第二种的推导的范围上失败了。在这个例子中，类型推导失败是因为在花括号里面的数值并不是单一类型的。

对待花括号初始化的行为是`auto`唯一和模板类型推导不一样的地方。当`auto`声明变量被使用一对花括号初始化，推导的类型是`std::intializer_list`的一个实例。但是如果相同的初始化递给相同的模板，类型推导会失败，代码不能编译。

```c++
auto x = { 11, 23, 9 };             // x的类型是
                                    // std::initializer_list<int>

template<typename T>                // 和x的声明等价的
void f(T param);                    // 模板

f({ 11, 23, 9 });                   // 错误的！没办法推导T的类型
```

但是，如果你明确模板的`param`的类型是一个不知道`T`类型的`std::initializer_list`：

```c++
template<typename T>
void f(std::initializer_list<T> initList);

f({ 11, 23, 9 });                   // T被推导成int，initList的
                                    // 类型是std::initializer_list<int>
```

所以`auto`和模板类型推导的本质区别就是`auto`假设花括号初始化代表的是std::initializer_list，但是模板类型推导却不是。

你可能对为什么`auto`类型推导有一个对花括号初始化有一个特殊的规则而模板的类型推导却没有感兴趣。我自己也非常奇怪。可是我一直没有能够找到一个有力的解释。但是法则就是法则，这就意味着你必须记住如果使用`auto`声明一个变量并且使用花括号来初始化它，类型推导的就是`std::initializer_list`。你必须习惯这种花括号的初始化哲学——使用花括号里面的数值来初始化是理所当然的。在C++11编程里面的一个经典的错误就是误被声明成`std::initializer_list`，而其实你是想声明另外的一种类型。这个陷阱使得一些开发者仅仅在必要的时候才会在初始化数值周围加上花括号。（什么时候是必要的会在条款7里面讨论。）

对于C++11，这是一个完整的故事，但是对于C++14来说，故事还要继续。C++14允许`auto`表示推导的函数返回值（参看条款3），而且C++14的lambda可能会在参数声明里面使用`auto`。但是，这里面的使用是复用了模板的类型推导，而不是`auto`的类型推导。所以一个使用`auto`声明的返回值的函数，返回一个花括号初始化就无法编译。

```c++
auto createInitList()
{
    return { 1, 2, 3 };             // 编译错误：不能推导出{ 1, 2, 3 }的类型
}
```

在C++14的lambda里面，当`auto`用在参数类型声明的时候也是如此：

```c++
std::vector<int> v;
auto resetV = [&v](const auto& newValue) { v = newValue; }    // C++14
resetV({ 1, 2, 3 });                // 编译错误，不能推导出{ 1, 2, 3 }的类型
```

| 要记住的东西                                                 |
| :----------------------------------------------------------- |
| `auto`类型推导通常和模板类型推导类似，但是`auto`类型推导假定花括号初始化代表的类型是`std::initializer_list`，但是模板类型推导却不是这样 |
| `auto`在函数返回值或者lambda参数里面执行模板的类型推导，而不是通常意义的`auto`类型推导 |

## 条款三：理解`decltype`





























