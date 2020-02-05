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

## 条款1：理解模板类型推导

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

## 条款2：理解`auto`类型推导

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
auto x5 = { 1, 2, 3.0 };            // 错误！ 不能将T推导成
                                    // std::intializer_list<T>
```

正如注释中所说的，在这种情况，类型推导会失败，但是认识到这里实际上是有两种类型推导是非常重要的。一种是`auto: x5`的类型被推导。因为`x5`的初始化是在花括号里面，`x5`必须被推导成`std::intializer_list`。但是`std::intializer_list`是一个模板。实例是对一些`T`实例化成`std::intializer_list`，这就意味着`T`的类型必须被推导出来。类型推导就在第二种的推导的范围上失败了。在这个例子中，类型推导失败是因为在花括号里面的数值并不是单一类型的。

**对待花括号初始化的行为是`auto`唯一和模板类型推导不一样的地方**。当`auto`声明变量被使用一对花括号初始化，推导的类型是`std::intializer_list`的一个实例。但是如果相同的初始化递给相同的模板，类型推导会失败，代码不能编译。

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

## 条款3：理解`decltype`

`decltype`是一个怪异的发明。给定一个变量名或者表达式，`decltype`会告诉你这个变量名或表达式的类型。`decltype`的返回的类型往往也是你期望的。然而有时候，它提供的结果会使开发者极度抓狂而不得参考其他文献或者在线的Q&A网站。

我们从在典型的情况开始讨论，这种情况下`decltype`不会有令人惊讶的行为。与`templates`和`auto`在类型推导中行为相比（请见条款一和条款二），`decltype`一般只是复述一遍你所给他的变量名或者表达式的类型，如下：

```c++
   const int i = 0;            // decltype(i) is const int

   bool f(const Widget& w);    // decltype(w) is const Widget&
                               // decltype(f) is bool(const Widget&)
   struct Point{
     int x, y;                 // decltype(Point::x) is int，没见过这种用法：decltype(Point::x) i = 10;
   };

   Widget w;                   // decltype(w) is Widget

   if (f(w)) ...               // decltype(f(w)) is bool

   template<typename T>        // simplified version of std::vector
   class vector {
   public:
     ...
     T& operator[](std::size_t index);
     ...
   };

   vector<int> v;              // decltype(v) is vector<int>
   ...
   if(v[0] == 0)               // decltype(v[0]) is int&
```

毫无令人惊讶的地方。

在C++11中，`decltype`最主要的用处可能就是用来声明一个函数模板，在这个函数模板中返回值的类型取决于参数的类型。举个例子，假设我们想写一个函数，这个函数中接受一个支持方括号索引（也就是"[]"）的容器作为参数，验证用户的合法性后返回索引结果。这个函数的返回值类型应该和索引操作的返回值类型是一样的。

操作子`[]`作用在一个对象类型为`T`的容器上得到的返回值类型为`T&`。对`std::deque`一般是成立的，例如，对`std::vector`，这个几乎是处处成立的。然而，**对`std::vector`，`[]`操作子不是返回`bool&`，而是返回一个全新的对象**。发生这种情况的原理将在条款六中讨论，对于**此处重要的是容器的`[]`操作返回的类型是取决于容器的**。

`decltype`使得这种情况很容易来表达。下面是一个模板程序的部分，展示了如何使用`decltype`来求返回值类型。这个模板需要改进一下，但是我们先推迟一下：

```c++
#include <iostream>
#include <vector>
#include <cassert>

using namespace std;

// works, but requires refinements（改进）
template<typename Container, typename Index>
auto authAndAccess(Container& c, Index i) -> decltype(c[i]) {
    assert(i < c.size());
    return c[i];
}

int main() {
    vector<int> ints = {1, 2, 3};
    // 这里返回的类型是int&，如果定义int&&，则编译报错：error: cannot bind rvalue reference of type 'int&&' to lvalue of type '__gnu_cxx::__alloc_traits<std::allocator<int>, int>::value_type' {aka 'int'}
    int& i = authAndAccess(ints, 2);  // 3
    cout << i << endl;

    vector<bool> bools = {true, false, true};
    // 这里返回类型是bool，可以定义类型为bool或bool&&或const bool&，如果定义bool&，则编译报错：error: cannot bind non-const lvalue reference of type 'bool&' to an rvalue of type 'bool'
    bool b = authAndAccess(bools, 2);
    cout << boolalpha << b << endl;  // true
    return 0;
}

```

将`auto`用在函数名之前和类型推导是没有关系的。更精确地讲，此处使用了`C++11`的尾随返回类型技术，即函数的返回值类型在函数参数之后声明(“->”后边)。尾随返回类型的一个优势是在定义返回值类型的时候使用函数参数。例如在函数`authAndAccess`中，我们使用了`c`和`i`定义返回值类型。在传统的方式下，我们在函数名前面声明返回值类型，`c`和`i`是得不到的，因为此时`c`和`i`还没被声明。

使用这种类型的声明，`authAndAccess`的返回值就是`[]`操作子的返回值，这正是我们所期望的。

`C++11`允许单语句的`lambda`表达式的返回类型被推导，在`C++14`中之中行为被拓展到包括多语句的所有的`lambda`表达式和函数。在上面`authAndAccess`中，意味着在`C++14`中我们可以忽略尾随返回类型，仅仅保留开头的`auto`。使用这种形式的声明，
意味着将会使用类型推导。特别注意的是，编译器将从函数的实现来推导这个函数的返回类型：

```c++
template<typename Container, typename Index>         // C++14;
auto authAndAccess(Container &c, Index i)            // not quite
{                                                    // correct
    authenticateUser();
    return c[i];
}                                 // return type deduced from c[i]

int& i = authAndAccess(ints, 2);  // 上例编译失败：error: cannot bind non-const lvalue reference of type 'int&' to an rvalue of type 'int'。相当于把int&类型对象赋给template<typename T> void f(T param);的模板函数，则T推导出的类型为int。
```

条款二解释说，对使用`auto`来表明函数返回类型的情况，编译器使用模板类型推导。但是这样是回产生问题的。正如我们所讨论的，对绝大部分对象类型为`T`的容器，`[]`操作子返回的类型是`T&`, 然而条款一提到，在模板类型推导的过程中,初始表达式的引用会被忽略。思考这对下面代码意味着什么：

```c++
std::deque<int> d;
...
authAndAccess(d, 5) = 10;       // authenticate user, return d[5],
                                // then assign 10 to it;
                                // this won't compile! error: lvalue required as left operand of assignment
```

此处，`d[5]`返回的是`int&`，但是`authAndAccess`的`auto`返回类型声明将会剥离这个引用，从而得到的返回类型是`int`。`int`作为一个右值成为真正的函数返回类型。上面的代码尝试给一个右值`int`赋值为10。这种行为是在`C++`中被禁止的，所以代码无法编译通过。

为了让`authAndAccess`按照我们的预期工作，我们需要为它的返回值使用`decltype`类型推导，即指定`authAndAccess`要返回的类型正是表达式`c[i]`的返回类型。`C++`的拥护者们预期到在某种情况下有使用`decltype`类型推导规则的需求，并将这个功能在`C++14`中通过`decltype(auto)`实现。这使这对原本的冤家（`decltype`和`auto`）在一起完美地发挥作用：`auto`指定需要推导的类型，`decltype`表明在推导的过程中使用`decltype`推导规则。因此，我们可以重写`authAndAccess`如下：

```c++
template<typename Container, typename Index>  // C++14; works,
decltype(auto)                                // but still
authAndAccess(Container &c, Index i)          // requires
{                                             // refinement
	authenticateUser();
	return c[i];
}
```

现在`authAndAccess`的返回类型就是`c[i]`的返回类型。在一般情况下，`c[i]`返回`T&`，`authAndAccess`就返回`T&`，在不常见的情况下，`c[i]`返回一个对象，`authAndAccess`也返回一个对象。

`decltype(auto)`并不仅限使用在函数返回值类型上。当时想对一个表达式使用`decltype`的推导规则时，它也可以很方便的来声明一个变量：

**注**：**我这里的理解decltype(auto)就是对表达式的原始类型做推导**，如上述函数返回值使用decltype(auto)表示decltype(c[i])，下例对赋值对象myWidget2使用decltype(auto)表示decltype(cw)。

```c++
class Widget {};

int main() {
    Widget w;
    const Widget& cw = w;
    auto myWidget1 = cw;  // auto type deduction, myWidget1's type is Widget
    decltype(auto) myWidget2 = cw;  // decltype type deduction: myWidget2's type is const Widget&
    decltype(myWidget1) myWidget3 = cw;  // 这里是对myWidget1类型做推导，还是Widget
    return 0;
}
```

我知道，到目前为止会有两个问题困扰着你。一个是我们前面提到的，对`authAndAccess`的改进。我们在这里讨论。

再次看一下`C++14`版本的`authAndAccess`的声明：

```c++
template<typename Container, typename Index>
decltype(auto) anthAndAccess(Container &c, Index i);
```

这个容器是通过非`const`左值引用传入的，因为通过返回一个容器元素的引用是来修改容器是被允许的。但是这也意味着不可能将右值（如临时变量）传入这个函数。右值不能和一个左值引用绑定（除非是`const`的左值引用，这不是这里的情况）。

诚然，传递一个右值容器给`authAndAccess`是一种极端情况。一个右值容器作为一个临时对象，在 `anthAndAccess` 所在语句的最后被销毁，意味着对容器中一个元素的引用（这个引用通常是`authAndAccess`返回的）在创建它的语句结束的地方将被悬空。然而，这对于传给`authAndAccess`一个临时对象是有意义的。一个用户可能仅仅想拷贝一个临时容器中的一个元素，例如：

```c++
std::deque<std::string> makeStringDeque(); // factory function
// make copy of 5th element of deque returned
// from makeStringDeque
auto s = authAndAccess(makeStringDeque(), 5);  // 通过工厂函数获得一个临时deque，获取其第5个元素值返回
```

支持这样的应用意味着我们需要修改`authAndAccess`的声明来可以接受左值和右值。重载可以解决这个问题（一个重载负责左值引用参数，另外一个负责右值引用参数），但是我们将有两个函数需要维护。避免这种情况的一个方法是使`authAndAccess`有一个既可以绑定左值又可以绑定右值的引用参数，条款24将说明这正是统一引用（`universal reference`）所做的。因此`authAndAccess`可以像如下声明：

```c++
template<typename Container, typename Index>  // c is now a
decltype(auto) authAndAccess(Container&& c,   // universal
                             Index i);        // reference

