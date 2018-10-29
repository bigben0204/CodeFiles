# 1. 常用排序算法

辅助类：

```java
//SortUtils.java
package algorithm.sort.utils;

import java.util.List;

public class SortUtils {
    public static <T extends Comparable<T>> void swap(List<T> eleList, int index1, int index2) {
        T tmp = eleList.get(index1);
        eleList.set(index1, eleList.get(index2));
        eleList.set(index2, tmp);
    }
}
```

## 1.1. 插入排序

时间代价：Θ(n*n)

插入扑克一样，每次把新来的一个对象找到它应该所在的对应位置。

```java
//InsertionSort.java
package algorithm.sort.insertion;

import algorithm.sort.utils.SortUtils;

import java.util.List;

public class InsertionSort {
    public static <T extends Comparable<T>> void sort(List<T> eleList) {
        for (int i = 0; i < eleList.size(); i++) {
            for (int j = i; j >= 1; j--) {
                int previousIndex = j - 1;
                if (eleList.get(j).compareTo(eleList.get(previousIndex)) < 0) {
                    SortUtils.swap(eleList, j, previousIndex);
                }
            }
        }
    }
}


//InsertionSortTest.java
package algorithm.sort.insertion;

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

### 1.2.1. 合并排序合并两个有序序列的算法

C++算法里的inplace_merge

分解：将n个元素分成各含n/2 个元素的子序列；

解决：用合并排序法对两个子序列递归地排序；

合并：合并两个已排序的子序列以得到排序结果。

Merge过程的时间代码为Θ(n)，n=r-p+1 是待合并的元素个数。

```java
//合并排序中的合并两个有序序列的算法
//InplaceMergeSort.java
package algorithm.sort.merge;

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
package algorithm.sort.merge;

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

### 1.2.2. 合并排序算法：

结合上面的InplaceMerge，完整的的合并排序算法：

合并排序的运行时间为Θ(nlgn)，在最坏情况下要比插入排序（其运行时间为Θ(n*n)）好。

```java
//MergeSort.sort
package algorithm.sort.merge;

import java.util.List;

public class MergeSort {
    public static <T extends Comparable<T>> void sort(List<T> eleList) {
        if (eleList.size() == 1) {
            return;
        } else {
            int halfSize = eleList.size() / 2;
            sort(eleList.subList(0, halfSize));
            sort(eleList.subList(halfSize, eleList.size()));
            InplaceMergeSort.sort(eleList, halfSize);
        }
    }
}

//MergeSortTest.java
package algorithm.sort.merge;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MergeSortTest {
    @Test
    public void testSort() {
        List<Integer> integers = Arrays.asList(3, 2);
        MergeSort.sort(integers);
        List<Integer> sortedIntegers = Arrays.asList(2, 3);
        assertEquals(sortedIntegers, integers);
    }

    @Test
    public void testSort2() {
        List<Integer> integers = Arrays.asList(3, 2, 4);
        MergeSort.sort(integers);
        List<Integer> sortedIntegers = Arrays.asList(2, 3, 4);
        assertEquals(sortedIntegers, integers);
    }

    @Test
    public void testSort3() {
        List<Integer> integers = Arrays.asList(3, 5, 2, 4);
        MergeSort.sort(integers);
        List<Integer> sortedIntegers = Arrays.asList(2, 3, 4, 5);
        assertEquals(sortedIntegers, integers);
    }

    @Test
    public void testSortByShuffle() {
        List<Integer> integers = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());
        Collections.shuffle(integers);

        System.out.println(integers);//已经随机打乱顺序的List

        MergeSort.sort(integers);
        List<Integer> sortedIntegers = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());
        assertEquals(sortedIntegers, integers);
    }
}
```

## 1.3. 冒泡排序

时间代价：Θ(n*n)

冒泡排序（bubblesort）算法，重复地交换相邻的两个反序元素：

