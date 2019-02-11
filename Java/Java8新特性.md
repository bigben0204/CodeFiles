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

## 1.3. 强大的Stream API

## 1.4. 便于并行

## 1.5. 最大化减少空指针异常

## 1.6. 优化了内存结构

方法区由堆中的永久区移到了物理内存中。

![8内存结构](pics/8内存结构.png)
