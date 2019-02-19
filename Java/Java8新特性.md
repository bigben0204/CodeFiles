# 1. Java 8 新特性简介

## 1.1. 速度更快

优化了HashMap，当每个数组内链表元素个数到达8或Hash总元素到达64时，则将链表优化为红黑树；

并且当Hash桶增加个数时，将元素移到新的数组位置时，使用红黑树可以直接计算得到新的位置；

在链表中增加元素时，增加到链表的链尾。

![HashMap数据结构](pics/HashMap数据结构.png)

## 1.2. 代码更少（增加了新的语法Lambda表达式）

Lambda是一个匿名函数，可以把Lambda表达式理解为是一段可以传递的代码（将代码像数据一样进行传递）。

参考<http://www.365yg.com/i6411483273897181698/#mid=1559096720023553>

```java
//Employee.java
package test;

import java.util.Objects;

public class Employee {
    enum Status {
        BUSY, FREE, VOCATIONAL
    }

    private int id;
    private String name;
    private int age;
    private double salary;
    private Status status;

    public Employee() {
    }

    public Employee(int id) {
        this.id = id;
    }

    public Employee(int id, int age) {
        this.id = id;
        this.age = age;
    }

    public Employee(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public Employee(String name, int age, double salary, Status status) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", age=" + age +
            ", salary=" + salary +
            ", status=" + status +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id &&
            age == employee.age &&
            Double.compare(employee.salary, salary) == 0 &&
            Objects.equals(name, employee.name) &&
            status == employee.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, salary, status);
    }
}


//MyPredicate.java
package test;

public interface MyPredicate<T> {
    boolean predicate(T t);
}

//EmployeeByAgeFilter.java
package test;

public class EmployeeByAgeFilter implements MyPredicate<Employee> {
    @Override
    public boolean predicate(Employee employee) {
        return employee.getAge() > 35;
    }
}

//EmployeeBySalaryFilter.java
package test;

public class EmployeeBySalaryFilter implements MyPredicate<Employee> {
    @Override
    public boolean predicate(Employee employee) {
        return employee.getSalary() > 5000;
    }
}

//TestLambda.java
package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.Test;

public class TestLambda {
    @Test
    public void test1() {
        Comparator<Integer> com = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };

        TreeSet<Integer> ts = new TreeSet<>(com);
    }

    @Test
    public void test2() {
//        Comparator<Integer> com = (x, y) -> Integer.compare(x, y); //可写成Comparator.comparingInt(x -> x)
//        System.out.println(Comparator.comparingInt((Integer x) -> x).compare(1, 3)); //需要指定第一个参数x的类型(Integer x) -> x，否则没有类型推导
        System.out.println(((Comparator<Integer>) (x, y) -> Integer.compare(x, y)).compare(1, 3)); //可以直接定义一个Lambda表达式，但是需要做类型转换表明该Lambda表达式是实现的哪个接口

        Comparator<Integer> com = Comparator.comparingInt(x -> Math.abs(x)); //入参keyExtractor表示对每一个待比较数据做什么转换
        //可以直接在TreeSet构造函数中写Lambda表达式如(x, y) -> Integer.compare(x, y) 或比较器如Comparator.comparingInt(x -> Math.abs(x))
        // 或类方法如Integer::compare
        TreeSet<Integer> ts = new TreeSet<>(com);
        ts.add(-3);
        ts.add(-1);
        ts.add(-5);
        System.out.println(ts);
    }

    List<Employee> employees = Arrays.asList(
        new Employee("张三", 18, 9999.99),
        new Employee("李四", 50, 5555.55),
        new Employee("王五", 40, 8888.88),
        new Employee("赵六", 15, 4444.44),
        new Employee("田七", 25, 3333.33)
    );

    //需求1：获取员工年龄大于35的信息
    @Test
    public void testAgeGt35() {
        List<Employee> employeeList = filterEmployeesAgeGt35(employees);
        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
    }

    private List<Employee> filterEmployeesAgeGt35(List<Employee> emps) {
        List<Employee> employeeList = new ArrayList<>();
        for (Employee emp : emps) {
            if (emp.getAge() > 35) {
                employeeList.add(emp);
            }
        }
        return employeeList;
    }

    //需求2：获取员工工资大于5000的信息
    @Test
    public void testSalaryGt5000() {
        List<Employee> employeeList = filterEmployeesSalaryGt5000(employees);
        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
    }

    private List<Employee> filterEmployeesSalaryGt5000(List<Employee> emps) {
        List<Employee> employeeList = new ArrayList<>();
        for (Employee emp : emps) {
            if (emp.getSalary() > 5000) {
                employeeList.add(emp);
            }
        }
        return employeeList;
    }

    //优化1：策略模式，每次需要新增一个策略实现类
    @Test
    public void testAgeGt35UsingStrategy() {
        List<Employee> employeeList = filterEmployees(employees, new EmployeeByAgeFilter());
        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
    }

    @Test
    public void testSalaryGt5000UsingStrategy() {
        List<Employee> employeeList = filterEmployees(employees, new EmployeeBySalaryFilter());
        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
    }

    //提供过滤的策略公用方法
    private List<Employee> filterEmployees(List<Employee> emps, MyPredicate<Employee> myPredicate) {
        List<Employee> employeeList = new ArrayList<>();
        for (Employee emp : emps) {
            if (myPredicate.predicate(emp)) {
                employeeList.add(emp);
            }
        }
        return employeeList;
    }

    //优化方式2：匿名内部类
    @Test
    public void testAgeLt30UsingAnonymous() {
        List<Employee> employeeList = filterEmployees(employees, new MyPredicate<Employee>() {
            @Override
            public boolean predicate(Employee employee) {
                return employee.getAge() < 30;
            }
        });

        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
    }

    //优化方式3：Lambda表达式
    @Test
    public void testSalaryLt5000UsingLambda() {
        List<Employee> employeeList = filterEmployees(employees, e -> e.getSalary() < 5000);
        employeeList.forEach(System.out::println);
    }

    //优化方式4：Stream API
    //如果上面所有的方法、接口和实现类都没有
    @Test
    public void testUsingStream() {
        //取工资大于5000
        employees.stream().filter(e -> e.getSalary() > 5000).forEach(System.out::println);
        //取前2个
        employees.stream().filter(e -> e.getSalary() > 5000).limit(2).forEach(System.out::println);
        //取名字集合
        List<String> nameList = employees.stream().map(Employee::getName).collect(Collectors.toList());
        nameList.forEach(System.out::println);
    }
}
```