#include <iostream>
#include <vector>
#include <cassert>

using namespace std;

template<typename Container, typename Index>
decltype(auto) authAndAccess(Container&& c, Index i) {  // 如果CMakeLists.txt里改成C++11版本，则这里decltype(auto)编译错误：error: expected primary-expression before 'auto'
    assert(i < c.size());
    return c[i];  // 这里没有使用forward，也是可以正常工作
}

int main() {
    vector<int> ints1 = {1, 2, 3};
    cout << authAndAccess(ints1, 2) << endl;  // 3

    auto val = authAndAccess(vector<int>({1, 2, 3}), 2);
    cout << val << endl;  // 3
    return 0;
}
```

在这个模板中，我们不知道我们在操作什么类型的容器，这也意味着我们等同地忽略了它用到的索引对象的类型。对于一个不清楚其类型的对象使用传值传递通常会冒一些风险，比如因为不必要的复制而造成的性能降低，对象切片的行为问题，被同事嘲笑，但是对容器索引的情况，正如一些标准库的索引（`std::string, std::vector, std::deque`的`[]`操作）按值传递看上去是合理的，因此对它们我们仍坚持按值传递。

然而，我们需要更新这个模板的实现，将`std::forward`应用给统一引用，使得它和条款25中的建议是一致的。

```c++
template<typename Container, typename Index>     // final
decltype(auto)                                   // C++14
authAndAccess(Container&& c, Index i)            // version
{
    authenticateUser();
    return std::forward<Container>(c)[i];
}
```

这个实现可以做我们期望的任何事情，但是它要求使用支持`C++14`的编译器。如果你没有一个这样的编译器，你可以使用这个模板的`C++11`版本。它除了要你自己必须指定返回类型以外，和对应的`C++14`版本是完全一样的，

```C++
template<typename Container, typename Index>    // final
auto                                            // C++11
authAndAccess(Container&& c, Index i)           // version
-> decltype(std::forward<Container>(c)[i])
{
    authenticateUser();
    return std::forward<Container>(c)[i];
}
```

另外一个容易被你挑刺的地方是我在本条款开头的那句话：`decltype`几乎所有时候都会输出你所期望的类型，但是有时候它的输出也会令你吃惊。诚实的讲，你不太可能遇到这种以外，除非你是一个重型库的实现人员。

为了彻底的理解`decltype`的行为，你必须使你自己对一些特殊情况比较熟悉。这些特殊情况太晦涩难懂，以至于很少有书会像本书一样讨论，但是同时也可以增加我们对`decltype`的认识。

对一个变量名使用`decltype`得到这个变量名的声明类型。变量名属于左值表达式，但这并不影响`decltype`的行为。然而，对于一个比变量名更复杂的左值表达式，`decltype`保证返回的类型是左值引用。因此说，如果**一个非变量名的类型为`T`的左值表达式，`decltype`报告的类型是`T&`**。这很少产生什么影响，因为绝大部分左值表达式的类型有内在的左值引用修饰符。例如，需要返回左值的函数返回的总是左值引用。

这种行为的意义是值得我们注意的。但是在下面这个语句中

```c++
    int x = 0;
```

`x`是一个变量名，因此`decltyper(x)`是`int`。但是如果给`x`加上括号"(x)"就得到一个比变量名复杂的表达式。作为变量名，`x`是一个左值，同时`C++`定义表达式`(x)`也是左值。因此`decltype((x))`是`int&`。给一个变量名加上括号会改变`decltype`返回的类型。

在`C++11`中，这仅仅是个好奇的探索，但是和`C++14`中对`decltype(auto)`支持相结合，函数中返回语句的一个细小改变会影响对这个函数的推导类型。

```c++
decltype(auto) f1()
{
    int x = 0;
    // ...
    return x;        // decltype(x) is int, so f1 returns int
}

decltype(auto) f2()
{
    int x = 0;
    return (x);     // decltype((x)) is int&, so f2 return int&
}
```

`f2`不仅返回值类型与`f1`不同，它返回的是对一个局部变量的引用。这种类型的代码将把你带上一个为定义行为的快速列车-你完全不想登上的列车。

本地试验：

```c++
#include <iostream>

using namespace std;

int main() {
    int x = 0;
    decltype(x) y = x;
    decltype((x)) z = x;

    x = 10;
    cout << y << endl;  // 0
    cout << z << endl;  // 10

    cout << boolalpha;
    cout << is_reference<decltype(x)>::value << endl;  // false
    cout << is_reference<decltype((x))>::value << endl;  // true
    return 0;
}
```

最主要的经验教训就是当使用`decltype(auto)`时要多留心一些。被推导的表达式中看上去无关紧要的细节都可能影响`decltype`返回的类型。为了保证推导出的类型是你所期望的，请使用条款4中的技术。

同时不能更大视角上的认识。当然，`decltype`（无论只有`decltype`或者还是和`auto`联合使用）有可能偶尔会产生类型推导的惊奇行为，但是这不是常见的情况。一般情况下，`decltype`会产生你期望的类型。将`decltype`应用于变量名无非是正确的，因为在这种情况下，`decltype`做的就是报告这个变量名的声明类型。

| 要记住的东西                                                 |
| :----------------------------------------------------------- |
| `decltype`几乎总是得到一个变量或表达式的类型而不需要任何修改 |
| 对于非变量名的类型为`T`的左值表达式，`decltype`总是返回`T&`  |
| `C++14`支持`decltype(auto)`，它的行为就像`auto`,从初始化操作来推导类型，但是它推导类型时使用`decltype`的规则 |

## 条款4：知道如何查看类型推导

对类型推导结果的查看的工具的选择和你在软件开发过程中的相关信息有关系。我们要探讨三种可能：在你编写代码的时候，在编译的时候和在运行的时候得到类型推导的信息。

### IDE编辑器

在IDE里面的代码编辑器里面当你使用光标悬停在实体之上，常常可以显示出程序实体（例如变量，参数，函数等等）的类型。举一个例子，下面的代码：

```c++
const int theAnswer = 42;
auto x = theAnswer;
auto y = &theAnswer;
```

一个IDE的编辑器很可能会展示出`x`的推导的类型是`int`，`y`的类型是`const int*`。

对于这样的情况，你的代码必须处在一个差不多可以编译的状态，因为这样可以使得IDE接受这种在IDE内部运行这的一个C++编译器（或者至少是一个前端）的信息。如果那个编译器无法能够有足够的能力去感知你的代码并且parse你的代码然后去执行类型推导，他就无法展示对应推导的类型了。

对于简单的类型例如`int`，IDE里面的信息是正常的。但是我们随后会发现，涉及到更加复杂的类型的时候，从IDE里面得到的信息并不一定是有帮助性的。

### 编译器诊断

一个有效的让编译器展示类型的办法就是故意制造编译问题。编译的错误输出会报告会和捕捉到的类型相关错误。

假设，举个例子，我们希望看在上面例子中的`x`和`y`被推导的类型。我们首先声明一个类模板，但是并不定义这个模板。就像下面优雅的做法：

```c++
template<typename T>                    // 声明TD
class TD;                               // TD == "Type Displayer"
```

尝试实例化这个模板会导致错误信息，因为没有模板的定义实现。想看`x`和`y`被推导的类型，只要尝试去使用这些类型去实例化`TD`：

```c++
TD<decltype(x)> xType;                  // 引起的错误
TD<decltype(y)> yType;                  // 包含了x和y的类型
```

我使用的变量名字的形式`variableNameType`是因为这样有利于输出的错误信息可以帮助我定位我要寻找的信息。对上面的代码，我的一个编译器输出了诊断信息，其中的一部分如下：（我把我们关注的类型信息高亮了（原文中高亮了模板中的`int`和`const int*`，但是Markdown在代码block中操作粗体比较麻烦，译文中没有加粗——译者注））：

```
error: aggregate 'TD<int> xType' has incomplete type and cannot be defined
error: aggregate 'TD<const int *> yType' has incomplete type and cannot be defined
```

另一个编译器提供相同的信息，但是格式不太一样：

```c++
error: 'xType' uses undefined class 'TD<int>'
error: 'yType' uses undefined class 'TD<const int *>'
```

排除格式的区别，我测试了所有的编译器都会在这种代码的技术中输出有用的错误信息。

本地试验：

```c++
#include <iostream>

using namespace std;

template<typename T>
class TD;

