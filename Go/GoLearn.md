# Go语言教程

## [Go 语言结构](https://www.runoob.com/go/go-program-structure.html)

关于包，根据本地测试得出以下几点：

-  文件名与包名没有直接关系，不一定要将文件名与包名定成同一个。
-  文件夹名与包名没有直接关系，并非需要一致。
-  同一个文件夹下的文件只能有一个包名，否则编译报错。

文件结构：

```go
Test
--helloworld.go

myMath
--myMath1.go
--myMath2.go
```

测试代码:

```go
// helloworld.go
package main

import (
"fmt"
"./myMath"
)

func main(){
    fmt.Println("Hello World!")
    fmt.Println(mathClass.Add(1,1))
    fmt.Println(mathClass.Sub(1,1))
}
// 如果改为mathClass1和mathClass2，报错：found packages mathClass1 (myMath1.go) and mathClass2 (myMath2.go) in D:\Program Files\JetBrains\GoProject\TestProject\myMath
// myMath1.go
package mathClass
func Add(x,y int) int {
    return x + y
}
// myMath2.go
package mathClass
func Sub(x,y int) int {
    return x - y
}
```

## [Go 语言变量](https://www.runoob.com/go/go-variables.html)

### 多变量声明

```go
//类型相同多个变量, 非全局变量
var vname1, vname2, vname3 type
vname1, vname2, vname3 = v1, v2, v3

var vname1, vname2, vname3 = v1, v2, v3 // 和 python 很像,不需要显示声明类型，自动推断

vname1, vname2, vname3 := v1, v2, v3 // 出现在 := 左侧的变量不应该是已经被声明过的，否则会导致编译错误


// 这种因式分解关键字的写法一般用于声明全局变量
var (
    vname1 v_type1
    vname2 v_type2
)
```

实例：

```go
package main

var x, y int
var (  // 这种因式分解关键字的写法一般用于声明全局变量
    a int  // a int = 10 也可以赋值
    b bool  // b bool = true
)

var c, d int = 1, 2
var e, f = 123, "hello"

//这种不带声明格式的只能在函数体中出现
//g, h := 123, "hello"

func main(){
    g, h := 123, "hello"
    println(x, y, a, b, c, d, e, f, g, h)
}
```

## [Go 语言常量](https://www.runoob.com/go/go-constants.html)

常量可以用len(), cap(), unsafe.Sizeof()函数计算表达式的值。常量表达式中，函数必须是内置函数，否则编译不过：

实例：

```go
package main

import "unsafe"
const (
    a = "abc"
    b = len(a)
    c = unsafe.Sizeof(a)
)

func main(){
    println(a, b, c)  // abc 3 16
}
```

**iota**

iota，特殊常量，可以认为是一个可以被编译器修改的常量。

iota 在 const关键字出现时将被重置为 0(const 内部的第一行之前)，const 中每新增一行常量声明将使 iota 计数一次(iota 可理解为 const 语句块中的行索引)。

iota 可以被用作枚举值：

```
const (
    a = iota  // 0
    b = iota  // 1
    c = iota  // 2
)
```

第一个 iota 等于 0，每当 iota 在新的一行被使用时，它的值都会自动加 1；所以 a=0, b=1, c=2 可以简写为如下形式：

```
const (
    a = iota
    b
    c
)
```

**iota 用法**

实例：

```go
package main

import "fmt"

func main() {
    const (
            a = iota   //0
            b          //1
            c          //2
            d = "ha"   //独立值，iota += 1
            e          //"ha"   iota += 1
            f = 100    //iota +=1
            g          //100  iota +=1
            h = iota   //7,恢复计数
            i          //8
    )
    fmt.Println(a,b,c,d,e,f,g,h,i)  // 0 1 2 ha ha 100 100 7 8
}
```

再看个有趣的的 iota 实例：

```go
package main

import "fmt"
const (
    i=1<<iota
    j=3<<iota
    k
    l
)

func main() {
    fmt.Println("i=",i)
    fmt.Println("j=",j)
    fmt.Println("k=",k)
    fmt.Println("l=",l)
}
```

以上实例运行结果为：

```
i= 1
j= 6
k= 12
l= 24
```

iota 表示从 0 开始自动加 1，所以 **i=1<<0**, **j=3<<1**（**<<** 表示左移的意思），即：i=1, j=6，这没问题，关键在 k 和 l，从输出结果看 **k=3<<2**，**l=3<<3**。

简单表述:

- **i=1**：左移 0 位,不变仍为 1;
- **j=3**：左移 1 位,变为二进制 110, 即 6;
- **k=3**：左移 2 位,变为二进制 1100, 即 12;
- **l=3**：左移 3 位,变为二进制 11000,即 24。

注：**<<n==\*(2^n)**。