### 1.2.1. 一、Lambda表达式的基础语法：Java8中引入了一个新的操作符"->"，该操作符称为箭头操作符或Lambda操作符。

>**左侧**：Lambda表达式的参数列表
>
>**右侧**：Lambda表达式中所需执行的功能，即Lambda体

Lambda表达式箭头左侧是接口函数的参数列表，箭头右侧是接口函数的实现。

语法格式：

> 1.无参数，无返回值
>
>> () -> System.out.println("Hello, world")

```java
@Test
public void testNoParameterNoReturnLambda() {
    int num = 3;//jdk 1.8前必须显示声明为final
    Runnable r1 = new Runnable() {
        @Override
        public void run() {
            System.out.println("hello, world" + num);//这里如果是num++依旧会报错，编译器要求num必须为final
        }
    };
    r1.run();

    System.out.println("------------------------");

    Runnable r2 = () -> System.out.println("hello, lambda" + num);
    r2.run();
}

输出：
hello, world0
------------------------
hello, lambda0
```

> 2.有一个参数，并且无返回值，**若只有一个参数，则小括号可以不写**
>
>> (x) -> System.out.println(x)
>>
>> 或 x -> System.out.println(x)

```java
@Test
public void testOneParameterNoReturnLambda() {
    Consumer<String> consumer = x -> System.out.println(x);
    consumer.accept("Good luck!");
    Consumer<String> consumer1 = consumer.andThen(x -> System.out.println(x + x));
    consumer1.accept("hello");
}

输出：
Good luck!
hello
hellohello
```

> 3.有两个以上参数，有返回值，并且Lambda体中有多条语句
>
>> (x, y) -> {
>>
>>     //...
>>     //...
>>     return ...;
>>
>> }

```java
@Test
public void testMultiParametersReturnLambda() {
    Comparator<Integer> comparator = (x, y) -> {
        System.out.println("函数式接口");
        return Integer.compare(x, y);
    };
    System.out.println(comparator.compare(3, 2));
}

输出：
1
```

> 4.若有返回值，且Lambda体中只有一条return语句，则return和大括号都可以省略不写
>
>> (x, y) -> x + y

```java
@Test
public void testMultiParametersOneReturnSentenceLambda() {
    Comparator<Integer> comparator = (x, y) -> Integer.compare(x, y);
    System.out.println(comparator.compare(3, 2));
}

输出：
1
```

> 5.Lambda表达式的参数列表的数据类型可以省略不写，因为JVM编译器可以通过上下文推断出数据类型，这个过程称为“类型推断”，如果显示指定参数列表的数据类型，则所有的参数类型都必须指定。
>
>> (Integer x, Integer y) -> Integer.compare(x, y);

上联：**左右遇一括号省**

下联：**左侧推断类型省**

横批：**能省则省**

