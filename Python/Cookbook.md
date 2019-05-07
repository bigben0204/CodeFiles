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

最后要说的是，如果你的目标是定义一个需要更新很多实例属性的高效数据结构，那么命名元组并不是你的最佳选择。 这时候你应该考虑定义一个包含 __slots__ 方法的类（参考8.4小节）。

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

## 数字的四舍五入

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

## 执行精确的浮点数运算

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

## 数字的格式化输出