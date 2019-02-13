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

public class Employee {
    private String name;
    private int age;
    private double salary;

    public Employee(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
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

    @Override
    public String toString() {
        return "Employee{" +
            "name='" + name + '\'' +
            ", age=" + age +
            ", salary=" + salary +
            '}';
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

## 1.3. 强大的Stream API

## 1.4. 便于并行

## 1.5. 最大化减少空指针异常

## 1.6. 优化了内存结构

方法区由堆中的永久区移到了物理内存中。

![8内存结构](pics/8内存结构.png)