```java
//BubbleSort.java
package algorithm.sort.bubble;

import algorithm.sort.utils.SortUtils;

import java.util.List;

public class BubbleSort {
    public static <T extends Comparable<T>> void sort(List<T> eleList) {
        for (int i = 0; i < eleList.size(); i++) {
            for (int j = eleList.size() - 1; j > i; j--) {
                if (eleList.get(j).compareTo(eleList.get(j - 1)) < 0) {
                    SortUtils.swap(eleList, j, j - 1);
                }
            }
        }
    }
}


//BubbleSortTest.java
package algorithm.sort.bubble;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BubbleSortTest {
    @Test
    public void testSort() {
        List<Integer> integers = Arrays.asList(3, 5, 4, 1, 2);
        BubbleSort.sort(integers);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5), integers);
    }

    @Test
    public void testSortByShuffle() {
        List<Integer> integers = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());
        Collections.shuffle(integers);

        System.out.println(integers);//已经随机打乱顺序的List

        BubbleSort.sort(integers);
        List<Integer> sortedIntegers = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());
        assertEquals(sortedIntegers, integers);
    }
}
```

## 1.4. 堆排序

### 1.4.1. 堆排序算法

运行时间：O(nlgn)。
它是一种原地排序算法：在任何时候，数组中只有常数个元素存储在输入数组以外。可以结合合并排序和插入的优点。

```java
//HeapSort.java
package algorithm.sort.heap;

import algorithm.sort.utils.SortUtils;

import java.util.List;

public class HeapSort {
    public static <T extends Comparable<T>> void max_heapify(List<T> eleList, int i) {
        int left_i = 2 * (i + 1) - 1;
        int right_i = left_i + 1;

        int heap_size = eleList.size();
        int largest = i;
        if (left_i < heap_size && eleList.get(largest).compareTo(eleList.get(left_i)) < 0) {
            largest = left_i;
        }
        if (right_i < heap_size && eleList.get(largest).compareTo(eleList.get(right_i)) < 0) {
            largest = right_i;
        }

        if (largest != i) {
            SortUtils.swap(eleList, i, largest);
            max_heapify(eleList, largest);
        }
    }

    public static <T extends Comparable<T>> void build_max_heap(List<T> eleList) {
        for (int i = eleList.size() / 2 - 1; i >= 0; --i) {
            max_heapify(eleList, i);
        }
    }

    public static <T extends Comparable<T>> void heap_sort(List<T> eleList) {
        build_max_heap(eleList);
        for (int i = eleList.size() - 1; i > 0; --i) {
            SortUtils.swap(eleList, 0, i);
            max_heapify(eleList.subList(0, i), 0);
        }
    }
}

//HeapSortTest.java
package algorithm.sort.heap;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HeapSortTest {
    @Test
    public void testMaxHeapify() {
        List<Integer> integers = Arrays.asList(16, 4, 10, 14, 7, 9, 3, 2, 8, 1);
        HeapSort.max_heapify(integers, 1);
        assertEquals(Arrays.asList(16, 14, 10, 8, 7, 9, 3, 2, 4, 1), integers);
    }

    @Test
    public void testBuildMaxHeap() {
        List<Integer> integers = Arrays.asList(4, 1, 3, 2, 16, 9, 10, 14, 8, 7);
        HeapSort.build_max_heap(integers);
        assertEquals(Arrays.asList(16, 14, 10, 8, 7, 9, 3, 2, 4, 1), integers);
    }

    @Test
    public void testHeapSort() {
        List<Integer> integers = Arrays.asList(4, 1, 3, 2, 16, 9, 10, 14, 8, 7);
        Collections.shuffle(integers); //如果打乱顺序，则每一个序列都可以形成一个最大堆
        System.out.println(integers);
        HeapSort.heap_sort(integers);
        assertEquals(Arrays.asList(1, 2, 3, 4, 7, 8, 9, 10, 14, 16), integers);
    }
}
```

### 1.4.2. 优先级队列