## [Go 语言函数方法](https://www.runoob.com/go/go-method.html)

```go
package main

import (
   "fmt"  
)

/* 定义结构体 */
type Circle struct {
  radius float64
}


func main()  { 
   var c Circle
   fmt.Println(c.radius)
   c.radius = 10.00
   fmt.Println(c.getArea())
   c.changeRadius(20)
   fmt.Println(c.radius)
   change(&c, 30)
   fmt.Println(c.radius)
}
func (c Circle) getArea() float64  {
   return c.radius * c.radius
}
// 注意如果想要更改成功c的值，这里需要传指针
func (c *Circle) changeRadius(radius float64)  {
   c.radius = radius
}

// 以下操作将不生效
//func (c Circle) changeRadius(radius float64)  {
//   c.radius = radius
//}
// 引用类型要想改变值需要传指针
func change(c *Circle, radius float64)  {
   c.radius = radius
}
// 输出：
0
100
20
30
```

## [Go 语言向函数传递数组](https://www.runoob.com/go/go-passing-arrays-to-functions.html)

如果你想向函数传递数组参数，你需要在函数定义时，声明形参为数组，我们可以通过以下两种方式来声明：

### 方式一

形参设定数组大小：

```
void myFunction(param [10]int)
{
.
.
.
}
```

### 方式二

形参未设定数组大小：

```
void myFunction(param []int)
{
.
.
.
}
```

```go
package main

import "fmt"

func main() {
    /* 数组长度为 5 */
    var balance = [5]int{1000, 2, 3, 17, 50}  // 这里如果声明为[5]，则函数入参必须为arr [5]int。如果声明为[]，则函数入参必须为arr []int。如果声明为[...]，则函数入参必须为arr [5]int
    var avg float32

    /* 数组作为参数传递给函数 */
    avg = getAverage(balance, 5)

    /* 输出返回的平均值 */
    fmt.Printf("平均值为: %f ", avg)
}
func getAverage(arr [5]int, size int) float32 {
    var i, sum int
    var avg float32

    for i = 0; i < size; i++ {
        sum += arr[i]
    }

    avg = float32(sum) / float32(size)

    return avg
}
```

## [go中如何实现多态](https://www.jianshu.com/p/b333c5f34ef6)

*go* 是一种强类型的语言，每当我从*php*切换到*go*时总有些许的不适应，但是追求优雅，就不应该妥协。

*go*没有 *implements*, *extends* 关键字，所以习惯于 **OOP** 编程，或许一开始会有点无所适从的感觉。 但*go*作为一种优雅的语言， 给我们提供了另一种解决方案， 那就是**鸭子类型**：*看起来像鸭子， 那么它就是鸭子*.

那么什么是鸭子类型， 如何去实现呢 ？

接下来我会以一个简单的例子来讲述这种实现方案。

首先我们需要一个超类：

```csharp
type Animal interface {
    Sleep()
    Age() int
    Type() string
}
```

必然我们需要真正去实现这些的子类:

```go
type Cat struct {
    MaxAge int
}

func (this *Cat) Sleep() {
    fmt.Println("Cat need sleep")
}
func (this *Cat) Age() int {
    return this.MaxAge
}
func (this *Cat) Type() string {
    return "Cat"
}
```

```go
type Dog struct {
    MaxAge int
}

func (this *Dog) Sleep() {
    fmt.Println("Dog need sleep")
}
func (this *Dog) Age() int {
    return this.MaxAge
}
func (this *Dog) Type() string {
    return "Dog"
}
```

我们有两个具体实现类 *Cat*, *Dog*, 但是*Animal*如何知道*Cat*, *Dog*已经实现了它呢？ 原因在于： *Cat*, *Dog*实现了*Animal*中的全部方法， 那么它就认为这就是我的子类。

那么如何去使用这种关系呢？

```go
func Factory(name string) Animal {
    switch name {
    case "dog":
        return &Dog{MaxAge: 20}
    case "cat":
        return &Cat{MaxAge: 10}
    default:
        panic("No such animal")
    }
}
```

我们使用具体工厂类来构造具体的实现类， 在调用时你知道有这些方法， 但是并不清楚具体的实现， 每一种类型的改变都不会影响到其它的类型。

```css
package main

import (
    "animals"
    "fmt"
)

func main() {
    animal := animals.Factory("dog")
    animal.Sleep()
    fmt.Printf("%s max age is: %d", animal.Type(), animal.Age())
}
```

来看看我们的输出会是什么吧

```swift
> Output:
animals
command-line-arguments
Dog need sleep
Dog max age is: 20
> Elapsed: 0.366s
> Result: Success
```