### 1.2.2. 二、Lambda表达式需要“函数式接口”的支持

函数式接口：接口中只有一个抽象方法的接口，称为函数式接口。

可以使用**注解@FunctionalInterface修饰**，用以检查是否是函数式接口。

使用@FunctionalInterface修饰过的接口，可以在额外的default函数。如下例：

```java
package test;

@FunctionalInterface
public interface MyPredicate<T> {
    boolean predicate(T t);
    default boolean predicate2(T t) {
        return true;
    }
}
```

### 1.2.3. Java8内置的四大核心函数式接口

```java
package test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * Java8内置的四大核心函数式接口
 *
 * Consumer<T>: 消费型接口
 *      void accept(T t);
 *
 * Supplier<T>: 供给型接口
 *      T get();
 *
 * Function<T, R>: 函数型接口
 *      R apply(T t);
 *
 * Predicate<T>: 断言型接口
 *      boolean test(T t)
 */
public class TestLambda2 {
    //Consumer<T>: 消费型接口
    @Test
    public void testConsumer() {
        happy(10000, d -> System.out.println(String.format("买东西消费：%s元", d)));
    }

    private void happy(double money, Consumer<Double> consumer) {
        consumer.accept(money);
    }

    //Supplier<T>: 供给型接口
    @Test
    public void testSupplier() {
        List<Integer> numList = getNumList(10, () -> (int) (Math.random() * 100));
        System.out.println(numList);
    }

    //需求：产生指定个数的整数，并放入集合中
    private List<Integer> getNumList(int num, Supplier<Integer> supplier) {
        return Stream.generate(supplier).limit(num).collect(Collectors.toList());
        /*List<Integer> list = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            list.add(supplier.get());
        }

        return list;*/
    }

    //Function<T, R>: 函数型接口
    @Test
    public void testFunction() {
        String newStr = strHandler("\t\t\t Hello, World   ", str -> str.trim());
        System.out.println(newStr);

        int length = strHandler("Good luck", str -> str.length());
        System.out.println(length);
    }

    //需求：用于处理字符串
    private <T, R> R strHandler(T t, Function<T, R> function) {
        return function.apply(t);
    }

    //Predicate<T>: 断言型接口
    @Test
    public void testPredicate() {
        List<String> list = Arrays.asList("Hello", "world", "lambda", "www", "ok");
        List<String> newStrList = filterStr(list, s -> s.length() > 3);
        System.out.println(newStrList);
    }

    //需求：将满足条件的字符串放入集合中
    private List<String> filterStr(List<String> list, Predicate<String> predicate) {
        return list.stream().filter(predicate).collect(Collectors.toList());
//        List<String> strList = new ArrayList<>();
//        for (String str : list) {
//            if (predicate.test(str)) {
//                strList.add(str);
//            }
//        }
//
//        return strList;
    }
}
```

### 1.2.4. Java8内置的其他函数式接口

![其他函数式接口](pics/其他函数式接口.png)

### 1.2.5. 方法引用与构造器引用

#### 1.2.5.1. 方法引用与构造器引用

方法引用：若Lambda体中的内容有方法已经实现了，我们可以使用“方法引用”（可以理解为方法引用是Lambda表达式的另外一种表现形式）

主要有三种语法格式：

1. 对象::实例方法名
2. 类::静态方法名
3. 类::实例方法名

注意：

1. Lambda体中调用方法的参数列表与返回值类型，要与函数式接口中抽象方法的函数列表和返回值类型保持一致
2. 若Lambda参数列表中的第一个参数是实例方法的调用者，而第二个及其余参数是实例方法的参数时，可以使用ClassName::methodName来调用

> 例1:
>> Lambda参数只有一个参数：
>> employees.stream().map(e -> e.getName()) 可以写为 employees.stream().map(Employee::getName)
>
> 例2：
>> Lambda参数两个参数：

```java
BiPredicate<String, String> biPredicate = (x, y) -> x.equals(y);
BiPredicate<String, String> biPredicate2 = String::equals;
System.out.println(biPredicate.test("abc", "abc"));
System.out.println(biPredicate2.test("abc", "def"));
```

#### 1.2.5.2. 构造器引用

格式：ClassName::new

**注意**：需要调用的构造器的参数列表要与函数式接口中抽象方法的参数列表保持一致！

#### 1.2.5.3. 数组引用