```java
//PriorityQueue.java
package algorithm.sort.heap;

import algorithm.sort.utils.SortUtils;

import java.util.LinkedList;
import java.util.List;

public class PriorityQueue<T extends Comparable<T>> {
    private List<T> queueContainer = new LinkedList<>();
    private int heapSize = 0;

    public PriorityQueue() {
    }

    public T maximum() {
        checkQueueIsEmpty();
        return queueContainer.get(0);
    }

    public T extract_max() {
        checkQueueIsEmpty();
        T max = queueContainer.get(0);
        queueContainer.set(0, queueContainer.get(--heapSize));
        max_heapify(queueContainer.subList(0, heapSize), 0);
        return max;
    }

    public void increase_key(int i, T key) {
        checkNewKeyIndex(i);
        T currentKey = queueContainer.get(i);
        queueContainer.set(i, key);
        if (key.compareTo(currentKey) > 0) {
            //如果新的key大于原key，则不断与其父节点替换
            max_heapify_parent(queueContainer, i);
        } else {
            //如果新的key小于原key，则进行一次max_heapify。（算法导论书中并没有提到这一点）
            max_heapify(queueContainer, i);
        }
    }

    public void insert(T key) {
        increaseHeapSize(key);
        max_heapify_parent(queueContainer, heapSize - 1);
    }

    public int size() {
        return heapSize;
    }

    private void increaseHeapSize(T key) {
        if (heapSize >= queueContainer.size()) {
            queueContainer.add(key);
        } else {
            queueContainer.set(heapSize, key);
        }
        ++heapSize;
    }

    private int getParentIndex(int index) {
        return (index + 1) / 2 - 1;
    }

    private void checkNewKeyIndex(int i) {
        if (i >= heapSize) {
            throw new IndexOutOfBoundsException("Index is larger than queue length!");
        }
    }

    private void checkQueueIsEmpty() {
        if (heapSize <= 0) {
            throw new RuntimeException("Heap is empty!");
        }
    }

    private void max_heapify_parent(List<T> eleList, int i) {
        int currentIndex = i;
        int parentIndex = getParentIndex(currentIndex);
        while (parentIndex >= 0 && eleList.get(currentIndex).compareTo(eleList.get(parentIndex)) > 0) {
            SortUtils.swap(eleList, currentIndex, parentIndex);
            currentIndex = parentIndex;
            parentIndex = getParentIndex(currentIndex);
        }
    }

    private void max_heapify(List<T> eleList, int i) {
        int left_i = 2 * (i + 1) - 1;
        int right_i = left_i + 1;

        int heap_size = eleList.size();
        int largest = i;
        if (left_i < heap_size && eleList.get(largest).compareTo(eleList.get(left_i)) < 0) {
            largest = left_i;
        }
        if (right_i < heap_size && eleList.get(largest).compareTo(eleList.get(right_i)) < 0) {
            largest = right_i;
        }

        if (largest != i) {
            SortUtils.swap(eleList, i, largest);
            max_heapify(eleList, largest);
        }
    }
}

//PriorityQueueTest.java
package algorithm.sort.heap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PriorityQueueTest {
    private static PriorityQueue<Integer> INTEGER_PRIORITY_QUEUE;

    @Before
    public void setUp() throws Exception {
        INTEGER_PRIORITY_QUEUE = new PriorityQueue<>(); //如果不每个用例重复赋值，则INTEGER_PRIORITY_QUEUE中会有前一个用例的数据，互相影响
    }

    @Test(expected=RuntimeException.class)
    public void test_maximum_withEmptyQueue() {
        INTEGER_PRIORITY_QUEUE.maximum();
    }

    @Test(expected=RuntimeException.class)
    public void test_extract_max_withEmptyQueue() {
        INTEGER_PRIORITY_QUEUE.extract_max();
    }

    @Test
    public void test_extract_max() {
        INTEGER_PRIORITY_QUEUE.insert(10);
        int maximum = INTEGER_PRIORITY_QUEUE.extract_max();
        assertEquals(10, maximum);
        assertEquals(0, INTEGER_PRIORITY_QUEUE.size());
    }

    @Test
    public void test_increase_key_biggerNewKey() {
        INTEGER_PRIORITY_QUEUE.insert(5);
        INTEGER_PRIORITY_QUEUE.insert(10);
        INTEGER_PRIORITY_QUEUE.insert(7);
        INTEGER_PRIORITY_QUEUE.increase_key(2, 12);
        assertEquals(3, INTEGER_PRIORITY_QUEUE.size());
        int maximum = INTEGER_PRIORITY_QUEUE.maximum();
        assertEquals(12, maximum);
    }

    @Test
    public void test_increase_key_smallerNewKey() {
        INTEGER_PRIORITY_QUEUE.insert(5);
        INTEGER_PRIORITY_QUEUE.insert(10);
        INTEGER_PRIORITY_QUEUE.insert(7);
        INTEGER_PRIORITY_QUEUE.increase_key(0, 3);
        assertEquals(3, INTEGER_PRIORITY_QUEUE.size());
        int maximum = INTEGER_PRIORITY_QUEUE.maximum();
        assertEquals(7, maximum);
    }

    @Test
    public void test_insert() {
        INTEGER_PRIORITY_QUEUE.insert(10);
        int maximum = INTEGER_PRIORITY_QUEUE.maximum();
        assertEquals(10, maximum);
        assertEquals(1, INTEGER_PRIORITY_QUEUE.size());
    }

    @Test
    public void test_insert2() {
        INTEGER_PRIORITY_QUEUE.insert(20);
        int maximum = INTEGER_PRIORITY_QUEUE.maximum();
        assertEquals(20, maximum);
        assertEquals(1, INTEGER_PRIORITY_QUEUE.size());
    }

    @Test
    public void test_insert3() {
        INTEGER_PRIORITY_QUEUE.insert(5);
        int maximum = INTEGER_PRIORITY_QUEUE.maximum();
        assertEquals(5, maximum);
        assertEquals(1, INTEGER_PRIORITY_QUEUE.size());
    }


    @Test
    public void test_insert_multiKeys() {
        INTEGER_PRIORITY_QUEUE.insert(5);
        INTEGER_PRIORITY_QUEUE.insert(10);
        INTEGER_PRIORITY_QUEUE.insert(7);
        int maximum = INTEGER_PRIORITY_QUEUE.maximum();
        assertEquals(10, maximum);
        assertEquals(3, INTEGER_PRIORITY_QUEUE.size());
    }
}
```

