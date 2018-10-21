# 1. 常用排序算法

## 1.1. 插入排序

时间代价：Θ(n*n)

插入扑克一样，每次把新来的一个对象找到它应该所在的对应位置。

```java
//InsertionSort.java
package algorithm.sort;

import java.util.List;

public class InsertionSort {
    public static <T extends Comparable<T>> void sort(List<T> eleList) {
        for (int i = 0; i < eleList.size(); i++) {
            for (int j = i; j >= 1; j--) {
                int previousIndex = j - 1;
                if (eleList.get(j).compareTo(eleList.get(previousIndex)) < 0) {
                    T tmp = eleList.get(previousIndex);
                    eleList.set(previousIndex, eleList.get(j));
                    eleList.set(j, tmp);
                }
            }
        }
    }
}

//InsertionSortTest.java
package algorithm.sort;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InsertionSortTest {
    @Test
    public void testSortInt() {
        List<Integer> integers = Arrays.asList(3, 5, 4, 1, 2);
        InsertionSort.sort(integers);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), integers);
    }

    @Test
    public void testSortString() {
        List<String> strings = Arrays.asList("e", "a", "d", "c", "b");
        InsertionSort.sort(strings);
        assertEquals(Arrays.asList("a", "b", "c", "d", "e"), strings);
    }
}
```

## 1.2. 分治法

### 1.2.1. 合并排序

C++算法里的inplace_merge

分解：将n个元素分成各含n/2 个元素的子序列；

解决：用合并排序法对两个子序列递归地排序；

合并：合并两个已排序的子序列以得到排序结果。

Merge过程的时间代码为Θ(n)，n=r-p+1 是待合并的元素个数。

```java
//合并排序中的合并两个有序序列的算法
//InplaceMergeSort.java
package algorithm.sort;

import java.util.ArrayList;
import java.util.List;

public class InplaceMergeSort {
    public static <T extends Comparable<T>> void sort(List<T> eleList, int mergeIndex) {
        List<T> firstSortedList = new ArrayList<>(mergeIndex);
        firstSortedList.addAll(eleList.subList(0, mergeIndex));//subList获取的是原List的引用，下面循环修改会导致原List值变化，但是allAll也不是深拷贝，如果List中的对象是引用类，则也会有问题
        List<T> secondSortedList = new ArrayList<>(eleList.size() - mergeIndex);
        secondSortedList.addAll(eleList.subList(mergeIndex, eleList.size()));

        int firstIndex = 0, secondIndex = 0;
        for (int eleIndex = 0; eleIndex < eleList.size(); ++eleIndex) {
            if (firstIndex == firstSortedList.size()) {
                eleList.set(eleIndex, secondSortedList.get(secondIndex));
                ++secondIndex;
            } else if (secondIndex == secondSortedList.size()) {
                eleList.set(eleIndex, firstSortedList.get(firstIndex));
                ++firstIndex;
            } else if (firstSortedList.get(firstIndex).compareTo(secondSortedList.get(secondIndex)) <= 0) {
                eleList.set(eleIndex, firstSortedList.get(firstIndex));
                ++firstIndex;
            } else {
                eleList.set(eleIndex, secondSortedList.get(secondIndex));
                ++secondIndex;
            }
        }
    }
}

//InplaceMergeSortTest.java
package algorithm.sort;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InplaceMergeSortTest {
    @Test
    public void testInplaceMergeSort() {
        List<Integer> integers = Arrays.asList(3, 2);
        InplaceMergeSort.sort(integers, 1);
        List<Integer> sortedIntegers = Arrays.asList(2, 3);
        assertEquals(sortedIntegers, integers);
    }

    @Test
    public void testInplaceMergeSort2() {
        List<Integer> integers = Arrays.asList(2, 15, 10, 20);
        InplaceMergeSort.sort(integers, 2);
        List<Integer> sortedIntegers = Arrays.asList(2, 10, 15, 20);
        assertEquals(sortedIntegers, integers);
    }

    @Test
    public void testInplaceMergeSort3() {
        List<Integer> integers = Arrays.asList(30, 40, 10, 20);
        InplaceMergeSort.sort(integers, 2);
        List<Integer> sortedIntegers = Arrays.asList(10, 20, 30, 40);
        assertEquals(sortedIntegers, integers);
    }

    @Test
    public void testInplaceMergeSort4() {
        List<Integer> integers = Arrays.asList(30, 40, 50, 10, 20);
        InplaceMergeSort.sort(integers, 3);
        List<Integer> sortedIntegers = Arrays.asList(10, 20, 30, 40, 50);
        assertEquals(sortedIntegers, integers);
    }

    @Test
    public void testInplaceMergeSortString() {
        List<String> strings = Arrays.asList("a", "b", "e", "c", "d");
        InplaceMergeSort.sort(strings, 3);
        List<String> sortedStrings = Arrays.asList("a", "b", "c", "d", "e");
        assertEquals(sortedStrings, strings);
    }

    @Test
    public void testInplaceMergeSortIntAfterShuffle() {
        List<Integer> integers = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());
        Collections.shuffle(integers);

        int mergeIndex = 5;
        List<Integer> firstSubList = integers.subList(0, mergeIndex);
        Collections.sort(firstSubList);
        List<Integer> secondSubList = integers.subList(mergeIndex, integers.size());
        Collections.sort(secondSubList);

        System.out.println(integers);//已经随机打乱顺序的List

        InplaceMergeSort.sort(integers, mergeIndex);
        List<Integer> sortedIntegers = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());
        assertEquals(sortedIntegers, integers);
    }
}

```