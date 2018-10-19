# 1. **Redis 配置**

<http://www.runoob.com/redis/redis-conf.html>

Redis 的配置文件位于 Redis 安装目录下，文件名为 redis.conf。

你可以通过 CONFIG 命令查看或设置配置项。

使用 * 号获取所有配置项：

    127.0.0.1:6379> CONFIG GET *
    1) "dbfilename"
    2) "dump.rdb"
    3) "requirepass"
    4) ""
    5) "masterauth"
    6) ""
    7) "unixsocket"
    8) ""

编辑配置

你可以通过修改 redis.conf 文件或使用 CONFIG set 命令来修改配置。

    redis 127.0.0.1:6379> CONFIG SET loglevel "notice"
    OK
    redis 127.0.0.1:6379> CONFIG GET loglevel
    1) "loglevel"
    2) "notice"

# 2. **Redis 数据类型**

<http://www.runoob.com/redis/redis-data-types.html>

## 2.1. **Redis 数据类型**

Redis支持五种数据类型：string（字符串），hash（哈希），list（列表），set（集合）及zset(sorted set：有序集合)。

## 2.2. **String（字符串）**

string 是 redis 最基本的类型，你可以理解成与 Memcached 一模一样的类型，一个 key 对应一个 value。

string 类型是二进制安全的。意思是 redis 的 string 可以包含任何数据。比如jpg图片或者序列化的对象。

string 类型是 Redis 最基本的数据类型，string 类型的值最大能存储 512MB。

注意：一个键最大能存储512MB。

## 2.3. **Hash（哈希）**

Redis hash 是一个键值(key=>value)对集合。

Redis hash 是一;个 string 类型的 field 和 value 的映射表，hash 特别适合用于存储对象。

实例

    redis> HMSET myhash field1 "Hello" field2 "World"
    "OK"
    redis> HGET myhash field1
    "Hello"
    redis> HGET myhash field2
    "World"

实例中我们使用了 Redis HMSET, HGET 命令，HMSET 设置了两个 field=>value 对, HGET 获取对应 field 对应的 value。

每个 hash 可以存储 232 -1 键值对（40多亿）。

## 2.4. **List（列表）**

Redis 列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边）。

列表最多可存储 232 - 1 元素 (4294967295, 每个列表可存储40多亿)。

## 2.5. **Set（集合）**

Redis的Set是string类型的无序集合。

集合是通过哈希表实现的，所以添加，删除，查找的复杂度都是O(1)。

注意：以上实例中 rabitmq 添加了两次，但根据集合内元素的唯一性，第二次插入的元素将被忽略。

集合中最大的成员数为 232 - 1(4294967295, 每个集合可存储40多亿个成员)。

## 2.6. **zset(sorted set：有序集合)**

Redis zset 和 set 一样也是string类型元素的集合,且不允许重复的成员。
不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。

zset的成员是唯一的,但分数(score)却可以重复。

zadd 命令
添加元素到集合，元素在集合中存在则更新对应score

# 3. **使用样例**