这就是*go*中的多态， 是不是比 *implements*/*extends* 显示的表明关系更优雅呢。

## [Go 语言结构体](https://www.runoob.com/go/go-structures.html)

```go
package main

import "fmt"

type Books struct {
    title   string
    author  string
    subject string
    book_id int
}

func main() {
    var Book1 *Books = &Books{  // 这里如果用引用结构体，则是指针类型，函数入参也必须是指针类型
        title:   "1",
        author:  "2",
        subject: "3",
        book_id: 0,
    }

    /* 打印 Book1 信息 */
    printBook(Book1)
}

func printBook(book *Books) {
    fmt.Printf("Book title : %s\n", book.title)
    fmt.Printf("Book author : %s\n", book.author)
    fmt.Printf("Book subject : %s\n", book.subject)
    fmt.Printf("Book book_id : %d\n", book.book_id)
}
```

## [Go 语言接口](https://www.runoob.com/go/go-interfaces.html)

Go 语言提供了另外一种数据类型即接口，它把所有的具有共性的方法定义在一起，任何其他类型只要实现了这些方法就是实现了这个接口。

```go
package main

import (
    "fmt"
)

type Phone interface {
    call()
}

type NokiaPhone struct {
}

func (nokiaPhone NokiaPhone) call() {
    fmt.Println("I am Nokia, I can call you!")
}

type IPhone struct {
}

func (iPhone IPhone) call() {
    fmt.Println("I am iPhone, I can call you!")
}

func main() {
    var phone Phone

    phone = new(NokiaPhone)
    phone = NokiaPhone{}  // interface可以用对象赋值
    phone = &NokiaPhone{}  // interface也可以用指针赋值
    phone.call()

    phone = new(IPhone)
    phone.call()
}
```

在上面的例子中，我们定义了一个接口Phone，接口里面有一个方法call()。然后我们在main函数里面定义了一个Phone类型变量，并分别为之赋值为NokiaPhone和IPhone。然后调用call()方法，输出结果如下：

```
I am Nokia, I can call you!
I am iPhone, I can call you!
```

## [Go 错误处理](https://www.runoob.com/go/go-error-handling.html)

Go 语言通过内置的错误接口提供了非常简单的错误处理机制。

error类型是一个接口类型，这是它的定义：

```go
type error interface {
    Error() string
}
```

在下面的例子中，我们在调用Sqrt的时候传递的一个负数，然后就得到了non-nil的error对象，将此对象与nil比较，结果为true，所以fmt.Println(fmt包在处理error时会调用Error方法)被调用，以输出错误，请看下面调用的示例代码：

```
package main

import (
    "fmt"
)

// 定义一个 DivideError 结构
type DivideError struct {
    dividee int
    divider int
}

// 实现 `error` 接口
func (de *DivideError) Error() string {
    strFormat := `
    Cannot proceed, the divider is zero.
    dividee: %d
    divider: 0
`
    return fmt.Sprintf(strFormat, de.dividee)
}

// 定义 `int` 类型除法运算的函数
func Divide(varDividee int, varDivider int) (result int, errorMsg string) {
    if varDivider == 0 {
        dData := DivideError{
            dividee: varDividee,
            divider: varDivider,
        }
        errorMsg = dData.Error()
        return
    } else {
        return varDividee / varDivider, ""
    }

}

func main() {

    // 正常情况
    if result, errorMsg := Divide(100, 10); errorMsg == "" {
        fmt.Println("100/10 = ", result)
    }
    // 当除数为零的时候会返回错误信息
    if _, errorMsg := Divide(100, 0); errorMsg != "" {
        fmt.Println("errorMsg is: ", errorMsg)
    }

}
```

执行以上程序，输出结果为：

```
100/10 =  10
errorMsg is:  
    Cannot proceed, the divider is zero.
    dividee: 100
    divider: 0
```

## [Go 并发](https://www.runoob.com/go/go-concurrent.html)

Go 语言支持并发，我们只需要通过 go 关键字来开启 goroutine 即可。

goroutine 是轻量级线程，goroutine 的调度是由 Golang 运行时进行管理的。

goroutine 语法格式：

```
go 函数名( 参数列表 )
```

例如：

```
go f(x, y, z)
```

开启一个新的 goroutine:

```
f(x, y, z)
```

Go 允许使用 go 语句开启一个新的运行期线程， 即 goroutine，以一个不同的、新创建的 goroutine 来执行一个函数。 同一个程序中的所有 goroutine 共享同一个地址空间。

**实例**

```go
package main

import (
        "fmt"
        "time"
)

func say(s string) {
        for i := 0; i < 5; i++ {
                time.Sleep(100 * time.Millisecond)
                fmt.Println(s)
        }
}