```java
//TestMethodRef.java
package test;

import java.io.PrintStream;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

public class TestMethodRef {

    //对象::实例方法名
    @Test
    public void test1() {
        Consumer<String> consumer = x -> System.out.println(x);
        PrintStream ps1 = System.out;
        Consumer<String> consumer1 = x -> ps1.println(x);

        //如果Lambda方法体中实现的方法已经有类函数实现了，则可以直接使用方法引用
        //要求引用函数参数和返回值和接口函数一致，则如上写法可以写成如下：
        PrintStream ps2 = System.out;
        Consumer<String> consumer2 = ps2::println; //System.out::println

        consumer2.accept("abcdef");
    }

    @Test
    public void test2() {
        Employee emp = new Employee("zhangsan", 30, 1234);
        Supplier<String> supplier = emp::getName; //() -> emp.getName();
        String str = supplier.get();
        System.out.println(str);

        Supplier<Integer> supplier2 = emp::getAge;
        System.out.println(supplier2.get());
    }

    //类::静态方法名
    @Test
    public void test3() {
        Comparator<Integer> comparator = Integer::compare; //(x, y) -> Integer.compare(x, y)
        System.out.println(comparator.compare(3, 1));
    }

    //类::实例方法名
    @Test
    public void test4() {
        Comparator<Integer> comparator2 = Integer::compareTo; //(x, y) -> x.compareTo(y)
        System.out.println(comparator2.compare(3, 1));

        BiPredicate<String, String> biPredicate = (x, y) -> x.equals(y);
        BiPredicate<String, String> biPredicate2 = String::equals;
        System.out.println(biPredicate.test("abc", "abc"));
        System.out.println(biPredicate2.test("abc", "def"));
    }

    //构造器引用
    @Test
    public void test5() {
        Supplier<Employee> supplier = () -> new Employee("zhangsan", 30, 1234);
        //构造器引用的方式
        Supplier<Employee> supplier2 = Employee::new;//引用无参数构造函数
        System.out.println(supplier2.get());

        Function<Integer, Employee> function = x -> new Employee(x);
        //构造器引用的方式
        Function<Integer, Employee> function2 = Employee::new;//引用一个参数的构造函数
        System.out.println(function2.apply(101));

        BiFunction<Integer, Integer, Employee> biFunction = Employee::new;//引用两个参数的构造函数
    }

    //数组引用
    @Test
    public void testArrayRef() {
        //同IntFunction<String[]> function2 = String[]::new; Stream.toArray(String[]::new)
        Function<Integer, String[]> function = String[]::new; // x -> new String[x]
        String[] strings = function.apply(10);
        System.out.println(strings.length);
    }

}
```

### 1.2.6. 创建Stream

### 1.2.7. Stream筛选与切片

多个中间操作可以连接起来形成一个流水线，除非流水线上触发终止操作，否则中间操作不会执行任何的处理！而在终止操作时一次性全部处理，称为“惰性求值”（延迟加载）

筛选与切片：

1. filter
2. limit
3. skip(n) --跳过元素，返回一个扔掉了前n个元素的流。若流中元素不足n个，则返回一个空流，与limit(n)互补
4. distinct --筛选，通过流所生成元素的hashCode()和equals()，去除重复元素

