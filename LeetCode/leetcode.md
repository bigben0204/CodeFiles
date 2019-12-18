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
同 5、两个字符串最大公共子序列

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

## [3. Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/)(华为题库)

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

// C++
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

## [98. 验证二叉搜索树](https://leetcode-cn.com/problems/validate-binary-search-tree/)(华为题库)

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

我们使用
[中序遍历](https://leetcode-cn.com/problems/binary-tree-inorder-traversal/solution/er-cha-shu-de-zhong-xu-bian-li-by-leetcode/)
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