func main() {
        go say("world")
        say("hello")
}
```

执行以上代码，你会看到输出的 hello 和 world 是没有固定先后顺序。因为它们是两个 goroutine 在执行：

```
world
hello
hello
world
world
hello
hello
world
world
hello
```

**通道（channel）**

通道（channel）是用来传递数据的一个数据结构。

通道可用于两个 goroutine 之间通过传递一个指定类型的值来同步运行和通讯。操作符 `<-` 用于指定通道的方向，发送或接收。如果未指定方向，则为双向通道。

```
ch <- v    // 把 v 发送到通道 ch
v := <-ch  // 从 ch 接收数据
           // 并把值赋给 v
```

声明一个通道很简单，我们使用chan关键字即可，通道在使用前必须先创建：

```
ch := make(chan int)
```

**注意**：默认情况下，通道是不带缓冲区的。发送端发送数据，同时必须有接收端相应的接收数据。

以下实例通过两个 goroutine 来计算数字之和，在 goroutine 完成计算后，它会计算两个结果的和：

**实例**

```go
package main

import "fmt"

func sum(s []int, c chan int) {
    sum := 0
    for _, v := range s {
        sum += v
    }
    c <- sum // 把 sum 发送到通道 c
}

func main() {
    s := []int{7, 2, 8, -9, 4, 0}

    c := make(chan int)
    go sum(s[:len(s)/2], c)
    go sum(s[len(s)/2:], c)
    x, y := <-c, <-c // 从通道 c 中接收

    fmt.Println(x, y, x+y)  // -5 17 12
}
```

**通道缓冲区**

通道可以设置缓冲区，通过 make 的第二个参数指定缓冲区大小：

```
ch := make(chan int, 100)
```

带缓冲区的通道允许发送端的数据发送和接收端的数据获取处于异步状态，就是说发送端发送的数据可以放在缓冲区里面，可以等待接收端去获取数据，而不是立刻需要接收端去获取数据。

不过由于缓冲区的大小是有限的，所以还是必须有接收端来接收数据的，否则缓冲区一满，数据发送端就无法再发送数据了。

**注意**：如果通道不带缓冲，发送方会阻塞直到接收方从通道中接收了值。如果通道带缓冲，发送方则会阻塞直到发送的值被拷贝到缓冲区内；如果缓冲区已满，则意味着需要等待直到某个接收方获取到一个值。接收方在有值可以接收之前会一直阻塞。

**实例**

```go
package main

import "fmt"

func main() {
    // 这里我们定义了一个可以存储整数类型的带缓冲通道
    // 缓冲区大小为2
    ch := make(chan int, 2)

    // 因为 ch 是带缓冲的通道，我们可以同时发送两个数据
    // 而不用立刻需要去同步读取数据
    ch <- 1
    ch <- 2

    // 获取这两个数据
    fmt.Println(<-ch)  // 1
    fmt.Println(<-ch)  // 2
}
```

**Go 遍历通道与关闭通道**

Go 通过 range 关键字来实现遍历读取到的数据，类似于与数组或切片。格式如下：

```
v, ok := <-ch
```

如果通道接收不到数据后 ok 就为 false，这时通道就可以使用 **close()** 函数来关闭。

**实例**

```go
package main

import (
    "fmt"
)

func fibonacci(n int, c chan int) {
    x, y := 0, 1
    for i := 0; i < n; i++ {
        c <- x
        x, y = y, x+y
    }
    /*
如果注释掉close(c)，会有如下错误：
fatal error: all goroutines are asleep - deadlock!

goroutine 1 [chan receive]:
main.main()
        D:/Program Files/JetBrains/GoProject/TestProject/src/main.go:23 +0x107
     */
    close(c)
}

func main() {
    c := make(chan int, 10)
    go fibonacci(cap(c), c)
    // range 函数遍历每个从通道接收到的数据，因为 c 在发送完 10 个
    // 数据之后就关闭了通道，所以这里我们 range 函数在接收到 10 个数据
    // 之后就结束了。如果上面的 c 通道不关闭，那么 range 函数就不
    // 会结束，从而在接收第 11 个数据的时候就阻塞了。
    for i := range c {
        fmt.Println(i)
    }
}
// 输出：
0
1
1
2
3
5
8
13
21
34
```



```go
package main

func main() {
    c := make(chan int, 2)
    c <- 10
    c <- 22
    close(c)

    var v int
    var ok bool
    v, ok = <-c
    if ok {
        println(v)
    }

    v, ok = <-c
    if ok {
        println(v)
    }

    v, ok = <-c
    if ok {
        println(v)
    } else {
        println("c has no value")
    }
}
// 输出：
10
22
c has no value

// 修改为for循环遍历：
package main

func main() {
    c := make(chan int, 2)
    c <- 10
    c <- 22
    close(c)

    for v, ok := <-c; ok; v, ok = <-c {
        println(v)
    }
}
```













