int main() {
    const int theAnswer = 42;
    auto x = theAnswer;
    auto y = &theAnswer;
    TD<decltype(x)> xType;  // error: aggregate 'TD<int> xType' has incomplete type and cannot be defined
    TD<decltype(y)> yType;  // error: aggregate 'TD<const int*> yType' has incomplete type and cannot be defined
    return 0;
}
```

### 运行时输出

`printf`到运行的时候可以用来显示类型信息（这并不是我推荐你使用`printf`的原因），但是它提供了对输出格式的完全掌控。挑战就在于你要创造一个你关心的对象的输出的格式控制展示的textual。“这还不容易，”你会这样想，“就是用`typeid`和`std::type_info::name`来救场啊。”在后续的对`x`和`y`的类型推导中，你可以发现你可以这样写：

```c++
std::cout << typeid(x).name() << '\n'; // display types for
std::cout << typeid(y).name() << '\n'; // x and y
// 输出：
i
PKi
```

这是基于对类似于`x`或者`y`运算`typeid`可以得到一个`std::type_info`对象，`std::type_info`有一个成员函数，`name`可以提供一个C-style的字符串（也就是`const char*`）代表了类型的名字。

调用`std::type_info::name`并不会确定返回有意义的东西，但是实现上是有帮助性质的。帮助是多种多样的。举一个例子，GNU和Clang编译器返回`x`的类型是“`i`”，`y`的类型是“`PKi`”。这些编译器的输出结果你一旦学会就可以理解他们，“`i`”意味着“`int`”，“`PK`”意味着“pointer to konst const”（所有的编译器都支持一个工具，`C++filt`，它可以解析这样的“乱七八糟”的类型。）微软的编译器提供更加直白的输出：“`int`”对`x`，“`int const*`”对`y`。

因为这些结果对`x`和`y`而言都是正确的，你可能认为类型输出的问题就此解决了，但是这并不能轻率。考虑一个更加复杂的例子：

```c++
template<typename T>                // template function to
void f(const T& param);             // be called

std::vector<Widget> createVec();    // 工厂方法

const auto vw = createVec();        // init vw w/factory return

if (!vw.empty()) {
    f(&vw[0]);                      // 调用f
    …
}
```

在代码中，涉及了一个用户定义的类型（`Widget`），一个STL容器（`std::vector`），一个`auto`变量（`vw`），这对你的编译器的类型推导的可视化是非常具有表现性的。举个例子，想看到模板类型参数`T`和`f`的函数模板参数`param`。

在问题中没有`typeid`是很直接的。在`f`中添加一些代码去展示你想要的类型：

```c++
template<typename T>
void f(const T& param)
{
    using std::cout;
    cout << "T = " << typeid(T).name() << '\n';         // 展示T
    cout << "param = " << typeid(param).name() << '\n'; // 展示param的类型
    …
}
```

使用GNU和Clang编译器编译会输出如下结果：

```
T = PK6Widget
param = PK6Widget
```

我们已经知道对于这些编译器，`PK`意味着“pointer to `const`”，所以比较奇怪的就是数字6，这是在后面跟着的类的名字(`Widget`)的字母字符的长度。所以这些编译器就告我我们`T`和`param`的类型都是`const Widget*`。

微软的编译器输出：

```
T = class Widget const *
param = class Widget const *
```

本地试验：

```c++
#include <iostream>
#include <vector>

using namespace std;

template<typename T>
void f(const T& param) {  // 断点调试时可以看到para类型为：const Widget* const&
    cout << param << endl;
    cout << "T = " << typeid(T).name() << "\n";  // 展示T
    cout << "param = " << typeid(param).name() << "\n";  // 展示param的类型
}

class Widget {
public:
    Widget(const string& name) : name_(name) {}

private:
    string name_;
};

std::vector<Widget> createVec() {  // 工厂方法
    vector<Widget> widgets = {Widget("widget1"), Widget("widget2"), Widget("widget3")};
    return std::move(widgets);
}

int main() {
    const auto vw = createVec();        // init vw w/factory return
    if (!vw.empty()) {
        f(&vw[0]);                      // 调用f
    }
    return 0;
}
// 输出：
0x80004afb0
T = PK6Widget
param = PK6Widget
```

三种不同的编译器都产出了相同的建议性信息，这表明信息是准确的。但是更加仔细的分析，在模板`f`中，`param`的类型是`const T&`。`T`和`param`的类型是一样的难道不会感到奇怪吗？举个例子，如果`T`是`int`，`param`的类型应该是`const int&`——根本不是相同的类型。

悲剧的是，`std::type_info::name`的结果并不可靠。在这种情况下，举个例子，所有的三种编译器报告的`param`的类型都是不正确的。更深入的话，它们本来就是不正确的，因为**`std::type_info::name`的特化指定了类型会被当做它们被传给模板函数的时候的按值传递的参数**。正如条款1所述，这就意味着**如果类型是一个引用，它的引用特性会被忽略，如果在忽略引用之后存在`const`（或者`volatile`），它的`const`特性（或者`volatile`特性）会被忽略**。这就是为什么`param`的类型——`const Widget * const &`——被报告成了`const Widget*`。首先类型的引用特性被去掉了，然后结果参数指针的`const`特性也被消除了。

同样的悲剧，由IDE编辑器显示的类型信息也并不准确——或者说至少并不可信。对之前的相同的例子，一个我知道的IDE的编辑器报告出`T`的类型（我不打算说）：

```
const
std::_Simple_types<std::_Wrap_alloc<std::_Vec_base_types<Widget,
std::allocator<Widget> >::_Alloc>::value_type>::value_type *
```

还是这个相同的IDE编辑器，`param`的类型是：

```
const std::_Simple_types<...>::value_type *const &
```

这个没有`T`的类型那么吓人，但是中间的“...”会让你感到困惑，直到你发现这是IDE编辑器的一种说辞“我们省略所有`T`类型的部分”。带上一点运气，你的开发环境也许会对这样的代码有着更好的表现。

如果你更加倾向于库而不是运气，你就应该知道`std::type_info::name`可能在IDE中会显示类型失败，但是Boost TypeIndex库（经常写做Boost.TypeIndex）是被设计成可以成功显示的。这个库并不是C++标准的一部分，也不是IDE和模板的一部分。更深层的是，事实上Boost库（在[boost.com](http://boost.com/)）是一个跨平台的，开源的，并且基于一个偏执的团队都比较喜欢的协议。这就意味着基于标准库之上使用Boost库的代码接近于一个跨平台的体验。

这里展示了一段我们使用Boost.TypeIndex的函数`f`精准的输出类型信息：

```
#include <boost/type_index.hpp>
template<typename T>
void f(const T& param)
{
    using std::cout;
    using boost::typeindex::type_id_with_cvr;

    // show T
    cout << "T = "
         << type_id_with_cvr<T>().pretty_name()
         << '\n';

    // show param's type
    cout << "param = "
         << type_id_with_cvr<decltype(param)>().pretty_name()
         << '\n';
    …
}
```

这个模板函数`boost::typeindex::type_id_with_cvr`接受一个类型参数（我们想知道的类型信息）来正常工作，它不会去除`const`，`volatile`或者引用特性（这也就是模板中的“`cvr`”的意思）。返回的结果是个`boost::typeindex::type_index`对象，其中的`pretty_name`成员函数产出一个`std::string`包含一个对人比较友好的类型展示的字符串。

通过这个`f`的实现，再次考虑之前使用`typeid`导致推导出现错误的`param`类型信息：

```c++
std::vector<Widget> createVec();    // 工厂方法

const auto vw = createVec();        // init vw w/factory return

if (!vw.empty()) {
    f(&vw[0]);                      // 调用f
    …
}
```

在GNU和Clang的编译器下面，Boost.TypeIndex输出（准确）的结果：

```
T = Widget const*
param = Widget const* const&
```

微软的编译器实际上输出的结果是一样的：

```
T = class Widget const *
param = class Widget const * const &
```

这种接近相同的结果很漂亮，但是需要注意IDE编辑器，编译器错误信息，和类似于Boost.TypeIndex的库仅仅是一个对你编译类型推导的一种工具而已。所有的都是有帮助意义的，但是到目前为止，没有什么关于类型推导法则1-3的替代品。

| 要记住的东西                                                 |
| :----------------------------------------------------------- |
| 类型推导的结果常常可以通过IDE的编辑器，编译器错误输出信息和Boost TypeIndex库的结果中得到 |
| 一些工具的结果不一定有帮助性也不一定准确，所以对C++标准的类型推导法则加以理解是很有必要的 |

# 第二章 auto关键字

知道如何引导`auto`得到正确的结果是很重要的，因为回到手动声明类型虽然是一个变通方案，但是应该尽量避免。

## 条款5：优先使用`auto`而非显式类型声明

使用下面语句是简单快乐的

```c++
    int x;
```

等等。见鬼，我忘记初始化`x`了，因此它的值是无法确定的。也许，它会被初始化为0。但是这根据上下文语境决定。这真令人叹息。

不要介意。我们来看看一个要通过迭代器解引用初始化的局部变量声明的简单与快乐。

```c++
    template<typename It>
    void dwim(It b, It e)
    {
      while(b != e){
      typename std::iterator_traits<It>::value_type
        currValue = *b;
      ...
      }
    }
```

额。`typename std::iterator_traits::value_type`来表示被迭代器指向的值的类型？真的是这样吗？我必须努力不去想这是多么有趣的一件事。见鬼。等等，难道我已经说出来了。

------

关于typename作为依赖类型声明可以参考：<https://blog.csdn.net/vanturman/article/details/80269081>

------

好吧，有三个令人愉悦的地方：声明一个封装好的局部变量的类型带来的快乐。是的，这是没有问题的。一个封装体的类型只有编译器知道，因此不能被显示的写出来。哎，见鬼。

见鬼，见鬼，见鬼！使用`C++`编程并不是它本该有的愉悦体验。

是的，过去的确不是。但是由于`C++11`，得益于`auto`，这些问题都消失了。`auto`变量从他们的初始化推导出其类型，所以它们必须被初始化。这就意味着你可以在现代的`C++`高速公路上对没有初始化的变量的问题说再见了。

```c++
    int x1;                   // potentially uninitialized
    auto x2;                  // error! initializer required
    auto x3 = 0;              // fine, x's value is well-defined
```

如上所述，高速公路上不再有由于解引用迭代器的声明局部变量而引起的坑坑洼洼。

```c++
    template<typename It>
    void dwim(It b, It e)
    {
      while(b != e){
        auto currValue = *b;
      ...
      }
    }
```

由于`auto`使用类型推导（参见条款2），它可以表示那些仅仅被编译器知晓的类型：

```c++
    auto dereUPLess =                         // comparison func.
      [](const std::unique_ptr<Widget>& p1,   // for Widgets
         const std::unique_ptr<Widget>& p2)   // pointed to by
      { return *p1 < *p2};                     // std::unique_ptrs
