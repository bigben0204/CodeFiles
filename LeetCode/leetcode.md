# 1. LeetCode

<https://leetcode.com/problemset/all/>

## 1.1. 747 Largest Number At Least Twice of Others

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

## 1.2. 406 Queue Reconstruction by Height

<https://leetcode.com/problems/queue-reconstruction-by-height/>

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

## 1.3. 324 Wiggle Sort II

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

## 1.4. 1144 Decrease Elements To Make Array Zigzag

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

## 1.5. 997 Find the Town Judge

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

## 1.6. 11 盛最多水的容器

<https://leetcode-cn.com/problems/container-with-most-water/>

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

## 1.7. 75 Sort Colors

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

## 1.8. 面试官D考试Demo1：

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

## 1.9. 441 Arranging Coins

<https://leetcode.com/problems/arranging-coins/submissions/>

```java
class Solution {
    public int arrangeCoins(int n) {
        return (int) Math.floor((-1 + Math.sqrt(1 + 8 * (long) n)) / 2);
    }
}
```

## 1.10. 632 最小区间

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

## 1.11. 831 隐藏个人信息

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

## 1.12. 516 最长回文子序列

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

## 1.13. 299 Bulls and Cows

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

## 1.14. 955 Delete Columns to Make Sorted II

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

## 1.15. 594 Longest Harmonious Subsequence

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

## 1.16. 165 Compare Version Numbers

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

## 1.17. 493 Reverse Pairs

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

## 1.18. 1 Two Sum

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
        // Collectors.toMap的第三个参数表示重复的Key时，如何处理Value值，(a, b) -> a表示保留用第一个Value，(a, b) -> b表示用第一个Value
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

## 1.19. 3 Longest Substring Without Repeating Characters

<https://leetcode.com/problems/longest-substring-without-repeating-characters/>

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

## 1.20. 621 Task Scheduler

<https://leetcode.com/problems/task-scheduler/>

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

## 1.21. 554 Brick Wall

<https://leetcode.com/problems/brick-wall/>

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