## 1.5. 快速排序

快速排序对包含n个数的输入数组，最坏情况运行时间为Θ(n^2)。

虽然最坏情况运行时间比较差，但快速排序通过是用于排序的最佳的实用选择，这是因为其平均性能相当好：期望的运行时间为Θ(nlgn)，且Θ(nlgn)记号中隐含的常数因子很小。另外，还能够就地排序。

### 1.5.1. 快速排序的一般版本

像合并排序一样，也是基于2.3.1 节介绍的分治模式的。

**分解**：将A[p..r]划分成两个子数组A[p..q-1]和A[q+1 ..r]，使得A[p..q-1]中的每个元素都小于等于A(q)，而且，小于等于A[q+1 ..r]中的元素。下标q也在这个划分过程中进行计算。

**注意**：位置q所对应的元素已经划分出来的两个数组的中间元素，不需要再处理。

**解决**：通过递归调用快速排序，对子数组A[p..q-1]和A[q+1 ..r]排序。

**合并**：因为两个子数组是就地排序的，将它们的合并不需要操作：整个数组A[p..r]已排序。

```java
//QuickSort.java
package algorithm.sort.quick;

import algorithm.sort.utils.SortUtils;

import java.util.List;
import java.util.Random;

public class QuickSort {
    public static <T extends Comparable<T>> int partition(List<T> eleList) {
        int size = eleList.size();
        T lastEle = eleList.get(size - 1);
        int i = -1;
        for (int j = 0; j < size - 1; ++j) {
            if (eleList.get(j).compareTo(lastEle) < 0) {
                SortUtils.swap(eleList, ++i, j);
            }
        }
        SortUtils.swap(eleList, ++i, size - 1);
        return i;
    }

    public static <T extends Comparable<T>> int randomized_partition(List<T> eleList) {
        int size = eleList.size();
        int randomIndex = new Random().nextInt(size);
        SortUtils.swap(eleList, randomIndex, size - 1);
        return partition(eleList);
    }

    public static <T extends Comparable<T>> void sort(List<T> eleList) {
        if (eleList.size() <= 1) {
            return;
        }
        int q = partition(eleList); //注意：位置q所对应的元素已经划分出来的两个数组的中间元素，不需要再处理。
        sort(eleList.subList(0, q));
        sort(eleList.subList(q + 1, eleList.size()));
    }

    public static <T extends Comparable<T>> void randomized_sort(List<T> eleList) {
        if (eleList.size() <= 1) {
            return;
        }
        int q = randomized_partition(eleList);
        randomized_sort(eleList.subList(0, q));
        randomized_sort(eleList.subList(q + 1, eleList.size()));
    }
}

//QuickSortTest.java
package algorithm.sort.quick;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QuickSortTest {
    @Test
    public void testPartition() {
        List<Integer> integers = Arrays.asList(20, 10, 40, 50, 30);
        assertEquals(2, QuickSort.partition(integers));
        System.out.println(integers);
    }

    @Test
    public void testPartition2() {
        List<Integer> integers = Arrays.asList(20, 10);
        assertEquals(0, QuickSort.partition(integers));
        System.out.println(integers);
    }

    @Test
    public void testSort() {
        List<Integer> integers = Arrays.asList(10);
        QuickSort.sort(integers);
        assertEquals(Arrays.asList(10), integers);
    }

    @Test
    public void testSort2() {
        List<Integer> integers = Arrays.asList(20, 10);
        QuickSort.sort(integers);
        assertEquals(Arrays.asList(10, 20), integers);
    }

    @Test
    public void testSort3() {
        List<Integer> integers = Arrays.asList(20, 10, 40, 50, 30);
        QuickSort.sort(integers);
        assertEquals(Arrays.asList(10, 20, 30, 40, 50), integers);
    }

    @Test
    public void testSortShuffle() {
        List<Integer> integers = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());
        Collections.shuffle(integers);

        System.out.println(integers);//已经随机打乱顺序的List

        QuickSort.sort(integers);
        List<Integer> sortedIntegers = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());
        assertEquals(sortedIntegers, integers);
    }

    @Test
    public void testRandomizedSortShuffle() {
        List<Integer> integers = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());
        Collections.shuffle(integers);

        System.out.println(integers);//已经随机打乱顺序的List

        QuickSort.randomized_sort(integers);
        List<Integer> sortedIntegers = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());
        assertEquals(sortedIntegers, integers);
    }
}
```