```

非常酷。在`C++14`中，模板（原文为temperature）被进一步丢弃，因为使用`lambda`表达式的参数可以包含`auto`：

```c++
  auto derefLess =                            // C++14 comparison
  [](const auto& p1,                          // function for
     const auto& p2)                          // values pointed
  { return *p1 < *p2; };
```

尽管非常酷，也许你在想，我们不需要使用`auto`去声明一个持有封装体的变量，因为我们可以使用一个`std::function`对象。这是千真万确的，我们可以这样干，但是也许那不是你正在思考的东西。也许你在思考“`std::function`是什么东东？”。因此让我们解释清楚。

`std::function`是`C++11`标准库的一个模板，它可以使函数指针普通化。鉴于函数指针只能指向一个函数，然而，`std::function`对象可以应用任何可以被调用的对象，就像函数。就像你声明一个函数指针的时候，必须指明这个函数指针指向的函数的类型，你产生一个`std::function`对象时，你也指明它要引用的函数的类型。你可以通过`std::function`的模板参数来完成这个工作。例如，有声明一个名为`func`的`std::function`对象，它可以引用有如下特点的可调用对象：

```c++
    bool(const std::unique_ptr<Widget> &,    // C++11 signature for
         const std::unique_ptr<Widget> &)    // std::unique_ptr<Widget>
                                             // comparison funtion
```

你可以这么写：

```c++
    std::function<bool(const std::unique_ptr<Widget> &,
                       const std::unique_ptr<Widget> &)> func; 
```

因为`lambda`表达式得到一个可调用对象，封装体可以存储在`std::function`对象里面。这意味着，我们可以声明不适用`auto`的`C++11`版本的`dereUPLess`如下：

```c++
    std::function<bool(const std::unique_ptr<Widget>&,
                       const std::unique_ptr<Widget>&)>
      derefUPLess = [](const std::unique_ptr<Widget>& p1,
                       const std::unique_ptr<Widget>& p2)
                      {return *p1 < *p2; };
```

意识到需要重复参数的类型这种冗余的语法是重要的，使用`std::function`和使用`auto`并不一样。一个使用`auto`声明持有一个封装的变量和封装体有同样的类型，也仅使用和封装体同样大小的内存。持有一个封装体的被`std::function`声明的变量的类型是`std::function`模板的一个实例，并且对任何类型只有一个固定的大小。这个内存大小可能不能满足封装体的需求。出现这种情况时，`std::function`将会开辟堆空间来存储这个封装体。导致的结果就是`std::function`对象一般会比`auto`声明的对象使用更多的内存。由于实现细节中，约束内嵌的使用和提供间接函数的调用，通过`std::function`对象来调用一个封装体比通过`auto`对象要慢。**换言之，`std::function`方法通常体积比`auto`大，并且慢，还有可能导致内存不足的异常**。就像你在上面一个例子中看到的，使用`auto`的工作量明显小于使用`std::function`。持有一个封装体时，`auto`和`std::function`之间的竞争，对`auto`简直就是游戏。（一个相似的论点也成立对于持有`std::blind`调用结果的`auto`和`std::function`，但是在条款34中，我将竭尽所能的说服你尽可能使用`lambda`表达式，而不是`std::blind`）。

本地试验代码：

```c++
#include <iostream>
#include <memory>

using namespace std;

class Widget {
public:
    explicit Widget(string&& name) : name_(move(std::forward<string>(name))) {}

    bool operator<(const Widget& rhs) const {
        return name_ < rhs.name_;
    }

private:
    string name_;
};

int main() {
    auto dereUPLess = [](const std::unique_ptr<Widget>& p1, const std::unique_ptr<Widget>& p2) { return *p1 < *p2; };
    // auto dereUPLess = [](const auto& p1, const auto& p2) { return *p1 < *p2; };
    // std::function<bool(const std::unique_ptr<Widget>&, const std::unique_ptr<Widget>&)> dereUPLess = [](const std::unique_ptr<Widget>& p1, const std::unique_ptr<Widget>& p2) { return *p1 < *p2; };
    cout << boolalpha << dereUPLess(make_unique<Widget>("abc"), make_unique<Widget>("abd")) << endl;  // true
    return 0;
}
```

`auto`的优点除了可以避免未初始化的变量，变量声明引起的歧义，直接持有封装体的能力。还有一个就是可以避免“类型截断”问题。下面有个例子，你可能见过或者写过：

```c++
   std::vector<int> v;
   // ...
   unsigned sz = v.size();
```

`v.size()`定义的返回类型是`std::vector::size_type`，但是很少有开发者对此十分清楚。`std::vector::size_type`被指定为一个非符号的整数类型，因此很多程序员认为`unsigned`类型是足够的，然后写出了上面的代码。这将导致一些有趣的后果。比如说在32位`Windows`系统上，`unsigned`和`std::vector::size_type`有同样的大小，但是在64位的`Windows`上，`unsigned`是32bit的，而`std::vector::size_type`是64bit的。这意味着上面的代码在32位`Windows`系统上工作良好，但是在64位`Windows`系统上时有可能不正确，当应用程序从32位移植到64位上时，谁又想在这种问题上浪费时间呢？
使用`auto`可以保证你不必被上面的东西所困扰：

```c++
    auto sz = v.size()    // sz's type is std::vector<int>::size_type
```

仍然不太确定使用`auto`的高明之处？看看下面的代码：

```
   std::unordered_map<std::string, int> m;
   ...

   for (const std::pair<std::string, int>& p : m)
   {
   ...                  // do something with p
   }
```

这看上去完美合理。但是有一个问题，你看出来了吗？
意识到`std::unorder_map`的`key`部分是`const`类型的，在哈希表中的`std::pair`的类型不是`std::pair<std::string, int>`，而是`std::pair<const std::string, int>`。但是这不是循环体外变量`p`的声明类型。后果就是，编译器竭尽全力去找到一种方式，把`std::pair<const std::string, int>`对象（正是哈希表中的内容）转化为`std::pair<std::string, int>`对象（`p`的声明类型）。这个过程将通过复制`m`的一个元素到一个临时对象，然后将这个临时对象和`p`绑定完成。在每个循环结束的时候这个临时对象将被销毁。如果是你写了这个循环，你将会感觉代码的行为令人吃惊，因为你本来想简单地将引用`p`和`m`的每个元素绑定的。

本地试验，将const去掉后，使用非常量左值引用，则编译错误，说明确实每次p值都是由`pair<const string, int>`对象构造而来：

```c++
#include <iostream>
#include <unordered_map>

using namespace std;

int main() {
    unordered_map<string, int> m = {make_pair("a", 1), make_pair("b", 2)};
    // 改成for (pair<const string, int>& p : m)后，无编译错误
    for (pair<string, int>& p : m) {  // 编译错误：error: invalid initialization of reference of type 'std::pair<std::basic_string<char>, int>&' from expression of type 'std::pair<const std::basic_string<char>, int>'
        cout << "p.first: " << p.first << ", p.second: " << p.second << endl;
    }
    return 0;
}
```

这种无意的类型不匹配可以通过`auto`解决

```c++
for (const auto& p : m)
{
    ...                     // as before
}
```

这不仅仅更高效，也更容易敲击代码。更近一步，这个代码还有一些吸引人的特性，比如如果你要取`p`的地址，你的确得到一个指向`m`的元素的指针。如果不使用`auto`，你将得到一个指向临时对象的指针——这个临时对象在每次循环结束时将被销毁。

上面两个例子中——在应该使用`std::vector<int>::size_type`的时候使用`unsigned`和在该使用`std::pair<const std::sting, int>`的地方使用`std::pair<std::sting, int>`——说明显式指定的类型是如何导致你万万没想到的隐式的转换的。如果你使用`auto`作为目标变量的类型，你不必为你声明类型和用来初始化它的表达式类型之间的不匹配而担心。

有好几个使用`auto`而不是显式类型声明的原因。然而，`auto`不是完美的。`auto`变量的类型都是从初始化它的表达式推导出来的，一些初始化表达式并不是我们期望的类型。发生这种情况时，你可以参考条款2和条款6来决定怎么办，我不在此处展开了。相反，我将我的精力集中在你将传统的类型声明替代为`auto`时带来的代码可读性问题。

首先，深呼吸放松一下。`auto`是一个可选项，不是必须项。如果根据你的专业判断，使用显式的类型声明比使用`auto`会使你的代码更加清晰或者更好维护，或者在其他方面更有优势，你可以继续使用显式的类型声明。牢记一点，`C++`并没有在这个方面有什么大的突破，这种技术在其他语言中被熟知，叫做类型推断(`type inference`)。其他的静态类型过程式语言（像`C#`，`D`，`Scala`，`Visual Basic`）也有或多或少等价的特点，对静态类型的函数编程语言（像`ML`，`Haskell`，`OCaml`，`F#`等）另当别论。一定程度上说，这是受到动态类型语言的成功所启发，比如`Perl`，`Python`，`Ruby`，在这些语言中很少显式指定变量的类型。软件开发社区对于类型推断有很丰富的经验，这些经验表明这些技术和创建及维护巨大的工业级代码库没有矛盾。

一些开发者被这样的事实困扰，使用`auto`会消除看一眼源代码就能确定对象的类型的能力。然而，IDE提示对象类型的功能经常能缓解这个问题（甚至考虑到在条款4中提到的IDE的类型显示问题），在很多情况下，一个对象类型的摘要视图和显示完全的类型一样有用。比如，摘要视图足以让开发者知道这个对象是容器还是计数器或者一个智能指针，而不需要知道这个容器，计数器或者智能指针的确切特性。假设比较好的选择变量名字，这样的摘要类型信息几乎总是唾手可得的。