```java
package test;

import test.Employee.Status;

import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

public class TestStreamApi {
    private List<Employee> employees = Arrays.asList(
        new Employee("张三", 18, 9999.99, Status.BUSY),
        new Employee("李四", 50, 5555.55, Status.FREE),
        new Employee("王五", 40, 8888.88, Status.BUSY),
        new Employee("赵六", 15, 4444.44, Status.VOCATIONAL),
        new Employee("田七", 25, 3333.33, Status.VOCATIONAL),
        new Employee("孙八", 29, 6666.66, Status.BUSY)
    );

    //内部迭代：迭代操作由Stream API完成
    @Test
    public void test1() {
        //中间操作，不会执行任何操作
        Stream<Double> doubleStream = Stream.generate(Math::random).limit(10).peek(System.out::println);
        //终止操作：一次性执行全部内容，即“惰性求值”。在没有执行终止操作前，流没有执行，不会打印输出
        doubleStream.forEach(System.out::println);
    }

    //外部迭代
    @Test
    public void test2() {
        Iterator<Integer> it = Arrays.asList(1, 2, 3).iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    /**
     * 多个中间操作可以连接起来形成一个流水线，除非流水线上触发终止操作，否则中间操作不会执行任何的处理！而在终止操作时一次性全部处理，称为“惰性求值”（延迟加载）
     * 筛选与切片
     * filter
     * limit
     * skip(n) --跳过元素，返回一个扔掉了前n个元素的流。若流中元素不足n个，则返回一个空流，与limit(n)互补
     * distinct --筛选，通过流所生成元素的hashCode()和equals()，去除重复元素
     */
    @Test
    public void testShortCircuit() {
        IntStream.range(0, 20).filter(x -> {
            System.out.println("filter: " + x); //短路特性，类似&& ||，找到满足的元素后，后续元素不再处理
            return x % 2 == 0;
        })
            .limit(5)
            .forEach(System.out::println);
    }
    /*
     * 输出：
     * filter: 0
     * 0
     * filter: 1
     * filter: 2
     * 2
     * filter: 3
     * filter: 4
     * 4
     * filter: 5
     * filter: 6
     * 6
     * filter: 7
     * filter: 8
     * 8
     * */

    @Test
    public void testLimitSkip() {
        //如下limit和skip的组合效果一样
        List<Integer> integers = IntStream.iterate(1, x -> x + 1).limit(15).skip(10).boxed().collect(Collectors.toList());
        List<Integer> integers2 =
            IntStream.iterate(1, x -> x + 1).skip(10).limit(5).boxed().collect(Collectors.toList());
    }

    @Test
    public void testDistinct() {
        //并不要求重复元素一定相邻
        Stream.of(1, 2, 1, 5, 7, 7, 5).distinct().forEach(System.out::println);
    }

    @Test
    public void testObjectDistinct() {
        employees.stream().distinct().forEach(System.out::println); //Employee类需要提供equals和hashCode方法
    }

    @Test
    public void testMax() {
        System.out.println(Stream.of(1, 2, 1, 5, 7, 7, 5).max(Integer::compareTo));
        System.out.println(Stream.of(1, 2, 1, 5, 7, 7, 5).max(Integer::compare));
    }

    @Test
    public void testReduce() {
        int sum = IntStream.range(0, 11).reduce(0, Integer::sum); //reduce有初始值0，所以结果不会为空，为int
        System.out.println(sum);

        //reduce没有初始值，stream可能为会，所以结果为Optional
        Optional<Double> sum2 = employees.stream().map(Employee::getSalary).reduce(Double::sum);
        System.out.println(sum2);

        Double sum3 = employees.stream().reduce(0d, (x, y) -> x + y.getSalary(), Double::sum);
        System.out.println(sum3);
    }

    @Test
    public void testCollect() {
        //Collectors.toList返回一个收集器Collector，内部是ArrayList，再将每个元素调用add方法加入到容器里
        List<String> names = employees.stream().map(Employee::getName).collect(Collectors.toList());
        System.out.println(names);

        HashSet<Employee> employeeSet = this.employees.stream().collect(Collectors.toCollection(HashSet::new));
        System.out.println(employeeSet);

        HashSet<String> stringHashSet = this.employees.stream().map(Employee::getName).collect(Collectors.toCollection(HashSet::new));
        System.out.println(stringHashSet);
    }

    @Test
    public void testCollectorsCounting() {
        //总数
        //同employees.stream().count()
        //counting实现：reducing(0L, e -> 1L, Long::sum)，每个元素map成1，再求和
        Long count = employees.stream().collect(Collectors.counting());
        System.out.println(count);

        //Collectors.reducing实现
        int sumAge = employees.stream().collect(Collectors.reducing(0, Employee::getAge, Integer::sum));
        System.out.println(sumAge);

        //平均值
        Double averageSalary = employees.stream().collect(Collectors.averagingDouble(Employee::getSalary));
        System.out.println(averageSalary);

        //总和
        Double sumSalary = employees.stream().collect(Collectors.summingDouble(Employee::getSalary));
        System.out.println(sumSalary);

        //最大值
//        Comparator<Employee> comparator = (e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary());
        Optional<Employee> maxSalary = employees.stream().collect(Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary)));
        System.out.println(maxSalary);

        //最小值
        Optional<Double> minSalary = employees.stream().map(Employee::getSalary).collect(Collectors.minBy(Double::compare));
        System.out.println(minSalary);
    }

    //获得多个统计结果
    @Test
    public void testSummarizing() {
        DoubleSummaryStatistics dss = employees.stream().collect(Collectors.summarizingDouble(Employee::getSalary));
        System.out.println(dss.getMax());
        System.out.println(dss.getMin());
        System.out.println(dss.getAverage());
    }

    //分组
    @Test
    public void testGroupingBy() {
        //按年龄分组
        Map<Integer, List<Employee>> map = employees.stream().collect(Collectors.groupingBy(Employee::getAge));
        System.out.println(map);
        Map<Integer, List<Employee>> map2 = employees.stream().collect(Collectors.groupingBy(Employee::getAge,
            Collectors.toCollection(LinkedList::new)));
        Map<Integer, Set<Employee>> map3 = employees.stream().collect(Collectors.groupingBy(Employee::getAge,
            LinkedHashMap::new, Collectors.toCollection(HashSet::new)));
        System.out.println(map3);

        //按年龄分组，并且value存的是名字List
        Map<Integer, List<String>> ageNamesMap = employees.stream().collect(Collectors.groupingBy(Employee::getAge, Collectors.mapping(Employee::getName,
            Collectors.toList())));
        System.out.println(ageNamesMap);

        //按Status分组，Value为对应所有员工工资求和
        Map<Status, Double> statusSumSalaryMap = employees.stream().collect(Collectors.groupingBy(Employee::getStatus,
            Collectors.reducing(0d, Employee::getSalary, Double::sum)));
        System.out.println(statusSumSalaryMap);

        //或用Collectors.summingDouble
        Map<Status, Double> statusSumSalaryMap2 = employees.stream().collect(Collectors.groupingBy(Employee::getStatus,
            Collectors.summingDouble(Employee::getSalary)));
        System.out.println(statusSumSalaryMap2);
    }

    //多级分组
    @Test
    public void testMultiGroupingBy() {
        Map<Status, Map<String, List<Employee>>> map = employees.stream().collect(Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy(e -> {
            if (e.getAge() <= 35) {
                return "青年";
            } else if (e.getAge() < 50) {
                return "中年";
            } else {
                return "老年";
            }
        })));
        System.out.println(map);
    }

    //分区，满足条件一个区，不满足条件一个区
    @Test
    public void testPartitionBy() {
        Map<Boolean, List<Employee>> partition = employees.stream().collect(Collectors.partitioningBy(e -> e.getSalary() > 8000));
        System.out.println(partition);
    }

    //多级分区
    @Test
    public void testMultiPartitionBy() {
        Map<Boolean, Map<Boolean, List<Employee>>> map = employees.stream().collect(Collectors.partitioningBy(e -> e.getSalary() > 8000,
            Collectors.partitioningBy(e -> e.getStatus() == Status.BUSY)));
        System.out.println(map);
    }

    //连接
    @Test
    public void testJoin() {
        String names = employees.stream().map(Employee::getName).collect(Collectors.joining("-"));
        System.out.println(names);
    }

    //收集并转换
    @Test
    public void testCollectAndThen() {
        int how= employees.stream().collect(Collectors.collectingAndThen(Collectors.toList(), List::size));
        System.out.println(how);
    }
}
```