### 1.5.2. 快速排序的随机化版本

见上述代码randomized_sort和randomized_partition。

## 1.6. 计数排序

计数排序的时间代价总的时间是Θ(k+n)。在实践中，当k=O(n)时，常采用计数排序，这时其运行时间为Θ(n)。

计数排序的下界优于8.1 节中证明的Ω(nlgn)，因为它不是个比较排序算法。事实上，其代码根本就不出现输入元素之间的比较。相反，计数排序是用了输入元素的实际值来确定它们在数组中的位置。

计数排序的一个重要性质就是它是稳定的：具有相同值的元素在输出数组中的相对次序与它们在输入数组中的次序相同。亦即，两个相同数之间的顺序是这样来规定的，即在输入数组中先出现的，在输出数组中也位于前面。说计数排序的稳定性非常重要，还有另一个原因，即计数排序经常用作基数排序算法的一个子过程。

```java
//CountSort.java
package algorithm.sort.count;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CountSort {
    public static List<Integer> sort(List<Integer> containerA) {
        //Arrays.asList(new Integer[containerA.size()]);//这种方法生成的List元素为Null
        List<Integer> containerB = IntStream.generate(() -> 0).limit(containerA.size()).boxed().collect(Collectors.toList());

        int k = Collections.max(containerA);
        List<Integer> containerC = IntStream.generate(() -> 0).limit(k + 1).boxed().collect(Collectors.toList()); //pos 0 will not be used

        //C[i]包含等于i的元素个数
        containerA.forEach(i -> containerC.set(i, containerC.get(i) + 1));

        //C[i]包含小于或等于i的元素个数
        for (int i = 1; i <= k; ++i) {
            containerC.set(i, containerC.get(i - 1) + containerC.get(i));
        }

        //找到每个A[i]对应元素所在的位置
        Collections.reverse(containerA);
        containerA.forEach(i -> {
            int countOfLessOrEqualI = containerC.get(i);
            containerB.set(countOfLessOrEqualI - 1, i);
            containerC.set(i, countOfLessOrEqualI - 1);
        });

        return containerB;
    }
}

//CountSortTest.java
package algorithm.sort.count;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CountSortTest {

    @Test
    public void testSort() {
        List<Integer> integers = Arrays.asList(2, 5, 3, 0, 2, 3, 0, 3);
        assertEquals(Arrays.asList(0, 0, 2, 2, 3, 3, 3, 5), CountSort.sort(integers));
    }

    @Test
    public void testSortShuffle() {
        List<Integer> integers = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());
        Collections.shuffle(integers);

        System.out.println(integers);//已经随机打乱顺序的List

        List<Integer> sortedIntegers = IntStream.rangeClosed(0, 10).boxed().collect(Collectors.toList());
        assertEquals(sortedIntegers, CountSort.sort(integers));
    }
}
```