事实是显式地写出类型可能会引入一些难以察觉的错误，导致正确性或者效率问题，或者两者兼而有之。除此之外，`auto`类型会自动的改变如果初始化它的表达式改变后，这意味着通过使用`auto`可以使代码重构变得更简单。举个例子，如果一个函数被声明为返回`int`，但是你稍后决定返回`long`可能更好一些，如果你把这个函数的返回结果存储在一个`auto`变量中，在下次编译的时候，调用代码将会自动的更新。结果如果存储在一个显式声明为`int`的变量中，你需要找到所有调用这个函数的地方然后改写他们。

| 要记住的东西                                                 |
| :----------------------------------------------------------- |
| `auto`变量一定要被初始化，并且对由于类型不匹配引起的兼容和效率问题有免疫力，可以简单化代码重构，一般会比显式的声明类型敲击更少的键盘 |
| `auto`类型的变量也受限于[条款2](https://www.kancloud.cn/kangdandan/DeducingTypes/2-Understand-auto-type-deduction.html)和条款6中描述的陷阱 |

## 条款6：当auto推导出非预期类型时应当使用显式的类型初始化

条款5解释了使用`auto`关键字去声明变量，这样就比直接显示声明类型提供了一系列的技术优势，但是有时候`auto`的类型推导会和你想的南辕北辙。举一个例子，假设我有一个函数接受一个`Widget`返回一个`std::vector`，其中每个`bool`表征`Widget`是否接受一个特定的特性：

```c++
std::vector<bool> features(const Widget& w);
```

进一步的，假设第五个bit表示`Widget`是否有高优先级。我们可以这样写代码：

```c++
#include <vector>

using namespace std;

class Widget {};

std::vector<bool> features(const Widget& w) {
    return std::move(std::vector<bool>());
}

void processWidget(const Widget& w, bool) {
}

int main() {
    Widget w;
    bool highPriority = features(w)[5];  // w是不是个高优先级的？
    processWidget(w, highPriority);  // 配合优先级处理w
    return 0;
}
```

这份代码没有任何问题。它工作正常。但是如果我们做一个看起来无伤大雅的修改，把`highPriority`的显式的类型换成`auto`：

```c++
auto highPriority = features(w)[5];         // w是不是个高优先级的？
```

情况变了。所有的代码还是可以编译，但是它的行为变得不可预测：

```c++
processWidget(w, highPriority);             // 未定义行为
```

正如注释中所提到的，调用`processWidget`现在会导致未定义的行为。但是为什么呢？答案是非常的令人惊讶的。在使用`auto`的代码中，`highPriority`的类型已经不是`bool`了。尽管`std::vector`从概念上说是`bool`的容器，对`std::vector`的`operator[]`运算符并不一定是返回容器中的元素的引用（`std::vector::operator[]`对所有的类型都返回引用，就是除了`bool`）。事实上，他返回的是一个`std::vector::reference`对象（是一个在`std::vector`中内嵌的class）。

`std::vector::reference`存在是因为`std::vector`是对`bool`数据封装的模板特化，一个bit对应一个`bool`。这就给`std::vector::operator[]`带来了问题，因为`std::vector`的`operator[]`应该返回一个`T&`，但是C++禁止bits的引用。没办法返回一个`bool&`，`std::vector`的`operator[]`于是就返回了一个行为上和`bool&`相似的对象。想要这种行为成功，`std::vector::reference`对象必须能在`bool&`的能处的语境中使用。在`std::vector::reference`对象的特性中，是他隐式的转换成`bool`才使得这种操作得以成功。（不是转换成`bool&`，而是`bool`。去解释详细的`std::vector::reference`对象如何模拟一个`bool&`的行为有有些偏离主题，所以我们就只是简单的提一下这种隐式转换只是这种技术中的一部。）

在大脑中带上这种信息，再次阅读原先的代码：

```
bool highPriority = features(w)[5];         // 直接显示highPriority的类型
```

这里，`features`返回了一个`std::vector`对象，在这里`operator[]`被调用。`operator[]`返回一个`std::vector::reference`对象，这个然后隐式的转换成`highPriority`需要用来初始化的`bool`类型。于是就以`features`返回的`std::vector`的第五个bit的数值来结束`highPriority`的数值，这也是我们所预期的。

和使用`auto`的`highPriority`声明进行对比：

```c++
auto highPriority = features(w)[5];         // 推导highPriority的类型
```

这次，`features`返回一个`std::vector`对象，而且，`operator[]`再次被调用。`operator[]`继续返回一个`std::vector::reference`对象，但是现在有一个变化，因为`auto`推导`highPriority`的类型。`highPriority`根本并没有`features`返回的`std::vector`的第五个bit的数值。

数值和`std::vector::reference`是如何实现的是有关系的。一种实现是这样的对象包含一个指向包含bit引用的机器word的指针，在word上面加上偏移。考虑这个对`highPriority`的初始化的意义，假设`std::vector::reference`的实现是恰当的。

调用`features`会返回一个临时的`std::vector`对象。这个对象是没有名字的，但是对于这个讨论的目的，我会把它叫做`temp`，`operator[]`是在`temp`上调用的，`std::vector::reference`返回一个由`temp`管理的包含一个指向一个包含bits的数据结构的指针，在word上面加上偏移定位到第五个bit。`highPriority`也是一个`std::vector::reference`对象的一份拷贝，所以`highPriority`也在`temp`中包含一个指向word的指针，加上偏移定位到第五个bit。在这个声明的结尾，`temp`被销毁，因为它是个临时对象。因此，`highPriority`包含一个野指针，这也就是调用`processWidget`会造成未定义的行为的原因：

```c++
processWidget(w, highPriority);         // 未定义的行为，highPriority包含野指针
```

`std::vector::reference`是代理类的一个例子：一个类的存在是为了模拟和对外行为和另外一个类保持一致。代理类在各种各样的目的上被使用。`std::vector::reference`的存在是为了提供一个对`std::vector`的`operator[]`的错觉，让它返回一个对bit的引用，而且标准库的智能指针类型（参考第4章）也是一些对托管的资源的代理类，使得他们的资源管理类似于原始指针。代理类的功能是良好确定的。事实上，“代理”模式是软件设计模式中的最坚挺的成员之一。

一些代理类被设计用来隔离用户。这就是`std::shared_ptr`和`std::unique_ptr`的情况。另外一些代理类是为了一些或多或少的不可见性。`std::vector::reference`就是这样一个“不可见”的代理，和他类似的是`std::bitset`，对应的是`std::bitset::reference`。

同时在一些C++库里面的类存在一种被称作表达式模板的技术。这些库最开始是为了提高数值运算的效率。提供一个`Matrix`类和`Matrix`对象`m1, m2, m3 and m4`，举一个例子，下面的表达式：

```c++
Matrix sum = m1 + m2 + m3 + m4;
```

可以计算的更快如果`Matrix`的`operator+`返回一个结果的代理而不是结果本身。这是因为，对于两个`Matrix`，`operator+`可能返回一个类似于`Sum`的代理类而不是一个`Matrix`对象。和`std::vector::reference`一样，这里会有一个隐式的从代理类到`Matrix`的转换，这个可能允许`sum`从由`=`右边的表达式产生的代理对象进行初始化。（其中的对象可能会编码整个初始化表达式，也就是，变成一种类似于`Sum, Matrix>, Matrix>`的类型。这是一个客户端需要屏蔽的类型。）

作为一个通用的法则，“不可见”的代理类不能和`auto`愉快的玩耍。这种类常常它的生命周期不会被设计成超过一个单个的语句，所以创造这样的类型的变量是会违反库的设计假定。这就是`std::vector::reference`的情况，而且我们可以看到这种违背约定的做法会导致未定义的行为。

因此你要避免使用下面的代码的形式：

```c++
auto someVar = expression of "invisible" proxy class type;
```

但是你怎么能知道代理类被使用呢？软件使用它们的时候并不可能会告知它们的存在。它们是不可见的，至少在概念上！一旦你发现了他们，难道你就必须放弃使用`auto`加之条款5所声明的`auto`的各种好处吗？

我们先看看怎么解决如何发现它们的问题。尽管“不可见”的代理类被设计用来fly beneath programmer radar in day-to-day use，库使用它们的时候常常会撰写关于它们的文档来解释为什么这样做。你对你所使用的库的基础设计理念越熟悉，你就越不可能在这些库中被代理的使用搞得狼狈不堪。

当文档不够用的时候，头文件可以弥补空缺。很少有源码封装一个完全的代理类。它们常常从一些客户调用者期望调用的函数返回，所有函数签名常常可以表征它们的存在。这里是`std::vector::operator[]`的例子：

```c++
namespace std {                     // from C++ Standards
    template <class Allocator>
    class vector<bool, Allocator> {
        public:
        …
        class reference { … };
        reference operator[](size_type n);
        …
    };
}
```

假设你知道对`std::vector`的`operator[]`常常返回一个`T&`，在这个例子中的这种非常规的`operator[]`的返回类型一般就表征了代理类的使用。在你正在使用的这些接口之上加以关注常常可以发现代理类的存在。

在实践上，很多的开发者只会在尝试修复一些奇怪的编译问题或者是调试一些错误的单元测试结果中发现代理类的使用。不管你是如何发现它们，一旦`auto`被决定作为推导代理类的类型而不是它被代理的类型，它就不需要涉及到关于`auto`，`auto`自己本身没有问题。问题在于`auto`推导的类型不是所想让它推导出来的类型。解决方案就是强制一个不同的类型推导。我把这种方法叫做显式的类型初始化原则。

显式的类型初始化原则涉及到使用`auto`声明一个变量，但是转换初始化表达式到`auto`想要的类型。下面就是一个强制`highPriority`类型是`bool`的例子：

```c++
auto highPriority = static_cast<bool>(features(w)[5]);
```

这里，`features(w)[5]`还是返回一个`std::vector::reference`的对象，就和它经常的表现一样，但是强制类型转换改变了表达式的类型成为`bool`，然后`auto`才推导其作为`highPriority`的类型。在运行的时候，从`std::vector::operator[]`返回的`std::vector::reference`对象支持执行转换到`bool`的行为，作为转换的一部分，从`features`返回的任然存活的指向`std::vector`的指针被间接引用。这样就在运行的开始避免了未定义行为。索引5然后放置在bits指针的偏移上，然后暴露的`bool`就作为`highPriority`的初始化数值。

针对于`Matrix`的例子，显示的类型初始化原则可能会看起来是这样的：

```c++
auto sum = static_cast<Matrix>(m1 + m2 + m3 + m4);
```

关于这个原则下面的程序并不禁止初始化但是要排除代理类类型。强调你要谨慎地创建一个类型的变量，它和从初始化表达式生成的类型是不同的也是有帮助意义的。举一个例子，假设你有一个函数去计算一些方差：

```c++
double calcEpsilon();               // 返回方差
```

`calcEpsilon`明确的返回一个`double`，但是假设你知道你的程序，`float`的精度就够了的时候，而且你要关注`double`和`float`的长度的区别。你可以声明一个`float`变量去存储`calcEpsilon`的结果：

```c++
float ep = calcEpsilon();           // 隐式转换double到float
```

但是这个会很难表明“我故意减小函数返回值的精度”，一个使用显式的类型初始化原则是这样做的：

```c++
auto ep = static_cast<float>(calcEpsilon());
```

# 第三章 使用现代C++

## [条款7 创建对象时区分()和{}](https://blog.csdn.net/big_yellow_duck/article/details/52202001)

基于你的看法，C++11的对象初始化的语法选择是不堪和混乱的。总的来说，初始值可以借助大括号”{}”，等号”=”，圆括号”()”：

```c++
int x(0);   // 初始值在圆括号内

int y = 0;   // 初始值跟在等号后面

int z{0};  // 初始值在大括号内
```

在很多例子中，我们可以同时使用大括号和等号：
`int z = {0};`
在本条款剩余的内容中，我会忽略这种语法，因为C++通常会把这种语法当作**只用大括号**语法对待。

使用等号初始化经常会让C++初学者认为会进行一次赋值，但不是那样的。对于内置类型，例如**int**，初始化和赋值操作的差别是模糊的，但是对于用户定义的类，区分初始化和赋值操作是很重要的，，因为这会导致不同的函数调用：

```c++
Widget w1;  // 调用默认构造函数

Widget w2 = w1;  // 不是赋值操作，调用拷贝构造函数

w1 = w2;   // 赋值操作，调用operator=函数
```

就算C++98有多种初始化语法，但是有些情况也无法得到想要的初始值，例如，STL容器无法直接用一组数来初始化。

因为初始化的语法很混乱，而且有些情况无法实现，所以C++11提出了统一初始化(uniform initailization)语法：一种至少在概念上可以用于表达任何值的语法。它的实现基于大括号，所以我称之为大括号初始化(braced initialization)。统一初始化是一个想法，大括号初始化是句法表现。

大括号初始化可以让你描述以前无法描述的初始值。使用大括号可以更容易的初始化容器：
`std::vector v{1, 3, 5};`

大括号也可以用于类内成员的默认初始值，在C++11中，等号”=”也可以实现，但是圆括号”()”则不可以：

```cpp
class Widget {
  ...
private:
  int x{ 0 };   // x的默认初始值为0
  int y = 0;  // 同上
  int z( 0 ); // 报错
}
```

另一方面，不可拷贝对象(例如，`std::atomic`)可以用大括号和圆括号初始化，但不能用等号：

```cpp
std::atomic<int> ai1{ 0 };  // 可以

std::atomic<int> ai2( 0 );  // 可以

std::atomic<int> ai3 = 0;  // 报错
```

大括号初始化有一个奇怪的特性，就是它会禁止内值类型间的隐式的范围窄化转换(narrowing conversions，也就是精度降低，范围变窄)。如果被初始化的变量的类型无法保证能被大括号内的值的类型所表述(本人理解是发生了类型截断，溢出等情况)，那么代码无法通过编译(本人测试代码可以编译运行…)：

```cpp
double x, y, z;
...
int sum1{x + y + z};  // 报错，double值的和可能在int中无法表述。本地测试有编译警告：warning: narrowing conversion of '((x + y) + z)' from 'double' to 'int' inside { } [-Wnarrowing]
int i2{10.0};  // 上面是警告，这里会有编译报错，还不知道为啥有差异。error: narrowing conversion of '1.0e+1' from 'double' to 'int' inside { } [-Wnarrowing]
```

使用圆括号和等号初始化不会检测隐式的窄化转换，这是为了与旧代码兼容：

```cpp
int sum2(x + y + z);  //通过，值被截断为int

int sum3 = x + y + z;  // 同上
```

大括号初始化的另一个值得注意的特性是它会免疫C++中的**most vexing parse**(最让人头痛的歧义？)。当开发者想要一个默认构造的对象时，经常受到**most vexing parse**的折磨，因为程序会不经意地声明个函数代替构造对象。这根本原因是当你你想要调用一个带参数的构造函数时，你可以这样：

```cpp
Widget w1(10);  // 调用Widget的带参构造函数
```

但当你尝试用类似的语法调用无参构造时，你声明了个函数，而不是创建对象：

```c++
#include <iostream>

using namespace std;

class Widget {
public:
    Widget() {}

    Widget(int x) : x(x) {}

    void show() {}

private:
    int x{0};   // x的默认初始值为0
};

int main() {
    Widget w1(10);
    w1.show();
    Widget w2();  // most vexing parse，声明了一个返回Widget的函数。error: request for member 'show' in 'w2', which is of non-class type 'Widget()'
    w2.show();
    return 0;
}

```

使用大括号包含参数是无法声明为函数的，所以使用大括号默认构造对象不会出现这个问题：

```cpp
Widget w3{};
```

我们讲了很多大括号初始化的内容，这种语法可以用于多种场景，还可以避免隐式范围窄化转换，又免疫C++的**most vexing parse**问题。一举多得，那么为什么这条款不起名为”用大括号初始化语法替代其他“呢?

大括号初始化的缺点是它有时会显现令人惊讶的的行为。这些行为的出现是因为与**std::initializer_list**混淆了。它们交互时会让代码不像表面上那样运行。例如，在条款2中讲述**auto**推断带大括号初始值时，会把类型推断为**std::initializer_list**。

在构造函数中，只要形参不带有**std::initializer_list**，圆括号和大括号行为一致：

```c++
class Widget {
public:
    Widget(int i, bool b) {
        cout << "int, bool" << endl;
    }

    Widget(int i, double d) {
        cout << "int, double" << endl;
    }
};

Widget w1(10, true);  // 调用第一个构造函数

Widget w2{10, true}; // 调用第一个构造函数

Widget w3(10, 5.0); // 调用第二个构造函数

Widget w4{10, 5.0};  // 调用第二个构造函数

// 输出：
int, bool
int, bool
int, double
int, double
```

但是，如果构造函数的形参带有**std::initializer_list**，调用构造函数时大括号初始化语法会强制使用带**std::initializer_list**参数的重载构造函数。例如，Widget类的构造函数带有参数类型是`std::initializer_list`：

```cpp
class Widget {
public:
    Widget(int i, bool b) {
        cout << "int, bool" << endl;
    }

    Widget(int i, double d) {
        cout << "int, double" << endl;
    }

    Widget(std::initializer_list<long double> il) {
        cout << "std::initializer_list<long double>" << endl;
    }
};
```

那么w2和w4会使用新的构造构造函数，尽管**std::initializer_list**的元素是**long double**，比起另外两个构造函数的参数来说是更差的匹配：

```c++
Widget w1(10, true);   // 使用圆括号，调用第一个构造函数

Widget w2{10, true};  // 使用大括号，调用第三个构造函数
// 10 和 true被转换为long double

Widget w3(10, 5.0);  // 使用圆括号，调用第一个构造函数

Widget w4{10, 5.0};  // 使用大括号，调用第三个构造函数
// 10 和 true被转换为long double

// 输出：
int, bool
std::initializer_list<long double>
int, double
std::initializer_list<long double>
```

就算是正常的拷贝构造和赋值构造也可以被带有**std::initializer_list**的构造函数劫持：

```cpp
class Widget {
public:
    Widget(int i, bool b) {
        cout << "int, bool" << endl;
    }

    Widget(int i, double d) {
        cout << "int, double" << endl;
    }

    Widget(std::initializer_list<long double> il) {
        cout << "std::initializer_list<long double>" << endl;
    }

    operator float() const {  // 支持隐式转换为float类型
        return 1.0;
    }
};

Widget w4{10, 5.0};  // 使用大括号，调用第三个构造函数
// 10 和 true被转换为long double

Widget w5(w4);    // 使用圆括号，调用拷贝构造函数

Widget w6{w4};   // 使用大括号，调用第三个构造函数
// 原因是先把w4转换为float，再把float转换为long dobule

Widget w7(std::move(w4));  // 使用圆括号，调用移动构造函数

Widget w8{std::move(w4)};  // 使用大括号，调用第三个构造函数，理由同w6

// 输出：
std::initializer_list<long double>  // w4
std::initializer_list<long double>  // Widget w6{w4};
std::initializer_list<long double>  // Widget w8{std::move(w4)};
```

编译器用带有**std::initializer_list**构造函数匹配大括号初始值的决心是
如此的坚定，就算带有**std::initializer_list**的构造函数是无法调用的：

```cpp
class Widget {
public:
    Widget(int i, bool b) {
        cout << "int, bool" << endl;
    }

    Widget(int i, double d) {
        cout << "int, double" << endl;
    }

    Widget(std::initializer_list<bool> il) {
        cout << "std::initializer_list<bool>" << endl;
    }
};

Widget w{10, 5.0};  // 报错，因为发生范围窄化转换。error: narrowing conversion of '10' from 'int' to 'bool' inside { } [-Wnarrowing]
```

就算是这样，编译器也会忽略另外两个构造函数(第二个还是参数精确匹配的)，并且尝试调用带有`std::initializer_list`的构造函数。而调用第三个构造函数会让一个**int**(10)值和一个**double**(5.0)值转换为**bool**类型。这两个转换都是范围窄化转换(**bool**的大小不能准确描述它们的值)，然而窄化转换在大括号初始化语法中是被禁止的，所以这个函数调用无效，代码无法编译通过。

只有当大括号内的值无法转换为**std::initializer_list**元素的类型时，编译器才会使用正常的重载选择方法，例如把上面的`std::initializer_list`改为`std::initializer_list`，那么那些非**std::initializer_list**构造函数会重新成为候选函数，因为没有办法从数值转换为**std::string**：

```c++
class Widget {
public:
    Widget(int i, bool b) {
        cout << "int, bool" << endl;
    }

    Widget(int i, double d) {
        cout << "int, double" << endl;
    }

    Widget(std::initializer_list<string> il) {  // bool 改为 std::string
        cout << "std::initializer_list<string>" << endl;
    }
};

Widget w1(10, true);  // 使用圆括号，调用第一个构造函数

Widget w2{10, true};  // 使用大括号，不过调用第一个构造函数

Widget w3(10, 5.0);   // 使用圆括号，调用第二个构造函数

Widget w4{10, 5.0};  // 使用大括号， 不过调用第二个构造函数

// 输出：
int, bool
int, bool
int, double
int, double
```

这就会让编译器检查大括号内的初始值然后选择重载函数，不过这里有一个有趣的边缘情况。一个大括号内无参的构造函数，不仅可以表示默认构造，还可以表示带**std::initializer_list**的构造函数。你的空括号是表示哪一种情况呢？如果它表示不带参数，那么就是默认构造；如果它表示一个空的**std::initializer_list**，那么就是从一个不带元素的**std::initializer_list**进行构造。

正确答案是你将使用默认构造，**一个空的大括号表示的是没有参数**，而不是一个空的**std::initializer_list**：

```c++
class Widget {
public:
    Widget() {
        cout << "Widget()" << endl;
    }

    Widget(std::initializer_list<int> il) {
        cout << "Widget(std::initializer_list<int> il)" << endl;
    }
};

Widget w1;   // 调用默认构造函数

Widget w2{};   // 调用默认构造函数

Widget w3();   // 出现most vexing parse，声明了一个函数

// 输出：
Widget()
Widget()
```

如果你想要用一个空的**std::initializer_list**参数来调用带**std::initializer_list**构造函数，那么你需要把大括号作为参数，即把空的大括号放在圆括号内或者大括号内：

```c++
Widget w4({});   // 用了一个空的list来调用带std::initializer_list构造函数

Widget w5{{}};   // 同上

// 输出：
Widget(std::initializer_list<int> il)
Widget(std::initializer_list<int> il)
```

此时此刻，大括号初始化，**std::initializer_list**，构造函数重载之间的复杂关系在你的大脑中冒泡，你可能想要知道这些信息会在多大程度上关系到你的日常编程。可能比你想象中要多，因为**std::vector**就是一个被它们直接影响的类。**std::vector**中有一个可以指定容器的大小和容器内元素的初始值的不带**std::initializer_list**构造函数，但它也有一个可以指定容器中元素值的带**std::initializer_list**函数。如果你想要创建一个数值类型的**std::vector**(例如**std::vector**)，然后你要传递两个值作为构造函数的参数，那么使用大括号与圆括号的行为是不同的：

```c++
#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int main() {
    std::vector<int> v1(10, 20);   // 使用不带std::initializer_list的构造函数
    // 创建10个元素的vector，每个元素的值为20
    auto p = [](const auto& i) { cout << i << " "; };
    for_each(v1.begin(), v1.end(), p);

    cout << "\n";
    std::vector<int> v2{10, 20};   // 使用带std::initializer_list的构造函数
    // 创建2个元素的vector，元素值为10和20
    for_each(v2.begin(), v2.end(), p);

    return 0;
}
// 输出：
20 20 20 20 20 20 20 20 20 20 
10 20 
```

我们先忽视**std::vector**和圆括号，大括号，构造函数重载规则。这次讨论不涉及两个要素。首先，作为一个类的作者，你需要知道如果你的构造函数集中包含一个带**std::initializer**构造函数，客户代码中使用了大括号初始化的话看起来好像只有带**std::initializer**构造函数。因此，你最好把构造函数设计得重载调用不受大括号和圆括号影响。换句话说，**把上面std::vector出现的情况中当作错误，自己写代码时应该避免这样**。

一种纠纷，比如说，你的类一开始不含有带**std::initializer_list**构造函数，后来你加了一个，那么用户会发现，原先使用大括号初始化时选择的是不带**std::initializer_list**构造函数，而现在全部都选择带**std::initializer_list**构造函数。当然，这种事情在你为重载函数再添加一个实现时也有可能发生：本来是调用旧的重载函数可能会选择新加入的函数。不过**std::initializer_list**构造函数的不同之处是，带**std::initializer_list**构造函数不用与其它构造函数竞争，它直接遮蔽了其它的构造函数。所以加入带**std::initializer_list**需要深思熟虑。

第二个要讲的是作为类的使用者(用户)，你创建对象时必须在使用大括号还是圆括号上仔细考虑。大多数开发者最后会选择一种符号作为默认使用，而使用另一种符号当它无可避免的时候。使用大括号作为默认符号的人喜欢大括号的广泛适用性，禁止窄化转换，避免C++的**most vexing parse**。这些人知道在某种情况下(例如，上面的vector)，圆括号是必须的。另一方便，使用圆括号作为默认符合的人们，沿袭着C++98的语法传统，又可以避免**auto-deduced-a-std::initializer_list**问题，还知道重载构造函数的选择不受**std::initializer_list**构造函数的拦截，他们也会让步当只有大括号才能实现时(例如用特殊的值来初始化容器)。这里没有哪个更好的说法，所以我的建议是选择一种符号并且大部分情况下使用它。

上面说的**auto-deduced-a-std::initializer_list**问题是如条款2所说的auto类型推导的特别之处，本意是想使用auto替换使用初始化列表构造的vector类型声明，但是auto将类型推导成initializer_list了，而非vector：

```c++
#include <iostream>
#include <vector>

using namespace std;

template<typename T>
class TD;

int main() {
    auto t1 = {1, 2, 3};
    TD<decltype(t1)> t1Type;  // error: aggregate 'TD<std::initializer_list<int> > t1Type' has incomplete type and cannot be defined

    vector<int> t2 = {1, 2, 3};
    TD<decltype(t2)> t2Type;  // error: aggregate 'TD<std::vector<int> > t2Type' has incomplete type and cannot be defined
    return 0;
}
```

如果你是模板的作者，那么大括号和圆括号的关系会变得很难搞，因为你不可能知道模板函数调用时使用哪一种。例如，你想通过随意的数值参数来创建一个数值类型对象。一个可变参数模板在概念上可以很直接的实现：

```cpp
template <typename T, typename... Ts>
void doSomeWork(Ts&&... params)
{
  create local T object from params...  //伪代码
  ...
}
```

在伪代码中有两种选择：

```cpp
T localObject(std::forward<Ts>(params)...);    // 使用圆括号

T localObject{std::forward<Ts>(params)...};    // 使用大括号
```

然后考虑以下代码：

```cpp
std::vector<int> v;
...
doSomeWork<std::vector<int>>(10,20);
```

如果`doSomeWork`使用的是圆括号的方式创建对象，那么局部对象**std::vector**有10个元素。如果`doSomeWork`使用的是大括号的方式创建对象，那么局部对象**std::vector**只有2个元素。是要使用哪一种方式呢，模板的作者是不知道的，只有用户知道他想使用哪一种方式。

这正是标准库函数`std::make_unique`和`std::make_shared`所面临的一个问题(条款21)。这些函数的解决办法是强制要求把参数写在圆括号内，然后在接口中说明这个决策。

本地试验模板中分别圆括号和大括号，可以看到括号使用方式不同，对于同样的可变参数调用，得到的vector元素也不同：

```c++
#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

template<typename T, typename... Ts>
decltype(auto) getVar1(Ts&& ... params) {
    return T(forward<Ts>(params)...);
}

template<typename T, typename... Ts>
decltype(auto) getVar2(Ts&& ... params) {
    return T{forward<Ts>(params)...};
}

int main() {
    auto p = [](const auto& i) { cout << i << " "; };

    auto&& v1 = getVar1<vector<int>>(10, 20);  // 20 20 20 20 20 20 20 20 20 20 
    for_each(v1.begin(), v1.end(), p);

    cout << "\n";
    auto&& v2 = getVar2<vector<int>>(10, 20);  // 10 20 
    for_each(v2.begin(), v2.end(), p);

    return 0;
}
```

**总结**

需要记住的3点：

- 大括号初始化是适用范围最广泛的初始化语法，它可以防止范围窄化转换和免疫C++的**most vexing parse**问题。
- 在选择重载构造函数期间，大括号初始化会尽可能的匹配带**std::initializer_list**构造函数，尽管看起来匹配其它的构造函数刚好。
- 使用大括号和圆括号初始化导致巨大差异的一个例子是用两个参数创建`std::vector`对象。
- 在模板中选择用大括号还是圆括号创建对象是具有挑战性的。

## 条款8：优先使用`nullptr`而不是`0`或者`NULL`

`0`字面上是一个`int`类型，而不是指针，这是显而易见的。`C++`扫描到一个`0`，但是发现在上下文中仅有一个指针用到了它，编译器将勉强将`0`解释为空指针，但是这仅仅是一个应变之策。`C++`最初始的原则是`0`是`int`而非指针。

经验上讲，同样的情况对`NULL`也是存在的。对`NULL`而言，仍有一些细节上的不确定性，因为赋予`NULL`一个除了`int`（即`long`）以外的整数类型是被允许的。这不常见，但是这真的是没有问题的，因为此处的焦点不是`NULL`的确切类型，而是`0`和`NULL`都不属于指针类型。

在`C++98`中，这意味着重载指针和整数类型的函数的行为会令人吃惊。传递`0`或者`NULL`作为参数给重载函数永远不会调用指针重载的那个函数：

```c++
#include <iostream>

using namespace std;

void f(int);       // 函数f的三个重载
void f(bool);
void f(void*);

int main() {
    f(0);          // 调用 f(int)，而非f(void*)
    f(NULL);       // 可能无法编译，但是调用f(int)，不可能调用 f(void*)。如果只有void f(void*)，则可以调用编译。
    return 0;
}
// 当前编译错误为：
/cygdrive/d/Program Files/JetBrains/CppProject/TestProject/main.cpp:14:11: error: call of overloaded 'f(NULL)' is ambiguous
     f(NULL);                                             // 可能无法编译，但是调用f(int)
           ^
/cygdrive/d/Program Files/JetBrains/CppProject/TestProject/main.cpp:7:6: note: candidate: 'void f(int)'
 void f(int);                                          // 函数f的三个重载
      ^
/cygdrive/d/Program Files/JetBrains/CppProject/TestProject/main.cpp:8:6: note: candidate: 'void f(bool)'
 void f(bool);
      ^
/cygdrive/d/Program Files/JetBrains/CppProject/TestProject/main.cpp:9:6: note: candidate: 'void f(void*)'
 void f(void*);
      ^
```

`f(NULL)`行为的不确定性的确反映了在实现`NULL`的类型上存在的自由发挥空间。如果`NULL`被定为`0L`（即`0`作为一个`long`整形），函数的调用是有歧义的，因为`long`转化为`int`，`long`转化为`bool`，`0L`转换为`void*`都被认为是同样可行的。关于这个函数调用有意思的事情是在源代码的字面意思（使用`NULL`调用`f`，`NULL`应该是个空指针）和它的真实意义（一个整数在调用`f`，`NULL`不是空指针）存在着冲突。这种违背直觉的行为正是`C++98`程序员不被允许重载指针和整数类型的原因。这个原则对于`C++11`依然有效，因为尽管有本条款的力荐，仍然还有一些开发者继续使用`0`和`NULL`，虽然`nullptr`是一个更好的选择。

`nullptr`的优势是它不再是一个整数类型。诚实的讲，它也不是一个指针类型，但是你可以把它想象成一个可以指向任意类型的指针。`nullptr`的类型实际上是`std::nullptr_t`，`std::nullptr_t`定义为`nullptr`的类型，这是一个完美的循环定义。`std::nullptr_t`可以隐式的转换为所有的原始的指针类型，这使得`nullptr`表现的像可以指向任意类型的指针。

使用`nullptr`作为参数去调用重载函数`f`将会调用`f(void*)`重载体，因为`nullptr`不能被视为整数类型的：

```c++
	f(nullptr);                                          //调用f(void*)重载体
```

使用`nullptr`而不是`0`或者`NULL`，可以避免重载解析上的令人吃惊行为，但是它的优势不仅限于此。它可以提高代码的清晰度，尤其是牵扯到`auto`类型变量的时候。例如，你在一个代码库中遇到下面代码：

```c++
	auto result = findRecord( /* arguments */);

	if(result == 0){
		...
	}
```

如果你不能轻松地的看出`findRecord`返回的是什么，要知道`result`是一个指针还是整数类型并不是很简单的。毕竟，`0`（被用来测试`result`的）即可以当做指针也可以当做整数类型。另一方面，你如果看到下面的代码：

```c++
	auto result = findRecord( /* arguments */);

	if(reuslt == nullptr){
		...
	}
```

明显就没有歧义了：`result`一定是个指针类型。

当模板进入我们考虑的范围，`nullptr`的光芒则显得更加耀眼了。假想你有一些函数，只有当对应的互斥量被锁定的时候，这些函数才可以被调用。每个函数的参数是不同类型的指针：

```c++
	int    f1(std::shared_ptr<Widget> spw);             // 只有对应的
	double f2(std::unique_ptr<Widget> upw);             // 互斥量被锁定
	bool   f3(Widget* pw);                              // 才会调用这些函数
```

想传递空指针给这些函数的调用看上去像这样：

```c++
	std::mutex f1m, f2m, f3m;                           // 对应于f1, f2和f3的互斥量 

	using MuxGuard =                                    // C++11 版typedef；参加条款9
      std::lock_guard<std::mutex>;
    ...
    {
      MuxGuard g(f1m);                                 // 为f1锁定互斥量
      auto result = f1(0);                             // 将0当做空指针作为参数传给f1
    }                                                  // 解锁互斥量

    ...

    {
      MuxGuard g(f2m);                                // 为f2锁定互斥量
      auto result = f2(NULL);                         // 将NULL当做空指针作为参数传给f2
    }                                                 // 解锁互斥量

    ...

    {
      MuxGuard g(f3m);                               // 为f3锁定互斥量
      auto result = f3(nullptr);                     // 将nullptr当做空指针作为参数传给f3
    }                                                // 解锁互斥量
```

在前两个函数调用中没有使用`nullptr`是令人沮丧的，但是上面的代码是可以工作的，这才是最重要的。然而，代码中的重复模式——锁定互斥量，调用函数，解锁互斥量——才是更令人沮丧和反感的。避免这种重复风格的代码正是模板的设计初衷，因此，让我们使用模板化上面的模式：

```c++
	template<typename FuncType,
	         typename MuxType,
	         typename PtrType>
	auto lockAndCall(FuncType func,
	                 MuxType& mutex,
	                 PtrType ptr) -> decltype(func(ptr))
	{
      MuxGuard g(mutex);
      return func(ptr);
    }
```

如果这个函数的返回值类型（`auto ...->decltype(func(ptr))`）让你挠头不已，你应该到条款3寻求一下帮助，在那里我们已经做过详细的介绍。在`C++14`中，你可以看到，返回值可以通过简单的`decltype(auto)`推导得出：

```c++
	template<typename FuncType,
	         typename MuxType,
	         typename PtrType>
	decltype(auto) lockAndCall(FuncType func,                       // C++14
	                 MuxType& mutex,
	                 PtrType ptr) 
	{
      MuxGuard g(mutex);
      return func(ptr);
    }
```

给定`lockAndCall`模板（上边的任意版本），调用者可以写像下面的代码：

```c++
#include <iostream>
#include <mutex>
#include <memory>

using namespace std;

using MuxGuard = std::lock_guard<std::mutex>;

template<typename FuncType, typename MuxType, typename PtrType>
decltype(auto) lockAndCall(FuncType func, MuxType& mutex, PtrType ptr) {
    MuxGuard g(mutex);
    return func(ptr);
}

class Widget {};

int f1(std::shared_ptr<Widget> spw) {
    return 0;
}

double f2(std::unique_ptr<Widget> upw) {
    return 0;
}

bool f3(Widget* pw) {
    return true;
}

int main() {
    std::mutex f1m, f2m, f3m;
    auto result1 = lockAndCall(f1, f1m,0);  // 错误，error: could not convert 'ptr' from 'int' to 'std::shared_ptr<Widget>'
    auto result2 = lockAndCall(f2, f2m, NULL);  // 错误，本地编译，NULL被推导为long int类型，error: could not convert 'ptr' from 'long int' to 'std::unique_ptr<Widget>'
    auto result3 = lockAndCall(f3, f2m, nullptr);  // 正确
    return 0;
}
```

他们可以这样写，但是就如注释中指明的，三种情况里面的两种是无法编译通过。在第一个调用中，当把`0`作为参数传给`lockAndCall`,模板通过类型推导得知它的类型。`0`的类型总是`int`，这就是对`lockAndCall`的调用实例化的时候的类型。不幸的是，这意味着在`lockAndCall`中调用`func`，被传入的是`int`，这个`f1`期望接受的参数`std::share_ptr`是不不兼容的。传入到`lockAndCall`的`0`尝试来表示一个空指针，但是正真不传入的是一个普通的`int`类型。尝试将`int`作为`std::share_ptr`传给`f1`会导致一个类型冲突错误。使用`0`调用`lockAndCall`会失败，因为在模板中，一个`int`类型传给一个要求参数是`std::share_ptr`的函数。

对调用`NULL`的情况的分析基本上是一样的。当`NULL`传递给`lockAndCall`时，从参数`ptr`推导出的类型是整数类型，当`ptr`——一个`int`或者类`int`的类型——传给`f2`，一个类型错误将会发生，因为这个函数期待的是得到一个`std::unique_ptr`类型的参数。

相反，使用`nullptr`是没有问题的。当`nullptr`传递给`lockAndCall`，`ptr`的类型被推导为`std::nullptr_t`。当`ptr`被传递给`f3`，有一个由`std::nullptr_t`到`Widget*`的隐形转换，因为`std::nullptr_t`可以隐式转换为任何类型的指针。

真正的原因是，对于`0`和`NULL`，模板类型推导出了错误的类型（他们的真正类型，而不是它们作为空指针而体现出的退化的内涵），这是在需要用到空指针时使用`nullptr`而非`0`或者`NULL`最引人注目的原因。使用`nullptr`，模板不会造成额外的困扰。另外结合`nullptr`在重载中不会导致像`0`和`NULL`那样的诡异行为的事实，胜负已定。当你需要用到空指针时，使用`nullptr`而不是`0`或者`NULL`。

| 要记住的东西                         |
| :----------------------------------- |
| 相较于`0`和`NULL`，优先使用`nullptr` |
| 避免整数类型和指针类型之间的重载     |

## 条款9：优先使用声明别名而不是`typedef`











