### 1.2.8. 映射

映射：

1. map
2. flatMap：接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流

### 1.2.9. 排序

排序：

1. sorted()
2. sorted(Comparator com)

### 1.2.10. 终止操作

1. allMatch(Predicate p)
2. anyMatch(Predicate p)
3. noneMatch(Predicate p)
4. findFirst()
5. findAny() --返回当前流中的任意元素
6. count()
7. max(Comparator c)
8. min(Comparator c)
9. forEach(Consumer c)

### 1.2.11. 归约

reduce(T identity, BinaryOperator) / reduce(BinaryOperator) --可以将流中元素反复结合起来，得到一个值

**备注**：map和reduce的连接通常称为map-reduce模式，因google用它来进行网络搜索而出名。

```java
@Test
public void testReduce() {
    int sum = IntStream.range(0, 11).reduce(0, Integer::sum); //reduce有初始值0，所以结果不会为空，为int
    System.out.println(sum);

    //reduce没有初始值，stream可能为会，所以结果为Optional
    Optional<Double> sum2 = employees.stream().map(Employee::getSalary).reduce(Double::sum);
    System.out.println(sum2);
}
```

### 1.2.12. 收集

collect--将流转换为其他形式。接收一个Collector接口的实现，用于给Stream中元素做汇总的方法。

Collector 接口中方法的实现决定了如何对流执行收集操作(如收集到 List、 Set、 Map)。但是 Collectors 实用类提供了很多静态方法，可以方便地创建常见收集器实例， 具体方法与实例如下表：