## 1.7. 基数排序

引理8.3 给定n个d位数，每一个数位可以取k种可能的值。基数排序算法能以Θ(d(n+k))的时间正确地对这些数进行排序。

当d为常数、k=O(n)时，基数排序有线性运行时间。更一般地，在如何将每个关键字分解成若干数位方面，我们有了一定的灵活性。

引理8.4 给定n个b位数和任何正整数r<=b，RADIX-SORT能在Θ((b/r)(n+2^r))时间内正确地对这些数进行排序。

```java
//RadixSort.java
package algorithm.sort.radix;

import java.util.List;

public class RadixSort {
    public static void sort(List<Integer> eleList) {
        //这里假定所有数字的长度是一样的，用到的方法是把数字转化成字符串，取到特定的位数进行比较
        int length = String.valueOf(eleList.get(0)).length();
        for (int i = length - 1; i >= 0; --i) {
            int finalI = i;
            eleList.sort((i1, i2) -> {
                String str1 = String.valueOf(i1);
                String str2 = String.valueOf(i2);
                return Integer.compare(str1.charAt(finalI), str2.charAt(finalI));
            });
        }
    }
}

//RadixSortTest.java
package algorithm.sort.radix;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RadixSortTest {
    @Test
    public void testSort() {
        List<Integer> integers = Arrays.asList(5678, 1234, 9816, 6382, 5282);
        RadixSort.sort(integers);
        assertEquals(Arrays.asList(1234, 5282, 5678, 6382, 9816), integers);
    }
}
```

## 1.8. 桶排序

P104 桶排序的期望运行时间为Θ(n)。

于是，整个桶排序算法以线性期望时间运行。


即便输入不符合均匀分布，桶排序也仍然可以以线性时间运行。只要输入满足这样一个性质，即各个桶尺寸的平方和与总的元素数呈线性关系，那么通过式8.1 可知，桶排序仍然能以线性时间运行。