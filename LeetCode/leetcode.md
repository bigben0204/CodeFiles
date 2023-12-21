# 学习

## [队列 & 栈](https://leetcode.cn/leetbook/read/queue-stack/ga4o2/)

# 1. LeetCode

<https://leetcode.com/problemset/all/>

## 747. Largest Number At Least Twice of Others

<https://leetcode.com/problems/largest-number-at-least-twice-of-others/submissions/>

```java
//把数字和其下标一起排序后获取最大值和第二大值元素，如果满足2倍关系，则直接返回最大值下标索引。
package test;

import java.util.Arrays;
import java.util.Comparator;

class Solution {
    public int dominantIndex(int[] nums) {
        if (nums.length == 1) {
            return 0;
        }

        int[][] sortedInts = new int[nums.length][2];
        for (int i = 0; i < nums.length; i++) {
            sortedInts[i][0] = nums[i];
            sortedInts[i][1] = i;
        }

        Arrays.sort(sortedInts, Comparator.comparing(e -> -e[0]));
        int[] max = sortedInts[0];
        int[] secondMax = sortedInts[1];

        return max[0] >= 2 * secondMax[0] ? max[1] : -1;
    }
}

//优化过的，只遍历一遍，找到最大值和第二大的值
package test;

class Solution { //0ms
    public int dominantIndex(int[] nums) {
        if (nums.length == 1) {
            return 0;
        }

        int max = nums[0];
        int maxIndex = 0;
        int secondMax = Integer.MIN_VALUE;

        for (int i = 1; i < nums.length; ++i) {
            if (nums[i] > max) { //如果找到了最大值，则原最大值变成第二大值
                secondMax = max;
                max = nums[i];
                maxIndex = i;
            } else if (secondMax == Integer.MIN_VALUE || nums[i] > secondMax) { //如果当前值不是最大值，则判断当前值是不是比原来的第二大值大，如果大于则将其赋给第二大值
                secondMax = nums[i];
            }
        }
        return max >= 2 * secondMax ? maxIndex : -1;
    }
}


//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        int[] nums = {3, 6, 1, 0};
        assertEquals(1, SOLUTION.dominantIndex(nums));
    }

    @Test
    public void test2() {
        int[] nums = {1, 2, 3, 4};
        assertEquals(-1, SOLUTION.dominantIndex(nums));
    }

    @Test
    public void test3() {
        int[] nums = {1, 0, 0, 0};
        assertEquals(0, SOLUTION.dominantIndex(nums));
    }
}
```

## [406. Queue Reconstruction by Height](https://leetcode.com/problems/queue-reconstruction-by-height/)(华为题库)

输入：二维数组，其中每个对象是一个小朋友信息，由{k, v}表示，k表示小朋友体重，v表示体重比这个小朋友轻的有几位排在这个小朋友前。
输出：小朋友排序好的二维数组队列。
例：
输入：{{6, 1}, {8, 0}, {9, 0}, {5, 0}, {4, 3}};
输出：{{5, 0}, {8, 0}, {6, 1}, {4, 3}, {9, 0}};

算法：先将输入小朋友按体重由小到大排序，然后依次插入到队列中，当前面有几个空位时，就把这个小朋友放在对应的位置。
分析：之所以可以这样排序，是因为每个小朋友在找位置时，已经没有比他轻的小朋友了，所以在这个小朋友前不用预留比他轻的小朋友位置，只需要预留比他重的小朋友位置，所以v为几，在前面留几个空位即可。

```java
//Solution.java
package test;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Solution { //48ms
    private static final int INVALID_STUDENT_FLAG = -1;

    public int[][] reconstructQueue(int[][] students) {
        List<int[]> sortedStudents =
            Arrays.stream(students).sorted((e1, e2) -> {
                if (e1[0] != e2[0]) {
                    return Integer.compare(e1[0], e2[0]);
                } else {
                    return Integer.compare(e2[1], e1[1]);
                }
            }).map(e -> new int[]{e[0], e[1]}).collect(Collectors.toList());
        emptyStudents(students);

        for (int[] sortedStudent : sortedStudents) {
            int studentIndex = getStudentIndex(sortedStudent, students);
            students[studentIndex] = sortedStudent;
        }

        return students;
    }

    private int getStudentIndex(int[] sortedStudent, int[][] students) {
        int emptySeatsInFront = sortedStudent[1];
        int tmp = 0;
        for (int i = 0; i < students.length; i++) {
            if (students[i][1] == INVALID_STUDENT_FLAG) {
                if (tmp++ == emptySeatsInFront) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void emptyStudents(int[][] students) {
        for (int[] student : students) {
            student[0] = INVALID_STUDENT_FLAG;
            student[1] = INVALID_STUDENT_FLAG;
        }
    }
}

//SolutionTest.java
package test;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SolutionTest {
    @Test
    public void test1() {
        int[][] students = {{6, 1}, {8, 0}, {9, 0}, {5, 0}, {4, 3}};
        int[][] expectedStudents = {{5, 0}, {8, 0}, {6, 1}, {4, 3}, {9, 0}};
        assertArrayEquals(expectedStudents, new Solution().reconstructQueue(students));
    }

    @Test
    public void test2() {
        int[][] students = {{7, 0}, {4, 4}, {7, 1}, {5, 0}, {6, 1}, {5, 2}};
        int[][] expectedStudents = {{5, 0}, {7, 0}, {5, 2}, {6, 1}, {4, 4}, {7, 1}};
        assertArrayEquals(expectedStudents, new Solution().reconstructQueue(students));

    }
}

//别人的优化算法，每个人在放入队列时，在他之后放的不会有比他大的了，所以可以直接按index放入，后面即使有人放在他前面，这些人也都比他矮，不会影响v参数：
step 1: sort
[7,0], [7,1], [6,1], [5,0], [5,2], [4,4]

step 2: insert by height
[7,0]
[7,0], [7,1]
[7,0], [6,1], [7,1]
[5,0], [7,0], [6,1], [7,1]
[5,0], [7,0], [5,2], [6,1], [7,1]
[5,0], [7,0], [5,2], [6,1], [4,4], [7,1]

def reconstructQueue(self, people: List[List[int]]) -> List[List[int]]:
    people.sort(key=lambda p: (p[0], -p[1]), reverse=True)
    res = []
    [res.insert(p[1], p) for p in people]
    return res

//Solution.java
package test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class Solution { //36ms
    public int[][] reconstructQueue(int[][] students) {
        Arrays.sort(students, (e1, e2) -> {
            if (e1[0] != e2[0]) {
                return -Integer.compare(e1[0], e2[0]);
            } else {
                return Integer.compare(e1[1], e2[1]);
            }
        });

        List<int[]> sortedStudents = new LinkedList<>();

        for (int[] student : students) {
            sortedStudents.add(student[1], student);
        }

        return sortedStudents.toArray(new int[0][]); //sortedStudents.stream().toArray(int[][]::new)
    }
}
```

## 324. Wiggle Sort II

<https://leetcode.com/problems/wiggle-sort-ii/>

思路：把数组排序，然后对半分，轮流从小的和大的里交插插入。另外要注意从大往小开始插入，以区分出差异值。

```java
//Solution.java
package test;

import java.util.Arrays;

class Solution { //40ms
    public void wiggleSort(int[] nums) {
        int[] sortedNums = Arrays.stream(nums).sorted().toArray();

        int index = 0;
        int length = nums.length;
        int middleIndex = (nums.length + 1) / 2;
        for (int firstIndex = middleIndex - 1, secondIndex = length - 1; firstIndex >= 0; --firstIndex, --secondIndex) {
            nums[index++] = sortedNums[firstIndex];
            if (index < length) {
                nums[index++] = sortedNums[secondIndex];
            }
        }
    }
}

//使用流比较耗时，优化后的，改为纯数组操作：
package test;

import java.util.Arrays;

class Solution { //3ms
    public void wiggleSort(int[] nums) {
        Arrays.sort(nums);
        int length = nums.length;
        int[] tmp = new int[length];

        int index = 0;
        int middleIndex = (nums.length + 1) / 2;
        for (int firstIndex = middleIndex - 1, secondIndex = length - 1; firstIndex >= 0; --firstIndex, --secondIndex) {
            tmp[index++] = nums[firstIndex];
            if (index < length) {
                tmp[index++] = nums[secondIndex];
            }
        }

        for (int i = 0; i < length; i++) {
            nums[i] = tmp[i];
        }
    }
}

//SolutionTest.java
package test;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SolutionTest {
    @Test
    public void test1() {
        int[] nums = {1, 5, 1, 1, 6, 4};
        int[] expectedStudents = {1, 6, 1, 5, 1, 4};
        new Solution().wiggleSort(nums);
        assertArrayEquals(expectedStudents, nums);
    }

    @Test
    public void test2() {
        int[] nums = {1, 5, 1, 1, 6, 4, 7};
        int[] expectedStudents = {4, 7, 1, 6, 1, 5, 1};
        new Solution().wiggleSort(nums);
        assertArrayEquals(expectedStudents, nums);
    }

    @Test
    public void test3() {
        int[] nums = {4, 5, 5, 6};
        int[] expectedStudents = {5, 6, 4, 5};
        new Solution().wiggleSort(nums);
        assertArrayEquals(expectedStudents, nums);
    }
}
```

## 1144. Decrease Elements To Make Array Zigzag

<https://leetcode.com/problems/decrease-elements-to-make-array-zigzag/>

LeetCode上有强调只通过减少数字来调整，不能增加数字，所以要么只减少奇数位，要么只减少偶数位即可，没有同时需要调整奇数位和调整偶数位的场景，因为同时减少奇数和偶数位肯定不是最优解。

```java
package test;

class Solution {
    public int movesToMakeZigzag(int[] nums) {
        int decreaseOddValue = 0, decreaseEvenValue = 0, length = nums.length;
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) { //如果i是偶数，只减少偶数位的场景
                int valueToLowerThanLeft = i > 0 && nums[i] >= nums[i - 1] ? nums[i] - nums[i - 1] + 1 : 0;
                int valueToLowerThanRight = i < length - 1 && nums[i] >= nums[i + 1] ? nums[i] - nums[i + 1] + 1 : 0;
                decreaseEvenValue += Math.max(valueToLowerThanLeft, valueToLowerThanRight);
            } else { //如果i是奇数，只减少奇数位的场景
                int valueToLowerThanLeft = nums[i] >= nums[i - 1] ? nums[i] - nums[i - 1] + 1 : 0;
                int valueToLowerThanRight = i < length - 1 && nums[i] >= nums[i + 1] ? nums[i] - nums[i + 1] + 1 : 0;
                decreaseOddValue += Math.max(valueToLowerThanLeft, valueToLowerThanRight);
            }
        }

        return Math.min(decreaseOddValue, decreaseEvenValue);
    }
}
```

## 997. Find the Town Judge

<https://leetcode.com/problems/find-the-town-judge/>

```java
//Solution.java
package test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Solution { //66ms
    public int findJudge(int N, int[][] trust) {
        if (N == 1) {
            return 1;
        }

        List<int[]> personHaveTrustList = IntStream.rangeClosed(1, N).mapToObj(i -> new int[]{i, 0}).collect(Collectors.toList());
        Map<Integer, Set<Integer>> personTrustedIndexMap = new HashMap<>();

        init(personHaveTrustList, personTrustedIndexMap, trust);

        List<int[]> personHaveNoTrustList = personHaveTrustList.stream().filter(e -> e[1] == 0).collect(Collectors.toList());
        if (personHaveNoTrustList.size() != 1) {
            return -1;
        }

        int tmpSecretIndex = personHaveNoTrustList.get(0)[0];
        Set<Integer> trustPersonSet = personTrustedIndexMap.get(tmpSecretIndex);
        if (trustPersonSet != null && trustPersonSet.size() == N - 1) {
            return tmpSecretIndex;
        }

        return -1;
    }

    private void init(List<int[]> personHaveTrustList, Map<Integer, Set<Integer>> personTrustedIndexMap, int[][] trust) {
        for (int[] trustPair : trust) {
            int trustPersonIndex = trustPair[0];
            int trustedPersonIndex = trustPair[1];
            personHaveTrustList.get(trustPersonIndex - 1)[1] = 1;

            Set<Integer> allTrustSet = personTrustedIndexMap.get(trustedPersonIndex);
            if (allTrustSet == null) {
                allTrustSet = new HashSet<>();
                personTrustedIndexMap.put(trustedPersonIndex, allTrustSet);
            }
            allTrustSet.add(trustPersonIndex);
        }
    }
}

//别人的优化算法
package test;

class Solution { //2ms
    public int findJudge(int N, int[][] trust) {
        if (trust == null || N <= 0) {
            return -1;
        }

        if (trust.length == 0 && N == 1) {
            return 1;
        }

        int[] candidateCounts = new int[N + 1];

        for (int i = 0; i < trust.length; i++) {
            if (candidateCounts[trust[i][1]] != -1) { //只有从来没有信任过别人，才能加1个被信任计数
                candidateCounts[trust[i][1]]++;
            }

            candidateCounts[trust[i][0]] = -1; //只要信任别人，该位置值就是-1
        }

        for (int i = 0; i <= N; i++) {
            if (candidateCounts[i] == N - 1) { //看哪个编号的值是N - 1，说明被其他所有人信任
                return i;
            }
        }

        return -1;
    }
}

//SolutionTest.java
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    @Test
    public void test1() {
        int[][] trust = {{1, 2}};
        assertEquals(2, new Solution().findJudge(2, trust));
    }

    @Test
    public void test2() {
        int[][] trust = {{1, 3}, {2, 3}};
        assertEquals(3, new Solution().findJudge(3, trust));
    }

    @Test
    public void test3() {
        int[][] trust = {{1, 3}, {2, 3}, {3, 1}};
        assertEquals(-1, new Solution().findJudge(3, trust));
    }

    @Test
    public void test4() {
        int[][] trust = {{1, 2}, {2, 3}};
        assertEquals(-1, new Solution().findJudge(3, trust));
    }

    @Test
    public void test5() {
        int[][] trust = {{1, 3}, {1, 4}, {2, 3}, {2, 4}, {4, 3}};
        assertEquals(3, new Solution().findJudge(4, trust));
    }

    @Test
    public void test6() {
        int[][] trust = {};
        assertEquals(-1, new Solution().findJudge(1, trust)); //这个用例感觉有问题
    }
}
```

## [11. 盛最多水的容器](<https://leetcode-cn.com/problems/container-with-most-water/>)(华为题库)

```java
//
package test;

class Solution {
    public int maxArea(int[] height) {
        int l = 0;
        int r = height.length - 1;
        int maxArea = 0;
        while (l < r) {
            maxArea = Math.max(maxArea, Math.min(height[l], height[r]) * (r - l));
            if (height[l] < height[r]) {
                ++l;
            } else {
                --r;
            }
        }
        return maxArea;
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    @Test
    public void test1() {
        int[] heights = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        assertEquals(49, new Solution().maxArea(heights));
    }
}
```

## 75. Sort Colors

<https://leetcode.com/problems/sort-colors/>

给定一个包含红色、白色和蓝色，一共 n 个元素的数组，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
此题中，我们使用整数 0、 1 和 2 分别表示红色、白色和蓝色。

```java
//Solution.java
package test;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

class Solution { //85ms

    private int wholeIndex;

    public void sortColors(int[] nums) {
//        Arrays.sort(nums);

        Map<Integer, Long> numCountMap = Arrays.stream(nums).boxed().collect(Collectors.groupingBy(i -> i,
            TreeMap::new, Collectors.counting()));

        wholeIndex = 0;
        numCountMap.forEach((k, v) -> {
            for (int index = wholeIndex; index < wholeIndex + v; ++index) {
                nums[index] = k;
            }
            wholeIndex += v;
        });
    }
}
```

类似三路排序的优化方案：维护好三个指针
`arr[0,zero_index]==0,arr[zero_index+1,i-1]==1,arr[two_index,n-1]==2`
终止条件：i>=two

```python
//Solution.java
package test;

class Solution { //1ms
    public void sortColors(int[] nums) {
        int zero_index = -1;
        int n = nums.length;
        int two_index = n;
        for (int i = 0; i < n; ) {
            if (i >= two_index) {
                return;
            }

            if (nums[i] == 1) {
                i++;
            } else if (nums[i] == 0) {
                swap(nums, ++zero_index, i);
                i++;
            } else if (nums[i] == 2) {
                swap(nums, i, --two_index);
            }
        }
    }

    private void swap(int[] nums, int l, int r) {
        if (l == r) {
            return;
        }
        int tmp = nums[l];
        nums[l] = nums[r];
        nums[r] = tmp;
    }
}

//SolutionTest.java
package test;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SolutionTest {
    @Test
    public void test1() {
        int[] ints = {2, 0, 2, 1, 1, 0};
        int[] expectInts = {0, 0, 1, 1, 2, 2};
        new Solution().sortColors(ints);
        assertArrayEquals(expectInts, ints);
    }
}
```

## 面试官D考试Demo1：

给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。

```java
//Solution.java
package test;

class Solution {
    public void moveZeroes(int[] nums) {
        int l = 0;
        for (int r = 0; r < nums.length; ++r) {
            if (nums[r] != 0) {
                swap(nums, l, r);
                ++l;
            }
        }
    }

    private void swap(int[] nums, int l, int r) {
        int tmp = nums[l];
        nums[l] = nums[r];
        nums[r] = tmp;
    }
}

//SolutionTest.java
package test;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SolutionTest {
    @Test
    public void test() {
        int[] ints = {0, 1, 0, 3, 12};
        int[] expectInts = {1, 3, 12, 0, 0};
        new Solution().moveZeroes(ints);
        assertArrayEquals(expectInts, ints);
    }
}
```

## 441. Arranging Coins

<https://leetcode.com/problems/arranging-coins/submissions/>

```java
class Solution {
    public int arrangeCoins(int n) {
        return (int) Math.floor((-1 + Math.sqrt(1 + 8 * (long) n)) / 2);
    }
}
```

## 632. 最小区间

<https://leetcode-cn.com/problems/smallest-range/submissions/>

你有?k?个升序排列的整数数组。找到一个最小区间，使得?k?个列表中的每个列表至少有一个数包含在其中。
我们定义如果?b-a < d-c?或者在?b-a == d-c?时?a < c，则区间 [a,b] 比 [c,d] 小。

```python
//Solution.java
package test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solution {
    public int[] smallestRange(List<List<Integer>> nums) {
        List<int[]> arr = sort(nums);

        int k = nums.size();
        int[] cnt = new int[k];
        int len = Integer.MAX_VALUE;
        int[] ans = new int[2];
        int count = 0;

        for (int r = 0, l = 0; r < arr.size(); r++) {
            if (cnt[arr.get(r)[1]]++ == 0) {
                ++count;
            }

            while (cnt[arr.get(l)[1]] > 1) {
                --cnt[arr.get(l)[1]];
                ++l;
            }

            if (count == k) {
                int tmpLen = arr.get(r)[0] - arr.get(l)[0];
                if (tmpLen < len) {
                    len = tmpLen;
                    ans[0] = arr.get(l)[0];
                    ans[1] = arr.get(r)[0];
                }
            }
        }
        return ans;
    }

    private List<int[]> sort(List<List<Integer>> nums) {
        List<int[]> arrIndexList = new ArrayList<>();
        for (int i = 0; i < nums.size(); i++) {
            final int index = i;
            nums.get(i).stream().forEach(e -> arrIndexList.add(new int[]{e, index}));
        }
        arrIndexList.sort(Comparator.comparingInt(o -> o[0]));
        return arrIndexList;
    }
}

//SolutionTest.java
package test;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SolutionTest {
    @Test
    public void test() {
        List<Integer> integers1 = Arrays.asList(4, 10, 15, 24, 26);
        List<Integer> integers2 = Arrays.asList(0, 9, 12, 20);
        List<Integer> integers3 = Arrays.asList(5, 18, 22, 30);
        List<List<Integer>> nums = Arrays.asList(integers1, integers2, integers3);

        int[] expectInts = {20, 24};
        assertArrayEquals(expectInts, new Solution().smallestRange(nums));
    }

    @Test
    public void test1() {
        List<Integer> integers1 = Arrays.asList(1, 2, 3);
        List<List<Integer>> nums = Arrays.asList(integers1, integers1, integers1);

        int[] expectInts = {1, 1};
        assertArrayEquals(expectInts, new Solution().smallestRange(nums));
    }
}
```

解析：先将所有数字排序，并记录所在列表编号0、1、2，之后通过l和r两个滑窗索引，判断是否在滑窗范围内包括所有列表编号，每次当r右移后，如果l所在的位置有两个同列表的数字，则将l开始右移，直到l所在位置只有一个该列表元素，再判断len，直到遍历完r。

```java
arr = {ArrayList@1023}  size = 13
 0 = {int[2]@1026} 
  0 = 0
  1 = 1
 1 = {int[2]@1029} 
  0 = 4
  1 = 0
 2 = {int[2]@1027} 
  0 = 5
  1 = 2
 3 = {int[2]@1030} 
  0 = 9
  1 = 1
 4 = {int[2]@1031} 
  0 = 10
  1 = 0
 5 = {int[2]@1032} 
  0 = 12
  1 = 1
 6 = {int[2]@1033} 
  0 = 15
  1 = 0
 7 = {int[2]@1034} 
  0 = 18
  1 = 2
 8 = {int[2]@1035} 
  0 = 20
  1 = 1
 9 = {int[2]@1036} 
  0 = 22
  1 = 2
 10 = {int[2]@1037} 
  0 = 24
  1 = 0
 11 = {int[2]@1038} 
  0 = 26
  1 = 0
 12 = {int[2]@1039} 
  0 = 30
  1 = 2
```

## 831. 隐藏个人信息

<https://leetcode-cn.com/problems/masking-personal-information/>

```java
//Solution.java
class Solution {
    public String maskPII(String S) {
        if(S.contains(".")){
            return maskEmail(S);
        }else{
            return maskPhone(S);
        }
    }
    public String maskEmail(String str){   
        str = str.toLowerCase();
        StringBuilder sb = new StringBuilder();
        String[] sp = str.split("@");
        sb.append(sp[0].charAt(0)+"*****"+sp[0].charAt(sp[0].length() - 1));
        sb.append("@"+sp[1]);
        return sb.toString();
    }
    public String maskPhone(String str){   
        StringBuilder sb = new StringBuilder();
        for(char ch : str.toCharArray()){
            if(Character.isDigit(ch))
                 sb.append(ch);
        }
        StringBuilder ans = new StringBuilder();
        if(sb.length() == 10){
            ans.append("***-***-");
        }else if(sb.length() == 11){
            ans.append("+*-***-***-"); 
        }else if(sb.length() == 12){
            ans.append("+**-***-***-"); 
        }else if(sb.length() == 13){
            ans.append("+***-***-***-");
        }
         ans.append(sb.substring(sb.length() - 4));
        return ans.toString();
    }
}
```

## 299. Bulls and Cows

<https://leetcode.com/problems/bulls-and-cows/>

```java
package test;

import java.util.Map;
import java.util.stream.Collectors;

class Solution {
    public String getHint(String secret, String guess) {
        int a = 0;
        for (int i = 0; i < secret.length(); i++) {
            if (secret.charAt(i) == guess.charAt(i)) {
                ++a;
            }
        }

        int b = 0;
        Map<Character, Long> characterCountMapSecret =
            secret.chars().mapToObj(c -> (char) c).collect(Collectors.groupingBy(c -> c, Collectors.counting()));
        Map<Character, Long> characterCountMapGuess =
            guess.chars().mapToObj(c -> (char) c).collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        for (Map.Entry<Character, Long> entry : characterCountMapSecret.entrySet()) {
            Character numberSecret = entry.getKey();
            Long numberSecretCount = entry.getValue();
            Long numberGuessCount = characterCountMapGuess.getOrDefault(numberSecret, 0L);
            b += Math.min(numberSecretCount, numberGuessCount);
        }

        return String.format("%dA%dB", a, b - a);
    }
}
```

```python
import collections


class Solution:
    def getHint(self, secret: str, guess: str) -> str:
        # 极简算法
        A = sum(s == g for s, g in zip(secret, guess))
        B = sum((collections.Counter(secret) & collections.Counter(guess)).values()) - A
        return "{A}A{B}B".format(A=A, B=B)

#
from unittest import TestCase

from src.test.main import Solution


class TestSolution(TestCase):
    def test_getHint(self):
        output = Solution().getHint("1807", "7810")
        self.assertEqual(output, '1A3B')
```

## 955. Delete Columns to Make Sorted II

<https://leetcode.com/problems/delete-columns-to-make-sorted-ii/>

```java
// Solution.java
package test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

enum ColumnOrderType {
    ASCEND, DESCEND, EQUAL_OR_ASCEND
}

class Solution { // 58ms
    private static final char INVALID_CHAR = '-';

    public int minDeletionSize(String[] A) {
        int deleteColumnNum = 0;
        List<Integer> lastOrders = Stream.generate(() -> 0).limit(A.length).collect(Collectors.toList());

        for (int columnIndex = 0; columnIndex < A[0].length(); columnIndex++) {
            ColumnOrderType columnOrderType = getColumnOrderType(A, columnIndex, lastOrders);
            if (columnOrderType == ColumnOrderType.ASCEND) {
                return deleteColumnNum;
            } else if (columnOrderType == ColumnOrderType.DESCEND) {
                ++deleteColumnNum;
            }
        }

        return deleteColumnNum;
    }

    private ColumnOrderType getColumnOrderType(String[] strings, int columnIndex, List<Integer> lastOrders) {
        char lastChar = INVALID_CHAR;
        ColumnOrderType columnOrderType = ColumnOrderType.ASCEND;
        List<Integer> currentOrders = new ArrayList<>();
        int orderLevel = 1;

        for (int i = 0; i < strings.length; i++) {
            char currentChar = strings[i].charAt(columnIndex);
            if (lastChar == INVALID_CHAR) {
                lastChar = currentChar;
                currentOrders.add(orderLevel);
                continue;
            }

            if (lastChar > currentChar) {
                // 如果之前的序列是相等的字典序，则该列是升序，需要删除
                if (lastOrders.get(i - 1) == lastOrders.get(i)) {
                    return ColumnOrderType.DESCEND;
                } else {
                    // 如果之前的序列满足字典序，则当前列字符无影响
                    ++orderLevel;
                }
            } else if (lastChar == currentChar) {
                // 如果之前的序列是相等的字典序，则orderLevel仍不变
                if (lastOrders.get(i - 1) == lastOrders.get(i)) {
                    columnOrderType = ColumnOrderType.EQUAL_OR_ASCEND;
                } else {
                    // 如果之前的序列满足字典序
                    ++orderLevel;
                }
            } else { // lastChar < currentChar
                // 无论之前序列什么字典序，当前都满足了字典序
                ++orderLevel;
            }

            lastChar = currentChar;
            currentOrders.add(orderLevel);
        }

        lastOrders.clear();
        lastOrders.addAll(currentOrders);
        return columnOrderType;
    }
}

// SolutionTest.java
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        String[] strings = {"ca", "bb", "ac"};
        assertEquals(SOLUTION.minDeletionSize(strings), 1);
    }

    @Test
    public void test2() {
        String[] strings = {"xc", "yb", "za"};
        assertEquals(SOLUTION.minDeletionSize(strings), 0);
    }

    @Test
    public void test3() {
        String[] strings = {"zyx", "wvu", "tsr"};
        assertEquals(SOLUTION.minDeletionSize(strings), 3);
    }

    @Test
    public void test4() {
        String[] strings = {"abx", "agz", "bgc", "bfc"};
        assertEquals(1, SOLUTION.minDeletionSize(strings));
    }

    @Test
    public void test5() {
        String[] strings = {"xga","xfb","yfa"};
        assertEquals(1, SOLUTION.minDeletionSize(strings));
    }
}
```

用字符串比较

```java
package test;

import java.util.Arrays;


enum ColumnOrderType {
    ASCEND, DESCEND, EQUAL_OR_ASCEND
}

class Solution { // 5ms
    public int minDeletionSize(String[] A) {
        int N = A.length;
        int W = A[0].length();
        int ans = 0;

        // cur : all rows we have written
        // For example, with A = ["abc","def","ghi"] we might have
        // cur = ["ab", "de", "gh"].
        String[] cur = new String[N];
        for (int j = 0; j < W; ++j) {
            // cur2 : What we potentially can write, including the
            //        newest column col = [A[i][j] for i]
            // Eg. if cur = ["ab","de","gh"] and col = ("c","f","i"),
            // then cur2 = ["abc","def","ghi"].
            String[] cur2 = Arrays.copyOf(cur, N);
            for (int i = 0; i < N; ++i)
                cur2[i] += A[i].charAt(j);

            ColumnOrderType columnOrderType = isSorted(cur2);
            if (columnOrderType == ColumnOrderType.ASCEND) {
                return ans;
            } else if (columnOrderType == ColumnOrderType.EQUAL_OR_ASCEND) {
                cur = cur2;
            } else {
                ans++;
            }
        }

        return ans;
    }

    public ColumnOrderType isSorted(String[] A) {
        ColumnOrderType columnOrderType = ColumnOrderType.ASCEND;
        for (int i = 0; i < A.length - 1; ++i) {
            int ret = A[i].compareTo(A[i + 1]);
            if (ret > 0) {
                return ColumnOrderType.DESCEND;
            } else if (ret == 0) {
                columnOrderType = ColumnOrderType.EQUAL_OR_ASCEND;
            }
        }
        return columnOrderType;
    }
}
```

使用StringBuilder慢了

```java
package test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


enum ColumnOrderType {
    ASCEND, DESCEND, EQUAL_OR_ASCEND
}

class Solution { //48ms
    public int minDeletionSize(String[] A) {
        int N = A.length;
        int W = A[0].length();
        int ans = 0;

        // cur : all rows we have written
        // For example, with A = ["abc","def","ghi"] we might have
        // cur = ["ab", "de", "gh"].

        List<StringBuilder> cur = Stream.generate(() -> new StringBuilder()).limit(N).collect(Collectors.toList());

        for (int j = 0; j < W; ++j) {
            // cur2 : What we potentially can write, including the
            //        newest column col = [A[i][j] for i]
            // Eg. if cur = ["ab","de","gh"] and col = ("c","f","i"),
            // then cur2 = ["abc","def","ghi"].
            List<StringBuilder> cur2 = cur.stream().map(e -> new StringBuilder(e.toString())).collect(Collectors.toList());
            for (int i = 0; i < N; ++i)
                cur2.get(i).append(A[i].charAt(j));

            ColumnOrderType columnOrderType = isSorted(cur2);
            if (columnOrderType == ColumnOrderType.ASCEND) {
                return ans;
            } else if (columnOrderType == ColumnOrderType.EQUAL_OR_ASCEND) {
                cur = cur2;
            } else {
                ans++;
            }
        }

        return ans;
    }

    public ColumnOrderType isSorted(List<StringBuilder> A) {
        ColumnOrderType columnOrderType = ColumnOrderType.ASCEND;
        for (int i = 0; i < A.size() - 1; ++i) {
            int ret = A.get(i).toString().compareTo(A.get(i + 1).toString());
            if (ret > 0) {
                return ColumnOrderType.DESCEND;
            } else if (ret == 0) {
                columnOrderType = ColumnOrderType.EQUAL_OR_ASCEND;
            }
        }
        return columnOrderType;
    }
}
```

另一个解法：cuts表示之前列是否已经区分出字典序了，如果已经区分出来，则当前列字母就不需要再比较了

```java
package test;

class Solution { //1ms
    public int minDeletionSize(String[] A) {
        int N = A.length;
        int W = A[0].length();
        // cuts[j] is true : we don't need to check any new A[i][j] <= A[i][j+1]
        boolean[] cuts = new boolean[N - 1];

        int ans = 0;
        search:
        for (int j = 0; j < W; ++j) {
            // Evaluate whether we can keep this column
            for (int i = 0; i < N - 1; ++i)
                if (!cuts[i] && A[i].charAt(j) > A[i + 1].charAt(j)) {
                    // Can't keep the column - delete and continue
                    ans++;
                    continue search;
                }

            // Update 'cuts' information
            for (int i = 0; i < N - 1; ++i)
                if (A[i].charAt(j) < A[i + 1].charAt(j))
                    cuts[i] = true;
        }

        return ans;
    }
}
```

## 594. Longest Harmonious Subsequence

<https://leetcode.com/problems/longest-harmonious-subsequence/>

```java
package test;

import java.util.HashMap;
import java.util.Map;

class Solution {
    public int findLHS(int[] nums) {
        Map<Integer, Integer> intCountMap = getIntCountMap(nums);
        
        int firstNum = 0;
        int firstNumCount = 0;
        int secondNum = 0;
        int secondNumCount = 0;
        int lhs = 0;

        int index = 0;
        for (Map.Entry<Integer, Integer> entry : intCountMap.entrySet()) {
            if (index % 2 == 0) {
                firstNum = entry.getKey();
                firstNumCount = entry.getValue();
            } else {
                secondNum = entry.getKey();
                secondNumCount = entry.getValue();
            }

            if (Math.abs(firstNum - secondNum) == 1 && firstNumCount > 0 && secondNumCount > 0) {
                lhs = Math.max(lhs, firstNumCount + secondNumCount);
            }

            ++index;
        }

        return lhs;
    }

    private Map<Integer, Integer> getIntCountMap(int[] nums) {
        Map<Integer, Integer> intCountMap = new HashMap<>();
        for (int num : nums) {
            intCountMap.put(num, intCountMap.getOrDefault(num, 0) + 1);
        }
        return intCountMap;
    }
}
```

改进1

```java
package test;

import java.util.HashMap;
import java.util.Map;

class Solution {
    public int findLHS(int[] nums) {
        Map<Integer, Integer> intCountMap = getIntCountMap(nums);

        int lhs = 0;
        for (Map.Entry<Integer, Integer> entry : intCountMap.entrySet()) {
            int tmpNum = entry.getKey();
            int tmpNumCount = entry.getValue();
            if (intCountMap.containsKey(tmpNum + 1)) {
                lhs = Math.max(lhs, tmpNumCount + intCountMap.get(tmpNum + 1));
            }
        }

        return lhs;
    }

    private Map<Integer, Integer> getIntCountMap(int[] nums) {
        Map<Integer, Integer> intCountMap = new HashMap<>();
        for (int num : nums) {
            intCountMap.put(num, intCountMap.getOrDefault(num, 0) + 1);
        }
        return intCountMap;
    }
}
```

改进2，没有改进1速度快

```java
package test;

import java.util.HashMap;
import java.util.Map;

class Solution {
    public int findLHS(int[] nums) {
        Map<Integer, Integer> intCountMap = new HashMap<>();

        int lhs = 0;
        for (int num : nums) {
            int numCount = intCountMap.getOrDefault(num, 0) + 1;
            intCountMap.put(num, numCount);
            if (intCountMap.containsKey(num + 1)) {
                lhs = Math.max(lhs, numCount + intCountMap.get(num + 1));
            }
            if (intCountMap.containsKey(num - 1)) {
                lhs = Math.max(lhs, numCount + intCountMap.get(num - 1));
            }
        }

        return lhs;
    }
}
```

## 165. Compare Version Numbers

<https://leetcode.com/problems/compare-version-numbers/>

通过这个例子，发现使用BigDecimal耗时也多，耗内存也多。

Integer.parseInt返回int，Integer.valueOf返回Integer（内部调用了parseInt），从效率上考虑，建议首先考虑parseInt方法。

```python
package test;

class Solution {
    public int compareVersion(String version1, String version2) {
        String[] versionArray1 = version1.split("\\.");
        String[] versionArray2 = version2.split("\\.");

        int length1 = versionArray1.length;
        int length2 = versionArray2.length;

        int minLength = Math.min(length1, length2);
        for (int i = 0; i < minLength; i++) {
            int i1 = Integer.parseInt(versionArray1[i]);
            int i2 = Integer.parseInt(versionArray2[i]);
            if (i1 == i2) {
                continue;
            } else {
                return i1 > i2 ? 1 : -1;
            }
        }

        //之前所有的位都相同
        int maxLength = Math.max(length1, length2);
        String[] longerVersionArray;
        int flag;

        if (length1 > length2) {
            longerVersionArray = versionArray1;
            flag = 1;
        } else {
            longerVersionArray = versionArray2;
            flag = -1;
        }
        for (int i = minLength; i < maxLength; i++) {
            int i1 = Integer.parseInt(longerVersionArray[i]);
            if (i1 == 0) {
                continue;
            } else {
                return flag * (i1 > 0 ? 1 : -1);
            }
        }

        return 0;
    }
//    public int compareVersion(String version1, String version2) {
//        String[] versionArray1 = version1.split("\\.");
//        String[] versionArray2 = version2.split("\\.");
//
//        int length1 = versionArray1.length;
//        int length2 = versionArray2.length;
//
//        int minLength = Math.min(length1, length2);
//        for (int i = 0; i < minLength; i++) {
//            BigDecimal bigDecimal1 = new BigDecimal(versionArray1[i]);
//            BigDecimal bigDecimal2 = new BigDecimal(versionArray2[i]);
//            if (bigDecimal1.equals(bigDecimal2)) {
//                continue;
//            } else {
//                return bigDecimal1.compareTo(bigDecimal2);
//            }
//        }
//
//        //之前所有的位都相同
//        int maxLength = Math.max(length1, length2);
//        String[] longerVersionArray;
//        BigDecimal bigDecimalZero = new BigDecimal("0");
//        int flag;
//
//        if (length1 > length2) {
//            longerVersionArray = versionArray1;
//            flag = 1;
//        } else {
//            longerVersionArray = versionArray2;
//            flag = -1;
//        }
//        for (int i = minLength; i < maxLength; i++) {
//            BigDecimal bigDecimal = new BigDecimal(longerVersionArray[i]);
//            if (bigDecimal.equals(bigDecimalZero)) {
//                continue;
//            } else {
//                return flag * bigDecimal.compareTo(bigDecimalZero);
//            }
//        }
//
//        return 0;
//    }
}
```

## 493. Reverse Pairs

<https://leetcode.com/problems/reverse-pairs/>

Given an array nums, we call (i, j) an important reverse pair if i < j and nums[i] > 2*nums[j].

You need to return the number of important reverse pairs in the given array.

Example1:

Input: [1,3,2,3,1]
Output: 2
Example2:

Input: [2,4,3,5,1]
Output: 3
Note:
The length of the given array will not exceed 50,000.
All the numbers in the input array are in the range of 32-bit integer.

```java
//Solution.java
public class Solution {

    private int ret;

    public int reversePairs(int[] nums) {
        ret = 0;
        mergeSort(nums, 0, nums.length - 1);
        return ret;
    }

    public void mergeSort(int[] nums, int left, int right) {
        if (right <= left) {
            return;
        }
        int middle = left + (right - left) / 2;
        mergeSort(nums, left, middle);
        mergeSort(nums, middle + 1, right);

        //合并排序，中间节点及之前的元素都是排好序的，中间节点之后的节点都是排好序的。
        //每次针对中间元素及之前的元素和中间节点之后元素比较，看是否满足nums[l] > 2 * num[r]
        //count elements
        int count = 0;
        for (int l = left, r = middle + 1; l <= middle; ) {
            //如果中间节点前的某个元素l值已经大于2*中间节点后的某个元素r值就做count++操作，则中间节点l及之后的所有元素都满足这个条件，这是每次可以做ret += count的原因
            //如果r已经大于right了，则说明中间节点后的元素已经检查完了，l~中间节点的值都满足count的个数，也可以直接ret += count
            if (r > right || (long) nums[l] <= 2 * (long) nums[r]) {
                l++;
                ret += count;
            } else {
                r++;
                count++;
            }
        }

        //merge sort
        int[] temp = new int[right - left + 1];
        for (int l = left, r = middle + 1, k = 0; l <= middle || r <= right; ) {
            if (l <= middle && ((r > right) || nums[l] < nums[r])) {
                temp[k++] = nums[l++];
            } else {
                temp[k++] = nums[r++];
            }
        }
        for (int i = 0; i < temp.length; i++) {
            nums[left + i] = temp[i];
        }
    }
}

//效率低的算法
class Solution {
   public int reversePairs(int[] nums) {
       List<Long> numList =
           Arrays.stream(nums).mapToObj(Long::valueOf).collect(Collectors.toCollection(LinkedList::new));
       int pairNum = 0;
       while (!numList.isEmpty()) {
           Long first = numList.get(0);
           numList.remove(0);

           pairNum += numList.stream().mapToInt(e -> (first > e * 2) ? 1 : 0).sum();
       }

       return pairNum;
   }
}

//SolutionTest
package test;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        int[] ints = {1, 3, 2, 3, 1};
        assertEquals(2, SOLUTION.reversePairs(ints));
    }

    @Test
    public void test2() {
        int[] ints = {7, 10, 3, 5, 1, 2};
        assertEquals(9, SOLUTION.reversePairs(ints));
    }

    @Test
    public void test3() {
        int[] ints = {2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647};
        assertEquals(0, SOLUTION.reversePairs(ints));
    }
}
```

## 1. Two Sum

<https://leetcode.com/problems/two-sum/>

```java
package leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * https://www.cnblogs.com/grandyang/p/4606334.html
 * 1    Two Sum
 */
public class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        // Collectors.toMap的第三个参数表示重复的Key时，如何处理Value值，(a, b) -> a表示保留用第一个Value，(a, b) -> b表示用第二个Value
        Map<Integer, Integer> valuePosMap = IntStream.range(0, nums.length).boxed().collect(Collectors.toMap(i -> nums[i], i -> i, (a, b) -> b));

        int[] res = new int[2];
        for (int firstIndex = 0; firstIndex < nums.length; firstIndex++) {
            int secondValue = target - nums[firstIndex];
            if (valuePosMap.containsKey(secondValue) && valuePosMap.get(secondValue) != firstIndex) {
                res[0] = firstIndex;
                res[1] = valuePosMap.get(secondValue);
                break;
            }
        }

        return res;
    }

    public int[] twoSumForOneLoop(int[] nums, int target) {
        Map<Integer, Integer> valuePosMap = new HashMap<>();

        int[] res = new int[2];
        for (int firstIndex = 0; firstIndex < nums.length; firstIndex++) {
            int firstValue = nums[firstIndex];
            int secondValue = target - firstValue;
            if (valuePosMap.containsKey(secondValue)) {
                res[0] = valuePosMap.get(secondValue); //这里的顺序是反的
                res[1] = firstIndex;
                break;
            } else {
                valuePosMap.put(firstValue, firstIndex);
            }
        }

        return res;
    }
}

//自己写的方法，只是把index存成了Map的List值
package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, List<Integer>> numIndexMap = getNumIndexMap(nums);

        for (int i = 0; i < nums.length; i++) {
            int otherInt = target - nums[i];

            List<Integer> otherIntIndexList = numIndexMap.get(otherInt);
            if (otherIntIndexList != null) {
                if (otherInt != nums[i]) {
                    return new int[]{i, otherIntIndexList.get(0)};
                } else if (otherIntIndexList.size() > 1) {
                    return new int[]{i, otherIntIndexList.get(1)};
                }
            }
        }

        return new int[]{};
    }

    private Map<Integer, List<Integer>> getNumIndexMap(int[] nums) {
        Map<Integer, List<Integer>> numIndexMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            List<Integer> indexList = numIndexMap.get(nums[i]);
            if (indexList == null) {
                indexList = new ArrayList<>();
                numIndexMap.put(nums[i], indexList);
            }
            indexList.add(i);
        }

        return numIndexMap;
    }
}

//test
package leetcode;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class TwoSumTest {

    private static final TwoSum TWO_SUM = new TwoSum();

    @Test
    public void test1() {
        int[] nums = new int[]{2, 3, 10, 7};
        int target = 10;
        int[] targetIndex = new int[]{1, 3};
//        assertArrayEquals(targetIndex, TWO_SUM.twoSum(nums, target));
        assertArrayEquals(targetIndex, TWO_SUM.twoSumForOneLoop(nums, target));
    }

    @Test
    public void test2() {
        int[] nums = new int[]{2, 3, 10, 2};
        int target = 4;
        int[] targetIndex = new int[]{0, 3};
//        assertArrayEquals(targetIndex, TWO_SUM.twoSum(nums, target));
        assertArrayEquals(targetIndex, TWO_SUM.twoSumForOneLoop(nums, target));
    }
}
```

```c++
#include <vector>
#include <unordered_map>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    vector<int> twoSum(vector<int>& nums, int target) {
        const auto& numIndexMap = initNumIndexMap(nums);

        std::vector<int> twoIndex;
        for (int index = 0; index < nums.size(); ++index) {
            int secondNum = target - nums.at(index);
            auto secondNumIter = numIndexMap.find(secondNum);
            if (secondNumIter != numIndexMap.end() && (*secondNumIter).second != index) {
                twoIndex = {index, (*secondNumIter).second};
                break;
            }
        }
        return twoIndex;
    }

private:
    std::unordered_map<int, int> initNumIndexMap(vector<int>& nums) {
        std::unordered_map<int, int> numIndexMap;
        for (int index = 0; index < nums.size(); ++index) {
            numIndexMap[nums.at(index)] = index;
        }
        return numIndexMap;
    }
};

class SolutionTest : public testing::Test {
protected:
    static Solution solution;
};

Solution SolutionTest::solution;

TEST_F(SolutionTest, Test1) {
    vector<int> nums = {2, 7, 11, 15};
    int target = 9;
    vector<int> expect = {0, 1};
    EXPECT_EQ(expect, solution.twoSum(nums, target));
}

TEST_F(SolutionTest, Test2) {
    vector<int> nums = {3, 2, 4};
    int target = 6;
    vector<int> expect = {1, 2};
    EXPECT_EQ(expect, solution.twoSum(nums, target));
}

TEST_F(SolutionTest, Test3) {
    vector<int> nums = {3, 3};
    int target = 6;
    vector<int> expect = {0, 1};
    EXPECT_EQ(expect, solution.twoSum(nums, target));
}
```



## [621. Task Scheduler](<https://leetcode.com/problems/task-scheduler/>)(华为题库)

自己写的算法不对

```c++
#include <vector>
#include <unordered_map>
#include <algorithm>

class Solution {  //算法不对
    typedef std::pair<char, int> CharCountPair;
public:
    int leastInterval(std::vector<char>& tasks, int n) {
        std::unordered_map<char, int> charCountMap;
        for_each(tasks.begin(), tasks.end(), [&] (char task) {++charCountMap[task]; });

        std::vector<CharCountPair> charCounts;
        for_each(charCountMap.begin(), charCountMap.end(), [&] (const CharCountPair& p) {charCounts.push_back(p); });
        sort(charCounts.begin(), charCounts.end(), [&] (const CharCountPair& lhs, const CharCountPair& rhs) {return lhs.second > rhs.second; });

        int interval = 0;
        int maxCharCount = (*charCounts.begin()).second;
        for (int index = 1; index <= maxCharCount; index++) {
            int tmpInterval = count_if(charCounts.begin(), charCounts.end(), [&] (const CharCountPair& p) {return p.second >= index; });
            interval += tmpInterval > n || index == maxCharCount ? tmpInterval : n + 1;  // 如果已经数到最后一轮，则无需再与n比较
        }

        return interval;
    }
};

//改后仍然不对
#include <vector>
#include <unordered_map>
#include <algorithm>

class Solution {
    typedef std::pair<char, int> CharCountPair;
public:
    int leastInterval(std::vector<char>& tasks, int n) {
        if (n == 0) {
            return tasks.size();
        }

        std::unordered_map<char, int> charCountMap;
        for_each(tasks.begin(), tasks.end(), [&] (char task) {++charCountMap[task]; });

        std::vector<CharCountPair> charCounts;
        for_each(charCountMap.begin(), charCountMap.end(), [&] (const CharCountPair& p) {charCounts.push_back(p); });
        sortCharCounts(charCounts);


        int interval = 0;
        int maxCharCount = (*charCounts.begin()).second;
        for (int index = 1; index <= maxCharCount; index++) {
            bool needReSort = false;
            int tmpInterval = getTmpInterval(charCounts, needReSort, n);
            if (tmpInterval == 0) {
                break;
            }

            if (needReSort) {
                sortCharCounts(charCounts);
            }

            interval += tmpInterval;
        }

        return interval;
    }
private:
    void sortCharCounts(std::vector<CharCountPair>& charCounts)
    {
        sort(charCounts.begin(), charCounts.end(), [&] (const CharCountPair& lhs, const CharCountPair& rhs) {return lhs.second > rhs.second; });
    }

    int getTmpInterval(std::vector<CharCountPair>& charCounts, bool& needReSort, const int& fixedInterval)
    {
        needReSort = false;
        if (charCounts.size() - 1 <= fixedInterval) {
            needReSort = false;
        }
        else if (charCounts.at(fixedInterval - 1).second - 1 >= charCounts.at(fixedInterval).second) {
            needReSort = false;
        }
        else {
            needReSort = true;
        }

        int tmpInterval = 0;
        for (std::vector<CharCountPair>::iterator iter = charCounts.begin(); iter != charCounts.end();) {
            --(*iter).second;
            ++tmpInterval;

            if ((*iter).second == 0) {
                iter = charCounts.erase(iter);
            }
            else {
                ++iter;
            }

            if (tmpInterval > fixedInterval) {
                break;
            }
        }
        return tmpInterval != 0 && tmpInterval <= fixedInterval && !charCounts.empty() ? fixedInterval + 1 : tmpInterval;
    }
};
```

参考别人答案优化后的：

<https://leetcode-cn.com/problems/task-scheduler/solution/python-xiang-jie-by-jalan/>

思路
完成所有任务的最短时间取决于出现次数最多的任务数量。

看下题目给出的例子

输入: tasks = ["A","A","A","B","B","B"], n = 2
输出: 8
执行顺序: A -> B -> (待命) -> A -> B -> (待命) -> A -> B.
因为相同任务必须要有时间片为 n 的间隔，所以我们先把出现次数最多的任务 A 安排上（当然你也可以选择任务 B）。例子中 n = 2，那么任意两个任务 A 之间都必须间隔 2 个单位的时间：

A -> (单位时间) -> (单位时间) -> A -> (单位时间) -> (单位时间) -> A
中间间隔的单位时间可以用来安排别的任务，也可以处于“待命”状态。当然，为了使总任务时间最短，我们要尽可能地把单位时间分配给其他任务。现在把任务 B 安排上：

A -> B -> (单位时间) -> A -> B -> (单位时间) -> A -> B
很容易观察到，前面两个 A 任务一定会固定跟着 2 个单位时间的间隔。最后一个 A 之后是否还有任务跟随取决于是否存在与任务 A 出现次数相同的任务。

该例子的计算过程为：

(任务 A 出现的次数 - 1) * (n + 1) + (出现次数为 3 的任务个数)，即：

(3 - 1) * (2 + 1) + 2 = 8
所以整体的解题步骤如下：

计算每个任务出现的次数
找出出现次数最多的任务，假设出现次数为 x
计算至少需要的时间 (x - 1) * (n + 1)，记为 min_time
计算出现次数为 x 的任务总数 count，计算最终结果为 min_time + count
特殊情况
然而存在一种特殊情况，例如：

输入: tasks = ["A","A","A","B","B","B","C","C","D","D"], n = 2
输出: 10
执行顺序: A -> B -> C -> A -> B -> D -> A -> B -> C -> D
此时如果按照上述方法计算将得到结果为 8，比数组总长度 10 要小，应返回数组长度。

```c++
#include <vector>
#include <unordered_map>
#include <algorithm>

class Solution {
    typedef std::pair<char, int> CharCountPair;
public:
    int leastInterval(std::vector<char>& tasks, int n) {
        std::unordered_map<char, int> charCountMap;
        for_each(tasks.begin(), tasks.end(), [&] (char task) {++charCountMap[task]; });

        std::vector<CharCountPair> charCounts;
        for_each(charCountMap.begin(), charCountMap.end(), [&] (const CharCountPair& p) {charCounts.push_back(p); });
        sort(charCounts.begin(), charCounts.end(), [&] (const CharCountPair& lhs, const CharCountPair& rhs) {return lhs.second > rhs.second; });

        int maxCharCount = charCounts.at(0).second;
        int minInterval = (maxCharCount - 1) * (n + 1);
        int charCountWithMaxCount = count_if(charCounts.begin(), charCounts.end(), [&] (const CharCountPair& p) {return p.second == maxCharCount; });
        int interval = minInterval + charCountWithMaxCount;

        return std::max<int>(interval, tasks.size());
    }
};

// gtest_Solution.cpp
#include "gtest/gtest.h"
#include "Solution.h"

TEST(SolutionTest, Test1)
{
    std::vector<char> chars = {'A', 'A', 'A', 'B', 'B', 'B'};
    int interval = 2;

    Solution solution;
    EXPECT_EQ(8, solution.leastInterval(chars, interval));
}

TEST(SolutionTest, Test2)
{
    std::vector<char> chars = {'A', 'A', 'A', 'B', 'B'};
    int interval = 2;

    Solution solution;
    EXPECT_EQ(7, solution.leastInterval(chars, interval));
}

TEST(SolutionTest, Test3)
{
    std::vector<char> chars = {'A', 'A', 'A', 'B', 'B'};
    int interval = 1;

    Solution solution;
    EXPECT_EQ(5, solution.leastInterval(chars, interval));
}

TEST(SolutionTest, Test4)
{
    std::vector<char> chars = {'A', 'A', 'A', 'B', 'B'};
    int interval = 3;

    Solution solution;
    EXPECT_EQ(9, solution.leastInterval(chars, interval));
}

TEST(SolutionTest, Test5)
{
    std::vector<char> chars = {'A', 'A', 'A', 'B', 'B', 'C'};
    int interval = 3;

    Solution solution;
    EXPECT_EQ(9, solution.leastInterval(chars, interval));
}

TEST(SolutionTest, Test6)
{
    std::vector<char> chars = {'A', 'A', 'A', 'B', 'B', 'C'};
    int interval = 2;

    Solution solution;
    EXPECT_EQ(7, solution.leastInterval(chars, interval));
}

TEST(SolutionTest, Test7)
{
    std::vector<char> chars = {'A','A','A','A','A','A','B','C','D','E','F','G'};
    int interval = 2;

    Solution solution;
    EXPECT_EQ(16, solution.leastInterval(chars, interval));
}

TEST(SolutionTest, Test8)
{
    std::vector<char> chars = {'A','A','A','B','B','B'};
    int interval = 0;

    Solution solution;
    EXPECT_EQ(6, solution.leastInterval(chars, interval));
}

TEST(SolutionTest, Test9)
{
    std::vector<char> chars = {'A','A','A','B'};
    int interval = 1;

    Solution solution;
    EXPECT_EQ(5, solution.leastInterval(chars, interval));
}
```

## [554. 砖墙](https://leetcode-cn.com/problems/brick-wall/)(华为题库)

思路：找到每行各个砖组成的数字和，数出各行砖出现的最多的数字，其个数就是竖线要穿过时经过的缝，用砖高度减去该数字即竖线穿过的砖个数。

```python
// Solution.h
#include <vector>
#include <unordered_map>
#include <algorithm>

class Solution {
    typedef std::vector<int> Ints;
    typedef std::vector<Ints> IntsVector;
    typedef std::unordered_map<int, int> IntCountMap;
    typedef std::pair<int, int> IntCountPair;
public:
    int leastBricks(IntsVector& wall) {
        IntCountMap intCountMap;
        initIntCountMap(wall, intCountMap);

        IntCountMap::const_iterator maxCountIter = std::max_element(intCountMap.begin(), intCountMap.end(),
            [] (const IntCountPair& lhs, const IntCountPair& rhs) {return lhs.second < rhs.second; });
        return maxCountIter != intCountMap.end() ? wall.size() - (*maxCountIter).second : wall.size();
    }

private:
    void initIntCountMap(const IntsVector& wall, IntCountMap& intCountMap)
    {
        for (const Ints& ints : wall) {
            if (ints.size() == 1) {
                continue;
            }

            int sum = 0;
            for_each(ints.begin(), ints.end() - 1, [&] (int brick) {
                sum += brick;
                ++intCountMap[sum];
                });
        }
    }
};

// gtest_Solution.cpp
#include "gtest/gtest.h"
#include "Solution.h"

TEST(SolutionTest, Test1)
{
    std::vector<std::vector<int>> wall = 
    {{1,2,2,1},
    {3,1,2},
    {1,3,2},
    {2,4},
    {3,1,2},
    {1,3,1,1}};

    Solution solution;
    EXPECT_EQ(2, solution.leastBricks(wall));
}

TEST(SolutionTest, Test2)
{
    std::vector<std::vector<int>> wall =
    {{1,2,2,1}};

    Solution solution;
    EXPECT_EQ(0, solution.leastBricks(wall));
}

TEST(SolutionTest, Test3)
{
    std::vector<std::vector<int>> wall =
    {{1},
    {1},
    {1}};

    Solution solution;
    EXPECT_EQ(3, solution.leastBricks(wall));
}
```

## 1016. Binary String With Substrings Representing 1 To N

<https://leetcode.com/problems/binary-string-with-substrings-representing-1-to-n/submissions/>

```c++
#include <string>

class Solution {
public:
    bool queryString(std::string S, int N) {
        for (int index = 1; index <= N; index++) {
            std::string binStr = decToBinString(index);
            if (S.find(binStr) == std::string::npos) {
                return false;
            }
        }

        return true;
    }

private:
    std::string decToBinString(int num)
    {
        if (num > 0) {
            std::string s;
            std::stringstream ss;
            s = decToBinString(num / 2);
            ss << num % 2;
            return s + ss.str();
        }
        else {
            return "";
        }
    }

    //std::string decToBinString(int num)
    //{
    //    int result = 0, temp = num, j = 1;
    //    while (temp) {
    //        result = result + j * (temp % 2);
    //        temp = temp / 2;
    //        j *= 10;
    //    }
//
    //    std::stringstream ss;
    //    ss << result;
    //    return ss.str();
    //}

    // 利用位与将10进制转成2进制字符串
    //std::string decToBinString(int x) {
    //    std::string ans;
    //    while (x) {
    //        ans = (x & 1) ? '1' + ans : '0' + ans;
    //        x >>= 1;
    //    }
    //    return ans;
    //}
};
```

## 326. Power of Three

<https://leetcode.com/problems/power-of-three/>

```c++
class Solution {
public:
    bool isPowerOfThree(int n) { // 20ms
        if (n == 1) {
            return true;
        }

        int remainder = 0;
        int divisor = n;
        while (divisor > 3) {
            remainder = divisor % 3;
            if (remainder != 0) {
                return false;
            }

            divisor = divisor / 3;
        }
        return divisor == 3;
    }
};

class Solution { // 优化12ms
public:
    bool isPowerOfThree(int n) {
        while (n % 3 == 0 && n != 0) {
            n /= 3;
        }
        return n == 1;
    }
};
```

## 234 Palindrome Linked List

<https://leetcode.com/problems/palindrome-linked-list/>

```c++
//Definition for singly-linked list.
struct ListNode {
    int val;
    ListNode* next;
    ListNode(int x): val(x), next(NULL) {}
};

class Solution {
public:
    bool isPalindrome(ListNode* head) {
        if (!head || !head->next) {  // 判断指针是否为空，使用!比和NULL相比快
            return true;
        }

        ListNode* slow = head;
        ListNode* fast = head;
        ListNode* slowPrevious = NULL;
        ListNode* tempSlowPrevious = NULL;

        // 通过定义两个快慢指针，在走到Tail时，也一并找到中间元素，同时也将从Head到中间元素的单链指向反转
        while (fast != NULL && fast->next != NULL) {
            slowPrevious = slow;
            slow = slow->next;
            fast = fast->next->next;
            slowPrevious->next = tempSlowPrevious;
            tempSlowPrevious = slowPrevious;
        }


        ListNode* mid2LeftStartNode = slowPrevious; //无论Node总个数是奇还是偶，左侧中间开始查找元素都是slow的前一个元素
        ListNode* mid2RightStartNode = fast == NULL ? slow : slow->next; //右侧中间开始查找元素要看总个数是奇还是偶，来判断起始查找元素

        while (mid2RightStartNode != NULL) {
            if (mid2LeftStartNode->val != mid2RightStartNode->val) {
                return false;
            }
            else {
                mid2LeftStartNode = mid2LeftStartNode->next;
                mid2RightStartNode = mid2RightStartNode->next;
            }
        }

        return true;
    }
};
```

## 870 Advantage Shuffle

<https://leetcode.com/problems/advantage-shuffle/>

```c++
#include "gtest/gtest.h"
#include <vector>

using namespace std;

class Solution {
    typedef vector<int> Ints;
    typedef multimap<int, int, greater<int>> ValueIndexMap;
public:
    vector<int> advantageCount(Ints& a, Ints& b) {
        sort(a.begin(), a.end());
        ValueIndexMap valueIndexMapOfB;
        getValueIndexMap(b, valueIndexMapOfB);

        Ints retA(a.size(), 0);
        int indexHeadOfA = 0, indexTailOfA = a.size() - 1;
        for (const auto& valueIndexPair : valueIndexMapOfB) {
            const int& valueOfB = valueIndexPair.first;
            const int& indexOfB = valueIndexPair.second;
            if (a.at(indexTailOfA) > valueOfB) {
                retA[indexOfB] = a.at(indexTailOfA--);
            } else {
                retA[indexOfB] = a.at(indexHeadOfA++);
            }
        }
        return retA;
    }

private:
    void getValueIndexMap(const Ints& b, ValueIndexMap& valueIndexMap) {
        int index = 0;
        for_each(b.begin(), b.end(), [&](int value) { valueIndexMap.insert(make_pair(value, index++)); });
    }
};

TEST(SolutionTest, Test1) {
    vector<int> A = {2, 7, 11, 15};
    vector<int> B = {1, 10, 4, 11};
    vector<int> expectA = {2, 11, 7, 15};
    EXPECT_EQ(expectA, Solution().advantageCount(A, B));
}

TEST(SolutionTest, Test2) {
    vector<int> A = {12, 24, 8, 32};
    vector<int> B = {13, 25, 32, 11};
    vector<int> expectA = {24, 32, 8, 12};
    EXPECT_EQ(expectA, Solution().advantageCount(A, B));
}

TEST(SolutionTest, Test3) {
    vector<int> A = {9, 10, 11, 12};
    vector<int> B = {10, 10, 10, 10};
    vector<int> expectA = {12, 11, 9, 10};
    EXPECT_EQ(expectA, Solution().advantageCount(A, B));
}
```

如果不使用greater函数，也可以自定义比较器：

```c++
struct IntReverseComp {
    bool operator()(const int& lhs, const int& rhs) const {
        return lhs > rhs;
    }
};

typedef multimap<int, int, greater<int>> ValueIndexMap;
```

## 658 Find K Closest Elements

<https://leetcode.com/problems/find-k-closest-elements/>

```c++
#include "gtest/gtest.h"
#include <vector>
#include <deque>

using namespace std;

class Solution {
public:
    vector<int> findClosestElements(vector<int>& arr, int k, int x) {
        if (x <= *arr.begin()) {
            return vector<int>(arr.begin(), arr.begin() + k);
        } else if (x >= *arr.rbegin()) {
            return vector<int>(arr.end() - k, arr.end());
        }

        int leftIndex = findClosestIndex(arr, x);  // 找到恰好小于等于x的index
        int rightIndex = leftIndex + 1;
        deque<int> retInts;
        while (retInts.size() < k) {  // 依次从index往走和往右遍历，查看哪个离x的值更小，就将这个值加入
            if (rightIndex >= arr.size()) {
                retInts.push_front(arr.at(leftIndex--));
            } else if (leftIndex < 0) {
                retInts.push_back(arr.at(rightIndex++));
            } else if (x - arr.at(leftIndex) <= arr.at(rightIndex) - x) {
                retInts.push_front(arr.at(leftIndex--));
            } else {
                retInts.push_back(arr.at(rightIndex++));
            }
        }

        return vector<int>(retInts.begin(), retInts.end());
    }

private:
    int findClosestIndex(vector<int>& arr, int x) {
        auto it = partition_point(arr.begin(), arr.end(), [=](int i) { return x >= i; });
        return distance(arr.begin(), it - 1);
    }
};

TEST(SolutionTest, Test1) {
    vector<int> arr = {1, 2, 3, 4, 5};
    int k = 4, x = 3;
    vector<int> expected = {1, 2, 3, 4};
    EXPECT_EQ(expected, Solution().findClosestElements(arr, k, x));
}

TEST(SolutionTest, Test2) {
    vector<int> arr = {1, 2, 3, 4, 5};
    int k = 4, x = -1;
    vector<int> expected = {1, 2, 3, 4};
    EXPECT_EQ(expected, Solution().findClosestElements(arr, k, x));
}

TEST(SolutionTest, Test3) {
    vector<int> arr = {1, 2, 3, 4, 5};
    int k = 4, x = 6;
    vector<int> expected = {2, 3, 4, 5};
    EXPECT_EQ(expected, Solution().findClosestElements(arr, k, x));
}

TEST(SolutionTest, Test4) {
    vector<int> arr = {1, 2, 3, 7, 8};
    int k = 4, x = 3;
    vector<int> expected = {1, 2, 3, 7};
    EXPECT_EQ(expected, Solution().findClosestElements(arr, k, x));
}

TEST(SolutionTest, Test5) {
    vector<int> arr = {1, 2, 3, 7, 8};
    int k = 4, x = 6;
    vector<int> expected = {2, 3, 7, 8};
    EXPECT_EQ(expected, Solution().findClosestElements(arr, k, x));
}

TEST(SolutionTest, Test6) {
    vector<int> arr = {1, 2, 3, 7, 8};
    int k = 4, x = 2;
    vector<int> expected = {1, 2, 3, 7};
    EXPECT_EQ(expected, Solution().findClosestElements(arr, k, x));
}

TEST(SolutionTest, Test7) {
    vector<int> arr = {1, 2, 3, 7, 8, 9};
    int k = 5, x = 5;
    vector<int> expected = {1, 2, 3, 7, 8};
    EXPECT_EQ(expected, Solution().findClosestElements(arr, k, x));
}

TEST(SolutionTest, Test8) {
    vector<int> arr = {1, 2, 3, 7, 8, 9};
    int k = 0, x = 5;
    vector<int> expected;
    EXPECT_EQ(expected, Solution().findClosestElements(arr, k, x));
}

TEST(SolutionTest, Test9) {
    vector<int> arr = {0, 0, 1, 2, 3, 3, 4, 7, 7, 8};
    int k = 3, x = 5;
    vector<int> expected = {3, 3, 4};
    EXPECT_EQ(expected, Solution().findClosestElements(arr, k, x));
}
```

## [820  Short Encoding of Words](https://leetcode.com/problems/short-encoding-of-words/)(华为题库)

```c++
// 每次的新单词，都遍历是不是能在前面所有单词中找到以其为结尾，最差的情况为O(n*n)
class Solution {  // 1228ms
public:
    int minimumLengthEncoding(vector<string>& words)
    {
        sort(words.begin(), words.end(), [](const string& lhs, const string& rhs) { return lhs.size() > rhs.size(); });
        vector<string> uniqueWords;
        int length = 0;

        for (const auto& word : words) {
            if (!isWordInUniqueWords(word, uniqueWords)) {
                uniqueWords.push_back(word);
                length += word.size() + 1;
            }
        }

        return length;
    }

private:
    bool isWordInUniqueWords(const string& word, const vector<string>& uniqueWords)
    {
        for (const auto& uniqueWord : uniqueWords) {
            if (uniqueWord.substr(uniqueWord.size() - word.size()) == word) {
                return true;
            }
        }
        return false;
    }
};

// 别人的优化的代码：也是先按单词长度排序，同时构造一个单词全集HashSet，然后每加入一个单词，就把这个单词的所有可组合形式从单词全集Set中删除，后续的单词只需要判断是不是在HashSet，如果在，则说明这个单词要加进来，如果不在，则说明被之前更长的单词包括了。耗时最差情况：O(n * 7)，7为单词长度
class Solution {  // 64ms
public:
    int minimumLengthEncoding(vector<string>& words)
    {
        unordered_set<string> m(words.begin(), words.end());
        sort(words.begin(), words.end(), [](const string& a, const string& b) {
            return a.size() > b.size(); // we need to iterate in decreasing order of length
        });

        int res = 0;
        for (string word: words) {
            if (m.find(word) != m.end()) { // add to our string only if no other word had this as suffix
                res += word.size() + 1;
                for (int i = 0;
                     i < word.size(); i++) { // all words which can be formed from this word will be covered here
                    string temp = word.substr(i);
                    if (m.find(temp) != m.end()) {
                        m.erase(temp);
                    }
                }
            }
        }
        return res;
    }
};
```

## [957. N 天后的牢房](https://leetcode-cn.com/problems/prison-cells-after-n-days/)

```c++
#include "gtest/gtest.h"
#include <vector>
#include <unordered_map>

using namespace std;

// https://leetcode.com/problems/prison-cells-after-n-days/discuss/411445/C%2B%2B-easyclean-code-4ms
class Solution {
public:
    vector<int> prisonAfterNDays(vector<int>& cells, int N) {
        int key = 0;
        unordered_map<int, int> mymap;
        while (N-- > 0) {  // 只要N大于0，就需要计算一次cells，所以在while最后会update一次cells
            key = get_key(cells);
            if (mymap.find(key) != mymap.end()) {
                N %= (mymap[key] - N);
            } else {
                mymap[key] = N;
            }
            update_cells(cells);
        }
        return cells;
    }

private:
    int get_key(vector<int>& cells) {
        int tmp = 0;
        // 把cells作为二进制转为十进制数字
        for (int i = 0; i < cells.size(); i++) {
            tmp = (tmp << 1 | cells[i]);
        }
        return tmp;
    }

    void update_cells(vector<int>& cells) {
        vector<int> tmp(8, 0);
        for (int i = 1; i < cells.size() - 1; i++) {
            tmp[i] = (cells[i - 1] == cells[i + 1]);
        }
        cells = tmp;
    }
};

TEST(SolutionTest, Test1) {
    vector<int> cells = {0, 1, 0, 1, 1, 0, 0, 1};
    int N = 7;
    vector<int> expectedCells = {0, 0, 1, 1, 0, 0, 0, 0};
    EXPECT_EQ(expectedCells, Solution().prisonAfterNDays(cells, N));
}

TEST(SolutionTest, Test2) {
    vector<int> cells = {0, 0, 1, 0, 0, 1, 0, 0};
    int N = 7;
    vector<int> expectedCells = {0, 0, 1, 0, 0, 1, 0, 0};
    EXPECT_EQ(expectedCells, Solution().prisonAfterNDays(cells, N));
}

TEST(SolutionTest, Test3) {
    vector<int> cells = {0, 1, 0, 1, 1, 0, 0, 1};
    int dec = 0;
    for (int i = 0; i < cells.size(); i++) {
        dec = (dec << 1 | cells[i]);
    }
    // 另一种将二进制转为十进制数字的方法
//    for (int i = 0; i < cells.size(); ++i) {
//        if (cells[i] > 0) {
//            tmp ^= 1 << i;  // 这里使用异或，相同位就是0，不同就是1，由于每次1左移i都不同，所以如果某位有值，则异或就是1，和|是一样的效果
//        }
//    }
    
    // 十进制转为二进制：https://leetcode-cn.com/problems/prison-cells-after-n-days/solution/n-tian-hou-de-lao-fang-by-leetcode/
    vector<int> originCells(8, 0);
    for (int i = 0; i < cells.size(); ++i) {
        if (((dec >> i) & 1) > 0) {  // 每次右移，如果最后一位为1，则说明该位为二进制1
            originCells[i] = 1;  // 这里与cells相比，是倒序的
        }
    }
}
```

## [2. 两数相加](https://leetcode-cn.com/problems/add-two-numbers/)(华为题库)

```c++
#include "gtest/gtest.h"

using namespace std;

struct ListNode {
    int val;
    ListNode* next;

    ListNode(int x) : val(x), next(NULL) {}
};

class Solution {
public:
    ListNode* addTwoNumbers(ListNode* l1, ListNode* l2) {
        ListNode* ret = new ListNode(0);
        ListNode* cur = ret;
        int carry = 0;
        while (l1 != NULL || l2 != NULL) {
            int val1 = l1 != NULL ? l1->val : 0;
            int val2 = l2 != NULL ? l2->val : 0;
            int sum = carry + val1 + val2;
            carry = sum / 10;
            cur->next = new ListNode(sum % 10);
            cur = cur->next;
            l1 = l1 != NULL ? l1->next : NULL;
            l2 = l2 != NULL ? l2->next : NULL;
        }
        if (carry > 0) {
            cur->next = new ListNode(carry);
        }

        cur = ret->next;
        delete ret;
        return cur;
    }
};

TEST(SolutionTest, Test1) {
    unique_ptr<ListNode> l1(make_unique<ListNode>(ListNode(2)));
    unique_ptr<ListNode> l11(make_unique<ListNode>(ListNode(4)));
    unique_ptr<ListNode> l111(make_unique<ListNode>(ListNode(3)));
    l1->next = l11.get();
    l11->next = l111.get();

    unique_ptr<ListNode> l2(make_unique<ListNode>(ListNode(5)));
    unique_ptr<ListNode> l22(make_unique<ListNode>(ListNode(6)));
    unique_ptr<ListNode> l222(make_unique<ListNode>(ListNode(4)));
    l2->next = l22.get();
    l22->next = l222.get();

    ListNode* l3 = Solution().addTwoNumbers(l1.get(), l2.get());
    EXPECT_EQ(7, l3->val);
    EXPECT_EQ(0, l3->next->val);
    EXPECT_EQ(8, l3->next->next->val);
}

TEST(SolutionTest, Test2) {
    unique_ptr<ListNode> l1(make_unique<ListNode>(ListNode(2)));
    unique_ptr<ListNode> l11(make_unique<ListNode>(ListNode(4)));
    unique_ptr<ListNode> l111(make_unique<ListNode>(ListNode(3)));
    l1->next = l11.get();
    l11->next = l111.get();

    unique_ptr<ListNode> l2(make_unique<ListNode>(ListNode(9)));

    ListNode* l3 = Solution().addTwoNumbers(l1.get(), l2.get());
    EXPECT_EQ(1, l3->val);
    EXPECT_EQ(5, l3->next->val);
    EXPECT_EQ(3, l3->next->next->val);
}

TEST(SolutionTest, Test3) {
    unique_ptr<ListNode> l1(make_unique<ListNode>(ListNode(2)));
    unique_ptr<ListNode> l11(make_unique<ListNode>(ListNode(9)));
    l1->next = l11.get();

    unique_ptr<ListNode> l2(make_unique<ListNode>(ListNode(9)));

    ListNode* l3 = Solution().addTwoNumbers(l1.get(), l2.get());
    EXPECT_EQ(1, l3->val);
    EXPECT_EQ(0, l3->next->val);
    EXPECT_EQ(1, l3->next->next->val);
}
```

## [43. 字符串相乘](https://leetcode-cn.com/problems/multiply-strings/)(华为题库)（只看没做）

<https://leetcode-cn.com/problems/multiply-strings/solution/you-hua-ban-shu-shi-da-bai-994-by-breezean/>

![](\pictures\43. 字符串相乘（方法一：普通竖式）.png)

```c++
class Solution {
    /**
    * 计算形式
    *    num1
    *  x num2
    *  ------
    *  result
    */
public:
    string multiply(string num1, string num2) {  // 56ms
        if (num1 == "0" || num2 == "0") {
            return "0";
        }
        // 保存计算结果
        string res = "0";

        // num2 逐位与 num1 相乘
        for (int i = num2.length() - 1; i >= 0; i--) {
            // 保存 num2 第i位数字与 num1 相乘的结果
            ostringstream temp;
            // 补 0
            for (int j = 0; j < num2.length() - 1 - i; j++) {
                temp << 0;
            }
            int n2 = num2.at(i) - '0';

            int carry = 0;
            // num2 的第 i 位数字 n2 与 num1 相乘
            for (int j = num1.length() - 1; j >= 0 || carry != 0; j--) {
                int n1 = j < 0 ? 0 : num1.at(j) - '0';
                int product = (n1 * n2 + carry) % 10;
                temp << product;
                carry = (n1 * n2 + carry) / 10;
            }

            // 将当前结果与新计算的结果求和作为新的结果
            string&& tempStr = temp.str();
            reverse(tempStr.begin(), tempStr.end());
            res = addStrings(res, tempStr);
        }
        return res;
    }

private:
    /**
     * 对两个字符串数字进行相加，返回字符串形式的和
     */
    string addStrings(const string& num1, const string& num2) {
        ostringstream builder;
        int carry = 0;
        for (int i = num1.length() - 1, j = num2.length() - 1;
             i >= 0 || j >= 0 || carry != 0;
             i--, j--) {
            int x = i < 0 ? 0 : num1.at(i) - '0';
            int y = j < 0 ? 0 : num2.at(j) - '0';
            int sum = (x + y + carry) % 10;
            builder << sum;
            carry = (x + y + carry) / 10;
        }
        string&& tempStr = builder.str();
        reverse(tempStr.begin(), tempStr.end());
        return tempStr;
    }
};

// 略微优化过的
class Solution {
    /**
    * 计算形式
    *    num1
    *  x num2
    *  ------
    *  result
    */
public:
    string multiply(string num1, string num2) {
        if (num1 == "0" || num2 == "0") {
            return "0";
        }
        // 保存计算结果
        string res = "0";

        // num2 逐位与 num1 相乘
        for (int i = num2.length() - 1; i >= 0; i--) {
            // 保存 num2 第i位数字与 num1 相乘的结果
            ostringstream temp;
            // 补 0
            for (int j = 0; j < num2.length() - 1 - i; j++) {
                temp << 0;
            }
            int n2 = num2.at(i) - '0';

            int carry = 0;
            // num2 的第 i 位数字 n2 与 num1 相乘
            for (int j = num1.length() - 1; j >= 0 || carry != 0; j--) {
                int n1 = j < 0 ? 0 : num1.at(j) - '0';
                int product = (n1 * n2 + carry) % 10;
                temp << product;
                carry = (n1 * n2 + carry) / 10;
            }

            // 将当前结果与新计算的结果求和作为新的结果
            res = move(addStrings(res, temp.str()));
        }

        // 之前的所有字符串相加都是逆序的，最后返回时要转换成正序
        reverse(res.begin(), res.end());
        return move(res);
    }

private:
    /**
     * 对两个字符串数字进行相加，返回字符串形式的和，这里直接是逆序的字符串相加
     */
    string addStrings(const string& num1, const string& num2) {
        ostringstream builder;
        int carry = 0;
        for (int i = 0, j = 0;
             i < num1.length() || j < num2.length() || carry != 0;
             ++i, ++j) {
            int x = i >= num1.length() ? 0 : num1.at(i) - '0';
            int y = j >= num2.length() ? 0 : num2.at(j) - '0';
            int sum = (x + y + carry) % 10;
            builder << sum;
            carry = (x + y + carry) / 10;
        }
        return move(builder.str());
    }
};
```

![](\pictures\43. 字符串相乘（方法二：优化竖式）.png)

```c++
class Solution {  // 优化竖式
public:
    string multiply(string num1, string num2) {
        if (num1 == "0" || num2 == "0") {
            return "0";
        }

        vector<int> retInts(num1.size() + num2.size(), 0);
        for (int i = num1.size() - 1; i >= 0; --i) {
            int n1 = num1.at(i) - '0';
            for (int j = num2.size() - 1; j >= 0; --j) {
                int n2 = num2.at(j) - '0';
                int sum = retInts[i + j + 1] + n1 * n2;
                retInts[i + j + 1] = sum % 10;
                retInts[i + j] += sum / 10;
            }
        }

        ostringstream oss;
        int index = 0;
        for_each(retInts.begin(), retInts.end(), [&](int i) {
            if (index++ == 0 && i == 0) {  // 如果首位为0，则不需要拼接
                return;
            }
            oss << i;
        });
        return oss.str();
    }
};
```

## [394. 字符串解码](https://leetcode-cn.com/problems/decode-string/)(华为题库)

```c++
#include <stack>
#include <cmath>
#include "gtest/gtest.h"

using namespace std;

// 把每个char单独推送入stack
class Solution {  // 4ms
public:
    string decodeString(string s) {
        ostringstream outputOss;
        stack<char> charStack;
        for (int i = 0; i < s.size(); ++i) {
            char c = s.at(i);
            if (c != ']') {  // 如果不是]，就直接进栈
                charStack.push(c);
            } else {  // 如果是[，是出栈展开
                getTempEncodedStr(charStack);
            }
        }

        while (!charStack.empty()) {
            outputOss << charStack.top();
            charStack.pop();
        }
        string&& outputStr = outputOss.str();
        reverse(outputStr.begin(), outputStr.end());
        return move(outputStr);
    }

private:
    void getTempEncodedStr(stack<char>& charStack) {
        ostringstream tempOss;
        while (charStack.top() != '[') {
            tempOss << charStack.top();
            charStack.pop();
        }
        charStack.pop();  // 把[弹出

        string&& tempStr = tempOss.str();
        reverse(tempStr.begin(), tempStr.end());

        int num = 0;
        int index = 0;
        while (!charStack.empty() && charStack.top() >= '0' && charStack.top() <= '9') {
            num += pow(10, index++) * (charStack.top() - '0');
            charStack.pop();
        }

        tempOss.str("");
        for (int i = 0; i < num; ++i) {
            tempOss << tempStr;
        }

        tempStr = tempOss.str();
        for_each(tempStr.begin(), tempStr.end(), [&](char c) { charStack.push(c); });
    }
};

TEST(SolutionTest, Test1) {
    EXPECT_EQ("aaabcbc", Solution().decodeString("3[a]2[bc]"));
}

TEST(SolutionTest, Test2) {
    EXPECT_EQ("accaccacc", Solution().decodeString("3[a2[c]]"));
}

TEST(SolutionTest, Test3) {
    EXPECT_EQ("abcabccdcdcdef", Solution().decodeString("2[abc]3[cd]ef"));
}

TEST(SolutionTest, Test4) {
    EXPECT_EQ("abcabcabcabcabcabcabcabcabcabc",Solution().decodeString("10[abc]"));
}

TEST(SolutionTest, Test5) {
    EXPECT_EQ("zzzyypqjkjkefjkjkefjkjkefjkjkefyypqjkjkefjkjkefjkjkefjkjkefef",
        Solution().decodeString("3[z]2[2[y]pq4[2[jk]e1[f]]]ef"));
}

// 这里把组合后的string入deque
class Solution {  // 也是4ms
public:
    string decodeString(string s) {
        ostringstream outputOss;
        deque<string> stringDeque;
        for (int i = 0; i < s.size(); ++i) {
            char c = s.at(i);
            if (c != ']') {  // 如果不是]，就直接进栈
                stringDeque.push_back(string(1, c));
            } else {  // 如果是[，是出栈展开
                getTempEncodedStr(stringDeque);
            }
        }

        while (!stringDeque.empty()) {
            outputOss << stringDeque.front();
            stringDeque.pop_front();
        }
        return move(outputOss.str());
    }

private:
    void getTempEncodedStr(deque<string>& stringDeque) {
        stack<string> tempStack;
        while (stringDeque.back() != "[") {
            tempStack.push(stringDeque.back());
            stringDeque.pop_back();
        }
        stringDeque.pop_back();  // 把[弹出

        ostringstream tempOss;
        while (!tempStack.empty()) {
            tempOss << tempStack.top();
            tempStack.pop();
        }

        int num = 0;
        int index = 0;
        while (!stringDeque.empty() && stringDeque.back() >= "0" && stringDeque.back() <= "9") {
            num += pow(10, index++) * (stringDeque.back().at(0) - '0');
            stringDeque.pop_back();
        }

        string&& tempStr = tempOss.str();
        tempOss.str("");
        for (int i = 0; i < num; ++i) {
            tempOss << tempStr;
        }

        stringDeque.push_back(tempOss.str());
    }
};

// 别人算法，同样是4ms，但是逻辑较简单
class Solution {
public:
    string decodeString(string s) {
        stack<int> numStack;
        stack<string> resStack;
        int num = 0;
        string res;
        for (int i = 0; i < s.size(); i++) {
            if (isalpha(s[i])) {
                res.push_back(s[i]);
            } else if (isdigit(s[i])) {
                num = num * 10 + s[i] - '0';
            } else if (s[i] == '[') {
                resStack.push(res);
                res = "";
                numStack.push(num);
                num = 0;
            } else {
                for (int j = 0; j < numStack.top(); j++) {
                    resStack.top() += res;
                }
                numStack.pop();
                res = resStack.top();
                resStack.pop();
            }
        }
        return res;
    }
};

// https://leetcode.cn/problems/decode-string/solutions/264391/zi-fu-chuan-jie-ma-by-leetcode-solution/
class Solution {
public:
    string decodeString(string s) {
        src = s;
        ptr = 0;
        return getString();
    }

private:
    string getString() {
        if (ptr == src.size() || src[ptr] == ']') {
            // String -> EPS
            return "";
        }

        char cur = src[ptr];
        int repTime = 1;
        string ret;

        if (isdigit(cur)) {
            // String -> Digits [ String ] String
            // 解析 Digits
            repTime = getDigits();
            // 过滤左括号
            ++ptr;
            // 解析 String
            string str = getString();
            // 过滤右括号
            ++ptr;
            // 构造字符串
            while (repTime--) {
                ret += str;
            }
        } else if (isalpha(cur)) {
            // String -> Char String
            // 解析 Char
            ret = string(1, src[ptr++]);
        }

        return ret + getString();
    }

    int getDigits() {
        int ret = 0;
        while (ptr < src.size() && isdigit(src[ptr])) {
            ret = ret * 10 + src[ptr++] - '0';
        }
        return ret;
    }

private:
    string src;
    size_t ptr;
};
```

## [93. 复原IP地址](https://leetcode-cn.com/problems/restore-ip-addresses/)(华为题库)

回溯算法，深度搜索递归遍历全部字符串，查看是否符合IP要求：

```c++
#include <queue>
#include <numeric>
#include "gtest/gtest.h"

using namespace std;

class Solution {
    typedef vector<string> Ips;
    typedef deque<string> IpSegment;
public:
    vector<string> restoreIpAddresses(string s) {
        Ips ips;
        IpSegment tmpIpSegment;
        splitIps(0, 1, s, tmpIpSegment, ips);
        return ips;
    }

private:
    void splitIps(int startIndex, int segmentNumber, const string& inputStr, IpSegment& tmpIpSegment, Ips& ips) {
        if (segmentNumber == 4) {
            string fourthSegment = inputStr.substr(startIndex);
            if (isValidIpSegment(fourthSegment)) {
                tmpIpSegment.push_back(fourthSegment);
                addOneIp(tmpIpSegment, ips);
                tmpIpSegment.pop_back();
            }
            return;
        }

        int indexIncrement = 0;
        while ((++indexIncrement + startIndex) < inputStr.size()) {
            string nextSegment = inputStr.substr(startIndex, indexIncrement);
            if (isValidIpSegment(nextSegment)) {
                tmpIpSegment.push_back(nextSegment);
                splitIps(startIndex + indexIncrement, segmentNumber + 1, inputStr, tmpIpSegment, ips);
                tmpIpSegment.pop_back();
            } else {
                break;
            }
        }
    }

    void addOneIp(const IpSegment& tmpIpSegment, Ips& ips) {
        string ip = accumulate(tmpIpSegment.begin() + 1, tmpIpSegment.end(), *tmpIpSegment.begin(),
                               [](const string& allIpSegment, const string& tmp) {
                                   return allIpSegment + "." + tmp;
                               });
        ips.push_back(ip);
    }

    bool isValidIpSegment(const string& segmentStr) {
        if (segmentStr.size() <= 3) {
            int segment = stringToInt(segmentStr);
            string convertedSegmentStr = intToString(segment);
            return segment >= 0 && segment <= 255 && segmentStr == convertedSegmentStr;
        }
        return false;
    }

    int stringToInt(const string& s) {
        char* offset;
        return strtol(s.c_str(), &offset, 10);
    }

    string intToString(int num) {
        stringstream ss;
        ss << num;
        return string(ss.str());
    }
};

TEST(SolutionTest, Test1) {
    string input = "25525511135";
    vector<string> ips = {"255.255.11.135", "255.255.111.35"};
    EXPECT_EQ(ips, Solution().restoreIpAddresses(input));
}

TEST(SolutionTest, Test2) {
    string input = "";
    vector<string> ips;
    EXPECT_EQ(ips, Solution().restoreIpAddresses(input));
}

TEST(SolutionTest, Test3) {
    string input = "255255255255";
    vector<string> ips = {"255.255.255.255"};
    EXPECT_EQ(ips, Solution().restoreIpAddresses(input));
}

TEST(SolutionTest, Test4) {
    string input = "2552552552552";
    vector<string> ips;
    EXPECT_EQ(ips, Solution().restoreIpAddresses(input));
}

TEST(SolutionTest, Test5) {
    string input = "0000";
    vector<string> ips = {"0.0.0.0"};
    EXPECT_EQ(ips, Solution().restoreIpAddresses(input));
}

TEST(SolutionTest, Test6) {
    string input = "1111";
    vector<string> ips = {"1.1.1.1"};
    EXPECT_EQ(ips, Solution().restoreIpAddresses(input));
}

TEST(SolutionTest, Test7) {
    string input = "111";
    vector<string> ips;
    EXPECT_EQ(ips, Solution().restoreIpAddresses(input));
}
```

## [55. 跳跃游戏](https://leetcode-cn.com/problems/jump-game/)(华为题库)

从后往前遍历，找到能跳到这个位置的最前一个index，再从这个位置继续往前遍历，直到找到index为0的位置：

```c++
class Solution {  // 736ms
public:
    bool canJump(vector<int>& nums) {
        int index = findTheMinimalIndexToDstIndex(nums, nums.size() - 1);
        return index == 0;
    }

private:
    int findTheMinimalIndexToDstIndex(vector<int>& nums, int dstIndex) {
        int availableIndex = dstIndex;
        for (int index = dstIndex - 1; index >= 0; --index) {
            if (nums.at(index) >= dstIndex - index) {
                availableIndex = index;
            }
        }
        if (availableIndex == 0) {
            return availableIndex;
        } else if (availableIndex != dstIndex) {
            return findTheMinimalIndexToDstIndex(nums, availableIndex);;
        } else {
            return -1;
        }
    }
};

TEST(SolutionTest, Test1) {
    vector<int> nums = {2, 3, 1, 1, 4};
    EXPECT_EQ(true, Solution().canJump(nums));
}

TEST(SolutionTest, Test2) {
    vector<int> nums = {3, 2, 1, 0, 4};
    EXPECT_EQ(false, Solution().canJump(nums));
}

TEST(SolutionTest, Test3) {
    vector<int> nums = {0};
    EXPECT_EQ(true, Solution().canJump(nums));
}
```

参考别人的优化代码：

<https://leetcode-cn.com/problems/jump-game/solution/55-by-ikaruga/>

 解题思路：

1. 如果某一个作为 **起跳点** 的格子可以跳跃的距离是 3，那么表示后面 3 个格子都可以作为 **起跳点**。
2. 可以对每一个能作为 **起跳点** 的格子都尝试跳一次，把 **能跳到最远的距离** 不断更新。
3. 如果可以一直跳到最后，就成功了。

```c++

class Solution {  // 12ms
public:
    bool canJump(vector<int>& nums) {
        int k = 0;
        int maxIndex = nums.size() - 1;
        for (int i = 0; i < nums.size(); i++) {
            if (k >= maxIndex) {
                return true;
            }
            if (i > k) {
                return false;
            }
            k = max(k, i + nums[i]);
        }
        return true;
    }
};
```

另一个优化代码：

<https://leetcode-cn.com/problems/jump-game/solution/hui-dao-zui-chu-de-qi-dian-by-martinyue/>

从最后一个点依次往前，看当前点是不是可以可到达终点，如果可达到就以当前点为观察点，再看它是不是可由更左边的点可到达，直到第1个点。

```c++
class Solution {  // 12ms
public:
    bool canJump(vector<int>& nums) {
        int leftPoint = nums.size() - 1;
        for (int index = nums.size() - 2; index >= 0; index--) {
            if (nums[index] >= leftPoint - index) {
                leftPoint = index;
            }
        }
        return leftPoint == 0 ? 1 : 0;
    }
};
```

## [8. 字符串转换整数 (atoi)](https://leetcode-cn.com/problems/string-to-integer-atoi/)(华为题库)（只看没做）

别人的解法： 

<https://leetcode-cn.com/problems/string-to-integer-atoi/solution/zi-fu-chuan-zhuan-huan-zheng-shu-atoi-cshi-xian-li/>

使用正则表达式：

```java
#include <regex>
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 436 ms
public:
    int myAtoi(string str) {
        int result;
        string resultStr;
        smatch tmpMatch;
        //c++中要用\\进行转义
        regex tmpRegex("^[\\+\\-\\d]\\d*");
        //先将左边的空格去除
        trimStart(str);
        regex_search(str, tmpMatch, tmpRegex);

        stringstream ss;
        if (int(tmpMatch.size()) == 0) {
            return 0;
        }
        string tmpStr = tmpMatch[0];
        //转换成int
        ss << tmpStr;
        long longResult;
        ss >> longResult;
        result = max(min(longResult, long(INT_MAX)), long(INT_MIN));

        return result;
    }

    void trimStart(string& s) {
        if (!s.empty()) {
            s.erase(0, s.find_first_not_of(" "));
            //s.erase(s.find_last_not_of(" ") + 1);
        }
    }
};

TEST(SolutionTest, Test1) {
    EXPECT_EQ(42, Solution().myAtoi("42"));
}

TEST(SolutionTest, Test2) {
    EXPECT_EQ(-42, Solution().myAtoi("         -42"));
}

TEST(SolutionTest, Test3) {
    EXPECT_EQ(0, Solution().myAtoi("words and 987"));
}

TEST(SolutionTest, Test4) {
    EXPECT_EQ(-2147483648, Solution().myAtoi("-91283472332"));
}
```

使用stringstream：

```java
class Solution {  // 8 ms
public:
    int myAtoi(string str) {
        int result = 0;
        stringstream ss;
        ss << str;
        ss >> result;
        return result;
    }
};
```

## [322. 零钱兑换](https://leetcode-cn.com/problems/coin-change/)(华为题库)（只看没做）

<https://leetcode-cn.com/problems/coin-change/solution/ling-qian-dui-huan-by-leetcode/>

方法一：暴力法[超出时间限制]

全遍历所有硬币组合，第1个硬币从取0个，再继续遍历第2个硬币从取0个，再到第3个硬币从取0个到最大个，再到第2个硬币取1个，再到第3个硬币从取0个到最大个，。。。，再到第1个硬币取1个，依次遍历所有组合：

```java
// Solution.java
package test;

class Solution {
    public int coinChange(int[] coins, int amount) {
        return coinChange(0, coins, amount);
    }

    private int coinChange(int idxCoin, int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        }
        if (idxCoin < coins.length && amount > 0) {
            int maxVal = amount / coins[idxCoin];
            int minCost = Integer.MAX_VALUE;
            for (int x = 0; x <= maxVal; x++) {
                if (amount >= x * coins[idxCoin]) {
                    int res = coinChange(idxCoin + 1, coins, amount - x * coins[idxCoin]);
                    if (res != -1) {
                        minCost = Math.min(minCost, res + x);
                    }
                }
            }
            return (minCost == Integer.MAX_VALUE) ? -1 : minCost;
        }
        return -1;
    }
}

// SolutionTest.java
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        int[] coins = {1, 2, 5};
        int amount = 11;
        assertEquals(3, SOLUTION.coinChange(coins, amount));
    }

    @Test
    public void test2() {
        int[] coins = {2};
        int amount = 3;
        assertEquals(-1, SOLUTION.coinChange(coins, amount));
    }

    @Test
    public void test3() {
        int[] coins = {2, 3, 7};
        int amount = 15;
        assertEquals(4, SOLUTION.coinChange(coins, amount));
    }
}
```

方法二：动态规划-自上而下[通过]

用count数组保存S金额时最小数量硬币，用res表示在rem金额时，依次遍历各种硬币种类，所需最小硬币个数，与临时变量min相比，判断当前rem金额所需的最小硬币个数，并保存在count数组中。

将全部情况遍历后，即得到最小总硬币个数。

```java
package test;

class Solution {  // 34 ms
    public int coinChange(int[] coins, int amount) {
        if (amount < 1) {
            return 0;
        }
        return coinChange(coins, amount, new int[amount]);
    }

    private int coinChange(int[] coins, int rem, int[] count) {
        if (rem < 0) {
            return -1;
        }
        if (rem == 0) {
            return 0;
        }
        if (count[rem - 1] != 0) {
            return count[rem - 1];
        }
        int min = Integer.MAX_VALUE;
        for (int coin : coins) {  // 在总金额为rem时，取一个硬币面值为coin时
            int res = coinChange(coins, rem - coin, count);  // 计算此情况时，剩余金额所需的最小硬币个数
            if (res >= 0 && res < min) {
                min = 1 + res;  // 递推公式也就是F(S) = F(S - coin) + 1
            }
        }
        count[rem - 1] = (min == Integer.MAX_VALUE) ? -1 : min;
        return count[rem - 1];
    }
}
```

方法三：动态规划-自下而上[通过]

![](pictures\322. 零钱兑换（方法三：动态规划-自下而上）.png)

```java
package test;

import java.util.Arrays;

class Solution {
    public int coinChange(int[] coins, int amount) {
        int max = amount + 1;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, max);
        dp[0] = 0;
        for (int i = 1; i <= amount; i++) {
            for (int j = 0; j < coins.length; j++) {
                if (coins[j] <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);  // 
                }
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }
}
```

上述代码自己用C++重新写一遍：

```c++
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    int coinChange(vector<int>& coins, int amount) {
        int invalidValue = amount + 1;
        vector<int> dp(amount + 1, invalidValue);
        dp[0] = 0;
        for (int i = 1; i <= amount; ++i) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        return dp[amount] == invalidValue ? -1 : dp[amount];
    }
};

TEST(SolutionTest, Test1) {
    vector<int> coins = {1, 2, 5};
    int amount = 11;
    EXPECT_EQ(3, Solution().coinChange(coins, amount));
}

TEST(SolutionTest, Test2) {
    vector<int> coins = {2};
    int amount = 3;
    EXPECT_EQ(-1, Solution().coinChange(coins, amount));
}

TEST(SolutionTest, Test3) {
    vector<int> coins = {2, 3, 7};
    int amount = 15;
    EXPECT_EQ(4, Solution().coinChange(coins, amount));
}
```

## [12. 整数转罗马数字](https://leetcode-cn.com/problems/integer-to-roman/)(华为题库)

全遍历所有组合即可：

```java
//
package test;

class Solution {  // 6ms
    public String intToRoman(int num) {
        int thousandsDigit = num / 1000;
        int left = num % 1000;

        int hundredsDigit = left / 100;
        left = left % 100;

        int tensDigit = left / 10;

        int digits = left % 10;

        return getDigitsRoman(thousandsDigit, "M", "", "") + getDigitsRoman(hundredsDigit, "C", "D", "M") +
            getDigitsRoman(tensDigit, "X", "L", "C") + getDigitsRoman(digits, "I", "V", "X");
    }

    private String getDigitsRoman(int digits, String smallAlpha, String middleAlpha, String bigAlpha) {
        StringBuilder stringBuilder = new StringBuilder();
        if (digits == 0) {
            return stringBuilder.toString();
        } else if (digits >= 1 && digits <= 3) {
            for (int i = 0; i < digits; i++) {
                stringBuilder.append(smallAlpha);
            }
            return stringBuilder.toString();
        } else if (digits == 4) {
            return smallAlpha + middleAlpha;
        } else if (digits == 5) {
            return middleAlpha;
        } else if (digits >= 6 && digits <= 8) {
            stringBuilder.append(middleAlpha);
            for (int i = 6; i <= digits; i++) {
                stringBuilder.append(smallAlpha);
            }
            return stringBuilder.toString();
        } else {
            return smallAlpha + bigAlpha;
        }
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {

    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        assertEquals("III", SOLUTION.intToRoman(3));
    }

    @Test
    public void test2() {
        assertEquals("IV", SOLUTION.intToRoman(4));
    }

    @Test
    public void test3() {
        assertEquals("IX", SOLUTION.intToRoman(9));
    }

    @Test
    public void test4() {
        assertEquals("LVIII", SOLUTION.intToRoman(58));
    }

    @Test
    public void test5() {
        assertEquals("MCMXCIV", SOLUTION.intToRoman(1994));
    }

    @Test
    public void test6() {
        assertEquals("VI", SOLUTION.intToRoman(6));
    }
}
```

参考别人的优化代码<https://leetcode-cn.com/problems/integer-to-roman/solution/zheng-shu-zhuan-luo-ma-shu-zi-cshi-xian-liang-chon/>：

方法一：暴力法：

```java
class Solution {
    public String intToRoman(int num) {  // 5ms
        String thousandsDigits[] = {"", "M", "MM", "MMM"};
        String hundredDigits[] = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String tensDigits[] = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String digits[] = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
        return thousandsDigits[num / 1000 % 10] + hundredDigits[num / 100 % 10] + tensDigits[num / 10 % 10] + digits[num % 10];
    }
}
```

方法二：贪心法：

```java
class Solution {  // 5ms
    public String intToRoman(int num) {
        StringBuilder result = new StringBuilder();
        int store[] = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String strs[] = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        int storeSize = store.length;
        //贪心法
        for (int i = 0; i < storeSize; i++) {
            while (num >= store[i]) {
                result.append(strs[i]);
                num -= store[i];
            }
        }
        return result.toString();
    }
}
```

## [54. 螺旋矩阵](https://leetcode-cn.com/problems/spiral-matrix/)(华为题库)（只看没做）

<https://leetcode-cn.com/problems/spiral-matrix/solution/luo-xuan-ju-zhen-by-leetcode/>

方法 1：模拟

绘制螺旋轨迹路径，我们发现当路径超出界限或者进入之前访问过的单元格时，会顺时针旋转方向。

假设数组有 \text{R}R 行 \text{C}C 列，\text{seen[r][c]}seen[r][c] 表示第 r 行第 c 列的单元格之前已经被访问过了。当前所在位置为 \text{(r, c)}(r, c)，前进方向是 \text{di}di。我们希望访问所有 \text{R}R x \text{C}C 个单元格。

当我们遍历整个矩阵，下一步候选移动位置是 \text{(cr, cc)}(cr, cc)。如果这个候选位置在矩阵范围内并且没有被访问过，那么它将会变成下一步移动的位置；否则，我们将前进方向顺时针旋转之后再计算下一步的移动位置。

```java
//
package test;

import java.util.ArrayList;
import java.util.List;

class Solution {  // 0ms
    public List<Integer> spiralOrder(int[][] matrix) {
        List ans = new ArrayList();
        if (matrix.length == 0) {
            return ans;
        }
        int R = matrix.length, C = matrix[0].length;
        boolean[][] seen = new boolean[R][C];
        int[] dr = {0, 1, 0, -1};
        int[] dc = {1, 0, -1, 0};
        int r = 0, c = 0, di = 0;
        for (int i = 0; i < R * C; i++) {
            ans.add(matrix[r][c]);
            seen[r][c] = true;
            int cr = r + dr[di];  // di在不断循环0~3，用于控制旋转和前进的方向
            int cc = c + dc[di];
            if (0 <= cr && cr < R && 0 <= cc && cc < C && !seen[cr][cc]) {
                r = cr;
                c = cc;
            } else {
                di = (di + 1) % 4;
                r += dr[di];
                c += dc[di];
            }
        }
        return ans;
    }
}

//
package test;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {

    private static final Solution SOLUTION = new Solution();

	@Test
    public void test1() {
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        assertEquals(Arrays.asList(1, 2, 3, 6, 9, 8, 7, 4, 5), SOLUTION.spiralOrder(matrix));
    }

    @Test
    public void test2() {
        int[][] matrix = {
            {3},
            {2}
        };
        assertEquals(Arrays.asList(3, 2), SOLUTION.spiralOrder(matrix));
    }
}
```

方法 2：按层模拟

答案是最外层所有元素按照顺时针顺序输出，其次是次外层，以此类推。

我们定义矩阵的第 k 层是到最近边界距离为 k 的所有顶点。例如，下图矩阵最外层元素都是第 1 层，次外层元素都是第 2 层，然后是第 3 层的。

[[1, 1, 1, 1, 1, 1, 1],
 [1, 2, 2, 2, 2, 2, 1],
 [1, 2, 3, 3, 3, 2, 1],
 [1, 2, 2, 2, 2, 2, 1],
 [1, 1, 1, 1, 1, 1, 1]]
对于每层，我们从左上方开始以顺时针的顺序遍历所有元素，假设当前层左上角坐标是 \text{(r1, c1)}(r1, c1)，右下角坐标是 \text{(r2, c2)}(r2, c2)。

首先，遍历上方的所有元素 \text{(r1, c)}(r1, c)，按照 \text{c = c1,...,c2}c = c1,...,c2 的顺序。然后遍历右侧的所有元素 \text{(r, c2)}(r, c2)，按照 \text{r = r1+1,...,r2}r = r1+1,...,r2 的顺序。如果这一层有四条边（也就是 \text{r1 < r2}r1 < r2 并且 \text{c1 < c2}c1 < c2 ），我们以下图所示的方式遍历下方的元素和左侧的元素。

```java
class Solution {  // 0ms
    public List<Integer> spiralOrder(int[][] matrix) {
        List ans = new ArrayList();
        if (matrix.length == 0) {
            return ans;
        }
        int r1 = 0, r2 = matrix.length - 1;
        int c1 = 0, c2 = matrix[0].length - 1;
        while (r1 <= r2 && c1 <= c2) {
            for (int c = c1; c <= c2; c++) {
                ans.add(matrix[r1][c]);
            }
            for (int r = r1 + 1; r <= r2; r++) {
                ans.add(matrix[r][c2]);
            }
            if (r1 < r2 && c1 < c2) {  // 必须同时满足r1 < r2和c1 < c2，这时才是个至少包含4个点的长方体，才需要加下边和左边，如果去掉这个判断条件，只有一条边时会重复增加点
                for (int c = c2 - 1; c > c1; c--) {
                    ans.add(matrix[r2][c]);
                }
                for (int r = r2; r > r1; r--) {
                    ans.add(matrix[r][c1]);
                }
            }
            r1++;
            r2--;
            c1++;
            c2--;
        }
        return ans;
    }
}
```

别人写的Python实现：

重点看这一行：

`matrix = list(map(list, zip(*matrix)))[::-1]`

如果调用前matrix为：

[[4, 5, 6], [7, 8, 9]]

则调用完，matrix为：

[[6, 9], [5, 8], [4, 7]]

这里首先zip(*matrix)将matrix各行依次zip成tuple（此时zip对象是个生成器），用map将每个tuple转成list（此时map对象还是个生成器），再list成列表对象，再用切片倒序，再赋值给matrix，从而完成了matrix的逆时针旋转。

这里有几个点要注意下：

* 只有list或tuple可以进行切片操作，而生成器对象无法切片，所以最外层要用list转出来再切片倒序
* []的+只能加[]对象，所以要用map将zip里的每个tuple对象转成list对象
* tuple是不可变对象，没有pop方法
* l是list，l += [1, 2]在原l内存地址上扩展元素，使用id(l)操作前后地址一样；l = l + [1, 2]会新生成一个list对象并用l引用，使用id(l)操作前后地址变化
* t是tuple，为不可变对象，无论使用t += (1, 2)还是t = t + (1, 2)，使用id(t)操作前后地址均会变化

关于typing的使用，可以参考<https://www.cnblogs.com/cwp-bg/p/7825729.html>。

```python
# solution.py
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from typing import List


class Solution:
    def spiralOrder(self, matrix: List[List[int]]) -> List[int]:
        res = []
        while matrix:
            res += matrix.pop(0)  # 这里会把首行list弹出，将元素加到res中
            matrix = list(map(list, zip(*matrix)))[::-1]
        return res

# test_solution.py
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from unittest import TestCase

from src.solution import Solution


class TestSolution(TestCase):
    SOLUTION = Solution()

    def test(self):
        matrix = [
            [1, 2, 3],
            [4, 5, 6],
            [7, 8, 9]
        ]
        self.assertEqual([1,2,3,6,9,8,7,4,5], self.SOLUTION.spiralOrder(matrix))

```

## [300. 最长上升子序列](https://leetcode-cn.com/problems/longest-increasing-subsequence/)(华为题库)（只看没做）

<https://leetcode-cn.com/problems/longest-increasing-subsequence/solution/zui-chang-shang-sheng-zi-xu-lie-by-leetcode/>

方法一：暴力法

算法：
最简单的方法是找到所有增加的子序列，然后返回最长增加的子序列的最大长度。为了做到这一点，我们使用递归函数 \text length of lislengthoflis 返回从当前元素（对应于 curposcurpos）开始可能的 lis 长度（包括当前元素）。在每个函数调用中，我们考虑两种情况：

当前元素大于包含在 lis 中的前一个元素。在这种情况下，我们可以在 lis 中包含当前元素。因此，我们通过将其包含在内，得出了 lis 的长度。此外，我们还通过不在 lis 中包含当前元素来找出 lis 的长度。因此，当前函数调用返回的值是两个长度中的最大值。
当前元素小于包含在 lis 中的前一个元素。在这种情况下，我们不能在 lis 中包含当前元素。因此，我们只通过不在 lis 中包含当前元素（由当前函数调用返回）来确定 lis 的长度。

```java
//
package test;

public class Solution {  // 超时

    public int lengthOfLIS(int[] nums) {
        return lengthofLIS(nums, Integer.MIN_VALUE, 0);
    }

    private int lengthofLIS(int[] nums, int prev, int curpos) {
        if (curpos == nums.length) {
            return 0;
        }
        int taken = 0;
        if (nums[curpos] > prev) {  // 如果当前元素大于前一个元素，则计算包含当前元素情况下，后面全部最长上升子序列长度
            taken = 1 + lengthofLIS(nums, nums[curpos], curpos + 1);  // 包含当前元素时的最长
        }
        int nottaken = lengthofLIS(nums, prev, curpos + 1);  // 计算不包含当前元素情况下，后面全部最长上升子序列长度
        return Math.max(taken, nottaken);  // 最上面两种情况的最大值
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {

    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
        assertEquals(4, SOLUTION.lengthOfLIS(nums));
    }
}
```

方法二：带记忆的递归
算法：
在前面的方法中，许多递归调用必须使用相同的参数进行一次又一次的调用。通过将为特定调用获得的结果存储在二维记忆数组 memo 中，可以消除这种冗余。`memo[i][j] `表示使用` nums[i]`作为上一个被认为包含/不包含在 lis 中的元素的 lis 可能的长度，其中 `nums[j]`作为当前被认为包含/不包含在 lis 中的元素。这里，nums表示给定的数组。

```java
public class Solution {
    public int lengthOfLIS(int[] nums) {  // 114ms
        int memo[][] = new int[nums.length + 1][nums.length];
        for (int[] l : memo) {
            Arrays.fill(l, -1);
        }
        return lengthofLIS(nums, -1, 0, memo);
    }

    public int lengthofLIS(int[] nums, int previndex, int curpos, int[][] memo) {
        if (curpos == nums.length) {
            return 0;
        }
        if (memo[previndex + 1][curpos] >= 0) {
            return memo[previndex + 1][curpos];
        }
        int taken = 0;
        if (previndex < 0 || nums[curpos] > nums[previndex]) {
            taken = 1 + lengthofLIS(nums, curpos, curpos + 1, memo);
        }

        int nottaken = lengthofLIS(nums, previndex, curpos + 1, memo);
        memo[previndex + 1][curpos] = Math.max(taken, nottaken);  // 表示以previous为前一个值，curpos为当前值的最大上升子序列个数，后面再用到时，可以直接获取，而不用重复计算
        return memo[previndex + 1][curpos];
    }
}
```

方法三：动态规划

我们使用 dp 数组来存储所需的数据。dp[i]表示考虑到数组元素一直到第i个元素的情况下可能的最长上升子序列的长度，包括第i个元素。

当判断到第i个元素时，如果第i个元素比第j个元素大，则此次i、j循环中，最大maxval为dp[j]+1，遍历出所有的j，取出最大的maxval则为当前判断到第i个元素的最长上升子序列时dp[i]。

```java
public class Solution {  // 13ms
    public int lengthOfLIS(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int[] dp = new int[nums.length];
        dp[0] = 1;
        int maxans = 1;
        for (int i = 1; i < dp.length; i++) {
            int maxval = 0;
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    maxval = Math.max(maxval, dp[j]);
                }
            }
            dp[i] = maxval + 1;
            maxans = Math.max(maxans, dp[i]);
        }
        return maxans;
    }
}
```

自己重新写的：

```java
public class Solution {
    public int lengthOfLIS(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }

        int[] dp = new int[nums.length];
        dp[0] = 1;  // 只要nums不为空，则至少有一个元素，则dp[0]就为1
        int maxAns = 1;  // 只要nums不为空，则maxAns至少为1
        for (int i = 1; i < nums.length; i++) {
            dp[i] = 1;  // 每次遍历到i时，dp[i]至少为1
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {  // 如果nums[i] > nums[j]，则此时dp[i] = dp[j] + 1，至于这么多j值，哪个最大，则需要Math.max所有的dp[j] + 1值
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxAns = Math.max(maxAns, dp[i]);  // 再遍历所有的dp[i]，获取最长的上升子序列
        }
        return maxAns;
    }
}
```

方法四：动态规划和二分搜索

在这种方法中，我们从左到右扫描数组。我们还使用了 0 初始化的 dp 数组。此 dp 数组用于存储当前遇到的元素形成的上升子序列。当遍历 nums 数组时，我们继续用到目前为止遇到的元素填充 dp 数组。对应与第j个（nums[j]）对应的元素，我们通过使用二分搜索（由于 dp数组存储递增的子序列，因此可以使用二分搜索）来确定其在 dp数组中的正确位置，需要注意的一个重要点是，对于二分搜索，我们只考虑 dp 数组中通过在其正确位置插入一些元素（始终保持排序）进行更新的那部分。

对于Java的Arrays.binarySearch的方法，C++有类似的：std::lower_bound和std::upper_bound方法使用。

```java
public class Solution {  // 1ms 时间复杂度：O(nlog n)。二分搜索需要花费logn 的时间且调用n次。
    public int lengthOfLIS(int[] nums) {
        int[] dp = new int[nums.length];
        int len = 0;
        for (int num : nums) {
            int i = Arrays.binarySearch(dp, 0, len, num);  // 返回key值对应的索引，如果找到了则i >= 0，如果没找到则i < 0，并且-i-1表示i应该插入的位置
            if (i < 0) {
                i = -(i + 1);
            }
            dp[i] = num;  // 这里只管把num替代在它正确的位置，通俗的解释就是：每来一个数字，如果这个数字更小，就用这个数字顶掉原来构成的上升子序列某个值，如果这个数字最大，就放在序列最后
            if (i == len) {  // 如果这个num是当前最大的，则说明当前数字构成了一个更长上升子序列，len++
                len++;
            }
        }
        return len;
    }
}
```

## [695. 岛屿的最大面积](https://leetcode-cn.com/problems/max-area-of-island/)(华为题库)（只看没做）

<https://leetcode-cn.com/problems/max-area-of-island/solution/dao-yu-de-zui-da-mian-ji-jian-dan-de-di-gui-tu-jie/>

给定一个包含了一些 0 和 1的非空二维数组 grid，一个 岛屿 是由四个方向 (水平或垂直) 的 1 (代表土地) 构成的组合。你可以假设二维矩阵的四个边缘都被水包围着。
由于每个岛屿皆被水包围，所以，仅需要确保每一次寻找到新岛屿时，所测量到的岛屿面积为该岛屿的最大面积，最后返回所测所有岛屿中的最大面积即可。

由于并不知道如何才可以测量出岛屿的面积，所以为了测量整个岛屿的面积，只能采取一步步探索的方式：

当登陆某个岛屿后，以此时所处位置为行动中心，随后分别向 东、南、西、北 四个方向前进。如果向某一方向前进后其为水或登记的地方则停止探索，而当步入新地点时，则继续以当前所处位置为行动中心，随后再一次向 东、南、西、北 四个方向前进，以此类推。

```java
//
package test;

class Solution {  // 3ms
    public int maxAreaOfIsland(int[][] grid) {
        int maxArea = 0;
        int area = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    //以此为中心，向四周扩散
                    area = getArea(grid, i, j);
                    maxArea = maxArea > area ? maxArea : area;
                }
            }
        }
        return maxArea;
    }

    public int getArea(int[][] grid, int i, int j) {
        //由于坐标每次变化 1 个单位，所以判断是否等于数组长度即可
        if (i == grid.length || i < 0) {
            return 0;
        } else if (j == grid[0].length || j < 0) {
            return 0;
        }
        if (grid[i][j] == 1) {
            grid[i][j] = 0;
            return 1 + getArea(grid, i + 1, j) + getArea(grid, i - 1, j) + getArea(grid, i, j + 1) + getArea(grid, i, j - 1);
        }
        return 0;
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        int[][] grid = {
            {0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
            {0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0},
            {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0}};
        assertEquals(6, SOLUTION.maxAreaOfIsland(grid));
    }
}
```

## [148. 排序链表](https://leetcode-cn.com/problems/sort-list/)(华为题库)（只看没做）

<https://leetcode-cn.com/problems/sort-list/solution/sort-list-gui-bing-pai-xu-lian-biao-by-jyd/>

方法一：归并排序（递归法）

```java
//
package test;

class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }
}

class Solution {  // 3ms
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode fast = head.next, slow = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode tmp = slow.next;
        slow.next = null;
        ListNode left = sortList(head);
        ListNode right = sortList(tmp);
        ListNode h = new ListNode(0);
        ListNode res = h;
        while (left != null && right != null) {
            if (left.val < right.val) {
                h.next = left;
                left = left.next;
            } else {
                h.next = right;
                right = right.next;
            }
            h = h.next;
        }
        h.next = left != null ? left : right;  // 如果left或right其中一个更长，则不等于null，将h.next直接指向多出的节点
        return res.next;  // res第1个节点是个0无效节点，所以返回res.next
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        ListNode l1 = new ListNode(4);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(1);
        ListNode l4 = new ListNode(3);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;

        ListNode sortedL1 = SOLUTION.sortList(l1);
        assertEquals(1, sortedL1.val);
        assertEquals(2, sortedL1.next.val);
        assertEquals(3, sortedL1.next.next.val);
        assertEquals(4, sortedL1.next.next.next.val);
    }
    @Test
    public void test2() {
        ListNode l1 = new ListNode(-1);
        ListNode l2 = new ListNode(5);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        ListNode l5 = new ListNode(0);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l5;

        ListNode sortedL1 = SOLUTION.sortList(l1);
        assertEquals(-1, sortedL1.val);
        assertEquals(0, sortedL1.next.val);
        assertEquals(3, sortedL1.next.next.val);
        assertEquals(4, sortedL1.next.next.next.val);
        assertEquals(5, sortedL1.next.next.next.next.val);
    }
}
```

方法二：归并排序（从底至顶直接合并）

```python
class Solution:
    def sortList(self, head: ListNode) -> ListNode:
        h, length, intv = head, 0, 1
        while h: h, length = h.next, length + 1
        res = ListNode(0)
        res.next = head
        # merge the list in different intv.
        while intv < length:
            pre, h = res, res.next
            while h:
                # get the two merge head `h1`, `h2`
                h1, i = h, intv
                while i and h: h, i = h.next, i - 1
                if i: break # no need to merge because the `h2` is None.
                h2, i = h, intv
                while i and h: h, i = h.next, i - 1
                c1, c2 = intv, intv - i # the `c2`: length of `h2` can be small than the `intv`.
                # merge the `h1` and `h2`.
                while c1 and c2:
                    if h1.val < h2.val: pre.next, h1, c1 = h1, h1.next, c1 - 1
                    else: pre.next, h2, c2 = h2, h2.next, c2 - 1
                    pre = pre.next
                pre.next = h1 if c1 else h2
                while c1 > 0 or c2 > 0: pre, c1, c2 = pre.next, c1 - 1, c2 - 1
                pre.next = h 
            intv *= 2
        return res.next
```

方法三：简单粗暴的按Integer排序，再转成List

```java
class Solution {  // 23ms
    public ListNode sortList(ListNode head) {
        List<Integer> list = new ArrayList<>();
        while (head != null) {
            list.add(head.val);
            head = head.next;
        }
        Collections.sort(list);
        return createLinkedList(list);
    }

    // list递归转链表
    private ListNode createLinkedList(List<Integer> data) {
        if (data.isEmpty()) {    //如果为空返回null
            return null;
        }
        ListNode firstNode = new ListNode(data.get(0));    //每次取第一个元素
        firstNode.next = createLinkedList(data.subList(1, data.size()));//第二个元素从下标为1开始取余下list
        return firstNode;
    }
}
```

## [46. 全排列](https://leetcode-cn.com/problems/permutations/)(华为题库)

递归全排列：

```java
class Solution {  // 2ms
    public List<List<Integer>> permute(int[] nums) {
        List<Integer> usedIndexs = new ArrayList<>();
        List<List<Integer>> ret = new ArrayList<>();
        List<Integer> oneCombination = new ArrayList<>();  // List<Integer> oneCombination = Arrays.stream(nums).boxed().collect(Collectors.toList());  // 使用stream会慢大约6ms
        for (int i = 0; i < nums.length; i++) {
            oneCombination.add(nums[i]);
        }
        
        loop(ret, 0, usedIndexs, nums, oneCombination);
        return ret;
    }

    private void loop(List<List<Integer>> ret, int currentIndex, List<Integer> usedIndexs, int[] nums,
                      List<Integer> oneCombination) {
        if (currentIndex == nums.length) {
            List<Integer> newCombination = new ArrayList<>(oneCombination);
            ret.add(newCombination);
            return;
        }

        for (int i = 0; i < nums.length; i++) {
            if (usedIndexs.contains(i)) {  // 如果某个位置用过nums某位数字了，则后面的位置不能使用该位数字了
                continue;
            }
            usedIndexs.add(i);
            oneCombination.set(currentIndex, nums[i]);
            loop(ret, currentIndex + 1, usedIndexs, nums, oneCombination);
            usedIndexs.remove(currentIndex);
        }
    }
}
```

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import copy
import unittest
from typing import List

'''
执行用时：
44 ms
, 在所有 Python3 提交中击败了
30.08%
的用户
内存消耗：
15 MB
, 在所有 Python3 提交中击败了
95.31%
的用户
'''
class Solution:
    def __init__(self):
        self._res = []

    def permute(self, nums: List[int]) -> List[List[int]]:
        self._res = []
        track = []
        used = [False] * len(nums)
        self._back_trace(nums, used, track)
        return self._res

    def _back_trace(self, nums, used, track):
        if len(track) == len(nums):
            self._res.append(copy.deepcopy(track))
            return

        for index, num in enumerate(nums):
            if used[index]:
                continue

            used[index] = True
            track.append(num)
            self._back_trace(nums, used, track)
            track.pop(-1)
            used[index] = False


class SolutionTest(unittest.TestCase):
    _SOLUTION = Solution()

    def test1(self):
        nums = [1, 2, 3]
        expected = [[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 1, 2], [3, 2, 1]]
        self.assertEqual(expected, self._SOLUTION.permute(nums))

    def test2(self):
        nums = [0, 1]
        expected = [[0, 1], [1, 0]]
        self.assertEqual(expected, self._SOLUTION.permute(nums))


if __name__ == '__main__':
    unittest.main()
```





## [567. 字符串的排列](https://leetcode-cn.com/problems/permutation-in-string/)(华为可信认证2019-12-6专业级)

参考46.全排列的方式，将s1的所有组合遍历，判断是否在s2中（超时）：

```java
//
package test;

import java.util.ArrayList;
import java.util.List;

class Solution {  // 超时
    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) {
            return false;
        }

        List<Integer> usedIndexs = new ArrayList<>();
        char[] oneCombination = s1.toCharArray();
        return loop(0, usedIndexs, s1, oneCombination, s2);
    }

    private boolean loop(int currentIndex, List<Integer> usedIndexs, String str,
                         char[] oneCombination, String s2) {
        if (currentIndex == str.length()) {
            String newCombination = String.valueOf(oneCombination);
            return s2.contains(newCombination);
        }

        for (int i = 0; i < str.length(); i++) {
            if (usedIndexs.contains(i)) {
                continue;
            }
            usedIndexs.add(i);
            oneCombination[currentIndex] = str.charAt(i);
            if (loop(currentIndex + 1, usedIndexs, str, oneCombination, s2)) {
                return true;
            }
            usedIndexs.remove(currentIndex);
        }
        return false;
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        assertEquals(true, SOLUTION.checkInclusion("ab", "eidbaooo"));
    }
}
```

<https://leetcode-cn.com/problems/permutation-in-string/solution/zi-fu-chuan-de-pai-lie-by-leetcode/>

方法一：暴力 [超过时间限制]

```java
public class Solution {
    boolean flag = false;
    public boolean checkInclusion(String s1, String s2) {
        permute(s1, s2, 0);
        return flag;
    }
    public String swap(String s, int i0, int i1) {
        if (i0 == i1)
            return s;
        String s1 = s.substring(0, i0);
        String s2 = s.substring(i0 + 1, i1);
        String s3 = s.substring(i1 + 1);
        return s1 + s.charAt(i1) + s2 + s.charAt(i0) + s3;
    }
    void permute(String s1, String s2, int l) {
        if (l == s1.length()) {
            if (s2.indexOf(s1) >= 0)
                flag = true;
        } else {
            for (int i = l; i < s1.length(); i++) {
                s1 = swap(s1, l, i);
                permute(s1, s2, l + 1);
                s1 = swap(s1, l, i);
            }
        }
    }
}
```

方法二 排序

对s1排序，依次对每个同样长度的s2排序，判断是否相同：

```java
public class Solution {  // 809ms

    public boolean checkInclusion(String s1, String s2) {
        s1 = sort(s1);
        for (int i = 0; i <= s2.length() - s1.length(); i++) {
            if (s1.equals(sort(s2.substring(i, i + s1.length())))) {
                return true;
            }
        }
        return false;
    }

    public String sort(String s) {
        char[] t = s.toCharArray();
        Arrays.sort(t);
        return new String(t);
    }
}
```

方法三 使用哈希表（原解答没有给代码，自己写的）

每次针对s1长度相同的s2字符串，用Hash表统计每个字符的出现的个数，然后依次判断是否与字符串s1的Hash表是否相等。

其中s2每次移一位字符时，并不是完全新建Hash表，而是在Hash表中将原字符个数减1，再将新字符个数加1，以减少运算量。

```java
public class Solution {  // 59ms
    public boolean checkInclusion(String s1, String s2) {
        if (s2.length() < s1.length()) {
            return false;
        }

        Map<Character, Integer> s1CharCountMap = getCharCountMap(s1);
        Map<Character, Integer> tmpCharCountMap = getCharCountMap(s2.substring(0, s1.length()));

        if (compare(s1CharCountMap, tmpCharCountMap)) {
            return true;
        }

        for (int i = 1; i <= s2.length() - s1.length(); i++) {
            tmpCharCountMap.merge(s2.charAt(i - 1), 1, (oldValue, newValue) -> (oldValue - newValue));
            tmpCharCountMap.merge(s2.charAt(i + s1.length() - 1), 1, (oldValue, newValue) -> (oldValue + newValue));
            if (compare(s1CharCountMap, tmpCharCountMap)) {
                return true;
            }
        }
        return false;
    }

    private boolean compare(Map<Character, Integer> s1CharCountMap, Map<Character, Integer> tmpCharCountMap) {
        for (Map.Entry<Character, Integer> charCountEntry : s1CharCountMap.entrySet()) {
            if (!charCountEntry.getValue().equals(tmpCharCountMap.getOrDefault(charCountEntry.getKey(), 0))) {  // 这里的Integer比较需要使用equals方法，如果使用==，则内存地址不一定相等
                return false;
            }
        }
        return true;
    }

    private Map<Character, Integer> getCharCountMap(String str) {
        Map<Character, Integer> charCountMap = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            charCountMap.put(str.charAt(i), charCountMap.getOrDefault(str.charAt(i), 0) + 1);
        }
        return charCountMap;
    }
}
```

关于Integer的小值缓存<https://www.cnblogs.com/qlqwjy/p/8759152.html>：

总结:Integer i = value;如果i是在-128到127之间，不会去堆中创建对象，而是直接返回IntegerCache中的值;如果值不在上面范围内则会从堆中创建对象。= 走的是valueOf()方法，valueOf(int)会走缓存。

Integer i2 = new Integer(xxxx);不管参数的value是多少都会从堆中创建对象，与IntegerCache没关系。

```java
    @Test
    public void test3() {
        Integer i1 = 1;
        Integer i2 = 1;
        assertTrue(i1 == i2);

        Integer i3 = 141;
        Integer i4 = 141;
        assertFalse(i3 == i4);
    }
```

方法四 使用数组 [通过]

我们可以使用更简单的数组数据结构来存储频率，而不是仅使用特殊的哈希表数据结构来存储字符出现的频率。给定字符串仅包含小写字母（'a'到'z'）。因此我们需要采用大小为 26 的数组。其余过程与最后一种方法保持一致。

```java
public class Solution {  // 54ms
    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) {
            return false;
        }
        int[] s1map = new int[26];
        for (int i = 0; i < s1.length(); i++) {
            s1map[s1.charAt(i) - 'a']++;
        }
        for (int i = 0; i <= s2.length() - s1.length(); i++) {
            int[] s2map = new int[26];
            for (int j = 0; j < s1.length(); j++) {
                s2map[s2.charAt(i + j) - 'a']++;
            }
            if (matches(s1map, s2map)) {
                return true;
            }
        }
        return false;
    }

    public boolean matches(int[] s1map, int[] s2map) {
        for (int i = 0; i < 26; i++) {
            if (s1map[i] != s2map[i]) {
                return false;
            }
        }
        return true;
    }
}
```

方法五 滑动窗口 [通过]:

我们可以为 s2中的第一个窗口创建一次哈希表，而不是为 s2 中考虑的每个窗口重新生成哈希表。然后，稍后当我们滑动窗口时，我们知道我们添加了一个前面的字符并将新的后续字符添加到所考虑的新窗口中。因此，我们可以通过仅更新与这两个字符相关联的索引来更新哈希表。同样，对于每个更新的哈希表，我们将哈希表的所有元素进行比较以获得所需的结果。

```java
public class Solution {  // 9ms
    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) {
            return false;
        }
        int[] s1map = new int[26];
        int[] s2map = new int[26];
        for (int i = 0; i < s1.length(); i++) {
            s1map[s1.charAt(i) - 'a']++;
            s2map[s2.charAt(i) - 'a']++;
        }
        for (int i = 0; i < s2.length() - s1.length(); i++) {
            if (matches(s1map, s2map)) {
                return true;
            }
            s2map[s2.charAt(i + s1.length()) - 'a']++;
            s2map[s2.charAt(i) - 'a']--;
        }
        return matches(s1map, s2map);
    }

    public boolean matches(int[] s1map, int[] s2map) {
        for (int i = 0; i < 26; i++) {
            if (s1map[i] != s2map[i]) {
                return false;
            }
        }
        return true;
    }
}
```

方法六 优化的滑动窗口 [通过]:

上一种方法可以优化，如果不是比较每个更新的 s2map 的哈希表的所有元素，而是对应于 s2 考虑的每个窗口，我们会跟踪先前哈希表中已经匹配的元素数量当我们向右移动窗口时，只更新匹配元素的数量。

为此，我们维护一个 count 变量，该变量存储字符数（26个字母表中的数字），这些字符在 s1中具有相同的出现频率，当前窗口在 s2中。当我们滑动窗口时，如果扣除最后一个元素并添加新元素导致任何字符的新频率匹配，我们将 count递增1。如果不是，我们保持 count 完整。但是，如果添加频率相同的字符（添加和删除之前）相同的字符，现在会导致频率不匹配，这会通过递减相同的 count变量来考虑。如果在移动窗口后，count 的计算结果为26，则表示所有字符的频率完全匹配。所以，我们立即返回一个True。

```java
package test;

public class Solution {
    public boolean checkInclusion(String s1, String s2) {
        if (s1.length() > s2.length()) {
            return false;
        }
        int[] s1map = new int[26];
        int[] s2map = new int[26];
        for (int i = 0; i < s1.length(); i++) {
            s1map[s1.charAt(i) - 'a']++;
            s2map[s2.charAt(i) - 'a']++;
        }
        int count = 0;
        for (int i = 0; i < 26; i++) {
            if (s1map[i] == s2map[i]) {
                count++;
            }
        }
        for (int i = 0; i < s2.length() - s1.length(); i++) {
            if (count == 26) {
                return true;
            }

            int r = s2.charAt(i + s1.length()) - 'a', l = s2.charAt(i) - 'a';

            s2map[r]++;  // r是右侧新增的一个字符，每次右移一位后，右侧字符计数加1
            if (s2map[r] == s1map[r]) {  // 如果增加计数后和s1计数相同，则count+1
                count++;
            } else if (s2map[r] == s1map[r] + 1) {  // 如果增加计数后比s1计数大1，说明原来相等，现在不等了，所以要count-1
                count--;
            }

            s2map[l]--;  // l是左侧要删除的一个字符，每次右移一位后，左侧字符计数减1
            if (s2map[l] == s1map[l]) {  // 如果减少计数后和s1计数相同，则count+1
                count++;
            } else if (s2map[l] == s1map[l] - 1) {  // 如果减少计数后比s1计数小1，说明原来相等，现在不等了，所以要count-1
                count--;
            }
        }
        return count == 26;
    }
}
```

## [315. 计算右侧小于当前元素的个数](https://leetcode-cn.com/problems/count-of-smaller-numbers-after-self/)(华为可信认证2019-12-6专业级)

```java
//
package test;

import java.util.ArrayList;
import java.util.List;

class Solution {  // 超时
    public List<Integer> countSmaller(int[] nums) {
        List<Integer> ret = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            int smaller = 0;
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[j] < nums[i]) {
                    ++smaller;
                }
            }
            ret.add(smaller);
        }
        return ret;
    }
}

//
package test;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        int[] nums = {5, 2, 6, 1};
        assertEquals(Arrays.asList(2, 1, 1, 0), SOLUTION.countSmaller(nums));
    }
}
```

参考别人算法写了个二分搜索的，逆序构造数组，并依次查找该数字应该所在的位置：（但是过了14个用例，第15个没过，还没排查出问题）

```java
class Solution {
    public List<Integer> countSmaller(int[] nums) {
        List<Integer> ret = new ArrayList<>();
        List<Integer> reversedNums = new ArrayList<>();
        for (int i = nums.length - 1; i >= 0; --i) {
            int dstIndex = Collections.binarySearch(reversedNums, nums[i]);
            if (dstIndex >= 0) {
                ret.add(dstIndex);
                reversedNums.add(dstIndex, nums[i]);
                // System.out.println(String.format("num: %d, dstIndex: %d", nums[i], dstIndex));
            } else {
                int tmpIndex = -dstIndex - 1;
                ret.add(tmpIndex);
                reversedNums.add(tmpIndex, nums[i]);
                // System.out.println(String.format("num: %d, dstIndex: %d, tmpIndex: %d", nums[i], dstIndex, tmpIndex));
            }
        }
        Collections.reverse(ret);
        return ret;
    }
}
```

别人的优化代码<https://leetcode-cn.com/problems/count-of-smaller-numbers-after-self/solution/gui-bing-pai-xu-suo-yin-shu-zu-python-dai-ma-java-/>：

归并排序 + 索引数组

总结：

1、我们借助计算 “逆序数” 的思路完成本题，关键在于这里我们只能在 “前有序数组” 出列的时候计算逆序数；
如果题目让我们计算 “nums[i] 左侧小于 nums[i] 的元素的数量” 可以在 “后有序数组” 出列的时候计算逆序数；

2、体会 “索引数组” 这个使用技巧。

```java
class Solution {  // 9ms
    private int[] index;
    private int[] helper;
    private int[] count;

    public List<Integer> countSmaller(int[] nums) {
        index = new int[nums.length];
        helper = new int[nums.length];
        count = new int[nums.length];
        for (int i = 0; i < index.length; i++) {
            index[i] = i;
        }

        merge(nums, 0, nums.length - 1);

        List<Integer> res = new ArrayList<>(nums.length);
        for (int i = 0; i < count.length; i++) {
            res.add(i, count[i]);
        }
        return res;
    }

    private void merge(int[] nums, int start, int end) {
        if (start == end || start > end) {
            return;
        }
        int mid = (start + end) >> 1;

        if (start < mid) {
            merge(nums, start, mid);
        }

        if (mid + 1 < end) {
            merge(nums, mid + 1, end);
        }

        int i = start, j = mid + 1;
        int hi = start;
        while (i <= mid && j <= end) {  // 左右都还有数字时
            if (nums[index[i]] <= nums[index[j]]) {  // 按从大到小来排序数组，对应的就是从大到小的方式来变更index。右侧数字大，则右侧出
                // 右侧出
                helper[hi++] = index[j++];
            } else {
                // 左侧出 计数
                count[index[i]] += end - j + 1;
                helper[hi++] = index[i++];
            }
        }

        while (i <= mid) {  // 左侧还有数字，右侧已经出完
            //左侧出
            helper[hi++] = index[i++];
        }

        while (j <= end) {  // 左侧已经出完，右侧还有数字
            // 右侧出
            helper[hi++] = index[j++];
        }

        for (int k = start; k <= end; k++) {
            index[k] = helper[k];
        }
    }
}
```

另几个算法：

<https://leetcode-cn.com/problems/count-of-smaller-numbers-after-self/solution/ji-suan-you-ce-xiao-yu-dang-qian-yuan-su-de-ge-shu/>

2. 模拟法+查找

我们从暴力模拟法为起点进一步优化，我们看到每次我们都要从末尾遍历相同的元素，实际上我们可以建立一个保持排序的数组sorted_num。
这个数组代表：在nums[i]之后所有的数，并且已经排好序。
每次在nums数组出现新的需要判断的数就要插入到这个sorted_num，然后在这个数通过二分查找到下界(可以用STL自带的lower_bound()) 减去sorted_num.begin()就是比nums[i]小的元素个数了。

```c++
class Solution {
public:
vector<int> countSmaller(vector<int>& nums) {
    vector<int> sorted_num;
    /*建立一个保持排序的数组*/
    vector<int> res;
    /*用于保存结果的数组*/
    for(int i=nums.size()-1;i>=0;i--){
        auto iter = lower_bound(sorted_num.begin(),sorted_num.end(),nums[i]);
        /*通过lower_bound()二分寻找下界，返回一个迭代器(也就是相对于sorted_num的index)*/
        int pos = iter-sorted_num.begin();
        /*
        通过排序数组的二分查找性质，我们可以知道iter-sorted_num.begin()(可以理解成sorted_num数组的起始位置)就是
        题目需要的比nums[i]小的数字个数    
        */
        sorted_num.insert(iter,nums[i]);
        /*这时nums[i]已经使用完了，需要给以后的数字拿来判断
        插入后要保持sorted_num排序，所以nums[i]插入到iter位置*/
        res.push_back(pos);
    }
    reverse(res.begin(),res.end());
    /*一路上都是倒序插入的，所以在最后要逆转数组*/
    return res;
}
};
```

未使用的STL的lower_bound()的模拟法加查找代码，因为减少了函数调用，效率会高很多

```c++
class Solution {
 public:
     vector<int> countSmaller(vector<int>& nums) {
         vector<int>t,res(nums.size());
　　　　　　/*初始化t,res*/
         for(int i=nums.size()-1;i>=0;i--){
             int left=0,right=t.size();
　　　　　　　　/*下面是一个在t数组里二分查找的过程*/
             while (left<right){
                 int mid=left+(right-left)/2;
                 if(t[mid]>=nums[i]){
                     right= mid;
                 }
                 else{
                     left=mid+1;
                 }
             }
             res[i]=right;
             t.insert(t.begin()+right,nums[i]);
         }
         return res;
     }
 };
```

3. 记忆化+排序（没看懂）

还有个很第二种方法比较类似的方法，就是用STL的pari记录:没有排序前每个num[i]对应的下标i,pair<int,int>:nums[i]->i。

记录完之后进行排序。 然后利用已排序的性质进行查找，代码有些复杂且用到了一些位操作的知识，比较难想到，但也是一种非常好的方法。

```java
class Solution {
    private static final int MAXN = 100007;

    private int[] cnt = new int[MAXN];

    private int n = 0;

    public List<Integer> countSmaller(int[] nums) {
        n = nums.length;
        List<Integer> ans = Arrays.asList(new Integer[nums.length]);
        List<int[]> A = new ArrayList<>();
        /*建立一个从数组内容到未排序前索引的映射A*/
        for (int i = 0; i < n; i++) {
            A.add(new int[]{nums[i], i + 1});
        }
        Collections.sort(A, Comparator.comparingInt(e -> e[0]));
        /*进行排序*/
        for (int i = 0; i < n; i++) {
            int id = A.get(i)[1];
            int t = sum(n) - sum(id);
            ans.set(id - 1, t);
            add(id);
        }
        return ans;
    }

    private void add(int k) {
        for (; k <= n; k += -k & k) {
            cnt[k] += 1;
        }
    }

    private int sum(int k) {
        int res = 0;
        for (; k != 0; k -= -k & k) {
            res += cnt[k];
        }
        return res;
    }
}
```

还有几个解法没看。



别人的Python的使用二分查找方法<https://leetcode.com/problems/count-of-smaller-numbers-after-self/discuss/439152/python-Very-simple-binary-search-solution-beats-99>：

```python
import bisect
class Solution:
    def countSmaller(self, nums: List[int]) -> List[int]:
        arr = []
        answer = [0 for i in range(len(nums))]
        
        for i in range(len(nums)-1,-1,-1):
            index = bisect.bisect_left(arr,nums[i])
            arr.insert(index,nums[i])
            answer[i] = index
        
        return answer
```



## [631. 设计 Excel 求和公式](https://leetcode-cn.com/problems/design-excel-sum-formula/)(华为可信认证2019-12-6专业级)

<https://leetcode-cn.com/problems/design-excel-sum-formula/solution/she-ji-excel-qiu-he-gong-shi-by-leetcode/>

```java
//
package test;

import java.util.HashMap;
import java.util.Stack;

public class Excel {
    private Formula[][] formulas;

    private class Formula {
        Formula(HashMap<String, Integer> cells, int v) {
            relevantCellsCountMap = cells;
            val = v;
        }

        HashMap<String, Integer> relevantCellsCountMap;
        int val;
    }

    public Excel(int H, char W) {
        formulas = new Formula[H][(W - 'A') + 1];
    }

    public int get(int r, char c) {
        return formulas[r - 1][c - 'A'] == null ? 0 : formulas[r - 1][c - 'A'].val;
    }

    public void set(int r, char c, int v) {
        // set时，是直接给定当前单元格的值，因此可以直接设置formula
        formulas[r - 1][c - 'A'] = new Formula(new HashMap<>(), v);
        // 同时把依赖当前单元格的全部其它单元格值重新计算一遍
        resetDependentCellsVal(r - 1, c - 'A');
    }

    public int sum(int r, char c, String[] strs) {
        HashMap<String, Integer> cells = convert(strs);
        int sum = calculateSum(cells);
        formulas[r - 1][c - 'A'] = new Formula(cells, sum);
        resetDependentCellsVal(r - 1, c - 'A');
        return sum;
    }

    private void resetDependentCellsVal(int r, int c) {
        Stack<int[]> stack = new Stack<>();
        topologicalSort(r, c, stack);  // 单元格值有变化，则需要触发所有依赖当前单元格值的拓扑排序并重新求值
        executeStack(stack);
    }

    private void topologicalSort(int r, int c, Stack<int[]> stack) {
        String currentCell = "" + (char) ('A' + c) + (r + 1);
        for (int i = 0; i < formulas.length; i++) {
            for (int j = 0; j < formulas[0].length; j++) {
                if (formulas[i][j] != null && formulas[i][j].relevantCellsCountMap.containsKey(currentCell)) {
                    // 如果某个其它单元络依赖当前单元格，则将依赖的单元格再递归调用，以确保依赖的单元格先入栈，被依赖的单元格后入栈，从而计算值的时候，被依赖的单元格在栈顶先计算
                    topologicalSort(i, j, stack);
                }
            }
        }
        stack.push(new int[]{r, c});
    }

    private void executeStack(Stack<int[]> stack) {
        // 从栈顶依次计算每个单元格的值，从而保证有依赖关系的单元格可以全部按序计算完成
        while (!stack.isEmpty()) {
            int[] top = stack.pop();
            int r = top[0], c = top[1];
            if (formulas[r][c].relevantCellsCountMap.size() > 0) {
                formulas[r][c].val = calculateSum(formulas[r][c].relevantCellsCountMap);
            }
        }
    }

    private HashMap<String, Integer> convert(String[] strs) {
        HashMap<String, Integer> relevantCellsCountMap = new HashMap<>();
        for (String str : strs) {
            if (str.indexOf(":") < 0) {
                relevantCellsCountMap.put(str, relevantCellsCountMap.getOrDefault(str, 0) + 1);
            } else {
                String[] cells = str.split(":");
                int startCellRow = Integer.parseInt(cells[0].substring(1)), endCellRow = Integer.parseInt(cells[1].substring(1));
                char startCellColumn = cells[0].charAt(0), endCellColumn = cells[1].charAt(0);
                for (int row = startCellRow; row <= endCellRow; row++) {
                    for (char column = startCellColumn; column <= endCellColumn; column++) {
                        relevantCellsCountMap.put("" + column + row, relevantCellsCountMap.getOrDefault("" + column + row, 0) + 1);
                    }
                }
            }
        }
        return relevantCellsCountMap;
    }

    private int calculateSum(HashMap<String, Integer> cells) {
        int sum = 0;
        for (String s : cells.keySet()) {
            int x = Integer.parseInt(s.substring(1)) - 1, y = s.charAt(0) - 'A';
            sum += (formulas[x][y] != null ? formulas[x][y].val : 0) * cells.get(s);
        }
        return sum;
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExcelTest {
    @Test
    public void test1() {
        Excel excel = new Excel(3, 'C');
        excel.set(1, 'A', 2);
        assertEquals(4, excel.sum(3, 'C', new String[]{"A1", "A1:B2"}));
        excel.set(2, 'B', 3);
        assertEquals(7, excel.get(3, 'C'));
    }
}
```

别人提交的C++代码：

```c++
class Excel {
public:
    Excel(int H, char W) {
        grid = vector<vector<int>>(H+1, vector<int>(W-'A'+1, 0));  // 保存直接的值
        number = vector<vector<vector<string>>>(H+1, vector<vector<string>>(W-'A'+1));  // 保存公式
    }
    
    void set(int r, char c, int v) {
        grid[r][c-'A'] = v;
        number[r][c-'A'].clear();
    }
    
    int get(int r, char c) {
        if(number[r][c-'A'].empty()) return grid[r][c-'A'];  // 如果没有公式，则直接取值
        return calculate(number[r][c-'A']);  // 如果有公式，则计算全部依赖的单元格值
    }

    int calculate(vector<string>& strs) {
        int res = 0;
        for(string str: strs) {
            res += calculate(str);
        }
        return res;
    }
    int calculate(string& s) {
        if(s.size()==2) {
            return get(s[1]-'0', s[0]);
        }
        // 解析起始点和终点
        char cl, cr;
        int rl=0, rr=0, res=0, n=s.size(), i=0;
        cl = s[i++];
        while(s[i]!=':') rl = rl*10 + s[i++]-'0';
        i++;
        cr = s[i++];
        while(i<n) rr = rr*10 + s[i++]-'0';
        for(char c=cl; c<=cr;c++) {
            for(int x=rl; x<=rr; x++) {
                res += get(x, c);
            }
        }
        // cout<<s<<res<<endl;
        return res;
    }
    
    // 获取时才计算每个单元格的值
    int sum(int r, char c, vector<string> strs) {
        number[r][c-'A'] = move(strs);
        return get(r, c);
    }
private:
    vector<vector<int>> grid;
    vector<vector<vector<string>>> number;
};
```

## [48. 旋转图像](https://leetcode-cn.com/problems/rotate-image/)(华为题库)（只看没做）

别人的代码：

<https://leetcode-cn.com/problems/rotate-image/solution/xuan-zhuan-tu-xiang-by-leetcode/>

方法 1 ：转置加翻转

最直接的想法是先转置矩阵，然后翻转每一行。这个简单的方法已经能达到最优的时间复杂度O(N^2)*O*(*N*2)。

```java
package test;

class Solution {  // 0ms
    public void rotate(int[][] matrix) {
        int n = matrix.length;

        // transpose matrix
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int tmp = matrix[j][i];
                matrix[j][i] = matrix[i][j];
                matrix[i][j] = tmp;
            }
        }
        // reverse each row
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n / 2; j++) {
                int tmp = matrix[i][j];
                matrix[i][j] = matrix[i][n - j - 1];
                matrix[i][n - j - 1] = tmp;
            }
        }
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        int[][] expected = {
            {7, 4, 1},
            {8, 5, 2},
            {9, 6, 3}
        };
        SOLUTION.rotate(matrix);
        assertArrayEquals(expected, matrix);
    }

    @Test
    public void test2() {
        int[][] matrix = {
            {5, 1, 9, 11},
            {2, 4, 8, 10},
            {13, 3, 6, 7},
            {15, 14, 12, 16}
        };
        int[][] expected = {
            {15, 13, 2, 5},
            {14, 3, 4, 1},
            {12, 6, 8, 9},
            {16, 7, 10, 11}
        };
        SOLUTION.rotate(matrix);
        assertArrayEquals(expected, matrix);
    }
}
```

方法 2 ：旋转四个矩形

方法 1 使用了两次矩阵操作，但是有只使用一次操作的方法完成旋转。

现在的解法很直接 - 可以在第一个矩形中移动元素并且在 长度为 `4` 个元素的临时列表中移动它们。

```java
class Solution {
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n / 2 + n % 2; i++) {
            for (int j = 0; j < n / 2; j++) {
                int[] tmp = new int[4];
                int row = i;
                int col = j;
                for (int k = 0; k < 4; k++) {
                    tmp[k] = matrix[row][col];
                    int x = row;
                    row = col;
                    col = n - 1 - x;
                }
                for (int k = 0; k < 4; k++) {
                    matrix[row][col] = tmp[(k + 3) % 4];
                    int x = row;
                    row = col;
                    col = n - 1 - x;
                }
            }
        }
    }
}
```



```java
class Solution {
  public void rotate(int[][] matrix) {
    int n = matrix.length;
    for (int i = 0; i < (n + 1) / 2; i ++) {
      for (int j = 0; j < n / 2; j++) {
        int temp = matrix[n - 1 - j][i];
        matrix[n - 1 - j][i] = matrix[n - 1 - i][n - j - 1];
        matrix[n - 1 - i][n - j - 1] = matrix[j][n - 1 -i];
        matrix[j][n - 1 - i] = matrix[i][j];
        matrix[i][j] = temp;
      }
    }
  }
}
```

方法 3：在单次循环中旋转 4 个矩形

该想法和方法 2 相同，但是所有的操作可以在单次循环内完成并且这是更精简的方法。

```java
class Solution {
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < (n + 1) / 2; i++) {
            for (int j = 0; j < n / 2; j++) {
                int temp = matrix[n - 1 - j][i];
                matrix[n - 1 - j][i] = matrix[n - 1 - i][n - j - 1];
                matrix[n - 1 - i][n - j - 1] = matrix[j][n - 1 - i];
                matrix[j][n - 1 - i] = matrix[i][j];
                matrix[i][j] = temp;
            }
        }
    }
}
```

## [547. 朋友圈](https://leetcode-cn.com/problems/friend-circles/)(华为题库)（只看没做）

自己写的，有问题。

```java
package test;

class Solution {
    public int findCircleNum(int[][] M) {
        int ret = 0;
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M.length; j++) {
                if (M[i][j] == 1) {
                    ++ret;
                    M[i][j] = 0;
                    setZeroDfs(M, i, j);
                }
            }
        }
        return ret;
    }

    private void setZeroDfs(int[][] m, int i, int j) {
        if (i + 1 < m.length && m[i + 1][j] == 1) {
            m[i + 1][j] = 0;
            setZeroDfs(m, i + 1, j);
        }
        if (i - 1 >= 0 && m[i - 1][j] == 1) {
            m[i - 1][j] = 0;
            setZeroDfs(m, i - 1, j);
        }
        if (j + 1 < m.length && m[i][j + 1] == 1) {
            m[i][j + 1] = 0;
            setZeroDfs(m, i, j + 1);
        }
        if (j - 1 >= 0 && m[i][j - 1] == 1) {
            m[i][j - 1] = 0;
            setZeroDfs(m, i, j - 1);
        }
    }
}

```

别人的代码：

<https://leetcode-cn.com/problems/friend-circles/solution/peng-you-quan-by-leetcode/>

方法 1：深度优先搜索

首先以i为起始节点查找其全部朋友，只要找到一个新的朋友节点j，则以j为起始，查找j的全部朋友节点，依次遍历这样就能够把一个朋友圈全部查找完成。

```java
package test;

public class Solution {  // 0ms
    public int findCircleNum(int[][] M) {
        int[] visited = new int[M.length];
        int count = 0;
        for (int i = 0; i < M.length; i++) {
            if (visited[i] == 0) {
                dfs(M, visited, i);
                count++;
            }
        }
        return count;
    }

    private void dfs(int[][] M, int[] visited, int i) {
        for (int j = 0; j < M.length; j++) {
            if (M[i][j] == 1 && visited[j] == 0) {
                visited[j] = 1;
                dfs(M, visited, j);
            }
        }
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        int[][] M = {
            {1, 1, 0},
            {1, 1, 0},
            {0, 0, 1},
        };
        assertEquals(2, SOLUTION.findCircleNum(M));
    }

    @Test
    public void test2() {
        int[][] M = {
            {1, 1, 0},
            {1, 1, 1},
            {0, 1, 1},
        };
        assertEquals(1, SOLUTION.findCircleNum(M));
    }

    @Test
    public void test3() {
        int[][] M = {
            {1, 0, 0, 1},
            {0, 1, 1, 0},
            {0, 1, 1, 1},
            {1, 0, 1, 1}
        };
        assertEquals(1, SOLUTION.findCircleNum(M));
    }
}
```

方法 2：广度优先搜索

```java
public class Solution {  // 11ms
    public int findCircleNum(int[][] M) {
        int[] visited = new int[M.length];
        int count = 0;
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < M.length; i++) {
            if (visited[i] == 0) {
                queue.add(i);
                while (!queue.isEmpty()) {
                    int s = queue.remove();
                    visited[s] = 1;
                    for (int j = 0; j < M.length; j++) {
                        if (M[s][j] == 1 && visited[j] == 0) {
                            queue.add(j);
                        }
                    }
                }
                count++;
            }
        }
        return count;
    }
}
```

方法 3：并查集

```java
public class Solution {  // 12ms
    public int findCircleNum(int[][] M) {
        int[] parent = new int[M.length];
        Arrays.fill(parent, -1);
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M.length; j++) {
                if (M[i][j] == 1 && i != j) {
                    union(parent, i, j);
                }
            }
        }
        int count = 0;
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == -1) {
                count++;
            }
        }
        return count;
    }

    private void union(int parent[], int x, int y) {
        int xset = find(parent, x);
        int yset = find(parent, y);
        if (xset != yset) {
            parent[xset] = yset;
        }
    }

    private int find(int parent[], int i) {
        if (parent[i] == -1) {
            return i;
        }
        return find(parent, parent[i]);
    }
}
```

## [17. 电话号码的字母组合](https://leetcode-cn.com/problems/letter-combinations-of-a-phone-number/)(华为题库)

递归遍历：

```java
//
package test;

import java.util.ArrayList;
import java.util.List;

class Solution {  // 0ms

    private static final String[] STRINGS = {"abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    public List<String> letterCombinations(String digits) {
        List<String> ret = new ArrayList<>();
        if (digits.isEmpty()) {
            return ret;
        }

        char[] chars = digits.toCharArray();
        loop(digits, 0, chars, ret);
        return ret;
    }

    private void loop(String digits, int index, char[] chars, List<String> ret) {
        if (index == digits.length()) {
            String oneCombination = new String(chars);
            ret.add(oneCombination);
            return;
        }

        String digit2String = STRINGS[digits.charAt(index) - '2'];
        for (int i = 0; i < digit2String.length(); i++) {
            chars[index] = digit2String.charAt(i);
            loop(digits, index + 1, chars, ret);
        }
    }
}

//
package test;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        String digits = "23";
        List<String> expected = Arrays.asList("ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf");
        assertEquals(expected, SOLUTION.letterCombinations(digits));
    }

    @Test
    public void test2() {
        String digits = "2";
        List<String> expected = Arrays.asList("a", "b", "c");
        assertEquals(expected, SOLUTION.letterCombinations(digits));
    }

    @Test
    public void test3() {
        String digits = "";
        List<String> expected = Arrays.asList();
        assertEquals(expected, SOLUTION.letterCombinations(digits));
    }
}
```

## [130. 被围绕的区域](https://leetcode-cn.com/problems/surrounded-regions/)(华为题库)

自己写的，遍历全部单元格，每找到一个O，就把与之关联的全部O找出来，同时判断这一批O是否是有效的O（不与边界连通），如果是有效的，则全部设置为X，否则就把这批O添加到用于记录全部无效O的Set中，提高后面查找效率。

深度搜索：

```java
package test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Solution {  // 34ms
    class Pos {
        Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Pos pos = (Pos) o;
            return x == pos.x &&
                y == pos.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        int x;
        int y;
    }

    private Set<Pos> oNotSurroundByXSets = new HashSet<>();
    private int maxRow;
    private int maxColumn;

    public void solve(char[][] board) {
        maxRow = board.length;
        if (maxRow == 0) {
            return;
        }
        maxColumn = board[0].length;

        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxColumn; j++) {
                if (board[i][j] == 'O' && !oNotSurroundByXSets.contains(new Pos(i, j))) {
                    // 如果当前点O有效，则把全部相连的O都取出来，并判断这些点是不是有效O点集合
                    Set<Pos> allOPosSets = new HashSet<>();
                    allOPosSets.add(new Pos(i, j));
                    boolean isCurrentOValid = isPosValidO(i, j, board);
                    boolean isAllOValid = loopAllConnectedOPos(i, j, board, allOPosSets, isCurrentOValid);
                    if (isAllOValid) {
                        setX(allOPosSets, board);
                    } else {
                        oNotSurroundByXSets.addAll(allOPosSets);
                    }
                }
            }
        }
    }

    private void setX(Set<Pos> allOPosSets, char[][] board) {
        for (Pos oPos : allOPosSets) {
            board[oPos.x][oPos.y] = 'X';
        }
    }

    private boolean loopAllConnectedOPos(int i, int j, char[][] board, Set<Pos> allOPosSets, boolean isCurrentOValid) {
        Pos pos = new Pos(i + 1, j);
        if (i + 1 < maxRow && !allOPosSets.contains(pos) &&
            board[i + 1][j] == 'O' && !oNotSurroundByXSets.contains(pos)) {
            allOPosSets.add(pos);
            isCurrentOValid = isCurrentOValid & isPosValidO(i + 1, j, board);
            isCurrentOValid = isCurrentOValid & loopAllConnectedOPos(i + 1, j, board, allOPosSets, isCurrentOValid);
        }
        pos = new Pos(i - 1, j);
        if (i - 1 >= 0 && !allOPosSets.contains(pos) &&
            board[i - 1][j] == 'O' && !oNotSurroundByXSets.contains(pos)) {
            allOPosSets.add(pos);
            isCurrentOValid = isCurrentOValid & isPosValidO(i - 1, j, board);
            isCurrentOValid = isCurrentOValid & loopAllConnectedOPos(i - 1, j, board, allOPosSets, isCurrentOValid);
        }
        pos = new Pos(i, j + 1);
        if (j + 1 < maxColumn && !allOPosSets.contains(pos) &&
            board[i][j + 1] == 'O' && !oNotSurroundByXSets.contains(pos)) {
            allOPosSets.add(pos);
            isCurrentOValid = isCurrentOValid & isPosValidO(i, j + 1, board);
            isCurrentOValid = isCurrentOValid & loopAllConnectedOPos(i, j + 1, board, allOPosSets, isCurrentOValid);
        }
        pos = new Pos(i, j - 1);
        if (j - 1 >= 0 && !allOPosSets.contains(pos) &&
            board[i][j - 1] == 'O' && !oNotSurroundByXSets.contains(pos)) {
            allOPosSets.add(pos);
            isCurrentOValid = isCurrentOValid & isPosValidO(i, j - 1, board);
            isCurrentOValid = isCurrentOValid & loopAllConnectedOPos(i, j - 1, board, allOPosSets, isCurrentOValid);
        }

        return isCurrentOValid;
    }

    private boolean isPosValidO(int i, int j, char[][] board) {
        // 当前点在边上就肯定无效了
        if (i <= 0 || i >= maxRow - 1 || j <= 0 || j >= maxColumn - 1) {
            return false;
        }
        // 当前点不在边上
        return true;
    }
}
```

广度搜索：

```java
package test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

class Solution {  // 46ms
    class Pos {
        Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Pos pos = (Pos) o;
            return x == pos.x &&
                y == pos.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        int x;
        int y;
    }

    private Set<Pos> oNotSurroundByXSets = new HashSet<>();
    private int maxRow;
    private int maxColumn;

    public void solve(char[][] board) {
        maxRow = board.length;
        if (maxRow == 0) {
            return;
        }
        maxColumn = board[0].length;

        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxColumn; j++) {
                if (board[i][j] == 'O' && !oNotSurroundByXSets.contains(new Pos(i, j))) {
                    Queue<Pos> queue = new LinkedList<>();
                    queue.add(new Pos(i, j));
                    boolean isAllOValid = isPosValidO(i, j, board);
                    Set<Pos> allOPosSets = new HashSet<>();

                    while (!queue.isEmpty()) {
                        Pos oPos = queue.remove();
                        allOPosSets.add(oPos);
                        isAllOValid = isAllOValid & addConnectedOPos(oPos.x + 1, oPos.y, board, allOPosSets, queue);
                        isAllOValid = isAllOValid & addConnectedOPos(oPos.x - 1, oPos.y, board, allOPosSets, queue);
                        isAllOValid = isAllOValid & addConnectedOPos(oPos.x, oPos.y + 1, board, allOPosSets, queue);
                        isAllOValid = isAllOValid & addConnectedOPos(oPos.x, oPos.y - 1, board, allOPosSets, queue);
                    }

                    if (isAllOValid) {
                        setX(allOPosSets, board);
                    } else {
                        oNotSurroundByXSets.addAll(allOPosSets);
                    }
                }
            }
        }
    }

    private void setX(Set<Pos> allOPosSets, char[][] board) {
        for (Pos oPos : allOPosSets) {
            board[oPos.x][oPos.y] = 'X';
        }
    }

    private boolean addConnectedOPos(int i, int j, char[][] board, Set<Pos> allOPosSets, Queue<Pos> queue) {
        Pos pos = new Pos(i, j);
        if (i >= 0 && i < maxRow && j >= 0 && j < maxColumn && !allOPosSets.contains(pos) &&
            board[i][j] == 'O' && !oNotSurroundByXSets.contains(pos)) {
            queue.add(pos);
            allOPosSets.add(pos);
            return isPosValidO(i, j, board);
        }
        return true;
    }

    private boolean isPosValidO(int i, int j, char[][] board) {
        // 当前点在边上就肯定无效了
        if (i <= 0 || i >= maxRow - 1 || j <= 0 || j >= maxColumn - 1) {
            return false;
        }
        // 当前点不在边上
        return true;
    }
}
```

别人的优化算法：

<https://leetcode-cn.com/problems/surrounded-regions/solution/bfsdi-gui-dfsfei-di-gui-dfsbing-cha-ji-by-ac_pipe/>

优化算法从边界的O开始查找，把全部与之关联的O设置为#。最后把全部O设置为X，把全部#设置为O。

dfs递归:

```java
package test;

class Solution {  // 3ms
    public void solve(char[][] board) {
        if (board == null || board.length == 0) {
            return;
        }
        int m = board.length;
        int n = board[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 从边缘o开始搜索
                boolean isEdge = i == 0 || j == 0 || i == m - 1 || j == n - 1;
                if (isEdge && board[i][j] == 'O') {
                    dfs(board, i, j);
                }
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
                if (board[i][j] == '#') {
                    board[i][j] = 'O';
                }
            }
        }
    }

    public void dfs(char[][] board, int i, int j) {
        if (i < 0 || j < 0 || i >= board.length || j >= board[0].length || board[i][j] == 'X' || board[i][j] == '#') {
            // board[i][j] == '#' 说明已经搜索过了.
            return;
        }
        board[i][j] = '#';
        dfs(board, i - 1, j); // 上
        dfs(board, i + 1, j); // 下
        dfs(board, i, j - 1); // 左
        dfs(board, i, j + 1); // 右
    }
}
```

dsf 非递归:

非递归的方式，我们需要记录每一次遍历过的位置，我们用 stack 来记录，因为它先进后出的特点。而位置我们定义一个内部类 Pos 来标记横坐标和纵坐标。注意的是，在写非递归的时候，我们每次查看 stack 顶，但是并不出 stack，直到这个位置上下左右都搜索不到的时候出 Stack。

```java
class Solution {  // 4ms
    public class Pos {
        int i;
        int j;

        Pos(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    public void solve(char[][] board) {
        if (board == null || board.length == 0) {
            return;
        }
        int m = board.length;
        int n = board[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 从边缘第一个是o的开始搜索
                boolean isEdge = i == 0 || j == 0 || i == m - 1 || j == n - 1;
                if (isEdge && board[i][j] == 'O') {
                    dfs(board, i, j);
                }
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
                if (board[i][j] == '#') {
                    board[i][j] = 'O';
                }
            }
        }
    }

    public void dfs(char[][] board, int i, int j) {
        Stack<Pos> stack = new Stack<>();
        stack.push(new Pos(i, j));
        board[i][j] = '#';
        while (!stack.isEmpty()) {
            // 取出当前stack 顶, 不弹出.
            Pos current = stack.peek();
            // 上
            if (current.i - 1 >= 0
                && board[current.i - 1][current.j] == 'O') {
                stack.push(new Pos(current.i - 1, current.j));
                board[current.i - 1][current.j] = '#';
                continue;
            }
            // 下
            if (current.i + 1 <= board.length - 1
                && board[current.i + 1][current.j] == 'O') {
                stack.push(new Pos(current.i + 1, current.j));
                board[current.i + 1][current.j] = '#';
                continue;
            }
            // 左
            if (current.j - 1 >= 0
                && board[current.i][current.j - 1] == 'O') {
                stack.push(new Pos(current.i, current.j - 1));
                board[current.i][current.j - 1] = '#';
                continue;
            }
            // 右
            if (current.j + 1 <= board[0].length - 1
                && board[current.i][current.j + 1] == 'O') {
                stack.push(new Pos(current.i, current.j + 1));
                board[current.i][current.j + 1] = '#';
                continue;
            }
            // 如果上下左右都搜索不到,本次搜索结束，弹出stack
            stack.pop();
        }
    }
}
```

bfs 非递归:

```java
class Solution {  // 3ms
    public class Pos {
        int i;
        int j;

        Pos(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    public void solve(char[][] board) {
        if (board == null || board.length == 0) {
            return;
        }
        int m = board.length;
        int n = board[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 从边缘第一个是o的开始搜索
                boolean isEdge = i == 0 || j == 0 || i == m - 1 || j == n - 1;
                if (isEdge && board[i][j] == 'O') {
                    bfs(board, i, j);
                }
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 'O') {
                    board[i][j] = 'X';
                }
                if (board[i][j] == '#') {
                    board[i][j] = 'O';
                }
            }
        }
    }

    public void bfs(char[][] board, int i, int j) {
        Queue<Pos> queue = new LinkedList<>();
        queue.add(new Pos(i, j));
        board[i][j] = '#';
        while (!queue.isEmpty()) {
            Pos current = queue.poll();
            // 上
            if (current.i - 1 >= 0
                && board[current.i - 1][current.j] == 'O') {
                queue.add(new Pos(current.i - 1, current.j));
                board[current.i - 1][current.j] = '#';
                // 没有continue.
            }
            // 下
            if (current.i + 1 <= board.length - 1
                && board[current.i + 1][current.j] == 'O') {
                queue.add(new Pos(current.i + 1, current.j));
                board[current.i + 1][current.j] = '#';
            }
            // 左
            if (current.j - 1 >= 0
                && board[current.i][current.j - 1] == 'O') {
                queue.add(new Pos(current.i, current.j - 1));
                board[current.i][current.j - 1] = '#';
            }
            // 右
            if (current.j + 1 <= board[0].length - 1
                && board[current.i][current.j + 1] == 'O') {
                queue.add(new Pos(current.i, current.j + 1));
                board[current.i][current.j + 1] = '#';
            }
        }
    }
}
```

bfs 递归:
bfs 一般我们不会去涉及，而且比较绕，之前我们唯一 A 过的用 bfs 递归的方式是层序遍历二叉树的时候可以用递归的方式。

并查集:
并查集这种数据结构好像大家不太常用，实际上很有用，我在实际的 production code 中用过并查集。并查集常用来解决连通性的问题，即将一个图中连通的部分划分出来。当我们判断图中两个点之间是否存在路径时，就可以根据判断他们是否在一个连通区域。 而这道题我们其实求解的就是和边界的 O 在一个连通区域的的问题。

并查集的思想就是，同一个连通区域内的所有点的根节点是同一个。将每个点映射成一个数字。先假设每个点的根节点就是他们自己，然后我们以此输入连通的点对，然后将其中一个点的根节点赋成另一个节点的根节点，这样这两个点所在连通区域又相互连通了。
并查集的主要操作有：

* find(int m)：这是并查集的基本操作，查找 mm 的根节点。

* isConnected(int m,int n)：判断 m，n 两个点是否在一个连通区域。

* union(int m,int n):合并 m，n 两个点所在的连通区域。

我们的思路是把所有边界上的 O 看做一个连通区域。遇到 O 就执行并查集合并操作，这样所有的 O 就会被分成两类

和边界上的 O 在一个连通区域内的。这些 O 我们保留。
不和边界上的 O 在一个连通区域内的。这些 O 就是被包围的，替换。
由于并查集我们一般用一维数组来记录，方便查找 parants，所以我们将二维坐标用 node 函数转化为一维坐标。

```java
class UnionFind {
    int[] parents;

    UnionFind(int totalNodes) {
        parents = new int[totalNodes];
        for (int i = 0; i < totalNodes; i++) {
            parents[i] = i;
        }
    }

    private int find(int node) {
        while (parents[node] != node) {
            // 当前节点的父节点 指向父节点的父节点.
            // 保证一个连通区域最终的parents只有一个.
            parents[node] = parents[parents[node]];
            node = parents[node];
        }

        return node;
    }

    public boolean isConnected(int node1, int node2) {
        return find(node1) == find(node2);
    }

    // 合并连通区域是通过find来操作的, 即看这两个节点是不是在一个连通区域内.
    public void union(int node1, int node2) {
        int root1 = find(node1);
        int root2 = find(node2);
        if (root1 != root2) {
            parents[root2] = root1;
        }
    }
}

public class Solution {  // 16ms

    private int cols;

    public void solve(char[][] board) {
        if (board == null || board.length == 0) {
            return;
        }

        int rows = board.length;
        cols = board[0].length;

        // 用一个虚拟节点, 边界上的O 的父节点都是这个虚拟节点
        UnionFind uf = new UnionFind(rows * cols + 1);
        int dummyNode = rows * cols;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'O') {
                    // 遇到O进行并查集操作合并
                    if (i == 0 || i == rows - 1 || j == 0 || j == cols - 1) {
                        // 边界上的O,把它和dummyNode 合并成一个连通区域.
                        uf.union(node(i, j), dummyNode);
                    } else {
                        // 和上下左右合并成一个连通区域.
                        if (i > 0 && board[i - 1][j] == 'O') {
                            uf.union(node(i, j), node(i - 1, j));
                        }
                        if (i < rows - 1 && board[i + 1][j] == 'O') {
                            uf.union(node(i, j), node(i + 1, j));
                        }
                        if (j > 0 && board[i][j - 1] == 'O') {
                            uf.union(node(i, j), node(i, j - 1));
                        }
                        if (j < cols - 1 && board[i][j + 1] == 'O') {
                            uf.union(node(i, j), node(i, j + 1));
                        }
                    }
                }
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (uf.isConnected(node(i, j), dummyNode)) {
                    // 和dummyNode 在一个连通区域的,那么就是O；
                    board[i][j] = 'O';
                } else {
                    board[i][j] = 'X';
                }
            }
        }
    }

    int node(int i, int j) {
        return i * cols + j;
    }
}
```

c++广度搜索

```cpp
#include <unordered_map>
#include <algorithm>
#include <queue>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    void solve(vector<vector<char>>& board) {
        int m = board.size();
        int n = board.at(0).size();
        fillAllEdgeO(board, m, n);

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (board.at(i).at(j) == 'A') {
                    board.at(i).at(j) = 'O';
                } else if (board.at(i).at(j) == 'O') {
                    board.at(i).at(j) = 'X';
                }
            }
        }
    }

private:
    void fillAllEdgeO(vector<vector<char>>& board, int m, int n) {
        std::queue<std::pair<int, int>> pairQueue;
        for (int i = 0; i < m; ++i) {
            if (board.at(i).at(0) == 'O') {
                pairQueue.emplace(i, 0);
                board.at(i).at(0) = 'A';
            }
            if (board.at(i).at(n - 1) == 'O') {
                pairQueue.emplace(i, n - 1);
                board.at(i).at(n - 1) = 'A';
            }
        }

        for (int j = 1; j < n - 1; ++j) {
            if (board.at(0).at(j) == 'O') {
                pairQueue.emplace(0, j);
                board.at(0).at(j) = 'A';
            }
            if (board.at(m - 1).at(j) == 'O') {
                pairQueue.emplace(m - 1, j);
                board.at(m - 1).at(j) = 'A';
            }
        }

        std::vector<std::pair<int, int>> directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        while (!pairQueue.empty()) {
            int x = pairQueue.front().first;
            int y = pairQueue.front().second;
            pairQueue.pop();
            for (const auto& direction: directions) {
                int newX = x + direction.first;
                int newY = y + direction.second;
                if (newX < 0 || newX >= m || newY < 0 || newY >= n || board.at(newX).at(newY) != 'O') {
                    continue;
                }
                pairQueue.emplace(newX, newY);
                board.at(newX).at(newY) = 'A';
            }
        }
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    vector<vector<char>> board = {{'X', 'O', 'X', 'O', 'X', 'O'},
                                  {'O', 'X', 'O', 'X', 'O', 'X'},
                                  {'X', 'O', 'X', 'O', 'X', 'O'},
                                  {'O', 'X', 'O', 'X', 'O', 'X'}};
    solution.solve(board);
//    EXPECT_EQ(true, );
}

TEST_F(SolutionTest, Test2) {
    vector<vector<char>> board = {
        {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
        {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'O', 'O', 'O', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
        {'X', 'X', 'X', 'X', 'X', 'O', 'O', 'O', 'X', 'O', 'X', 'O', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
        {'X', 'X', 'X', 'X', 'X', 'O', 'X', 'O', 'X', 'O', 'X', 'O', 'O', 'O', 'X', 'X', 'X', 'X', 'X', 'X'},
        {'X', 'X', 'X', 'X', 'X', 'O', 'X', 'O', 'O', 'O', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
        {'X', 'X', 'X', 'X', 'X', 'O', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}};
    solution.solve(board);
    vector<vector<char>> expected = {
        {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
        {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'O', 'O', 'O', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
        {'X', 'X', 'X', 'X', 'X', 'O', 'O', 'O', 'X', 'O', 'X', 'O', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
        {'X', 'X', 'X', 'X', 'X', 'O', 'X', 'O', 'X', 'O', 'X', 'O', 'O', 'O', 'X', 'X', 'X', 'X', 'X', 'X'},
        {'X', 'X', 'X', 'X', 'X', 'O', 'X', 'O', 'O', 'O', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
        {'X', 'X', 'X', 'X', 'X', 'O', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}};
    EXPECT_EQ(expected, board);
}
```



## [56. 合并区间](https://leetcode-cn.com/problems/merge-intervals/)(华为题库)

先排序，再每两个区域依次判断：

* 如果有重叠区域，则将重叠后的完整区域增加到List中，同时要注意List中是否有相同区域，如果有则修改，如果没有则新增。

* 如果没有重叠，则直接将后一个区域增加到List中。

```java
package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class Solution {  // 13ms
    public int[][] merge(int[][] intervals) {
        if (intervals.length == 0) {
            return new int[0][];
        }

        Arrays.sort(intervals, Comparator.comparingInt(e -> e[0]));

        List<int[]> retIntervals = new ArrayList<>();
        retIntervals.add(new int[]{intervals[0][0], intervals[0][1]});  // 至少加一个int[]对象

        for (int i = 0; i < intervals.length - 1; i++) {
            int[] interval = intervals[i];
            int[] nextInterval = intervals[i + 1];
            if (interval[1] >= nextInterval[0]) {  //说明有重叠
                int max = Math.max(interval[1], nextInterval[1]);

                nextInterval[0] = interval[0];
                nextInterval[1] = max;

                int[] last = retIntervals.get(retIntervals.size() - 1);
                if (last[0] == interval[0] && last[1] == interval[1]) {  // 判断最后一个对象是不是和当前修改的重叠区域相等，如果相等，则直接修改list
                    last[0] = nextInterval[0];
                    last[1] = nextInterval[1];
                } else {  // 如果不相等，说明是一个新的区域
                    retIntervals.add(new int[]{nextInterval[0], nextInterval[1]});
                }
            } else {
                retIntervals.add(new int[]{nextInterval[0], nextInterval[1]});
            }
        }

        int[][] ret = new int[retIntervals.size()][];
        retIntervals.toArray(ret);
        return ret;
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] expected = {{1, 6}, {8, 10}, {15, 18}};
        assertArrayEquals(expected, SOLUTION.merge(intervals));
    }

    @Test
    public void test2() {
        int[][] intervals = {{1, 8}, {2, 6}, {8, 10}, {15, 18}};
        int[][] expected = {{1, 10}, {15, 18}};
        assertArrayEquals(expected, SOLUTION.merge(intervals));
    }

    @Test
    public void test3() {
        int[][] intervals = {{1, 8}};
        int[][] expected = {{1, 8}};
        assertArrayEquals(expected, SOLUTION.merge(intervals));
    }

    @Test
    public void test4() {
        int[][] intervals = {};
        int[][] expected = {};
        assertArrayEquals(expected, SOLUTION.merge(intervals));
    }

    @Test
    public void test5() {
        int[][] intervals = {{1, 4}, {5, 6}};
        int[][] expected = {{1, 4}, {5, 6}};
        assertArrayEquals(expected, SOLUTION.merge(intervals));
    }

    @Test
    public void test6() {
        int[][] intervals = {{1, 4}, {0, 4}};
        int[][] expected = {{0, 4}};
        assertArrayEquals(expected, SOLUTION.merge(intervals));
    }
}
```

别人的算法：

<https://leetcode-cn.com/problems/merge-intervals/solution/he-bing-qu-jian-by-leetcode/>

方法 1：连通块

没有看

方法2：排序

思路一样，不过别人的算法会简洁一些：

```java
package test;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

class Solution {
    private static class Interval {
        int start;
        int end;

        Interval(int[] interval) {
            this.start = interval[0];
            this.end = interval[1];
        }

        int[] toArray() {
            return new int[]{this.start, this.end};
        }
    }

    public int[][] merge(int[][] intervals) {
        List<Interval> intervalsList = new LinkedList<>();
        for (int[] interval : intervals) {
            intervalsList.add(new Interval(interval));
        }
        intervalsList.sort(Comparator.comparingInt(a -> a.start));

        LinkedList<Interval> merged = new LinkedList<>();
        for (Interval interval : intervalsList) {
            // if the list of merged intervals is empty or if the current
            // interval does not overlap with the previous, simply append it.
            if (merged.isEmpty() || merged.getLast().end < interval.start) {  // 如果最后一个区域的end比新区域的start小，说明两部分没有重叠，则新增区域
                merged.add(interval);
            }
            // otherwise, there is overlap, so we merge the current and previous
            // intervals.
            else {  // 否则，有重叠，则合并区域
                merged.getLast().end = Math.max(merged.getLast().end, interval.end);
            }
        }

        int i = 0;
        int[][] result = new int[merged.size()][2];
        for (Interval mergedInterval : merged) {
            result[i] = mergedInterval.toArray();
            i++;
        }
        return result;
        // return merged.stream().map(Interval::toArray).toArray(int[][]::new); 使用stream会慢一些
    }
}
```

## [289. 生命游戏](https://leetcode-cn.com/problems/game-of-life/)(华为题库)

新拷贝了一个二维数组用于计算每个单元格的计算，不太符合题目要求：

```java
package test;

class Solution {  // 0ms
    public void gameOfLife(int[][] board) {
        int[][] copiedBoard = new int[board.length][];
        for (int i = 0; i < board.length; i++) {
            copiedBoard[i] = new int[board[i].length];
            System.arraycopy(board[i], 0, copiedBoard[i], 0, board[i].length);  // 深拷贝，Arrays.copyOf是浅拷贝
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = getStatus(copiedBoard, i, j);
            }
        }
        return;
    }

    private int getStatus(int[][] board, int i, int j) {
        int minI = Math.max(i - 1, 0);
        int maxI = Math.min(i + 1, board.length - 1);
        int minJ = Math.max(j - 1, 0);
        int maxJ = Math.min(j + 1, board[0].length - 1);
        int liveCount = 0;
        for (int tmpI = minI; tmpI <= maxI; tmpI++) {
            for (int tmpJ = minJ; tmpJ <= maxJ; tmpJ++) {
                if (tmpI == i && tmpJ == j) {
                    continue;
                }

                if (board[tmpI][tmpJ] == 1) {
                    ++liveCount;
                }
            }
        }

        if (liveCount < 2) {
            return 0;
        } else if (liveCount == 2) {
            return board[i][j];
        } else if (liveCount == 3) {
            return 1;
        } else {  // liveCount > 3
            return 0;
        }
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        int[][] board = {
            {0, 1, 0},
            {0, 0, 1},
            {1, 1, 1},
            {0, 0, 0}
        };
        int[][] expected = {
            {0, 0, 0},
            {1, 0, 1},
            {0, 1, 1},
            {0, 1, 0}
        };
        SOLUTION.gameOfLife(board);
        assertArrayEquals(expected, board);
    }

    @Test
    public void test2() {
        int[][] board = {
            {0, 1, 0},
        };
        int[][] expected = {
            {0, 0, 0},
        };
        SOLUTION.gameOfLife(board);
        assertArrayEquals(expected, board);
    }

    @Test
    public void test3() {
        int[][] board = {};
        int[][] expected = {};
        SOLUTION.gameOfLife(board);
        assertArrayEquals(expected, board);
    }
}
```

别人的优化算法：

<https://leetcode-cn.com/problems/game-of-life/solution/ren-sheng-ku-duan-de-yuan-di-biao-ji-suan-fa-java0/>

先把要变更的值修改为-1或-2，全部计算完成后再进行一次性修改为0或1

```java
package test;

/**
 * 1——保持1
 * -1——1转0
 * 0——保持0
 * -2——0转1
 */
class Solution {
    public void gameOfLife(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = checkLoc(board, i, j);
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = board[i][j] == 1 || board[i][j] == -2 ? 1 : 0;
            }
        }
    }

    public int checkLoc(int[][] board, int i, int j) {
        int count = 0;
        int left = Math.max(j - 1, 0);
        int right = Math.min(j + 1, board[i].length - 1);
        int top = Math.max(i - 1, 0);
        int bottom = Math.min(i + 1, board.length - 1);
        for (int x = top; x <= bottom; x++) {
            for (int y = left; y <= right; y++) {
                count = board[x][y] == 1 || board[x][y] == -1 ? count + 1 : count;
            }
        }
        // 上面在统计每个单元格值的时候，没有排除掉单元格自身，所以这里在判断时多加了1，判断count==3或4
        return board[i][j] == 1 ? (count == 3 || count == 4 ? 1 : -1) : (count == 3 ? -2 : 0);
    }
}
```

## [120. 三角形最小路径和](https://leetcode-cn.com/problems/triangle/)(华为题库)

动态规划：

```java
package test;

import java.util.List;

class Solution {  // 2ms 使用stream 6ms
    public int minimumTotal(List<List<Integer>> triangle) {
        if (triangle.isEmpty()) {
            return 0;
        }

        int[] dp = new int[triangle.get(triangle.size() - 1).size()];
        dp[0] = triangle.get(0).get(0);
        for (int i = 1; i < triangle.size(); i++) {
            List<Integer> row = triangle.get(i);
            for (int j = i; j >= 0; j--) {  // 这里要倒序才能保证上一行的数据不被修改掉
                if (j == 0) {  // 第1个只能取上一行第1个数字
                    dp[0] += row.get(0);
                } else if (i == j) {  // 最后1个只能取上一行最后1个数字
                    dp[j] = dp[j - 1] + row.get(j);
                } else {  // 其余数字要比较哪个小就取哪个
                    dp[j] = Math.min(dp[j - 1], dp[j]) + row.get(j);
                }
            }
        }

        int min = dp[0];
        for (int i = 1; i < dp.length; i++) {
            if (dp[i] < min) {
                min = dp[i];
            }
        }
        return min;
        // return Arrays.stream(dp).min().orElse(0);
    }
}

//
package test;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        List<List<Integer>> triangle = Arrays.asList(
            Arrays.asList(2),
            Arrays.asList(3, 4),
            Arrays.asList(6, 5, 7),
            Arrays.asList(4, 1, 8, 3)
        );
        assertEquals(11, SOLUTION.minimumTotal(triangle));
    }
}
```

## [1162. 地图分析](https://leetcode-cn.com/problems/as-far-from-land-as-possible/)(华为题库)

自己写的暴力算法，一开始对于每个ocean全遍历所有的lands，超时；后优化只要有一个land的最小值小于maxDistance，这个ocean就直接跳过。最终过了但也不快-_-：

```java
package test;

import java.util.ArrayList;
import java.util.List;

class Solution {  // 664ms
    public int maxDistance(int[][] grid) {
        int length = grid.length;
        List<int[]> lands = new ArrayList<>();
        List<int[]> oceans = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (grid[i][j] == 1) {
                    lands.add(new int[]{i, j});
                } else {
                    oceans.add(new int[]{i, j});
                }
            }
        }

        if (lands.isEmpty() || oceans.isEmpty()) {
            return -1;
        }

        int maxDistance = 0;
        for (int[] ocean : oceans) {
            int tmpMaxDistance = getTmpMaxDistance(ocean, lands, maxDistance);
            maxDistance = Math.max(maxDistance, tmpMaxDistance);
        }
        return maxDistance;
    }

    private int getTmpMaxDistance(int[] ocean, List<int[]> lands, int maxDistance) {
        int tmpMaxDistance = 0;
        for (int[] land : lands) {
            int tmpDistance = Math.abs(land[0] - ocean[0]) + Math.abs(land[1] - ocean[1]);
            if (tmpDistance <= maxDistance) {
                return -1;
            }

            if (tmpMaxDistance == 0 || tmpDistance < tmpMaxDistance) {
                tmpMaxDistance = tmpDistance;
            }
        }
        return tmpMaxDistance;
    }
}
```

别人的优化算法：

<https://leetcode-cn.com/problems/as-far-from-land-as-possible/solution/java-bfs-by-dinary-2/>

BFS填海造陆：

思路：一看到最值，首先想到BFS。我们可以先找出所有的陆地，然后从陆地开始向外进行BFS式的扩散，然后每扩散一层就将计数器加一，不断“填海造陆”直到整个地图再也不存在海洋为止。这个时候的计数器就是最远的海洋距离陆地的距离。

```java
class Solution {  // 21ms
    public int maxDistance(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    queue.offer(new int[]{i, j});
                }
            }
        }
        if (queue.isEmpty() || queue.size() == m * n) {
            return -1;
        }
        int[][] next = {{1, 0}, {-1, 0}, {0, -1}, {0, 1}};
        int level = -1;
        while (!queue.isEmpty()) {
            int count = queue.size();
            for (int i = 0; i < count; i++) {
                int[] cur = queue.poll();
                for (int[] nt : next) {
                    int nx = cur[0] - nt[0], ny = cur[1] - nt[1];
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n && grid[nx][ny] == 0) {
                        grid[nx][ny] = 1;
                        queue.offer(new int[]{nx, ny});
                    }
                }
            }
            level++;
        }
        return level;
    }
}
```

## [187. 重复的DNA序列](https://leetcode-cn.com/problems/repeated-dna-sequences/)(华为题库)（只看没做）

别人的算法：

<https://leetcode-cn.com/problems/repeated-dna-sequences/submissions/>

这个题一看是个字母单词，找子串重复很容易就想到滑动窗口，以十个字母为窗口滑动一轮，直接得出结果。
把得到的单词计入hashmap之后，得到的单词存入计数加一，得到计数等于1就加入list，否则不加入list直接去重完成。

```java
class Solution {  // 24ms
    public List<String> findRepeatedDnaSequences(String s) {
        List<String> res = new ArrayList<>();
        //s长度小于等于10当然不会有重复子串
        if (s.length() <= 10) {
            return res;
        }
        //窗口大小
        int window = 10;
        int start = 0;
        //记录出现过的子串
        Map<String, Integer> sMap = new HashMap<>();
        //左为0，右为窗口大小，开始滑动每轮while循环lr自增1
        int l = 0;
        int r = window;
        while (r <= s.length()) {
            //获取当前子串
            String temp = s.substring(l, r);
            //如果已经记录存在，返回对应value值，否则返回1
            int count = sMap.getOrDefault(temp, 0);
            //value值加1，存入map
            sMap.put(temp, count + 1);
            //第一次重复出现的时候加入list，之后再出现此单词不用加入list了，等于去重
            if (count == 1) {
                res.add(temp);
            }
            l++;
            r++;
        }
        return res;
    }
}
```

## [19. 删除链表的倒数第N个节点](https://leetcode-cn.com/problems/remove-nth-node-from-end-of-list/)(华为题库)

自己写的遍历了两遍链表，题目期望的是遍历一遍：

```java
package test;

class ListNode {  // 1ms
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
    }
}

class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        int count = 0;
        ListNode l = head;
        while (l != null) {
            ++count;
            l = l.next;
        }

        int indexToDelete = count - n;
        if (indexToDelete == 0) {  // 如果要删除第1个
            l = head.next;
            head.next = null;
            return l;
        } else {
            // 找到要删除节点的前一个
            int index = 0;
            l = head;
            while (++index < indexToDelete) {
                l = l.next;
            }

            ListNode nodeToDelete = l.next;
            l.next = nodeToDelete.next;
            nodeToDelete.next = null;
            return head;
        }
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        ListNode l5 = new ListNode(5);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l5;

        ListNode expected = SOLUTION.removeNthFromEnd(l1, 2);
        assertEquals(1, expected.val);
        assertEquals(2, expected.next.val);
        assertEquals(3, expected.next.next.val);
        assertEquals(5, expected.next.next.next.val);
        assertEquals(null, expected.next.next.next.next);
    }

    @Test
    public void test2() {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        ListNode l5 = new ListNode(5);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l5;

        ListNode expected = SOLUTION.removeNthFromEnd(l1, 5);
        assertEquals(2, expected.val);
        assertEquals(3, expected.next.val);
        assertEquals(4, expected.next.next.val);
        assertEquals(5, expected.next.next.next.val);
        assertEquals(null, expected.next.next.next.next);
    }

    @Test
    public void test3() {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        ListNode l5 = new ListNode(5);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        l4.next = l5;

        ListNode expected = SOLUTION.removeNthFromEnd(l1, 1);
        assertEquals(1, expected.val);
        assertEquals(2, expected.next.val);
        assertEquals(3, expected.next.next.val);
        assertEquals(4, expected.next.next.next.val);
        assertEquals(null, expected.next.next.next.next);
    }
}
```

别人的优化算法：

<https://leetcode-cn.com/problems/remove-nth-node-from-end-of-list/solution/shan-chu-lian-biao-de-dao-shu-di-nge-jie-dian-by-l/>

一次遍历算法
算法

上述算法可以优化为只使用一次遍历。我们可以使用两个指针而不是一个指针。第一个指针从列表的开头向前移动 n+1 步，而第二个指针将从列表的开头出发。现在，这两个指针被 n 个结点分开。我们通过同时移动两个指针向前来保持这个恒定的间隔，直到第一个指针到达最后一个结点。此时第二个指针将指向从最后一个结点数起的第 n 个结点。我们重新链接第二个指针所引用的结点的 next 指针指向该结点的下下个结点。

```java
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode first = dummy;
        ListNode second = dummy;
        // Advances first pointer so that the gap between first and second is n nodes apart
        for (int i = 1; i <= n + 1; i++) {
            first = first.next;
        }
        // Move first to the end, maintaining the gap
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return dummy.next;
    }
}
```

## [343. 整数拆分](https://leetcode-cn.com/problems/integer-break/)(华为题库)

自己写的两两拆分，不太对：

```java
package test;

class Solution {
    public int integerBreak(int n) {
        int first = n / 2;
        int second = n - first;
        int ret = first * second;

        int firstSplitRet = splitToGetMaxMul(first, second, ret);
        int secondSplitRet = splitToGetMaxMul(second, first, ret);
        return firstSplitRet * secondSplitRet;
    }

    private int splitToGetMaxMul(int numToSplit, int fixedNum, int ret) {
        if (numToSplit < 2) {
            return numToSplit;
        }

        int first = numToSplit / 2;
        int second = numToSplit - first;
        int tmpMulRet = first * second;
        if (tmpMulRet * fixedNum > ret) {
            int firstSplitRet = splitToGetMaxMul(first, second, tmpMulRet);
            int secondSplitRet = splitToGetMaxMul(second, first, tmpMulRet);
            return firstSplitRet * secondSplitRet;
        }
        return numToSplit;
    }
}
```

自己改过的代码，把每个数字都从2次平均拆分开始不断增加平均拆分次数，看乘积是否更大，如果增加了拆分次数，乘积没有增大，则说明已经获得了最大乘积了。同时用Map保存：每个数字-均拆分次数-乘积 来缓存免得重复计算：

```java
package test;

import java.util.HashMap;
import java.util.Map;

class Solution {  // 1ms
    public int integerBreak(int n) {
        Map<Integer, Map<Integer, Integer>> nSplitCountMaxMulMap = new HashMap<>();

        int splitNum = 2;
        int ret = 1;
        while (splitNum <= n) {
            int tmpRet = splitToGetMaxMul(n, splitNum, nSplitCountMaxMulMap);
            if (tmpRet > ret) {
                ret = tmpRet;
                ++splitNum;
            } else {
                break;
            }
        }
        return ret;
    }

    private int splitToGetMaxMul(int n, int nextSplitNum, Map<Integer, Map<Integer, Integer>> nSplitCountMaxMulMap) {
        if (nextSplitNum == 2) {
            return n / 2 * (n - n / 2);
        }

        Map<Integer, Integer> splitCountMaxMulMap = nSplitCountMaxMulMap.get(n);
        if (splitCountMaxMulMap != null) {
            int defaultMulVal = splitCountMaxMulMap.getOrDefault(nextSplitNum, 0);
            if (defaultMulVal != 0) {
                return defaultMulVal;
            }
        }

        int n1 = n / nextSplitNum;
        int left = splitToGetMaxMul(n - n1, nextSplitNum - 1, nSplitCountMaxMulMap);
        int maxVal = n1 * left;
        if (splitCountMaxMulMap == null) {
            splitCountMaxMulMap = new HashMap<>();
            nSplitCountMaxMulMap.put(n, splitCountMaxMulMap);
        }
        splitCountMaxMulMap.put(nextSplitNum, maxVal);
        return maxVal;
    }
}
```

另外去掉Map的缓存，直接全部计算，反而更快：

```java
package test;

class Solution {  // 0ms
    public int integerBreak(int n) {
        int splitNum = 2;
        int ret = 1;
        while (splitNum <= n) {
            int tmpRet = splitToGetMaxMul(n, splitNum);
            if (tmpRet > ret) {
                ret = tmpRet;
                ++splitNum;
            } else {
                break;
            }
        }
        return ret;
    }

    private int splitToGetMaxMul(int n, int nextSplitNum) {
        if (nextSplitNum == 2) {
            return n / 2 * (n - n / 2);
        }

        int n1 = n / nextSplitNum;
        int left = splitToGetMaxMul(n - n1, nextSplitNum - 1);
        int maxVal = n1 * left;
        return maxVal;
    }
}
```

别人的算法：

<https://leetcode-cn.com/problems/integer-break/solution/343-zheng-shu-chai-fen-tan-xin-by-jyd/>

经分析，拆成3乘积最大（4除外），所以拆成a个3，根据余数算最大乘积。

```java
class Solution {
    public int integerBreak(int n) {
        if (n <= 3) {
            return n - 1;
        }
        int a = n / 3, b = n % 3;
        if (b == 0) {
            return (int) Math.pow(3, a);
        }
        if (b == 1) {
            return (int) Math.pow(3, a - 1) * 4;
        }
        return (int) Math.pow(3, a) * 2;
    }
}
```

别人的动态规划：

<https://leetcode-cn.com/problems/integer-break/solution/c-dong-tai-gui-hua-by-da-li-wang-26/>

dp[i]表示数字i的最大乘积。

则依次判断每个数字是否要拆分，Math.max(j, dp[j])表示用j去组成i时的最大值，Math.max(i - j, dp[i - j])表示拆出j后的最大值，乘积后获得i拆分成j和i - j的乘积，将此值与dp[i]判断，取最大值，则为dp[i]时的最大乘积。

```java
class Solution {
    public int integerBreak(int n) {
        int[] dp = new int[n + 1];
        dp[2] = 1;
        for (int i = 3; i <= n; ++i) {
            for (int j = 1; j < i; ++j) {
                dp[i] = Math.max(dp[i], Math.max(j, dp[j]) * Math.max(i - j, dp[i - j]));
            }
        }
        return dp[n];
    }
}
```

## [6. Z 字形变换](https://leetcode-cn.com/problems/zigzag-conversion/)(华为题库)

构造了numRows行StringBuilder，根据index不断append字符即可：

```java
package test;

import java.util.ArrayList;
import java.util.List;

class Solution {  // 8ms
    public String convert(String s, int numRows) {
        // 如果只有一行，则直接返回
        if (numRows == 1) {
            return s;
        }

        List<StringBuilder> stringBuilders = new ArrayList<>();
        for (int i = 0; i < Math.min(numRows, s.length()); i++) {
            stringBuilders.add(new StringBuilder());
        }

        int direction = 1;
        int index = 0;
        for (int i = 0; i < s.length(); i++) {
            stringBuilders.get(index).append(s.charAt(i));

            index += direction;
            if (index + 1 == numRows || index == 0) {
                direction = -direction;
            }
        }

        StringBuilder ret = new StringBuilder();
        for (StringBuilder stringBuilder : stringBuilders) {
            ret.append(stringBuilder);
        }
        return ret.toString();
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        assertEquals("LCIRETOESIIGEDHN", SOLUTION.convert("LEETCODEISHIRING", 3));
    }

    @Test
    public void test2() {
        assertEquals("LDREOEIIECIHNTSG", SOLUTION.convert("LEETCODEISHIRING", 4));
    }

    @Test
    public void test3() {
        assertEquals("AB", SOLUTION.convert("AB", 1));
    }
}
```

别人的算法：

<https://leetcode-cn.com/problems/zigzag-conversion/solution/z-zi-xing-bian-huan-by-leetcode/>

第一种跟上面自己想的一样。

方法二：按行访问

计算每行数字在原字符串的位置，按公式直接拼字符串，访问0行所有字符、1行所有字符。。。

```java
class Solution {  // 4ms
    public String convert(String s, int numRows) {
        if (numRows == 1) {
            return s;
        }

        StringBuilder ret = new StringBuilder();
        int n = s.length();
        int cycleLen = 2 * numRows - 2;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j + i < n; j += cycleLen) {
                ret.append(s.charAt(j + i));
                if (i != 0 && i != numRows - 1 && j + cycleLen - i < n) {
                    ret.append(s.charAt(j + cycleLen - i));
                }
            }
        }
        return ret.toString();
    }
}
```

## [227. 基本计算器 II](https://leetcode-cn.com/problems/basic-calculator-ii/)(华为题库)（只看没做）

初步思路是把按+、-、*、/四个符合拆分字符串，然后用两个栈，一个存储数字，一个存储运算符，两个栈来计算。

别人代码：

<https://leetcode-cn.com/problems/basic-calculator-ii/solution/c-zhan-jie-fa-by-da-li-wang-3/>

```java
package test;

import java.util.Stack;

class Solution {  // 46ms
    public int calculate(String s) {
        s += "+";
        if (s.isEmpty()) {
            return 0;
        }

        Stack<Integer> vals = new Stack<>();
        Stack<Character> ops = new Stack<>();
        int val = 0;
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c == ' ') {
                continue;
            } else if (c >= '0' && c <= '9') {
                val *= 10;
                val += c - '0';
            } else {
                vals.push(val);
                val = 0;
                // * / 在每次遍历字符的过程中就已经计算消耗了
                if (!ops.empty() && (ops.peek() == '*' || ops.peek() == '/')) {
                    int v1 = vals.pop();
                    int v2 = vals.pop();
                    char op = ops.pop();
                    int v = (op == '*') ? (v2 * v1) : (v2 / v1);
                    vals.push(v);
                }
                ops.push(c);
            }
        }
        vals.push(val);
        int res = 0;
        while (!ops.empty()) {
            int sign = ops.pop() == '-' ? -1 : 1;
            int v = vals.pop();
            res += sign * v;
        }
        if (!vals.empty()) {
            res += vals.peek();
        }
        return res;
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        assertEquals(5, SOLUTION.calculate("3+5/2"));
    }
}
```

## [151. 翻转字符串里的单词](https://leetcode-cn.com/problems/reverse-words-in-a-string/)(华为题库)

正则表达式拆分再反向拼接：

```java
package test;

class Solution {  // 8ms
    public String reverseWords(String s) {
        s = s.trim();
        String[] strings = s.split(" +");  // 注意如果有多个空格，要正则表达式+号一起拆分
        StringBuilder ret = new StringBuilder();
        for (int i = strings.length - 1; i >= 0; --i) {
            if (ret.length() != 0) {
                ret.append(" ");
            }
            ret.append(strings[i]);
        }
        return ret.toString();
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        assertEquals("blue is sky the", SOLUTION.reverseWords("the sky is blue"));
    }

    @Test
    public void test2() {
        assertEquals("world! hello", SOLUTION.reverseWords("  hello world!  "));
    }

    @Test
    public void test3() {
        assertEquals("example good a", SOLUTION.reverseWords("a good   example"));
    }
}
```

别人的优化原地算法：

<https://leetcode-cn.com/problems/reverse-words-in-a-string/solution/xiang-xi-tong-su-de-si-lu-fen-xi-duo-jie-fa-by-36/>

```java
package test;

class Solution {  // 5ms
    public String reverseWords(String s) {
        if (s == null) {
            return null;
        }

        char[] a = s.toCharArray();
        int n = a.length;

        // step 1. reverse the whole string
        reverse(a, 0, n - 1);
        // step 2. reverse each word
        reverseWords(a, n);
        // step 3. clean up spaces
        return cleanSpaces(a, n);
    }

    void reverseWords(char[] a, int n) {
        int i = 0, j = 0;

        while (i < n) {
            while (i < j || i < n && a[i] == ' ') {
                i++; // skip spaces
            }
            while (j < i || j < n && a[j] != ' ') {
                j++; // skip non spaces
            }
            reverse(a, i, j - 1);                      // reverse the word
        }
    }

    // trim leading, trailing and multiple spaces
    String cleanSpaces(char[] a, int n) {
        int i = 0, j = 0;

        while (j < n) {
            while (j < n && a[j] == ' ') {
                j++;             // skip spaces
            }
            while (j < n && a[j] != ' ') {
                a[i++] = a[j++]; // keep non spaces
            }
            while (j < n && a[j] == ' ') {
                j++;             // skip spaces
            }
            if (j < n) {
                a[i++] = ' ';                      // keep only one space
            }
        }

        return new String(a).substring(0, i);
    }

    // reverse a[] from a[i] to a[j]
    private void reverse(char[] a, int i, int j) {
        while (i < j) {
            char t = a[i];
            a[i++] = a[j];
            a[j--] = t;
        }
    }
}
```

使用C++写，正则表达式拆分字符串：

```c++
#include "gtest/gtest.h"
#include <regex>

using namespace std;

class Solution {  // 48ms
public:
    string reverseWords(string s) {
        regex re(" +");
        vector<string> splitStr{sregex_token_iterator(s.begin(), s.end(), re, -1), sregex_token_iterator()};

        string ret;
        for_each(splitStr.rbegin(), splitStr.rend(), [&ret](const auto& word) {
            if (!word.empty()) {
                ret += word + " ";
            }
        });
        return ret.substr(0, ret.size() - 1);
    }
};

class SolutionTest : public testing::Test {
protected:
    static Solution solution;
};

Solution SolutionTest::solution;

TEST_F(SolutionTest, Test1) {
    EXPECT_EQ("world! hello", solution.reverseWords("  hello world!  "));
}

TEST_F(SolutionTest, Test2) {
    EXPECT_EQ("example good a", solution.reverseWords("a good   example"));
}

TEST_F(SolutionTest, Test3) {
    EXPECT_EQ("blue is sky the", solution.reverseWords("the sky is blue"));
}
```

原地算法：

```c++
// 使用字符串遍历查找反转单词
class Solution {  // 4ms
public:
    string reverseWords(string s) {
        // 移除头尾和中间空格
        removeSpaces(s);
        // 翻转整个字符串
        reverse(s.begin(), s.end());
        // 依次翻转每个单词
        reverseWord(s);
        return s;
    }

private:
    void reverseWord(string& inStr) {
        auto iterWordBegin = inStr.begin();
        auto iterWordEnd = inStr.begin();
        while (iterWordBegin != inStr.end()) {
            iterWordBegin = iterWordEnd;
            while (iterWordBegin != inStr.end() && *iterWordBegin == ' ') {
                ++iterWordBegin;
            }

            iterWordEnd = iterWordBegin;
            while (iterWordEnd != inStr.end() && *iterWordEnd != ' ') {
                ++iterWordEnd;
            }

            reverse(iterWordBegin, iterWordEnd);
        }
    }

    void removeSpaces(string& inStr) {
        inStr.erase(0, inStr.find_first_not_of(" "));
        inStr.erase(inStr.find_last_not_of(" ") + 1);
        inStr.erase(unique(inStr.begin(), inStr.end(), [](char c1, char c2) { return c1 == ' ' && c1 == c2; }),
                    inStr.end());
    }
};

// 使用正则表达式来查找反转单词
class Solution {
public:
    string reverseWords(string s) {
        // 移除头尾和中间空格
        removeSpaces(s);
        // 翻转整个字符串
        reverse(s.begin(), s.end());
        // 依次翻转每个单词
        reverseWord(s);
        return s;
    }

private:
    void reverseWord(string& inStr) {
        auto cIterBegin = inStr.cbegin();
        auto cIterEnd = inStr.cend();
        smatch matchWord;
        regex re("[^ ]+");
        while (regex_search(cIterBegin, cIterEnd, matchWord, re)) {  // 这里regex_search必须使用const_iterator
            auto iterWordBegin = convertCIterToIter(inStr.begin(), matchWord[0].first);  // 获取const_iterator对应的iterator
            auto iterWordEnd = convertCIterToIter(inStr.begin(), matchWord[0].second);
            reverse(iterWordBegin, iterWordEnd);  // 这里reverse必须使用iterator
            cIterBegin = matchWord[0].second;
        }
    }

    // 根据起始iterator和目标const_iterator获得目标iterator
    string::iterator convertCIterToIter(string::iterator iterDst, string::const_iterator cIterDst) {
        advance(iterDst, distance<string::const_iterator>(iterDst, cIterDst));  // distance的两个入参一个是iterator，一个是const_iterator，所以这里必须指定函数模板参数<string:const_iterator>
        return iterDst;
    }

    void removeSpaces(string& inStr) {
        inStr.erase(0, inStr.find_first_not_of(" "));
        inStr.erase(inStr.find_last_not_of(" ") + 1);
        inStr.erase(unique(inStr.begin(), inStr.end(), [](char c1, char c2) { return c1 == ' ' && c1 == c2; }),
                    inStr.end());
    }
};
```

## [62. 不同路径](https://leetcode-cn.com/problems/unique-paths/)(华为题库)

动态规划：

`dp[i][j]`表示i * j大小的格子的走法。每个新格子i, j的走法就是往下走一步即i - 1, j的走法 + 往右走一步即i, j - 1的走法和。如果i或j为1，则只有一条走法。

```java
package test;

class Solution {  // 1ms
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (i == 1 || j == 1) {
                    dp[i][j] = 1;
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        return dp[m][n];
    }
}
```

别人的优化算法：

<https://leetcode-cn.com/problems/unique-paths/solution/dong-tai-gui-hua-by-powcai-2/>

思路一：排列组合

因为机器到底右下角，向下几步，向右几步都是固定的，所就是从总步数m+n-2中挑m-1步（向右，向下都无所谓）即可，其余步数就另一个方向。

比如，m=3, n=2，我们只要向下 1 步，向右 2 步就一定能到达终点。

所以有 C m+n-2 m-1

```python
def uniquePaths(self, m: int, n: int) -> int:
        return int(math.factorial(m+n-2)/math.factorial(m-1)/math.factorial(n-1))
```

思路二：动态规划

优化的算法：基于一维数组计算，每次遍历完j获得的数组cur值即为下表中的每列值：

![](pictures\62. 不同路径（表格演算）.png)

```java
class Solution {  // 0ms
    public int uniquePaths(int m, int n) {
        int[] cur = new int[n];
        Arrays.fill(cur,1);
        for (int i = 1; i < m;i++){
            for (int j = 1; j < n; j++){
                cur[j] += cur[j-1] ;
            }
        }
        return cur[n-1];
    }
}
```

## [96. 不同的二叉搜索树](https://leetcode-cn.com/problems/unique-binary-search-trees/)(华为题库)（只看没做）

别人的算法：

<https://leetcode-cn.com/problems/unique-binary-search-trees/solution/bu-tong-de-er-cha-sou-suo-shu-by-leetcode/>

方法一：动态规划

问题是计算不同二叉搜索树的个数。为此，我们可以定义两个函数：

G(n): 长度为n的序列的不同二叉搜索树个数。

F(i, n): 以i为根的不同二叉搜索树个数(1≤i≤n)。

![](pictures\96. 不同的二叉搜索树（方法一：动态规划）.png)

```java
public class Solution {  // 0ms
  public int numTrees(int n) {
    int[] G = new int[n + 1];
    G[0] = 1;
    G[1] = 1;

    for (int i = 2; i <= n; ++i) {
      for (int j = 1; j <= i; ++j) {
        G[i] += G[j - 1] * G[i - j];
      }
    }
    return G[n];
  }
}
```

方法二：数学演绎法

事实上 G(n)*G*(*n*)函数的值被称为 [卡塔兰数](https://baike.baidu.com/item/catalan/7605685?fr=aladdin) Cn，根据递推公式，可得：

```java
class Solution {
  public int numTrees(int n) {
    // Note: we should use long here instead of int, otherwise overflow
    long C = 1;
    for (int i = 0; i < n; ++i) {
      C = C * 2 * (2 * i + 1) / (i + 2);
    }
    return (int) C;
  }
}
```

或根据卡塔兰数的计算公式，可直接得到结果：

令h(0)=1,h(1)=1，卡塔兰数满足[递归](https://baike.baidu.com/item/递归/1740695)式：

**h(n)= h(0)\*h(n-1) + h(1)\*h(n-2) + ... + h(n-1)h(0) (其中n>=2),这是n阶[递推](https://baike.baidu.com/item/递推/4506973)关系;**

还可以化简为1阶递推关系: 如h(n)=(4n-2)/(n+1)\*h(n-1)(n>1) h(0)=1

该递推关系的解为：`h(n)=C(2n,n)/(n+1)=P(2n,n)/(n+1)!=(2n)!/(n!*(n+1)!) (n=1,2,3,...)`

## [442. 数组中重复的数据](https://leetcode-cn.com/problems/find-all-duplicates-in-an-array/)(华为题库)（只看没做）

你可以不用到任何额外空间并在O(*n*)时间复杂度内解决这个问题吗？

别人的算法：

<https://leetcode-cn.com/problems/find-all-duplicates-in-an-array/solution/chou-ti-yuan-li-ji-yu-yi-huo-yun-suan-jiao-huan-li/>

方法：“抽屉原理” + 基于“异或运算”交换两个变量的值

思路分析：“桶排序”的思想是“抽屉原理”，即“一个萝卜一个坑”，8 个萝卜要放在 7 个坑里，则至少有 1 个坑里至少有 2 个萝卜。

“抽屉原理”的思想很简单，但是借助它我们可以完成一些比较难的问题，例如：「力扣」第 41 题：缺失的第一个正数（困难）。还有一个与本题（标注为“中等”）类似的问题：「力扣」第 448 题： 找到所有数组中消失的数字（简单），就很神奇，同样是“抽屉原理”，可以解决的问题涵盖了“简单”、“中等”、“困难”三个等级。

这里由于数组元素限定在数组长度的范围内，因此，我们可以通过一次遍历：

* 让数值 1 就放在索引位置 0 处；
* 让数值 2 就放在索引位置 1 处；
* 让数值 3 就放在索引位置 2 处；
  ……

一次遍历以后，那些“无处安放”的元素就是我们要找的“出现两次的元素”。

为了不使用额外的空间，这里使用到的一个技巧是“基于异或运算交换两个变量的值”：交换两个整数，除了引入一个新的变量，写出一个“轮换”的赋值表达式以外，还有两种比较 tricky 的做法，下面给出结论。

![](pictures\442. 数组中重复的数据（异或交换公式）.png)

```java
public class Solution {
    public List<Integer> findDuplicates(int[] nums) {
        List<Integer> res = new ArrayList<>();
        int len = nums.length;
        if (len == 0) {
            return res;
        }
        for (int i = 0; i < len; i++) {  // 不断交换每个数字，使得每个数字i都在索引为i - 1的位置
            while (nums[nums[i] - 1] != nums[i]) {
                swap(nums, i, nums[i] - 1);
            }
        }
        for (int i = 0; i < len; i++) {  // 所有不在索引为i - 1的数字即为重复数字
            if (nums[i] - 1 != i) {
                res.add(nums[i]);
            }
        }
        return res;
    }

    private void swap(int[] nums, int index1, int index2) {
        if (index1 == index2) {
            return;
        }
        nums[index1] = nums[index1] ^ nums[index2];
        nums[index2] = nums[index1] ^ nums[index2];
        nums[index1] = nums[index1] ^ nums[index2];
    }
}
```

## [593. 有效的正方形](https://leetcode-cn.com/problems/valid-square/)(华为题库)（只看没做）

别人的算法：

<https://leetcode-cn.com/problems/valid-square/solution/you-xiao-de-zheng-fang-xing-by-leetcode/>

方法一：枚举
根据定义，一个四边形为正方形当且仅当它的四条边长度相等并且两条对角线的长度也相等。因此我们只需要枚举给定的 4 个点分别对应正方形的哪一个角，再计算长度并进行比较即可。4 个点对应了 4! = 24 种全排列，因此我们最多只需要枚举 24 次。

```java
public class Solution {  // 1ms
    public boolean validSquare(int[] p1, int[] p2, int[] p3, int[] p4) {
        int[][] p = {p1, p2, p3, p4};
        return checkAllPermutations(p, 0);
    }

    private double dist(int[] p1, int[] p2) {
        return (p2[1] - p1[1]) * (p2[1] - p1[1]) + (p2[0] - p1[0]) * (p2[0] - p1[0]);
    }

    private boolean check(int[] p1, int[] p2, int[] p3, int[] p4) {
        return dist(p1, p2) > 0 && dist(p1, p2) == dist(p2, p3) && dist(p2, p3) == dist(p3, p4) && dist(p3, p4) == dist(p4, p1) && dist(p1, p3) == dist(p2, p4);  // 判断>0是为了避免4个点在重叠的场景
    }

    private boolean checkAllPermutations(int[][] p, int l) {
        if (l == 4) {
            return check(p[0], p[1], p[2], p[3]);
        } else {
            for (int i = l; i < 4; i++) {
                swap(p, l, i);
                if (checkAllPermutations(p, l + 1)) {  // 只要一种组合满足正方形的定义即可返回，不用判断其它组合了
                    return true;
                }
                swap(p, l, i);
            }
            return false;
        }
    }

    private void swap(int[][] p, int x, int y) {
        int[] temp = p[x];
        p[x] = p[y];
        p[y] = temp;
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        int[] p1 = {0, 0}, p2 = {0, 1}, p3 = {1, 0}, p4 = {1, 1};
        assertEquals(true, SOLUTION.validSquare(p1, p2, p3, p4));
    }
}
```

方法二：排序
我们将这 4 个点按照 x 轴坐标第一关键字，y 轴坐标第二关键字进行升序排序。假设排完序之后的 4 个点依次为 p0, p1, p2, p3，那么我们可以发现正方形的四条边依次为 p0p1，p1p3，p3p2 和 p2p0，对角线为 p0p3 和 p1p2。那么如果能组成正方形，则p0p3一定为对角线，其余各边和对角线类似。

```java
public class Solution {  // 2ms
    public boolean validSquare(int[] p1, int[] p2, int[] p3, int[] p4) {
        int[][] p = {p1, p2, p3, p4};
        Arrays.sort(p, (l1, l2) -> l2[0] == l1[0] ? l1[1] - l2[1] : l1[0] - l2[0]);
        return dist(p[0], p[1]) != 0 && dist(p[0], p[1]) == dist(p[1], p[3]) && dist(p[1], p[3]) == dist(p[3], p[2]) && dist(p[3], p[2]) == dist(p[2], p[0]) && dist(p[0], p[3]) == dist(p[1], p[2]);
    }

    private double dist(int[] p1, int[] p2) {
        return (p2[1] - p1[1]) * (p2[1] - p1[1]) + (p2[0] - p1[0]) * (p2[0] - p1[0]);
    }
}
```

方法三：考虑本质不同的情况
在方法一中，我们枚举了所有的 24 种情形，我们把所有的情形写在下面的表中，可以发现一共只有 3 种本质不同的情形，这与方法二也是一致的。

```java
public class Solution {
    public double dist(int[] p1, int[] p2) {
        return (p2[1] - p1[1]) * (p2[1] - p1[1]) + (p2[0] - p1[0]) * (p2[0] - p1[0]);
    }
    public boolean check(int[] p1, int[] p2, int[] p3, int[] p4) {
        return dist(p1,p2) > 0 && dist(p1, p2) == dist(p2, p3) && dist(p2, p3) == dist(p3, p4) && dist(p3, p4) == dist(p4, p1) && dist(p1, p3) == dist(p2, p4);
    }
    public boolean validSquare(int[] p1, int[] p2, int[] p3, int[] p4) {
        return check(p1, p2, p3, p4) || check(p1, p3, p2, p4) || check(p1, p2, p4, p3);
    }
}
```

## [423. 从英文中重建数字](https://leetcode-cn.com/problems/reconstruct-original-digits-from-english/)(华为题库)（只看没做）

自己的思路：把全部的字母计数，然后从0（zero）取4个字母最小值一起减，就为0的个数，再从1（one）取3个字母最小值一起减，依次类推。看了如下分析，则此方法有问题，如twonine会把one给拆分出来，导致剩下的字母无法组成合法的数字了。

也想到要找每个单词的关键字母，但没有细想实施。

别人思路：

<https://leetcode-cn.com/problems/reconstruct-original-digits-from-english/solution/cong-ying-wen-zhong-zhong-jian-shu-zi-by-leetcode/>

方法：哈希表
直觉

最直接的方法是，首先从字母中构造尽可能多的 “zero”，然后再构造尽可能多的 “one”，以此类推。问题在于，字母 “o”，“n”，“e” 都可以在其他的数字中出现，这意味着直接的方法可能会带来一些问题。

因此，我们需要寻找一些独特的标志。注意到，所有的偶数都包含一个独特的字母：

“z” 只在 “zero” 中出现。
“w” 只在 “two” 中出现。
“u” 只在 “four” 中出现。
“x” 只在 “six” 中出现。
“g” 只在 “eight” 中出现。
因此，从偶数开始是一个很好的思路。

这也是计算 3，5 和 7 的关键，因为有些单词只在一个奇数和一个偶数中出现（而且偶数已经被计算过了）：

“h” 只在 “three” 和 “eight” 中出现。
“f” 只在 “five” 和 “four” 中出现。
“s” 只在 “seven” 和 “six” 中出现。
接下来只需要处理 9和 0，思路依然相同。

“i” 在 “nine”，“five”，“six” 和 “eight” 中出现。
“n” 在 “one”，“seven” 和 “nine” 中出现。

```java
class Solution {  // 6ms
    public String originalDigits(String s) {
        // building hashmap letter -> its frequency
        char[] count = new char[26];
        for (char letter : s.toCharArray()) {
            count[letter - 'a']++;
        }

        // building hashmap digit -> its frequency
        int[] out = new int[10];
        // letter "z" is present only in "zero"
        out[0] = count['z' - 'a'];
        // letter "w" is present only in "two"
        out[2] = count['w' - 'a'];
        // letter "u" is present only in "four"
        out[4] = count['u' - 'a'];
        // letter "x" is present only in "six"
        out[6] = count['x' - 'a'];
        // letter "g" is present only in "eight"
        out[8] = count['g' - 'a'];
        // letter "h" is present only in "three" and "eight"
        out[3] = count['h' - 'a'] - out[8];
        // letter "f" is present only in "five" and "four"
        out[5] = count['f' - 'a'] - out[4];
        // letter "s" is present only in "seven" and "six"
        out[7] = count['s' - 'a'] - out[6];
        // letter "i" is present in "nine", "five", "six", and "eight"
        out[9] = count['i' - 'a'] - out[5] - out[6] - out[8];
        // letter "n" is present in "one", "nine", and "seven"
        out[1] = count['n' - 'a'] - out[7] - 2 * out[9];

        // building output string
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < out[i]; j++) {
                output.append(i);
            }
        }
        return output.toString();
    }
}
```

## [94. 二叉树的中序遍历](https://leetcode-cn.com/problems/binary-tree-inorder-traversal/)(华为题库)

参考98. 验证二叉树中的算法

## [22. 括号生成](https://leetcode-cn.com/problems/generate-parentheses/)(华为题库)（只看没做）

别人的优化算法：

<https://leetcode-cn.com/problems/generate-parentheses/solution/gua-hao-sheng-cheng-by-leetcode/>

方法一：暴力法
思路

我们可以生成所有 2^2n  个 '(' 和 ')' 字符构成的序列。然后，我们将检查每一个是否有效。

算法

为了生成所有序列，我们使用递归。长度为 n 的序列就是 '(' 加上所有长度为 n-1 的序列，以及 ')' 加上所有长度为 n-1 的序列。

为了检查序列是否为有效的，我们会跟踪 平衡，也就是左括号的数量减去右括号的数量的净值。如果这个值始终小于零或者不以零结束，该序列就是无效的，否则它是有效的。

```java
class Solution {  // 2ms
    public List<String> generateParenthesis(int n) {
        List<String> combinations = new ArrayList();
        generateAll(new char[2 * n], 0, combinations);
        return combinations;
    }

    public void generateAll(char[] current, int pos, List<String> result) {
        if (pos == current.length) {
            if (valid(current)) {
                result.add(new String(current));
            }
        } else {
            current[pos] = '(';
            generateAll(current, pos + 1, result);
            current[pos] = ')';
            generateAll(current, pos + 1, result);
        }
    }

    public boolean valid(char[] current) {
        int balance = 0;
        for (char c : current) {
            if (c == '(') {
                balance++;
            } else {
                balance--;
            }
            if (balance < 0) {
                return false;
            }
        }
        return (balance == 0);
    }
}
```

方法二：回溯法
思路和算法

只有在我们知道序列仍然保持有效时才添加 '(' or ')'，而不是像 方法一 那样每次添加。我们可以通过跟踪到目前为止放置的左括号和右括号的数目来做到这一点，

如果我们还剩一个位置，我们可以开始放一个左括号。 如果它不超过左括号的数量，我们可以放一个右括号。

```java
class Solution {  // 2ms
    public List<String> generateParenthesis(int n) {
        List<String> ans = new ArrayList();
        backtrack(ans, "", 0, 0, n);
        return ans;
    }

    public void backtrack(List<String> ans, String cur, int open, int close, int max) {
        if (cur.length() == max * 2) {
            ans.add(cur);
            return;
        }

        if (open < max) {
            backtrack(ans, cur + "(", open + 1, close, max);
        }
        if (close < open) {
            backtrack(ans, cur + ")", open, close + 1, max);
        }
    }
}
```

方法三：闭合数

思路

为了枚举某些内容，我们通常希望将其表示为更容易计算的不相交子集的总和。

考虑有效括号序列 S 的 闭包数：至少存在 index >= 0，使得 S[0], S[1], ..., S[2*index+1]是有效的。 显然，每个括号序列都有一个唯一的闭包号。 我们可以尝试单独列举它们。

算法

对于每个闭合数 c，我们知道起始和结束括号必定位于索引 0 和 2*c + 1。然后两者间的 2*c 个元素一定是有效序列，其余元素一定是有效序列。

方法三的理解： 对于任何一个括号组合（1对以上），可以表达为这么一种组合 ( left ) right 的形式。其中，若left 和 right 两部分的括号总数为 n-1对，那么新的组合 ( left ) right 则有n对，这样将n对括号求解，转化为n-1对的求解，以此类推直到零对括号可以直接给出空字符串的解。以3对为例，可拆解为 “( left=0对）right=2对”“( left=1对）right=1对”“( left=2对）right=0对”。对于上述题解给出的方案，如果缓存中间组合，可以减少搜索次数。

```java
class Solution {  // 2ms
    private Map<Integer, List<String>> nBracketsMap = new HashMap<>();  // 不缓存耗时约3ms

    public List<String> generateParenthesis(int n) {
        if (nBracketsMap.containsKey(n)) {
            return nBracketsMap.get(n);
        }

        List<String> ans = new ArrayList();
        if (n == 0) {
            ans.add("");
        } else {
            for (int c = 0; c < n; ++c) {
                for (String left : generateParenthesis(c)) {
                    for (String right : generateParenthesis(n - 1 - c)) {
                        ans.add("(" + left + ")" + right);
                    }
                }
            }
        }
        nBracketsMap.put(n, ans);
        return ans;
    }
}
```

别人的解法：

<https://leetcode-cn.com/problems/generate-parentheses/solution/zui-jian-dan-yi-dong-de-dong-tai-gui-hua-bu-lun-da/>

动态规划：

这个非递归的方法和上面的闭合数的思路一样，只不过把每组()计算结合保存在`List<List<String>>`中，需要的时候直接获取：

```java
class Solution {  // 2ms
    public List<String> generateParenthesis(int n) {
        if (n == 0) {
            return new ArrayList<>();
        }

        List<List<String>> result = new LinkedList<>();
        result.add(Arrays.asList(""));
        result.add(Arrays.asList("()"));

        for (int i = 2; i <= n; i++) {
            List<String> temp = new LinkedList<>();
            for (int j = 0; j < i; j++) {
                List<String> str1 = result.get(j);
                List<String> str2 = result.get(i - 1 - j);
                for (String s1 : str1) {
                    for (String s2 : str2) {
                        String el = "(" + s1 + ")" + s2;
                        temp.add(el);
                    }
                }
            }
            result.add(temp);
        }
        return result.get(n);
    }
}
```

## [647. 回文子串](https://leetcode-cn.com/problems/palindromic-substrings/)(华为题库)

暴力法：

全遍历所有子串，依次判断每个子串是否是回文子串：

```java
package test;

class Solution {  // 102ms
    public int countSubstrings(String s) {
        int count = 0;
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < s.length() - i + 1; j++) {
                if (isPalindrome(s.substring(j, j + i))) {
                    ++count;
                }
            }
        }
        return count;
    }

    private boolean isPalindrome(String str) {
        if (str.length() == 1) {
            return true;
        } else {
            for (int i = 0; i < str.length() / 2; i++) {
                if (str.charAt(i) != str.charAt(str.length() - i - 1)) {
                    return false;
                }
            }
        }
        return true;
    }
}

//
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        assertEquals(3, SOLUTION.countSubstrings("abc"));
    }

    @Test
    public void test2() {
        assertEquals(6, SOLUTION.countSubstrings("aaa"));
    }
}
```

别人的优化算法：

<https://leetcode-cn.com/problems/palindromic-substrings/solution/dong-tai-gui-hua-wu-xing-dai-ma-tong-su-yi-dong-by/>

动态规划：

思路可以参考leetcode第五题

使用动态规划， `dp[i][j]`代表`str[i] - str[j]`是否是回文子串
考虑单字符和双字符的特殊情况
状态转移方程：`dp[i][j] = dp[i+1][j-1] && str[i]==str[j]`

```java
class Solution {  // 7ms
    public int countSubstrings(String s) {
        int res = 0;
        boolean dp[][] = new boolean[s.length()][s.length()];
        for (int j = 0; j < s.length(); j++) {
            for (int i = j; i >= 0; i--) {
                if (s.charAt(i) == s.charAt(j) && ((j - i < 2) || dp[i + 1][j - 1])) {
                    dp[i][j] = true;
                    res++;
                }
            }
        }
        return res;
    }
}
```

中心扩展法：

<https://leetcode-cn.com/problems/palindromic-substrings/solution/czhong-xin-kuo-san-by-haydenmiao/>

```java
class Solution {  // 1ms
    public int countSubstrings(String s) {
        int res = 0;
        //当前位置（i）作为中心位置向两边扩散或（i,i+1）向两边扩散
        for (int i = 0; i < s.length(); i++) {
            res += help(s, i, i);//（i）作为中心位置向两边扩散
            res += help(s, i, i + 1);//（i,i+1）向两边扩散
        }
        return res;
    }

    private int help(String s, int start, int end) {
        int count = 0;
        while (start >= 0 && end < s.length()) {
            if (s.charAt(start) == s.charAt(end)) {
                start--;
                end++;
                count++;
            } else {
                break;
            }
        }
        return count;
    }
}
```

## [64. 最小路径和](https://leetcode-cn.com/problems/minimum-path-sum/)(华为题库)（只看没做）

自己的思路：

深度搜索全遍历，查找每条路径上的数字，求和取最小路径。

别人的算法：

<https://leetcode-cn.com/problems/minimum-path-sum/solution/zui-xiao-lu-jing-he-by-leetcode/>

方法 1： 暴力

暴力就是利用递归，对于每个元素我们考虑两条路径，向右走和向下走，在这两条路径中挑选路径权值和较小的一个。
`cost(i,j)=grid[i][j]+min(cost(i+1,j),cost(i,j+1))`

```java
public class Solution {  // 超时
    public int minPathSum(int[][] grid) {
        return calculate(grid, 0, 0);
    }

    private int calculate(int[][] grid, int i, int j) {
        if (i == grid.length || j == grid[0].length) {  // 超出边界给MAX值，就不会选到这条无效路径
            return Integer.MAX_VALUE;
        }
        if (i == grid.length - 1 && j == grid[0].length - 1) {  // 终点值
            return grid[i][j];
        }
        return grid[i][j] + Math.min(calculate(grid, i + 1, j), calculate(grid, i, j + 1));
    }
}
```

方法 2：二维动态规划

我们新建一个额外的 dpdp 数组，与原矩阵大小相同。在这个矩阵中，dp(i, j)dp(i,j) 表示从坐标 (i, j)(i,j) 到右下角的最小路径权值。我们初始化右下角的 dpdp 值为对应的原矩阵值，然后去填整个矩阵，对于每个元素考虑移动到右边或者下面，因此获得最小路径和我们有如下递推公式：
`dp(i,j)=grid(i,j)+min(dp(i+1,j),dp(i,j+1))`

```java
public class Solution {
    public int minPathSum(int[][] grid) {
        int[][] dp = new int[grid.length][grid[0].length];
        for (int i = grid.length - 1; i >= 0; i--) {
            for (int j = grid[0].length - 1; j >= 0; j--) {
                if (i == grid.length - 1 && j != grid[0].length - 1) {  // i在下边界，j不在右边界
                    dp[i][j] = grid[i][j] + dp[i][j + 1];
                } else if (j == grid[0].length - 1 && i != grid.length - 1) {  // i不在下边界，j在右边界
                    dp[i][j] = grid[i][j] + dp[i + 1][j];
                } else if (j != grid[0].length - 1 && i != grid.length - 1) {  // i、j都不在边界
                    dp[i][j] = grid[i][j] + Math.min(dp[i + 1][j], dp[i][j + 1]);
                } else {  // i、j都在边界，就是最右下角节点
                    dp[i][j] = grid[i][j];
                }
            }
        }
        return dp[0][0];
    }
}
```

自己用C++写了一遍：

```c++
class Solution {  // 12ms
public:
    int minPathSum(vector<vector<int>>& grid)
    {
        if (grid.empty()) {
            return 0;
        }

        int m = grid.size();
        int n = grid.at(0).size();
        vector<vector<int>> dp(m, vector<int>(n, 0));
        for (int i = m - 1; i >= 0; --i) {
            for (int j = n - 1; j >= 0; --j) {
                if (i == m - 1 && j == n - 1) {
                    dp[i][j] = grid[i][j];
                } else if (i == m - 1 && j != n - 1) {
                    dp[i][j] = grid[i][j] + dp[i][j + 1];
                } else if (i != m - 1 && j == n - 1) {
                    dp[i][j] = grid[i][j] + dp[i + 1][j];
                } else {
                    dp[i][j] = grid[i][j] + min(dp[i + 1][j], dp[i][j + 1]);
                }
            }
        }
        return dp[0][0];
    }
};
```

## [225. 用队列实现栈](https://leetcode-cn.com/problems/implement-stack-using-queues/)(华为可信认证2019-12-20 Demo1)

```c++
#include "gtest/gtest.h"
#include <queue>

using namespace std;

class MyStack {
public:
    MyStack() {}

    void push(int x) {
        pushQueue_->push(x);
    }

    int pop() {
        checkAndSwapPushQueue();
        transPushQueueToOneValue();

        int top = pushQueue_->front();
        pushQueue_->pop();
        return top;
    }

    int top() {
        checkAndSwapPushQueue();
        transPushQueueToOneValue();
        return pushQueue_->front();
    }

    bool empty() {
        return pushQueue_->empty() && transQueue_->empty();
    }

private:
    void checkAndSwapPushQueue() {
        if (pushQueue_->empty()) {
            queue<int>* tmpQueue_ = pushQueue_;
            pushQueue_ = transQueue_;
            transQueue_ = tmpQueue_;
        }
    }

    void transPushQueueToOneValue() const {
        while (pushQueue_->size() != 1) {
            int front = pushQueue_->front();
            pushQueue_->pop();
            transQueue_->push(front);
        }
    }

private:
    queue<int> queue1_;
    queue<int> queue2_;
    queue<int>* pushQueue_ = &queue1_;
    queue<int>* transQueue_ = &queue2_;
};

class MyStackTest : public testing::Test {
protected:
    void SetUp() {
//        myStack = MyStack();  // 这种写法的问题在于指针浅拷贝，myStack的成员指针指向了临时变量MyStack()里的指针，所以运行时踩内存了
        myStack = make_unique<MyStack>();
    }

protected:
    unique_ptr<MyStack> myStack;
};

TEST_F(MyStackTest, Test1) {
    myStack->push(10);
    myStack->push(20);

    EXPECT_FALSE(myStack->empty());
    EXPECT_EQ(20, myStack->top());
    EXPECT_EQ(20, myStack->pop());
    EXPECT_EQ(10, myStack->top());
    EXPECT_EQ(10, myStack->pop());
    EXPECT_TRUE(myStack->empty());
}

TEST_F(MyStackTest, Test2) {
    myStack->push(10);
    myStack->push(20);

    EXPECT_FALSE(myStack->empty());
    EXPECT_EQ(20, myStack->top());
    EXPECT_EQ(20, myStack->pop());
    myStack->push(30);
    myStack->push(40);

    EXPECT_EQ(40, myStack->pop());
    EXPECT_EQ(30, myStack->pop());
    EXPECT_EQ(10, myStack->pop());
    EXPECT_TRUE(myStack->empty());
}
```

## [316. 去除重复字母](https://leetcode-cn.com/problems/remove-duplicate-letters/)(华为可信认证2019-12-20 Demo3)

一个输入字符串只有小写字母，请消除重复字符，使得每个字符都唯一，并返回字符串为自然序最小。

参考别人代码：

<https://leetcode-cn.com/problems/remove-duplicate-letters/solution/qu-chu-zhong-fu-zi-mu-by-watson-22/>

```c++
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 8ms
public:
    string removeDuplicateLetters(string s) {
        map<char, int> charCountMap;
        map<char, bool> charAppendFlagMap;
        for_each(s.begin(), s.end(), [&](char c) {
            ++charCountMap[c];
            charAppendFlagMap[c] = false;
        });

        string ret("0");  // 初始化方便处理 否则操作result.back()需要讨论result为空的情形
        for (char c : s) {
            --charCountMap[c];  // 更新剩余子串字符ch出现次数
            if (charAppendFlagMap[c]) {
                continue;
            }

            // 当字符串中字符比当前字符大，并且后面还有该字符时，就可以弹出该字符，后面再添加进来
            while (ret.back() > c && charCountMap[ret.back()] > 0) {
                charAppendFlagMap[ret.back()] = false;
                ret.pop_back();
            }
            ret.push_back(c);  // 贪心算法的体现——尽可能多吃下字符
            charAppendFlagMap[c] = true;
        }
        return ret.substr(1);
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    EXPECT_EQ("abc", solution.removeDuplicateLetters("bcabc"));
}

TEST_F(SolutionTest, Test2) {
    EXPECT_EQ("acdb", solution.removeDuplicateLetters("cbacdcbc"));
}
```

## [714. 买卖股票的最佳时机含手续费](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/)

别人的解答：

<https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/solution/mai-mai-gu-piao-de-zui-jia-shi-ji-han-shou-xu-fei-/>

方法：动态规划
我们维护两个变量cash 和 hold，前者表示当我们不持有股票时的最大利润，后者表示当我们持有股票时的最大利润。

在第 i 天时，我们需要根据第 i - 1天的状态来更新 cash 和hold 的值。对于cash，我们可以保持不变，或者将手上的股票卖出，状态转移方程为

`cash = max(cash, hold + prices[i] - fee)`
对于 hold，我们可以保持不变，或者买入这一天的股票，状态转移方程为

`hold = max(hold, cash - prices[i])`
在计算这两个状态转移方程时，我们可以不使用临时变量来存储第i−1 天cash 和 hold 的值，而是可以先计算 cash 再计算 hold，原因是在同一天卖出再买入（亏了一笔手续费）一定不会比不进行任何操作好。

```c++
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 120ms
public:
    int maxProfit(vector<int>& prices, int fee) {
        int cash = 0, hold = -prices[0];
        for (int i = 1; i < prices.size(); i++) {
            cash = max(cash, hold + prices[i] - fee);
            hold = max(hold, cash - prices[i]);
        }
        return cash;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    vector<int> prices = {1, 3, 2, 8, 4, 9};
    int fee = 2;
    EXPECT_EQ(8, solution.maxProfit(prices, fee));
}

```

### [一个方法团灭 6 道股票问题](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/solution/yi-ge-fang-fa-tuan-mie-6-dao-gu-piao-wen-ti-by-l-2/)

这 6 道股票买卖问题是有共性的，我们通过对第四题（限制最大交易次数为 k）的分析一道一道解决。因为第四题是一个最泛化的形式，其他的问题都是这个形式的简化。

第一题是只进行一次交易，相当于 k = 1；第二题是不限交易次数，相当于 k = +infinity（正无穷）；第三题是只进行 2 次交易，相当于 k = 2；剩下两道也是不限次数，但是加了交易「冷冻期」和「手续费」的额外条件，其实就是第二题的变种，都很容易处理。

一、穷举框架
首先，还是一样的思路：如何穷举？这里的穷举思路和上篇文章递归的思想不太一样。

递归其实是符合我们思考的逻辑的，一步步推进，遇到无法解决的就丢给递归，一不小心就做出来了，可读性还很好。缺点就是一旦出错，你也不容易找到错误出现的原因。比如上篇文章的递归解法，肯定还有计算冗余，但确实不容易找到。

而这里，我们不用递归思想进行穷举，而是利用「状态」进行穷举。我们具体到每一天，看看总共有几种可能的「状态」，再找出每个「状态」对应的「选择」。我们要穷举所有「状态」，穷举的目的是根据对应的「选择」更新状态。听起来抽象，你只要记住「状态」和「选择」两个词就行，下面实操一下就很容易明白了。

```
for 状态1 in 状态1的所有取值：
    for 状态2 in 状态2的所有取值：
        for ...
            dp[状态1][状态2][...] = 择优(选择1，选择2...)
```

比如说这个问题，每天都有三种「选择」：买入、卖出、无操作，我们用 buy, sell, rest 表示这三种选择。但问题是，并不是每天都可以任意选择这三种选择的，因为 sell 必须在 buy 之后，buy 必须在 sell 之后。那么 rest 操作还应该分两种状态，一种是 buy 之后的 rest（持有了股票），一种是 sell 之后的 rest（没有持有股票）。而且别忘了，我们还有交易次数 k 的限制，就是说你 buy 还只能在 k > 0 的前提下操作。

很复杂对吧，不要怕，我们现在的目的只是穷举，你有再多的状态，老夫要做的就是一把梭全部列举出来。这个问题的「状态」有三个，第一个是天数，第二个是允许交易的最大次数，第三个是当前的持有状态（即之前说的 rest 的状态，我们不妨用 1 表示持有，0 表示没有持有）。然后我们用一个三维数组就可以装下这几种状态的全部组合：

```
dp[i][k][0 or 1]
0 <= i <= n-1, 1 <= k <= K
n 为天数，大 K 为最多交易数
此问题共 n × K × 2 种状态，全部穷举就能搞定。

for 0 <= i < n:
    for 1 <= k <= K:
        for s in {0, 1}:
            dp[i][k][s] = max(buy, sell, rest)
```

而且我们可以用自然语言描述出每一个状态的含义，比如说 dp[3][2][1] 的含义就是：今天是第三天，我现在手上持有着股票，至今最多进行 2 次交易。再比如 dp[2][3][0] 的含义：今天是第二天，我现在手上没有持有股票，至今最多进行 3 次交易。很容易理解，对吧？

我们想求的最终答案是 dp[n - 1][K][0]，即最后一天，最多允许 K 次交易，最多获得多少利润。读者可能问为什么不是 dp[n - 1][K][1]？因为 [1] 代表手上还持有股票，[0] 表示手上的股票已经卖出去了，很显然后者得到的利润一定大于前者。

记住如何解释「状态」，一旦你觉得哪里不好理解，把它翻译成自然语言就容易理解了。

二、状态转移框架
现在，我们完成了「状态」的穷举，我们开始思考每种「状态」有哪些「选择」，应该如何更新「状态」。只看「持有状态」，可以画个状态转移图。

通过这个图可以很清楚地看到，每种状态（0 和 1）是如何转移而来的。根据这个图，我们来写一下状态转移方程：

```
dp[i][k][0] = max(dp[i-1][k][0], dp[i-1][k][1] + prices[i])
              max(   选择 rest  ,           选择 sell      )

解释：今天我没有持有股票，有两种可能：
要么是我昨天就没有持有，然后今天选择 rest，所以我今天还是没有持有；
要么是我昨天持有股票，但是今天我 sell 了，所以我今天没有持有股票了。

dp[i][k][1] = max(dp[i-1][k][1], dp[i-1][k-1][0] - prices[i])
              max(   选择 rest  ,           选择 buy         )

解释：今天我持有着股票，有两种可能：
要么我昨天就持有着股票，然后今天选择 rest，所以我今天还持有着股票；
要么我昨天本没有持有，但今天我选择 buy，所以今天我就持有股票了。
```

这个解释应该很清楚了，如果 buy，就要从利润中减去 prices[i]，如果 sell，就要给利润增加 prices[i]。今天的最大利润就是这两种可能选择中较大的那个。而且注意 k 的限制，我们在选择 buy 的时候，把 k 减小了 1，很好理解吧，当然你也可以在 sell 的时候减 1，一样的。

现在，我们已经完成了动态规划中最困难的一步：状态转移方程。如果之前的内容你都可以理解，那么你已经可以秒杀所有问题了，只要套这个框架就行了。不过还差最后一点点，就是定义 base case，即最简单的情况。

```
dp[-1][k][0] = 0
解释：因为 i 是从 0 开始的，所以 i = -1 意味着还没有开始，这时候的利润当然是 0 。
dp[-1][k][1] = -infinity
解释：还没开始的时候，是不可能持有股票的，用负无穷表示这种不可能。
dp[i][0][0] = 0
解释：因为 k 是从 1 开始的，所以 k = 0 意味着根本不允许交易，这时候利润当然是 0 。
dp[i][0][1] = -infinity
解释：不允许交易的情况下，是不可能持有股票的，用负无穷表示这种不可能。
```

把上面的状态转移方程总结一下：

```
base case：
dp[-1][k][0] = dp[i][0][0] = 0
dp[-1][k][1] = dp[i][0][1] = -infinity

状态转移方程：
dp[i][k][0] = max(dp[i-1][k][0], dp[i-1][k][1] + prices[i])
dp[i][k][1] = max(dp[i-1][k][1], dp[i-1][k-1][0] - prices[i])
```

读者可能会问，这个数组索引是 -1 怎么编程表示出来呢，负无穷怎么表示呢？这都是细节问题，有很多方法实现。现在完整的框架已经完成，下面开始具体化。

三、秒杀题目
**第一题，k = 1**

#### [121. 买卖股票的最佳时机](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock/)

直接套状态转移方程，根据 base case，可以做一些化简：

```c++
dp[i][1][0] = max(dp[i-1][1][0], dp[i-1][1][1] + prices[i])
dp[i][1][1] = max(dp[i-1][1][1], dp[i-1][0][0] - prices[i]) 
            = max(dp[i-1][1][1], -prices[i])
解释：k = 0 的 base case，所以 dp[i-1][0][0] = 0。

现在发现 k 都是 1，不会改变，即 k 对状态转移已经没有影响了。
可以进行进一步化简去掉所有 k：
dp[i][0] = max(dp[i-1][0], dp[i-1][1] + prices[i])
dp[i][1] = max(dp[i-1][1], -prices[i])
```

直接写出代码：

```c++
int n = prices.length;
int[][] dp = new int[n][2];
for (int i = 0; i < n; i++) {
    dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i]);
    dp[i][1] = Math.max(dp[i-1][1], -prices[i]);
}
return dp[n - 1][0];
```

显然` i = 0 时 dp[i-1]`是不合法的。这是因为我们没有对 i 的 base case 进行处理。可以这样处理：

```java
for (int i = 0; i < n; i++) {
    if (i - 1 == -1) {
        dp[i][0] = 0;
        // 解释：
        //   dp[i][0] 
        // = max(dp[-1][0], dp[-1][1] + prices[i])
        // = max(0, -infinity + prices[i]) = 0
        dp[i][1] = -prices[i];
        //解释：
        //   dp[i][1] 
        // = max(dp[-1][1], dp[-1][0] - prices[i])
        // = max(-infinity, 0 - prices[i]) 
        // = -prices[i]
        continue;
    }
    dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i]);
    dp[i][1] = Math.max(dp[i-1][1], -prices[i]);
}
return dp[n - 1][0];
```

第一题就解决了，但是这样处理 base case 很麻烦，而且注意一下状态转移方程，新状态只和相邻的一个状态有关，其实不用整个 dp 数组，只需要一个变量储存相邻的那个状态就足够了，这样可以把空间复杂度降到 O(1):

```java
// k == 1
int maxProfit_k_1(int[] prices) {
    int n = prices.length;
    // base case: dp[-1][0] = 0, dp[-1][1] = -infinity
    int dp_i_0 = 0, dp_i_1 = Integer.MIN_VALUE;
    for (int i = 0; i < n; i++) {
        // dp[i][0] = max(dp[i-1][0], dp[i-1][1] + prices[i])
        dp_i_0 = Math.max(dp_i_0, dp_i_1 + prices[i]);
        // dp[i][1] = max(dp[i-1][1], -prices[i])
        dp_i_1 = Math.max(dp_i_1, -prices[i]);
    }
    return dp_i_0;
}
```

两种方式都是一样的，不过这种编程方法简洁很多。但是如果没有前面状态转移方程的引导，是肯定看不懂的。后续的题目，我主要写这种空间复杂度 O(1) 的解法。

自己写的C++第1个版本：

```c++
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 16ms
public:
    int maxProfit(vector<int>& prices) {
        if (prices.empty()) {
            return 0;
        }

        size_t n = prices.size();
        vector<vector<int>> dp(n, vector<int>(2, 0));
        for (int i = 0; i < n; ++i) {
            if (i == 0) {
                dp[0][0] = 0;  // 第1天未持有股票，则收益为0
                dp[0][1] = -prices[0];  // 第1天持有股票，则收益为当天购买股票的价格
                continue;
            }
            dp[i][0] = max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            dp[i][1] = max(dp[i - 1][1], -prices[i]);
        }
        return dp[n - 1][0];
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    vector<int> prices = {1, 3, 2, 8, 4, 9};
    EXPECT_EQ(8, solution.maxProfit(prices));
}

TEST_F(SolutionTest, Test2) {
    vector<int> prices = {7, 1, 5, 3, 6, 4};
    EXPECT_EQ(5, solution.maxProfit(prices));
}

TEST_F(SolutionTest, Test3) {
    vector<int> prices = {7, 6, 4, 3, 1};
    EXPECT_EQ(0, solution.maxProfit(prices));
}

TEST_F(SolutionTest, Test4) {
    vector<int> prices = {};
    EXPECT_EQ(0, solution.maxProfit(prices));
}

TEST_F(SolutionTest, Test5) {
    vector<int> prices = {10};
    EXPECT_EQ(0, solution.maxProfit(prices));
}
```

只用两个变量存储：

```c++
class Solution {  // 4ms
public:
    int maxProfit(vector<int>& prices) {
        size_t n = prices.size();
        int dpINotHold = 0, dpIHold = numeric_limits<int>::min();
        for (int i = 0; i < n; ++i) {
            dpINotHold = max(dpINotHold, dpIHold + prices[i]);
            dpIHold = max(dpIHold, -prices[i]);
        }
        return dpINotHold;
    }
};
```

**第二题，k = +infinity**

#### [122. 买卖股票的最佳时机 II](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii/)

如果 k 为正无穷，那么就可以认为 k 和 k - 1 是一样的。可以这样改写框架：

```java
dp[i][k][0] = max(dp[i-1][k][0], dp[i-1][k][1] + prices[i])
dp[i][k][1] = max(dp[i-1][k][1], dp[i-1][k-1][0] - prices[i])
            = max(dp[i-1][k][1], dp[i-1][k][0] - prices[i])

我们发现数组中的 k 已经不会改变了，也就是说不需要记录 k 这个状态了：
dp[i][0] = max(dp[i-1][0], dp[i-1][1] + prices[i])
dp[i][1] = max(dp[i-1][1], dp[i-1][0] - prices[i])
```

直接翻译成代码：

```java
int maxProfit_k_inf(int[] prices) {
    int n = prices.length;
    int dp_i_0 = 0, dp_i_1 = Integer.MIN_VALUE;
    for (int i = 0; i < n; i++) {
        int temp = dp_i_0;
        dp_i_0 = Math.max(dp_i_0, dp_i_1 + prices[i]);
        dp_i_1 = Math.max(dp_i_1, temp - prices[i]);
    }
    return dp_i_0;
}
```

使用C++编写：

```c++
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 4ms
public:
    int maxProfit(vector<int>& prices) {
        size_t n = prices.size();
        int dpINotHold = 0, dpIHold = numeric_limits<int>::min();
        for (int i = 0; i < n; ++i) {
            int tempDpINotHold = dpINotHold;
            dpINotHold = max(dpINotHold, dpIHold + prices[i]);
            dpIHold = max(dpIHold, tempDpINotHold - prices[i]);
        }
        return dpINotHold;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    vector<int> prices = {7, 1, 5, 3, 6, 4};
    EXPECT_EQ(7, solution.maxProfit(prices));
}

TEST_F(SolutionTest, Test2) {
    vector<int> prices = {1, 2, 3, 4, 5};
    EXPECT_EQ(4, solution.maxProfit(prices));
}

TEST_F(SolutionTest, Test3) {
    vector<int> prices = {7, 6, 4, 3, 1};
    EXPECT_EQ(0, solution.maxProfit(prices));
}

TEST_F(SolutionTest, Test4) {
    vector<int> prices = {};
    EXPECT_EQ(0, solution.maxProfit(prices));
}

TEST_F(SolutionTest, Test5) {
    vector<int> prices = {10};
    EXPECT_EQ(0, solution.maxProfit(prices));
}
```

**第三题，k = +infinity with cooldown**

#### [309. 最佳买卖股票时机含冷冻期](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/)

每次 sell 之后要等一天才能继续交易。只要把这个特点融入上一题的状态转移方程即可：

```
dp[i][0] = max(dp[i-1][0], dp[i-1][1] + prices[i])
dp[i][1] = max(dp[i-1][1], dp[i-2][0] - prices[i])
解释：第 i 天选择 buy 的时候，要从 i-2 的状态转移，而不是 i-1 。
```

翻译成代码：

```java
int maxProfit_with_cool(int[] prices) {
    int n = prices.length;
    int dp_i_0 = 0, dp_i_1 = Integer.MIN_VALUE;
    int dp_pre_0 = 0; // 代表 dp[i-2][0]
    for (int i = 0; i < n; i++) {
        int temp = dp_i_0;
        dp_i_0 = Math.max(dp_i_0, dp_i_1 + prices[i]);
        dp_i_1 = Math.max(dp_i_1, dp_pre_0 - prices[i]);
        dp_pre_0 = temp;
    }
    return dp_i_0;
}
```

使用C++编写：

```c++
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 8ms
public:
    int maxProfit(vector<int>& prices) {
        size_t n = prices.size();
        int dpINotHold = 0, dpIHold = numeric_limits<int>::min();
        int dpIPrevNotHold = 0;
        for (int i = 0; i < n; ++i) {
            int tempDpINotHold = dpINotHold;
            dpINotHold = max(dpINotHold, dpIHold + prices[i]);
            dpIHold = max(dpIHold, dpIPrevNotHold - prices[i]);
            dpIPrevNotHold = tempDpINotHold;
        }
        return dpINotHold;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    vector<int> prices = {1, 2, 3, 0, 2};
    EXPECT_EQ(3, solution.maxProfit(prices));
}
```

**第四题，k = +infinity with fee**

#### [714. 买卖股票的最佳时机含手续费](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/)

每次交易要支付手续费，只要把手续费从利润中减去即可。改写方程：

```
dp[i][0] = max(dp[i-1][0], dp[i-1][1] + prices[i])
dp[i][1] = max(dp[i-1][1], dp[i-1][0] - prices[i] - fee)
解释：相当于买入股票的价格升高了。
在第一个式子里减也是一样的，相当于卖出股票的价格减小了。
```

直接翻译成代码：

```java
int maxProfit_with_fee(int[] prices, int fee) {
    int n = prices.length;
    int dp_i_0 = 0, dp_i_1 = Integer.MIN_VALUE;
    for (int i = 0; i < n; i++) {
        int temp = dp_i_0;
        dp_i_0 = Math.max(dp_i_0, dp_i_1 + prices[i]);
        dp_i_1 = Math.max(dp_i_1, temp - prices[i] - fee);
    }
    return dp_i_0;
}
```

使用C++编写：

```c++
class Solution {  // 120ms
public:
    int maxProfit(vector<int>& prices, int fee) {
        size_t n = prices.size();
        int dpINotHold = 0, dpIHold = numeric_limits<int>::min();
        for (int i = 0; i < n; ++i) {
            int tempDpINotHold = dpINotHold;
            dpINotHold = max(dpINotHold, dpIHold + prices[i]);
            dpIHold = max(dpIHold, tempDpINotHold - prices[i] - fee);
        }
        return dpINotHold;
    }
};
```

**第五题，k = 2**

#### [123. 买卖股票的最佳时机 III](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iii/)

k = 2 和前面题目的情况稍微不同，因为上面的情况都和 k 的关系不太大。要么 k 是正无穷，状态转移和 k 没关系了；要么 k = 1，跟 k = 0 这个 base case 挨得近，最后也没有存在感。

这道题 k = 2 和后面要讲的 k 是任意正整数的情况中，对 k 的处理就凸显出来了。我们直接写代码，边写边分析原因。

```
原始的动态转移方程，没有可化简的地方
dp[i][k][0] = max(dp[i-1][k][0], dp[i-1][k][1] + prices[i])
dp[i][k][1] = max(dp[i-1][k][1], dp[i-1][k-1][0] - prices[i])
```

按照之前的代码，我们可能想当然这样写代码（错误的）

```java
int k = 2;
int[][][] dp = new int[n][k + 1][2];
for (int i = 0; i < n; i++)
    if (i - 1 == -1) { /* 处理一下 base case*/ }
    dp[i][k][0] = Math.max(dp[i-1][k][0], dp[i-1][k][1] + prices[i]);
    dp[i][k][1] = Math.max(dp[i-1][k][1], dp[i-1][k-1][0] - prices[i]);
}
return dp[n - 1][k][0];
```


为什么错误？我这不是照着状态转移方程写的吗？

还记得前面总结的「穷举框架」吗？就是说我们必须穷举所有状态。其实我们之前的解法，都在穷举所有状态，只是之前的题目中 k 都被化简掉了。这道题由于没有消掉 k 的影响，所以必须要对 k 进行穷举：

```java
int max_k = 2;
int[][][] dp = new int[n][max_k + 1][2];
for (int i = 0; i < n; i++) {
    for (int k = max_k; k >= 1; k--) {
        if (i - 1 == -1) { /*处理 base case */ }
        dp[i][k][0] = max(dp[i-1][k][0], dp[i-1][k][1] + prices[i]);
        dp[i][k][1] = max(dp[i-1][k][1], dp[i-1][k-1][0] - prices[i]);
    }
}
// 穷举了 n × max_k × 2 个状态，正确。
return dp[n - 1][max_k][0];
```

如果你不理解，可以返回第一点「穷举框架」重新阅读体会一下。

这里 k 取值范围比较小，所以可以不用 for 循环，直接把 k = 1 和 2 的情况手动列举出来也可以：

```java
dp[i][2][0] = max(dp[i-1][2][0], dp[i-1][2][1] + prices[i])
dp[i][2][1] = max(dp[i-1][2][1], dp[i-1][1][0] - prices[i])
dp[i][1][0] = max(dp[i-1][1][0], dp[i-1][1][1] + prices[i])
dp[i][1][1] = max(dp[i-1][1][1], -prices[i])

int maxProfit_k_2(int[] prices) {
    int dp_i10 = 0, dp_i11 = Integer.MIN_VALUE;
    int dp_i20 = 0, dp_i21 = Integer.MIN_VALUE;
    for (int price : prices) {
        dp_i20 = Math.max(dp_i20, dp_i21 + price);
        dp_i21 = Math.max(dp_i21, dp_i10 - price);
        dp_i10 = Math.max(dp_i10, dp_i11 + price);
        dp_i11 = Math.max(dp_i11, -price);
    }
    return dp_i20;
}
```

有状态转移方程和含义明确的变量名指导，相信你很容易看懂。其实我们可以故弄玄虚，把上述四个变量换成 a, b, c, d。这样当别人看到你的代码时就会一头雾水，大惊失色，不得不对你肃然起敬。

自己写的C++第1个双循环方法：

```c++
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 32ms
public:
    int maxProfit(vector<int>& prices) {
        if (prices.empty()) {
            return 0;
        }

        size_t n = prices.size();
        int maxK = 2;
        vector<vector<vector<int>>> dp(n, vector<vector<int>>(maxK + 1, vector<int>(2, 0)));
        for (int i = 0; i < n; ++i) {
            for (int k = maxK; k >= 1; --k) {  // 这里k从maxK递减到1，还是1递增到maxK，效果都一样，因为递推式的第1个维度i不同，不会出现相互依赖的情况
                if (i == 0) {
                    dp[0][k][0] = 0;
                    dp[0][k][1] = -prices[0];
                    continue;
                }
                dp[i][k][0] = max(dp[i - 1][k][0], dp[i - 1][k][1] + prices[i]);
                dp[i][k][1] = max(dp[i - 1][k][1], dp[i - 1][k - 1][0] - prices[i]);
            }
        }
        return dp[n - 1][maxK][0];
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    vector<int> prices = {3, 3, 5, 0, 0, 3, 1, 4};
    EXPECT_EQ(6, solution.maxProfit(prices));
}

TEST_F(SolutionTest, Test2) {
    vector<int> prices = {1, 2, 3, 4, 5};
    EXPECT_EQ(4, solution.maxProfit(prices));
}

TEST_F(SolutionTest, Test3) {
    vector<int> prices = {7, 6, 4, 3, 1};
    EXPECT_EQ(0, solution.maxProfit(prices));
}
```

自己写的C++第2个简化方法：

```c++
class Solution {  // 8ms
public:
    int maxProfit(vector<int>& prices) {
        int dp_i10 = 0, dp_i11 = numeric_limits<int>::min();
        int dp_i20 = 0, dp_i21 = numeric_limits<int>::min();
        for (int price : prices) {
            dp_i20 = max(dp_i20, dp_i21 + price);
            dp_i21 = max(dp_i21, dp_i10 - price);
            dp_i10 = max(dp_i10, dp_i11 + price);
            dp_i11 = max(dp_i11, -price);
        }
        return dp_i20;
    }
};
```

第六题，k = any integer

#### [188. 买卖股票的最佳时机 IV](https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iv/)

有了上一题 k = 2 的铺垫，这题应该和上一题的第一个解法没啥区别。但是出现了一个超内存的错误，原来是传入的 k 值会非常大，dp 数组太大了。现在想想，交易次数 k 最多有多大呢？

一次交易由买入和卖出构成，至少需要两天。所以说有效的限制 k 应该不超过 n/2，如果超过，就没有约束作用了，相当于 k = +infinity。这种情况是之前解决过的。

直接把之前的代码重用：

```java
int maxProfit_k_any(int max_k, int[] prices) {
    int n = prices.length;
    if (max_k > n / 2) 
        return maxProfit_k_inf(prices);

    int[][][] dp = new int[n][max_k + 1][2];
    for (int i = 0; i < n; i++) 
        for (int k = max_k; k >= 1; k--) {
            if (i - 1 == -1) { /* 处理 base case */ }
            dp[i][k][0] = max(dp[i-1][k][0], dp[i-1][k][1] + prices[i]);
            dp[i][k][1] = max(dp[i-1][k][1], dp[i-1][k-1][0] - prices[i]);     
        }
    return dp[n - 1][max_k][0];
}
```

至此，6 道题目通过一个状态转移方程全部解决。

自己写的C++代码：

```c++
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 40ms
public:
    int maxProfit(int maxK, vector<int>& prices) {
        if (prices.empty()) {
            return 0;
        }

        size_t n = prices.size();
        if (maxK > n / 2) {
            return maxProfitKInfinity(prices);
        }

        vector<vector<vector<int>>> dp(n, vector<vector<int>>(maxK + 1, vector<int>(2, 0)));
        for (int i = 0; i < n; ++i) {
            for (int k = maxK; k >= 1; --k) {  // 这里k从maxK递减到1，还是1递增到maxK，效果都一样，因为递推式的第1个维度i不同，不会出现相互依赖的情况
                if (i == 0) {
                    dp[0][k][0] = 0;
                    dp[0][k][1] = -prices[0];
                    continue;
                }
                dp[i][k][0] = max(dp[i - 1][k][0], dp[i - 1][k][1] + prices[i]);
                dp[i][k][1] = max(dp[i - 1][k][1], dp[i - 1][k - 1][0] - prices[i]);
            }
        }
        return dp[n - 1][maxK][0];
    }

private:
    int maxProfitKInfinity(const vector<int>& prices) {
        size_t n = prices.size();
        int dpINotHold = 0, dpIHold = numeric_limits<int>::min();
        for (int i = 0; i < n; ++i) {
            int tempDpINotHold = dpINotHold;
            dpINotHold = max(dpINotHold, dpIHold + prices[i]);
            dpIHold = max(dpIHold, tempDpINotHold - prices[i]);
        }
        return dpINotHold;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    vector<int> prices = {2, 4, 1};
    int k = 2;
    EXPECT_EQ(2, solution.maxProfit(k, prices));
}

TEST_F(SolutionTest, Test2) {
    vector<int> prices = {3, 2, 6, 5, 0, 3};
    int k = 2;
    EXPECT_EQ(7, solution.maxProfit(k, prices));
}
```



本文给大家讲了如何通过状态转移的方法解决复杂的问题，用一个状态转移方程秒杀了 6 道股票买卖问题，现在想想，其实也不算难对吧？这已经属于动态规划问题中较困难的了。

关键就在于列举出所有可能的「状态」，然后想想怎么穷举更新这些「状态」。一般用一个多维 dp 数组储存这些状态，从 base case 开始向后推进，推进到最后的状态，就是我们想要的答案。想想这个过程，你是不是有点理解「动态规划」这个名词的意义了呢？

具体到股票买卖问题，我们发现了三个状态，使用了一个三维数组，无非还是穷举 + 更新，不过我们可以说的高大上一点，这叫「三维 DP」，怕不怕？这个大实话一说，立刻显得你高人一等，名利双收有没有。

所以，大家不要被各种高大上的名词吓到，再多的困难问题，奇技淫巧，也不过是基本套路的不断升级组合产生的。只要把住算法的底层原理，即可举一反三，逐个击破。

## [84. 柱状图中最大的矩形](https://leetcode-cn.com/problems/largest-rectangle-in-histogram/)

https://leetcode-cn.com/problems/largest-rectangle-in-histogram/solution/bao-li-jie-fa-zhan-by-liweiwei1419/ 可以看最后的单调栈总结

1.暴力解法

遍历以每个格子为高的矩形，找最大矩形：

```c++
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 超时
public:
    int largestRectangleArea(vector<int>& heights) {
        auto area = 0;
        for (int index = 0; index < heights.size(); ++index) {
            auto height = heights.at(index);

            auto leftIndex = index;
            while (leftIndex > 0 && heights.at(leftIndex - 1) >= height) {
                --leftIndex;
            }

            auto rightIndex = index;
            while (rightIndex < heights.size() - 1 && heights.at(rightIndex + 1) >= height) {
                ++rightIndex;
            }

            auto tmpArea = height * (rightIndex - leftIndex + 1);
            area = max(area, tmpArea);
        }
        return area;
    }
};

class SolutionTest : public testing::Test {
protected:
    static Solution solution;
};

Solution SolutionTest::solution;

TEST_F(SolutionTest, Test1) {
    vector<int> heights = {2, 1, 5, 6, 2, 3};
    EXPECT_EQ(10, solution.largestRectangleArea(heights));
}
```

2.单调栈+哨兵

```c++
class Solution {  // 12ms
public:
    int largestRectangleArea(vector<int>& heights) {
        auto area = 0;
        heights.insert(heights.begin(), 0);
        heights.push_back(0);

        stack<int> stack;
        stack.push(0);
        for (int index = 1; index < heights.size(); ++index) {
            while (heights.at(index) < heights.at(stack.top())) {
                auto curIndex = stack.top();
                stack.pop();
                auto curHeight = heights.at(curIndex);
                auto curWidth = index - stack.top() - 1;
                area = max(area, curWidth * curHeight);
            }
            stack.push(index);
        }
        return area;
    }
};
```

## [136. 只出现一次的数字](https://leetcode-cn.com/problems/single-number/)

1. 使用HashMap遍历一遍存储每个数字的个数，再从HashMap里找一遍哪个数字个数为1，时间和空间复杂度都为O(N)：

```c++
#include <unordered_map>
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 80 ms/20 MB
public:
    int singleNumber(vector<int>& nums) {
        unordered_map<int, int> numCountMap;
        for_each(nums.begin(), nums.end(), [&](auto num) { ++numCountMap[num]; });
        auto iter = find_if(numCountMap.begin(), numCountMap.end(),
                            [](const auto& numCountPair) { return numCountPair.second == 1; });
        return iter->first;
    }
};

class SolutionTest : public testing::Test {
protected:
    static Solution solution;
};

Solution SolutionTest::solution;

TEST_F(SolutionTest, Test1) {
    vector<int> nums = {2, 2, 1};
    EXPECT_EQ(1, solution.singleNumber(nums));
}

TEST_F(SolutionTest, Test2) {
    vector<int> nums = {4, 1, 2, 1, 2};
    EXPECT_EQ(4, solution.singleNumber(nums));
}

```

2. 使用HashSet存储数字，发现已经存在，就删除掉，不存在就插入，最后留下的就是只出现一次的数字，时间和空间复杂度都为O(N)：

```c++
class Solution {  // 80 ms/19.8 MB
public:
    int singleNumber(vector<int>& nums) {
        unordered_set<int> numSet;
        for_each(nums.begin(), nums.end(), [&](auto num) {
            if (numSet.find(num) != numSet.end()) {
                numSet.erase(num);
            } else {
                numSet.insert(num);
            }
        });
        
        return *numSet.begin();
    }
};
```

3. 利用异或(XOR)的特性，使用线性时间：O(N)，不使用额外空间：O(1)：

   1. 0 ⊕ 0 = 0， 0 ⊕ 1 = 1
   2. 1 ⊕ 0 = 1，1 ⊕ 1 = 0
   3. 满足交换律和结合律：a ⊕ b ⊕ a = (a ⊕ a) ⊕ b = 0 ⊕ b = b

   总结下来就是：相同的数字⊕为0，不同的数字⊕为1

   所以利用交换律和结合律：`num[0] ⊕ num[1] ⊕ ... num[n-1] = 所用重复数字异 ⊕ 唯一数字 = 0 ⊕ 唯一数字 = 唯一数字`

```c++
class Solution {  // 44 ms/16.7 MB
public:
    int singleNumber(vector<int>& nums) {
        int num = nums.at(0);
        for (int i = 1; i < nums.size(); ++i) {
            num ^= nums.at(i);
        }
        return num;
    }
};
```

## [409. 最长回文串](https://leetcode-cn.com/problems/longest-palindrome/)

给定一个包含大写字母和小写字母的字符串，找到通过这些字母构造成的最长的回文串。

在构造过程中，请注意区分大小写。比如 `"Aa"` 不能当做一个回文字符串。

```python
class Solution:  # 60 ms
    def longestPalindrome(self, s: str) -> int:
        char_count_dict = dict()
        for c in s:
            char_count_dict[c] = char_count_dict.get(c, 0) + 1

        length = 0;
        has_single = False
        has_single_count_equals_one = False
        for c, count in char_count_dict.items():
            if count % 2 == 0:
                length += count
            else:
                has_single = True
                if count > 1:
                    length += count - 1
                else:
                    has_single_count_equals_one = True

        if has_single or has_single_count_equals_one:
            length += 1
        return length
    
class Solution:  # 48 ms
    def longestPalindrome(self, s: str) -> int:
        char_count_dict = Counter(s)
        length = 0;
        for count in char_count_dict.values():
            length += count // 2 * 2
            if length % 2 == 0 and count % 2 == 1:  # 长度是偶数并且某个字母又是奇数的时，才需要把长度加1
                length += 1
        return length
```

## [257. 二叉树的所有路径](https://leetcode-cn.com/problems/binary-tree-paths/)

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import unittest
from typing import List


class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None


class Solution:  # 48 ms
    def binaryTreePaths(self, root: TreeNode) -> List[str]:
        paths = []
        self._search_tree_paths(root, '', paths)
        return paths

    def _search_tree_paths(self, root, prev_path: str, paths):
        if not root:
            return

        current_path = prev_path + ('->' if prev_path else '') + str(root.val)
        if not root.left and not root.right:
            paths.append(current_path)
        else:
            self._search_tree_paths(root.left, current_path, paths)
            self._search_tree_paths(root.right, current_path, paths)


class TestSolution(unittest.TestCase):
    def test_solution(self):
        root_1 = TreeNode(1)
        node_2 = TreeNode(2)
        node_3 = TreeNode(3)
        node_5 = TreeNode(5)
        root_1.left = node_2
        root_1.right = node_3
        node_2.right = node_5

        solution = Solution()
        expect_list = ["1->2->5", "1->3"]
        self.assertEqual(expect_list, solution.binaryTreePaths(root_1))
```

## [202. 快乐数](https://leetcode-cn.com/problems/happy-number/)

<https://leetcode-cn.com/problems/happy-number/solution/kuai-le-shu-by-leetcode-solution/>

解法1：使用set保存遍历过数字，时间和空间复杂度均为O(logn)。

```c++
#include <unordered_set>
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 4 ms
public:
    bool isHappy(int n) {
        int next = n;
        unordered_set<long> nexts;
        while (next != 1) {
            next = getNext(next);
            if (nexts.find(next) != nexts.end()) {
                return false;
            }
            nexts.insert(next);
        }
        return true;
    }

private:
    int getNext(int num) {
        int sum = 0;
        while (num > 0) {
            int remainder = num % 10;
            sum += remainder * remainder;
            num = num / 10;
        }
        return sum;
    }
};

class SolutionTest : public testing::Test {
protected:
    static Solution solution;
};

Solution SolutionTest::solution;

TEST_F(SolutionTest, Test1) {
    EXPECT_EQ(true, solution.isHappy(19));
}

TEST_F(SolutionTest, Test2) {
    EXPECT_EQ(true, solution.isHappy(1));
}

TEST_F(SolutionTest, Test3) {
    EXPECT_EQ(false, solution.isHappy(2));
}
```

解法2：使用快速指针，如果不是快乐数，则一定会出现循环并且快指针和慢指针数字重和时不等于1；如果是快乐数，则重和时等于1。

```c++
class Solution {  // 0 ms
public:
    bool isHappy(int n) {
        int slow = getNext(n), fast = getNext(getNext(n));
        while (slow != fast) {
            slow = getNext(slow);
            fast = getNext(getNext(fast));
        }
        return slow == 1;
    }

private:
    int getNext(int num) {
        int sum = 0;
        while (num > 0) {
            int remainder = num % 10;
            sum += remainder * remainder;
            num = num / 10;
        }
        return sum;
    }
};

```

解法3：数学法

```c++
class Solution {  // 4 ms
public:
    bool isHappy(int n) {
        unordered_set<int> cycles = {4, 16, 37, 58, 89, 145, 42, 20};
        int next = n;
        while (next != 1 && cycles.find(next) == cycles.end()) {
            next = getNext(next);
        }
        return next == 1;
    }

private:
    int getNext(int num) {
        int sum = 0;
        while (num > 0) {
            int remainder = num % 10;
            sum += remainder * remainder;
            num = num / 10;
        }
        return sum;
    }
};
```

## [970. 强整数](https://leetcode-cn.com/problems/powerful-integers/)

```cpp
#include <unordered_set>
#include <cmath>
#include <algorithm>
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 0ms, 6.7MB
public:
    vector<int> powerfulIntegers(int x, int y, int bound) {
        std::unordered_set<int> ret;
        int iMax = getMaxPower(bound, x);
        int jMax = getMaxPower(bound, y);
        for (int i = 0; i <= iMax; ++i) {
            for (int j = 0; j <= jMax; ++j) {
                int powerfulInteger = pow(x, i) + pow(y, j);
                if (powerfulInteger <= bound) {
                    ret.insert(powerfulInteger);
                } else {
                    break;
                }
            }
        }
        std::vector<int> retList;
        std::copy(ret.begin(), ret.end(), std::back_inserter(retList));
        return retList;
    }

private:
    int getMaxPower(int bound, int inputNumber) {
        if (bound == 0) {
            return 0;
        } else if (inputNumber == 1) {
            return 1;
        }
        return log(bound) / log(inputNumber);
    }
};

class SolutionTest : public testing::Test {
protected:
    void executeEquation(const std::vector<int>& expected, std::vector<int>&& ret) {
        std::sort(ret.begin(), ret.end());
        EXPECT_EQ(expected, ret);
    }
protected:
    static Solution solution;
};

Solution SolutionTest::solution;

TEST_F(SolutionTest, Test1) {
    const vector<int>& expected = std::vector<int>({2, 3, 4, 5, 7, 9, 10});
    vector<int>&& ret = solution.powerfulIntegers(2, 3, 10);
    executeEquation(expected, std::move(ret));
}

TEST_F(SolutionTest, Test2) {
    const vector<int>& expected = std::vector<int>({2, 4, 6, 8, 10, 14});
    vector<int>&& ret = solution.powerfulIntegers(3, 5, 15);
    executeEquation(expected, std::move(ret));
}

TEST_F(SolutionTest, Test3) {
    const vector<int>& expected = std::vector<int>({2, 3, 5, 9});
    vector<int>&& ret = solution.powerfulIntegers(2, 1, 10);
    executeEquation(expected, std::move(ret));
}

TEST_F(SolutionTest, Test4) {
    const vector<int>& expected = std::vector<int>();
    vector<int>&& ret = solution.powerfulIntegers(2, 3, 0);
    executeEquation(expected, std::move(ret));
}
```

## [946. 验证栈序列](https://leetcode-cn.com/problems/validate-stack-sequences/)

把每个要pop的值判断是否已经压过栈，如果压过栈并且是栈顶值就pop出来，如果压过栈不是栈顶值，则顺序不对。如果同有压过栈就压栈直到该值。

```cpp
#include <vector>
#include <unordered_set>
#include <stack>
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 12ms, 16.1MB
public:
    bool validateStackSequences(vector<int>& pushed, vector<int>& popped) {
        std::stack<int> pushedStack;
        std::unordered_set<int> alreadyPushed;
        int indexToPush = 0;
        for (const auto& poppedNum : popped) {
            bool isNumAlreadyPushed = alreadyPushed.find(poppedNum) != alreadyPushed.end();
            if (isNumAlreadyPushed) {
                if (pushedStack.top() != poppedNum) {
                    return false;
                }
            } else {
                pushNum(pushed, pushedStack, alreadyPushed, indexToPush);
                while (pushedStack.top() != poppedNum) {
                    pushNum(pushed, pushedStack, alreadyPushed, indexToPush);
                }
            }
            pushedStack.pop();
        }
        return true;
    }

private:
    void pushNum(const vector<int>& pushed, std::stack<int>& pushedStack, std::unordered_set<int>& alreadyPushed,
                 int& indexToPush) {
        const auto& numToPush = pushed.at(indexToPush++);
        pushedStack.push(numToPush);
        alreadyPushed.insert(numToPush);
    }
};

class SolutionTest : public testing::Test {
protected:
    static Solution solution;
};

Solution SolutionTest::solution;

TEST_F(SolutionTest, Test1) {
    vector<int> pushed = std::vector<int>({1, 2, 3, 4, 5});
    vector<int> popped = std::vector<int>({4, 5, 3, 2, 1});
    EXPECT_TRUE(solution.validateStackSequences(pushed, popped));
}

TEST_F(SolutionTest, Test2) {
    vector<int> pushed = std::vector<int>({1, 2, 3, 4, 5});
    vector<int> popped = std::vector<int>({4, 3, 5, 1, 2});
    EXPECT_FALSE(solution.validateStackSequences(pushed, popped));
}

TEST_F(SolutionTest, Test3) {
    vector<int> pushed = std::vector<int>({1, 2, 3, 4, 5});
    vector<int> popped = std::vector<int>({1, 2, 3, 4, 5});
    EXPECT_TRUE(solution.validateStackSequences(pushed, popped));
}

TEST_F(SolutionTest, Test4) {
    vector<int> pushed = std::vector<int>({1, 2, 3, 4, 5});
    vector<int> popped = std::vector<int>({5, 4, 3, 2, 1});
    EXPECT_TRUE(solution.validateStackSequences(pushed, popped));
}
```

[官方解答](https://leetcode-cn.com/problems/validate-stack-sequences/solution/yan-zheng-zhan-xu-lie-by-leetcode/)：

方法一： 贪心
思路

所有的元素一定是按顺序 push 进去的，重要的是怎么 pop 出来？

假设当前栈顶元素值为 2，同时对应的 popped 序列中下一个要 pop 的值也为 2，那就必须立刻把这个值 pop 出来。因为之后的 push 都会让栈顶元素变成不同于 2 的其他值，这样再 pop 出来的数 popped 序列就不对应了。

算法

将 pushed 队列中的每个数都 push 到栈中，同时检查这个数是不是 popped 序列中下一个要 pop 的值，如果是就把它 pop 出来。

最后，检查不是所有的该 pop 出来的值都是 pop 出来了。

```cpp
// 执行用时：4 ms, 在所有 C++ 提交中击败了98.73%的用户
// 内存消耗：15.1 MB, 在所有 C++ 提交中击败了6.64%的用户
class Solution {
public:
    bool validateStackSequences(vector<int>& pushed, vector<int>& popped) {
        std::stack<int> pushedStack;
        int length = pushed.size();
        int indexToPop = 0;
        for (const auto& numToPush : pushed) {
            pushedStack.push(numToPush);
            while (!pushedStack.empty() && indexToPop < length && pushedStack.top() == popped.at(indexToPop)) {
                pushedStack.pop();
                ++indexToPop;
            }
        }
        return indexToPop == length;
    }
};
```

## [8. 字符串转换整数 (atoi)](https://leetcode.cn/problems/string-to-integer-atoi/)

```c++
#include <climits>
#include <algorithm>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    int myAtoi(string s) {
        bool isPositive = true;
        if (!preprocess(s, isPositive)) {
            return 0;
        }

        keepNumberStr(s);
        if (s.size() > (std::to_string(INT_MAX)).size()) {
            return isPositive ? INT_MAX : INT_MIN;
        }

        long numberLong = 0;
        try {
            numberLong = std::stol(s);
        } catch (...) {
            return numberLong;
        }

        numberLong *= isPositive ? 1 : -1;
        return std::min<long>(std::max<long>(numberLong, INT_MIN), INT_MAX);
    }

private:
    void keepNumberStr(std::string& input) {
        input.erase(std::find_if(input.begin(), input.end(), [](auto c) { return !isdigit(c); }), input.end());
        input.erase(0, input.find_first_not_of('0'));
    }

    bool preprocess(std::string& s, bool& isPositive) {
        s.erase(0, s.find_first_not_of(' '));
        if (s.empty()) {
            return false;
        }

        auto firstChar = s.at(0);
        isPositive = true;
        bool isInputValid = true;
        if (firstChar == '+') {
            s.erase(0, 1);
        } else if (isdigit(firstChar)) {
        } else if (firstChar == '-') {
            s.erase(0, 1);
            isPositive = false;
        } else {
            isInputValid = false;
        }
        return isInputValid;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    const auto& input = "42"s;
    int expect = 42;
    EXPECT_EQ(expect, solution.myAtoi(input));
}

TEST_F(SolutionTest, Test2) {
    const auto& input = "    -42"s;
    int expect = -42;
    EXPECT_EQ(expect, solution.myAtoi(input));
}

TEST_F(SolutionTest, Test3) {
    const auto& input = "        "s;
    int expect = 0;
    EXPECT_EQ(expect, solution.myAtoi(input));
}

TEST_F(SolutionTest, Test4) {
    const auto& input = "+1234"s;
    int expect = 1234;
    EXPECT_EQ(expect, solution.myAtoi(input));
}

TEST_F(SolutionTest, Test5) {
    const auto& input = "4193 with words"s;
    int expect = 4193;
    EXPECT_EQ(expect, solution.myAtoi(input));
}

TEST_F(SolutionTest, Test6) {
    const auto& input = "92233720368547758065461061 good luck"s;
    int expect = INT_MAX;
    EXPECT_EQ(expect, solution.myAtoi(input));
}

TEST_F(SolutionTest, Test7) {
    const auto& input = "-922337203685477580834214321 good luck"s;
    int expect = INT_MIN;
    EXPECT_EQ(expect, solution.myAtoi(input));
}

TEST_F(SolutionTest, Test8) {
    const auto& input = "-+10"s;
    int expect = 0;
    EXPECT_EQ(expect, solution.myAtoi(input));
}

TEST_F(SolutionTest, Test9) {
    const auto& input = "  0000000000012345678"s;
    int expect = 12345678;
    EXPECT_EQ(expect, solution.myAtoi(input));
}

TEST_F(SolutionTest, Test10) {
    const auto& input = "+  0000000000012345678"s;
    int expect = 0;
    EXPECT_EQ(expect, solution.myAtoi(input));
}

TEST_F(SolutionTest, Test11) {
    const auto& input = "  +0000000000012345678"s;
    int expect = 12345678;
    EXPECT_EQ(expect, solution.myAtoi(input));
}

TEST_F(SolutionTest, Test12) {
    const auto& input = "00000-42a1234"s;
    int expect = 0;
    EXPECT_EQ(expect, solution.myAtoi(input));
}

TEST_F(SolutionTest, Test13) {
    const auto& input = "2147483648"s;
    int expect = 2147483647;
    EXPECT_EQ(expect, solution.myAtoi(input));
}
```

[直接每位数字判断]([字符串转换整数 (atoi) - 字符串转换整数 (atoi) - 力扣（LeetCode）](https://leetcode.cn/problems/string-to-integer-atoi/solution/zi-fu-chuan-zhuan-huan-zheng-shu-atoi-by-leetcode-/))：

```c++
class Solution {
public:
    int myAtoi(std::string s) {
        auto validIndex = s.find_first_not_of(' ');
        if (validIndex == std::string::npos) {
            return 0;
        }

        bool isPositive = s.at(validIndex) == '-' ? false : true;
        validIndex += (s.at(validIndex) == '+' || s.at(validIndex) == '-') ? 1 : 0;

        int ret = 0;
        while (validIndex < s.size() && isdigit(s.at(validIndex))) {
            int bitNum = s.at(validIndex) - '0';
            if (ret > INT_MAX / 10 || (ret == INT_MAX / 10 && bitNum > INT_MAX % 10)) {
                return isPositive ? INT_MAX : INT_MIN;
            }
            ret = ret * 10 + bitNum;
            ++validIndex;
        }
        return isPositive ? ret : -ret;
    }
};
```

[用流的方式](https://leetcode.cn/problems/string-to-integer-atoi/solution/zi-fu-chuan-zhuan-huan-zheng-shu-atoi-by-leetcode-/)：

```c++
class Solution {
public:
    int myAtoi(string s) {
        stringstream inputSs(s);
        int ret = 0;
        inputSs >> ret;  // 从读到的第1个属于int的有效字符（+/-/数字）开始直到不属于int的字符，解析成int
        return ret;
    }
};
```

[使用Python的正则表达式](https://leetcode.cn/problems/string-to-integer-atoi/solution/python-1xing-zheng-ze-biao-da-shi-by-knifezhu/)：

正则表达式设置了^开头，所以查找的结果num的list列表最多只有一个值。

int(*num)是对num解包，如果num列表只有一个值，则将此一个值传入int()函数中；如果num列表为空，则int()返回0；如果num列表有多个值，则解包传入int()函数报错。

```python
import re
import unittest


class Solution:
    INT_MAX = 2147483647
    INT_MIN = -2147483648

    def myAtoi(self, str: str) -> int:
        str = str.lstrip()  # 清除左边多余的空格
        num_re = re.compile(r'^[\+\-]?\d+')  # 设置正则规则
        num = num_re.findall(str)  # 查找匹配的内容
        num = int(*num)  # 由于返回的是个列表，解包并且转换成整数
        return max(min(num, self.INT_MAX), self.INT_MIN)  # 返回值


class TestSolution(unittest.TestCase):
    _SOLUTION = Solution()

    def test_solution(self):
        input_str = r'92233720368547758065461061 good luck'
        expect_value = Solution.INT_MAX
        self.assertEqual(expect_value, self._SOLUTION.myAtoi(input_str))
```

使用C++正则表达式

```c++
#include <climits>
#include <algorithm>
#include <regex>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    int myAtoi(string s) {
        bool isPositive = true;
        if (!preprocess(s, isPositive)) {
            return 0;
        }

        if (s.size() > (std::to_string(INT_MAX)).size()) {
            return isPositive ? INT_MAX : INT_MIN;
        }

        long numberLong = 0;
        try {
            numberLong = std::stol(s);
        } catch (...) {
            return numberLong;
        }

        numberLong *= isPositive ? 1 : -1;
        return std::min<long>(std::max<long>(numberLong, INT_MIN), INT_MAX);
    }

private:
    bool preprocess(std::string& s, bool& isPositive) {
        s.erase(0, s.find_first_not_of(' '));

        std::regex re(R"(^[\+\-]?\d+)");
        std::smatch result;
        if (!std::regex_search(s, result, re)) {
            return false;
        }

        s = result.str();
        isPositive = s.at(0) == '-' ? false : true;
        if (s.at(0) == '+' || s.at(0) == '-') {
            s.erase(0, 1);
        }

        s.erase(0, s.find_first_not_of('0'));
        return true;
    }
};

// 优化正则表达式
class Solution {
public:
    int myAtoi(string s) {
        bool isPositive = true;
        if (!preprocess(s, isPositive)) {
            return 0;
        }

        if (s.size() > (std::to_string(INT_MAX)).size()) {
            return isPositive ? INT_MAX : INT_MIN;
        }

        long numberLong = 0;
        try {
            numberLong = std::stol(s);
        } catch (...) {
            return numberLong;
        }

        numberLong *= isPositive ? 1 : -1;
        return std::min<long>(std::max<long>(numberLong, INT_MIN), INT_MAX);
    }

private:
    bool preprocess(std::string& s, bool& isPositive) {
        s.erase(0, s.find_first_not_of(' '));

        std::regex re(R"(^([\+\-]?)(\d+))");
        std::smatch result;
        if (!std::regex_search(s, result, re)) {
            return false;
        }

        isPositive = result[1].str() == "-" ? false : true;  // 第1个小括号的内容
        s = result[2].str();  // 第2个小括号内容

        s.erase(0, s.find_first_not_of('0'));
        return true;
    }
};

```

[状态机](https://leetcode.cn/problems/string-to-integer-atoi/solution/zi-fu-chuan-zhuan-huan-zheng-shu-atoi-by-leetcode-/)

方法一：自动机
思路

字符串处理的题目往往涉及复杂的流程以及条件情况，如果直接上手写程序，一不小心就会写出极其臃肿的代码。

因此，为了有条理地分析每个输入字符的处理方法，我们可以使用自动机这个概念：

我们的程序在每个时刻有一个状态 s，每次从序列中输入一个字符 c，并根据字符 c 转移到下一个状态 s'。这样，我们只需要建立一个覆盖所有情况的从 s 与 c 映射到 s' 的表格即可解决题目中的问题。

算法

我们也可以用下面的表格来表示这个自动机：

' '	+/-	number	other
start	start	signed	in_number	end
signed	end	end	in_number	end
in_number	end	end	in_number	end
end	end	end	end	end
接下来编程部分就非常简单了：我们只需要把上面这个状态转换表抄进代码即可。

另外自动机也需要记录当前已经输入的数字，只要在 s' 为 in_number 时，更新我们输入的数字，即可最终得到输入的数字。

```c++
#include <climits>
#include <regex>
#include <unordered_map>
#include "gtest/gtest.h"

using namespace std;

class Automaton {
    std::string state = "start";
    std::unordered_map<std::string, std::vector<std::string>> table = {
        {"start", {"start", "signed", "in_number", "end"}},
        {"signed", {"end", "end", "in_number", "end"}},
        {"in_number", {"end", "end", "in_number", "end"}},
        {"end", {"end", "end", "end", "end"}}
    };

    int get_col(char c) {
        if (isspace(c)) {
            return 0;
        }
        if (c == '+' or c == '-') {
            return 1;
        }
        if (isdigit(c)) {
            return 2;
        }
        return 3;
    }

public:
    int sign = 1;
    long long ans = 0;

    void get(char c) {
        state = table[state][get_col(c)];
        if (state == "in_number") {
            ans = ans * 10 + c - '0';
            ans = sign == 1 ? min(ans, (long long) INT_MAX) : min(ans, -(long long) INT_MIN);
        } else if (state == "signed") {
            sign = c == '+' ? 1 : -1;
        }
    }
};

class Solution {
public:
    int myAtoi(string str) {
        Automaton automaton;
        for (char c: str) {
            automaton.get(c);
        }
        return automaton.sign * automaton.ans;
    }
};
```

## [49. 字母异位词分组](https://leetcode.cn/problems/group-anagrams/)

```c++
#include <algorithm>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    bool test(std::string s1, std::string s2) {
        std::sort(s1.begin(), s1.end());
        std::sort(s2.begin(), s2.end());
        return s1 == s2;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    const auto& input1 = "anagram"s;
    const auto& input2 = "nagaram"s;
    EXPECT_TRUE(solution.test(input1, input2));
}

TEST_F(SolutionTest, Test2) {
    const auto& input1 = "anagra"s;
    const auto& input2 = "nagaram"s;
    EXPECT_FALSE(solution.test(input1, input2));
}

// 使用hash map比较
class Solution {
public:
    bool test(std::string s1, std::string s2) {
        const auto& firstCharCountMap = getCharCountMap(s1);
        const auto& secondCharCountMap = getCharCountMap(s2);
        return firstCharCountMap == secondCharCountMap;
    }

private:
    std::unordered_map<char, int> getCharCountMap(const std::string& inputStr) {
        std::unordered_map<char, int> charCountMap;
        for (auto c: inputStr) {
            ++charCountMap[c];
        }
        return charCountMap;
    }
};
```

## [三个字符串是否能组成等腰三角形](https://blog.csdn.net/Adong366/article/details/122115925)

```c++
#include <algorithm>
#include <unordered_map>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    bool isTwoSidesSameTriangle(std::string s1, std::string s2, std::string s3) {
        int side1 = 0, side2 = 0, side3 = 0;
        if (!isThreeSidesValid(s1, s2, s3, side1, side2, side3)) {
            return false;
        }

        if (!isThreeSidesValidForTriangle(side1, side2, side3)) {
            return false;
        }

        return isTwoSidesSame(side1, side2, side3);
    }

private:
    bool isThreeSidesValid(const std::string& s1, const std::string& s2, const std::string& s3,
                           int& side1, int& side2, int& side3) {
        try {
            side1 = std::stoi(s1);
            side2 = std::stoi(s2);
            side3 = std::stoi(s3);
            return true;
        } catch (...) {
            return false;
        }
    }

    bool isThreeSidesValidForTriangle(int side1, int side2, int side3) {
        return side1 + side2 > side3 && side1 + side3 > side2 && side2 + side3 > side1 &&
               abs(side1 - side2) < side3 && abs(side1 - side3) < side2 && abs(side2 - side3) < side1;
    }

    bool isTwoSidesSame(int side1, int side2, int side3) {
        return (side1 == side2 && side1 != side3) || (side1 == side3 && side1 != side2) ||
               (side2 == side3 && side2 != side1);
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    const auto& input1 = "10"s;
    const auto& input2 = "7"s;
    const auto& input3 = "7"s;
    EXPECT_TRUE(solution.isTwoSidesSameTriangle(input1, input2, input3));
}

TEST_F(SolutionTest, Test2) {
    const auto& input1 = "10"s;
    const auto& input2 = "7"s;
    const auto& input3 = "8"s;
    EXPECT_FALSE(solution.isTwoSidesSameTriangle(input1, input2, input3));
}

TEST_F(SolutionTest, Test3) {
    const auto& input1 = "10"s;
    const auto& input2 = "7"s;
    const auto& input3 = "3"s;
    EXPECT_FALSE(solution.isTwoSidesSameTriangle(input1, input2, input3));
}

TEST_F(SolutionTest, Test4) {
    const auto& input1 = ""s;
    const auto& input2 = "7"s;
    const auto& input3 = "3"s;
    EXPECT_FALSE(solution.isTwoSidesSameTriangle(input1, input2, input3));
}
```

## Json相似度（没太看懂）

对比非固定层级json，给出相似度，完全相同的key 相似度1

非list类的不相同是0，list你可以算0.5

{"key1": [4,1,2,3],

"key2": "abc",

"key3": "string" }

{"key1": [1,2,3, 4],

"key2": "abc",

"key3": [] }

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import json
import unittest


class Solution:
    def get_similarity(self, json_str1, json_str2):
        json1 = json.loads(json_str1)
        json2 = json.loads(json_str2)
        similarity = 0.0
        for key1, value1 in json1.items():
            value2 = json2.get(key1)
            if value2 is None:
                continue

            similarity += 1

            type1 = type(value1)
            type2 = type(value2)

            if type1 != type2:
                continue

            if type1 == type2:
                similarity += 0.5

            if type1 == type2 and type1 == list:
                value1.sort()
                value2.sort()
                pass
            elif type1 == type2 and type1 == str:
                pass
            elif type1 == type2 and type1 == int:
                pass

        return similarity


class TestSolution(unittest.TestCase):
    _SOLUTION = Solution()

    def test_solution(self):
        json_str1 = r'''
{"key1": [4,1,2,3],
"key2": "abc",
"key3": "string" }
'''
        json_str2 = r'''
{"key1": [1,2,3,4],
"key2": "abc",
"key3": [] }
'''
        self.assertEqual(4.0, self._SOLUTION.get_similarity(json_str1, json_str2))
```

遍历非固定层级 json，把key用如下形式输出为列表：

{"a":1,"b":1,"c":[{"d":123},{"e":1234}]} -> ["a", "b", "c_0_d", "c_1_e"]

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import json
import unittest


class Solution:
    _INVALID_INDEX = -1
    _START_INDEX = 0

    def gen_json_key_index(self, str):
        json_value = json.loads(str)
        output_list = []
        start_str = ''
        self._gen_key_index(json_value, start_str, output_list, self._START_INDEX)
        return output_list

    def _gen_key_index(self, json_value, start_str, output_list, index):
        if type(json_value) != dict:  # isinstance(json_value, dict)
            return self._INVALID_INDEX

        for key, value in json_value.items():
            tmp_str = f'{start_str}_{index}_{key}' if start_str else key
            value_type = type(value)
            if value_type == list:
                self._process_list_value(value, tmp_str, output_list)
            elif value_type == dict:
                self._gen_key_index(value, tmp_str, output_list, self._START_INDEX)
            else:
                output_list.append(tmp_str)
            index += 1
        return len(json_value)

    def _process_list_value(self, value, tmp_str, output_list):
        start_index = self._START_INDEX
        for each_value in value:
            start_index = self._gen_key_index(each_value, tmp_str, output_list, start_index)
            if start_index == self._INVALID_INDEX:
                output_list.append(tmp_str)
                break


class TestSolution(unittest.TestCase):
    _SOLUTION = Solution()

    def test_solution(self):
        input_str = r'''
{
	"a": 1,
	"b": 1,
	"c": [
		{
			"d": 123,
			"e": 1234
		}
	]
}
        '''
        expect_value = ["a", "b", "c_0_d", "c_1_e"]
        self.assertEqual(expect_value, self._SOLUTION.gen_json_key_index(input_str))

    def test_solution2(self):
        input_str = r'''
{
	"a": 1,
	"b": 1,
	"c": [
		{
			"d": 123,
			"e": 1234
		},
		{
			"f": 123,
			"g": 1234
		}
	]
}
        '''
        expect_value = ["a", "b", "c_0_d", "c_1_e", "c_2_f", "c_3_g"]
        self.assertEqual(expect_value, self._SOLUTION.gen_json_key_index(input_str))

    def test_solution3(self):
        input_str = r'''
{
	"a": 1,
	"b": 1,
	"c": {
		"d": 123,
		"e": 1234
	}
}
        '''
        expect_value = ["a", "b", "c_0_d", "c_1_e"]
        self.assertEqual(expect_value, self._SOLUTION.gen_json_key_index(input_str))

    def test_solution4(self):
        input_str = r'''
{
	"a": 1,
	"b": 1,
	"c": {
		"d": {
		    "f": 111,
		    "g": 222
		},
		"e": 1234
	}
}
        '''
        expect_value = ["a", "b", "c_0_d_0_f", "c_0_d_1_g", "c_1_e"]
        self.assertEqual(expect_value, self._SOLUTION.gen_json_key_index(input_str))

    def test_solution5(self):
        input_str = r'''
{
	"a": 1,
	"b": 1,
	"c": [
		1,
		2,
		3,
		4
	]
}
        '''
        expect_value = ["a", "b", "c"]
        self.assertEqual(expect_value, self._SOLUTION.gen_json_key_index(input_str))


if __name__ == '__main__':
    unittest.main()

```

## 计算任意数字的N次方根

使用二分法：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import unittest


class Solution:
    def __init__(self, root_num=None, dest_num=None, points_num=None, points_precision=None):
        self._root_num = root_num
        self._dest_num = dest_num
        self._points_num = points_num
        self._points_precision = points_precision
        self._recursion_count = 0

    def calc_root(self, root_num, dest_num, points_num):
        self.__init__(root_num, dest_num, points_num, 0.1 ** (points_num + 2))
        return self._calc_root_by_recursion(1, dest_num)

    def _calc_root_by_recursion(self, begin, end):
        self._recursion_count += 1
        if begin ** self._root_num == self._dest_num:
            return begin
        if end ** self._root_num == self._dest_num:
            return end

        if (end - begin) <= self._points_precision:
            print(f'recursion count: {self._recursion_count}')
            split_points_list = str(begin).split('.')
            return float(split_points_list[0] + '.' + split_points_list[1][:self._points_num])

        half = (begin + end) / 2
        if half ** self._root_num > self._dest_num:
            return self._calc_root_by_recursion(begin, half)
        else:
            return self._calc_root_by_recursion(half, end)


class TestSolution(unittest.TestCase):
    _SOLUTION = Solution()

    def test_solution1(self):
        self.assertEqual(1.41421, self._SOLUTION.calc_root(root_num=2, dest_num=2, points_num=5))

    def test_solution2(self):
        self.assertEqual(1.4142135623730, self._SOLUTION.calc_root(root_num=2, dest_num=2, points_num=13))

    def test_solution3(self):
        self.assertEqual(1.732, self._SOLUTION.calc_root(root_num=2, dest_num=3, points_num=3))

    def test_solution4(self):
        self.assertEqual(1.44224, self._SOLUTION.calc_root(root_num=3, dest_num=3, points_num=5))
# 输出
test_solution.py::TestSolution::test_solution1 PASSED                    [ 25%]recursion count: 25
test_solution.py::TestSolution::test_solution2 PASSED                    [ 50%]recursion count: 51
test_solution.py::TestSolution::test_solution3 PASSED                    [ 75%]recursion count: 19
test_solution.py::TestSolution::test_solution4 PASSED                    [100%]recursion count: 26
```

使用步进法：

```python
class Solution:
    def __init__(self, root_num=None, dest_num=None, points_num=None):
        self._root_num = root_num
        self._dest_num = dest_num
        self._points_num = points_num
        self._recursion_count = 0

    def calc_root(self, root_num, dest_num, points_num):
        self.__init__(root_num, dest_num, points_num)

        start_num = 1
        for point_num in range(1, points_num + 1):
            start_num = self._get_each_point_num(start_num, 0.1 ** point_num)

        print(f'recursion count: {self._recursion_count}')
        split_points_list = str(start_num).split('.')
        return float(split_points_list[0] + '.' + split_points_list[1][:self._points_num])

    def _get_each_point_num(self, start_num, interval):
        precious_num = start_num
        tmp_num = start_num
        while tmp_num <= self._dest_num:
            self._recursion_count += 1

            if tmp_num ** self._root_num > self._dest_num:
                return precious_num
            precious_num = tmp_num
            tmp_num += interval

        # 标准的range的step只能为整数，不能为小数，可以使用numpy.arange来迭代小数，但有些精度问题
        # import numpy
        # for tmp_num in numpy.arange(start_num, self._dest_num, interval):
        #    self._recursion_count += 1

        #    if tmp_num ** self._root_num > self._dest_num:
        #        return precious_num
        #    precious_num = tmp_num
# 输出：
test_solution.py::TestSolution::test_solution1 PASSED                    [ 25%]recursion count: 22
test_solution.py::TestSolution::test_solution2 PASSED                    [ 50%]recursion count: 67
test_solution.py::TestSolution::test_solution3 PASSED                    [ 75%]recursion count: 18
test_solution.py::TestSolution::test_solution4 PASSED                    [100%]recursion count: 26
```

## 最小的因子和

一个正整数N，可拆成多个因子相乘，比如

100 = 2\*50 = 4\*5\*5 = 100\*1 …..

他们的因子和分别对应50+2、4+5+5、100+1；

现在对正整数N，编程实现，计算出最小的因子和；

```c++
#include <iostream>
#include <vector>
using namespace std;

class Solution {
public:
    int GetMinSum(int inputNum) const
    {
        std::vector<int> nums;
        SplitNum(inputNum, nums);
        if (nums.size() == 1) {
            nums.push_back(1);
        }

        int minSum = GetSum(nums);
        return minSum;
    }

private:
    void SplitNum(int inputNum, std::vector<int>& nums) const
    {
        int leftNum = 0;
        bool isSplit = false;
        for (int i = 2; i < inputNum / 2; ++i) {
            if (inputNum % i == 0) {
                nums.push_back(i);
                leftNum = inputNum / i;
                isSplit = true;
                break;
            }
        }

        if (isSplit) {
            SplitNum(leftNum, nums);
        } else {
            nums.push_back(inputNum);
        }
    }

    int GetSum(const std::vector<int>& nums) const
    {
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        return sum;
    }
};

int main() {
    Solution solution;
    std::cout << solution.GetMinSum(9) << std::endl;
    std::cout << solution.GetMinSum(5) << std::endl;
    std::cout << solution.GetMinSum(100) << std::endl;
    return 0;
}
```

## [543. 二叉树的直径](https://leetcode.cn/problems/diameter-of-binary-tree/)

前序遍历每个节点获取其直径（通过后序遍历获取每个节点的最大深度），参考<[东哥带你刷二叉树（纲领篇） :: labuladong的算法小抄 (gitee.io)](https://labuladong.gitee.io/algo/1/4/)>

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import unittest

# 执行用时：576 ms, 在所有 Python3 提交中击败了5.12% 的用户
# 内存消耗：17.3 MB, 在所有 Python3 提交中击败了20.48% 的用户
# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right


class Solution:
    def __init__(self):
        self._max_diameter = 0

    def diameterOfBinaryTree(self, root):
        self._traverse(root)
        return self._max_diameter

    def _traverse(self, root):
        if root is None:
            return

        left_depth = self._max_depth(root.left)
        right_depth = self._max_depth(root.right)
        self._max_diameter = max(left_depth + right_depth, self._max_diameter)

        self._traverse(root.left)
        self._traverse(root.right)

    def _max_depth(self, root):
        if root is None:
            return 0

        left_depth = self._max_depth(root.left)
        right_depth = self._max_depth(root.right)
        return max(left_depth, right_depth) + 1


class TestSolution(unittest.TestCase):
    _SOLUTION = Solution()

    def test_solution1(self):
        tn4 = TreeNode(4)
        tn5 = TreeNode(5)
        tn2 = TreeNode(2, tn4, tn5)
        tn3 = TreeNode(3)
        tn1 = TreeNode(1, tn2, tn3)
        self.assertEqual(3, self._SOLUTION.diameterOfBinaryTree(tn1))
```

优化：

在后序遍历计算所有节点的最大深度的同时把直径计算出来，可以认为是通过一个副作用来完成计算

```python
# 执行用时： 48 ms , 在所有 Python3 提交中击败了 71.61% 的用户 
# 内存消耗： 17.3 MB , 在所有 Python3 提交中击败了 25.63% 的用户
class Solution:
    def __init__(self):
        self._max_diameter = 0

    def diameterOfBinaryTree(self, root):
        self._max_depth(root)
        return self._max_diameter

    def _max_depth(self, root):
        if root is None:
            return 0

        left_depth = self._max_depth(root.left)
        right_depth = self._max_depth(root.right)
        self._max_diameter = max(self._max_diameter, left_depth + right_depth)

        return max(left_depth, right_depth) + 1
```

## [325. 和等于 k 的最长子数组长度](https://cloud.tencent.com/developer/article/1787698)

给定一个数组 nums 和一个目标值 k，找到和等于 k 的最长子数组长度。 如果不存在任意一个符合要求的子数组，则返回 0。

注意: nums 数组的总和是一定在 32 位有符号整数范围之内的。

算法：用m保存每个sum所对应的前n个数字下标。这样每次用全部数字总和sum-k，如果值存在于m中，则说明有一个子数组求和为k。这时再计算这个子数组长度`i - m[sum - k]`，看是否更大。

```cpp
#include <unordered_map>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    int maxSubArrayLen(vector<int>& nums, int k) {
        unordered_map<int, int> m;//前缀和，idx
        m[0] = -1;
        int i, sum = 0, maxlen = 0;
        for (i = 0; i < nums.size(); ++i) {
            sum += nums[i];
            if (m.count(sum - k)) {
                maxlen = max(maxlen, i - m[sum - k]);
            }
            if (!m.count(sum)) {
                m[sum] = i;
            }
        }
        return maxlen;
    }
};

// 用find代替count
class Solution {
public:
    int maxSubArrayLen(vector<int>& nums, int k) {
        std::unordered_map<int, int> sumIndexMap = {{0, -1}};
        int sum = 0;
        int maxLen = 0;
        for (int i = 0; i < nums.size(); ++i) {
            sum += nums[i];
            auto iter = sumIndexMap.find(sum - k);
            if (iter != sumIndexMap.end()) {
                maxLen = std::max(maxLen, i - (*iter).second);
            }
            if (sumIndexMap.find(sum) == sumIndexMap.end()) {
                sumIndexMap.emplace(sum, i);
            }
        }
        return maxLen;
    }
};

// 暴力破解
class Solution {
public:
    int maxSubArrayLen(vector<int>& nums, int k) {
        int maxLen = 0;
        for (int i = 0; i < nums.size(); ++i) {
            for (int j = i; j < nums.size(); ++j) {
                int sum = getSum(nums, i, j);
                if (sum == k) {
                    maxLen = max(maxLen, j - i + 1);
                }
            }
        }
        return maxLen;
    }

private:
    int getSum(const vector<int>& nums, int i, int j) {
        int sum = 0;
        for (int index = i; index <= j; ++index) {
            sum += nums[index];
        }
        return sum;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::vector<int> nums = {1, -1, 5, -2, 3};
    int k = 3;
    EXPECT_EQ(4, solution.maxSubArrayLen(nums, k));
}

TEST_F(SolutionTest, Test2) {
    std::vector<int> nums = {-2, -1, 2, 1};
    int k = 1;
    EXPECT_EQ(2, solution.maxSubArrayLen(nums, k));
}
```





# 深度优先搜索

## [模板1](https://leetcode.cn/leetbook/read/queue-stack/gp5a7/)

```java
/*
 * Return true if there is a path from cur to target.
 */
boolean DFS(Node cur, Node target, Set<Node> visited) {
    return true if cur is target;
    for (next : each neighbor of cur) {
        if (next is not in visited) {
            add next to visted;
            return true if DFS(next, target, visited) == true;
        }
    }
    return false;
}
```

## 模板2

```java
/*
 * Return true if there is a path from cur to target.
 */
boolean DFS(int root, int target) {
    Set<Node> visited;
    Stack<Node> s;  // 这里是栈，广度优先搜索这里是队列
    add root to s;
    while (s is not empty) {
        Node cur = the top element in s;
        return true if cur is target;
        for (Node next : the neighbors of cur) {
            if (next is not in visited) {
                add next to s;
                add next to visited;
            }
        }
        remove cur from s;
    }
    return false;
}
```



## [200. 岛屿数量](https://leetcode-cn.com/problems/number-of-islands/)(华为题库)（只看没做）

<https://leetcode-cn.com/problems/number-of-islands/solution/dao-yu-shu-liang-by-leetcode/>

深度搜索：

遍历全部二维网格，如果任意一个单元格为1，则使用深度搜索把与该单元格相连的全部单元格设置为0：

```c++
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 8ms
public:
    int numIslands(vector<vector<char>>& grid) {
        int nr = grid.size();
        if (!nr) {
            return 0;
        }
        int nc = grid[0].size();

        int num_islands = 0;
        for (int r = 0; r < nr; ++r) {
            for (int c = 0; c < nc; ++c) {
                if (grid[r][c] == '1') {
                    ++num_islands;
                    dfs(grid, r, c);
                }
            }
        }

        return num_islands;
    }

private:
    void dfs(vector<vector<char>>& grid, int r, int c) {
        int nr = grid.size();
        int nc = grid[0].size();

        grid[r][c] = '0';
        if (r - 1 >= 0 && grid[r - 1][c] == '1') {
            dfs(grid, r - 1, c);
        }
        if (r + 1 < nr && grid[r + 1][c] == '1') {
            dfs(grid, r + 1, c);
        }
        if (c - 1 >= 0 && grid[r][c - 1] == '1') {
            dfs(grid, r, c - 1);
        }
        if (c + 1 < nc && grid[r][c + 1] == '1') {
            dfs(grid, r, c + 1);
        }
    }
};

TEST(SolutionTest, Test1) {
    vector<vector<char>> grid = {
        {'1', '1', '1', '1', '0'},
        {'1', '1', '0', '1', '0'},
        {'1', '1', '0', '0', '0'},
        {'0', '0', '0', '0', '0'},
    };
    EXPECT_EQ(1, Solution().numIslands(grid));
}

TEST(SolutionTest, Test2) {
    vector<vector<char>> grid = {
        {'1', '1', '0', '0', '0'},
        {'1', '1', '0', '0', '0'},
        {'0', '0', '1', '0', '0'},
        {'0', '0', '0', '1', '1'},
    };
    EXPECT_EQ(3, Solution().numIslands(grid));
}
```

广度搜索：

同样遍历二维表格，任意一个单元格值为1，则使用队列进行广度搜索，把相邻的1都标为0，再去遍历下一个单元格。

```c++
class Solution {
public:
    int numIslands(vector<vector<char>>& grid) {
        int nr = grid.size();
        if (!nr) {
            return 0;
        }
        int nc = grid[0].size();

        int num_islands = 0;
        for (int r = 0; r < nr; ++r) {
            for (int c = 0; c < nc; ++c) {
                if (grid[r][c] == '1') {
                    ++num_islands;
                    grid[r][c] = '0'; // mark as visited
                    queue<pair<int, int>> neighbors;
                    neighbors.push({r, c});
                    while (!neighbors.empty()) {
                        auto rc = neighbors.front();
                        neighbors.pop();
                        int row = rc.first, col = rc.second;
                        if (row - 1 >= 0 && grid[row - 1][col] == '1') {
                            neighbors.push({row - 1, col});
                            grid[row - 1][col] = '0';
                        }
                        if (row + 1 < nr && grid[row + 1][col] == '1') {
                            neighbors.push({row + 1, col});
                            grid[row + 1][col] = '0';
                        }
                        if (col - 1 >= 0 && grid[row][col - 1] == '1') {
                            neighbors.push({row, col - 1});
                            grid[row][col - 1] = '0';
                        }
                        if (col + 1 < nc && grid[row][col + 1] == '1') {
                            neighbors.push({row, col + 1});
                            grid[row][col + 1] = '0';
                        }
                    }
                }
            }
        }

        return num_islands;
    }
};
```

并查集 

遍历二维网格，将竖直或水平相邻的陆地联结。最终，返回并查集数据结构中相连部分的数量。

下面的动画展示了整个算法。

```c++
class UnionFind {
public:
    UnionFind(vector<vector<char>>& grid) {
        count = 0;
        int m = grid.size();
        int n = grid[0].size();
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (grid[i][j] == '1') {
                    parent.push_back(i * n + j);
                    ++count;
                }
                else {
                    parent.push_back(-1);
                }
                rank.push_back(0);
            }
        }
    }

    int find(int i) { // path compression
        if (parent[i] != i) {
            parent[i] = find(parent[i]);
        }
        return parent[i];
    }

    void Union(int x, int y) { // union with rank
        int rootx = find(x);
        int rooty = find(y);
        if (rootx != rooty) {
            if (rank[rootx] > rank[rooty]) {
                parent[rooty] = rootx;
            }
            else if (rank[rootx] < rank[rooty]) {
                parent[rootx] = rooty;
            }
            else {
                parent[rooty] = rootx;
                rank[rootx] += 1;
            }
            --count;
        }
    }

    int getCount() const {
        return count;
    }

private:
    vector<int> parent;
    vector<int> rank;
    int count; // # of connected components
};

class Solution {
public:
    int numIslands(vector<vector<char>>& grid) {
        int nr = grid.size();
        if (!nr) {
            return 0;
        }
        int nc = grid[0].size();

        UnionFind uf(grid);
        int num_islands = 0;
        for (int r = 0; r < nr; ++r) {
            for (int c = 0; c < nc; ++c) {
                if (grid[r][c] == '1') {
                    grid[r][c] = '0';
                    if (r - 1 >= 0 && grid[r - 1][c] == '1') {
                        uf.Union(r * nc + c, (r - 1) * nc + c);
                    }
                    if (r + 1 < nr && grid[r + 1][c] == '1') {
                        uf.Union(r * nc + c, (r + 1) * nc + c);
                    }
                    if (c - 1 >= 0 && grid[r][c - 1] == '1') {
                        uf.Union(r * nc + c, r * nc + c - 1);
                    }
                    if (c + 1 < nc && grid[r][c + 1] == '1') {
                        uf.Union(r * nc + c, r * nc + c + 1);
                    }
                }
            }
        }

        return uf.getCount();
    }
};
```

## [98. 验证二叉搜索树](https://leetcode.cn/problems/validate-binary-search-tree/)

```cpp
#include <climits>
#include "gtest/gtest.h"

using namespace std;

struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

class Solution {
public:
    bool isValidBST(TreeNode* root) {
        return checkVal(root, LONG_MIN, LONG_MAX);
    }

private:
    bool checkVal(TreeNode* node, long lower, long upper) {
        if (node == nullptr) {
            return true;
        }

        if (node->val >= upper || node->val <= lower) {
            return false;
        }
        return checkVal(node->left, lower, node->val) && checkVal(node->right, node->val, upper);
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::unique_ptr<TreeNode> root = std::make_unique<TreeNode>(2);
    std::unique_ptr<TreeNode> left = std::make_unique<TreeNode>(1);
    std::unique_ptr<TreeNode> right = std::make_unique<TreeNode>(3);
    root->left = left.get();
    root->right = right.get();
    EXPECT_TRUE(solution.isValidBST(root.get()));
}

TEST_F(SolutionTest, Test2) {
    std::unique_ptr<TreeNode> root5 = std::make_unique<TreeNode>(5);
    std::unique_ptr<TreeNode> node3 = std::make_unique<TreeNode>(3);
    std::unique_ptr<TreeNode> node7 = std::make_unique<TreeNode>(7);
    std::unique_ptr<TreeNode> node1 = std::make_unique<TreeNode>(1);
    std::unique_ptr<TreeNode> node4 = std::make_unique<TreeNode>(4);
    std::unique_ptr<TreeNode> node6 = std::make_unique<TreeNode>(6);
    std::unique_ptr<TreeNode> node8 = std::make_unique<TreeNode>(8);
    root5->left = node3.get();
    root5->right = node7.get();
    node3->left = node1.get();
    node3->right = node4.get();
    node7->left = node6.get();
    node7->right = node8.get();
    std::vector<int> expected = {4, 5, 2, 6, 7, 3, 1};
    EXPECT_TRUE(solution.isValidBST(root5.get()));
}
```

自己写的都有问题：

```java
package test;

import java.util.LinkedList;
import java.util.Queue;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}

class Solution {
    public boolean isValidBST(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode treeNode = queue.remove();
            if (treeNode == null) {
                continue;
            }

            TreeNode left = treeNode.left;
            if (left != null) {
                if (left.val >= treeNode.val) {
                    return false;
                }
                queue.add(left);
            }

            TreeNode right = treeNode.right;
            if (right != null) {
                if (right.val <= treeNode.val) {
                    return false;
                }
                queue.add(right);
            }
        }
        return true;
    }
}

class Solution {
    public boolean isValidBST(TreeNode root) {
        if (root == null) {
            return true;
        }
        if (!check(root, null, null)) {
            return false;
        }

        return true;
    }

    private boolean check(TreeNode treeNode, Integer leftMaxVal, Integer rightMinVal) {
        if (!checkLeft(treeNode, leftMaxVal == null ? treeNode.val : leftMaxVal, rightMinVal)) {
            return false;
        }
        if (!checkRight(treeNode, leftMaxVal, rightMinVal == null ? treeNode.val : rightMinVal)) {
            return false;
        }
        return true;
    }

    private boolean checkLeft(TreeNode treeNode, Integer leftMaxVal, Integer rightMinVal) {
        TreeNode left = treeNode.left;
        if (left != null) {
            if (left.val >= treeNode.val) {  // 左子节点值不能大于等于父节点
                return false;
            }
            if (rightMinVal != null && left.val <= rightMinVal) {
                return false;
            }
            return check(left, Math.max(left.val, leftMaxVal == null ? left.val : leftMaxVal.intValue()),
                Math.min(left.val, rightMinVal == null ? left.val : rightMinVal.intValue()));
        }
        return true;
    }

    private boolean checkRight(TreeNode treeNode, Integer leftMaxVal, Integer rightMinVal) {
        TreeNode right = treeNode.right;
        if (right != null) {
            if (right.val <= treeNode.val) {
                return false;
            }
            if (leftMaxVal != null && right.val >= leftMaxVal) {
                return false;
            }
            return check(right, Math.max(right.val, leftMaxVal == null ? right.val : leftMaxVal.intValue()),
                Math.min(right.val, rightMinVal == null ? right.val : rightMinVal.intValue()));
        }
        return true;
    }
}

//
package test;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static Solution SOLUTION;

    @Before
    public void before() {
        SOLUTION = new Solution();
    }

    @Test
    public void test1() {
        TreeNode treeNode1 = new TreeNode(5);
        TreeNode treeNode2 = new TreeNode(1);
        TreeNode treeNode3 = new TreeNode(4);
        treeNode1.left = treeNode2;
        treeNode1.right = treeNode3;

        TreeNode treeNode4 = new TreeNode(3);
        TreeNode treeNode5 = new TreeNode(6);
        treeNode3.left = treeNode4;
        treeNode3.right = treeNode5;

        assertEquals(false, SOLUTION.isValidBST(treeNode1));
    }

    @Test
    public void test2() {
        TreeNode treeNode1 = new TreeNode(5);
        TreeNode treeNode2 = new TreeNode(1);
        TreeNode treeNode3 = new TreeNode(7);
        treeNode1.left = treeNode2;
        treeNode1.right = treeNode3;

        TreeNode treeNode4 = new TreeNode(4);
        TreeNode treeNode5 = new TreeNode(8);
        treeNode3.left = treeNode4;
        treeNode3.right = treeNode5;

        assertEquals(false, SOLUTION.isValidBST(treeNode1));
    }

    @Test
    public void test3() {
        TreeNode treeNode1 = new TreeNode(5);
        TreeNode treeNode2 = new TreeNode(1);
        TreeNode treeNode3 = new TreeNode(7);
        treeNode1.left = treeNode2;
        treeNode1.right = treeNode3;

        TreeNode treeNode4 = new TreeNode(6);
        TreeNode treeNode5 = new TreeNode(8);
        treeNode3.left = treeNode4;
        treeNode3.right = treeNode5;

        assertEquals(true, SOLUTION.isValidBST(treeNode1));
    }

    @Test
    public void test4() {
        TreeNode treeNode1 = new TreeNode(0);
        assertEquals(true, SOLUTION.isValidBST(treeNode1));
    }

    @Test
    public void test5() {
        TreeNode treeNode1 = new TreeNode(1);
        TreeNode treeNode2 = new TreeNode(1);
        treeNode1.left = treeNode2;
        assertEquals(false, SOLUTION.isValidBST(treeNode1));
    }
}
```

别人的优化算法：

<https://leetcode-cn.com/problems/validate-binary-search-tree/solution/yan-zheng-er-cha-sou-suo-shu-by-leetcode/>

方法一: 递归

```java
package test;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int x) {
        val = x;
    }
}

class Solution {  // 1ms
    public boolean isValidBST(TreeNode root) {
        return helper(root, null, null);
    }

    private boolean helper(TreeNode node, Integer lower, Integer upper) {
        if (node == null) {
            return true;
        }

        int val = node.val;
        if (lower != null && val <= lower) {
            return false;
        }
        if (upper != null && val >= upper) {
            return false;
        }

        if (!helper(node.left, lower, val)) {
            return false;
        }
        if (!helper(node.right, val, upper)) {
            return false;
        }
        return true;
    }
}
```

方法二: 迭代

通过使用栈，上面的递归法可以转化为迭代法。这里使用深度优先搜索，比广度优先搜索要快一些。（感觉说的有些问题，看起来这里使用的是广度搜索）

```java
class Solution {  // 5ms
    private Queue<TreeNode> queue = new LinkedList();
    private Queue<Integer> lowers = new LinkedList();
    private Queue<Integer> uppers = new LinkedList();

    public boolean isValidBST(TreeNode root) {
        Integer lower = null, upper = null, val;
        update(root, lower, upper);

        while (!queue.isEmpty()) {
            root = queue.poll();
            lower = lowers.poll();
            upper = uppers.poll();

            if (root == null) {
                continue;
            }
            val = root.val;
            if (lower != null && val <= lower) {
                return false;
            }
            if (upper != null && val >= upper) {
                return false;
            }
            update(root.left, lower, val);
            update(root.right, val, upper);
        }
        return true;
    }

    private void update(TreeNode root, Integer lower, Integer upper) {
        queue.add(root);
        lowers.add(lower);
        uppers.add(upper);
    }
}
```

方法三：中序遍历

二叉搜索树的中序遍历是单调递增的，所以我们使用[中序遍历](https://leetcode-cn.com/problems/binary-tree-inorder-traversal/solution/er-cha-shu-de-zhong-xu-bian-li-by-leetcode/)
`左子树 -> 结点 -> 右子树`的顺序。

```java
class Solution {  // 3ms
    public boolean isValidBST(TreeNode root) {
        Stack<TreeNode> stack = new Stack();
        double inorder = -Double.MAX_VALUE;  // 这里用double是因为有个测试用例的节点值为Integer.MIN_VALUE，如果使用int则失败

        while (!stack.isEmpty() || root != null) {
            while (root != null) {
                stack.push(root);
                root = root.left;
            }
            root = stack.pop();
            // If next element in inorder traversal
            // is smaller than the previous one
            // that's not BST.
            if (root.val <= inorder) {
                return false;
            }
            inorder = root.val;
            root = root.right;
        }
        return true;
    }
}
```

参考下面的中序遍历的递归算法：

```java
class Solution {
    private double inOrderValue = -Double.MAX_VALUE;

    public boolean isValidBST(TreeNode root) {
        return inOrderRecursively(root);
    }

    private boolean inOrderRecursively(TreeNode root) {
        if (root == null) {
            return true;
        }

        if (!inOrderRecursively(root.left)) {
            return false;
        }

        if (root.val <= inOrderValue) {
            return false;
        }
        inOrderValue = root.val;

        if (!inOrderRecursively(root.right)) {
            return false;
        }

        return true;
    }
}
```

### 二叉树遍历算法讲解

<https://blog.csdn.net/qq_33243189/article/details/80222629>

<https://blog.csdn.net/coder__666/article/details/80349039>

<https://www.cnblogs.com/llhthinker/p/4747962.html>

以如下二叉树为样例：

![](pictures\二叉树数据样例.png)

#### 1.前序遍历

递归

```java
//
package test;

import java.util.ArrayList;
import java.util.List;

class TreeNode<T> {
    T val;
    TreeNode<T> left;
    TreeNode<T> right;

    TreeNode(T x) {
        val = x;
    }
}

class Solution<T> {
    private List<T> output = new ArrayList<>();

    public List<T> preOrder(TreeNode<T> root) {
        preOrderRecursively(root);
        return output;
    }

    private void preOrderRecursively(TreeNode<T> root) {
        if (root != null) {
            output.add(root.val);
            preOrderRecursively(root.left);
            preOrderRecursively(root.right);
        }
    }
}
```

非递归：

```java
class Solution<T> {
    private List<T> output = new ArrayList<>();

    public List<T> preOrder(TreeNode<T> root) {
        Stack<TreeNode<T>> stack = new Stack<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {  // 不断的找左子节点，输出后再入栈
                output.add(root.val);
                stack.push(root);
                root = root.left;
            }
            if (!stack.isEmpty()) {  // 当找不到左子节点后，栈顶中的元素弹出，并获取其右子节点重复上述操作
                root = stack.pop();
                root = root.right;
            }
        }
        return output;
    }
}
```

测试用例：

```java
//
package test;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution<String> SOLUTION = new Solution();

    @Test
    public void test1() {
        TreeNode<String> treeNode1 = new TreeNode<>("A");

        TreeNode<String> treeNode2 = new TreeNode<>("B");
        treeNode1.left = treeNode2;

        TreeNode<String> treeNode3 = new TreeNode<>("C");
        treeNode2.right = treeNode3;

        TreeNode<String> treeNode4 = new TreeNode<>("D");
        treeNode3.left = treeNode4;

        TreeNode<String> treeNode5 = new TreeNode<>("E");
        treeNode1.right = treeNode5;

        TreeNode<String> treeNode6 = new TreeNode<>("F");
        treeNode5.right = treeNode6;

        TreeNode<String> treeNode7 = new TreeNode<>("G");
        treeNode6.left = treeNode7;

        TreeNode<String> treeNode8 = new TreeNode<>("H");
        TreeNode<String> treeNode9 = new TreeNode<>("K");
        treeNode7.left = treeNode8;
        treeNode7.right = treeNode9;

        List<String> expected = "ABCDEFGHK".chars().mapToObj(i -> (char) i).map(String::valueOf).collect(Collectors.toList());
        assertEquals(expected, SOLUTION.preOrder(treeNode1));
    }
}
```

#### 2.中序遍历

递归：

```java
class Solution<T> {
    private List<T> output = new ArrayList<>();

    public List<T> inOrder(TreeNode<T> root) {
        inOrderRecursively(root);
        return output;
    }

    private void inOrderRecursively(TreeNode<T> root) {
        if (root == null) {
            return;
        }

        inOrderRecursively(root.left);
        output.add(root.val);
        inOrderRecursively(root.right);
    }
}

```

非递归：

```java
class Solution<T> {
    private List<T> output = new ArrayList<>();

    public List<T> inOrder(TreeNode<T> root) {
        Stack<TreeNode<T>> stack = new Stack<>();
        while (root != null || !stack.isEmpty()) {
            while (root != null) {  // 不断的找左子节点，并直接入栈
                stack.push(root);
                root = root.left;
            }

            if (!stack.isEmpty()) {  // 当找不到左子节点后，栈顶中的元素弹出，输出，并获取其右子节点重复上述操作
                root = stack.pop();
                output.add(root.val);
                root = root.right;
            }
        }
        return output;
    }
}
```

测试用例：

```java
List<String> expected = "BDCAEHGKF".chars().mapToObj(i -> (char) i).map(String::valueOf).collect(Collectors.toList());
assertEquals(expected, SOLUTION.inOrder(treeNode1));    
```

#### 3.后序遍历

递归：

```java
class Solution<T> {
    private List<T> output = new ArrayList<>();

    public List<T> postOrder(TreeNode<T> root) {
        postOrderRecursively(root);
        return output;
    }

    private void postOrderRecursively(TreeNode<T> root) {
        if (root == null) {
            return;
        }
        
        postOrderRecursively(root.left);
        postOrderRecursively(root.right);
        output.add(root.val);
    }
}
```

非递归：

```java
class Solution<T> {
    private List<T> output = new ArrayList<>();

    public List<T> postOrder(TreeNode<T> root) {
        int left = 1;  // 在辅助栈里表示左节点
        int right = 2;  // 在辅助栈里表示右节点
        Stack<TreeNode<T>> stack = new Stack<>();
        Stack<Integer> stack2 = new Stack<>();  // 辅助栈，用来判断子节点返回父节点时处于左节点还是右节点。

        while (root != null || !stack.empty()) {
            while (root != null) {  // 将节点压入栈1，并在栈2将节点标记为左节点
                stack.push(root);
                stack2.push(left);
                root = root.left;
            }

            while (!stack.empty() && stack2.peek() == right) {  // 如果是从右子节点返回父节点，则任务完成，将两个栈的栈顶弹出
                stack2.pop();
                output.add(stack.pop().val);
            }

            if (!stack.empty() && stack2.peek() == left) {  // 如果是从左子节点返回父节点，则将标记改为右子节点
                stack2.pop();
                stack2.push(right);
                root = stack.peek().right;
            }
        }
        return output;
    }
}
```

测试用例：

```java
List<String> expected = "DCBHKGFEA".chars().mapToObj(i -> (char) i).map(String::valueOf).collect(Collectors.toList());
assertEquals(expected, SOLUTION.postOrder(treeNode1));
```

C++写的后序遍历：

```cpp
#include <stack>
#include "gtest/gtest.h"

using namespace std;

struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;

    TreeNode() : val(0), left(nullptr), right(nullptr) {}

    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}

    TreeNode(int x, TreeNode* left, TreeNode* right) : val(x), left(left), right(right) {}
};

class Solution {
public:
    vector<int> traverseTree(TreeNode* root) {
        vector<int> res;
        std::stack<TreeNode*> stack;

        int left = -1, right = 1;
        std::stack<int> helpStack;
        while (root != nullptr || !stack.empty()) {
            while (root != nullptr) {
                stack.push(root);
                helpStack.push(left);
                root = root->left;
            }

            while (!stack.empty() && helpStack.top() == right) {
                res.push_back(stack.top()->val);
                stack.pop();
                helpStack.pop();
            }

            if (!stack.empty() && helpStack.top() == left) {
                helpStack.top() = right;
                root = stack.top()->right;
            }
        }
        //        dfs(root, res);
        return res;
    }

private:
//    void dfs(TreeNode* node, std::vector<int>& res) {
//        if (node == nullptr) {
//            return;
//        }
//
//        res.push_back(node->val);
//        dfs(node->left, res);
//        dfs(node->right, res);
//    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1_preOrder) {
    std::unique_ptr<TreeNode> root1 = std::make_unique<TreeNode>(1);
    std::unique_ptr<TreeNode> node2 = std::make_unique<TreeNode>(2);
    std::unique_ptr<TreeNode> node3 = std::make_unique<TreeNode>(3);
    std::unique_ptr<TreeNode> node4 = std::make_unique<TreeNode>(4);
    std::unique_ptr<TreeNode> node5 = std::make_unique<TreeNode>(5);
    std::unique_ptr<TreeNode> node6 = std::make_unique<TreeNode>(6);
    std::unique_ptr<TreeNode> node7 = std::make_unique<TreeNode>(7);
    root1->left = node2.get();
    root1->right = node3.get();
    node2->left = node4.get();
    node2->right = node5.get();
    node3->left = node6.get();
    node3->right = node7.get();
    std::vector<int> expected = {1, 2, 4, 5, 3, 6, 7};
    EXPECT_EQ(expected, solution.traverseTree(root1.get()));
}

TEST_F(SolutionTest, Test1_inOrder) {
    std::unique_ptr<TreeNode> root1 = std::make_unique<TreeNode>(1);
    std::unique_ptr<TreeNode> node2 = std::make_unique<TreeNode>(2);
    std::unique_ptr<TreeNode> node3 = std::make_unique<TreeNode>(3);
    std::unique_ptr<TreeNode> node4 = std::make_unique<TreeNode>(4);
    std::unique_ptr<TreeNode> node5 = std::make_unique<TreeNode>(5);
    std::unique_ptr<TreeNode> node6 = std::make_unique<TreeNode>(6);
    std::unique_ptr<TreeNode> node7 = std::make_unique<TreeNode>(7);
    root1->left = node2.get();
    root1->right = node3.get();
    node2->left = node4.get();
    node2->right = node5.get();
    node3->left = node6.get();
    node3->right = node7.get();
    std::vector<int> expected = {4, 2, 5, 1, 6, 3, 7};
    EXPECT_EQ(expected, solution.traverseTree(root1.get()));
}

TEST_F(SolutionTest, Test1_postOrder) {
    std::unique_ptr<TreeNode> root1 = std::make_unique<TreeNode>(1);
    std::unique_ptr<TreeNode> node2 = std::make_unique<TreeNode>(2);
    std::unique_ptr<TreeNode> node3 = std::make_unique<TreeNode>(3);
    std::unique_ptr<TreeNode> node4 = std::make_unique<TreeNode>(4);
    std::unique_ptr<TreeNode> node5 = std::make_unique<TreeNode>(5);
    std::unique_ptr<TreeNode> node6 = std::make_unique<TreeNode>(6);
    std::unique_ptr<TreeNode> node7 = std::make_unique<TreeNode>(7);
    root1->left = node2.get();
    root1->right = node3.get();
    node2->left = node4.get();
    node2->right = node5.get();
    node3->left = node6.get();
    node3->right = node7.get();
    std::vector<int> expected = {4, 5, 2, 6, 7, 3, 1};
    EXPECT_EQ(expected, solution.traverseTree(root1.get()));
}
```



#### 4.层次遍历

就是广度搜索遍历。

与树的前中后序遍历的DFS思想不同，层次遍历用到的是BFS思想。一般DFS用递归去实现（也可以用栈实现），BFS需要用队列去实现。
层次遍历的步骤是：
    1.对于不为空的结点，先把该结点加入到队列中
    2.从队中拿出结点，如果该结点的左右结点不为空，就分别把左右结点加入到队列中
    3.重复以上操作直到队列为空

```java
class Solution<T> {
    private List<T> output = new ArrayList<>();

    public List<T> levelTraverse(TreeNode<T> root) {
        Queue<TreeNode<T>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode<T> treeNode = queue.remove();
            if (treeNode == null) {
                continue;
            }
            output.add(treeNode.val);

            if (treeNode.left != null) {
                queue.add(treeNode.left);
            }

            if (treeNode.right != null) {
                queue.add(treeNode.right);
            }
        }
        return output;
    }
}
```

测试用例：

```java
List<String> expected = "ABECFDGHK".chars().mapToObj(i -> (char) i).map(String::valueOf).collect(Collectors.toList());
assertEquals(expected, SOLUTION.levelTraverse(treeNode1));
```

## [99. 恢复二叉搜索树](https://leetcode.cn/problems/recover-binary-search-tree/)

```cpp
#include <stack>
#include <climits>
#include "gtest/gtest.h"

using namespace std;

struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;

    TreeNode() : val(0), left(nullptr), right(nullptr) {}

    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}

    TreeNode(int x, TreeNode* left, TreeNode* right) : val(x), left(left), right(right) {}
};

// 隐式中序遍历
class Solution {
public:
    void recoverTree(TreeNode* root) {
        stack<TreeNode*> stk;
        TreeNode* x = nullptr;
        TreeNode* y = nullptr;
        TreeNode* pred = nullptr;

        while (!stk.empty() || root != nullptr) {
            while (root != nullptr) {
                stk.push(root);
                root = root->left;
            }
            root = stk.top();
            stk.pop();
            if (pred != nullptr && root->val < pred->val) {
                y = root;
                if (x == nullptr) {
                    x = pred;
                } else {
                    break;
                }
            }
            pred = root;
            root = root->right;
        }

        swap(x->val, y->val);
    }
};

class Solution2 {
public:
    bool isValidBST(TreeNode* root) {
        return checkVal(root, LONG_MIN, LONG_MAX);
    }

private:
    bool checkVal(TreeNode* node, long lower, long upper) {
        if (node == nullptr) {
            return true;
        }

        if (node->val >= upper || node->val <= lower) {
            return false;
        }
        return checkVal(node->left, lower, node->val) && checkVal(node->right, node->val, upper);
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
    Solution2 solution2;
};

TEST_F(SolutionTest, Test1) {
    std::unique_ptr<TreeNode> root3 = std::make_unique<TreeNode>(3);
    std::unique_ptr<TreeNode> left1 = std::make_unique<TreeNode>(1);
    std::unique_ptr<TreeNode> right4 = std::make_unique<TreeNode>(4);
    std::unique_ptr<TreeNode> right4_left2 = std::make_unique<TreeNode>(2);
    root3->left = left1.get();
    root3->right = right4.get();
    right4->left = right4_left2.get();
    EXPECT_FALSE(solution2.isValidBST(root3.get()));
    solution.recoverTree(root3.get());
    EXPECT_TRUE(solution2.isValidBST(root3.get()));
}

// 显示中序遍历
class Solution {
public:
    void inorder(TreeNode* root, vector<int>& nums) {
        if (root == nullptr) {
            return;
        }
        inorder(root->left, nums);
        nums.push_back(root->val);
        inorder(root->right, nums);
    }

    pair<int,int> findTwoSwapped(vector<int>& nums) {
        int n = nums.size();
        int index1 = -1, index2 = -1;
        for (int i = 0; i < n - 1; ++i) {
            if (nums[i + 1] < nums[i]) {
                index2 = i + 1;
                if (index1 == -1) {
                    index1 = i;
                } else {
                    break;
                }
            }
        }
        int x = nums[index1], y = nums[index2];
        return {x, y};
    }
    
    void recover(TreeNode* r, int count, int x, int y) {
        if (r != nullptr) {
            if (r->val == x || r->val == y) {
                r->val = r->val == x ? y : x;
                if (--count == 0) {
                    return;
                }
            }
            recover(r->left, count, x, y);
            recover(r->right, count, x, y);
        }
    }

    void recoverTree(TreeNode* root) {
        vector<int> nums;
        inorder(root, nums);
        pair<int,int> swapped= findTwoSwapped(nums);
        recover(root, 2, swapped.first, swapped.second);
    }
};

```

## [113. 路径总和 II](https://leetcode.cn/problems/path-sum-ii/)

```cpp
// dfs
class Solution {
public:
    vector<vector<int>> pathSum(TreeNode* root, int targetSum) {
        std::vector<std::vector<int>> paths;
        searchAllPaths(paths, root, std::vector<int>(), targetSum);
        return paths;
    }
private:
    void searchAllPaths(vector<vector<int>>& paths, TreeNode* node, vector<int> path, int targetSum) {
        if (node == nullptr) {
            return;
        }

        path.push_back(node->val);
        targetSum -= node->val;
        if (node->left == nullptr && node->right == nullptr) {
            if (targetSum == 0) {
                paths.push_back(path);
            }
            return;
        }
        searchAllPaths(paths, node->left, path, targetSum);
        searchAllPaths(paths, node->right, path, targetSum);
    }
};

// bfs https://leetcode.cn/problems/path-sum-ii/solutions/427759/lu-jing-zong-he-ii-by-leetcode-solution/
class Solution {
public:
    vector<vector<int>> ret;
    unordered_map<TreeNode*, TreeNode*> parent;

    void getPath(TreeNode* node) {
        vector<int> tmp;
        while (node != nullptr) {
            tmp.emplace_back(node->val);
            node = parent[node];
        }
        reverse(tmp.begin(), tmp.end());
        ret.emplace_back(tmp);
    }

    vector<vector<int>> pathSum(TreeNode* root, int targetSum) {
        if (root == nullptr) {
            return ret;
        }

        queue<TreeNode*> que_node;
        queue<int> que_sum;
        que_node.emplace(root);
        que_sum.emplace(0);

        while (!que_node.empty()) {
            TreeNode* node = que_node.front();
            que_node.pop();
            int rec = que_sum.front() + node->val;
            que_sum.pop();

            if (node->left == nullptr && node->right == nullptr) {
                if (rec == targetSum) {
                    getPath(node);
                }
            } else {
                if (node->left != nullptr) {
                    parent[node->left] = node;
                    que_node.emplace(node->left);
                    que_sum.emplace(rec);
                }
                if (node->right != nullptr) {
                    parent[node->right] = node;
                    que_node.emplace(node->right);
                    que_sum.emplace(rec);
                }
            }
        }

        return ret;
    }
};
```

### [一篇文章解决所有二叉树路径问题（问题分析+分类模板+题目剖析）](https://leetcode.cn/problems/path-sum-ii/solutions/1/yi-pian-wen-zhang-jie-jue-suo-you-er-cha-oo63/)

解题模板 这类题通常用深度优先搜索(DFS)和广度优先搜索(BFS)解决，BFS较DFS繁琐，这里为了简洁只展现DFS代码 下面是我对两类题目的分析与模板 

一、自顶而下： dfs

```cpp
// 一般路径：
vector<vector<int>>res;
void dfs(TreeNode*root,vector<int>path)
{
    if(!root) return;  //根节点为空直接返回
    path.push_back(root->val);  //作出选择
    if(!root->left && !root->right) //如果到叶节点  
    {
        res.push_back(path);
        return;
    }
    dfs(root->left,path);  //继续递归
    dfs(root->right,path);
}

# **给定和的路径：**
void dfs(TreeNode*root, int sum, vector<int> path)
{
    if (!root)
        return;
    sum -= root->val;
    path.push_back(root->val);
    if (!root->left && !root->right && sum == 0)
    {
        res.push_back(path);
        return;
    }
    dfs(root->left, sum, path);
    dfs(root->right, sum, path);
}

```

这类题型DFS注意点： 1、如果是找路径和等于给定target的路径的，那么可以不用新增一个临时变量cursum来判断当前路径和， 只需要用给定和target减去节点值，最终结束条件判断target==0即可

2、是否要回溯：二叉树的问题大部分是不需要回溯的，原因如下： 二叉树的递归部分：dfs(root->left),dfs(root->right)已经把可能的路径穷尽了, 因此到任意叶节点的路径只可能有一条，绝对不可能出现另外的路径也到这个满足条件的叶节点的;

而对比二维数组(例如迷宫问题)的DFS,for循环向四个方向查找每次只能朝向一个方向，并没有穷尽路径， 因此某一个满足条件的点可能是有多条路径到该点的

并且visited数组标记已经走过的路径是会受到另外路径是否访问的影响，这时候必须回溯

3、找到路径后是否要return: 取决于题目是否要求找到叶节点满足条件的路径,如果必须到叶节点,那么就要return; 但如果是到任意节点都可以，那么必不能return,因为这条路径下面还可能有更深的路径满足条件，还要在此基础上继续递归

4、是否要双重递归(即调用根节点的dfs函数后，继续调用根左右节点的pathsum函数)：看题目要不要求从根节点开始的，还是从任意节点开始

二、非自顶而下： 这类题目一般解题思路如下： 设计一个辅助函数maxpath，调用自身求出以一个节点为根节点的左侧最长路径left和右侧最长路径right，那么经过该节点的最长路径就是left+right 接着只需要从根节点开始dfs,不断比较更新全局变量即可

```cpp
int res=0;
int maxPath(TreeNode *root) //以root为路径起始点的最长路径
{
    if (!root)
        return 0;
    int left=maxPath(root->left);
    int right=maxPath(root->right);
    res = max(res, left + right + root->val); //更新全局变量  
    return max(left, right);   //返回左右路径较长者
}
```

这类题型DFS注意点： 1、left,right代表的含义要根据题目所求设置，比如最长路径、最大路径和等等

2、全局变量res的初值设置是0还是INT_MIN要看题目节点是否存在负值,如果存在就用INT_MIN，否则就是0

3、注意两点之间路径为1，因此一个点是不能构成路径的



## [437. 路径总和 III](https://leetcode.cn/problems/path-sum-iii/)

```c++
/*
时间
20 ms
击败
66.85%
内存
15.5 MB
击败
75.33%
时间复杂度：O(N^2)
空间复杂度：O(N)
*/
class Solution {
public:
    int pathSum(TreeNode* root, int targetSum) {
        if (root == nullptr) {
            return sum_;
        }
        dfs(root, targetSum);
        pathSum(root->left, targetSum);
        pathSum(root->right, targetSum);
        return sum_;
    }

private:
    void dfs(TreeNode* node, long targetSum) {
        if (node == nullptr) {
            return;
        }

        targetSum -= node->val;
        if (targetSum == 0) {
            ++sum_;
        }
        dfs(node->left, targetSum);
        dfs(node->right, targetSum);
    }

private:
    int sum_ = 0;
};

// 前缀和 https://leetcode.cn/problems/path-sum-iii/solutions/1021296/lu-jing-zong-he-iii-by-leetcode-solution-z9td/
/*
时间
详情
12ms
击败 93.24%使用 C++ 的用户
内存
详情
18.37mb
击败 22.48%使用 C++ 的用户

时间复杂度：O(n)
空间复杂度：O(N)
*/
class Solution {
public:
    int pathSum(TreeNode* root, int targetSum) {
        prefixSumCount_[0] = 1;
        return dfs(root, 0, targetSum);
    }

private:
    int dfs(TreeNode* node, long long curr, int targetSum) {
        if (node == nullptr) {
            return 0;
        }

        int ret = 0;
        curr += node->val;
        auto iter = prefixSumCount_.find(curr - targetSum);
        if (iter != prefixSumCount_.end()) {
            ret = iter->second;
        }

        ++prefixSumCount_[curr];
        ret += dfs(node->left, curr, targetSum);
        ret += dfs(node->right, curr, targetSum);
        --prefixSumCount_[curr];
        return ret;
    }

private:
    std::unordered_map<long long, int> prefixSumCount_;
};
```

## [988. 从叶结点开始的最小字符串](https://leetcode.cn/problems/smallest-string-starting-from-leaf/)

```cpp
#include <queue>
#include <numeric>
#include <stack>
#include "gtest/gtest.h"

using namespace std;

struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;

    TreeNode() : val(0), left(nullptr), right(nullptr) {}

    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}

    TreeNode(int x, TreeNode* left, TreeNode* right) : val(x), left(left), right(right) {}
};
/*
时间
详情
8ms
击败 88.31%使用 C++ 的用户
内存
详情
18.79mb
击败 45.02%使用 C++ 的用户
*/
class Solution {
public:
    string smallestFromLeaf(TreeNode* root) {
        path_.push_back('z' + 1);
        dfs(root, "");
        return path_;
    }

private:
    void dfs(TreeNode* node, std::string str) {
        if (node == nullptr) {
            return;
        }

        str.push_back((node->val + 'a'));
        if (node->left == nullptr && node->right == nullptr) {
            std::reverse(str.begin(), str.end());
            if (path_ > str) {
                path_.swap(str);
            }
        }
        dfs(node->left, str);
        dfs(node->right, str);
    }

private:
    std::string path_;
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::unique_ptr<TreeNode> roota = std::make_unique<TreeNode>(0);
    std::unique_ptr<TreeNode> nodeb = std::make_unique<TreeNode>(1);
    std::unique_ptr<TreeNode> nodec = std::make_unique<TreeNode>(2);
    std::unique_ptr<TreeNode> noded1 = std::make_unique<TreeNode>(3);
    std::unique_ptr<TreeNode> nodee1 = std::make_unique<TreeNode>(4);
    std::unique_ptr<TreeNode> noded2 = std::make_unique<TreeNode>(3);
    std::unique_ptr<TreeNode> nodee2 = std::make_unique<TreeNode>(4);
    roota->left = nodeb.get();
    roota->right = nodec.get();
    nodeb->left = noded1.get();
    nodeb->right = nodee1.get();
    nodec->left = noded2.get();
    nodec->right = nodee2.get();

    EXPECT_EQ("dba", solution.smallestFromLeaf(roota.get()));
}
```

## [124. 二叉树中的最大路径和](https://leetcode.cn/problems/binary-tree-maximum-path-sum/)

```cpp
/*
时间
详情
16ms
击败 95.85%使用 C++ 的用户
内存
详情
26.39mb
击败 39.03%使用 C++ 的用户
*/

class Solution {
public:
    int maxPathSum(TreeNode* root) {
        dfs(root);
        return maxSum_;
    }

private:
    int dfs(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }

        int left = std::max(dfs(node->left), 0);
        int right = std::max(dfs(node->right), 0);
        maxSum_ = std::max(maxSum_, left + right + node->val);
        return std::max(left, right) + node->val;
    }

private:
    int maxSum_ = INT_MIN;
};
```

## [687. 最长同值路径](https://leetcode.cn/problems/longest-univalue-path/)

```cpp
/*
时间
详情
112ms
击败 76.11%使用 C++ 的用户
内存
详情
68.45mb
击败 54.86%使用 C++ 的用户
*/
class Solution {
public:
    int longestUnivaluePath(TreeNode* root) {
        if (root == nullptr) {
            return longestPath_;
        }

        dfs(root);
        return longestPath_;
    }

private:
    int dfs(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }

        int left = dfs(node->left);
        int right = dfs(node->right);
        if (node->left != nullptr && node->val == node->left->val) {
            ++left;
        } else {
            left = 0;
        }
        if (node->right != nullptr && node->val == node->right->val) {
            ++right;
        } else {
            right = 0;
        }
        longestPath_ = std::max(longestPath_, left + right);
        return std::max(left, right);
    }

private:
    int longestPath_ = 0;
};
```

## [543. 二叉树的直径](https://leetcode.cn/problems/diameter-of-binary-tree/)

```cpp
/*
时间
详情
12ms
击败 46.30%使用 C++ 的用户
内存
详情
19.23mb
击败 74.59%使用 C++ 的用户
*/
class Solution {
public:
    int diameterOfBinaryTree(TreeNode* root) {
        if (root == nullptr) {
            return diameter_;
        }

        dfs(root);
        return diameter_;
    }

private:
    int dfs(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }

        int left = dfs(node->left);
        int right = dfs(node->right);
        diameter_ = std::max(diameter_, left + right);
        return std::max(left, right) + 1;
    }

private:
    int diameter_ = 0;
};

```



## [114. 二叉树展开为链表](https://leetcode.cn/problems/flatten-binary-tree-to-linked-list/)

```cpp
#include <stack>
#include "gtest/gtest.h"

using namespace std;

struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;

    TreeNode() : val(0), left(nullptr), right(nullptr) {}

    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}

    TreeNode(int x, TreeNode* left, TreeNode* right) : val(x), left(left), right(right) {}
};

class Solution {
public:
    void flatten(TreeNode* root) {
        if (root == nullptr) {
            return;
        }

        while (root != nullptr) {
            if (root->left != nullptr) {
                TreeNode* preNode = root->left;
                while (preNode->right != nullptr) {
                    preNode = preNode->right;
                }

                preNode->right = root->right;
                root->right = root->left;
                root->left = nullptr;
            }
            root = root->right;
        }
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    auto root1 = std::make_unique<TreeNode>(1);
    auto node2 = std::make_unique<TreeNode>(2);
    auto node3 = std::make_unique<TreeNode>(3);
    auto node4 = std::make_unique<TreeNode>(4);
    auto node5 = std::make_unique<TreeNode>(5);
    auto node6 = std::make_unique<TreeNode>(6);
    root1->left = node2.get();
    root1->right = node5.get();
    node2->left = node3.get();
    node2->right = node4.get();
    node5->right = node6.get();

    solution.flatten(root1.get());

    EXPECT_EQ(1, root1->val);
    EXPECT_EQ(2, root1->right->val);
    EXPECT_EQ(3, root1->right->right->val);
    EXPECT_EQ(4, root1->right->right->right->val);
    EXPECT_EQ(5, root1->right->right->right->right->val);
    EXPECT_EQ(6, root1->right->right->right->right->right->val);
}

// 使用迭代实现前序遍历
class Solution {
public:
    void flatten(TreeNode* root) {
        std::vector<TreeNode*> preOrders;
        std::stack<TreeNode*> stack;
        TreeNode* node = root;
        while (node != nullptr || !stack.empty()) {
            while (node != nullptr) {
                preOrders.push_back(node);
                stack.push(node);
                node = node->left;
            }

            node = stack.top();
            stack.pop();
            node = node->right;
        }

        for (int i = 1; i < preOrders.size(); ++i) {
            auto pre = preOrders.at(i - 1);
            auto cur = preOrders.at(i);
            pre->left = nullptr;
            pre->right = cur;
        }
    }
};
```

## [199. 二叉树的右视图](https://leetcode.cn/problems/binary-tree-right-side-view/)

```cpp
#include <stack>
#include "gtest/gtest.h"

using namespace std;

struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;

    TreeNode() : val(0), left(nullptr), right(nullptr) {}

    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}

    TreeNode(int x, TreeNode* left, TreeNode* right) : val(x), left(left), right(right) {}
};

// 自己写的错误的
class Solution {
public:
    vector<int> rightSideView(TreeNode* root) {
        std::vector<int> views;
        if (root == nullptr) {
            return views;
        }

        getRightViews(root, views);
        std::vector<int> leftViews;
        leftViews.push_back(root->val);
        getRightViews(root->left, leftViews);
        if (leftViews.size() > views.size()) {
            for (int i = 0; i < views.size(); ++i) {
                leftViews[i] = views.at(i);
            }
            swap(leftViews, views);
        }
        return views;
    }

private:
    void getRightViews(TreeNode* root, vector<int>& views) const {
        while (root != nullptr) {
            views.push_back(root->val);
            if (root->right != nullptr) {
                root = root->right;
            } else if (root->left != nullptr) {
                root = root->left;
            } else {
                root = nullptr;
            }
        }
    }
};
```

深度优先搜索

```cpp
class Solution {
public:
    vector<int> rightSideView(TreeNode* root) {
        std::vector<int> rightSideViews;
        std::stack<std::pair<TreeNode*, int>> nodeDepthStack;
        nodeDepthStack.push({root, 0});

        while (!nodeDepthStack.empty()) {
            auto nodeDepth = nodeDepthStack.top();
            nodeDepthStack.pop();
            auto node = nodeDepth.first;
            auto depth = nodeDepth.second;

            if (node != nullptr) {
                if (depth + 1 > rightSideViews.size()) {
                    rightSideViews.push_back(node->val);
                }

                nodeDepthStack.push({node->left, depth + 1});
                nodeDepthStack.push({node->right, depth + 1});
            }
        }
        return rightSideViews;
    }
};

//
class Solution {
public:
    vector<int> rightSideView(TreeNode* root) {
        std::vector<int> rightSideViews;
        dfs(root, 0, rightSideViews);
        return rightSideViews;
    }

private:
    void dfs(TreeNode* node, int depth, vector<int>& res) {
        if (node == nullptr) {
            return;
        }

        if (depth == res.size()) {
            res.push_back(node->val);
        }
        dfs(node->right, depth + 1, res);
        dfs(node->left, depth + 1, res);
    }
};
```

广度优先搜索

```cpp
class Solution {
public:
    vector<int> rightSideView(TreeNode* root) {
        std::vector<int> rightSideViews;
        std::queue<std::pair<TreeNode*, int>> nodeDepthQueue;
        nodeDepthQueue.push({root, 0});

        while (!nodeDepthQueue.empty()) {
            auto nodeDepth = nodeDepthQueue.front();
            nodeDepthQueue.pop();
            auto node = nodeDepth.first;
            auto depth = nodeDepth.second;

            if (node != nullptr) {
                if (depth + 1 > rightSideViews.size()) {
                    rightSideViews.push_back(node->val);
                } else {
                    rightSideViews[depth] = node->val;
                }

                nodeDepthQueue.push({node->left, depth + 1});
                nodeDepthQueue.push({node->right, depth + 1});
            }
        }
        return rightSideViews;
    }
};

// 记下每层个数，最后一个加入结果列表
class Solution {
public:
    vector<int> rightSideView(TreeNode* root) {
        std::vector<int> rightSideViews;
        if (root == nullptr) {
            return rightSideViews;
        }

        std::queue<TreeNode*> nodeQueue;
        nodeQueue.push(root);

        while (!nodeQueue.empty()) {
            int size = nodeQueue.size();
            for (int i = 0; i < size; ++i) {
                auto node = nodeQueue.front();
                nodeQueue.pop();

                if (node->left != nullptr) {
                    nodeQueue.push(node->left);
                }
                if (node->right != nullptr) {
                    nodeQueue.push(node->right);
                }

                if (i == size - 1) {
                    rightSideViews.push_back(node->val);
                }
            }
        }
        return rightSideViews;
    }
};
```

## [207. 课程表](https://leetcode.cn/problems/course-schedule/)

```cpp
#include <stack>
#include <unordered_map>
#include <queue>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    bool canFinish(int numCourses, vector<vector<int>>& prerequisites) {
        std::vector<int> visited(numCourses, 0);
        std::vector<std::vector<int>> adj(numCourses, std::vector<int>());
        for (const auto& prerequisite: prerequisites) {
            adj[prerequisite.at(1)].push_back(prerequisite.at(0));
        }

        for (int i = 0; i < numCourses; ++i) {
            if (visited.at(i) == TODO && !dfs(adj, visited, i)) {
                return false;
            }
        }
        return true;
    }

private:
    bool dfs(const vector<vector<int>>& adj, vector<int>& visited, int v) {
        if (visited.at(v) == FINISHED) {
            return true;
        } else if (visited.at(v) == WORKING) {
            return false;
        }

        visited.at(v) = WORKING;
        for (const auto& u: adj.at(v)) {
            if (!dfs(adj, visited, u)) {
                return false;
            }
        }

        visited.at(v) = FINISHED;
        return true;
    }

private:
    static const int TODO = 0;
    static const int WORKING = 1;
    static const int FINISHED = 2;
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::vector<vector<int>> prerequisites = {{1, 0}};
    EXPECT_TRUE(solution.canFinish(2, prerequisites));
}

TEST_F(SolutionTest, Test2) {
    std::vector<vector<int>> prerequisites = {{1, 0}, {0, 1}};
    EXPECT_FALSE(solution.canFinish(2, prerequisites));
}

TEST_F(SolutionTest, Test3) {
    std::vector<vector<int>> prerequisites = {{0, 1}};
    EXPECT_TRUE(solution.canFinish(2, prerequisites));
}
```

## [133. 克隆图](https://leetcode.cn/problems/clone-graph/)

```cpp
// Definition for a Node.
class Node {
public:
    int val;
    vector<Node*> neighbors;
    Node() {
        val = 0;
        neighbors = vector<Node*>();
    }
    Node(int _val) {
        val = _val;
        neighbors = vector<Node*>();
    }
    Node(int _val, vector<Node*> _neighbors) {
        val = _val;
        neighbors = _neighbors;
    }
};

/*
时间
详情
4ms
击败 85.54%使用 C++ 的用户
内存
详情
8.67mb
击败 18.97%使用 C++ 的用户
*/
class Solution {  //dfs
public:
    Node* cloneGraph(Node* node) {
        std::unordered_map<int, Node*> visited;
        return dfs(node, visited);
    }

private:
    Node* dfs(Node* node, unordered_map<int, Node*>& visited) {
        if (node == nullptr) {
            return nullptr;
        }

        auto visitedIter = visited.find(node->val);
        if (visitedIter != visited.end()) {
            return (*visitedIter).second;
        }

        Node* newNode = new Node(node->val);
        visited.emplace(node->val, newNode);

        for (const auto& neighbor: node->neighbors) {
            newNode->neighbors.push_back(dfs(neighbor, visited));
        }
        return newNode;
    }
};

/*
时间
详情
8ms
击败 33.59%使用 C++ 的用户
内存
详情
8.25mb
击败 85.87%使用 C++ 的用户
*/
class Solution {  //bfs
public:
    Node* cloneGraph(Node* node) {
        if (node == nullptr) {
            return nullptr;
        }

        std::queue<Node*> nodeQueue;
        nodeQueue.push(node);

        Node* newNode = new Node(node->val);
        std::unordered_map<int, Node*> visited = {{node->val, newNode}};
        while (!nodeQueue.empty()) {
            auto oriNode = nodeQueue.front();
            nodeQueue.pop();
            for (const auto& neighbor: oriNode->neighbors) {
                if (visited.find(neighbor->val) == visited.end()) {
                    nodeQueue.push(neighbor);
                    visited.emplace(neighbor->val, new Node(neighbor->val));
                }
                visited[oriNode->val]->neighbors.push_back(visited[neighbor->val]);
            }
        }
        return newNode;
    };
};
```

## [102. 目标和](https://leetcode.cn/problems/YaVDxD/)

dfs， 比较慢：

```cpp
#include "gtest/gtest.h"

using namespace std;

/*
时间
1816 ms
击败
5.9%
内存
8.7 MB
击败
87.72%

复杂度分析
时间复杂度：O(2^n)
空间复杂度：O(n)
*/
class Solution {  // dfs
public:
    int findTargetSumWays(vector<int>& nums, int target) {
        dfs(nums, 0, 0, target);
        return waysCount_;
    }

private:
    void dfs(const vector<int>& nums, int index, int sum, int target) {
        if (index == nums.size()) {
            if (sum == target) {
                ++waysCount_;
            }
            return;
        }

        dfs(nums, index + 1, sum + nums.at(index), target);
        dfs(nums, index + 1, sum - nums.at(index), target);
    }

private:
    int waysCount_ = 0;
};

// 别人优化的，减枝：https://leetcode.cn/problems/YaVDxD/solutions/1025570/jia-jian-de-mu-biao-zhi-by-leetcode-solu-be5t/
// 对于每个位置有正负两种选择，全部探索要2^20，是会超时的； 先求总和，然后选择哪些位置变减号； 进行剪枝，如果当前结果已经小于目标了，那他后续的探索都不可能达到目标，剪掉就行了；
class Solution {
public:
    int findTargetSumWays(vector<int>& nums, int target) {
        auto sum = std::accumulate(nums.begin(), nums.end(), 0);
        dfs(nums, target, 0, sum);
        return waysCount_;
    }

private:
    void dfs(const vector<int>& nums, int target, int index, int sum) {
        if (index == nums.size()) {
            if (sum == target) {
                ++waysCount_;
            }
            return;
        }

        if (sum < target) {
            return;
        }
        dfs(nums, target, index + 1, sum);
        dfs(nums, target, index + 1, sum - 2 * nums.at(index));
    }

private:
    int waysCount_ = 0;
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::vector<int> nums = {1, 1, 1, 1, 1};
    int target = 3;
    EXPECT_EQ(5, solution.findTargetSumWays(nums, target));
}

TEST_F(SolutionTest, Test2) {
    std::vector<int> nums = {1};
    int target = 1;
    EXPECT_EQ(1, solution.findTargetSumWays(nums, target));
}
```

动态规划，https://leetcode.cn/problems/YaVDxD/solutions/1025570/jia-jian-de-mu-biao-zhi-by-leetcode-solu-be5t/

```cpp
/*
时间
详情
4ms
击败 97.01%使用 C++ 的用户
内存
详情
11.55mb
击败 28.21%使用 C++ 的用户
*/
class Solution {
public:
    int findTargetSumWays(vector<int>& nums, int target) {
        int sum = 0;
        for (int& num : nums) {
            sum += num;
        }
        int diff = sum - target;
        if (diff < 0 || diff % 2 != 0) {
            return 0;
        }
        int n = nums.size(), neg = diff / 2;
        vector<vector<int>> dp(n + 1, vector<int>(neg + 1));
        dp[0][0] = 1;
        for (int i = 1; i <= n; i++) {  // 这里从1开始
            int num = nums[i - 1];  // 这里要注意i - 1
            for (int j = 0; j <= neg; j++) {  // 这里从0开始
                dp[i][j] = dp[i - 1][j];
                if (j >= num) {
                    dp[i][j] += dp[i - 1][j - num];
                }
            }
        }
        return dp[n][neg];
    }
};

// 优化空间
/*
时间
详情
8ms
击败 87.39%使用 C++ 的用户
内存
详情
8.76mb
击败 67.92%使用 C++ 的用户
*/
class Solution {
public:
    int findTargetSumWays(vector<int>& nums, int target) {
        int sum = 0;
        for (int& num : nums) {
            sum += num;
        }
        int diff = sum - target;
        if (diff < 0 || diff % 2 != 0) {
            return 0;
        }
        int neg = diff / 2;
        vector<int> dp(neg + 1);
        dp[0] = 1;
        for (int& num : nums) {
            for (int j = neg; j >= num; j--) {
                dp[j] += dp[j - num];
            }
        }
        return dp[neg];
    }
};
```

## [841. 钥匙和房间](https://leetcode.cn/problems/keys-and-rooms/)

参考：https://leetcode.cn/problems/keys-and-rooms/solutions/393524/yao-chi-he-fang-jian-by-leetcode-solution/

dfs：

```cpp
#include <queue>
#include <numeric>
#include <stack>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    bool canVisitAllRooms(vector<vector<int>>& rooms) {
        std::vector<bool> visited(rooms.size(), false);
        int visitedNums = 0;
        dfs(rooms, 0, visitedNums, visited);
        return visitedNums == rooms.size();
    }

private:
    void dfs(vector<vector<int>>& rooms, int roomIndex, int& visitedNums, std::vector<bool>& visited) {
        visited.at(roomIndex) = true;
        ++visitedNums;
        for (const auto& nextRoomIndex: rooms.at(roomIndex)) {
            if (!visited.at(nextRoomIndex)) {
                dfs(rooms, nextRoomIndex, visitedNums, visited);
            }
        }
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    vector<vector<int>> rooms = {{1}, {2}, {3}, {}};
    EXPECT_EQ(true, solution.canVisitAllRooms(rooms));
}

TEST_F(SolutionTest, Test2) {
    vector<vector<int>> rooms = {{1, 3}, {3, 0, 1}, {2}, {0}};
    EXPECT_EQ(false, solution.canVisitAllRooms(rooms));
}
```

bfs：

```cpp
class Solution {
public:
    bool canVisitAllRooms(vector<vector<int>>& rooms) {
        int n = rooms.size(), num = 0;
        vector<int> vis(n);
        queue<int> que;
        vis[0] = true;
        que.emplace(0);
        while (!que.empty()) {
            int x = que.front();
            que.pop();
            num++;
            for (auto& it : rooms[x]) {
                if (!vis[it]) {
                    vis[it] = true;
                    que.emplace(it);
                }
            }
        }
        return num == n;
    }
};

```



# 广度优先搜索

## [模板](https://leetcode.cn/leetbook/read/queue-stack/gp5a7/)

```java
/*
 * Return true if there is a path from cur to target.
 */
boolean DFS(int root, int target) {
    Set<Node> visited;
    Stack<Node> s;
    add root to s;
    while (s is not empty) {
        Node cur = the top element in s;
        return true if cur is target;
        for (Node next : the neighbors of cur) {
            if (next is not in visited) {
                add next to s;
                add next to visited;
            }
        }
        remove cur from s;
    }
    return false;
}
```



## [752. 打开转盘锁](https://leetcode.cn/problems/open-the-lock/)

参考：https://leetcode.cn/problems/open-the-lock/solutions/843687/da-kai-zhuan-pan-suo-by-leetcode-solutio-l0xo/

广度搜索每个4位数字的后续8个状态，加入队列中

```cpp
#include <stack>
#include <queue>
#include <unordered_set>
#include "gtest/gtest.h"

using namespace std;
/*
时间
详情
252ms
击败 23.79%使用 C++ 的用户
内存
详情
112.75mb
击败 5.05%使用 C++ 的用户
*/
class Solution {
public:
    int openLock(vector<string>& deadends, string target) {
        const std::string startStatus = "0000";

        if (target == startStatus) {
            return 0;
        }

        std::unordered_set<std::string> deadendsSet(deadends.begin(), deadends.end());
        if (deadendsSet.find(startStatus) != deadendsSet.end()) {
            return -1;
        }

        std::queue<std::pair<std::string, int>> statusStepQueue;
        statusStepQueue.emplace(startStatus, 0);
        std::unordered_set<std::string> seen = {startStatus};

        while (!statusStepQueue.empty()) {
            auto [status, step] = statusStepQueue.front();
            statusStepQueue.pop();

            const auto& nextStatusList = get(std::move(status));
            for (const auto& nextStatus: nextStatusList) {
                // seen在这里判断，可以减少很多对象加入queue，提高效率
                if (seen.find(nextStatus) == seen.end() && deadendsSet.find(nextStatus) == deadendsSet.end()) {
                    if (nextStatus == target) {
                        return step + 1;
                    }

                    statusStepQueue.emplace(nextStatus, step + 1);
                    seen.insert(nextStatus);
                }
            }
        }
        return -1;
    }

private:
    std::vector<std::string> get(std::string&& status) {
        std::vector<std::string> nextStatusList;
        for (int i = 0; i < status.size(); ++i) {
            char ch = status.at(i);

            status.at(i) = ch == '9' ? '0' : ch + 1;
            nextStatusList.push_back(status);

            status.at(i) = ch == '0' ? '9' : ch - 1;
            nextStatusList.push_back(status);

            status.at(i) = ch;
        }
        return nextStatusList;
    };
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::vector<std::string> deadends = {"0201", "0101", "0102", "1212", "2002"};
    std::string target = "0202";
    EXPECT_EQ(6, solution.openLock(deadends, target));
}

TEST_F(SolutionTest, Test2) {
    std::vector<std::string> deadends = {"8888"};
    std::string target = "0009";
    EXPECT_EQ(1, solution.openLock(deadends, target));
}

TEST_F(SolutionTest, Test3) {
    std::vector<std::string> deadends = {"8887", "8889", "8878", "8898", "8788", "8988", "7888", "9888"};
    std::string target = "8888";
    EXPECT_EQ(-1, solution.openLock(deadends, target));
}
```

启发式搜索（不懂，没有看）：

```cpp
struct AStar {
    // 计算启发函数
    static int getH(const string& status, const string& target) {
        int ret = 0;
        for (int i = 0; i < 4; ++i) {
            int dist = abs(int(status[i]) - int(target[i]));
            ret += min(dist, 10 - dist);
        }
        return ret;
    };

    AStar(const string& status, const string& target, int g): status_{status}, g_{g}, h_{getH(status, target)} {
        f_ = g_ + h_;
    }

    bool operator< (const AStar& that) const {
        return f_ > that.f_;
    }

    string status_;
    int f_, g_, h_;
};

/*
时间
详情
72ms
击败 82.37%使用 C++ 的用户
内存
详情
29.97mb
击败 74.47%使用 C++ 的用户
*/
class Solution {
public:
    int openLock(vector<string>& deadends, string target) {
        if (target == "0000") {
            return 0;
        }

        unordered_set<string> dead(deadends.begin(), deadends.end());
        if (dead.count("0000")) {
            return -1;
        }

        auto num_prev = [](char x) -> char {
            return (x == '0' ? '9' : x - 1);
        };
        auto num_succ = [](char x) -> char {
            return (x == '9' ? '0' : x + 1);
        };

        auto get = [&](string& status) -> vector<string> {
            vector<string> ret;
            for (int i = 0; i < 4; ++i) {
                char num = status[i];
                status[i] = num_prev(num);
                ret.push_back(status);
                status[i] = num_succ(num);
                ret.push_back(status);
                status[i] = num;
            }
            return ret;
        };

        priority_queue<AStar> q;
        q.emplace("0000", target, 0);
        unordered_set<string> seen = {"0000"};

        while (!q.empty()) {
            AStar node = q.top();
            q.pop();
            for (auto&& next_status: get(node.status_)) {
                if (!seen.count(next_status) && !dead.count(next_status)) {
                    if (next_status == target) {
                        return node.g_ + 1;
                    }
                    q.emplace(next_status, target, node.g_ + 1);
                    seen.insert(move(next_status));
                }
            }
        }

        return -1;
    }
};
```

## [107. 01 矩阵](https://leetcode.cn/problems/2bCMpM/)

```cpp
#include <queue>
#include <numeric>
#include <stack>
#include "gtest/gtest.h"

using namespace std;

class Solution {
private:
    static constexpr int dirs[4][2] = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

public:
    vector<vector<int>> updateMatrix(vector<vector<int>>& matrix) {
        int m = matrix.size(), n = matrix[0].size();
        vector<vector<int>> dist(m, vector<int>(n, -1));
        queue<pair<int, int>> q;
        // 将所有的 0 添加进初始队列中
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (matrix[i][j] == 0) {
                    q.emplace(i, j);
                    dist[i][j] = 0;
                }
            }
        }

        // 广度优先搜索
        while (!q.empty()) {
            auto [i, j] = q.front();
            q.pop();
            for (int d = 0; d < 4; ++d) {
                int ni = i + dirs[d][0];
                int nj = j + dirs[d][1];
                if (ni >= 0 && ni < m && nj >= 0 && nj < n && dist[ni][nj] == -1) {
                    dist[ni][nj] = dist[i][j] + 1;
                    q.emplace(ni, nj);
                }
            }
        }

        return dist;
    }
};

// 动态规划
// https://leetcode.cn/problems/2bCMpM/solutions/1412130/ju-zhen-zhong-de-ju-chi-by-leetcode-solu-0sxk/
class Solution {
public:
    vector<vector<int>> updateMatrix(vector<vector<int>>& matrix) {
        int m = matrix.size(), n = matrix[0].size();
        // 初始化动态规划的数组，所有的距离值都设置为一个很大的数
        vector<vector<int>> dist(m, vector<int>(n, INT_MAX / 2));
        // 如果 (i, j) 的元素为 0，那么距离为 0
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (matrix[i][j] == 0) {
                    dist[i][j] = 0;
                }
            }
        }
        // 只有 水平向左移动 和 竖直向上移动，注意动态规划的计算顺序
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (i - 1 >= 0) {
                    dist[i][j] = min(dist[i][j], dist[i - 1][j] + 1);
                }
                if (j - 1 >= 0) {
                    dist[i][j] = min(dist[i][j], dist[i][j - 1] + 1);
                }
            }
        }
        // 只有 水平向右移动 和 竖直向下移动，注意动态规划的计算顺序
        for (int i = m - 1; i >= 0; --i) {
            for (int j = n - 1; j >= 0; --j) {
                if (i + 1 < m) {
                    dist[i][j] = min(dist[i][j], dist[i + 1][j] + 1);
                }
                if (j + 1 < n) {
                    dist[i][j] = min(dist[i][j], dist[i][j + 1] + 1);
                }
            }
        }
        return dist;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    vector<vector<int>> mat = {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};
    vector<vector<int>> expected = {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};
    EXPECT_EQ(expected, solution.updateMatrix(mat));
}

TEST_F(SolutionTest, Test2) {
    vector<vector<int>> mat = {{0, 0, 0}, {0, 1, 0}, {1, 1, 1}};
    vector<vector<int>> expected = {{0, 0, 0}, {0, 1, 0}, {1, 2, 1}};
    EXPECT_EQ(expected, solution.updateMatrix(mat));
}

```



# 数组

## [15. 三数之和](https://leetcode-cn.com/problems/3sum/)(华为题库)

自己写的，参考第1题：两数之和的方法，依次判断每个数字取反后，是否可以由其它两个数字相加获得。（超时）

```java
//
package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Solution {  // 超时
    public List<List<Integer>> threeSum(int[] nums) {
        Set<List<Integer>> threeSet = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            twoSumForOneLoop(nums, -nums[i], i, threeSet);
        }
        return new ArrayList<>(threeSet);
    }

    private void twoSumForOneLoop(int[] nums, int target, int excludedIndex, Set<List<Integer>> threeSet) {
        Map<Integer, Integer> valuePosMap = new HashMap<>();

        for (int firstIndex = 0; firstIndex < nums.length; firstIndex++) {
            if (firstIndex == excludedIndex) {
                continue;
            }

            int firstValue = nums[firstIndex];
            int secondValue = target - firstValue;
            if (valuePosMap.containsKey(secondValue)) {
                List<Integer> combination = Arrays.asList(-target, firstValue, secondValue);
                Collections.sort(combination);
                threeSet.add(combination);
            } else {
                valuePosMap.put(firstValue, firstIndex);
            }
        }
    }
}

//
package test;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {
    private static final Solution SOLUTION = new Solution();

    @Test
    public void test1() {
        int[] nums = {-1, 0, 1, 2, -1, -4};
        List<List<Integer>> expected = Arrays.asList(Arrays.asList(-1, -1, 2), Arrays.asList(-1, 0, 1));
        assertEquals(expected, SOLUTION.threeSum(nums));
    }
}
```

别人的解法<https://leetcode.com/problems/3sum/discuss/448047/My-simple-java-solution>：

排序+双指针遍历 

```java
package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {  // 51ms
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();

        int n = nums.length;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0 || i + 3 > n) {  // 如果最小值已经大于0了，或者剩余不足3个值了，则无需处理
                return res;
            }
            if (i > 0 && nums[i] == nums[i - 1]) {  // 如果下一个值和前一个值相同，则无需处理，因为所有情况都由前一个i-1处理过了
                continue;
            }

            int target = -nums[i];
            int l = i + 1;
            int r = n - 1;
            while (l < r) {
                int actual = nums[l] + nums[r];
                if (actual == target) {
                    res.add(Arrays.asList(nums[i], nums[l], nums[r]));
                    do {
                        ++l;  // 跳过所有和当前l一样的值
                    } while (l < r && nums[l - 1] == nums[l]);
                    do {
                        --r;  // 跳过所有和当前r一样的值
                    } while (l < r && nums[r + 1] == nums[r]);
                } else if (actual < target) {  // 如果实际值小于target，则增加小值
                    l++;
                } else {  // 如果实际值大于target，则减少大值
                    r--;
                }
            }
        }
        return res;
    }
}
```

```cpp
#include <unordered_map>
#include <algorithm>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    vector<vector<int>> threeSum(vector<int>& nums) {
        std::vector<std::vector<int>> res;
        std::sort(nums.begin(), nums.end());
        for (int i = 0; i < nums.size(); ++i) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            const auto& twoSumRes = twoSum(nums, -nums[i], i + 1, nums.size() - 1);
            res.insert(res.end(), twoSumRes.begin(), twoSumRes.end());
        }
        return res;
    }

private:
    std::vector<std::vector<int>> twoSum(const vector<int>& nums, int target, int start, int end) {
        std::vector<std::vector<int>> res;
        while (start < end) {
            int sum = nums[start] + nums[end];
            if (sum == target) {
                res.push_back({-target, nums[start], nums[end]});
                while (start < end && nums[start] == nums[start + 1]) {
                    ++start;
                }
                ++start;
                while (start < end && nums[end - 1] == nums[end]) {
                    --end;
                }
                --end;
            } else if (sum < target) {
                ++start;
            } else {
                --end;
            }
        }
        return res;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::vector<int> nums = {-1,0,1,2,-1,-4};
    std::vector<std::vector<int>> expected = {{-1,-1,2},{-1,0,1}};
    EXPECT_EQ(expected, solution.threeSum(nums));
}

```

## [79. 单词搜索](https://leetcode.cn/problems/word-search/)

```c++
#include <algorithm>
#include "gtest/gtest.h"

using namespace std;

// 超时
class Solution {
public:
    bool exist(vector<vector<char>>& board, string word) {
        int m = board.size();
        int n = board[0].size();
        vector<vector<bool>> visited(m, vector<bool>(n, false));
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (dfs(board, visited, i, j, word, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

private:
    bool dfs(const vector<vector<char>>& board, vector<vector<bool>>& visited, int i, int j, const string& word,
             int charIndex) {
        if (board[i][j] != word.at(charIndex)) {
            return false;
        }
        if (charIndex == word.size() - 1) {
            return true;
        }

        visited[i][j] = true;
        std::vector<std::pair<int, int>> directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        bool result = false;
        for (const auto& direction: directions) {
            int newI = i + direction.first;
            int newJ = j + direction.second;
            if (newI >= 0 && newI < board.size() && newJ >= 0 && newJ <= board[0].size()) {
                if (visited[newI][newJ]) {
                    continue;
                }

                if (dfs(board, visited, newI, newJ, word, charIndex + 1)) {
                    result = true;
                    break;
                }
            }
        }
        visited[i][j] = false;
        return result;
    }
};

// 优化下写法，就可以通过
class Solution {  // 380ms
public:
    bool exist(vector<vector<char>>& board, string word) {
        int m = board.size();
        int n = board[0].size();
        vector<vector<bool>> visited(m, vector<bool>(n, false));
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (dfs(board, visited, i, j, word, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

private:
    bool dfs(const vector<vector<char>>& board, vector<vector<bool>>& visited, int i, int j, const string& word,
             int charIndex) {
        if (i < 0 || i >= board.size() || j < 0 || j >= board[0].size() || visited[i][j] ||
            board[i][j] != word.at(charIndex)) {
            return false;
        }

        if (charIndex == word.size() - 1) {
            return true;
        }

        visited[i][j] = true;
        bool result = false;
        if (dfs(board, visited, i - 1, j, word, charIndex + 1) ||
            dfs(board, visited, i + 1, j, word, charIndex + 1) ||
            dfs(board, visited, i, j - 1, word, charIndex + 1) ||
            dfs(board, visited, i, j + 1, word, charIndex + 1)
            ) {
            result = true;
        }

        visited[i][j] = false;
        return result;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    vector<vector<char>> board = {{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}};
    std::string word = "ABCCED";
    EXPECT_EQ(true, solution.exist(board, word));
}

TEST_F(SolutionTest, Test2) {
    vector<vector<char>> board = {{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}};
    std::string word = "SEE";
    EXPECT_EQ(true, solution.exist(board, word));
}

TEST_F(SolutionTest, Test3) {
    vector<vector<char>> board = {{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}};
    std::string word = "ABCB";
    EXPECT_EQ(false, solution.exist(board, word));
}
```

## [31. 下一个排列](https://leetcode.cn/problems/next-permutation/)

参考：https://leetcode.cn/problems/next-permutation/solutions/479151/xia-yi-ge-pai-lie-by-leetcode-solution/

注意到下一个排列总是比当前排列要大，除非该排列已经是最大的排列。我们希望找到一种方法，能够找到一个大于当前序列的新序列，且变大的幅度尽可能小。具体地：

我们需要将一个左边的「较小数」与一个右边的「较大数」交换，以能够让当前排列变大，从而得到下一个排列。

同时我们要让这个「较小数」尽量靠右，而「较大数」尽可能小。当交换完成后，「较大数」右边的数需要按照升序重新排列。这样可以在保证新排列大于原来排列的情况下，使变大的幅度尽可能小。

```cpp
#include <unordered_map>
#include <algorithm>
#include <queue>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    void nextPermutation(vector<int>& nums) {
        int i = nums.size() - 2;
        while (i >= 0 && nums[i] >= nums[i + 1]) {
            --i;
        }
        if (i >= 0) {
            int j = nums.size() - 1;
            while (j >= 0 && nums[i] > nums[j]) {
                --j;
            }
            swap(nums[i], nums[j]);
        }
        std::reverse(nums.begin() + i + 1, nums.end());
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    vector<int> nums = {1, 2, 3};
    solution.nextPermutation(nums);
    vector<int> expected = {1, 3, 2};
    EXPECT_EQ(expected, nums);
}

TEST_F(SolutionTest, Test2) {
    vector<int> nums = {3, 2, 1};
    solution.nextPermutation(nums);
    vector<int> expected = {1, 2, 3};
    EXPECT_EQ(expected, nums);
}

TEST_F(SolutionTest, Test3) {
    vector<int> nums = {4, 5, 2, 6, 3, 1};
    solution.nextPermutation(nums);
    vector<int> expected = {4, 5, 3, 1, 2, 6};
    EXPECT_EQ(expected, nums);
}
```

## [3. 无重复字符的最长子串](https://leetcode.cn/problems/longest-substring-without-repeating-characters/)

```java
package leetcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * https://www.cnblogs.com/grandyang/p/4606334.html
 * 3    Longest Substring Without Repeating Characters
 */
public class LongestSubstring {
    public int lengthOfLongestSubstring(String s) {
        int[] m = new int[256];
        Arrays.fill(m, -1);
        int res = 0, left = -1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            left = Math.max(left, m[c]); //取滑窗左边位置
            m[c] = i;
            res = Math.max(res, i - left); //取res和当前滑窗的最大长度
        }
        return res;
    }

    public int lengthOfLongestSubstringWithMap(String s) {
        Map<Character, Integer> characterPosMap = new HashMap<>();
        int res = 0, left = -1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            left = Math.max(left, characterPosMap.getOrDefault(c, -1)); //取滑窗左侧位置
            characterPosMap.put(c, i);
            res = Math.max(res, i - left); //取res和当前滑窗的最大长度
        }
        return res;
    }

    public int lengthOfLongestSubstringWithSet(String s) {
        int res = 0, left = 0, right = 0;
        Set<Character> characterSet = new HashSet<>();
        while (right < s.length()) {
            char c = s.charAt(right);
            if (!characterSet.contains(c)) {
                characterSet.add(c);
                right++;
                res = Math.max(res, characterSet.size());
            } else {
                characterSet.remove(s.charAt(left++)); //只要发现right对应的字符串在滑窗(即Set)中存在，就不断往右移滑窗左侧，直到滑窗不包含right字符
            }
        }
        return res;
    }
}

//test
package leetcode;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LongestSubstringTest {

    private static final LongestSubstring LONGEST_SUBSTRING = new LongestSubstring();

    @Test
    public void lengthOfLongestSubstring() {
        String s = "abcabcbb";
        int length = 3;
        assertEquals(length, LONGEST_SUBSTRING.lengthOfLongestSubstringWithMap(s));
    }

    @Test
    public void lengthOfLongestSubstring2() {
        String s = "bbbbb";
        int length = 1;
        assertEquals(length, LONGEST_SUBSTRING.lengthOfLongestSubstringWithMap(s));
    }

    @Test
    public void lengthOfLongestSubstring3() {
        String s = "pwwkew";
        int length = 3;
        assertEquals(length, LONGEST_SUBSTRING.lengthOfLongestSubstringWithMap(s));
    }
}
```



```cpp
#include <unordered_map>
#include <algorithm>
#include <queue>
#include <unordered_set>
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 36ms
public:
    std::string getMaxUniqueSubString(const std::string& inputStr) {
        std::unordered_set<char> uniqueChars;
        size_t maxLength = 0;
        std::string maxUniqueSubString;

        int leftIndex = 0;
        int rightIndex = 0;

        while (rightIndex < inputStr.size()) {
            char c = inputStr.at(rightIndex);
            if (uniqueChars.find(c) == uniqueChars.end()) {
                uniqueChars.insert(c);
                ++rightIndex;
                continue;
            }

            if (uniqueChars.size() > maxLength) {
                maxLength = uniqueChars.size();
                maxUniqueSubString = inputStr.substr(leftIndex, rightIndex - leftIndex);
            }

            while (inputStr.at(leftIndex) != c) {
                uniqueChars.erase(s.at(leftIndex));
                ++leftIndex;
            }
            uniqueChars.erase(inputStr.at(leftIndex));
            ++leftIndex;
        }

        if (uniqueChars.size() > maxLength) {
            maxLength = uniqueChars.size();
            maxUniqueSubString = inputStr.substr(leftIndex, rightIndex - leftIndex);
        }
        return maxUniqueSubString;
    }
};

// 上面算法的优化
class Solution {
public:
    int lengthOfLongestSubstring(string s) {
        unordered_set<char> uniqueChars;
        int longestLength = 0, right = 0, left = 0;
        int strLength = s.length();
        while (right < strLength) {
            char c = s.at(right);
            if (uniqueChars.find(c) == uniqueChars.end()) {
                uniqueChars.insert(c);
                ++right;
                longestLength = max<int>(longestLength, uniqueChars.size());
            } else {
                uniqueChars.erase(s.at(left++));
            }
        }
        return longestLength;
    }
};

class Solution {  // 8ms
public:
    int lengthOfLongestSubstring(string s) {
        std::array<int, 256> charPos;  // 1个char是8位，所在最大值为2^8=256
        charPos.fill(-1);

        int maxLength = 0, left = -1;
        for (int i = 0; i < s.size(); ++i) {
            char c = s.at(i);
            left = std::max(left, charPos.at(c));  // 下标-1表示这个字符第1次出现，如果不为-1，说明已经出现过，则左滑窗要移到这个位置
            charPos.at(c) = i;  // 记录每个字符的最后出现的下标
            maxLength = std::max(maxLength, i - left);  // 取左右滑窗的距离与最大值比较
        }
        return maxLength;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::string inputStr = "abcade";
    std::string expected = "bcade";
    EXPECT_EQ(expected, solution.getMaxUniqueSubString(inputStr));
}

TEST_F(SolutionTest, Test2) {
    std::string inputStr = "aaaa";
    std::string expected = "a";
    EXPECT_EQ(expected, solution.getMaxUniqueSubString(inputStr));
}

TEST_F(SolutionTest, Test3) {
    std::string inputStr = "aaabcdefod";
    std::string expected = "abcdefo";
    EXPECT_EQ(expected, solution.getMaxUniqueSubString(inputStr));
}
```

# 贪心

## [45. 跳跃游戏 II](https://leetcode.cn/problems/jump-game-ii/)

参考：https://leetcode.cn/problems/jump-game-ii/solutions/230241/tiao-yue-you-xi-ii-by-leetcode-solution/

每次跳跃前，都在可以跳到的位置里找到最远的位置去跳

```cpp
#include <unordered_map>
#include <algorithm>
#include <queue>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    int jump(vector<int>& nums) {
        int maxPos = 0, n = nums.size(), end = 0, step = 0;
        for (int i = 0; i < n - 1; ++i) {
            if (maxPos >= i) {  // 这里的判断可以不写，题目保证可以跳到最后 
                maxPos = max(maxPos, i + nums[i]);
                if (i == end) {
                    end = maxPos;
                    ++step;
                }
            }
        }
        return step;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    vector<int> nums = {2,3,1,1,4};
    EXPECT_EQ(2, solution.jump(nums));
}

TEST_F(SolutionTest, Test2) {
    vector<int> nums = {2,3,1,2,4,2,3};
    EXPECT_EQ(3, solution.jump(nums));
}
```



## [134. 加油站](https://leetcode.cn/problems/gas-station/)

直观理解，不用公式推导。可以这样想：假设从x加油站出发经过z加油站最远能到达y加油站，那么从z加油站直接出发，不可能到达y下一个加油站。因为从x出发到z加油站时肯定还有存储的油，这都到不了y的下一站，而直接从z出发刚开始是没有存储的油的，所以更不可能到达y的下一站。

```cpp
#include <unordered_map>
#include <algorithm>
#include <queue>
#include <unordered_set>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    int canCompleteCircuit(vector<int>& gas, vector<int>& cost) {
        int length = gas.size();
        int index = 0;
        int resStartIndex = -1;
        while (index < length) {  // 从index位置出发
            int gasSum = 0, costSum = 0;
            int count = 0;
            while (count < length) {  // 找到从index出发可以到达的最远位置
                int tmpIndex = (index + count) % length;
                gasSum += gas.at(tmpIndex);
                costSum += cost.at(tmpIndex);
                if (gasSum < costSum) {
                    break;
                }
                ++count;
            }

            if (count == length) {  // 判断从index出发可以走过几个加油站
                resStartIndex = index;
                break;
            } else {
                index += count + 1;
            }
        }
        return resStartIndex;
    }
};

// https://leetcode.cn/problems/gas-station/solutions/488357/jia-you-zhan-by-leetcode-solution/
class Solution {
public:
    int canCompleteCircuit(vector<int>& gas, vector<int>& cost) {
        int n = gas.size();
        int bottom = 0, sum = 0, startid = -1;//startid为起点前一个点
        //bottom为最低点油量，sum为当前油量
        for (int i = 0; i < n; i++) {
            sum = sum + gas[i] - cost[i];
            if (sum < bottom) {  // 这个bottom越小，说明消耗越大，越需要从下一个节点出发，以尽可能补充积累油
                bottom = sum;
                startid = i;
            }
        }
        if (sum < 0) {
            return -1;
        }
        return startid + 1;
    }
};

// 
class Solution {
public:
    int canCompleteCircuit(vector<int>& gas, vector<int>& cost) {
        int maxIndex = -1;
        int max = INT_MIN;
        int sum = 0;
        for (int i = gas.size() - 1; i >= 0; --i) {
            sum += gas.at(i) - cost.at(i);  // 从后遍历，哪个加油站出发可以累积最多的油，说明这里出发可以绕圈
            if (sum > max) {
                maxIndex = i;
                max = sum;
            }
        }
        return sum >= 0 ? maxIndex : -1;
    }
};


class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::vector<int> gas = {1, 2, 3, 4, 5};
    std::vector<int> cost = {3, 4, 5, 1, 2};
    EXPECT_EQ(3, solution.canCompleteCircuit(gas, cost));
}

TEST_F(SolutionTest, Test2) {
    std::vector<int> gas = {2, 3, 4};
    std::vector<int> cost = {3, 4, 3};
    EXPECT_EQ(-1, solution.canCompleteCircuit(gas, cost));
}
```

## [179. 最大数](https://leetcode.cn/problems/largest-number/)

这个算法其实就是对这些数字组成的字符串做了个倒序排序，而大小的原则就是左右两个字符串相加比右左相加更大，则最后拼接的字符串就会更大：

```cpp
#include <algorithm>
#include <numeric>
#include "gtest/gtest.h"

using namespace std;
/*
时间
详情
4ms
击败 93.99%使用 C++ 的用户
内存
详情
11.02mb
击败 16.33%使用 C++ 的用户
*/
class Solution {
public:
    string largestNumber(vector<int>& nums) {
        std::vector<std::string> numsStr;
        std::for_each(nums.begin(), nums.end(), [&](const auto& num) {
            numsStr.push_back(std::to_string(num));
        });
        /*
         std::vector<std::string> numsStr(nums.size());
        std::transform(nums.begin(), nums.end(), numsStr.begin(), [](const auto& num) { return std::to_string(num); });
		*/

        std::sort(numsStr.begin(), numsStr.end(), [](const auto& lhs, const auto& rhs) {
            return lhs + rhs > rhs + lhs;
        });

        if (numsStr.at(0) == "0") {
            return "0";
        }

        return std::accumulate(numsStr.begin(), numsStr.end(), std::string());
    }
};

/*
时间
详情
4ms
击败 93.99%使用 C++ 的用户
内存
详情
10.60mb
击败 60.19%使用 C++ 的用户
*/
class Solution {
public:
    string largestNumber(vector<int>& nums) {
        sort(nums.begin(), nums.end(), [](const int& x, const int& y) {
            unsigned long long sx = 10, sy = 10;
            while (sx <= x) {
                sx *= 10;
            }
            while (sy <= y) {
                sy *= 10;
            }
            return sy * x + y > sx * y + x;
        });
        if (nums[0] == 0) {
            return "0";
        }
        string ret;
        for (int& x: nums) {
            ret += to_string(x);
        }
        return ret;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::vector<int> nums = {10, 2};
    EXPECT_EQ("210", solution.largestNumber(nums));
}

TEST_F(SolutionTest, Test2) {
    std::vector<int> nums = {3, 30, 34, 5, 9};
    EXPECT_EQ("9534330", solution.largestNumber(nums));
}
```

## [316. 去除重复字母](https://leetcode.cn/problems/remove-duplicate-letters/)

参考：https://leetcode.cn/problems/remove-duplicate-letters/solutions/527359/qu-chu-zhong-fu-zi-mu-by-leetcode-soluti-vuso/

```cpp
#include "gtest/gtest.h"

using namespace std;
/*
时间
详情
4ms
击败 62.10%使用 C++ 的用户
内存
详情
6.12mb
击败 94.38%使用 C++ 的用户
*/
class Solution {
public:
    string removeDuplicateLetters(string s) {
        std::array<bool, 26> visited;
        visited.fill(false);  // 每个字符是否在栈中

        std::array<int, 26> charsPos;
        charsPos.fill(-1);
        for (int i = 0; i < s.size(); ++i) {
            charsPos.at(s.at(i) - 'a') = i;  // 记录每个字符的最后位置
        }

        std::string res;
        for (int i = 0; i < s.size(); ++i) {
            char c = s.at(i);
            if (visited.at(c - 'a')) {  // 如果字符已经在栈中了，则跳过。因为已在栈中的这个字符一定不是单调递增字母的最后一个，如果是最后一个，那么之前的相同字母就已经会被出栈舍弃掉。所以这个字符之前有比它大的字符不会再出现，不能出栈舍弃。或者可以这样理解，这个字符再怎么调整，也不会出现一个更小的字母序列。
                continue;
            }
			
            // 看栈顶字符是不是在后面还有。如果有的话，要是比当前字符大，则可以出栈舍弃
            while (!res.empty() && charsPos.at(res.back() - 'a') > i && res.back() > c) {
                char top = res.back();
                res.pop_back();
                visited.at(top - 'a') = false;
            }

            res.push_back(c);
            visited.at(s.at(i) - 'a') = true;
        }
        return res;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    EXPECT_EQ("abc", solution.removeDuplicateLetters("bcabc"));
}

TEST_F(SolutionTest, Test2) {
    EXPECT_EQ("acdb", solution.removeDuplicateLetters("cbacdcbc"));
}
```

同316一样思路的：402和321

## [402. 移掉 K 位数字](https://leetcode.cn/problems/remove-k-digits/)

参考：https://leetcode.cn/problems/remove-duplicate-letters/solutions/1/yi-zhao-chi-bian-li-kou-si-dao-ti-ma-ma-zai-ye-b-4/

```cpp
#include "gtest/gtest.h"

using namespace std;
/*
时间
详情
8ms
击败 85.97%使用 C++ 的用户
内存
详情
8.34mb
击败 67.97%使用 C++ 的用户
*/
class Solution {
public:
    string removeKdigits(string num, int k) {
        std::string res;
        int remain = num.size() - k;
        for (const auto& ch: num) {  // 生成一个最小的升序序列
            while (k > 0 && !res.empty() && res.back() > ch) {
                res.pop_back();
                --k;
            }
            res.push_back(ch);
        }
        res.erase(remain);
        res.erase(0, res.find_first_not_of("0"));
        return res.empty() ? "0" : res;
    }
};

/*
时间
详情
4ms
击败 98.66%使用 C++ 的用户
内存
详情
7.99mb
击败 94.43%使用 C++ 的用户
*/
class Solution {
public:
    string removeKdigits(string num, int k) {
        int remain = num.size() - k;
        std::string res(remain, '0');
        int top = -1;
        for (const auto& ch: num) {  // 生成一个最小的升序序列
            while (k > 0 && top >= 0 && res.at(top) > ch) {
                --top;
                --k;  // 出栈则删除一个
            }
            if (top + 1 < remain) {
                res.at(++top) = ch;
            } else {
                --k;  // 栈满则当前数字不保留，要丢弃，也删除一个
            }
        }
        res.erase(0, res.find_first_not_of("0"));
        return res.empty() ? "0" : res;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    EXPECT_EQ("1219", solution.removeKdigits("1432219", 3));
}

TEST_F(SolutionTest, Test2) {
    EXPECT_EQ("200", solution.removeKdigits("10200", 1));
}

TEST_F(SolutionTest, Test3) {
    EXPECT_EQ("0", solution.removeKdigits("10", 2));
}

TEST_F(SolutionTest, Test4) {
    EXPECT_EQ("0", solution.removeKdigits("9", 1));
}
```

## [321. 拼接最大数](https://leetcode.cn/problems/create-maximum-number/)

参考：https://leetcode.cn/problems/remove-duplicate-letters/solutions/1/yi-zhao-chi-bian-li-kou-si-dao-ti-ma-ma-zai-ye-b-4/

```cpp
#include "gtest/gtest.h"

using namespace std;

/*
时间
详情
124ms
击败 18.59%使用 C++ 的用户
内存
详情
26.25mb
击败 40.90%使用 C++ 的用户
*/
class Solution {
public:
    vector<int> maxNumber(vector<int>& nums1, vector<int>& nums2, int k) {
        std::vector<int> res;
        for (int i = 0; i <= k; ++i) {
            if (i > nums1.size() || (k - i) > nums2.size()) {
                continue;
            }

            std::vector<int>&& maxNum1 = pickMax(nums1, i);
            std::vector<int>&& maxNum2 = pickMax(nums2, k - i);
            std::vector<int>&& mergeNum = mergeMax(std::move(maxNum1), std::move(maxNum2));
            if (mergeNum > res) {
                res = std::move(mergeNum);
            }
        }
        return res;
    }

private:
    std::vector<int> pickMax(const std::vector<int>& nums, int pickNum) {
        std::vector<int> res;
        int deleteNum = nums.size() - pickNum;
        for (const auto& num: nums) {
            while (!res.empty() && deleteNum > 0 && res.back() < num) {
                res.pop_back();
                --deleteNum;
            }
            res.push_back(num);  // 这里慢
        }
        res.erase(res.begin() + pickNum, res.end());  // 这里慢
        return res;
    }

    std::vector<int> mergeMax(std::vector<int>&& nums1, std::vector<int>&& nums2) {
        std::vector<int> res;
        // 这里对vector删除元素慢
        while (!nums1.empty() || !nums2.empty()) {
            auto& bigger = nums1 > nums2 ? nums1 : nums2;
            res.push_back(bigger.at(0));
            bigger.erase(bigger.begin());
        }
        return res;
    }

    /*std::vector<int> mergeMax(const std::vector<int>& nums1, const std::vector<int>& nums2) {
        std::vector<int> res;
        int i = 0, j = 0;
        // 这里只比较了vector的第1位，没有比较后面的数字，是有问题的，并不能完整的做merge
        for (; i < nums1.size() && j < nums2.size();) {
            if (nums1.at(i) >= nums2.at(j)) {
                res.push_back(nums1.at(i));
                ++i;
            } else {
                res.push_back(nums2.at(j));
                ++j;
            }
        }

        if (i == nums1.size()) {
            res.insert(res.end(), nums2.begin() + j, nums2.end());
        } else if (j == nums2.size()) {
            res.insert(res.end(), nums1.begin() + i, nums1.end());
        }
        return res;
    }*/
};

// 参考官方题解：https://leetcode.cn/problems/create-maximum-number/solutions/505931/pin-jie-zui-da-shu-by-leetcode-solution/
class Solution {
public:
    vector<int> maxNumber(vector<int>& nums1, vector<int>& nums2, int k) {
        std::vector<int> maxSubsequence(k, 0);
        int start = std::max<int>(0, k - nums2.size()), end = min<int>(k, nums1.size());
        for (int i = start; i <= end; ++i) {
            std::vector<int>&& maxNum1 = pickMax(nums1, i);
            std::vector<int>&& maxNum2 = pickMax(nums2, k - i);
            std::vector<int>&& curMaxSubsequence = merge(maxNum1, maxNum2);
            if (compare(curMaxSubsequence, 0, maxSubsequence, 0) > 0) {
                maxSubsequence.swap(curMaxSubsequence);
            }
        }
        return maxSubsequence;
    }

private:
    std::vector<int> pickMax(const std::vector<int>& nums, int pick) {
        int length = nums.size();
        // 固定要选pick个，所以预先构造好长度为pick的stack。这里已经知道长度了，就用vector+top来模拟栈。不用使用stack来入栈和出栈，是为了提高效率。
        vector<int> stack(pick, 0);
        int top = -1;  // 栈顶位置
        int toDelete = length - pick;
        for (int i = 0; i < length; i++) {
            int num = nums[i];
            while (top >= 0 && stack[top] < num && toDelete > 0) {
                top--;
                toDelete--;
            }
            if (top < pick - 1) {  // top为栈顶索引，top+1即为栈内个数，当个数小于要选的个数pick时，就可以入栈
                stack[++top] = num;
            } else {
                toDelete--;
            }
        }
        return stack;
    }

    vector<int> merge(vector<int>& subsequence1, vector<int>& subsequence2) {
        int x = subsequence1.size(), y = subsequence2.size();
        if (x == 0) {
            return subsequence2;
        }
        if (y == 0) {
            return subsequence1;
        }
        int mergeLength = x + y;
        vector<int> merged(mergeLength);
        int index1 = 0, index2 = 0;
        for (int i = 0; i < mergeLength; i++) {
            if (compare(subsequence1, index1, subsequence2, index2) > 0) {
                merged[i] = subsequence1[index1++];
            } else {
                merged[i] = subsequence2[index2++];
            }
        }
        return merged;
    }

    int compare(vector<int>& subsequence1, int index1, vector<int>& subsequence2, int index2) {
        int x = subsequence1.size(), y = subsequence2.size();
        while (index1 < x && index2 < y) {
            int difference = subsequence1[index1] - subsequence2[index2];
            if (difference != 0) {
                return difference;
            }
            index1++;
            index2++;
        }
        return (x - index1) - (y - index2);
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::vector<int> nums1 = {3, 4, 6, 5};
    std::vector<int> nums2 = {9, 1, 2, 5, 8, 3};
    std::vector<int> expected = {9, 8, 6, 5, 3};
    EXPECT_EQ(expected, solution.maxNumber(nums1, nums2, 5));
}

TEST_F(SolutionTest, Test2) {
    std::vector<int> nums1 = {6, 7};
    std::vector<int> nums2 = {6, 0, 4};
    std::vector<int> expected = {6, 7, 6, 0, 4};
    EXPECT_EQ(expected, solution.maxNumber(nums1, nums2, 5));
}

TEST_F(SolutionTest, Test3) {
    std::vector<int> nums1 = {3, 9};
    std::vector<int> nums2 = {8, 9};
    std::vector<int> expected = {9, 8, 9};
    EXPECT_EQ(expected, solution.maxNumber(nums1, nums2, 3));
}

TEST_F(SolutionTest, Test4) {
    std::vector<int> nums1 = {2, 5, 6, 4, 4, 0};
    std::vector<int> nums2 = {7, 3, 8, 0, 6, 5, 7, 6, 2};
    std::vector<int> expected = {7, 3, 8, 2, 5, 6, 4, 4, 0, 6, 5, 7, 6, 2, 0};
    EXPECT_EQ(expected, solution.maxNumber(nums1, nums2, 15));
}
```

# 二叉树

# 栈

## [155. 最小栈](https://leetcode.cn/problems/min-stack/)

1. 辅助栈：记录每个元素入栈后，当前最小值，与元素一起入栈出栈。使用额外空间O(n)。

2. 不使用额外空间：参考：[155. 最小栈 - 力扣（LeetCode）](https://leetcode.cn/problems/min-stack/solutions/242190/zui-xiao-zhan-by-leetcode-solution/) 

   ```python
   【不使用辅助栈的Java解法】
   栈中每个元素代表的是要压入元素与当前栈中最小值的差值
   有个很重要问题：
   在弹出时如何维护min？
   因为每次压入新的元素时，压入的都是与当前栈中最小值的差值（还未压入当前元素），故在弹出元素时，若弹出了当前最小值，因为栈中记录了当前元素与【之前】最小值的差值，故根据这个记录可以更新弹出元素后的最小值。
   ```

```java
class MinStack {
    // 记录每个元素与【未压入】该元素时栈中最小元素的差值
    LinkedList<Long> stack;
    // 当前【已压入】栈中元素的最小值
    private long min;
    public MinStack() {
        stack = new LinkedList();
    }
    
    public void push(int val) {
        // 压入第一个元素
        if(stack.isEmpty()){
            min = val;
            stack.addFirst(0L);
            return;
        }
        // 栈不为空时，每次压入计算与min的差值后压入结果
        stack.push((long)val-min);
        // 更新min
        min = Math.min((long)val,min);
        // 上面两个语句是不能颠倒的！一定是先压入，在更新，因为min一定是当前栈中的最小值
    }
    
    public void pop() {
        long pop = stack.removeFirst();
        // 当弹出元素小于0时，说明弹出元素是当前栈中的最小值，要更新最小值
        if(pop<0){
            // 因为对于当前弹出的元素而言，计算压入栈中的值时，计算的是该元素与【未压入】该元素时
            // 栈中元素的最小值的差值，故弹出该元素后栈中的最小值就是未压入该元素时的最小值
            // 即当前元素的值（min）减去两者的差值
            long lastMin = min;
            min = lastMin - pop;
        }
        // 若大于等于0，不会对min有影响
    }
    
    public int top() {
        long peek = stack.peek();
        // 若当前栈顶小于等于0，说明最小值就是栈顶元素
        if(peek<=0) return (int)min;
        // 否则就是min+peek
        return (int)(min+peek);
    }
    
    public int getMin() {
        return (int)min;
    }
}
```

## [739. 每日温度](https://leetcode-cn.com/problems/daily-temperatures/)(华为题库)

用双循环遍历，比较慢：

```c++
class Solution {  // 2312ms
public:
    vector<int> dailyTemperatures(vector<int>& T) {
        vector<int> days;
        for (auto tempIter = T.begin(); tempIter != T.end(); ++tempIter) {
            auto iterGreater = find_if(tempIter + 1, T.end(), [=](int temperature) { return temperature > *tempIter; });
            days.push_back(iterGreater == T.end() ? 0 : distance(tempIter, iterGreater));
        }
        return move(days);
    }
};
```

优化代码：

<https://leetcode-cn.com/problems/daily-temperatures/solution/mei-ri-wen-du-by-leetcode/>

温度总共就30到100，把每个温度值都记录在next向量中，每来一个温度，就查找比它高的全部温度，看哪个温度的位置离它最近：

```c++
class Solution {  // 256ms
public:
    vector<int> dailyTemperatures(vector<int>& T) {
        vector<int> ans(T.size(), 0);
        vector<int> next(101, numeric_limits<int>::max());
        for (int i = T.size() - 1; i >= 0; --i) {
            int warmer_index = numeric_limits<int>::max();
            for (int t = T[i] + 1; t <= 100; ++t) {
                if (next[t] < warmer_index) {
                    warmer_index = next[t];
                }
            }
            if (warmer_index < numeric_limits<int>::max()) {
                ans[i] = warmer_index - i;
            }
            next[T[i]] = i;
        }
        return move(ans);
    }
};
```

使用栈的方法（如果某些数据在使用后就没用了，可以考虑用栈）：

<https://leetcode-cn.com/problems/daily-temperatures/solution/mei-ri-wen-du-by-leetcode/>

stack里存的自顶向下递增的元素的索引，倒序每新来一个元素，就把栈里比它小的元素全部弹出（因为当前元素索引又小，值又比早入栈的元素大，所以早入栈的那些元素在之后的比较中不会起任何作用），此时栈顶的元素即是值刚刚比它大，索引又最小的元素，再把该新元素入栈：

```c++
class Solution {
public:
    vector<int> dailyTemperatures(vector<int>& T) {
        vector<int> ans(T.size(), 0);
        stack<int> stack;
        for (int i = T.size() - 1; i >= 0; --i) {
            while (!stack.empty() && T[i] >= T[stack.top()]) {
                stack.pop();
            }
            ans[i] = stack.empty() ? 0 : stack.top() - i;
            stack.push(i);
        }

        return move(ans);
    }
};
```

# [动态规划解题套路框架](https://labuladong.gitee.io/algo/1/5/)

首先，虽然动态规划的核心思想就是穷举求最值，但是问题可以千变万化，穷举所有可行解其实并不是一件容易的事，需要你熟练掌握递归思维，只有列出**正确的「状态转移方程」**，才能正确地穷举。而且，你需要判断算法问题是否**具备「最优子结构」**，是否能够通过子问题的最值得到原问题的最值。另外，动态规划问题**存在「重叠子问题」**，如果暴力穷举的话效率会很低，所以需要你使用「备忘录」或者「DP table」来优化穷举过程，避免不必要的计算。

以上提到的重叠子问题、最优子结构、状态转移方程就是动态规划三要素。

提供总结的一个思维框架，辅助你思考状态转移方程：

**明确 base case -> 明确「状态」-> 明确「选择」 -> 定义 `dp` 数组/函数的含义**。

按上面的套路走，最后的解法代码就会是如下的框架：

```python
# 自顶向下递归的动态规划
def dp(状态1, 状态2, ...):
    for 选择 in 所有可能的选择:
        # 此时的状态已经因为做了选择而改变
        result = 求最值(result, dp(状态1, 状态2, ...))
    return result

# 自底向上迭代的动态规划
# 初始化 base case
dp[0][0][...] = base case
# 进行状态转移
for 状态1 in 状态1的所有取值：
    for 状态2 in 状态2的所有取值：
        for ...
            dp[状态1][状态2][...] = 求最值(选择1，选择2...)
```

至此，带备忘录的递归解法的效率已经和迭代的动态规划解法一样了。实际上，这种解法和常见的动态规划解法已经差不多了，只不过这种解法是「自顶向下」进行「递归」求解，我们更常见的动态规划代码是「自底向上」进行「递推」求解。

啥叫「自顶向下」？注意我们刚才画的递归树（或者说图），是从上向下延伸，都是从一个规模较大的原问题比如说 `f(20)`，向下逐渐分解规模，直到 `f(1)` 和 `f(2)` 这两个 base case，然后逐层返回答案，这就叫「自顶向下」。

啥叫「自底向上」？反过来，我们直接从最底下、最简单、问题规模最小、已知结果的 `f(1)` 和 `f(2)`（base case）开始往上推，直到推到我们想要的答案 `f(20)`。这就是「递推」的思路，这也是动态规划一般都脱离了递归，而是由循环迭代完成计算的原因。

**3、`dp` 数组的迭代（递推）解法**

有了上一步「备忘录」的启发，我们可以把这个「备忘录」独立出来成为一张表，通常叫做 DP table，在这张表上完成「自底向上」的推算岂不美哉！

```java
int fib(int N) {
    if (N == 0) return 0;
    int[] dp = new int[N + 1];
    // base case
    dp[0] = 0; dp[1] = 1;
    // 状态转移
    for (int i = 2; i <= N; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }

    return dp[N];
}
```

[![img](https://labuladong.gitee.io/algo/images/%e5%8a%a8%e6%80%81%e8%a7%84%e5%88%92%e8%af%a6%e8%a7%a3%e8%bf%9b%e9%98%b6/4.jpg)](https://labuladong.gitee.io/algo/images/动态规划详解进阶/4.jpg)

画个图就很好理解了，而且你发现这个 DP table 特别像之前那个「剪枝」后的结果，只是反过来算而已。实际上，带备忘录的递归解法中的「备忘录」，最终完成后就是这个 DP table，所以说这两种解法其实是差不多的，大部分情况下，效率也基本相同。

这里，引出「状态转移方程」这个名词，实际上就是描述问题结构的数学形式：

![img](https://labuladong.gitee.io/algo/images/%e5%8a%a8%e6%80%81%e8%a7%84%e5%88%92%e8%af%a6%e8%a7%a3%e8%bf%9b%e9%98%b6/fib.png)

为啥叫「状态转移方程」？其实就是为了听起来高端。

`f(n)` 的函数参数会不断变化，所以你把参数 `n` 想做一个状态，这个状态 `n` 是由状态 `n - 1` 和状态 `n - 2` 转移（相加）而来，这就叫状态转移，仅此而已。

你会发现，上面的几种解法中的所有操作，例如 `return f(n - 1) + f(n - 2)`，`dp[i] = dp[i - 1] + dp[i - 2]`，以及对备忘录或 DP table 的初始化操作，都是围绕这个方程式的不同表现形式。

可见列出「状态转移方程」的重要性，它是解决问题的核心，而且很容易发现，其实状态转移方程直接代表着暴力解法。

**千万不要看不起暴力解，动态规划问题最困难的就是写出这个暴力解，即状态转移方程**。

只要写出暴力解，优化方法无非是用备忘录或者 DP table，再无奥妙可言。

这个例子的最后，讲一个细节优化。

细心的读者会发现，根据斐波那契数列的状态转移方程，当前状态只和之前的两个状态有关，其实并不需要那么长的一个 DP table 来存储所有的状态，只要想办法存储之前的两个状态就行了。

所以，可以进一步优化，把空间复杂度降为 O(1)。这也就是我们最常见的计算斐波那契数的算法：

```python
class Solution:
    def fib(self, n):
        if n == 0 or n == 1:
            return n

        dp_i_1, dp_i_2 = 1, 0
        for i in range(2, n + 1):
            dp_i = dp_i_1 + dp_i_2
            dp_i_2, dp_i_1 = dp_i_1, dp_i
        return dp_i
```



## 二、凑零钱问题

```python
class Solution:
    _INT_MAX = 1 << 31

    def coinChange(self, coins, amount):
        amount_coin_count = dict()
        return self._dp(coins, amount, amount_coin_count)

    def _dp(self, coins, amount, amount_coin_count):
        if amount == 0:
            return 0
        if amount <= 0:
            return -1

        coin_count = amount_coin_count.get(amount)
        if coin_count is not None:
            return coin_count

        res = self._INT_MAX
        for coin in coins:
            sub_problem = self._dp(coins, amount - coin, amount_coin_count)
            if sub_problem < 0:
                continue
            res = min(res, sub_problem + 1)

        coin_count = -1 if res == self._INT_MAX else res
        amount_coin_count.setdefault(amount, coin_count)
        return coin_count


class TestSolution(unittest.TestCase):
    _SOLUTION = Solution()

    def test_solution1(self):
        coins = [1, 2, 5]
        amount = 11
        expected_count = 3
        self.assertEqual(expected_count, self._SOLUTION.coinChange(coins, amount))
```

自底向上：

```python
class Solution:
    def coinChange(self, coins, amount):
        dp = [amount + 1] * (amount + 1)
        dp[0] = 0
        for i in range(0, amount + 1):
            for coin in coins:
                if i - coin < 0:
                    continue
                dp[i] = min(dp[i], 1 + dp[i - coin])
        return -1 if dp[amount] == amount + 1 else dp[amount]
```

### 三、最后总结

第一个斐波那契数列的问题，解释了如何通过「备忘录」或者「dp table」的方法来优化递归树，并且明确了这两种方法本质上是一样的，只是自顶向下和自底向上的不同而已。

第二个凑零钱的问题，展示了如何流程化确定「状态转移方程」，只要通过状态转移方程写出暴力递归解，剩下的也就是优化递归树，消除重叠子问题而已。

如果你不太了解动态规划，还能看到这里，真得给你鼓掌，相信你已经掌握了这个算法的设计技巧。

**计算机解决问题其实没有任何奇技淫巧，它唯一的解决办法就是穷举**，穷举所有可能性。算法设计无非就是先思考“如何穷举”，然后再追求“如何聪明地穷举”。

列出状态转移方程，就是在解决“如何穷举”的问题。之所以说它难，一是因为很多穷举需要递归实现，二是因为有的问题本身的解空间复杂，不那么容易穷举完整。

备忘录、DP table 就是在追求“如何聪明地穷举”。用空间换时间的思路，是降低时间复杂度的不二法门，除此之外，试问，还能玩出啥花活？

之后我们会有一章专门讲解动态规划问题，如果有任何问题都可以随时回来重读本文，希望读者在阅读每个题目和解法时，多往「状态」和「选择」上靠，才能对这套框架产生自己的理解，运用自如。

## [动态规划设计：300. 最长递增子序列](https://labuladong.gitee.io/algo/3/24/74/)

### 一、动态规划解法

动态规划的核心设计思想是数学归纳法。

相信大家对数学归纳法都不陌生，高中就学过，而且思路很简单。比如我们想证明一个数学结论，那么**我们先假设这个结论在 `k < n` 时成立，然后根据这个假设，想办法推导证明出 `k = n` 的时候此结论也成立**。如果能够证明出来，那么就说明这个结论对于 `k` 等于任何数都成立。

类似的，我们设计动态规划算法，不是需要一个 dp 数组吗？我们可以假设 `dp[0...i-1]` 都已经被算出来了，然后问自己：怎么通过这些结果算出 `dp[i]`？

直接拿最长递增子序列这个问题举例你就明白了。不过，首先要定义清楚 dp 数组的含义，即 `dp[i]` 的值到底代表着什么？

**我们的定义是这样的：`dp[i]` 表示以 `nums[i]` 这个数结尾的最长递增子序列的长度**。

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import unittest
from typing import List

'''
执行用时：
3388 ms
, 在所有 Python3 提交中击败了
37.19%
的用户
内存消耗：
15.1 MB
, 在所有 Python3 提交中击败了
81.77%
的用户
'''
class Solution:
    def lengthOfLIS(self, nums: List[int]) -> int:
        dp = [1] * len(nums)
        for i in range(0, len(nums)):
            for j in range(0, i):
                if nums[i] > nums[j]:
                    dp[i] = max(dp[i], dp[j] + 1)

        res = 0
        for i in range(0, len(dp)):
            res = max(res, dp[i])
        return res


class TestSolution(unittest.TestCase):
    _SOLUTION = Solution()

    def test_solution1(self):
        nums = [10, 9, 2, 5, 3, 7, 101, 18]
        expected_length = 4
        self.assertEqual(expected_length, self._SOLUTION.lengthOfLIS(nums))

    def test_solution2(self):
        nums = [0, 1, 0, 3, 2, 3]
        expected_length = 4
        self.assertEqual(expected_length, self._SOLUTION.lengthOfLIS(nums))

    def test_solution3(self):
        nums = [7, 7, 7, 7, 7, 7, 7]
        expected_length = 1
        self.assertEqual(expected_length, self._SOLUTION.lengthOfLIS(nums))

    def test_solution4(self):
        nums = [6, 3, 5, 10, 11, 2, 9, 14, 13, 7, 4, 8, 12]
        expected_length = 5
        self.assertEqual(expected_length, self._SOLUTION.lengthOfLIS(nums))
```

### 二、二分查找解法

这个解法的时间复杂度为 `O(NlogN)`，但是说实话，正常人基本想不到这种解法（也许玩过某些纸牌游戏的人可以想出来）。所以大家了解一下就好，正常情况下能够给出动态规划解法就已经很不错了。

根据题目的意思，我都很难想象这个问题竟然能和二分查找扯上关系。其实最长递增子序列和一种叫做 patience game 的纸牌游戏有关，甚至有一种排序方法就叫做 patience sorting（耐心排序）。

为了简单起见，后文跳过所有数学证明，通过一个简化的例子来理解一下算法思路。

首先，给你一排扑克牌，我们像遍历数组那样从左到右一张一张处理这些扑克牌，最终要把这些牌分成若干堆。

[![img](https://labuladong.gitee.io/algo/images/%e6%9c%80%e9%95%bf%e9%80%92%e5%a2%9e%e5%ad%90%e5%ba%8f%e5%88%97/poker1.jpeg)](https://labuladong.gitee.io/algo/images/最长递增子序列/poker1.jpeg)

**处理这些扑克牌要遵循以下规则**：

只能把点数小的牌压到点数比它大的牌上；如果当前牌点数较大没有可以放置的堆，则新建一个堆，把这张牌放进去；如果当前牌有多个堆可供选择，则选择最左边的那一堆放置。

```python
'''
执行用时：
88 ms
, 在所有 Python3 提交中击败了
73.27%
的用户
内存消耗：
15 MB
, 在所有 Python3 提交中击败了
99.30%
的用户
'''
class Solution:
    def lengthOfLIS(self, nums: List[int]) -> int:
        top = [0] * len(nums)
        # 牌堆数初始化为0
        piles = 0
        for i in range(0, len(nums)):
            # 要处理的扑克牌
            poker = nums[i]

            # 搜索左侧边界的二分查找
            left, right = 0, piles
            while left < right:
                mid = int((left + right) / 2)
                if top[mid] > poker:
                    right = mid
                elif top[mid] < poker:
                    left = mid + 1
                else:
                    right = mid

            # 没找到合适的牌堆，新建一堆
            if left == piles:
                piles += 1

            # 把这张牌放到牌堆顶
            top[left] = poker

        # 牌堆数就是 LIS 长度
        return piles
```

暴力递归

```cpp
#include "gtest/gtest.h"

using namespace std;

/*
时间：O(2^n * n) 有2^n个子序列（每个数可取或不取），每个子序列遍历一次n
*/
class Solution {  // 超时
public:
    int lengthOfLIS(vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }

        int lis = 1;
        int n = nums.size();
        for (int i = 0; i < n; ++i) {
            lis = std::max(lis, dfs(nums, i));
        }
        return lis;
    }

private:
    int dfs(const std::vector<int>& nums, int index) {
        if (index == nums.size() - 1) {
            return 1;
        }

        int lis = 1;
        for (int j = index + 1; j < nums.size(); ++j) {
            if (nums[j] > nums[index]) {  //
                lis = std::max(lis, 1 + dfs(nums, j));
            }
        }
        return lis;
    }
};

//  带备忘录
class Solution {  // 288 ms
public:
    int lengthOfLIS(vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }

        int lis = 1;
        int n = nums.size();
        std::vector<int> memo(n, 0);

        for (int i = 0; i < n; ++i) {
            lis = std::max(lis, dfs(nums, i, memo));
        }
        return lis;
    }

private:
    int dfs(const std::vector<int>& nums, int index, std::vector<int>& memo) {
        if (index == nums.size() - 1) {
            return 1;
        }

        if (memo[index] > 0) {
            return memo[index];
        }

        int lis = 1;
        for (int j = index + 1; j < nums.size(); ++j) {
            if (nums[j] > nums[index]) {  //
                lis = std::max(lis, 1 + dfs(nums, j, memo));
            }
        }
        memo[index] = lis;
        return lis;
    }
};

// 动态规划
// 时间：O(n^2)
class Solution {  // 280ms
public:
    int lengthOfLIS(vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }

        int lis = 1;
        int n = nums.size();
        std::vector<int> dp(n, 1);

        for (int i = n - 1; i >= 0; --i) {
            for (int j = i + 1; j < n; ++j) {
                if (nums[j] > nums[i]) {
                    dp[i] = std::max(dp[i], dp[j] + 1);
                }
            }
            lis = std::max(lis, dp[i]);
        }
        return lis;
    }
};

class SolutionTest1 : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest1, Test1) {
    std::vector<int> nums = {10, 9, 2, 5, 3, 7, 101, 18};
    EXPECT_EQ(4, solution.lengthOfLIS(nums));
}
```





### [354. 俄罗斯套娃信封问题 - 力扣（LeetCode）](https://leetcode.cn/problems/russian-doll-envelopes/submissions/)

**这道题目其实是最长递增子序列的一个变种，因为每次合法的嵌套是大的套小的，相当于在二维平面中找一个最长递增的子序列，其长度就是最多能嵌套的信封个数**。

前面说的标准 LIS 算法只能在一维数组中寻找最长子序列，而我们的信封是由 `(w, h)` 这样的二维数对形式表示的，如何把 LIS 算法运用过来呢？

[![img](https://labuladong.gitee.io/algo/images/%e4%bf%a1%e5%b0%81%e5%b5%8c%e5%a5%97/0.jpg)](https://labuladong.gitee.io/algo/images/信封嵌套/0.jpg)

读者也许会想，通过 `w × h` 计算面积，然后对面积进行标准的 LIS 算法。但是稍加思考就会发现这样不行，比如 `1 × 10` 大于 `3 × 3`，但是显然这样的两个信封是无法互相嵌套的。

这道题的解法比较巧妙：

**先对宽度 `w` 进行升序排序，如果遇到 `w` 相同的情况，则按照高度 `h` 降序排序；之后把所有的 `h` 作为一个数组，在这个数组上计算 LIS 的长度就是答案**。

画个图理解一下，先对这些数对进行排序：

[![img](https://labuladong.gitee.io/algo/images/%e4%bf%a1%e5%b0%81%e5%b5%8c%e5%a5%97/1.jpg)](https://labuladong.gitee.io/algo/images/信封嵌套/1.jpg)

然后在 `h` 上寻找最长递增子序列，这个子序列就是最优的嵌套方案：

[![img](https://labuladong.gitee.io/algo/images/%e4%bf%a1%e5%b0%81%e5%b5%8c%e5%a5%97/2.jpg)](https://labuladong.gitee.io/algo/images/信封嵌套/2.jpg)

为什么呢？稍微思考一下就明白了：

首先，对宽度 `w` 从小到大排序，确保了 `w` 这个维度可以互相嵌套，所以我们只需要专注高度 `h` 这个维度能够互相嵌套即可。

其次，两个 `w` 相同的信封不能相互包含，所以对于宽度 `w` 相同的信封，对高度 `h` 进行降序排序，保证 LIS 中不存在多个 `w` 相同的信封（因为题目说了长宽相同也无法嵌套）。

下面看解法代码：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import unittest
from typing import List

'''
执行用时：
600 ms
, 在所有 Python3 提交中击败了
9.58%
的用户
内存消耗：
44.2 MB
, 在所有 Python3 提交中击败了
72.11%
的用户
'''
class Solution:
    def maxEnvelopes(self, envelopes: List[List[int]]) -> int:
        envelopes.sort(key=lambda envelope: (envelope[0], -envelope[1]))
        heights = list(map(lambda envelope: envelope[1], envelopes))
        return self._lengthOfLIS(heights)

    def _lengthOfLIS(self, nums: List[int]) -> int:
        top = [0] * len(nums)
        # 牌堆数初始化为0
        piles = 0
        for i in range(0, len(nums)):
            # 要处理的扑克牌
            poker = nums[i]

            # 搜索左侧边界的二分查找
            left, right = 0, piles
            while left < right:
                mid = int((left + right) / 2)
                if top[mid] > poker:
                    right = mid
                elif top[mid] < poker:
                    left = mid + 1
                else:
                    right = mid

            # 没找到合适的牌堆，新建一堆
            if left == piles:
                piles += 1

            # 把这张牌放到牌堆顶
            top[left] = poker

        # 牌堆数就是 LIS 长度
        return piles


class TestSolution(unittest.TestCase):
    _SOLUTION = Solution()

    def test_solution1(self):
        envelopes = [[6, 4], [6, 7], [1, 8], [2, 3], [5, 2], [5, 4]]
        expected_length = 3
        self.assertEqual(expected_length, self._SOLUTION.maxEnvelopes(envelopes))

    def test_solution2(self):
        nums = [[5, 4], [6, 4], [6, 7], [2, 3]]
        expected_length = 3
        self.assertEqual(expected_length, self._SOLUTION.maxEnvelopes(nums))

    def test_solution3(self):
        nums = [[1, 1], [1, 1], [1, 1]]
        expected_length = 1
        self.assertEqual(expected_length, self._SOLUTION.maxEnvelopes(nums))
```

为了清晰，我将代码分为了两个函数， 你也可以合并，这样可以节省下 `height` 数组的空间。

由于增加了测试用例，这里必须使用二分搜索版的 `lengthOfLIS` 函数才能通过所有测试用例。这样的话算法的时间复杂度为 `O(NlogN)`，因为排序和计算 LIS 各需要 `O(NlogN)` 的时间，加到一起还是 `O(NlogN)`；空间复杂度为 `O(N)`，因为计算 LIS 的函数中需要一个 `top` 数组。

### [53. 最大子数组和 - 力扣（LeetCode）](https://leetcode.cn/problems/maximum-subarray/)

力扣第 53 题「 [最大子序和](https://leetcode.cn/problems/maximum-subarray/)」问题和前文讲过的 [经典动态规划：最长递增子序列](https://labuladong.gitee.io/algo/3/24/74/) 的套路非常相似，代表着一类比较特殊的动态规划问题的思路，题目如下：

给你输入一个整数数组 `nums`，请你找在其中找一个和最大的子数组，返回这个子数组的和。函数签名如下：

```java
int maxSubArray(int[] nums);
```

比如说输入 `nums = [-3,1,3,-1,2,-4,2]`，算法返回 5，因为最大子数组 `[1,3,-1,2]` 的和为 5。

其实第一次看到这道题，我首先想到的是 [滑动窗口算法](https://labuladong.gitee.io/algo/2/18/25/)，因为我们前文说过嘛，滑动窗口算法就是专门处理子串/子数组问题的，这里不就是子数组问题么？

但是，稍加分析就发现，**这道题还不能用滑动窗口算法，因为数组中的数字可以是负数**。

滑动窗口算法无非就是双指针形成的窗口扫描整个数组/子串，但关键是，你得清楚地知道什么时候应该移动右侧指针来扩大窗口，什么时候移动左侧指针来减小窗口。而对于这道题目，你想想，当窗口扩大的时候可能遇到负数，窗口中的值也就可能增加也可能减少，这种情况下不知道什么时机去收缩左侧窗口，也就无法求出「最大子数组和」。

#### 动态规划思路

解决这个问题可以用动态规划技巧解决，但是 `dp` 数组的定义比较特殊。按照我们常规的动态规划思路，一般是这样定义 `dp` 数组：

**`nums[0..i]` 中的「最大的子数组和」为 `dp[i]`**。

如果这样定义的话，整个 `nums` 数组的「最大子数组和」就是 `dp[n-1]`。如何找状态转移方程呢？按照数学归纳法，假设我们知道了 `dp[i-1]`，如何推导出 `dp[i]` 呢？

如下图，按照我们刚才对 `dp` 数组的定义，`dp[i] = 5` ，也就是等于 `nums[0..i]` 中的最大子数组和：

[![img](https://labuladong.gitee.io/algo/images/%e6%9c%80%e5%a4%a7%e5%ad%90%e6%95%b0%e7%bb%84/1.jpeg)](https://labuladong.gitee.io/algo/images/最大子数组/1.jpeg)

那么在上图这种情况中，利用数学归纳法，你能用 `dp[i]` 推出 `dp[i+1]` 吗？

**实际上是不行的，因为子数组一定是连续的，按照我们当前 `dp` 数组定义，并不能保证 `nums[0..i]` 中的最大子数组与 `nums[i+1]` 是相邻的**，也就没办法从 `dp[i]` 推导出 `dp[i+1]`。

所以说我们这样定义 `dp` 数组是不正确的，无法得到合适的状态转移方程。对于这类子数组问题，我们就要重新定义 `dp` 数组的含义：

**以 `nums[i]` 为结尾的「最大子数组和」为 `dp[i]`**。

这种定义之下，想得到整个 `nums` 数组的「最大子数组和」，不能直接返回 `dp[n-1]`，而需要遍历整个 `dp` 数组：

```java
int res = Integer.MIN_VALUE;
for (int i = 0; i < n; i++) {
    res = Math.max(res, dp[i]);
}
return res;
```

依然使用数学归纳法来找状态转移关系：假设我们已经算出了 `dp[i-1]`，如何推导出 `dp[i]` 呢？

可以做到，`dp[i]` 有两种「选择」，要么与前面的相邻子数组连接，形成一个和更大的子数组；要么不与前面的子数组连接，自成一派，自己作为一个子数组。

如何选择？既然要求「最大子数组和」，当然选择结果更大的那个啦：

```java
// 要么自成一派，要么和前面的子数组合并
dp[i] = Math.max(nums[i], nums[i] + dp[i - 1]);
```

综上，我们已经写出了状态转移方程，就可以直接写出解法了：

```java
int maxSubArray(int[] nums) {
    int n = nums.length;
    if (n == 0) return 0;
    // 定义：dp[i] 记录以 nums[i] 为结尾的「最大子数组和」
    int[] dp = new int[n];
    // base case
    // 第一个元素前面没有子数组
    dp[0] = nums[0];
    // 状态转移方程
    for (int i = 1; i < n; i++) {
        dp[i] = Math.max(nums[i], nums[i] + dp[i - 1]);
    }
    // 得到 nums 的最大子数组
    int res = Integer.MIN_VALUE;
    for (int i = 0; i < n; i++) {
        res = Math.max(res, dp[i]);
    }
    return res;
}
```

以上解法时间复杂度是 O(N)，空间复杂度也是 O(N)，较暴力解法已经很优秀了，不过**注意到 `dp[i]` 仅仅和 `dp[i-1]` 的状态有关**，那么我们可以施展前文 [动态规划的降维打击：空间压缩技巧](https://labuladong.gitee.io/algo/3/23/70/) 讲的技巧进行进一步优化，将空间复杂度降低：

```java
int maxSubArray(int[] nums) {
    int n = nums.length;
    if (n == 0) return 0;
    // base case
    int dp_0 = nums[0];
    int dp_1 = 0, res = dp_0;

    for (int i = 1; i < n; i++) {
        // dp[i] = max(nums[i], nums[i] + dp[i-1])
        dp_1 = Math.max(nums[i], nums[i] + dp_0);
        dp_0 = dp_1;
        // 顺便计算最大的结果
        res = Math.max(res, dp_1);
    }
    
    return res;
}
```

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import unittest
from typing import List

'''
执行用时：
256 ms
, 在所有 Python3 提交中击败了
9.46%
的用户
内存消耗：
26.1 MB
, 在所有 Python3 提交中击败了
31.03%
的用户
'''
class Solution:
    def maxSubArray(self, nums: List[int]) -> int:
        dp = [0] * len(nums)
        dp[0], res = nums[0], nums[0]
        for i in range(1, len(nums)):
            dp[i] = max(nums[i], dp[i - 1] + nums[i])
            res = max(res, dp[i])
        return res


class TestSolution(unittest.TestCase):
    _SOLUTION = Solution()

    def test_solution1(self):
        nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
        expected_length = 6
        self.assertEqual(expected_length, self._SOLUTION.maxSubArray(nums))

    def test_solution2(self):
        nums = [1]
        expected_length = 1
        self.assertEqual(expected_length, self._SOLUTION.maxSubArray(nums))

    def test_solution3(self):
        nums = [5, 4, -1, 7, 8]
        expected_length = 23
        self.assertEqual(expected_length, self._SOLUTION.maxSubArray(nums))
```

#### 前缀和思路

在动态规划解法中，我们通过状态转移方程推导以 `nums[i]` 结尾的最大子数组和，其实用前文 [小而美的算法技巧：前缀和数组](https://labuladong.gitee.io/algo/2/18/22/) 讲过的前缀和数组也可以达到相同的效果。

回顾一下，前缀和数组 `preSum` 就是 `nums` 元素的累加和，`preSum[i+1] - preSum[j]` 其实就是子数组 `nums[j..i]` 之和（根据 `preSum` 数组的实现，索引 0 是占位符，所以 `i` 有一位索引偏移）。

**那么反过来想，以 `nums[i]` 为结尾的最大子数组之和是多少？其实就是 `preSum[i+1] - min(preSum[0..i])`**。

所以，我们可以利用前缀和数组计算以每个元素结尾的子数组之和，进而得到和最大的子数组：

```java
// 前缀和技巧解题
int maxSubArray(int[] nums) {
    int n = nums.length;
    int[] preSum = new int[n + 1];
    preSum[0] = 0;
    // 构造 nums 的前缀和数组
    for (int i = 1; i <= n; i++) {
        preSum[i] = preSum[i - 1] + nums[i - 1];
    }
    
    int res = Integer.MIN_VALUE;
    int minVal = Integer.MAX_VALUE;
    for (int i = 0; i < n; i++) {
        // 维护 minVal 是 preSum[0..i] 的最小值
        minVal = Math.min(minVal, preSum[i]);
        // 以 nums[i] 结尾的最大子数组和就是 preSum[i+1] - min(preSum[0..i])
        res = Math.max(res, preSum[i + 1] - minVal);
    }
    return res;
}
```

至此，前缀和解法也完成了。

简单总结下动态规划解法吧，虽然说状态转移方程确实有点玄学，但大部分还是有些规律可循的，跑不出那几个套路。像子数组、子序列这类问题，你就可以尝试定义 `dp[i]` 是以 `nums[i]` 为结尾的最大子数组和/最长递增子序列，因为这样定义更容易将 `dp[i+1]` 和 `dp[i]` 建立起联系，利用数学归纳法写出状态转移方程。

### [详解最长公共子序列问题，秒杀三道动态规划题目](https://mp.weixin.qq.com/s/ZhPEchewfc03xWv9VP3msg)

#### [1143. 最长公共子序列](https://leetcode.cn/problems/longest-common-subsequence/)

计算最长公共子序列（Longest Common Subsequence，简称 LCS）是一道经典的动态规划题目，大家应该都见过：

给你输入两个字符串`s1`和`s2`，请你找出他们俩的最长公共子序列，返回这个子序列的长度。

力扣第 1143 题就是这道题，函数签名如下：

```
int longestCommonSubsequence(String s1, String s2);
```

比如说输入`s1 = "zabcde", s2 = "acez"`，它俩的最长公共子序列是`lcs = "ace"`，长度为 3，所以算法返回 3。

如果没有做过这道题，一个最简单的暴力算法就是，把`s1`和`s2`的所有子序列都穷举出来，然后看看有没有公共的，然后在所有公共子序列里面再寻找一个长度最大的。

显然，这种思路的复杂度非常高，你要穷举出所有子序列，这个复杂度就是指数级的，肯定不实际。

正确的思路是不要考虑整个字符串，而是细化到`s1`和`s2`的每个字符。前文 子序列解题模板 中总结的一个规律：

**对于两个字符串求子序列的问题，都是用两个指针`i`和`j`分别在两个字符串上移动，大概率是动态规划思路**。

最长公共子序列的问题也可以遵循这个规律，我们可以先写一个`dp`函数：

```
// 定义：计算 s1[i..] 和 s2[j..] 的最长公共子序列长度
int dp(String s1, int i, String s2, int j)
```

这个`dp`函数的定义是：**`dp(s1, i, s2, j)`计算`s1[i..]`和`s2[j..]`的最长公共子序列长度**。

根据这个定义，那么我们想要的答案就是`dp(s1, 0, s2, 0)`，且 base case 就是`i == len(s1)`或`j == len(s2)`时，因为这时候`s1[i..]`或`s2[j..]`就相当于空串了，最长公共子序列的长度显然是 0：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import unittest

'''
执行用时：
732 ms
, 在所有 Python3 提交中击败了
5.06%
的用户
内存消耗：
25.5 MB
, 在所有 Python3 提交中击败了
6.95%
的用户
'''
class Solution:
    def __init__(self):
        self._memo = None

    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        self._memo = [[-1 for _ in range(0, len(text2))] for _ in range(0, len(text1))]
        return self._dp(text1, 0, text2, 0)

    def _dp(self, text1, i, text2, j):
        if i == len(text1) or j == len(text2):
            return 0

        if self._memo[i][j] != -1:
            return self._memo[i][j]

        sub_lcs = -1
        if text1[i] == text2[j]:
            sub_lcs = 1 + self._dp(text1, i + 1, text2, j + 1)
        else:
            sub_lcs = max(self._dp(text1, i + 1, text2, j), self._dp(text1, i, text2, j + 1))
        self._memo[i][j] = sub_lcs
        return sub_lcs


class TestSolution(unittest.TestCase):
    _SOLUTION = Solution()

    def test_solution1(self):
        text1 = "abcde"
        text2 = "ace"
        expected_length = 3
        self.assertEqual(expected_length, self._SOLUTION.longestCommonSubsequence(text1, text2))

    def test_solution2(self):
        text1 = "abc"
        text2 = "abc"
        expected_length = 3
        self.assertEqual(expected_length, self._SOLUTION.longestCommonSubsequence(text1, text2))

    def test_solution3(self):
        text1 = "abc"
        text2 = "def"
        expected_length = 0
        self.assertEqual(expected_length, self._SOLUTION.longestCommonSubsequence(text1, text2))

    def test_solution4(self):
        text1 = "bl"
        text2 = "yby"
        expected_length = 1
        self.assertEqual(expected_length, self._SOLUTION.longestCommonSubsequence(text1, text2))
```

以上思路完全就是按照我们之前的爆文 动态规划套路框架 来的，应该是很容易理解的。至于为什么要加`memo`备忘录，我们之前写过很多次，为了照顾新来的读者，这里再简单重复一下，首先抽象出我们核心`dp`函数的递归框架：

```
int dp(int i, int j) {
    dp(i + 1, j + 1); // #1
    dp(i, j + 1);     // #2
    dp(i + 1, j);     // #3
}
```

你看，假设我想从`dp(i, j)`转移到`dp(i+1, j+1)`，有不止一种方式，可以直接走`#1`，也可以走`#2 -> #3`，也可以走`#3 -> #2`。

这就是重叠子问题，如果我们不用`memo`备忘录消除子问题，那么`dp(i+1, j+1)`就会被多次计算，这是没有必要的。

至此，最长公共子序列问题就完全解决了，用的是自顶向下带备忘录的动态规划思路，我们当然也可以使用自底向上的迭代的动态规划思路，和我们的递归思路一样，关键是如何定义`dp`数组，我这里也写一下自底向上的解法吧：

```python
'''
执行用时：
360 ms
, 在所有 Python3 提交中击败了
60.11%
的用户
内存消耗：
23.7 MB
, 在所有 Python3 提交中击败了
12.55%
的用户
'''
class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        len1, len2 = len(text1), len(text2)
        # 定义：s1[0..i-1] 和 s2[0..j-1] 的 lcs 长度为 dp[i][j]
    	# 目标：s1[0..m-1] 和 s2[0..n-1] 的 lcs 长度，即 dp[m][n]
    	# base case: dp[0][..] = dp[..][0] = 0
        
        dp = [[0 for _ in range(0, len2 + 1)] for _ in range(0, len1 + 1)]
        for i in range(1, len1 + 1):
            for j in range(1, len2 + 1):
                if text1[i - 1] == text2[j - 1]:
                    dp[i][j] = 1 + dp[i - 1][j - 1]
                else:
                    dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
        return dp[len1][len2]
```

C++实现：

```cpp
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    int longestCommonSubsequence(string text1, string text2) {
        int n1 = text1.size();
        int n2 = text2.size();
        std::vector<std::vector<int>> dp(n1 + 1, std::vector<int>(n2 + 1, 0));
        for (int i = 0; i < n1; ++i) {
            for (int j = 0; j < n2; ++j) {
                if (text1[i] == text2[j]) {
                    dp[i + 1][j + 1] = dp[i][j] + 1;
                } else {
                    dp[i + 1][j + 1] = std::max(dp[i][j + 1], dp[i + 1][j]);
                }
            }
        }
        return dp[n1][n2];
    }
};
// 使用dfs递归实现
class Solution {
public:
    int longestCommonSubsequence(string text1, string text2) {
        int n1 = text1.size();
        int n2 = text2.size();
        std::vector<std::vector<int>> memo(n1, std::vector<int>(n2, -1));

        // 这里使用auto无法通过编译，递归调用自己的时候类型还不完整；同时注意递归函数要使用&传递
        // http://www.manongjc.com/detail/39-hctzmvwgnjshgcu.html https://blog.csdn.net/weixin_43686836/article/details/106952856
        std::function<int(int, int)> dfs = [&](int i, int j) {
            if (i < 0 || j < 0) {
                return 0;
            }

            if (memo[i][j] >= 0) {
                return memo[i][j];
            }

            int subLcs = text1[i] == text2[j] ? dfs(i - 1, j - 1) + 1 : std::max(dfs(i - 1, j), dfs(i, j - 1));
            memo[i][j] = subLcs;
            return subLcs;
        };

        return dfs(n1 - 1, n2 - 1);
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    string text1 = "abcde";
    string text2 = "ace";
    EXPECT_EQ(3, solution.longestCommonSubsequence(text1, text2));
}

TEST_F(SolutionTest, Test2) {
    string text1 = "abc";
    string text2 = "abc";
    EXPECT_EQ(3, solution.longestCommonSubsequence(text1, text2));
}
```



#### [583. 两个字符串的删除操作](https://leetcode.cn/problems/delete-operation-for-two-strings/)

这是力扣第 583 题「两个字符串的删除操作」，看下题目：

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/gibkIz0MVqdFj04Aic9zfP6rnHdGicfrafh6dgl5pUnutqNCqlEVtJHCYqNnwwiae3tR8ENPRF3PBzUWPJ7Nl4RWBQ/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

函数签名如下：

```
int minDistance(String s1, String s2);
```

题目让我们计算将两个字符串变得相同的最少删除次数，那我们可以思考一下，最后这两个字符串会被删成什么样子？

删除的结果不就是它俩的最长公共子序列嘛！

那么，要计算删除的次数，就可以通过最长公共子序列的长度推导出来：



```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import unittest

'''
执行用时：
232 ms
, 在所有 Python3 提交中击败了
81.96%
的用户
内存消耗：
17.2 MB
, 在所有 Python3 提交中击败了
75.59%
的用户
'''
class Solution:
    def minDistance(self, word1: str, word2: str) -> int:
        lcs = self._longestCommonSubsequence(word1, word2)
        return len(word1) - lcs + len(word2) - lcs

    def _longestCommonSubsequence(self, text1: str, text2: str) -> int:
        len1, len2 = len(text1), len(text2)
        dp = [[0 for _ in range(0, len2 + 1)] for _ in range(0, len1 + 1)]
        for i in range(1, len1 + 1):
            for j in range(1, len2 + 1):
                if text1[i - 1] == text2[j - 1]:
                    dp[i][j] = 1 + dp[i - 1][j - 1]
                else:
                    dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
        return dp[len1][len2]


class TestSolution(unittest.TestCase):
    _SOLUTION = Solution()

    def test_solution1(self):
        text1 = "sea"
        text2 = "eat"
        expected_length = 2
        self.assertEqual(expected_length, self._SOLUTION._longestCommonSubsequence(text1, text2))

    def test_solution3(self):
        text1 = "leetcode"
        text2 = "etco"
        expected_length = 4
        self.assertEqual(expected_length, self._SOLUTION._longestCommonSubsequence(text1, text2))
```

#### [712. 两个字符串的最小ASCII删除和](https://leetcode.cn/problems/minimum-ascii-delete-sum-for-two-strings/)

这是力扣第 712 题，看下题目：

![图片](https://mmbiz.qpic.cn/sz_mmbiz_png/gibkIz0MVqdFj04Aic9zfP6rnHdGicfrafhJl197b0zVcibXdfJNOEIAqWh9qHS9z1IjGwsXVcibKKPZI25URoWNWYA/640?wx_fmt=png&wxfrom=5&wx_lazy=1&wx_co=1)

这道题，和上一道题非常类似，这回不问我们删除的字符个数了，问我们删除的字符的 ASCII 码加起来是多少。

那就不能直接复用计算最长公共子序列的函数了，但是可以依照之前的思路，**稍微修改 base case 和状态转移部分即可直接写出解法代码**：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import unittest

'''
执行用时：
1164 ms
, 在所有 Python3 提交中击败了
11.02%
的用户
内存消耗：
20.1 MB
, 在所有 Python3 提交中击败了
13.77%
的用户
'''
class Solution:
    def __init__(self):
        self._memo = None

    def minimumDeleteSum(self, s1: str, s2: str) -> int:
        self._memo = [[-1 for _ in range(0, len(s2) + 1)] for _ in range(0, len(s1) + 1)]
        return self._dp(s1, 0, s2, 0)

    def _dp(self, s1, i, s2, j):
        if self._memo[i][j] != -1:
            return self._memo[i][j]

        if i == len(s1):
            sub_min = sum(ord(c) for c in s2[j:])
        elif j == len(s2):
            sub_min = sum(ord(c) for c in s1[i:])
        elif s1[i] == s2[j]:
            sub_min = self._dp(s1, i + 1, s2, j + 1)
        else:
            sub_min = min(ord(s1[i]) + self._dp(s1, i + 1, s2, j), ord(s2[j]) + self._dp(s1, i, s2, j + 1))

        self._memo[i][j] = sub_min
        return sub_min


class TestSolution(unittest.TestCase):
    _SOLUTION = Solution()

    def test_solution1(self):
        text1 = "sea"
        text2 = "eat"
        expected_length = 231
        self.assertEqual(expected_length, self._SOLUTION.minimumDeleteSum(text1, text2))

    def test_solution2(self):
        text1 = "delete"
        text2 = "leet"
        expected_length = 403
        self.assertEqual(expected_length, self._SOLUTION.minimumDeleteSum(text1, text2))
```

base case 有一定区别，计算`lcs`长度时，如果一个字符串为空，那么`lcs`长度必然是 0；但是这道题如果一个字符串为空，另一个字符串必然要被全部删除，所以需要计算另一个字符串所有字符的 ASCII 码之和。

关于状态转移，当`s1[i]`和`s2[j]`相同时不需要删除，不同时需要删除，所以可以利用`dp`函数计算两种情况，得出最优的结果。其他的大同小异，就不具体展开了。

至此，三道子序列问题就解决完了，关键在于将问题细化到字符，根据每两个字符是否相同来判断他们是否在结果子序列中，从而避免了对所有子序列进行穷举。

这也算是在两个字符串中求子序列的常用思路吧，建议好好体会，多多联系~

[使用自底向上的方法](https://leetcode.cn/problems/minimum-ascii-delete-sum-for-two-strings/solution/liang-ge-zi-fu-chuan-de-zui-xiao-asciishan-chu-he-/)：

```python
'''
执行用时：
636 ms
, 在所有 Python3 提交中击败了
55.51%
的用户
内存消耗：
19.5 MB
, 在所有 Python3 提交中击败了
74.41%
的用户
'''
class Solution:
    def minimumDeleteSum(self, s1: str, s2: str) -> int:
        len1 = len(s1)
        len2 = len(s2)
        dp = [[0] * (len2 + 1) for _ in range(0, len1 + 1)]

        for i in range(1, len1 + 1):
            dp[i][0] = dp[i - 1][0] + ord(s1[i - 1])

        for j in range(1, len2 + 1):
            dp[0][j] = dp[0][j - 1] + ord(s2[j - 1])

        for i in range(1, len1 + 1):
            for j in range(1, len2 + 1):
                if s1[i - 1] == s2[j - 1]:
                    dp[i][j] = dp[i - 1][j - 1]
                else:
                    dp[i][j] = min(ord(s1[i - 1]) + dp[i - 1][j], ord(s2[j - 1]) + dp[i][j - 1])
        return dp[len1][len2]
```

官方写法更容易理解一些：

我们用 dp\[i\]\[j\] 表示字符串 s1\[i:\] 和 s2\[j:\]（s1\[i:\] 表示字符串 s1 从第 ii 位到末尾的子串，s2\[j:\] 表示字符串 s2 从第 jj 位到末尾的子串，字符串下标从 0 开始）达到相等所需删除的字符的 ASCII 值的最小和，最终的答案为 dp\[0\]\[0\]。

当 s1\[i:\] 和 s2\[j:\] 中的某一个字符串为空时，dp\[i\]\[j\] 的值即为另一个非空字符串的所有字符的 ASCII 值之和。例如当 s2\[j:\] 为空时，此时有 j = s2.length()，状态转移方程为

dp\[i\]\[j\] = s1.asciiSumFromPos(i)
也可以写成递推的形式，即

dp\[i\]\[j\] = dp\[i + 1\]\[j\] + s1.asciiAtPos(i)
对于其余的情况，即两个字符串都非空时，如果有 s1\[i\] == s2\[j\]，那么当前位置的两个字符相同，它们不需要被删除，状态转移方程为

dp\[i\]\[j\] = dp\[i + 1\]\[j + 1\]
如果 s1\[i\] != s2\[j\]，那么我们至少要删除 s1\[i\] 和 s2\[j\] 两个字符中的一个，因此状态转移方程为

dp\[i\]\[j\] = min(dp\[i + 1\]\[j\] + s1.asciiAtPos(i), dp\[i\]\[j + 1\] + s2.asciiAtPos(j))

```python
class Solution(object):
    def minimumDeleteSum(self, s1, s2):
        dp = [[0] * (len(s2) + 1) for _ in range(len(s1) + 1)]

        for i in range(len(s1) - 1, -1, -1):
            dp[i][len(s2)] = dp[i + 1][len(s2)] + ord(s1[i])

        for j in range(len(s2) - 1, -1, -1):
            dp[len(s1)][j] = dp[len(s1)][j + 1] + ord(s2[j])

        for i in range(len(s1) - 1, -1, -1):
            for j in range(len(s2) - 1, -1, -1):
                if s1[i] == s2[j]:
                    dp[i][j] = dp[i + 1][j + 1]
                else:
                    dp[i][j] = min(dp[i + 1][j] + ord(s1[i]), dp[i][j + 1] + ord(s2[j]))
        return dp[0][0]
```

## [279. 完全平方数](https://leetcode.cn/problems/perfect-squares/)

参考：https://leetcode.cn/problems/perfect-squares/solutions/822940/wan-quan-ping-fang-shu-by-leetcode-solut-t99c/

```cpp
#include <climits>
#include <queue>
#include <unordered_set>
#include "gtest/gtest.h"

using namespace std;

/*时间
详情 
116ms
击败 77.96%使用 C++ 的用户
内存
详情
8.66mb
击败 48.67%使用 C++ 的用户
*/
class Solution {
public:
    int numSquares(int n) {
        std::vector<int> f(n + 1, 0);
        for (int i = 1; i <= n; ++i) {
            int minn = INT_MAX;
            for (int j = 1; j * j <= i; ++j) {
                minn = std::min(minn, f.at(i - j * j));
            }
            f.at(i) = minn + 1;
        }
        return f.at(n);
    }
};

/*
一个数学定理可以帮助解决本题：「四平方和定理」。
时间
详情
-ms
击败 100.00%使用 C++ 的用户
内存
详情
5.66mb
击败 97.54%使用 C++ 的用户
*/
class Solution {
public:
    int numSquares(int n) {
        if (isPerfectSquare(n)) {
            return 1;
        }
        if (checkAnswer4(n)) {
            return 4;
        }
        for (int i = 1; i * i <= n; ++i) {
            if (isPerfectSquare(n - i * i)) {
                return 2;
            }
        }
        return 3;
    }

private:
    bool isPerfectSquare(int x) {
        int y = std::sqrt(x);
        return y * y == x;
    }

    bool checkAnswer4(int x) {
        while (x % 4 == 0) {
            x /= 4;
        }
        return x % 8 == 7;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    EXPECT_EQ(1, solution.numSquares(25));
}

TEST_F(SolutionTest, Test2) {
    EXPECT_EQ(3, solution.numSquares(12));
}

TEST_F(SolutionTest, Test3) {
    EXPECT_EQ(2, solution.numSquares(13));
}
```



# [回溯算法解题套路框架](https://labuladong.gitee.io/algo/1/6/)

# 回文类问题

## 516. 最长回文子序列

<https://leetcode-cn.com/problems/longest-palindromic-subsequence/>

状态：
f[i][j] 表示 s 的第 i 个字符到第 j 个字符组成的子串中，最长的回文序列长度是多少。

转移方程：
如果 s 的第 i 个字符和第 j 个字符相同的话
f[i][j] = f[i + 1][j - 1] + 2

如果 s 的第 i 个字符和第 j 个字符不同的话
f[i][j] = max(f[i + 1][j], f[i][j - 1])

然后注意遍历顺序，i 从最后一个字符开始往前遍历，j 从 i + 1 开始往后遍历，这样可以保证每个子问题都已经算好了。

初始化：
f[i][i] = 1 单个字符的最长回文序列是 1

结果：
f[0][n - 1]

```java
                b    b    b    a    b
        下标j   0    1    2    3    4
 下标i
    0           1    2    3    3    4
    1           0    1    2    2    3
    2           0    0    1    1    3
    3           0    0    0    1    1
    4           0    0    0    0    1
```

```java
//DynamicSolution.java
package test;

public class DynamicSolution {
    public int longestPalindromeSubseq(String s) {
        int n = s.length();
        int[][] f = new int[n][n];
        for (int i = n - 1; i >= 0; i--) {
            f[i][i] = 1;
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    f[i][j] = f[i + 1][j - 1] + 2;
                } else {
                    f[i][j] = Math.max(f[i + 1][j], f[i][j - 1]);
                }
            }
        }
        return f[0][n - 1];
    }
}

//DynamicSolutionTest.java
package test;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DynamicSolutionTest {
    @Test
    public void test() {
        assertEquals(4, new DynamicSolution().longestPalindromeSubseq("bbbab"));
    }
}
```

另一个解法：
原题相当于，原字符串s与倒置后所得字符串_s，计算两个字符串的最长公共子序列。
Tip:必须用dp[1...n1][1...n2]来存储公共子序列长度，边界默认为0，否则的话在i-1和j-1关于0的边界处处理起来略复杂。
同 [1143. 最长公共子序列](https://leetcode.cn/problems/longest-common-subsequence/)

## [5. 最长回文子串](https://leetcode-cn.com/problems/longest-palindromic-substring/)(华为题库)

动态规划：

![](pictures\5. 最长回访子串（方法三：动态规划）.png)

```c++
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 使用lambda表达式耗时1408ms，把lambda表达式换成private方法运行超时
public:
    string longestPalindrome(string s) {
        string maxPalindrome;
        vector<bool> matrix(s.size(), false);
        vector<vector<bool>> intsIJ(s.size(), matrix);

        auto calculateMaxPalindrome = [&](int i, int j, string& maxPalindrome) {
            if (j - i + 1 > maxPalindrome.size()) {
                maxPalindrome = s.substr(i, j - i + 1);
            }
        };

        for (int i = s.size() - 1; i >= 0; --i) {
            for (int j = i; j < s.size(); ++j) {
                if (i == j) {
                    intsIJ.at(i).at(j) = true;
                    calculateMaxPalindrome(i, j, maxPalindrome);
                } else if (i + 1 == j) {
                    if (s.at(i) == s.at(j)) {
                        intsIJ.at(i).at(j) = true;
                        calculateMaxPalindrome(i, j, maxPalindrome);
                    }
                } else {
                    intsIJ.at(i).at(j) = intsIJ.at(i + 1).at(j - 1) && s.at(i) == s.at(j);
                    if (intsIJ.at(i).at(j)) {
                        calculateMaxPalindrome(i, j, maxPalindrome);
                    }
                }
            }
        }

        return maxPalindrome;
    }
};

/* 自己新写的，使用[]比使用at快，减少了范围检查
时间
416 ms
击败
19.27%
内存
276.4 MB
击败
16.52%
*/
class Solution {
public:
    string longestPalindrome(string s) {
        int n = s.size();
        if (n == 1 || n == 0) {
            return s;
        }

        std::vector<std::vector<int>> dp(n, std::vector<int>(n, 1));

        int minIndex = 0, maxIndex = -1;
        for (int i = n - 2; i >= 0; --i) {
            for (int j = i; j < n; ++j) {
                if (i + 1 == j || i == j) {
                    dp[i][j] = s[i] == s[j];
                } else {
                    dp[i][j] = s[i] == s[j] && dp[i + 1][j - 1];
                }

                if (dp[i][j] && j - i + 1 >= maxIndex - minIndex + 1) {
                    minIndex = i;
                    maxIndex = j;
                }
            }
        }

        return s.substr(minIndex, maxIndex - minIndex + 1);
    }
};

// 优化一下判断
/*
时间
详情
328ms
击败 29.89%使用 C++ 的用户
内存
详情
269.92mb
击败 11.32%使用 C++ 的用户
*/
class Solution {
public:
    string longestPalindrome(string s) {
        int n = s.size();
        if (n == 1 || n == 0) {
            return s;
        }

        std::vector<std::vector<int>> dp(n, std::vector<int>(n, 1));

        int minIndex = 0, maxIndex = 0;
        for (int i = n - 1; i >= 0; --i) {
            for (int j = i + 1; j < n; ++j) {
                dp[i][j] = s[i] == s[j] && dp[i + 1][j - 1];

                if (dp[i][j] && j - i + 1 >= maxIndex - minIndex + 1) {
                    minIndex = i;
                    maxIndex = j;
                }
            }
        }

        return s.substr(minIndex, maxIndex - minIndex + 1);
    }
};

TEST(SolutionTest, Test1) {
    string inputStr = "babad";
    string expectedPalindrome = "aba";
    EXPECT_EQ(expectedPalindrome, Solution().longestPalindrome(inputStr));
}

TEST(SolutionTest, Test2) {
    string inputStr = "cbbd";
    string expectedPalindrome = "bb";
    EXPECT_EQ(expectedPalindrome, Solution().longestPalindrome(inputStr));
}

TEST(SolutionTest, Test3) {
    string inputStr = "aaaa";
    string expectedPalindrome = "aaaa";
    EXPECT_EQ(expectedPalindrome, Solution().longestPalindrome(inputStr));
}
```

[方法四：中心扩展算法](https://leetcode-cn.com/problems/longest-palindromic-substring/solution/zui-chang-hui-wen-zi-chuan-by-leetcode/)：

每次以一个字符或两个相邻字符向左右扩展，查找以这一个字符或两个字符为中心的最长回文字符：

```c++
class Solution {  // 64ms
public:
    string longestPalindrome(string s) {
        if (s.empty()) {
            return "";
        }

        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = max(len1, len2);
            if (len > end - start + 1) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        return s.substr(start, end - start + 1);
    }

private:
    int expandAroundCenter(string s, int left, int right) {
        int L = left, R = right;
        while (L >= 0 && R < s.length() && s.at(L) == s.at(R)) {
            L--;
            R++;
        }
        return R - L - 1;
    }
};
```

[Manacher算法的详细讲解](https://www.jianshu.com/p/116aa58b7d81)

## [9. 回文数](https://leetcode.cn/problems/palindrome-number/)

参考：https://leetcode.cn/problems/palindrome-number/solutions/281686/hui-wen-shu-by-leetcode-solution/

```cpp
/*
时间
详情
-ms
击败 100.00%使用 C++ 的用户
内存
详情
5.62mb
击败 67.21%使用 C++ 的用户
*/
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    bool isPalindrome(int x) {
        if ((x % 10 == 0 && x != 0) || x < 0) {
            return false;
        }

        int reverseRightHalf = 0;
        while (x > reverseRightHalf) {
            reverseRightHalf = reverseRightHalf * 10 + x % 10;
            x /= 10;
        }

        return x == reverseRightHalf || x == reverseRightHalf / 10;
    }
};

// 别人的解法，把全部数字算一遍，写起来不用判断那么多，但需要对所有数字计算
class Solution {
public:
    bool isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        long cur = 0;
        int num = x;
        while (num > 0) {
            cur = cur * 10 + num % 10;
            num /= 10;
        }
        return cur == x;
    }
};

class Solution {
public:
    bool isPalindrome(int x) {
        const auto& numStr = std::to_string(x);
        std::string revertedNumStr(numStr);  // 4ms
        std::reverse(revertedNumStr.begin(), revertedNumStr.end());
//        std::string revertedNumStr;  // 12ms
//        std::reverse_copy(numStr.begin(), numStr.end(), std::back_inserter(revertedNumStr));
//        std::string revertedNumStr(numStr);  // 20ms
//        std::reverse_copy(numStr.begin(), numStr.end(), revertedNumStr.begin());
//        std::string revertedNumStr(numStr.size(), '0');  // 12ms
//        std::reverse_copy(numStr.begin(), numStr.end(), revertedNumStr.begin());
        return numStr == revertedNumStr;
    }
};


class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    EXPECT_EQ(true, solution.isPalindrome(121));
}

TEST_F(SolutionTest, Test2) {
    EXPECT_EQ(false, solution.isPalindrome(10));
}

TEST_F(SolutionTest, Test3) {
    EXPECT_EQ(true, solution.isPalindrome(1));
}

TEST_F(SolutionTest, Test4) {
    EXPECT_EQ(false, solution.isPalindrome(1000));
}

TEST_F(SolutionTest, Test5) {
    EXPECT_EQ(true, solution.isPalindrome(1001));
}

TEST_F(SolutionTest, Test6) {
    EXPECT_EQ(true, solution.isPalindrome(0));
}

TEST_F(SolutionTest, Test7) {
    EXPECT_EQ(false, solution.isPalindrome(INT_MAX));
}
```

## [564. 寻找最近的回文数](https://leetcode.cn/problems/find-the-closest-palindrome/)

```cpp
#include "gtest/gtest.h"

using namespace std;

// 自己写的，转成数字，再用第9题的方法判断。2147483647超时
class Solution {
public:
    string nearestPalindromic(string n) {
        auto num = std::stoull(n);
        decltype(num) smallerIndex = 0;
        while (!isPalindrome(num - (++smallerIndex))) {
        }

        decltype(num) biggerIndex = 0;
        while (!isPalindrome(num + (++biggerIndex))) {
        }

        return smallerIndex <= biggerIndex ? std::to_string(num - smallerIndex) : std::to_string(num + biggerIndex);
    }

private:
    bool isPalindrome(int x) {
        if ((x % 10 == 0 && x != 0) || x < 0) {
            return false;
        }

        int reverseRightHalf = 0;
        while (x > reverseRightHalf) {
            reverseRightHalf = reverseRightHalf * 10 + x % 10;
            x /= 10;
        }

        return x == reverseRightHalf || x == reverseRightHalf / 10;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    EXPECT_EQ("121", solution.nearestPalindromic("123"));
    EXPECT_EQ("1221", solution.nearestPalindromic("1234"));
    EXPECT_EQ("12321", solution.nearestPalindromic("12345"));
}

TEST_F(SolutionTest, Test10) {
    EXPECT_EQ("989", solution.nearestPalindromic("987"));
    EXPECT_EQ("9889", solution.nearestPalindromic("9876"));
    EXPECT_EQ("98789", solution.nearestPalindromic("98765"));
}

TEST_F(SolutionTest, Test7) {
    EXPECT_EQ("22", solution.nearestPalindromic("19"));
    EXPECT_EQ("10201", solution.nearestPalindromic("10199"));
    EXPECT_EQ("102201", solution.nearestPalindromic("101999"));
    EXPECT_EQ("992299", solution.nearestPalindromic("991999"));
}

TEST_F(SolutionTest, Test8) {
    EXPECT_EQ("99899", solution.nearestPalindromic("99900"));
    EXPECT_EQ("998899", solution.nearestPalindromic("999000"));
    EXPECT_EQ("99799", solution.nearestPalindromic("99899"));
}

TEST_F(SolutionTest, Test2) {
    EXPECT_EQ("0", solution.nearestPalindromic("1"));
    EXPECT_EQ("1", solution.nearestPalindromic("2"));
    EXPECT_EQ("8", solution.nearestPalindromic("9"));
}

TEST_F(SolutionTest, Test3) {
    EXPECT_EQ("2147447412", solution.nearestPalindromic("2147483647"));
}

TEST_F(SolutionTest, Test5) {
    EXPECT_EQ("101", solution.nearestPalindromic("99"));
    EXPECT_EQ("1001", solution.nearestPalindromic("999"));
}

TEST_F(SolutionTest, Test6) {
    EXPECT_EQ("9", solution.nearestPalindromic("10"));
    EXPECT_EQ("99", solution.nearestPalindromic("100"));
    EXPECT_EQ("99", solution.nearestPalindromic("101"));
}

```

```cpp
class Solution {
    using ULL = unsigned long long;
public:
    string nearestPalindromic(string n) {
        unsigned long length = n.size();
        int middleIndex = (length - 1) / 2;
        std::string leftStr = n.substr(0, middleIndex + 1);

        // reverse left
        std::string candidate1(n);
        std::reverse_copy(leftStr.begin(), leftStr.end(),
                          length % 2 == 0 ? candidate1.begin() + middleIndex + 1 : candidate1.begin() + middleIndex);

        // left + 1
        ULL leftNum = std::stoull(leftStr);
        std::string leftStrAddOne = std::to_string(leftNum + 1);
        std::string candidate2(n);
        std::reverse_copy(leftStrAddOne.begin(), leftStrAddOne.end(),
                          length % 2 == 0 ? candidate1.begin() + middleIndex + 1 : candidate1.begin() + middleIndex);



        return candidate1;
    }

private:
};

// 重构如下：
/*
时间
详情
-ms
击败 100.00%使用 C++ 的用户
内存
详情
5.96mb
击败 83.33%使用 C++ 的用户
时间、空间复杂度都是O(logn)
*/
class Solution {
    using ULL = unsigned long long;
public:
    string nearestPalindromic(string n) {
        unsigned long length = n.size();
        ULL nNum = stoull(n);

        if (length == 1) {
            return std::to_string(nNum - 1);
        } else if (pow(10, length) - 1 == nNum) {  // 9, 99
            return std::to_string(nNum + 2);
        } else if (pow(10, length - 1) == nNum) {  // 10, 100
            return std::to_string(nNum - 1);
        } else if (pow(10, length - 1) + 1 == nNum) {  // 11, 101
            return std::to_string(nNum - 2);
        }

        int middleIndex = (length - 1) / 2;
        std::string leftStr = n.substr(0, middleIndex + 1);

        ULL nearestNum = ULLONG_MAX;
        ULL delta = ULLONG_MAX;
        const auto& leftCandidates = getLeftCandidates(leftStr);
        for (const auto& leftCandidate: leftCandidates) {
            std::string candidate(n);
            std::copy(leftCandidate.begin(), leftCandidate.end(), candidate.begin());
            std::reverse_copy(leftCandidate.begin(), leftCandidate.end(),
                              length % 2 == 0 ? candidate.begin() + middleIndex + 1 : candidate.begin() + middleIndex);  // 可能会超长度

            if (candidate == n) {
                continue;
            }

            ULL candidateNum = std::stoull(candidate);
            long long tmpDelta = std::abs(static_cast<long long>(nNum - candidateNum));
            if (tmpDelta < delta || (tmpDelta == delta && candidateNum < nNum)) {
                nearestNum = candidateNum;
                delta = tmpDelta;
            }
        }

        return std::to_string(nearestNum);
    }

private:
    std::vector<std::string> getLeftCandidates(const std::string& leftStr) {
        ULL leftNum = std::stoull(leftStr);
        std::string leftStrAddOne = std::to_string(leftNum + 1);  // 向上增加会导致中间数字变化
        std::string leftStrSubOne = std::to_string(leftNum - 1);  // 向下减少会导致中间数字变化

        return {leftStr, leftStrAddOne, leftStrSubOne};
    }
};

// 参考答案写的，函数功能单一，逻辑清晰：
class Solution {
    using ULL = unsigned long long;
public:
    string nearestPalindromic(string n) {
        const auto& candidates = getCandidates(n);
        return getNearest(n, candidates);
    }

private:
    std::vector<ULL> getCandidates(const std::string& n) {
        unsigned long len = n.size();
        std::vector<ULL> candidates = {static_cast<ULL>(std::pow(10, len) + 1),
                                       static_cast<ULL>(std::pow(10, len - 1) - 1)};
        ULL leftNum = std::stoull(n.substr(0, (len + 1) / 2));
        for (const auto& leftCandidateNum: {leftNum, leftNum + 1, leftNum - 1}) {
            const auto& leftCandidateStr = std::to_string(leftCandidateNum);
            const auto& candidateStr = leftCandidateStr +
                                       std::string(leftCandidateStr.rbegin() + (len % 2 == 0 ? 0 : 1),
                                                   leftCandidateStr.rend());
            candidates.push_back(std::stoull(candidateStr));
        }
        return candidates;
    }

    std::string getNearest(const std::string& n, const std::vector<ULL>& candidates) {
        ULL nNum = std::stoull(n);
        ULL ans = -1, delta = -1;
        for (const auto& candidate: candidates) {
            if (candidate == nNum) {
                continue;
            }

            ULL tmpDelta = std::abs(static_cast<long long>(candidate - nNum));
            if (ans == -1 || tmpDelta < delta || (tmpDelta == delta && candidate < nNum)) {
                ans = candidate;
                delta = tmpDelta;
            }
        }
        return std::to_string(ans);
    }
};

// https://leetcode.cn/problems/find-the-closest-palindrome/solutions/1300885/xun-zhao-zui-jin-de-hui-wen-shu-by-leetc-biyt/
using ULL = unsigned long long;

class Solution {
public:
    string nearestPalindromic(string n) {
        ULL selfNumber = stoull(n), ans = -1;
        const vector<ULL>& candidates = getCandidates(n);
        for (auto& candidate: candidates) {
            if (candidate != selfNumber) {
                if (ans == -1 ||
                    llabs(candidate - selfNumber) < llabs(ans - selfNumber) ||
                    llabs(candidate - selfNumber) == llabs(ans - selfNumber) && candidate < ans) {
                    ans = candidate;
                }
            }
        }
        return to_string(ans);
    }

private:
    vector<ULL> getCandidates(const string& n) {
        int len = n.length();
        vector<ULL> candidates = {
            (ULL) pow(10, len - 1) - 1,
            (ULL) pow(10, len) + 1,
        };
        ULL selfPrefix = stoull(n.substr(0, (len + 1) / 2));
        for (int i: {selfPrefix - 1, selfPrefix, selfPrefix + 1}) {
            string prefix = to_string(i);
            string candidate = prefix + string(prefix.rbegin() + (len & 1), prefix.rend());
            candidates.push_back(stoull(candidate));
        }
        return candidates;
    }
};

```

## [131. 分割回文串](https://leetcode.cn/problems/palindrome-partitioning/)

参考：https://leetcode.cn/problems/palindrome-partitioning/solutions/

回溯也是在遍历树，是多节点树：

​						   aab

​			a	     	  aa  		aab(x)

​	   a	  ab(x)		b

  b

把所有节点连起来，就是\[a, a, b\]\[aa, b\]

```cpp
/*
时间
详情
120ms
击败 49.49%使用 C++ 的用户
内存
详情
72.26mb
击败 45.82%使用 C++ 的用户
时间：O(n*2^n)
空间：O(2^n)
*/
class Solution {
public:
    vector<vector<string>> partition(string s) {
        int n = s.size();
        dp_.assign(n, std::vector<int>(n, 1));

        for (int i = n - 1; i >= 0; --i) {
            for (int j = i + 1; j < n; ++j) {
                dp_[i][j] = s[i] == s[j] && dp_[i + 1][j - 1];
            }
        }


        dfs(s, 0);
        return res_;
    };

private:
    void dfs(const std::string& s, int index) {
        if (index == s.size()) {
            res_.push_back(ans_);
            return;
        }

        for (int j = index; j < s.size(); ++j) {
            if (dp_[index][j]) {
                ans_.push_back(s.substr(index, j - index + 1));
                dfs(s, j + 1);
                ans_.pop_back();
            }
        }
    }

private:
    std::vector<std::vector<std::string>> res_;
    std::vector<std::string> ans_;
    std::vector<std::vector<int>> dp_;
};
```

## [132. 分割回文串 II](https://leetcode.cn/problems/palindrome-partitioning-ii/)

```cpp
/*
时间
详情
144ms
击败 10.46%使用 C++ 的用户
内存
详情
57.14mb
击败 5.20%使用 C++ 的用户
时间复杂度和空间复杂度：O(n^2)
*/
#include <climits>
#include <valarray>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    int minCut(string s) {
        int n = s.size();
        std::vector<std::vector<int>> dp(n, std::vector<int>(n, 1));
        for (int i = n - 1; i >= 0; --i) {
            for (int j = i + 1; j < n; ++j) {
                dp[i][j] = s[i] == s[j] && dp[i + 1][j - 1];
            }
        }

        std::vector<int> f(n, INT_MAX);
        for (int i = 0; i < n; ++i) {
            if (dp[0][i]) {
                f[i] = 0;
            } else {
                for (int j = 0; j <= i; ++j) {
                    if (dp[j][i]) {
                        f[i] = min(f[i], f[j - 1] + 1);
                    }
                }
            }
        }
        return f[n - 1];
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST(SolutionTest, Test1) {
    string inputStr = "aab";
    EXPECT_EQ(1, Solution().minCut(inputStr));
}

TEST(SolutionTest, Test2) {
    string inputStr = "a";
    EXPECT_EQ(0, Solution().minCut(inputStr));
}

TEST(SolutionTest, Test3) {
    string inputStr = "ab";
    EXPECT_EQ(1, Solution().minCut(inputStr));
}
```

## [214. 最短回文串](https://leetcode.cn/problems/shortest-palindrome/)

```cpp
/*
求出s从0开始的最长回访子串记为s1，则后段s2倒序拼接在s前即可
超时
时间空间：O(n^2)
*/
class Solution {
public:
    string shortestPalindrome(string s) {
        if (s.empty()) {
            return s;
        }

        int n = s.size();
        std::vector<std::vector<bool>> dp(n, std::vector<bool>(n, true));
        for (int i = n - 1; i >= 0; --i) {
            for (int j = i + 1; j < n; ++j) {
                dp[i][j] = s[i] == s[j] && dp[i + 1][j - 1];
            }
        }

        int maxPrefixPalindrome = 0;
        for (int j = 0; j < n; ++j) {
            if (dp[0][j]) {
                maxPrefixPalindrome = std::max(maxPrefixPalindrome, j);
            }
        }

        return std::string(s.rbegin(), s.rbegin() + n - maxPrefixPalindrome - 1) + s;
    }
};

// https://leetcode.cn/problems/shortest-palindrome/solutions/392561/zui-duan-hui-wen-chuan-by-leetcode-solution/
/*
方法一：字符串哈希
时间：O(|s|)
空间：O(1)
*/
class Solution { 
public:
    string shortestPalindrome(string s) {
        int n = s.size();
        int base = 131, mod = 1000000007;
        int left = 0, right = 0, mul = 1;
        int best = -1;
        for (int i = 0; i < n; ++i) {
            left = ((long long)left * base + s[i]) % mod;
            right = (right + (long long)mul * s[i]) % mod;
            if (left == right) {
                best = i;
            }
            mul = (long long)mul * base % mod;
        }
        string add = (best == n - 1 ? "" : s.substr(best + 1, n));
        reverse(add.begin(), add.end());
        return add + s;
    }
};

/*
时间
8 ms
击败
59.71%
内存
8.7 MB
击败
26.97%
时间空间：O(|s|)
*/
class Solution {
public:
    string shortestPalindrome(string s) {
        if (s.empty()) {
            return s;
        }
        int n = s.size();
        std::string reverseS(s.rbegin(), s.rend());
        int maxPrefixPalindrome = kmpSearch(reverseS, s);
        return std::string(s.rbegin(), s.rbegin() + n - maxPrefixPalindrome) + s;
    }

private:
    int kmpSearch(const std::string& s, const std::string& patt) {
        const auto& next = buildNext(patt);
        int i = 0, j = 0;
        while (i < s.size()) {
            if (s[i] == patt[j]) {
                ++i;
                ++j;
            } else if (j > 0) {  // 字符匹配失败，根据next跳过子串前面的一些字符
                j = next[j - 1];
            } else {  // 子串第1个字符就匹配失败
                ++i;
            }
        }
        return j;
    }

    std::vector<int> buildNext(const std::string& patt) {
        std::vector<int> next(patt.size(), 0);
        int prefixLen = 0;  // 当前共同前后缀的长度
        int i = 1;
        while (i < patt.size()) {
            if (patt[prefixLen] == patt[i]) {  // 字符相同，则构成了更长的前后缀
                next[i++] = ++prefixLen;
            } else if (prefixLen == 0) {  // 字符不同，并且没有共同前后缀，则直接赋0
                next[i++] = 0;
            } else {
                prefixLen = next[prefixLen - 1];  // 字符不同，直接查表看是否存在更短的前后缀
            }
        }
        return next;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    string inputStr = "aacecaaa";
    EXPECT_EQ("aaacecaaa", solution.shortestPalindrome(inputStr));
}

TEST_F(SolutionTest, Test2) {
    string inputStr = "abcd";
    EXPECT_EQ("dcbabcd", solution.shortestPalindrome(inputStr));
}

TEST_F(SolutionTest, Test3) {
    string inputStr = "abcba";
    EXPECT_EQ("abcba", solution.shortestPalindrome(inputStr));
}

TEST_F(SolutionTest, Test4) {
    string inputStr = "a";
    EXPECT_EQ("a", solution.shortestPalindrome(inputStr));
}

TEST_F(SolutionTest, Test5) {  // 运行超时
    string inputStr = "aaa...";  // 2W个a + cd + 2W个a
    EXPECT_EQ("xxx", solution.shortestPalindrome(inputStr));
}
```

## [647. 回文子串](https://leetcode.cn/problems/palindromic-substrings/)

```cpp
#include "gtest/gtest.h"

using namespace std;

/*
时间
详情
24ms
击败 20.96%使用 C++ 的用户
内存
详情
7.33mb
击败 26.73%使用 C++ 的用户
*/
class Solution {
public:
    int countSubstrings(string s) {
        auto n = s.size();
        std::vector<std::vector<bool>> dp(n, std::vector<bool>(n, true));
        int count = 0;
        for (int i = n - 1; i >= 0; --i) {
            for (int j = i; j < n; ++j) {
                if (i != j) {
                    dp[i][j] = s[i] == s[j] && dp[i + 1][j - 1];
                }

                if (dp[i][j]) {
                    ++count;
                }
            }
        }
        return count;
    }
};

// https://leetcode.cn/problems/palindromic-substrings/solutions/379987/hui-wen-zi-chuan-by-leetcode-solution/
/*
时间
详情
-ms
击败 100.00%使用 C++ 的用户
内存
详情
5.99mb
击败 83.10%使用 C++ 的用户
时间：O(n^2)
空间：O(1)
*/
class Solution {
public:
    int countSubstrings(string s) {
        int n = s.size(), ans = 0;
        for (int i = 0; i < 2 * n - 1; ++i) {
            int l = i / 2, r = i / 2 + i % 2;
            while (l >= 0 && r < n && s[l] == s[r]) {
                --l;
                ++r;
                ++ans;
            }
        }
        return ans;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    string inputStr = "abc";
    EXPECT_EQ(3, solution.countSubstrings(inputStr));
}

TEST_F(SolutionTest, Test2) {
    string inputStr = "aaa";
    EXPECT_EQ(6, solution.countSubstrings(inputStr));
}

TEST_F(SolutionTest, Test3) {
    string inputStr = "";
    EXPECT_EQ(0, solution.countSubstrings(inputStr));
}


```



# 算法

## KMP

```cpp
#include "gtest/gtest.h"

using namespace std;

// https://www.bilibili.com/video/BV1AY4y157yL/?spm_id_from=333.337.search-card.all.click&vd_source=c9f7d901dfa9fbc04fdbc2cf37bd0212

std::vector<int> buildNext(const std::string& patt) {
    std::vector<int> next(patt.size(), 0);
    int prefixLen = 0;  // 当前共同前后缀的长度
    int i = 1;
    while (i < patt.size()) {
        if (patt[prefixLen] == patt[i]) {  // 字符相同，则构成了更长的前后缀
            next[i++] = ++prefixLen;
        } else if (prefixLen == 0) {  // 字符不同，并且没有共同前后缀，则直接赋0
            next[i++] = 0;
        } else {
            prefixLen = next[prefixLen - 1];  // 字符不同，直接查表看是否存在更短的前后缀
        }
    }
    return next;
}

int kmpSearch(const std::string& s, const std::string& patt) {
    const auto& next = buildNext(patt);
    int i = 0, j = 0;
    while (i < s.size()) {
        if (s[i] == patt[j]) {
            ++i;
            ++j;
        } else if (j > 0) {  // 字符匹配失败，根据next跳过子串前面的一些字符
            j = next[j - 1];
        } else {  // 子串第1个字符就匹配失败
            ++i;
        }

        if (j == patt.size()) {
            return i - j;
        }
    }
    return -1;
}

TEST(KmpTest, Test1) {
    string patt = "ABACABAB";
    std::vector<int> next = {0, 0, 1, 0, 1, 2, 3, 2};
    EXPECT_EQ(next, buildNext(patt));
}

TEST(KmpTest, Test2) {
    std::string s = "ABABACABAB";
    string patt = "ABACABAB";
    EXPECT_EQ(2, kmpSearch(s, patt));
}

TEST(KmpTest, Test3) {
    std::string s = "aaacecaa";
    string patt = "aacecaaa";
    EXPECT_EQ(-1, kmpSearch(s, patt));
}

TEST(KmpTest, Test4) {
    std::string s = "abcdabcde";
    string patt = "abcd";
    EXPECT_EQ(0, kmpSearch(s, patt));
}

```

# [经典150题](https://leetcode.cn/studyplan/top-interview-150/)

## 数组/字符串

### [169. 多数元素](https://leetcode.cn/problems/majority-element/)

https://leetcode.cn/problems/majority-element/solutions/146074/duo-shu-yuan-su-by-leetcode-solution/

哈希表：

```cpp
// 时间空间：O(n)
class Solution {
public:
    int majorityElement(vector<int>& nums) {
        unordered_map<int, int> counts;
        int majority = 0, cnt = 0;
        for (int num: nums) {
            ++counts[num];
            if (counts[num] > cnt) {
                majority = num;
                cnt = counts[num];
            }
        }
        return majority;
    }
};

```

排序：

```cpp
// 时间空间：O(nlogn)
class Solution {
public:
    int majorityElement(vector<int>& nums) {
        sort(nums.begin(), nums.end());
        return nums[nums.size() / 2];
    }
};
```

Boyer-Moore 投票算法

```cpp
/*
时间：O(n)
空间：O(1)
*/
class Solution {
public:
    int majorityElement(vector<int>& nums) {
        int candidate = -1;
        int count = 0;
        for (int num : nums) {
            if (num == candidate)
                ++count;
            else if (--count < 0) {
                candidate = num;
                count = 1;
            }
        }
        return candidate;
    }
};
```



### [189. 轮转数组](https://leetcode.cn/problems/rotate-array/solutions/551039/xuan-zhuan-shu-zu-by-leetcode-solution-nipk/?envType=study-plan-v2&envId=top-interview-150)

方法二：环状替换

```cpp
#include <numeric>
#include "gtest/gtest.h"

using namespace std;

// 时间：O(n) 空间：O(1)
class Solution {
public:
    void rotate(vector<int>& nums, int k) {
        int n = nums.size();
        k = k % n;
        int count = gcd(k, n);
        for (int start = 0; start < count; ++start) {
            int current = start;
            int prev = nums[start];
            do {
                int next = (current + k) % n;
                swap(nums[next], prev);
                current = next;
            } while (start != current);
        }
    }
};

// 理解不了最大公约数，就计数count
class Solution {
public:
    void rotate(vector<int>& nums, int k) {
        int n = nums.size();
        k %= n;
        int count = 0;
        int start = 0;
        while (count < n) {
            int current = start;
            int prev = nums[current];
            do {
                int next = (current + k) % n;
                std::swap(nums[next], prev);
                current = next;
                ++count;
            } while (current != start);
            ++start;
        }
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::vector<int> nums = {1, 2, 3, 4, 5, 6};
    std::vector<int> expected = {5, 6, 1, 2, 3, 4};
    solution.rotate(nums, 2);
    EXPECT_EQ(expected, nums);
}
```

数组翻转

```cpp
// 时间：O(n) 空间：O(1)
class Solution {
public:
    void rotate(vector<int>& nums, int k) {
        int n = nums.size();
        k %= n;
        std::reverse(nums.begin(), nums.end());
        std::reverse(nums.begin(), nums.begin() + k);
        std::reverse(nums.begin() + k, nums.end());
    }
};
```

### [274. H 指数](https://leetcode.cn/problems/h-index/)

```cpp
#include "gtest/gtest.h"

using namespace std;

/*
时间
详情
-ms
击败 100.00%使用 C++ 的用户
内存
详情
8.24mb
击败 24.81%使用 C++ 的用户
时间：O(nlogn)
空间：O(logn)
*/
class Solution {
public:
    int hIndex(vector<int>& citations) {
        std::sort(citations.begin(), citations.end());
        int h = 0;
        for (int i = citations.size() - 1; i >= 0; --i) {
            if (citations[i] > h) {
                ++h;
            }
        }
        return h;
    }
};

/*
计数排序
时间
详情
4ms
击败 59.01%使用 C++ 的用户
内存
详情
8.32mb
击败 10.79%使用 C++ 的用户
时间：O(n)
空间：O(n)
*/
class Solution {
public:
    int hIndex(vector<int>& citations) {
        int n = citations.size(), tot = 0;
        vector<int> counter(n + 1);
        for (int i = 0; i < n; i++) {
            if (citations[i] >= n) {
                counter[n]++;
            } else {
                counter[citations[i]]++;
            }
        }
        for (int i = n; i >= 0; i--) {
            tot += counter[i];
            if (tot >= i) {
                return i;
            }
        }
        return 0;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::vector<int> citations = {3, 0, 6, 1, 5};
    EXPECT_EQ(3, solution.hIndex(citations));
}

TEST_F(SolutionTest, Test2) {
    std::vector<int> citations = {1, 3, 1};
    EXPECT_EQ(1, solution.hIndex(citations));
}

```

### [238. 除自身以外数组的乘积](https://leetcode.cn/problems/product-of-array-except-self/)

参考：https://leetcode.cn/problems/product-of-array-except-self/solutions/272369/chu-zi-shen-yi-wai-shu-zu-de-cheng-ji-by-leetcode-/?envType=study-plan-v2&envId=top-interview-150

```cpp
#include "gtest/gtest.h"

using namespace std;
/*
时间
详情
20ms
击败 76.96%使用 C++ 的用户
内存
详情
23.90mb
击败 29.43%使用 C++ 的用户
时间空间：O(N)
*/
class Solution {
public:
    vector<int> productExceptSelf(vector<int>& nums) {
        int length = nums.size();

        // L 和 R 分别表示左右两侧的乘积列表
        vector<int> L(length, 0), R(length, 0);

        vector<int> answer(length);

        // L[i] 为索引 i 左侧所有元素的乘积
        // 对于索引为 '0' 的元素，因为左侧没有元素，所以 L[0] = 1
        L[0] = 1;
        for (int i = 1; i < length; i++) {
            L[i] = nums[i - 1] * L[i - 1];
        }

        // R[i] 为索引 i 右侧所有元素的乘积
        // 对于索引为 'length-1' 的元素，因为右侧没有元素，所以 R[length-1] = 1
        R[length - 1] = 1;
        for (int i = length - 2; i >= 0; i--) {
            R[i] = nums[i + 1] * R[i + 1];
        }

        // 对于索引 i，除 nums[i] 之外其余各元素的乘积就是左侧所有元素的乘积乘以右侧所有元素的乘积
        for (int i = 0; i < length; i++) {
            answer[i] = L[i] * R[i];
        }

        return answer;
    }
};

/*
时间
详情
20ms
击败 76.96%使用 C++ 的用户
内存
详情
22.83mb
击败 83.00%使用 C++ 的用户
每个位置的数字先保存为左侧乘积或右侧乘积，再由遍历时的另一侧值来乘从而刷新成最终值
*/
class Solution {
public:
    vector<int> productExceptSelf(vector<int>& nums) {
        int n = nums.size();
        vector<int> ans(n, 1);
        int prefix = 1, suffix = 1;
        for (int i = 0; i < n; i++) {
            ans[i] *= prefix;
            ans[n - i - 1] *= suffix;
            prefix *= nums[i];
            suffix *= nums[n - i - 1];
        }
        return ans;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::vector<int> nums = {1, 2, 3, 4};
    std::vector<int> expect = {24, 12, 8, 6};
    EXPECT_EQ(expect, solution.productExceptSelf(nums));
}
```

### [135. 分发糖果](https://leetcode.cn/problems/candy/)

https://leetcode.cn/problems/candy/solutions/533150/fen-fa-tang-guo-by-leetcode-solution-f01p/

两次遍历：

```cpp
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    int candy(vector<int>& ratings) {
        int n = ratings.size();
        vector<int> left(n);
        for (int i = 0; i < n; i++) {
            if (i > 0 && ratings[i] > ratings[i - 1]) {
                left[i] = left[i - 1] + 1;
            } else {
                left[i] = 1;
            }
        }
        int right = 0, ret = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (i < n - 1 && ratings[i] > ratings[i + 1]) {
                right++;
            } else {
                right = 1;
            }
            ret += max(left[i], right);
        }
        return ret;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::vector<int> ratings = {1, 3, 5, 3, 2, 2};
    EXPECT_EQ(10, solution.candy(ratings));
}

TEST_F(SolutionTest, Test2) {
    std::vector<int> ratings = {1, 3, 5, 3, 2, 1};
    EXPECT_EQ(13, solution.candy(ratings));
}
```

方法二：常数空间遍历

```cpp

class Solution {
public:
    int candy(vector<int>& ratings) {
        int n = ratings.size();
        int ret = 1;
        int inc = 1, dec = 0, pre = 1;
        for (int i = 1; i < n; i++) {
            if (ratings[i] >= ratings[i - 1]) {
                dec = 0;
                pre = ratings[i] == ratings[i - 1] ? 1 : pre + 1;
                ret += pre;
                inc = pre;
            } else {
                dec++;
                if (dec == inc) {
                    dec++;
                }
                ret += dec;
                pre = 1;
            }
        }
        return ret;
    }
};
```

### [6. N 字形变换](https://leetcode.cn/problems/zigzag-conversion/)

https://leetcode.cn/problems/zigzag-conversion/solutions/1298127/z-zi-xing-bian-huan-by-leetcode-solution-4n3u/?envType=study-plan-v2&envId=top-interview-150

```cpp
#include <cmath>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    string convert(string s, int numRows) {
        int n = s.length(), r = numRows;
        if (r == 1 || r >= n) {
            return s;
        }
        int t = r * 2 - 2;  // 每个周期有几个字符
//        int c = (n + t - 1) / t * (r - 1);
        int c = std::ceil((float) n / t) * (r - 1);  // n/t计算有几个周期，r - 1为每个周期有几列，算出来共有多少列
        vector<string> mat(r, string(c, 0));
        for (int i = 0, x = 0, y = 0; i < n; ++i) {
            mat[x][y] = s[i];
            if (i % t < r - 1) {
                ++x; // 向下移动
            } else {
                --x;
                ++y; // 向右上移动
            }
        }
        string ans;
        for (auto& row: mat) {
            for (char ch: row) {
                if (ch) {
                    ans += ch;
                }
            }
        }
        return ans;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    EXPECT_EQ("PAHNAPLSIIGYIR", solution.convert("PAYPALISHIRING", 3));
}

TEST_F(SolutionTest, Test2) {
    EXPECT_EQ("PINALSIGYAHRPI", solution.convert("PAYPALISHIRING", 4));
}
```

压缩矩阵空间：

```cpp
class Solution {
public:
    string convert(string s, int numRows) {
        int n = s.length(), r = numRows;
        if (r == 1 || r >= n) {
            return s;
        }
        vector<string> mat(r);
        for (int i = 0, x = 0, t = r * 2 - 2; i < n; ++i) {
            mat[x] += s[i];
            i % t < r - 1 ? ++x : --x;
        }
        string ans;
        for (auto &row : mat) {
            ans += row;
        }
        return ans;
    }
};
```

直接构造：

```cpp
class Solution {
public:
    string convert(string s, int numRows) {
        int n = s.length(), r = numRows;
        if (r == 1 || r >= n) {
            return s;
        }
        string ans;
        int t = r * 2 - 2;
        for (int i = 0; i < r; ++i) { // 枚举矩阵的行
            for (int j = 0; j + i < n; j += t) { // 枚举每个周期的起始下标
                ans += s[j + i]; // 当前周期的第一个字符
                if (0 < i && i < r - 1 && j + t - i < n) {
                    ans += s[j + t - i]; // 当前周期的第二个字符
                }
            }
        }
        return ans;
    }
};
```

## 双指针

### [392. 判断子序列](https://leetcode.cn/problems/is-subsequence/)

https://leetcode.cn/problems/is-subsequence/solutions/346539/pan-duan-zi-xu-lie-by-leetcode-solution/?envType=study-plan-v2&envId=top-interview-150

暴力：

```cpp
class Solution {
public:
    bool isSubsequence(string s, string t) {
        int n = s.size();
        int m = t.size();
        int i = 0, j = 0;
        while (i < n && j < m) {
            if (s[i] == t[j]) {
                ++i;
            }
            ++j;
        }
        return i == n;
    }
};
```

方法二：动态规划

```cpp
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    bool isSubsequence(string s, string t) {
        int n = s.size();
        int m = t.size();
        std::vector<std::vector<int>> dp(m + 1, std::vector<int>(26, 0));
        for (int i = 0; i < 26; ++i) {
            dp[m][i] = -1;
        }

        for (int i = m - 1; i >= 0; --i) {
            for (int j = 0; j < 26; ++j) {
                if (t[i] == j + 'a') {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = dp[i + 1][j];
                }
            }
        }

        int start = 0;
        for (int i = 0; i < n; ++i) {
            start = dp[start][s[i] - 'a'];
            if (start == -1) {
                return false;
            }
            start++;
        }

        return true;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    EXPECT_EQ(true, solution.isSubsequence("abc", "ahbgdc"));
}

TEST_F(SolutionTest, Test2) {
    EXPECT_EQ(false, solution.isSubsequence("axc", "ahbgdc"));
}
```

## 滑动窗口

### [209. 长度最小的子数组](https://leetcode.cn/problems/minimum-size-subarray-sum/)

暴力：

```cpp
#include <climits>
#include "gtest/gtest.h"

using namespace std;

class Solution {  // 超时
public:
    int minSubArrayLen(int target, vector<int>& nums) {
        int n = nums.size();
        if (n == 0) {
            return 0;
        }

        int ans = INT_MAX;
        for (int i = 0; i < n; ++i) {
            int sum = 0;
            for (int j = i; j < n; ++j) {
                sum += nums[j];
                if (sum >= target) {
                    ans = std::min(ans, j - i + 1);
                }
            }
        }
        return ans == INT_MAX ? 0 : ans;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::vector<int> nums = {2, 3, 1, 2, 4, 3};
    EXPECT_EQ(2, solution.minSubArrayLen(7, nums));
}
```

方法二：前缀和 + 二分查找

```cpp
/*
时间
详情
52ms
击败 6.57%使用 C++ 的用户
内存
详情
27.86mb
击败 5.02%使用 C++ 的用户
时间：O(nlogn)
空间：O(n)
*/
class Solution {
public:
    int minSubArrayLen(int target, vector<int>& nums) {
        int n = nums.size();
        if (n == 0) {
            return 0;
        }

        std::vector<int> sums(n + 1, 0);
        for (int i = 1; i <= n; ++i) {
            sums[i] = sums[i - 1] + nums[i - 1];
        }

        int ans = INT_MAX;
        for (int i = 1; i <= n; ++i) {
            int sumTarget = target + sums[i - 1];
            auto sumIter = std::lower_bound(sums.begin(), sums.end(), sumTarget);
            if (sumIter != sums.end()) {
                ans = std::min(ans, (int) std::distance(sums.begin(), sumIter) - (i - 1));
            }
        }
        return ans == INT_MAX ? 0 : ans;
    }
};
```

方法三：滑动窗口

```cpp
/*
时间
详情
32ms
击败 60.45%使用 C++ 的用户
内存
详情
27.05mb
击败 8.68%使用 C++ 的用户
时间：O(n)
空间：O(1)
*/
class Solution {
public:
    int minSubArrayLen(int target, vector<int>& nums) {
        int n = nums.size();
        if (n == 0) {
            return 0;
        }

        int ans = INT_MAX;
        int start = 0, end = 0;
        int sum = 0;
        while (end < n) {
            sum += nums[end];
            while (sum >= target) {
                ans = std::min(ans, end - start + 1);
                sum -= nums[start++];
            }
            ++end;
        }
        return ans == INT_MAX ? 0 : ans;
    }
};
```

## 矩阵

### [36. 有效的数独](https://leetcode.cn/problems/valid-sudoku/)

```cpp
class Solution {
public:
    bool isValidSudoku(vector<vector<char>>& board) {
        std::vector<std::vector<int>> rows(9, std::vector<int>(9, 0));
        std::vector<std::vector<int>> columns(9, std::vector<int>(9, 0));
        std::vector<std::vector<std::vector<int>>> subBoxes(3,
                                                            std::vector<std::vector<int>>(3, std::vector<int>(9, 0)));

        for (int i = 0; i < board.size(); ++i) {
            for (int j = 0; j < board.size(); ++j) {
                char c = board[i][j];
                if (c != '.') {
                    int intValue = c - '0' - 1;
                    ++rows[i][intValue];
                    ++columns[j][intValue];
                    ++subBoxes[i / 3][j / 3][intValue];
                    if (rows[i][intValue] > 1 || columns[j][intValue] > 1 || subBoxes[i / 3][j / 3][intValue] > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
};
```

### [54. 螺旋矩阵](https://leetcode.cn/problems/spiral-matrix/)

参考：https://leetcode.cn/problems/spiral-matrix/solutions/275393/luo-xuan-ju-zhen-by-leetcode-solution/?envType=study-plan-v2&envId=top-interview-150

```cpp
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    vector<int> spiralOrder(vector<vector<int>>& matrix) {
        if (matrix.empty() || matrix[0].empty()) {
            return {};
        }

        std::vector<int> order;
        int top = 0, bottom = matrix.size() - 1, left = 0, right = matrix[0].size() - 1;
        while (top <= bottom && left <= right) {
            for (int column = left; column <= right; ++column) {
                order.push_back(matrix[top][column]);
            }
            for (int row = top + 1; row <= bottom; ++row) {
                order.push_back(matrix[row][right]);
            }

            if (top < bottom && left < right) {
                for (int column = right - 1; column >= left; --column) {
                    order.push_back(matrix[bottom][column]);
                }
                for (int row = bottom - 1; row >= top + 1; --row) {
                    order.push_back(matrix[row][left]);
                }
            }
            ++left;
            --right;
            ++top;
            --bottom;
        }
        return order;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    vector<vector<int>> matrix = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    vector<int> expect = {1, 2, 3, 6, 9, 8, 7, 4, 5};
    EXPECT_EQ(expect, solution.spiralOrder(matrix));
}

TEST_F(SolutionTest, Test2) {
    vector<vector<int>> matrix = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
    vector<int> expect = {1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7};
    EXPECT_EQ(expect, solution.spiralOrder(matrix));
}

TEST_F(SolutionTest, Test3) {
    vector<vector<int>> matrix = {{1, 2, 3, 4}};
    vector<int> expect = {1, 2, 3, 4};
    EXPECT_EQ(expect, solution.spiralOrder(matrix));
}

TEST_F(SolutionTest, Test4) {
    vector<vector<int>> matrix = {{3}, {2}};
    vector<int> expect = {3, 2};
    EXPECT_EQ(expect, solution.spiralOrder(matrix));
}
```

### [48. 旋转图像 - 力扣（LeetCode）](https://leetcode.cn/problems/rotate-image/solutions/526980/xuan-zhuan-tu-xiang-by-leetcode-solution-vu3m/?envType=study-plan-v2&envId=top-interview-150)

参考：https://leetcode.cn/problems/rotate-image/solutions/526980/xuan-zhuan-tu-xiang-by-leetcode-solution-vu3m/?envType=study-plan-v2&envId=top-interview-150

```cpp
/*
时间：O(N^2)
空间：O(1)
*/
class Solution {
public:
    void rotate(vector<vector<int>>& matrix) {
        int n = matrix.size();
        for (int i = 0; i < n / 2; ++i) {
            std::swap(matrix[i], matrix[n - i - 1]);
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                std::swap(matrix[i][j], matrix[j][i]);
            }
        }
    }
};
```



## 哈希表

### [49. 字母异位词分组](https://leetcode.cn/problems/group-anagrams/)

https://leetcode.cn/problems/group-anagrams/solutions/520469/zi-mu-yi-wei-ci-fen-zu-by-leetcode-solut-gyoc/

```cpp
/*
时间复杂度：O(nklogk)
空间复杂度：O(nk)
*/
class Solution {
public:
    vector<vector<string>> groupAnagrams(vector<string>& strs) {
        unordered_map<string, vector<string>> mp;
        for (string& str: strs) {
            string key = str;
            sort(key.begin(), key.end());
            mp[key].emplace_back(str);
        }
        vector<vector<string>> ans;
        for (const auto& [key, value]: mp) {
            ans.emplace_back(value);
        }
        return ans;
    }
};

/*
时间复杂度：O(n(k+|Σ|)
空间复杂度：O(n(k+|Σ|)
*/
class Solution {
public:
    vector<vector<string>> groupAnagrams(vector<string>& strs) {
        // 自定义对 array<int, 26> 类型的哈希函数
        auto arrayHash = [fn = hash<int>{}] (const array<int, 26>& arr) -> size_t {
            return accumulate(arr.begin(), arr.end(), 0u, [&](size_t acc, int num) {
                return (acc << 1) ^ fn(num);
            });
        };

        unordered_map<array<int, 26>, vector<string>, decltype(arrayHash)> mp(0, arrayHash);
        for (string& str: strs) {
            array<int, 26> counts{};
            int length = str.length();
            for (int i = 0; i < length; ++i) {
                counts[str[i] - 'a'] ++;
            }
            mp[counts].emplace_back(str);
        }
        vector<vector<string>> ans;
        for (auto it = mp.begin(); it != mp.end(); ++it) {
            ans.emplace_back(it->second);
        }
        return ans;
    }
};
```

### [128. 最长连续序列](https://leetcode.cn/problems/longest-consecutive-sequence/)

```cpp
#include <unordered_set>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    int longestConsecutive(vector<int>& nums) {
        unordered_set<int> num_set;
        for (const int& num : nums) {
            num_set.insert(num);
        }

        int longestStreak = 0;

        for (const int& num : num_set) {
            if (!num_set.count(num - 1)) {  // 有比当前数小的，就不处理了，最后交给最小的数去计算
                int currentNum = num;
                int currentStreak = 1;

                while (num_set.count(currentNum + 1)) {
                    currentNum += 1;
                    currentStreak += 1;
                }

                longestStreak = max(longestStreak, currentStreak);
            }
        }

        return longestStreak;
    }
};


class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    std::vector<int> nums = {100,4,200,1,3,2};
    EXPECT_EQ(4, solution.longestConsecutive(nums));
}
```

## 字典树

### [208. 实现 Trie (前缀树)](https://leetcode.cn/problems/implement-trie-prefix-tree/)

### [211. 添加与搜索单词 - 数据结构设计](https://leetcode.cn/problems/design-add-and-search-words-data-structure/)



## 回溯

### [17. 电话号码的字母组合](https://leetcode.cn/problems/letter-combinations-of-a-phone-number/)

### [77. 组合](https://leetcode.cn/problems/combinations/)



## Kadane算法

### [918. 环形子数组的最大和](https://leetcode.cn/problems/maximum-sum-circular-subarray/)



## 位运算

### [137. 只出现一次的数字 II](https://leetcode.cn/problems/single-number-ii/)



## 数学

### [50. Pow(x, n)](https://leetcode.cn/problems/powx-n/)

```cpp
#include <iostream>

double binaryCalc(double base, int exponent) {
    if (exponent == 1) {
        return base;
    }

    double result = binaryCalc(base, exponent / 2);
    result *= result;
    if (exponent % 2 == 1) {
        result *= base;
    }
    return result;
}

double Power(double base, int exponent) {
    if (exponent < 0) {
        exponent = -exponent;
        base = 1 / base;
    }

    double result = binaryCalc(base, exponent);
    // for (int i = 0; i < exponent; ++i) {
    //     result *= base;
    // }

    return result;
}

int main()
{
    std::pair<double, int> test_set[] = {
        {2, 5},     // 32
        {2, 10},    // 1024
        {2, -3},    // 0.125
        {2, -4},    // 0.0625
        {2.1, 3},   // 9.261
        {1.5, -5},  // 0.131687
    };

    for (auto &test : test_set) {
        printf("%f ^ %d = %f\n", test.first, test.second, Power(test.first, test.second));
    }

    return 0;
}
```

https://leetcode.cn/problems/powx-n/solutions/238559/powx-n-by-leetcode-solution/?envType=study-plan-v2&envId=top-interview-150

递归：

```cpp
#include "gtest/gtest.h"

using namespace std;

/*
时间
详情
-ms
击败 100.00%使用 C++ 的用户
内存
详情
5.72mb
击败 42.62%使用 C++ 的用户
时间空间：O(n)
*/
class Solution {
public:
    double myPow(double x, int n) {
        long N = n;
        return N >= 0 ? quickMul(x, N) : 1 / quickMul(x, -N);
    }

private:
    double quickMul(double x, long N) {
        if (N == 0) {
            return 1.0;
        }
        double y = quickMul(x, N / 2);
        return N % 2 == 0 ? y * y : y * y * x;
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    EXPECT_EQ(1, solution.myPow(0.44528, 0));
}

TEST_F(SolutionTest, Test2) {
    EXPECT_EQ(1, solution.myPow(1.0, -2147483648));
}

TEST_F(SolutionTest, Test3) {
    EXPECT_EQ(32, solution.myPow(2.0, 5));
}

TEST_F(SolutionTest, Test4) {
    EXPECT_EQ(1024, solution.myPow(2.0, 10));
}

```

迭代：

```cpp
class Solution {  // 自己写的，不太对
public:
    double myPow(double x, int n) {
        long N = n;
        return N >= 0 ? quickMul(x, N) : 1 / quickMul(x, -N);
    }

private:
    double quickMul(double x, long N) {
        if (N == 0) {
            return 1.0;
        }
        bool isOdd = N % 2 == 1;
        double ans = x;
        N /= 2;
        while (N > 0) {
            ans *= ans;
            N /= 2;
        }
        return isOdd ? ans * x : ans;
    }
};

//计算方法参考上面链接
class Solution {
public:
    double myPow(double x, int n) {
        long N = n;
        return N >= 0 ? quickMul(x, N) : 1 / quickMul(x, -N);
    }

private:
    double quickMul(double x, long N) {
        double ans = 1.0;
        double x_contribute = x;
        while (N > 0) {
            if (N % 2 == 1) {
                ans *= x_contribute;
            }

            x_contribute *= x_contribute;
            N /= 2;
        }
        return ans;
    }
};
```

## 栈

### [20. 有效的括号](https://leetcode.cn/problems/valid-parentheses/)

```cpp
#include <valarray>
#include <stack>
#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
    bool isValid(string s) {
        std::stack<char> stack;
        std::unordered_map<char, char> bracketsMap = {{')', '('}, {']', '['}, {'}', '{'}};
        for (char ch: s) {
            if (bracketsMap.find(ch) != bracketsMap.end()) {
                if (!stack.empty() && bracketsMap[ch] == stack.top()) {
                    stack.pop();
                } else {
                    stack.push(ch);
                    break;
                }
            } else {
                stack.push(ch);
            }
        }
        return stack.empty();
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    EXPECT_EQ(true, solution.isValid("()[]{}"));
}

TEST_F(SolutionTest, Test2) {
    EXPECT_EQ(false, solution.isValid("(}"));
}
```





## 动态规划

### [139. 单词拆分](https://leetcode.cn/problems/word-break/)



# 其它

## 抛硬币

考虑一个投掷硬币问题
背景：小P和小H分别选择一个4个硬币组成的序列（正面由U表示，反面由D表示），一枚硬币每次会公正的投掷并记录结果，先出现的序列为获胜者。
问题：给定小 P 选择的一个序列，请为小 H 设法给出胜率最大的对应序列，并给出python代码
实例：小P选择DDDD ，小H选择UDDD，获胜概率最大
扩展：将4改为任意有限正整数，你会如何优化你的计算

使用模拟算概率：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import itertools
import random

UP_STR = 'U'
DOWN_STR = 'D'

def simulate_game(seq_a, seq_b):
    # 模拟一局游戏，给定B选择的序列，返回获胜者 'A' 或 'B'
    game_sequence = ""
    seq_len = len(seq_a)
    while True:
        game_sequence += random.choice([UP_STR, DOWN_STR])  # 模拟硬币抛掷
        if game_sequence[-seq_len:] == seq_a:
            return 'A'
        elif game_sequence[-seq_len:] == seq_b:
            return 'B'


def calculate_win_probability(seq_a, seq_b, num_simulations):
    # 计算给定序列下B获胜的概率
    wins_b = 0
    for _ in range(num_simulations):
        winner = simulate_game(seq_a, seq_b)
        if winner == 'B':
            wins_b += 1

    probability_b = wins_b / num_simulations
    print(f'seq b: {seq_b}, probability: {probability_b}')
    return probability_b


def cal_max_probability_sequence(seq_a, num_simulations):
    best_sequence = ""
    best_probability = 0
    # 遍历所有可能的序列，计算获胜概率
    for sequence in itertools.product(UP_STR + DOWN_STR, repeat=len(seq_a)):
        sequence_str = "".join(sequence)
        prob_B = calculate_win_probability(seq_a, sequence_str, num_simulations)
        if prob_B > best_probability:
            best_probability = prob_B
            best_sequence = sequence_str
    print(f"最佳序列: {best_sequence}")
    print(f"最大获胜概率: {best_probability}")


if __name__ == "__main__":
    num_simulations = 10000  # 模拟的次数，可以根据需要调整
    seq_a = 'UDUUD'
    cal_max_probability_sequence(seq_a, num_simulations)
```

[使用公式计算](https://www.163.com/dy/article/DASPKMIA05118CTM.html)：

```cpp
//coin_sequence_calculator.h
#ifndef GTEST_PROJECT_COIN_SEQUENCE_CALCULATOR_H
#define GTEST_PROJECT_COIN_SEQUENCE_CALCULATOR_H

#include <random>
#include <iostream>
#include "thread_pool.h"

struct SequenceInfo {
    std::string sequence;
    double probability;
};

class CoinSequenceCalculator {
public:
    enum class Type {
        SIMULATION, FORMULA
    };

public:
    virtual ~CoinSequenceCalculator() noexcept = default;

    virtual SequenceInfo calBestProbabilitySequence(const std::string& inputSequence) = 0;

    static std::unique_ptr<CoinSequenceCalculator> create(Type type, int simulationCount = 10000);
};

#endif //GTEST_PROJECT_COIN_SEQUENCE_CALCULATOR_H


//coin_sequence_calculator.cpp
#include "coin_sequence_calculator.h"

static const char UP_STR = 'U';
static const char DOWN_STR = 'D';

class CoinSequenceCalculatorSimulationImpl : public CoinSequenceCalculator {
public:
    CoinSequenceCalculatorSimulationImpl(int simulateCount) : simulateCount_(simulateCount) {
    }

    virtual ~CoinSequenceCalculatorSimulationImpl() noexcept = default;

    virtual SequenceInfo calBestProbabilitySequence(const std::string& inputSequence) override {
        const auto& allSequence = getAllSequence(inputSequence.size());
        const auto& bestSequenceInfo = getBestSequenceInfo(inputSequence, allSequence, simulateCount_);
        return bestSequenceInfo;
    }

private:
    enum class SimulationResult {
        WIN_A, WIN_B
    };

private:
    std::vector<std::string> getAllSequence(int length) {
        std::string sequence;
        std::vector<std::string> allSequence;
        calAllSequence(length, sequence, allSequence);
        return allSequence;
    }

    void calAllSequence(int length, std::string& sequence, std::vector<std::string>& allSequence) {
        if (sequence.size() == length) {
            allSequence.push_back(sequence);
            return;
        }

        for (const auto& eachSide: {UP_STR, DOWN_STR}) {
            sequence.push_back(eachSide);
            calAllSequence(length, sequence, allSequence);
            sequence.pop_back();
        }
    }

    SequenceInfo getBestSequenceInfo(const std::string& sequenceA, const std::vector<std::string>& allSequence,
                                     int simulateCount) {
        ThreadPool threadPool;
        threadPool.init();
        std::vector<std::future<std::pair<std::string, double>>> allSequenceProbability;
        for (const auto& tmpSequence: allSequence) {
            allSequenceProbability.emplace_back(threadPool.submit(
                std::bind(&CoinSequenceCalculatorSimulationImpl::getSimulationProbability, this, std::placeholders::_1,
                          std::placeholders::_2, std::placeholders::_3), sequenceA, tmpSequence,
                simulateCount));
        }

        std::string sequence;
        double probability = 0;
        for (auto& sequenceProbability: allSequenceProbability) {
            const auto& [tmpSequence, tmpProbability] = sequenceProbability.get();
            if (tmpProbability > probability) {
                probability = tmpProbability;
                sequence = tmpSequence;
            }
        }
        threadPool.shutdown();

        return {sequence, probability};
    }

    std::pair<std::string, double> getSimulationProbability(const std::string& sequenceA, const std::string& sequenceB,
                                                            int count) {
        int winBCount = 0;
        for (int i = 0; i < count; ++i) {
            if (simulateSequence(sequenceA, sequenceB) == SimulationResult::WIN_B) {
                ++winBCount;
            }
        }
        std::cout << "tmpSequence: << " << sequenceB << ", tmpProbability: "
                  << (static_cast<double>(winBCount) / count) << std::endl;
        return {sequenceB, static_cast<double>(winBCount) / count};
    };

    SimulationResult simulateSequence(const std::string& sequenceA, const std::string& sequenceB) {
        std::string simulateSequence;
        while (true) {
            simulateSequence.push_back(getOneCoinSimulation());
            int sequenceLength = sequenceA.size();
            int simulateSeqLength = simulateSequence.size();
            if (simulateSeqLength < sequenceLength) {
                continue;
            }

            if (simulateSequence.compare(simulateSeqLength - sequenceLength, sequenceLength, sequenceA) == 0) {
                return SimulationResult::WIN_A;
            } else if (simulateSequence.compare(simulateSeqLength - sequenceLength, sequenceLength, sequenceB) == 0) {
                return SimulationResult::WIN_B;
            }
        }
    }

    char getOneCoinSimulation() {
        std::random_device rd;
        std::mt19937 gen(rd());
        std::binomial_distribution<> distrib;
        return distrib(gen) == 0 ? UP_STR : DOWN_STR;
    }

private:
    int simulateCount_;
};

class CoinSequenceCalculatorFormulaImpl : public CoinSequenceCalculator {
public:
    CoinSequenceCalculatorFormulaImpl() {
    }

    virtual ~CoinSequenceCalculatorFormulaImpl() noexcept = default;

    virtual SequenceInfo calBestProbabilitySequence(const std::string& inputSequence) override {
        const auto& allSequence = getTwoSequence(inputSequence);
        const auto& bestSequenceInfo = getBestSequenceInfo(inputSequence, allSequence);
        return bestSequenceInfo;
    }

private:
    std::vector<std::string> getTwoSequence(const std::string& inputSequence) {
        const std::string& prefix = inputSequence.substr(0, inputSequence.size() - 1);
        return {UP_STR + prefix, DOWN_STR + prefix};
    }

    SequenceInfo getBestSequenceInfo(const std::string& sequenceA, const std::vector<std::string>& allSequence) {
        std::string sequence;
        double probability = 0;
        for (const auto& tmpSequence: allSequence) {
            double tmpProbability = getProbabilityByFormula(sequenceA, tmpSequence);
            std::cout << "tmpSequence: << " << tmpSequence << ", tmpProbability: " << tmpProbability << std::endl;
            if (tmpProbability > probability) {
                probability = tmpProbability;
                sequence = tmpSequence;
            }
        }
        return {sequence, probability};
    }

    /*
L(10110, 10110) = (10010)2 = 18
L(10110, 01011) = (00001)2 = 1
L(01011, 01011) = (10000)2 = 16
L(01011, 10110) = (01001)2 = 9
那么， 01 串 a 和 b 的胜率之比就是
(L(b, b) – L(b, a)) : (L(a, a) – L(a, b))
     */
    double getProbabilityByFormula(const std::string& seqA, const std::string& seqB) {
        int lbb = getLvalue(seqB, seqB);
        int lba = getLvalue(seqB, seqA);
        int laa = getLvalue(seqA, seqA);
        int lab = getLvalue(seqA, seqB);
        return static_cast<double>(laa - lab) / ((lbb - lba) + (laa - lab));
    }

    int getLvalue(const std::string& lhsSeq, const std::string& rhsSeq) {
        int value = 0;
        int seqLength = lhsSeq.size();
        for (int i = 0; i < lhsSeq.size(); ++i) {
            value <<= 1;
            if (lhsSeq.compare(i, seqLength - i, rhsSeq, 0, seqLength - i) == 0) {
                value += 1;
            }
        }
        return value;
    }
};

std::unique_ptr<CoinSequenceCalculator> CoinSequenceCalculator::create(Type type, int simulationCount) {
    if (type == Type::SIMULATION) {
        return std::make_unique<CoinSequenceCalculatorSimulationImpl>(simulationCount);
    } else if (type == Type::FORMULA) {
        return std::make_unique<CoinSequenceCalculatorFormulaImpl>();
    }
    return std::unique_ptr<CoinSequenceCalculator>();
}

//thread_pool.h
#ifndef GTEST_PROJECT_THREAD_POOL_H
#define GTEST_PROJECT_THREAD_POOL_H

#include <queue>
#include <mutex>
#include <thread>
#include <condition_variable>
#include <future>
#include <functional>

template<typename T>
class SafeQueue {
public:
    SafeQueue() {
    }

    SafeQueue(SafeQueue&& other) {
    }

    ~SafeQueue() {
    }

    bool empty() // 返回队列是否为空
    {
        std::unique_lock<std::mutex> lock(mutex_); // 互斥信号变量加锁，防止m_queue被改变
        return queue_.empty();
    }

    int size() {
        std::unique_lock<std::mutex> lock(mutex_); // 互斥信号变量加锁，防止m_queue被改变
        return queue_.size();
    }

    // 队列添加元素
    void enqueue(T& t) {
        std::unique_lock<std::mutex> lock(mutex_);
        queue_.emplace(t);
    }

    // 队列取出元素
    bool dequeue(T& t) {
        std::unique_lock<std::mutex> lock(mutex_); // 队列加锁
        if (queue_.empty()) {
            return false;
        }
        t = std::move(queue_.front()); // 取出队首元素，返回队首元素值，并进行右值引用
        queue_.pop(); // 弹出入队的第一个元素
        return true;
    }

private:
    std::queue<T> queue_; //利用模板函数构造队列
    std::mutex mutex_; // 访问互斥信号量
};

class ThreadPool {
public:
    // 线程池构造函数
    ThreadPool(const int n_threads = -1)
        : isInit_(false), isShutdown_(false),
          threads_(n_threads > 0 ? n_threads : std::thread::hardware_concurrency()) {
    }

    ~ThreadPool() {
        if (!isShutdown_) {
            shutdown();
        }
    }

    ThreadPool(const ThreadPool&) = delete;

    ThreadPool(ThreadPool&&) = delete;

    ThreadPool& operator=(const ThreadPool&) = delete;

    ThreadPool& operator=(ThreadPool&&) = delete;

    // Inits thread pool
    bool init() {
        if (!isInit_) {
            for (int i = 0; i < threads_.size(); ++i) {
                threads_.at(i) = std::thread(ThreadWorker(this, i)); // 分配工作线程
            }
            isInit_ = true;
        }
        return isInit_;
    }

    // Waits until threads finish their current task and shutdowns the pool
    void shutdown() {
        isShutdown_ = true;
        conditionalLock_.notify_all(); // 通知，唤醒所有工作线程

        for (int i = 0; i < threads_.size(); ++i) {
            if (threads_.at(i).joinable()) // 判断线程是否在等待
            {
                threads_.at(i).join(); // 将线程加入到等待队列
            }
        }
    }

    // Submit a function to be executed asynchronously by the pool
    template<typename F, typename... Args>
    auto submit(F&& f, Args&& ...args) -> std::future<decltype(f(args...))> {
        static bool dummyInit = init();
        // Create a function with bounded parameter ready to execute
        std::function<decltype(f(args...))()> func = std::bind(std::forward<F>(f),
                                                               std::forward<Args>(args)...); // 连接函数和参数定义，特殊函数类型，避免左右值错误

        // Encapsulate it into a shared pointer in order to be able to copy construct
        auto task_ptr = std::make_shared<std::packaged_task<decltype(f(args...))() >>(func);

        // Warp packaged task into void function
        std::function<void()> warpper_func = [task_ptr]() {
            (*task_ptr)();
        };

        // 队列通用安全封包函数，并压入安全队列
        queue_.enqueue(warpper_func);
        // 唤醒一个等待中的线程
        conditionalLock_.notify_one();
        // 返回先前注册的任务指针
        return task_ptr->get_future();
    }

private:
    class ThreadWorker // 内置线程工作类
    {
    public:
        // 构造函数
        ThreadWorker(ThreadPool* pool, const int id) : threadPool_(pool), id_(id) {
        }

        // 重载()操作
        void operator()() {
            std::function<void()> func; // 定义基础函数类func

            bool dequeued; // 是否正在取出队列中元素
            while (!threadPool_->isShutdown_) {
                {
                    // 为线程环境加锁，互访问工作线程的休眠和唤醒
                    std::unique_lock<std::mutex> lock(threadPool_->conditionalMutex_);

                    // 如果任务队列为空，阻塞当前线程
                    if (threadPool_->queue_.empty()) {
                        threadPool_->conditionalLock_.wait(lock); // 等待条件变量通知，开启线程
                    }

                    // 取出任务队列中的元素
                    dequeued = threadPool_->queue_.dequeue(func);
                }

                // 如果成功取出，执行工作函数
                if (dequeued) {
                    func();
                }
            }
        }

    private:
        int id_; // 工作id
        ThreadPool* threadPool_; // 所属线程池
    };

private:
    bool isInit_; // 线程池是否关闭
    bool isShutdown_; // 线程池是否关闭
    std::vector<std::thread> threads_; // 工作线程队列
    SafeQueue<std::function<void()>> queue_; // 执行函数安全队列，即任务队列
    std::mutex conditionalMutex_; // 线程休眠锁互斥变量
    std::condition_variable conditionalLock_; // 线程环境锁，可以让线程处于休眠或者唤醒状态
};

#endif //GTEST_PROJECT_THREAD_POOL_H

//timer.h
#ifndef GTEST_PROJECT_TIMER_H
#define GTEST_PROJECT_TIMER_H

#include <iostream>
#include "chrono"

class Timer {
public:
    ~Timer() {
        std::chrono::steady_clock::time_point end = std::chrono::steady_clock::now();
        std::chrono::duration<double> timeUsed = std::chrono::duration_cast<std::chrono::duration<double >>(
            end - start_);
        std::cout << "Elapse time : " << timeUsed.count() << "s" << std::endl;
    }

private:
    std::chrono::steady_clock::time_point start_ = std::chrono::steady_clock::now();;
};

#endif //GTEST_PROJECT_TIMER_H

//main.cpp
#include <iostream>
#include "timer.h"
#include "coin_sequence_calculator.h"

void calCoinMaxProbabilitySequence(CoinSequenceCalculator& calculator, const std::string& inputSequence) {
    Timer timer;
    const auto& sequenceInfo = calculator.calBestProbabilitySequence(inputSequence);
    std::cout << "sequence: " << sequenceInfo.sequence << ", max probability: " << sequenceInfo.probability
              << std::endl;
}

int main() {
    const std::string inputSequence = "UDUUD";

    auto simulationCalculator = CoinSequenceCalculator::create(CoinSequenceCalculator::Type::SIMULATION);
    calCoinMaxProbabilitySequence(*simulationCalculator, inputSequence);

    std::cout << "--------------------------------------------------------" << std::endl;

    auto formulaCalculator = CoinSequenceCalculator::create(CoinSequenceCalculator::Type::FORMULA);
    calCoinMaxProbabilitySequence(*formulaCalculator, inputSequence);

    return 0;
}
```

## [最大回撤][最大回撤_c++ 最大回撤-CSDN博客](https://blog.csdn.net/weixin_42806752/article/details/101565813)

```python
"""
题目形式：有一个数组，求其中两个数x,y，满足x的索引小于y的索引，使得 x-y 最大。
例如 arr = [3,7,2,6,4,1,9,8,5]， 最大回撤是6，对应的x=7,y=1。
"""


def maxRetreat(nums):
    """
    到了现在，我们应该对这种最大最小的最优化类的题目有一定的敏感度，知道要用动态规划。
    由于输入是一维数组，我们可以用一个一维数组来保存子问题的状态。
    dp[i]表示下标为i的元素的最大回撤
    那么转移方程为：
            dp[i+1] + nums[i] - nums[i+1], if dp[i + 1] > 0
    dp[i] = 
            nums[i] - nums[i+1], else
            
    其实这种题目我们应该做的就是使用一个例子来模拟一遍。如果我们从前往后计算每个下标的最大回撤，那
    么会有很多重复计算的子问题，这一点类似于字符串解码的问题。而如果我们从后往前来计算每个下标的最
    大回撤，那么就可以利用到已经计算出来的子问题的解。
    而这个状态转移方程也是基于观察得到的。
    
    :param nums: 待计算最大回撤的数组
    :return: 输入的数组的最大回撤
    """
    if not nums:
        return 0
    dp = [0] * len(nums)
    maxRet = 0  # 可以用一个变量来保存全局最大的回撤的值，也可以不用，但是这样可以节省一次遍历
    for i in range(len(nums) - 2, -1, -1):
        if dp[i + 1] > 0:
            dp[i] = dp[i + 1] + nums[i] - nums[i + 1]
        else:
            dp[i] = nums[i] - nums[i + 1]
        maxRet = max(maxRet, dp[i])

    return maxRet


print(maxRetreat([3, 7, 2, 6, 4, 1, 9, 8, 5]))
```

C++写的：

```cpp
#include "gtest/gtest.h"

using namespace std;

const double DoubleZero = 0.0;

const std::string MaxDrawDownRatioStr = "MaxDrawDownRatio";
const std::string MaxValueDownStr = "MaxValueDown";
const std::string FromDateStr = "FromDate";
const std::string ToDateStr = "ToDate";

class AnalyzerDrawDown {
public:
    ~AnalyzerDrawDown() noexcept = default;

    const std::unordered_map<std::string, std::string> add(const std::vector<std::string>& date,
                                                           const std::vector<double>& values) {
        dates_ = date;
        values_ = values;
        calcMaxDrawDown();
        setAnalysisMap();
        return analysisMap_;
    }

    std::string getAnalysisMsg() {
        std::ostringstream oss;
        oss << MaxDrawDownRatioStr << ": " << analysisMap_[MaxDrawDownRatioStr] << ", "
            << MaxValueDownStr << ": " << analysisMap_[MaxValueDownStr] << ", "
            << FromDateStr << ": " << valueMaxDate_ << ", "
            << ToDateStr << ": " << valueMinDate_;
        return oss.str();
    }

private:
    void calcMaxDrawDown() {
        double tmpValueMin;
        std::string tmpValueMinDate;

        double maxDrawDown = DoubleZero;
        std::vector<double> dp(values_.size(), DoubleZero);
        for (int i = values_.size() - 2; i >= 0; --i) {
            if (dp[i + 1] > DoubleZero) {
                dp[i] = dp[i + 1] + values_[i] - values_[i + 1];
            } else {
                dp[i] = values_[i] - values_[i + 1];
                tmpValueMin = values_[i + 1];
                tmpValueMinDate = dates_[i + 1];
            }

            if (dp[i] > maxDrawDown) {
                maxDrawDown = dp[i];
                valueMax_ = values_[i];
                valueMin_ = tmpValueMin;
                valueMaxDate_ = dates_[i];
                valueMinDate_ = tmpValueMinDate;
            }
        }
    }

    void setAnalysisMap() {
        analysisMap_ = {
            {MaxDrawDownRatioStr, std::to_string((valueMax_ - valueMin_) / valueMax_)},
            {MaxValueDownStr, std::to_string(valueMax_ - valueMin_)},
            {FromDateStr, valueMaxDate_},
            {ToDateStr, valueMinDate_}
        };
    }

private:
    std::unordered_map<std::string, std::string> analysisMap_;
    double valueMax_;
    double valueMin_;
    std::string valueMaxDate_;
    std::string valueMinDate_;
    std::vector<std::string> dates_;
    std::vector<double> values_;
};

class AnalyzerDrawDownTest : public testing::Test {
protected:
    AnalyzerDrawDown analyzerDrawDown;
};

TEST_F(AnalyzerDrawDownTest, Test1) {
    std::vector<std::string> dates = {"2017-01-01", "2017-01-02", "2017-01-03", "2017-01-04", "2017-01-05",
                                      "2017-01-06", "2017-01-07", "2017-01-08", "2017-01-09"};
    std::vector<double> values = {3, 7, 2, 6, 4, 1, 9, 8, 5};
    const auto& analysisMap = analyzerDrawDown.add(dates, values);
    std::cout << analyzerDrawDown.getAnalysisMsg() << std::endl;

    // 比较结果
    auto maxDrawDownRatio = analysisMap.at(MaxDrawDownRatioStr);
    auto expectMaxDrawDownRatio = std::to_string(static_cast<double >(7 - 1) / 7);
    EXPECT_EQ(maxDrawDownRatio, expectMaxDrawDownRatio);
    EXPECT_EQ(7 - 1, std::stod(analysisMap.at(MaxValueDownStr)));
    EXPECT_EQ("2017-01-02", analysisMap.at(FromDateStr));
    EXPECT_EQ("2017-01-06", analysisMap.at(ToDateStr));
}
```

## 数组元素和小于K的组合个数

1. 两个整数数组p、q和一个整数K，求满足p[i]+q[j] < K的所有(i, j)组合的个数。

最简单的就是双循环，时间复杂度就是O(n^2)。

优化一些的话就是两个数组排序p'和q'，然后固定外层循环p'从最小的数，q'从最大往小开始找，找到满足和小于K的，这时候所有q'的元素下标前的都满足。再把p'的下标+1，q'的上一个满足的数下标往前找。

2. 三个整数数组p、q、r和一个整数K，求满足p[i]+q[j]+r[s] < K的所有(i, j, s)组合的个数。

还是类似刚才的算法，先排序，再固定p'，里面q'和r'的循环还是用上面的算法，这样算下来应该是类似O(n^2)的复杂度。

网上的[三个数之和小于某个值的组合个数](https://blog.csdn.net/u013709270/article/details/104671747/)。

[373. 查找和最小的 K 对数字 ](https://leetcode.cn/problems/find-k-pairs-with-smallest-sums/solutions/1208350/cha-zhao-he-zui-xiao-de-kdui-shu-zi-by-l-z526/)

```cpp
class Solution {
public:
    int countCombinations(std::vector<int>& p, std::vector<int>& q, int target) {
        std::sort(p.begin(), p.end()); // 对数组p进行排序
        std::sort(q.begin(), q.end()); // 对数组q进行排序

        int count = 0;
        int i = 0, j = q.size() - 1;

        while (i < p.size() && j >= 0) {
            if (p[i] + q[j] < target) {
                count += (j + 1); // 将j的位置与j相同的元素个数加到count中
                i++;
            } else {
                j--;
            }
        }

        return count;
    }

private:
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1)
{
    std::vector<int> p = {1, 3, 5};
    std::vector<int> q = {2, 4, 6};
    int target = 7;

    int result = solution.countCombinations(p, q, target);
    EXPECT_EQ(3, result);
}
```





## 查找搜索二叉树中与目标值距离最近的节点

[数据结构——二叉搜索树详解](https://blog.csdn.net/L_T_W_Y/article/details/108407686)

从搜索二叉树根节点搜索，根据节点值与目标值的大小，进行左右节点的递归查找。

如果找到与目标值一样的节点，则直接返回；否则就直接遍历到叶子节点。

时间复杂度O(logN)。

```cpp
#include <climits>
#include "gtest/gtest.h"

struct Node {
    explicit Node(int value) : val(value)
    {
    }

    Node* left = nullptr;
    Node* right = nullptr;
    int val;
};

class Solution {
public:
    std::vector<int> getMinDistance(Node* root, int target)
    {
        int minDistance = INT_MAX;
        std::vector<int> ret;
        dfs(root, target, minDistance, ret);
        return ret;
    }

private:
    void dfs(Node* node, int target, int& minDistance, std::vector<int>& ret)
    {
        if (node == nullptr) {
            return;
        }

        int curDistance = std::abs(node->val - target);
        if (curDistance == 0) {
            ret.assign(1, node->val);
            return;
        }

        if (curDistance < minDistance) {
            ret.assign(1, node->val);
            minDistance = curDistance;
        } else if (curDistance == minDistance) {
            ret.push_back(node->val);
        }

        if (node->val > target) {
            dfs(node->left, target, minDistance, ret);
        } else {
            dfs(node->right, target, minDistance, ret);
        }
    }
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1)
{
    auto root = std::make_unique<Node>(14);
    auto node7 = std::make_unique<Node>(7);
    auto node28 = std::make_unique<Node>(28);
    root->left = node7.get();
    root->right = node28.get();

    auto node1 = std::make_unique<Node>(1);
    auto node10 = std::make_unique<Node>(10);
    node7->left = node1.get();
    node7->right = node10.get();

    auto nodeNeg3 = std::make_unique<Node>(-3);
    auto node5 = std::make_unique<Node>(5);
    node1->left = nodeNeg3.get();
    node1->right = node5.get();

    EXPECT_EQ(std::vector<int>({1}), solution.getMinDistance(root.get(), 1));
    EXPECT_EQ(std::vector<int>({7}), solution.getMinDistance(root.get(), 8));
    EXPECT_EQ(std::vector<int>({1, 5}), solution.getMinDistance(root.get(), 3));
    EXPECT_EQ(std::vector<int>({-3}), solution.getMinDistance(root.get(), -5));
    EXPECT_EQ(std::vector<int>({28}), solution.getMinDistance(root.get(), 30));
}
```
