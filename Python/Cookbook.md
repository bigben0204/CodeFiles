Python3 Cookbook

# 1. 数据结构与算法

## 1.1. 解压序列赋值给多个变量

```python
>>> p = (4, 5)
>>> x, y = p
>>> x
4
>>> y
5
>>>
>>> data = [ 'ACME', 50, 91.1, (2012, 12, 21) ]
>>> name, shares, price, date = data
>>> name
'ACME'
>>> date
(2012, 12, 21)
>>> name, shares, price, (year, mon, day) = data
>>> name
'ACME'
>>> year
2012
>>> mon
12
>>> day
21
```

## 1.2. 解压可迭代对象赋值给多个变量

<https://python3-cookbook.readthedocs.io/zh_CN/latest/c01/p02_unpack_elements_from_iterables.html>

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-


if __name__ == '__main__':
    record = ('Dave', 'dave@example.com', '773-555-1212', '847-555-1212')
    name, email, *phone_numbers = record
    print(phone_numbers)

输出：
['773-555-1212', '847-555-1212']
```

```python
record = ('ACME', 50, 123.45, (12, 18, 2012))
name, *_, (*_, year) = record
print(name, year)
输出：
ACME 2012
```

在很多函数式语言中，星号解压语法跟列表处理有许多相似之处。比如，如果你有一个列表， 你可以很容易的将它分割成前后两部分：

```python
>>> items = [1, 10, 7, 4, 5, 9]
>>> head, *tail = items
>>> head
1
>>> tail
[10, 7, 4, 5, 9]
>>>
```

如果你够聪明的话，还能用这种分割语法去巧妙的实现递归算法。比如：

```python
>>> def sum(items):
...     head, *tail = items
...     return head + sum(tail) if tail else head
...
>>> sum(items)
36
>>>
```

## 1.3. 保留最后 N 个元素

```python
from collections import deque


def search(lines, pattern, history=5):
    previous_lines = deque(maxlen=history)
    for line in lines:
        if pattern in line:
            yield line, previous_lines
        previous_lines.append(line)

# Example use on a file
if __name__ == '__main__':
    with open(r'../../cookbook/somefile.txt') as f:
        for line, prevlines in search(f, 'python', 5):
            for pline in prevlines:
                print(pline, end='')
            print(line, end='')
            print('-' * 20)
```

我们在写查询元素的代码时，通常会使用包含 yield 表达式的生成器函数，也就是我们上面示例代码中的那样。 这样可以将搜索过程代码和使用搜索结果代码解耦。如果你还不清楚什么是生成器，请参看 4.3 节。

使用 deque(maxlen=N) 构造函数会新建一个固定大小的队列。当新的元素加入并且这个队列已满的时候， 最老的元素会自动被移除掉。

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from collections import deque

if __name__ == '__main__':
    q = deque(maxlen=3)
    q.append(1)
    q.append(2)
    q.append(3)
    print(q)
    print(q.maxlen)

    q.append(4)
    print(q)
输出：
deque([1, 2, 3], maxlen=3)
3
deque([2, 3, 4], maxlen=3)
```

更一般的， deque 类可以被用在任何你只需要一个简单队列数据结构的场合。 如果你不设置最大队列大小，那么就会得到一个无限大小队列，你可以在队列的两端执行添加和弹出元素的操作。

在队列两端插入或删除元素时间复杂度都是 O(1) ，区别于列表，在列表的开头插入或删除元素的时间复杂度为 O(N) 。

```python
>>> q = deque()
>>> q.append(1)
>>> q.append(2)
>>> q.append(3)
>>> q
deque([1, 2, 3])
>>> q.appendleft(4)
>>> q
deque([4, 1, 2, 3])
>>> q.pop()
3
>>> q
deque([4, 1, 2])
>>> q.popleft()
4
```

## 1.4. 查找最大或最小的 N 个元素

<https://python3-cookbook.readthedocs.io/zh_CN/latest/c01/p04_find_largest_or_smallest_n_items.html>

heapq 模块有两个函数：nlargest() 和 nsmallest() 可以完美解决这个问题。

```python
import heapq
nums = [1, 8, 2, 23, 7, -4, 18, 23, 42, 37, 2]
print(heapq.nlargest(3, nums)) # Prints [42, 37, 23]
print(heapq.nsmallest(3, nums)) # Prints [-4, 1, 2]
```

两个函数都能接受一个关键字参数，用于更复杂的数据结构中：

```python

portfolio = [
    {'name': 'IBM', 'shares': 100, 'price': 91.1},
    {'name': 'AAPL', 'shares': 50, 'price': 543.22},
    {'name': 'FB', 'shares': 200, 'price': 21.09},
    {'name': 'HPQ', 'shares': 35, 'price': 31.75},
    {'name': 'YHOO', 'shares': 45, 'price': 16.35},
    {'name': 'ACME', 'shares': 75, 'price': 115.65}
]
cheap = heapq.nsmallest(3, portfolio, key=lambda s: s['price'])
expensive = heapq.nlargest(3, portfolio, key=lambda s: s['price'])

输出：
[{'name': 'YHOO', 'shares': 45, 'price': 16.35}, {'name': 'FB', 'shares': 200, 'price': 21.09}, {'name': 'HPQ', 'shares': 35, 'price': 31.75}]
[{'name': 'AAPL', 'shares': 50, 'price': 543.22}, {'name': 'ACME', 'shares': 75, 'price': 115.65}, {'name': 'IBM', 'shares': 100, 'price': 91.1}]
```

译者注：上面代码在对每个元素进行对比的时候，会以 price 的值进行比较。

讨论：

如果你想在一个集合中查找最小或最大的 N 个元素，并且 N 小于集合元素数量，那么这些函数提供了很好的性能。 因为在底层实现里面，首先会先将集合数据进行堆排序后放入一个列表中：

堆数据结构最重要的特征是 heap[0] 永远是最小的元素。并且剩余的元素可以很容易的通过调用 heapq.heappop() 方法得到， 该方法会先将第一个元素弹出来，然后用下一个最小的元素来取代被弹出元素（这种操作时间复杂度仅仅是 O(log N)，N 是堆大小）。 比如，如果想要查找最小的 3 个元素，你可以这样做：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import heapq

if __name__ == '__main__':
    nums = [1, 8, 2, 23, 7, -4, 18, 23, 42, 37, 2]
    heap = list(nums)
    print(heap)
    heapq.heapify(heap)
    print(heap)

    heapq.heappop(heap)
    print(heap)
    heapq.heappop(heap)
    print(heap)
    heapq.heappop(heap)
    print(heap)
输出：
[1, 8, 2, 23, 7, -4, 18, 23, 42, 37, 2]
[-4, 2, 1, 23, 7, 2, 18, 23, 42, 37, 8]
[1, 2, 2, 23, 7, 8, 18, 23, 42, 37]
[2, 2, 8, 23, 7, 37, 18, 23, 42]
[2, 7, 8, 23, 42, 37, 18, 23]
```

当要查找的元素个数相对比较小的时候，函数 nlargest() 和 nsmallest() 是很合适的。 如果你仅仅想查找唯一的最小或最大（N=1）的元素的话，那么使用 min() 和 max() 函数会更快些。 类似的，如果 N 的大小和集合大小接近的时候，通常先排序这个集合然后再使用切片操作会更快点 （ sorted(items)[:N] 或者是 sorted(items)[-N:] ）。 需要在正确场合使用函数 nlargest() 和 nsmallest() 才能发挥它们的优势 （如果 N 快接近集合大小了，那么使用排序操作会更好些）。

尽管你没有必要一定使用这里的方法，但是堆数据结构的实现是一个很有趣并且值得你深入学习的东西。 基本上只要是数据结构和算法书籍里面都会有提及到。 heapq 模块的官方文档里面也详细的介绍了堆数据结构底层的实现细节。

## 1.5. 实现一个优先级队列

<https://python3-cookbook.readthedocs.io/zh_CN/latest/c01/p05_implement_a_priority_queue.html>

仔细观察可以发现，第一个 pop() 操作返回优先级最高的元素。 另外注意到如果两个有着相同优先级的元素（ foo 和 grok ），pop 操作按照它们被插入到队列的顺序返回的。

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import heapq


class PriorityQueue:
    def __init__(self):
        self._queue = []
        self._index = 0

    def push(self, item, priority):
        heapq.heappush(self._queue, (-priority, self._index, item))
        self._index += 1

    def pop(self):
        return heapq.heappop(self._queue)[-1]


class Item:
    def __init__(self, name):
        self.name = name

    def __repr__(self):
        return 'Item({!r})'.format(self.name)


if __name__ == '__main__':
    q = PriorityQueue()
    q.push(Item('foo'), 1)
    q.push(Item('bar'), 5)
    q.push(Item('spam'), 4)
    q.push(Item('grok'), 1)
    print(q.pop())
    print(q.pop())
    print(q.pop())
    print(q.pop())

```

这一小节我们主要关注 heapq 模块的使用。 函数 heapq.heappush() 和 heapq.heappop() 分别在队列 _queue 上插入和删除第一个元素， 并且队列 _queue 保证第一个元素拥有最高优先级（ 1.4 节已经讨论过这个问题）。 heappop() 函数总是返回”最小的”的元素，这就是保证队列pop操作返回正确元素的关键。 另外，由于 push 和 pop 操作时间复杂度为 O(log N)，其中 N 是堆的大小，因此就算是 N 很大的时候它们运行速度也依旧很快。

在上面代码中，队列包含了一个 (-priority, index, item) 的元组。 优先级为负数的目的是使得元素按照优先级从高到低排序。 这个跟普通的按优先级从低到高排序的堆排序恰巧相反。

index 变量的作用是保证同等优先级元素的正确排序。 通过保存一个不断增加的 index 下标变量，可以确保元素按照它们插入的顺序排序。 而且， index 变量也在相同优先级元素比较的时候起到重要作用。

为了阐明这些，先假定 Item 实例是不支持排序的：

```python
>>> a = Item('foo')
>>> b = Item('bar')
>>> a < b
Traceback (most recent call last):
File "<stdin>", line 1, in <module>
TypeError: unorderable types: Item() < Item()
>>>
```

如果你使用元组 (priority, item) ，只要两个元素的优先级不同就能比较。 但是如果两个元素优先级一样的话，那么比较操作就会跟之前一样出错：

```python
>>> a = (1, Item('foo'))
>>> b = (5, Item('bar'))
>>> a < b
True
>>> c = (1, Item('grok'))
>>> a < c
Traceback (most recent call last):
File "<stdin>", line 1, in <module>
TypeError: unorderable types: Item() < Item()
>>>
```

通过引入另外的 index 变量组成三元组 (priority, index, item) ，就能很好的避免上面的错误， 因为不可能有两个元素有相同的 index 值。Python 在做元组比较时候，如果前面的比较已经可以确定结果了， 后面的比较操作就不会发生了：

```python
>>> a = (1, 0, Item('foo'))
>>> b = (5, 1, Item('bar'))
>>> c = (1, 2, Item('grok'))
>>> a < b
True
>>> a < c
True
>>>
```

如果你想在多个线程中使用同一个队列，那么你需要增加适当的锁和信号量机制。 可以查看 12.3 小节的例子演示是怎样做的。

## 1.6. 字典中的键映射多个值

<https://python3-cookbook.readthedocs.io/zh_CN/latest/c01/p06_map_keys_to_multiple_values_in_dict.html>

你可以很方便的使用 collections 模块中的 defaultdict 来构造这样的字典。 defaultdict 的一个特征是它会自动初始化每个 key 刚开始对应的值，所以你只需要关注添加元素操作了。比如：

```python
from collections import defaultdict

d = defaultdict(list)
d['a'].append(1)
d['a'].append(2)
d['b'].append(4)

d = defaultdict(set)
d['a'].add(1)
d['a'].add(2)
d['b'].add(4)
```

需要注意的是， defaultdict 会自动为将要访问的键（就算目前字典中并不存在这样的键）创建映射实体。 如果你并不需要这样的特性，你可以在一个普通的字典上使用 setdefault() 方法来代替。比如：

```python
d = {} # 一个普通的字典
d.setdefault('a', []).append(1)
d.setdefault('a', []).append(2)
d.setdefault('b', []).append(4)
```

一般来讲，创建一个多值映射字典是很简单的。但是，如果你选择自己实现的话，那么对于值的初始化可能会有点麻烦， 你可能会像下面这样来实现：

如果使用 defaultdict 的话代码就更加简洁了：

```python
d = {}
for key, value in pairs:
    if key not in d:
        d[key] = []
    d[key].append(value)


d = defaultdict(list)
for key, value in pairs:
    d[key].append(value)
```

## 1.7. 字典排序

为了能控制一个字典中元素的顺序，你可以使用 collections 模块中的 OrderedDict 类。 在迭代操作的时候它会保持元素被插入时的顺序，示例如下：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import json
from collections import OrderedDict

from sortedcollections import OrderedSet

if __name__ == '__main__':
    d = dict()
    d['foo'] = 1
    d['bar'] = 2
    d['spam'] = 3
    d['grok'] = 4
    print(d)

    d = OrderedDict()
    d['foo'] = 1
    d['bar'] = 2
    d['spam'] = 3
    d['grok'] = 4
    # for key, value in d.items():
    #     print(key, value)
    print(d)

    s = set()
    s.add('foo')
    s.add('bar')
    s.add('spam')
    print(s)

    s = OrderedSet()
    s.add('foo')
    s.add('bar')
    s.add('spam')
    print(s)
输出：
{'foo': 1, 'bar': 2, 'spam': 3, 'grok': 4}
OrderedDict([('foo', 1), ('bar', 2), ('spam', 3), ('grok', 4)])
{'spam', 'bar', 'foo'}
OrderedSet(['foo', 'bar', 'spam'])
```

当你想要构建一个将来需要序列化或编码成其他格式的映射的时候， OrderedDict 是非常有用的。 比如，你想精确控制以 JSON 编码后字段的顺序，你可以先使用 OrderedDict 来构建这样的数据：

```python
>>> import json
>>> json.dumps(d)
'{"foo": 1, "bar": 2, "spam": 3, "grok": 4}'
```

OrderedDict 内部维护着一个根据键插入顺序排序的双向链表。每次当一个新的元素插入进来的时候， 它会被放到链表的尾部。对于一个已经存在的键的重复赋值不会改变键的顺序。

需要注意的是，一个 OrderedDict 的大小是一个普通字典的两倍，因为它内部维护着另外一个链表。 所以如果你要构建一个需要大量 OrderedDict 实例的数据结构的时候（比如读取 100,000 行 CSV 数据到一个 OrderedDict 列表中去）， 那么你就得仔细权衡一下是否使用 OrderedDict 带来的好处要大过额外内存消耗的影响。

## 1.8. 字典的运算

怎样在数据字典中执行一些计算操作（比如求最小值、最大值、排序等等）？

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

if __name__ == '__main__':
    l1 = [1, 2, 3]
    l2 = ['a', 'b', 'c']
    z = zip(l1, l2)
    for i in z:
        print(i)

    prices = {
        'ACME': 45.23,
        'AAPL': 612.78,
        'IBM': 205.55,
        'HPQ': 37.20,
        'FB': 10.75
    }

    min_price = min(zip(prices.values(), prices.keys()))
    print(min_price)
    # min_price is (10.75, 'FB')
    max_price = max(zip(prices.values(), prices.keys()))
    print(max_price)
    # max_price is (612.78, 'AAPL')

    prices_sorted = sorted(zip(prices.values(), prices.keys()), reverse=True)
    # prices_sorted is [(10.75, 'FB'), (37.2, 'HPQ'),
    #                   (45.23, 'ACME'), (205.55, 'IBM'),
    #                   (612.78, 'AAPL')]
    print(prices_sorted)

    # 直接对dict排序成tuple list
    prices_sorted = sorted(prices.items(), key=lambda t: t[1])
    print(prices_sorted)
输出：
(1, 'a')
(2, 'b')
(3, 'c')
(10.75, 'FB')
(612.78, 'AAPL')
[(612.78, 'AAPL'), (205.55, 'IBM'), (45.23, 'ACME'), (37.2, 'HPQ'), (10.75, 'FB')]
[('AAPL', 612.78), ('IBM', 205.55), ('ACME', 45.23), ('HPQ', 37.2), ('FB', 10.75)]
```

执行这些计算的时候，需要注意的是 zip() 函数创建的是一个只能访问一次的迭代器。 比如，下面的代码就会产生错误：

```python
prices_and_names = zip(prices.values(), prices.keys())
print(min(prices_and_names)) # OK
print(max(prices_and_names)) # ValueError: max() arg is an empty sequence
```

如果你在一个字典上执行普通的数学运算，你会发现它们仅仅作用于键，而不是值。比如：

```python
min(prices) # Returns 'AAPL'
max(prices) # Returns 'IBM'
```

不幸的是，通常这个结果同样也不是你想要的。 你可能还想要知道对应的键的信息（比如那种股票价格是最低的？）。

你可以在 min() 和 max() 函数中提供 key 函数参数来获取最小值或最大值对应的键的信息。比如：

```python
min(prices, key=lambda k: prices[k]) # Returns 'FB'
max(prices, key=lambda k: prices[k]) # Returns 'AAPL'
```

但是，如果还想要得到最小值，你又得执行一次查找操作。比如：

```python
min_value = prices[min(prices, key=lambda k: prices[k])]
```

需要注意的是在计算操作中使用到了 (值，键) 对。当多个实体拥有相同的值的时候，键会决定返回结果。 比如，在执行 min() 和 max() 操作的时候，如果恰巧最小或最大值有重复的，那么拥有最小或最大键的实体会返回：

```python
>>> prices = { 'AAA' : 45.23, 'ZZZ': 45.23 }
>>> min(zip(prices.values(), prices.keys()))
(45.23, 'AAA')
>>> max(zip(prices.values(), prices.keys()))
(45.23, 'ZZZ')
```

## 1.9. 查找两字典的相同点

怎样在两个字典中寻寻找相同点（比如相同的键、相同的值等等）？

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

if __name__ == '__main__':
    a = {
        'x': 1,
        'y': 2,
        'z': 3
    }

    b = {
        'w': 10,
        'x': 11,
        'y': 2
    }

    # Find keys in common
    print(a.keys() & b.keys())  # { 'x', 'y' }
    # Find keys in a that are not in b
    print(a.keys() - b.keys())  # { 'z' }
    # Find (key,value) pairs in common
    print(a.items() & b.items())  # { ('y', 2) }

    # print(a.values() & b.values())  # values()不是set，不能进行集合操作

    # Make a new dictionary with certain keys removed
    c = {key: a[key] for key in a.keys() - {'z', 'w'}}
    # c is {'x': 1, 'y': 2}
    print(c)
输出：
{'y', 'x'}
{'z'}
{('y', 2)}
{'y': 2, 'x': 1}
```

一个字典就是一个键集合与值集合的映射关系。 字典的 keys() 方法返回一个展现键集合的键视图对象。 键视图的一个很少被了解的特性就是它们也支持集合操作，比如集合并、交、差运算。 所以，如果你想对集合的键执行一些普通的集合操作，可以直接使用键视图对象而不用先将它们转换成一个 set。

字典的 items() 方法返回一个包含 (键，值) 对的元素视图对象。 这个对象同样也支持集合操作，并且可以被用来查找两个字典有哪些相同的键值对。

尽管字典的 values() 方法也是类似，但是它并不支持这里介绍的集合操作。 某种程度上是因为值视图不能保证所有的值互不相同，这样会导致某些集合操作会出现问题。 不过，如果你硬要在值上面执行这些集合操作的话，你可以先将值集合转换成 set，然后再执行集合运算就行了。

## 1.10. 删除序列相同元素并保持顺序

怎样在一个序列上面保持元素顺序的同时消除重复的值？

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-


# def dedupe(items):
#     seen = set()
#     for item in items:
#         if item not in seen:
#             yield item
#             seen.add(item)


def dedupe(items, key=None):
    seen = set()
    for item in items:
        val = item if key is None else key(item)
        if val not in seen:
            yield item
            seen.add(val)


if __name__ == '__main__':
    a = [1, 5, 2, 1, 9, 1, 5, 10]
    print(list(dedupe(a)))  # 使用list(OrderedSet(a))可以达到同样目的

    a = [{'x': 1, 'y': 2}, {'x': 1, 'y': 3}, {'x': 1, 'y': 2}, {'x': 2, 'y': 4}]
    print(list(dedupe(a, key=lambda d: (d['x'], d['y']))))  # 对于dict，按(value1, value2)去重
    print(list(dedupe(a, key=lambda d: d['x'])))  # 对于dict，按value1去重
输出：
[1, 5, 2, 9, 10]
[{'x': 1, 'y': 2}, {'x': 1, 'y': 3}, {'x': 2, 'y': 4}]
[{'x': 1, 'y': 2}, {'x': 2, 'y': 4}]
```

如果你仅仅就是想消除重复元素，通常可以简单的构造一个集合。比如：

```python
>>> a
[1, 5, 2, 1, 9, 1, 5, 10]
>>> set(a)
{1, 2, 10, 5, 9}
```


然而，这种方法不能维护元素的顺序，生成的结果中的元素位置被打乱。而上面的方法可以避免这种情况。

在本节中我们使用了生成器函数让我们的函数更加通用，不仅仅是局限于列表处理。 比如，如果如果你想读取一个文件，消除重复行，你可以很容易像这样做：

```python
with open(somefile,'r') as f:
    for line in dedupe(f):
        #  ...
```

上述key函数参数模仿了 sorted() , min() 和 max() 等内置函数的相似功能。 可以参考 1.8 和 1.13 小节了解更多。

## 1.11. 命名切片

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-


if __name__ == '__main__':
    #####    0123456789012345678901234567890123456789012345678901234567890'
    record = '....................100 .......513.25 ..........'
    cost = int(record[20:23]) * float(record[31:37])
    print(cost)

    SHARES = slice(20, 23)
    PRICE = slice(31, 37)
    cost = int(record[SHARES]) * float(record[PRICE])
    print(cost)
```

```python
>>> items = [0, 1, 2, 3, 4, 5, 6]
>>> a = slice(2, 4)
>>> items[2:4]
[2, 3]
>>> items[a]
[2, 3]
>>> items[a] = [10,11]
>>> items
[0, 1, 10, 11, 4, 5, 6]
>>> del items[a]
>>> items
[0, 1, 4, 5, 6]
```

如果你有一个切片对象a，你可以分别调用它的 a.start , a.stop , a.step 属性来获取更多的信息。比如：

```python
>>> a = slice(5, 50, 2)
>>> a.start
5
>>> a.stop
50
>>> a.step
2
```

```python
a = slice(5, 50, 2)
print(a.start)
print(a.stop)
print(a.step)
print(a)

s = 'HelloWorld'
b = a.indices(len(s))
print(b)
for i in range(*a.indices(len(s))):  # 注意这里要将slice对象解成三个对象，start, stop, step才可以range
    print(s[i])
输出：
5
50
2
slice(5, 50, 2)
(5, 10, 2)
W
r
d
```

## 1.12. 序列中出现次数最多的元素

作为输入， Counter 对象可以接受任意的由可哈希（hashable）元素构成的序列对象。 在底层实现上，一个 Counter 对象就是一个字典，将元素映射到它出现的次数上。比如：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from collections import Counter

if __name__ == '__main__':
    words = [
        'look', 'into', 'my', 'eyes', 'look', 'into', 'my', 'eyes',
        'the', 'eyes', 'the', 'eyes', 'the', 'eyes', 'not', 'around', 'the',
        'eyes', "don't", 'look', 'around', 'the', 'eyes', 'look', 'into',
        'my', 'eyes', "you're", 'under'
    ]

    word_counts = Counter(words)
    # 出现频率最高的3个单词
    top_three = word_counts.most_common(3)
    print(top_three, type(top_three))
    # Outputs [('eyes', 8), ('the', 5), ('look', 4)]

    print(word_counts['look'])
    print(word_counts['abc'])  # 不存在的对象并不会增加到Counter对象中

    morewords = ['why', 'are', 'you', 'not', 'looking', 'in', 'my', 'eyes']
    for word in morewords:
        word_counts[word] += 1
    print(word_counts['eyes'])

    word_counts.update(morewords)
    print(word_counts, type(word_counts))
输出：
[('eyes', 8), ('the', 5), ('look', 4)] <class 'list'>
4
0
9
Counter({'eyes': 10, 'my': 5, 'the': 5, 'look': 4, 'into': 3, 'not': 3, 'around': 2, 'why': 2, 'are': 2, 'you': 2, 'looking': 2, 'in': 2, "don't": 1, "you're": 1, 'under': 1}) <class 'collections.Counter'>
```

Counter 实例一个鲜为人知的特性是它们可以很容易的跟数学运算操作相结合。比如：

```python
words = [
    'look', 'into', 'my', 'eyes', 'look', 'into', 'my', 'eyes',
    'the', 'eyes', 'the', 'eyes', 'the', 'eyes', 'not', 'around', 'the',
    'eyes', "don't", 'look', 'around', 'the', 'eyes', 'look', 'into',
    'my', 'eyes', "you're", 'under'
]
morewords = ['why', 'are', 'you', 'not', 'looking', 'in', 'my', 'eyes']

a = Counter(words)
b = Counter(morewords)
print(a)
print(b)
# Combine counts
c = a + b
print(c)
# Subtract counts
d = a - b
print(d)
输出：
Counter({'eyes': 8, 'the': 5, 'look': 4, 'into': 3, 'my': 3, 'around': 2, 'not': 1, "don't": 1, "you're": 1, 'under': 1})
Counter({'why': 1, 'are': 1, 'you': 1, 'not': 1, 'looking': 1, 'in': 1, 'my': 1, 'eyes': 1})
Counter({'eyes': 9, 'the': 5, 'look': 4, 'my': 4, 'into': 3, 'not': 2, 'around': 2, "don't": 1, "you're": 1, 'under': 1, 'why': 1, 'are': 1, 'you': 1, 'looking': 1, 'in': 1})
Counter({'eyes': 7, 'the': 5, 'look': 4, 'into': 3, 'my': 2, 'around': 2, "don't": 1, "you're": 1, 'under': 1})
```

毫无疑问， Counter 对象在几乎所有需要制表或者计数数据的场合是非常有用的工具。 在解决这类问题的时候你应该优先选择它，而不是手动的利用字典去实现。

## 1.13. 通过某个关键字排序一个字典列表

通过使用 operator 模块的 itemgetter 函数，可以非常容易的排序这样的数据结构。 

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from operator import itemgetter

if __name__ == '__main__':
    rows = [
        {'fname': 'Brian', 'lname': 'Jones', 'uid': 1003},
        {'fname': 'David', 'lname': 'Beazley', 'uid': 1002},
        {'fname': 'John', 'lname': 'Cleese', 'uid': 1001},
        {'fname': 'Big', 'lname': 'Jones', 'uid': 1004}
    ]

    rows_by_fname = sorted(rows, key=itemgetter('fname'))
    rows_by_uid = sorted(rows, key=itemgetter('uid'))
    print(rows_by_fname)
    print(rows_by_uid)

    # itemgetter() 函数也支持多个 keys，比如下面的代码
    rows_by_lfname = sorted(rows, key=itemgetter('lname','fname'))
    print(rows_by_lfname)

    rows_by_lfname = sorted(rows, key=itemgetter('lname', 'uid'))
    print(rows_by_lfname)

    print(rows[0].__getitem__("fname"))
输出：
[{'fname': 'Big', 'lname': 'Jones', 'uid': 1004}, {'fname': 'Brian', 'lname': 'Jones', 'uid': 1003}, {'fname': 'David', 'lname': 'Beazley', 'uid': 1002}, {'fname': 'John', 'lname': 'Cleese', 'uid': 1001}]
[{'fname': 'John', 'lname': 'Cleese', 'uid': 1001}, {'fname': 'David', 'lname': 'Beazley', 'uid': 1002}, {'fname': 'Brian', 'lname': 'Jones', 'uid': 1003}, {'fname': 'Big', 'lname': 'Jones', 'uid': 1004}]
[{'fname': 'David', 'lname': 'Beazley', 'uid': 1002}, {'fname': 'John', 'lname': 'Cleese', 'uid': 1001}, {'fname': 'Big', 'lname': 'Jones', 'uid': 1004}, {'fname': 'Brian', 'lname': 'Jones', 'uid': 1003}]
[{'fname': 'David', 'lname': 'Beazley', 'uid': 1002}, {'fname': 'John', 'lname': 'Cleese', 'uid': 1001}, {'fname': 'Brian', 'lname': 'Jones', 'uid': 1003}, {'fname': 'Big', 'lname': 'Jones', 'uid': 1004}]
Brian
```

在上面例子中， rows 被传递给接受一个关键字参数的 sorted() 内置函数。 这个参数是 callable 类型，并且从 rows 中接受一个单一元素，然后返回被用来排序的值。 itemgetter() 函数就是负责创建这个 callable 对象的。

operator.itemgetter() 函数有一个被 rows 中的记录用来查找值的索引参数。可以是一个字典键名称， 一个整形值或者任何能够传入一个对象的 __getitem__() 方法的值。 如果你传入多个索引参数给 itemgetter() ，它生成的 callable 对象会返回一个包含所有元素值的元组， 并且 sorted() 函数会根据这个元组中元素顺序去排序。 但你想要同时在几个字段上面进行排序（比如通过姓和名来排序，也就是例子中的那样）的时候这种方法是很有用的。

itemgetter() 有时候也可以用 lambda 表达式代替，比如：

```python
rows_by_fname = sorted(rows, key=lambda r: r['fname'])
rows_by_lfname = sorted(rows, key=lambda r: (r['lname'],r['fname']))
```

这种方案也不错。但是，使用 itemgetter() 方式会运行的稍微快点。因此，如果你对性能要求比较高的话就使用 itemgetter() 方式。

最后，不要忘了这节中展示的技术也同样适用于 min() 和 max() 等函数。比如：

```python
>>> min(rows, key=itemgetter('uid'))
{'fname': 'John', 'lname': 'Cleese', 'uid': 1001}
>>> max(rows, key=itemgetter('uid'))
{'fname': 'Big', 'lname': 'Jones', 'uid': 1004}
```

## 1.14. 排序不支持原生比较的对象

内置的 sorted() 函数有一个关键字参数 key ，可以传入一个 callable 对象给它， 这个 callable 对象对每个传入的对象返回一个值，这个值会被 sorted 用来排序这些对象。 比如，如果你在应用程序里面有一个 User 实例序列，并且你希望通过他们的 user_id 属性进行排序， 你可以提供一个以 User 实例作为输入并输出对应 user_id 值的 callable 对象。比如：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from operator import attrgetter


class User:
    def __init__(self, user_id):
        self.user_id = user_id  # 如果改成__user_id，没找到可以使用attrgetter的方法，只能用'_User__user_id'

    def __repr__(self):
        return 'User({})'.format(self.user_id)

    def get_uid(self):
        return self.user_id


if __name__ == '__main__':
    users = [User(23), User(3), User(99)]
    print(users)
    # print(sorted(users, key=lambda u: u.get_uid()))
    print(sorted(users, key=attrgetter('user_id')))
输出：
[User(23), User(3), User(99)]
[User(3), User(23), User(99)]
```

选择使用 lambda 函数或者是 attrgetter() 可能取决于个人喜好。 但是， attrgetter() 函数通常会运行的快点，并且还能同时允许多个字段进行比较。 这个跟 operator.itemgetter() 函数作用于字典类型很类似（参考1.13小节）。 例如，如果 User 实例还有一个 first_name 和 last_name 属性，那么可以向下面这样排序：

`by_name = sorted(users, key=attrgetter('last_name', 'first_name'))`

同样需要注意的是，这一小节用到的技术同样适用于像 min() 和 max() 之类的函数。比如：

```python
>>> min(users, key=attrgetter('user_id'))
User(3)
>>> max(users, key=attrgetter('user_id'))
User(99)
```

## 1.15. 通过某个字段将记录分组

itertools.groupby() 函数对于这样的数据分组操作非常实用。 为了演示，假设你已经有了下列的字典列表：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from itertools import groupby
from operator import itemgetter

if __name__ == '__main__':
    rows = [
        {'address': '5412 N CLARK', 'date': '07/01/2012'},
        {'address': '5148 N CLARK', 'date': '07/04/2012'},
        {'address': '5800 E 58TH', 'date': '07/02/2012'},
        {'address': '2122 N CLARK', 'date': '07/03/2012'},
        {'address': '5645 N RAVENSWOOD', 'date': '07/02/2012'},
        {'address': '1060 W ADDISON', 'date': '07/02/2012'},
        {'address': '4801 N BROADWAY', 'date': '07/01/2012'},
        {'address': '1039 W GRANVILLE', 'date': '07/04/2012'},
    ]

    # Sort by the desired field first
    rows.sort(key=itemgetter('date'))
    # Iterate in groups
    for date, items in groupby(rows, key=itemgetter('date')):
        print(date)
        for i in items:
            print(' ', i)

    print(type(groupby(rows, key=itemgetter('date'))))
运行结果：
07/01/2012
  {'address': '5412 N CLARK', 'date': '07/01/2012'}
  {'address': '4801 N BROADWAY', 'date': '07/01/2012'}
07/02/2012
  {'address': '5800 E 58TH', 'date': '07/02/2012'}
  {'address': '5645 N RAVENSWOOD', 'date': '07/02/2012'}
  {'address': '1060 W ADDISON', 'date': '07/02/2012'}
07/03/2012
  {'address': '2122 N CLARK', 'date': '07/03/2012'}
07/04/2012
  {'address': '5148 N CLARK', 'date': '07/04/2012'}
  {'address': '1039 W GRANVILLE', 'date': '07/04/2012'}
<class 'itertools.groupby'>
```

groupby() 函数扫描整个序列并且查找连续相同值（或者根据指定 key 函数返回值相同）的元素序列。 在每次迭代的时候，它会返回一个值和一个迭代器对象， 这个迭代器对象可以生成元素值全部等于上面那个值的组中所有对象。

一个非常重要的准备步骤是要根据指定的字段将数据排序。 因为 groupby() 仅仅检查连续的元素，如果事先并没有排序完成的话，分组函数将得不到想要的结果。

如果你仅仅只是想根据 date 字段将数据分组到一个大的数据结构中去，并且允许随机访问， 那么你最好使用 defaultdict() 来构建一个多值字典，关于多值字典已经在 1.6 小节有过详细的介绍。比如：

```python
from collections import defaultdict
rows_by_date = defaultdict(list)
for row in rows:
    rows_by_date[row['date']].append(row)
输出：
>>> for r in rows_by_date['07/01/2012']:
... print(r)
...
{'date': '07/01/2012', 'address': '5412 N CLARK'}
{'date': '07/01/2012', 'address': '4801 N BROADWAY'}
>>>
```

在上面这个例子中，我们没有必要先将记录排序。因此，如果对内存占用不是很关心， 这种方式会比先排序然后再通过 groupby() 函数迭代的方式运行得快一些。

## 1.16. 过滤序列元素

最简单的过滤序列元素的方法就是使用列表推导。比如：

```python
>>> mylist = [1, 4, -5, 10, -7, 2, 3, -1]
>>> [n for n in mylist if n > 0]
[1, 4, 10, 2, 3]
>>> [n for n in mylist if n < 0]
[-5, -7, -1]
```

使用列表推导的一个潜在缺陷就是如果输入非常大的时候会产生一个非常大的结果集，占用大量内存。 如果你对内存比较敏感，那么你可以使用生成器表达式迭代产生过滤的元素。比如：

```python
>>> pos = (n for n in mylist if n > 0)
>>> pos
<generator object <genexpr> at 0x1006a0eb0>
>>> for x in pos:
... print(x)
...
1
4
10
2
3
```

有时候，过滤规则比较复杂，不能简单的在列表推导或者生成器表达式中表达出来。 比如，假设过滤的时候需要处理一些异常或者其他复杂情况。这时候你可以将过滤代码放到一个函数中， 然后使用内建的 filter() 函数。示例如下：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-


def is_int(val):
    try:
        x = int(val)
        return True
    except ValueError:
        return False


if __name__ == '__main__':
    values = ['1', '2', '-3', '-', '4', 'N/A', '5']

    ivals = list(filter(is_int, values))
    print(ivals)
    # Outputs ['1', '2', '-3', '4', '5']
```

列表推导和生成器表达式通常情况下是过滤数据最简单的方式。 其实它们还能在过滤的时候转换数据。比如：

```python
>>> mylist = [1, 4, -5, 10, -7, 2, 3, -1]
>>> import math
>>> [math.sqrt(n) for n in mylist if n > 0]
[1.0, 2.0, 3.1622776601683795, 1.4142135623730951, 1.7320508075688772]
```

过滤操作的一个变种就是将不符合条件的值用新的值代替，而不是丢弃它们。 比如，在一列数据中你可能不仅想找到正数，而且还想将不是正数的数替换成指定的数。 通过将过滤条件放到条件表达式中去，可以很容易的解决这个问题，就像这样：

```python
>>> clip_neg = [n if n > 0 else 0 for n in mylist]
>>> clip_neg
[1, 4, 0, 10, 0, 2, 3, 0]
>>> clip_pos = [n if n < 0 else 0 for n in mylist]
>>> clip_pos
[0, 0, -5, 0, -7, 0, 0, -1]
```

另外一个值得关注的过滤工具就是 itertools.compress() ， 它以一个 iterable 对象和一个相对应的 Boolean 选择器序列作为输入参数。 然后输出 iterable 对象中对应选择器为 True 的元素。 当你需要用另外一个相关联的序列来过滤某个序列的时候，这个函数是非常有用的。 比如，假如现在你有下面两列数据，现在你想将那些对应 count 值大于5的地址全部输出，那么你可以这样做：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from itertools import compress

if __name__ == '__main__':
    addresses = [
        '5412 N CLARK',
        '5148 N CLARK',
        '5800 E 58TH',
        '2122 N CLARK',
        '5645 N RAVENSWOOD',
        '1060 W ADDISON',
        '4801 N BROADWAY',
        '1039 W GRANVILLE',
    ]
    counts = [0, 3, 10, 4, 1, 7, 6, 1]
    more5 = [n > 5 for n in counts]
    print(list(compress(addresses, more5)))

    more5 = [1, 0, 1] # 即使Boolean序列与迭代序列长度不一致，也可以输出，长度不足的都按False处理
    print(list(compress(addresses, more5)))
输出：
['5800 E 58TH', '1060 W ADDISON', '4801 N BROADWAY']
['5412 N CLARK', '5800 E 58TH']
```

这里的关键点在于先创建一个 Boolean 序列，指示哪些元素符合条件。 然后 compress() 函数根据这个序列去选择输出对应位置为 True 的元素。

和 filter() 函数类似， compress() 也是返回的一个迭代器。因此，如果你需要得到一个列表， 那么你需要使用 list() 来将结果转换为列表类型。

## 1.17. 从字典中提取子集

最简单的方式是使用字典推导。比如：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

if __name__ == '__main__':
    prices = {
        'ACME': 45.23,
        'AAPL': 612.78,
        'IBM': 205.55,
        'HPQ': 37.20,
        'FB': 10.75
    }
    # Make a dictionary of all prices over 200
    p1 = {key: value for key, value in prices.items() if value > 200}
    # Make a dictionary of tech stocks
    tech_names = {'AAPL', 'IBM', 'HPQ', 'MSFT'}  # 这种构造方式是set
    p2 = {key: value for key, value in prices.items() if key in tech_names}
    print(p1, p2)

    p1 = dict((key, value) for key, value in prices.items() if value > 200)
    print(p1)

    d = dict(((1, 2), (3, 4)))
    print(d)
输出：
{'AAPL': 612.78, 'IBM': 205.55} {'AAPL': 612.78, 'IBM': 205.55, 'HPQ': 37.2}
{'AAPL': 612.78, 'IBM': 205.55}
{1: 2, 3: 4}
```

大多数情况下字典推导能做到的，通过创建一个元组序列然后把它传给 dict() 函数也能实现，如上。

但是，字典推导方式表意更清晰，并且实际上也会运行的更快些 （在这个例子中，实际测试几乎比 dict() 函数方式快整整一倍）。

```python
# Make a dictionary of tech stocks
tech_names = {'AAPL', 'IBM', 'HPQ', 'MSFT'}
p2 = {key: prices[key] for key in prices.keys() & tech_names}  # 使用set的并集操作
print(p2)
```

但是，运行时间测试结果显示这种方案大概比第一种方案慢 1.6 倍。 如果对程序运行性能要求比较高的话，需要花点时间去做计时测试。 关于更多计时和性能测试，可以参考 14.13 小节。

## 1.18. 映射名称到序列元素

collections.namedtuple() 函数通过使用一个普通的元组对象来帮你解决这个问题。 这个函数实际上是一个返回 Python 中标准元组类型子类的一个工厂方法。 你需要传递一个类型名和你需要的字段给它，然后它就会返回一个类，你可以初始化这个类，为你定义的字段传递值等。 代码示例：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from collections import namedtuple

if __name__ == '__main__':
    Subscriber = namedtuple('Subscriber', ['addr', 'joined'])  # 这里的返回值可以随便定义
    sub = Subscriber('jonesy@example.com', '2012-10-19')
    print(sub)
    print(sub.addr)
    print(sub.joined)

    sub.addr = 'ben@163.com'  # 不能设置属性，异常AttributeError: can't set attribute
    print(sub)
输出：
Subscriber(addr='jonesy@example.com', joined='2012-10-19')
jonesy@example.com
2012-10-19
```

尽管 namedtuple 的实例看起来像一个普通的类实例，但是它跟元组类型是可交换的，支持所有的普通元组操作，比如索引和解压。 比如：

```python
print(len(sub))
print(sub[0], sub[1])
addr, joined = sub
print(addr, joined)
输出：
2
jonesy@example.com 2012-10-19
jonesy@example.com 2012-10-19
```

命名元组的一个主要用途是将你的代码从下标操作中解脱出来。 因此，如果你从数据库调用中返回了一个很大的元组列表，通过下标去操作其中的元素， 当你在表中添加了新的列的时候你的代码可能就会出错了。但是如果你使用了命名元组，那么就不会有这样的顾虑。

为了说明清楚，下面是使用普通元组的代码：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from collections import namedtuple

# 使用普通元组
def compute_cost(records):
    total = 0.0
    for rec in records:
        total += rec[1] * rec[2]
    return total

# 使用命名元组的版本
def compute_cost_with_name_tuple(records):
    Fruit = namedtuple('Fruit', ['name', 'weight', 'price'])

    total = 0.0
    for rec in records:
        s = Fruit(*rec)
        total += s.weight * s.price
    return total


if __name__ == '__main__':
    records = (('apple', 3.5, 100), ('orange', 4.1, 200), ('strawberry', 2, 50))
    print(compute_cost(records))
    print(compute_cost_with_name_tuple(records))
```

下标操作通常会让代码表意不清晰，并且非常依赖记录的结构。

命名元组另一个用途就是作为字典的替代，因为字典存储需要更多的内存空间。 如果你需要构建一个非常大的包含字典的数据结构，那么使用命名元组会更加高效。 但是需要注意的是，不像字典那样，一个命名元组是不可更改的。比如：

```python
>>> s = Stock('ACME', 100, 123.45)
>>> s
Stock(name='ACME', shares=100, price=123.45)
>>> s.shares = 75
Traceback (most recent call last):
File "<stdin>", line 1, in <module>
AttributeError: can't set attribute
>>>
```

如果你真的需要改变属性的值，那么可以使用命名元组实例的 _replace() 方法， 它会创建一个全新的命名元组并将对应的字段用新的值取代。比如：

```python
Fruit = namedtuple('Fruit', ['name', 'weight', 'price'])
apple = Fruit('apple', 4, 20)
# apple.price = 10
new_apple = apple._replace(price=10)
print(apple, new_apple)
输出：
Fruit(name='apple', weight=4, price=20) Fruit(name='apple', weight=4, price=10)
```

_replace() 方法还有一个很有用的特性就是当你的命名元组拥有可选或者缺失字段时候， 它是一个非常方便的填充数据的方法。 你可以先创建一个包含缺省值的原型元组，然后使用 _replace() 方法创建新的值被更新过的实例。比如：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from collections import namedtuple

Stock = namedtuple('Stock', ['name', 'shares', 'price', 'date', 'time'])

# Create a prototype instance
stock_prototype = Stock('', 0, 0.0, None, None)


# Function to convert a dictionary to a Stock
def dict_to_stock(s):
    return stock_prototype._replace(**s)


if __name__ == '__main__':
    a = {'name': 'ACME', 'shares': 100, 'price': 123.45}
    print(dict_to_stock(a))

    b = {'name': 'ACME', 'shares': 100, 'price': 123.45, 'date': '12/17/2012'}
    print(dict_to_stock(b))
输出：
Stock(name='ACME', shares=100, price=123.45, date=None, time=None)
Stock(name='ACME', shares=100, price=123.45, date='12/17/2012', time=None)
```

最后要说的是，如果你的目标是定义一个需要更新很多实例属性的高效数据结构，那么命名元组并不是你的最佳选择。 这时候你应该考虑定义一个包含 \_\_slots\_\_ 方法的类（参考8.4小节）。

## 1.19. 转换并同时计算数据

你需要在数据序列上执行聚集函数（比如 sum() , min() , max() ）， 但是首先你需要先转换或者过滤数据

解决方案
一个非常优雅的方式去结合数据计算与转换就是使用一个生成器表达式参数。 比如，如果你想计算平方和，可以像下面这样做：

```python
nums = [1, 2, 3, 4, 5]
s = sum(x * x for x in nums)
```

下面是更多的例子：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import os

if __name__ == '__main__':
    # Determine if any .py files exist in a directory
    files = os.listdir(r'../test')
    if any(name.endswith('.py') for name in files):
        print('There be python!')
    else:
        print('Sorry, no python.')


    # Output a tuple as CSV
    s = ('ACME', 50, 123.45)
    print(','.join(str(x) for x in s))


    # Data reduction across fields of a data structure
    portfolio = [
        {'name': 'GOOG', 'shares': 50},
        {'name': 'YHOO', 'shares': 75},
        {'name': 'AOL', 'shares': 20},
        {'name': 'SCOX', 'shares': 65}
    ]
    min_shares = min(s['shares'] for s in portfolio)
    print(min_shares)
输出：
There be python!
ACME,50,123.45
20
```

上面的示例向你演示了当生成器表达式作为一个单独参数传递给函数时候的巧妙语法（你并不需要多加一个括号）。 比如，下面这些语句是等效的：

```python
s = sum((x * x for x in nums)) # 显式的传递一个生成器表达式对象
s = sum(x * x for x in nums) # 更加优雅的实现方式，省略了括号
```

使用一个生成器表达式作为参数会比先创建一个临时列表更加高效和优雅。 比如，如果你不使用生成器表达式的话，你可能会考虑使用下面的实现方式：

```python
nums = [1, 2, 3, 4, 5]
s = sum([x * x for x in nums])
```

这种方式同样可以达到想要的效果，但是它会多一个步骤，先创建一个额外的列表。 对于小型列表可能没什么关系，但是如果元素数量非常大的时候， 它会创建一个巨大的仅仅被使用一次就被丢弃的临时数据结构。而生成器方案会以迭代的方式转换数据，因此更省内存。

在使用一些聚集函数比如 min() 和 max() 的时候你可能更加倾向于使用生成器版本， 它们接受的一个 key 关键字参数或许对你很有帮助。 比如，在上面的证券例子中，你可能会考虑下面的实现版本：

```python
# Original: Returns 20
min_shares = min(s['shares'] for s in portfolio)
# Alternative: Returns {'name': 'AOL', 'shares': 20}
min_shares = min(portfolio, key=lambda s: s['shares'])  # min(portfolio, key=itemgetter('shares'))
```

## 1.20. 合并多个字典或映射

现在有多个字典或者映射，你想将它们从逻辑上合并为一个单一的映射后执行某些操作， 比如查找值或者检查某些键是否存在。

假如你有如下两个字典:

```python
a = {'x': 1, 'z': 3 }
b = {'y': 2, 'z': 4 }
```

现在假设你必须在两个字典中执行查找操作（比如先从 a 中找，如果找不到再在 b 中找）。 一个非常简单的解决方案就是使用 collections 模块中的 ChainMap 类。比如：

```python
from collections import ChainMap
c = ChainMap(a,b)
print(c['x']) # Outputs 1 (from a)
print(c['y']) # Outputs 2 (from b)
print(c['z']) # Outputs 3 (from a)
print('c : {}'.format(c))

new_c = c.new_child(dict(a=3, b=4))  # 新的ChainMap对象，在头增加了一个新的Map
print('new_c : {}'.format(new_c))

back_c = new_c.parents  # 新的ChainMap对象，抛弃了原来的头Map
print('back_c : {}'.format(back_c))
输出：
1
2
3
c : ChainMap({'x': 1, 'z': 3}, {'y': 2, 'z': 4})
new_c : ChainMap({'a': 3, 'b': 4}, {'x': 1, 'z': 3}, {'y': 2, 'z': 4})
back_c : ChainMap({'x': 1, 'z': 3}, {'y': 2, 'z': 4})
```

一个 ChainMap 接受多个字典并将它们在逻辑上变为一个字典。 然后，这些字典并不是真的合并在一起了， ChainMap 类只是在内部创建了一个容纳这些字典的列表 并重新定义了一些常见的字典操作来遍历这个列表。大部分字典操作都是可以正常使用的，比如：

```python
print(len(c))
print(list(c.keys()))
print(list(c.values()))
输出：
3
['y', 'z', 'x']
[2, 3, 1]
```

如果出现重复键，那么第一次出现的映射值会被返回。 因此，例子程序中的 c['z'] 总是会返回字典 a 中对应的值，而不是 b 中对应的值。

对于字典的更新或删除操作总是影响的是列表中第一个字典。比如：

```python
c['z'] = 10
c['w'] = 40
del c['x']
print(a)  # {'z': 10, 'w': 40}
del c['y']  # KeyError: "Key not found in the first mapping: 'y'"
```

ChainMap 对于编程语言中的作用范围变量（比如 globals , locals 等）是非常有用的。 事实上，有一些方法可以使它变得简单：

```python
values = ChainMap()
values['x'] = 1
# Add a new mapping
values = values.new_child()
values['x'] = 2
# Add a new mapping
values = values.new_child()
values['x'] = 3
print(values)

print(values['x'])
# Discard last mapping
values = values.parents
print(values['x'])
# Discard last mapping
values = values.parents
print(values['x'])
print(values)
输出：
ChainMap({'x': 3}, {'x': 2}, {'x': 1})
3
2
1
ChainMap({'x': 1})
```

作为 ChainMap 的替代，你可能会考虑使用 update() 方法将两个字典合并。比如：

```python
>>> a = {'x': 1, 'z': 3 }
>>> b = {'y': 2, 'z': 4 }
>>> merged = dict(b)
>>> merged.update(a)
>>> merged['x']
1
>>> merged['y']
2
>>> merged['z']
3
```

这样也能行得通，但是它需要你创建一个完全不同的字典对象（或者是破坏现有字典结构）。 同时，如果原字典做了更新，这种改变不会反应到新的合并字典中去。比如：

```python
>>> a['x'] = 13
>>> merged['x']
1
```

ChainMap 使用原来的字典，它自己不创建新的字典。所以它并不会产生上面所说的结果，比如：

```python
>>> a = {'x': 1, 'z': 3 }
>>> b = {'y': 2, 'z': 4 }
>>> merged = ChainMap(a, b)
>>> merged['x']
1
>>> a['x'] = 42
>>> merged['x'] # Notice change to merged dicts
42
```

# 2. 字符串与文本

## 2.1. 使用多个界定符分割字符串

string 对象的 split() 方法只适应于非常简单的字符串分割情形， 它并不允许有多个分隔符或者是分隔符周围不确定的空格。 当你需要更加灵活的切割字符串的时候，最好使用 re.split() 方法：

```python
>>> line = 'asdf fjdk; afed, fjek,asdf, foo'
>>> import re
>>> re.split(r'[;,\s]\s*', line)
['asdf', 'fjdk', 'afed', 'fjek', 'asdf', 'foo']
```

函数 re.split() 是非常实用的，因为它允许你为分隔符指定多个正则模式。 比如，在上面的例子中，分隔符可以是逗号，分号或者是空格，并且后面紧跟着任意个的空格。 只要这个模式被找到，那么匹配的分隔符两边的实体都会被当成是结果中的元素返回。 返回结果为一个字段列表，这个跟 str.split() 返回值类型是一样的。

当你使用 re.split() 函数时候，需要特别注意的是正则表达式中是否包含一个括号捕获分组。 如果使用了捕获分组，那么被匹配的文本也将出现在结果列表中。比如，观察一下这段代码运行后的结果：

```python
>>> fields = re.split(r'(;|,|\s)\s*', line)
>>> fields
['asdf', ' ', 'fjdk', ';', 'afed', ',', 'fjek', ',', 'asdf', ',', 'foo']
```

获取分割字符在某些情况下也是有用的。 比如，你可能想保留分割字符串，用来在后面重新构造一个新的输出字符串：

```python
>>> values = fields[::2]
>>> delimiters = fields[1::2] + ['']
>>> values
['asdf', 'fjdk', 'afed', 'fjek', 'asdf', 'foo']
>>> delimiters
[' ', ';', ',', ',', ',', '']
>>> # Reform the line using the same delimiters
>>> ''.join(v+d for v,d in zip(values, delimiters))
'asdf fjdk;afed,fjek,asdf,foo'
```

如果你不想保留分割字符串到结果列表中去，但仍然需要使用到括号来分组正则表达式的话， 确保你的分组是非捕获分组，形如 (?:...) 。比如：

使用[]只能匹配里面的一个字符，使用(?:)的形式可以在|中匹配多个字符：

```python
>>> re.split(r'(?:,|;|\s)\s*', line)
['asdf', 'fjdk', 'afed', 'fjek', 'asdf', 'foo']
```

## 2.2. 字符串开头或结尾匹配

你需要通过指定的文本模式去检查字符串的开头或者结尾，比如文件名后缀，URL Scheme等等。

检查字符串开头或结尾的一个简单方法是使用 str.startswith() 或者是 str.endswith() 方法。比如：

```python
>>> filename = 'spam.txt'
>>> filename.endswith('.txt')
True
>>> filename.startswith('file:')
False
>>> url = 'http://www.python.org'
>>> url.startswith('http:')
True
```

如果你想检查多种匹配可能，只需要将所有的匹配项放入到一个元组中去， 然后传给 startswith() 或者 endswith() 方法：

```python
>>> import os
>>> filenames = os.listdir('.')
>>> filenames
[ 'Makefile', 'foo.c', 'bar.py', 'spam.c', 'spam.h' ]
>>> [name for name in filenames if name.endswith(('.c', '.h')) ]
['foo.c', 'spam.c', 'spam.h'
>>> any(name.endswith('.py') for name in filenames)
True
```

下面是另一个例子：

```python
from urllib.request import urlopen

def read_data(name):
    if name.startswith(('http:', 'https:', 'ftp:')):
        return urlopen(name).read()
    else:
        with open(name) as f:
            return f.read()
```

奇怪的是，这个方法中必须要输入一个元组作为参数。 如果你恰巧有一个 list 或者 set 类型的选择项， 要确保传递参数前先调用 tuple() 将其转换为元组类型。比如：

```python
>>> choices = ['http:', 'ftp:']
>>> url = 'http://www.python.org'
>>> url.startswith(choices)
Traceback (most recent call last):
File "<stdin>", line 1, in <module>
TypeError: startswith first arg must be str or a tuple of str, not list
>>> url.startswith(tuple(choices))
True
```

startswith() 和 endswith() 方法提供了一个非常方便的方式去做字符串开头和结尾的检查。 类似的操作也可以使用切片来实现，但是代码看起来没有那么优雅。比如：

```python
>>> filename = 'spam.txt'
>>> filename[-4:] == '.txt'
True
>>> url = 'http://www.python.org'
>>> url[:5] == 'http:' or url[:6] == 'https:' or url[:4] == 'ftp:'
True
```

你可以能还想使用正则表达式去实现，比如：

```python
>>> import re
>>> url = 'http://www.python.org'
>>> re.match('http:|https:|ftp:', url)
<_sre.SRE_Match object at 0x101253098>
```

这种方式也行得通，但是对于简单的匹配实在是有点小材大用了，本节中的方法更加简单并且运行会更快些。

最后提一下，当和其他操作比如普通数据聚合相结合的时候 startswith() 和 endswith() 方法是很不错的。 比如，下面这个语句检查某个文件夹中是否存在指定的文件类型：

```python
if any(name.endswith(('.c', '.h')) for name in listdir(dirname)):
...
```

## 2.3. 用Shell通配符匹配字符串

你想使用 Unix Shell 中常用的通配符(比如 *.py , Dat[0-9]*.csv 等)去匹配文本字符串

fnmatch 模块提供了两个函数—— fnmatch() 和 fnmatchcase() ，可以用来实现这样的匹配。用法如下：

```python
>>> from fnmatch import fnmatch, fnmatchcase
>>> fnmatch('foo.txt', '*.txt')
True
>>> fnmatch('foo.txt', '?oo.txt')
True
>>> fnmatch('Dat45.csv', 'Dat[0-9]*')
True
>>> names = ['Dat1.csv', 'Dat2.csv', 'config.ini', 'foo.py']
>>> [name for name in names if fnmatch(name, 'Dat*.csv')]
['Dat1.csv', 'Dat2.csv']
```

fnmatch() 函数使用底层操作系统的大小写敏感规则(不同的系统是不一样的)来匹配模式。比如：

```python
>>> # On OS X (Mac)
>>> fnmatch('foo.txt', '*.TXT')
False
>>> # On Windows
>>> fnmatch('foo.txt', '*.TXT')
True
```

如果你对这个区别很在意，可以使用 fnmatchcase() 来代替。它完全使用你的模式大小写匹配。比如：

```python
>>> fnmatchcase('foo.txt', '*.TXT')
False
```

这两个函数通常会被忽略的一个特性是在处理非文件名的字符串时候它们也是很有用的。 比如，假设你有一个街道地址的列表数据：

```python
addresses = [
    '5412 N CLARK ST',
    '1060 W ADDISON ST',
    '1039 W GRANVILLE AVE',
    '2122 N CLARK ST',
    '4802 N BROADWAY',
]
```

你可以像这样写列表推导：

```python
>>> from fnmatch import fnmatchcase
>>> [addr for addr in addresses if fnmatchcase(addr, '* ST')]
['5412 N CLARK ST', '1060 W ADDISON ST', '2122 N CLARK ST']
>>> [addr for addr in addresses if fnmatchcase(addr, '54[0-9][0-9] *CLARK*')]
['5412 N CLARK ST']
```

fnmatch() 函数匹配能力介于简单的字符串方法和强大的正则表达式之间。 如果在数据处理操作中只需要简单的通配符就能完成的时候，这通常是一个比较合理的方案。

如果你的代码需要做文件名的匹配，最好使用 glob 模块。参考5.13小节。

## 2.4. 字符串匹配和搜索

如果你想匹配的是字面字符串，那么你通常只需要调用基本字符串方法就行， 比如 str.find() , str.endswith() , str.startswith() 或者类似的方法：

```python
>>> text = 'yeah, but no, but yeah, but no, but yeah'
>>> # Exact match
>>> text == 'yeah'
False
>>> # Match at start or end
>>> text.startswith('yeah')
True
>>> text.endswith('no')
False
>>> # Search for the location of the first occurrence
>>> text.find('no')
10
```

对于复杂的匹配需要使用正则表达式和 re 模块。 为了解释正则表达式的基本原理，假设你想匹配数字格式的日期字符串比如 11/27/2012 ，你可以这样做：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import re

if __name__ == '__main__':
    text1 = '11/27/2012'
    text2 = 'Nov 27, 2012'
    # Simple matching: \d+ means match one or more digits
    if re.match(r'\d+/\d+/\d+', text1):
        print('yes')
    else:
        print('no')

    if re.match(r'\d+/\d+/\d+', text2):
        print('yes')
    else:
        print('no')
输出：
yes
no
```

如果你想使用同一个模式去做多次匹配，你应该先将模式字符串预编译为模式对象。比如：

```python
import re

if __name__ == '__main__':
    text1 = '11/27/2012'
    text2 = 'Nov 27, 2012'
    datepat = re.compile(r'\d+/\d+/\d+')
    if datepat.match(text1):
        print('yes')
    else:
        print('no')

    if datepat.match(text2):
        print('yes')
    else:
        print('no')
```

match() 总是从字符串开始去匹配，如果你想查找字符串任意部分的模式出现位置， 使用 findall() 方法去代替（**我觉得应该是search()方法来代替**）。比如：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import re

if __name__ == '__main__':
    text1 = '11/27/2012'
    text2 = 'Nov 27, 2012'
    text = 'Today is 11/27/2012. PyCon starts 3/13/2013.'
    datepat = re.compile(r'\d+/\d+/\d+')
    m = datepat.search(text)
    if m:
        print(m.group())

    l = datepat.findall(text)
    print(l)
输出：
11/27/2012
['11/27/2012', '3/13/2013']
```

在定义正则式的时候，通常会利用括号去捕获分组。比如：

`>>> datepat = re.compile(r'(\d+)/(\d+)/(\d+)')`

捕获分组可以使得后面的处理更加简单，因为可以分别将每个组的内容提取出来。比如：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import re

if __name__ == '__main__':
    datepat = re.compile(r'(\d+)/(\d+)/(\d+)')
    m = datepat.match('11/27/2012')
    print(m.group())  # 当前正则表达式匹配的全部内容
    print(m.group(0))  # 同group()
    print(m.group(1))
    print(m.group(2))
    print(m.group(3))
    print(m.groups())
    month, day, year = m.groups()
    # Find all matches (notice splitting into tuples)
    text = 'Today is 11/27/2012. PyCon starts 3/13/2013.'
    print(datepat.findall(text))
    for month, day, year in datepat.findall(text):
        print('{}-{}-{}'.format(year, month, day))
输出：
11/27/2012
11/27/2012
11
27
2012
('11', '27', '2012')
[('11', '27', '2012'), ('3', '13', '2013')]
2012-11-27
2013-3-13
```

```python
import re

if __name__ == '__main__':
    datepat = re.compile(r'((\d+)/(\d+)/(\d+))')
    m = datepat.match('11/27/2012')
    print(m.group())  # 当前正则表达式匹配的全部内容
    print(m.group(0))  # 同group()
    print(m.group(1))  # 第1个括号匹配内容
    print(m.group(2))  # 第2个括号匹配内容
    print(m.group(3))
    print(m.groups())
    _, month, day, year = m.groups()
    # Find all matches (notice splitting into tuples)
    text = 'Today is 11/27/2012. PyCon starts 3/13/2013.'
    print(datepat.findall(text))
    for _, month, day, year in datepat.findall(text):
        print('{}-{}-{}'.format(year, month, day))
输出：
11/27/2012
11/27/2012
11/27/2012
11
27
('11/27/2012', '11', '27', '2012')
[('11/27/2012', '11', '27', '2012'), ('3/13/2013', '3', '13', '2013')]
2012-11-27
2013-3-13
```

findall() 方法会搜索文本并以**字符串列表形式**返回**所有小括号**的匹配。 如果你想以迭代方式返回匹配，可以使用 finditer() 方法来代替，**每个迭代对象是match对象**，比如：

```python
import re

if __name__ == '__main__':
    datepat = re.compile(r'((\d+)/(\d+)/(\d+))')
    text = 'Today is 11/27/2012. PyCon starts 3/13/2013.'
    for m in datepat.finditer(text):
        # _, month, day, year = m.group(1, 2, 3, 4)
        month, day, year = m.group(2, 3, 4)  # 这里要写group(2, 3, 4)，不能写group(2, 4)
        print('{}-{}-{}'.format(year, month, day))
        print(m.groups())
输出：
2012-11-27
('11/27/2012', '11', '27', '2012')
2013-3-13
('3/13/2013', '3', '13', '2013')
```

关于正则表达式理论的教程已经超出了本书的范围。 不过，这一节阐述了使用re模块进行匹配和搜索文本的最基本方法。 核心步骤就是先使用 re.compile() 编译正则表达式字符串， 然后使用 match() , findall() 或者 finditer() 等方法。

当写正则式字符串的时候，相对普遍的做法是使用原始字符串比如 r'(\d+)/(\d+)/(\d+)' 。 这种字符串将不去解析反斜杠，这在正则表达式中是很有用的。 如果不这样做的话，你必须使用两个反斜杠，类似 '(\\d+)/(\\d+)/(\\d+)' 。

需要注意的是 match() 方法仅仅检查字符串的开始部分。它的匹配结果有可能并不是你期望的那样。比如：

```python
import re

if __name__ == '__main__':
    datepat = re.compile(r'(\d+)/(\d+)/(\d+)')
    m = datepat.match('11/27/2012abcdef')
    print(m.group())

    # 如果你想精确匹配，确保你的正则表达式以$结尾，就像这么这样：
    datepat = re.compile(r'(\d+)/(\d+)/(\d+)$')
    m = datepat.match('11/27/2012abcdef')
    # print(m.group())  # m is None

    m = datepat.match('11/27/2012')
    print(m.group())
输出：
11/27/2012
11/27/2012
```

最后，如果你仅仅是做一次简单的文本匹配/搜索操作的话，可以略过编译部分，直接使用 re 模块级别的函数。比如：

```python
>>> re.findall(r'(\d+)/(\d+)/(\d+)', text)
[('11', '27', '2012'), ('3', '13', '2013')]
```

但是需要注意的是，如果你打算做大量的匹配和搜索操作的话，最好先编译正则表达式，然后再重复使用它。 模块级别的函数会将最近编译过的模式缓存起来，因此并不会消耗太多的性能， 但是如果使用预编译模式的话，你将会减少查找和一些额外的处理损耗。

## 2.5. 字符串搜索和替换

你想在字符串中搜索和匹配指定的文本模式

对于简单的字面模式，直接使用 str.replace() 方法即可，比如：

```python
>>> text = 'yeah, but no, but yeah, but no, but yeah'
>>> text.replace('yeah', 'yep')
'yep, but no, but yep, but no, but yep'
```

对于复杂的模式，请使用 re 模块中的 sub() 函数。 为了说明这个，假设你想将形式为 11/27/2012 的日期字符串改成 2012-11-27 。示例如下：

```python
text = 'Today is 11/27/2012. PyCon starts 3/13/2013.'
print(re.sub(r'(\d+)/(\d+)/(\d+)', r'\3-\1-\2', text))
输出：
Today is 2012-11-27. PyCon starts 2013-3-13.
```

sub() 函数中的第一个参数是被匹配的模式，第二个参数是替换模式。反斜杠数字比如 \3 指向前面模式的捕获组号。

如果你打算用相同的模式做多次替换，考虑先编译它来提升性能。比如：

```python
import re

if __name__ == '__main__':
    text = 'Today is 11/27/2012. PyCon starts 3/13/2013.'
    datepat = re.compile(r'(\d+)/(\d+)/(\d+)')
    print(datepat.sub(r'\3-\1-\2', text))
```

对于更加复杂的替换，可以传递一个替换回调函数来代替，比如：

```python
import re
from calendar import month_abbr


def change_date(m):
    mon_name = month_abbr[int(m.group(1))]
    return '{} {} {}'.format(m.group(2), mon_name, m.group(3))


if __name__ == '__main__':
    text = 'Today is 11/27/2012. PyCon starts 3/13/2013.'
    datepat = re.compile(r'(\d+)/(\d+)/(\d+)')
    print(datepat.sub(change_date, text))

    new_text, n = datepat.subn(change_date, text)
    print(new_text, n)
输出：
Today is 27 Nov 2012. PyCon starts 13 Mar 2013.
Today is 27 Nov 2012. PyCon starts 13 Mar 2013. 2
```

一个替换回调函数的参数是一个 match 对象，也就是 match() 或者 find() 返回的对象。 使用 group() 方法来提取特定的匹配部分。回调函数最后返回替换字符串。

如果除了替换后的结果外，你还想知道有多少替换发生了，可以使用 re.subn() 来代替。比如：

```python
>>> newtext, n = datepat.subn(r'\3-\1-\2', text)
>>> newtext
'Today is 2012-11-27. PyCon starts 2013-3-13.'
>>> n
2
```

## 2.6. 字符串忽略大小写的搜索替换

为了在文本操作时忽略大小写，你需要在使用 re 模块的时候给这些操作提供 re.IGNORECASE 标志参数。比如：

```python
import re

if __name__ == '__main__':
    text = 'UPPER PYTHON, lower python, Mixed Python'
    print(re.findall('python', text, flags=re.IGNORECASE))
    print(re.sub('python', 'snake', text, flags=re.IGNORECASE))
输出：
['PYTHON', 'python', 'Python']
UPPER snake, lower snake, Mixed snake
```

最后的那个例子揭示了一个小缺陷，替换字符串并不会自动跟被匹配字符串的大小写保持一致。 为了修复这个，你可能需要一个辅助函数，就像下面的这样：

```python
import re


def matchcase(word):
    def replace(m):
        text = m.group()
        if text.isupper():
            return word.upper()
        elif text.islower():
            return word.lower()
        elif text[0].isupper():
            return word.capitalize()
        else:
            return word

    return replace


if __name__ == '__main__':
    text = 'UPPER PYTHON, lower python, Mixed Python'
    print(re.sub('python', matchcase('snake'), text, flags=re.IGNORECASE))

    # 编译模式：
    datepat = re.compile('python', flags=re.IGNORECASE)
    print(datepat.sub(matchcase('snake'), text))
输出：
UPPER SNAKE, lower snake, Mixed Snake
UPPER SNAKE, lower snake, Mixed Snake
```

matchcase('snake') 返回了一个回调函数(参数必须是 match 对象)，前面一节提到过， sub() 函数除了接受替换字符串外，还能接受一个回调函数。

对于一般的忽略大小写的匹配操作，简单的传递一个 re.IGNORECASE 标志参数就已经足够了。 但是需要注意的是，这个对于某些需要大小写转换的Unicode匹配可能还不够， 参考2.10小节了解更多细节。

## 2.7. 最短匹配模式

这个问题一般出现在需要匹配一对分隔符之间的文本的时候(比如引号包含的字符串)。 为了说明清楚，考虑如下的例子：

```python
import re

if __name__ == '__main__':
    str_pat = re.compile(r'"(.*)"')
    text1 = 'Computer says "no."'
    print(str_pat.findall(text1))
    text2 = 'Computer says "no." Phone says "yes."'
    print(str_pat.findall(text2))
输出：
['no.']
['no." Phone says "yes.']
```

在这个例子中，模式 r'\"(.*)\"' 的意图是匹配被双引号包含的文本。 但是在正则表达式中*操作符是贪婪的，因此匹配操作会查找最长的可能匹配。 于是在第二个例子中搜索 text2 的时候返回结果并不是我们想要的。

为了修正这个问题，可以在模式中的*操作符后面加上?修饰符，就像这样：

```python
str_pat = re.compile(r'"(.*?)"')
text2 = 'Computer says "no." Phone says "yes."'
print(str_pat.findall(text2))
输出：
['no.', 'yes.']
```

这样就使得匹配变成非贪婪模式，从而得到最短的匹配，也就是我们想要的结果。

这一节展示了在写包含点(.)字符的正则表达式的时候遇到的一些常见问题。 在一个模式字符串中，点(.)匹配除了换行外的任何字符。 然而，如果你将点(.)号放在开始与结束符(比如引号)之间的时候，那么匹配操作会查找符合模式的最长可能匹配。 这样通常会导致很多中间的被开始与结束符包含的文本被忽略掉，并最终被包含在匹配结果字符串中返回。 通过在 * 或者 + 这样的操作符后面添加一个 ? 可以强制匹配算法改成寻找最短的可能匹配。

## 2.8. 多行匹配模式

这个问题很典型的出现在当你用点(.)去匹配任意字符的时候，忘记了点(.)不能匹配换行符的事实。 比如，假设你想试着去匹配C语言分割的注释：

```python
import re

if __name__ == '__main__':
    comment = re.compile(r'/\*(.*?)\*/')
    text1 = '/* this is a comment */'
    text2 = '''/* this is a
    multiline comment */
    '''
    print(comment.findall(text1))
    print(comment.findall(text2))
输出：
[' this is a comment ']
[]
```

为了修正这个问题，你可以修改模式字符串，增加对换行的支持。比如：

```python
comment = re.compile(r'/\*((?:.|\n)*?)\*/')
text1 = '/* this is a comment */'
text2 = '''/* this is a
multiline comment */
'''
print(comment.findall(text1))
print(comment.findall(text2))
输出：
[' this is a comment ']
[' this is a\n    multiline comment ']
```

在这个模式中， (?:.|\n) 指定了一个非捕获组 (也就是它定义了一个仅仅用来做匹配，而不能通过单独捕获或者编号的组)。

```python
comment = re.compile(r'/\*(.*?)\*/', re.DOTALL)
print(comment.findall(text2))
输出：
[' this is a\n    multiline comment ']
```

对于简单的情况使用 re.DOTALL 标记参数工作的很好， 但是如果模式非常复杂或者是为了构造字符串令牌而将多个模式合并起来(2.18节有详细描述)， 这时候使用这个标记参数就可能出现一些问题。 如果让你选择的话，最好还是定义自己的正则表达式模式，这样它可以在不需要额外的标记参数下也能工作的很好。

## 2.9. 将Unicode文本标准化

<https://python3-cookbook.readthedocs.io/zh_CN/latest/c02/p09_normalize_unicode_text_to_regexp.html>

## 2.10. 在正则式中使用Unicode

<https://python3-cookbook.readthedocs.io/zh_CN/latest/c02/p10_work_with_unicode_in_regexp.html>

## 2.11. 删除字符串中不需要的字符

strip() 方法能用于删除开始或结尾的字符。 lstrip() 和 rstrip() 分别从左和从右执行删除操作。 默认情况下，这些方法会去除空白字符，但是你也可以指定其他字符。比如：

```python
>>> # Whitespace stripping
>>> s = ' hello world \n'
>>> s.strip()
'hello world'
>>> s.lstrip()
'hello world \n'
>>> s.rstrip()
' hello world'
>>>
>>> # Character stripping
>>> t = '-----hello====='
>>> t.lstrip('-')
'hello====='
>>> t.strip('-=')
'hello'
```

这些 strip() 方法在读取和清理数据以备后续处理的时候是经常会被用到的。 比如，你可以用它们来去掉空格，引号和完成其他任务。

但是需要注意的是去除操作不会对字符串的中间的文本产生任何影响。比如：

```python
>>> s = ' hello     world \n'
>>> s = s.strip()
>>> s
'hello     world'
```

如果你想处理中间的空格，那么你需要求助其他技术。比如使用 replace() 方法或者是用正则表达式替换。示例如下：

```python
>>> s.replace(' ', '')
'helloworld'
>>> import re
>>> re.sub('\s+', ' ', s)
'hello world'
```

通常情况下你想将字符串 strip 操作和其他迭代操作相结合，比如从文件中读取多行数据。 如果是这样的话，那么生成器表达式就可以大显身手了。比如：

```python
with open(filename) as f:
    lines = (line.strip() for line in f)
    for line in lines:
        print(line)
```

在这里，表达式 lines = (line.strip() for line in f) 执行数据转换操作。 这种方式非常高效，因为它不需要预先读取所有数据放到一个临时的列表中去。 它仅仅只是创建一个生成器，并且每次返回行之前会先执行 strip 操作。

对于更高阶的strip，你可能需要使用 translate() 方法。请参阅下一节了解更多关于字符串清理的内容。

## 2.12. 审查清理文本字符串

文本清理问题会涉及到包括文本解析与数据处理等一系列问题。 在非常简单的情形下，你可能会选择使用字符串函数(比如 str.upper() 和 str.lower() )将文本转为标准格式。 使用 str.replace() 或者 re.sub() 的简单替换操作能删除或者改变指定的字符序列。 你同样还可以使用2.9小节的 unicodedata.normalize() 函数将unicode文本标准化。

然后，有时候你可能还想在清理操作上更进一步。比如，你可能想消除整个区间上的字符或者去除变音符。 为了这样做，你可以使用经常会被忽视的 str.translate() 方法。 为了演示，假设你现在有下面这个凌乱的字符串：

```python
s = 'pýtĥöñ\fis\tawesome\r\n'
print(s)

remap = {
    ord('\t'): ' ',
    ord('\f'): ' ',
    ord('\r'): None  # Deleted
}
a = s.translate(remap)
print(a)
输出：
pýtĥöñis	awesome

pýtĥöñ is awesome

```

正如你看的那样，空白字符 \t 和 \f 已经被重新映射到一个空格。回车字符r直接被删除。

你可以以这个表格为基础进一步构建更大的表格。比如，让我们删除所有的和音符：

```python
>>> import unicodedata
>>> import sys
>>> cmb_chrs = dict.fromkeys(c for c in range(sys.maxunicode)
...                         if unicodedata.combining(chr(c)))
...
>>> b = unicodedata.normalize('NFD', a)
>>> b
'pýtĥöñ is awesome\n'
>>> b.translate(cmb_chrs)
'python is awesome\n'
```

上面例子中，通过使用 dict.fromkeys() 方法构造一个字典，每个Unicode和音符作为键，对应的值全部为 None 。

然后使用 unicodedata.normalize() 将原始输入标准化为分解形式字符。 然后再调用 translate 函数删除所有重音符。 同样的技术也可以被用来删除其他类型的字符(比如控制字符等)。

作为另一个例子，这里构造一个将所有Unicode数字字符映射到对应的ASCII字符上的表格：

```python
import sys
import unicodedata

if __name__ == '__main__':
    digitmap = {c: ord('0') + unicodedata.digit(chr(c))
                for c in range(sys.maxunicode)
                if unicodedata.category(chr(c)) == 'Nd'}
    print(len(digitmap))
    # Arabic digits
    x = '\u0661\u0662\u0663'
    print(x.translate(digitmap))
输出：
610
123
```

另一种清理文本的技术涉及到I/O解码与编码函数。这里的思路是先对文本做一些初步的清理， 然后再结合 encode() 或者 decode() 操作来清除或修改它。比如：

```python
import unicodedata

if __name__ == '__main__':
    a = 'pýtĥöñ is awesome\n'
    b = unicodedata.normalize('NFD', a)
    print(b)
    print(b.encode('ascii', 'ignore').decode('ascii'))
输出：
pýtĥöñ is awesome

python is awesome

```

这里的标准化操作将原来的文本分解为单独的和音符。接下来的ASCII编码/解码只是简单的一下子丢弃掉那些字符。 当然，这种方法仅仅只在最后的目标就是获取到文本对应ACSII表示的时候生效。

文本字符清理一个最主要的问题应该是运行的性能。一般来讲，代码越简单运行越快。 对于简单的替换操作， str.replace() 方法通常是最快的，甚至在你需要多次调用的时候。 比如，为了清理空白字符，你可以这样做：

```python
def clean_spaces(s):
    s = s.replace('\r', '')
    s = s.replace('\t', ' ')
    s = s.replace('\f', ' ')
    return s
```

如果你去测试的话，你就会发现这种方式会比使用 translate() 或者正则表达式要快很多。

另一方面，如果你需要执行任何复杂字符对字符的重新映射或者删除操作的话， tanslate() 方法会非常的快。

从大的方面来讲，对于你的应用程序来说性能是你不得不去自己研究的东西。 不幸的是，我们不可能给你建议一个特定的技术，使它能够适应所有的情况。 因此实际情况中需要你自己去尝试不同的方法并评估它。

尽管这一节集中讨论的是文本，但是类似的技术也可以适用于字节，包括简单的替换，转换和正则表达式。

## 2.13. 字符串对齐

对于基本的字符串对齐操作，可以使用字符串的 ljust() , rjust() 和 center() 方法。比如：

```python
text = 'Hello World'
print(text.ljust(20))
print(text.rjust(20))
print(text.center(20))
输出：
Hello World         
         Hello World
    Hello World  
```

所有这些方法都能接受一个可选的填充字符。比如：

```python
print(text.rjust(20, '='))
print(text.center(20, '*'))
输出：
=========Hello World
****Hello World*****
```

函数 format() 同样可以用来很容易的对齐字符串。 你要做的就是使用 <,> 或者 ^ 字符后面紧跟一个指定的宽度。比如：

```python
text = 'Hello World'
print(format(text, '20'))
print(format(text, '>20'))
print(format(text, '<20'))
print(format(text, '^20'))
输出：
Hello World         
         Hello World
Hello World         
    Hello World     
```

如果你想指定一个非空格的填充字符，将它写到对齐字符的前面即可：

```python
print(format(text, '=>20'))
print(format(text, '*^20'))
输出：
=========Hello World
****Hello World*****
```

当格式化多个值的时候，这些格式代码也可以被用在 format() 方法中。比如：

```python
print('{:=>10s} {:*>10s}'.format('Hello', 'World'))
输出：
=====Hello *****World
```

format() 函数的一个好处是它不仅适用于字符串。它可以用来格式化任何值，使得它非常的通用。 比如，你可以用它来格式化数字：

```python
>>> x = 1.2345
>>> format(x, '>10')
'    1.2345'
>>> format(x, '^10.2f')
'   1.23   '
```

在老的代码中，你经常会看到被用来格式化文本的 % 操作符。比如：

```python
>>> '%-20s' % text
'Hello World         '
>>> '%20s' % text
'         Hello World'
```

但是，在新版本代码中，你应该优先选择 format() 函数或者方法。 format() 要比 % 操作符的功能更为强大。 并且 format() 也比使用 ljust() , rjust() 或 center() 方法更通用， 因为它可以用来格式化任意对象，而不仅仅是字符串。

## 2.14. 合并拼接字符串

如果你想要合并的字符串是在一个序列或者 iterable 中，那么最快的方式就是使用 join() 方法。比如：

```python
>>> parts = ['Is', 'Chicago', 'Not', 'Chicago?']
>>> ' '.join(parts)
'Is Chicago Not Chicago?'
>>> ','.join(parts)
'Is,Chicago,Not,Chicago?'
>>> ''.join(parts)
'IsChicagoNotChicago?'
```

初看起来，这种语法看上去会比较怪，但是 join() 被指定为字符串的一个方法。 这样做的部分原因是你想去连接的对象可能来自各种不同的数据序列(比如列表，元组，字典，文件，集合或生成器等)， 如果在所有这些对象上都定义一个 join() 方法明显是冗余的。 因此你只需要指定你想要的分割字符串并调用他的 join() 方法去将文本片段组合起来。

如果你仅仅只是合并少数几个字符串，使用加号(+)通常已经足够了：

```python
>>> a = 'Is Chicago'
>>> b = 'Not Chicago?'
>>> a + ' ' + b
'Is Chicago Not Chicago?'
```

加号(+)操作符在作为一些复杂字符串格式化的替代方案的时候通常也工作的很好，比如：

```python
>>> print('{} {}'.format(a,b))
Is Chicago Not Chicago?
>>> print(a + ' ' + b)
Is Chicago Not Chicago?
```

如果你想在源码中将两个字面字符串合并起来，你只需要简单的将它们放到一起，不需要用加号(+)。比如：

```python
>>> a = 'Hello' 'World'
>>> a
'HelloWorld'
```

字符串合并可能看上去并不需要用一整节来讨论。 但是不应该小看这个问题，程序员通常在字符串格式化的时候因为选择不当而给应用程序带来严重性能损失。

最重要的需要引起注意的是，当我们使用加号(+)操作符去连接大量的字符串的时候是非常低效率的， 因为加号连接会引起内存复制以及垃圾回收操作。 特别的，你永远都不应像下面这样写字符串连接代码：

```python
s = ''
for p in parts:
    s += p
```

这种写法会比使用 join() 方法运行的要慢一些，因为每一次执行+=操作的时候会创建一个新的字符串对象。 你最好是先收集所有的字符串片段然后再将它们连接起来。

一个相对比较聪明的技巧是利用生成器表达式(参考1.19小节)转换数据为字符串的同时合并字符串，比如：

```python
>>> data = ['ACME', 50, 91.1]
>>> ','.join(str(d) for d in data)
'ACME,50,91.1'
```

同样还得注意不必要的字符串连接操作。有时候程序员在没有必要做连接操作的时候仍然多此一举。比如在打印的时候：

```python
a = 'a'
b = 'b'
c = 'c'

print(a + ':' + b + ':' + c)  # Ugly
print(':'.join([a, b, c]))  # Still ugly
print(a, b, c, sep=':')  # Better
```

当混合使用I/O操作和字符串连接操作的时候，有时候需要仔细研究你的程序。 比如，考虑下面的两端代码片段：

```python
# Version 1 (string concatenation)
f.write(chunk1 + chunk2)

# Version 2 (separate I/O operations)
f.write(chunk1)
f.write(chunk2)
```

如果两个字符串很小，那么第一个版本性能会更好些，因为I/O系统调用天生就慢。 另外一方面，如果两个字符串很大，那么第二个版本可能会更加高效， 因为它避免了创建一个很大的临时结果并且要复制大量的内存块数据。 还是那句话，有时候是需要根据你的应用程序特点来决定应该使用哪种方案。

最后谈一下，如果你准备编写构建大量小字符串的输出代码， 你最好考虑下使用生成器函数，利用yield语句产生输出片段。比如：

```python
def sample():
    yield 'Is'
    yield 'Chicago'
    yield 'Not'
    yield 'Chicago?'
```

这种方法一个有趣的方面是它并没有对输出片段到底要怎样组织做出假设。 例如，你可以简单的使用 join() 方法将这些片段合并起来：

```python
text = ''.join(sample())
```

或者你也可以将字符串片段重定向到I/O：

```p
for part in sample():
    f.write(part)
```

再或者你还可以写出一些结合I/O操作的混合方案：

```python
def combine(source, maxsize):
    parts = []
    size = 0
    for part in source:
        parts.append(part)
        size += len(part)
        if size > maxsize:
            yield ''.join(parts)
            parts = []
            size = 0
    yield ''.join(parts)

# 结合文件操作
with open('filename', 'w') as f:
    for part in combine(sample(), 32768):
        f.write(part)
```

这里的关键点在于原始的生成器函数并不需要知道使用细节，它只负责生成字符串片段就行了。

## 2.15. 字符串中插入变量

Python并没有对在字符串中简单替换变量值提供直接的支持。 但是通过使用字符串的 format() 方法来解决这个问题。比如：

```python
s = '{name} has {n} messages.'
print(s.format(name='Guido', n=37))  # Guido has 37 messages.

name = 'Guido'
n = 37
message = f'{name} has {n} messages.'
print(message)  # Guido has 37 messages.
```

或者，如果要被替换的变量能在变量域中找到， 那么你可以结合使用 format_map() 和 vars() 。就像下面这样：

```python
s = '{name} has {n} messages.'
name = 'Guido'
n = 37
print(s.format_map(vars()))  # Guido has 37 messages.

d = {'name': name, 'n': n}
print(s.format_map(d))  # Guido has 37 messages.
```

vars() 还有一个有意思的特性就是它也适用于对象实例。比如：

```python
class Info:
    def __init__(self, name, n):
        self.name = name
        self.n = n


if __name__ == '__main__':
    s = '{name} has {n} messages.'
    a = Info('Guido', 37)
    print(s.format_map(vars(a)))
输出：
'Guido has 37 messages.'
```

format 和 format_map() 的一个缺陷就是它们并不能很好的处理变量缺失的情况，比如：

```python
>>> s.format(name='Guido')
Traceback (most recent call last):
  File "<stdin>", line 1, in <module>
KeyError: 'n'
```

一种避免这种错误的方法是另外定义一个含有 __missing__() 方法的字典对象，就像下面这样，现在你可以利用这个类包装输入后传递给 format_map() ：

```python
class Safesub(dict):
    """防止key找不到"""

    def __missing__(self, key):
        return '{' + key + '}'


if __name__ == '__main__':
    s = '{name} has {n} messages.'
    name = 'Guido'
    print(s.format_map(Safesub(vars())))
输出：
Guido has {n} messages.
```

如果你发现自己在代码中频繁的执行这些步骤，你可以将变量替换步骤用一个工具函数封装起来。就像下面这样：

```python
import sys


class Safesub(dict):
    """防止key找不到"""
    def __missing__(self, key):
        return '{' + key + '}'


def sub(text):
    return text.format_map(Safesub(sys._getframe(1).f_locals))


if __name__ == '__main__':
    name = 'Guido'
    n = 37
    print(sub('Hello {name}'))
    print(sub('You have {n} messages.'))
    print(sub('Your favorite color is {color}'))
输出：
Hello Guido
You have 37 messages.
Your favorite color is {color}
```

多年以来由于Python缺乏对变量替换的内置支持而导致了各种不同的解决方案。 作为本节中展示的一个可能的解决方案，你可以有时候会看到像下面这样的字符串格式化代码：

```python
>>> name = 'Guido'
>>> n = 37
>>> '%(name) has %(n) messages.' % vars()
'Guido has 37 messages.'
```

你可能还会看到字符串模板的使用：

```python
>>> import string
>>> s = string.Template('$name has $n messages.')
>>> s.substitute(vars())
'Guido has 37 messages.'
```

然而， format() 和 format_map() 相比较上面这些方案而已更加先进，因此应该被优先选择。 使用 format() 方法还有一个好处就是你可以获得对字符串格式化的所有支持(对齐，填充，数字格式化等待)， 而这些特性是使用像模板字符串之类的方案不可能获得的。

本机还部分介绍了一些高级特性。映射或者字典类中鲜为人知的 __missing__() 方法可以让你定义如何处理缺失的值。 在 SafeSub 类中，这个方法被定义为对缺失的值返回一个占位符。 你可以发现缺失的值会出现在结果字符串中(在调试的时候可能很有用)，而不是产生一个 KeyError 异常。

sub() 函数使用 sys._getframe(1) 返回调用者的栈帧。可以从中访问属性 f_locals 来获得局部变量。 毫无疑问绝大部分情况下在代码中去直接操作栈帧应该是不推荐的。 但是，对于像字符串替换工具函数而言它是非常有用的。 另外，值得注意的是 f_locals 是一个复制调用函数的本地变量的字典。 尽管你可以改变 f_locals 的内容，但是这个修改对于后面的变量访问没有任何影响。 所以，虽说访问一个栈帧看上去很邪恶，但是对它的任何操作不会覆盖和改变调用者本地变量的值。

## 2.16. 以指定列宽格式化字符串

你有一些长字符串，想以指定的列宽将它们重新格式化。

使用 textwrap 模块来格式化字符串的输出。比如，假如你有下列的长字符串：

下面演示使用 textwrap 格式化字符串的多种方式：

```python
import textwrap

if __name__ == '__main__':
    s = "Look into my eyes, look into my eyes, the eyes, the eyes, \
    the eyes, not around the eyes, don't look around the eyes, \
    look into my eyes, you're under."
    print(textwrap.fill(s, 70))
    print('====================')
    print(textwrap.fill(s, 40))
    print('====================')
    print(textwrap.fill(s, 40, initial_indent='    '))
    print('====================')
    print(textwrap.fill(s, 40, subsequent_indent='    '))
    print('====================')
输出：
Look into my eyes, look into my eyes, the eyes, the eyes,     the
eyes, not around the eyes, don't look around the eyes,     look into
my eyes, you're under.
====================
Look into my eyes, look into my eyes,
the eyes, the eyes,     the eyes, not
around the eyes, don't look around the
eyes,     look into my eyes, you're
under.
====================
    Look into my eyes, look into my
eyes, the eyes, the eyes,     the eyes,
not around the eyes, don't look around
the eyes,     look into my eyes, you're
under.
====================
Look into my eyes, look into my eyes,
    the eyes, the eyes,     the eyes,
    not around the eyes, don't look
    around the eyes,     look into my
    eyes, you're under.
====================
```

textwrap 模块对于字符串打印是非常有用的，特别是当你希望输出自动匹配终端大小的时候。 你可以使用 os.get_terminal_size() 方法来获取终端的大小尺寸。比如：

```python
>>> import os
>>> os.get_terminal_size().columns
80
```

fill() 方法接受一些其他可选参数来控制tab，语句结尾等。 参阅 textwrap.TextWrapper文档 获取更多内容。

## 2.17. 在字符串中处理html和xml

你想将HTML或者XML实体如 &entity; 或 &#code; 替换为对应的文本。 再者，你需要转换文本中特定的字符(比如<, >, 或 &)。

如果你想替换文本字符串中的 ‘<’ 或者 ‘>’ ，使用 html.escape() 函数可以很容易的完成。比如：

```python
import html

if __name__ == '__main__':
    s = 'Elements are written as "<tag>text</tag>".'
    print(s)
    print(html.escape(s))

    # Disable escaping of quotes
    print(html.escape(s, quote=False))
输出：
Elements are written as "<tag>text</tag>".
Elements are written as &quot;&lt;tag&gt;text&lt;/tag&gt;&quot;.
Elements are written as "&lt;tag&gt;text&lt;/tag&gt;".
```

如果你正在处理的是ASCII文本，并且想将非ASCII文本对应的编码实体嵌入进去， 可以给某些I/O函数传递参数 errors='xmlcharrefreplace' 来达到这个目。比如：

```python
s = 'Spicy Jalapeño'
print(s.encode('ascii', errors='xmlcharrefreplace'))
输出：
b'Spicy Jalape&#241;o'
```

为了替换文本中的编码实体，你需要使用另外一种方法。 如果你正在处理HTML或者XML文本，试着先使用一个合适的HTML或者XML解析器。 通常情况下，这些工具会自动替换这些编码值，你无需担心。

有时候，如果你接收到了一些含有编码值的原始文本，需要手动去做替换， 通常你只需要使用HTML或者XML解析器的一些相关工具函数/方法即可。比如：

```python
import html
from xml.sax.saxutils import unescape

if __name__ == '__main__':
    s = 'Spicy &quot;Jalape&#241;o&quot.'
    # p = HTMLParser()
    # p.unescape(s)  # 3.5之后使用html.unescape(s)
    print(html.unescape(s))
    t = 'The prompt is &gt;&gt;&gt;'
    print(unescape(t))
输出：
Spicy "Jalapeño".
The prompt is >>>
```

在生成HTML或者XML文本的时候，如果正确的转换特殊标记字符是一个很容易被忽视的细节。 特别是当你使用 print() 函数或者其他字符串格式化来产生输出的时候。 使用像 html.escape() 的工具函数可以很容易的解决这类问题。

如果你想以其他方式处理文本，还有一些其他的工具函数比如 xml.sax.saxutils.unescapge() 可以帮助你。 然而，你应该先调研清楚怎样使用一个合适的解析器。 比如，如果你在处理HTML或XML文本， 使用某个解析模块比如 html.parse 或 xml.etree.ElementTree 已经帮你自动处理了相关的替换细节。

## 2.18. 字符串令牌解析

为了令牌化字符串，你不仅需要匹配模式，还得指定模式的类型。 比如，你可能想将字符串像下面这样转换为序列对：

`tokens = [('NAME', 'foo'), ('EQ','='), ('NUM', '23'), ('PLUS','+'),
          ('NUM', '42'), ('TIMES', '*'), ('NUM', '10')]`

为了执行这样的切分，第一步就是像下面这样利用命名捕获组的正则表达式来定义所有可能的令牌，包括空格：

```python
import re
NAME = r'(?P<NAME>[a-zA-Z_][a-zA-Z_0-9]*)'
NUM = r'(?P<NUM>\d+)'
PLUS = r'(?P<PLUS>\+)'
TIMES = r'(?P<TIMES>\*)'
EQ = r'(?P<EQ>=)'
WS = r'(?P<WS>\s+)'

master_pat = re.compile('|'.join([NAME, NUM, PLUS, TIMES, EQ, WS]))
```

在上面的模式中， `?P<TOKENNAME>` 用于给一个模式命名，供后面使用。

下一步，为了令牌化，使用模式对象很少被人知道的 scanner() 方法。 这个方法会创建一个 scanner 对象， 在这个对象上不断的调用 match() 方法会一步步的扫描目标文本，每步一个匹配。 下面是演示一个 scanner 对象如何工作的交互式例子：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import re

if __name__ == '__main__':
    tokens = [('NAME', 'foo'), ('EQ', '='), ('NUM', '23'), ('PLUS', '+'),
              ('NUM', '42'), ('TIMES', '*'), ('NUM', '10')]

    NAME = r'(?P<NAME>[a-zA-Z_][a-zA-Z_0-9]*)'
    NUM = r'(?P<NUM>\d+)'
    PLUS = r'(?P<PLUS>\+)'
    TIMES = r'(?P<TIMES>\*)'
    EQ = r'(?P<EQ>=)'
    WS = r'(?P<WS>\s+)'

    master_pat = re.compile('|'.join([NAME, NUM, PLUS, TIMES, EQ, WS]))

    scanner = master_pat.scanner('foo = 42')
    _ = scanner.match()
    print(_.lastgroup, _.group(), sep=', ')
    _ = scanner.match()
    print(_.lastgroup, _.group(), sep=', ')
    _ = scanner.match()
    print(_.lastgroup, _.group(), sep=', ')
    _ = scanner.match()
    print(_.lastgroup, _.group(), sep=', ')
    _ = scanner.match()
    print(_.lastgroup, _.group(), sep=', ')
输出：
NAME, foo
WS,  
EQ, =
WS,  
NUM, 42
```

实际使用这种技术的时候，可以很容易的像下面这样将上述代码打包到一个生成器中：

```python
import re
from collections import namedtuple


def generate_tokens(pat, text):
    Token = namedtuple('Token', ['type', 'value'])
    scanner = pat.scanner(text)
    for m in iter(scanner.match, None):
        yield Token(m.lastgroup, m.group())


if __name__ == '__main__':
    tokens = [('NAME', 'foo'), ('EQ', '='), ('NUM', '23'), ('PLUS', '+'),
              ('NUM', '42'), ('TIMES', '*'), ('NUM', '10')]

    NAME = r'(?P<NAME>[a-zA-Z_][a-zA-Z_0-9]*)'
    NUM = r'(?P<NUM>\d+)'
    PLUS = r'(?P<PLUS>\+)'
    TIMES = r'(?P<TIMES>\*)'
    EQ = r'(?P<EQ>=)'
    WS = r'(?P<WS>\s+)'

    master_pat = re.compile('|'.join([NAME, NUM, PLUS, TIMES, EQ, WS]))

    # Example use
    for tok in generate_tokens(master_pat, 'foo = 42'):
        print(tok)
输出：
Token(type='NAME', value='foo')
Token(type='WS', value=' ')
Token(type='EQ', value='=')
Token(type='WS', value=' ')
Token(type='NUM', value='42')
```

==============================================

注：iter方法介绍：<https://www.cnblogs.com/yitouniu/p/5243136.html>

iter(object, sentinel)

这句话的意思是说：如果传递了第二个参数，则object必须是一个可调用的对象（如，函数）。此时，iter创建了一个迭代器对象，每次调用这个迭代器对象的__next__()方法时，都会调用object。

如果__next__的返回值等于sentinel，则抛出StopIteration异常，否则返回下一个值。

```python
class Counter:
    def __init__(self, _start, _end):
        self.start = _start
        self.end = _end

    def get_next(self):
        s = self.start
        if self.start < self.end:
            self.start += 1
        else:
            raise StopIteration
        return s

c = Counter(1, 5)
iterator = iter(c.get_next, 3)
print(type(iterator))
for i in iterator:
    print(i)
输出：
<class 'callable_iterator'>
1
2  
```

```python
# 错误的使用样例
def counter(n):
    def inner():
        i = 1
        while i < n:
            yield i
            i += 1

    return inner

if __name__ == '__main__':
    c = counter(10)  # 返回一个闭包函数
    for i in iter(c, None):  # 每次调用c()，返回一个新的generator，永远不等于None，所以死循环
        print(i)

# 修改如下：
def counter(n):
    i = 0

    def inner():
        nonlocal i
        i += 1
        if i < n:
            return i

    return inner


if __name__ == '__main__':
    c = counter(10)  # 返回一个闭包函数
    for i in iter(c, None):  # 每次调用c()，得到一个int
        print(i)
```

==============================================

如果你想过滤令牌流，你可以定义更多的生成器函数或者使用一个生成器表达式。 比如，下面演示怎样过滤所有的空白令牌：

```python
tokens = (tok for tok in generate_tokens(master_pat, 'foo = 42')
            if tok.type != 'WS')
for tok in tokens:
    print(tok)
输出：
Token(type='NAME', value='foo')
Token(type='EQ', value='=')
Token(type='NUM', value='42')
```

通常来讲令牌化是很多高级文本解析与处理的第一步。 为了使用上面的扫描方法，你需要记住这里一些重要的几点。 第一点就是你必须确认你使用正则表达式指定了所有输入中可能出现的文本序列。 如果有任何不可匹配的文本出现了，扫描就会直接停止。这也是为什么上面例子中必须指定空白字符令牌的原因。

令牌的顺序也是有影响的。 re 模块会按照指定好的顺序去做匹配。 因此，**如果一个模式恰好是另一个更长模式的子字符串，那么你需要确定长模式写在前面**。比如：

```python
LT = r'(?P<LT><)'
LE = r'(?P<LE><=)'
EQ = r'(?P<EQ>=)'

master_pat = re.compile('|'.join([LE, LT, EQ])) # Correct
# master_pat = re.compile('|'.join([LT, LE, EQ])) # Incorrect
```

第二个模式是错的，因为它会将文本<=匹配为令牌LT紧跟着EQ，而不是单独的令牌LE，这个并不是我们想要的结果。

最后，你需要留意下子字符串形式的模式。比如，假设你有如下两个模式：

```python
PRINT = r'(?P<PRINT>print)'
NAME = r'(?P<NAME>[a-zA-Z_][a-zA-Z_0-9]*)'

# 解析成两个字符串
master_pat = re.compile('|'.join([PRINT, NAME]))  # Token(type='PRINT', value='print')\nToken(type='NAME', value='er')
# 解析成一个字符串
# master_pat = re.compile('|'.join([NAME, PRINT]))  # Token(type='NAME', value='printer')

for tok in generate_tokens(master_pat, 'printer'):
    print(tok)
```

关于更高阶的令牌化技术，你可能需要查看 PyParsing 或者 PLY 包。 一个调用PLY的例子在下一节会有演示。

## 2.19. 实现一个简单的递归下降分析器

<https://python3-cookbook.readthedocs.io/zh_CN/latest/c02/p19_writing_recursive_descent_parser.html>

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Topic: 下降解析器
Desc :
"""
import collections
import re

# Token specification
NUM = r'(?P<NUM>\d+)'
PLUS = r'(?P<PLUS>\+)'
MINUS = r'(?P<MINUS>-)'
TIMES = r'(?P<TIMES>\*)'
DIVIDE = r'(?P<DIVIDE>/)'
LPAREN = r'(?P<LPAREN>\()'
RPAREN = r'(?P<RPAREN>\))'
WS = r'(?P<WS>\s+)'

master_pat = re.compile('|'.join([NUM, PLUS, MINUS, TIMES,
                                  DIVIDE, LPAREN, RPAREN, WS]))
# Tokenizer
Token = collections.namedtuple('Token', ['type', 'value'])


def generate_tokens(text):
    scanner = master_pat.scanner(text)
    for m in iter(scanner.match, None):
        tok = Token(m.lastgroup, m.group())
        if tok.type != 'WS':
            yield tok


# Parser
class ExpressionEvaluator:
    '''
    Implementation of a recursive descent parser. Each method
    implements a single grammar rule. Use the ._accept() method
    to test and accept the current lookahead token. Use the ._expect()
    method to exactly match and discard the next token on on the input
    (or raise a SyntaxError if it doesn't match).
    '''

    def parse(self, text):
        self.tokens = generate_tokens(text)
        self.tok = None  # Last symbol consumed
        self.nexttok = None  # Next symbol tokenized
        self._advance()  # Load first lookahead token
        return self.expr()

    def _advance(self):
        'Advance one token ahead'
        self.tok, self.nexttok = self.nexttok, next(self.tokens, None)

    def _accept(self, toktype):
        'Test and consume the next token if it matches toktype'
        if self.nexttok and self.nexttok.type == toktype:
            self._advance()
            return True
        else:
            return False

    def _expect(self, toktype):
        'Consume next token if it matches toktype or raise SyntaxError'
        if not self._accept(toktype):
            raise SyntaxError('Expected ' + toktype)

    # Grammar rules follow
    def expr(self):
        "expression ::= term { ('+'|'-') term }*"
        exprval = self.term()
        while self._accept('PLUS') or self._accept('MINUS'):
            op = self.tok.type
            right = self.term()
            if op == 'PLUS':
                exprval += right
            elif op == 'MINUS':
                exprval -= right
        return exprval

    def term(self):
        "term ::= factor { ('*'|'/') factor }*"
        termval = self.factor()
        while self._accept('TIMES') or self._accept('DIVIDE'):
            op = self.tok.type
            right = self.factor()
            if op == 'TIMES':
                termval *= right
            elif op == 'DIVIDE':
                termval /= right
        return termval

    def factor(self):
        "factor ::= NUM | ( expr )"
        if self._accept('NUM'):
            return int(self.tok.value)
        elif self._accept('LPAREN'):
            exprval = self.expr()
            self._expect('RPAREN')
            return exprval
        else:
            raise SyntaxError('Expected NUMBER or LPAREN')


def descent_parser():
    e = ExpressionEvaluator()
    # print(e.parse('2'))
    # print(e.parse('2 + 3'))
    # print(e.parse('2 + 3 * 4'))
    # print(e.parse('2 + (3 + 4) * 5'))
    # print(e.parse('(3 + 4)'))
    print(e.parse('3 * 4'))

    # print(e.parse('2 + (3 + * 4)'))
    # Traceback (most recent call last):
    #    File "<stdin>", line 1, in <module>
    #    File "exprparse.py", line 40, in parse
    #    return self.expr()
    #    File "exprparse.py", line 67, in expr
    #    right = self.term()
    #    File "exprparse.py", line 77, in term
    #    termval = self.factor()
    #    File "exprparse.py", line 93, in factor
    #    exprval = self.expr()
    #    File "exprparse.py", line 67, in expr
    #    right = self.term()
    #    File "exprparse.py", line 77, in term
    #    termval = self.factor()
    #    File "exprparse.py", line 97, in factor
    #    raise SyntaxError("Expected NUMBER or LPAREN")
    #    SyntaxError: Expected NUMBER or LPAREN


if __name__ == '__main__':
    descent_parser()
```

对于复杂的语法，你最好是选择某个解析工具比如PyParsing或者是PLY。 下面是使用PLY来重写表达式求值程序的代码：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from ply.lex import lex
from ply.yacc import yacc

# Token list
tokens = ['NUM', 'PLUS', 'MINUS', 'TIMES', 'DIVIDE', 'LPAREN', 'RPAREN']
# Ignored characters
t_ignore = ' \t\n'
# Token specifications (as regexs)
t_PLUS = r'\+'
t_MINUS = r'-'
t_TIMES = r'\*'
t_DIVIDE = r'/'
t_LPAREN = r'\('
t_RPAREN = r'\)'


# Token processing functions
def t_NUM(t):
    r'\d+'
    t.value = int(t.value)
    return t


# Error handler
def t_error(t):
    print('Bad character: {!r}'.format(t.value[0]))
    t.skip(1)


# Build the lexer
lexer = lex()


# Grammar rules and handler functions
def p_expr(p):
    '''
    expr : expr PLUS term
        | expr MINUS term
    '''
    if p[2] == '+':
        p[0] = p[1] + p[3]
    elif p[2] == '-':
        p[0] = p[1] - p[3]


def p_expr_term(p):
    '''
    expr : term
    '''
    p[0] = p[1]


def p_term(p):
    '''
    term : term TIMES factor
    | term DIVIDE factor
    '''
    if p[2] == '*':
        p[0] = p[1] * p[3]
    elif p[2] == '/':
        p[0] = p[1] / p[3]


def p_term_factor(p):
    '''
    term : factor
    '''
    p[0] = p[1]


def p_factor(p):
    '''
    factor : NUM
    '''
    p[0] = p[1]


def p_factor_group(p):
    '''
    factor : LPAREN expr RPAREN
    '''
    p[0] = p[2]


def p_error(p):
    print('Syntax error')


if __name__ == '__main__':
    parser = yacc()
    print(parser.parse('2+3'))
    print(parser.parse('2'))
    print(parser.parse('2+(3+4)*5'))
```

这个程序中，所有代码都位于一个比较高的层次。你只需要为令牌写正则表达式和规则匹配时的高阶处理函数即可。 而实际的运行解析器，接受令牌等等底层动作已经被库函数实现了。

如果你想在你的编程过程中来点挑战和刺激，编写解析器和编译器是个不错的选择。 再次，一本编译器的书籍会包含很多底层的理论知识。不过很多好的资源也可以在网上找到。 Python自己的ast模块也值得去看一下。

## 2.20. 字节字符串上的字符串操作

你想在字节字符串上执行普通的文本操作(比如移除，搜索和替换)。

解决方案
字节字符串同样也支持大部分和文本字符串一样的内置操作。比如：

```python
>>> data = b'Hello World'
>>> data[0:5]
b'Hello'
>>> data.startswith(b'Hello')
True
>>> data.split()
[b'Hello', b'World']
>>> data.replace(b'Hello', b'Hello Cruel')
b'Hello Cruel World'
```

这些操作同样也适用于字节数组。比如：

```python
>>> data = bytearray(b'Hello World')
>>> data[0:5]
bytearray(b'Hello')
>>> data.startswith(b'Hello')
True
>>> data.split()
[bytearray(b'Hello'), bytearray(b'World')]
>>> data.replace(b'Hello', b'Hello Cruel')
bytearray(b'Hello Cruel World')
```

你可以使用正则表达式匹配字节字符串，但是正则表达式本身必须也是字节串。比如：

```python
>>> data = b'FOO:BAR,SPAM'
>>> import re
>>> re.split('[:,]',data)
Traceback (most recent call last):
File "<stdin>", line 1, in <module>
File "/usr/local/lib/python3.3/re.py", line 191, in split
return _compile(pattern, flags).split(string, maxsplit)
TypeError: can't use a string pattern on a bytes-like object
>>> re.split(b'[:,]',data) # Notice: pattern as bytes
[b'FOO', b'BAR', b'SPAM']
```

大多数情况下，在文本字符串上的操作均可用于字节字符串。 然而，这里也有一些需要注意的不同点。首先，字节字符串的索引操作返回整数而不是单独字符。比如：

```python
>>> a = 'Hello World' # Text string
>>> a[0]
'H'
>>> a[1]
'e'
>>> b = b'Hello World' # Byte string
>>> b[0]
72
>>> b[1]
101
```

这种语义上的区别会对于处理面向字节的字符数据有影响。

第二点，字节字符串不会提供一个美观的字符串表示，也不能很好的打印出来，除非它们先被解码为一个文本字符串。比如：

```python
>>> s = b'Hello World'
>>> print(s)
b'Hello World' # Observe b'...'
>>> print(s.decode('ascii'))
Hello World
```

类似的，也不存在任何适用于字节字符串的格式化操作：

```python
>>> b'%10s %10d %10.2f' % (b'ACME', 100, 490.1)
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
TypeError: unsupported operand type(s) for %: 'bytes' and 'tuple'
>>> b'{} {} {}'.format(b'ACME', 100, 490.1)
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
AttributeError: 'bytes' object has no attribute 'format'
```

如果你想格式化字节字符串，你得先使用标准的文本字符串，然后将其编码为字节字符串。比如：

```python
>>> '{:10s} {:10d} {:10.2f}'.format('ACME', 100, 490.1).encode('ascii')
b'ACME 100 490.10'
```

最后需要注意的是，使用字节字符串可能会改变一些操作的语义，特别是那些跟文件系统有关的操作。 比如，如果你使用一个编码为字节的文件名，而不是一个普通的文本字符串，会禁用文件名的编码/解码。比如：

```python
>>> # Write a UTF-8 filename
>>> with open('jalape\xf1o.txt', 'w') as f:
...     f.write('spicy')
...
>>> # Get a directory listing
>>> import os
>>> os.listdir('.') # Text string (names are decoded)
['jalapeño.txt']
>>> os.listdir(b'.') # Byte string (names left as bytes)
[b'jalapen\xcc\x83o.txt']
```

注意例子中的最后部分给目录名传递一个字节字符串是怎样导致结果中文件名以未解码字节返回的。 在目录中的文件名包含原始的UTF-8编码。 参考5.15小节获取更多文件名相关的内容。

最后提一点，一些程序员为了提升程序执行的速度会倾向于使用字节字符串而不是文本字符串。 尽管操作字节字符串确实会比文本更加高效(因为处理文本固有的Unicode相关开销)。 这样做通常会导致非常杂乱的代码。你会经常发现字节字符串并不能和Python的其他部分工作的很好， 并且你还得手动处理所有的编码/解码操作。 坦白讲，如果你在处理文本的话，就直接在程序中使用普通的文本字符串而不是字节字符串。不做死就不会死！

# 3. 数字日期和时间

## 3.1. 数字的四舍五入

你想对浮点数执行指定精度的舍入运算。

对于简单的舍入运算，使用内置的 round(value, ndigits) 函数即可。比如：

```python
>>> round(1.23, 1)
1.2
>>> round(1.27, 1)
1.3
>>> round(-1.27, 1)
-1.3
>>> round(1.25361,3)
1.254
```

当一个值刚好在两个边界的中间的时候， round 函数返回离它最近的偶数。 也就是说，对1.5或者2.5的舍入运算都会得到2。

传给 round() 函数的 ndigits 参数可以是负数，这种情况下， 舍入运算会作用在十位、百位、千位等上面。比如：

```python
>>> a = 1627731
>>> round(a, -1)
1627730
>>> round(a, -2)
1627700
>>> round(a, -3)
1628000
```

不要将舍入和格式化输出搞混淆了。 如果你的目的只是简单的输出一定宽度的数，你不需要使用 round() 函数。 而仅仅只需要在格式化的时候指定精度即可。比如：

```python
>>> x = 1.23456
>>> format(x, '0.2f')
'1.23'
>>> format(x, '0.3f')
'1.235'
>>> 'value is {:0.3f}'.format(x)
'value is 1.235'
```

同样，不要试着去舍入浮点值来”修正”表面上看起来正确的问题。比如，你可能倾向于这样做：

```python
>>> a = 2.1
>>> b = 4.2
>>> c = a + b
>>> c
6.300000000000001
>>> c = round(c, 2) # "Fix" result (???)
>>> c
6.3
```

对于大多数使用到浮点的程序，没有必要也不推荐这样做。 尽管在计算的时候会有一点点小的误差，但是这些小的误差是能被理解与容忍的。 如果不能允许这样的小误差(比如涉及到金融领域)，那么就得考虑使用 decimal 模块了，下一节我们会详细讨论。

## 3.2. 执行精确的浮点数运算

你需要对浮点数执行精确的计算操作，并且不希望有任何小误差的出现。

浮点数的一个普遍问题是它们并不能精确的表示十进制数。 并且，即使是最简单的数学运算也会产生小的误差，比如：

```python
>>> a = 4.2
>>> b = 2.1
>>> a + b
6.300000000000001
>>> (a + b) == 6.3
False
```

这些错误是由底层CPU和IEEE 754标准通过自己的浮点单位去执行算术时的特征。 由于Python的浮点数据类型使用底层表示存储数据，因此你没办法去避免这样的误差。

如果你想更加精确(并能容忍一定的性能损耗)，你可以使用 decimal 模块：

```python
>>> from decimal import Decimal
>>> a = Decimal('4.2')
>>> b = Decimal('2.1')
>>> a + b
Decimal('6.3')
>>> print(a + b)
6.3
>>> (a + b) == Decimal('6.3')
True
>>> print(a + 1.2)  # TypeError: unsupported operand type(s) for +: 'decimal.Decimal' and 'float'
```

初看起来，上面的代码好像有点奇怪，比如我们用字符串来表示数字。 然而， Decimal 对象会像普通浮点数一样的工作(支持所有的常用数学运算)。 如果你打印它们或者在字符串格式化函数中使用它们，看起来跟普通数字没什么两样。

decimal 模块的一个主要特征是允许你控制计算的每一方面，包括数字位数和四舍五入运算。 为了这样做，你先得创建一个本地上下文并更改它的设置，比如：

```python
from decimal import Decimal
from decimal import localcontext

if __name__ == '__main__':
    a = Decimal('1.3')
    b = Decimal('1.7')
    print(a / b)
    with localcontext() as ctx:
        ctx.prec = 3
        print(a / b)
    with localcontext() as ctx:
        ctx.prec = 50
        print(a / b)

    print(a / b)
输出：
0.7647058823529411764705882353
0.765
0.76470588235294117647058823529411764705882352941176
0.7647058823529411764705882353
```

decimal 模块实现了IBM的”通用小数运算规范”。不用说，有很多的配置选项这本书没有提到。

Python新手会倾向于使用 decimal 模块来处理浮点数的精确运算。 然而，先理解你的应用程序目的是非常重要的。 如果你是在做科学计算或工程领域的计算、电脑绘图，或者是科学领域的大多数运算， 那么使用普通的浮点类型是比较普遍的做法。 其中一个原因是，在真实世界中很少会要求精确到普通浮点数能提供的17位精度。 因此，计算过程中的那么一点点的误差是被允许的。 第二点就是，原生的浮点数计算要快的多-有时候你在执行大量运算的时候速度也是非常重要的。

即便如此，你却不能完全忽略误差。数学家花了大量时间去研究各类算法，有些处理误差会比其他方法更好。 你也得注意下减法删除以及大数和小数的加分运算所带来的影响。比如：

上面的错误可以利用 math.fsum() 所提供的更精确计算能力来解决：

```python
import math

if __name__ == '__main__':
    nums = [1.23e+18, 1, -1.23e+18]
    print(sum(nums))  # Notice how 1 disappears
    print(math.fsum(nums))
输出：
0.0
1.0
```

然而，对于其他的算法，你应该仔细研究它并理解它的误差产生来源。

总的来说， decimal 模块主要用在涉及到金融的领域。 在这类程序中，哪怕是一点小小的误差在计算过程中蔓延都是不允许的。 因此， decimal 模块为解决这类问题提供了方法。 当Python和数据库打交道的时候也通常会遇到 Decimal 对象，并且，通常也是在处理金融数据的时候。

## 3.3. 数字的格式化输出

格式化输出单个数字的时候，可以使用内置的 format() 函数，比如：

```python
>>> x = 1234.56789

>>> # Two decimal places of accuracy
>>> format(x, '0.2f')
'1234.57'

>>> # Right justified in 10 chars, one-digit accuracy
>>> format(x, '>10.1f')
'    1234.6'

>>> # Left justified
>>> format(x, '<10.1f')
'1234.6    '

>>> # Centered
>>> format(x, '^10.1f')
'  1234.6  '

>>> # Inclusion of thousands separator
>>> format(x, ',')
'1,234.56789'
>>> format(x, '0,.1f')
'1,234.6'
```

如果你想使用指数记法，将f改成e或者E(取决于指数输出的大小写形式)。比如：

```python
>>> format(x, 'e')
'1.234568e+03'
>>> format(x, '0.2E')
'1.23E+03'
```

同时指定宽度和精度的一般形式是 '[<>^]?width[,]?(.digits)?' ， 其中 width 和 digits 为整数，？代表可选部分。 同样的格式也被用在字符串的 format() 方法中。比如：

```python
>>> 'The value is {:0,.2f}'.format(x)
'The value is 1,234.57'
```

数字格式化输出通常是比较简单的。上面演示的技术同时适用于浮点数和 decimal 模块中的 Decimal 数字对象。

当指定数字的位数后，结果值会根据 round() 函数同样的规则进行四舍五入后返回。比如：

```python
>>> x
1234.56789
>>> format(x, '0.1f')
'1234.6'
>>> format(-x, '0.1f')
'-1234.6'
```

包含千位符的格式化跟本地化没有关系。 如果你需要根据地区来显示千位符，你需要自己去调查下 locale 模块中的函数了。 你同样也可以使用字符串的 translate() 方法来交换千位符。比如：

```python
>>> swap_separators = { ord('.'):',', ord(','):'.' }
>>> format(x, ',').translate(swap_separators)
'1.234,56789'
```

在很多Python代码中会看到使用%来格式化数字的，比如：

```python
>>> '%0.2f' % x
'1234.57'
>>> '%10.1f' % x
'    1234.6'
>>> '%-10.1f' % x
'1234.6    '
```

这种格式化方法也是可行的，不过比更加先进的 format() 要差一点。 比如，在使用%操作符格式化数字的时候，一些特性(添加千位符)并不能被支持。

## 3.4. 二八十六进制整数

为了将整数转换为二进制、八进制或十六进制的文本串， 可以分别使用 bin() , oct() 或 hex() 函数：

```python
>>> x = 1234
>>> bin(x)
'0b10011010010'
>>> oct(x)
'0o2322'
>>> hex(x)
'0x4d2'
```

另外，如果你不想输出 0b , 0o 或者 0x 的前缀的话，可以使用 format() 函数。比如：

```python
>>> format(x, 'b')
'10011010010'
>>> format(x, 'o')
'2322'
>>> format(x, 'x')
'4d2'
```

整数是有符号的，所以如果你在处理负数的话，输出结果会包含一个负号。比如：

```python
>>> x = -1234
>>> format(x, 'b')
'-10011010010'
>>> format(x, 'x')
'-4d2'
```

如果你想产生一个无符号值，你需要增加一个指示最大位长度的值。比如为了显示32位的值，可以像下面这样写：

```python
>>> x = -1234
>>> format(2**32 + x, 'b')
'11111111111111111111101100101110'
>>> format(2**32 + x, 'x')
'fffffb2e'
```

为了以不同的进制转换整数字符串，简单的使用带有进制的 int() 函数即可：

```python
>>> int('4d2', 16)
1234
>>> int('10011010010', 2)
1234
```

大多数情况下处理二进制、八进制和十六进制整数是很简单的。 只要记住这些转换属于整数和其对应的文本表示之间的转换即可。永远只有一种整数类型。

最后，使用八进制的程序员有一点需要注意下。 Python指定八进制数的语法跟其他语言稍有不同。比如，如果你像下面这样指定八进制，会出现语法错误：

```python
>>> import os
>>> os.chmod('script.py', 0755)
    File "<stdin>", line 1
        os.chmod('script.py', 0755)
                            ^
SyntaxError: invalid token
```

需确保八进制数的前缀是 0o ，就像下面这样：

```python
>>> os.chmod('script.py', 0o755)
```

## 3.5. 字节到大整数的打包与解包

假设你的程序需要处理一个拥有128位长的16个元素的字节字符串。比如：

```python
data = b'\x00\x124V\x00x\x90\xab\x00\xcd\xef\x01\x00#\x004'
```

为了将bytes解析为整数，使用 int.from_bytes() 方法，并像下面这样指定字节顺序：

```python
>>> len(data)
16
>>> int.from_bytes(data, 'little')
69120565665751139577663547927094891008
>>> int.from_bytes(data, 'big')
94522842520747284487117727783387188
```

为了将一个大整数转换为一个字节字符串，使用 int.to_bytes() 方法，并像下面这样指定字节数和字节顺序：

```python
>>> x = 94522842520747284487117727783387188
>>> x.to_bytes(16, 'big')
b'\x00\x124V\x00x\x90\xab\x00\xcd\xef\x01\x00#\x004'
>>> x.to_bytes(16, 'little')
b'4\x00#\x00\x01\xef\xcd\x00\xab\x90x\x00V4\x12\x00'
```

大整数和字节字符串之间的转换操作并不常见。 然而，在一些应用领域有时候也会出现，比如密码学或者网络。 例如，IPv6网络地址使用一个128位的整数表示。 如果你要从一个数据记录中提取这样的值的时候，你就会面对这样的问题。

作为一种替代方案，你可能想使用6.11小节中所介绍的 struct 模块来解压字节。 这样也行得通，不过利用 struct 模块来解压对于整数的大小是有限制的。 因此，你可能想解压多个字节串并将结果合并为最终的结果，就像下面这样：

```python
>>> data
b'\x00\x124V\x00x\x90\xab\x00\xcd\xef\x01\x00#\x004'
>>> import struct
>>> hi, lo = struct.unpack('>QQ', data)
>>> (hi << 64) + lo
94522842520747284487117727783387188
```

字节顺序规则(little或big)仅仅指定了构建整数时的字节的低位高位排列方式。 我们从下面精心构造的16进制数的表示中可以很容易的看出来：

```python
>>> x = 0x01020304
>>> x.to_bytes(4, 'big')
b'\x01\x02\x03\x04'
>>> x.to_bytes(4, 'little')
b'\x04\x03\x02\x01'
```

如果你试着将一个整数打包为字节字符串，那么它就不合适了，你会得到一个错误。 如果需要的话，你可以使用 int.bit_length() 方法来决定需要多少字节位来存储这个值。

```python
>>> x = 523 ** 23
>>> x
335381300113661875107536852714019056160355655333978849017944067
>>> x.to_bytes(16, 'little')
Traceback (most recent call last):
File "<stdin>", line 1, in <module>
OverflowError: int too big to convert
>>> x.bit_length()
208
>>> nbytes, rem = divmod(x.bit_length(), 8)
>>> if rem:
... nbytes += 1
...
>>>
>>> x.to_bytes(nbytes, 'little')
b'\x03X\xf1\x82iT\x96\xac\xc7c\x16\xf3\xb9\xcf...\xd0'
```

## 3.6. 复数的数学运算

复数可以用使用函数 complex(real, imag) 或者是带有后缀j的浮点数来指定。比如：

```python
>>> a = complex(2, 4)
>>> b = 3 - 5j
>>> a
(2+4j)
>>> b
(3-5j)
```

对应的实部、虚部和共轭复数可以很容易的获取。就像下面这样：

```python
>>> a.real
2.0
>>> a.imag
4.0
>>> a.conjugate()
(2-4j)
```

另外，所有常见的数学运算都可以工作：

```python
>>> a + b
(5-1j)
>>> a * b
(26+2j)
>>> a / b
(-0.4117647058823529+0.6470588235294118j)
>>> abs(a)
4.47213595499958
```

如果要执行其他的复数函数比如正弦、余弦或平方根，使用 cmath 模块：

```python
>>> import cmath
>>> cmath.sin(a)
(24.83130584894638-11.356612711218174j)
>>> cmath.cos(a)
(-11.36423470640106-24.814651485634187j)
>>> cmath.exp(a)
(-4.829809383269385-5.5920560936409816j)
```

Python中大部分与数学相关的模块都能处理复数。 比如如果你使用 numpy ，可以很容易的构造一个复数数组并在这个数组上执行各种操作：

```python
>>> import numpy as np
>>> a = np.array([2+3j, 4+5j, 6-7j, 8+9j])
>>> a
array([ 2.+3.j, 4.+5.j, 6.-7.j, 8.+9.j])
>>> a + 2
array([ 4.+3.j, 6.+5.j, 8.-7.j, 10.+9.j])
>>> np.sin(a)
array([ 9.15449915 -4.16890696j, -56.16227422 -48.50245524j,
        -153.20827755-526.47684926j, 4008.42651446-589.49948373j])
```

Python的标准数学函数确实情况下并不能产生复数值，因此你的代码中不可能会出现复数返回值。比如：

```python
>>> import math
>>> math.sqrt(-1)
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
ValueError: math domain error
```

如果你想生成一个复数返回结果，你必须显示的使用 cmath 模块，或者在某个支持复数的库中声明复数类型的使用。比如：

```python
>>> import cmath
>>> cmath.sqrt(-1)
1j
```

## 3.7. 无穷大与NaN

你想创建或测试正无穷、负无穷或NaN(非数字)的浮点数。

Python并没有特殊的语法来表示这些特殊的浮点值，但是可以使用 float() 来创建它们。比如：

```python
>>> a = float('inf')
>>> b = float('-inf')
>>> c = float('nan')
>>> a
inf
>>> b
-inf
>>> c
nan
```

为了测试这些值的存在，使用 math.isinf() 和 math.isnan() 函数。比如：

```python
>>> math.isinf(a)
True
>>> math.isnan(c)
True
```

想了解更多这些特殊浮点值的信息，可以参考IEEE 754规范。 然而，也有一些地方需要你特别注意，特别是跟比较和操作符相关的时候。

无穷大数在执行数学计算的时候会传播，比如：

```python
>>> a = float('inf')
>>> a + 45
inf
>>> a * 10
inf
>>> 10 / a
0.0
```

但是有些操作时未定义的并会返回一个NaN结果。比如：

```python
>>> a = float('inf')
>>> a/a
nan
>>> b = float('-inf')
>>> a + b
nan
```

NaN值会在所有操作中传播，而不会产生异常。比如：

```python
>>> c = float('nan')
>>> c + 23
nan
>>> c / 2
nan
>>> c * 2
nan
>>> math.sqrt(c)
nan
```

NaN值的一个特别的地方时它们之间的比较操作总是返回False。比如：

```python
>>> c = float('nan')
>>> d = float('nan')
>>> c == d
False
>>> c is d
False
```

由于这个原因，测试一个NaN值得唯一安全的方法就是使用 math.isnan() ，也就是上面演示的那样。

有时候程序员想改变Python默认行为，在返回无穷大或NaN结果的操作中抛出异常。 fpectl 模块可以用来改变这种行为，但是它在标准的Python构建中并没有被启用，它是平台相关的， 并且针对的是专家级程序员。可以参考在线的Python文档获取更多的细节。

## 3.8. 分数运算

你进入时间机器，突然发现你正在做小学家庭作业，并涉及到分数计算问题。 或者你可能需要写代码去计算在你的木工工厂中的测量值。

fractions 模块可以被用来执行包含分数的数学运算。比如：

```python
>>> from fractions import Fraction
>>> a = Fraction(5, 4)
>>> b = Fraction(7, 16)
>>> print(a + b)
27/16
>>> print(a * b)
35/64

>>> # Getting numerator/denominator
>>> c = a * b
>>> c.numerator
35
>>> c.denominator
64

>>> # Converting to a float
>>> float(c)
0.546875

>>> # Limiting the denominator of a value
>>> print(c.limit_denominator(8))
4/7

>>> # Converting a float to a fraction
>>> x = 3.75
>>> y = Fraction(*x.as_integer_ratio())
>>> y
Fraction(15, 4)
```

在大多数程序中一般不会出现分数的计算问题，但是有时候还是需要用到的。 比如，在一个允许接受分数形式的测试单位并以分数形式执行运算的程序中， 直接使用分数可以减少手动转换为小数或浮点数的工作。

## 3.9. 大型数组运算

涉及到数组的重量级运算操作，可以使用 NumPy 库。 NumPy 的一个主要特征是它会给Python提供一个数组对象，相比标准的Python列表而已更适合用来做数学运算。 下面是一个简单的小例子，向你展示标准列表对象和 NumPy 数组对象之间的差别：

```python
>>> # Python lists
>>> x = [1, 2, 3, 4]
>>> y = [5, 6, 7, 8]
>>> x * 2
[1, 2, 3, 4, 1, 2, 3, 4]
>>> x + 10
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
TypeError: can only concatenate list (not "int") to list
>>> x + y
[1, 2, 3, 4, 5, 6, 7, 8]

>>> # Numpy arrays
>>> import numpy as np
>>> ax = np.array([1, 2, 3, 4])
>>> ay = np.array([5, 6, 7, 8])
>>> ax * 2
array([2, 4, 6, 8])
>>> ax + 10
array([11, 12, 13, 14])
>>> ax + ay
array([ 6, 8, 10, 12])
>>> ax * ay
array([ 5, 12, 21, 32])
```

正如所见，两种方案中数组的基本数学运算结果并不相同。 特别的， NumPy 中的标量运算(比如 ax * 2 或 ax + 10 )会作用在每一个元素上。 另外，当两个操作数都是数组的时候执行元素对等位置计算，并最终生成一个新的数组。

对整个数组中所有元素同时执行数学运算可以使得作用在整个数组上的函数运算简单而又快速。 比如，如果你想计算多项式的值，可以这样做：

```python
import numpy as np

def f(x):
    return 3 * x ** 2 - 2 * x + 7

if __name__ == '__main__':
    ax = np.array([1, 2, 3, 4])
    print(f(ax))  # array([ 8, 15, 28, 47])
```

NumPy 还为数组操作提供了大量的通用函数，这些函数可以作为 math 模块中类似函数的替代。比如：

```python
>>> np.sqrt(ax)
array([ 1. , 1.41421356, 1.73205081, 2. ])
>>> np.cos(ax)
array([ 0.54030231, -0.41614684, -0.9899925 , -0.65364362])
```

使用这些通用函数要比循环数组并使用 math 模块中的函数执行计算要快的多。 因此，只要有可能的话尽量选择 NumPy 的数组方案。

底层实现中， NumPy 数组使用了C或者Fortran语言的机制分配内存。 也就是说，它们是一个非常大的连续的并由同类型数据组成的内存区域。 所以，你可以构造一个比普通Python列表大的多的数组。 比如，如果你想构造一个10,000*10,000的浮点数二维网格，很轻松：

```python
>>> grid = np.zeros(shape=(10000,10000), dtype=float)
>>> grid
    array([[ 0., 0., 0., ..., 0., 0., 0.],
    [ 0., 0., 0., ..., 0., 0., 0.],
    [ 0., 0., 0., ..., 0., 0., 0.],
    ...,
    [ 0., 0., 0., ..., 0., 0., 0.],
    [ 0., 0., 0., ..., 0., 0., 0.],
    [ 0., 0., 0., ..., 0., 0., 0.]])
```

所有的普通操作还是会同时作用在所有元素上：

```python
>>> grid += 10
>>> grid
array([[ 10., 10., 10., ..., 10., 10., 10.],
    [ 10., 10., 10., ..., 10., 10., 10.],
    [ 10., 10., 10., ..., 10., 10., 10.],
    ...,
    [ 10., 10., 10., ..., 10., 10., 10.],
    [ 10., 10., 10., ..., 10., 10., 10.],
    [ 10., 10., 10., ..., 10., 10., 10.]])
>>> np.sin(grid)
array([[-0.54402111, -0.54402111, -0.54402111, ..., -0.54402111,
        -0.54402111, -0.54402111],
    [-0.54402111, -0.54402111, -0.54402111, ..., -0.54402111,
        -0.54402111, -0.54402111],
    [-0.54402111, -0.54402111, -0.54402111, ..., -0.54402111,
        -0.54402111, -0.54402111],
    ...,
    [-0.54402111, -0.54402111, -0.54402111, ..., -0.54402111,
        -0.54402111, -0.54402111],
    [-0.54402111, -0.54402111, -0.54402111, ..., -0.54402111,
        -0.54402111, -0.54402111],
    [-0.54402111, -0.54402111, -0.54402111, ..., -0.54402111,
        -0.54402111, -0.54402111]])
```

关于 NumPy 有一点需要特别的主意，那就是它扩展Python列表的索引功能 - 特别是对于多维数组。 为了说明清楚，先构造一个简单的二维数组并试着做些试验：

```python
>>> a = np.array([[1, 2, 3, 4], [5, 6, 7, 8], [9, 10, 11, 12]])
>>> a
array([[ 1, 2, 3, 4],
[ 5, 6, 7, 8],
[ 9, 10, 11, 12]])

>>> # Select row 1
>>> a[1]
array([5, 6, 7, 8])

>>> # Select column 1
>>> a[:,1]
array([ 2, 6, 10])

>>> # Select a subregion and change it
>>> a[1:3, 1:3]
array([[ 6, 7],
        [10, 11]])
>>> a[1:3, 1:3] += 10
>>> a
array([[ 1, 2, 3, 4],
        [ 5, 16, 17, 8],
        [ 9, 20, 21, 12]])

>>> # Broadcast a row vector across an operation on all rows
>>> a + [100, 101, 102, 103]
array([[101, 103, 105, 107],
        [105, 117, 119, 111],
        [109, 121, 123, 115]])
>>> a
array([[ 1, 2, 3, 4],
        [ 5, 16, 17, 8],
        [ 9, 20, 21, 12]])

>>> # Conditional assignment on an array
>>> np.where(a < 10, a, 10)
array([[ 1, 2, 3, 4],
        [ 5, 10, 10, 8],
        [ 9, 10, 10, 10]])
```

NumPy 是Python领域中很多科学与工程库的基础，同时也是被广泛使用的最大最复杂的模块。 即便如此，在刚开始的时候通过一些简单的例子和玩具程序也能帮我们完成一些有趣的事情。

通常我们导入 NumPy 模块的时候会使用语句 import numpy as np 。 这样的话你就不用再你的程序里面一遍遍的敲入 numpy ，只需要输入 np 就行了，节省了不少时间。

如果想获取更多的信息，你当然得去 NumPy 官网逛逛了，网址是： <http://www.numpy.org>

## 3.10. 矩阵与线性代数运算

NumPy 库有一个矩阵对象可以用来解决这个问题。
矩阵类似于3.9小节中数组对象，但是遵循线性代数的计算规则。下面的一个例子展示了矩阵的一些基本特性：

```python
>>> import numpy as np
>>> m = np.matrix([[1,-2,3],[0,4,5],[7,8,-9]])
>>> m
matrix([[ 1, -2, 3],
        [ 0, 4, 5],
        [ 7, 8, -9]])

>>> # Return transpose
>>> m.T
matrix([[ 1, 0, 7],
        [-2, 4, 8],
        [ 3, 5, -9]])

>>> # Return inverse
>>> m.I
matrix([[ 0.33043478, -0.02608696, 0.09565217],
        [-0.15217391, 0.13043478, 0.02173913],
        [ 0.12173913, 0.09565217, -0.0173913 ]])

>>> # Create a vector and multiply
>>> v = np.matrix([[2],[3],[4]])
>>> v
matrix([[2],
        [3],
        [4]])
>>> m * v
matrix([[ 8],
        [32],
        [ 2]])
```

可以在 numpy.linalg 子包中找到更多的操作函数，比如：

```python
>>> import numpy.linalg

>>> # Determinant
>>> numpy.linalg.det(m)
-229.99999999999983

>>> # Eigenvalues
>>> numpy.linalg.eigvals(m)
array([-13.11474312, 2.75956154, 6.35518158])

>>> # Solve for x in mx = v
>>> x = numpy.linalg.solve(m, v)
>>> x
matrix([[ 0.96521739],
        [ 0.17391304],
        [ 0.46086957]])
>>> m * x
matrix([[ 2.],
        [ 3.],
        [ 4.]])
>>> v
matrix([[2],
        [3],
        [4]])
```

很显然线性代数是个非常大的主题，已经超出了本书能讨论的范围。 但是，如果你需要操作数组和向量的话， NumPy 是一个不错的入口点。 可以访问 NumPy 官网 <http://www.numpy.org> 获取更多信息。

## 3.11. 随机选择

random 模块有大量的函数用来产生随机数和随机选择元素。 比如，要想从一个序列中随机的抽取一个元素，可以使用 random.choice() ：

```python
>>> import random
>>> values = [1, 2, 3, 4, 5, 6]
>>> random.choice(values)
2
>>> random.choice(values)
3
>>> random.choice(values)
1
>>> random.choice(values)
4
>>> random.choice(values)
6
```

为了提取出N个不同元素的样本用来做进一步的操作，可以使用 random.sample() ：

```python
>>> random.sample(values, 2)
[6, 2]
>>> random.sample(values, 2)
[4, 3]
>>> random.sample(values, 3)
[4, 3, 1]
>>> random.sample(values, 3)
[5, 4, 1]
```

如果你仅仅只是想打乱序列中元素的顺序，可以使用 random.shuffle() ：

```python
>>> random.shuffle(values)
>>> values
[2, 4, 6, 5, 3, 1]
>>> random.shuffle(values)
>>> values
[3, 5, 2, 1, 6, 4]
```

生成随机整数，请使用 random.randint() ：

```python
>>> random.randint(0,10)
2
>>> random.randint(0,10)
5
>>> random.randint(0,10)
0
>>> random.randint(0,10)
7
>>> random.randint(0,10)
10
>>> random.randint(0,10)
3
```

为了生成0到1范围内均匀分布的浮点数，使用 random.random() ：

```python
>>> random.random()
0.9406677561675867
>>> random.random()
0.133129581343897
>>> random.random()
0.4144991136919316
```

如果要获取N位随机位(二进制)的整数，使用 random.getrandbits() ：

```python
>>> random.getrandbits(200)
335837000776573622800628485064121869519521710558559406913275
```

random 模块使用 Mersenne Twister 算法来计算生成随机数。这是一个确定性算法， 但是你可以通过 random.seed() 函数修改初始化种子。比如：

```python
random.seed() # Seed based on system time or os.urandom()
random.seed(12345) # Seed based on integer given
random.seed(b'bytedata') # Seed based on byte data
```

除了上述介绍的功能，random模块还包含基于均匀分布、高斯分布和其他分布的随机数生成函数。 比如， random.uniform() 计算均匀分布随机数， random.gauss() 计算正态分布随机数。 对于其他的分布情况请参考在线文档。

在 random 模块中的函数不应该用在和密码学相关的程序中。 如果你确实需要类似的功能，可以使用ssl模块中相应的函数。 比如， ssl.RAND_bytes() 可以用来生成一个安全的随机字节序列。

## 3.12. 基本的日期与时间转换

为了执行不同时间单位的转换和计算，请使用 datetime 模块。 比如，为了表示一个时间段，可以创建一个 timedelta 实例，就像下面这样：

```python
from datetime import timedelta

if __name__ == '__main__':
    a = timedelta(days=2, hours=6)
    b = timedelta(hours=4.5)
    c = a + b
    print(c.days)
    print(c.seconds)
    print(c.seconds / 3600)
    print(c.total_seconds() / 3600)
输出：
2
37800
10.5
58.5
```

如果你想表示指定的日期和时间，先创建一个 datetime 实例然后使用标准的数学运算来操作它们。比如：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from datetime import timedelta, datetime

if __name__ == '__main__':
    a = datetime(2012, 9, 23)
    print(a + timedelta(days=10))

    b = datetime(2012, 12, 21)
    d = b - a
    print(d.days)

    now = datetime.today()
    print(now)

    print(now + timedelta(minutes=10))
输出：
2012-10-03 00:00:00
89
2019-05-12 13:49:04.126557
2019-05-12 13:59:04.126557
```

在计算的时候，需要注意的是 datetime 会自动处理闰年。比如：

```python
>>> a = datetime(2012, 3, 1)
>>> b = datetime(2012, 2, 28)
>>> a - b
datetime.timedelta(2)
>>> (a - b).days
2
>>> c = datetime(2013, 3, 1)
>>> d = datetime(2013, 2, 28)
>>> (c - d).days
1
```

对大多数基本的日期和时间处理问题， datetime 模块已经足够了。 如果你需要执行更加复杂的日期操作，比如处理时区，模糊时间范围，节假日计算等等， 可以考虑使用 dateutil模块

许多类似的时间计算可以使用 dateutil.relativedelta() 函数代替。 但是，有一点需要注意的就是，它会在处理月份(还有它们的天数差距)的时候填充间隙。看例子最清楚：

```python
>>> a = datetime(2012, 9, 23)
>>> a + timedelta(months=1)  # 不支持months参数
Traceback (most recent call last):
File "<stdin>", line 1, in <module>
TypeError: 'months' is an invalid keyword argument for this function
>>>
>>> from dateutil.relativedelta import relativedelta
>>> a + relativedelta(months=+1)
datetime.datetime(2012, 10, 23, 0, 0)
>>> a + relativedelta(months=+4)
datetime.datetime(2013, 1, 23, 0, 0)
>>>
>>> # Time between two dates
>>> b = datetime(2012, 12, 21)
>>> d = b - a
>>> d
datetime.timedelta(89)
>>> d = relativedelta(b, a)
>>> d
relativedelta(months=+2, days=+28)
>>> d.months
2
>>> d.days
28
```

## 3.13. 计算最后一个周五的日期

Python的 datetime 模块中有工具函数和类可以帮助你执行这样的计算。 下面是对类似这样的问题的一个通用解决方案：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Topic: 最后的周五
Desc :
"""
from datetime import datetime, timedelta

weekdays = ['Monday', 'Tuesday', 'Wednesday', 'Thursday',
            'Friday', 'Saturday', 'Sunday']


def get_previous_byday(dayname, start_date=None):
    if start_date is None:
        start_date = datetime.today()
    day_num = start_date.weekday()
    day_num_target = weekdays.index(dayname)
    days_ago = (7 + day_num - day_num_target) % 7
    if days_ago == 0:
        days_ago = 7
    target_date = start_date - timedelta(days=days_ago)
    return target_date


if __name__ == '__main__':
    print(datetime.today())
    print(get_previous_byday('Monday'))
    print(get_previous_byday('Tuesday'))
    print(get_previous_byday('Friday'))

    print(get_previous_byday('Sunday', datetime(2012, 12, 21)))
输出：
2019-05-12 14:13:02.356540
2019-05-06 14:13:02.356540
2019-05-07 14:13:02.356540
2019-05-10 14:13:02.356540
2012-12-16 00:00:00
```

上面的算法原理是这样的：先将开始日期和目标日期映射到星期数组的位置上(星期一索引为0)， 然后通过模运算计算出目标日期要经过多少天才能到达开始日期。然后用开始日期减去那个时间差即得到结果日期。

如果你要像这样执行大量的日期计算的话，你最好安装第三方包 python-dateutil 来代替。 比如，下面是是使用 dateutil 模块中的 relativedelta() 函数执行同样的计算：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from datetime import datetime

from dateutil.relativedelta import relativedelta
from dateutil.rrule import *

if __name__ == '__main__':
    d = datetime.now()
    print(d)

    # Next Friday
    print(d + relativedelta(weekday=FR))

    # Last Friday
    print(d + relativedelta(weekday=FR(-1)))  # FR(-1)调用了__call__方法，新构造了一个FR对象
输出：
2019-05-12 14:38:11.741898
2019-05-17 14:38:11.741898
2019-05-10 14:38:11.741898
```

## 3.14. 计算当前月份的日期范围

你的代码需要在当前月份中循环每一天，想找到一个计算这个日期范围的高效方法。

在这样的日期上循环并需要事先构造一个包含所有日期的列表。 你可以先计算出开始日期和结束日期， 然后在你步进的时候使用 datetime.timedelta 对象递增这个日期变量即可。

下面是一个接受任意 datetime 对象并返回一个由当前月份开始日和下个月开始日组成的元组对象。

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import calendar
from datetime import date, timedelta


def get_month_range(start_date=None):
    if start_date is None:
        start_date = date.today().replace(day=1)  # .replace把当前date用day=1替换
    _, days_in_month = calendar.monthrange(start_date.year, start_date.month)
    end_date = start_date + timedelta(days=days_in_month)
    return (start_date, end_date)


if __name__ == '__main__':
    a_day = timedelta(days=1)
    first_day, last_day = get_month_range()  # 增加入参date.today()则会从当天开始增加当月天数输出
    while first_day < last_day:
        print(first_day)
        first_day += a_day
输出：
2019-05-01
2019-05-02
2019-05-03
2019-05-04
#... and so on...

get_month_range(datetime.today())
输出：
2019-05-12 14:50:56.321440
2019-05-13 14:50:56.321440
2019-05-14 14:50:56.321440
2019-05-15 14:50:56.321440
#... and so on...
```

有了这个就可以很容易的在返回的日期范围上面做循环操作了。

上面的代码先计算出一个对应月份第一天的日期。 一个快速的方法就是使用 date 或 datetime 对象的 replace() 方法简单的将 days 属性设置成1即可。 replace() 方法一个好处就是它会创建和你开始传入对象类型相同的对象。 所以，如果输入参数是一个 date 实例，那么结果也是一个 date 实例。 同样的，如果输入是一个 datetime 实例，那么你得到的就是一个 datetime 实例。

然后，使用 calendar.monthrange() 函数来找出该月的总天数。 任何时候只要你想获得日历信息，那么 calendar 模块就非常有用了。 monthrange() 函数会返回包含星期和该月天数的元组。

注：monthrange()返回的第1个值day1，调用的是如下函数，返回的是当月第1天是星期几：

```python
day1 = weekday(year, month, 1)

def weekday(year, month, day):
    """Return weekday (0-6 ~ Mon-Sun) for year, month (1-12), day (1-31)."""
```

一旦该月的天数已知了，那么结束日期就可以通过在开始日期上面加上这个天数获得。 有个需要注意的是结束日期并不包含在这个日期范围内(事实上它是下个月的开始日期)。 这个和Python的 slice 与 range 操作行为保持一致，同样也不包含结尾。

为了在日期范围上循环，要使用到标准的数学和比较操作。 比如，可以利用 timedelta 实例来递增日期，小于号<用来检查一个日期是否在结束日期之前。

理想情况下，如果能为日期迭代创建一个同内置的 range() 函数一样的函数就好了。 幸运的是，可以使用一个生成器来很容易的实现这个目标：

```python
def date_range(start, stop, step):
    while start < stop:
        yield start
        start += step
for d in date_range(datetime(2019, 5, 12), datetime(2019, 5, 15), timedelta(hours=6)):
    print(d)
输出：
2019-05-12 00:00:00
2019-05-12 06:00:00
2019-05-12 12:00:00
2019-05-12 18:00:00
2019-05-13 00:00:00
2019-05-13 06:00:00
2019-05-13 12:00:00
2019-05-13 18:00:00
2019-05-14 00:00:00
2019-05-14 06:00:00
2019-05-14 12:00:00
2019-05-14 18:00:00
```

这种实现之所以这么简单，还得归功于Python中的日期和时间能够使用标准的数学和比较操作符来进行运算。

## 3.15. 字符串转换为日期

你的应用程序接受字符串格式的输入，但是你想将它们转换为 datetime 对象以便在上面执行非字符串操作。

使用Python的标准模块 datetime 可以很容易的解决这个问题。比如：

```python
from datetime import datetime

if __name__ == '__main__':
    text = '2019-05-11'
    y = datetime.strptime(text, '%Y-%m-%d')
    z = datetime.now()
    print(z)
    diff = z - y
    print(diff)
    print(type(diff))
输出：
2019-05-12 15:08:04.122641
1 day, 15:08:04.122641
<class 'datetime.timedelta'>
```

datetime.strptime() 方法支持很多的格式化代码， 比如 %Y 代表4位数年份， %m 代表两位数月份。 还有一点值得注意的是这些格式化占位符也可以反过来使用，将日期输出为指定的格式字符串形式。

比如，假设你的代码中生成了一个 datetime 对象， 你想将它格式化为漂亮易读形式后放在自动生成的信件或者报告的顶部：

```python
from datetime import datetime

if __name__ == '__main__':
    z = datetime.today()
    nice_z = datetime.strftime(z, '%A %B %d, %Y')
    print(nice_z)
输出：
Sunday May 12, 2019
```

还有一点需要注意的是， strptime() 的性能要比你想象中的差很多， 因为它是使用纯Python实现，并且必须处理所有的系统本地设置。 如果你要在代码中需要解析大量的日期并且已经知道了日期字符串的确切格式，可以自己实现一套解析方案来获取更好的性能。 比如，如果你已经知道所以日期格式是 YYYY-MM-DD ，你可以像下面这样实现一个解析函数：

```python
from datetime import datetime, date


def parse_ymd(s):
    year_s, mon_s, day_s = s.split('-')
    # return datetime(int(year_s), int(mon_s), int(day_s))
    return date(int(year_s), int(mon_s), int(day_s))


if __name__ == '__main__':
    text = '2012-09-20'
    print(parse_ymd(text))
输出：
2012-09-20
```

实际测试中，这个函数比 datetime.strptime() 快7倍多。 如果你要处理大量的涉及到日期的数据的话，那么最好考虑下这个方案！

## 3.16. 结合时区的日期操作

你有一个安排在2012年12月21日早上9:30的电话会议，地点在芝加哥。 而你的朋友在印度的班加罗尔，那么他应该在当地时间几点参加这个会议呢？

解决方案
对几乎所有涉及到时区的问题，你都应该使用 pytz 模块。这个包提供了Olson时区数据库， 它是时区信息的事实上的标准，在很多语言和操作系统里面都可以找到。

pytz 模块一个主要用途是将 datetime 库创建的简单日期对象本地化。 比如，下面如何表示一个芝加哥时间的示例：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from datetime import datetime

from pytz import timezone

if __name__ == '__main__':
    d = datetime(2019, 5, 12, 15, 20, 0)
    print(d)

    central = timezone('Asia/Shanghai')
    loc_d = central.localize(d)
    print(loc_d)
输出：
2019-05-12 15:20:00
2019-05-12 15:20:00+08:00
```

一旦日期被本地化了， 它就可以转换为其他时区的时间了。 为了得到班加罗尔对应的时间，你可以这样做：

```python
bang_d = loc_d.astimezone(timezone('Asia/Tokyo'))  # 'Asia/Kolkata'
print(bang_d)
输出：
2019-05-12 16:20:00+09:00
```

注：使用pytz样例：

```python
from pytz import country_timezones

# https://www.cnblogs.com/makeabug/articles/3426876.html
print(country_timezones('cn'))
print(country_timezones('us'))
输出：
['Asia/Shanghai', 'Asia/Urumqi']
['America/New_York', 'America/Detroit', 'America/Kentucky/Louisville', 'America/Kentucky/Monticello', 'America/Indiana/Indianapolis', 'America/Indiana/Vincennes', 'America/Indiana/Winamac', 'America/Indiana/Marengo', 'America/Indiana/Petersburg', 'America/Indiana/Vevay', 'America/Chicago', 'America/Indiana/Tell_City', 'America/Indiana/Knox', 'America/Menominee', 'America/North_Dakota/Center', 'America/North_Dakota/New_Salem', 'America/North_Dakota/Beulah', 'America/Denver', 'America/Boise', 'America/Phoenix', 'America/Los_Angeles', 'America/Anchorage', 'America/Juneau', 'America/Sitka', 'America/Metlakatla', 'America/Yakutat', 'America/Nome', 'America/Adak', 'Pacific/Honolulu']
```

如果你打算在本地化日期上执行计算，你需要特别注意夏令时转换和其他细节。 比如，在2013年，美国标准夏令时时间开始于本地时间3月13日凌晨2:00(在那时，时间向前跳过一小时)。 如果你正在执行本地计算，你会得到一个错误。比如：

结果错误是因为它并没有考虑在本地时间中有一小时的跳跃。 为了修正这个错误，可以使用时区对象 normalize() 方法。比如：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from datetime import datetime, timedelta

from pytz import timezone

if __name__ == '__main__':
    d = datetime(2013, 3, 10, 1, 45)
    print(d)

    central = timezone('US/Central')
    loc_d = central.localize(d)
    print(loc_d)

    later = loc_d + timedelta(minutes=30)
    print(later)  # 2013-03-10 02:15:00-06:00 # WRONG! WRONG!

    later = central.normalize(loc_d + timedelta(minutes=30))
    print(later)
输出：
2013-03-10 01:45:00
2013-03-10 01:45:00-06:00
2013-03-10 02:15:00-06:00
2013-03-10 03:15:00-05:00
```

为了不让你被这些东东弄的晕头转向，处理本地化日期的通常的策略先将所有日期转换为UTC时间， 并用它来执行所有的中间存储和操作。比如：

```python
>>> print(loc_d)
2013-03-10 01:45:00-06:00
>>> utc_d = loc_d.astimezone(pytz.utc)
>>> print(utc_d)
2013-03-10 07:45:00+00:00
```

一旦转换为UTC，你就不用去担心跟夏令时相关的问题了。 因此，你可以跟之前一样放心的执行常见的日期计算。 当你想将输出变为本地时间的时候，使用合适的时区去转换下就行了。比如：

```python
>>> later_utc = utc_d + timedelta(minutes=30)
>>> print(later_utc.astimezone(central))
2013-03-10 03:15:00-05:00
```

当涉及到时区操作的时候，有个问题就是我们如何得到时区的名称。 比如，在这个例子中，我们如何知道“Asia/Kolkata”就是印度对应的时区名呢？ 为了查找，可以使用ISO 3166国家代码作为关键字去查阅字典 pytz.country_timezones 。比如：

通过如下网址查看国家代码：
<http://doc.chacuo.net/iso-3166-1>

```python
>>> pytz.country_timezones['IN']
['Asia/Kolkata']
```

注：当你阅读到这里的时候，有可能 pytz 模块已经不再建议使用了，因为PEP431提出了更先进的时区支持。 但是这里谈到的很多问题还是有参考价值的(比如使用UTC日期的建议等)。

使用样例：

```python
from datetime import datetime, timedelta

import pytz
from pytz import timezone

if __name__ == '__main__':
    d = datetime(2019, 5, 12, 15, 30)
    print(d)

    central = timezone('Asia/Shanghai')
    loc_d = central.localize(d)
    print(loc_d)

    utc_d = loc_d.astimezone(pytz.utc)
    print(utc_d)

    later_utc = utc_d + timedelta(minutes=30)
    print(later_utc.astimezone(central))
```

# 4. 迭代器与生成器

迭代是Python最强大的功能之一。初看起来，你可能会简单的认为迭代只不过是处理序列中元素的一种方法。 然而，绝非仅仅就是如此，还有很多你可能不知道的， 比如创建你自己的迭代器对象，在itertools模块中使用有用的迭代模式，构造生成器函数等等。 这一章目的就是向你展示跟迭代有关的各种常见问题。

## 4.1. 手动遍历迭代器

为了手动的遍历可迭代对象，使用 next() 函数并在代码中捕获 StopIteration 异常。 比如，下面的例子手动读取一个文件中的所有行：

```python
def manual_iter():
    with open('test.txt') as f:
        try:
            while True:
                line = next(f)
                print(line, end='')
        except StopIteration:
            pass

if __name__ == '__main__':
    manual_iter()
```

通常来讲， StopIteration 用来指示迭代的结尾。 然而，如果你手动使用上面演示的 next() 函数的话，你还可以通过返回一个指定值来标记结尾，比如 None 。 下面是示例：

```python
def manual_iter():
    with open('test.txt') as f:
        while True:
            line = next(f, None)
            if line is None:
                break
            print(line, end='')

# 使用f.readline函数
with open('test.txt') as f:
    line = f.readline()
    while line:
        print(line, end='')
        line = f.readline()
```

大多数情况下，我们会使用 for 循环语句用来遍历一个可迭代对象。 但是，偶尔也需要对迭代做更加精确的控制，这时候了解底层迭代机制就显得尤为重要了。

下面的交互示例向我们演示了迭代期间所发生的基本细节：

```python
>>> items = [1, 2, 3]
>>> # Get the iterator
>>> it = iter(items) # Invokes items.__iter__()
>>> # Run the iterator
>>> next(it) # Invokes it.__next__()
1
>>> next(it)
2
>>> next(it)
3
>>> next(it)
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
StopIteration
```

## 4.2. 代理迭代

你构建了一个自定义容器对象，里面包含有列表、元组或其他可迭代对象。 你想直接在你的这个新容器对象上执行迭代操作。

实际上你只需要定义一个 __iter__() 方法，将迭代操作代理到容器内部的对象上去。比如：

```python
class Node:
    def __init__(self, value):
        self._value = value
        self._children = []

    def __repr__(self):
        return 'Node({!r})'.format(self._value)

    def add_child(self, node):
        self._children.append(node)

    def __iter__(self):
        return iter(self._children)


# Example
if __name__ == '__main__':
    root = Node(0)
    child1 = Node(1)
    child2 = Node(2)
    root.add_child(child1)
    root.add_child(child2)
    # Outputs Node(1), Node(2)
    for ch in root:
        print(ch)
输出：
Node(1)
Node(2)
```

在上面代码中，\_\_iter\_\_() 方法只是简单的将迭代请求传递给内部的 \_children 属性。

Python的迭代器协议需要 \_\_iter\_\_() 方法返回一个实现了 \_\_next\_\_() 方法的迭代器对象。 如果你只是迭代遍历其他容器的内容，你无须担心底层是怎样实现的。你所要做的只是传递迭代请求既可。

这里的 iter() 函数的使用简化了代码， iter(s) 只是简单的通过调用 s.\_\_iter\_\_() 方法来返回对应的迭代器对象， 就跟 len(s) 会调用 s.\_\_len\_\_() 原理是一样的。

注：x!r代表repr(x)，x!s代表str(x)，x!a代表ascii(x)

<https://www.zhihu.com/question/48140853/answer/109343702>

```python
class Pair:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __repr__(self):
        return 'Pair({0.x!r},{0.y!r})'.format(self)

    def __str__(self):
        return '({0.x!s},{0.y!s})'.format(self)


# Example
if __name__ == '__main__':
    p = Pair(1, 2)
    print(p)  # (1,2)
```

__str__和__repr__区别：

<http://baijiahao.baidu.com/s?id=1596817611604972751&wfr=spider&for=pc>

```python
class Car(object):
    def __init__(self, color, mileage):
        self.color = color
        self.mileage = mileage

    def __repr__(self):
        return (f'{self.__class__.__name__}('
                f'{self.color!r}, {self.mileage!r})')

    # 可选
    def __str__(self):
        return f'a {self.color} car'


# Example
if __name__ == '__main__':
    car = Car('red', 37281)
    print(car)
    print(repr(car))
输出：
a red car
Car('red', 37281)
```

* 我们可以使用__str__和__repr__方法定义类到字符串的转化方式，而不需要手动打印某些属性或是添加额外的方法。

* 一般来说，__str__的返回结果在于强可读性，而__repr__的返回结果在于准确性。

* 我们至少需要添加一个__repr__方法来保证类到字符串的自定义转化的有效性，__str__是可选的。因为默认情况下，在需要却找不到__str__方法的时候，会自动调用__repr__方法。

## 4.3. 使用生成器创建新的迭代模式

你想实现一个自定义迭代模式，跟普通的内置函数比如 range() , reversed() 不一样。

如果你想实现一种新的迭代模式，使用一个生成器函数来定义它。 下面是一个生产某个范围内浮点数的生成器：

```python
def frange(start, stop, increment):
    x = start
    while x < stop:
        yield x
        x += increment
```

为了使用这个函数， 你可以用for循环迭代它或者使用其他接受一个可迭代对象的函数(比如 sum() , list() 等)。示例如下：

```python
>>> for n in frange(0, 4, 0.5):
...     print(n)
...
0
0.5
1.0
1.5
2.0
2.5
3.0
3.5
>>> list(frange(0, 1, 0.125))
[0, 0.125, 0.25, 0.375, 0.5, 0.625, 0.75, 0.875]
```

一个函数中需要有一个 yield 语句即可将其转换为一个生成器。 跟普通函数不同的是，生成器只能用于迭代操作。 下面是一个实验，向你展示这样的函数底层工作机制：

```python
>>> def countdown(n):
...     print('Starting to count from', n)
...     while n > 0:
...         yield n
...         n -= 1
...     print('Done!')
...

>>> # Create the generator, notice no output appears
>>> c = countdown(3)
>>> c
<generator object countdown at 0x1006a0af0>

>>> # Run to first yield and emit a value
>>> next(c)
Starting to count from 3
3

>>> # Run to the next yield
>>> next(c)
2

>>> # Run to next yield
>>> next(c)
1

>>> # Run to next yield (iteration stops)
>>> next(c)
Done!
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
StopIteration
```

一个生成器函数主要特征是它只会回应在迭代中使用到的 next 操作。 一旦生成器函数返回退出，迭代终止。我们在迭代中通常使用的for语句会自动处理这些细节，所以你无需担心。

## 4.4. 实现迭代器协议

你想构建一个能支持迭代操作的自定义对象，并希望找到一个能实现迭代协议的简单方法。

目前为止，在一个对象上实现迭代最简单的方式是使用一个生成器函数。 在4.2小节中，使用Node类来表示树形数据结构。你可能想实现一个以深度优先方式遍历树形节点的生成器。 下面是代码示例：

```python
class Node:
    def __init__(self, value):
        self._value = value
        self._children = []

    def __repr__(self):
        return 'Node({!r})'.format(self._value)

    def add_child(self, node):
        self._children.append(node)

    def __iter__(self):
        return iter(self._children)

    def depth_first(self):
        yield self
        for c in self:
            yield from c.depth_first()

# Example
if __name__ == '__main__':
    root = Node(0)
    child1 = Node(1)
    child2 = Node(2)
    root.add_child(child1)
    root.add_child(child2)
    child1.add_child(Node(3))
    child1.add_child(Node(4))
    child2.add_child(Node(5))

    for ch in root.depth_first():
        print(ch)
    # Outputs Node(0), Node(1), Node(3), Node(4), Node(2), Node(5)
```

在这段代码中，depth_first() 方法简单直观。 它首先返回自己本身并迭代每一个子节点并 通过调用子节点的 depth_first() 方法(使用 yield from 语句)返回对应元素。

Python的迭代协议要求一个__iter__()方法返回一个特殊的迭代器对象， 这个迭代器对象实现了__next__()方法并通过 StopIteration 异常标识迭代的完成。 但是，实现这些通常会比较繁琐。 下面我们演示下这种方式，如何使用一个关联迭代器类重新实现 depth_first() 方法：

```python
class Node2:
    def __init__(self, value):
        self._value = value
        self._children = []

    def __repr__(self):
        return 'Node({!r})'.format(self._value)

    def add_child(self, node):
        self._children.append(node)

    def __iter__(self):
        return iter(self._children)

    def depth_first(self):
        return DepthFirstIterator(self)


class DepthFirstIterator(object):
    '''
    Depth-first traversal
    '''

    def __init__(self, start_node):
        self._node = start_node
        self._children_iter = None
        self._child_iter = None

    def __iter__(self):
        return self

    def __next__(self):
        # Return myself if just started; create an iterator for children
        if self._children_iter is None:
            self._children_iter = iter(self._node)
            return self._node
        # If processing a child, return its next item
        elif self._child_iter:
            try:
                nextchild = next(self._child_iter)
                return nextchild
            except StopIteration:
                self._child_iter = None
                return next(self)
        # Advance to the next child and start its iteration
        else:
            self._child_iter = next(self._children_iter).depth_first()
            return next(self)
```

DepthFirstIterator 类和上面使用生成器的版本工作原理类似， 但是它写起来很繁琐，因为迭代器必须在迭代处理过程中维护大量的状态信息。 坦白来讲，没人愿意写这么晦涩的代码。将你的迭代器定义为一个生成器后一切迎刃而解。

## 4.5. 反向迭代

使用内置的 reversed() 函数，比如：

```python
>>> a = [1, 2, 3, 4]
>>> for x in reversed(a):
...     print(x)
...
4
3
2
1
```

反向迭代仅仅当对象的大小可预先确定或者对象实现了 __reversed__() 的特殊方法时才能生效。 如果两者都不符合，那你必须先将对象转换为一个列表才行，比如：

```python
# Print a file backwards
f = open('somefile')
for line in reversed(list(f)):
    print(line, end='')

# 如下报错：
with open('test.txt', 'r') as f:
    for line in reversed(f):
        print(line, end='')
TypeError: '_io.TextIOWrapper' object is not reversible
```

要注意的是如果可迭代对象元素很多的话，将其预先转换为一个列表要消耗大量的内存。

很多程序员并不知道可以通过在自定义类上实现 __reversed__() 方法来实现反向迭代。比如：

```python
class Countdown:
    def __init__(self, start):
        self.start = start

    # Forward iterator
    def __iter__(self):
        n = self.start
        while n > 0:
            yield n
            n -= 1

    # Reverse iterator
    def __reversed__(self):
        n = 1
        while n <= self.start:
            yield n
            n += 1


if __name__ == '__main__':
    for rr in reversed(Countdown(5)):
        print(rr)
    for rr in Countdown(5):
        print(rr)
输出：
1
2
3
4
5
5
4
3
2
1
```

定义一个反向迭代器可以使得代码非常的高效， 因为它不再需要将数据填充到一个列表中然后再去反向迭代这个列表。

## 4.6. 带有外部状态的生成器函数

你想定义一个生成器函数，但是它会调用某个你想暴露给用户使用的外部状态值。

如果你想让你的生成器暴露外部状态给用户， 别忘了你可以简单的将它实现为一个类，然后把生成器函数放到 __iter__() 方法中过去。比如：

```python
from collections import deque


class linehistory:
    def __init__(self, lines, histlen=3):
        self.lines = lines
        self.history = deque(maxlen=histlen)

    def __iter__(self):
        for lineno, line in enumerate(self.lines, 1):
            self.history.append((lineno, line))
            yield line

    def clear(self):
        self.history.clear()
```

为了使用这个类，你可以将它当做是一个普通的生成器函数。 然而，由于可以创建一个实例对象，于是你可以访问内部属性值， 比如 history 属性或者是 clear() 方法。代码示例如下：

```python
if __name__ == '__main__':
    with open('test.txt') as f:
        lines = linehistory(f)
        for line in lines:
            if 'python' in line:
                for lineno, hline in lines.history:
                    print('{}:{}'.format(lineno, hline), end='')

# 测试test.txt
hello, world
good
python is a good language
learning python
luck
nice day
this is python iter test

输出：
1:hello, world
2:good
3:python is a good language
2:good
3:python is a good language
4:learning python
5:luck
6:nice day
7:this is python iter test
```

关于生成器，很容易掉进函数无所不能的陷阱。 如果生成器函数需要跟你的程序其他部分打交道的话(比如暴露属性值，允许通过方法调用来控制等等)， 可能会导致你的代码异常的复杂。 如果是这种情况的话，可以考虑使用上面介绍的定义类的方式。 在 __iter__() 方法中定义你的生成器不会改变你任何的算法逻辑。 由于它是类的一部分，所以允许你定义各种属性和方法来供用户使用。

一个需要注意的小地方是，如果你在迭代操作时不使用for循环语句，那么你得先调用 iter() 函数。比如：

```python
>>> f = open('somefile.txt')
>>> lines = linehistory(f)
>>> next(lines)
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
TypeError: 'linehistory' object is not an iterator

>>> # Call iter() first, then start iterating
>>> it = iter(lines)
>>> next(it)
'hello world\n'
>>> next(it)
'this is a test\n'
```

## 4.7. 迭代器切片

你想得到一个由迭代器生成的切片对象，但是标准切片操作并不能做到。

函数 itertools.islice() 正好适用于在迭代器和生成器上做切片操作。比如：

```python
>>> def count(n):
...     while True:
...         yield n
...         n += 1
...
>>> c = count(0)
>>> c[10:20]
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
TypeError: 'generator' object is not subscriptable

>>> # Now using islice()
>>> import itertools
>>> for x in itertools.islice(c, 10, 20):
...     print(x)
...
10
11
12
13
14
15
16
17
18
19
```

迭代器和生成器不能使用标准的切片操作，因为它们的长度事先我们并不知道(并且也没有实现索引)。 函数 islice() 返回一个可以生成指定元素的迭代器，它通过遍历并丢弃直到切片开始索引位置的所有元素。 然后才开始一个个的返回元素，并直到切片结束索引位置。

这里要着重强调的一点是 islice() 会消耗掉传入的迭代器中的数据。 必须考虑到迭代器是不可逆的这个事实。 所以如果你需要之后再次访问这个迭代器的话，那你就得先将它里面的数据放入一个列表中。

迭代器使用切片样例：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import itertools


class Countdown:
    def __init__(self, start):
        self.start = start

    # Forward iterator
    def __iter__(self):
        n = self.start
        while n > 0:
            yield n
            n -= 1


def count(n):
    while n > 0:
        yield n
        n -= 1


if __name__ == '__main__':
    c = Countdown(10)
    for x in itertools.islice(c, 3, 7):
        print(x)

    print('=======1')
    for x in c:
        print(x)

    print('=======2')
    iterC = iter(c)
    for x in itertools.islice(c, 1, 4):
        print(x)

    print('=======3')
    for x in iterC:
        print(x)

    print('=======4')
    r = range(1, 5)
    for i in itertools.islice(r, 0, 2):
        print(i)

    print('=======5')
    for i in r:  # range对象不会被消耗
        print(i)

    print('=======6')
    c = count(5)
    for i in itertools.islice(c, 0, 3):
        print(i)

    print('=======7')
    for i in c:  # 经过itertools.islice后，generator对象c被消耗，只能迭代出剩余对象
        print(i)

输出：
7
6
5
4
=======1
10
9
8
7
6
5
4
3
2
1
=======2
9
8
7
=======3
10
9
8
7
6
5
4
3
2
1
=======4
1
2
=======5
1
2
3
4
=======6
5
4
3
=======7
2
1
```

## 4.8. 跳过可迭代对象的开始部分

你想遍历一个可迭代对象，但是它开始的某些元素你并不感兴趣，想跳过它们。

解决方案
itertools 模块中有一些函数可以完成这个任务。 首先介绍的是 itertools.dropwhile() 函数。使用时，你给它传递一个函数对象和一个可迭代对象。 它会返回一个迭代器对象，丢弃原有序列中直到函数返回Flase之前的所有元素，然后返回后面所有元素。

为了演示，假定你在读取一个开始部分是几行注释的源文件。比如：

```python
with open('test.txt') as f:
    for line in f:
        print(line, end='')
输出：
# test.txt
# User Database
#
# Note that this file is consulted directly only when the system is running
# in single-user mode. At other times, this information is provided by
# Open Directory.
#
##
nobody:*:-2:-2:Unprivileged User:/var/empty:/usr/bin/false
root:*:0:0:System Administrator:/var/root:/bin/sh
```

如果你想跳过开始部分的注释行的话，可以这样做：

```python
import itertools

if __name__ == '__main__':
    with open('test.txt') as f:
        for line in itertools.dropwhile(lambda line: line.startswith('#'), f):
            print(line, end='')
输出：
nobody:*:-2:-2:Unprivileged User:/var/empty:/usr/bin/false
root:*:0:0:System Administrator:/var/root:/bin/sh
```

这个例子是基于根据某个测试函数跳过开始的元素。 如果你已经明确知道了要跳过的元素的个数的话，那么可以使用 itertools.islice() 来代替。比如：

```python
import itertools

if __name__ == '__main__':
    items = ['a', 'b', 'c', 1, 4, 10, 15]
    for x in itertools.islice(items, 3, None):
        print(x)

    for x in itertools.islice(items, None, 3):
        print(x)
输出：
1
4
10
15
a
b
c
```

在这个例子中， islice() 函数最后那个 None 参数指定了你要获取从第3个到最后的所有元素， 如果 None 和3的位置对调，意思就是仅仅获取前三个元素恰恰相反， (这个跟切片的相反操作 [3:] 和 [:3] 原理是一样的)。

函数 dropwhile() 和 islice() 其实就是两个帮助函数，为的就是避免写出下面这种冗余代码：

```python
with open('test.txt') as f:
    # Skip over initial comments
    while True:
        line = next(f, '')
        if not line.startswith('#'):
            break

    # Process remaining lines
    while line:
        # Replace with useful processing
        print(line, end='')
        line = next(f, None)
```

跳过一个可迭代对象的开始部分跟通常的过滤是不同的。 比如，上述代码的第一个部分可能会这样重写：

```python
with open('/etc/passwd') as f:
    lines = (line for line in f if not line.startswith('#'))
    for line in lines:
        print(line, end='')
```

这样写确实可以跳过开始部分的注释行，但是同样也会跳过文件中其他所有的注释行。 换句话讲，我们的解决方案是仅仅跳过开始部分满足测试条件的行，在那以后，所有的元素不再进行测试和过滤了。

最后需要着重强调的一点是，本节的方案适用于所有可迭代对象，包括那些事先不能确定大小的， 比如生成器，文件及其类似的对象。

## 4.9. 排列组合的迭代

你想迭代遍历一个集合中元素的所有可能的排列或组合

itertools模块提供了三个函数来解决这类问题。 其中一个是 itertools.permutations() ， 它接受一个集合并产生一个元组序列，每个元组由集合中所有元素的一个可能排列组成。 也就是说通过打乱集合中元素排列顺序生成一个元组，比如：

```python
from itertools import permutations

if __name__ == '__main__':
    items = ['a', 'b', 'c']
    for p in permutations(items):
        print(p)
输出：
('a', 'b', 'c')
('a', 'c', 'b')
('b', 'a', 'c')
('b', 'c', 'a')
('c', 'a', 'b')
('c', 'b', 'a')
```

如果你想得到指定长度的所有排列，你可以传递一个可选的长度参数。就像这样：

```python
>>> for p in permutations(items, 2):
...     print(p)
...
('a', 'b')
('a', 'c')
('b', 'a')
('b', 'c')
('c', 'a')
('c', 'b')
```

使用 itertools.combinations() 可得到输入集合中元素的所有的组合。比如：

```python
>>> from itertools import combinations
>>> for c in combinations(items, 3):
...     print(c)
...
('a', 'b', 'c')

>>> for c in combinations(items, 2):
...     print(c)
...
('a', 'b')
('a', 'c')
('b', 'c')

>>> for c in combinations(items, 1):
...     print(c)
...
('a',)
('b',)
('c',)
```

combinations是按先后顺序组成所有序列，可以理解为挑n个元素，后一个挑选的元素位置只能在前一个元素后：

```python
from itertools import combinations

for c in combinations(range(0, 4), 3):
    print(c)
输出：
(0, 1, 2)
(0, 1, 3)
(0, 2, 3)
(1, 2, 3)
```

对于 combinations() 来讲，元素的顺序已经不重要了。 也就是说，组合 ('a', 'b') 跟 ('b', 'a') 其实是一样的(最终只会输出其中一个)。

在计算组合的时候，一旦元素被选取就会从候选中剔除掉(比如如果元素’a’已经被选取了，那么接下来就不会再考虑它了)。 而函数 itertools.combinations_with_replacement() 允许同一个元素被选择多次，比如：

combinations_with_replacement是按先后顺序组成所有序列，可以理解为挑n个元素，后一个挑选的元素位置只能是前一个元素或在前一个元素后：

```python
from itertools import combinations_with_replacement

if __name__ == '__main__':
    items = ['a', 'b', 'c']
    for c in combinations_with_replacement(items, 3):
        print(c)
输出：
('a', 'a', 'a')
('a', 'a', 'b')
('a', 'a', 'c')
('a', 'b', 'b')
('a', 'b', 'c')
('a', 'c', 'c')
('b', 'b', 'b')
('b', 'b', 'c')
('b', 'c', 'c')
('c', 'c', 'c')
```

这一小节我们向你展示的仅仅是 itertools 模块的一部分功能。 尽管你也可以自己手动实现排列组合算法，但是这样做得要花点脑力。 当我们碰到看上去有些复杂的迭代问题时，最好可以先去看看itertools模块。 如果这个问题很普遍，那么很有可能会在里面找到解决方案！

## 4.10. 序列上索引值迭代

内置的 enumerate() 函数可以很好的解决这个问题：

```python
>>> my_list = ['a', 'b', 'c']
>>> for idx, val in enumerate(my_list):
...     print(idx, val)
...
0 a
1 b
2 c
```

为了按传统行号输出(行号从1开始)，你可以传递一个开始参数：

```python
>>> my_list = ['a', 'b', 'c']
>>> for idx, val in enumerate(my_list, 1):
...     print(idx, val)
...
1 a
2 b
3 c
```

这种情况在你遍历文件时想在错误消息中使用行号定位时候非常有用：

```python
def parse_data(filename):
    with open(filename, 'rt') as f:
        for lineno, line in enumerate(f, 1):
            fields = line.split()
            try:
                count = int(fields[1])
                ...
            except ValueError as e:
                print('Line {}: Parse error: {}'.format(lineno, e))
```

enumerate() 对于跟踪某些值在列表中出现的位置是很有用的。 所以，如果你想将一个文件中出现的单词映射到它出现的行号上去，可以很容易的利用 enumerate() 来完成：

```python
word_summary = defaultdict(list)

with open('myfile.txt', 'r') as f:
    lines = f.readlines()

for idx, line in enumerate(lines):
    # Create a list of words in current line
    words = [w.strip().lower() for w in line.split()]
    for word in words:
        word_summary[word].append(idx)

    print(word_summary)
输出：
defaultdict(<class 'list'>, {'#': [0, 1, 2, 3, 4, 5], 'user': [0], 'database': [0], 'note': [2], 'that': [2], 'this': [2, 3], 'file': [2], 'is': [2, 2, 3], 'consulted': [2], 'directly': [2], 'only': [2], 'when': [2], 'the': [2], 'system': [2], 'running': [2], 'in': [3], 'single-user': [3], 'mode.': [3], 'at': [3], 'other': [3], 'times,': [3], 'information': [3], 'provided': [3], 'by': [3], 'open': [4], 'directory.': [4], '##': [6], 'nobody:*:-2:-2:unprivileged': [7], 'user:/var/empty:/usr/bin/false': [7], 'root:*:0:0:system': [8], 'administrator:/var/root:/bin/sh': [8]})
```

如果你处理完文件后打印 word_summary ，会发现它是一个字典(准确来讲是一个 defaultdict )， 对于每个单词有一个 key ，每个 key 对应的值是一个由这个单词出现的行号组成的列表。 如果某个单词在一行中出现过两次，那么这个行号也会出现两次， 同时也可以作为文本的一个简单统计。

当你想额外定义一个计数变量的时候，使用 enumerate() 函数会更加简单。你可能会像下面这样写代码：

```python
lineno = 1
for line in f:
    # Process line
    ...
    lineno += 1
```

但是如果使用 enumerate() 函数来代替就显得更加优雅了：

```python
for lineno, line in enumerate(f):
    # Process line
    ...
```

enumerate() 函数返回的是一个 enumerate 对象实例， 它是一个迭代器，返回连续的包含**一个计数和一个值**的元组， 元组中的值通过在传入序列上调用 next() 返回。

还有一点可能并不很重要，但是也值得注意， 有时候当你在一个已经解压后的元组序列上使用 enumerate() 函数时很容易调入陷阱。 你得像下面正确的方式这样写：

```python
data = [(1, 2), (3, 4), (5, 6), (7, 8)]

# Correct!
for n, (x, y) in enumerate(data):
    print(f'pos: {n}, value: {x, y}')
# Error!
for n, x, y in enumerate(data):
    pass
输出：
pos: 0, value: (1, 2)
pos: 1, value: (3, 4)
pos: 2, value: (5, 6)
pos: 3, value: (7, 8)

Traceback (most recent call last):
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 12, in <module>
    for n, x, y in enumerate(data):
ValueError: not enough values to unpack (expected 3, got 2)


# 以上样例：
t = (1, (2, 3))
x, (y, z) = t  # 错误的解压方式 x, y, z = t
```

## 4.11. 同时迭代多个序列

你想同时迭代多个序列，每次分别从一个序列中取一个元素。

为了同时迭代多个序列，使用 zip() 函数。比如：

```python
>>> xpts = [1, 5, 4, 2, 10, 7]
>>> ypts = [101, 78, 37, 15, 62, 99]
>>> for x, y in zip(xpts, ypts):
...     print(x,y)
...
1 101
5 78
4 37
2 15
10 62
7 99
```

zip(a, b) 会生成一个可返回元组 (x, y) 的迭代器，其中x来自a，y来自b。 一旦其中某个序列到底结尾，迭代宣告结束。 因此迭代长度跟参数中最短序列长度一致。

```python
>>> a = [1, 2, 3]
>>> b = ['w', 'x', 'y', 'z']
>>> for i in zip(a,b):
...     print(i)
...
(1, 'w')
(2, 'x')
(3, 'y')
```

如果这个不是你想要的效果，那么还可以使用 itertools.zip_longest() 函数来代替。比如：

```python
>>> from itertools import zip_longest
>>> for i in zip_longest(a,b):
...     print(i)
...
(1, 'w')
(2, 'x')
(3, 'y')
(None, 'z')

>>> for i in zip_longest(a, b, fillvalue=0):
...     print(i)
...
(1, 'w')
(2, 'x')
(3, 'y')
(0, 'z')
```

当你想成对处理数据的时候 zip() 函数是很有用的。 比如，假设你头列表和一个值列表，就像下面这样：

```python
headers = ['name', 'shares', 'price']
values = ['ACME', 100, 490.1]
```

使用zip()可以让你将它们打包并生成一个字典：

```python
s = dict(zip(headers,values))

# 可以如下方式构造dict
d = dict((('a', 1), ('b', 2), ('c', 3)))
print(d)  # {'a': 1, 'b': 2, 'c': 3}
```

或者你也可以像下面这样产生输出：

```python
for name, val in zip(headers, values):
    print(name, '=', val)
```

虽然不常见，但是 zip() 可以接受多于两个的序列的参数。 这时候所生成的结果元组中元素个数跟输入序列个数一样。比如;

```python
>>> a = [1, 2, 3]
>>> b = [10, 11, 12]
>>> c = ['x','y','z']
>>> for i in zip(a, b, c):
...     print(i)
...
(1, 10, 'x')
(2, 11, 'y')
(3, 12, 'z')
```

最后强调一点就是， zip() 会创建一个迭代器来作为结果返回。 如果你需要将结对的值存储在列表中，要使用 list() 函数。比如：

```python
>>> zip(a, b)
<zip object at 0x1007001b8>
>>> list(zip(a, b))
[(1, 10), (2, 11), (3, 12)]
```

## 4.12. 不同集合上元素的迭代

你想在多个对象执行相同的操作，但是这些对象在不同的容器中，你希望代码在不失可读性的情况下避免写重复的循环。

解决方案
itertools.chain() 方法可以用来简化这个任务。 它接受一个可迭代对象列表作为输入，并返回一个迭代器，有效的屏蔽掉在多个容器中迭代细节。 为了演示清楚，考虑下面这个例子：

```python
>>> from itertools import chain
>>> a = [1, 2, 3, 4]
>>> b = ['x', 'y', 'z']
>>> for x in chain(a, b):
... print(x)
...
1
2
3
4
x
y
z
```

使用 chain() 的一个常见场景是当你想对不同的集合中所有元素执行某些操作的时候。比如：

```python
# Various working sets of items
active_items = set()
inactive_items = set()

# Iterate over all items
for item in chain(active_items, inactive_items):
    # Process item
```
    
这种解决方案要比像下面这样使用两个单独的循环更加优雅，

```python
for item in active_items:
    # Process item
    ...

for item in inactive_items:
    # Process item
    ...
```

itertools.chain() 接受一个或多个可迭代对象作为输入参数。 然后创建一个迭代器，依次连续的返回每个可迭代对象中的元素。 这种方式要比先将序列合并再迭代要高效的多。比如：

```python
# Inefficent
for x in a + b:
    ...

# Better
for x in chain(a, b):
    ...
```

第一种方案中， a + b 操作会创建一个全新的序列并要求a和b的类型一致。 chian() 不会有这一步，所以如果输入序列非常大的时候会很省内存。 并且当可迭代对象类型不一样的时候 chain() 同样可以很好的工作。

```python
from itertools import chain

if __name__ == '__main__':
    l = [1, 2, 3]
    s = set([4, 5, 6])
    t = (7, 8, 9)
    # print(l + s)  # TypeError: can only concatenate list (not "set") to list
    # print(l + t)  # TypeError: can only concatenate list (not "tuple") to list
    print(chain(l, s, t))
    for x in chain(l, s, t):
        print(x)
输出：
1
2
3
4
5
6
7
8
9
```

## 4.13. 创建数据处理管道

<https://python3-cookbook.readthedocs.io/zh_CN/latest/c04/p13_create_data_processing_pipelines.html>

你想以数据管道(类似Unix管道)的方式迭代处理数据。 比如，你有个大量的数据需要处理，但是不能将它们一次性放入内存中。

生成器函数是一个实现管道机制的好办法。 为了演示，假定你要处理一个非常大的日志文件目录：

```python
log/
    foo/
        log-a.txt
        log-b.txt
        log-c.txt
    bar/
        log-d.txt
        log-e.txt
```

假设每个日志文件包含这样的数据：

```python
# log-a.txt
Hello, a
How are you, Python a?
Fine, thank you. And you?

# log-b.txt
Hello, b
How are you, python b?
Fine, thank you. And you?

# log-c.txt
Hello, c
How are you
Fine, thank you. And you?

# log-d.txt
Hello, d
How are you
Fine, thank you. And you?

# log-e.txt
Hello, e
How are you, python e?
Fine, thank you. And you?
```

为了处理这些文件，你可以定义一个由多个执行特定任务独立任务的简单生成器函数组成的容器。就像这样：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import fnmatch
import os
import re


def gen_find(file_pat, top):
    """
    Find all filenames in a directory tree that match a shell wildcard pattern
    """
    for path, dir_list, file_list in os.walk(top):
        for name in fnmatch.filter(file_list, file_pat):
            yield os.path.join(path, name)


def gen_opener(file_names):
    """
    Open a sequence of filenames one at a time producing a file object.
    The file is closed immediately when proceeding to the next iteration.
    """
    for file_name in file_names:
        with open(file_name) as f:
            yield f


def gen_concatenate(iterators):
    """
    Chain a sequence of iterators together into a single sequence.
    """
    for it in iterators:
        yield from it  # 每一次yield from it，将获得f中的一行
        # 同下
        # for line in it:
        #     yield line


def gen_grep(pattern, lines):
    """
    Look for a regex pattern in a sequence of lines
    """
    pat = re.compile(pattern)
    for line in lines:
        if pat.search(line):
            yield line
```

现在你可以很容易的将这些函数连起来创建一个处理管道。 比如，为了查找包含单词python的所有日志行，你可以这样做：

```python
if __name__ == '__main__':
    log_names = gen_find('log*', 'log')
    files = gen_opener(log_names)
    lines = gen_concatenate(files)
    pylines = gen_grep('(?i)python', lines)  # (?i)忽略大小写
    for line in pylines:
        print(line, end='')
输出：
How are you, Python a?
How are you, python b?
How are you, python e?
```

如果将来的时候你想扩展管道，你甚至可以在生成器表达式中包装数据。 比如，下面这个版本计算出传输的字节数并计算其总和。

```python
log_names = gen_find('log*', 'log')
files = gen_opener(log_names)
lines = gen_concatenate(files)
pylines = gen_grep('(?i)python', lines)  # (?i)忽略大小写
lines_length = (len(line.strip()) for line in pylines)
print('Total length: ', sum(lines_length))
输出：
Total length:  66
```

以管道方式处理数据可以用来解决各类其他问题，包括解析，读取实时数据，定时轮询等。

为了理解上述代码，重点是要明白 yield 语句作为数据的生产者而 for 循环语句作为数据的消费者。 当这些生成器被连在一起后，每个 yield 会将一个单独的数据元素传递给迭代处理管道的下一阶段。 在例子最后部分， sum() 函数是最终的程序驱动者，每次从生成器管道中提取出一个元素。

这种方式一个非常好的特点是每个生成器函数很小并且都是独立的。这样的话就很容易编写和维护它们了。 很多时候，这些函数如果比较通用的话可以在其他场景重复使用。 并且最终将这些组件组合起来的代码看上去非常简单，也很容易理解。

使用这种方式的内存效率也不得不提。上述代码即便是在一个超大型文件目录中也能工作的很好。 事实上，由于使用了迭代方式处理，代码运行过程中只需要很小很小的内存。

在调用 gen_concatenate() 函数的时候你可能会有些不太明白。 这个函数的目的是将输入序列拼接成一个很长的行序列。 itertools.chain() 函数同样有类似的功能，但是它需要将所有可迭代对象最为参数传入。 在上面这个例子中，你可能会写类似这样的语句 lines = itertools.chain(*files) ， 这将导致 gen_opener() 生成器被提前全部消费掉。 但由于 gen_opener() 生成器每次生成一个打开过的文件， 等到下一个迭代步骤时文件就关闭了，因此 chain() 在这里不能这样使用。 上面的方案可以避免这种情况。

gen_concatenate() 函数中出现过 yield from 语句，它将 yield 操作代理到父生成器上去。 语句 yield from it 简单的返回生成器 it 所产生的所有值。 关于这个我们在4.14小节会有更进一步的描述。

最后还有一点需要注意的是，管道方式并不是万能的。 有时候你想立即处理所有数据。 然而，即便是这种情况，使用生成器管道也可以将这类问题从逻辑上变为工作流的处理方式。

如可以使用list()，消费生成出全部txt内容：

```python
log_names = gen_find('log*', 'log')
files = gen_opener(log_names)
lines = gen_concatenate(files)
print(list(lines))
输出：
['Hello, a\n', 'How are you, Python a?\n', 'Fine, thank you. And you?', 'Hello, b\n', 'How are you, python b?\n', 'Fine, thank you. And you?', 'Hello, c\n', 'How are you\n', 'Fine, thank you. And you?', 'Hello, d\n', 'How are you\n', 'Fine, thank you. And you?', 'Hello, e\n', 'How are you, python e?\n', 'Fine, thank you. And you?']

```

David Beazley 在他的 Generator Tricks for Systems Programmers 教程中对于这种技术有非常深入的讲解。可以参考这个教程获取更多的信息。

## 4.14. 展开嵌套的序列

你想将一个多层嵌套的序列展开成一个单层列表

可以写一个包含 yield from 语句的递归生成器来轻松解决这个问题。比如：

```python
from collections.abc import Iterable


def flatten(items, ignore_types=(str, bytes)):
    for x in items:
        if isinstance(x, Iterable) and not isinstance(x, ignore_types):
            yield from flatten(x)
        else:
            yield x


if __name__ == '__main__':
    items = [1, 2, [3, 4, [5, 6], 7], 8]
    # Produces 1 2 3 4 5 6 7 8
    for x in flatten(items):
        print(x)
输出：
1
2
3
4
5
6
7
8
```

在上面代码中， isinstance(x, Iterable) 检查某个元素是否是可迭代的。 如果是的话， yield from 就会返回所有子例程的值。最终返回结果就是一个没有嵌套的简单序列了。

额外的参数 ignore_types 和检测语句 isinstance(x, ignore_types) 用来将字符串和字节排除在可迭代对象外，防止将它们再展开成单个的字符。 这样的话字符串数组就能最终返回我们所期望的结果了。比如：

```python
>>> items = ['Dave', 'Paula', ['Thomas', 'Lewis']]
>>> for x in flatten(items):
...     print(x)
...
Dave
Paula
Thomas
Lewis
```

语句 yield from 在你想在生成器中调用其他生成器作为子例程的时候非常有用。 如果你不使用它的话，那么就必须写额外的 for 循环了。比如：

```python
def flatten(items, ignore_types=(str, bytes)):
    for x in items:
        if isinstance(x, Iterable) and not isinstance(x, ignore_types):
            for i in flatten(x):
                yield i
        else:
            yield x
```

尽管只改了一点点，但是 yield from 语句看上去感觉更好，并且也使得代码更简洁清爽。

之前提到的对于字符串和字节的额外检查是为了防止将它们再展开成单个字符。 如果还有其他你不想展开的类型，修改参数 ignore_types 即可。

最后要注意的一点是， yield from 在涉及到基于协程和生成器的并发编程中扮演着更加重要的角色。 可以参考12.12小节查看另外一个例子。

## 4.15. 顺序迭代合并后的排序迭代

你有一系列排序序列，想将它们合并后得到一个排序序列并在上面迭代遍历。

heapq.merge() 函数可以帮你解决这个问题。比如：

```python
import heapq

if __name__ == '__main__':
    a = [1, 4, 7, 10]
    b = [2, 5, 6, 11]
    for c in heapq.merge(a, b):
        print(c)
输出：
1
2
4
5
6
7
10
11
```

heapq.merge 可迭代特性意味着它不会立马读取所有序列。 这就意味着你可以在非常长的序列中使用它，而不会有太大的开销。 比如，下面是一个例子来演示如何合并两个排序文件：

```python
import heapq

if __name__ == '__main__':
    with open('sorted_file_1.txt', 'rt') as file1, open('sorted_file_2.txt', 'rt') as file2, open('merged_file.txt', 'wt') as outf:
        for line in heapq.merge(file1, file2):
            outf.write(line)
```

有一点要强调的是 heapq.merge() 需要所有输入序列必须是排过序的。 特别的，它并不会预先读取所有数据到堆栈中或者预先排序，也不会对输入做任何的排序检测。 它仅仅是检查所有序列的开始部分并返回最小的那个，这个过程一直会持续直到所有输入序列中的元素都被遍历完。

## 4.16. 迭代器代替while无限循环

你在代码中使用 while 循环来迭代处理数据，因为它需要调用某个函数或者和一般迭代模式不同的测试条件。 能不能用迭代器来重写这个循环呢？

一个常见的IO操作程序可能会想下面这样：

```python
CHUNKSIZE = 8192

def reader(s):
    while True:
        data = s.recv(CHUNKSIZE)
        if data == b'':
            break
        process_data(data)
```

这种代码通常可以使用 iter() 来代替，如下所示：

```python
def reader2(s):
    for chunk in iter(lambda: s.recv(CHUNKSIZE), b''):
        pass
        # process_data(data)
```

如果你怀疑它到底能不能正常工作，可以试验下一个简单的例子。比如：

```python
import sys

if __name__ == '__main__':
    f = open('test.txt')
    for chunk in iter(lambda: f.read(10), ''):
        n = sys.stdout.write(chunk)  # print(chunk, end='')
输出：
nobody:*:-2:-2:Unprivileged User:/var/empty:/usr/bin/false
root:*:0:0:System Administrator:/var/root:/bin/sh
daemon:*:1:1:System Services:/var/root:/usr/bin/false
_uucp:*:4:4:Unix to Unix Copy Protocol:/var/spool/uucp:/usr/sbin/uucico
```

iter 函数一个鲜为人知的特性是它接受一个可选的 callable 对象和一个标记(结尾)值作为输入参数。 当以这种方式使用的时候，它会创建一个迭代器， 这个迭代器会不断调用 callable 对象直到返回值和标记值相等为止。

这种特殊的方法对于一些特定的会被重复调用的函数很有效果，比如涉及到I/O调用的函数。 举例来讲，如果你想从套接字或文件中以数据块的方式读取数据，通常你得要不断重复的执行 read() 或 recv() ， 并在后面紧跟一个文件结尾测试来决定是否终止。这节中的方案使用一个简单的 iter() 调用就可以将两者结合起来了。 其中 lambda 函数参数是为了创建一个无参的 callable 对象，并为 recv 或 read() 方法提供了 size 参数。

# 5. 文件与IO

## 5.1. 读写文本数据

你需要读写各种不同编码的文本数据，比如ASCII，UTF-8或UTF-16编码等。

使用带有 rt 模式的 open() 函数读取文本文件。如下所示：

```python
# Read the entire file as a single string
with open('test.txt', 'rt') as f:
    data = f.read()
    print(data)

# Iterate over the lines of the file
with open('somefile.txt', 'rt') as f:
    for line in f:
        # process line
        ...
```

如果是在已存在文件中添加内容，使用模式为 at 的 open() 函数。

文件的读写操作默认使用系统编码，可以通过调用 sys.getdefaultencoding() 来得到。 在大多数机器上面都是utf-8编码。如果你已经知道你要读写的文本是其他编码方式， 那么可以通过传递一个可选的 encoding 参数给open()函数。如下所示：

```python
with open('somefile.txt', 'rt', encoding='latin-1') as f:
    ...
```

本地测试，但是open函数不加encoding函数，输出是乱码：

```python
print(sys.getdefaultencoding())
with open('test.txt', 'rt', encoding='utf-8') as f:
    data = f.read()
    print(data)
输出：
utf-8
你好
```

Python支持非常多的文本编码。几个常见的编码是ascii, latin-1, utf-8和utf-16。 在web应用程序中通常都使用的是UTF-8。 ascii对应从U+0000到U+007F范围内的7位字符。 latin-1是字节0-255到U+0000至U+00FF范围内Unicode字符的直接映射。 当读取一个未知编码的文本时使用latin-1编码永远不会产生解码错误。 使用latin-1编码读取一个文件的时候也许不能产生完全正确的文本解码数据， 但是它也能从中提取出足够多的有用数据。同时，如果你之后将数据回写回去，原先的数据还是会保留的。

读写文本文件一般来讲是比较简单的。但是也几点是需要注意的。 首先，在例子程序中的with语句给被使用到的文件创建了一个上下文环境， 但 with 控制块结束时，文件会自动关闭。你也可以不使用 with 语句，但是这时候你就必须记得手动关闭文件：

```python
f = open('somefile.txt', 'rt')
data = f.read()
f.close()
```

另外一个问题是关于换行符的识别问题，在Unix和Windows中是不一样的(分别是 \n 和 \r\n )。 默认情况下，Python会以统一模式处理换行符。 这种模式下，在读取文本的时候，Python可以识别所有的普通换行符并将其转换为单个 \n 字符。 类似的，在输出时会将换行符 \n 转换为系统默认的换行符。 如果你不希望这种默认的处理方式，可以给 open() 函数传入参数 newline='' ，就像下面这样：

```python
# Read with disabled newline translation
with open('somefile.txt', 'rt', newline='') as f:
    ...
```

为了说明两者之间的差异，下面我在Unix机器上面读取一个Windows上面的文本文件，里面的内容是 hello world!\r\n ：

```python
>>> # Newline translation enabled (the default)
>>> f = open('hello.txt', 'rt')
>>> f.read()
'hello world!\n'

>>> # Newline translation disabled
>>> g = open('hello.txt', 'rt', newline='')
>>> g.read()
'hello world!\r\n'
```

最后一个问题就是文本文件中可能出现的编码错误。 但你读取或者写入一个文本文件时，你可能会遇到一个编码或者解码错误。比如：

```python
>>> f = open('sample.txt', 'rt', encoding='ascii')
>>> f.read()
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
    File "/usr/local/lib/python3.3/encodings/ascii.py", line 26, in decode
        return codecs.ascii_decode(input, self.errors)[0]
UnicodeDecodeError: 'ascii' codec can't decode byte 0xc3 in position
12: ordinal not in range(128)
```

如果出现这个错误，通常表示你读取文本时指定的编码不正确。 你最好仔细阅读说明并确认你的文件编码是正确的(比如使用UTF-8而不是Latin-1编码或其他)。 如果编码错误还是存在的话，你可以给 open() 函数传递一个可选的 errors 参数来处理这些错误。 下面是一些处理常见错误的方法：

```python
>>> # Replace bad chars with Unicode U+fffd replacement char
>>> f = open('sample.txt', 'rt', encoding='ascii', errors='replace')
>>> f.read()
'Spicy Jalape?o!'
>>> # Ignore bad chars entirely
>>> g = open('sample.txt', 'rt', encoding='ascii', errors='ignore')
>>> g.read()
'Spicy Jalapeo!'
```

如果你经常使用 errors 参数来处理编码错误，可能会让你的生活变得很糟糕。 对于文本处理的首要原则是确保你总是使用的是正确编码。当模棱两可的时候，就使用默认的设置(通常都是UTF-8)。

## 5.2. 打印输出至文件中

你想将 print() 函数的输出重定向到一个文件中去。

在 print() 函数中指定 file 关键字参数，像下面这样：

```python
with open('d:/work/test.txt', 'wt') as f:
    print('Hello World!', file=f)
```

关于输出重定向到文件中就这些了。但是有一点要注意的就是文件必须是以文本模式打开。 如果文件是二进制模式的话，打印就会出错。

```python
with open('test.txt', 'wb') as f:
    print(b'hello, world', file=f)
报错：
Traceback (most recent call last):
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 7, in <module>
    print(b'hello, world', file=f)
TypeError: a bytes-like object is required, not 'str'
```

## 5.3. 使用其他分隔符或行终止符打印

你想使用 print() 函数输出数据，但是想改变默认的分隔符或者行尾符。

可以使用在 print() 函数中使用 sep 和 end 关键字参数，以你想要的方式输出。比如：

```python
print('ACME', 50, 91.5)
print('ACME', 50, 91.5, sep=',')
print('ACME', 50, 91.5, sep=',', end='!!\n')
输出：
ACME 50 91.5
ACME,50,91.5
ACME,50,91.5!!
```

使用 end 参数也可以在输出中禁止换行。比如：

```python
>>> for i in range(5):
...     print(i)
...
0
1
2
3
4
>>> for i in range(5):
...     print(i, end=' ')
...
0 1 2 3 4 >>>
```

当你想使用非空格分隔符来输出数据的时候，给 print() 函数传递一个 sep 参数是最简单的方案。 有时候你会看到一些程序员会使用 str.join() 来完成同样的事情。比如：

```python
>>> print(','.join(('ACME','50','91.5')))
ACME,50,91.5
```

str.join() 的问题在于它仅仅适用于字符串。这意味着你通常需要执行另外一些转换才能让它正常工作。比如：

```python
>>> row = ('ACME', 50, 91.5)
>>> print(','.join(row))
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
TypeError: sequence item 1: expected str instance, int found
>>> print(','.join(str(x) for x in row))
ACME,50,91.5
```

你当然可以不用那么麻烦，只需要像下面这样写：

```python
>>> print(*row, sep=',')
ACME,50,91.5
```

## 5.4. 读写字节数据

你想读写二进制文件，比如图片，声音文件等等。

使用模式为 rb 或 wb 的 open() 函数来读取或写入二进制数据。比如：

```python
# Read the entire file as a single byte string
with open('somefile.bin', 'rb') as f:
    data = f.read()

# Write binary data to a file
with open('somefile.bin', 'wb') as f:
    f.write(b'Hello World')
```

在读取二进制数据时，需要指明的是所有返回的数据都是字节字符串格式的，而不是文本字符串。 类似的，在写入的时候，必须保证参数是以字节形式对外暴露数据的对象(比如字节字符串，字节数组对象等)。

在读取二进制数据的时候，字节字符串和文本字符串的语义差异可能会导致一个潜在的陷阱。 特别需要注意的是，索引和迭代动作返回的是字节的值而不是字节字符串。比如：

```python
>>> # Text string
>>> t = 'Hello World'
>>> t[0]
'H'
>>> for c in t:
...     print(c)
...
H
e
l
l
o
...
>>> # Byte string
>>> b = b'Hello World'
>>> b[0]
72
>>> for c in b:
...     print(c)
...
72
101
108
108
111
```

如果你想从二进制模式的文件中读取或写入文本数据，必须确保要进行解码和编码操作。比如：

```python
with open('somefile.bin', 'rb') as f:
    data = f.read(16)
    text = data.decode('utf-8')  # 把bytes转成str

with open('somefile.bin', 'wb') as f:
    text = 'Hello World'
    f.write(text.encode('utf-8'))  # 把str转成bytes
```

二进制I/O还有一个鲜为人知的特性就是数组和C结构体类型能直接被写入，而不需要中间转换为自己对象。比如：

```python
import array
nums = array.array('i', [1, 2, 3, 4])
with open('data.bin','wb') as f:
    f.write(nums)
```

这个适用于任何实现了被称之为”缓冲接口”的对象，这种对象会直接暴露其底层的内存缓冲区给能处理它的操作。 二进制数据的写入就是这类操作之一。

很多对象还允许通过使用文件对象的 readinto() 方法直接读取二进制数据到其底层的内存中去。比如：

```python
import array

if __name__ == '__main__':
    nums = array.array('i', [1, 2, 3, 4])
    with open('test.txt', 'wb') as f:
        f.write(nums)

    a = array.array('i', [0, 0, 0, 0, 0, 0, 0])
    print(a)
    with open('test.txt', 'rb') as f:
        f.readinto(a)
    print(a)
输出：
array('i', [0, 0, 0, 0, 0, 0, 0])
array('i', [1, 2, 3, 4, 0, 0, 0])
```

但是使用这种技术的时候需要格外小心，因为它通常具有平台相关性，并且可能会依赖字长和字节顺序(高位优先和低位优先)。 可以查看5.9小节中另外一个读取二进制数据到可修改缓冲区的例子。

## 5.5. 文件不存在才能写入

你想像一个文件中写入数据，但是前提必须是这个文件在文件系统上不存在。 也就是不允许覆盖已存在的文件内容。

可以在 open() 函数中使用 x 模式来代替 w 模式的方法来解决这个问题。比如：

```python
with open('test.txt', 'wt') as f:
    f.write('Hello')

with open('test.txt', 'xt') as f:
    f.write('Hello')
```

如果文件是二进制的，使用 xb 来代替 xt。

这一小节演示了在写文件时通常会遇到的一个问题的完美解决方案(不小心覆盖一个已存在的文件)。 一个替代方案是先测试这个文件是否存在，像下面这样：

```python
if not os.path.exists('test.txt'):
    with open('test.txt', 'wt') as f:
        f.write('Hello')
else:
    print('File already exists!')
```

显而易见，使用x文件模式更加简单。要注意的是x模式是一个Python3对 open() 函数特有的扩展。 在Python的旧版本或者是Python实现的底层C函数库中都是没有这个模式的。

## 5.6. 字符串的I/O操作

你想使用操作类文件对象的程序来操作文本或二进制字符串。

使用 io.StringIO() 和 io.BytesIO() 类来创建类文件对象操作字符串数据。比如：

```python
>>> s = io.StringIO()
>>> s.write('Hello World\n')
12
>>> print('This is a test', file=s)
15
>>> # Get all of the data written so far
>>> s.getvalue()
'Hello World\nThis is a test\n'
>>>

>>> # Wrap a file interface around an existing string
>>> s = io.StringIO('Hello\nWorld\n')
>>> s.read(4)
'Hell'
>>> s.read()
'o\nWorld\n'
```

可以像读文件一样，读取io.StringIO对象：

```python
import io


def get_file_content(f):
    return f.read()


if __name__ == '__main__':
    # Wrap a file interface around an existing string
    s = io.StringIO('Hello\nWorld\n')
    print(get_file_content(s))
输出：
Hello
World
```

io.StringIO 只能用于文本。如果你要操作二进制数据，要使用 io.BytesIO 类来代替。比如：

```python
>>> s = io.BytesIO()
>>> s.write(b'binary data')
>>> s.getvalue()
b'binary data'
```

当你想模拟一个普通的文件的时候 StringIO 和 BytesIO 类是很有用的。 比如，在单元测试中，你可以使用 StringIO 来创建一个包含测试数据的类文件对象， 这个对象可以被传给某个参数为普通文件对象的函数。

需要注意的是， StringIO 和 BytesIO 实例并没有正确的整数类型的文件描述符。 因此，它们不能在那些需要使用真实的系统级文件如文件，管道或者是套接字的程序中使用。

## 5.7. 读写压缩文件

你想读写一个gzip或bz2格式的压缩文件。

gzip 和 bz2 模块可以很容易的处理这些文件。 两个模块都为 open() 函数提供了另外的实现来解决这个问题。 比如，为了以文本形式读取压缩文件，可以这样做：

```python
# gzip compression
import gzip
with gzip.open('somefile.gz', 'rt') as f:
    text = f.read()

# bz2 compression
import bz2
with bz2.open('somefile.bz2', 'rt') as f:
    text = f.read()
```

类似的，为了写入压缩数据，可以这样做：

```python
# gzip compression
import gzip
with gzip.open('somefile.gz', 'wt') as f:
    f.write(text)

# bz2 compression
import bz2
with bz2.open('somefile.bz2', 'wt') as f:
    f.write(text)
```

如上，所有的I/O操作都使用文本模式并执行Unicode的编码/解码。 类似的，如果你想操作二进制数据，使用 rb 或者 wb 文件模式即可。

大部分情况下读写压缩数据都是很简单的。但是要注意的是选择一个正确的文件模式是非常重要的。 如果你不指定模式，那么默认的就是二进制模式，如果这时候程序想要接受的是文本数据，那么就会出错。 gzip.open() 和 bz2.open() 接受跟内置的 open() 函数一样的参数， 包括 encoding，errors，newline 等等。

当写入压缩数据时，可以使用 compresslevel 这个可选的关键字参数来指定一个压缩级别。比如：

```python
with gzip.open('somefile.gz', 'wt', compresslevel=5) as f:
    f.write(text)
```

默认的等级是9，也是最高的压缩等级。等级越低性能越好，但是数据压缩程度也越低。

最后一点， gzip.open() 和 bz2.open() 还有一个很少被知道的特性， 它们可以作用在一个已存在并以二进制模式打开的文件上。比如，下面代码是可行的：

```python
import gzip
f = open('somefile.gz', 'rb')
with gzip.open(f, 'rt') as g:
    text = g.read()
```

这样就允许 gzip 和 bz2 模块可以工作在许多类文件对象上，比如套接字，管道和内存中文件等。

## 5.8. 固定大小记录的文件迭代

你想在一个固定长度记录或者数据块的集合上迭代，而不是在一个文件中一行一行的迭代。

通过下面这个小技巧使用 iter 和 functools.partial() 函数：

```python
from functools import partial

RECORD_SIZE = 5

if __name__ == '__main__':
    with open('test.txt', 'rb') as f:
        records = iter(partial(f.read, RECORD_SIZE), b'')
        # records = iter(lambda: f.read(RECORD_SIZE), b'')  # 该写法效果同上
        for r in records:
            print(r)
输出：
b'Hello'
b' Worl'
b'd\r\nGo'
b'od lu'
b'ck!'
```

这个例子中的 records 对象是一个可迭代对象，它会不断的产生固定大小的数据块，直到文件末尾。 要注意的是如果总记录大小不是块大小的整数倍的话，最后一个返回元素的字节数会比期望值少。

注：functools.partial函数为偏函数，把参数绑定到函数上，例：

```python
from functools import partial

if __name__ == '__main__':
    p = partial(print, 'hello world')
    p()
    p('Good luck', sep=',')
输出：
hello world
hello world,Good luck
```

## 5.9. 读取二进制数据到可变缓冲区中

你想直接读取二进制数据到一个可变缓冲区中，而不需要做任何的中间复制操作。 或者你想原地修改数据并将它写回到一个文件中去。

为了读取数据到一个可变数组中，使用文件对象的 readinto() 方法。比如：

```python
import os.path


def read_into_buffer(filename):
    buf = bytearray(os.path.getsize(filename))
    with open(filename, 'rb') as f:
        f.readinto(buf)
    return buf


if __name__ == '__main__':
    buf = read_into_buffer('test.txt')
    print(buf)

    buf[0:5] = b'ABCDE'
    print(buf)

    with open('new_test.txt', 'wb') as f:
        f.write(buf)

# test.txt
Hello World
Good luck!

输出：
bytearray(b'Hello World\r\nGood luck!')
bytearray(b'ABCDE World\r\nGood luck!')

# new_test.txt
ABCDE World
Good luck!
```

文件对象的 readinto() 方法能被用来为预先分配内存的数组填充数据，甚至包括由 array 模块或 numpy 库创建的数组。 和普通 read() 方法不同的是， readinto() 填充已存在的缓冲区而不是为新对象重新分配内存再返回它们。 因此，你可以使用它来避免大量的内存分配操作。 比如，如果你读取一个由相同大小的记录组成的二进制文件时，你可以像下面这样写：

```python
record_size = 5  # Size of each record (adjust value)

if __name__ == '__main__':
    buf = bytearray(record_size)
    with open('test.txt', 'rb') as f:
        while True:
            n = f.readinto(buf)
            if n < record_size:
                print(buf)
                break
            # Use the contents of buf
            print(buf)
输出：
bytearray(b'Hello')
bytearray(b' Worl')
bytearray(b'd\r\nGo')
bytearray(b'od lu')
bytearray(b'ck!lu')  # 最后只填充了3位，所有lu还是上一次的od lu的最后2位
```

另外有一个有趣特性就是 memoryview ， 它可以通过零复制的方式对已存在的缓冲区执行切片操作，甚至还能修改它的内容。比如：

```python
buf = read_into_buffer('test.txt')
print(buf)

buf[0:5] = b'ABCDE'  # 可以直接对buf做切片操作
buf[-5:] = b'DEBUG'
print(buf)

m1 = memoryview(buf)
m2 = m1[-5:]
m2[:] = b'WORLD'
print(buf)
输出：
bytearray(b'Hello World\r\nGood luck!')
bytearray(b'ABCDE World\r\nGood DEBUG')
bytearray(b'ABCDE World\r\nGood WORLD')
```

使用 f.readinto() 时需要注意的是，你必须检查它的返回值，也就是实际读取的字节数。

如果字节数小于缓冲区大小，表明数据被截断或者被破坏了(比如你期望每次读取指定数量的字节)。

最后，留心观察其他函数库和模块中和 into 相关的函数(比如 recv_into() ， pack_into() 等)。 Python的很多其他部分已经能支持直接的I/O或数据访问操作，这些操作可被用来填充或修改数组和缓冲区内容。

关于解析二进制结构和 memoryviews 使用方法的更高级例子，请参考6.12小节。

## 5.10. 内存映射的二进制文件

你想内存映射一个二进制文件到一个可变字节数组中，目的可能是为了随机访问它的内容或者是原地做些修改。

使用 mmap 模块来内存映射文件。 下面是一个工具函数，向你演示了如何打开一个文件并以一种便捷方式内存映射这个文件。

```python
import mmap
import os


def memory_map(filen_name, access=mmap.ACCESS_WRITE):
    size = os.path.getsize(filen_name)
    fd = os.open(filen_name, os.O_RDWR)
    return mmap.mmap(fd, size, access=access)
```

为了使用这个函数，你需要有一个已创建并且内容不为空的文件。 下面是一个例子，教你怎样初始创建一个文件并将其内容扩充到指定大小：

```python
if __name__ == '__main__':
    size = 100
    with open('test.txt', 'wb') as f:
        f.seek(size - 1)
        f.write(b'\x00')
```

下面是一个利用 memory_map() 函数类内存映射文件内容的例子：

```python
>>> m = memory_map('data')
>>> len(m)
100
>>> m[0:10]
b'\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00'
>>> m[0]
0
>>> # Reassign a slice
>>> m[0:11] = b'Hello World'
>>> m.close()

>>> # Verify that changes were made
>>> with open('data', 'rb') as f:
... print(f.read(11))
...
b'Hello World'
```

mmap() 返回的 mmap 对象同样也可以作为一个上下文管理器来使用， 这时候底层的文件会被自动关闭。比如：

```python
with memory_map('test.txt') as m:
    print(len(m))
    print(m[0:11])

print(m.closed)
输出：
100
b'Hello World'
True
```

默认情况下， memeory_map() 函数打开的文件同时支持读和写操作。 任何的修改内容都会复制回原来的文件中。 如果需要只读的访问模式，可以给参数 access 赋值为 mmap.ACCESS_READ 。比如：

```python
m = memory_map(filename, mmap.ACCESS_READ)

# 以ACCESS_READ去写文件时，抛异常
with memory_map('test.txt', mmap.ACCESS_READ) as m:
    m[11:13] = b'OK'
    print(m[0:20])
输出：
Traceback (most recent call last):
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 15, in <module>
    m[11:13] = b'OK'
TypeError: mmap can't modify a readonly memory map.
```

如果你想在本地修改数据，但是又不想将修改写回到原始文件中，可以使用 mmap.ACCESS_COPY ：

```python
m = memory_map(filename, mmap.ACCESS_COPY)

# 使用样例
with memory_map('test.txt', mmap.ACCESS_COPY) as m:
    m[11:15] = b'Good'
    print(m[0:20])

with memory_map('test.txt', mmap.ACCESS_COPY) as m:
    print(m[0:20])
输出：
b'Hello WorldGood\x00\x00\x00\x00\x00'
b'Hello WorldOK\x00\x00\x00\x00\x00\x00\x00'
```

为了随机访问文件的内容，使用 mmap 将文件映射到内存中是一个高效和优雅的方法。 例如，你无需打开一个文件并执行大量的 seek() ， read() ， write() 调用， 只需要简单的映射文件并使用切片操作访问数据即可。

一般来讲， mmap() 所暴露的内存看上去就是一个二进制数组对象。 但是，你可以使用一个内存视图来解析其中的数据。比如：

```python
m = memory_map('test.txt')
v = memoryview(m).cast('I')
m[0:4] = b'\x00\x00\x00\x00'
print(v[0])

v[0] = 7
print(v[0])

print(m[0:4])
m[0:4] = b'\x07\x01\x00\x00'
print(v[0])
输出：
0
7
b'\x07\x00\x00\x00'
263
```

需要强调的一点是，内存映射一个文件并不会导致整个文件被读取到内存中。 也就是说，文件并没有被复制到内存缓存或数组中。相反，操作系统仅仅为文件内容保留了一段虚拟内存。 当你访问文件的不同区域时，这些区域的内容才根据需要被读取并映射到内存区域中。 而那些从没被访问到的部分还是留在磁盘上。所有这些过程是透明的，在幕后完成！

如果多个Python解释器内存映射同一个文件，得到的 mmap 对象能够被用来在解释器直接交换数据。 也就是说，所有解释器都能同时读写数据，并且其中一个解释器所做的修改会自动呈现在其他解释器中。 很明显，这里需要考虑同步的问题。但是这种方法有时候可以用来在管道或套接字间传递数据。

这一小节中函数尽量写得很通用，同时适用于Unix和Windows平台。 要注意的是使用 mmap() 函数时会在底层有一些平台的差异性。 另外，还有一些选项可以用来创建匿名的内存映射区域。 如果你对这个感兴趣，确保你仔细研读了Python文档中 这方面的内容 。

## 5.11. 文件路径名的操作

你需要使用路径名来获取文件名，目录名，绝对路径等等。

使用 os.path 模块中的函数来操作路径名。 下面是一个交互式例子来演示一些关键的特性：

```python
import os


if __name__ == '__main__':
    path = r'D:\Program Files\JetBrains\PythonProject\Py3TestProject\src\test\test.txt'
    # Get the last component of the path
    print(os.path.basename(path))

    # Get the directory name
    print(os.path.dirname(path))

    # Join path components together
    print(os.path.join('abc', 'def', os.path.basename(path)))

    # Expand the user's home directory
    path = '~/Data/data.csv'
    print(os.path.expanduser(path))

    # Split the file extension
    print(os.path.splitext(path))
输出：
test.txt
D:\Program Files\JetBrains\PythonProject\Py3TestProject\src\test
abc\def\test.txt
C:\Users\Ben/Data/data.csv
('~/Data/data', '.csv')
```

对于任何的文件名的操作，你都应该使用 os.path 模块，而不是使用标准字符串操作来构造自己的代码。 特别是为了可移植性考虑的时候更应如此， 因为 os.path 模块知道Unix和Windows系统之间的差异并且能够可靠地处理类似 Data/data.csv 和 Data\data.csv 这样的文件名。 其次，你真的不应该浪费时间去重复造轮子。通常最好是直接使用已经为你准备好的功能。

要注意的是 os.path 还有更多的功能在这里并没有列举出来。 可以查阅官方文档来获取更多与文件测试，符号链接等相关的函数说明。

## 5.12. 测试文件是否存在

你想测试一个文件或目录是否存在。

使用 os.path 模块来测试一个文件或目录是否存在。比如：

```python
>>> import os
>>> os.path.exists('/etc/passwd')
True
>>> os.path.exists('/tmp/spam')
False
```

你还能进一步测试这个文件时什么类型的。 在下面这些测试中，如果测试的文件不存在的时候，结果都会返回False：

```python
>>> # Is a regular file
>>> os.path.isfile('/etc/passwd')
True

>>> # Is a directory
>>> os.path.isdir('/etc/passwd')
False

>>> # Is a symbolic link
>>> os.path.islink('/usr/local/bin/python3')
True

>>> # Get the file linked to
>>> os.path.realpath('/usr/local/bin/python3')
'/usr/local/bin/python3.3'
```

如果你还想获取元数据(比如文件大小或者是修改日期)，也可以使用 os.path 模块来解决：

```python
import os
import time

if __name__ == '__main__':
    file_path = r'D:\Program Files\JetBrains\PythonProject\Py3TestProject\src\test\test.txt'
    print(os.path.getsize(file_path))

    file_mtime = os.path.getmtime(file_path)
    print(file_mtime)

    print(time.ctime(file_mtime))
输出：
100
1559489044.3936353
Sun Jun  2 23:24:04 2019
```

使用 os.path 来进行文件测试是很简单的。 在写这些脚本时，可能唯一需要注意的就是你需要考虑文件权限的问题，特别是在获取元数据时候。比如：

```python
>>> os.path.getsize('/Users/guido/Desktop/foo.txt')
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
    File "/usr/local/lib/python3.3/genericpath.py", line 49, in getsize
        return os.stat(filename).st_size
PermissionError: [Errno 13] Permission denied: '/Users/guido/Desktop/foo.txt'
```

## 5.13. 获取文件夹中的文件列表

你想获取文件系统中某个目录下的所有文件列表。

使用 os.listdir() 函数来获取某个目录中的文件列表：

```python
import os

if __name__ == '__main__':
    names = os.listdir('.')
    print(names)
输出：
['dirA', 'main.py', 'test.txt', '__init__.py', '__pycache__']
```

结果会返回目录中所有文件列表，包括所有文件，子目录，符号链接等等。 如果你需要通过某种方式过滤数据，可以考虑结合 os.path 库中的一些函数来使用列表推导。比如：

```python
import os

if __name__ == '__main__':
    # Get all regular files
    names = [name for name in os.listdir('.') if os.path.isfile(os.path.join('.', name))]
    print(names)

    # Get all dirs
    dirnames = [name for name in os.listdir('.') if os.path.isdir(os.path.join('.', name))]
    print(dirnames)
输出：
['main.py', 'test.txt', '__init__.py']
['dirA', '__pycache__']
```

字符串的 startswith() 和 endswith() 方法对于过滤一个目录的内容也是很有用的。比如：

```python
import os

if __name__ == '__main__':
    pyfiles = [name for name in os.listdir('.') if name.endswith('.py')]
    print(pyfiles)
输出：
['main.py', '__init__.py']
```

对于文件名的匹配，你可能会考虑使用 glob 或 fnmatch 模块。比如：

```python
import glob
import os
from fnmatch import fnmatch

if __name__ == '__main__':
    pyfiles = glob.glob('./*py')
    print(pyfiles)

    pyfiles = [name for name in os.listdir('.') if fnmatch(name, '*.py')]
    print(pyfiles)
输出：
['.\\main.py', '.\\__init__.py']
['main.py', '__init__.py']
```

获取目录中的列表是很容易的，但是其返回结果只是目录中实体名列表而已。 如果你还想获取其他的元信息，比如文件大小，修改时间等等， 你或许还需要使用到 os.path 模块中的函数或着 os.stat() 函数来收集数据。比如：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import glob
import os
import time

if __name__ == '__main__':
    pyfiles = glob.glob('*.py')

    # Get file sizes and modification dates
    name_sz_date = [(name, os.path.getsize(name), os.path.getmtime(name)) for name in pyfiles]
    for name, size, mtime in name_sz_date:
        print(name, size, time.ctime(mtime))

    # Alternative: Get file metadata
    file_metadata = [(name, os.stat(name)) for name in pyfiles]
    for name, meta in file_metadata:
        print(name, meta.st_size, time.ctime(meta.st_mtime))
输出：
main.py 587 Wed Jun  5 23:05:43 2019
__init__.py 131 Wed Nov 21 19:33:28 2018
main.py 587 Wed Jun  5 23:05:43 2019
__init__.py 131 Wed Nov 21 19:33:28 2018
```

最后还有一点要注意的就是，有时候在处理文件名编码问题时候可能会出现一些问题。 通常来讲，函数 os.listdir() 返回的实体列表会根据系统默认的文件名编码来解码。 但是有时候也会碰到一些不能正常解码的文件名。 关于文件名的处理问题，在5.14和5.15小节有更详细的讲解。

## 5.14. 忽略文件名编码

你想使用原始文件名执行文件的I/O操作，也就是说文件名并没有经过系统默认编码去解码或编码过。

默认情况下，所有的文件名都会根据 sys.getfilesystemencoding() 返回的文本编码来编码或解码。比如：

```python
>>> sys.getfilesystemencoding()
'utf-8'
```

如果因为某种原因你想忽略这种编码，可以使用一个原始字节字符串来指定一个文件名即可。比如：

```python
import os

if __name__ == '__main__':
    # Wrte a file using a unicode filename
    with open('jalape\xf1o.txt', 'w') as f:
        f.write('Spicy!')

    # Directory listing (decoded)
    print(os.listdir('.'))

    # Directory listing (raw)
    print(os.listdir(b'.'))

    # Open file with raw filename
    with open(b'jalape\xc3\xb1o.txt') as f:
        print(f.read())
输出：
['jalapeño.txt']
[b'jalape\xc3\xb1o.txt', b'main.py', b'test.txt', b'__init__.py', b'__pycache__']
Spicy!
```

正如你所见，在最后两个操作中，当你给文件相关函数如 open() 和 os.listdir() 传递字节字符串时，文件名的处理方式会稍有不同。

通常来讲，你不需要担心文件名的编码和解码，普通的文件名操作应该就没问题了。 但是，有些操作系统允许用户通过偶然或恶意方式去创建名字不符合默认编码的文件。 这些文件名可能会神秘地中断那些需要处理大量文件的Python程序。

读取目录并通过原始未解码方式处理文件名可以有效的避免这样的问题， 尽管这样会带来一定的编程难度。

关于打印不可解码的文件名，请参考5.15小节。

## 5.15. 打印不合法的文件名

你的程序获取了一个目录中的文件名列表，但是当它试着去打印文件名的时候程序崩溃， 出现了 UnicodeEncodeError 异常和一条奇怪的消息—— surrogates not allowed 。

当打印未知的文件名时，使用下面的方法可以避免这样的错误：

```python
def bad_filename(filename):
    return repr(filename)[1:-1]

try:
    print(filename)
except UnicodeEncodeError:
    print(bad_filename(filename))
```

这一小节讨论的是在编写必须处理文件系统的程序时一个不太常见但又很棘手的问题。 默认情况下，Python假定所有文件名都已经根据 sys.getfilesystemencoding() 的值编码过了。 但是，有一些文件系统并没有强制要求这样做，因此允许创建文件名没有正确编码的文件。 这种情况不太常见，但是总会有些用户冒险这样做或者是无意之中这样做了( 可能是在一个有缺陷的代码中给 open() 函数传递了一个不合规范的文件名)。

当执行类似 os.listdir() 这样的函数时，这些不合规范的文件名就会让Python陷入困境。 一方面，它不能仅仅只是丢弃这些不合格的名字。而另一方面，它又不能将这些文件名转换为正确的文本字符串。 Python对这个问题的解决方案是从文件名中获取未解码的字节值比如 \xhh 并将它映射成Unicode字符 \udchh 表示的所谓的”代理编码”。 下面一个例子演示了当一个不合格目录列表中含有一个文件名为bäd.txt(使用Latin-1而不是UTF-8编码)时的样子：

```python
>>> import os
>>> files = os.listdir('.')
>>> files
['spam.py', 'b\udce4d.txt', 'foo.txt']
```

如果你有代码需要操作文件名或者将文件名传递给 open() 这样的函数，一切都能正常工作。 只有当你想要输出文件名时才会碰到些麻烦(比如打印输出到屏幕或日志文件等)。 特别的，当你想打印上面的文件名列表时，你的程序就会崩溃：

```python
with open('b\udce4d.txt', 'w') as f:
    f.write('Spicy!')

files = os.listdir('.')
print(files)
for name in files:
    print(name)
输出：
['b\udce4d.txt', 'jalapeño.txt', 'main.py', 'test.txt', '__init__.py', '__pycache__']
Traceback (most recent call last):
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 12, in <module>
    print(name)
UnicodeEncodeError: 'utf-8' codec can't encode character '\udce4' in position 1: surrogates not allowed
```

程序崩溃的原因就是字符 \udce4 是一个非法的Unicode字符。 它其实是一个被称为代理字符对的双字符组合的后半部分。 由于缺少了前半部分，因此它是个非法的Unicode。 所以，唯一能成功输出的方法就是当遇到不合法文件名时采取相应的补救措施。 比如可以将上述代码修改如下：

```python
import os


def bad_filename(filename):
    return repr(filename)[1:-1]


if __name__ == '__main__':
    files = os.listdir('.')
    for name in files:
        try:
            print(name)
        except UnicodeEncodeError:
            print(bad_filename(name))
输出：
b\udce4d.txt
jalapeño.txt
main.py
```

在 bad_filename() 函数中怎样处置取决于你自己。 另外一个选择就是通过某种方式重新编码，示例如下：

```python
import sys

def bad_filename(filename):
    temp = filename.encode(sys.getfilesystemencoding(), errors='surrogateescape')
    return temp.decode('latin-1')

输出：
bäd.txt
jalapeño.txt
main.py
```

译者注:

> surrogateescape:
> 这种是Python在绝大部分面向OS的API中所使用的错误处理器，
> 它能以一种优雅的方式处理由操作系统提供的数据的编码问题。
> 在解码出错时会将出错字节存储到一个很少被使用到的Unicode编码范围内。
> 在编码时将那些隐藏值又还原回原先解码失败的字节序列。
> 它不仅对于OS API非常有用，也能很容易的处理其他情况下的编码错误。

这一小节主题可能会被大部分读者所忽略。但是如果你在编写依赖文件名和文件系统的关键任务程序时， 就必须得考虑到这个。否则你可能会在某个周末被叫到办公室去调试一些令人费解的错误。

## 5.16. 增加或改变已打开文件的编码

你想在不关闭一个已打开的文件前提下增加或改变它的Unicode编码。

如果你想给一个以二进制模式打开的文件添加Unicode编码/解码方式， 可以使用 io.TextIOWrapper() 对象包装它。比如：

```python
import urllib.request
import io

u = urllib.request.urlopen('http://www.python.org')
f = io.TextIOWrapper(u, encoding='utf-8')
text = f.read()
```

如果你想修改一个已经打开的文本模式的文件的编码方式，可以先使用 detach() 方法移除掉已存在的文本编码层， 并使用新的编码方式代替。下面是一个在 sys.stdout 上修改编码方式的例子：

```python
>>> import sys
>>> sys.stdout.encoding
Out[3]: 'UTF-8'
>>> print('你好')
你好
>>> import io
>>> sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding='latin-1')
>>> sys.stdout.encoding
Out[7]: 'latin-1'
>>> print('你好')
Traceback (most recent call last):
  File "D:\Anaconda3\lib\site-packages\IPython\core\interactiveshell.py", line 3296, in run_code
    exec(code_obj, self.user_global_ns, self.user_ns)
  File "<ipython-input-8-5b6bd01b6109>", line 1, in <module>
    print('你好')
UnicodeEncodeError: 'latin-1' codec can't encode characters in position 0-1: ordinal not in range(256)
```

I/O系统由一系列的层次构建而成。你可以试着运行下面这个操作一个文本文件的例子来查看这种层次，本地验证如下：

```python
f = open('test.txt', 'w')
print(f)
print(f.buffer)
print(f.buffer.raw)
输出：
<_io.TextIOWrapper name='test.txt' mode='w' encoding='cp936'>
<_io.BufferedWriter name='test.txt'>
<_io.FileIO name='test.txt' mode='wb' closefd=True>
```

在这个例子中，io.TextIOWrapper 是一个编码和解码Unicode的文本处理层， io.BufferedWriter 是一个处理二进制数据的带缓冲的I/O层， io.FileIO 是一个表示操作系统底层文件描述符的原始文件。 增加或改变文本编码会涉及增加或改变最上面的 io.TextIOWrapper 层。

一般来讲，像上面例子这样通过访问属性值来直接操作不同的层是很不安全的。 例如，如果你试着使用下面这样的技术改变编码看看会发生什么：

```python
import io

if __name__ == '__main__':
    f = open('test.txt', 'w')
    print(f)
    f = io.TextIOWrapper(f.buffer, encoding='UTF-8')
    print(f)
    f.write('Hello')
输出：
<_io.TextIOWrapper name='test.txt' mode='w' encoding='cp936'>
<_io.TextIOWrapper name='test.txt' encoding='UTF-8'>
Traceback (most recent call last):
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 11, in <module>
    f.write('Hello')
ValueError: I/O operation on closed file.
```

结果出错了，因为f的原始值已经被破坏了并关闭了底层的文件。

detach() 方法会断开文件的最顶层并返回第二层，之后最顶层就没什么用了。例如：

```python
f = open('test.txt', 'w')
print(f)
b = f.detach()
print(b)
f.write('hello')
输出：
<_io.TextIOWrapper name='test.txt' mode='w' encoding='cp936'>
<_io.BufferedWriter name='test.txt'>
Traceback (most recent call last):
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 11, in <module>
    f.write('hello')
ValueError: underlying buffer has been detached
```

一旦断开最顶层后，你就可以给返回结果添加一个新的最顶层。比如：

```python
import io

if __name__ == '__main__':
    f = open('test.txt', 'w')
    print(f)
    b = f.detach()
    print(b)
    f = io.TextIOWrapper(b, encoding='UTF-8')
    print(f)
输出：
<_io.TextIOWrapper name='test.txt' mode='w' encoding='cp936'>
<_io.BufferedWriter name='test.txt'>
<_io.TextIOWrapper name='test.txt' encoding='UTF-8'>
```

尽管已经向你演示了改变编码的方法， 但是你还可以利用这种技术来改变文件行处理、错误机制以及文件处理的其他方面。例如：

```python
>>> sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding='ascii',
...                             errors='xmlcharrefreplace')
>>> print('Jalape\u00f1o')
Jalape&#241;o
```

注意下最后输出中的非ASCII字符 ñ 是如何被 &#241; 取代的。

## 5.17. 将字节写入

你想在文本模式打开的文件中写入原始的字节数据。

将字节数据直接写入文件的缓冲区即可，例如：

```python
>>> import sys
>>> sys.stdout.write(b'Hello\n')
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
TypeError: must be str, not bytes
>>> sys.stdout.buffer.write(b'Hello\n')
Hello
5
>>>
```

类似的，能够通过读取文本文件的 buffer 属性来读取二进制数据。

I/O系统以层级结构的形式构建而成。 文本文件是通过在一个拥有缓冲的二进制模式文件上增加一个Unicode编码/解码层来创建。 buffer 属性指向对应的底层文件。如果你直接访问它的话就会绕过文本编码/解码层。

本小节例子展示的 sys.stdout 可能看起来有点特殊。 默认情况下，sys.stdout 总是以文本模式打开的。 但是如果你在写一个需要打印二进制数据到标准输出的脚本的话，你可以使用上面演示的技术来绕过文本编码层。

## 5.18. 将文件描述符包装成文件对象

你有一个对应于操作系统上一个已打开的I/O通道(比如文件、管道、套接字等)的整型文件描述符， 你想将它包装成一个更高层的Python文件对象。

一个文件描述符和一个打开的普通文件是不一样的。 文件描述符仅仅是一个由操作系统指定的整数，用来指代某个系统的I/O通道。 如果你碰巧有这么一个文件描述符，你可以通过使用 open() 函数来将其包装为一个Python的文件对象。 你仅仅只需要使用这个整数值的文件描述符作为第一个参数来代替文件名即可。例如：

```python
import os

if __name__ == '__main__':
    # Open a low-level file descriptor
    fd = os.open('test.txt', os.O_WRONLY | os.O_CREAT)

    # Turn into a proper file
    f = open(fd, 'wt')
    f.write('hello world\n')
    f.close()
```

当高层的文件对象被关闭或者破坏的时候，底层的文件描述符也会被关闭。 如果这个并不是你想要的结果，你可以给 open() 函数传递一个可选的 colsefd=False 。比如：

```python
# Create a file object, but don't close underlying fd when done
f = open(fd, 'wt', closefd=False)
```

在Unix系统中，这种包装文件描述符的技术可以很方便的将一个类文件接口作用于一个以不同方式打开的I/O通道上， 如管道、套接字等。举例来讲，下面是一个操作管道的例子：

```python
from socket import socket, AF_INET, SOCK_STREAM

def echo_client(client_sock, addr):
    print('Got connection from', addr)

    # Make text-mode file wrappers for socket reading/writing
    client_in = open(client_sock.fileno(), 'rt', encoding='latin-1',
                closefd=False)

    client_out = open(client_sock.fileno(), 'wt', encoding='latin-1',
                closefd=False)

    # Echo lines back to the client using file I/O
    for line in client_in:
        client_out.write(line)
        client_out.flush()

    client_sock.close()

def echo_server(address):
    sock = socket(AF_INET, SOCK_STREAM)
    sock.bind(address)
    sock.listen(1)
    while True:
        client, addr = sock.accept()
        echo_client(client, addr)
```

需要重点强调的一点是，上面的例子仅仅是为了演示内置的 open() 函数的一个特性，并且也只适用于基于Unix的系统。 如果你想将一个类文件接口作用在一个套接字并希望你的代码可以跨平台，请使用套接字对象的 makefile() 方法。 但是如果不考虑可移植性的话，那上面的解决方案会比使用 makefile() 性能更好一点。

你也可以使用这种技术来构造一个别名，允许以不同于第一次打开文件的方式使用它。 例如，下面演示如何创建一个文件对象，它允许你输出二进制数据到标准输出(通常以文本模式打开)：

```python
import sys

if __name__ == '__main__':
    # Create a binary-mode file for stdout
    bstdout = open(sys.stdout.fileno(), 'wb', closefd=False)
    bstdout.write(b'Hello World\n')
    bstdout.flush()
输出：
Hello World
```

尽管可以将一个已存在的文件描述符包装成一个正常的文件对象， 但是要注意的是并不是所有的文件模式都被支持，并且某些类型的文件描述符可能会有副作用 (特别是涉及到错误处理、文件结尾条件等等的时候)。 在不同的操作系统上这种行为也是不一样，特别的，上面的例子都不能在非Unix系统上运行。 我说了这么多，意思就是让你充分测试自己的实现代码，确保它能按照期望工作。

## 5.19. 创建临时文件和文件夹

你需要在程序执行时创建一个临时文件或目录，并希望使用完之后可以自动销毁掉。

tempfile 模块中有很多的函数可以完成这任务。 为了创建一个匿名的临时文件，可以使用 tempfile.TemporaryFile ：

```python
from tempfile import TemporaryFile

if __name__ == '__main__':
    with TemporaryFile('w+t') as f:
        print('filename is:', f.name)
        # Read/write to the file
        f.write('Hello World\n')
        f.write('Testing\n')

        # Seek back to beginning and read the data
        f.seek(0)
        data = f.read()

    # Temporary file is destroyed
    print(data)
输出：
filename is: C:\Users\Ben\AppData\Local\Temp\tmps8hcmnph
Hello World
Testing
```

或者，如果你喜欢，你还可以像这样使用临时文件：

```python
f = TemporaryFile('w+t')
# Use the temporary file
...
f.close()
# File is destroyed
```

TemporaryFile() 的第一个参数是文件模式，通常来讲文本模式使用 w+t ，二进制模式使用 w+b 。 这个模式同时支持读和写操作，在这里是很有用的，因为当你关闭文件去改变模式的时候，文件实际上已经不存在了。 TemporaryFile() 另外还支持跟内置的 open() 函数一样的参数。比如：

```python
with TemporaryFile('w+t', encoding='utf-8', errors='ignore') as f:
    ...
```

在大多数Unix系统上，通过 TemporaryFile() 创建的文件都是匿名的，甚至连目录都没有。 如果你想打破这个限制，可以使用 NamedTemporaryFile() 来代替。比如：

```python
from tempfile import NamedTemporaryFile

if __name__ == '__main__':
    with NamedTemporaryFile('w+t') as f:
        print('filename is:', f.name)

# File automatically destroyed
输出：
filename is: C:\Users\Ben\AppData\Local\Temp\tmp2wvojnt6
```

这里，被打开文件的 f.name 属性包含了该临时文件的文件名。 当你需要将文件名传递给其他代码来打开这个文件的时候，这个就很有用了。 和 TemporaryFile() 一样，结果文件关闭时会被自动删除掉。 如果你不想这么做，可以传递一个关键字参数 delete=False 即可。比如：

```python
from tempfile import NamedTemporaryFile

if __name__ == '__main__':
    with NamedTemporaryFile('w+t', delete=False) as f:
        print('filename is:', f.name)
        f.write('Hello World')
输出：
filename is: C:\Users\Ben\AppData\Local\Temp\tmpzbp1v_nr
# 程序运行完，打开文件filename is: C:\Users\Ben\AppData\Local\Temp\tmpzbp1v_nr，可以看到内容为Hello World
```

为了创建一个临时目录，可以使用 tempfile.TemporaryDirectory() 。比如：

```python
from tempfile import TemporaryDirectory

with TemporaryDirectory() as dirname:
    print('dirname is:', dirname)
    # Use the directory
    ...
# Directory and all contents destroyed
```

TemporaryFile() 、NamedTemporaryFile() 和 TemporaryDirectory() 函数 应该是处理临时文件目录的最简单的方式了，因为它们会自动处理所有的创建和清理步骤。 在一个更低的级别，你可以使用 mkstemp() 和 mkdtemp() 来创建临时文件和目录。比如：

```python
>>> import tempfile
>>> tempfile.mkstemp()
(3, '/var/folders/7W/7WZl5sfZEF0pljrEB1UMWE+++TI/-Tmp-/tmp7fefhv')
>>> tempfile.mkdtemp()
'/var/folders/7W/7WZl5sfZEF0pljrEB1UMWE+++TI/-Tmp-/tmp5wvcv6'
```

但是，这些函数并不会做进一步的管理了。 例如，函数 mkstemp() 仅仅就返回一个原始的OS文件描述符，你需要自己将它转换为一个真正的文件对象。 同样你还需要自己清理这些文件。

使用样例：

```python
import tempfile

if __name__ == '__main__':
    # The return value is a pair (fd, name) where fd is the file descriptor returned by os.open, and name is the filename.
    fd = tempfile.mkstemp()
    print(fd)
    f = open(fd[0], 'wt')  # 这里使用fd[0]或fd[1]都可以
    f.write('Hello')
    f.close()
# 可以在C:\Users\Ben\AppData\Local\Temp\tmp_bwdd1ip查看文件内容为Hello

fd = tempfile.mkstemp(suffix='.txt', prefix='abc_')
# 文件路径为：C:\Users\Ben\AppData\Local\Temp\abc_4e99vm6l.txt
```

通常来讲，临时文件在系统默认的位置被创建，比如 /var/tmp 或类似的地方。 为了获取真实的位置，可以使用 tempfile.gettempdir() 函数。比如：

```python
print(tempfile.gettempdir())
输出：
C:\Users\Ben\AppData\Local\Temp
```

所有和临时文件相关的函数都允许你通过使用关键字参数 prefix 、suffix 和 dir 来自定义目录以及命名规则。比如：

```python
from tempfile import NamedTemporaryFile

if __name__ == '__main__':
    f = NamedTemporaryFile(prefix='mytemp', suffix='.txt', dir='e:', delete=False)
    print(f.name)
输出：
E:\mytempdzp1ohlc.txt
```

最后还有一点，尽可能以最安全的方式使用 tempfile 模块来创建临时文件。 包括仅给当前用户授权访问以及在文件创建过程中采取措施避免竞态条件。 要注意的是不同的平台可能会不一样。因此你最好阅读 官方文档 来了解更多的细节。

## 5.20. 与串行端口的数据通信

你想通过串行端口读写数据，典型场景就是和一些硬件设备打交道(比如一个机器人或传感器)。

尽管你可以通过使用Python内置的I/O模块来完成这个任务，但对于串行通信最好的选择是使用 pySerial包 。 这个包的使用非常简单，先安装pySerial，使用类似下面这样的代码就能很容易的打开一个串行端口：

```python
import serial
ser = serial.Serial('/dev/tty.usbmodem641', # Device name varies
                    baudrate=9600,
                    bytesize=8,
                    parity='N',
                    stopbits=1)
```

设备名对于不同的设备和操作系统是不一样的。 比如，在Windows系统上，你可以使用0, 1等表示的一个设备来打开通信端口”COM0”和”COM1”。 一旦端口打开，那就可以使用 read()，readline() 和 write() 函数读写数据了。例如：

```python
ser.write(b'G1 X50 Y50\r\n')
resp = ser.readline()
```

大多数情况下，简单的串口通信从此变得十分简单。

尽管表面上看起来很简单，其实串口通信有时候也是挺麻烦的。 推荐你使用第三方包如 pySerial 的一个原因是它提供了对高级特性的支持 (比如超时，控制流，缓冲区刷新，握手协议等等)。举个例子，如果你想启用 RTS-CTS 握手协议， 你只需要给 Serial() 传递一个 rtscts=True 的参数即可。 其官方文档非常完善，因此我在这里极力推荐这个包。

时刻记住所有涉及到串口的I/O都是二进制模式的。因此，确保你的代码使用的是字节而不是文本 (或有时候执行文本的编码/解码操作)。 另外当你需要创建二进制编码的指令或数据包的时候，struct 模块也是非常有用的。

## 5.21. 序列化Python对象

你需要将一个Python对象序列化为一个字节流，以便将它保存到一个文件、存储到数据库或者通过网络传输它。

对于序列化最普遍的做法就是使用 pickle 模块。为了将一个对象保存到一个文件中，可以这样做：

```python
import pickle

if __name__ == '__main__':
    data = 'Hello, world'
    with open('dump.txt', 'wb') as f:
        pickle.dump(data, f)
# 会生成一个dump.txt文件
```

为了将一个对象转储为一个字符串，可以使用 pickle.dumps() ：

```python
data = 'Hello, world'
s = pickle.dumps(data)
print(s)
输出：
b'\x80\x03X\x0c\x00\x00\x00Hello, worldq\x00.'
# 和dump.txt文件中内容一致。
```

为了从字节流中恢复一个对象，使用 pickle.load() 或 pickle.loads() 函数。比如：

```python
import pickle

if __name__ == '__main__':
    # Restore from a file
    f = open('dump.txt', 'rb')
    data = pickle.load(f)
    print(data)

    # Restore from a string
    dump_data = pickle.dumps(data)
    restored_data = pickle.loads(dump_data)
    print(restored_data)
输出：
Hello, world
Hello, world
```

对于大多数应用程序来讲，dump() 和 load() 函数的使用就是你有效使用 pickle 模块所需的全部了。 它可适用于绝大部分Python数据类型和用户自定义类的对象实例。 如果你碰到某个库可以让你在数据库中保存/恢复Python对象或者是通过网络传输对象的话， 那么很有可能这个库的底层就使用了 pickle 模块。

pickle 是一种Python特有的自描述的数据编码。 通过自描述，被序列化后的数据包含每个对象开始和结束以及它的类型信息。 因此，你无需担心对象记录的定义，它总是能工作。 举个例子，如果要处理多个对象，你可以这样做：

```python
import pickle

if __name__ == '__main__':
    with open('dump.txt', 'wb') as f:
        pickle.dump([1, 2, 3, 4], f)
        pickle.dump('hello', f)
        pickle.dump({'Apple', 'Pear', 'Banana'}, f)

    with open('dump.txt', 'rb') as f:
        print(pickle.load(f))
        print(pickle.load(f))
        print(pickle.load(f))
输出：
[1, 2, 3, 4]
hello
{'Apple', 'Pear', 'Banana'}
```

你还能序列化函数，类，还有接口，但是结果数据仅仅将它们的名称编码成对应的代码对象。例如：

```python
import pickle

import math

if __name__ == '__main__':
    print(pickle.dumps(math.cos))
输出：
b'\x80\x03cmath\ncos\nq\x00.'
```

当数据反序列化回来的时候，会先假定所有的源数据时可用的。 模块、类和函数会自动按需导入进来。对于Python数据被不同机器上的解析器所共享的应用程序而言， 数据的保存可能会有问题，因为所有的机器都必须访问同一个源代码。

```python
# 将math.cos序列化到文件中
import pickle

import math

if __name__ == '__main__':
    with open('dump.txt', 'wb') as f:
        pickle.dump(math.cos, f)

# 将math.cos反序列化回来
import pickle

if __name__ == '__main__':
    with open('dump.txt', 'rb') as f:
        data = pickle.load(f)

    print(data(0))
输出：
1.0
```

注

> 千万不要对不信任的数据使用pickle.load()。
> pickle在加载时有一个副作用就是它会自动加载相应模块并构造实例对象。
> 但是某个坏人如果知道pickle的工作原理，
> 他就可以创建一个恶意的数据导致Python执行随意指定的系统命令。
> 因此，一定要保证pickle只在相互之间可以认证对方的解析器的内部使用。

有些类型的对象是不能被序列化的。这些通常是那些依赖外部系统状态的对象， 比如打开的文件，网络连接，线程，进程，栈帧等等。 用户自定义类可以通过提供__getstate__()和__setstate__()方法来绕过这些限制。 如果定义了这两个方法，pickle.dump() 就会调用__getstate__()获取序列化的对象。 类似的__setstate__()在反序列化时被调用。为了演示这个工作原理， 下面是一个在内部定义了一个线程但仍然可以序列化和反序列化的类：

```python
# countdown.py
import time
import threading

class Countdown:
    def __init__(self, n):
        self.n = n
        self.thr = threading.Thread(target=self.run)
        self.thr.daemon = True
        self.thr.start()

    def run(self):
        while self.n > 0:
            print('T-minus', self.n)
            self.n -= 1
            time.sleep(5)

    def __getstate__(self):
        return self.n

    def __setstate__(self, n):
        self.__init__(n)
```

试着运行下面的序列化试验代码：

```python
>>> import countdown
>>> c = countdown.Countdown(30)
>>> T-minus 30
T-minus 29
T-minus 28
...

>>> # After a few moments
>>> f = open('cstate.p', 'wb')
>>> import pickle
>>> pickle.dump(c, f)
>>> f.close()
```

然后退出Python解析器并重启后再试验下（本地没有验证成功，没有继续打印T-minus 19）：

```python
>>> f = open('cstate.p', 'rb')
>>> pickle.load(f)
countdown.Countdown object at 0x10069e2d0>
T-minus 19
T-minus 18
```

你可以看到线程又奇迹般的重生了，从你第一次序列化它的地方又恢复过来。

pickle 对于大型的数据结构比如使用 array 或 numpy 模块创建的二进制数组效率并不是一个高效的编码方式。 如果你需要移动大量的数组数据，你最好是先在一个文件中将其保存为数组数据块或使用更高级的标准编码方式如HDF5 (需要第三方库的支持)。

由于 pickle 是Python特有的并且附着在源码上，所有如果需要长期存储数据的时候不应该选用它。 例如，如果源码变动了，你所有的存储数据可能会被破坏并且变得不可读取。 坦白来讲，对于在数据库和存档文件中存储数据时，你最好使用更加标准的数据编码格式如XML，CSV或JSON。 这些编码格式更标准，可以被不同的语言支持，并且也能很好的适应源码变更。

最后一点要注意的是 pickle 有大量的配置选项和一些棘手的问题。 对于最常见的使用场景，你不需要去担心这个，但是如果你要在一个重要的程序中使用pickle去做序列化的话， 最好去查阅一下 官方文档 。

# 6. 数据编码和处理

## 6.1. 读写CSV数据

你想读写一个CSV格式的文件。

对于大多数的CSV格式的数据读写问题，都可以使用 csv 库。 例如：假设你在一个名叫stocks.csv文件中有一些股票市场数据，就像这样：

> Symbol,Price,Date,Time,Change,Volume
> "AA",39.48,"6/11/2007","9:36am",-0.18,181800
> "AIG",71.38,"6/11/2007","9:36am",-0.15,195500
> "AXP",62.58,"6/11/2007","9:36am",-0.46,935000
> "BA",98.31,"6/11/2007","9:36am",+0.12,104800
> "C",53.08,"6/11/2007","9:36am",-0.25,360900
> "CAT",78.29,"6/11/2007","9:36am",-0.23,225400

下面向你展示如何将这些数据读取为一个元组的序列：

```python
import csv

if __name__ == '__main__':
    with open('stocks.csv') as f:
        f_csv = csv.reader(f)
        headers = next(f_csv)
        print(f'Headers: {headers}')
        for row in f_csv:
            # Process row
            print(f'Data: {row}')
输出：
Headers: ['Symbol', 'Price', 'Date', 'Time', 'Change', 'Volume']
Data: ['AA', '39.48', '6/11/2007', '9:36am', '-0.18', '181800']
Data: ['AIG', '71.38', '6/11/2007', '9:36am', '-0.15', '195500']
Data: ['AXP', '62.58', '6/11/2007', '9:36am', '-0.46', '935000']
Data: ['BA', '98.31', '6/11/2007', '9:36am', '+0.12', '104800']
Data: ['C', '53.08', '6/11/2007', '9:36am', '-0.25', '360900']
Data: ['CAT', '78.29', '6/11/2007', '9:36am', '-0.23', '225400']
```

在上面的代码中， row 会是一个列表。因此，为了访问某个字段，你需要使用下标，如 row[0] 访问Symbol， row[4] 访问Change。

```python
import csv

if __name__ == '__main__':
    with open('stocks.csv') as f:
        f_csv = csv.reader(f)
        headers = next(f_csv)
        print(headers[0], headers[4], sep=';')
        for row in f_csv:
            # Process row
            print(row[0], row[4], sep=';')
输出：
Symbol;Change
AA;-0.18
AIG;-0.15
AXP;-0.46
BA;+0.12
C;-0.25
CAT;-0.23
```

由于这种下标访问通常会引起混淆，你可以考虑使用命名元组。例如：

```python
import csv
from collections import namedtuple

if __name__ == '__main__':
    with open('stocks.csv') as f:
        f_csv = csv.reader(f)
        headers = next(f_csv)
        Row = namedtuple('Row', headers)
        for r in f_csv:
            row = Row(*r)
            # Process row
            print(row)  # print(row.Symbol, row.Change, sep=';')
输出：
Row(Symbol='AA', Price='39.48', Date='6/11/2007', Time='9:36am', Change='-0.18', Volume='181800')
Row(Symbol='AIG', Price='71.38', Date='6/11/2007', Time='9:36am', Change='-0.15', Volume='195500')
Row(Symbol='AXP', Price='62.58', Date='6/11/2007', Time='9:36am', Change='-0.46', Volume='935000')
Row(Symbol='BA', Price='98.31', Date='6/11/2007', Time='9:36am', Change='+0.12', Volume='104800')
Row(Symbol='C', Price='53.08', Date='6/11/2007', Time='9:36am', Change='-0.25', Volume='360900')
Row(Symbol='CAT', Price='78.29', Date='6/11/2007', Time='9:36am', Change='-0.23', Volume='225400')
```

另外一个选择就是将数据读取到一个字典序列中去。可以这样做：

```python
import csv

if __name__ == '__main__':
    with open('stocks.csv') as f:
        f_csv = csv.DictReader(f)
        for row in f_csv:
            # Process row
            print(row)
输出：
OrderedDict([('Symbol', 'AA'), ('Price', '39.48'), ('Date', '6/11/2007'), ('Time', '9:36am'), ('Change', '-0.18'), ('Volume', '181800')])
OrderedDict([('Symbol', 'AIG'), ('Price', '71.38'), ('Date', '6/11/2007'), ('Time', '9:36am'), ('Change', '-0.15'), ('Volume', '195500')])
OrderedDict([('Symbol', 'AXP'), ('Price', '62.58'), ('Date', '6/11/2007'), ('Time', '9:36am'), ('Change', '-0.46'), ('Volume', '935000')])
OrderedDict([('Symbol', 'BA'), ('Price', '98.31'), ('Date', '6/11/2007'), ('Time', '9:36am'), ('Change', '+0.12'), ('Volume', '104800')])
OrderedDict([('Symbol', 'C'), ('Price', '53.08'), ('Date', '6/11/2007'), ('Time', '9:36am'), ('Change', '-0.25'), ('Volume', '360900')])
OrderedDict([('Symbol', 'CAT'), ('Price', '78.29'), ('Date', '6/11/2007'), ('Time', '9:36am'), ('Change', '-0.23'), ('Volume', '225400')])
```

在这个版本中，你可以使用列名去访问每一行的数据了。比如，row['Symbol'] 或者 row['Change']

为了写入CSV数据，你仍然可以使用csv模块，不过这时候先创建一个 writer 对象。例如:

```python
import csv

if __name__ == '__main__':
    headers = ['Symbol', 'Price', 'Date', 'Time', 'Change', 'Volume']
    rows = [('AA', 39.48, '6/11/2007', '9:36am', -0.18, 181800),
            ('AIG', 71.38, '6/11/2007', '9:36am', -0.15, 195500),
            ('AXP', 62.58, '6/11/2007', '9:36am', -0.46, 935000),
            ]

    with open('stocks.csv', 'w') as f:
        f_csv = csv.writer(f)
        f_csv.writerow(headers)
        f_csv.writerows(rows)
# 生成文件内容：
Symbol,Price,Date,Time,Change,Volume

AA,39.48,6/11/2007,9:36am,-0.18,181800

AIG,71.38,6/11/2007,9:36am,-0.15,195500

AXP,62.58,6/11/2007,9:36am,-0.46,935000

```

如果你有一个字典序列的数据，可以像这样做：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import csv

if __name__ == '__main__':
    headers = ['Symbol', 'Price', 'Date', 'Time', 'Change', 'Volume']
    rows = [{'Symbol': 'AA', 'Price': 39.48, 'Date': '6/11/2007',
             'Time': '9:36am', 'Change': -0.18, 'Volume': 181800},
            {'Symbol': 'AIG', 'Price': 71.38, 'Date': '6/11/2007',
             'Time': '9:36am', 'Change': -0.15, 'Volume': 195500},
            {'Symbol': 'AXP', 'Price': 62.58, 'Date': '6/11/2007',
             'Time': '9:36am', 'Change': -0.46, 'Volume': 935000},
            ]

    with open('stocks.csv', 'w') as f:
        f_csv = csv.DictWriter(f, headers)
        f_csv.writeheader()
        f_csv.writerows(rows)
```

你应该总是优先选择csv模块分割或解析CSV数据。例如，你可能会像编写类似下面这样的代码：

```python
with open('stocks.csv') as f:
for line in f:
    row = line.split(',')
    # process row
```

使用这种方式的一个缺点就是你仍然需要去处理一些棘手的细节问题。 比如，如果某些字段值被引号包围，你不得不去除这些引号。 另外，如果一个被引号包围的字段碰巧含有一个逗号，那么程序就会因为产生一个错误大小的行而出错。

默认情况下，csv 库可识别Microsoft Excel所使用的CSV编码规则。 这或许也是最常见的形式，并且也会给你带来最好的兼容性。 然而，如果你查看csv的文档，就会发现有很多种方法将它应用到其他编码格式上(如修改分割字符等)。 例如，如果你想读取以tab分割的数据，可以这样做：

```python
# Example of reading tab-separated values
with open('stock.tsv') as f:
    f_tsv = csv.reader(f, delimiter='\t')
    for row in f_tsv:
        # Process row
```

如果你正在读取CSV数据并将它们转换为命名元组，需要注意对列名进行合法性认证。 例如，一个CSV格式文件有一个包含非法标识符的列头行，类似下面这样：

> Street Address,Num-Premises,Latitude,Longitude 5412 N CLARK,10,41.980262,-87.668452

这样最终会导致在创建一个命名元组时产生一个 ValueError 异常而失败。 为了解决这问题，你可能不得不先去修正列标题。 例如，可以像下面这样在非法标识符上使用一个正则表达式替换：

```python
import csv
import re
from collections import namedtuple

if __name__ == '__main__':
    with open('stocks.csv') as f:
        f_csv = csv.reader(f)
        headers = [re.sub('[^a-zA-Z_]', '_', h) for h in next(f_csv)]  # 
        Row = namedtuple('Row', headers)
        for r in f_csv:
            row = Row(*r)
            print(row)
# 原列头headers被替换后，为：
<class 'list'>: ['Street_Address', 'Num_Premises', 'Latitude', 'Longitude______N_CLARK', '__', '_________', '__________']
在构造namedtuple时抛异常：ValueError: Field names cannot start with an underscore: '__'

后修改stocks.csv为如下：
Street Address,Num-Premises,Latitude,Longitude 5412 N CLARK
"AA",39.48,"6/11/2007","9:36am"
"AIG",71.38,"6/11/2007","9:36am"
"AXP",62.58,"6/11/2007","9:36am"
"BA",98.31,"6/11/2007","9:36am"

输出如下：
Row(Street_Address='AA', Num_Premises='39.48', Latitude='6/11/2007', Longitude______N_CLARK='9:36am')
Row(Street_Address='AIG', Num_Premises='71.38', Latitude='6/11/2007', Longitude______N_CLARK='9:36am')
Row(Street_Address='AXP', Num_Premises='62.58', Latitude='6/11/2007', Longitude______N_CLARK='9:36am')
Row(Street_Address='BA', Num_Premises='98.31', Latitude='6/11/2007', Longitude______N_CLARK='9:36am')
```

还有重要的一点需要强调的是，csv产生的数据都是字符串类型的，它不会做任何其他类型的转换。 如果你需要做这样的类型转换，你必须自己手动去实现。 下面是一个在CSV数据上执行其他类型转换的例子：

```python
col_types = [str, float, str, str, float, int]
with open('stocks.csv') as f:
    f_csv = csv.reader(f)
    headers = next(f_csv)
    for row in f_csv:
        # Apply conversions to the row items
        row = tuple(convert(value) for convert, value in zip(col_types, row))
```

另外，下面是一个转换字典中特定字段的例子：

```python
import csv

if __name__ == '__main__':
    print('Reading as dicts with type conversion')
    field_types = [('Price', float),
                   ('Change', float),
                   ('Volume', int)]

    with open('stocks.csv') as f:
        for row in csv.DictReader(f):
            row.update((key, conversion(row[key])) for key, conversion in field_types)
            print(row)
```

通常来讲，你可能并不想过多去考虑这些转换问题。 在实际情况中，CSV文件都或多或少有些缺失的数据，被破坏的数据以及其它一些让转换失败的问题。 因此，除非你的数据确实有保障是准确无误的，否则你必须考虑这些问题(你可能需要增加合适的错误处理机制)。

最后，如果你读取CSV数据的目的是做数据分析和统计的话， 你可能需要看一看 Pandas 包。Pandas 包含了一个非常方便的函数叫 pandas.read_csv() ， 它可以加载CSV数据到一个 DataFrame 对象中去。 然后利用这个对象你就可以生成各种形式的统计、过滤数据以及执行其他高级操作了。 在6.13小节中会有这样一个例子。

注：字典update方法

```python
d1 = {1: 1, 2: 2, 3: 3}
d2 = {1: '1', 2: '2', 4: '4'}
d1.update(d2)
print(d1)
输出：
{1: '1', 2: '2', 3: 3, 4: '4'}

d1 = {1: 1, 2: 2, 3: 3}
d1.update((i, str(i)) for i in range(1, 5))
输出：
{1: '1', 2: '2', 3: '3', 4: '4'}
```

## 6.2. 读写JSON数据

你想读写JSON(JavaScript Object Notation)编码格式的数据。

json 模块提供了一种很简单的方式来编码和解码JSON数据。 其中两个主要的函数是 json.dumps() 和 json.loads() ， 要比其他序列化函数库如pickle的接口少得多。 下面演示如何将一个Python数据结构转换为JSON：

```python
import json

if __name__ == '__main__':
    data = {
        'name': 'ACME',
        'shares': 100,
        'price': 542.23
    }
    print(data)

    json_str = json.dumps(data)
    print(json_str)
输出：
{'name': 'ACME', 'shares': 100, 'price': 542.23}
{"name": "ACME", "shares": 100, "price": 542.23}
```

下面演示如何将一个JSON编码的字符串转换回一个Python数据结构：

```python
data = json.loads(json_str)
print(data)
输出：
{'name': 'ACME', 'shares': 100, 'price': 542.23}
```

如果你要处理的是文件而不是字符串，你可以使用 json.dump() 和 json.load() 来编码和解码JSON数据。例如：

```python
# Writing JSON data
with open('data.json', 'w') as f:
    json.dump(data, f)

# Reading data back
with open('data.json', 'r') as f:
    data = json.load(f)
```

JSON编码支持的基本数据类型为 None ， bool ， int ， float 和 str ， 以及包含这些类型数据的lists，tuples和dictionaries。 对于dictionaries，keys需要是字符串类型(字典中任何非字符串类型的key在编码时会先转换为字符串)。 为了遵循JSON规范，你应该只编码Python的lists和dictionaries。 而且，在web应用程序中，顶层对象被编码为一个字典是一个标准做法。

JSON编码的格式对于Python语法而已几乎是完全一样的，除了一些小的差异之外。 比如，True会被映射为true，False被映射为false，而None会被映射为null。 下面是一个例子，演示了编码后的字符串效果：

```python
>>> json.dumps(False)
'false'
>>> d = {'a': True,
...     'b': 'Hello',
...     'c': None}
>>> json.dumps(d)
'{"b": "Hello", "c": null, "a": true}'
```

如果你试着去检查JSON解码后的数据，你通常很难通过简单的打印来确定它的结构， 特别是当数据的嵌套结构层次很深或者包含大量的字段时。 为了解决这个问题，可以考虑使用pprint模块的 pprint() 函数来代替普通的 print() 函数。 它会按照key的字母顺序并以一种更加美观的方式输出。 下面是一个演示如何漂亮的打印输出Twitter上搜索结果的例子：

```python
>>> from urllib.request import urlopen
>>> import json
>>> u = urlopen('http://search.twitter.com/search.json?q=python&rpp=5')
>>> resp = json.loads(u.read().decode('utf-8'))
>>> from pprint import pprint
>>> pprint(resp)
{'completed_in': 0.074,
'max_id': 264043230692245504,
'max_id_str': '264043230692245504',
'next_page': '?page=2&max_id=264043230692245504&q=python&rpp=5',
'page': 1,
'query': 'python',
'refresh_url': '?since_id=264043230692245504&q=python',
'results': [{'created_at': 'Thu, 01 Nov 2012 16:36:26 +0000',
            'from_user': ...
            },
            {'created_at': 'Thu, 01 Nov 2012 16:36:14 +0000',
            'from_user': ...
            },
            {'created_at': 'Thu, 01 Nov 2012 16:36:13 +0000',
            'from_user': ...
            },
            {'created_at': 'Thu, 01 Nov 2012 16:36:07 +0000',
            'from_user': ...
            }
            {'created_at': 'Thu, 01 Nov 2012 16:36:04 +0000',
            'from_user': ...
            }],
'results_per_page': 5,
'since_id': 0,
'since_id_str': '0'}

# 刚才的json_str例子
import json
from pprint import pprint

if __name__ == '__main__':
    data = {
        'name': 'ACME',
        'shares': 100,
        'price': 542.23
    }
    print(data)

    json_str = json.dumps(data)
    print(json_str)

    data = json.loads(json_str)
    pprint(data)
输出：
{'name': 'ACME', 'shares': 100, 'price': 542.23}
{"name": "ACME", "shares": 100, "price": 542.23}
{'name': 'ACME', 'price': 542.23, 'shares': 100}
```

一般来讲，JSON解码会根据提供的数据创建dicts或lists。 如果你想要创建其他类型的对象，可以给 json.loads() 传递object_pairs_hook或object_hook参数。 例如，下面是演示如何解码JSON数据并在一个OrderedDict中保留其顺序的例子：

```python
import json
from collections import OrderedDict

if __name__ == '__main__':
    s = '{"name": "ACME", "shares": 50, "price": 490.1}'
    print(json.loads(s))
    print(json.loads(s, object_pairs_hook=OrderedDict))
输出：
{'name': 'ACME', 'shares': 50, 'price': 490.1}
OrderedDict([('name', 'ACME'), ('shares', 50), ('price', 490.1)])
```

下面是如何将一个JSON字典转换为一个Python对象例子：

```python
import json


class JsonObject(object):
    def __init__(self, d):
        self.__dict__ = d


if __name__ == '__main__':
    s = '{"name": "ACME", "shares": 50, "price": 490.1}'
    data = json.loads(s, object_hook=JsonObject)
    print(data)
    print(data.name)
    print(data.shares)
    print(data.price)
输出：
<__main__.JsonObject object at 0x000001BE57197128>
ACME
50
490.1
```

最后一个例子中，JSON解码后的字典作为一个单个参数传递给 __init__() 。 然后，你就可以随心所欲的使用它了，比如作为一个实例字典来直接使用它。

在编码JSON的时候，还有一些选项很有用。 如果你想获得漂亮的格式化字符串后输出，可以使用 json.dumps() 的indent参数。 它会使得输出和pprint()函数效果类似。比如：

```python
import json

if __name__ == '__main__':
    data = {
        'name': 'ACME',
        'shares': 100,
        'price': 542.23
    }
    print(json.dumps(data))
    print(json.dumps(data, indent=4))
输出：
{"name": "ACME", "shares": 100, "price": 542.23}
{
    "name": "ACME",
    "shares": 100,
    "price": 542.23
}
```

对象实例通常并不是JSON可序列化的。例如：

```python
import json

class Point(object):
    def __init__(self, x, y):
        self.x = x
        self.y = y


if __name__ == '__main__':
    p = Point(2, 3)
    json.dumps(p)
输出报错：
Traceback (most recent call last):
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 13, in <module>
    json.dumps(p)
  File "D:\Anaconda3\lib\json\__init__.py", line 231, in dumps
    return _default_encoder.encode(obj)
  File "D:\Anaconda3\lib\json\encoder.py", line 199, in encode
    chunks = self.iterencode(o, _one_shot=True)
  File "D:\Anaconda3\lib\json\encoder.py", line 257, in iterencode
    return _iterencode(o, 0)
  File "D:\Anaconda3\lib\json\encoder.py", line 179, in default
    raise TypeError(f'Object of type {o.__class__.__name__} '
TypeError: Object of type Point is not JSON serializable
```

如果你想序列化对象实例，你可以提供一个函数，它的输入是一个实例，返回一个可序列化的字典。例如：

```python
import json


def serialize_instance(obj):
    d = {'__classname__': type(obj).__name__}
    d.update(vars(obj))
    return d


class Point(object):
    def __init__(self, x, y):
        self.x = x
        self.y = y


if __name__ == '__main__':
    p = Point(2, 3)
    print(json.dumps(serialize_instance(p)))
输出：
{"__classname__": "Point", "x": 2, "y": 3}
```

如果你想反过来获取这个实例，可以这样做：

```python
# Dictionary mapping names to known classes
classes = {
    'Point' : Point
}

def unserialize_object(d):
    clsname = d.pop('__classname__', None)
    if clsname:
        cls = classes[clsname]
        obj = cls.__new__(cls) # Make instance without calling __init__
        for key, value in d.items():
            setattr(obj, key, value)
        return obj
    else:
        return d
```

下面是如何使用这些函数的例子：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import json


def serialize_instance(obj):
    d = {'__classname__': type(obj).__name__}
    d.update(vars(obj))
    return d


def unserialize_object(d):
    clsname = d.pop('__classname__', None)  # 将serialize_instance函数中的d.__classname__取出来，得到'Point'字符串
    if clsname:
        cls = classes[clsname]
        obj = cls.__new__(cls)  # Make instance without calling __init__
        for key, value in d.items():
            setattr(obj, key, value)
        return obj
    else:
        return d


class Point(object):
    def __init__(self, x, y):
        self.x = x
        self.y = y


# Dictionary mapping names to known classes
classes = {
    'Point': Point
}

if __name__ == '__main__':
    p = Point(2, 3)
    s = json.dumps(p, default=serialize_instance)
    print(s)

    a = json.loads(s, object_hook=unserialize_object)
    print(a)
    print(a.x)
    print(a.y)
输出：
{"__classname__": "Point", "x": 2, "y": 3}
<__main__.Point object at 0x000001933A087128>
2
3
```

json 模块还有很多其他选项来控制更低级别的数字、特殊值如NaN等的解析。 可以参考官方文档获取更多细节。

注：vars()函数 <https://www.jb51.net/article/129698.htm>

```python
class A(object):
    a = 1  # a是类A的属性，而不是类实例的属性

    def __init__(self):
        self.__a = 'a'
        self._b = 'b'
a = A()
print(a.__dict__)
print(vars(A))
print(vars(a))
输出：
{'_A__a': 'a', '_b': 'b'}
{'__module__': '__main__', 'a': 1, '__init__': <function A.__init__ at 0x000002002FD48E18>, '__dict__': <attribute '__dict__' of 'A' objects>, '__weakref__': <attribute '__weakref__' of 'A' objects>, '__doc__': None}
{'_A__a': 'a', '_b': 'b'}
```

## 6.3. 解析简单的XML数据

你想从一个简单的XML文档中提取数据。

可以使用 xml.etree.ElementTree 模块从简单的XML文档中提取数据。 为了演示，假设你想解析Planet Python上的RSS源。下面是相应的代码：

```python
from urllib.request import urlopen
from xml.etree.ElementTree import parse

if __name__ == '__main__':
    u = urlopen('http://planet.python.org/rss20.xml')
    doc = parse(u)

    # Extract and output tags of interest
    for item in doc.iterfind('channel/item'):
        title = item.findtext('title')
        date = item.findtext('pubDate')
        link = item.findtext('link')

        print(title)
        print(date)
        print(link)
        print()
```

运行上面的代码，输出结果类似这样：

```python
Codementor: How I learned that a lambda can't be stopped
Sat, 15 Jun 2019 13:27:36 +0000
https://www.codementor.io/nadaj/how-i-learned-that-a-lambda-can-t-be-stopped-vyu3dp9dy

Ian Ozsvald: &#8220;On the Delivery of Data Science Projects&#8221; &#8211; talk at PyDataCambridge meetup
Sat, 15 Jun 2019 11:34:44 +0000
https://ianozsvald.com/2019/06/15/on-the-delivery-of-data-science-projects-talk-at-pydatacambridge-meetup/

J. Pablo Fernández: Converting a Python data into a ReStructured Text table
Sat, 15 Jun 2019 10:26:18 +0000


NumFOCUS: NumFOCUS Hires First Ever Development Director
Fri, 14 Jun 2019 21:37:17 +0000
https://numfocus.org/blog/numfocus-hires-first-ever-development-director

Doug Hellmann: sphinxcontrib-spelling 4.3.0
Fri, 14 Jun 2019 21:05:44 +0000
http://feeds.doughellmann.com/~r/doughellmann/python/~3/Y8SHpGzRXcM/
```

很显然，如果你想做进一步的处理，你需要替换 print() 语句来完成其他有趣的事。

在很多应用程序中处理XML编码格式的数据是很常见的。 不仅因为XML在Internet上面已经被广泛应用于数据交换， 同时它也是一种存储应用程序数据的常用格式(比如字处理，音乐库等)。 接下来的讨论会先假定读者已经对XML基础比较熟悉了。

在很多情况下，当使用XML来仅仅存储数据的时候，对应的文档结构非常紧凑并且直观。 例如，上面例子中的RSS订阅源类似于下面的格式：

```python
<?xml version="1.0"?>
<rss version="2.0" xmlns:dc="http://purl.org/dc/elements/1.1/">
    <channel>
        <title>Planet Python</title>
        <link>http://planet.python.org/</link>
        <language>en</language>
        <description>Planet Python - http://planet.python.org/</description>
        <item>
            <title>Steve Holden: Python for Data Analysis</title>
            <guid>http://holdenweb.blogspot.com/...-data-analysis.html</guid>
            <link>http://holdenweb.blogspot.com/...-data-analysis.html</link>
            <description>...</description>
            <pubDate>Mon, 19 Nov 2012 02:13:51 +0000</pubDate>
        </item>
        <item>
            <title>Vasudev Ram: The Python Data model (for v2 and v3)</title>
            <guid>http://jugad2.blogspot.com/...-data-model.html</guid>
            <link>http://jugad2.blogspot.com/...-data-model.html</link>
            <description>...</description>
            <pubDate>Sun, 18 Nov 2012 22:06:47 +0000</pubDate>
        </item>
        <item>
            <title>Python Diary: Been playing around with Object Databases</title>
            <guid>http://www.pythondiary.com/...-object-databases.html</guid>
            <link>http://www.pythondiary.com/...-object-databases.html</link>
            <description>...</description>
            <pubDate>Sun, 18 Nov 2012 20:40:29 +0000</pubDate>
        </item>
    </channel>
</rss>
```

xml.etree.ElementTree.parse() 函数解析整个XML文档并将其转换成一个文档对象。 然后，你就能使用 find() 、iterfind() 和 findtext() 等方法来搜索特定的XML元素了。 这些函数的参数就是某个指定的标签名，例如 channel/item 或 title 。

每次指定某个标签时，你需要遍历整个文档结构。每次搜索操作会从一个起始元素开始进行。 同样，每次操作所指定的标签名也是起始元素的相对路径。 例如，执行 doc.iterfind('channel/item') 来搜索所有在 channel 元素下面的 item 元素。 doc 代表文档的最顶层(也就是第一级的 rss 元素)。 然后接下来的调用 item.findtext() 会从已找到的 item 元素位置开始搜索。

ElementTree 模块中的每个元素有一些重要的属性和方法，在解析的时候非常有用。 tag 属性包含了标签的名字，text 属性包含了内部的文本，而 get() 方法能获取属性值。例如：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from xml.etree.ElementTree import parse

if __name__ == '__main__':
    # with open('python.xml', encoding='utf-8') as f:
    #     doc = parse(f)  # 可以直接传入文件名，或传入file对象
    #     doc = fromstring(xml_text)  # 传入xml字符串
    doc = parse('python.xml')
    print(doc)

    e = doc.find('channel/title')
    print(e)
    print(e.tag)
    print(e.text)
    print(e.get('some_attribute'))
```

有一点要强调的是 xml.etree.ElementTree 并不是XML解析的唯一方法。 对于更高级的应用程序，你需要考虑使用 lxml 。 它使用了和ElementTree同样的编程接口，因此上面的例子同样也适用于lxml。 你只需要将刚开始的import语句换成 from lxml.etree import parse 就行了。 lxml 完全遵循XML标准，并且速度也非常快，同时还支持验证，XSLT，和XPath等特性。

## 6.4. 增量式解析大型XML文件

你想使用尽可能少的内存从一个超大的XML文档中提取数据。

任何时候只要你遇到增量式的数据处理时，第一时间就应该想到迭代器和生成器。 下面是一个很简单的函数，只使用很少的内存就能增量式的处理一个大型XML文件：

```python
from xml.etree.ElementTree import iterparse


def parse_and_remove(file_name, path):
    path_parts = path.split('/')
    doc = iterparse(file_name, ('start', 'end'))
    # Skip the root element
    next(doc)

    tag_stack = []
    elem_stack = []
    for event, elem in doc:  # 在同一个节点的start和end时，对应的elem是同一个内存对象
        if event == 'start':
            tag_stack.append(elem.tag)
            elem_stack.append(elem)
        elif event == 'end':
            if tag_stack == path_parts:  # 在每一个节点end事件时，如果是row/row的节点end事件，则把该row节点yield出去
                yield elem
                elem_stack[-2].remove(elem)
            try:
                tag_stack.pop()
                elem_stack.pop()
            except IndexError:
                pass
```

为了测试这个函数，你需要先有一个大型的XML文件。 通常你可以在政府网站或公共数据网站上找到这样的文件。 例如，你可以下载XML格式的芝加哥城市道路坑洼数据库。 在写这本书的时候，下载文件已经包含超过100,000行数据，编码格式类似于下面这样：

```python
<response>
    <row>
        <row>
            <creation_date>2012-11-18T00:00:00</creation_date>
            <status>Completed</status>
            <completion_date>2012-11-18T00:00:00</completion_date>
            <service_request_number>12-01906549</service_request_number>
            <type_of_service_request>Pot Hole in Street</type_of_service_request>
            <current_activity>Final Outcome</current_activity>
            <most_recent_action>CDOT Street Cut ... Outcome</most_recent_action>
            <street_address>4714 S TALMAN AVE</street_address>
            <zip>60632</zip>
            <x_coordinate>1159494.68618856</x_coordinate>
            <y_coordinate>1873313.83503384</y_coordinate>
            <ward>14</ward>
            <police_district>9</police_district>
            <community_area>58</community_area>
            <latitude>41.808090232127896</latitude>
            <longitude>-87.69053684711305</longitude>
            <location latitude="41.808090232127896"
            longitude="-87.69053684711305" />
        </row>
        <row>
            <creation_date>2012-11-18T00:00:00</creation_date>
            <status>Completed</status>
            <completion_date>2012-11-18T00:00:00</completion_date>
            <service_request_number>12-01906695</service_request_number>
            <type_of_service_request>Pot Hole in Street</type_of_service_request>
            <current_activity>Final Outcome</current_activity>
            <most_recent_action>CDOT Street Cut ... Outcome</most_recent_action>
            <street_address>3510 W NORTH AVE</street_address>
            <zip>60647</zip>
            <x_coordinate>1152732.14127696</x_coordinate>
            <y_coordinate>1910409.38979075</y_coordinate>
            <ward>26</ward>
            <police_district>14</police_district>
            <community_area>23</community_area>
            <latitude>41.91002084292946</latitude>
            <longitude>-87.71435952353961</longitude>
            <location latitude="41.91002084292946"
            longitude="-87.71435952353961" />
        </row>
        <row>
            <creation_date>2012-11-18T00:00:00</creation_date>
            <status>Completed</status>
            <completion_date>2012-11-18T00:00:00</completion_date>
            <service_request_number>12-01906695</service_request_number>
            <type_of_service_request>Pot Hole in Street</type_of_service_request>
            <current_activity>Final Outcome</current_activity>
            <most_recent_action>CDOT Street Cut ... Outcome</most_recent_action>
            <street_address>3510 W NORTH AVE</street_address>
            <zip>60648</zip>
            <x_coordinate>1152732.14127696</x_coordinate>
            <y_coordinate>1910409.38979075</y_coordinate>
            <ward>26</ward>
            <police_district>14</police_district>
            <community_area>23</community_area>
            <latitude>41.91002084292946</latitude>
            <longitude>-87.71435952353961</longitude>
            <location latitude="41.91002084292946"
            longitude="-87.71435952353961" />
        </row>
        <row>
            <creation_date>2012-11-18T00:00:00</creation_date>
            <status>Completed</status>
            <completion_date>2012-11-18T00:00:00</completion_date>
            <service_request_number>12-01906695</service_request_number>
            <type_of_service_request>Pot Hole in Street</type_of_service_request>
            <current_activity>Final Outcome</current_activity>
            <most_recent_action>CDOT Street Cut ... Outcome</most_recent_action>
            <street_address>3510 W NORTH AVE</street_address>
            <zip>60647</zip>
            <x_coordinate>1152732.14127696</x_coordinate>
            <y_coordinate>1910409.38979075</y_coordinate>
            <ward>26</ward>
            <police_district>14</police_district>
            <community_area>23</community_area>
            <latitude>41.91002084292946</latitude>
            <longitude>-87.71435952353961</longitude>
            <location latitude="41.91002084292946"
            longitude="-87.71435952353961" />
        </row>
        <row>
            <creation_date>2012-11-18T00:00:00</creation_date>
            <status>Completed</status>
            <completion_date>2012-11-18T00:00:00</completion_date>
            <service_request_number>12-01906695</service_request_number>
            <type_of_service_request>Pot Hole in Street</type_of_service_request>
            <current_activity>Final Outcome</current_activity>
            <most_recent_action>CDOT Street Cut ... Outcome</most_recent_action>
            <street_address>3510 W NORTH AVE</street_address>
            <zip>60647</zip>
            <x_coordinate>1152732.14127696</x_coordinate>
            <y_coordinate>1910409.38979075</y_coordinate>
            <ward>26</ward>
            <police_district>14</police_district>
            <community_area>23</community_area>
            <latitude>41.91002084292946</latitude>
            <longitude>-87.71435952353961</longitude>
            <location latitude="41.91002084292946"
            longitude="-87.71435952353961" />
        </row>
        <row>
            <creation_date>2012-11-18T00:00:00</creation_date>
            <status>Completed</status>
            <completion_date>2012-11-18T00:00:00</completion_date>
            <service_request_number>12-01906695</service_request_number>
            <type_of_service_request>Pot Hole in Street</type_of_service_request>
            <current_activity>Final Outcome</current_activity>
            <most_recent_action>CDOT Street Cut ... Outcome</most_recent_action>
            <street_address>3510 W NORTH AVE</street_address>
            <zip>60632</zip>
            <x_coordinate>1152732.14127696</x_coordinate>
            <y_coordinate>1910409.38979075</y_coordinate>
            <ward>26</ward>
            <police_district>14</police_district>
            <community_area>23</community_area>
            <latitude>41.91002084292946</latitude>
            <longitude>-87.71435952353961</longitude>
            <location latitude="41.91002084292946"
            longitude="-87.71435952353961" />
        </row>
    </row>
</response>
```

假设你想写一个脚本来按照坑洼报告数量排列邮编号码。你可以像这样做：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from collections import Counter
from xml.etree.ElementTree import parse

if __name__ == '__main__':
    potholes_by_zip = Counter()

    doc = parse('potholes.xml')
    for pothole in doc.iterfind('row/row'):
        potholes_by_zip[pothole.findtext('zip')] += 1

    for zipcode, num in potholes_by_zip.most_common():  # 按计数从大到小排列输出，如果使用potholes_by_zip.items()，则无序输出
        print(zipcode, num)
输出：
60647 3
60632 2
60648 1
```

这个脚本唯一的问题是它会先将整个XML文件加载到内存中然后解析。 在我的机器上，为了运行这个程序需要用到450MB左右的内存空间。 如果使用如下代码，程序只需要修改一点点：

```python
from collections import Counter

if __name__ == '__main__':
    potholes_by_zip = Counter()

    data = parse_and_remove('potholes.xml', 'row/row')
    for pothole in data:
        potholes_by_zip[pothole.findtext('zip')] += 1  # 这里的pothole是yield出来的第二个row节点，直接查找zip节点的text，也可以这样用pothole.find('zip').text或pothole.find('location').attrib.get('latitude')

    for zipcode, num in potholes_by_zip.most_common():  # 按计数从大到小排列输出，如果使用potholes_by_zip.items()，则无序输出
        print(zipcode, num)
```

结果是：这个版本的代码运行时只需要7MB的内存–大大节约了内存资源。

这一节的技术会依赖 ElementTree 模块中的两个核心功能。 第一，iterparse() 方法允许对XML文档进行增量操作。 使用时，你需要提供文件名和一个包含下面一种或多种类型的事件列表： start , end, start-ns 和 end-ns 。 由 iterparse() 创建的迭代器会产生形如 (event, elem) 的元组， 其中 event 是上述事件列表中的某一个，而 elem 是相应的XML元素。例如：

```python
data = iterparse('potholes.xml', ('start', 'end'))
print(next(data))
print(next(data))
print(next(data))
print(next(data))
print(next(data))
print(next(data))
print(next(data))
输出：
('start', <Element 'response' at 0x000002E89C9FBBD8>)
('start', <Element 'row' at 0x000002E8AD74DB38>)
('start', <Element 'row' at 0x000002E8AD7AE9A8>)
('start', <Element 'creation_date' at 0x000002E8AD7AE9F8>)
('end', <Element 'creation_date' at 0x000002E8AD7AE9F8>)
('start', <Element 'status' at 0x000002E8AD7BC098>)
('end', <Element 'status' at 0x000002E8AD7BC098>)
```

这本节例子中， start 和 end 事件被用来管理元素和标签栈。 栈代表了文档被解析时的层次结构， 还被用来判断某个元素是否匹配传给函数 parse_and_remove() 的路径。 如果匹配，就利用 yield 语句向调用者返回这个元素。

在 yield 之后的下面这个语句才是使得程序占用极少内存的ElementTree的核心特性：

```python
elem_stack[-2].remove(elem)
```

这个语句使得之前由 yield 产生的元素从它的父节点中删除掉。 假设已经没有其它的地方引用这个元素了，那么这个元素就被销毁并回收内存。

对节点的迭代式解析和删除的最终效果就是一个在文档上高效的增量式清扫过程。 文档树结构从始自终没被完整的创建过。尽管如此，还是能通过上述简单的方式来处理这个XML数据。

这种方案的主要缺陷就是它的运行性能了。 我自己测试的结果是，读取整个文档到内存中的版本的运行速度差不多是增量式处理版本的两倍快。 但是它却使用了超过后者60倍的内存。 因此，如果你更关心内存使用量的话，那么增量式的版本完胜。

注：etree使用样例学习参考：<https://www.jb51.net/article/67120.htm>

## 6.5. 将字典转换为XML

你想使用一个Python字典存储数据，并将它转换成XML格式。

尽管 xml.etree.ElementTree 库通常用来做解析工作，其实它也可以创建XML文档。 例如，考虑如下这个函数：

```python
from xml.etree.ElementTree import Element


def dict_to_xml(tag, d):
    '''
    Turn a simple dict of key/value paris into xml
    '''
    elem = Element(tag)
    for key, val in d.items():
        child = Element(key)
        child.text = str(val)
        elem.append(child)
    return elem
```

下面是一个使用例子：

```python
s = {'name': 'GOOG', 'shares': 100, 'price': 490.1}
e = dict_to_xml('stock', s)
print(e)
print(tostring(e))
输出：
<Element 'stock' at 0x000002B9DD33BA48>
b'<stock><name>GOOG</name><shares>100</shares><price>490.1</price></stock>'
格式化后：
<stock>
    <name>GOOG</name>
    <shares>100</shares>
    <price>490.1</price>
</stock>
```

转换结果是一个 Element 实例。对于I/O操作，使用 xml.etree.ElementTree 中的 tostring() 函数很容易就能将它转换成一个字节字符串。例如：

```python
>>> from xml.etree.ElementTree import tostring
>>> tostring(e)
b'<stock><price>490.1</price><shares>100</shares><name>GOOG</name></stock>'
```

如果你想给某个元素添加属性值，可以使用 set() 方法：

```python
>>> e.set('_id','1234')
>>> tostring(e)
b'<stock _id="1234"><price>490.1</price><shares>100</shares><name>GOOG</name>
</stock>'
```

如果你还想保持元素的顺序，可以考虑构造一个 OrderedDict 来代替一个普通的字典。请参考1.7小节。

当创建XML的时候，你被限制只能构造字符串类型的值。例如：

```python
def dict_to_xml_str(tag, d):
    '''
    Turn a simple dict of key/value paris into xml
    '''
    parts = ['<{}>'.format(tag)]
    for key, val in d.items():
        parts.append('<{0}>{1}</{0}>'.format(key, val))
    parts.append('</{}>'.format(tag))
    return ''.join(parts)
```

问题是如果你手动的去构造的时候可能会碰到一些麻烦。例如，当字典的值中包含一些特殊字符的时候会怎样呢？

```python
d = {'name': '<spam>'}
# String creation
print(dict_to_xml_str('item', d))

# Proper XML creation
e = dict_to_xml('item', d)
print(tostring(e))
输出：
<item><name><spam></name></item>
b'<item><name>&lt;spam&gt;</name></item>'
```

注意到程序的后面那个例子中，字符 ‘<’ 和 ‘>’ 被替换成了 &lt; 和 &gt;

下面仅供参考，如果你需要手动去转换这些字符， 可以使用 xml.sax.saxutils 中的 escape() 和 unescape() 函数。例如：

```python
escaped_str = escape('<spam>')
print(escaped_str)

unescaped_str = unescape(escaped_str)
print(unescaped_str)
输出：
&lt;spam&gt;
<spam>

# 使用交互式界面，_表示上一个未赋值变量
>>> from xml.sax.saxutils import escape, unescape
>>> escape('<spam>')
'&lt;spam&gt;'
>>> unescape(_)
'<spam>'

```


除了能创建正确的输出外，还有另外一个原因推荐你创建 Element 实例而不是字符串， 那就是使用字符串组合构造一个更大的文档并不是那么容易。 而 Element 实例可以不用考虑解析XML文本的情况下通过多种方式被处理。 也就是说，你可以在一个高级数据结构上完成你所有的操作，并在最后以字符串的形式将其输出。

## 6.6. 解析和修改XML

你想读取一个XML文档，对它最一些修改，然后将结果写回XML文档。

使用 xml.etree.ElementTree 模块可以很容易的处理这些任务。 第一步是以通常的方式来解析这个文档。例如，假设你有一个名为 pred.xml 的文档，类似下面这样：

```python
<?xml version="1.0"?>
<stop>
    <id>14791</id>
    <nm>Clark &amp; Balmoral</nm>
    <sri>
        <rt>22</rt>
        <d>North Bound</d>
        <dd>North Bound</dd>
    </sri>
    <cr>22</cr>
    <pre>
        <pt>5 MIN</pt>
        <fd>Howard</fd>
        <v>1378</v>
        <rn>22</rn>
    </pre>
    <pre>
        <pt>15 MIN</pt>
        <fd>Howard</fd>
        <v>1867</v>
        <rn>22</rn>
    </pre>
</stop>
```

下面是一个利用 ElementTree 来读取这个文档并对它做一些修改的例子：

```python
from xml.etree.ElementTree import parse, Element

if __name__ == '__main__':
    doc = parse('pred.xml')
    root = doc.getroot()
    print(root)

    # Remove a few elements
    root.remove(root.find('sri'))
    root.remove(root.find('cr'))
    # Insert a new element after <nm>...</nm>
    # nm_index = root.getchildren().index(root.find('nm'))  # 使用root.getchildren()有告警提示：DeprecationWarning: This method will be removed in future versions.  Use 'list(elem)' or iteration over elem instead.
    nm_index = list(root).index(root.find('nm'))
    print(nm_index)
    e = Element('spam')
    e.text = 'This is a test'
    root.insert(2, e)
    # Write back to a file
    doc.write('newpred.xml', xml_declaration=True)
```

处理结果是一个像下面这样新的XML文件：

```python
<?xml version='1.0' encoding='us-ascii'?>
<stop>
    <id>14791</id>
    <nm>Clark &amp; Balmoral</nm>
    <spam>This is a test</spam>
    <pre>
        <pt>5 MIN</pt>
        <fd>Howard</fd>
        <v>1378</v>
        <rn>22</rn>
    </pre>
    <pre>
        <pt>15 MIN</pt>
        <fd>Howard</fd>
        <v>1867</v>
        <rn>22</rn>
    </pre>
</stop>
```

修改一个XML文档结构是很容易的，但是你必须牢记的是所有的修改都是针对父节点元素， 将它作为一个列表来处理。例如，如果你删除某个元素，通过调用父节点的 remove() 方法从它的直接父节点中删除。 如果你插入或增加新的元素，你同样使用父节点元素的 insert() 和 append() 方法。 还能对元素使用索引和切片操作，比如 element[i] 或 element[i:j]

如果你需要创建新的元素，可以使用本节方案中演示的 Element 类。我们在6.5小节已经详细讨论过了。

## 6.7. 利用命名空间解析XML文档

你想解析某个XML文档，文档中使用了XML命名空间。

考虑下面这个使用了命名空间的文档：

```python
<?xml version="1.0" encoding="utf-8"?>
<top>
    <author>David Beazley</author>
    <content>
        <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <title>Hello World</title>
            </head>
            <body>
                <h1>Hello World!</h1>
            </body>
        </html>
    </content>
</top>
```

如果你解析这个文档并执行普通的查询，你会发现这个并不是那么容易，因为所有步骤都变得相当的繁琐。

```python
from xml.etree.ElementTree import parse

if __name__ == '__main__':
    doc = parse('namespace.xml')
    root = doc.getroot()

    print(doc.findtext('author'))
    print(doc.find('content'))

    # A query involving a namespace (doesn't work)
    print(doc.find('content/html'))

    # Works if fully qualified
    print(doc.find('content/{http://www.w3.org/1999/xhtml}html'))

    # Doesn't work
    print(doc.findtext('content/{http://www.w3.org/1999/xhtml}html/head/title'))

    # Fully qualified
    print(doc.findtext('content/{http://www.w3.org/1999/xhtml}html/'
                       '{http://www.w3.org/1999/xhtml}head/{http://www.w3.org/1999/xhtml}title'))
输出：
David Beazley
<Element 'content' at 0x0000014EDAFAEA48>
None
<Element '{http://www.w3.org/1999/xhtml}html' at 0x0000014EDB00F8B8>
None
Hello World
```

你可以通过将命名空间处理逻辑包装为一个工具类来简化这个过程：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from xml.etree.ElementTree import parse


class XmlNamespaces(object):
    def __init__(self, **kwargs):
        self.namespaces = {}
        for name, uri in kwargs.items():
            self.register(name, uri)

    def register(self, name, uri):
        self.namespaces[name] = '{' + uri + '}'

    def __call__(self, path):
        return path.format_map(self.namespaces)
```

通过下面的方式使用这个类：

```python
if __name__ == '__main__':
    ns = XmlNamespaces(html='http://www.w3.org/1999/xhtml')

    doc = parse('namespace.xml')
    print(doc.find(ns('content/{html}html')))
    print(doc.findtext(ns('content/{html}html/{html}head/{html}title')))
输出：
<Element '{http://www.w3.org/1999/xhtml}html' at 0x000001F34A97B048>
Hello World
```

解析含有命名空间的XML文档会比较繁琐。 上面的 XMLNamespaces 仅仅是允许你使用缩略名代替完整的URI将其变得稍微简洁一点。

很不幸的是，在基本的 ElementTree 解析中没有任何途径获取命名空间的信息。 但是，如果你使用 iterparse() 函数的话就可以获取更多关于命名空间处理范围的信息。例如：

```python
for evt, elem in iterparse('namespace.xml', ('end', 'start-ns', 'end-ns')):  # 如果只需要end事件信息，则定义event参数为('end',)
    print(evt, elem)
print(elem)  # This is the topmost element
输出：
end <Element 'author' at 0x000002127EB3F908>
start-ns ('', 'http://www.w3.org/1999/xhtml')
end <Element '{http://www.w3.org/1999/xhtml}title' at 0x000002127EB52048>
end <Element '{http://www.w3.org/1999/xhtml}head' at 0x000002127EB4DF98>
end <Element '{http://www.w3.org/1999/xhtml}h1' at 0x000002127EB520E8>
end <Element '{http://www.w3.org/1999/xhtml}body' at 0x000002127EB52098>
end <Element '{http://www.w3.org/1999/xhtml}html' at 0x000002127EB4DF48>
end-ns None
end <Element 'content' at 0x000002127EB3F958>
end <Element 'top' at 0x000002127EB00958>
<Element 'top' at 0x000001B76E3AF9A8>
```

最后一点，如果你要处理的XML文本除了要使用到其他高级XML特性外，还要使用到命名空间， 建议你最好是使用 lxml 函数库来代替 ElementTree 。 例如，lxml 对利用DTD验证文档、更好的XPath支持和一些其他高级XML特性等都提供了更好的支持。 这一小节其实只是教你如何让XML解析稍微简单一点。

## 6.8. 与关系型数据库的交互

你想在关系型数据库中查询、增加或删除记录。

Python中表示多行数据的标准方式是一个由元组构成的序列。例如：

```python
stocks = [
    ('GOOG', 100, 490.1),
    ('AAPL', 50, 545.75),
    ('FB', 150, 7.45),
    ('HPQ', 75, 33.2),
]
```

依据PEP249，通过这种形式提供数据， 可以很容易的使用Python标准数据库API和关系型数据库进行交互。 所有数据库上的操作都通过SQL查询语句来完成。每一行输入输出数据用一个元组来表示。

为了演示说明，你可以使用Python标准库中的 sqlite3 模块。 如果你使用的是一个不同的数据库(比如MySql、Postgresql或者ODBC)， 还得安装相应的第三方模块来提供支持。 不过相应的编程接口几乎都是一样的，除了一点点细微差别外。

第一步是连接到数据库。通常你要执行 connect() 函数， 给它提供一些数据库名、主机、用户名、密码和其他必要的一些参数。例如：

```python
>>> import sqlite3
>>> db = sqlite3.connect('database.db')
```

为了处理数据，下一步你需要创建一个游标。 一旦你有了游标，那么你就可以执行SQL查询语句了。比如：

```python
>>> c = db.cursor()
>>> c.execute('create table portfolio (symbol text, shares integer, price real)')
<sqlite3.Cursor object at 0x10067a730>
>>> db.commit()
```

为了向数据库表中插入多条记录，使用类似下面这样的语句：

```python
>>> c.executemany('insert into portfolio values (?,?,?)', stocks)
<sqlite3.Cursor object at 0x10067a730>
>>> db.commit()
```

为了执行某个查询，使用像下面这样的语句：

```python
>>> for row in db.execute('select * from portfolio'):
...     print(row)
...
('GOOG', 100, 490.1)
('AAPL', 50, 545.75)
('FB', 150, 7.45)
('HPQ', 75, 33.2)
```

如果你想接受用户输入作为参数来执行查询操作，必须确保你使用下面这样的占位符``?``来进行引用参数：

```python
>>> min_price = 100
>>> for row in db.execute('select * from portfolio where price >= ?',
                          (min_price,)):
...     print(row)
...
('GOOG', 100, 490.1)
('AAPL', 50, 545.75)
```

本地学习的完整程序：

```python
import sqlite3

if __name__ == '__main__':
    db = sqlite3.connect('database.db')
    c = db.cursor()

    # 创建表
    # c.execute('create table portfolio (symbol text, shares integer, price real)')
    # db.commit()

    # stocks = [
    #     ('GOOG', 100, 490.1),
    #     ('AAPL', 50, 545.75),
    #     ('FB', 150, 7.45),
    #     ('HPQ', 75, 33.2),
    # ]
    # # 插入多条记录
    # c.executemany('insert into portfolio values (?,?,?)', stocks)
    # db.commit()

    # 查询数据
    # for row in db.execute('select * from portfolio'):
    #     print(row)

    # 条件查询数据
    min_price = 100
    for row in db.execute('select * from portfolio where price >= ?', (min_price,)):
        print(row)
```

在比较低的级别上和数据库交互是非常简单的。 你只需提供SQL语句并调用相应的模块就可以更新或提取数据了。 虽说如此，还是有一些比较棘手的细节问题需要你逐个列出去解决。

一个难点是数据库中的数据和Python类型直接的映射。 对于日期类型，通常可以使用 datetime 模块中的 datetime 实例， 或者可能是 time 模块中的系统时间戳。 对于数字类型，特别是使用到小数的金融数据，可以用 decimal 模块中的 Decimal 实例来表示。 不幸的是，对于不同的数据库而言具体映射规则是不一样的，你必须参考相应的文档。

另外一个更加复杂的问题就是SQL语句字符串的构造。 你千万不要使用Python字符串格式化操作符(如%)或者 .format() 方法来创建这样的字符串。 如果传递给这些格式化操作符的值来自于用户的输入，那么你的程序就很有可能遭受SQL注入攻击(参考 <http://xkcd.com/327> )。 查询语句中的通配符 ? 指示后台数据库使用它自己的字符串替换机制，这样更加的安全。

不幸的是，不同的数据库后台对于通配符的使用是不一样的。大部分模块使用 ? 或 %s ， 还有其他一些使用了不同的符号，比如:0或:1来指示参数。 同样的，你还是得去参考你使用的数据库模块相应的文档。 一个数据库模块的 paramstyle 属性包含了参数引用风格的信息。

对于简单的数据库数据的读写问题，使用数据库API通常非常简单。 如果你要处理更加复杂的问题，建议你使用更加高级的接口，比如一个对象关系映射ORM所提供的接口。 类似 SQLAlchemy 这样的库允许你使用Python类来表示一个数据库表， 并且能在隐藏底层SQL的情况下实现各种数据库的操作。

## 6.9. 编码和解码十六进制数

你想将一个十六进制字符串解码成一个字节字符串或者将一个字节字符串编码成一个十六进制字符串。

如果你只是简单的解码或编码一个十六进制的原始字符串，可以使用　binascii 模块。例如：

```python
import binascii

if __name__ == '__main__':
    # Initial byte string
    s = b'hello'
    # Encode as hex
    h = binascii.b2a_hex(s)
    print(h)
    # Decode back to bytes
    print(binascii.a2b_hex(h))  # print(base64.b16decode(h.upper())) 如果使用base64.b16decode转成二进制，必须是大写字母
输出：
b'68656c6c6f'
b'hello'
```

类似的功能同样可以在 base64 模块中找到。例如：

```python
h = base64.b16encode(s)
print(h)
print(base64.b16decode(h))
输出：
b'68656C6C6F'
b'hello'
```

注：可以在<http://ascii.911cha.com/>查看每个字符的ASCII表中的对应二进制、十进制、十六进制值。

大部分情况下，通过使用上述的函数来转换十六进制是很简单的。 上面两种技术的主要不同在于大小写的处理。 函数 base64.b16decode() 和 base64.b16encode() 只能操作大写形式的十六进制字母， 而 binascii 模块中的函数大小写都能处理。

还有一点需要注意的是编码函数所产生的输出总是一个字节字符串。 如果想强制以Unicode形式输出，你需要增加一个额外的界面步骤。例如：

```python
>>> h = base64.b16encode(s)
>>> print(h)
b'68656C6C6F'
>>> print(h.decode('ascii'))
68656C6C6F
```

在解码十六进制数时，函数 b16decode() 和 a2b_hex() 可以接受字节或unicode字符串。 但是，unicode字符串必须仅仅只包含ASCII编码的十六进制数。

```python
import base64
import binascii

if __name__ == '__main__':
    s = b'hello'
    h = base64.b16encode(s)
    print(h)
    h_str = h.decode('ascii')
    print(h_str)
    print(f'base64.b16decode: {base64.b16decode(h)}, {base64.b16decode(h_str)}')
    print(f'binascii.a2b_hex: {binascii.a2b_hex(h)}, {binascii.a2b_hex(h_str)}')
```

## 6.10. 编码解码Base64数据

你需要使用Base64格式解码或编码二进制数据。

base64 模块中有两个函数 b64encode() and b64decode() 可以帮你解决这个问题。例如;

```python
import base64

if __name__ == '__main__':
    s = b'hello'
    # Encode as Base64
    a = base64.b64encode(s)
    print(a)

    # Decode from Base64
    print(base64.b64decode(a))
输出：
b'aGVsbG8='
b'hello'
```

Base64编码仅仅用于面向字节的数据比如字节字符串和字节数组。 此外，编码处理的输出结果总是一个字节字符串。 如果你想混合使用Base64编码的数据和Unicode文本，你必须添加一个额外的解码步骤。例如：

```python
>>> a = base64.b64encode(s).decode('ascii')
>>> a
'aGVsbG8='
```

当解码Base64的时候，字节字符串和Unicode文本都可以作为参数。 但是，Unicode字符串只能包含ASCII字符。

## 6.11. 读写二进制数组数据

你想读写一个二进制数组的结构化数据到Python元组中。

可以使用 struct 模块处理二进制数据。 下面是一段示例代码将一个Python元组列表写入一个二进制文件，并使用 struct 将每个元组编码为一个结构体。

```python
from struct import Struct


def write_records(records, format, f):
    """
    Write a sequence of tuples to a binary file of structures.
    """
    record_struct = Struct(format)
    for r in records:
        f.write(record_struct.pack(*r))


if __name__ == '__main__':
    records = [(1, 2.3, 4.5),
               (6, 7.8, 9.0),
               (12, 13.4, 56.7)]
    with open('data.b', 'wb') as f:
        write_records(records, '<idd', f)
```

有很多种方法来读取这个文件并返回一个元组列表。 首先，如果你打算以块的形式增量读取文件，你可以这样做：

```python
from struct import Struct


def read_records(format, f):
    record_struct = Struct(format)
    chunks = iter(lambda : f.read(record_struct.size), b'')
    return [record_struct.unpack(chunk) for chunk in chunks]  # 可以用生成器:(record_struct.unpack(chunk) for chunk in chunks) 但是对rec的所有操作只能在with范围内


if __name__ == '__main__':
    with open('data.b', 'rb') as f:
        # Process rec
        for rec in read_records('<idd', f):
            print(rec)
输出：
(1, 2.3, 4.5)
(6, 7.8, 9.0)
(12, 13.4, 56.7)
```

如果你想将整个文件一次性读取到一个字节字符串中，然后在分片解析。那么你可以这样做：

```python
from struct import Struct


def unpack_records(format, data):
    record_struct = Struct(format)
    return [record_struct.unpack_from(data, offset) for offset in range(0, len(data), record_struct.size)]


if __name__ == '__main__':
    with open('data.b', 'rb') as f:
        data = f.read()
    for rec in unpack_records('<idd', data):
        # Process rec
        print(rec)
输出：
(1, 2.3, 4.5)
(6, 7.8, 9.0)
(12, 13.4, 56.7)
```

两种情况下的结果都是一个可返回用来创建该文件的原始元组的可迭代对象。

对于需要编码和解码二进制数据的程序而言，通常会使用 struct 模块。 为了声明一个新的结构体，只需要像这样创建一个 Struct 实例即可：

```python
# Little endian 32-bit integer, two double precision floats
record_struct = Struct('<idd')
```

结构体通常会使用一些结构码值i, d, f等 [参考 Python文档 ]。 这些代码分别代表某个特定的二进制数据类型如32位整数，64位浮点数，32位浮点数等。 第一个字符 < 指定了字节顺序。在这个例子中，它表示”低位在前”。 更改这个字符为 > 表示高位在前，或者是 ! 表示网络字节顺序。

产生的 Struct 实例有很多属性和方法用来操作相应类型的结构。 size 属性包含了结构的字节数，这在I/O操作时非常有用。 pack() 和 unpack() 方法被用来打包和解包数据。比如：

```python
>>> from struct import Struct
>>> record_struct = Struct('<idd')
>>> record_struct.size
20
>>> record_struct.pack(1, 2.0, 3.0)
b'\x01\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00@\x00\x00\x00\x00\x00\x00\x08@'
>>> record_struct.unpack(_)
(1, 2.0, 3.0)
```

有时候你还会看到 pack() 和 unpack() 操作以模块级别函数被调用，类似下面这样：

```python
>>> from struct import Struct
>>> record_struct = Struct('<idd')
>>> record_struct.size
20
>>> record_struct.pack(1, 2.0, 3.0)
b'\x01\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00@\x00\x00\x00\x00\x00\x00\x08@'
>>> record_struct.unpack(_)
(1, 2.0, 3.0)

# 4个样例：
s = struct.pack('<iddd', 1, 2.0, 3.0, 4.0)
print(s)
print(struct.unpack('<iddd', s))
输出：
b'\x01\x00\x00\x00\x00\x00\x00\x00\x00\x00\x00@\x00\x00\x00\x00\x00\x00\x08@\x00\x00\x00\x00\x00\x00\x10@'
(1, 2.0, 3.0, 4.0)
```

这样可以工作，但是感觉没有实例方法那么优雅，特别是在你代码中同样的结构出现在多个地方的时候。 通过创建一个 Struct 实例，格式代码只会指定一次并且所有的操作被集中处理。 这样一来代码维护就变得更加简单了(因为你只需要改变一处代码即可)。

读取二进制结构的代码要用到一些非常有趣而优美的编程技巧。 在函数　read_records 中，iter() 被用来创建一个返回固定大小数据块的迭代器，参考5.8小节。 这个迭代器会不断的调用一个用户提供的可调用对象(比如 lambda: f.read(record_struct.size) )， 直到它返回一个特殊的值(如b'')，这时候迭代停止。例如：

```python
>>> f = open('data.b', 'rb')
>>> chunks = iter(lambda: f.read(20), b'')
>>> chunks
<callable_iterator object at 0x10069e6d0>
>>> for chk in chunks:
... print(chk)
...
b'\x01\x00\x00\x00ffffff\x02@\x00\x00\x00\x00\x00\x00\x12@'
b'\x06\x00\x00\x00333333\x1f@\x00\x00\x00\x00\x00\x00"@'
b'\x0c\x00\x00\x00\xcd\xcc\xcc\xcc\xcc\xcc*@\x9a\x99\x99\x99\x99YL@'
```

如你所见，创建一个可迭代对象的一个原因是它能允许使用一个生成器推导来创建记录。 如果你不使用这种技术，那么代码可能会像下面这样：

```python
def read_records(format, f):
    record_struct = Struct(format)
    while True:
        chk = f.read(record_struct.size)
        if chk == b'':
            break
        yield record_struct.unpack(chk)
```

在函数 unpack_records() 中使用了另外一种方法 unpack_from() 。 unpack_from() 对于从一个大型二进制数组中提取二进制数据非常有用， 因为它不会产生任何的临时对象或者进行内存复制操作。 你只需要给它一个字节字符串(或数组)和一个字节偏移量，它会从那个位置开始直接解包数据。

如果你使用 unpack() 来代替 unpack_from() ， 你需要修改代码来构造大量的小的切片以及进行偏移量的计算。比如：

```python
def unpack_records(format, data):
    record_struct = Struct(format)
    return (record_struct.unpack(data[offset:offset + record_struct.size])
            for offset in range(0, len(data), record_struct.size))
```

这种方案除了代码看上去很复杂外，还得做很多额外的工作，因为它执行了大量的偏移量计算， 复制数据以及构造小的切片对象。 如果你准备从读取到的一个大型字节字符串中解包大量的结构体的话，unpack_from() 会表现的更出色。

在解包的时候，collections 模块中的命名元组对象或许是你想要用到的。 它可以让你给返回元组设置属性名称。例如：

```python
from collections import namedtuple

Record = namedtuple('Record', ['kind', 'x', 'y'])

with open('data.b', 'rb') as f:
    records = (Record(*rec) for rec in read_records('<idd', f))

for r in records:
    print(r.kind, r.x, r.y)
```

如果你的程序需要处理大量的二进制数据，你最好使用 numpy 模块。 例如，你可以将一个二进制数据读取到一个结构化数组中而不是一个元组列表中。就像下面这样：

```python
    with open('data.b', 'rb') as f:
        records = np.fromfile(f, dtype='<i,<d,<d')

    print(records)
    print(records[0])
    print(records[1])
输出：
[( 1,  2.3,  4.5) ( 6,  7.8,  9. ) (12, 13.4, 56.7)]
(1, 2.3, 4.5)
(6, 7.8, 9.)
```

最后提一点，如果你需要从已知的文件格式(如图片格式，图形文件，HDF5等)中读取二进制数据时， 先检查看看Python是不是已经提供了现存的模块。因为不到万不得已没有必要去重复造轮子。

## 6.12. 读取嵌套和可变长二进制数据（TODO）

你需要读取包含嵌套或者可变长记录集合的复杂二进制格式的数据。这些数据可能包含图片、视频、电子地图文件等。

## 6.13. 数据的累加与统计操作

你需要处理一个很大的数据集并需要计算数据总和或其他统计量。

对于任何涉及到统计、时间序列以及其他相关技术的数据分析问题，都可以考虑使用 Pandas库 。

为了让你先体验下，下面是一个使用Pandas来分析芝加哥城市的 老鼠和啮齿类动物数据库 的例子。 在我写这篇文章的时候，这个数据库是一个拥有大概74,000行数据的CSV文件。

```python
import pandas

if __name__ == '__main__':
    # Read a CSV file, skipping last line
    # stocks = pandas.read_csv('stocks.csv', skipfooter=1, engine='python')
    stocks = pandas.read_csv('stocks.csv')
    print(f'stocks is: \n{stocks}')

    print('=============================')

    # Investigate range of values for a certain field
    print(f'stocks["Volume"].unique() is: \n{stocks["Volume"].unique()}')

    print('=============================')

    # Filter the data
    symbol_aa = stocks[stocks['Symbol'] == 'AA']
    print(f'symbol_aa is: \n{symbol_aa}')

    volume_list = stocks[stocks['Volume'] == 225400]
    print(f'volume_list is: \n{volume_list}')

    print('=============================')

    print(f'stocks["Volume"].value_counts() is: \n{stocks["Volume"].value_counts()}')  # stocks['Volume'].value_counts()[:3] 取前3

    print('=============================')

    volume_groupby = stocks.groupby('Volume')
    print(f'volume_groupby is: {volume_groupby}, len is: {len(volume_groupby)}')

    volume_counts = volume_groupby.size()
    print(f'volume_counts is: \n{volume_counts}')
输出：
stocks is: 
  Symbol  Price       Date    Time  Change  Volume
0     AA  39.48  6/11/2007  9:36am   -0.18  181800
1    AIG  71.38  6/11/2007  9:36am   -0.15  195500
2    AXP  62.58  6/11/2007  9:36am   -0.46  935000
3     BA  98.31  6/11/2007  9:36am    0.12  104800
4      C  53.08  6/11/2007  9:36am   -0.25  360900
5    CAT  78.29  6/11/2007  9:36am   -0.23  225400
6     OP  78.29  6/11/2007  9:36am   -0.23  225400
7     MN  78.29  6/11/2007  9:36am   -0.23  225400
8     DE  53.08  6/11/2007  9:36am   -0.25  360900
=============================
stocks["Volume"].unique() is: 
[181800 195500 935000 104800 360900 225400]
=============================
symbol_aa is: 
  Symbol  Price       Date    Time  Change  Volume
0     AA  39.48  6/11/2007  9:36am   -0.18  181800
volume_list is: 
  Symbol  Price       Date    Time  Change  Volume
5    CAT  78.29  6/11/2007  9:36am   -0.23  225400
6     OP  78.29  6/11/2007  9:36am   -0.23  225400
7     MN  78.29  6/11/2007  9:36am   -0.23  225400
=============================
stocks["Volume"].value_counts() is: 
225400    3
360900    2
195500    1
181800    1
935000    1
104800    1
Name: Volume, dtype: int64
=============================
volume_groupby is: <pandas.core.groupby.generic.DataFrameGroupBy object at 0x000002671B16CCF8>, len is: 6
volume_counts is: 
Volume
104800    1
181800    1
195500    1
225400    3
360900    2
935000    1
dtype: int64
```

Pandas是一个拥有很多特性的大型函数库，我在这里不可能介绍完。 但是只要你需要去分析大型数据集合、对数据分组、计算各种统计量或其他类似任务的话，这个函数库真的值得你去看一看。

# 7. 函数

使用 def 语句定义函数是所有程序的基础。 本章的目标是讲解一些更加高级和不常见的函数定义与使用模式。 涉及到的内容包括默认参数、任意数量参数、强制关键字参数、注解和闭包。 另外，一些高级的控制流和利用回调函数传递数据的技术在这里也会讲解到。

## 7.1. 可接受任意数量参数的函数

你想构造一个可接受任意数量参数的函数。

为了能让一个函数接受任意数量的位置参数，可以使用一个*参数。例如：

```python
def avg(first, *rest):
    return (first + sum(rest)) / (1 + len(rest))

# Sample use
avg(1, 2) # 1.5
avg(1, 2, 3, 4) # 2.5
```

在这个例子中，rest是由所有其他位置参数组成的元组。然后我们在代码中把它当成了一个序列来进行后续的计算。

为了接受任意数量的关键字参数，使用一个以**开头的参数。比如：

```python
import html


def make_element(name, value, **attrs):
    keyvals = [' %s="%s"' % item for item in attrs.items()]
    attr_str = ''.join(keyvals)
    element = '<{name}{attrs}>{value}</{name}>'.format(
        name=name,
        attrs=attr_str,
        value=html.escape(value))
    return element


if __name__ == '__main__':
    # Example
    # Creates '<item size="large" quantity="6">Albatross</item>'
    print(make_element('item', 'Albatross', size='large', quantity=6))

    # Creates '<p>&lt;spam&gt;</p>'
    print(make_element('p', '<spam>'))
输出：
<item size="large" quantity="6">Albatross</item>
<p>&lt;spam&gt;</p>
```

在这里，attrs是一个包含所有被传入进来的关键字参数的字典。

如果你还希望某个函数能同时接受任意数量的位置参数和关键字参数，可以同时使用\*和\*\*。比如：

```python
def anyargs(*args, **kwargs):
    print(args)  # A tuple
    print(kwargs)  # A dict


if __name__ == '__main__':
    anyargs(1, 2, 3)
    anyargs(1, 2, 3, a='aaa', b='bbb')

    d = {str(i): str(i) for i in range(0, 5)}
    anyargs(**d)
输出：
(1, 2, 3)
{}
(1, 2, 3)
{'a': 'aaa', 'b': 'bbb'}
()
{'0': '0', '1': '1', '2': '2', '3': '3', '4': '4'}
```

使用这个函数时，所有位置参数会被放到args元组中，所有关键字参数会被放到字典kwargs中。

一个\*参数只能出现在函数定义中最后一个位置参数后面，而 \*\*参数只能出现在最后一个参数。 有一点要注意的是，在\*参数后面仍然可以定义其他参数。

```python
def a(x, *args, y):
    print(x)
    print(args)
    print(y)

def b(x, *args, y, **kwargs):
    pass

if __name__ == '__main__':
    a(1, 2, 3, 4, y=True)  # 如果调用为a(1, 2, 3, 4)  报错：TypeError: a() missing 1 required keyword-only argument: 'y'
```

这种参数就是我们所说的强制关键字参数，在后面7.2小节还会详细讲解到。

## 7.2. 只接受关键字参数的函数

你希望函数的某些参数强制使用关键字参数传递

将强制关键字参数放到某个\*参数或者单个\*后面就能达到这种效果。比如：

```python
def recv(maxsize, *, block):
    'Receives a message'
    pass

recv(1024, True) # TypeError
recv(1024, block=True) # Ok
```

利用这种技术，我们还能在接受任意多个位置参数的函数中指定关键字参数。比如：

```python
def minimum(*values, clip=None):
    m = min(values)
    if clip is not None:
        m = clip if clip > m else m
    return m

minimum(1, 5, 2, -5, 10) # Returns -5
minimum(1, 5, 2, -5, 10, clip=0) # Returns 0
```

很多情况下，使用强制关键字参数会比使用位置参数表意更加清晰，程序也更加具有可读性。 例如，考虑下如下一个函数调用：

```python
msg = recv(1024, False)
```

如果调用者对recv函数并不是很熟悉，那他肯定不明白那个False参数到底来干嘛用的。 但是，如果代码变成下面这样子的话就清楚多了：

```python
msg = recv(1024, block=False)
```

另外，使用强制关键字参数也会比使用\*\*kwargs参数更好，因为在使用函数help的时候输出也会更容易理解：

```python
>>> help(recv)
Help on function recv in module __main__:
recv(maxsize, *, block)
    Receives a message
```

强制关键字参数在一些更高级场合同样也很有用。 例如，它们可以被用来在使用\*args和\*\*kwargs参数作为输入的函数中插入参数，9.11小节有一个这样的例子。

## 7.3. 给函数参数增加元信息

你写好了一个函数，然后想为这个函数的参数增加一些额外的信息，这样的话其他使用者就能清楚的知道这个函数应该怎么使用。

使用函数参数注解是一个很好的办法，它能提示程序员应该怎样正确使用这个函数。 例如，下面有一个被注解了的函数：

```python
def add(x:int, y:int) -> int:
    return x + y
```

python解释器不会对这些注解添加任何的语义。它们不会被类型检查，运行时跟没有加注解之前的效果也没有任何差距。 然而，对于那些阅读源码的人来讲就很有帮助啦。第三方工具和框架可能会对这些注解添加语义。同时它们也会出现在文档中。

```python
>>> help(add)
Help on function add in module __main__:
add(x: int, y: int) -> int
```

尽管你可以使用任意类型的对象给函数添加注解(例如数字，字符串，对象实例等等)，不过通常来讲使用类或者字符串会比较好点。

函数注解只存储在函数的 \_\_annotations\_\_ 属性中。例如：

```python
def add(x: int, y: int) -> int:
    return x + y


def add2(x, y):
    return x + y


if __name__ == '__main__':
    print(add.__annotations__)
    print(add2.__annotations__)
输出：
{'x': <class 'int'>, 'y': <class 'int'>, 'return': <class 'int'>}
{}
```

尽管注解的使用方法可能有很多种，但是它们的主要用途还是文档。 因为python并没有类型声明，通常来讲仅仅通过阅读源码很难知道应该传递什么样的参数给这个函数。 这时候使用注解就能给程序员更多的提示，让他们可以正确的使用函数。

参考9.20小节的一个更加高级的例子，演示了如何利用注解来实现多分派(比如重载函数)。

## 7.4. 返回多个值的函数

你希望构造一个可以返回多个值的函数

为了能返回多个值，函数直接return一个元组就行了。例如：

```python
>>> def myfun():
... return 1, 2, 3
...
>>> a, b, c = myfun()
>>> a
1
>>> b
2
>>> c
```

尽管myfun()看上去返回了多个值，实际上是先创建了一个元组然后返回的。 这个语法看上去比较奇怪，实际上我们使用的是逗号来生成一个元组，而不是用括号。比如下面的：

```python
>>> a = (1, 2) # With parentheses
>>> a
(1, 2)
>>> b = 1, 2 # Without parentheses
>>> b
(1, 2)
```

当我们调用返回一个元组的函数的时候 ，通常我们会将结果赋值给多个变量，就像上面的那样。 其实这就是1.1小节中我们所说的元组解包。返回结果也可以赋值给单个变量， 这时候这个变量值就是函数返回的那个元组本身了：

```python
>>> x = myfun()
>>> x
(1, 2, 3)
```

## 7.5. 定义有默认参数的函数

你想定义一个函数或者方法，它的一个或多个参数是可选的并且有一个默认值。

定义一个有可选参数的函数是非常简单的，直接在函数定义中给参数指定一个默认值，并放到参数列表最后就行了。例如：

```python
def spam(a, b=42):
    print(a, b)

spam(1) # Ok. a=1, b=42
spam(1, 2) # Ok. a=1, b=2
```

如果默认参数是一个可修改的容器比如一个列表、集合或者字典，可以使用None作为默认值，就像下面这样：

```python
# Using a list as a default value
def spam(a, b=None):
    if b is None:
        b = []
        # ...
```

如果你并不想提供一个默认值，而是想仅仅测试下某个默认参数是不是有传递进来，可以像下面这样写：

```python
_no_value = object()


def spam(a, b=_no_value):
    if b is _no_value:
        print('No b value supplied')
    print(a, b)


if __name__ == '__main__':
    spam(1, object())
输出：
1 <object object at 0x000002427F640AD0>
```

测试object对象：

```python
a = object()
b = object()
print(a)
print(b)
print(a is b)
print(object())
print(object())
print(object() is object())
输出：
<object object at 0x0000014D572B1AD0>
<object object at 0x0000014D572B1AE0>
False
<object object at 0x0000014D572B1AF0>
<object object at 0x0000014D572B1AF0>
False
```

我们测试下这个函数：

```python
spam(1)
spam(1, 2)
spam(1, None)
输出：
No b value supplied
1 <object object at 0x0000023CE11E0AC0>
1 2
1 None
```

仔细观察可以发现到传递一个None值和不传值两种情况是有差别的。

定义带默认值参数的函数是很简单的，但绝不仅仅只是这个，还有一些东西在这里也深入讨论下。

首先，默认参数的值仅仅在函数定义的时候赋值一次。试着运行下面这个例子：

```python
>>> x = 42
>>> def spam(a, b=x):
...     print(a, b)
...
>>> spam(1)
1 42
>>> x = 23 # Has no effect
>>> spam(1)
```

注意到当我们改变x的值的时候对默认参数值并没有影响，这是因为在函数定义的时候就已经确定了它的默认值了。

其次，默认参数的值应该是不可变的对象，比如None、True、False、数字或字符串。 特别的，千万不要像下面这样写代码：

```python
def spam(a, b=[]): # NO!
    # ...
```

如果你这么做了，当默认值在其他地方被修改后你将会遇到各种麻烦。这些修改会影响到下次调用这个函数时的默认值。比如：

```python
>>> def spam(a, b=[]):
...     print(b)
...     return b
...
>>> x = spam(1)
>>> x
[]
>>> x.append(99)
>>> x.append('Yow!')
>>> x
[99, 'Yow!']
>>> spam(1) # Modified list gets returned!
[99, 'Yow!']
```

这种结果应该不是你想要的。为了避免这种情况的发生，最好是将默认值设为None， 然后在函数里面检查它，前面的例子就是这样做的。

在测试None值时使用 is 操作符是很重要的，也是这种方案的关键点。 有时候大家会犯下下面这样的错误：

```python
def spam(a, b=None):
    if not b:  # NO! Use 'b is None' instead
        print('b is None')

if __name__ == '__main__':
    spam(1, None)
    spam(1, [])
    spam(1, 0)
    spam(1, '')
输出：
b is None
b is None
b is None
b is None
```

这么写的问题在于尽管None值确实是被当成False， 但是还有其他的对象(比如长度为0的字符串、列表、元组、字典等)都会被当做False。 因此，上面的代码会误将一些其他输入也当成是没有输入。如上。

最后一个问题比较微妙，那就是一个函数需要测试某个可选参数是否被使用者传递进来。 这时候需要小心的是你不能用某个默认值比如None、 0或者False值来测试用户提供的值(因为这些值都是合法的值，是可能被用户传递进来的)。 因此，你需要其他的解决方案了。

为了解决这个问题，你可以创建一个独一无二的私有对象实例，就像上面的_no_value变量那样。 在函数里面，你可以通过检查被传递参数值跟这个实例是否一样来判断。 这里的思路是用户不可能去传递这个_no_value实例作为输入。 因此，这里通过检查这个值就能确定某个参数是否被传递进来了。

这里对 object() 的使用看上去有点不太常见。object 是python中所有类的基类。 你可以创建 object 类的实例，但是这些实例没什么实际用处，因为它并没有任何有用的方法， 也没有任何实例数据(因为它没有任何的实例字典，你甚至都不能设置任何属性值)。 你唯一能做的就是测试同一性。这个刚好符合我的要求，因为我在函数中就只是需要一个同一性的测试而已。

## 7.6. 定义匿名或内联函数

你想为 sort() 操作创建一个很短的回调函数，但又不想用 def 去写一个单行函数， 而是希望通过某个快捷方式以内联方式来创建这个函数。

当一些函数很简单，仅仅只是计算一个表达式的值的时候，就可以使用lambda表达式来代替了。比如：

```python
>>> add = lambda x, y: x + y
>>> add(2,3)
5
>>> add('hello', 'world')
'helloworld'
```

这里使用的lambda表达式跟下面的效果是一样的：

```python
>>> def add(x, y):
...     return x + y
...
>>> add(2,3)
5
```

lambda表达式典型的使用场景是排序或数据reduce等：

```python
names = ['David Beazley', 'Brian Jones', 'Raymond Hettinger', 'Ned Batchelder']
sorted_names = sorted(names, key=lambda name: name.split()[-1].lower())
print(sorted_names)
输出：
['Ned Batchelder', 'David Beazley', 'Raymond Hettinger', 'Brian Jones']
```

尽管lambda表达式允许你定义简单函数，但是它的使用是有限制的。 你只能指定单个表达式，它的值就是最后的返回值。也就是说不能包含其他的语言特性了， 包括多个语句、条件表达式、迭代以及异常处理等等。

你可以不使用lambda表达式就能编写大部分python代码。 但是，当有人编写大量计算表达式值的短小函数或者需要用户提供回调函数的程序的时候， 你就会看到lambda表达式的身影了。

## 7.7. 匿名函数捕获变量值

你用lambda定义了一个匿名函数，并想在定义时捕获到某些变量的值。

先看下下面代码的效果：

```python
>>> x = 10
>>> a = lambda y: x + y
>>> x = 20
>>> b = lambda y: x + y
```

现在我问你，a(10)和b(10)返回的结果是什么？如果你认为结果是20和30，那么你就错了：

```python
>>> a(10)
30
>>> b(10)
30
```

这其中的奥妙在于lambda表达式中的x是一个自由变量， 在运行时绑定值，而不是定义时就绑定，这跟函数的默认值参数定义是不同的。 因此，在调用这个lambda表达式的时候，x的值是执行时的值。例如：

```python
>>> x = 15
>>> a(10)
25
>>> x = 3
>>> a(10)
13
```

如果你想让某个匿名函数在定义时就捕获到值，可以将那个参数值定义成默认参数即可，就像下面这样：

```python
>>> x = 10
>>> a = lambda y, x=x: x + y
>>> x = 20
>>> b = lambda y, x=x: x + y
>>> a(10)
20
>>> b(10)
30
```

在这里列出来的问题是新手很容易犯的错误，有些新手可能会不恰当的使用lambda表达式。 比如，通过在一个循环或列表推导中创建一个lambda表达式列表，并期望函数能在定义时就记住每次的迭代值。例如：

```python
funcs = [lambda x: x + n for n in range(5)]
for f in funcs:
    print(f(0))
输出：
4
4
4
4
4
```

但是实际效果是运行是n的值为迭代的最后一个值。现在我们用另一种方式修改一下：

```python
funcs = [lambda x, n=n: x + n for n in range(5)]
for f in funcs:
    print(f(0))
输出：
0
1
2
3
4
```

通过使用函数默认值参数形式，lambda函数在定义时就能绑定到值。

## 7.8. 减少可调用对象的参数个数

你有一个被其他python代码使用的callable对象，可能是一个回调函数或者是一个处理器， 但是它的参数太多了，导致调用时出错。

如果需要减少某个函数的参数个数，你可以使用 functools.partial() 。 partial() 函数允许你给一个或多个参数设置固定的值，减少接下来被调用时的参数个数。 为了演示清楚，假设你有下面这样的函数：

```python
def spam(a, b, c, d):
    print(a, b, c, d)
```

现在我们使用 partial() 函数来固定某些参数值：

```python
>>> from functools import partial
>>> s1 = partial(spam, 1) # a = 1
>>> s1(2, 3, 4)
1 2 3 4
>>> s1(4, 5, 6)
1 4 5 6
>>> s2 = partial(spam, d=42) # d = 42
>>> s2(1, 2, 3)
1 2 3 42
>>> s2(4, 5, 5)
4 5 5 42
>>> s3 = partial(spam, 1, 2, d=42) # a = 1, b = 2, d = 42
>>> s3(3)
1 2 3 42
>>> s3(4)
1 2 4 42
>>> s3(5)
1 2 5 42
```

可以看出 partial() 固定某些参数并返回一个新的callable对象。这个新的callable接受未赋值的参数， 然后跟之前已经赋值过的参数合并起来，最后将所有参数传递给原始函数。

本节要解决的问题是让原本不兼容的代码可以一起工作。下面我会列举一系列的例子。

第一个例子是，假设你有一个点的列表来表示(x,y)坐标元组。 你可以使用下面的函数来计算两点之间的距离：

```python
# points = [(i, i + 1) for i in range(1, 8, 2)]
points = [ (1, 2), (3, 4), (5, 6), (7, 8) ]

import math
def distance(p1, p2):
    x1, y1 = p1
    x2, y2 = p2
    return math.hypot(x2 - x1, y2 - y1)
```

现在假设你想以某个点为基点，根据点和基点之间的距离来排序所有的这些点。 列表的 sort() 方法接受一个关键字参数来自定义排序逻辑， 但是它只能接受一个单个参数的函数(distance()很明显是不符合条件的)。 现在我们可以通过使用 partial() 来解决这个问题：

```python
points = [(i, i + 1) for i in range(1, 8, 2)]
pt = (4, 3)
points.sort(key=partial(distance, pt))  # 同points = sorted(points, key=partial(distance, pt))
print(points)
输出：
[(3, 4), (1, 2), (5, 6), (7, 8)]
```

更进一步，partial() 通常被用来微调其他库函数所使用的回调函数的参数。 例如，下面是一段代码，使用 multiprocessing 来异步计算一个结果值， 然后这个值被传递给一个接受一个result值和一个可选logging参数的回调函数：

```python
import logging
from functools import partial
from multiprocessing.pool import Pool


def output_result(result, log=None):
    if log is not None:
        log.debug('Got: %r', result)  # log.info('Got: %r', result)


def output_result_to_console(result):
    print(f'result is {result}')


# A Sample function
def add(x, y):
    return x + y


if __name__ == '__main__':
    logging.basicConfig(level=logging.DEBUG)  # logging.basicConfig(level=logging.INFO)
    log = logging.getLogger('test')

    p = Pool()
    p.apply_async(add, (3, 4), callback=partial(output_result, log=log))
    p.apply_async(add, (3, 6), callback=output_result_to_console)
    p.close()
    p.join()
输出：
DEBUG:test:Got: 7
result is 9
```

当给 apply_async() 提供回调函数时，通过使用 partial() 传递额外的 logging 参数。 而 multiprocessing 对这些一无所知——它仅仅只是使用单个值来调用回调函数。

作为一个类似的例子，考虑下编写网络服务器的问题，socketserver 模块让它变得很容易。 下面是个简单的echo服务器：

```python
from socketserver import StreamRequestHandler, TCPServer

class EchoHandler(StreamRequestHandler):
    def handle(self):
        for line in self.rfile:
            self.wfile.write(b'GOT:' + line)

serv = TCPServer(('', 15000), EchoHandler)
serv.serve_forever()
```

不过，假设你想给EchoHandler增加一个可以接受其他配置选项的 __init__ 方法。比如：

```python
class EchoHandler(StreamRequestHandler):
    # ack is added keyword-only argument. *args, **kwargs are
    # any normal parameters supplied (which are passed on)
    def __init__(self, *args, ack, **kwargs):
        self.ack = ack
        super().__init__(*args, **kwargs)

    def handle(self):
        for line in self.rfile:
            self.wfile.write(self.ack + line)
```

这么修改后，我们就不需要显式地在TCPServer类中添加前缀了。 但是你再次运行程序后会报类似下面的错误：

```python
Exception happened during processing of request from ('127.0.0.1', 59834)
Traceback (most recent call last):
...
TypeError: __init__() missing 1 required keyword-only argument: 'ack'
```

初看起来好像很难修正这个错误，除了修改 socketserver 模块源代码或者使用某些奇怪的方法之外。 但是，如果使用 partial() 就能很轻松的解决——给它传递 ack 参数的值来初始化即可，如下：

```python
from functools import partial
serv = TCPServer(('', 15000), partial(EchoHandler, ack=b'RECEIVED:'))
serv.serve_forever()
```

在这个例子中，__init__() 方法中的ack参数声明方式看上去很有趣，其实就是声明ack为一个强制关键字参数。 关于强制关键字参数问题我们在7.2小节我们已经讨论过了，读者可以再去回顾一下。

很多时候 partial() 能实现的效果，lambda表达式也能实现。比如，之前的几个例子可以使用下面这样的表达式：

```python
points.sort(key=lambda p: distance(pt, p))
p.apply_async(add, (3, 4), callback=lambda result: output_result(result,log))
serv = TCPServer(('', 15000),
        lambda *args, **kwargs: EchoHandler(*args, ack=b'RECEIVED:', **kwargs))
```

这样写也能实现同样的效果，不过相比而已会显得比较臃肿，对于阅读代码的人来讲也更加难懂。 这时候使用 partial() 可以更加直观的表达你的意图(给某些参数预先赋值)。

看下partial绑定参数后，修改绑定参数函数的输出：

```python
from functools import partial


def spam(a, b, c, d):
    print(a, b, c, d)


if __name__ == '__main__':
    l = ['a']
    p1 = partial(spam, l)
    p1(4, 5, 6)
    l.append('b')  # 修改list对象，会影响绑定函数
    p1(4, 5, 6)

    x = 3
    p2 = partial(spam, x)
    p2(4, 5, 6)
    x = 10  # 修改基本类型对象值，无法影响到绑定函数
    p2(4, 5, 6)
输出：
['a'] 4 5 6
['a', 'b'] 4 5 6
3 4 5 6
3 4 5 6
```

## 7.9. 将单方法的类转换为函数

你有一个除 \_\_init\_\_() 方法外只定义了一个方法的类。为了简化代码，你想将它转换成一个函数。

大多数情况下，可以使用闭包来将单个方法的类转换成函数。 举个例子，下面示例中的类允许使用者根据某个模板方案来获取到URL链接地址。

```python
from urllib.request import urlopen

class UrlTemplate:
    def __init__(self, template):
        self.template = template

    def open(self, **kwargs):
        return urlopen(self.template.format_map(kwargs))

# Example use. Download stock data from yahoo
yahoo = UrlTemplate('http://finance.yahoo.com/d/quotes.csv?s={names}&f={fields}')
for line in yahoo.open(names='IBM,AAPL,FB', fields='sl1c1v'):
    print(line.decode('utf-8'))
```

这个类可以被一个更简单的函数来代替：

```python
def urltemplate(template):
    def opener(**kwargs):
        return urlopen(template.format_map(kwargs))
    return opener

# Example use
yahoo = urltemplate('http://finance.yahoo.com/d/quotes.csv?s={names}&f={fields}')
for line in yahoo(names='IBM,AAPL,FB', fields='sl1c1v'):
    print(line.decode('utf-8'))
```

大部分情况下，你拥有一个单方法类的原因是需要存储某些额外的状态来给方法使用。 比如，定义UrlTemplate类的唯一目的就是先在某个地方存储模板值，以便将来可以在open()方法中使用。

使用一个内部函数或者闭包的方案通常会更优雅一些。简单来讲，一个闭包就是一个函数， 只不过在函数内部带上了一个额外的变量环境。闭包关键特点就是它会记住自己被定义时的环境。 因此，在我们的解决方案中，opener() 函数记住了 template 参数的值，并在接下来的调用中使用它。

任何时候只要你碰到需要给某个函数增加额外的状态信息的问题，都可以考虑使用闭包。 相比将你的函数转换成一个类而言，闭包通常是一种更加简洁和优雅的方案。

## 7.10. 带额外状态信息的回调函数

你的代码中需要依赖到回调函数的使用(比如事件处理器、等待后台任务完成后的回调等)， 并且你还需要让回调函数拥有额外的状态值，以便在它的内部使用到。

这一小节主要讨论的是那些出现在很多函数库和框架中的回调函数的使用——特别是跟异步处理有关的。 为了演示与测试，我们先定义如下一个需要调用回调函数的函数：

```python
def apply_async(func, args, *, callback):
    # Compute the result
    result = func(*args)

    # Invoke the callback with the result
    callback(result)
```

实际上，这段代码可以做任何更高级的处理，包括线程、进程和定时器，但是这些都不是我们要关心的。 我们仅仅只需要关注回调函数的调用。下面是一个演示怎样使用上述代码的例子：

```python
def print_result(result):
    print('Got:', result)


def add(x, y):
    return x + y


if __name__ == '__main__':
    apply_async(add, (2, 3), callback=print_result)
    apply_async(add, ('hello', 'world'), callback=print_result)
输出：
Got: 5
Got: helloworld
```

注意到 print_result() 函数仅仅只接受一个参数 result 。不能再传入其他信息。 而当你想让回调函数访问其他变量或者特定环境的变量值的时候就会遇到麻烦。

为了让回调函数访问外部信息，一种方法是使用一个绑定方法来代替一个简单函数。 比如，下面这个类会保存一个内部序列号，每次接收到一个 result 的时候序列号加1：

```python
class ResultHandler:

    def __init__(self):
        self.sequence = 0

    def handler(self, result):
        self.sequence += 1
        print('[{}] Got: {}'.format(self.sequence, result))
```

使用这个类的时候，你先创建一个类的实例，然后用它的 handler() 绑定方法来做为回调函数：

```python
r = ResultHandler()
apply_async(add, (2, 3), callback=r.handler)
apply_async(add, ('hello', 'world'), callback=r.handler)
输出：
[1] Got: 5
[2] Got: helloworld
```

第二种方式，作为类的替代，可以使用一个闭包捕获状态值，例如：

```python
def make_handler():
    sequence = 0

    def handler(result):
        nonlocal sequence
        sequence += 1
        print('[{}] Got: {}'.format(sequence, result))

    return handler
```

下面是使用闭包方式的一个例子：

```python
handler = make_handler()
apply_async(add, (2, 3), callback=handler)
apply_async(add, ('hello', 'world'), callback=handler)
输出：
[1] Got: 5
[2] Got: helloworld
```

还有另外一个更高级的方法，可以使用协程来完成同样的事情：

```python
def make_handler():
    sequence = 0
    while True:
        result = yield
        sequence += 1
        print('[{}] Got: {}'.format(sequence, result))
```

对于协程，你需要使用它的 send() 方法作为回调函数，如下所示：

```python
handler = make_handler()
next(handler)  # Advance to the yield
apply_async(add, (2, 3), callback=handler.send)
apply_async(add, ('hello', 'world'), callback=handler.send)
输出：
[1] Got: 5
[2] Got: helloworld
```

基于回调函数的软件通常都有可能变得非常复杂。一部分原因是回调函数通常会跟请求执行代码断开。 因此，请求执行和处理结果之间的执行环境实际上已经丢失了。如果你想让回调函数连续执行多步操作， 那你就必须去解决如何保存和恢复相关的状态信息了。

至少有两种主要方式来捕获和保存状态信息，你可以在一个对象实例(通过一个绑定方法)或者在一个闭包中保存它。 两种方式相比，闭包或许是更加轻量级和自然一点，因为它们可以很简单的通过函数来构造。 它们还能自动捕获所有被使用到的变量。因此，你无需去担心如何去存储额外的状态信息(代码中自动判定)。

如果使用闭包，你需要注意对那些可修改变量的操作。在上面的方案中， nonlocal 声明语句用来指示接下来的变量会在回调函数中被修改。如果没有这个声明，代码会报错。

而使用一个协程来作为一个回调函数就更有趣了，它跟闭包方法密切相关。 某种意义上来讲，它显得更加简洁，因为总共就一个函数而已。 并且，你可以很自由的修改变量而无需去使用 nonlocal 声明。 这种方式唯一缺点就是相对于其他Python技术而言或许比较难以理解。 另外还有一些比较难懂的部分，比如使用之前需要调用 next() ，实际使用时这个步骤很容易被忘记。 尽管如此，协程还有其他用处，比如作为一个内联回调函数的定义(下一节会讲到)。

如果你仅仅只需要给回调函数传递额外的值的话，还有一种使用 partial() 的方式也很有用。 在没有使用 partial() 的时候，你可能经常看到下面这种使用lambda表达式的复杂代码：

```python
def handler(result, sequence):
    sequence += 1
    print('[{}] Got: {}'.format(sequence, result))


if __name__ == '__main__':
    seq = 0
    apply_async(add, (2, 3), callback=lambda r: handler(r, seq))  # 只能给回调函数传递额外的值，无法保存状态
    apply_async(add, ('hello', 'world'), callback=lambda r: handler(r, seq))
输出：
[1] Got: 5
[1] Got: helloworld
```

可以参考7.8小节的几个示例，教你如何使用 partial() 来更改参数签名来简化上述代码。

## 7.11. 内联回调函数

当你编写使用回调函数的代码的时候，担心很多小函数的扩张可能会弄乱程序控制流。 你希望找到某个方法来让代码看上去更像是一个普通的执行序列。

通过使用生成器和协程可以使得回调函数内联在某个函数中。 为了演示说明，假设你有如下所示的一个执行某种计算任务然后调用一个回调函数的函数(参考7.10小节)：

```python
def apply_async(func, args, *, callback):
    # Compute the result
    result = func(*args)

    # Invoke the callback with the result
    callback(result)
```

接下来让我们看一下下面的代码，它包含了一个 Async 类和一个 inlined_async 装饰器：

```python
from queue import Queue
from functools import wraps

class Async:
    def __init__(self, func, args):
        self.func = func
        self.args = args

def inlined_async(func):
    @wraps(func)
    def wrapper(*args):
        f = func(*args)
        result_queue = Queue()
        result_queue.put(None)
        while True:
            result = result_queue.get()
            try:
                a = f.send(result)
                apply_async(a.func, a.args, callback=result_queue.put)
            except StopIteration:
                break
    return wrapper
```

这两个代码片段允许你使用 yield 语句内联回调步骤。比如

```python
def add(x, y):
    return x + y

@inlined_async
def test():  # 这是一个生成器generator，所以在wrapper函数中调用f = func(*args)，并没有真实的调用test函数，只是获取到test函数
    r = yield Async(add, (2, 3))
    print(r)
    r = yield Async(add, ('hello', 'world'))
    print(r)
    for n in range(10):
        r = yield Async(add, (n, n))
        print(r)
    print('Goodbye')
```

如果你调用 test() ，你会得到类似如下的输出：

```python
5
helloworld
0
2
4
6
8
10
12
14
16
18
Goodbye
```

你会发现，除了那个特别的装饰器和 yield 语句外，其他地方并没有出现任何的回调函数(其实是在后台定义的)。

本小节会实实在在的测试你关于回调函数、生成器和控制流的知识。

首先，在需要使用到回调的代码中，关键点在于当前计算工作会挂起并在将来的某个时候重启(比如异步执行)。 当计算重启时，回调函数被调用来继续处理结果。apply_async() 函数演示了执行回调的实际逻辑， 尽管实际情况中它可能会更加复杂(包括线程、进程、事件处理器等等)。

计算的暂停与重启思路跟生成器函数的执行模型不谋而合。 具体来讲，yield 操作会使一个生成器函数产生一个值并暂停。 接下来调用生成器的 __next__() 或 send() 方法又会让它从暂停处继续执行。

根据这个思路，这一小节的核心就在 inline_async() 装饰器函数中了。 关键点就是，装饰器会逐步遍历生成器函数的所有 yield 语句，每一次一个。 为了这样做，刚开始的时候创建了一个 result 队列并向里面放入一个 None 值。 然后开始一个循环操作，从队列中取出结果值并发送给生成器，它会持续到下一个 yield 语句， 在这里一个 Async 的实例被接受到。然后循环开始检查函数和参数，并开始进行异步计算 apply_async() 。 然而，这个计算有个最诡异部分是它并没有使用一个普通的回调函数，而是用队列的 put() 方法来回调。

这时候，是时候详细解释下到底发生了什么了。主循环立即返回顶部并在队列上执行 get() 操作。 如果数据存在，它一定是 put() 回调存放的结果。如果没有数据，那么先暂停操作并等待结果的到来。 这个具体怎样实现是由 apply_async() 函数来决定的。 如果你不相信会有这么神奇的事情，你可以使用 multiprocessing 库来试一下， 在单独的进程中执行异步计算操作，如下所示：

```python
if __name__ == '__main__':
    import multiprocessing
    pool = multiprocessing.Pool()
    apply_async = pool.apply_async

    # Run the test function
    test()
```

实际上你会发现这个真的就是这样的，但是要解释清楚具体的控制流得需要点时间了。

将复杂的控制流隐藏到生成器函数背后的例子在标准库和第三方包中都能看到。 比如，在 contextlib 中的 @contextmanager 装饰器使用了一个令人费解的技巧， 通过一个 yield 语句将进入和离开上下文管理器粘合在一起。 另外非常流行的 Twisted 包中也包含了非常类似的内联回调。

注：wraps函数使用<https://www.cnblogs.com/Neeo/p/8371826.html>：

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from functools import wraps


def trace(func):
    """ 装饰器 """

    @wraps(func)
    def callf(*args, **kwargs):
        """ A wrapper function """
        print("Calling function:{}".format(func.__name__))  # Calling function:foo
        res = func(*args, **kwargs)
        print("Return value:{}".format(res))  # Return value:9
        return res

    return callf


@trace
def foo(x):
    """ 返回给定数字的平方 """
    return x * x


if __name__ == '__main__':
    print(foo(3))
    print('==================================')
    print(foo.__doc__)
    print('==================================')
    help(foo)
    print('==================================')
    print(foo.__name__)
    print('==================================')
    t = trace(foo)
    print(t)
    print(t(3))  # callf函数主动调用打印一次，func函数调用再打印一次
    print('==================================')
输出：
Calling function:foo
Return value:9
9
==================================
 返回给定数字的平方 
==================================
Help on function foo in module __main__:

foo(x)
    返回给定数字的平方

==================================
foo
==================================
<function foo at 0x00000221A079F8C8>
Calling function:foo
Calling function:foo
Return value:9
Return value:9
9
==================================
```

上面的装饰器例子等价于：trace(foo)(3),只是在使用装饰器时，我们不用再手动调用装饰器函数，如：

```python
def foo(x):
    """ 返回给定数字的平方 """
    return x * x

print(trace(foo)(3))  # trace(foo(3))只返回一个callf函数，但是不可调用
输出：
Calling function:foo
Return value:9
9
```

## 7.12. 访问闭包中定义的变量

你想要扩展函数中的某个闭包，允许它能访问和修改函数的内部变量。

通常来讲，闭包的内部变量对于外界来讲是完全隐藏的。 但是，你可以通过编写访问函数并将其作为函数属性绑定到闭包上来实现这个目的。例如：

```python
def sample():
    n = 0

    # Closure function
    def func():
        print('n=', n)

    # Accessor methods for n
    def get_n():
        return n

    def set_n(value):
        nonlocal n
        n = value

    # Attach as function attributes
    func.get_n = get_n
    func.set_n = set_n
    return func
```

下面是使用的例子:

```python
>>> f = sample()
>>> f()
n= 0
>>> f.set_n(10)
>>> f()
n= 10
>>> f.get_n()
10
```

为了说明清楚它如何工作的，有两点需要解释一下。首先，nonlocal 声明可以让我们编写函数来修改内部变量的值。 其次，函数属性允许我们用一种很简单的方式将访问方法绑定到闭包函数上，这个跟实例方法很像(尽管并没有定义任何类)。

还可以进一步的扩展，让闭包模拟类的实例。你要做的仅仅是复制上面的内部函数到一个字典实例中并返回它即可。例如：

```python
import sys
class ClosureInstance:
    def __init__(self, locals=None):
        if locals is None:
            locals = sys._getframe(1).f_locals

        # Update instance dictionary with callables
        self.__dict__.update((key,value) for key, value in locals.items()
                            if callable(value) )
    # Redirect special methods
    def __len__(self):
        return self.__dict__['__len__']()

# Example use
def Stack():
    items = []
    def push(item):
        items.append(item)

    def pop():
        return items.pop()

    def __len__():
        return len(items)

    return ClosureInstance()
```

下面是一个交互式会话来演示它是如何工作的：

```python
s = Stack()
print(s)

s.push(10)
s.push(20)
s.push('Hello')
print(len(s))
print(s.pop())
print(s.pop())
print(s.pop())
输出：
<__main__.ClosureInstance object at 0x00000209709F66D8>
3
Hello
20
10
```

有趣的是，这个代码运行起来会比一个普通的类定义要快很多。你可能会像下面这样测试它跟一个类的性能对比：

```python
class Stack2:
    def __init__(self):
        self.items = []

    def push(self, item):
        self.items.append(item)

    def pop(self):
        return self.items.pop()

    def __len__(self):
        return len(self.items)
```

如果这样做，你会得到类似如下的结果：

```python
# Test involving closures
s = Stack()
print(timeit('s.push(1);s.pop()', 'from __main__ import s'))  # 0.89690163
# Test involving a class
s = Stack2()
print(timeit('s.push(1);s.pop()', 'from __main__ import s'))  # 0.916409772
```

结果显示，闭包的方案运行起来要快大概8%，大部分原因是因为对实例变量的简化访问， 闭包更快是因为不会涉及到额外的self变量。

Raymond Hettinger对于这个问题设计出了更加难以理解的改进方案。不过，你得考虑下是否真的需要在你代码中这样做， 而且它只是真实类的一个奇怪的替换而已，例如，类的主要特性如继承、属性、描述器或类方法都是不能用的。 并且你要做一些其他的工作才能让一些特殊方法生效(比如上面 ClosureInstance 中重写过的 __len__() 实现。)

最后，你可能还会让其他阅读你代码的人感到疑惑，为什么它看起来不像一个普通的类定义呢？ (当然，他们也想知道为什么它运行起来会更快)。尽管如此，这对于怎样访问闭包的内部变量也不失为一个有趣的例子。

总体上讲，在配置的时候给闭包添加方法会有更多的实用功能， 比如你需要重置内部状态、刷新缓冲区、清除缓存或其他的反馈机制的时候。

# 8. 类与对象

本章主要关注点的是和类定义有关的常见编程模型。包括让对象支持常见的Python特性、特殊方法的使用、 类封装技术、继承、内存管理以及有用的设计模式。

## 8.1. 改变对象的字符串显示

你想改变对象实例的打印或显示输出，让它们更具可读性。

要改变一个实例的字符串表示，可重新定义它的 \_\_str\_\_() 和 \_\_repr\_\_() 方法。例如：

```python
class Pair:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __repr__(self):
        return 'Pair({0.x!r}, {0.y!r})'.format(self)

    def __str__(self):
        return '({0.x!s}, {0.y!s})'.format(self)
```

\_\_repr\_\_() 方法返回一个实例的代码表示形式，通常用来重新构造这个实例。 内置的 repr() 函数返回这个字符串，跟我们使用交互式解释器显示的值是一样的。 \_\_str\_\_() 方法将实例转换为一个字符串，使用 str() 或 print() 函数会输出这个字符串。比如：

```python
>>> p = Pair(3, 4)
>>> p
Pair(3, 4) # __repr__() output
>>> print(p)
(3, 4) # __str__() output
```

我们在这里还演示了在格式化的时候怎样使用不同的字符串表现形式。 特别来讲，!r 格式化代码指明输出使用 \_\_repr\_\_() 来代替默认的 \_\_str\_\_() 。 你可以用前面的类来试着测试下：

```python
p = Pair(3, 4)
print('p is {0!r}'.format(p))
print('p is {0}'.format(p))
print(f'p is {p!r}')
print(f'p is {p}')
输出：
p is Pair(3, 4)
p is (3, 4)
p is Pair(3, 4)
p is (3, 4)
```

自定义 \_\_repr\__() 和 __str__() 通常是很好的习惯，因为它能简化调试和实例输出。 例如，如果仅仅只是打印输出或日志输出某个实例，那么程序员会看到实例更加详细与有用的信息。

\_\_repr\_\_() 生成的文本字符串标准做法是需要让 eval(repr(x)) == x 为真。 如果实在不能这样子做，应该创建一个有用的文本表示，并使用 < 和 > 括起来。比如：

```python
p = Pair(3, 4)
print(repr(p))
p2 = eval(repr(p))
print(p2)
print(type(p2))
print(id(p), id(p2))
输出：
Pair(3, 4)
(3, 4)
<class '__main__.Pair'>
2362880936368 2362884006968
False

>>> f = open('file.dat')
>>> f
<_io.TextIOWrapper name='file.dat' mode='r' encoding='UTF-8'>
```

如果 \_\_str\_\_() 没有被定义，那么就会使用 \_\_repr\_\_() 来代替输出。

上面的 format() 方法的使用看上去很有趣，格式化代码 {0.x} 对应的是第1个参数的x属性。 因此，在下面的函数中，0实际上指的就是 self 本身：

```python
def __repr__(self):
    return 'Pair({0.x!r}, {0.y!r})'.format(self)
```

作为这种实现的一个替代，你也可以使用 % 操作符，就像下面这样：

```python
def __repr__(self):
    return 'Pair(%r, %r)' % (self.x, self.y)
```

## 8.2. 自定义字符串的格式化

你想通过 format() 函数和字符串方法使得一个对象能支持自定义的格式化。

解决方案
为了自定义字符串的格式化，我们需要在类上面定义 \_\_format\_\_() 方法。例如：

```python
_formats = {
    'ymd': '{d.year}-{d.month}-{d.day}',
    'mdy': '{d.month}/{d.day}/{d.year}',
    'dmy': '{d.day}/{d.month}/{d.year}'
}


class Date(object):
    def __init__(self, year, month, day):
        self.year = year
        self.month = month
        self.day = day

    def __format__(self, code):
        if code == '':
            code = 'ymd'
        fmt = _formats[code]
        return fmt.format(d=self)
```

现在 Date 类的实例可以支持格式化操作了，如同下面这样：

```python
d = Date(2012, 12, 21)
print(format(d))  # 如果不定义__format__则，format(d)输出：<__main__.Date object at 0x000002AE1B3231D0>
print(format(d, 'mdy'))
print('The date is {:ymd}'.format(d))
print('The date is {:mdy}'.format(d))
输出：
2012-12-21
12/21/2012
The date is 2012-12-21
The date is 12/21/2012
```

\_\_format\_\_() 方法给Python的字符串格式化功能提供了一个钩子。 这里需要着重强调的是格式化代码的解析工作完全由类自己决定。因此，格式化代码可以是任何值。 例如，参考下面来自 datetime 模块中的代码：

```python
>>> from datetime import date
>>> d = date(2012, 12, 21)
>>> format(d)
'2012-12-21'
>>> format(d,'%A, %B %d, %Y')
'Friday, December 21, 2012'
>>> 'The end is {:%d %b %Y}. Goodbye'.format(d)
'The end is 21 Dec 2012. Goodbye'
```

对于内置类型的格式化有一些标准的约定。 可以参考 string模块文档 说明。

## 8.3. 让对象支持上下文管理协议

你想让你的对象支持上下文管理协议(with语句)。

为了让一个对象兼容 with 语句，你需要实现 __enter__() 和 __exit__() 方法。 例如，考虑如下的一个类，它能为我们创建一个网络连接：

```python
from socket import AF_INET, SOCK_STREAM, socket


class LazyConnection(object):
    def __init__(self, address, family=AF_INET, type=SOCK_STREAM):
        self.address = address
        self.family = family
        self.type = type
        self.sock = None

    def __enter__(self):
        if self.sock is not None:
            raise RuntimeError('Already connected')
        self.sock = socket()
        self.sock.connect(self.address)
        return self.sock

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.sock.close()
        self.sock = None
```

这个类的关键特点在于它表示了一个网络连接，但是初始化的时候并不会做任何事情(比如它并没有建立一个连接)。 连接的建立和关闭是使用 with 语句自动完成的，例如：

```python
from functools import partial

conn = LazyConnection(('www.python.org', 80))
# Connection closed
with conn as s:
    # conn.__enter__() executes: connection open
    s.send(b'GET /index.html HTTP/1.0\r\n')
    s.send(b'Host: www.python.org\r\n')
    s.send(b'\r\n')
    resp = b''.join(iter(partial(s.recv, 8192), b''))
    # conn.__exit__() executes: connection closed
```

编写上下文管理器的主要原理是你的代码会放到 with 语句块中执行。 当出现 with 语句的时候，对象的 __enter__() 方法被触发， 它返回的值(如果有的话)会被赋值给 as 声明的变量。然后，with 语句块里面的代码开始执行。 最后，__exit__() 方法被触发进行清理工作。

不管 with 代码块中发生什么，上面的控制流都会执行完，就算代码块中发生了异常也是一样的。 事实上，__exit__() 方法的第三个参数包含了异常类型、异常值和追溯信息(如果有的话)。 __exit__() 方法能自己决定怎样利用这个异常信息，或者忽略它并返回一个None值。 如果 __exit__() 返回 True ，那么异常会被清空，就好像什么都没发生一样， with 语句后面的程序继续在正常执行。

还有一个细节问题就是 LazyConnection 类是否允许多个 with 语句来嵌套使用连接。 很显然，上面的定义中一次只能允许一个socket连接，如果正在使用一个socket的时候又重复使用 with 语句， 就会产生一个异常了。不过你可以像下面这样修改下上面的实现来解决这个问题：

```python
from socket import socket, AF_INET, SOCK_STREAM

class LazyConnection:
    def __init__(self, address, family=AF_INET, type=SOCK_STREAM):
        self.address = address
        self.family = family
        self.type = type
        self.connections = []

    def __enter__(self):
        sock = socket(self.family, self.type)
        sock.connect(self.address)
        self.connections.append(sock)
        return sock

    def __exit__(self, exc_ty, exc_val, tb):
        self.connections.pop().close()

# Example use
from functools import partial

conn = LazyConnection(('www.python.org', 80))
with conn as s1:
    pass
    with conn as s2:
        pass
        # s1 and s2 are independent sockets
```

在第二个版本中，LazyConnection 类可以被看做是某个连接工厂。在内部，一个列表被用来构造一个栈。 每次 __enter__() 方法执行的时候，它复制创建一个新的连接并将其加入到栈里面。 __exit__() 方法简单的从栈中弹出最后一个连接并关闭它。 这里稍微有点难理解，不过它能允许嵌套使用 with 语句创建多个连接，就如上面演示的那样。

在需要管理一些资源比如文件、网络连接和锁的编程环境中，使用上下文管理器是很普遍的。 这些资源的一个主要特征是它们必须被手动的关闭或释放来确保程序的正确运行。 例如，如果你请求了一个锁，那么你必须确保之后释放了它，否则就可能产生死锁。 通过实现 __enter__() 和 __exit__() 方法并使用 with 语句可以很容易的避免这些问题， 因为 __exit__() 方法可以让你无需担心这些了。

在 contextmanager 模块中有一个标准的上下文管理方案模板，可参考9.22小节。 同时在12.6小节中还有一个对本节示例程序的线程安全的修改版。

## 8.4. 创建大量对象时节省内存方法

你的程序要创建大量(可能上百万)的对象，导致占用很大的内存。

对于主要是用来当成简单的数据结构的类而言，你可以通过给类添加 __slots__ 属性来极大的减少实例所占的内存。比如：

```python

# 使用__dict__
class Date(object):
    def __init__(self, year, month, day):
        self.year = year
        self.month = month
        self.day = day


if __name__ == '__main__':
    date = Date(2019, 6, 23)
    print(date.__dict__)
输出：
{'year': 2019, 'month': 6, 'day': 23}

# 使用__slot__
class Date(object):
    __slots__ = ['year', 'month', 'day']

    def __init__(self, year, month, day):
        self.year = year
        self.month = month
        self.day = day


if __name__ == '__main__':
    date = Date(2019, 6, 23)
    # print(date.__dict__)  # 增加__slot__后，'Date' object has no attribute '__dict__'
```

当你定义 \_\_slots\_\_ 后，Python就会为实例使用一种更加紧凑的内部表示。 实例通过一个很小的固定大小的数组来构建，而不是为每个实例定义一个字典，这跟元组或列表很类似。 在 \_\_slots\_\_ 中列出的属性名在内部被映射到这个数组的指定小标上。 使用slots一个不好的地方就是我们不能再给实例添加新的属性了，只能使用在 \_\_slots\_\_ 中定义的那些属性名。

使用slots后节省的内存会跟存储属性的数量和类型有关。 不过，一般来讲，使用到的内存总量和将数据存储在一个元组中差不多。 为了给你一个直观认识，假设你不使用slots直接存储一个Date实例， 在64位的Python上面要占用428字节，而如果使用了slots，内存占用下降到156字节。 如果程序中需要同时创建大量的日期实例，那么这个就能极大的减小内存使用量了。

尽管slots看上去是一个很有用的特性，很多时候你还是得减少对它的使用冲动。 Python的很多特性都依赖于普通的基于字典的实现。 另外，定义了slots后的类不再支持一些普通类特性了，比如多继承。 大多数情况下，你应该只在那些经常被使用到的用作数据结构的类上定义slots (比如在程序中需要创建某个类的几百万个实例对象)。

关于 \_\_slots\_\_ 的一个常见误区是它可以作为一个封装工具来防止用户给实例增加新的属性。 尽管使用slots可以达到这样的目的，但是这个并不是它的初衷。 \_\_slots\_\_ 更多的是用来作为一个内存优化工具。

## 8.5. 在类中封装属性名

你想封装类的实例上面的“私有”数据，但是Python语言并没有访问控制。

Python程序员不去依赖语言特性去封装数据，而是通过遵循一定的属性和方法命名规约来达到这个效果。 第一个约定是任何以单下划线_开头的名字都应该是内部实现。比如：

```python
class A:
    def __init__(self):
        self._internal = 0 # An internal attribute
        self.public = 1 # A public attribute

    def public_method(self):
        '''
        A public method
        '''
        pass

    def _internal_method(self):
        pass
```

Python并不会真的阻止别人访问内部名称。但是如果你这么做肯定是不好的，可能会导致脆弱的代码。 同时还要注意到，使用下划线开头的约定同样适用于模块名和模块级别函数。 例如，如果你看到某个模块名以单下划线开头(比如_socket)，那它就是内部实现。 类似的，模块级别函数比如 sys._getframe() 在使用的时候就得加倍小心了。

你还可能会遇到在类定义中使用两个下划线(__)开头的命名。比如：

```python
class B:
    def __init__(self):
        self.__private = 0

    def __private_method(self):
        pass

    def public_method(self):
        pass
        self.__private_method()
```

使用双下划线开始会导致访问名称变成其他形式。 比如，在前面的类B中，私有属性会被分别重命名为 _B__private 和 _B__private_method 。 这时候你可能会问这样重命名的目的是什么，答案就是继承——这种属性通过继承是无法被覆盖的。比如：

```python
class C(B):
    def __init__(self):
        super().__init__()
        self.__private = 1 # Does not override B.__private

    # Does not override B.__private_method()
    def __private_method(self):
        pass
```

这里，私有名称 __private 和 __private_method 被重命名为 _C__private 和 _C__private_method ，这个跟父类B中的名称是完全不同的。

上面提到有两种不同的编码约定(单下划线和双下划线)来命名私有属性，那么问题就来了：到底哪种方式好呢？ **大多数而言，你应该让你的非公共名称以单下划线开头。但是，如果你清楚你的代码会涉及到子类， 并且有些内部属性应该在子类中隐藏起来，那么才考虑使用双下划线方案**。

还有一点要注意的是，有时候你定义的一个变量和某个保留关键字冲突，这时候可以使用单下划线作为后缀，例如：

```python
lambda_ = 2.0 # Trailing _ to avoid clash with lambda keyword
```

这里我们并不使用单下划线前缀的原因是它避免误解它的使用初衷 (如使用单下划线前缀的目的是为了防止命名冲突而不是指明这个属性是私有的)。 通过使用单下划线后缀可以解决这个问题。

## 8.6. 创建可管理的属性

你想给某个实例attribute增加除访问与修改之外的其他处理逻辑，比如类型检查或合法性验证。

```python
class Person:
    def __init__(self, first_name):
        self.first_name = first_name  # 调用@first_name.setter方法

    # Getter function
    @property
    def first_name(self):
        return self._first_name

    # Setter function
    @first_name.setter
    def first_name(self, value):
        if not isinstance(value, str):
            raise TypeError('Expected a string')
        self._first_name = value

    # Deleter function (optional)
    @first_name.deleter
    def first_name(self):
        raise AttributeError("Can't delete attribute")


if __name__ == '__main__':
    person = Person('Zhao')
    print(person.first_name)  # Zhao
    person.first_name = 'Qian'
    print(person.first_name)  # Qian
```

上述代码中有三个相关联的方法，这三个方法的名字都必须一样。 第一个方法是一个 getter 函数，它使得 first_name 成为一个属性。 其他两个方法给 first_name 属性添加了 setter 和 deleter 函数。 需要强调的是只有在 first_name 属性被创建后， 后面的两个装饰器 @first_name.setter 和 @first_name.deleter 才能被定义。如果把@first_name.setter 和 @first_name.deleter定义在@property之前，则会报错提示：name 'first_name' is not defined。

property的一个关键特征是它看上去跟普通的attribute没什么两样， 但是访问它的时候会自动触发 getter 、setter 和 deleter 方法。例如：

```python
>>> a = Person('Guido')
>>> a.first_name # Calls the getter
'Guido'
>>> a.first_name = 42 # Calls the setter
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
    File "prop.py", line 14, in first_name
        raise TypeError('Expected a string')
TypeError: Expected a string
>>> del a.first_name
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
AttributeError: can`t delete attribute
```

在实现一个property的时候，底层数据(如果有的话)仍然需要存储在某个地方。 因此，在get和set方法中，你会看到对 _first_name 属性的操作，这也是实际数据保存的地方。 另外，你可能还会问为什么 __init__() 方法中设置了 self.first_name 而不是 self._first_name 。 在这个例子中，我们创建一个property的目的就是在设置attribute的时候进行检查。 因此，你可能想在初始化的时候也进行这种类型检查。通过设置 self.first_name ，自动调用 setter 方法， 这个方法里面会进行参数的检查，否则就是直接访问 self._first_name 了。

还能在已存在的get和set方法基础上定义property。例如：

```python
class Person:
    def __init__(self, first_name):
        self.set_first_name(first_name)

    # Getter function
    def get_first_name(self):
        return self._first_name

    # Setter function
    def set_first_name(self, value):
        if not isinstance(value, str):
            raise TypeError('Expected a string')
        self._first_name = value

    # Deleter function (optional)
    def del_first_name(self):
        raise AttributeError("Can't delete attribute")

    # Make a property from existing get/set methods
    name = property(get_first_name, set_first_name, del_first_name)


if __name__ == '__main__':
    person = Person('Zhao')
    print(person.name)
    person.name = 'Qian'
    print(person.name)
    person.name = 42
输出：
Zhao
Qian
Traceback (most recent call last):
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 32, in <module>
    person.name = 42
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 16, in set_first_name
    raise TypeError('Expected a string')
TypeError: Expected a string
```

一个property属性其实就是一系列相关绑定方法的集合。如果你去查看拥有property的类， 就会发现property本身的fget、fset和fdel属性就是类里面的普通方法。比如：

```python
>>> Person.first_name.fget
<function Person.first_name at 0x1006a60e0>
>>> Person.first_name.fset
<function Person.first_name at 0x1006a6170>
>>> Person.first_name.fdel
<function Person.first_name at 0x1006a62e0>
```

只有当你确实需要对attribute执行其他额外的操作的时候才应该使用到property。 有时候一些从其他编程语言(比如Java)过来的程序员总认为所有访问都应该通过getter和setter， 所以他们认为代码应该像下面这样写：

```python
class Person:
    def __init__(self, first_name):
        self.first_name = first_name

    @property
    def first_name(self):
        return self._first_name

    @first_name.setter
    def first_name(self, value):
        self._first_name = value
```

**不要写这种没有做任何其他额外操作的property。 首先，它会让你的代码变得很臃肿，并且还会迷惑阅读者。 其次，它还会让你的程序运行起来变慢很多。 最后，这样的设计并没有带来任何的好处。** 特别是当你以后想给普通attribute访问添加额外的处理逻辑的时候， 你可以将它变成一个property而无需改变原来的代码。 因为访问attribute的代码还是保持原样。

Properties还是一种定义动态计算attribute的方法。 这种类型的attributes并不会被实际的存储，而是在需要的时候计算出来。比如：

```python
import math
class Circle:
    def __init__(self, radius):
        self.radius = radius

    @property
    def area(self):
        return math.pi * self.radius ** 2

    @property
    def diameter(self):
        return self.radius * 2

    @property
    def perimeter(self):
        return 2 * math.pi * self.radius
```

在这里，我们通过使用properties，将所有的访问接口形式统一起来， 对半径、直径、周长和面积的访问都是通过属性访问，就跟访问简单的attribute是一样的。 如果不这样做的话，那么就要在代码中混合使用简单属性访问和方法调用。 下面是使用的实例：

```python
>>> c = Circle(4.0)
>>> c.radius
4.0
>>> c.area  # Notice lack of ()
50.26548245743669
>>> c.perimeter  # Notice lack of ()
25.132741228718345
```

尽管properties可以实现优雅的编程接口，但有些时候你还是会想直接使用getter和setter函数。例如：

```python
>>> p = Person('Guido')
>>> p.get_first_name()
'Guido'
>>> p.set_first_name('Larry')
```

这种情况的出现通常是因为Python代码被集成到一个大型基础平台架构或程序中。 例如，有可能是一个Python类准备加入到一个基于远程过程调用的大型分布式系统中。 这种情况下，直接使用get/set方法(普通方法调用)而不是property或许会更容易兼容。

最后一点，不要像下面这样写有大量重复代码的property定义：

```python
class Person:
    def __init__(self, first_name, last_name):
        self.first_name = first_name
        self.last_name = last_name

    @property
    def first_name(self):
        return self._first_name

    @first_name.setter
    def first_name(self, value):
        if not isinstance(value, str):
            raise TypeError('Expected a string')
        self._first_name = value

    # Repeated property code, but for a different name (bad!)
    @property
    def last_name(self):
        return self._last_name

    @last_name.setter
    def last_name(self, value):
        if not isinstance(value, str):
            raise TypeError('Expected a string')
        self._last_name = value
```

重复代码会导致臃肿、易出错和丑陋的程序。好消息是，通过使用装饰器或闭包，有很多种更好的方法来完成同样的事情。 可以参考8.9和9.21小节的内容。

## 8.7. 调用父类方法

你想在子类中调用父类的某个已经被覆盖的方法。

为了调用父类(超类)的一个方法，可以使用 super() 函数，比如：

```python
class A:
    def spam(self):
        print('A.spam')


class B(A):
    def spam(self):
        print('B.spam')
        super().spam()  # Call parent spam()


if __name__ == '__main__':
    b = B()
    b.spam()
输出：
B.spam
A.spam
```

super() 函数的一个常见用法是在 __init__() 方法中确保父类被正确的初始化了：

```python
class A:
    def __init__(self):
        self.x = 0

class B(A):
    def __init__(self):
        super().__init__()
        self.y = 1


if __name__ == '__main__':
    b = B()
    print(b.x, b.y, sep=',')
输出：
0,1

# 如果B类不调用super().__init__()
class A:
    def __init__(self):
        self.x = 0

class B(A):
    def __init__(self):
        self.y = 1


if __name__ == '__main__':
    b = B()
    print(b.x, b.y, sep=',')
输出：
抛异常：
Traceback (most recent call last):
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 16, in <module>
    print(b.x, b.y, sep=',')
AttributeError: 'B' object has no attribute 'x'
```

super() 的另外一个常见用法出现在覆盖Python特殊方法的代码中，比如：

```python
class A:
    def __init__(self):
        self.x = 0


class Proxy:
    def __init__(self, obj):
        self._obj = obj

    # Delegate attribute lookup to internal obj
    def __getattr__(self, name):
        return getattr(self._obj, name)

    # Delegate attribute assignment
    def __setattr__(self, name, value):
        if name.startswith('_'):
            super().__setattr__(name, value)  # Call original __setattr__
        else:
            setattr(self._obj, name, value)


if __name__ == '__main__':
    p = Proxy(A())
    setattr(p, 'a', 1)  # a被设置到_obj上
    print(getattr(p, 'a'))

    setattr(p, '_b', 2)  # b被设置到p上
    print(getattr(p, '_b'))
输出：
1
2
```

在上面代码中，\_\_setattr\_\_() 的实现包含一个名字检查。 如果某个属性名以下划线(_)开头，就通过 super() 调用原始的 \_\_setattr\_\_() ， 否则的话就委派给内部的代理对象 self._obj 去处理。 这看上去有点意思，因为就算没有显式的指明某个类的父类， super() 仍然可以有效的工作。

实际上，大家对于在Python中如何正确使用 super() 函数普遍知之甚少。 你有时候会看到像下面这样直接调用父类的一个方法：

```python
class Base:
    def __init__(self):
        print('Base.__init__')

class A(Base):
    def __init__(self):
        Base.__init__(self)
        print('A.__init__')
```

尽管对于大部分代码而言这么做没什么问题，但是在更复杂的涉及到多继承的代码中就有可能导致很奇怪的问题发生。 比如，考虑如下的情况：

```python
class Base:
    def __init__(self):
        print('Base.__init__')


class A(Base):
    def __init__(self):
        Base.__init__(self)
        print('A.__init__')


class B(Base):
    def __init__(self):
        Base.__init__(self)
        print('B.__init__')


class C(A, B):
    def __init__(self):
        A.__init__(self)
        B.__init__(self)
        print('C.__init__')
```

如果你运行这段代码就会发现 Base.\_\_init\_\_() 被调用两次，如下所示：

```python
>>> c = C()
Base.__init__
A.__init__
Base.__init__
B.__init__
C.__init__
```

可能两次调用 Base.__init__() 没什么坏处，但有时候却不是。 另一方面，假设你在代码中换成使用 super() ，结果就很完美了：

```python
class Base:
    def __init__(self):
        print('Base.__init__')

class A(Base):
    def __init__(self):
        super().__init__()
        print('A.__init__')

class B(Base):
    def __init__(self):
        super().__init__()
        print('B.__init__')

class C(A,B):
    def __init__(self):
        super().__init__()  # Only one call to super() here
        print('C.__init__')
```

运行这个新版本后，你会发现每个 __init__() 方法只会被调用一次了：

```python
>>> c = C()
Base.__init__
B.__init__
A.__init__
C.__init__
```

为了弄清它的原理，我们需要花点时间解释下Python是如何实现继承的。 对于你定义的每一个类，Python会计算出一个所谓的方法解析顺序(MRO)列表。 这个MRO列表就是一个简单的所有基类的线性顺序表。例如：

```python
>>> C.__mro__
(<class '__main__.C'>, <class '__main__.A'>, <class '__main__.B'>,
<class '__main__.Base'>, <class 'object'>)
```

为了实现继承，Python会在MRO列表上从左到右开始查找基类，直到找到第一个匹配这个属性的类为止。

而这个MRO列表的构造是通过一个C3线性化算法来实现的。 我们不去深究这个算法的数学原理，它实际上就是合并所有父类的MRO列表并遵循如下三条准则：

* 子类会先于父类被检查
* 多个父类会根据它们在列表中的顺序被检查
* 如果对下一个类存在两个合法的选择，选择第一个父类

老实说，你所要知道的就是MRO列表中的类顺序会让你定义的任意类层级关系变得有意义。

当你使用 super() 函数时，Python会在MRO列表上继续搜索下一个类。 只要每个重定义的方法统一使用 super() 并只调用它一次， 那么控制流最终会遍历完整个MRO列表，每个方法也只会被调用一次。 这也是为什么在第二个例子中你不会调用两次 Base.__init__() 的原因。

super() 有个令人吃惊的地方是它并不一定去查找某个类在MRO中下一个直接父类， 你甚至可以在一个没有直接父类的类中使用它。例如，考虑如下这个类：

```python
class A:
    def spam(self):
        print('A.spam')
        super().spam()
```

如果你试着直接使用这个类就会出错：

```python
>>> a = A()
>>> a.spam()
A.spam
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
    File "<stdin>", line 4, in spam
AttributeError: 'super' object has no attribute 'spam'
```

但是，如果你使用多继承的话看看会发生什么：

```python
class B:
    def spam(self):
        print('B.spam')


class C(A, B):
    pass

c = C()
c.spam()
输出：
A.spam
B.spam
```

你可以看到在类A中使用 super().spam() 实际上调用的是跟类A毫无关系的类B中的 spam() 方法。 这个用类C的MRO列表就可以完全解释清楚了：

```python
>>> C.__mro__
(<class '__main__.C'>, <class '__main__.A'>, <class '__main__.B'>,
<class 'object'>)
```

然而，由于 super() 可能会调用不是你想要的方法，你应该遵循一些通用原则。 首先，确保在继承体系中所有相同名字的方法拥有可兼容的参数签名(比如相同的参数个数和参数名称)。 这样可以确保 super() 调用一个非直接父类方法时不会出错。 其次，最好确保最顶层的类提供了这个方法的实现，这样的话在MRO上面的查找链肯定可以找到某个确定的方法。

在Python社区中对于 super() 的使用有时候会引来一些争议。 尽管如此，如果一切顺利的话，你应该在你最新代码中使用它。 Raymond Hettinger为此写了一篇非常好的文章 “Python’s super() Considered Super!” ， 通过大量的例子向我们解释了为什么 super() 是极好的。

如果定义了重载函数，则前一个会被覆盖：

```python
class Base(object):
    def __init__(self, a):
        self.a = a
        self.b = None

    def __init__(self, a, b):
        self.a = a
        self.b = b


if __name__ == '__main__':
    b1 = Base(1)
    b2 = Base(1, 2)
输出：
Traceback (most recent call last):
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 30, in <module>
    b1 = Base(1)
TypeError: __init__() missing 1 required positional argument: 'b'
```

## 8.8. 子类中扩展property

在子类中，你想要扩展定义在父类中的property的功能。

考虑如下的代码，它定义了一个property：

```python
class Person:
    def __init__(self, name):
        self.name = name

    # Getter function
    @property
    def name(self):
        return self._name

    # Setter function
    @name.setter
    def name(self, value):
        if not isinstance(value, str):
            raise TypeError('Expected a string')
        self._name = value

    # Deleter function
    @name.deleter
    def name(self):
        raise AttributeError("Can't delete attribute")
```

下面是一个示例类，它继承自Person并扩展了 name 属性的功能：

```python
class SubPerson(Person):
    @property
    def name(self):
        print('Getting name')
        return super().name

    @name.setter
    def name(self, value):
        print('Setting name to', value)
        super(SubPerson, SubPerson).name.__set__(self, value)

    @name.deleter
    def name(self):
        print('Deleting name')
        super(SubPerson, SubPerson).name.__delete__(self)
```

接下来使用这个新类：

```python
>>> s = SubPerson('Guido')
Setting name to Guido
>>> s.name
Getting name
'Guido'
>>> s.name = 'Larry'
Setting name to Larry
>>> s.name = 42
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
    File "example.py", line 16, in name
        raise TypeError('Expected a string')
TypeError: Expected a string
```

如果你仅仅只想扩展property的某一个方法，那么可以像下面这样写：

```python
class SubPerson(Person):
    @Person.name.getter
    def name(self):
        print('Getting name')
        return super().name
```

或者，你只想修改setter方法，就这么写：

```python
class SubPerson(Person):
    @Person.name.setter
    def name(self, value):
        print('Setting name to', value)
        super(SubPerson, SubPerson).name.__set__(self, value)
```

在子类中扩展一个property可能会引起很多不易察觉的问题， 因为一个property其实是 getter、setter 和 deleter 方法的集合，而不是单个方法。 因此，当你扩展一个property的时候，你需要先确定你是否要重新定义所有的方法还是说只修改其中某一个。

在第一个例子中，所有的property方法都被重新定义。 在每一个方法中，使用了 super() 来调用父类的实现。 在 setter 函数中使用 super(SubPerson, SubPerson).name.__set__(self, value) 的语句是没有错的。 为了委托给之前定义的setter方法，需要将控制权传递给之前定义的name属性的 __set__() 方法。 不过，获取这个方法的唯一途径是使用类变量而不是实例变量来访问它。 这也是为什么我们要使用 super(SubPerson, SubPerson) 的原因。

如果你只想重定义其中一个方法，那只使用 @property 本身是不够的。比如，下面的代码就无法工作：

```python
class SubPerson(Person):
    @property  # Doesn't work
    def name(self):
        print('Getting name')
        return super().name
```

如果你试着运行会发现setter函数整个消失了：

```python
>>> s = SubPerson('Guido')
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
    File "example.py", line 5, in __init__
        self.name = name
AttributeError: can't set attribute
```

你应该像之前说过的那样修改代码：

```python
class SubPerson(Person):
    @Person.name.getter
    def name(self):
        print('Getting name')
        return super().name
```

这么写后，property之前已经定义过的方法会被复制过来，而getter函数被替换。然后它就能按照期望的工作了：

```python
>>> s = SubPerson('Guido')
>>> s.name
Getting name
'Guido'
>>> s.name = 'Larry'
>>> s.name
Getting name
'Larry'
>>> s.name = 42
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
    File "example.py", line 16, in name
        raise TypeError('Expected a string')
TypeError: Expected a string
```

在这个特别的解决方案中，我们没办法使用更加通用的方式去替换硬编码的 Person 类名。 如果你不知道到底是哪个基类定义了property， 那你只能通过重新定义所有property并使用 super() 来将控制权传递给前面的实现。

值得注意的是上面演示的第一种技术还可以被用来扩展一个描述器(在8.9小节我们有专门的介绍)。比如：

```python
# A descriptor
class String:
    def __init__(self, name):
        self.name = name

    def __get__(self, instance, cls):
        if instance is None:
            return self
        return instance.__dict__[self.name]

    def __set__(self, instance, value):
        if not isinstance(value, str):
            raise TypeError('Expected a string')
        instance.__dict__[self.name] = value


# A class with a descriptor
class Person:
    name = String('name')

    def __init__(self, name):
        self.name = name


# Extending a descriptor with a property
class SubPerson(Person):
    @property
    def name(self):
        print('Getting name')
        return super().name

    @name.setter
    def name(self, value):
        print('Setting name to', value)
        super(SubPerson, SubPerson).name.__set__(self, value)

    @name.deleter
    def name(self):
        print('Deleting name')
        super(SubPerson, SubPerson).name.__delete__(self)


if __name__ == '__main__':
    s = SubPerson('Guido')
    print(s.name)
    s.name = 'Larry'
    print(s.name)
输出：
Setting name to Guido
Getting name
Guido
Setting name to Larry
Getting name
Larry
```

最后值得注意的是，读到这里时，你应该会发现子类化 setter 和 deleter 方法其实是很简单的。 这里演示的解决方案同样适用，但是在 Python的issue页面 报告的一个bug，或许会使得将来的Python版本中出现一个更加简洁的方法。

## 8.9. 创建新的类或实例属性

你想创建一个新的拥有一些额外功能的实例属性类型，比如类型检查。

如果你想创建一个全新的实例属性，可以通过一个描述器类的形式来定义它的功能。下面是一个例子：

```python
# Descriptor attribute for an integer type-checked attribute
class Integer:
    def __init__(self, name):
        self.name = name

    def __get__(self, instance, cls):
        if instance is None:
            return self
        else:
            return instance.__dict__[self.name]

    def __set__(self, instance, value):
        if not isinstance(value, int):
            raise TypeError('Expected an int')
        instance.__dict__[self.name] = value

    def __delete__(self, instance):
        del instance.__dict__[self.name]
```

一个描述器就是一个实现了三个核心的属性访问操作(get, set, delete)的类， 分别为 \_\_get\_\_() 、\_\_set\_\_() 和 \_\_delete\_\_() 这三个特殊的方法。 这些方法接受一个实例作为输入，之后相应的操作实例底层的字典。

为了使用一个描述器，需将这个描述器的实例作为类属性放到一个类的定义中。例如：

```python
class Point:
    x = Integer('x')
    y = Integer('y')

    def __init__(self, x, y):
        self.x = x
        self.y = y
```

当你这样做后，所有对描述器属性(比如x或y)的访问会被 \_\_get\_\_() 、\_\_set\_\_() 和 \_\_delete\_\_() 方法捕获到。例如：

```python
>>> p = Point(2, 3)
>>> p.x # Calls Point.x.__get__(p,Point)
2
>>> p.y = 5 # Calls Point.y.__set__(p, 5)
>>> p.x = 2.3 # Calls Point.x.__set__(p, 2.3)
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
    File "descrip.py", line 12, in __set__
        raise TypeError('Expected an int')
TypeError: Expected an int
```

作为输入，描述器的每一个方法会接受一个操作实例。 为了实现请求操作，会相应的操作实例底层的字典(__dict__属性)。 描述器的 self.name 属性存储了在实例字典中被实际使用到的key。

描述器可实现大部分Python类特性中的底层魔法， 包括 @classmethod 、@staticmethod 、@property ，甚至是 \_\_slots\_\_ 特性。

通过定义一个描述器，你可以在底层捕获核心的实例操作(get, set, delete)，并且可完全自定义它们的行为。 这是一个强大的工具，有了它你可以实现很多高级功能，并且它也是很多高级库和框架中的重要工具之一。

描述器的一个比较困惑的地方是它只能在类级别被定义，而不能为每个实例单独定义。因此，下面的代码是无法工作的：

```python
# Does NOT work
class Point:
    def __init__(self, x, y):
        self.x = Integer('x') # No! Must be a class variable
        self.y = Integer('y')
        self.x = x
        self.y = y
```

同时，\_\_get\_\_() 方法实现起来比看上去要复杂得多：

```python
# Descriptor attribute for an integer type-checked attribute
class Integer:

    def __get__(self, instance, cls):
        if instance is None:
            return self
        else:
            return instance.__dict__[self.name]
```

\_\_get\_\_() 看上去有点复杂的原因归结于实例变量和类变量的不同。 如果一个描述器被当做一个类变量来访问，那么 instance 参数被设置成 None 。 这种情况下，标准做法就是简单的返回这个描述器本身即可(尽管你还可以添加其他的自定义操作)。例如：

```python
>>> p = Point(2,3)
>>> p.x # Calls Point.x.__get__(p, Point)
2
>>> Point.x # Calls Point.x.__get__(None, Point)
<__main__.Integer object at 0x100671890>
```

描述器通常是那些使用到装饰器或元类的大型框架中的一个组件。同时它们的使用也被隐藏在后面。 举个例子，下面是一些更高级的基于描述器的代码，并涉及到一个类装饰器：

```python
# Descriptor for a type-checked attribute
class Typed:
    def __init__(self, name, expected_type):
        self.name = name
        self.expected_type = expected_type

    def __get__(self, instance, cls):
        if instance is None:
            return self
        else:
            return instance.__dict__[self.name]

    def __set__(self, instance, value):
        if not isinstance(value, self.expected_type):
            raise TypeError('Expected ' + str(self.expected_type))
        instance.__dict__[self.name] = value

    def __delete__(self, instance):
        del instance.__dict__[self.name]


# Class decorator that applies it to selected attributes
def typeassert(**kwargs):
    def decorate(cls):
        for name, expected_type in kwargs.items():
            # Attach a Typed descriptor to the class
            setattr(cls, name, Typed(name, expected_type))
        return cls

    return decorate


# Example use
@typeassert(name=str, shares=int, price=float)
class Stock:
    def __init__(self, name, shares, price):
        self.name = name
        self.shares = shares
        self.price = price


if __name__ == '__main__':
    stock = Stock('abc', 100.1, 27.3)
输出：
Traceback (most recent call last):
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 48, in <module>
    stock2 = Stock('abc', 100.1, 27.3)
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 42, in __init__
    self.shares = shares
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 19, in __set__
    raise TypeError('Expected ' + str(self.expected_type))
TypeError: Expected <class 'int'>
```

最后要指出的一点是，如果你只是想简单的自定义某个类的单个属性访问的话就不用去写描述器了。 这种情况下使用8.6小节介绍的property技术会更加容易。 当程序中有很多重复代码的时候描述器就很有用了 (比如你想在你代码的很多地方使用描述器提供的功能或者将它作为一个函数库特性)。

## 8.10. 使用延迟计算属性

你想将一个只读属性定义成一个property，并且只在访问的时候才会计算结果。 但是一旦被访问后，你希望结果值被缓存起来，不用每次都去计算。

定义一个延迟属性的一种高效方法是通过使用一个描述器类，如下所示：

```python
class lazyproperty(object):
    def __init__(self, func):
        self.func = func

    def __get__(self, instance, cls):
        if instance is None:
            return self
        else:
            value = self.func(instance)
            setattr(instance, self.func.__name__, value)
            return value
```

你需要像下面这样在一个类中使用它：

```python
class Circle(object):
    def __init__(self, radius):
        self.radius = radius

    @lazyproperty  # 定义的描述器类似是类的全局方法，在类加载时初始化好，即在main函数之前进行初始化
    def area(self):
        print('Computing area')
        return math.pi * self.radius ** 2

    @lazyproperty
    def perimeter(self):
        print('Computing perimeter')
        return 2 * math.pi * self.radius
```

下面在一个交互环境中演示它的使用：

```python
if __name__ == '__main__':
    c = Circle(4.0)
    print(c.radius)
    print(c.area)
    print(c.area)

    print(c.perimeter)
    print(c.perimeter)
输出：
4.0
Computing area
50.26548245743669
50.26548245743669
Computing perimeter
25.132741228718345
25.132741228718345
```

仔细观察你会发现消息 Computing area 和 Computing perimeter 仅仅出现一次。

很多时候，构造一个延迟计算属性的主要目的是为了提升性能。 例如，你可以避免计算这些属性值，除非你真的需要它们。 这里演示的方案就是用来实现这样的效果的， 只不过它是通过以非常高效的方式使用描述器的一个精妙特性来达到这种效果的。

正如在其他小节(如8.9小节)所讲的那样，当一个描述器被放入一个类的定义时， 每次访问属性时它的 \_\_get\_\_() 、\_\_set\_\_() 和 \_\_delete\_\_() 方法就会被触发。 不过，如果一个描述器仅仅只定义了一个 \_\_get\_\_() 方法的话，它比通常的具有更弱的绑定。 特别地，只有当被访问属性不在实例底层的字典中时 \_\_get\_\_() 方法才会被触发。

lazyproperty 类利用这一点，使用 \_\_get\_\_() 方法在实例中存储计算出来的值， 这个实例使用相同的名字作为它的property。 这样一来，结果值被存储在实例字典中并且以后就不需要再去计算这个property了。 你可以尝试更深入的例子来观察结果：

```python
>>> c = Circle(4.0)
>>> # Get instance variables
>>> vars(c)
{'radius': 4.0}

>>> # Compute area and observe variables afterward
>>> c.area
Computing area
50.26548245743669
>>> vars(c)
{'area': 50.26548245743669, 'radius': 4.0}

>>> # Notice access doesn't invoke property anymore
>>> c.area
50.26548245743669

>>> # Delete the variable and see property trigger again
>>> del c.area
>>> vars(c)
{'radius': 4.0}
>>> c.area
Computing area
50.26548245743669
```

这种方案有一个小缺陷就是计算出的值被创建后是可以被修改的。例如：

```python
>>> c.area
Computing area
50.26548245743669
>>> c.area = 25
>>> c.area
25
```

如果你担心这个问题，那么可以使用一种稍微没那么高效的实现，就像下面这样：

```python
def lazyproperty(func):
    name = '_lazy_' + func.__name__

    @property
    def lazy(self):
        if hasattr(self, name):
            return getattr(self, name)
        else:
            value = func(self)
            setattr(self, name, value)
            return value

    return lazy
```

如果你使用这个版本，就会发现现在修改操作已经不被允许了：

```python
>>> c = Circle(4.0)
>>> c.area
Computing area
50.26548245743669
>>> c.area
50.26548245743669
>>> c.area = 25
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
AttributeError: can't set attribute
```

然而，这种方案有一个缺点就是所有get操作都必须被定向到属性的 getter 函数上去。 这个跟之前简单的在实例字典中查找值的方案相比效率要低一点。 如果想获取更多关于property和可管理属性的信息，可以参考8.6小节。而描述器的相关内容可以在8.9小节找到。

## 8.11. 简化数据结构的初始化

你写了很多仅仅用作数据结构的类，不想写太多烦人的 \_\_init\_\_() 函数。

可以在一个基类中写一个公用的 \_\_init\_\_() 函数：

```python
import math

class Structure1:
    # Class variable that specifies expected fields
    _fields = []

    def __init__(self, *args):
        if len(args) != len(self._fields):
            raise TypeError('Expected {} arguments'.format(len(self._fields)))
        # Set the arguments
        for name, value in zip(self._fields, args):
            setattr(self, name, value)
```

然后使你的类继承自这个基类:

```python
# Example class definitions
class Stock(Structure1):
    _fields = ['name', 'shares', 'price']


class Point(Structure1):
    _fields = ['x', 'y']


class Circle(Structure1):
    _fields = ['radius']

    def area(self):
        return math.pi * self.radius ** 2
```

使用这些类的示例：

```python
if __name__ == '__main__':
        s = Stock('ACME', 50, 91.1)
    print(s.__dict__)
    p = Point(2, 3)
    print(p.__dict__)
    c = Circle(4.5)
    print(c.__dict__)

    s2 = Stock('ACME', 50)
输出：
{'name': 'ACME', 'shares': 50, 'price': 91.1}
{'x': 2, 'y': 3}
{'radius': 4.5}
Traceback (most recent call last):
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 42, in <module>
    s2 = Stock('ACME', 50)
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 12, in __init__
    raise TypeError('Expected {} arguments'.format(len(self._fields)))
TypeError: Expected 3 arguments
```

如果还想支持关键字参数，可以将关键字参数设置为实例属性：

```python
class Structure2:
    _fields = []

    def __init__(self, *args, **kwargs):
        if len(args) > len(self._fields):
            raise TypeError('Expected {} arguments'.format(len(self._field)))

        # Set all of the positional arguments
        for name, value in zip(self._fields, args):
            setattr(self, name, value)

        # Set the remaining keyword arguments
        for name in self._fields[len(args):]:
            setattr(self, name, kwargs.pop(name))

        # Check for any remaining unknown arguments
        if kwargs:
            raise TypeError('Invalid argument(s): {}'.format(','.join(kwargs)))


# Example use
class Stock(Structure2):
    _fields = ['name', 'shares', 'price']


if __name__ == '__main__':
    s1 = Stock('ACME', 50, 91.1)
    s2 = Stock('ACME', 50, price=91.1)
    s3 = Stock('ACME', shares=50, price=91.1)

    s4 = Stock('ACME', shares=50, price=91.1, aa=1)
输出：
Traceback (most recent call last):
  File "D:\Program Files\JetBrains\PyCharm 2019.1.3\helpers\pydev\pydevd.py", line 1758, in <module>
    main()
  File "D:\Program Files\JetBrains\PyCharm 2019.1.3\helpers\pydev\pydevd.py", line 1752, in main
    globals = debugger.run(setup['file'], None, None, is_module)
  File "D:\Program Files\JetBrains\PyCharm 2019.1.3\helpers\pydev\pydevd.py", line 1147, in run
    pydev_imports.execfile(file, globals, locals)  # execute the script
  File "D:\Program Files\JetBrains\PyCharm 2019.1.3\helpers\pydev\_pydev_imps\_pydev_execfile.py", line 18, in execfile
    exec(compile(contents+"\n", file, 'exec'), glob, loc)
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 35, in <module>
    s3 = Stock('ACME', shares=50, price=91.1, aa=1)
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 22, in __init__
    raise TypeError('Invalid argument(s): {}'.format(','.join(kwargs)))
TypeError: Invalid argument(s): aa
```

你还能将不在 _fields 中的名称加入到属性中去：

```python
class Structure3:
    # Class variable that specifies expected fields
    _fields = []

    def __init__(self, *args, **kwargs):
        if len(args) != len(self._fields):
            raise TypeError('Expected {} arguments'.format(len(self._fields)))

        # Set the arguments
        for name, value in zip(self._fields, args):
            setattr(self, name, value)

        # Set the additional arguments (if any)
        extra_args = kwargs.keys() - self._fields
        for name in extra_args:
            setattr(self, name, kwargs.pop(name))

        if kwargs:
            raise TypeError('Duplicate values for {}'.format(','.join(kwargs)))


# Example use
class Stock(Structure3):
    _fields = ['name', 'shares', 'price']


if __name__ == '__main__':
    s1 = Stock('ACME', 50, 91.1)
    s2 = Stock('ACME', 50, 91.1, date='8/2/2012')
    s3 = Stock('ACME', 50, 91.1, date='8/2/2012', price=30)
输出：
Traceback (most recent call last):
  File "D:\Program Files\JetBrains\PyCharm 2019.1.3\helpers\pydev\pydevd.py", line 1758, in <module>
    main()
  File "D:\Program Files\JetBrains\PyCharm 2019.1.3\helpers\pydev\pydevd.py", line 1752, in main
    globals = debugger.run(setup['file'], None, None, is_module)
  File "D:\Program Files\JetBrains\PyCharm 2019.1.3\helpers\pydev\pydevd.py", line 1147, in run
    pydev_imports.execfile(file, globals, locals)  # execute the script
  File "D:\Program Files\JetBrains\PyCharm 2019.1.3\helpers\pydev\_pydev_imps\_pydev_execfile.py", line 18, in execfile
    exec(compile(contents+"\n", file, 'exec'), glob, loc)
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 53, in <module>
    s2 = Stock('ACME', 50, 91.1, date='8/2/2012', price=30)
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 43, in __init__
    raise TypeError('Duplicate values for {}'.format(','.join(kwargs)))
TypeError: Duplicate values for price
```

在上面的实现中我们使用了 setattr() 函数类设置属性值， 你可能不想用这种方式，而是想直接更新实例字典，就像下面这样：

```python
class Structure:
    # Class variable that specifies expected fields
    _fields= []
    def __init__(self, *args):
        if len(args) != len(self._fields):
            raise TypeError('Expected {} arguments'.format(len(self._fields)))

        # Set the arguments (alternate)
        self.__dict__.update(zip(self._fields,args))
```

尽管这也可以正常工作，但是当定义子类的时候问题就来了。 当一个子类定义了 __slots__ 或者通过property(或描述器)来包装某个属性， 那么直接访问实例字典就不起作用了。我们上面使用 setattr() 会显得更通用些，因为它也适用于子类情况。

这种方法唯一不好的地方就是对某些IDE而言，在显示帮助函数时可能不太友好。比如：

```python
>>> help(Stock)
Help on class Stock in module __main__:
class Stock(Structure)
...
| Methods inherited from Structure:
|
| __init__(self, *args, **kwargs)
|
...
>>>

# 本地PyCharm：
help(Stock)
显示：
Help on class Stock in module __main__:

class Stock(Structure3)
 |  Stock(*args, **kwargs)
 |  
 |  # Example use
 |  
 |  Method resolution order:
 |      Stock
 |      Structure3
 |      builtins.object
 |  
 |  Data and other attributes defined here:
 |  
 |  _fields = ['name', 'shares', 'price']
 |  
 |  ----------------------------------------------------------------------
 |  Methods inherited from Structure3:
 |  
 |  __init__(self, *args, **kwargs)
 |      Initialize self.  See help(type(self)) for accurate signature.
 |  
 |  ----------------------------------------------------------------------
 |  Data descriptors inherited from Structure3:
 |  
 |  __dict__
 |      dictionary for instance variables (if defined)
 |  
 |  __weakref__
 |      list of weak references to the object (if defined)
```

可以参考9.16小节来强制在 __init__() 方法中指定参数的类型签名。

## 8.12. 定义接口或者抽象基类

你想定义一个接口或抽象类，并且通过执行类型检查来确保子类实现了某些特定的方法

使用 abc 模块可以很轻松的定义抽象基类：

```python
from abc import ABCMeta, abstractmethod


class IStream(metaclass=ABCMeta):
    @abstractmethod
    def read(self, maxbytes=-1):
        pass

    @abstractmethod
    def write(self, data):
        pass
```

抽象类的一个特点是它不能直接被实例化，比如你想像下面这样做是不行的：

```python
a = IStream()
输出：
Traceback (most recent call last):
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 17, in <module>
    a = IStream()
TypeError: Can't instantiate abstract class IStream with abstract methods read, write
```

抽象类的目的就是让别的类继承它并实现特定的抽象方法：

```python
class SocketStream(IStream):
    def read(self, maxbytes=-1):
        pass

    def write(self, data):
        pass
```

抽象基类的一个主要用途是在代码中检查某些类是否为特定类型，实现了特定接口：

```python
def serialize(obj, stream):
    if not isinstance(stream, IStream):
        raise TypeError('Expected an IStream')
    pass

s = SocketStream()
serialize(object(), s)
serialize(object(), 'abc')  # 异常 TypeError: Expected an IStream
```

除了继承这种方式外，还可以通过注册方式来让某个类实现抽象基类：

```python
import io

# Register the built-in I/O classes as supporting our interface
IStream.register(io.IOBase)

# Open a normal file and type check
f = open('foo.txt')
isinstance(f, IStream) # Returns True
```

@abstractmethod 还能注解静态方法、类方法和 properties 。 你只需保证这个注解紧靠在函数定义前即可：

```python
class A(metaclass=ABCMeta):
    @property
    @abstractmethod
    def name(self):
        pass

    @name.setter
    @abstractmethod
    def name(self, value):
        pass

    @classmethod
    @abstractmethod
    def method1(cls):
        pass

    @staticmethod
    @abstractmethod
    def method2():
        pass
```

标准库中有很多用到抽象基类的地方。collections 模块定义了很多跟容器和迭代器(序列、映射、集合等)有关的抽象基类。 numbers 库定义了跟数字对象(整数、浮点数、有理数等)有关的基类。io 库定义了很多跟I/O操作相关的基类。

你可以使用预定义的抽象类来执行更通用的类型检查，例如：

```python
import collections

# Check if x is a sequence
if isinstance(x, collections.Sequence):
...

# Check if x is iterable
if isinstance(x, collections.Iterable):
...

# Check if x has a size
if isinstance(x, collections.Sized):
...

# Check if x is a mapping
if isinstance(x, collections.Mapping):
```

尽管ABCs可以让我们很方便的做类型检查，但是我们在代码中最好不要过多的使用它。 因为Python的本质是一门动态编程语言，其目的就是给你更多灵活性， 强制类型检查或让你代码变得更复杂，这样做无异于舍本求末。

注：metaclass元类，用于创建类的类 <https://www.cnblogs.com/piperck/p/5840443.html>

```python
class Test(object):
    def __init__(self):
        pass

s = SocketStream()
print(s.__class__)
print(s.__class__.__class__)
print(s.__class__.__class__.__class__)

t = Test()
print(t.__class__)
print(t.__class__.__class__)
print(t.__class__.__class__.__class__)
输出：
<class '__main__.SocketStream'>
<class 'abc.ABCMeta'>
<class 'type'>
<class '__main__.Test'>
<class 'type'>
<class 'type'>

# 使用type创建类：
input:
class FlyToSky(object):
    pass

pw = type('Trick', (FlyToSky, ), {'laugh_at': 'hahahaha'})
print pw().laugh_at
print pw.__dict__
print pw.__bases__
print pw().__class__
print pw().__class__.__class__


output:
hahahaha
{'__module__': '__main__', 'laugh_at': 'hahahaha', '__doc__': None}
(<class '__main__.FlyToSky'>,)
<class '__main__.Trick'>
<type 'type'>
```

自定义元类：

```python
def upper_attr(class_name, class_parents, class_attr):
    """
    返回一个对象,将属性都改为大写的形式
    :param class_name:  类的名称
    :param class_parents: 类的父类tuple
    :param class_attr: 类的参数
    :return: 返回类
    """
    # 生成了一个generator
    attrs = ((name, value) for name, value in class_attr.items() if not name.startswith('__'))
    uppercase_attrs = dict((name.upper(), value) for name, value in attrs)
    return type(class_name, class_parents, uppercase_attrs)


__metaclass__ = upper_attr

if __name__ == '__main__':
    pw = upper_attr('Trick', (), {'bar': 0})
    print(hasattr(pw, 'bar'))
    print(hasattr(pw, 'BAR'))
    print(pw.BAR)
输出：
False
True
0
```

可以从上面看到，我实现了一个元类(metaclass)， 然后指定了模块使用这个元类来创建类，所以当我下面使用type进行类创建的时候，可以发现小写的bar参数被替换成了大写的BAR参数，并且在最后我调用了这个类属性并，打印了它。

上面我们使用了函数做元类传递给类，下面我们使用一个正式类来作为元类传递给__metaclass__。（这是Py2的写法）

```python
class UpperAttrMetaClass(type):
    def __new__(mcs, class_name, class_parents, class_attr):
        attrs = ((name, value) for name, value in class_attr.items() if not name.startswith('__'))
        uppercase_attrs = dict((name.upper(), value) for name, value in attrs)
        return super(UpperAttrMetaClass, mcs).__new__(mcs, class_name, class_parents, uppercase_attrs)

# py2
# class Trick(object):
#     __metaclass__ = UpperAttrMetaClass
#     bar = 12
#     money = 'unlimited'

# py3
class Trick(metaclass=UpperAttrMetaClass):
    bar = 12
    money = 'unlimited'

if __name__ == '__main__':
    print(Trick.BAR)
    print(Trick.MONEY)
```

## 8.13. 实现数据模型的类型约束

你想定义某些在属性赋值上面有限制的数据结构。

在这个问题中，你需要在对某些实例属性赋值时进行检查。 所以你要自定义属性赋值函数，这种情况下最好使用描述器。

下面的代码使用描述器实现了一个系统类型和赋值验证框架：

```python
# Base class. Uses a descriptor to set a value
class Descriptor:
    def __init__(self, name=None, **opts):
        self.name = name
        for key, value in opts.items():
            setattr(self, key, value)

    def __set__(self, instance, value):
        instance.__dict__[self.name] = value

    # 这里如果不定义__get__方法，则调用s.name时，直接从s对象上获取属性name，如果定义了__get__方法，则调用__get__方法
    # def __get__(self, instance, cls):
    #     if instance is None:
    #         return self
    #     else:
    #         return instance.__dict__[self.name]

# Descriptor for enforcing types
class Typed(Descriptor):
    expected_type = type(None)

    def __set__(self, instance, value):
        if not isinstance(value, self.expected_type):
            raise TypeError('Expected ' + str(self.expected_type))
        super().__set__(instance, value)


# Descriptor for enforcing types
class Unsigned(Descriptor):
    def __set__(self, instance, value):
        if value < 0:
            raise ValueError('Expected >= 0')
        super().__set__(instance, value)


class MaxSized(Descriptor):
    def __init__(self, name=None, **opts):
        if 'size' not in opts:
            raise TypeError('Missing size option')
        super().__init__(name, **opts)

    def __set__(self, instance, value):
        if len(value) >= self.size:
            raise ValueError('Size must be < ' + str(self.size))
        super().__set__(instance, value)
```

这些类就是你要创建的数据模型或类型系统的基础构建模块。 下面就是我们实际定义的各种不同的数据类型：

```python
class Integer(Typed):
    expected_type = int


class UnsignedInteger(Integer, Unsigned):
    pass


class Float(Typed):
    expected_type = float


class UnsignedFloat(Float, Unsigned):
    pass


class String(Typed):
    expected_type = str


# self.name = name时，给SizedString对象赋值，先调用Typed.__set__方法（但是super().__set__(instance, value)不调用），再调用MaxSized.__set__方法，在MaxSized.__set__方法调用super().__set__(instance, value)方法，再从MaxSized.__set__方法退出，再从Typed.__set__方法退出。所有父类的__set__校验都会调到。
class SizedString(String, MaxSized):
    pass
```

然后使用这些自定义数据类型，我们定义一个类：

```python
class Stock:
    # Specify constraints
    name = SizedString('name', size=8)
    shares = UnsignedInteger('shares')
    price = UnsignedFloat('price')

    def __init__(self, name, shares, price):
        self.name = name
        self.shares = shares
        self.price = price
```

然后测试这个类的属性赋值约束，可发现对某些属性的赋值违法了约束是不合法的：

```python
>>> s = Stock('ACME', 70, 40.5)
>>> s.name
'ACME'
>>> s.shares = 75
>>> s.shares = -10
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
    File "example.py", line 17, in __set__
        super().__set__(instance, value)
    File "example.py", line 23, in __set__
        raise ValueError('Expected >= 0')
ValueError: Expected >= 0
>>> s.price = 'a lot'
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
    File "example.py", line 16, in __set__
        raise TypeError('expected ' + str(self.expected_type))
TypeError: expected <class 'float'>
>>> s.name = 'ABRACADABRA'
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
    File "example.py", line 17, in __set__
        super().__set__(instance, value)
    File "example.py", line 35, in __set__
        raise ValueError('size must be < ' + str(self.size))
ValueError: size must be < 8
```

还有一些技术可以简化上面的代码，其中一种是使用类装饰器：

```python
# Class decorator to apply constraints
def check_attributes(**kwargs):
    def decorate(cls):
        for key, value in kwargs.items():
            if isinstance(value, Descriptor):  # SizedString(size=8)满足这里的判断，是Descriptor的实例
                value.name = key
                setattr(cls, key, value)
            else:  # UnsignedInteger/UnsignedFloat是类名，所以不是Descriptor的实例，类是元类的实例，所以这里isinstance(value, type) == true
                setattr(cls, key, value(key))
        return cls

    return decorate


# Example
@check_attributes(name=SizedString(size=8),
                  shares=UnsignedInteger,
                  price=UnsignedFloat)
class Stock:
    def __init__(self, name, shares, price):
        self.name = name
        self.shares = shares
        self.price = price


if __name__ == '__main__':
    s = Stock('ACME', 70, 40.5)
    print(s.name)
    s.shares = 75
    # s.shares = -10
    # s.price = 'a lot'
    s.name = 'ABRACADABRA'
输出：
ACME
Traceback (most recent call last):
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 106, in <module>
    s.name = 'ABRACADABRA'
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 29, in __set__
    super().__set__(instance, value)
  File "D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py", line 48, in __set__
    raise ValueError('Size must be < ' + str(self.size))
ValueError: Size must be < 8
```

另外一种方式是使用元类：

```python
# A metaclass that applies checking
class checkedmeta(type):
    def __new__(cls, clsname, bases, methods):
        # Attach attribute names to the descriptors
        for key, value in methods.items():
            if isinstance(value, Descriptor):
                value.name = key
        return type.__new__(cls, clsname, bases, methods)


# Example
class Stock2(metaclass=checkedmeta):
    name = SizedString(size=8)
    shares = UnsignedInteger()
    price = UnsignedFloat()

    def __init__(self, name, shares, price):
        self.name = name
        self.shares = shares
        self.price = price


if __name__ == '__main__':
    s = Stock2('ACME', 70, 40.5)
    print(s.name)
    s.shares = 75
    # s.shares = -10
    # s.price = 'a lot'
    s.name = 'ABRACADABRA'  # ValueError: Size must be < 8
```

本节使用了很多高级技术，包括描述器、混入类、super() 的使用、类装饰器和元类。 不可能在这里一一详细展开来讲，但是可以在8.9、8.18、9.19小节找到更多例子。 但是，我在这里还是要提一下几个需要注意的点。

首先，在 Descriptor 基类中你会看到有个 _\_\set_\_\() 方法，却没有相应的 _\_\get_\_\() 方法。 如果一个描述仅仅是从底层实例字典中获取某个属性值的话，那么没必要去定义 _\_\get_\_\() 方法。

所有描述器类都是基于混入类来实现的。比如 Unsigned 和 MaxSized 要跟其他继承自 Typed 类混入。 这里利用多继承来实现相应的功能。

混入类的一个比较难理解的地方是，调用 super() 函数时，你并不知道究竟要调用哪个具体类。 你需要跟其他类结合后才能正确的使用，也就是必须合作才能产生效果。

使用类装饰器和元类通常可以简化代码。上面两个例子中你会发现你只需要输入一次属性名即可了。

```python
# Normal
class Point1:
    x = Integer('x')
    y = Integer('y')


# 类装饰器
@check_attributes(x=Integer, y=Integer)
class Point2:
    pass


# 元类Metaclass
class Point3(metaclass=checkedmeta):
    x = Integer()
    y = Integer()


if __name__ == '__main__':
    p = Point3()
    p.x = 10.5  # TypeError: Expected <class 'int'>
```

所有方法中，类装饰器方案应该是最灵活和最高明的。 首先，它并不依赖任何其他新的技术，比如元类。其次，装饰器可以很容易的添加或删除。

最后，装饰器还能作为混入类的替代技术来实现同样的效果;

```python
# Base class. Uses a descriptor to set a value
class Descriptor:
    def __init__(self, name=None, **opts):
        self.name = name
        for key, value in opts.items():
            setattr(self, key, value)

    def __set__(self, instance, value):
        instance.__dict__[self.name] = value


# Decorator for applying type checking
def Typed(expected_type, cls=None):
    if cls is None:
        return lambda cls: Typed(expected_type, cls)
    super_set = cls.__set__

    def __set__(self, instance, value):
        if not isinstance(value, expected_type):
            raise TypeError('Expected ' + str(expected_type))
        super_set(self, instance, value)

    cls.__set__ = __set__
    return cls


# Decorator for unsigned values
def Unsigned(cls):
    super_set = cls.__set__

    def __set__(self, instance, value):
        if value < 0:
            raise ValueError('Expected >= 0')
        super_set(self, instance, value)

    cls.__set__ = __set__
    return cls


# Decorator for allowing sized values
def MaxSized(cls):
    super_init = cls.__init__

    def __init__(self, name=None, **opts):
        if 'size' not in opts:
            raise TypeError('Missing size option')
        super_init(self, name, **opts)

    cls.__init__ = __init__

    super_set = cls.__set__

    def __set__(self, instance, value):
        if len(value) >= self.size:
            raise ValueError('Size must be < ' + str(self.size))
        super_set(self, instance, value)

    cls.__set__ = __set__
    return cls


# Specialized descriptors
@Typed(int)
class Integer(Descriptor):
    pass


@Unsigned
class UnsignedInteger(Integer):
    pass


@Typed(float)
class Float(Descriptor):
    pass


@Unsigned
class UnsignedFloat(Float):
    pass


@Typed(str)
class String(Descriptor):
    pass


@MaxSized
class SizedString(String):
    pass


class Stock:
    # Specify constraints
    name = SizedString('name', size=8)
    shares = UnsignedInteger('shares')
    price = UnsignedFloat('price')

    def __init__(self, name, shares, price):
        self.name = name
        self.shares = shares
        self.price = price


if __name__ == '__main__':
    s = Stock('ACME', 70, 40.5)
    print(s.name)  # ACME
    s.shares = 75
    # s.shares = -10
    # s.price = 'a lot'
    s.name = 'ABRACADABRA'  # ValueError: Size must be < 8
```

这种方式定义的类跟之前的效果一样，而且执行速度会更快。 设置一个简单的类型属性的值，装饰器方式要比之前的混入类的方式几乎快100%。 现在你应该庆幸自己读完了本节全部内容了吧？^_^

## 8.14. 实现自定义容器

你想实现一个自定义的类来模拟内置的容器类功能，比如列表和字典。但是你不确定到底要实现哪些方法。

collections 定义了很多抽象基类，当你想自定义容器类的时候它们会非常有用。 比如你想让你的类支持迭代，那就让你的类继承 collections.Iterable 即可：

```python
import collections
class A(collections.Iterable):
    pass
```

不过你需要实现 collections.Iterable 所有的抽象方法，否则会报错:

```python
>>> a = A()
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
TypeError: Can't instantiate abstract class A with abstract methods __iter__
```

你只要实现 __iter__() 方法就不会报错了(参考4.2和4.7小节)。

```python
import collections


class A(collections.Iterable):
    def __init__(self, l):
        self._list = l

    def __iter__(self):
        return iter(self._list)


if __name__ == '__main__':
    a = A(range(0, 5))
    for i in a:
        print(i)
输出：
D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py:6: DeprecationWarning: Using or importing the ABCs from 'collections' instead of from 'collections.abc' is deprecated, and in 3.8 it will stop working
0
1
2
3
4
  class A(collections.Iterable):
```

你可以先试着去实例化一个对象，在错误提示中可以找到需要实现哪些方法：

```python
>>> import collections
>>> collections.Sequence()
Traceback (most recent call last):
    File "<stdin>", line 1, in <module>
TypeError: Can't instantiate abstract class Sequence with abstract methods \
__getitem__, __len__
```

下面是一个简单的示例，继承自上面Sequence抽象类，并且实现元素按照顺序存储：

```python
import collections
import bisect


class SortedItems(collections.Sequence):
    def __init__(self, initial=None):
        self._items = sorted(initial) if initial is not None else []

    # Required sequence methods
    def __getitem__(self, index):
        return self._items[index]

    def __len__(self):
        return len(self._items)

    # Method for adding an item in the right location
    def add(self, item):
        bisect.insort(self._items, item)


if __name__ == '__main__':
    items = SortedItems([5, 1, 3])
    print(list(items))  # 调用__len__和__getitem__方法
    print(items[0], items[-1])
    items.add(2)
    print(list(items))
输出：
D:/Program Files/JetBrains/PythonProject/Py3TestProject/src/test/main.py:7: DeprecationWarning: Using or importing the ABCs from 'collections' instead of from 'collections.abc' is deprecated, and in 3.8 it will stop working
  class SortedItems(collections.Sequence):
[1, 3, 5]
1 5
[1, 2, 3, 5]
```

可以看到，SortedItems跟普通的序列没什么两样，支持所有常用操作，包括索引、迭代、包含判断，甚至是切片操作。

这里面使用到了 bisect 模块，它是一个在排序列表中插入元素的高效方式。可以保证元素插入后还保持顺序。

```python
import bisect

l = [5, 1, 3]
bisect.insort(l, 2)  # 非排序列表调用
print(l)  # [5, 1, 2, 3]
```

使用 collections 中的抽象基类可以确保你自定义的容器实现了所有必要的方法。并且还能简化类型检查。 你的自定义容器会满足大部分类型检查需要，如下所示：

```python
>>> items = SortedItems()
>>> import collections
>>> isinstance(items, collections.Iterable)
True
>>> isinstance(items, collections.Sequence)
True
>>> isinstance(items, collections.Container)
True
>>> isinstance(items, collections.Sized)
True
>>> isinstance(items, collections.Mapping)
False
```

collections 中很多抽象类会为一些常见容器操作提供默认的实现， 这样一来你只需要实现那些你最感兴趣的方法即可。假设你的类继承自 collections.MutableSequence ，如下：

```python
class Items(collections.MutableSequence):
    def __init__(self, initial=None):
        self._items = list(initial) if initial is not None else []

    # Required sequence methods
    def __getitem__(self, index):
        print('Getting:', index)
        return self._items[index]

    def __setitem__(self, index, value):
        print('Setting:', index, value)
        self._items[index] = value

    def __delitem__(self, index):
        print('Deleting:', index)
        del self._items[index]

    def insert(self, index, value):
        print('Inserting:', index, value)
        self._items.insert(index, value)

    def __len__(self):
        print('Len')
        return len(self._items)
```

如果你创建 Items 的实例，你会发现它支持几乎所有的核心列表方法(如append()、remove()、count()等)。 下面是使用演示：

```python
a = Items([1, 2, 3])
print(len(a))
a.append(4)
a.append(2)
a.append(3)
print(a.count(2))
a.remove(3)
print(list(a))
输出：
Len
3
Len
Inserting: 3 4
Len
Inserting: 4 2
  class Items(collections.MutableSequence):
Len
Inserting: 5 3
Getting: 0
Getting: 1
Getting: 2
Getting: 3
Getting: 4
Getting: 5
Getting: 6
2
Getting: 0
Getting: 1
Getting: 2
Deleting: 2
Len
Getting: 0
Getting: 1
Getting: 2
Getting: 3
Getting: 4
Getting: 5
[1, 2, 4, 2, 3]
```

本小节只是对Python抽象类功能的抛砖引玉。numbers 模块提供了一个类似的跟整数类型相关的抽象类型集合。 可以参考8.12小节来构造更多自定义抽象基类。

## 8.15. 属性的代理访问

你想将某个实例的属性访问代理到内部另一个实例中去，目的可能是作为继承的一个替代方法或者实现代理模式。

简单来说，代理是一种编程模式，它将某个操作转移给另外一个对象来实现。 最简单的形式可能是像下面这样：

```python
class A:
    def spam(self, x):
        print('A spam()')

    def foo(self):
        print('A foo()')


class B1:
    """简单的代理"""

    def __init__(self):
        self._a = A()

    def spam(self, x):
        # Delegate to the internal self._a instance
        return self._a.spam(x)

    def foo(self):
        # Delegate to the internal self._a instance
        return self._a.foo()

    def bar(self):
        pass
```

如果仅仅就两个方法需要代理，那么像这样写就足够了。但是，如果有大量的方法需要代理， 那么使用 __getattr__() 方法或许或更好些：

```python
class B2:
    """使用__getattr__的代理，代理方法比较多时候"""

    def __init__(self):
        self._a = A()

    def bar(self):
        print('B2 bar()')

    # Expose all of the methods defined on class A
    def __getattr__(self, name):
        """这个方法在访问的attribute不存在的时候被调用
        the __getattr__() method is actually a fallback method
        that only gets called when an attribute is not found"""
        return getattr(self._a, name)


if __name__ == '__main__':
    b2 = B2()
    b2.spam(10)  # Calls B.__getattr__('spam') and delegates to A.spam
    b2.foo()  # Calls B.__getattr__('spam') and delegates to A.spam
    b2.bar()  # Calls B.bar() (exists on B)
输出:
A spam()
A foo()
B2 bar()
```

另外一个代理例子是实现代理模式，例如：

```python
# A proxy class that wraps around another object, but
# exposes its public attributes
class Proxy:
    def __init__(self, obj):
        self._obj = obj

    # Delegate attribute lookup to internal obj
    def __getattr__(self, name):
        print('getattr:', name)
        return getattr(self._obj, name)

    # Delegate attribute assignment
    def __setattr__(self, name, value):
        if name.startwith('_'):
            super().__setattr__(name, value)
        else:
            print('setattr:', name, value)
            setattr(self._obj, name, value)

    # Delegate attribute deletion
    def __delattr__(self, name):
        if name.startwith('_'):
            super().__delattr__(name)
        else:
            print('delattr:', name)
            delattr(self._obj, name)
```

使用这个代理类时，你只需要用它来包装下其他类即可：

```python
class Spam:
    def __init__(self, x):
        self.x = x

    def bar(self, y):
        print('Spam.bar:', self.x, y)


if __name__ == '__main__':
    # Create an instance
    s = Spam(2)
    # Create a proxy around it
    p = Proxy(s)
    # Access the proxy
    print(p.x)
    p.bar(3)
    p.x = 37
输出：
getattr: x
2
getattr: bar
Spam.bar: 2 3
setattr: x 37
```

通过自定义属性访问方法，你可以用不同方式自定义代理类行为(比如加入日志功能、只读访问等)。

代理类有时候可以作为继承的替代方案。例如，一个简单的继承如下：

```python
class A:
    def spam(self, x):
        print('A.spam', x)
    def foo(self):
        print('A.foo')

class B(A):
    def spam(self, x):
        print('B.spam')
        super().spam(x)
    def bar(self):
        print('B.bar')
```

使用代理的话，就是下面这样：

```python
class A:
    def spam(self, x):
        print('A.spam', x)
    def foo(self):
        print('A.foo')

class B:
    def __init__(self):
        self._a = A()
    def spam(self, x):
        print('B.spam', x)
        self._a.spam(x)
    def bar(self):
        print('B.bar')
    def __getattr__(self, name):
        return getattr(self._a, name)
```

当实现代理模式时，还有些细节需要注意。 首先，\_\_getattr\_\_() 实际是一个后备方法，只有在属性不存在时才会调用。 因此，如果代理类实例本身有这个属性的话，那么不会触发这个方法的。 另外，\_\_setattr\_\_() 和 \_\_delattr\_\_() 需要额外的魔法来区分代理实例和被代理实例 \_obj 的属性。 一个通常的约定是只代理那些不以下划线 _ 开头的属性(代理类只暴露被代理类的公共属性)。

还有一点需要注意的是，\_\_getattr\_\_() 对于大部分以双下划线(\_\_)开始和结尾的属性并不适用。 比如，考虑如下的类：

```python
class ListLike:
    """__getattr__对于双下划线开始和结尾的方法是不能用的，需要一个个去重定义"""

    def __init__(self):
        self._items = []

    def __getattr__(self, name):
        return getattr(self._items, name)
```

如果是创建一个ListLike对象，会发现它支持普通的列表方法，如append()和insert()， 但是却不支持len()、元素查找等。例如：

```python
a = ListLike()
a.append(2)
a.insert(0, 1)
a.sort()
# len(a)  # TypeError: object of type 'ListLike' has no len()
# a[0]  # TypeError: 'ListLike' object is not subscriptable
```

为了让它支持这些方法，你必须手动的实现这些方法代理：

```python
class ListLike:
    """__getattr__对于双下划线开始和结尾的方法是不能用的，需要一个个去重定义"""

    def __init__(self):
        self._items = []

    def __getattr__(self, name):
        return getattr(self._items, name)

    # Added special methods to support certain list operations
    def __len__(self):
        return len(self._items)

    def __getitem__(self, index):
        return self._items[index]

    def __setitem__(self, index, value):
        self._items[index] = value

    def __delitem__(self, index):
        del self._items[index]


if __name__ == '__main__':
    a = ListLike()
    a.append(3)
    a.append(2)
    a.insert(0, 1)
    a.sort()
    print(len(a))
    print(a[0])
    print(list(a))
输出：
3
1
[1, 2, 3]
```

11.8小节还有一个在远程方法调用环境中使用代理的例子。

## 8.16. 在类中定义多个构造器