方法|返回类型|作用
--|--|--
toList|`List<T>`|把流中元素收集到List
`List<Employee> emps= list.stream().collect(Collectors.toList());`||
toSet|`Set<T>`|把流中元素收集到Set
`Set<Employee> emps= list.stream().collect(Collectors.toSet());`||
toCollection|`Collection<T>`|把流中元素收集到创建的集合
`Collection<Employee> emps=list.stream().collect(Collectors.toCollection(ArrayList::new));`||
counting|Long|计算流中元素的个数
`long count = list.stream().collect(Collectors.counting());`||
summingInt|Integer|对流中元素的整数属性求和
`int total=list.stream().collect(Collectors.summingInt(Employee::getSalary));`||
averagingInt|Double|计算流中元素Integer属性的平均值
`doubleavg= list.stream().collect(Collectors.averagingInt(Employee::getSalary));`||
summarizingInt|IntSummaryStatistics|收集流中Integer属性的统计值。如：平均值
`IntSummaryStatisticsiss= list.stream().collect(Collectors.summarizingInt(Employee::getSalary));`||
joining|String|连接流中每个字符串
`String str= list.stream().map(Employee::getName).collect(Collectors.joining());`||
maxBy|`Optional<T>`|根据比较器选择最大值
`Optional<Emp> max= list.stream().collect(Collectors.maxBy(comparingInt(Employee::getSalary)));`||
minBy|`Optional<T>`|根据比较器选择最小值
`Optional<Emp> min = list.stream().collect(Collectors.minBy(comparingInt(Employee::getSalary)));`||
reducing|归约产生的类型|从一个作为累加器的初始值开始，利用BinaryOperator与流中元素逐个结合，从而归约成单个值
`int total=list.stream().collect(Collectors.reducing(0, Employee::getSalar, Integer::sum));`||
collectingAndThen|转换函数返回的类型|包裹另一个收集器，对其结果转换函数
`int how= list.stream().collect(Collectors.collectingAndThen(Collectors.toList(), List::size));`||
groupingBy|`Map<K, List<T>>`|根据某属性值对流分组，属性为K，结果为V
`Map<Emp.Status, List<Emp>> map= list.stream().collect(Collectors.groupingBy(Employee::getStatus));`||
partitioningBy|`Map<Boolean, List<T>>`|根据true或false进行分区
`Map<Boolean,List<Emp>>vd= list.stream().collect(Collectors.partitioningBy(Employee::getManage));`||

### 1.2.13. Stream练习

```java
//Trader.java
package trade;

//交易员类
public class Trader {

    private String name;
    private String city;

    public Trader() {
    }

    public Trader(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Trader [name=" + name + ", city=" + city + "]";
    }

}

//Transaction.java
package trade;

//交易类
public class Transaction {

    private Trader trader;
    private int year;
    private int value;

    public Transaction() {
    }

    public Transaction(Trader trader, int year, int value) {
        this.trader = trader;
        this.year = year;
        this.value = value;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Transaction [trader=" + trader + ", year=" + year + ", value="
            + value + "]";
    }
}

//TestTransaction.java
package trade;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class TestTransaction {

    List<Transaction> transactions = null;

    @Before
    public void before() {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        transactions = Arrays.asList(
            new Transaction(brian, 2011, 300),
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
        );
    }

    //1. 找出2011年发生的所有交易， 并按交易额排序（从低到高）
    @Test
    public void test1() {
        List<Transaction> transactions = this.transactions.stream()
            .filter(e -> e.getYear() == 2011)
            .sorted(Comparator.comparingInt(Transaction::getValue)) //逆序Comparator.comparingInt(Transaction::getValue).reversed()
            .collect(Collectors.toList());
        System.out.println(transactions);
    }

    //2. 交易员都在哪些不同的城市工作过？
    @Test
    public void test2() {
//        transactions.stream().map(Transaction::getTrader).map(Trader::getCity).distinct().forEach(System.out::println);
        transactions.stream().map(e -> e.getTrader().getCity()).distinct().forEach(System.out::println);
    }

    //3. 查找所有来自剑桥的交易员，并按姓名排序
    @Test
    public void test3() {
        transactions.stream().filter(e -> "Cambridge".equals(e.getTrader().getCity()))
            .map(Transaction::getTrader)
            .distinct()
            .sorted(Comparator.comparing(Trader::getName))
            .forEach(System.out::println);
    }

    //4. 返回所有交易员的姓名字符串，按字母顺序排序
    @Test
    public void test4() {
        transactions.stream().map(e -> e.getTrader().getName()).distinct().sorted().forEach(System.out::println);

        System.out.println("--------------------------");

        String name = transactions.stream().map(e -> e.getTrader().getName()).distinct().sorted().reduce("", String::concat);
        System.out.println(name);

        String name2 = transactions.stream().map(e -> e.getTrader().getName()).distinct().sorted().collect(Collectors.joining());
        System.out.println(name2);

        System.out.println("--------------------------");

        transactions.stream().map(e -> e.getTrader().getName()).flatMap(TestTransaction::StrToCharStream).sorted().forEach(System.out::print);
        System.out.println();
        transactions.stream().map(e -> e.getTrader().getName()).flatMap(TestTransaction::StrToStrStream).sorted(String::compareToIgnoreCase).forEach(System.out::print);
    }

    private static Stream<Character> StrToCharStream(String name) {
        return name.chars().mapToObj(c -> (char) c); //转成char后，mapToObj方法会将char转成Character
    }

    private static Stream<String> StrToStrStream(String name) {
        return name.chars().mapToObj(c -> (char) c).map(c -> c.toString()); //map(Object::toString)
    }

    //5. 有没有交易员是在米兰工作的？
    @Test
    public void test5() {
        System.out.println(transactions.stream().anyMatch(e -> "Milan".equals(e.getTrader().getCity())));
    }

    //6. 打印生活在剑桥的交易员的所有交易额
    @Test
    public void test6() {
        Optional<Integer> tradeSumValue = transactions.stream().filter(e -> "Cambridge".equals(e.getTrader().getCity()))
            .map(Transaction::getValue)
            .reduce(Integer::sum);
        System.out.println(tradeSumValue);
    }

    //7. 所有交易中，最高的交易额是多少
    @Test
    public void test7() {
        Optional<Integer> max = transactions.stream().map(Transaction::getValue).max(Integer::compare);
        System.out.println(max);
    }

    //8. 找到交易额最小的交易
    @Test
    public void test8() {
        Optional<Transaction> min = transactions.stream().min(Comparator.comparing(Transaction::getValue, Integer::compare));
        System.out.println(min);

        Optional<Transaction> min1 =
            transactions.stream().collect(Collectors.minBy(Comparator.comparing(Transaction::getValue, Integer::compare)));
        System.out.println(min1);
    }
}
```

## 1.3. 并行流与串行流

原来的多线程如果一个线程阻塞了，其它多线程即便执行完了，也会等待全部线程完成。

fork/join模式，采用“工作窃取”模式（work-stealing）：

当执行新的任务时它可以将其拆分分成更小的任务执行，并将小任务加到线程队列中，然后再从一个随机线程的队列中偷一个并把它放在自己的队列中。

相对于一般的线程池实现,fork/join框架的优势体现在对其中包含的任务的处理方式上.在一般的线程池中,如果一个线程正在执行的任务由于某些原因无法继续运行,那么该线程会处于等待状态.而在fork/join框架实现中,如果某个子问题由于等待另外一个子问题的完成而无法继续运行.那么处理该子问题的线程会主动寻找其他尚未运行的子问题来执行.这种方式减少了线程的等待时间,提高了性能。

```java
//ForJoinCalculate.java
package test;

import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

//递归任务
public class ForJoinCalculate extends RecursiveTask<Long> {
    private static final long serialVersionUID = 6347643815009683165L;

    private long start;
    private long end;

    private static final long THRESHOLD = 10000;

    public ForJoinCalculate(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long length = end - start;
        if (length <= THRESHOLD) {
            return LongStream.rangeClosed(start, end).sum();
        } else {
            long middle = (start + end) / 2;
            ForJoinCalculate left = new ForJoinCalculate(start, middle);
            left.fork();//拆分子任务，同时压入线程队列

            ForJoinCalculate right = new ForJoinCalculate(middle + 1, end);
            right.fork();

            return left.join() + right.join();
        }
    }
}

//ForJoinCalculateTest.java
package test;


import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

import org.junit.Test;

public class ForJoinCalculateTest {

    private static final long END_VALUE = 20000000000L;

    /**
     * ForkJoin 框架，1e8:85，1e10:2605，拆线程耗时，所以数字越大，效果越明显
     */
    @Test
    public void test1() {
        Instant start = Instant.now();

        ForkJoinPool pool = new ForkJoinPool();
        ForkJoinTask<Long> task = new ForJoinCalculate(0, END_VALUE);
        Long sum = pool.invoke(task);
        System.out.println(sum);

        Instant end = Instant.now();
        System.out.println(String.format("耗费时间为：%s", Duration.between(start, end).toMillis())); //85
    }

    /**
     * 普通 for，1e8:49，1e10:2799，但是经本地实验，发现for也会用8个线程，而不是单线程，是否JVM或Windows10做了什么优化？
     */
    @Test
    public void test2() {
        Instant start = Instant.now();

        long sum = 0L;
        for (long i = 0; i <= END_VALUE; i++) {
            sum += i;
        }
        System.out.println(sum);

        Instant end = Instant.now();
        System.out.println(String.format("耗费时间为：%s", Duration.between(start, end).toMillis())); //49
    }

    /**
     * Java8 2e10:parallel():1889, sequential()或者不写:7172
     */
    @Test
    public void test3() {
        Instant start = Instant.now();

        long sum = LongStream.rangeClosed(0, END_VALUE).parallel().sum();
        System.out.println(sum);

        Instant end = Instant.now();
        System.out.println(String.format("耗费时间为：%s", Duration.between(start, end).toMillis())); //49
    }
}
```

## 1.4. 强大的Stream API

## 1.5. 便于并行

## 1.6. 最大化减少空指针异常

## 1.7. 优化了内存结构

方法区由堆中的永久区移到了物理内存中。

![8内存结构](pics/8内存结构.png)
