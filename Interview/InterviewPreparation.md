# 1. **TDD测试**

## 1.1. **TDD测试原则**

First原则。快的，独立的，可重复的，自己验证的测试Case。

* F Fast: Tests are fast, so fast that developers run them with every small change without waits that break the flow.

* I Isolated: Tests are isolated. One test does not set up the next test. Tests also isolate failures.

* R Repeatable: Tests are repeatable; repeatable means automated. Tests run in a loop always giving the same result.

* S Self-verifying: Tests verify their outcome, reporting a simple “OK” when they pass while providing concise details when they fail.

* T Timely: Tests are timely. Programmers write them just in time, in lock-step (but just before) the production code, preventing bugs.

# 2. **数据库**

## 2.1. **给一些数据，怎么删除重复的数据只保留一条**

    sid birthday                gender  major  name
    1   2018-05-12 15:05:42     男      太极拳  张三
    2   2018-05-12 15:05:42     男      太极拳  李四
    3   2018-05-12 15:05:42     女      太极拳  王五
    4   2018-05-12 15:05:42     女      太极拳  赵六
    
    delete from t_students where t_students.sid not in (
    select t.t_sid from
    (select min(sid) as t_sid from t_students b group by b.major)
    as t
    )

也可以分几步创建临时表。参考：<https://www.cnblogs.com/nzbbody/p/4470638.html>

## 2.2. **一张表中，给某一字段设置了索引，在什么情况下会失效**

<https://www.2cto.com/database/201712/702834.html>

例如：

    create index idx_name_age on student(name,age);

* 未使用索引首列或使用列上无索引

        explain select * from student where age = 1;

* 不要在索引列上做任何操作

        select * from student where left(name,1) = '张' and age = 1;

* 尽量使用覆盖索引减少使用select *

        explain select * from student where name = 'zhangsan';
        修改为：
        explain select name from student where name = 'zhangsan';

* 使用不等于（！= 或者<>）不能使用索引

        explain select * from student where name != '张三';

* 使用 is null 或者 is not null 也不能使用索引

        explain select * from student where name is not null;

* like 已通配符开头（%abc）导致索引失效 （解决方法：使用覆盖索引）

        explain select * from student where name like '%张%';
        修改为：
        explain select * from student where name like '张%';
        或使用覆盖索引
        explain select name from student where name like '%张%';

* 少用or，用它来连接索引会失效

        explain select * from student where name = '张三' or age = 2;

## 2.3. **索引原理**

<https://www.cnblogs.com/aspwebchh/p/6652855.html>

要理解索引原理必须清楚一种数据结构「平衡树」(非二叉)，也就是b tree或者 b+ tree，重要的事情说三遍：“平衡树，平衡树，平衡树”。当然， 有的数据库也使用哈希桶作用索引的数据结构 ， 然而， 主流的RDBMS都是把平衡树当做数据表默认的索引数据结构的。

我们平时建表的时候都会为表加上主键， 在某些关系数据库中， 如果建表时不指定主键，数据库会拒绝建表的语句执行。 事实上， 一个加了主键的表，并不能被称之为「表」。一个没加主键的表，它的数据无序的放置在磁盘存储器上，一行一行的排列的很整齐， 跟我认知中的「表」很接近。如果给表上了主键，那么表在磁盘上的存储结构就由整齐排列的结构转变成了树状结构，也就是上面说的「平衡树」结构，换句话说，就是整个表就变成了一个索引。没错， 再说一遍，**整个表变成了一个索引，也就是所谓的「聚集索引」。 这就是为什么一个表只能有一个主键， 一个表只能有一个「聚集索引」，因为主键的作用就是把「表」的数据格式转换成「索引（平衡树）」的格式放置**。

每次给字段建一个新索引， 字段中的数据就会被复制一份出来， 用于生成索引。 因此， 给表添加索引，会增加表的体积， 占用磁盘存储空间。

非聚集索引和聚集索引的区别在于， 通过聚集索引可以查到需要查找的数据， 而通过非聚集索引可以查到记录对应的主键值 ， 再使用主键的值通过聚集索引查找到需要的数据

不管以任何方式查询表， 最终都会利用主键通过聚集索引来定位到数据， 聚集索引（主键）是通往真实数据所在的唯一路径。

然而， 有一种例外可以不使用聚集索引就能查询出所需要的数据， 这种非主流的方法 称之为「覆盖索引」查询， 也就是平时所说的复合索引或者多字段索引查询。 文章上面的内容已经指出， 当为字段建立索引以后， 字段中的内容会被同步到索引之中， 如果为一个索引指定两个字段， 那么这个两个字段的内容都会被同步至索引之中。

先看下面这个SQL语句

        //建立索引
    
        create index index_birthday on user_info(birthday);
    
        //查询生日在1991年11月1日出生用户的用户名
    
        select user_name from user_info where birthday = '1991-11-1'

这句SQL语句的执行过程如下

首先，通过非聚集索引index_birthday查找birthday等于1991-11-1的所有记录的主键ID值

然后，通过得到的主键ID值执行聚集索引查找，找到主键ID值对就的真实数据（数据行）存储的位置

最后， 从得到的真实数据中取得user_name字段的值返回， 也就是取得最终的结果

我们把birthday字段上的索引改成双字段的覆盖索引

        create index index_birthday_and_user_name on user_info(birthday, user_name);

这句SQL语句的执行过程就会变为

通过非聚集索引index_birthday_and_user_name查找birthday等于1991-11-1的叶节点的内容，然而， 叶节点中除了有user_name表主键ID的值以外， user_name字段的值也在里面， 因此不需要通过主键ID值的查找数据行的真实所在， 直接取得叶节点中user_name的值返回即可。 通过这种覆盖索引直接查找的方式， 可以省略不使用覆盖索引查找的后面两个步骤， 大大的提高了查询性能，如下图：

![index](pictures/覆盖索引查找.png)

## 2.4. **RDBMS与NoSql**

<https://blog.csdn.net/u010166386/article/details/70226136>

* **关系型数据库  V.S.  非关系型数据库**

关系型数据库的最大特点就是事务的一致性：传统的关系型数据库读写操作都是事务的，具有ACID的特点，这个特性使得关系型数据库可以用于几乎所有对一致性有要求的系统中，如典型的银行系统。

但是，在网页应用中，尤其是SNS应用中，一致性却不是显得那么重要，用户A看到的内容和用户B看到同一用户C内容更新不一致是可以容忍的，或者 说，两个人看到同一好友的数据更新的时间差那么几秒是可以容忍的，因此，关系型数据库的最大特点在这里已经无用武之地，起码不是那么重要了。

相反地，关系型数据库为了维护一致性所付出的巨大代价就是其读写性能比较差，而像微博、facebook这类SNS的应用，对并发读写能力要求极 高，关系型数据库已经无法应付(在读方面，传统上为了克服关系型数据库缺陷，提高性能，都是增加一级memcache来静态化网页，而在SNS中，变化太 快，memchache已经无能为力了)，因此，必须用新的一种数据结构存储来代替关系数据库。

关系数据库的另一个特点就是其具有固定的表结构，因此，其扩展性极差，而在SNS中，系统的升级，功能的增加，往往意味着数据结构巨大变动，这一点关系型数据库也难以应付，需要新的结构化数据存储。

于是，非关系型数据库应运而生，由于不可能用一种数据结构化存储应付所有的新的需求，因此，非关系型数据库严格上不是一种数据库，应该是一种数据结构化存储方法的集合。

必须强调的是，数据的持久存储，尤其是海量数据的持久存储，还是需要一种关系数据库这员老将。

* **非关系型数据库分类**

由于非关系型数据库本身天然的多样性，以及出现的时间较短，因此，不想关系型数据库，有几种数据库能够一统江山，非关系型数据库非常多，并且大部分都是开源的。

这些数据库中，其实实现大部分都比较简单，除了一些共性外，很大一部分都是针对某些特定的应用需求出现的，因此，对于该类应用，具有极高的性能。依据结构化方法以及应用场合的不同，主要分为以下几类：

* 面向高性能并发读写的key-value数据库：

key-value数据库的主要特点即使具有极高的并发读写性能，Redis,Tokyo Cabinet,Flare就是这类的代表

* 面向海量数据访问的面向文档数据库：

这类数据库的特点是，可以在海量的数据中快速的查询数据，典型代表为MongoDB以及CouchDB

* 面向可扩展性的分布式数据库：

这类数据库想解决的问题就是传统数据库存在可扩展性上的缺陷，这类数据库可以适应数据量的增加以及数据结构的变化

## 2.5. **数据库事务ACID特性**

<https://blog.csdn.net/u012440687/article/details/52116108>

数据库管理系统中事务(transaction)的四个特性（分析时根据首字母缩写依次解释）：原子性（Atomicity）、一致性（Consistency）、隔离性（Isolation）、持久性（Durability）

所谓事务，它是一个操作序列，这些操作要么都执行，要么都不执行，它是一个不可分割的工作单位。（执行单个逻辑功能的一组指令或操作称为事务）

## 2.6. **数据库设计需要遵循的原则**

<https://blog.csdn.net/edgar1989/article/details/50958310>

为了建立冗余较小、结构合理的数据库，设计数据库时必须遵循一定的规则。在关系型数据库中这种规则就称为范式。范式是符合某一种设计要求的总结。要想设计一个结构合理的关系型数据库，必须满足一定的范式。

在实际开发中最为常见的设计范式有三个：

1. 第一范式(确保每列保持原子性)

        第一范式是最基本的范式。如果数据库表中的所有字段值都是不可分解的原子值，就说明该数据库表满足了第一范式。
        
        第一范式的合理遵循需要根据系统的实际需求来定。比如某些数据库系统中需要用到“地址”这个属性，本来直接将“地址”属性设计成一个数据库表的字段就行。但是如果系统经常会访问“地址”属性中的“城市”部分，那么就非要将“地址”这个属性重新拆分为省份、城市、详细地址等多个部分进行存储，这样在对地址中某一部分操作的时候将非常方便。这样设计才算满足了数据库的第一范式，如下表所示。
        
        上表所示的用户信息遵循了第一范式的要求，这样在对用户使用城市进行分类的时候就非常方便，也提高了数据库的性能。

2. 第二范式(确保表中的每列都和主键相关)

        第二范式在第一范式的基础之上更进一层。第二范式需要确保数据库表中的每一列都和主键相关，而不能只与主键的某一部分相关（主要针对联合主键而言）。也就是说在一个数据库表中，一个表中只能保存一种数据，不可以把多种数据保存在同一张数据库表中。
        
        比如要设计一个订单信息表，因为订单中可能会有多种商品，所以要将订单编号和商品编号作为数据库表的联合主键，如下表所示。
        
        这样就产生一个问题：这个表中是以订单编号和商品编号作为联合主键。这样在该表中商品名称、单位、商品价格等信息不与该表的主键相关，而仅仅是与商品编号相关。所以在这里违反了第二范式的设计原则。
        
        而如果把这个订单信息表进行拆分，把商品信息分离到另一个表中，把订单项目表也分离到另一个表中，就非常完美了。如下所示。

3. 第三范式(确保每列都和主键列直接相关,而不是间接相关)

        第三范式需要确保数据表中的每一列数据都和主键直接相关，而不能间接相关。
        
        比如在设计一个订单数据表的时候，可以将客户编号作为一个外键和订单表建立相应的关系。而不可以在订单表中添加关于客户其它信息（比如姓名、所属公司等）的字段。如下面这两个表所示的设计就是一个满足第三范式的数据库表。
        
        这样在查询订单信息的时候，就可以使用客户编号来引用客户信息表中的记录，也不必在订单信息表中多次输入客户信息的内容，减小了数据冗余。

4. 范式总结

        第一范式：具有原子性
        
        第二范式：主键列与非主键列遵循完全函数依赖关系
        
        第三范式：非主键列之间没有传递函数依赖关系

设计规范：

        规则 1：弄清楚将要开发的应用程序是什么性质的（OLTP 还是 OPAP）？
        是事务处理型（增删改查多），还是分析型(更关注数据分析、报表、趋势预测)
    
        规则 4：把重复、不统一的数据当成你最大的敌人来对待
        前面的规则 2 即“第一范式”说的是避免 “重复组” 。下面这个图表作为其中的一个例子解释了 “重复组”是什么样子的。如果你仔细的观察 syllabus（课程） 这个字段，会发现在这一个字段里实在是填充了太多的数据了。像这些字段就被称为 “重复组” 了。如果我们又得必须使用这些数据，那么这些查询将会十分复杂并且我也怀疑这些查询会有性能问题。
        把行变列。
    
        规则 7：仔细地选择派生列
        这个规则也被称为 “三范式” 里的第三条：“不应该有依赖于非主键的列” 。 我的个人看法是不要盲目地运用这条规则，应该要看实际情况，冗余数据并不总是坏的。如果冗余数据是计算出来的，看看实际情况再来决定是否应用这第三范式。
    
        规则 8：如果性能是关键，不要固执地去避免冗余
        不要把 “避免冗余” 当作是一条绝对的规则去遵循。如果对性能有迫切的需求，考虑一下打破常规。常规情况下你需要做多个表的连接操作，而在非常规的情况下这样的多表连接是会大大地降低性能的。
    
        规则 9：多维数据是各种不同数据的聚合
        为这种情况做一个实际的设计是一个更好的办法。简单的说，你可以创建一个简单的主要销售表，它包含了销售额字段，通过外键将其他所有不同维度的表连接起来。
    
        规则 11：无限分级结构的数据，引用自己的主键作为外键
        我们会经常碰到一些无限父子分级结构的数据（树形结构？）。例如考虑一个多级销售方案的情况，一个销售人员之下可以有多个销售人员。注意到都是 “销售人员” 。也就是说数据本身都是一种。但是层级不同。这时候我们可以引用自己的主键作为外键来表达这种层级关系，从而达成目的。

## 2.7 [Redis 中常见的集群部署方案](https://baijiahao.baidu.com/s?id=1725335262935882644&wfr=spider&for=pc)(https://blog.csdn.net/dage_188/article/details/123236839)

- 主从集群模式
- 哨兵机制

核心功能就是:监控，选主，通知。

监控：哨兵机制，会周期性的给所有主服务器发出 PING 命令，检测它们是否仍然在线运行，如果在规定的时间内响应了 PING 通知则认为，仍在线运行；如果没有及时回复，则认为服务已经下线了，就会进行切换主库的动作。

选主：当主库挂掉的时候，会从从库中按照既定的规则选出一个新的的主库，

通知：当一个主库被新选出来，会通知其他从库，进行连接，然后进行数据的复制。当客户端试图连接失效的主库时，集群也会向客户端返回新主库的地址，使得集群可以使用新的主库。

如何保证选主的准确性

哨兵会通过 PING 命令检测它和从库，主库之间的连接情况，如果发现响应超时就会认为给服务已经下线了。

当然这会存在误判的情况，如果集群的网络压力比较大，网路堵塞，这时候会存在误判的情况。

如果误判的节点是从节点，影响不会很大，拿掉一个从节点，对整体的服务，影响不大，还是会不间断的对外提供服务。

如果误判的节点是主节点，影响就很大了，主节点被标注下线了，就会触发后续的选主，数据同步，等一连串的动作，这一连串的动作很很消耗性能的。所以对于误判，应该去规避。

如何减少误判呢？

引入哨兵集群，一个哨兵节点可能会进行误判，引入多个少哨兵节点一起做决策，就能减少误判了。

当有多个哨兵节点的时候，大多数哨兵节点认为主库下线了，主库才会真正的被标记为下线了，一般来讲当有 N 个哨兵实例时，最好要有`N/2 + 1`个实例判断主库下线了，才能最终判定主库的下线状态。当然这个数值在 Redis 中是可以配置的。

**如何选主**

**选举主节点的规则**

1、过滤掉已经下线的服务器；

2、过滤掉最近5秒钟没有回复过主节点的 INFO(用于观察服务器的角色) 命令的服务器，保证选中的服务器都是最近成功通过信的；

3、过滤掉和下线主服务器连接超过`down-after-milliseconds*10`毫秒的从服务器，`down-after-milliseconds`是主服务器下线的时间，这一操作避免从服务器与主服务器过早的断开，影响到从库中数据同步，因为断开时间越久，从库里面的数据就越老旧过时。

4、然后对这些服务器根据`slave-priority`优先级(这个优先级是手动设置的，比如希望那个从服务器优先变成主服务器，优先级就设置的高一点) 进行排序。

5、如果几台从服务器优先级相同，然后根据复制偏移量从大到小进行排序，如果还有相同偏移量的从服务器，然后按照 runID 从小到大进行排序，直到选出一台从服务器。

- 切片集群(分片集群)

**Redis Cluster方案**

1、`Redis Cluster`方案采用哈希槽来处理 KEY 在不同实例中的分布，一个切片集群共有16384个哈希槽，这些哈希槽类似于数据分区，每个键值对都会根据它的key，被映射到一个哈希槽中。

2、一个 KEY ，首先会根据CRC16算法计算一个16 bit的值；然后，再用这个 16bit 值对 16384 取模，得到0~16383范围内的模数，每个模数代表一个相应编号的哈希槽。

3、然后把哈希槽分配到所有的实例中，例如，如果集群中有N个实例，那么，每个实例上的槽个数为16384/N个。

当然这是平均分配的，如果平均分配额哈希槽中，某一个实例中 KEY，存储的数据比较大，造成某一个实例的内存过大，这时候可以通过`cluster addslots`手动调节哈希槽的分配。

当手动分配哈希槽时，需要把16384个槽都分配完，否则Redis集群无法正常工作。

**客户端中的 KEY 如何找到对应的实例**

在集群刚刚创建的时候，每个实例只知道自己被分配了哪些哈希槽，是不知道其他实例拥有的哈希槽信息的。但是，Redis 实例会把自己的哈希槽信息发给和它相连接的其它实例，来完成哈希槽分配信息的扩散。

所以当客户端和集群实例连接后，就可以知道所有的哈希槽的映射，客户端会把哈希槽的映射保存在本地，这样如果客户端响应一个 KEY ，计算出哈希槽，然后就可以向对应的实例发送请求了。

**避免 Hot Key**

`Hot Key`就是采用切片集群部署的 Redis ,出现的集群访问倾斜。

切片集群中的 Key 最终会存储到集群中的一个固定的 Redis 实例中。某一个 Key 在一段时间内访问远高于其它的 Key,也就是该 Key 对应的 Redis 实例,会收到过大的流量请求，该实例容易出现过载和卡顿现象，甚至还会被打挂掉。

常见引发热点 Key 的情况：

1、新闻中的热点事件；

2、秒杀活动中的，性价比高的商品；

### **Hot Key 如何解决**

知道了`Hot Key`如何来应对呢

- 1、对 Key 进行分散处理；

举个栗子

有一个热 Key 名字为`Hot-key-test`,可以将其分散为`Hot-key-test1`，`Hot-key-test2`...然后将这些 Key 分散到多个实例节点中，当客户端进行访问的时候，随机一个下标的 Key 进行访问，这样就能将流量分散到不同的实例中了，避免了一个缓存节点的过载。

一般来讲，可以通过添加后缀或者前缀，把一个 hotkey 的数量变成 redis 实例个数 N 的倍数 M，从而由访问一个`redis key`变成访问`N * M`个redis key。 `N*M`个`redis key`经过分片分布到不同的实例上，将访问量均摊到所有实例。

- 2、使用本地缓存;

业务端还可以使用本地缓存，将这些热 key 记录在本地缓存，来减少对远程缓存的冲击。

**避免 Big Key**

什么是 `Big Key`：我们将含有较大数据或含有大量成员、列表数的Key称之为大Key。

- 一个STRING类型的Key，它的值为5MB（数据过大）
- 一个LIST类型的Key，它的列表数量为20000个（列表数量过多）
- 一个ZSET类型的Key，它的成员数量为10000个（成员数量过多）
- 一个HASH格式的Key，它的成员数量虽然只有1000个但这些成员的value总大小为100MB（成员体积过大）

**Big Key 存在问题**

- 内存空间不均匀：如果采用切片集群的部署方案，容易造成某些实例节点的内存分配不均匀；
- 造成网络拥塞：读取 bigkey 意味着需要消耗更多的网络流量，可能会对 Redis 服务器造成影响；
- 过期删除：big key 不单读写慢，删除也慢，删除过期 big key 也比较耗时；
- 迁移困难：由于数据庞大，备份和还原也容易造成阻塞，操作失败；

**如何发现 Big Key**

- 使用 redis-cli 客户端的命令 --bigkeys;
- 生成 rdb 文件，离线分析 rdb 文件。比如：redis-rdb-cli，rdbtools;
- 通过 scan 命令，对扫描出来的key进行类型判断，例如：string长度大于10K，list长度大于10240认为是big bigkeys;

### **Big Key 如何避免**

对于`Big Key`可以从以下两个方面进行处理

- 合理优化数据结构：

1、对较大的数据进行压缩处理；

2、拆分集合：将大的集合拆分成小集合（如以时间进行分片）或者单个的数据。

- 选择其他的技术来存储 big key：

使用其他的存储形式，考虑使用 cdn 或者文档性数据库 MongoDB。

**Big Key 如何删除**

直接使用 DEL 命令会发生什么？危险：同步删除 bigkey 会阻塞 Redis 其他命令，造成 Redis 阻塞。

推荐使用 UNLINK 命令，异步删除 bigkey，不影响主线程执行其他命令。

在业务的低峰期使用 scan 命令查找 big key，对于类型为集合的key，可以使用脚本逐一删除里面的元素。

## 2.8 [redis使用场景之防止缓存穿透](https://juejin.cn/post/6844904097296941069)

###  [缓存篇-Redis缓存失效以及解决方案](https://blog.csdn.net/Simon_09010817/article/details/117291339)

一、缓存穿透
1.原因
缓存穿透是指缓存和数据库中都没有数据，用户不断发起请求，比如id卫负数或者id值特别大不存在的值，这时请求越过redis直接访问数据库，造成数据库压力过大。

2.解决方案
缓存空对象
布隆过滤器
mvc拦截
二、缓存雪崩
1.原因
缓存雪崩是指设置缓存时，redis中key设置了相同的过期时间，导致缓存在某一时刻同时失效，请求全部访问数据库，造成数据库短时间内压力过大，导致雪崩。

2.解决方案
将缓存数据的过期时间设置为随机，防止同一时间大量数据过期。
分布式部署的情况下，将热点数据均匀分布在不同的缓存数据库中
设置热点数据永不过期
出现雪崩进行服务降级、熔断
三、缓存击穿
1.缓存击穿和缓存雪崩的区别
缓存击穿是指并发查同一条数据。指的是缓存中没有数据，但是数据库中有数据。这时由于并发用户特别多，同时读缓存没有读取到数据，又同时去数据库查询，引起数据库压力瞬间增大
缓存雪崩是不同的数据都过期了，很多数据都查不到从而查数据库。
2.解决方案
设置热点数据永不过期
加锁。比较常用的做法是mutex。在缓存失效的时候，不立即去读取数据库。而是先使用缓存工具的某些带成功操作返回值的操作



### [如何避免缓存穿透、缓存击穿、缓存雪崩](https://blog.csdn.net/weixin_40725027/article/details/115349237)

缓存穿透

缓存空数据，使用2级缓存或布隆过滤器

**缓存穿透**
先来看一下缓存穿透，顾名思义，是指业务请求穿过了缓存层，落到持久化存储上。在大多数场景下，我们应用缓存是为了承载前端业务请求，缓存被击穿以后，如果请求量比较大，则会导致数据库出现风险。

以双十一为例，由于各类促销活动的叠加，整体网站的访问量、商品曝光量会是平时的千倍甚至万倍。巨大的流量暴涨，单靠数据库是不能承载的，如果缓存不能很好的工作，可能会影响数据库的稳定性，继而直接影响整体服务。 

那么哪些场景下会发生缓存穿透呢？

1. 不合理的缓存失效策略
   缓存失效策略如果设置不合理，比如设置了大量缓存在同一时间点失效，那么将导致大量缓存数据在同一时刻发生缓存穿透，业务请求直接打到持久化存储层。

2. 外部用户的恶意攻击
   外部恶意用户利用不存在的 Key，来构造大批量不存在的数据请求我们的服务，由于缓存中并不存在这些数据，因此海量请求全部穿过缓存，落在数据库中，将导致数据库崩溃。

 介绍了出现缓存穿透的原因，那么缓存穿透如何在业务中避免呢？

1. 首先是设置合理的缓存失效策略，避免缓存数据在同一时间失效。

缓存穿透还可以通过缓存空数据的方式避免。缓存空数据非常好理解，就是针对数据库不存在的数据，在查询为空时，添加一个对应 null 的值到缓存中，这样在下次请求时，可以通过缓存的结果判断数据库中是否存在，避免反复的请求数据库。不过这种方式，需要考虑空数据的 Key 在新增后的处理，感兴趣的同学可以思考一下。

2. 另外一个方案是使用布隆过滤器。布隆过滤器是应用非常广泛的一种数据结构，我们熟悉的 Bitmap，可以看作是一种特殊的布隆过滤器，布隆过滤器的实现细节不是本课时关注的重点，如果你对布隆过滤器还不熟悉，可以抽空查阅数据结构相关的资料学习。

使用布隆过滤器，可在缓存前添加一层过滤，布隆过滤器映射到缓存，在缓存中不存在的数据，会在布隆过滤器这一层拦截，从而保护缓存和数据库的安全。

**缓存击穿**
缓存击穿也是缓存应用常见的问题场景，其是一个非常形象的表达。具体表现：前端请求大量的访问某个热点 Key，而这个热点 Key 在某个时刻恰好失效，导致请求全部落到数据库上。

不知道你有没有听过二八定律（80/20 定律、帕累托法则），百度百科中对二八定律的具体描述是这样的：

在任何一组东西中，最重要的只占其中一小部分，约 20%，其余 80% 尽管是多数，却是次要的，因此又称二八定律。

二八定律在缓存应用中也不能避免，往往是 20% 的缓存数据，承担了 80% 或者更高的请求，剩下 80% 的缓存数据，仅仅承担了 20% 的访问流量。

由于二八定律的存在，缓存击穿虽然可能只是一小部分数据失效，但这部分数据如果恰好是热点数据，还是会对系统造成非常大的危险。

缓存击穿和缓存穿透都是降低了整体的缓存命中率，不过在表现上比较类似。缓存击穿可以认为是缓存穿透的一种特殊场景，所以在解决方案上也可以应用上面提到的那几种手段。

接下来看一下缓存雪崩，其是缓存穿透和缓存击穿升级的一种问题场景。

**缓存雪崩**
缓存雪崩的表现有两种，一种是大量的缓存数据在同一时刻失效，请求全部转发到数据库，将导致数据库压力过大，服务宕机；另外一种是缓存服务不稳定，比如负责缓存的 Redis 集群宕机。

在业务开发中，出现缓存雪崩非常危险，可能会直接导致大规模服务不可用，因为缓存失效时导致的雪崩，一方面是整体的数据存储链路，另一方面是服务调用链路，最终导致微服务整体的对外服务出现问题。

我们知道，微服务本身就存在雪崩效应，在电商场景中，如果商品服务不可用，最终可能会导致依赖的订单服务、购物车服务、用户浏览等级联出现故障。

你考虑一下，如果商品服务出现缓存雪崩，继而商品服务不可用，关联的周边服务都会受影响。

那么缓存雪崩在业务中如何避免呢？

首先是明确缓存集群的容量峰值，通过合理的限流和降级，防止大量请求直接拖垮缓存；其次是做好缓存集群的高可用，以 Redis 为例，可以通过部署 RedisCluster、Proxy 等不同的缓存集群，来实现缓存集群高可用。

## 2.9 [存储高可用](https://www.cnblogs.com/timePasser-leoli/p/12566256.html)

#### 二. MySQL篇

> MySQL作为当今最流行的开源数据库之一，高可用方案可谓五花八门，下面依次介绍！
>
> PS：下述MySQL常见架构中的从库，一般都可以进行只读操作，程序上如果进行`数据源拆分`基本都可以达到`分担压力`的效果，所以下述中所涉及到的`负载`更多是意味着该方案能否在`不拆分数据源`的情况下，依靠方案本身达到`负载均衡`的目的！
>
> 同理的话，故障转移也是，最简单的主从复制其实就可以实现`手动故障转移`，再配合`keepalived(中间件)`也可以达到`自动故障转移`的功能，所以下述中所涉及到的`故障转移`均意味着方案在不借助中间件的情况下可以实现`自动故障转移`，且对业务程序透明！

##### 2.1. 主从复制

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173131729001.png)

`主从复制`是MySQL数据库使用率非常高的一种技术，它使用某个数据库服务器为主库（Master），然后实时在其他数据库服务器上进行数据复制，后面复制的数据库也称从库（Slave），架构上可以根据业务需求而进行多种变化组合，因此引申出了`主主复制`，`一主多从`，`多主一从`，`联级复制`等高可用架构。

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173139343001.png)

`主从复制方案`主要是通过复制数据达到数据冗余的效果，其本身并不能实现负载，亦或是故障转移的功能，只能作为最基础的容灾手段！

##### 2.2. MySQL MHA

`MHA(Master High Availability)`是MySQL高可用方案中是一个相对成熟的解决方案。在主从复制的基础上，通过`Perl脚本`实现了一整套MySQL故障切换方案，保障了数据库的高可用性。

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173146551001.png)

MHA 可以实现`故障转移`功能，但并不具备`负载`的功能。另外需要注意的是，MHA在故障切换后，由于主从架构的变动，需要人工修复确认后，才可再次具备自动故障转移功能，自身并不具备`故障恢复（挂掉的主库无法自动重新加入集群）`功能！

MHA的自动故障转移主要通过`VIP漂浮`实现，我们常用的是通过`ifconfig命令+脚本`实现，当然也可以通过`keepalived`中间件实现。

##### 2.3. MySQL MGR

`MGR(MySQL Group Replication)`是MySQL官方于2016年12月（MySQL5.7.17 GA版）推出的一个全新的高可用与高扩展的解决方案。

MGR提供了`single-primary`和`multi-primary`两种模式：

1. `single-primary mode(单主模式)`即组内只有一个节点负责写入，读可以从任意节点读取，组内数据保持最终一致。

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173155560001.png)

1. `multi-primary mode(多主模式)`即组内所有节点均可以进行读写，同时保证组内数据最终一致性。

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173201450001.png)

MGR方案本身并不具备`故障转移`和`负载均衡`的能力，只有搭配其他第三方中间件后，才可以实现`故障转移及负载均衡`。

MGR具有`故障恢复`的能力，所谓`故障恢复`指的是主库挂掉后，拥有重新选举主库的能力，并且当老的主库恢复后，可以自动重新加入集群，而不需要人工修复。

PS：由于MGR目前功能并不完整，主要是官方尚未推出`故障转移`和`负载均衡`的对应解决方案，导致还需要其他第三方中间件配合实现，比较繁琐且复杂，所以公司目前主要还是采用MHA作为MySQL高可用方案，相信MGR会是今后MySQL最主流的高可用方案！

##### 2.6. MySQL InnoDB Cluster

`MIC(MySQL InnoDB Cluster)` 是MySQL官方在2017年4月推出了一套完整的、高可用的解决方案，。

MIC主要由三部分组成，分别是：`MySQL Shell`、`MySQL Router`、`MySQL Group Replication(MGR)`。

- MySQL Shell：通过内置的管理API创建及管理Innodb集群。
- MySQL Router：路由转发中间件，提供透明切换与读写分离能力。
- MySQL Group Replication：底层的数据同步集群(MGR)，提供容错、故障恢复与弹性扩展。

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173223775001.png)

这里需要说明的是MySQL Router的读写分离并`非对应用透明`，而是通过划分`读写端口(6446)`与 `只读端口(6447)`来转发不同的请求实现读写分离，本质还是需要程序端`根据业务类型重新定义数据源`才可以实现。

MIC是MySQL官方尝试在`不依赖于第三方中间件`的情况下，推出的一个`功能比较完整`的高可用方案，个人感觉还是不错的，起码官方开始尝试了，期待未来进一步的改进完善！

##### 2.8. 云数据库

> 所谓云数据库就是一种稳定可靠的`在线数据库服务`，常见的公有云数据库产品有以下几种。

1. **阿里云：DRDS**
   - DRDS 是一款基于 MySQL 存储、采用分库分表技术进行水平扩展的分布式 OLTP 数据库服务产品，支持 `RDS for MySQL` 以及 `POLARDB for MySQL`。

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173242151001.png)

1. **腾讯云：TDSQL**
   - TDSQL是部署在腾讯公有云上的一种支持自动水平拆分的share nothing架构的分布式数据库。

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173248631001.png)

1. **华为云：TaurusDB**
   - TaurusDB是华为基于`新一代DFV存储`与`计算存储分离架构`自研的新一代企业级高扩展海量存储分布式数据库。

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173324710001.png)

1. **UCloud：UDDB**
   - UDDB是基于公有云构建的新一代分布式数据库，为用户提供稳定、可靠、容量和服务能力可弹性伸缩的关系型数据库服务。

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173331638001.png)

上述几类云数据库是不同厂商基于MySQL改造或是自研的`分布式数据库`，完全兼容MySQL，并且在架构上实现了故障转移、读写分离、数据分片、水平扩展等配套功能。

云数据库`优势`：不需要关心后端的数据库架构及存储，可以专注于业务开发。

云数据库`劣势`：隐私安全问题、数据意外丢失风险、定制化服务不足。

#### 五. MongoDB篇

> MongoDB作为`非关系型数据库(NoSQL)`的典型，广泛应用于分布式文件存储，其`复制集`和`分片`的功能，可以作为WEB应用提高可扩展性的高性能数据存储解决方案之一。

##### 5.1. 复制集

`复制集`是由一组mongod实例组成的集群，其中一个节点为`主节点(Primary)`，其余节点都是`从节点(Secondary)`，主从节点之间通过`oplog`保持数据同步。

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173539975001.png)

复制集可以同时实现`自动故障转移`与`负载均衡`功能。

其自动故障转移的原理与SQL镜像类似，都是在`驱动层`实现，只要保证半数以上节点存活，整个复制集就可以对外提供服务。

01:27017,mongodb03:27017?replicaSet=rs0

负载均衡的原理是实现了`读写分离`，其读写分离也是通过`驱动层`实现，简而言之，就是在驱动层识别SQL为`写SQL`或是`读SQL`，然后选择转发到`主节点`或是`从节点`。

之所以MongoDB选择在SQL层实现读写分离，是因为NoSQL里没有事务的概念，也就不会有`分布式事务`的概念。

##### 5.2. 分片

`分片(sharding)`是MongoDB下通过`分片技术`从而进行`水平扩展`，用以支撑海量数据集和高吞吐量的方案。

`分片技术`指按照某个维度将存放在`单一数据库中`的`数据`分散存放至`多个数据库`或`数据表`中以达到提升性能与可用性的一种优化技术，该技术常应用在`分布式数据库`架构中。

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173546840001.png)

##### 5.3. 分片集群

如Oracle下`RAC+DG`一样，多个方案之间互补，形成更强的数据库架构一样，`分片集群`即是MongoDB下`复制集+分片`组合而成的一种高可用方案。

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173552757001.png)

- mongos：数据库集群请求的入口，路由转发的作用。
- shard：将表数据拆分后形成的分片，同一分片冗余在不同节点中。
- config server：存储所有数据库元信息（路由、分片）的配置。
- 仲裁：充当一个MongoDB实例，但不保存数据，主要用于集群投票。

分片集群同样可实现透明切换与负载均衡，其中复制集可以提供`高可用`，而分片可以提供`高性能`与`高吞吐量`

#### 六. Redis篇

> Redis是一个开源的高性能key-value数据库，是NoSQL的典型代表之一。

##### 6.1. Redis 多副本

Redis多副本，采用主从(replication) 部署结构，与MySQL主从复制如出一辙。

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173559586001.png)

Redis多副本不支持自动故障转移与负载功能，其从库只读，可以承担只读流量。

RedisHA过程可以自己开发对应的切换功能，或是由keepalived替代实现透明切换。

##### 6.2. Redis Sentinel

`Redis Sentinel(哨兵)`是Redis官方推出的高可用性解决方案，包含若干个Sentinel节点和Redis数据节点，每个Sentinel节点会对数据节点和其余Sentinel节点进行监控。

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173606642001.png)

Redis Sentinel可以实现自动故障转移，但不能实现负载，其实现原理类似于`MySQL MHA`，但是比MHA好的地方在于存在`多个监控节点(Sentinel)`，可以有效防止单点故障。

##### 6.3. Redis Cluster

`Redis Cluster`是Redis官方推出的分布式集群解决方案，其主要通过分布式架构解决了单节点Redis性能瓶颈的问题，可以理解为Redis下分片技术的实现。

![img](https://oa.epoint.com.cn/EpointCommunity/eWebEditor/uploadfile/20200320173612612001.png)

Redis Cluster中数据以`哈希槽(slot)`的形式，分布在不同的节点上，每个节点又采用了主从模式，防止单节点故障。

Redis Cluster中的一个重要概念便是去中心化，即每个节点都可以提供读写服务，类似于MySQL MGR中的多主模式。

Redis Cluster可以实现`自动故障转移`与`负载均衡`，均是在驱动层实现。



## 2.10 [Redis 如何实现消息队列](https://baijiahao.baidu.com/s?id=1724542173865359245&wfr=spider&for=pc)

### 一、消息队列#

消息队列（Messeage Queue，MQ）是在分布式系统架构中常用的一种中间件技术，从字面表述看，是一个存储消息的队列，所以它一般用于给 MQ 中间的两个组件提供通信服务。

### 1.1 消息队列介绍#

我们引入一个削峰填谷实际场景来介绍 MQ ，削峰填谷是指处理短时间内爆发的请求任务，将巨量请求任务“削峰”，平摊在平常请求任务较低的时间段，也就是“填谷”。 比如组件1 发布请求任务，组件2接受请求任务并处理。如果没有 MQ , 组件2 就会在大量的请求任务下会出现假死的情况：

![img](https://pics7.baidu.com/feed/b2de9c82d158ccbf6117340c11f85c37b1354102.jpeg?token=36a47fad3a219b13a072c505a8df505d)

而如果使用 MQ 后可以将这些请求先暂存到队列中，排队执行，就不会出现组件2 假死的情况了。我们一般把发送消息的组件称为生产者，接受消息的组件称为消费者，

消息队列需要满足**消息有序性、能处理重复的消息以及消息可靠性**，这样才能保证存取消息的一致性。

- **消息有序性**：虽然消费者异步读取消息，但是要按照生产者发送消息的顺序来处理消息，避免后发送的消息被先处理掉。
- **重复消息处理**：在消息队列存取信息时，有可能因为网络阻塞而出现消息重传的情况。可能会造成业务逻辑被多次执行，所以要避免重复消息的处理。
- **消息可靠性**：在组件故障时，比如消费者宕机或者没有处理完信息时，消息队列需要能提供消息可靠性保证。所以需要在消费者故障时，可以重新读取消息再次进行处理，不影响业务服务。

### 1.2 消息队列应用场景#

主要的应用有：异步处理、流量削峰、系统解耦

### 1.2.1 商品秒杀#

秒杀活动中，会短时间出现爆发式的用户请求，如果没有消息队列，会导致服务器响应不过来。轻则会导致服务假死；重则会让服务器直接宕机。

这时可以加上消息队列，服务器接收到用户的请求后，先把这些请求全部写入消息队列中再排队处理，这样就不会导致同时处理多个请求的情况；若消息队列长度超过承载的最大数量，可以抛弃后续的消息，给用户返回“页面出错，请重新刷新”提示，这样降低服务器的负载，而且也能给用户很好的交互体验。

### 1.2.2 系统解耦#

此外，我们可以利用消息队列来把系统的业务功能模块化，实现系统功能的解耦。如下图：

![img](https://pics4.baidu.com/feed/a5c27d1ed21b0ef4a3059faad7e4b1d380cb3e7a.jpeg?token=67a9a127f419bf8101259e18ef329d23)



如果有两个功能服务，而且关系不是很紧密，比如订单系统和优惠券，虽然都和用户有关联，但是如果都放在用户模块，面临功能删减时会很麻烦。所以采用把两个服务独立出来，而将两个服务的消息发送以约定的方式通过消息队列发送过去，让其对应的消费者分别处理即可达到系统解耦的目的。

### 1.3 常见的消息队列中间件#

### 1.3.1 RabbitMQ#

### 1.3.1.1 RabbitMQ 介绍

RabbitMQ 是一个老牌的开源消息中间件，它实现了标准的 AMQP（Advanced Message Queuing Protocol，高级消息队列协议）消息中间件，使用 Erlang 语言开发，支持集群部署。支持 java、python、Go、.NET 等等主流开发语言。

其主要的运行流程如下图：

![img](https://pics4.baidu.com/feed/024f78f0f736afc3e6dc7868b4390bcdb6451290.jpeg?token=b6d6ba6f62725c58a93040b67faa542f)

我们发现在 Rabbit 服务器中，它在生产者和队列间加入了交换器（ExChange）模块，它的作用和交换机很相似，它会根据配置的路由规则将生产者发出的消息分发到不同的队列中。路由规则很灵活，可以自己来进行设计。

### 1.3.1.2 RabbitMQ 特点

1. **支持持久化**，RabbitMQ 支持磁盘持久化功能，保证了消息不会丢失；
2. **高并发**，RabbitMQ 使用了 Erlang 开发语言，Erlang 是为电话交换机开发的语言，天生自带高并发光环和高可用特性；
3. **支持分布式集群**，正是因为 Erlang 语言实现的，因此 RabbitMQ 集群部署也非常简单，只需要启动每个节点并使用 --link 把节点加入到集群中即可，并且 RabbitMQ 支持自动选主和自动容灾；
4. **支持多种语言**，比如 Java、.NET、PHP、Python、JavaScript、Ruby、Go 等；
5. **支持消息确认**，支持消息消费确认（ack）保证了每条消息可以被正常消费；
6. **它支持很多插件**，比如网页控制台消息管理插件、消息延迟插件等，RabbitMQ 的插件很多并且使用都很方便。

因为中间中的交换器模块，所以RabbitMQ 有不同的消息类型，主要分为以下几种：

- **direct（默认类型）模式**，此模式为一对一的发送方式，也就是一条消息只会发送给一个消费者；
- **headers 模式**，允许你匹配消息的 header 而非路由键（RoutingKey），除此之外 headers 和 direct 的使用完全一致，但因为 headers 匹配的性能很差，几乎不会被用到；
- **fanout 模式**，为多播的方式，会把一个消息分发给所有的订阅者；
- **topic 模式**，为主题订阅模式，允许使用通配符（#、*）匹配一个或者多个消息，我可以使用“cn.mq.#”匹配到多个前缀是“cn.mq.xxx”的消息，比如可以匹配到“cn.mq.rabbit”、“cn.mq.kafka”等消息。

但是 Rabbit 也存在以下的问题：

- **RabbitMQ 对消息堆积的支持并不好**，当大量消息积压的时候，会导致 RabbitMQ 的性能急剧下降。
- **RabbitMQ 的性能是这几个消息队列中最差的**，大概每秒钟可以处理几万到十几万条消息。如果应用对消息队列的性能要求非常高，那不要选择 RabbitMQ。
- RabbitMQ 使用的编程语言 Erlang，**扩展和二次开发成本高**。

### 1.3.2 Kafka#

### 1.3.2.1 Kafka 介绍

> Kafka 是 LinkedIn 公司开发的基于 ZooKeeper 的多分区、多副本的分布式消息系统，它于 2010 年贡献给了 Apache 基金会，并且成为了 Apache 的顶级开源项目。其中 ZooKeeper 的作用是用来为 Kafka 提供集群元数据管理以及节点的选举和发现等功能。

与 RabbitMQ 不同中间的 Kafka 集群部分是由 Broker 代理和 ZooKeeper 集群组成：

![img](https://pics6.baidu.com/feed/f9198618367adab4954866f38cf453158601e494.jpeg?token=30cc7df12b00e8b4d294ecbcaea55f98)

### 1.3.2.2 Kafka 特点

- **Kafka 与周边生态系统的兼容性**是最好的没有之一，尤其在大数据和流计算领域，几乎所有的相关开源软件系统都会优先支持 Kafka。
- **Kafka 性能高效、可扩展良好并且可持久化**。它的分区特性，可复制和可容错都是不错的特性。
- **Kafka 使用 Scala 和 Java 语言开发**，设计上大量使用了批量和异步的思想，使得 Kafka 能做到超高的性能。Kafka 的性能，尤其是异步收发的性能，是三者中最好的，但与 RocketMQ 并没有量级上的差异，大约每秒钟可以处理几十万条消息。
- 在有足够的客户端并发进行异步批量发送，并且开启压缩的情况下，Kafka 的极限处理能力可以超过每秒 2000 万条消息。

同时 Kafka 也有缺点：

- Kafka 同步收发消息的响应时延较高。因为其异步批量的设计带来的问题，在它的 Broker 中，很多地方都会使用这种先攒一波再一起处理的设计。当你的业务场景中，每秒钟消息数量没有那么多的时候，Kafka 的时延反而会比较高。所以，Kafka 不太适合在线业务场景。

### 1.3.3 RocketMQ#

1.3.3.1 RocketMQ 介绍

> RocketMQ 是阿里巴巴开源的分布式消息中间件，用 Java 语言实现，在设计时参考了 Kafka，并做出了自己的一些改进，后来捐赠给 Apache 软件基金会。支持事务消息、顺序消息、批量消息、定时消息、消息回溯等。它里面有几个区别于标准消息中件间的概念，如Group、Topic、Queue等。系统组成则由Producer、Consumer、Broker、NameServer等

![img](https://pics4.baidu.com/feed/7a899e510fb30f24f2b1957ac3b5314aad4b034d.jpeg?token=2e061ca64a2586411dad81a136850af1)

RocketMQ 要求生产者和消费者必须是一个集群。集群级别的高可用，是RocketMQ 和其他 MQ 的区别。

- **Name Server（名称服务提供者）** ：是一个几乎无状态节点，可集群部署，节点之间没有任何信息同步。提供命令、更新和发现 Broker 服务

- **Broker （消息中转提供者）**：负责存储转发消息

- - broker分为 Master Broker 和 Slave Broker，一个 Master Broker 可以对应多个 Slave Broker，但是一个 Slave Broker 只能对应一个 Master Broker。

### 1.3.3.2 RocketMQ 特点

- 是一个队列模型的消息中间件，具有高性能、高可靠、高实时、分布式等特点
- Producer 向一些队列轮流发送消息，队列集合称为 Topic，Consumer 如果做广播消费，则一个 Consumer 实例消费这个 Topic 对应的所有队列，如果做集群消费，则多个 Consumer 实例平均消费这个 Topic 对应的队列集合
- RocketMQ 的性能比 RabbitMQ 要高一个数量级，每秒钟大概能处理几十万条消息
- RocketMQ 的劣势是与周边生态系统的集成和兼容程度不够。

### 2.1 基于List 实现消息队列

List 的先进先出其实就符合消息队列对消息有序性的需求。具体实现如下图：

![img](https://pics4.baidu.com/feed/902397dda144ad3462268d43c382ecfd30ad8506.jpeg?token=99a3889360f20a3907f8a0e0206e2646)



但是，在生产者往 List 中写入数据时，List 消息集合并不会主动地通知消费者有新消息写入。所以 Redis 提供了 `brpop` 命令， **`brpop` 命令也称为阻塞式读取，客户端在没有读到队列数据时，自动阻塞，直到有新的数据写入队列，再开始读取新数据**。此外，消息队列通过**给每一个消息提供全局唯一的 ID 号来解决分辨重复消息的需求**。而消息的最后一个需求，消息可靠性如何解决呢？为了留存消息，List 类型提供`brpoplpush` 命令来让消费者从一个 List 中读取消息，同时， Redis 会把这个消息再插入到另一个 List 中留存。这样如果消费者处理时发生宕机，再次重启时，也可以从备份 List 中重新读取消息并进行处理。如下图：

![img](https://pics7.baidu.com/feed/4610b912c8fcc3ce024cfb28b0653681d53f20c2.png?token=20ea67e023ab67e594b255d4df0519fb)

### 2.2 基于发布订阅实现消息队列

Redis 主要有两种发布/订阅模式：基于频道（channel）和基于模式（pattern）的发布/订阅。

### 2.2.1 基于频道的发布/订阅

在 Redis 2.0 之后 Redis 就新增了专门的发布和订阅的类型，Publisher（发布者）和 Subscriber（订阅者）来实现消息队列了，它们对应的执行命令如下：

```
# 发布消息publish channel "message"# 订阅消息subscribe channel# 取消订阅unsubscribe channel
```

![img](https://pics0.baidu.com/feed/d53f8794a4c27d1e27e4fb6113f54d67dcc4387b.jpeg?token=cf35332b4f63c4df76dad0d381ebb243)



### 2.2.2 基于模式的发布/订阅

除了订阅频道外，客户端还可以通过 `psubscribe` 命令订阅一个或者多个模式，从而成为这些模式的订阅者，它还会被发送给所有与这个频道相匹配的模式的订阅者，命令如下：

```
# 订阅模式psubscribe pattern# 退订模式punsubscribe pattern
```

![img](https://pics2.baidu.com/feed/ac4bd11373f08202801b8a9d4cdb1be4aa641bb4.jpeg?token=1c0075885949ffebe0e2767ddfb6ef4e)

那么我们如何用发布/订阅来实现消息队列？我们可以使用模式订阅的功能，利用一个消费者"queue_"来订阅所有以"queue__"开头的消息队列。如下图：

![img](https://pics5.baidu.com/feed/a50f4bfbfbedab646492dbb1fd164fca78311e8c.jpeg?token=1b134a64407afbcd02fd0f1c306e5062)

但是发布订阅模式也存在以下缺点：

- 无法持久化保存消息
- 发布订阅模式是“先发后忘”的工作模式，若有订阅者离线，重连后不能消费之前的历史消息
- 不支持消费者确认机制，稳定性无法得到保证

### 2.3 基于Stream 实现消息队列

然而在 Redis 5.0 之后新增了 Stream 类型，它提供了丰富的消息队列操作命令：

- **XADD**：插入消息，**保证 MQ 有序**，可以自动生成全局唯一 ID

  ```
  # mqstream 为消息队列，消息的键是 repo 值为5# * 表示自动生成一个全局唯一IDXADD mqstream * repo 5
  ```

- **XREAD**：用于读取消息，可以按 ID 读取数据，保证**MQ对重复消息的处理**；

  ```
  # 从 1599203861727-0 起读取后续的所有消息XREAD BLOCK 100 STREAMS mqstream 1599203861727-0
  ```

  `XREAD` 后的block 配置项，类似于 `brpop` 命令的阻塞读取操作，后面的 100 的单位是毫秒，表示如果没有消息到来，`XREAD` 将阻塞 100 毫秒。

- **XREADGROUP**：按消费组形式读取消息；

  ```
  # 创建名为 group1 的消费组,其消费队列是 mqstreamXGROUP create mqstream group1 0# 让 group1 消费组里的消费者 consumer1 从 mqstream 中读取所有消息# 命令最后的参数 ">" 表示从第一条尚未被消费的消息开始读取XREADGROUP group group1 consumer1 streams mqstream >
  ```

  **使用消费组的目的是让组内的多个消费者共同分担读取消息，通常会让每个消费者读取部分消息，从而实现消息读取负载在多个消费者间是均衡分布的。**

- **XPENDING 和 XACK**：XPENDING 命令可以用来查询每个消费组内所有消费者已读取但尚未确认的消息（保证消费者在发生故障或宕机再次重启后，仍然可以读取未处理完的消息），而 XACK 命令用于向消息队列确认消息处理已完成。

### 2.4 总结

List 和 Streams 实现消息队列的特点和区别：

![img](https://pics1.baidu.com/feed/b219ebc4b74543a90eb7446b19376a8bb8011496.jpeg?token=f9ccf418ff1be7da79a7fc895ae89f85)

关于 Redis 是否适合做消息队列，引用一下蒋德钧老师的看法：

> Redis 是一个非常轻量级的键值数据库，部署一个 Redis 实例就是启动一个进程，部署 Redis 集群，也就是部署多个 Redis 实例。而 Kafka、RabbitMQ 部署时，涉及额外的组件，例如 Kafka 的运行就需要再部署 ZooKeeper。相比 Redis 来说，Kafka 和 RabbitMQ 一般被认为是重量级的消息队列。所以，关于是否用 Redis 做消息队列的问题，不能一概而论，我们需要考虑业务层面的数据体量，以及对性能、可靠性、可扩展性的需求。如果分布式系统中的组件消息通信量不大，那么，Redis 只需要使用有限的内存空间就能满足消息存储的需求，而且，Redis 的高性能特性能支持快速的消息读写，不失为消息队列的一个好的解决方案。

## 2.11 [SQL题目](https://blog.csdn.net/itcodexy/article/details/120574511)

建表：

```sql
--drop table student;
create table student(
   student_id varchar(100) not null,
   name varchar(100) not null,
   birth date not null,
   sex varchar(100) not null
);
insert into student(student_id,name,birth,sex) 
values('0001' , '猴子' , '1989-01-01' , '男');
 
insert into student(student_id,name,birth,sex) 
values('0002' , '猴子' , '1990-12-21' , '女');
 
insert into student(student_id,name,birth,sex) 
values('0003' , '马云' , '1991-12-21' , '男');
 
insert into student(student_id,name,birth,sex) 
values('0004' , '王思聪' , '1990-05-20' , '男');


--drop table score;
create table score(
   student_id varchar(100) not null,
   course_id varchar(100) not null,
   score int not null
);
insert into score(student_id,course_id,score) 
values('0001' , '0001' , 80);
 
insert into score(student_id,course_id,score) 
values('0001' , '0002' , 90);
 
insert into score(student_id,course_id,score) 
values('0001' , '0003' , 99);
 
insert into score(student_id,course_id,score) 
values('0002' , '0002' , 60);
 
insert into score(student_id,course_id,score) 
values('0002' , '0003' , 80);
 
insert into score(student_id,course_id,score) 
values('0003' , '0001' , 80);
 
insert into score(student_id,course_id,score) 
values('0003' , '0002' , 80);
 
insert into score(student_id,course_id,score) 
values('0003' , '0003' , 80);


--drop table course;
create table course(
   course_id varchar(100) not null,
   course_name varchar(100) not null,
   teacher_id varchar(100) not null
);
insert into course(course_id,course_name,teacher_id)
values('0001' , '语文' , '0002');
 
insert into course(course_id,course_name,teacher_id)
values('0002' , '数学' , '0001');
 
insert into course(course_id,course_name,teacher_id)
values('0003' , '英语' , '0003');


--drop table teacher;
create table teacher(
   teacher_id varchar(100) not null,
   teacher_name varchar(100) null
);
-- 教师表：添加数据
insert into teacher(teacher_id,teacher_name) 
values('0001' , '孟扎扎');
 
insert into teacher(teacher_id,teacher_name) 
values('0002' , '马化腾');
 
-- 这里的教师姓名是空值（null）
insert into teacher(teacher_id,teacher_name) 
values('0003' , null);
 
-- 这里的教师姓名是空字符串（''）
insert into teacher(teacher_id,teacher_name) 
values('0004' , '');
```

为了方便学习，我将50道面试题进行了分类

![ecbb92b48ecac4e829ae3f6ad7597d05.png](https://img-blog.csdnimg.cn/img_convert/ecbb92b48ecac4e829ae3f6ad7597d05.png)

查询姓“猴”的学生名单

![bab45077cff5d2bbf5c502688e7d444a.png](https://img-blog.csdnimg.cn/img_convert/bab45077cff5d2bbf5c502688e7d444a.png)

查询姓“孟”老师的个数

```sql
select count(教师号)
from teacher
where 教师姓名 like '孟%';
```

### 2.汇总统计分组分析

![12c0280a1142259a89f463d21f1d520f.png](https://img-blog.csdnimg.cn/img_convert/12c0280a1142259a89f463d21f1d520f.png)

面试题：查询课程编号为“0002”的总成绩

```go
--分析思路
--select 查询结果 [总成绩:汇总函数sum]
--from 从哪张表中查找数据[成绩表score]
--where 查询条件 [课程号是0002]
select sum(成绩)
from score
where 课程号 = '0002';

select sum(score) from score where course_id = '0002'
```

查询选了课程的学生人数

```go
--这个题目翻译成大白话就是：查询有多少人选了课程
--select 学号，成绩表里学号有重复值需要去掉
--from 从课程表查找score;
select count(distinct 学号) as 学生人数 
from score;

select count(distinct student_id) as num from score
```



![84841a310135c21137eed53452a065af.png](https://img-blog.csdnimg.cn/img_convert/84841a310135c21137eed53452a065af.png)

查询各科成绩最高和最低的分， 以如下的形式显示：课程号，最高分，最低分

```go
/*
分析思路
select 查询结果 [课程ID：是课程号的别名,最高分：max(成绩) ,最低分：min(成绩)]
from 从哪张表中查找数据 [成绩表score]
where 查询条件 [没有]
group by 分组 [各科成绩：也就是每门课程的成绩，需要按课程号分组];
*/
select 课程号,max(成绩) as 最高分,min(成绩) as 最低分
from score
group by 课程号;

select course_id, max(score), min(score) from score group by course_id;
```

查询每门课程被选修的学生数

```go
/*
分析思路
select 查询结果 [课程号，选修该课程的学生数：汇总函数count]
from 从哪张表中查找数据 [成绩表score]
where 查询条件 [没有]
group by 分组 [每门课程：按课程号分组];
*/
select 课程号, count(学号)
from score
group by 课程号;

select course_id, count(student_id) from score group by course_id
```

查询男生、女生人数

```go
/*
分析思路
select 查询结果 [性别，对应性别的人数：汇总函数count]
from 从哪张表中查找数据 [性别在学生表中，所以查找的是学生表student]
where 查询条件 [没有]
group by 分组 [男生、女生人数：按性别分组]
having 对分组结果指定条件 [没有]
order by 对查询结果排序[没有];
*/
select 性别,count(*)
from student
group by 性别;

select sex, count(1) from student group by sex
```

![320cf9893b8194ae89d98e0536d660f9.png](https://img-blog.csdnimg.cn/img_convert/320cf9893b8194ae89d98e0536d660f9.png)

查询平均成绩大于60分学生的学号和平均成绩

```go
/* 
题目翻译成大白话：
平均成绩：展开来说就是计算每个学生的平均成绩
这里涉及到“每个”就是要分组了
平均成绩大于60分，就是对分组结果指定条件
分析思路
select 查询结果 [学号，平均成绩：汇总函数avg(成绩)]
from 从哪张表中查找数据 [成绩在成绩表中，所以查找的是成绩表score]
where 查询条件 [没有]
group by 分组 [平均成绩：先按学号分组，再计算平均成绩]
having 对分组结果指定条件 [平均成绩大于60分]
*/
select 学号, avg(成绩)
from score
group by 学号
having avg(成绩)>60;

select student_id, avg(score) from score group by student_id having avg(score) > 60
```

查询至少选修两门课程的学生学号

```go
/* 
翻译成大白话：
第1步，需要先计算出每个学生选修的课程数据，需要按学号分组
第2步，至少选修两门课程：也就是每个学生选修课程数目>=2，对分组结果指定条件
分析思路
select 查询结果 [学号,每个学生选修课程数目：汇总函数count]
from 从哪张表中查找数据 [课程的学生学号：课程表score]
where 查询条件 [至少选修两门课程：需要先计算出每个学生选修了多少门课，需要用分组，所以这里没有where子句]
group by 分组 [每个学生选修课程数目：按课程号分组，然后用汇总函数count计算出选修了多少门课]
having 对分组结果指定条件 [至少选修两门课程：每个学生选修课程数目>=2]
*/
select 学号, count(课程号) as 选修课程数目
from score
group by 学号
having count(课程号)>=2;

select student_id, count(course_id) from score group by student_id having count(course_id) >= 2
```

查询同名同性学生名单并统计同名人数

```go
/* 
翻译成大白话，问题解析：
1）查找出姓名相同的学生有谁，每个姓名相同学生的人数
查询结果：姓名,人数
条件：怎么算姓名相同？按姓名分组后人数大于等于2，因为同名的人数大于等于2
分析思路
select 查询结果 [姓名,人数：汇总函数count(*)]
from 从哪张表中查找数据 [学生表student]
where 查询条件 [没有]
group by 分组 [姓名相同：按姓名分组]
having 对分组结果指定条件 [姓名相同：count(*)>=2]
order by 对查询结果排序[没有];
*/
select 姓名,count(*) as 人数
from student
group by 姓名
having count(*)>=2;

select name, count(name) from student group by name having count(name) >= 2
```

查询不及格的课程并按课程号从大到小排列

```go
/* 
分析思路
select 查询结果 [课程号]
from 从哪张表中查找数据 [成绩表score]
where 查询条件 [不及格：成绩 <60]
group by 分组 [没有]
having 对分组结果指定条件 [没有]
order by 对查询结果排序[课程号从大到小排列：降序desc];
*/
select 课程号
from score 
where 成绩<60
order by 课程号 desc;

select * from score where score < 95 order by score desc;
```

查询每门课程的平均成绩，结果按平均成绩升序排序，平均成绩相同时，按课程号降序排列

```go
/* 
分析思路
select 查询结果 [课程号,平均成绩：汇总函数avg(成绩)]
from 从哪张表中查找数据 [成绩表score]
where 查询条件 [没有]
group by 分组 [每门课程：按课程号分组]
having 对分组结果指定条件 [没有]
order by 对查询结果排序[按平均成绩升序排序:asc，平均成绩相同时，按课程号降序排列:desc];
*/
select 课程号, avg(成绩) as 平均成绩
from score
group by 课程号
order by 平均成绩 asc,课程号 desc;

select course_id, avg(score) as avg_score from score group by course_id order by avg_score, course_id desc
```

检索课程编号为“0004”且分数小于60的学生学号，结果按按分数降序排列

```go
/* 
分析思路
select 查询结果 []
from 从哪张表中查找数据 [成绩表score]
where 查询条件 [课程编号为“04”且分数小于60]
group by 分组 [没有]
having 对分组结果指定条件 []
order by 对查询结果排序[查询结果按按分数降序排列];
*/
select 学号
from score
where 课程号='04' and 成绩 <60
order by 成绩 desc;

select * from score where course_id = '0004' and score < 95 order by score desc
```

统计每门课程的学生选修人数(超过2人的课程才统计)

要求输出课程号和选修人数，查询结果按人数降序排序，若人数相同，按课程号升序排序

```go
/* 
分析思路
select 查询结果 [要求输出课程号和选修人数]
from 从哪张表中查找数据 []
where 查询条件 []
group by 分组 [每门课程：按课程号分组]
having 对分组结果指定条件 [学生选修人数(超过2人的课程才统计)：每门课程学生人数>2]
order by 对查询结果排序[查询结果按人数降序排序，若人数相同，按课程号升序排序];
*/
select 课程号, count(学号) as '选修人数'
from score
group by 课程号
having count(学号)>2
order by count(学号) desc,课程号 asc;

select course_id, count(student_id) as student_num from score group by course_id having student_num >= 2 order by student_num desc, course_id
```

查询两门以上不及格课程的同学的学号及其平均成绩

```go
/*
分析思路
先分解题目：
1）[两门以上][不及格课程]限制条件
2）[同学的学号及其平均成绩]，也就是每个学生的平均成绩，显示学号，平均成绩
分析过程：
第1步：得到每个学生的平均成绩，显示学号，平均成绩
第2步：再加上限制条件：
1）不及格课程
2）两门以上[不及格课程]：课程数目>2
/* 
第1步：得到每个学生的平均成绩，显示学号，平均成绩
select 查询结果 [学号,平均成绩：汇总函数avg(成绩)]
from 从哪张表中查找数据 [涉及到成绩：成绩表score]
where 查询条件 [没有]
group by 分组 [每个学生的平均：按学号分组]
having 对分组结果指定条件 [没有]
order by 对查询结果排序[没有];
*/
select 学号, avg(成绩) as 平均成绩
from score
group by 学号;
/* 
第2步：再加上限制条件：
1）不及格课程
2）两门以上[不及格课程]
select 查询结果 [学号,平均成绩：汇总函数avg(成绩)]
from 从哪张表中查找数据 [涉及到成绩：成绩表score]
where 查询条件 [限制条件：不及格课程，平均成绩<60]
group by 分组 [每个学生的平均：按学号分组]
having 对分组结果指定条件 [限制条件：课程数目>2,汇总函数count(课程号)>2]
order by 对查询结果排序[没有];
*/
select 学号, avg(成绩) as 平均成绩
from score
where 成绩 <60
group by 学号
having count(课程号)>=2;

select student_id, avg(score) from score where score < 95 group by student_id having count(course_id) >= 2
```

如果上面题目不会做，可以复习这部分涉及到的sql知识：

### 3.复杂查询

查询所有课程成绩小于60分学生的学号、姓名

【知识点】子查询

1.翻译成大白话

1）查询结果：学生学号，姓名 2）查询条件：所有课程成绩 < 60 的学生，需要从成绩表里查找，用到子查询

第1步，写子查询（所有课程成绩 < 60 的学生）

- select 查询结果[学号]
- from 从哪张表中查找数据[成绩表：score]
- where 查询条件[成绩 < 60]
- group by 分组[没有]
- having 对分组结果指定条件[没有]
- order by 对查询结果排序[没有]
- limit 从查询结果中取出指定行[没有];

```go
select 学号 
from score
where 成绩 < 60;
```

第2步，查询结果：学生学号，姓名，条件是前面1步查到的学号

- select 查询结果[学号,姓名]
- from 从哪张表中查找数据[学生表:student]
- where 查询条件[用到运算符in]
- group by 分组[没有]
- having 对分组结果指定条件[没有]
- order by 对查询结果排序[没有]
- limit 从查询结果中取出指定行[没有];

```go
select 学号,姓名
from student
where  学号 in (
select 学号 
from score
where 成绩 < 60
);

select distinct b.student_id, b.name from score a, student b where a.student_id = b.student_id and a.score < 90 
select a.student_id, a.name from student a where a.student_id in (select student_id from score where score < 90)
```

查询没有学全所有课的学生的学号、姓名

```go
/*
查找出学号，条件：没有学全所有课，也就是该学生选修的课程数 < 总的课程数
【考察知识点】in，子查询
*/
select 学号,姓名
from student
where 学号 in(
select 学号 
from score
group by 学号
having count(课程号) < (select count(课程号) from course)
); --这个写法应该有问题，如果某个学生一门课都没有修，则这里查不出来学号

select a.student_id, a.name, count(b.course_id) from student a left join score b on a.student_id = b.student_id group by a.student_id having count(b.course_id) < (select count(1) from course)

select a.student_id, a.name from student a where a.student_id in (select student_id from score group by student_id having count(course_id) < (select count(1) from course))
```

查询出只选修了两门课程的全部学生的学号和姓名

```go
select 学号,姓名
from student
where 学号 in(
select 学号
from score
group by 学号
having count(课程号)=2
);

select a.student_id, a.name, count(b.course_id) from student a, score b where a.student_id = b.student_id group by a.student_id having count(b.course_id) = 2
select a.student_id, a.name from student a where a.student_id in (select student_id from score group by student_id having count(course_id) = 2)
```



1990年出生的学生名单

![7981b0f319938bc0a5ad9eeeafa372e0.png](https://img-blog.csdnimg.cn/img_convert/7981b0f319938bc0a5ad9eeeafa372e0.png)

```go
/*
查找1990年出生的学生名单
学生表中出生日期列的类型是datetime
*/
select 学号,姓名 
from student 
where year(出生日期)=1990;
```

查询各科成绩前两名的记录

这类问题其实就是常见的：分组取每组最大值、最小值，每组最大的N条（top N）记录。

**案例：按课程号分组取成绩最大值所在行的数据**

我们可以使用分组（group by）和汇总函数得到每个组里的一个值（最大值，最小值，平均值等）。但是无法得到成绩最大值所在行的数据。

```go
select 课程号,max(成绩) as 最大成绩
from score 
group by 课程号;
```

![3b9c47ae9c28f30c791079072a904826.png](https://img-blog.csdnimg.cn/img_convert/3b9c47ae9c28f30c791079072a904826.png)

我们可以使用关联子查询来实现：

```go
select * 
from score as a 
where 成绩 = (
select max(成绩) 
from score as b 
where b.课程号 = a.课程号);
```

![6483f64e90c09f570c72bd9939ca7c6e.png](https://img-blog.csdnimg.cn/img_convert/6483f64e90c09f570c72bd9939ca7c6e.png)

上面查询结果课程号“0001”有2行数据，是因为最大成绩80有2个

```sql
select a.* from score a , (select course_id, max(score) as max_score from score group by course_id) b where a.course_id = b.course_id and a.score = b.max_score

select * from score a where score = (select max(score) from score b where b.course_id = a.course_id)
```

**案例：查询各科成绩前两名的记录**

第1步，查出有哪些组

我们可以按课程号分组，查询出有哪些组，对应这个问题里就是有哪些课程号

```go
select 课程号,max(成绩) as 最大成绩
from score 
group by 课程号;
```

![f01cda3b8fc9b2e28aded35f326abad2.png](https://img-blog.csdnimg.cn/img_convert/f01cda3b8fc9b2e28aded35f326abad2.png)

第2步：先使用order by子句按成绩降序排序（desc），然后使用limt子句返回topN（对应这个问题返回的成绩前两名）

```go
-- 课程号'0001' 这一组里成绩前2名
select * 
from score 
where 课程号 = '0001' 
order by 成绩  desc 
limit 2;
```

同样的，可以写出其他组的（其他课程号）取出成绩前2名的sql

第3步，使用union all 将每组选出的数据合并到一起

```go
-- 左右滑动可以可拿到全部sql
(select * from score where 课程号 = '0001' order by 成绩  desc limit 2)
union all
(select * from score where 课程号 = '0002' order by 成绩  desc limit 2)
union all
(select * from score where 课程号 = '0003' order by 成绩  desc limit 2);
```

### 4.多表查询

![3ada0811c680313aa5356d15003b7c4b.png](https://img-blog.csdnimg.cn/img_convert/3ada0811c680313aa5356d15003b7c4b.png)

查询所有学生的学号、姓名、选课数、总成绩

```go
select a.学号,a.姓名,count(b.课程号) as 选课数,sum(b.成绩) as 总成绩
from student as a left join score as b
on a.学号 = b.学号
group by a.学号;

select a.student_id, a.name, count(b.course_id), sum(b.score) from student a left join score b on a.student_id = b.student_id group by a.student_id
```

查询平均成绩大于85的所有学生的学号、姓名和平均成绩

```go
select a.学号,a.姓名, avg(b.成绩) as 平均成绩
from student as a left join score as b
on a.学号 = b.学号
group by a.学号
having avg(b.成绩)>85;

select a.student_id, a.name, avg(b.score) as avg_score from student a left join score b on a.student_id = b.student_id group by a.student_id having avg_score > 85
```

查询学生的选课情况：学号，姓名，课程号，课程名称

```sql
select a.学号, a.姓名, c.课程号,c.课程名称
from student a inner join score b on a.学号=b.学号
inner join course c on b.课程号=c.课程号;

select a.student_id, a.name, c.course_id, c.course_name from student a inner join score b on a.student_id = b.student_id inner join course c on b.course_id = c.course_id
```

查询出每门课程的及格人数和不及格人数

```sql
-- 考察case表达式
select 课程号,
sum(case when 成绩>=60 then 1 
  else 0 
    end) as 及格人数,
sum(case when 成绩 <  60 then 1 
  else 0 
    end) as 不及格人数
from score
group by 课程号;
    
select course_id, sum(case when score >= 60 then 1 else 0 end) as pass_num, sum(case when score < 60 then 1 else 0 end) as no_pass_num from score group by course_id
```

使用分段[100-85],[85-70],[70-60],[<60]来统计各科成绩，分别统计：各分数段人数，课程号和课程名称

```sql
-- 考察case表达式
select a.课程号,b.课程名称,
sum(case when 成绩 between 85 and 100 
  then 1 else 0 end) as '[100-85]',
sum(case when 成绩 >=70 and 成绩<85 
  then 1 else 0 end) as '[85-70]',
sum(case when 成绩>=60 and 成绩<70  
  then 1 else 0 end) as '[70-60]',
sum(case when 成绩<60 then 1 else 0 end) as '[<60]'
from score as a right join course as b 
on a.课程号=b.课程号
group by a.课程号,b.课程名称;
    
select a.course_id, b.course_name, sum(case when a.score between 85 and 100 then 1 else 0 end) as '[100-85]' from score a join course b on a.course_id = b.course_id group by a.course_id, b.course_name
```

查询课程编号为0003且课程成绩在80分以上的学生的学号和姓名|

```go
select a.学号,a.姓名
from student  as a inner join score as b on a.学号=b.学号
where b.课程号='0003' and b.成绩>80;

select * from student a inner join score b on a.student_id = b.student_id where b.course_id = '0003' and b.score > 80
```

#### 【面试题类型总结】这类题目属于行列如何互换，解题思路如下：

**【面试题】下面是学生的成绩表（表名score，列名：学号、课程号、成绩）**

![eee53983c917d41564e994f74e04ac5e.png](https://img-blog.csdnimg.cn/img_convert/eee53983c917d41564e994f74e04ac5e.png)

使用sql实现将该表行转列为下面的表结构

![a1f6944aa6d9cc770ca4d267a9c4c138.png](https://img-blog.csdnimg.cn/img_convert/a1f6944aa6d9cc770ca4d267a9c4c138.png)

**【解答】**

第1步，使用常量列输出目标表的结构

可以看到查询结果已经和目标表非常接近了

```go
select 学号,'课程号0001','课程号0002','课程号0003'
from score;
```

![54055ced31d611cde209d9458ef8fcbe.png](https://img-blog.csdnimg.cn/img_convert/54055ced31d611cde209d9458ef8fcbe.png)

第2步，使用case表达式，替换常量列为对应的成绩

```go
select 学号,
(case 课程号 when '0001' then 成绩 else 0 end) as '课程号0001',
(case 课程号 when '0002' then 成绩 else 0 end) as  '课程号0002',
(case 课程号 when '0003' then 成绩 else 0 end) as '课程号0003'
from score;
```

![f80774ad321f6098113298f97c1ebd78.png](https://img-blog.csdnimg.cn/img_convert/f80774ad321f6098113298f97c1ebd78.png)

在这个查询结果中，每一行表示了某个学生某一门课程的成绩。比如第一行是'学号0001'选修'课程号00001'的成绩，而其他两列的'课程号0002'和'课程号0003'成绩为0。

每个学生选修某门课程的成绩在下图的每个方块内。我们可以通过分组，取出每门课程的成绩。

![520ab2f09e361406ce012018e40c7598.png](https://img-blog.csdnimg.cn/img_convert/520ab2f09e361406ce012018e40c7598.png)

第3关，分组

分组，并使用最大值函数max取出上图每个方块里的最大值

```go
select 学号,
max(case 课程号 when '0001' then 成绩 else 0 end) as '课程号0001',
max(case 课程号 when '0002' then 成绩 else 0 end) as '课程号0002',
max(case 课程号 when '0003' then 成绩 else 0 end) as '课程号0003'
from score
group by 学号;
    
select student_id, max(case when course_id = '0001' then score else 0 end) as 'course_0001' , max(case when course_id = '0002' then score else 0 end) as 'course_0002', max(case when course_id = '0003' then score else 0 end) as 'course_0003'  from score group by student_id
```

这样我们就得到了目标表（行列互换）

![36df0f9887f548af81433675a12e1099.png](https://img-blog.csdnimg.cn/img_convert/36df0f9887f548af81433675a12e1099.png)



## 2.12 [redis数据类型及应用](https://blog.csdn.net/qq_39595769/article/details/121810180)



## 2.13 数据库表关联题目

![image-20220518003359006](pictures/student_score_domitory_info.png)

1.姓名和平均成绩？

select c.name, a.math from student_score a
inner join student c on a.student_id = c.id 
where a.english >= 60 and exists (select 1 from student_dormitory b where a.student_id = b.student_id and b.dormitory_id = 1)

或

select c.name, a.math from student_score a

inner join student c on a.student_id = c.id

where a.english >= 60 and a.student_id in (select student_id from student_dormitory b where b.dormitory_id = 1)

推荐使用第1中方法，exists性能会比in好

[为何 EXISTS(NOT EXIST) 与 JOIN(LEFT JOIN) 的性能会比 IN(NOT IN) 好 - JavaShuo](http://www.javashuo.com/article/p-rigdaezg-cw.html)

```sql
--两个学生的姓名，但不是平均成绩
select c.name, a.math from student_score a
inner join student c on a.student_id = c.id
where a.english >= 60 and a.student_id in (select student_id from student_dormitory b where b.dormitory_id = 1)

--取数学的平均成绩，只有一条记录了
select c.name, avg(a.math) from student_score a
inner join student c on a.student_id = c.id
where a.english >= 60 and a.student_id in (select student_id from student_dormitory b where b.dormitory_id = 1)

--姓名和平均值，写得比较麻烦，查询了两次
select c.name, h.avg_math from student_score a
inner join student c on a.student_id = c.id
inner join (select avg(e.math) as avg_math from student_score e
            where e.english >= 60 and e.student_id in (select student_id from student_dormitory g 														where g.dormitory_id = 1)) h
where a.english >= 60 and a.student_id in (select student_id from student_dormitory b where b.dormitory_id = 1)

--换了更清晰的写法
select d.name, e.avg_math 
from
(select c.name from student_score a
inner join student c on a.student_id = c.id
where a.english >= 60 and a.student_id in (select student_id from student_dormitory b where b.dormitory_id = 1)) d
,
(select avg(a.math) as avg_math from student_score a
where a.english >= 60 and a.student_id in (select student_id from student_dormitory b where b.dormitory_id = 1)) e
```



2.

select dormitory_id, count(1) as students_num

from student_dormitory 

group by dormitory_id 

having students_num > 3

 

3.

select a.id, a.name

from student a

where not exists (select 1 from student_score b where a.id = b.student_id)

或：

select a.id, a.name

from student a

left join student_score b on a.id = b.student_id

where b.id is null



<https://www.zhihu.com/question/490938417/answer/2168534234>

1、从执行计划来看，两个表都使用了索引，区别在于NOT EXISTS使用“DEPENDENT SUBQUERY”方式，而LEFT JOIN使用普通表关联的方式

2、从执行过程来看，LEFT JOIN方式主要消耗Sending data的上，在NOT EXISTS方式主要消耗在"executing"和“Sending data”两项上，受限于PROFILE只能记录100行结果，因此超过57万个"executing"和“Sending data”的组合项没有显示，虽然每次"executing"和“Sending data”的组合项消耗时间较少（约50毫秒），但由于执行次数较高，导致最终执行时间较长（50μs*578436=28921800us=28.92s）

如何在NOT EXISTS和LEFT JOIN中选择：

1、当外层数据较少时，子查询循环次数较少，使用NOT EXISTS并不会导致严重的性能问题，推荐使用NOT EXISTS方式。

2、当外层数据较大时，子查询消耗随外层数据量递增，查询性能较差，推荐使用LEFT JOIN方式

总结：

按照存在即合理是客观唯心主义的理论，NOT EXISTS以更直观地方式实现业务需求，在SQL复杂度上要远低于LEFT JOIN，且在生产执行计划时，NOT EXISTS方式相对更稳定些，LEFT JOIN可能会随统计信息变化而生产不同的执行计划。



[Mysql的not exists和left join谁的性能更好](https://www.zhihu.com/question/490938417)

两者相差无几，LEFT JOIN 性能会略好一些，因此建议使用 LEFT JOIN。



4.

insert into student(id, name, gender, hobby) values(7, 'Zoe', 'F', 'painting')

如果id是自增列，则不需要指定id

## 2.14 [一致性Hash原理与实现](https://www.jianshu.com/p/528ce5cd7e8f)

如果我们使用Hash的方式，每一张图片在进行分库的时候都可以定位到特定的服务器，示意图如图1-3所示：



![img](https:////upload-images.jianshu.io/upload_images/6555006-7899a430dc3142a8.png?imageMogr2/auto-orient/strip|imageView2/2/w/708/format/webp)

图1-3：使用Hash方式的命中缓存

从上图中，我们需要查询的是图`product.png`，由于我们有6台主服务器，所以计算的公式为：`hash(product.png) % 6 = 5`, 我们就可以定位到是5号主从，这们就省去了遍历所有服务器的时间，从而大大提升了性能。

### 使用Hash时遇到的问题

在上述hash取模的过程中，我们虽然不需要对所有Redis服务器进行遍历而提升了性能。但是，使用Hash算法缓存时会出现一些问题，`Redis服务器变动时，所有缓存的位置都会发生改变`。
 比如，现在我们的Redis缓存服务器增加到了8台，我们计算的公式从`hash(product.png) % 6 = 5`变成了`hash(product.png) % 8 = ?` 结果肯定不是原来的5了。
 再者，6台的服务器集群中，当某个主从群出现故障时，无法进行缓存，那我们需要把故障机器移除，所以取模数又会从6变成了5。我们计算的公式也会变化。

由于上面hash算法是使用取模来进行缓存的，为了规避上述情况，Hash一致性算法就诞生了~~

### 一致性Hash算法原理

一致性Hash算法也是使用取模的方法，不过，上述的取模方法是对服务器的数量进行取模，而一致性的Hash算法是对`2的32方`取模。即，一致性Hash算法将整个Hash空间组织成一个虚拟的圆环，Hash函数的值空间为`0 ~ 2^32 - 1(一个32位无符号整型)`，整个哈希环如下：

![img](https:////upload-images.jianshu.io/upload_images/6555006-1f81e81466729c6b.png?imageMogr2/auto-orient/strip|imageView2/2/w/830/format/webp)

图1-4：Hash圆环

 整个圆环以`顺时针方向组织`，圆环正上方的点代表0，0点右侧的第一个点代表1，以此类推。
 第二步，我们将各个服务器使用Hash进行一个哈希，具体可以选择服务器的IP或主机名作为关键字进行哈希，这样每台服务器就确定在了哈希环的一个位置上，比如我们有三台机器，使用IP地址哈希后在环空间的位置如图1-4所示：

![img](https:////upload-images.jianshu.io/upload_images/6555006-1f100c1012b06b40.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

图1-4：服务器在哈希环上的位置

现在，我们使用以下算法定位数据访问到相应的服务器：

> 将数据Key使用相同的函数Hash计算出哈希值，并确定此数据在环上的位置，从此位置沿环顺时针查找，遇到的服务器就是其应该定位到的服务器。

例如，现在有ObjectA，ObjectB，ObjectC三个数据对象，经过哈希计算后，在环空间上的位置如下：

![img](https:////upload-images.jianshu.io/upload_images/6555006-defb48ae9714580d.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

图1-5：数据对象在环上的位置

根据一致性算法，Object -> NodeA，ObjectB -> NodeB, ObjectC -> NodeC

### 一致性Hash算法的容错性和可扩展性

现在，假设我们的Node C宕机了，我们从图中可以看到，A、B不会受到影响，只有Object C对象被重新定位到Node A。所以我们发现，在一致性Hash算法中，如果一台服务器不可用，受影响的数据仅仅是此服务器到其环空间前一台服务器之间的数据（这里为Node C到Node B之间的数据），其他不会受到影响。如图1-6所示：

![img](https:////upload-images.jianshu.io/upload_images/6555006-cd54d5c30e9cad6f.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

图1-6：C节点宕机情况，数据移到节点A上

另外一种情况，现在我们系统增加了一台服务器Node X，如图1-7所示：

![img](https:////upload-images.jianshu.io/upload_images/6555006-8f61754de37eb380.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

图1-7：增加新的服务器节点X

此时对象ObjectA、ObjectB没有受到影响，只有Object C重新定位到了新的节点X上。
 如上所述：

> 一致性Hash算法对于节点的增减都只需重定位环空间中的一小部分数据，有很好的容错性和可扩展性。

### 数据倾斜问题

在一致性Hash算法服务节点太少的情况下，容易因为节点分布不均匀面造成`数据倾斜（被缓存的对象大部分缓存在某一台服务器上）问题`，如图1-8特例：

![img](https:////upload-images.jianshu.io/upload_images/6555006-c504a13cbe34e617.png?imageMogr2/auto-orient/strip|imageView2/2/w/806/format/webp)

图1-8：数据倾斜


 这时我们发现有大量数据集中在节点A上，而节点B只有少量数据。为了解决数据倾斜问题，一致性Hash算法引入了`虚拟节点机制`，即对每一个服务器节点计算多个哈希，每个计算结果位置都放置一个此服务节点，称为虚拟节点。
 具体操作可以为服务器IP或主机名后加入编号来实现，实现如图1-9所示：

![img](https:////upload-images.jianshu.io/upload_images/6555006-f15ec4f10a433beb.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

图1-9：增加虚拟节点情况

数据定位算法不变，只需要增加一步：虚拟节点到实际点的映射。
 所以加入虚拟节点之后，即使在服务节点很少的情况下，也能做到数据的均匀分布。



# 3. **Java**

## 3.1. **java类加载的过程**

<http://baijiahao.baidu.com/s?id=1572591315475500&wfr=spider&for=pc>

<https://www.cnblogs.com/xiaoxian1369/p/5498817.html>

类从被加载到JVM中开始，到卸载为止，整个生命周期包括：加载、验证、准备、解析、初始化、使用和卸载七个阶段。
其中类加载过程包括加载、验证、准备、解析和初始化五个阶段。

![classLife](pictures/类生命周期.png)

1、加载

简单的说，类加载阶段就是由类加载器负责根据一个类的全限定名来读取此类的二进制字节流到JVM内部，并存储在运行时内存区的方法区，然后将其转换为一个与目标类型对应的java.lang.Class对象实例（Java虚拟机规范并没有明确要求一定要存储在堆区中，只是hotspot选择将Class对戏那个存储在方法区中），这个Class对象在日后就会作为方法区中该类的各种数据的访问入口。

![classLoader](pictures/类加载过程.png)

2、链接

链接阶段要做的是将加载到JVM中的二进制字节流的类数据信息合并到JVM的运行时状态中，经由验证、准备和解析三个阶段。

    1）、验证
    验证类数据信息是否符合JVM规范，是否是一个有效的字节码文件，验证内容涵盖了类数据信息的格式验证、语义分析、操作验证等。
    
    格式验证：验证是否符合class文件规范
    
    语义验证：检查一个被标记为final的类型是否包含子类；检查一个类中的final方法视频被子类进行重写；确保父类和子类之间没有不兼容的一些方法声明（比如方法签名相同，但方法的返回值不同）
    
    操作验证：在操作数栈中的数据必须进行正确的操作，对常量池中的各种符号引用执行验证（通常在解析阶段执行，检查是否通过富豪引用中描述的全限定名定位到指定类型上，以及类成员信息的访问修饰符是否允许访问等）
    
    2）、准备
    
    为类中的所有静态变量分配内存空间，并为其设置一个初始值（由于还没有产生对象，实例变量不在此操作范围内）
    
    被final修饰的静态变量，会直接赋予原值；类字段的字段属性表中存在ConstantValue属性，则在准备阶段，其值就是ConstantValue的值
    
    3）、解析
    
    将常量池中的符号引用转为直接引用（得到类或者字段、方法在内存中的指针或者偏移量，以便直接调用该方法），这个可以在初始化之后再执行。
    
    可以认为是一些静态绑定的会被解析，动态绑定则只会在运行是进行解析；静态绑定包括一些final方法(不可以重写),static方法(只会属于当前类)，构造器(不会被重写)

3、初始化

将一个类中所有被static关键字标识的代码统一执行一遍，如果执行的是静态变量，那么就会使用用户指定的值覆盖之前在准备阶段设置的初始值；如果执行的是static代码块，那么在初始化阶段，JVM就会执行static代码块中定义的所有操作。

所有类变量初始化语句和静态代码块都会在编译时被前端编译器放在收集器里头，存放到一个特殊的方法中，这个方法就是<clinit>方法，即类/接口初始化方法。该方法的作用就是初始化一个中的变量，使用用户指定的值覆盖之前在准备阶段里设定的初始值。任何invoke之类的字节码都无法调用<clinit>方法，因为该方法只能在类加载的过程中由JVM调用。

如果父类还没有被初始化，那么优先对父类初始化，但在<clinit>方法内部不会显示调用父类的<clinit>方法，由JVM负责保证一个类的<clinit>方法执行之前，它的父类<clinit>方法已经被执行。

JVM必须确保一个类在初始化的过程中，如果是多线程需要同时初始化它，仅仅只能允许其中一个线程对其执行初始化操作，其余线程必须等待，只有在活动线程执行完对类的初始化操作之后，才会通知正在等待的其他线程。

## 3.2. **JDBC连接数据库**

    public static final String url = "jdbc:mysql:///hibernate?serverTimezone=UTC";
    //    public static final String url = "jdbc:mysql://127.0.0.1/hibernate?serverTimezone=UTC";
    //    public static final String url = "jdbc:mysql://localhost:3306/hibernate?serverTimezone=UTC";
    public static final String name = "com.mysql.cj.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "root123";
    
    public Connection conn = null;
    public PreparedStatement pst = null;
    
    Class.forName(name);//指定连接类型
    conn = DriverManager.getConnection(url, user, password);//获取连接
    pst = conn.prepareStatement(sql);//准备执行语句
    ret = db1.pst.executeQuery();//执行语句，得到结果集

## 3.3. **ArrayList、LinkedList、Vector区别**

<https://www.cnblogs.com/IvesHe/p/6108933.html>

List：

1.可以允许重复的对象。

2.可以插入多个null元素。

3.是一个有序容器，保持了每个元素的插入顺序，输出的顺序就是插入的顺序。

4.常用的实现类有 ArrayList、LinkedList 和 Vector。ArrayList 最为流行，它提供了使用索引的随意访问，而 LinkedList 则对于经常需要从 List 中添加或删除元素的场合更为合适。

Set：

1.不允许重复对象

2.无序容器，你无法保证每个元素的存储顺序，TreeSet通过 Comparator  或者 Comparable 维护了一个排序顺序。

3.只允许一个 null 元素

4.Set 接口最流行的几个实现类是 HashSet、LinkedHashSet 以及 TreeSet。最流行的是基于 HashMap 实现的 HashSet；TreeSet 还实现了 SortedSet 接口，因此 TreeSet 是一个根据其 compare() 和 compareTo() 的定义进行排序的有序容器。

Map:

1.Map不是collection的子接口或者实现类。Map是一个接口。

2.Map 的 每个 Entry 都持有两个对象，也就是一个键一个值，Map 可能会持有相同的值对象但键对象必须是唯一的。

3.TreeMap 也通过 Comparator  或者 Comparable 维护了一个排序顺序。

4.Map 里你可以拥有随意个 null 值但最多只能有一个 null 键。

5.Map 接口最流行的几个实现类是 HashMap、LinkedHashMap、Hashtable 和 TreeMap。（HashMap、TreeMap最常用）

* **List的功能方法**

实际上有两种List: 一种是基本的ArrayList,其优点在于随机访问元素，另一种是更强大的LinkedList,它并不是为快速随机访问设计的，而是具有一套更通用的方法。

List : 次序是List最重要的特点：它保证维护元素特定的顺序。List为Collection添加了许多方法，使得能够向List中间插入与移除元素(这只推荐LinkedList使用。)一个List可以生成ListIterator,使用它可以从两个方向遍历List,也可以从List中间插入和移除元素。

ArrayList : 由数组实现的List。允许对元素进行快速随机访问，但是向List中间插入与移除元素的速度很慢。ListIterator只应该用来由后向前遍历ArrayList,而不是用来插入和移除元素。因为那比LinkedList开销要大很多。

Vector：增加了并发访问控制，效率低一些

LinkedList : 对顺序访问进行了优化，向List中间插入与删除的开销并不大。随机访问则相对较慢。(使用ArrayList代替。)还具有下列方法：addFirst(), addLast(), getFirst(), getLast(), removeFirst() 和removeLast(), 这些方法 (没有在任何接口或基类中定义过)使得LinkedList可以当作堆栈、队列和双向队列使用。

* **Set的功能方法**

Set具有与Collection完全一样的接口，因此没有任何额外的功能，不像前面有两个不同的List。实际上Set就是Collection,只是行为不同。(这是继承与多态思想的典型应用：表现不同的行为。)Set不保存重复的元素(至于如何判断元素相同则较为负责)

Set : 存入Set的每个元素都必须是唯一的，因为Set不保存重复元素。加入Set的元素必须定义equals()方法以确保对象的唯一性。Set与Collection有完全一样的接口。Set接口不保证维护元素的次序。

HashSet : 为快速查找设计的Set。存入HashSet的对象必须定义hashCode()。

TreeSet : 保存次序的Set, 底层为树结构。使用它可以从Set中提取有序的序列。

LinkedHashSet : 具有HashSet的查询速度，且内部使用链表维护元素的顺序(插入的次序)。于是在使用迭代器遍历Set时，结果会按元素插入的次序显示。

<https://www.cnblogs.com/jinlinFighting/p/5775281.html>

1. Vector & ArrayList

1）  Vector的方法都是同步的(Synchronized),是线程安全的(thread-safe)，而ArrayList的方法不是，由于线程的同步必然要影响性能，因此,ArrayList的性能比Vector好。

2） 当Vector或ArrayList中的元素超过它的初始大小时,Vector会将它的容量翻倍,而ArrayList只增加50%的大小，这样,ArrayList就有利于节约内存空间。

## 3.4. **spring中的BeanFactory与ApplicationContext的作用和区别**

<https://blog.csdn.net/u011202334/article/details/51509235>

作用：

1. BeanFactory负责读取bean配置文档，管理bean的加载，实例化，维护bean之间的依赖关系，负责bean的声明周期。
2. ApplicationContext除了提供上述BeanFactory所能提供的功能之外，还提供了更完整的框架功能：

        a. 国际化支持
        
        b. 资源访问：Resource rs = ctx. getResource(“classpath:config.properties”), “file:c:/config.properties”
        
        c. 事件传递：通过实现ApplicationContextAware接口

3. 常用的获取ApplicationContext的方法：

FileSystemXmlApplicationContext：从文件系统或者url指定的xml配置文件创建，参数为配置文件名或文件名数组
ClassPathXmlApplicationContext：从classpath的xml配置文件创建，可以从jar包中读取配置文件
WebApplicationContextUtils：从web应用的根目录读取配置文件，需要先在web.xml中配置，可以配置监听器或者servlet来实现

        <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
        </listener>
        <servlet>
        <servlet-name>context</servlet-name>
        <servlet-class>org.springframework.web.context.ContextLoaderServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
        </servlet>

这两种方式都默认配置文件为web-inf/applicationContext.xml，也可使用context-param指定配置文件

        <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/myApplicationContext.xml</param-value>
        </context-param>

<https://blog.csdn.net/hotmocha/article/details/72935227>

我们如何得到Bean对象，spring都做了那些工作？BeanFactory提供了多种方式得到bean对象，getBean()方法是最核心得到bean对象。getBean主要由AbstractBeanFactory、AbstractAutowireCapableBeanFactory、以及DefaultListableBeanFactory实现

AbstractBeanFactory 实现了依赖关系处理
AbstractAutowireCapableBeanFactory 实现了bean的create过程
DefaultListableBeanFactory 实现了BeanDefinition的管理

## 3.5. **Spring声明式事务的实现原理**

没看懂

## 3.6. **thread和runnable区别**

<https://blog.csdn.net/u013755987/article/details/51855098>

至于两者的真正区别最主要的就是一个是继承，一个是实现；其他还有一些面向对象的思想，Runnable就相当于一个作业，而Thread才是真正的处理线程，我们需要的只是定义这个作业，然后将作业交给线程去处理，这样就达到了松耦合，也符合面向对象里面组合的使用，另外也节省了函数开销，继承Thread的同时，不仅拥有了作业的方法run()，还继承了其他所有的方法。综合来看，用Runnable比Thread好的多。

## 3.7. **数据库连接池配置**

<http://www.jb51.net/article/89492.htm>

## 3.8. **单例模式**

<https://www.cnblogs.com/NaLanZiYi-LinEr/p/7492571.html>

* 多线程时，使用synchronized关键字：

        class Singleton{
        private static Singleton singleton;
        private Singleton(){}
        
        public static synchronized Singleton getInstance(){
                        if(singleton == null){
                        singleton = new Singleton();    //1
                        }
                        return singleton;
                }
        }
        
        但是每次进行同步检测，性能较低。

* 双重检测

        class Singleton{
        private static Singleton singleton;
        private Singleton(){}
        
        public static Singleton getInstance(){
                if(singleton == null){
                synchronized(Singleton.class){
                        if(singleton == null)
                        singleton = new Singleton();   //1
                }
                }
                return singleton;
        }
        }
        
        到这里已经很完美了，看起来没有问题。但是这种双重检测机制在JDK1.5之前是有问题的，问题还是出在//1，由所谓的无序写入造成的。一般来讲，当初始化一个对象的时候，会经历内存分配、初始化、返回对象在堆上的引用等一系列操作，这种方式产生的对象是一个完整的对象，可以正常使用。但是JAVA的无序写入可能会造成顺序的颠倒，即内存分配、返回对象引用、初始化的顺序，这种情况下对应到//1就是singleton已经不是null，而是指向了堆上的一个对象，但是该对象却还没有完成初始化动作。当后续的线程发现singleton不是null而直接使用的时候，就会出现意料之外的问题。
        
        JDK1.5之后，可以使用volatile关键字修饰变量来解决无序写入产生的问题，因为volatile关键字的一个重要作用是禁止指令重排序，即保证不会出现内存分配、返回对象引用、初始化这样的顺序，从而使得双重检测真正发挥作用。

* 采用非延迟加载的方式

        class Singleton{
        private static Singleton singleton = new Singleton();
        private Singleton(){}
        
        public static Singleton getInstance(){
                        return singleton;
                }
        }

* 延迟初始化占位（Holder）类模式 <https://blog.csdn.net/zhangzeyuaaa/article/details/42673245>
        package com.zzj.pattern.singleton;

        public class Singleton {
        private static class InstanceHolder {
                public static Singleton instance = new Singleton();
        }
        
        private Singleton() {
        }
        
        public static Singleton getInstance() {
                return InstanceHolder.instance;
        }
        }

## 3.9. **volatile原理**

<https://www.cnblogs.com/chenssy/p/6379280.html>

观察加入volatile关键字和没有加入volatile关键字时所生成的汇编代码发现，加入volatile关键字时，会多出一个lock前缀指令。lock前缀指令其实就相当于一个内存屏障。内存屏障是一组处理指令，用来实现对内存操作的顺序限制。volatile的底层就是通过内存屏障来实现的。

<https://blog.csdn.net/eff666/article/details/67640648>

但是用volatile修饰之后就变得不一样了：

第一：使用volatile关键字会强制将修改的值立即写入主存；

第二：使用volatile关键字的话，当线程2进行修改时，会导致线程1的工作内存中缓存变量stop的缓存行无效（反映到硬件层的话，就是CPU的L1或者L2缓存中对应的缓存行无效）；

第三：由于线程1的工作内存中缓存变量stop的缓存行无效，所以线程1再次读取变量stop的值时会去主存读取。

实现机制

前面讲述了源于volatile关键字的一些使用，下面我们来探讨一下volatile到底如何保证可见性和禁止指令重排序的。在x86处理器下通过工具获取JIT编译器生成的汇编指令来看看对Volatile进行写操作CPU会做什么事情。

        Java代码: instance = new Singleton();//instance是volatile变量
    
        汇编代码:  0x01a3de1d: movb $0x0,0x1104800(%esi);0x01a3de24: lock addl $0x0,(%esp);

观察加入volatile关键字和没有加入volatile关键字时所生成的汇编代码发现，加入volatile关键字时，会多出一个lock前缀指令。lock前缀指令实际上相当于一个内存屏障（也成内存栅栏），内存屏障会提供3个功能：

（1）它确保指令重排序时不会把其后面的指令排到内存屏障之前的位置，也不会把前面的指令排到内存屏障的后面；即在执行到内存屏障这句指令时，在它前面的操作已经全部完成；

（2）它会强制将对缓存的修改操作立即写入主内存；

（3）如果是写操作，它会导致其他CPU中对应的缓存行无效。

## 3.10 spring和spring boot区别

https://blog.csdn.net/m0_51660523/article/details/121023013

二者主要区别是：

1、Spring Boot提供极其快速和简化的操作，让 Spring 开发者快速上手。

2、Spring Boot提供了 Spring 运行的默认配置。

3、Spring Boot为通用 Spring项目提供了很多非功能性特性。



嵌入Tomcat、Jetty、Undertow等容器且不需要部署：

提供的"starters" poms来简化Maven配置:

提供生产指标,健壮检查和外部化配置；

绝对没有代码生成；

不需要设置Spring应用程序所需的XML配置要求。

简化打包部署应用程序的流程：



## 3.11 [Spring AOP 中两种代理模式](https://blog.csdn.net/faramita_of_mine/article/details/123342404)

静态代理会产生很多静态类，所以我们要想办法可以通过一个代理类完成全部的代理功能，这就引出了动态代理。

JDK原生动态代理

- 代理对象，不需要实现接口，但是目标对象要实现接口，否则不能用动态代理
- 代理对象的生成，是通过 JDK 的 API（反射机制），动态的在内存中构建代理对象

cglib代理
  静态代理和 JDK 代理模式都要求目标对象实现一个接口，但有时候目标对象只是一个单独的对象，并没有实现任何接口，这个时候就可以使用目标对象子类来实现代理，这就是 cglib 代理。

* cglib(Code Generation Library)是一个基于ASM的字节码生成库，它允许我们在运行时对字节码进行修改和动态生成。cglib 通过继承方式实现代理。它广泛的被许多AOP的框架使用，比如我们的 Spring AOP。
* cglib 包的底层是通过使用字节码处理框架 ASM 来转换字节码并生成新的类。
* cglib 代理也被叫做子类代理，它是在内存中构建一个子类对象从而实现目标对象功能扩展。

# 4. **C++**

## 4.1. **extern static const**

<https://www.cnblogs.com/yc_sunniwell/archive/2010/07/14/1777431.html>

1 基本解释：extern可以置于变量或者函数前，以标示变量或者函数的定义在别的文件中，提示编译器遇到此变量和函数时在其他模块中寻找其定义。此外extern也可用来进行链接指定。

也就是说extern有两个作用:

第一个,当它与"C"一起连用时，如: extern "C" void fun(int a, int b);则告诉编译器在编译fun这个函数名时按着C的规则去翻译相应的函数名而不是C++的，C++的规则在翻译这个函数名时会把fun这个名字变得面目全非，可能是fun@aBc_int_int#%$也可能是别的，这要看编译器的"脾气"了(不同的编译器采用的方法不一样)，为什么这么做呢，因为C++支持函数的重载啊，在这里不去过多的论述这个问题，如果你有兴趣可以去网上搜索，相信你可以得到满意的解释!

第二，当extern不与"C"在一起修饰变量或函数时，如在头文件中: extern int g_Int; 它的作用就是声明函数或全局变量的作用范围的关键字，其声明的函数和变量可以在本模块活其他模块中使用，记住它是一个声明不是定义!也就是说B模块(编译单元)要是引用模块(编译单元)A中定义的全局变量或函数时，它只要包含A模块的头文件即可,在编译阶段，模块B虽然找不到该函数或变量，但它不会报错，它会在连接时从模块A生成的目标代码中找到此函数。

## 4.2 为什么要使用智能指针，auto_ptr/unique_ptr/shared_ptr有什么差异

## 4.3 [C编译的四个步骤](https://blog.csdn.net/weixin_47092777/article/details/120079026)

c的编译过程主要分为四个步骤：[预处理](https://so.csdn.net/so/search?q=预处理&spm=1001.2101.3001.7020)、编译、汇编、链接

![img](https://img-blog.csdnimg.cn/20210903120859539.jpeg?x-oss-process=image/watermark,type_ZHJvaWRzYW5zZmFsbGJhY2s,shadow_50,text_Q1NETiBA5L2g6K-05bCP55m955m95LiN55m9,size_18,color_FFFFFF,t_70,g_se,x_16)

**预处理：头文件替换 删除注释 条件编译 不会检查错误**
gcc -E hello.c -o hello.i
具体做的事儿如下：

（1）将所有的#define删除，并且展开所有的宏定义。说白了就是字符替换

（2）处理所有的条件编译指令，#ifdef #ifndef #endif等，就是带#的那些

（3）条件语句中符合判断条件的会保留，不符合的会删除

（3）处理#include，将#include指向的文件插入到该行处

（4）删除所有注释

（5）添加行号和文件标示，这样的在调试和编译出错的时候才知道是是哪个文件的哪一行

（6）保留#pragma编译器指令，因为编译器需要使用它们。

**编译： 检查错误 生成汇编文件 实质是把高级语言编译成机器可识别的汇编语言**
gcc -S hello.i -o hello.s

**汇编： 将汇编文件生成二进制文件**
gcc - c hello.s -o hello.o
**链接：链接器把目标文件与所需要的附加的目标文件(如静态链接库、动态链接库)链接起来成为可执行的文件**
gcc hello.o -o hello



## 4.4 [NEON](https://www.elecfans.com/emb/dsp/202212261966065.html)

NEON 是适用于 Arm Cortex-A 系列处理器的一种128位 SIMD 扩展结构，每个处理器核心均有一个 NEON 单元，因此可以实现多线程并行的加速效果。

![image-20230716175744022](pictures/NEON基本理.png)

NEON 指令简类型

NEON 数据处理指令可以分为正常指令、长指令、宽指令、窄指令和饱和指令。
以 Intrinsic 的长指令为例 int16x8_t vaddl_s8(int8x8_t __a, int8x8_t __b);
\- 上面的函数将两个64位的 D 寄存器向量（每个向量包含8个8位数字）相加，生成一个包含8个16位数字的向量（存储在128位的Q寄存器中），从而避免相加的结果溢出。



https://blog.csdn.net/u013752202/article/details/92008192

一、什么是Neon

Neon是ARM ARMv7-A架构以上的处理器（从Cortex-A5开始）中集成的一套SIMD(Single Instruction, Multiple Data）单指令多数据指令集，相当于X86上的SSE，MIPS上的MXU,PowerPC上的AltiVec。

二、Neon有什么作用

Neon作为单指令多数据指令集使CPU可以在一个指令周期内并行处理多个数据，节省算法处理时间，常常作为图像算法处理的一种加速手段。




## 4.5 [C++多态的三种实现方式](https://blog.csdn.net/weixin_72686492/article/details/131137938)

C++中的[多态性](https://so.csdn.net/so/search?q=多态性&spm=1001.2101.3001.7020)主要分为三种实现方式：静态多态、非继承多态和常规多态。下面我们将分别介绍它们的实现方式。

* 静态多态
  静态多态指的是通过函数重载或者运算符重载实现的多态，在编译时确定函数调用或操作的具体实现。这种多态性也称为编译时多态，因为在程序编译阶段就已经决定了要调用哪个函数或使用哪个运算符。静态多态的实现方式如下：

（1）函数重载

函数重载是指在同一作用域内定义多个函数，它们的函数名相同但参数列表不同，从而实现对不同参数类型的适应和处理。例如：
（2）[运算符重载](https://so.csdn.net/so/search?q=运算符重载&spm=1001.2101.3001.7020))

* 非继承多态
  非继承多态指的是通过模板和函数对象实现的多态性，它不依赖于类的继承关系。这种多态性也称为参数化多态或泛型编程，因为它可以处理不同类型的数据，具有很强的通用性。非继承多态的实现方式如下：

（1）模板

模板是一种用于生成特定类型的代码的机制，它允许程序员编写与类型无关的代码。通过模板，可以在编译时自动生成特定类型的代码，从而实现对不同数据类型的处理。例如：
在上述代码中，我们定义了一个名为max的函数模板，可以比较任意类型的数据并返回其中最大值。当我们调用max函数时，编译器会根据传入参数的数据类型来自动生成相应的代码。这就是模板实现的非继承多态性。

（2）函数对象

函数对象是指将一个函数封装成一个对象，从而使得这个函数可以像一个普通对象一样进行传递和操作。在实现非继承多态时，我们可以使用函数对象来实现对不同类型数据的处理。例如：
在上述代码中，我们定义了一个名为Square的函数对象，并通过重载operator()实现了对任意类型数据的平方运算。当我们调用square对象时，编译器会根据传入参数的类型自动选择相应的版本进行处理。

* 常规多态
  常规多态指的是基于类的继承关系和虚函数机制实现的多态性。它使用基类指针或引用来访问派生类对象的方法，在运行时确定函数调用的具体实现。这种多态性也称为运行时多态，因为在程序运行阶段根据实际情况来确定要调用哪个函数。常规多态的实现方式如下：

（1）基类指针或引用

基类指针或引用是指将基类类型的指针或引用指向派生类对象，从而实现对派生类方法的调用。在基类中，我们将需要被派生类改写的方法声明为虚函数，并使用virtual关键字来修饰。例如：
在上述代码中，我们定义了一个名为Shape的基类和一个继承自Shape的派生类Rectangle。在Shape类中，我们声明了一个纯虚函数area，并使用virtual关键字来修饰，表示该方法是可重写的。在Rectangle类中，我们重写了area方法，并使用override关键字来使编译器检查该方法是否确实是虚函数的覆盖。

在main函数中，我们定义了一个指向Rectangle对象的Shape指针，通过该指针调用area方法时，编译器会根据实际情况选择相应的版本进行处理。这就是基类指针或引用实现常规多态的方式。

（2）虚函数表

虚函数表是编译器生成的一个用于存储虚函数地址的表格，每个类中都有自己的虚函数表。在派生类继承了基类中的虚函数时，它会将这些虚函数的指针放到自己的虚函数表中，并可能添加新的虚函数指针。

在程序运行时，当我们使用基类指针或引用来访问派生类对象的方法时，编译器会根据该指针或引用所指向的对象类型，在相应的虚函数表中查找正确的虚函数地址，然后调用对应的函数。这就是基于虚函数表实现常规多态的机制。

## 4.7 [构造函数不可以是虚函数；析构函数可以是虚函数，也可以是纯虚函数](https://blog.csdn.net/weibo1230123/article/details/81977943)

一：构造函数不能声明为虚函数的原因

1 构造一个对象的时候，必须知道对象的实际类型，而虚函数行为是在运行期间确定实际类型的。
而在构造一个对象时，由于对象还未构造成功。编译器无法知道对象 的实际类型，是该类本身，还是该类的一个派生类，或是更深层次的派生类。无法确定。

2 虚函数的执行依赖于虚函数表。而虚函数表在构造函数中进行初始化工作，即初始化vptr，让它指向正确的虚函数表。
而在构造对象期间，虚函数表还没有被初 始化，将无法进行。

二：[析构函数](https://so.csdn.net/so/search?q=析构函数&spm=1001.2101.3001.7020)可以是虚函数，也可以是纯虚函数


 在某些类里声明纯虚析构函数很方便。[纯虚函数](https://so.csdn.net/so/search?q=纯虚函数&spm=1001.2101.3001.7020)将产生抽象类——不能实例化的类（即不能创建此类型的对象）。有些时候，你想使一个类成为抽象类，但刚好又没有任何纯虚函数。怎么办？因为抽象类是准备被用做基类的，基类必须要有一个虚析构函数，纯虚函数会产生抽象类，所以方法很简单：在想要成为抽象类的类里声明一个纯虚析构函数。

```c++
　class awov {
　　public:
　　virtual ~awov() = 0; // 声明一个纯虚析构函数
　　};
```

  这个类有一个纯虚函数，所以它是抽象的，而且它有一个虚析构函数，所以不会产生析构函数问题。但这里还有一件事：必须提供纯虚析构函数的定义：

　　awov::~awov() {} // 纯虚析构函数的定义

  这个定义是必需的，因为虚析构函数工作的方式是：最底层的派生类的析构函数最先被调用，然后各个基类的析构函数被调用。这就是说，即使是抽象类，编译器也要产生对~awov的调用，所以要保证为它提供函数体。如果不这么做，链接器就会检测出来，最后还是得回去把它添上。

## 4.8 CMake的public、private、interface作用



## 4.10 [C++智能指针std::shared_ptr](https://zhuanlan.zhihu.com/p/548864356)

[C++(标准库):04---智能指针之shared_ptr](https://dongshao.blog.csdn.net/article/details/105438328?spm=1001.2101.3001.6650.6&utm_medium=distribute.pc_relevant.none-task-blog-2~default~BlogCommendFromBaidu~Rate-6-105438328-blog-131969612.235^v38^pc_relevant_anti_vip_base&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2~default~BlogCommendFromBaidu~Rate-6-105438328-blog-131969612.235^v38^pc_relevant_anti_vip_base&utm_relevant_index=9)

实现原理

此处以如下代码为例，讲解std::shared_ptr的实现原理(**仅给出便于理解的感性认知，源码层面的讲解不在本文范围之内**).

```cpp
#include <iostream>
#include <memory>
 
int main() {
  auto p = std::shared_ptr<int>(new int(4));
  auto p1 = std::make_shared<int>(4);
  std::cout << *p << *p1 << "\n";
  return 0;
}
```

上述对象p的内存布局如下所示

![img](https://pic3.zhimg.com/80/v2-ba8f731684e37aade06cb4a9eecf90f6_720w.webp)

关于上述实现原理图，需要作如下说明

- **std::shared_ptr本身只包含两个对象：指向控制块对象的Ctr指针和一个指向其管理的资源的指针Ptr**
- **当采用std::shared_ptr<T>(new T{})这种形式创建智能指针时，其控制块对象中会包含一个M_ptr成员，该指针指向智能指针所管理的资源**
- **控制块对象至少会包含use_count(引用计数), weak_count(弱引用计数), 以及其他数据这三部分内容**

再来看看，针对对象p1的内存布局



![img](https://pic3.zhimg.com/80/v2-6b0181cc3cceba61a1db94b18ad7432e_720w.webp)

关于上述实现原理图，需要作如下说明

- **当采用std::make_shared创建智能指针时，其控制块对象和被管理资源被存放在同一个内存块中**
- **当采用std::make_shared创建智能指针时，其控制块对象中，不会包含M_ptr成员(该成员指向智能指针所管理的资源)**

**NOTE:建议优先考虑std::make_shared方式创建shared_ptr对象**



### 实现shared_ptr，基本功能同标准库

[C++智能指针之shared_Ptr的原理以及简单实现](https://blog.csdn.net/Arthur__Cui/article/details/131969612)

源码参考：[C++深度探索 - shared_ptr源码分析](https://blog.csdn.net/Huuaaaaa/article/details/128660585)

![](https://img-blog.csdnimg.cn/6786a4cb79a14d6fb81f13e71307ed95.png)

weak_ptr持有的是_Sp_counted_base*。

* 当用shared_ptr去构造weak_ptr时，则会增加\_M_weak_count弱计数，当析构weak_ptr或reset时，则会调用weak_release()减少\_M_weak_count弱计数。\_M_weak_count为0，则调用destroy方法，delete掉_Sp_counted_base*。
* 当调用lock()方法时，则用\_Sp_counted_base*去构造一个shared_ptr，并增加\_M_use_count。

#### [C++11的智能指针shared_ptr、weak_ptr源码解析](https://blog.csdn.net/weixin_43798887/article/details/116464334)

1. 有一个类成员：_M_pi(计数器，类型为_Sp_counted_base)
2. 仔细一看`__shared_count`里也持有这个成员，类型一模一样，这样也就解释得通为什么`__shared_count`和`__weak_count`可以互相转换了，转换的方式很简单：

```shell
__shared_count转换为__weak_count的过程为：
拷贝_M_pi，然后调用_M_weak_add_ref方法增加一次弱引用计数
__weak_count转换为__shared_count的过程为：
拷贝_M_pi，然后调用_M_add_ref_copy方法增加一次引用计数
```

3. 构造函数、拷贝构造函数、赋值函数均不为`_M_pi`分配了内存，这点也可以看出weak_ptr确实是shared_ptr的附属品而已，自己不持有资源不控制资源
4. 析构函数中调用了`_M_pi`的`_M_weak_release`方法，释放了`_M_pi`的内存（条件满足的情况下才会释放）

从上面的代码中我们可以看到，首先`__shared_count`使用`__weak_count`的`_M_pi`来构建自己的`_M_pi`，从前面的分析我们可以知道，在所有的shared_ptr和weak_ptr消亡之前，`_M_pi`的内存是不会被释放的，所以这里就算之前的shared_ptr已经全部消亡（即资源已释放），`_M_pi`还是有效的（因为weak_ptr还没有消亡）。而通过判断`_M_add_ref_lock_nothrow`的返回值来确定是否要将`_M_pi`置为nullptr，可以看到判断的条件为`_M_use_count`是否为0（即判断资源是否被释放了）。



##### enable_shared_from_this解析

4.3.1、从一个典型的例子来认识智能指针的不足之处
有时候我们需要在一个被shared_ptr管理的对象的内部获取自己的shared_ptr，比如下面这个的例子:

```cpp
class Ptr
{
public:
	void fun()
	{
		std::shared_ptr<Ptr> p(this);
		std::cout << sp->use_count() << std::endl;
	}
};

std::shared_ptr<Ptr> p= std::make_shared<Ptr>();
p->fun(); //输出为1
```

从上面这个简单的例子可以看到，fun输出的居然是1而不是2，这是为什么？倒回去4.1.2小节可以看到，当使用普通指针（上面的那个this）去构造shared_ptr时，构造出来的shared_ptr一定是独立的，不与其他人共享的。这样就会出现一个非常严重的问题，那就是析构时会导致对象被重复释放, 从而引发错误

改进方法
现在明确一下我们的需求：在一个对象内部构造该对象的shared_ptr 时，即使该对象已经被shared_ptr管理着，也不会造成对象被两个独立的智能指针管理。这就要求我们在对象内构造对象的智能指针时，必须能识别有对象是否已经由其他智能指针管理，智能指针的数量，并且我们创建智能指针后也能让之前的智能指针感知到。当然标准已经也给出了解决了这个问题办法，那就是使用接下来所提到的

enable_shared_from_this

```cpp
#include <iostream>
#include <memory>

class Test1 {
public:
    Test1(int a, int b) : a_(a), b_(b) {}

    void show() const {
        std::cout << "Test1() a: " << a_ << ", b: " << b_ << std::endl;
    }

//    void fun() {
//        std::shared_ptr<Test> p(this);
//        std::cout << p.use_count() << std::endl;
//    }

private:
    int a_ = 1;
    int b_ = 2;
};

class Test2 : public std::enable_shared_from_this<Test2> {
public:
    Test2(int a, int b) : a_(a), b_(b) {}

    void show() const {
        std::cout << "Test2() a: " << a_ << ", b: " << b_ << std::endl;
    }

private:
    int a_ = 0;
    int b_ = 0;
};

int main(int argc, char* argv[]) {
    std::shared_ptr<Test1> test1(new Test1(1, 2));
    test1->show();
//    test1->fun();  // exit code 134
    std::shared_ptr<Test2> test2(new Test2(1, 2));
    test2->show();
    auto test22 = test2->shared_from_this();
    std::cout << test22.use_count() << std::endl;  // 2
    return 0;
}
```





### [智能指针之shared_ptr初始化，引用计数，常用操作和自定义删除器](https://blog.csdn.net/weixin_44517656/article/details/114171334)

make_shared

//1 性能更好(高效)
/*
	 ①性能更好(高效)：用new来构造shared_ptr指针，那么new的过程是一次堆上面的内存分配，
	 而在构造shared_ptr对象的时候，由于需要使用堆上面共享的引用计数(指针)，又需要在堆上面
	 分配一次内存，即需要分配两次内存，而如果用make_shared函数，则只需分配一次内存，
	 所以性能会好很多。
//2 更安全
/*
	 ②更加安全：若我们使用裸指针对shared_ptr构造时，包含两步操作：
	（1）new一个堆内存。
	（2）分配一个引用计数区域管理该内存空间(指上面的_pRefCount)。
	 但是并没有保证这两个步骤的原子性，当做了第（1）步，没有做第二步如果程序抛出了异常，将导致内存泄露。
	 而make_shared内部有对这两个步骤合成一步进行处理，
	 因此更推荐使用make_shared来分配内存。

*/

//3 make_shared缺点
/*
	虽然make_shared针对裸指针更好，但它也有缺点。
	③缺点：make_shared一次性分配堆内存的做法，在释放的时候可能会导致内存延迟释放，
	因为如果有weak_ptr持有了指针，引用计数不会释放，而引用计数和实际的对象分配在同一块堆内存，
	因此无法将该对象释放，如果两块内存分开申请，则不存在这个延迟释放的问题。

​    常用删除迭代器为何不使用make_shared，因为使用make_shared这种方法我们无法指定自己的删除器。所以只能使用new。

*/

//4 总结
/*
	实际开发可能不会考虑这些，使用裸指针或者make_shared都行，但是大家一定要知道它们可能存在的问题。
*/

## 4.11 [std::shared_ptr 的线程安全性 & 在多线程中的使用注意事项](https://cloud.tencent.com/developer/article/2240964)

在讨论之前，我们先理清楚这样的一个简单但却容易混淆的逻辑。 std::shared_ptr 是个类模版，无法孤立存在的，因此实际使用中，我们都是使用他的具体模版类。这里使用 std::shared_ptr 来举例，我们讨论的时候，其实上是在讨论 std::shared_ptr 的线程安全性，并不是 SomeType 的线程安全性。

那我们在讨论某个操作是否线程安全的时候，也需要看具体的代码是作用在 std::shared_ptr 上，还是 SomeType 上。

举个例子:

```cpp
#include <memory>

struct SomeType {
  void DoSomething() {
    some_value++;
  }

  int some_value;
};

int main() {
  std::shared_ptr<SomeType> ptr;
  ptr->DoSomething();
  return 0;
}
```

这里例子中，如果 ptr->DoSomething () 是运行在多线程中，讨论它是否线程安全，如何进行判断呢？

首先它可以展开为 `ptr.operator->()->DoSomething()`，拆分为两步：

1. ptr.operator->() 这个是作用在 ptr 上，也就是 std::shared_ptr 上，因此要看 std::shared_ptr->() 是否线程安全，这个问题后面会详细来说
2. ->DoSomething () 是作用在 SomeType* 上，因此要看 SomeType::DoSomething () 函数是否线程安全，这里显示是非线程安全的，因为对 some_value 的操作没有加锁，也没有使用 atomic 类型，多线程访问就出现未定义行为（UB）

**std::shared_ptr 线程安全性**

我们来看看 cppreference 里是怎么描述的:

>  All member functions (including copy constructor and copy assignment) can be called by multiple threads on different instances of shared_ptr without additional synchronization even if these instances are copies and share ownership of the same object. If multiple threads of execution access the same instance of shared_ptr without synchronization and any of those accesses uses a non-const member function of shared_ptr then a data race will occur; the shared_ptr overloads of atomic functions can be used to prevent the data race. 

我们可以得到下面的结论：

1. 多线程环境中，对于持有相同裸指针的不同std::shared_ptr 实例，所有成员函数的调用都是线程安全的。
   - 当然，对于不同的裸指针的 std::shared_ptr 实例，更是线程安全的
   - 这里的 “成员函数” 指的是 std::shared_ptr 的成员函数，比如 get ()、reset ()、 operrator->() 等）
2. 多线程环境中，对于同一个 std::shared_ptr 实例，只有访问 const 的成员函数，才是线程安全的，对于非 const 成员函数，是非线程安全的，需要加锁访问。

首先来看一下 std::shared_ptr 的所有成员函数，只有前 3 个是 non-const 的，剩余的全是 const 的：

| 成员函数              | 是否 const |
| :-------------------- | :--------- |
| operator=             | non-const  |
| reset                 | non-const  |
| swap                  | non-const  |
| get                   | const      |
| operator*、operator-> | const      |
| operator              | const      |
| use_count             | const      |
| unique(until C++20)   | const      |
| operator bool         | const      |
| owner_before          | const      |
| use_count             | const      |

我们来看两个例子 例 1:

```cpp
#include <iostream>
#include <memory>
#include <thread>
#include <vector>
#include <atomic>
using namespace std;

struct SomeType {
  void DoSomething() {
    some_value++;
  }

  int some_value;
};

int main(int argc, char *argv[]) {
  auto test = std::make_shared<SomeType>();
  std::vector<std::thread> operations;
  for (int i = 0; i < 10000; i++) {
    std::thread([=]() mutable {  //<<--
      auto n = std::make_shared<SomeType>();
      test.swap(n);
    }).detach();
  }

  using namespace std::literals::chrono_literals;
  std::this_thread::sleep_for(5s);
  return 0;
}

// 
int main(int argc, char *argv[]) {
    auto test = std::make_shared<SomeType>();
    std::vector<std::thread> operations;
    for (int i = 0; i < 100; i++) {
        std::thread([=]() mutable {  //<<--
            auto n = std::make_shared<SomeType>();
            for (int j = 0; j < 2000; ++j) {
                test->DoSomething();
            }
            test.swap(n);
        }).detach();
    }

    using namespace std::literals::chrono_literals;
    std::this_thread::sleep_for(10s);
    std::cout << test->some_value << std::endl;  // 打印了200000，按理说应该小于该值，初步觉得每个线程运行很快完成，还未来得及数据竞争就结束了
    return 0;
}

// 再改成如下：
int main(int argc, char *argv[]) {
    auto test = std::make_shared<SomeType>();
    std::vector<std::thread> operations;
    for (int i = 0; i < 2; i++) {
        std::thread([=]() mutable {  // <<---
            for (int j = 0; j < 100000; ++j) {
                test->DoSomething();
            }
        }).detach();
    }

    using namespace std::literals::chrono_literals;
    for (int i = 0; i < 10; ++i) {
        std::this_thread::sleep_for(1s);
        std::cout << i << "s" << std::endl;
    }
    std::cout << test->some_value << std::endl;  // 167520
    return 0;
}
```

例 2:

```cpp
#include <iostream>
#include <memory>
#include <thread>
#include <vector>
#include <atomic>
using namespace std;

struct SomeType {
  void DoSomething() {
    some_value++;
  }

  int some_value;
};

int main(int argc, char *argv[]) {
  auto test = std::make_shared<SomeType>();
  std::vector<std::thread> operations;
  for (int i = 0; i < 10000; i++) {
    std::thread([&]() mutable {  // <<---
      auto n = std::make_shared<SomeType>();
      test.swap(n);
    }).detach();
  }

  using namespace std::literals::chrono_literals;
  std::this_thread::sleep_for(5s);
  return 0;
}
```

这两个的区别只有传入到 std::thread 的 lambda 的捕获类型，一个是 capture by copy, 后者是 capture by reference，哪个会有线程安全问题呢？

根据刚才的两个结论，显然例 1 是没有问题的，因为每个 thread 对象都有一份 test 的 copy，因此访问任意成员函数都是线程安全的。 例 2 是有数据竞争存在的，因为所有 thread 都共享了同一个 test 的引用，根据刚才的结论 2，对于同一个 std::shared_ptr 对象，多线程访问 non-const 的函数是非线程安全的。 这个的 swap 改为 reset 也一样是非线程安全的，但如果改为 get () 就是线程安全的。

这里我们打开 Thread Sanitizer 编译例 2（clang 下是 -fsanitize=thread 参数），运行就会 crash 并告诉我们出现数据竞争的地方。

```cpp
==================
WARNING: ThreadSanitizer: data race (pid=11868)
  Read of size 8 at 0x00016ba5f110 by thread T2:
    #0 std::__1::enable_if<(is_move_constructible<SomeType*>::value) && (is_move_assignable<SomeType*>::value), void>::type std::__1::swap<SomeType*>(SomeType*&, SomeType*&) swap.h:38 (Untitled 4:arm64+0x1000061a8)
    #1 std::__1::shared_ptr<SomeType>::swap(std::__1::shared_ptr<SomeType>&) shared_ptr.h:1045 (Untitled 4:arm64+0x100006140)
    #2 main::$_0::operator()() Untitled 4.cpp:22 (Untitled 4:arm64+0x1000060d4)
    #3 decltype(static_cast<main::$_0>(fp)()) std::__1::__invoke<main::$_0>(main::$_0&&) type_traits:3918 (Untitled 4:arm64+0x100005fc8)
    #4 void std::__1::__thread_execute<std::__1::unique_ptr<std::__1::__thread_struct, std::__1::default_delete<std::__1::__thread_struct> >, main::$_0>(std::__1::tuple<std::__1::unique_ptr<std::__1::__thread_struct, std::__1::default_delete<std::__1::__thread_struct> >, main::$_0>&, std::__1::__tuple_indices<>) thread:287 (Untitled 4:arm64+0x100005ec4)
    #5 void* std::__1::__thread_proxy<std::__1::tuple<std::__1::unique_ptr<std::__1::__thread_struct, std::__1::default_delete<std::__1::__thread_struct> >, main::$_0> >(void*) thread:298 (Untitled 4:arm64+0x100004f90)

  Previous write of size 8 at 0x00016ba5f110 by thread T1:
    #0 std::__1::enable_if<(is_move_constructible<SomeType*>::value) && (is_move_assignable<SomeType*>::value), void>::type std::__1::swap<SomeType*>(SomeType*&, SomeType*&) swap.h:39 (Untitled 4:arm64+0x1000061f0)
    #1 std::__1::shared_ptr<SomeType>::swap(std::__1::shared_ptr<SomeType>&) shared_ptr.h:1045 (Untitled 4:arm64+0x100006140)
    #2 main::$_0::operator()() Untitled 4.cpp:22 (Untitled 4:arm64+0x1000060d4)
    #3 decltype(static_cast<main::$_0>(fp)()) std::__1::__invoke<main::$_0>(main::$_0&&) type_traits:3918 (Untitled 4:arm64+0x100005fc8)
    #4 void std::__1::__thread_execute<std::__1::unique_ptr<std::__1::__thread_struct, std::__1::default_delete<std::__1::__thread_struct> >, main::$_0>(std::__1::tuple<std::__1::unique_ptr<std::__1::__thread_struct, std::__1::default_delete<std::__1::__thread_struct> >, main::$_0>&, std::__1::__tuple_indices<>) thread:287 (Untitled 4:arm64+0x100005ec4)
    #5 void* std::__1::__thread_proxy<std::__1::tuple<std::__1::unique_ptr<std::__1::__thread_struct, std::__1::default_delete<std::__1::__thread_struct> >, main::$_0> >(void*) thread:298 (Untitled 4:arm64+0x100004f90)
...

SUMMARY: ThreadSanitizer: data race swap.h:38 in std::__1::enable_if<(is_move_constructible<SomeType*>::value) && (is_move_assignable<SomeType*>::value), void>::type std::__1::swap<SomeType*>(SomeType*&, SomeType*&)

...

ThreadSanitizer: reported 4 warnings
Terminated due to signal: ABORT TRAP (6)
```

从错误信息中可以清晰地看到出现的数据竞争，在 22 行，也就是调用 swap () 的行。 如果确实需要在多线程环境下对同一 std::shared_ptr 实例做 swap () 操作，可以调用 atomic 对 std::shared_ptr 的重载函数，如：

```cpp
template< class T >
std::shared_ptr<T> atomic_exchange( std::shared_ptr<T>* p,
                                    std::shared_ptr<T> r);
```

### [关于`shared_ptr`的线程安全问题](https://www.jianshu.com/p/d625150cdffc)

首先什么是线程安全？
 简单来说就是**多个线程操作一个共享数据，都能按照预期的行为进行，无论多个线程的运行次序如何交织。**

对于`shared_ptr`，其内部有两个变量，引用计数和真正的对象类型指针。其中引用计数是原子操作，所以**对于`shared_ptr`的读操作是线程安全的。**

但是对于`shared_ptr`中赋值如`ptr1 = ptr2`，需要两个步骤，**1、`ptr1`的内部对象指针`Obj1`替换成`ptr2`内部对象`Obj2`指针；2、`ptr1`的对于`Obj1`的引用计数缓存`Obj2`的引用计数。**

这两步并不是原子的，如果一个线程需要对`shared_ptr`进行赋值操作`ptr1 = ptr2`，刚完成第一步，就切换到其他线程又对ptr2进行操作，如`ptr2 = ptr3`，就有可能造成析构了引用计数。而继续之前线程的第二步，就会出错。

**总之：对于`shared_ptr`的读操作是线程安全的。
 对于`shared_ptr`读写操作不是线程安全的，需要加锁。**



## 4.12 [C++ 对象指针比较](https://blog.csdn.net/noahzuo/article/details/51135968)

在C++中，一个对象可以有多个有效的地址，所以，对于这个时候的比较不是地址问题，而是对象同一性的问题。

```cpp
#include < iostream >
using namespace std;

class Shape{
    public:
    Shape():a (1){}

    private:
    int a;
};

class Subject{
    public:
    Subject():b (2){}


    private:
    int b;
};

class ObservedBlob: public Shape , public Subject{
    private:
    int c;
    public:
    ObservedBlob ():c( 3){}
};

int main()
{
    ObservedBlob * ob = new ObservedBlob;
    Shape * s = ob;
    Subject * subj = ob;

    cout << ob << endl << s << endl << subj << endl;
    cout << (subj == ob) << endl;
    return 0;
}
```

那么问题来了，可以看到subj和ob的地址并不相同，但是对指针进行比较的时候却输出了true，这个是为何？

先看这种继承的2种内存分布图：

![内存分布](https://img-blog.csdn.net/20160412182353765)

* 在前面那种布局里面，s和subj分别指向ob中的对应子对象，它们与ob拥有完全不同的地址。

* 在后面的布局里面，s对象与subj对象的地址相同，可以看到上图中就是采用这样的布局。

但是无论是哪种布局，ob、s和subj都指向同样的一个ObservedBlob对象，因此编译器必须要确保ob与s和subj的比较结果都是true。所以编译器通过将参与比较的指针值之一调整一定的偏移量来完成这种比较，比如：
`ob == subj`

可能会被编译器翻译成：

`ob ? (ob + delta == subj) : (subj == 0)`

delta是Subject子对象在ObservedBlob对象的偏移量，换句话说，如果subj和ob都是空的话，那么它们相等，否则ob被调整为指向Subject基类子对象后再和subj进行比较。

所以最终可以得到一个很重要的经验，一般而言，当我们处理指向对象的指针或者引用的时候，必须小心避免丢失类型信息。

如果把上面的指针编程void *v = subj然后在判断ob和v的相等情况，就会得到不相等结论。

如果通过复制到void*指针后，就丢失了类型信息了，编译器这是就只能进行原始地址比较，这样的比较对于指向类的指针来说，很少会是正确的。

----------

本地验证：

```cpp
#include <iostream>

class Base1 {
public:
    virtual void func1() {}
};

class Base2 {
public:
    virtual void func2() {}
};

class Derived : public Base1, public Base2 {
public:
    virtual void func1() {}

    virtual void func2() {}
};

int main() {
    // https://blog.csdn.net/fishhg/article/details/6410390

    Derived derived, derived2;
    Base1* pBase1 = &derived;
    Base2* pBase2 = &derived;
    Derived* pDerived = &derived;
    Derived* pDerived2 = &derived2;

    std::cout << "pBase1: " << pBase1 << "\n"
              << "pBase2: " << pBase2 << "\n"
              << "pDerived: " << pDerived << "\n"
              << "pDerived2: " << pDerived2 << std::endl;
    std::cout << "pBase1 == pDerived: " << std::boolalpha << (pBase1 == pDerived) << std::endl;
    std::cout << "pBase2 == pDerived: " << std::boolalpha << (pBase2 == pDerived) << std::endl;
    std::cout << "(uint64_t) pBase1 == (uint64_t) pBase2: " << std::boolalpha
              << ((uint64_t) pBase1 == (uint64_t) pBase2) << std::endl;
//    std::cout << "pBase1 == pBase2: " << std::boolalpha << (pBase1 == pBase2) << std::endl;  // error: comparison between distinct pointer types 'Base1*' and 'Base2*' lacks a cast
//    std::cout << "(uint64_t)(&derived) == pDerived: " << std::boolalpha << ((uint64_t)(&derived) == pDerived) << std::endl;  // error: ISO C++ forbids comparison between pointer and integer [-fpermissive]
//    std::cout << "(int)(&derived) == pDerived: " << std::boolalpha << ((int)(&derived) == pDerived) << std::endl;  // error: cast from 'Derived*' to 'int' loses precision [-fpermissive]
//    std::cout << "(ulong)(&derived) == pDerived: " << std::boolalpha << ((ulong)(&derived) == pDerived) << std::endl;  // error: ISO C++ forbids comparison between pointer and integer [-fpermissive]
    std::cout << "pDerived > pDerived2: " << std::boolalpha << (pDerived > pDerived2)
              << std::endl;  // On stack, address is allocated from high to low


    int i1 = 1, i2 = 1;
    int* p1 = &i1;
    int* p2 = &i1;
    std::cout << "p1 == p2: " << std::boolalpha << (p1 == p2) << std::endl;  // true，指针值比较，均为i1地址，相同
    int* p3 = &i2;
    std::cout << "p1 == p3: " << std::boolalpha << (p1 == p3) << std::endl;  // false，指针值比较，i1和i2地址不同，不相同

    // 如果单纯的是两个不相关的指针进行比较，一般编译不通过。 https://blog.csdn.net/yangxingpa/article/details/78779787
    int name[8] = {1, 2, 3, 4, 5, 6, 7, 8};
    int* pName1 = name, * pName1_1 = pName1 + 8 - 1;
    std::cout << "pName1 < pName1_1: " << std::boolalpha << (pName1 < pName1_1) << std::endl;  // pName1、pName1_1比较的就是地址。对于这两个指针，指向这个数组前面元素的指针小于指向后面元素的指针。不是根据指向的值。
    int name2[8] = {1, 2, 3, 4, 5, 6, 7, 8};
    int* pName2 = name2;
    std::cout << "pName1 > pName2: " << std::boolalpha << (pName1 > pName2) << std::endl;  // 栈地址从大到小分配
    return 0;
}
```

两个基类对象，是有不同的指针地址，分别在与派生类对象比较时，是同一个对象比较，所以是相等的。

而两个基类对象，进行比较时，并没有继承关系，所以是不同类型的指针，所以无法进行比较，在转成数字后，是进行数字比较，由于地址不同，所以转成数字不同。

![image-20231012220945952](pictures/指针比较.png)



## 4.13 [map和unordered_map](https://blog.csdn.net/qq_45890970/article/details/123955261)

相同
map和unordered_map这两种字典结构都是通过键值对（key-value）存储数据的，键（key）和值（value）的数据类型可以不同。且它们的key是唯一的。

**不同**

头文件

```cpp
#include<map>
#include<unordered_map>
```


**实现的数据结构**
map是基于红黑树结构实现的，因此在map中的元素排列都是有序的。对map的增删改查，时间复杂度都为O(logn)，n即为红黑树的高度。
unordered_map是基于哈希表（也叫散列表）实现的。散列表使得unordered_map的插入和查询速度接近于O(1)（在没有冲突的情况下），但是其内部元素的排列顺序是无序的。
**存储空间**
unordered_map的散列空间会存在部分未被使用的位置，所以其内存效率不是100%的。而map的红黑树的内存效率接近于100%。

**查找性能的稳定性**
map的查找类似于平衡二叉树的查找，其性能十分稳定。例如在1M数据中查找一个元素，需要多少次比较呢？20次。map的查找次数几乎与存储数据的分布与大小无关。而unordered_map依赖于散列表，如果哈希函数映射的关键码出现的冲突过多，则最坏时间复杂度可以达到是O(n)。因此unordered_map的查找次数是与存储数据的分布与大小有密切关系的，它的效率是不稳定的。

**使用场景**
map元素有序，性能较为稳定。适用于元素要求有序、或者对单次查询时间较为敏感，必须保持查询性能的稳定性，比如实时应用。

unordered_map查询速度快O(1)，但是元素无序、查询性能不稳定（最坏为O(n)）。适用于要求查询速率快，对单次查询性能要求不敏感。

**总结**：在需要元素有序性或者对单次查询性能要求较为敏感时使用map，其余情况下应使用unordered_map。进行算法编程的大部分情况下，都需要使用unordered_map而不是map。


## 4.14 gdb堆栈被破坏定位

[gdb堆栈被破坏时的定位方法_gdb定位踩坏内存_](https://blog.csdn.net/weixin_37357702/article/details/121804164)

[gdb分析堆栈破坏实例_gdb bt报错信息分析](https://blog.csdn.net/mergerly/article/details/80523750?spm=1001.2101.3001.6650.1&utm_medium=distribute.pc_relevant.none-task-blog-2~default~CTRLIST~Rate-1-80523750-blog-128719639.235^v38^pc_relevant_anti_vip_base&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2~default~CTRLIST~Rate-1-80523750-blog-128719639.235^v38^pc_relevant_anti_vip_base&utm_relevant_index=2)

[Linux：GDB 调试一些函数栈被毁坏的问题](https://blog.csdn.net/hhd1988/article/details/128719639)

此类问题，可以从gdb的栈保护设置开始着手。

1. 编译的时候添加编译选项：-fstack-protector 和 -fstack-protector-all 这两个选项指示编译器开启栈保护，这样在栈乱序的第一时间可以dump出来现场。可加在Makefile里面。

堆栈可以看出函数名了cmdMsgParse，查看源码文件ActivityServer.cpp:930是函数返回地址，断定是某一个消息引起的堆栈破坏，这个是统一消息处理函数，消息量很大，

2. 结合日志：

增加消息号处理日志，再放一个版本
多台服务器的日志最后一行都是：



## 4.15 [原子操作](https://blog.csdn.net/u010164190/article/details/125157771)

1.原子操作：std::atomic
【1】原子操作理解为：不需要用到互斥量加锁（无锁）技术的多线程并发编程方式，并且原子操作在多线程中不会被打断执行；        

【2】原子操作比互斥量在效率上更高；

【3】互斥量的加锁一般是针对一个代码段（几行代码), 原子操作针对的都是一个变量，而不是针对一个代码段；	

【4】原子操作：该变量要么完成，要么没完成，不存在中间状态

std::atomic代表原子操作。std::atomic是一个类模板，用于封装某个类型的值。

2.原子操作步骤（分三步：Read ---> Modified ----> Write）
atomic是原子的意思，意味"不可分割"的整体。在Linux kernel中有一类atomic操作API。这些操作对用户而言是原子执行的，在一个CPU上执行过程中，不会被其他CPU打断。最常见的操作是原子读改写，简称RMW。

一个变量自增操作，CPU微观指令级别分成3步操作。
1) 先read变量的值到CPU内存寄存器； //读：Read
2) 对寄存器的值递增；//改：Modified
3) 将寄存器的值写回变量。//写： Write

**原子操作：**
**可以把原子操作理解为：不需要用到互斥量加锁（无锁）技术的多线程编程方式，多线程中不会被打断的程序执行片段。**

```cpp
#include <iostream>
#include <thread>
#include <atomic>
//int g_count = 0;
std::atomic_int g_count = 0;

void work() {
    for (int i = 0; i < 100000; ++i) {
        ++g_count;
    }
}

int main() {
    std::thread th1(work);
    std::thread th2(work);

    th1.join();
    th2.join();
    std::cout << "g_count: " << g_count << std::endl;  // 如果使用int g_count，则输出值随机数，小于200000
    return 0;
}
```

总结：

1.先理解下CPU和内存连接方式

在SMP系统中，多个CPU通过共享的总线和内存相连接，如果它们同时申请访问内存，那么总线就会从硬件上进行仲裁，以确定接下来哪一个CPU可以使用总线，然后将总线授权给它，并且允许该CPU完成一次原子的load操作或者store操作。在完成每一次操作之后，就会重复这个周期：对总线进行仲裁，并授权给另一个CPU。每次只能有一个CPU使用总线。

![CPU与存储器](https://img-blog.csdnimg.cn/img_convert/5ac5f44bec044dba8c8dd5ed35d80e67.png)

SMP：对称多处理（Symmetric multiprocessing）简称SMP，是一种多处理器的电脑硬件架构，指在一个计算机上汇集了一组处理器(多[CPU](https://baike.baidu.com/item/CPU/120556?fromModule=lemma_inlink))，各CPU之间[共享内存](https://baike.baidu.com/item/共享内存/2182364?fromModule=lemma_inlink)[子系统](https://baike.baidu.com/item/子系统/4670893?fromModule=lemma_inlink)以及[总线结构](https://baike.baidu.com/item/总线结构/10183496?fromModule=lemma_inlink)。它是相对[非对称多处理](https://baike.baidu.com/item/非对称多处理/15668918?fromModule=lemma_inlink)技术而言的、应用十分广泛的[并行技术](https://baike.baidu.com/item/并行技术/3345169?fromModule=lemma_inlink)。

2.理解原子操作

原子操作常用的做法是给总线上锁(bus lock)，以获得在一定的时间窗口内对总线独占的授权，就好像是一个CPU在告诉总线说“在我完成之前，别让其他CPU来读写内存的数据”。


### [C++11：原子交换函数compare_exchange_weak和compare_exchange_strong](https://blog.csdn.net/feikudai8460/article/details/107035480/)

我们知道在C++11中引入了mutex和方便优雅的lock_guard。但是有时候我们想要的是性能更高的无锁实现，下面我们来讨论C++11中新增的原子操作类Atomic，我们可以利用它巧妙地实现无锁同步。

CAS(Compare and Swap)是个原子操作，保证了如果需要更新的地址没有被他人改动多，那么它可以安全的写入。而这也是我们对于某个数据或者数据结构加锁要保护的内容，保证读写的一致性，不出现dirty data。现在几乎所有的CPU指令都支持CAS的原子操作。

 

Atomic
C++11给我们带来的Atomic一系列原子操作类，它们提供的方法能保证具有原子性。这些方法是不可再分的，获取这些变量的值时，永远获得修改前的值或修改后的值，不会获得修改过程中的中间数值。

 这些类都禁用了拷贝构造函数，原因是原子读和原子写是2个独立原子操作，无法保证2个独立的操作加在一起仍然保证原子性。

`atomic<T>`提供了常见且容易理解的方法：

store
load
exchange
compare_exchange_weak
compare_exchange_strong

store是原子写操作，而load则是对应的原子读操作。

exchange允许2个数值进行交换，并保证整个过程是原子的。

而compare_exchange_weak和compare_exchange_strong则是著名的CAS（compare and set）。参数会要求在这里传入期待的数值和新的数值。它们对比变量的值和期待的值是否一致，如果是，则替换为用户指定的一个新的数值。如果不是，则将变量的值和期待的值交换。

compare_exchange_strong：atomic库中的一个函数，入参是3个，expect，desire，memoryorder，意思是如果当前的变
量this的值==expect值，则将this值改为desire，并返回true，否则，返回false，不进行修改，即进行一个读的操作。通常用于例如线程B等待线程A执行完毕，或者执行到某个步骤。此时线程B可以进行while等待，线程A在执行到对应步骤，将对应的原子变量置为expect值即可。类似于“接力运动”。这里由于会进行读写操作，所以，memory order一般是acq rel，而A线程由于要保证都执行完毕，执行顺序没有关系，所以一般是Release的memory order。

**当前值与期望值(expect)相等时，修改当前值为设定值(desired)，返回true
 当前值与期望值(expect)不等时，将期望值(expect)修改为当前值，返回false**

**weak版和strong版的区别：**
weak版本的CAS允许偶然出乎意料的返回（比如在字段值和期待值一样的时候却返回了false），不过在一些循环算法中，这是可以接受的。通常它比起strong有更高的性能。

实例：
       在非并发条件下，要实现一个栈的Push操作，我们可能有如下操作：

新建一个节点
将该节点的next指针指向现有栈顶
更新栈顶    
       但是在并发条件下，上述无保护的操作明显可能出现问题。下面举一个例子：

1. 原栈顶为A。（此时栈状态: A->P->Q->...，我们约定从左到右第一个值为栈顶，P->Q代表p.next = Q）

2. 线程1准备将B压栈。线程1执行完步骤2后被强占。（新建节点B，并使　B.next = A，即B->A）

3. 线程2得到cpu时间片并完成将C压栈的操作，即完成步骤1、2、3。此时栈状态（此时栈状态: C->A->...）

4. 这时线程1重新获得cpu时间片，执行步骤3。导致栈状态变为（此时栈状态: B->A->...）

   结果线程2的操作丢失，这显然不是我们想要的结果。

那么我们如何解决这个问题呢？

只要保证步骤3更新栈顶时候，栈顶是我们在步骤2中获得顶栈顶即可。因为如果有其它线程进行操作，栈顶必然改变。

我们可以利用CAS轻松解决这个问题：如果栈顶是我们步骤2中获取顶栈顶，则执行步骤3。否则，自旋（即重新执行步骤2）。
```cpp
#include <iostream>
#include <thread>
#include <atomic>

template<typename T>
class LockFreeStack {
private:
    struct node {
        T data;
        node* next;

        node(T const& data_) :
            data(data_) {}
    };

    std::atomic<node*> head = nullptr;
public:
    void push(T const& data) {
        static std::atomic_int count = 0;
        ++count;
        node* const new_node = new node(data);
        new_node->next = head.load();  //如果不使用compare_exchange_weak，如果head更新了，这条语句要重来一遍
        if (count == 3) {  // 模拟其它线程进行了push
            node* const new_node_other_thread = new node(100);
            new_node_other_thread->next = head.load();
            head = new_node_other_thread;
        }
        while (!head.compare_exchange_weak(new_node->next, new_node)) {
            std::cout << "while in" << std::endl;
        }
    }

    void print() {
        auto cur = head.load();
        while (cur != nullptr) {
            std::cout << cur->data << "->";
            cur = cur->next;
        }
    }
};

int main() {
    LockFreeStack<int> lockFreeStack;
    lockFreeStack.push(1);
    lockFreeStack.push(2);
    lockFreeStack.push(3);
    lockFreeStack.print();
    return 0;
}
// 输出：
while in
3->100->2->1->
```

数据流程如下：

![原子变量无锁实现stack.png](pictures/原子变量无锁实现stack.png)

### [简单理解compare_exchange_weak函数](https://blog.csdn.net/u011573853/article/details/126284456)

C++11中提供了一系列的原子操作，atomic提供了常见的原子操作方法：

* store 原子写操作，可以类比是赋值操作
```cpp
std::atomic<int> tem;
tem.store(10);
```

* load 原子读操作，可以类比读取
```cpp
std::cout<<"tem"<<tem.load()<<std::endl;
```
* exchange 原子改变操作 a. exchange(b) 会把b的值赋值给a。
```cpp
std::atomic<int> tem;
tem.store(10);
std::atomic<int> tem1;
tem1.store(30);
tem.exchange(tem1);
std::cout<<"tem:"<<tem<<" tem1:"<<tem1<<std::endl; //tem:30 tem1:30
```

* compare_exchange_weak/compare_exchange_strong (是著名的CAS（compare and set）)。
  参数传入期待值与新值，通过比较当前值与期待值的情况进行区别改变。
  a.compare_exchange_weak(b,c)其中a是当前值，b期望值，c新值
  **a==b时：函数返回真，并把c赋值给a**
  **a!=b时：函数返回假，并把a复制给b**

```cpp
int main()   //相等案例
{
    std::atomic<int> a;
    a.store(10);
    int b=10;  //a==b
    int c=20;
    std::cout<<"a:"<<a<<std::endl;
    if(a.compare_exchange_weak(b,c))
    {
        std::cout<<"a true:"<<a.load()<<std::endl;
    } 
    std::cout<<"a:"<<a<<" b:"<<b<<" c:"<<c<<std::endl;
    return 0;
}

a:10
a true:20
a:20 b:10 c:20
```

```cpp
int main()  //不等案例
{
    std::atomic<int> a;
    a.store(10);
    int b=100;  //a!=b
    int c=20;
    std::cout<<"a:"<<a<<std::endl;
    if(a.compare_exchange_weak(b,c))
    {
        std::cout<<"a true:"<<a.load()<<std::endl;
    } 
    std::cout<<"a:"<<a<<" b:"<<b<<" c:"<<c<<std::endl;
	return 0;
}
a:10
a:10 b:10 c:20
```
compare_exchange_weak比compare_exchange_strong的性能更高一些，常在循环中使用。

如下代码样例：

```cpp
std::atomic<int> g_atomic_ptr = 0;

void thread_func() {
    auto local = 42;
    while (!g_atomic_ptr.compare_exchange_weak(local, local)) {  // local每次赋值为0，循环中被赋值给42，将导致死循环
        local = 42;
    }
}

int main() {
    auto s1 = std::make_shared<int>(100);
    auto s2 = std::make_shared<int>(100);
    std::cout << std::boolalpha << (s1 == s2) << std::endl;  // shared_ptr对象不同，比较为false
    thread_func();
    return 0;
}
```

## 4.16 空类的大小

```cpp
#include <iostream>

struct A {
    char c;
};

struct B {
};

class C {
public:
};

class D {
public:
    virtual void f() {}
};

class E : public D {
public:
    virtual void f() {}
};

class F : virtual public C {
};

class G : public B, public C {
};

int main() {
    printf("sizeof A: %ld\n", sizeof(struct A));  // 1
    printf("sizeof B: %ld\n", sizeof(struct B));  // 1
    printf("sizeof C: %ld\n", sizeof(struct C));  // 1
    printf("sizeof D: %ld\n", sizeof(struct D));  // 8 具有虚函数表的地址
    printf("sizeof E: %ld\n", sizeof(struct E));  // 8 具有虚函数表的地址
    printf("sizeof F: %ld\n", sizeof(struct F));  // 8 含有指向虚基类的指针
    printf("sizeof G: %ld\n", sizeof(struct G));  // 1
    return 0;
}
```

[为什么C++空类的大小不为0](https://blog.csdn.net/aa314924994/article/details/80846742)

在C++中，空类同样是可以进行实例化的，这是一个前提。既然可以进行实例化，每个实例在内存中就的有一个独一无二的地址。为了达到这个，编译器往往会隐含的给空类一个地址。既然有地址，就得有内存分配。根据编译器的不同，可能分配的大小不一样。但是至少得分配一个大小为1的地址。

[空类的大小](https://blog.csdn.net/yujiaming493323/article/details/8768237)

1、为何空类的大小不是0呢？

为了确保两个不同对象的地址不同，必须如此。

类的实例化是在内存中分配一块地址，每个实例在内存中都有独一无二的二地址。同样，空类也会实例化，所以编译器会给空类隐含的添加一个字节，这样空类实例化后就有独一无二的地址了。所以，空类的sizeof为1，而不是0.

2、请看下面的类：

class A{ virtual void f(){} };

class B:public A{}

此时，类A和类B都不是空类，其sizeof都是4，因为它们都具有虚函数表的地址。

3、请看：

class A{};

class B:public virtual A{};

此时，A是空类，其大小为1；B不是空类，其大小为4.因为含有指向虚基类的指针。

4、多重继承的空类的大小也是1.

class Father1{}; class Father2{};

class Child:Father1, Father2{};

它们的sizeof都是1.

5、何时共享虚函数地址表：

如果派生类继承的第一个是基类，且该基类定义了虚函数地址表，则派生类就共享该表首址占用的存储单元。对于除前述情形以外的其他任何情形，派生类在处理完所有基类或虚基类后，根据派生类是否建立了虚函数地址表，确定是否为该表首址分配存储单元。





# 5. **数据结构**

## 5.1. **链表结构**

单向链表，双向链表

## 5.2. **栈和队列互相转换**

<https://www.cnblogs.com/csbdong/p/5677293.html>

* 栈1和栈2实现队列

        分析到现在，下面给出总结：如果栈2不为空，同时又需要出队，那么顺其自然直接弹出即可。如果栈2为空，那么从栈1中逐个弹出压入，那么完整的实现了先进先出的功能。

![Stack2Queue](pictures/Stack2Queue.jpg)

* 队列1和队列2实现栈

        起初的时候，两个队列都是空的，那么当“栈”要压入一个元素，我们就默认将该元素压入到队列1中。接下来继续压入剩余的元素。
        
        接下来考虑，如果我们想要弹出一个元素如何操作。栈中要求出栈的为栈顶元素，那么即为最后插入的元素，但是该元素在队列的尾部，必须要求前面的元素出队后才能访问，说到这里，你也就发现思路的：出队前面的元素，到另一个队列中，那么就可以在原队列中弹出唯一的元素了。
        
        现在我们再考虑另一个情况，队列里面还有元素，“栈”又进来了新的元素，那么就将新元素，压入到存在元素的那一个队列中，剩余的操作，上面已经提到了，一样的操作，看图也许就清晰多了。

![Queue2Stack](pictures/Queue2Stack.jpg)

## 5.3. **树的广度深度遍历**

<https://www.cnblogs.com/xiaolovewei/p/7763867.html>

1.广度优先遍历

 英文缩写为BFS即Breadth FirstSearch。其过程检验来说是对每一层节点依次访问，访问完一层进入下一层，而且每个节点只能访问一次。对于上面的例子来说，广度优先遍历的 结果是：A,B,C,D,E,F,G,H,I(假设每层节点从左到右访问)。

先往队列中插入左节点，再插右节点，这样出队就是先左节点后右节点了。

广度优先遍历树，需要用到队列（Queue）来存储节点对象,队列的特点就是先进先出。例如，上面这颗树的访问如下：

　　首先将A节点插入队列中，队列中有元素（A）;

　　将A节点弹出，同时将A节点的左、右节点依次插入队列，B在队首，C在队尾，（B，C），此时得到A节点；

　　继续弹出队首元素，即弹出B，并将B的左、右节点插入队列，C在队首，E在队尾（C,D，E），此时得到B节点；

　　继续弹出，即弹出C，并将C节点的左、中、右节点依次插入队列，（D,E,F,G,H），此时得到C节点；

　　将D弹出，此时D没有子节点，队列中元素为（E,F,G,H），得到D节点；

　　。。。以此类推。。

2、深度优先

英文缩写为DFS即Depth First Search.其过程简要来说是对每一个可能的分支路径深入到不能再深入为止，而且每个节点只能访问一次。对于上面的例子来说深度优先遍历的结果就是：A,B,D,E,I,C,F,G,H.(假设先走子节点的的左侧)。

深度优先遍历各个节点，需要使用到栈（Stack）这种数据结构。stack的特点是是先进后出。整个遍历过程如下：

先往栈中压入右节点，再压左节点，这样出栈就是先左节点后右节点了。
首先将A节点压入栈中，stack（A）;

将A节点弹出，同时将A的子节点C，B压入栈中，此时B在栈的顶部，stack(B,C)；

将B节点弹出，同时将B的子节点E，D压入栈中，此时D在栈的顶部，stack（D,E,C）；

将D节点弹出，没有子节点压入,此时E在栈的顶部，stack（E，C）；

将E节点弹出，同时将E的子节点I压入，stack（I,C）；

...依次往下，最终遍历完成。

## 5.4. **100G文件，每行都是一个URL，统计出现次数前10的URL，内存只有2G**

<https://blog.csdn.net/woliuyunyicai/article/details/48489525>

主要思想：大文件切割，对100G文件做切割，成100个文件，每个1G。
对URL做Hash运算，% Mod，100，平均情况下，会将文件分布在100个文件中，同时相同的URL还分布在同一个文件中，再对每个文件分别做统计。



# 6. Python

## 6.1 [Python多线程是否有用](https://blog.csdn.net/xiaoxianerqq/article/details/81325106?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param)

在大多数应用程序线程在I / O上等待的情况下，其他线程可以获取CPU，从而提高性能。如果python没有多线程，那么其他一些线程无法获得cpu因此浪费时间。现在至少当一个线程正在等待I/O时（大部分应用程序都是这种情况），其余的线程都可以工作，那么多线程还是有它存在的必要，所以这就是我们没有看到GIL所拥有问题的原因。

当然如果你的应用程序是CPU密集型的，那么在python中确实没有太多的线程可以提供使用。



<https://blog.csdn.net/weixin_40481076/article/details/101594705/>

'''
                           GIL  全局解释器
        在非python环境中，单核情况下，同时只能有一个任务执行。多核时可以支持多个线程同时执行。但是在python中，无论有多少个核
        同时只能执行一个线程。究其原因，这就是由于GIL的存在导致的。
        GIL的全程是全局解释器，来源是python设计之初的考虑，为了数据安全所做的决定。某个线程想要执行，必须先拿到GIL，我们可以
        把GIL看做是“通行证”，并且在一个python进程之中，GIL只有一个。拿不到线程的通行证，并且在一个python进程中，GIL只有一个，
        拿不到通行证的线程，就不允许进入CPU执行。GIL只在cpython中才有，因为cpython调用的是c语言的原生线程，所以他不能直接操
        作cpu，而只能利用GIL保证同一时间只能有一个线程拿到数据。而在pypy和jpython中是没有GIL的
        python在使用多线程的时候，调用的是c语言的原生过程。
'''
'''
                            python针对不同类型的代码执行效率也是不同的
        1、CPU密集型代码（各种循环处理、计算等），在这种情况下，由于计算工作多，ticks技术很快就会达到阀值，然后出发GIL的
        释放与再竞争（多个线程来回切换当然是需要消耗资源的），所以python下的多线程对CPU密集型代码并不友好。
        2、IO密集型代码（文件处理、网络爬虫等设计文件读写操作），多线程能够有效提升效率（单线程下有IO操作会进行IO等待，
        造成不必要的时间浪费，而开启多线程能在线程A等待时，自动切换到线程B，可以不浪费CPU的资源，从而能提升程序的执行
        效率）。所以python的多线程对IO密集型代码比较友好。
'''
'''
    主要要看任务的类型，我们把任务分为I/O密集型和计算密集型，而多线程在切换中又分为I/O切换和时间切换。如果任务属于是I/O密集型，
    若不采用多线程，我们在进行I/O操作时，势必要等待前面一个I/O任务完成后面的I/O任务才能进行，在这个等待的过程中，CPU处于等待
    状态，这时如果采用多线程的话，刚好可以切换到进行另一个I/O任务。这样就刚好可以充分利用CPU避免CPU处于闲置状态，提高效率。但是
    如果多线程任务都是计算型，CPU会一直在进行工作，直到一定的时间后采取多线程时间切换的方式进行切换线程，此时CPU一直处于工作状态，
    此种情况下并不能提高性能，相反在切换多线程任务时，可能还会造成时间和资源的浪费，导致效能下降。这就是造成上面两种多线程结果不能的解释。
结论:I/O密集型任务，建议采取多线程，还可以采用多进程+协程的方式(例如:爬虫多采用多线程处理爬取的数据)；对于计算密集型任务，python此时就不适用了。
'''



## 6.2 Python常用的装饰器



# 7. Kubernetes

## 7.1 Kubernetes 组件

### 控制平面组件（Control Plane Components） 

控制平面的组件对集群做出全局决策(比如调度)，以及检测和响应集群事件（例如，当不满足部署的 `replicas` 字段时，启动新的 [pod](https://kubernetes.io/docs/concepts/workloads/pods/pod-overview/)）。

控制平面组件可以在集群中的任何节点上运行。 然而，为了简单起见，设置脚本通常会在同一个计算机上启动所有控制平面组件， 并且不会在此计算机上运行用户容器。 请参阅[使用 kubeadm 构建高可用性集群](https://kubernetes.io/zh/docs/setup/production-environment/tools/kubeadm/high-availability/) 中关于跨多机器控制平面设置的示例。

#### kube-apiserver

API 服务器是 Kubernetes [控制面](https://kubernetes.io/zh/docs/reference/glossary/?all=true#term-control-plane)的组件， 该组件公开了 Kubernetes API。 API 服务器是 Kubernetes 控制面的前端。

Kubernetes API 服务器的主要实现是 [kube-apiserver](https://kubernetes.io/zh/docs/reference/command-line-tools-reference/kube-apiserver/)。 kube-apiserver 设计上考虑了水平伸缩，也就是说，它可通过部署多个实例进行伸缩。 你可以运行 kube-apiserver 的多个实例，并在这些实例之间平衡流量。

#### etcd

etcd 是兼具一致性和高可用性的键值数据库，可以作为保存 Kubernetes 所有集群数据的后台数据库。

您的 Kubernetes 集群的 etcd 数据库通常需要有个备份计划。

要了解 etcd 更深层次的信息，请参考 [etcd 文档](https://etcd.io/docs/)。

#### kube-scheduler

控制平面组件，负责监视新创建的、未指定运行[节点（node）](https://kubernetes.io/zh/docs/concepts/architecture/nodes/)的 [Pods](https://kubernetes.io/docs/concepts/workloads/pods/pod-overview/)，选择节点让 Pod 在上面运行。

调度决策考虑的因素包括单个 Pod 和 Pod 集合的资源需求、硬件/软件/策略约束、亲和性和反亲和性规范、数据位置、工作负载间的干扰和最后时限。

#### kube-controller-manager

运行[控制器](https://kubernetes.io/zh/docs/concepts/architecture/controller/)进程的控制平面组件。

从逻辑上讲，每个[控制器](https://kubernetes.io/zh/docs/concepts/architecture/controller/)都是一个单独的进程， 但是为了降低复杂性，它们都被编译到同一个可执行文件，并在一个进程中运行。

这些控制器包括:

- 节点控制器（Node Controller）: 负责在节点出现故障时进行通知和响应
- 任务控制器（Job controller）: 监测代表一次性任务的 Job 对象，然后创建 Pods 来运行这些任务直至完成
- 端点控制器（Endpoints Controller）: 填充端点(Endpoints)对象(即加入 Service 与 Pod)
- 服务帐户和令牌控制器（Service Account & Token Controllers）: 为新的命名空间创建默认帐户和 API 访问令牌

#### cloud-controller-manager

云控制器管理器是指嵌入特定云的控制逻辑的 [控制平面](https://kubernetes.io/zh/docs/reference/glossary/?all=true#term-control-plane)组件。 云控制器管理器使得你可以将你的集群连接到云提供商的 API 之上， 并将与该云平台交互的组件同与你的集群交互的组件分离开来。

`cloud-controller-manager` 仅运行特定于云平台的控制回路。 如果你在自己的环境中运行 Kubernetes，或者在本地计算机中运行学习环境， 所部署的环境中不需要云控制器管理器。

与 `kube-controller-manager` 类似，`cloud-controller-manager` 将若干逻辑上独立的 控制回路组合到同一个可执行文件中，供你以同一进程的方式运行。 你可以对其执行水平扩容（运行不止一个副本）以提升性能或者增强容错能力。

下面的控制器都包含对云平台驱动的依赖：

- 节点控制器（Node Controller）: 用于在节点终止响应后检查云提供商以确定节点是否已被删除
- 路由控制器（Route Controller）: 用于在底层云基础架构中设置路由
- 服务控制器（Service Controller）: 用于创建、更新和删除云提供商负载均衡器

### Node 组件 

节点组件在每个节点上运行，维护运行的 Pod 并提供 Kubernetes 运行环境。

#### kubelet

一个在集群中每个[节点（node）](https://kubernetes.io/zh/docs/concepts/architecture/nodes/)上运行的代理。 它保证[容器（containers）](https://kubernetes.io/zh/docs/concepts/overview/what-is-kubernetes/#why-containers)都 运行在 [Pod](https://kubernetes.io/docs/concepts/workloads/pods/pod-overview/) 中。

kubelet 接收一组通过各类机制提供给它的 PodSpecs，确保这些 PodSpecs 中描述的容器处于运行状态且健康。 kubelet 不会管理不是由 Kubernetes 创建的容器。

#### kube-proxy

[kube-proxy](https://kubernetes.io/zh/docs/reference/command-line-tools-reference/kube-proxy/) 是集群中每个节点上运行的网络代理， 实现 Kubernetes [服务（Service）](https://kubernetes.io/zh/docs/concepts/services-networking/service/) 概念的一部分。

kube-proxy 维护节点上的网络规则。这些网络规则允许从集群内部或外部的网络会话与 Pod 进行网络通信。

如果操作系统提供了数据包过滤层并可用的话，kube-proxy 会通过它来实现网络规则。否则， kube-proxy 仅转发流量本身。

#### 容器运行时（Container Runtime） 

容器运行环境是负责运行容器的软件。

Kubernetes 支持容器运行时，例如 [Docker](https://kubernetes.io/zh/docs/reference/kubectl/docker-cli-to-kubectl/)、 [containerd](https://containerd.io/docs/)、[CRI-O](https://cri-o.io/#what-is-cri-o) 以及 [Kubernetes CRI (容器运行环境接口)](https://github.com/kubernetes/community/blob/master/contributors/devel/sig-node/container-runtime-interface.md) 的其他任何实现。

### 插件（Addons） 

插件使用 Kubernetes 资源（[DaemonSet](https://kubernetes.io/zh/docs/concepts/workloads/controllers/daemonset/)、 [Deployment](https://kubernetes.io/zh/docs/concepts/workloads/controllers/deployment/)等）实现集群功能。 因为这些插件提供集群级别的功能，插件中命名空间域的资源属于 `kube-system` 命名空间。

下面描述众多插件中的几种。有关可用插件的完整列表，请参见 [插件（Addons）](https://kubernetes.io/zh/docs/concepts/cluster-administration/addons/)。

#### DNS 

尽管其他插件都并非严格意义上的必需组件，但几乎所有 Kubernetes 集群都应该 有[集群 DNS](https://kubernetes.io/zh/docs/concepts/services-networking/dns-pod-service/)， 因为很多示例都需要 DNS 服务。

集群 DNS 是一个 DNS 服务器，和环境中的其他 DNS 服务器一起工作，它为 Kubernetes 服务提供 DNS 记录。

Kubernetes 启动的容器自动将此 DNS 服务器包含在其 DNS 搜索列表中。

#### Web 界面（仪表盘）

[Dashboard](https://kubernetes.io/zh/docs/tasks/access-application-cluster/web-ui-dashboard/) 是 Kubernetes 集群的通用的、基于 Web 的用户界面。 它使用户可以管理集群中运行的应用程序以及集群本身并进行故障排除。

#### 容器资源监控

[容器资源监控](https://kubernetes.io/zh/docs/tasks/debug/debug-cluster/resource-usage-monitoring/) 将关于容器的一些常见的时间序列度量值保存到一个集中的数据库中，并提供用于浏览这些数据的界面。

#### 集群层面日志

[集群层面日志](https://kubernetes.io/zh/docs/concepts/cluster-administration/logging/) 机制负责将容器的日志数据 保存到一个集中的日志存储中，该存储能够提供搜索和浏览接口。

## 7.2 节点

Kubernetes 通过将容器放入在节点（Node）上运行的 Pod 中来执行你的工作负载。 节点可以是一个虚拟机或者物理机器，取决于所在的集群配置。 每个节点包含运行 [Pods](https://kubernetes.io/docs/concepts/workloads/pods/pod-overview/) 所需的服务； 这些节点由 [控制面](https://kubernetes.io/zh/docs/reference/glossary/?all=true#term-control-plane) 负责管理。

通常集群中会有若干个节点；而在一个学习用或者资源受限的环境中，你的集群中也可能 只有一个节点。

节点上的[组件](https://kubernetes.io/zh/docs/concepts/overview/components/#node-components)包括 [kubelet](https://kubernetes.io/docs/reference/generated/kubelet)、 [容器运行时](https://kubernetes.io/zh/docs/setup/production-environment/container-runtimes)以及 [kube-proxy](https://kubernetes.io/zh/docs/reference/command-line-tools-reference/kube-proxy/)。

### 管理 

向 [API 服务器](https://kubernetes.io/zh/docs/reference/command-line-tools-reference/kube-apiserver/)添加节点的方式主要有两种：

1. 节点上的 `kubelet` 向控制面执行自注册；
2. 你，或者别的什么人，手动添加一个 Node 对象。

在你创建了 Node [对象](https://kubernetes.io/zh/docs/concepts/overview/working-with-objects/kubernetes-objects/#kubernetes-objects)或者节点上的 `kubelet` 执行了自注册操作之后，控制面会检查新的 Node 对象是否合法。 例如，如果你尝试使用下面的 JSON 对象来创建 Node 对象：

```json
{
  "kind": "Node",
  "apiVersion": "v1",
  "metadata": {
    "name": "10.240.79.157",
    "labels": {
      "name": "my-first-k8s-node"
    }
  }
}
```

Kubernetes 会在内部创建一个 Node 对象作为节点的表示。Kubernetes 检查 `kubelet` 向 API 服务器注册节点时使用的 `metadata.name` 字段是否匹配。 如果节点是健康的（即所有必要的服务都在运行中），则该节点可以用来运行 Pod。 否则，直到该节点变为健康之前，所有的集群活动都会忽略该节点。

**Note:**

Kubernetes 会一直保存着非法节点对应的对象，并持续检查该节点是否已经变得健康。 你，或者某个[控制器](https://kubernetes.io/zh/docs/concepts/architecture/controller/)必须显式地删除该 Node 对象以停止健康检查操作。

Node 对象的名称必须是合法的 [DNS 子域名](https://kubernetes.io/zh/docs/concepts/overview/working-with-objects/names#dns-subdomain-names)。

#### 节点名称唯一性 

节点的[名称](https://kubernetes.io/zh/docs/concepts/overview/working-with-objects/names#names)用来标识 Node 对象。 没有两个 Node 可以同时使用相同的名称。 Kubernetes 还假定名字相同的资源是同一个对象。 就 Node 而言，隐式假定使用相同名称的实例会具有相同的状态（例如网络配置、根磁盘内容） 和类似节点标签这类属性。这可能在节点被更改但其名称未变时导致系统状态不一致。 如果某个 Node 需要被替换或者大量变更，需要从 API 服务器移除现有的 Node 对象， 之后再在更新之后重新将其加入。

#### 节点自注册

当 kubelet 标志 `--register-node` 为 true（默认）时，它会尝试向 API 服务注册自己。 这是首选模式，被绝大多数发行版选用。

对于自注册模式，kubelet 使用下列参数启动：

- `--kubeconfig` - 用于向 API 服务器执行身份认证所用的凭据的路径。
- `--cloud-provider` - 与某[云驱动](https://kubernetes.io/zh/docs/reference/glossary/?all=true#term-cloud-provider) 进行通信以读取与自身相关的元数据的方式。
- `--register-node` - 自动向 API 服务注册。
- `--register-with-taints` - 使用所给的[污点](https://kubernetes.io/zh/docs/concepts/scheduling-eviction/taint-and-toleration/)列表 （逗号分隔的 `<key>=<value>:<effect>`）注册节点。当 `register-node` 为 false 时无效。
- `--node-ip` - 节点 IP 地址。
- `--node-labels` - 在集群中注册节点时要添加的[标签](https://kubernetes.io/zh/docs/concepts/overview/working-with-objects/labels/)。 （参见 [NodeRestriction 准入控制插件](https://kubernetes.io/zh/docs/reference/access-authn-authz/admission-controllers/#noderestriction)所实施的标签限制）。
- `--node-status-update-frequency` - 指定 kubelet 向控制面发送状态的频率。

启用[Node 鉴权模式](https://kubernetes.io/zh/docs/reference/access-authn-authz/node/)和 [NodeRestriction 准入插件](https://kubernetes.io/zh/docs/reference/access-authn-authz/admission-controllers/#noderestriction)时， 仅授权 `kubelet` 创建或修改其自己的节点资源。

**Note:**

正如[节点名称唯一性](https://kubernetes.io/zh/docs/concepts/architecture/nodes/#node-name-uniqueness)一节所述，当 Node 的配置需要被更新时， 一种好的做法是重新向 API 服务器注册该节点。例如，如果 kubelet 重启时其 `--node-labels` 是新的值集，但同一个 Node 名称已经被使用，则所作变更不会起作用， 因为节点标签是在 Node 注册时完成的。

如果在 kubelet 重启期间 Node 配置发生了变化，已经被调度到某 Node 上的 Pod 可能会出现行为不正常或者出现其他问题，例如，已经运行的 Pod 可能通过污点机制设置了与 Node 上新设置的标签相排斥的规则，也有一些其他 Pod， 本来与此 Pod 之间存在不兼容的问题，也会因为新的标签设置而被调到到同一节点。 节点重新注册操作可以确保节点上所有 Pod 都被排空并被正确地重新调度。

#### 手动节点管理

你可以使用 [kubectl](https://kubernetes.io/docs/user-guide/kubectl-overview/) 来创建和修改 Node 对象。

如果你希望手动创建节点对象时，请设置 kubelet 标志 `--register-node=false`。

你可以修改 Node 对象（忽略 `--register-node` 设置）。 例如，修改节点上的标签或标记其为不可调度。

你可以结合使用 Node 上的标签和 Pod 上的选择算符来控制调度。 例如，你可以限制某 Pod 只能在符合要求的节点子集上运行。

如果标记节点为不可调度（unschedulable），将阻止新 Pod 调度到该 Node 之上， 但不会影响任何已经在其上的 Pod。 这是重启节点或者执行其他维护操作之前的一个有用的准备步骤。

要标记一个 Node 为不可调度，执行以下命令：

```shell
kubectl cordon $NODENAME
```

更多细节参考[安全地腾空节点](https://kubernetes.io/zh/docs/tasks/administer-cluster/safely-drain-node/)。

**Note:**

被 [DaemonSet](https://kubernetes.io/zh/docs/concepts/workloads/controllers/daemonset/) 控制器创建的 Pod 能够容忍节点的不可调度属性。 DaemonSet 通常提供节点本地的服务，即使节点上的负载应用已经被腾空， 这些服务也仍需运行在节点之上。

### 节点状态 

一个节点的状态包含以下信息:

- [地址（Addresses）](https://kubernetes.io/zh/docs/concepts/architecture/nodes/#addresses)
- [状况（Condition）](https://kubernetes.io/zh/docs/concepts/architecture/nodes/#condition)
- [容量与可分配（Capacity）](https://kubernetes.io/zh/docs/concepts/architecture/nodes/#capacity)
- [信息（Info）](https://kubernetes.io/zh/docs/concepts/architecture/nodes/#info)

你可以使用 `kubectl` 来查看节点状态和其他细节信息：

```shell
kubectl describe node <节点名称>
```

下面对每个部分进行详细描述。

#### 地址 

这些字段的用法取决于你的云服务商或者物理机配置。

- HostName：由节点的内核报告。可以通过 kubelet 的 `--hostname-override` 参数覆盖。
- ExternalIP：通常是节点的可外部路由（从集群外可访问）的 IP 地址。
- InternalIP：通常是节点的仅可在集群内部路由的 IP 地址。

#### 状况

`conditions` 字段描述了所有 `Running` 节点的状况。状况的示例包括：

| 节点状况             | 描述                                                         |
| -------------------- | ------------------------------------------------------------ |
| `Ready`              | 如节点是健康的并已经准备好接收 Pod 则为 `True`；`False` 表示节点不健康而且不能接收 Pod；`Unknown` 表示节点控制器在最近 `node-monitor-grace-period` 期间（默认 40 秒）没有收到节点的消息 |
| `DiskPressure`       | `True` 表示节点存在磁盘空间压力，即磁盘可用量低, 否则为 `False` |
| `MemoryPressure`     | `True` 表示节点存在内存压力，即节点内存可用量低，否则为 `False` |
| `PIDPressure`        | `True` 表示节点存在进程压力，即节点上进程过多；否则为 `False` |
| `NetworkUnavailable` | `True` 表示节点网络配置不正确；否则为 `False`                |

**Note:**

如果使用命令行工具来打印已保护（Cordoned）节点的细节，其中的 Condition 字段可能包括 `SchedulingDisabled`。`SchedulingDisabled` 不是 Kubernetes API 中定义的 Condition，被保护起来的节点在其规约中被标记为不可调度（Unschedulable）。

在 Kubernetes API 中，节点的状况表示节点资源中`.status` 的一部分。 例如，以下 JSON 结构描述了一个健康节点：

```json
"conditions": [
  {
    "type": "Ready",
    "status": "True",
    "reason": "KubeletReady",
    "message": "kubelet is posting ready status",
    "lastHeartbeatTime": "2019-06-05T18:38:35Z",
    "lastTransitionTime": "2019-06-05T11:41:27Z"
  }
]
```

如果 Ready 状况的 `status` 处于 `Unknown` 或者 `False` 状态的时间超过了 `pod-eviction-timeout` 值（一个传递给 [kube-controller-manager](https://kubernetes.io/docs/reference/generated/kube-controller-manager/) 的参数），[节点控制器](https://kubernetes.io/zh/docs/concepts/architecture/nodes/#node-controller)会对节点上的所有 Pod 触发 [API-发起的驱逐](https://kubernetes.io/zh/docs/concepts/scheduling-eviction/pod-eviction/#api-eviction)。 默认的逐出超时时长为 **5 分钟**。

某些情况下，当节点不可达时，API 服务器不能和其上的 kubelet 通信。 删除 Pod 的决定不能传达给 kubelet，直到它重新建立和 API 服务器的连接为止。 与此同时，被计划删除的 Pod 可能会继续在游离的节点上运行。

节点控制器在确认 Pod 在集群中已经停止运行前，不会强制删除它们。 你可以看到可能在这些无法访问的节点上运行的 Pod 处于 `Terminating` 或者 `Unknown` 状态。 如果 kubernetes 不能基于下层基础设施推断出某节点是否已经永久离开了集群， 集群管理员可能需要手动删除该节点对象。 从 Kubernetes 删除节点对象将导致 API 服务器删除节点上所有运行的 Pod 对象并释放它们的名字。

当节点上出现问题时，Kubernetes 控制面会自动创建与影响节点的状况对应的 [污点](https://kubernetes.io/zh/docs/concepts/scheduling-eviction/taint-and-toleration/)。 调度器在将 Pod 指派到某 Node 时会考虑 Node 上的污点设置。 Pod 也可以设置[容忍度](https://kubernetes.io/zh/docs/concepts/scheduling-eviction/taint-and-toleration/)， 以便能够在设置了特定污点的 Node 上运行。

进一步的细节可参阅[根据状况为节点设置污点](https://kubernetes.io/zh/docs/concepts/scheduling-eviction/taint-and-toleration/#taint-nodes-by-condition)。

#### 容量（Capacity）与可分配（Allocatable） 

这两个值描述节点上的可用资源：CPU、内存和可以调度到节点上的 Pod 的个数上限。

`capacity` 块中的字段标示节点拥有的资源总量。 `allocatable` 块指示节点上可供普通 Pod 消耗的资源量。

可以在学习如何在节点上[预留计算资源](https://kubernetes.io/zh/docs/tasks/administer-cluster/reserve-compute-resources/#node-allocatable) 的时候了解有关容量和可分配资源的更多信息。

#### 信息（Info）[ ](https://kubernetes.io/zh/docs/concepts/architecture/nodes/#info)

Info 指的是节点的一般信息，如内核版本、Kubernetes 版本（`kubelet` 和 `kube-proxy` 版本）、 容器运行时详细信息，以及节点使用的操作系统。 `kubelet` 从节点收集这些信息并将其发布到 Kubernetes API。



## [7.3 k8s整体架构的知识整理](https://baijiahao.baidu.com/s?id=1711258936914342244&wfr=spider&for=pc)

### 1、master节点

Master是Kubernetes Cluster的大脑，运行着的Daemon服务有以下几个：

- kube-apiserver
- kube-scheduler
- kube-controller-manager
- etcd
- Pod 网络（例如 flannel）

如图所示：



![img](https://pics4.baidu.com/feed/fd039245d688d43fce38295aab45f1120ff43b6d.png?token=546e276fd924620c6555154e226f098c)

接下来就一个一个介绍如上组建的功能。 1）API Server（kube-apiserver） API Server 提供 HTTP/HTTPS RESTful API，即 Kubernetes API。API Server 是 Kubernetes Cluster 的前端接口，各种客户端工具（CLI 或 UI）以及 Kubernetes 其他组件可以通过它管理 Cluster 的各种资源。

2）Scheduler（kube-scheduler） Scheduler 负责决定将 Pod 放在哪个 Node 上运行。Scheduler 在调度时会充分考虑 Cluster 的拓扑结构，当前各个节点的负载，以及应用对高可用、性能、数据亲和性的需求

3）Controller Manager（kube-controller-manager） Controller Manager 负责管理 Cluster 各种资源，保证资源处于预期的状态。Controller Manager 由多种 controller 组成，包括 replication controller、endpoints controller、namespace controller、serviceaccounts controller 等。

不同的 controller 管理不同的资源。例如 replication controller 管理 Deployment、StatefulSet、DaemonSet 的生命周期，namespace controller 管理 Namespace 资源。

4）etcd etcd 负责保存 Kubernetes Cluster 的配置信息和各种资源的状态信息。当数据发生变化时，etcd 会快速地通知 Kubernetes 相关组件

5）Pod 网络 Pod 要能够相互通信，Kubernetes Cluster 必须部署 Pod 网络，flannel 是其中一个可选方案，也是kubernetes官方默认的一种方案。

### 2、Node节点

Node 是 Pod 运行的地方，Kubernetes 支持 Docker、rkt 等容器 Runtime。 Node上运行的 Kubernetes 组件有：

- kubelet
- kube-proxy

如图所示：

![img](https://pics3.baidu.com/feed/500fd9f9d72a605908fcfe8efe6f1792023bba1e.png?token=0f07b619b40f19852aed21c1e55ef09a)

1）kubelet kubelet 是 Node  agent，当 Scheduler 确定在某个 Node 上运行 Pod 后，会将 Pod 的具体配置信息（image、volume 等）发送给该节点的 kubelet，kubelet 根据这些信息创建和运行容器，并向 Master 报告运行状态

2）kube-proxy service 在逻辑上代表了后端的多个 Pod，外界通过 service 访问 Pod。service 接收到的请求是如何转发到 Pod 的呢？这就是 kube-proxy 要完成的工作。

每个 Node 都会运行 kube-proxy 服务，它负责将访问 service 的 TCP/UPD 数据流转发到后端的容器。如果有多个副本，kube-proxy 会实现负载均衡。

3）Pod 网络 Pod 要能够相互通信，Kubernetes Cluster 必须部署 Pod 网络，flannel 是其中一个可选方案。



## 7.4 [K8S--架构及基本概念](https://www.cnblogs.com/liconglong/p/15044124.html)

### 一、K8S简述

　　K8S是Kubernetes的简称，其是基于谷歌的Borg系统开发的。

　　K8S主要功能：

　　K8s是用来对docker容器进行管理和编排的工具，其是一个基于docker构建的调度服务，提供资源调度、均衡容灾、服务注册、动态扩容等功能套件，其作用如下所示：

　　（1）数据卷：pod中容器之间数据共享，可以使用数据卷

　　（2）应用程序健康检查：容器内服务可能发生异常导致服务不可用，可以使用健康检查策略保证应用的健壮性。

　　（3）复制应用程序实例：控制器维护着pod的副本数量，保证一个pod或者一组同类的pod数量始终可用。

　　（4）弹性伸缩：根据设定的指标（CPU利用率等）动态的自动缩放pod数

　　（5）负载均衡：一组pod副本分配一个私有的集群IP地址，负载均衡转发请求到后端容器，在集群内布，其他pod可通过这个Cluster IP访问集群。

　　（6）滚动更新：更新服务不中断，一次更新一个pod，而不是同时删除整个服务

　　（7）服务编排：通过文件描述部署服务，使的程序部署更高效。

　　（8）资源监控：Node节点组件集成cAdvisor资源收集工具，可通过Heapster汇总整个集群节点资源数据，然后存储到InfluxDB时序数据库，再由Grafana展示

　　（9）提供认证和授权：支持属性访问控制、角色访问控制等认证授权策略。

　　K8S的架构图如下所示：

　　　　　　![img](https://img2020.cnblogs.com/blog/1782729/202107/1782729-20210722202052766-752524239.png)

 　从上图可以看到，K8S提供了：

　　　　web browsers提供可视化操作

　　　　kubectl来接收Docker镜像进行部署

　　　　scheduler进行任务调度

　　　　Controller进行请求控制

　　　　API Server进行请求的统一网关处理

　　　　etcd用于存储集群中的网络及状态信息

　　　　Kubelet来接收控制器的处理任务

　　　　Container Registry用来存储镜像仓库

[回到顶部](https://www.cnblogs.com/liconglong/p/15044124.html#_labelTop)

### 二、K8S集群



### （一）集群概述

　　一个K8S集群包含一个master节点和一群node节点，Mater节点负责管理和控制，Node节点是工作负载节点，里面是具体的容器，容器中部署的是具体的服务。

　　Master节点包括API Server、Scheduler、Controller Manager、etcd。

　　　　API Server：整个集群的对外接口，供客户端和其他组件调用。

　　　　Scheduler：负责集群内部资源调度

　　　　Controller Manager：负责管理控制器。

　　　　etcd：用于保存集群中所有网络配置和对象状态信息。

　　node节点包括Docker、kubelet、kube-proxy、Fluentd、kube-dns（可选）、pod　　　　　



###  （二）Mater节点

　　上面已经提到，Mater节点包括API Server、Scheduler、Controller Manager、etcd。

　　**1、API Server**

　　　　API Server是整个集群的统一入口，各组件的协调者，以HTTP的形式对外提供服务，所有对象资源的增删改查和监听操作都交给API Server处理后再提交给etcd进行存储。

　　**2、Scheduler**

　　　　Scheduler负责集群内部资源调度，其会根据调度算法为新创建的pod选择一个node节点，Scheduler在整个集群中起到了承上启下的重要功能，承上是指她负责接收Controller Manager创建的新的pod，为其安排一个node节点，启下指的是当为pod选定node节点后，目标Node上的kubelet服务进程会接管该pod。

　　　　　　![img](https://img2020.cnblogs.com/blog/1782729/202107/1782729-20210722203441898-830912786.png)

　　　　这里就要提一下创建Pod的流程：

　　　　　　（1）kubectl发送创建pod的请求，此时这个命令被apiserver拦截，把创建的pod存储到etcd的podQueue

　　　　　　（2）Scheduler发起调用请求，此时这个命令被apiserver拦截，获取etcd中podQueue.NodeList，使用调度算法（调度算法：预选调度、优选策略）选择一个合适的node节点

　　　　　　（3）把选择合适的pod、node存储到etcd中

　　　　　　（4）node节点上的Kubelet进程，发送请求获取pod、node对应创建资源

　　　　　　（5）此时node发现pod是本node需要创建的，kubelet就开始创建pod

　　**3、Controller Manager**

　　　　每个资源都对应一个控制器（Kubernets Controller），其用来处理集群中常规的后台任务，而Controller Manager是用来负责管理控制器的。

　　　　K8S集群有以下控制器：

　　　　　　（1）Replication Controller：保证Replication Controller中定义的副本数量与实际运行的pod数量一致。

　　　　　　（2）Node Controller：管理维护Node，定期检查Node节点的健康状态，标识出失效和未失效的Node节点。

　　　　　　（3）Namespace Controller：管理维护Namespace，定期清理无效的Namespace，包括Namespace下的API对象，例如pod和service等

　　　　　　（4）Service Controller：管理维护Service，提供负载以及服务代理。

　　　　　　（5）Endpoints Controller：管理维护Endpoints，即维护关联service和pod的对应关系，其对应关系通过Label来进行关联的

　　　　　　（6）Service Account Controller：管理维护Service Account，为每个Namespace创建默认的Service Account，同时为Service Account创建Service Account Secret。

　　　　　　（7）Persistent Volume Controller：持久化数据控制器，用来部署有状态服务

　　　　　　（8）Deamon Set Controller：让每一个Node节点都运行相同的服务

　　　　　　（9）Deployment Controller：无状态服务部署控制器

　　　　　　（10）Job Controller：管理维护Job，为Job创建一次性任务Pod，保证完成Job指定完成的任务数目。

　　　　　　（11）Pod Autoscaler Controller：实现pod的自动伸缩，定时获取监控数据，进行策略匹配，当满足条件时执行pod的伸缩动作。

　　**4、etcd**

　　　　etcd是一个第三方服务，分布式键值对存储系统，用于保存网络配置、集群状态等信息，例如service、pod等对象的信息。

　　　　K8S中一共有两个服务需要用到etcd来协调和存储数据，分别是网络插件flannel和K8S本身，其中flannel使用etcd存储网络配置信息，K8S本身使用etcd存储各种对象的状态和元信息配置。



### （三）Node节点

　　上面提到，node节点包括Docker、kubelet、kube-proxy、Fluentd、kube-dns（可选）、pod等信息。

　　　　　　![img](https://img2020.cnblogs.com/blog/1782729/202107/1782729-20210722203202058-2086367794.png)

　　**1、kubelet**

　　　　kubelet是Mater在Node节点上的代理，每个Node节点都会启动一个kubelet进程，用来处理Mater节点下发到Node节点的任务，管理本机运行容器的生命周期，比如创建容器、Pod挂载数据卷、下载secret、获取容器和节点的状态等工作，kubelet将每个pod转换成一组容器。

　　　　kubelet默认监听四个端口：10250、10255、10248、4194

　　　　10250端口：kubelet API的端口，也就是kubelet server与api server的通讯端口，定期请求apiserver获取自己所应当处理的任务，通过该端口可以访问和获取node资源及状态。

　　　　10248端口：健康检查的端口，通过访问该端口可以判断kubelet是否正常工作，可以通过 kubelet 的启动 参数 --healthz-port 和 --healthz-bind-address 来指定监听的地址和端口

　　　　4194端口：kubelet通过该端口可以获取到该节点的环境信息以及node上运行的容器状态等内容，访问 http://localhost:4194 可以看到 cAdvisor 的管理界面,通过 kubelet 的启动参 数 --cadvisor-port 可以指定启动的端口。

　　　　10255端口：提供了pod和node的信息，接口以只读形式暴露出去，访问该端口不需要认证和鉴权。

　　**2、kube-proxy**

　　　　在Node节点上实现Pod网络代理，维护网络规则和四层负载均衡工作，kube-proxy本质上类似于一个反向代理，我们可以把每个节点上运行的kube-proxy看作是service的透明代理兼LB。

　　　　kube-proxy监听apiserver中service与endpoints的信息，配置iptables规则，请求通过iptables直接转发给pod。

　　　　![img](https://img2020.cnblogs.com/blog/1782729/202107/1782729-20210722215314300-1238618727.png)

 　**3、docker**

　　　　运行容器的引擎，pod内部运行的都是容器，这个容器是由Docker引擎创建的，Docker引擎是node节点的基础服务。

　　**4、pod**

　　　　pod是最小的部署单元，一个pod由一个或多个容器组成，pod中共享存储和网络，在同一个Docker主机上运行。pod内部可以运行一个或多个容器，一般情况下，为了便于管理，一个pod下只运行一个容器。



### （四）Pod

　　pod就是一个容器，内部封装了docker容器，同时拥有自己的ip地址，也有用自己的HostName，Pod就像一个物理机一样，实际上Pod就是一个虚拟化的容器（进程），pod中运行的是一个或者多个容器。

　　　　　　![img](https://img2020.cnblogs.com/blog/1782729/202107/1782729-20210722222659860-1734078012.png)

 　pod是一个大的容器，由K8S创建，pod内部的是docker容器，由Docker引擎创建。K8S不会直接管理容器，而是管理Pod。

　　pod的作用是管理线上运行的应用程序，在通常情况下，在服务上线部署的时候，pod通常被用来部署一组相关的服务。而一个调用链上的服务就叫做一组相关的服务。但是实际生产上一般是一个Pod对应一个服务，不会在一个Pod上部署太多的服务。

　　pod的具体结构如下图所示，一个Node中有很多pause容器，这些pause容器和pod是一一对应的，每个Pod里面运行着一个特殊的被称为Pause的容器，其他的容器则为业务容器，这些容器共享Pause容器的网络和存储，因此他们之间的通信更高效，在同一个pod里面的容器之间仅需要通过localhost就可以通信。

　　　　　　![img](https://img2020.cnblogs.com/blog/1782729/202107/1782729-20210722223320303-1480415218.png)

 　K8S中的pause容器主要为每个业务容器提供以下功能，从而对各个Pod进行了隔离：

　　　　PID命名空间隔离：pod中不同的应用程序可以看到其他应用程序的进程ID

　　　　网络命名空间隔离：Pod中多个容器能够访问同一个IP和端口范围

　　　　IPC命名空间隔离：Pod中多个容器能够使用System VIPC或POSIX消息队列进行通信

　　　　UTS命名空间隔离：pod中多个容器共享一个主机名和挂在卷

　　　　Pod中各个容器可以访问在Pod级别定义的Volumes

　　一个Pod创建的过程：首先kubelet会先创建一个pod，然后立马会创建一个pause容器，pause容器是默认创建的，然后再创建内部其他的业务容器。

[回到顶部](https://www.cnblogs.com/liconglong/p/15044124.html#_labelTop)

### 三、核心组件及原理

### 　　**1、RC控制器（ReplicationController）**

　　　　用来确保容器应用的副本数始终与用户定义的副本数一致，如果有副本由于异常退出，Replication Pod会自动创建新的Pod来替代，而如果出现多余的pod，控制器也会自动将其回收。

　　　　在新版的K8S中，建议使用ReplicaSet来取代Replication Controller

### 　　**2、RS控制器（ReplicaSet）**

　　　　ReplicaSet和Replication Contreoller并没有本质上的区别，ReplicaSet支持集合式的选择器。

　　　　虽然ReplicaSet可以独立使用，但一般情况下还是建议使用Deployment来自动管理ReplicaSet，这样就无需担心跟其他机制的不兼容问题。

　　　　RC和RS的区别是RC只支持单个标签选择器，不支持复合标签选择器；而RS同时支持单个和复合选择器。

### 　　3**、label（标签）**

　　　　label用于区分对象，例如区分是service还是pod，以键值对的形式存在，每个对象可以有多个标签，可以通过标签关联对象。

　　　　label是replication Controller和Service运行的基础，二者是通过label来进行关联Node上运行的Pod。我们可以通过给指定的资源对象捆绑一个或多个不同的Label来实现多维度资源分配管理功能，一些常用的Label如下所示：

```
版本标签："release":"stable","release":"canary"......
环境标签："environment":"dev","environment":"qa","environment":"production"
架构标签："tier":"frontend","tier":"backend","tier":"middleware"
分区标签："partition":"customerA","partition":"customerB"
质量管控标签："track":"daily","track":"weekly"
```

　　　　label就类似与标签，给某个对象定义了一个label就相当于给对象定义了一个标签。

　　　　如果多个pod拥有相同的标签，就说明这是一组pod。

### 　　**4、selector**

　　　　标签选择器是K8S非常重要的一环，其用来查询和筛选某些拥有具体标签的对象，K8S也是使用这种方式进行对象的查询。

　　　　Label Selector在K8S中的应用有以下几个场景：

　　　　　　Kube-Controller进程通过资源对象RC上定义的Label Selector来筛选要监控的Pod的副本数量，从而实现副本数量与用户定义的副本数量保持一致。

　　　　　　Kube-proxy进程通过Service的Label Selector来筛选对应的Pod，自动建立起每个Service到对应Pod的请求链路表，从而实现Service的负载均衡。

　　　　　　Kube-Scheduler通过对某些Pod的自定义Label，并且在pod定义文件中使用Node Selector这种标签的调度策略，从而实现了Pod定向调度的特性。

　　　　例如上面Label的例子中，如果服务中既有环境Label又有版本Label，使用RC只能对同一个版本标签或者同一个环境标签的Pod进行选择，不能同时对版本和环境两个维度进行筛选，而RS可以对两个维度同时进行筛选。

### 　　**5、Deployment**

　　　　RS虽然可以控制副本的数量，但是单独的RS部署却不能滚动更新。因此衍生了Deployment组件，其支持滚动更新，其会先创建新版本的pod容器，然后再删除老旧版本的pod容器。

　　　　滚动发布：滚动发布有金丝雀发布和灰度发布，该种发布一般是以25%的模式进行发布，也就是先删除25%旧版本，在部署对应数量的新版本Pod，然后再删除25%旧版本，这样以此滚动更新。

　　　　因此实际生产中一般都是用RS和Deployment的组合来进行发布服务。

　　　　总体来说，Deployment是用来管理RS，RS来管理Pod，deployment支持动态更新，也就是在更新时动态的创建一个新的RS，然后由新RS创建新版本的Pod，然后将旧版本RS中的Pod进行删除。如果发生回滚，就是一个逆向操作，产生一个旧版本的RS，用来生成旧版本的Pod。

　　　　在滚动发布过程中，对于流量是转发到新版本还是老版本的Pod中，是由商城的Service进行转发的。

　　　　Deployment为Pod和ReplicaSet提供了一个声明式定义方法，典型的应用场景：

　　　　　　定义Deployment来创建Pod和ReplicaSet

　　　　　　滚动升级和回滚应用

　　　　　　扩容和缩容

　　　　　　暂停和继续Deployment

　　　　　　![img](https://img2020.cnblogs.com/blog/1782729/202107/1782729-20210722223347759-1287680549.png)

###  　**6、HPA（HorizontalPodAutoScale）**

　　　　HPA仅适用于Deployment和ReplicaSet，在V1.0版本中，仅支持根据pod的CPU利用率进行扩容缩容，在新版本中，支持根据内存和用户自定义的metric进行扩容缩容。说的通俗一点，就是在流量突然增大，可以自动扩容，流量降下后，可以自动缩容。

### 　　**7、StatefullSet**

　　　　deployment和StatefullSet都是用来进行服务部署的，但是这两个组件各自使用的场景不一样，deployment是用来部署无状态服务的，而StatefullSet是用来部署有状态服务的。

　　　　这里说明一下有状态服务和无状态服务，有状态服务指的是需要进行实时的数据更新和存储的服务，如果将某个服务抽离，再加入进来就没办法进行正常工作，例如mysql、redis等；无状态服务指的是没有对应数据进行更新和存储的服务，如果将某个服务抽离，再加入进来依然可以提供服务，我们常用的业务服务一般都是无状态服务，而docker业主要是为了无状态服务提供部署的。

　　　　StatefullSet应用场景包括：

　　　　　　（1）稳定的持久化存储，即pod重新调度后仍然可以访问相同的持久化数据，基于PVC实现

　　　　　　（2）稳定的网络标志，即pod重新调度后，podName和HostName不变，基于Headless Service来实现

　　　　　　（3）有序部署&有序扩展，即pod是有顺序的，在部署或者扩展的时候要依据定义的顺序依次进行，也就是说，从0到N-1，在下一个pod运行前，所有之前的pod都处于running和ready状态。基于init contains实现。

　　　　　　（4）有序收缩&有序删除，即从N-1到0进行回收或删除

　　　　总体来说，Pod是可能随时删除或者新增的，一个pod也是有自己的网络和存储的，对于例如Mysql这类的Pod，可能不能发生网络和存储上的变化，StatefullSet就是为了解决这个问题而产生的。

### 　　8**、DaemonSet**

　　　　DaemonSet确保全部Node上运行一个Pod副本，当有Node加入集群时，也会为他们新增一个pod，当有Node从集群中被移除时，这些pod也会被回收，删除DaemonSet将会删除其创建的所有pod。最典型的场景就是每个Pod里面都有服务在运行，需要收集服务运行日志，但是Pod是由K8S自动创建或删除的，因此需要使用DaemonSet来设定在每一个Pod中进行日志收集。

　　　　DaemonSet的一些典型用法：

　　　　　　（1）运行集群存储Daemon，例如在每个Node上运行glustered、ceph

　　　　　　（2）在每个Node上运行日志收集Daemon，例如fluentd、logstash

　　　　　　（3）在每个Node上运行监控Daemon，例如Prometheus Node Exporter

　　　　Job负责批处理任务，即仅执行一次的任务，他保证批处理任务的一个或多个pod成功结束。

### 　　9**、Volume**

　　　　数据卷，共享pod中容器使用的数据。

## 7.5 [K8S的架构及工作原理](https://blog.csdn.net/qq_40378034/article/details/104884825)

K8S创建一个Pod的流程


1）、用户提交创建Pod的请求，可以通过API Server的REST API，也可用Kubectl命令行工具

2）、API Server处理用户请求，存储Pod数据到etcd

3）、Schedule通过和API Server的watch机制，查看到新的Pod，尝试为Pod绑定Node

4）、过滤主机：调度器用一组规则过滤掉不符合要求的主机，比如Pod指定了所需要的资源，那么就要过滤掉资源不够的主机

5）、主机打分：对第一步筛选出的符合要求的主机进行打分，在主机打分阶段，调度器会考虑一些整体优化策略，比如把一个Replication Controller的副本分布到不同的主机上，使用最低负载的主机等

6）、选择主机：选择打分最高的主机，进行binding操作，结果存储到etcd中

7）、Kubelet根据调度结果执行Pod创建操作： 绑定成功后，会启动container，Scheduler会调用API在数据库etcd中创建一个bound pod对象，描述在一个工作节点上绑定运行的所有Pod信息。运行在每个工作节点上的Kubelet也会定期与etcd同步bound pod信息，一旦发现应该在该工作节点上运行的bound pod对象没有更新，则调用Docker API创建并启动Pod内的容器

在这期间，Control Manager同时会根据K8S的mainfiles文件执行RC Pod的数量来保证指定的Pod副本数。而其他的组件，比如Scheduler负责Pod绑定的调度，从而完成整个Pod的创建

## 7.6 [云计算的三层服务模式 IAAS / PAAS / SAAS](https://zhuanlan.zhihu.com/p/463071146)

### **SaaS (Software-as-a-service:软件即服务)**

电子邮箱，在线office，在线代码编辑器等

### **PaaS(Platform-as-a-Service:平台即服务)**

Kubernetes，docker

### **IaaS(Infrastructure as a Service:基础设施即服务)**

很多年前，如果你想在办公室或者公司的网站上运行一些企业应用，你需要去买服务器，或者别的高昂的硬件来控制本地应用，让你的业务运行起来。但是现在有IaaS，你可以将硬件外包到别的地方去。IaaS公司会提供场外服务器，存储和网络硬件，你可以租用。节省了维护成本和办公场地，公司可以在任何时候利用这些硬件来运行其应用。

![img](https://pic1.zhimg.com/80/v2-15725b2982f2039266253318e0707acc_720w.jpg)

**特点**

IaaS 属于基础设施，比如网络光纤，服务器，存储，防火墙设备等。

PaaS 是在IaaS上的一层集成的操作系统，数据库，服务器程序，中间件等。

SaaS 是将软件当成服务来提供的方式，不再作为产品来销售，一般以按月度收费为主。

**技术上的区别**

IaaS: 最底层的，提供基础硬件平台，网络服务等, 像亚马逊, 阿里云等

Paas: 提供中间层服务, 像支付, 安全, 大数据等中间件

Saas: 提供最上层等业务方面的服务, 像CRM(SalesForce), 协同(e之助), 表单(金表单)

**服务对象的区别**

IaaS: 开发者和企业客户

Paas: 开发者

Saas: 最终用户

**成熟度的区别**

IaaS: 在应用层成熟后兴起, 成熟度较高

Paas: 起步最晚, 成熟度最低

Saas: 发展最早, 成熟度最高

**核心能力的区别**

IaaS: 帮助企业/开发者快速拥有存储、计算等资源（服务器，数据库等）

Paas: 帮助开发者的产品快速获得某种功能（Kubernetes，docker容器，队列，缓存等）

Saas: 帮助企业优化业务流程（应用软件）



## 7.7 [knative](https://www.zhihu.com/question/363951810/answer/1616931913)

Kubernetes 是基座，Istio和Knative都是通过CRD + controller模式 扩展出来的项目，分别用于不同的场景。

Istio 提供了对整个服务网格的行为洞察和操作控制的能力，以及一个完整的满足微服务应用各种需求的解决方案。Istio 号称k8s 网络++，在网络上对k8s是一种增强。

Knative 则专注于构建这些工具和服务来提升现有的 Kubernetes 体验。这为不断增长的开发者（Kubernetes 用户）群体带来了即时利益以及轻松的无服务器开发体验。为此，Knative 使用与 Kubernetes 本身相同的模式（控制器）、API (kube-api) 和 Kubernetes 基础架构（Kubernetes 资源）构建而成。Knative 还提供“缩容至零”功能，支持真正零成本使用空闲应用程序，并支持采用蓝/绿部署来测试无服务器应用的新版本。Knative 和Istio 并没有什么强关联，只是istio 的gateway 组件可以被knative用来做流量接入。事实上，knative 还支持solo，ambassador，contour等其他gateway来做流量接入。这点对比knative早期和现在的架构图可以看出。

Knative用于解决k8s对流量控制的不足，增加流量分发控制的能力。



https://www.zhihu.com/question/363951810/answer/1567202794
Kubernetes是容器运行的平台，处在最底层。
Knative是一个serverless的平台，简单来说，你只需要一个docker image，kantive会自动加上自动缩放，api，灰度
Istio，Knative需要一个组件来实现流量分发相关的功能，主要是7层，这时候可以使用Istio，也可以使用别的，类似nginx ingress之类的，不是必须使用Istio。



## 7.8 [Knative对比k8s及相关概念](https://zhuanlan.zhihu.com/p/386538443)

Knative 是谷歌开源的 serverless 架构方案，旨在提供一套简单易用的 serverless 方案，把 serverless 标准化。目前参与的公司主要是 **Google、Pivotal、IBM、Red Hat**，2018年7月24日才刚刚对外发布，当前还处于快速发展的阶段(3.4k star, 3.3k issue)。


Knative 包含 build（已被tekton取代），serving，event三个部分，本文主要介绍serving。
先看下Knative的发展里历程前言 有了 ”k8s，为什么还要 knative”
通常情况下 Serverless = Faas + Baas，Faas 无状态（业务逻辑），Baas 有状态（通用服务：数据库，认证，消息队列）。
既然有了 k8s (paas), 为什么还需要 Knative (Serverless),下面从四个方面来进行解释：资源利用率，弹性伸缩，按比例灰度发布，用户运维复杂性

1. 资源利用率
想一下，如果用 paas(k8s) 来实现的话，对于应用 A，需按照资源占用的资源最高点来申请规格，也就是 4U10G, 而且 paas 最低实例数>=1, 长此以往, 当中长尾应用足够多时，资源利用率可想而知。有可能会出现 大部分边缘集群资源被预占，但是利用率却很低。
而 Knative，恰恰可以解决应用A的资源占用问题，因为 Knative 可以将实例缩容为0，并根据请求自动扩缩容，缩容到零可以大大增加集群的资源利用率，因为中长尾应用都是按需所取，不会过度空占用资源。
比较合理的是对应应用A 用 Knative(Serverless)，对于应用 B 用 k8s（Paas）

2. 弹性伸缩
大家可能会想到，k8s 也有 hpa 进行扩缩容，但是 Knative 的 kpa 和 k8s 的 hpa 有很大的不同：
• Knative 支持缩容到 0 和从 0 启动，反应更迅速适合流量突发场景；
• K8s HPA 不支持缩容到 0 ，反应比较保守

3. 按比例灰度发布
设想一下，假如通过 k8s来进行灰度发布怎么做，只能是通过两个Deployment和两个service，如果灰度升级的话只能通过修改两个 Deployment 的rs，一个逐渐增加，一个逐渐减少，如果想要按照百分比灰度，只能在外部负载均衡做文章，所以要想 Kubernetes 原生实现，至少需要一个按流量分发的网关，两个 service，两个 deployment ，两个 ingress ， hpa，prometheus 等，实现起来相当复杂。
使用 Knative 就可以很简单的实现，只需一个 ksvc 即可

4. 用户运维复杂性
使用 Knative 免运维，低成本：用户只关心业务逻辑，由工具和云去管理资源，复杂性由平台去做：容器镜像构建，Pod 的管控，服务的发布，相关的运维等。
k8s 本质上还是基础设施的抽象，对应pod的管控，服务的发布，镜像的构建等等需要上层的包装。

**1. 相关概念介绍**
**资源介绍**： knative 资源
推荐一个工具 [kubectl tree ](https://link.zhihu.com/?target=https%3A//github.com/ahmetb/kubectl-tree)可以查看k8s资源之间的引用关系


**1.** ***Service(ksvc)\***
ksvc 是 Knative 中 最顶层的 CR 资源，用于定义 Knative 应用 ，包含镜像以及 traffic 百分比等等（本例配了 两个版本，流量百分比是 10%，90%）, 可以接管 route 和 configuration 的配置

![img](https://pic4.zhimg.com/80/v2-837194d5b606671f418ec5c711c27e47_720w.jpg)


**2.** ***Configuration\***
configuration 是 Knative 应用的最新配置，也就是应用目前期望的状态。configuration 更改会产生快照 revision

![img](https://pic4.zhimg.com/80/v2-71e14fc6b9be9f7b371d4286b5368e8f_720w.jpg)


**3.** ***Revision\***
revision 是 Knative 应用的快照，Knative 的设计理念中 revision 是不可更改的，可以看做是 git 的 历史 commit 记录

![img](https://pic4.zhimg.com/80/v2-1415622e4305ff46d88726a9b34c4cef_720w.jpg)


**4.** ***Route\***
route 是 Knative 蓝绿发布，金丝雀发布的关键，用于声明不同版本之间流量的百分比。

![img](https://pic2.zhimg.com/80/v2-26e42ba4d7d09f2d4e3918a6b0b8a09d_720w.jpg)


**5.** ***Ingress(kingress)\***
Knative 的流量入口网关 是通过 kingress 抽象的。详情可以看第二节 **Knative 网关**

![img](https://pic2.zhimg.com/80/v2-15d577496aeb0f4239a723f640f7ca9d_720w.jpg)


**6. PodAutoScaler(kpa)**

![img](https://pic3.zhimg.com/80/v2-e610b3bcfd4b485d777d4087e702d38e_720w.jpg)


**7.** ***ServerlessService(sks)\***

![img](https://pic2.zhimg.com/80/v2-bcd12f3acbf15852dca6b5bbf5cdc5e9_720w.jpg)


**8.** ***k8s Service (public)\***
注意，此处的 k8s service 没有 label selector，说明这个 service 的后端 endpoint 不是由 k8s 自动控制的，实际上这个 svc 的后端 endpoint 是 由 knative 自己来控制的，（是取 activator 的pod ip 还是 服务真实实例的pod ip）

![img](https://pic2.zhimg.com/80/v2-875f9fa25cb8cb270cbaa8342e360045_720w.jpg)


**9.** ***k8s Service (private)\***
private 类型 的 svc 不同于 public 类型的 svc，这个 svc 是通过label selector 来筛选后端 endpoint 的，这里后端指向的永远是 服务真实实例的 pod ip

![img](https://pic3.zhimg.com/80/v2-a24d54c5f48b85485c12b4970aba13ce_720w.jpg)


**2. Knative 网关**


Knative 从设计之初就考虑到了其扩展性，通过抽象出来 Knative Ingress （kingress）资源来对接不同的网络扩展：**Ambassador**、**Contour**、**Gloo**、**Istio**、**Kong**、**Kourier**
这些网络插件都是基于 **Envoy** 这个新生的云原生服务代理，关键特性是可以**基于流量百分比进行分流**。


感兴趣的可以研究下 [https://www.servicemesher.com/envoy/intro/what_is_envoy.html](https://link.zhihu.com/?target=https%3A//www.servicemesher.com/envoy/intro/what_is_envoy.html)

![img](https://pic2.zhimg.com/80/v2-5ce93c5205ca12e27b38f4d19663325d_720w.jpg)


**3. Knative 组件**


**1. Queue-proxy**

![img](https://pic3.zhimg.com/80/v2-ce174eda8de2b505b4b366516ebb7cfa_720w.jpg)


Queue-proxy 是每个业务 pod 中都存在的 sidecar，每个发到业务pod的请求都会先经过 queue-proxy


queue-proxy 的主要作用是 **收集和限制** 业务应用的并发量，比如当一个 revision 设定了并发量为 5 ，那么 queue-proxy 会保证每次到达业务容器的请求数不会大于 5. 如果多于 5 个请求到达，queue-proxy 会将请求暂存在本地队列中。


几个端口表示如下：
• 8012， queue-proxy 代理的http端口，流量的入口都会到 8012
• 8013， http2 端口，用于grpc流量的转发
• 8022, queue-proxy 管理端口，如健康检查
• 9090， queue-proxy的监控端口，暴露指标供 autoscaler 采集，用于kpa扩缩容
• 9091， prometheus 应用监控指标（请求数，响应时长等）
• USER_PORT， 是用户配置的容器端口，即业务实际暴露的服务端口，ksvc container port 配置的


**2. Autoscaller**

![img](https://pic3.zhimg.com/80/v2-437d0165873089cdda7815fc66659662_720w.jpg)


AutoScaller 主要是 Knative 的扩缩容实现，通过request指标来决定是否扩缩容实例，指标来源有两个：
• 通过获取每个 pod queue-proxy 中的指标
• Activator 通过 websocket 主动上报




**扩缩容算法如下：**


autoscaler 是基于每个 Pod（并发）的运行中请求的平均数量。系统的默认目标并发性为 100，但是我们为服务使用了 10。我们为服务加载了 50 个并发请求，因此自动缩放器创建了 5 个容器（ 50 个并发请求/目标 10 = 5 个容器）。
算法中有两种模式，分别是 panic 和 stable 模式，一个是短时间，一个是长时间，为了解决短时间内请求突增的场景，需要快速扩容。


**Stable Mode**（稳定模式）
在稳定模式下，Autoscaler 根据每个pod期望的并发来调整Deployment的副本个数。根据每个pod在60秒窗口内的平均并发来计算，而不是根据现有副本个数计算，因为pod的数量增加和pod变为可服务和提供指标数据有一定时间间隔。


**Panic Mode** **（恐慌模式）**
KPA会在 **60** 秒的窗口内计算平均并发性，因此系统需要一分钟时间才能稳定在所需的并发性级别。但是，自动缩放器还会计算一个 **6秒** 的紧急窗口，如果该窗口达到目标并发性的 2 倍，它将进入紧急模式。在紧急模式下，自动缩放器在较短，更敏感的紧急窗口上运行。一旦在 60 s秒内不再满足紧急情况，autoscaler 将返回到最初的 60 秒稳定窗口。

![img](https://pic3.zhimg.com/80/v2-b711aca0be98c4a506f5ee16b644081e_720w.jpg)



**3. Activator**

![img](https://pic3.zhimg.com/80/v2-437d0165873089cdda7815fc66659662_720w.jpg)


Activator 的作用：**流量的负载和缓存，是Knative能缩容到 0 的关键**


实例为0 时（冷启动），流量会先转发到 Activator，由 Activator 通过 websocket 主动触发 Autoscaler 扩缩容。
Activator 本身的扩缩容通过 hpa 实现

```text
root@admin03.gyct:~# kubectl get hpa -n knative-serving
NAME        REFERENCE              TARGETS   MINPODS   MAXPODS   REPLICAS   AGE
activator   Deployment/activator   8%/100%   1         20        1          73d
```


可以看到 Activator 默认最大可以扩缩容到 20
Activator 只在 冷启动阶段是 proxy 模式，当当实例足够时，autoscaler 会更新 public service 的endpoints 指向 revision对应的pod，将请求导向真正的后端，这时候处理请求过程中 activator 不在起作用


**详细步骤如下：**

![img](https://pic3.zhimg.com/80/v2-abcd72546ba3e8940249cdbccb4bd056_720w.jpg)


冷启动时 activator 的角色

1. Kingress 收到请求（确切的是 网关收到请求后根据kingress生成的配置做转发，如istio是virtualservice）后，将请求导至 activator
2. activator 将请求保存在缓存中
3. Activator 触发 autoscaller， 触发过程中其中做了两件事：
   a. Activator 携带了第2步缓存的请求信息到 autoscaler；
   b. 触发信号促使 autoscaller 立即做出扩容决定，而不是等下个扩容周期；
4. 考虑到有请求需要处理，但是目前实例是0，autoscaller 决定扩容出一个实例，于是设置一个新的 scale 目标
5. activator 在等待 autoscaler 和 Serving 准备工作的时候，activator 会去轮询 Serving 查看实例时候准备完毕
6. Serving 调用k8s 生成k8s 实例（deploy，pod）
7. 当实例可用时，Activator 将请求从缓存中 proxy 到实例
8. Activator 中的 proxy 模块 将请求代理到实例
9. Activator 中的 proxy 模块 同样将 response 返回到 kingress

**4. 扩缩容原理**
**1.** **正常扩缩容场景（非 0 实例）**

![img](https://pic3.zhimg.com/80/v2-d088b7ed703dae0541cfa1ade525bf62_720w.jpg)


**稳定状态下的工作流程如下：**

1. 请求通过 ingress 路由到 public service ，此时 public service 对应的 endpoints 是 revision 对应的 pod
2. Autoscaler 会定期通过 queue-proxy 获取 revision 活跃实例的指标，并不断调整 revision 实例。
3. 请求打到系统时， Autoscaler 会根据当前最新的请求指标确定扩缩容比例。
4. SKS 模式是 serve, 它会监控 private service 的状态，保持 public service 的 endpoints 与 private service 一致 。

**2. 缩容到 0 的场景**

![img](https://pic2.zhimg.com/80/v2-08cb758b0842c6e40acaff7c767a1ad9_720w.jpg)


**缩容到零过程的工作流程如下：**

1. AutoScaler 通过 queue-proxy 获取 revision 实例的请求指标
2. 一旦系统中某个 revision 不再接收到请求（此时 Activator 和 queue-proxy 收到的请求数都为 0）
3. AutoScaler 会通过 Decider 确定出当前所需的实例数为 0，通过 PodAutoscaler 修改 revision 对应 Deployment 的 实例数
4. 在系统删掉 revision 最后一个 Pod 之前，会先将 Activator 加到 数据流路径中（请求先到 Activator）。Autoscaler 触发 SKS 变为 proxy 模式，此时 SKS 的 public service 后端的endpoints 变为 Activator 的IP，所有的流量都直接导到 Activator
5. 此时，如果在冷却窗口时间内依然没有流量进来，那么最后一个 Pod 才会真正缩容到零。

**3.** **从 0 启动的场景**

![img](https://pic3.zhimg.com/80/v2-e6e19458962bc2bdf5f750eb3a5976be_720w.jpg)

**冷启动过程的工作流程如下：**
当 revision 缩容到零之后，此时如果有请求进来，则系统需要扩容。因为 SKS 在 proxy 模式，流量会直接请求到 Activator 。Activator 会统计请求量并将 指标主动上报到 Autoscaler， 同时 Activator 会缓存请求，并 watch SKS 的 private service， 直到 private service 对应的endpoints产生。
Autoscaler 收到 Activator 发送的指标后，会立即启动扩容的逻辑。这个过程的得出的结论是至少一个Pod要被创造出来，AutoScaler 会修改 revision 对应 Deployment 的副本数为为N（N>0）,AutoScaler 同时会将 SKS 的状态置为 serve 模式，流量会直接到导到 revision 对应的 pod上。
Activator 最终会监测到 private service 对应的endpoints的产生，并对 endpoints 进行健康检查。健康检查通过后，Activator 会将之前缓存的请求转发到健康的实例上。
最终 revison 完成了冷启动（从零扩容）。



## 7.9 [理解serverless无服务架构原理](https://baijiahao.baidu.com/s?id=1712564587058793457&wfr=spider&for=pc)

一：什么是serverless无服务？

serverless中文的含义是 "无服务器"，但是它真正的含义是开发者再也不用过多考虑服务器的问题，但是并不代表完全去除服务器，而是我们依靠第三方资源服务器后端，比如使用 Amazon Web Services(AWS) Lambda. 计算服务来执行代码，那么Serverless架构分为 Backend as a Service(BaaS) 和 Functions as a Service(FaaS) 两种技术，Serverless 它是由开发者实现的服务端逻辑运行在无状态的计算容器中，它是由事件触发，完全被第三方管理的。

**什么是BaaS?**

Baas 的英文翻译成中文的含义：后端即服务，它的应用架构由大量第三方云服务器和API组成的，使应用中关于服务器的逻辑和状态都由服务提供方来管理的。比如我们的典型的单页应用SPA和移动APP富客户端应用，前后端交互主要是以RestAPI调用为主。只需要调用服务提供方的API即可完成相应的功能，比如常见的身份验证，云端数据/文件存储，消息推送，应用数据分析等。

**什么是FaaS?**

FaaS可以被叫做：函数即服务。开发者可以直接将服务业务逻辑代码部署，运行在第三方提供的无状态计算容器中，开发者只需要编写业务代码即可，无需关注服务器，并且代码的执行它是由事件触发的。其中AWS Lambda是目前最佳的FaaS实现之一。

Serverless的应用架构是将BaaS和FaaS组合在一起的应用，用户只需要关注应用的业务逻辑代码，编写函数为粒度将其运行在FaaS平台上，并且和BaaS第三方服务整合在一起，最后就搭建了一个完整的系统。整个系统过程中完全无需关注服务器。



<https://www.zhihu.com/question/499702791/answer/2229624554>

所以，这里就可以看到，云原生是一个更加泛的概念，他表示的是在云计算领中的一部分，哪一部分呢？是在云且原生的部分。那么Serverless架构呢，它本身也是因云而生，成长在云。

所以，你可以认为Serverless架构是一种技术架构，而云原生是一种泛概念，他的外面还有云计算。



# 8. 中间件

## 8.1 [Zookeeper](https://zhuanlan.zhihu.com/p/62526102)

- ZooKeeper主要**服务于分布式系统**，可以用ZooKeeper来做：统一配置管理、统一命名服务、分布式锁、集群管理。
- 使用分布式系统就无法避免对节点管理的问题(需要实时感知节点的状态、对节点进行统一管理等等)，而由于这些问题处理起来可能相对麻烦和提高了系统的复杂性，ZooKeeper作为一个能够**通用**解决这些问题的中间件就应运而生了。

从上面我们可以知道，可以用ZooKeeper来做：统一配置管理、统一命名服务、分布式锁、集群管理。

- 这里我们**先**不管`统一配置管理、统一命名服务、分布式锁、集群管理`每个具体的含义(后面会讲)

那为什么ZooKeeper可以干那么多事？来看看ZooKeeper究竟是何方神物，在Wiki中其实也有提到：

> ZooKeeper nodes store their data in a hierarchical name space, much like a file system or a [tree](https://link.zhihu.com/?target=https%3A//en.wikipedia.org/wiki/Tree_(data_structure)) data structure

ZooKeeper的数据结构，跟Unix文件系统非常类似，可以看做是一颗**树**，每个节点叫做**ZNode**。每一个节点可以通过**路径**来标识，结构图如下：



![img](https://pic4.zhimg.com/80/v2-787d82f1f9b7a9a1db8f08aa932058fb_720w.jpg)



那ZooKeeper这颗"树"有什么特点呢？？ZooKeeper的节点我们称之为**Znode**，Znode分为**两种**类型：

- **短暂/临时(Ephemeral)**：当客户端和服务端断开连接后，所创建的Znode(节点)**会自动删除**
- **持久(Persistent)**：当客户端和服务端断开连接后，所创建的Znode(节点)**不会删除**

> ZooKeeper和Redis一样，也是C/S结构(分成客户端和服务端)



![img](https://pic3.zhimg.com/80/v2-ddc7da9fd715c0906f38c5989e11118e_720w.jpg)



### 2.1 监听器

在上面我们已经简单知道了ZooKeeper的数据结构了，ZooKeeper还配合了**监听器**才能够做那么多事的。

**常见**的监听场景有以下两项：

- 监听Znode节点的**数据变化**
- 监听子节点的**增减变化**



![img](https://pic3.zhimg.com/80/v2-d88bb8f393873725519b40f2f1e53246_720w.jpg)





![img](https://pic1.zhimg.com/80/v2-8250e2c7d6873378a8780d9c03f46e9c_720w.jpg)



没错，通过**监听+Znode节点(持久/短暂[临时])**，ZooKeeper就可以玩出这么多花样了。

下面我们来看看用ZooKeeper怎么来做：统一配置管理、统一命名服务、分布式锁、集群管理。

### 3.1 统一配置管理

比如我们现在有三个系统A、B、C，他们有三份配置，分别是`ASystem.yml、BSystem.yml、CSystem.yml`，然后，这三份配置又非常类似，很多的配置项几乎都一样。

- 此时，如果我们要改变其中一份配置项的信息，很可能其他两份都要改。并且，改变了配置项的信息**很可能就要重启系统**

于是，我们希望把`ASystem.yml、BSystem.yml、CSystem.yml`相同的配置项抽取出来成一份**公用**的配置`common.yml`，并且即便`common.yml`改了，也不需要系统A、B、C重启。



![img](https://pic1.zhimg.com/80/v2-2dfe4bb28b448c3a623deeda2cabecd8_720w.jpg)



做法：我们可以将`common.yml`这份配置放在ZooKeeper的Znode节点中，系统A、B、C监听着这个Znode节点有无变更，如果变更了，**及时**响应。



![img](https://pic4.zhimg.com/80/v2-40a7b398992105e1b278fca39ba1338b_720w.jpg)



参考资料：

- 基于zookeeper实现统一配置管理

- - [https://blog.csdn.net/u011320740/article/details/78742625](https://link.zhihu.com/?target=https%3A//blog.csdn.net/u011320740/article/details/78742625)

### 3.2 统一命名服务

统一命名服务的理解其实跟**域名**一样，是我们为这某一部分的资源给它**取一个名字**，别人通过这个名字就可以拿到对应的资源。

比如说，现在我有一个域名`www.java3y.com`，但我这个域名下有多台机器：

- 192.168.1.1
- 192.168.1.2
- 192.168.1.3
- 192.168.1.4

别人访问`www.java3y.com`即可访问到我的机器，而不是通过IP去访问。



![img](https://pic3.zhimg.com/80/v2-4b86e886479dc91b9527f46fe125e45a_720w.jpg)



### 3.3 分布式锁([Zookeeper实现分布式锁](https://blog.csdn.net/kongmin_123/article/details/82081953?utm_medium=distribute.pc_feed_404.none-task-blog-2~default~BlogCommendFromBaidu~Rate-2-82081953-blog-null.pc_404_mixedpudn&depth_1-utm_source=distribute.pc_feed_404.none-task-blog-2~default~BlogCommendFromBaidu~Rate-2-82081953-blog-null.pc_404_mixedpud))

锁的概念在这我就不说了，如果对锁概念还不太了解的同学，可参考下面的文章

- [Java锁？分布式锁？乐观锁？行锁？](https://link.zhihu.com/?target=https%3A//mp.weixin.qq.com/s%3F__biz%3DMzI4Njg5MDA5NA%3D%3D%26mid%3D2247484989%26idx%3D1%26sn%3D7beaa0db8b29cc8758c7846fe04dfbd2%26chksm%3Debd7473cdca0ce2a7aea8e6e2a22a5c183b8be3f1cdc93f8d7c3842a560eb5668071cebe5e37%26token%3D948022247%26lang%3Dzh_CN%23rd)

我们可以使用ZooKeeper来实现分布式锁，那是怎么做的呢？？下面来看看：

系统A、B、C都去访问`/locks`节点



![img](https://pic4.zhimg.com/80/v2-4d762a6ece13303b72f33b46a15f0097_720w.jpg)



访问的时候会创建**带顺序号的临时/短暂**(`EPHEMERAL_SEQUENTIAL`)节点，比如，系统A创建了`id_000000`节点，系统B创建了`id_000002`节点，系统C创建了`id_000001`节点。



![img](https://pic3.zhimg.com/80/v2-338b221850de334723018c9164804576_720w.jpg)



接着，拿到`/locks`节点下的所有子节点(id_000000,id_000001,id_000002)，**判断自己创建的是不是最小的那个节点**

- 如果是，则拿到锁。

- - 释放锁：执行完操作后，把创建的节点给删掉

- 如果不是，则监听比自己要小1的节点变化

举个例子：

- 系统A拿到`/locks`节点下的所有子节点，经过比较，发现自己(`id_000000`)，是所有子节点最小的。所以得到锁
- 系统B拿到`/locks`节点下的所有子节点，经过比较，发现自己(`id_000002`)，不是所有子节点最小的。所以监听比自己小1的节点`id_000001`的状态
- 系统C拿到`/locks`节点下的所有子节点，经过比较，发现自己(`id_000001`)，不是所有子节点最小的。所以监听比自己小1的节点`id_000000`的状态
- …...
- 等到系统A执行完操作以后，将自己创建的节点删除(`id_000000`)。通过监听，系统C发现`id_000000`节点已经删除了，发现自己已经是最小的节点了，于是顺利拿到锁
- ….系统B如上

### 3.4集群状态

经过上面几个例子，我相信大家也很容易想到ZooKeeper是怎么"**感知**"节点的动态新增或者删除的了。

还是以我们三个系统A、B、C为例，在ZooKeeper中创建**临时节点**即可：



![img](https://pic1.zhimg.com/80/v2-64f633e7f829b5daeedf5e4d116972bc_720w.jpg)



只要系统A挂了，那`/groupMember/A`这个节点就会删除，通过**监听**`groupMember`下的子节点，系统B和C就能够感知到系统A已经挂了。(新增也是同理)

除了能够感知节点的上下线变化，ZooKeeper还可以实现**动态选举Master**的功能。(如果集群是主从架构模式下)

原理也很简单，如果想要实现动态选举Master的功能，Znode节点的类型是带**顺序号的临时节点**(`EPHEMERAL_SEQUENTIAL`)就好了。

- Zookeeper会每次选举最小编号的作为Master，如果Master挂了，自然对应的Znode节点就会删除。然后让**新的最小编号作为Master**，这样就可以实现动态选举的功能了。



### 四，zookeeper如何实现分布式锁：

1. 利用节点名称的唯一性来实现共享锁

> 算法思路: 利用名称唯一性，加锁操作时，只需要所有客户端一起创建/test/Lock节点，只有一个创建成功，成功者获得锁。解锁时，只需删除/test/Lock节点，其余客户端再次进入竞争创建节点，直到所有客户端都获得锁。
>
> 该共享锁实现很符合我们通常多个线程去竞争锁的概念，利用节点名称唯一性的做法简明、可靠。
>
> 由上述算法容易看出，由于客户端会同时收到/test/Lock被删除的通知，重新进入竞争创建节点，故存在"惊群现象"。

2.利用临时顺序节点实现共享锁

> 算法思路：对于加锁操作，可以让所有客户端都去/lock目录下创建临时顺序节点，如果创建的客户端发现自身创建节点序列号是/lock/目录下最小的节点，则获得锁。否则，监视比自己创建节点的序列号小的节点（比自己创建的节点小的最大节点），进入等待。对于解锁操作，只需要将自身创建的节点删除即可。

## 8.2 [用大白话给你解释Zookeeper的选举机制](https://baijiahao.baidu.com/s?id=1685254558927619982&wfr=spider&for=pc)

**选举机制中涉及到的核心概念**

敲黑板了，这些概念是面试必考的。

**（1）Server id（或sid）：服务器ID**

比如有三台服务器，编号分别是1,2,3。编号越大在选择算法中的权重越大，比如初始化启动时就是根据服务器ID进行比较。

**（2）Zxid：事务ID**

服务器中存放的数据的事务ID，值越大说明数据越新，在选举算法中数据越新权重越大。

**（3）Epoch：逻辑时钟**

也叫投票的次数，同一轮投票过程中的逻辑时钟值是相同的，每投完一次票这个数据就会增加。

**（4）Server状态：选举状态**

LOOKING，竞选状态。

FOLLOWING，随从状态，同步leader状态，参与投票。

OBSERVING，观察状态,同步leader状态，不参与投票。

LEADING，领导者状态。

**总结**

（1）Zookeeper 选举会发生在服务器初始状态和运行状态下。

（2）初始状态下会根据服务器sid的编号对比，编号越大权值越大，投票过半数即可选出Leader。

（3）Leader 故障会触发新一轮选举，zxid 代表数据越新，权值也就越大。

（4）在运行期选举还可能会遇到脑裂的情况，大家可以自行学习。



[可能是全网把 ZooKeeper 概念讲的最清楚的一篇文章 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/44731983)

**6.2 ZAB 协议介绍**

**ZAB（ZooKeeper Atomic Broadcast 原子广播） 协议是为分布式协调服务 ZooKeeper 专门设计的一种支持崩溃恢复的原子广播协议。 在 ZooKeeper 中，主要依赖 ZAB 协议来实现分布式数据一致性，基于该协议，ZooKeeper 实现了一种主备模式的系统架构来保持集群中各个副本之间的数据一致性。**

**6.3 ZAB 协议两种基本的模式：崩溃恢复和消息广播**

ZAB协议包括两种基本的模式，分别是 **崩溃恢复和消息广播**。当整个服务框架在启动过程中，或是当 Leader 服务器出现网络中断、崩溃退出与重启等异常情况时，ZAB 协议就会进入恢复模式并选举产生新的Leader服务器。当选举产生了新的 Leader 服务器，同时集群中已经有过半的机器与该Leader服务器完成了状态同步之后，ZAB协议就会退出恢复模式。其中，**所谓的状态同步是指数据同步，用来保证集群中存在过半的机器能够和Leader服务器的数据状态保持一致**。

**当集群中已经有过半的Follower服务器完成了和Leader服务器的状态同步，那么整个服务框架就可以进入消息广播模式了。** 当一台同样遵守ZAB协议的服务器启动后加人到集群中时，如果此时集群中已经存在一个Leader服务器在负责进行消息广播，那么新加人的服务器就会自觉地进人数据恢复模式：找到Leader所在的服务器，并与其进行数据同步，然后一起参与到消息广播流程中去。正如上文介绍中所说的，ZooKeeper设计成只允许唯一的一个Leader服务器来进行事务请求的处理。Leader服务器在接收到客户端的事务请求后，会生成对应的事务提案并发起一轮广播协议；而如果集群中的其他机器接收到客户端的事务请求，那么这些非Leader服务器会首先将这个事务请求转发给Leader服务器。

## 8.3 [Zookeeper的选举算法和脑裂问题](https://www.cnblogs.com/satire/p/15087403.html)

## 8.4 [用redis实现分布式锁](https://blog.csdn.net/mlwsmqq/article/details/127723729)

1、什么是分布式锁

​	分布式锁是控制分布式系统之间同步访问共享资源的一种方式。

​    在分布式系统中，常常需要协调他们的动作，若不同的系统或是同一个系统的不同主机之间共享了一个或一组资源，那么访问这些资源的时候，往往需要互斥来防止彼此干扰来保证一致性，这个时候，便需要使用到分布式锁。

2、分布式锁应具备的条件        

* 在分布式系统环境下，一段代码在同一时间只能被一个机器的一个线程执行
* 高可用的获取锁与释放锁
* 高性能的获取锁与释放锁
* 具备可重入特性（一个线程多次获取同一把锁）
* 具备锁失效机制，即自动解锁，防止死锁
* 具备非阻塞特性，即没有获取到锁将直接返回获取锁失败

3、为什么使用分布式锁
        提起synchronized和Lock想必大家都不陌生，可以做到线程间的同步，但仅限于单机应用，在分布式集群系统中用来协调共享资源的时候肯定是不行的；例如下单减库存的操作，使用synchronized进行加锁，部署三台服务，若此时商品库存只有一个，同时刻有三个下单请求分别到三台服务上处理，这时三个请求都能抢到锁去下单减库存，就很可能出现超卖的情况，使用分布式锁便可避免此问题发生。

4、SETNX介绍
    Redis实现分布式锁的核心便在于SETNX命令，它是SET if Not eXists的缩写，如果键不存在，则将键设置为给定值，在这种情况下，它等于SET；当键已存在时，不执行任何操作；成功时返回1，失败返回0

​	使用示例：两次插入相同键不同值，第一次返回成功，第二次返回失败：  ![](https://img-blog.csdnimg.cn/f0859a54e3b449bf85bfbe30f8424fbf.png)

​    也可使用set命令实现跟SETNX一样的效果，还能设置过期时间：![](https://img-blog.csdnimg.cn/fc9a6419e1a94092af95c217397cff68.png)

set命令介绍：

    SET key value [EX seconds] [PX milliseconds] [NX|XX]
    生存时间（TTL，以秒为单位）
    Redis 2.6.12 版本开始:(等同SETNX 、 SETEX 和 PSETEX)
    EX second ：设置键的过期时间为 second 秒，SET key value EX second 效果等同于 SETEX key second value 。
    PX millisecond ：设置键的过期时间为millisecond毫秒，SET key value PX millisecond 效果等同于 PSETEX key millisecond value 。
    NX ：只在键不存在时，才对键进行设置操作，SET key value NX 效果等同于 SETNX key value 。
    XX ：只在键已经存在时，才对键进行设置操作。
5、分布式锁实现

```java
@Api(tags = "Redis")
@RestController
@RequestMapping("/testRedis")
@Slf4j
public class TestRedisController {
 
	private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNamePrefix("shouhu-").setDaemon(true).build();
	private static final ScheduledExecutorService daemonPool = Executors.newScheduledThreadPool(5,THREAD_FACTORY);
 
	@Resource
	private RedisTemplate<String ,Object> redisTemplate;
 
	@GetMapping("/testSetNX")
	@ApiOperation("SETNX")
	public ResultVO<Object> testSetNX(@RequestParam Long goodsId){
		String key = "lock_" + goodsId;
		String value = UUID.randomUUID().toString();
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		ScheduledFuture<?> scheduledFuture = null;
		try {
			// 加锁
			Boolean ifAbsent = valueOperations.setIfAbsent(key, value, 30, TimeUnit.SECONDS);
			log.info("加锁{}返回值：{}",key,ifAbsent);
			if ((null==ifAbsent) || (!ifAbsent)){
				log.info("加锁失败，请稍后重试！");
				return ResultUtils.error("加锁失败，请稍后重试！");
			}
			// 模拟看门狗逻辑
			AtomicInteger count = new AtomicInteger(1);
			scheduledFuture = daemonPool.scheduleWithFixedDelay(() -> {
				log.info("看门狗第：{}次执行开始", count.get());
				Object cache = redisTemplate.opsForValue().get(key);
				if (Objects.nonNull(cache) && (value.equals(cache.toString()))) {
					// 重新设置有效时间为30秒
					redisTemplate.expire(key, 30, TimeUnit.SECONDS);
					log.info("看门狗第：{}次执行结束，有效时间为：{}", count.get(), redisTemplate.getExpire(key));
				}else {
					log.info("看门狗执行第：{}次异常：key：{} 期望值：{} 实际值：{}",count.get(), key, value, cache);
				}
				count.incrementAndGet();
			}, 10, 10, TimeUnit.SECONDS);
			// 执行业务逻辑
			TimeUnit.SECONDS.sleep(5);
			log.info("业务逻辑执行结束");
		}catch (Exception e){
			log.error("testSetNX exception:",e);
			return ResultUtils.sysError();
		}finally {
			// 释放锁，判断是否是当前线程加的锁
			String delVal = valueOperations.get(key).toString();
			if (value.equals(delVal)){
				Boolean delete = redisTemplate.delete(key);
				log.info("释放{}锁结果：{}",key,delete);
				// 关闭看门狗线程
				if (Objects.nonNull(scheduledFuture)){
					boolean cancel = scheduledFuture.cancel(true);
					log.info("关闭看门狗结果：{}",cancel);
				}
			}else {
				log.info("不予释放，key：{} value：{} delVal：{}",key,value,delVal);
			}
		}
		return ResultUtils.success("success");
	}
}
```

上面是最终实现，其中有几个需要注意的地方：

（1）防止解锁失败：如拿到锁后执行业务逻辑时一旦出现异常就无法释放锁，解决这个问题只需将释放锁的逻辑放入finally代码块中即可，无论是否有异常都会释放锁

（2）设置锁的有效期：虽然将释放锁的逻辑放在finally代码块中，但并不能达到锁失效机制要求的目标，如拿到锁的线程在执行业务过程中遇到服务重启、宕机等情况无法释放锁，锁便会一直存在，导致其它线程无法获取到那问题就大了；解决这个问题我们可以给锁设置过期时间，即便出现上述问题超时也能自动释放锁，不影响其它请求往下执行，那来看看下面的写法是否可行：

Boolean ifAbsent = valueOperations.setIfAbsent(key, value);
redisTemplate.expire(key,30,TimeUnit.SECONDS);
 这样可以实现设置锁的过期时间，但是加锁和设置过期时间不是原子操作，在加锁成功之后，即将执行设置过期时间的时候系统发生崩溃还是会死锁；其实实现原子性有现成的接口，如下：

Boolean ifAbsent = valueOperations.setIfAbsent(key, value, 30, TimeUnit.SECONDS);
（3）防止误删锁：若锁的过期时间为10s，A线程抢到锁执行业务逻辑但执行了12s，在第10s时锁过期自动删除，B线程立马拿到锁执行业务，到第12s时A线程执行完去释放锁，但锁已经不是A的，A线程把B线程的锁释放了，那B线程不就无锁裸奔了，所以我们可以在加锁的时候把值设置为唯一的，如UUID、雪花算法等方式，释放锁时获取锁的值判断是不是当前线程设置的值，如果是再去删除 

（4）Watch Dog机制：也叫看门狗，旨在延长锁的过期时间；为什么要这么做呢？比如把锁的过期时间设为10秒，但拿到锁的线程要执行20秒才结束，锁超时自动释放其它线程便能获取到，这是不被允许的，所以看门狗就闪亮登场了；它的大概流程是在加锁成功后启动一个监控线程，每隔1/3的锁的过期时间就去重置锁过期时间，比如说锁设置为30秒，那就是每隔10秒判断锁是否存在，存在就去延长锁的过期时间，重新设置为30秒，业务执行结束关闭监控线程；这样就解决了业务未执行完锁被释放的问题，本文使用ScheduleThreadPool线程池模拟实现看门狗功能，每隔10秒去重置锁的过期时间。（真正的看门狗实现肯定比本文中的复杂完善很多，本文只是阐述这种思想，大家不要被带跑偏，个人练习可以，但不要在项目中使用！）

**温馨提示：本文主要阐述分布式锁的思路，代码实现上还有漏洞，如果大家需要用到分布式锁可以考虑使用Redisson或zookeeper**



# 9. 容灾

## 9.1 [同城容灾、异地容灾、 双活 数据中心、 两地三中心的区别](https://blog.csdn.net/IDCzhan/article/details/105676058)

常见的 容灾 模式可分为同城容灾、异地容灾、 双活 数据中心、 两地 三中心几种。

### 1、 同城 容灾

同城 容灾 是在同城或相近区域内 （ ≤ 200K M ）建立两个数据中心 : 一个为数据中心，负责日常生产运行 ; 另一个为灾难备份中心，负责在灾难发生后的应用系统运行。同城灾难备份的数据中心与灾难备份中心的距离比较近，通信线路质量较好，比较容易实现数据的同步 复制 ，保证高度的数据完整性和数据零丢失。同城灾难备份一般用于防范火灾、建筑物破坏、供电故障、计算机系统及人为破坏引起的灾难。

### 2、 异地 容灾

异地 容灾 主备中心之间的距离较远 （＞ 200KM ) ， 因此一般采用异步镜像，会有少量的数据丢失。异地灾难备份不仅可以防范火灾、建筑物破坏等可能遇到的风险隐患，还能够防范战争、地震、水灾等风险。由于同城灾难备份和异地灾难备份各有所长，为达到最理想的防灾效果，数据中心应考虑采用同城和异地各建立一个灾难备份中心的方式解决。
本地容灾 是指在本地机房建立容灾系统，日常情况下可同时分担业务及管理系统的运行，并可切换运行；灾难情况下可在基本不丢失数据的情况下进行灾备应急切换，保持业务连续运行。与异地灾备模式相比较，本地双中心具有投资成本低、建设速度快、运维管理相对简单、可靠性更高等优点；异地灾备中心是指在异地建立一个备份的灾备中心，用于双中心的数据备份，当双中心出现自然灾害等原因而发生故障时，异地灾备中心可以用备份数据进行业务的恢复。

本地机房的容灾主要是用于防范生产服务器发生的故障，异地灾备中心用于防范大规模区域性灾难。本地机房的容灾由于其与生产中心处于同一个机房，可通过局域网进行连接，因此数据复制和应用切换比较容易实现，可实现生产与灾备服务器之间数据的实时复制和应用的快速切换。异地灾备中心由于其与生产中心不在同一机房，灾备端与生产端连接的网络线路带宽和质量存在一定的限制，应用系统的切换也需要一定的时间，因此异地灾备中心可以实现在业务限定的时间内进行恢复和可容忍丢失范围内的数据恢复。

### 3、 两地 三中心

结合近年国内出现的大范围自然灾害，以同城双中心加异地灾备中心的 “两地三中心”的灾备模式也随之出现，这一方案兼具高可用性和灾难备份的能力。
同城双中心 是指在同城或邻近城市建立两个可独立承担关键系统运行的数据中心，双中心具备基本等同的业务处理能力并通过高速链路实时同步数据，日常情况下可同时分担业务及管理系统的运行，并可切换运行；灾难情况下可在基本不丢失数据的情况下进行灾备应急切换，保持业务连续运行。与异地灾备模式相比较，同城双中心具有投资成本低、建设速度快、运维管理相对简单、可靠性更高等优点。
异地灾备中心 是指在异地的城市建立一个备份的灾备中心，用于双中心的数据备份，当双中心出现自然灾害等原因而发生故障时，异地灾备中心可以用备份数据进行业务的恢复。

两地三中心 ： 是指 同城双中心 加 异地灾备 一种商用容灾备份解决方案；
两地 是指同城、异地；
三中心 是指生产中心、同城容灾中心、异地容灾中心。（ 生产中心、同城灾备中心、异地 灾备 中心 ）

### 4、 双活 数据中心

所谓 “ 双活 ” 或 “ 多活 ” 数据中心，区别于 传统 数据中心 和 灾备中心的模式，前者 多个 或两个数据中心都处于运行当中， 运行相同的应用，具备同样的数据，能够提供跨中心业务负载均衡运行能力，实现持续的应用可用性和灾难备份能力， 所以称为 “双活 ” 和 “ 多 活 ” ；后者是 生产 数据中心投入运行， 灾备 数据中心处在不工作状态，只有当灾难发生时，生产数据中心瘫痪，灾备中心才启动。
“ 双活 ” 数据中心最大的特点是 ： 一、充分利用资源，避免了一个数据中心常年处于闲置状态而造成浪费 ， 通过资源整合， “ 双活 ” 数据中心的服务能力是 翻 倍的 ；   二 、 “ 双活 ” 数据中心如果断了一个数据中心， 其 业务可以 迅速 切换到另外一个 正在 运行的数据中心， 切换 过程对用户来说是不可感知的。
在 “ 双活 ” 的模式中，两地数据中心同时接纳交易，技术难度很大，需要更改众多底层程序 ， 因而在现实中，国内还没有 真正 “ 双活 ” 数据中心 的成功 应用 案例。

数据容灾技术选择度量标准
在构建 容灾 系统时，首先考虑的是结合实际情况选择合理的数据复制技术。 在 选择合理的数据复制技术时主要考虑以下因素：
Ø  灾难承受程度 ：明确计算机系统需要承受的灾难类型，系统故障、通信故障、长时间断电、火灾及地震等各种意外情况所采取的备份、保护方案不尽相同。
Ø  业务影响程度 ：必须明确当计算机系统发生意外无法工作时，导致业务停顿所造成的损失程度，也就是定义用户对于计算机系统发生故障的最大容忍时间。这是设计备份方案的重要技术指标。
Ø  数据保护程度 ：是否要求数据库恢复所有提交的交易 ， 并且要求实时同步 ，保证 数据的连续性和一致性， 这是 备份方案复杂程度的重要依据。
UCACHE灾备云与本地服务中心建立的灾备中心，数据通过G口网络实时同步备份至灾备中心，可以实现实时备份，或是定时备份，当本地灾备中专心出现服务器故障或者数据丢失时，可快速从云平台将数据恢复，同时云平台也可将数属据恢复至本地服务中心。未来可平滑的升级成灾备中心与云虚拟机之间的远程异地高可用保护。

# 10. 系统设计

## 10.1 [秒杀系统设计思路](https://blog.csdn.net/root1596335858/article/details/113406469)

### **一：秒杀应该考虑哪些问题**

#### 1.1：超卖问题

分析秒杀的业务场景，最重要的有一点就是超卖问题，假如备货只有100个，但是最终超卖了200，一般来讲秒杀系统的价格都比较低，如果超卖将严重影响公司的财产利益，因此首当其冲的就是**解决商品的超卖问题**。

####  1.2：高并发

秒杀具有时间短、并发量大的特点，秒杀持续时间只有几分钟，而一般公司都为了制造轰动效应，会以极低的价格来吸引用户，因此参与抢购的用户会非常的多。

短时间内会有大量请求涌进来，后端如何**防止并发过高造成缓存击穿或者失效**，击垮数据库都是需要考虑的问题。

####  1.3：接口防刷

现在的秒杀大多都会出来针对秒杀对应的软件，这类软件会模拟不断向后台服务器发起请求，一秒几百次都是很常见的，如何**防止**这类软件的**重复无效请求**，防止不断发起的请求也是需要我们针对性考虑的

#### 1.4：秒杀url

对于普通用户来讲，看到的只是一个比较简单的秒杀页面，在未达到规定时间，秒杀按钮是灰色的，一旦到达规定时间，灰色按钮变成可点击状态。这部分是针对小白用户的

如果是稍微有点电脑功底的用户，会通过F12看浏览器的network看到秒杀的url，通过特定软件去请求也可以实现秒杀。

或者提前知道秒杀url的人，一请求就直接实现秒杀了。这个问题我们需要考虑解决。

#### 1.5：数据库设计

秒杀有把我们服务器击垮的风险，如果让它与我们的其他业务使用在同一个数据库中，耦合在一起，就很有可能牵连和影响其他的业务。

如何防止这类问题发生，就算秒杀发生了宕机、服务器卡死问题，也应该让他尽量不影响线上正常进行的业务。

#### 1.6：大量请求问题

按照1.2的考虑，就算使用缓存还是不足以应对短时间的高并发的流量的冲击。如何承载这样巨大的访问量，同时提供稳定低时延的服务保证，是需要面对的一大挑战。

我们来算一笔账，假如使用的是redis缓存，单台redis服务器可承受的QPS大概是4W左右，如果一个秒杀吸引的用户量足够多的话，单QPS可能达到几十万，单体redis还是不足以支撑如此巨大的请求量。缓存会被击穿，直接渗透到DB，从而击垮mysql。后台会将会大量报错。

### **二：秒杀系统的设计和技术方案**

#### 2.1：秒杀系统数据库设计

针对1.5提出的秒杀数据库的问题，因此应该单独设计一个秒杀数据库，防止因为秒杀活动的高并发访问拖垮整个网站。

这里只需要两张表，一张是秒杀订单表，一张是秒杀货品表

![img](https://img-blog.csdnimg.cn/img_convert/9d9e7939ecc4a05edcb6ca3b04c8ca76.png) ![img](https://img-blog.csdnimg.cn/img_convert/8a159b77456a075668aacce689adbc25.png)

其实应该还有几张表，商品表：可以关联goods_id查到具体的商品信息，商品图像、名称、平时价格、秒杀价格等，还有用户表：根据用户user_id可以查询到用户昵称、用户手机号，收货地址等其他额外信息，这个具体就不给出实例了。

#### 2.2：秒杀url的设计

为了避免有程序访问经验的人通过下单页面url直接访问后台接口来秒杀货品，我们需要将秒杀的url实现动态化，即使是开发整个系统的人都无法在秒杀开始前知道秒杀的url。

具体的做法就是通过md5加密一串随机字符作为秒杀的url，然后前端访问后台获取具体的url，后台校验通过之后才可以继续秒杀。

#### 2.3：秒杀页面静态化

将商品的描述、参数、成交记录、图像、评价等全部写入到一个静态页面，用户请求不需要通过访问后端服务器，不需要经过数据库，直接在前台客户端生成，这样可以最大可能的减少服务器的压力。

具体的方法可以使用freemarker模板技术，建立网页模板，填充数据，然后渲染网页。

#### 2.4：单体redis升级为集群redis

秒杀是一个读多写少的场景，使用redis做缓存再合适不过。不过考虑到缓存击穿问题，我们应该构建redis集群，采用哨兵模式，可以提升redis的性能和可用性。

#### 2.5：使用nginx

nginx是一个高性能web服务器，它的并发能力可以达到几万，而tomcat只有几百。通过nginx映射客户端请求，再分发到后台tomcat服务器集群中可以大大提升并发能力。

#### 2.6：精简sql

典型的一个场景是在进行扣减库存的时候，传统的做法是先查询库存，再去update。这样的话需要两个sql，而实际上一个sql我们就可以完成的。

可以用这样的做法：

update miaosha_goods set stock =stock-1 where goos_id ={#goods_id} and version = #{version} and sock>0;

这样的话，就可以保证库存不会超卖并且一次更新库存,还有注意一点这里使用了版本号的乐观锁，相比较悲观锁，它的性能较好。

#### 2.7：redis预减库存

很多请求进来，都需要后台查询库存,这是一个频繁读的场景。可以使用redis来预减库存，在秒杀开始前可以在redis设值

比如 redis.set(goodsId,100)，这里预放的库存为100可以设值为常量),每次下单成功之后，Integer stock = (Integer)redis.get(goosId); 然后判断sock的值，如果小于常量值就减去1。

不过注意当取消的时候，需要增加库存，增加库存的时候也得注意不能大于之间设定的总库存数(查询库存和扣减库存需要原子操作，此时可以借助lua脚本)下次下单再获取库存的时候，直接从redis里面查就可以了。

#### 2.8:接口限流

秒杀最终的本质是数据库的更新，但是有很多大量无效的请求，我们最终要做的就是如何把这些无效的请求过滤掉，防止渗透到数据库。

限流的话，需要入手的方面很多：

##### 2.8.1：前端限流

首先第一步就是通过前端限流，用户在秒杀按钮点击以后发起请求，那么在接下来的5秒是无法点击(通过设置按钮为disable)。这一小举措开发起来成本很小，但是很有效。

##### 2.8.2 同一个用户xx秒内重复请求直接拒绝

具体多少秒需要根据实际业务和秒杀的人数而定，一般限定为10秒。

具体的做法就是通过redis的键过期策略，首先对每个请求都从String value = redis.get(userId);

如果获取到这个value为空或者为null，表示它是有效的请求，然后放行这个请求。如果不为空表示它是重复性请求，直接丢掉这个请求。

如果有效，采用redis.setexpire(userId,value,10).value可以是任意值，一般放业务属性比较好，这个是设置以userId为key，10秒的过期时间(10秒后,key对应的值自动为null)

##### 2.8.3：令牌桶算法限流

接口限流的策略有很多，我们这里采用令牌桶算法。

令牌桶算法的基本思路是每个请求尝试获取一个令牌，后端只处理持有令牌的请求，生产令牌的速度和效率我们都可以自己限定，guava提供了RateLimter的api供我们使用。

以下做一个简单的例子，注意需要引入guava

```csharp
public class TestRateLimiter {
    public static void main(String[] args) {
        //1秒产生1个令牌
        final RateLimiter rateLimiter = RateLimiter.create(1);
        for (int i = 0; i < 10; i++) {
            //该方法会阻塞线程，直到令牌桶中能取到令牌为止才继续向下执行。
            double waitTime= rateLimiter.acquire();
            System.out.println("任务执行" + i + "等待时间" + waitTime);
        }
        System.out.println("执行结束");
    }
}
```

上面代码的思路就是通过RateLimiter来限定我们的令牌桶每秒产生1个令牌(生产的效率比较低)，循环10次去执行任务。

acquire会阻塞当前线程直到获取到令牌，也就是如果任务没有获取到令牌，会一直等待。那么请求就会卡在我们限定的时间内才可以继续往下走，这个方法返回的是线程具体等待的时间。

执行如下：

![img](https://img-blog.csdnimg.cn/img_convert/24173b43a1d3b50a97c083e021bdc798.png)

可以看到任务执行的过程中，第1个是无需等待的，因为已经在开始的第1秒生产出了令牌。

接下来的任务请求就必须等到令牌桶产生了令牌才可以继续往下执行。如果没有获取到就会阻塞(有一个停顿的过程)。

不过这个方式不太好，因为用户如果在客户端请求，如果较多的话，直接后台在生产token就会卡顿(用户体验较差)，它是不会抛弃任务的，我们需要一个更优秀的策略:**如果超过某个时间没有获取到，直接拒绝该任务**。

接下来再来个案例：



其中用到了tryAcquire方法，这个方法的主要作用是设定一个超时的时间，如果在指定的时间内**预估(注意是预估并不会真实的等待)**，如果能拿到令牌就返回true，如果拿不到就返回false。

然后我们让无效的直接跳过，这里设定每秒生产1个令牌，让每个任务尝试在0.5秒获取令牌，如果获取不到,就直接跳过这个任务(放在秒杀环境里就是直接抛弃这个请求)；

程序实际运行如下：

![img](https://img-blog.csdnimg.cn/img_convert/5454bbdb8926f552c4a561a1b847ddbf.png)

只有第1个获取到了令牌，顺利执行了，下面的基本都直接抛弃了，因为0.5秒内，令牌桶(1秒1个)来不及生产就肯定获取不到返回false了。

**这个限流策略的效率有多高呢？假如我们的并发请求是400万瞬间的请求，将令牌产生的效率设为每秒20个，每次尝试获取令牌的时间是0.05秒，那么最终测试下来的结果是，每次只会放行4个左右的请求，大量的请求会被拒绝，这就是令牌桶算法的优秀之处。**

####  2.9：异步下单

为了提升下单的效率，并且防止下单服务的失败。需要将下单这一操作进行异步处理。

最常采用的办法是使用队列，队列最显著的三个优点：**异步、削峰、解耦**。

这里可以采用rabbitmq，在后台经过了限流、库存校验之后，流入到这一步骤的就是有效请求。然后发送到队列里，队列接受消息，异步下单。

下完单，入库没有问题可以用短信通知用户秒杀成功。假如失败的话,可以采用补偿机制，重试。

#### 2.10：服务降级

假如在秒杀过程中出现了某个服务器宕机，或者服务不可用，应该做好后备工作。之前的博客里有介绍通过Hystrix进行服务熔断和降级，可以开发一个备用服务。

假如服务器真的宕机了，直接给用户一个友好的提示返回，而不是直接卡死，服务器错误等生硬的反馈。

**三：总结**

秒杀流程图：

![img](https://img-blog.csdnimg.cn/img_convert/c4f66a61b77b76c11fc72ab01deaf31a.png)

这就是我设计出来的秒杀流程图，当然不同的秒杀体量针对的技术选型都不一样，这个流程可以支撑起几十万的流量，如果是成千万破亿那就得重新设计了。比如数据库的分库分表、队列改成用kafka、redis增加集群数量等手段。

## 10.2 [详解秒杀系统](https://zhuanlan.zhihu.com/p/433618121)

**电商系统架构**

在电商领域，存在着典型的秒杀业务场景，那何谓秒杀场景呢。简单的来说就是一件商品的购买人数远远大于这件商品的库存，而且这件商品在很短的时间内就会被抢购一空。

比如每年的618、双11大促，小米新品促销等业务场景，就是典型的秒杀业务场景。

我们可以将电商系统的架构简化成下图所示。

![img](https://pic3.zhimg.com/80/v2-54e737597bf48fb0ff83b730626aa1ba_720w.jpg)

由图所示，我们可以简单的将电商系统的核心层分为：负载均衡层、应用层和持久层。接下来，我们就预估下每一层的并发量。

- 假如负载均衡层使用的是高性能的Nginx，则我们可以预估Nginx最大的并发度为：10W+，这里是以万为单位。
- 假设应用层我们使用的是Tomcat，而Tomcat的最大并发度可以预估为800左右，这里是以百为单位。
- 假设持久层的缓存使用的是Redis，数据库使用的是MySQL，MySQL的最大并发度可以预估为1000左右，以千为单位。Redis的最大并发度可以预估为5W左右，以万为单位。

所以，负载均衡层、应用层和持久层各自的并发度是不同的，那么，为了提升系统的总体并发度和缓存，我们通常可以采取哪些方案呢？

**（1）系统扩容**

系统扩容包括垂直扩容和水平扩容，增加设备和机器配置，绝大多数的场景有效。

**（2）缓存**

本地缓存或者集中式缓存，减少网络IO，基于内存读取数据。大部分场景有效。

**（3）读写分离**

采用读写分离，分而治之，增加机器的并行处理能力。

**秒杀系统的特点**

对于秒杀系统来说，我们可以从**业务和技术**两个角度来阐述其自身存在的一些特点。

**秒杀系统的业务特点**

这里，我们可以使用12306网站来举例，每年春运时，12306网站的访问量是非常大的，但是网站平时的访问量却是比较平缓的，也就是说，每年春运时节，12306网站的访问量会出现瞬时突增的现象。

再比如，小米秒杀系统，在上午10点开售商品，10点前的访问量比较平缓，10点时同样会出现并发量瞬时突增的现象。

所以，秒杀系统的流量和并发量我们可以使用下图来表示。

![img](https://pic2.zhimg.com/80/v2-ab15cb0e2e6db1020ebd10de394bd385_720w.jpg)

由图可以看出，秒杀系统的并发量存在瞬时凸峰的特点，也叫做流量突刺现象。

我们可以将秒杀系统的特点总结如下。

![img](https://pic1.zhimg.com/80/v2-9c9d33840781c8fa8190d746b47cb4d0_720w.jpg)

**（1）限时、限量、限价**

在规定的时间内进行；秒杀活动中商品的数量有限；商品的价格会远远低于原来的价格，也就是说，在秒杀活动中，商品会以远远低于原来的价格出售。

例如，秒杀活动的时间仅限于某天上午10点到10点半，商品数量只有10万件，售完为止，而且商品的价格非常低，例如：1元购等业务场景。

**限时、限量和限价可以单独存在，也可以组合存在。**

**（2）活动预热**

需要提前配置活动；活动还未开始时，用户可以查看活动的相关信息；秒杀活动开始前，对活动进行大力宣传。

**（3）持续时间短**

购买的人数数量庞大；商品会迅速售完。

在系统流量呈现上，就会出现一个突刺现象，此时的并发访问量是非常高的，大部分秒杀场景下，商品会在极短的时间内售完。

**秒杀系统的技术特点**

我们可以将秒杀系统的技术特点总结如下。

![img](https://pic3.zhimg.com/80/v2-fc37b78cc82b26618fc16d6717e16cda_720w.jpg)

**（1）瞬时并发量非常高**

大量用户会在同一时间抢购商品；瞬间并发峰值非常高。

**（2）读多写少**

系统中商品页的访问量巨大；商品的可购买数量非常少；库存的查询访问数量远远大于商品的购买数量。

在商品页中往往会加入一些限流措施，例如早期的秒杀系统商品页会加入验证码来平滑前端对系统的访问流量，近期的秒杀系统商品详情页会在用户打开页面时，提示用户登录系统。这都是对系统的访问进行限流的一些措施。

**（3）流程简单**

秒杀系统的业务流程一般比较简单；总体上来说，秒杀系统的业务流程可以概括为：下单减库存。

**秒杀三阶段**

通常，从秒杀开始到结束，往往会经历三个阶段：

- **准备阶段**：这个阶段也叫作系统预热阶段，此时会提前预热秒杀系统的业务数据，往往这个时候，用户会不断刷新秒杀页面，来查看秒杀活动是否已经开始。在一定程度上，通过用户不断刷新页面的操作，可以将一些数据存储到Redis中进行预热。
- **秒杀阶段**：这个阶段主要是秒杀活动的过程，会产生瞬时的高并发流量，对系统资源会造成巨大的冲击，所以，在秒杀阶段一定要做好系统防护。
- **结算阶段：** 完成秒杀后的数据处理工作，比如数据的一致性问题处理，异常情况处理，商品的回仓处理等。

针对这种短时间内大流量的系统来说，就不太适合使用系统扩容了，因为即使系统扩容了，也就是在很短的时间内会使用到扩容后的系统，大部分时间内，系统无需扩容即可正常访问。

那么，我们可以采取哪些方案来提升系统的秒杀性能呢？

**秒杀系统方案**

针对秒杀系统的特点，我们可以采取如下的措施来提升系统的性能。

![img](https://pic3.zhimg.com/80/v2-411727db998006b4334cea56d92b2e4a_720w.jpg)

**（1）异步解耦**

将整体流程进行拆解，核心流程通过队列方式进行控制。

**（2）限流防刷**

控制网站整体流量，提高请求的门槛，避免系统资源耗尽。

**（3）资源控制**

将整体流程中的资源调度进行控制，扬长避短。

由于应用层能够承载的并发量比缓存的并发量少很多。所以，在高并发系统中，我们可以直接使用OpenResty由负载均衡层访问缓存，避免了调用应用层的性能损耗。

大家可以到[https://openresty.org/cn/](https://link.zhihu.com/?target=https%3A//openresty.org/cn/)来了解有关OpenResty更多的知识。同时，由于秒杀系统中，商品数量比较少，我们也可以使用动态渲染技术，CDN技术来加速网站的访问性能。

如果在秒杀活动开始时，并发量太高时，我们可以将用户的请求放入队列中进行处理，并为用户弹出排队页面。

![img](https://pic3.zhimg.com/80/v2-7deacb1a880e956c7a7c1b836e75cef2_720w.jpg)

## 10.3 [指标与日志的监控告警系统设计](https://zhuanlan.zhihu.com/p/345562326)

### 2.技术选型

监控告警功能系统是基于当前市面上的开源系统做了统一集成。主要通过使用telegraf+influxdb用作虚拟机指标监控，使用prometheus+ grafana用做基础指标监控，使用fELK用做业务服务日志监控。

![img](https://pic3.zhimg.com/80/v2-c1940f6b811c7252722ad20bba6bd042_720w.jpg)

### 4.功能架构

告警系统主要分为三个模块：监控模块，展示模块与告警模块。监控告警系统总体功能架构如下图所示：

![img](https://pic3.zhimg.com/80/v2-c5686322e88e69593c499d5ab1ff1abe_720w.jpg)

监控模块主要将**基础性能指标与业务服务日志**进行收集和存储（图上基础指标收集与展示面板部分）。VMware层面的**基础性能**指标通过telegraf服务采集，上报给influxdb。GuestOS层，容器层和kubernetes **资源层性能指标**分别通过node-exporter，cadvisor和kube-state-metrics来进行采集，Prometheus通过Pull的方式对数据指标进行获取。**业务服务的报错**通常以日志的形式进行展现，比如业务服务报错打印错误堆栈信息等，监控系统主要filebeat对业务服务的日志进行采集，并上报给logstash进行日志数据的结构化处理，最终保存到Elasticsearch中。

展示模块以grafana做为监控入口，将数据进行可视化展示，同时可以在grafana上动态设置告警规则。grafana上支持多种数据源的展示，并根据不同的数据源进行聚合查询，在面板上展示具体指标的时序图。grafana根据不同维度集成不同维度的指标面板，方便运维人员对服务运行状态的检查。grafana提供统一的告警列表面板，可展示当前告警信息与告警历史记录。

对于告警信息而言，分为**业务服务告警信息和基础性能指标告警信息**。业务服务告警信息对业务服务的日志有一定的要求，需要日志格式包含跟踪号Traceid，如一次http请求调用所产生的所有相关日志都应该具有同一Traceid。这样在业务服务发生错误信息告警的时候会根据Traceid链接到Kibana界面中进行相关日志信息的全量查看。对于基础性能监控指标而言，如内存使用率在深夜激增，触发告警，但是在凌晨六点钟内存恢复为正常的状态，告警解除。我们可以在告警历史记录中将这条记录找到，根据告警触发和结束时间可以连接到指定环境进行问题排查。

告警模块是由alertmanager和alertwebhook组成的，alertmanager是Prometheus监控组件中一个开源的告警管理服务，它可以对告警信息进行**分组、静默和抑制**等操作。alertmanager负责将**告警信息转发**，可以对接短信，邮箱，webhook等。alertwebhook是自研的一个告警信息管理服务，它会持久化所有告警信息，更新告警状态。



## 10.4 [微服务之服务降级、服务熔断、服务限流](https://blog.csdn.net/dl962454/article/details/122193938?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0-122193938-blog-112211549.pc_relevant_default&spm=1001.2101.3001.4242.1&utm_relevant_index=3)

一、微服务三板斧
在开发微服务系统时我们通常会面临着高并发问题的考验，为了保证服务的可用性，通常会使用降级、限流和熔断进行处理。接下来我们介绍下这三个基本的概念：服务熔断、服务降级和服务限流，为后面讲解Alibaba的Sentinel组件打下扎实的基础。

1.1 **服务降级**
服务降级一般是指在服务器压力剧增的时候，根据实际业务使用情况以及流量，对一些服务和页面有策略的不处理或者用一种简单的方式进行处理，从而释放服务器资源的资源以保证核心业务的正常高效运行。通常原因为服务器的资源是有限的，而请求是无限的。在用户使用即并发高峰期，会影响整体服务的性能，严重的话会导致宕机，以至于某些重要服务不可用。故高峰期为了保证核心功能服务的可用性，就需要对某些服务降级处理。可以理解为舍小保大，通常处理为不让客户端等待而是立即返回一个友好的提示。

服务降级是从整个系统的负荷情况出发和考虑的，对某些负荷会比较高的情况，为了预防某些功能（业务场景）出现负荷过载或者响应慢的情况，在其内部暂时舍弃对一些非核心的接口和数据的请求，而直接返回一个提前准备好的fallback（兜底处理）错误处理信息。这样，虽然提供的是一个有损的服务，但却保证了整个系统的稳定性和可用性。

1.2 **服务熔断**
在介绍熔断机制之前，我们需要了解微服务的雪崩效应。在微服务架构中，微服务是完成一个单一的业务功能，这样做的好处是可以做到解耦，每个微服务可以独立演进。但是，一个应用可能会有多个微服务组成，微服务之间的数据交互通过远程过程调用完成。这就带来一个问题，假设微服务A调用微服务B和微服务C，微服务B和微服务C又调用其它的微服务，这就是所谓的“扇出”。如果扇出的链路上某个微服务的调用响应时间过长或者不可用，对微服务A的调用就会占用越来越多的系统资源，进而引起系统崩溃，所谓的“雪崩效应”。

熔断机制是应对雪崩效应的一种微服务链路保护机制。我们在各种场景下都会接触到熔断这两个字。高压电路中，如果某个地方的电压过高，熔断器就会熔断，对电路进行保护。同样，在微服务架构中，熔断机制也是起着类似的作用。当扇出链路的某个微服务不可用或者响应时间太长时，会进行服务的降级，进而熔断该节点微服务的调用，快速返回错误的响应信息。当检测到该节点微服务调用响应正常后，恢复调用链路。

1.3 **服务熔断和服务降级的区别**

这里主要从三个原因分开进行比较：

触发原因：服务熔断由链路上某个服务引起的，也就是说，服务熔断一般是某个服务（下游服务）故障引起的，而服务降级是从整体的负载考虑。服务熔断是应对系统服务雪崩的一种保险措施，给出的一种特殊降级措施。而服务降级则是更加宽泛的概念，主要是对系统整体资源的合理分配以应对压力。
管理目标层次：服务熔断是一个框架层次的处理，服务降级是业务层次的处理。
实现方式：服务熔断一般是自我熔断恢复，调用的降级处理是在客户端进行降级处理（编写相应的兜底方法），而服务降级相当于是在服务端进行的兜底方案控制。
总的来说：

服务熔断是服务降级的一种特殊情况，是防止服务雪崩而采取的措施。系统发生异常或者延迟或者流量太大，都会触发该服务的服务熔断措施，链路熔断，返回兜底方法。这是对局部的一种保险措施。
服务降级是对系统整体资源的合理分配。区分核心服务和非核心服务。对某个服务的访问延迟时间、异常等情况做出预估并给出兜底方法。这是一种全局性的考量，对系统整体负荷进行管理。

1.4 **服务限流**
在上述两种方式中，最终服务的确可以使用，但是访问的却是缺省的服务，比如服务熔断和服务降级最终都会调用相应的兜底方案返回，也就是当服务出问题或者影响到核心流程的性能则需要暂时屏蔽掉，待高峰或者问题解决后再打开；而有些场景并不能用熔断和降级来解决，比如稀缺资源（秒杀、抢购）、写服务（如评论、下单）、频繁的复杂查询（评论的最后几页），因此需有一种手段来限制这些场景的并发/请求量，即限流。

限流的目的是通过对并发请求进行限速或者一个时间窗口内的的请求进行限速来保护系统，一旦达到限制速率则可以拒绝服务（定向到错误页或告知资源没有了）、排队或等待（比如秒杀、评论、下单）、降级（返回兜底数据或默认数据，如商品详情页库存默认有货）。

一般开发高并发系统常见的限流有：**限制总并发数**（比如数据库连接池、线程池）、**限制瞬时并发数**（如nginx的limit_conn模块，用来限制瞬时并发连接数）、**限制时间窗口内的平均速率**（如Guava的RateLimiter、nginx的limit_req模块，限制每秒的平均速率）；其他还有如限制远程接口调用速率、限制MQ的消费速率。另外还可以**根据网络连接数、网络流量、CPU或内存负载**等来限流。

**常见的限流算法有：令牌桶、漏桶**（Sentinel采用的）。计数器也可以进行简单的限流实现。

漏桶算法(Leaky Bucket)思路很简单，水(请求)先进入到漏桶里，漏桶以一定的速度出水(接口有响应速率)，当水流入速度过大会直接溢出(访问频率超过接口响应速率)，然后就拒绝请求，可以看出漏桶算法能强行限制数据的传输速率。示意图如下：


令牌桶算法(Token Bucket)和漏桶效果一样，但方向相反，更加容易理解。随着时间流逝，系统会按恒定1/QPS时间间隔(如果QPS=100，则间隔是10ms)往桶里加入Token(想象和漏洞漏水相反，有个水龙头在不断的加水)，如果桶已经满了就不再加了。新请求来临时，会各自拿走一个Token，如果没有Token可拿了就阻塞或者拒绝服务。

令牌桶的另外一个好处是可以方便的改变速度，一旦需要提高速率,则按需提高放入桶中的令牌的速率。一般会定时(比如100毫秒)往桶中增加一定数量的令牌， 有些变种算法则实时的计算应该增加的令牌的数量。

## 10.5 [如何实现服务降级](http://c.biancheng.net/view/5550.html)

当访问量剧增，服务出现问题时，需要做一些处理，比如服务降级。服务降级就是将某些服务停掉或者不进行业务处理，释放资源来维持主要服务的功能。

某电商网站在搞活动时，活动期间压力太大，如果再进行下去，整个系统有可能挂掉，这个时候可以释放掉一些资源，将一些不那么重要的服务采取降级措施，比如登录、注册。登录服务停掉之后就不会有更多的用户抢购，同时释放了一些资源，登录、注册服务就算停掉了也不影响商品抢购。

服务降级有很多种方式，最好的方式就是利用 Docker来实现。当需要对某个服务进行降级时，直接将这个服务所有的容器停掉，需要恢复的时候重新启动就可以了。

还有就是在 API 网关层进行处理，当某个服务被降级了，前端过来的请求就直接拒绝掉，不往内部服务转发，将流量挡回去。



## 10.6 [Prometheus vs. Graphite：时序数据监控工具选择](https://blog.csdn.net/weixin_45443931/article/details/98869553)

如果想要一个能够长期保存历史数据的集群解决方案，那么Graphite可能是一个更好的选择，因为它简单而且在这方面的应用历史悠久。Graphite也有内置的数据汇总。同样的，如果您现有的基础结构已经使用了Flow entd、Gogtd或statd之类的集合工具，那么可以选择Graphite，因为Graphite与他们兼容，可以组合成为更完整的解决方案。

相反，如果是从零开始并打算使用完整的解决方案实施应用监控 (可能监控的对象包括更多的动态和多变的数据)，并且数据保留是短期的，则Prometheus可能是一个更好的选择，因为监控所需的一切都已经集成了。

Prometheus的学习曲线略长一些。然而，投入的时间将值回票价，毕竟，不必去维护单独的工具集，如数据收集和告警。

在做出选择之前，仔细评估自己的需求和当前实现的状态。Prometheus是一个完整的监控系统，内置了所有的。Graphite只是一个存储和数据可视化的框架。自己现有的系统能做什么，需要新的工具做到什么？审慎决定。

## 10.7 [多台服务器共享session的问题](https://blog.csdn.net/weixin_45593546/article/details/111298765)

**在现在的大型网站中，如何实现多台服务器中的session数据共享呢？**

当使用多台服务器架设成集群之后，我们通过[负载均衡](https://so.csdn.net/so/search?q=负载均衡&spm=1001.2101.3001.7020)的方式，同一个用户（或者ip）访问时被分配到不同的服务器上，假设在A服务器登录，如果在B服务器拿不到用户的登录信息session。这时访问到B服务器时就出现未登录情况。所以如何对于这种情况做到共享session至关重要。

以下给出一些解决方案：（来源网络以及自己的一些见解）

1.通过数据库mysql共享[session](https://so.csdn.net/so/search?q=session&spm=1001.2101.3001.7020)

a.采用一台专门的mysql服务器来存储所有的session信息。
用户访问随机的web服务器时，会去这个专门的数据库服务器check一下session的情况，以达到session同步的目的。
缺点就是：依懒性太强，mysql服务器无法工作，影响整个系统；

b.将存放session的数据表与业务的数据表放在同一个库。如果mysql做了主从，需要每一个库都需要存在这个表，并且需要数据实时同步。

缺点：用数据库来同步session，会加大数据库的负担，数据库本来就是容易产生瓶颈的地方，如果把session还放到数据库里面，无疑是雪上加霜。上面的二种方法，第一点方法较好，把放session的表独立开来，减轻了真正数据库的负担 。但是session一般的查询频率较高，放在数据库中查询性能也不是很好，不推荐使用这种方式。

2.通过cookie共享session

把用户访问页面产生的session放到cookie里面，就是以cookie为中转站。

当访问服务器A时，登录成功之后将产生的session信息存放在cookie中；当访问请求分配到服务器B时，服务器B先判断服务器有没有这个session，如果没有，在去看看客户端的cookie里面有没有这个session，如果cookie里面有，就把cookie里面的sessoin同步到web服务器B，这样就可以实现session的同步了。

缺点：cookie的安全性不高，容易伪造、客户端禁止使用cookie等都可能造成无法共享session。

3.通过服务器之间的数据同步session

使用一台作为用户的登录服务器，当用户登录成功之后，会将session写到当前服务器上，我们通过脚本或者守护进程将session同步到其他服务器上，这时当用户跳转到其他服务器，session一致，也就不用再次登录。

缺陷：速度慢，同步session有延迟性，可能导致跳转服务器之后，session未同步。而且单向同步时，登录服务器宕机，整个系统都不能正常运行。

4.通过NFS共享Session

选择一台公共的NFS服务器（Network File Server）做共享服务器，所有的Web服务器登陆的时候把session数据写到这台服务器上，那么所有的session数据其实都是保存在这台NFS服务器上的，不论用户访问那太Web服务器，都要来这台服务器获取session数据，那么就能够实现共享session数据了。

缺点：依赖性太强，如果NFS服务器down掉了，那么大家都无法工作了，当然，可以考虑多台NFS服务器同步的形式。

5.通过memcache同步session

memcache可以做分布式，如果没有这功能，他也不能用来做session同步。他可以把web服务器中的内存组合起来，成为一个"内存池"，不管是哪个服务器产生的sessoin都可以放到这个"内存池"中，其他的都可以使用。

优点：以这种方式来同步session，不会加大数据库的负担，并且安全性比用cookie大大的提高，把session放到内存里面，比从文件中读取要快很多。

缺点：memcache把内存分成很多种规格的存储块，有块就有大小，这种方式也就决定了，memcache不能完全利用内存，会产生内存碎片，如果存储块不足，还会产生内存溢出。

6.通过redis共享session

redis与memcache一样，都是将数据放在内存中。区别的是redis会周期性的把更新的数据写入磁盘或者把修改操作写入追加的记录文件，并且在此基础上实现了master-slave(主从)同步。

根据实际开发应用，一般选择使用memcache或redis方式来共享session.



## 10.8 [互联网架构（一）总体架构设计](https://blog.csdn.net/baidu_41934937/article/details/108918250)

### 总体架构设计

架构的基本手段就是分与合，先把系统打散，然后再重新组合。
分的过程是把系统拆分为各个子系统/模块/组件。拆的时候首先要解决每个组件的定位问题，然后才能划分彼此的边界，实现合理的拆分。
合就是根据最新终要求，把各个分离的组件有机的整合在一起。
拆分的结构使开发人人员能够做到业务聚焦、技术聚焦、实现开发敏捷，合的结果使系统变得柔软，可以因需而变，实现业务敏捷。

### 架构的分类

架构一般可分为**业务架构、应用架构、技术架构**

业务架构：从概念层面帮助开发人员更好的理解系统、比如业务流程、业务模块、输入输出、业务域。
应用架构：从逻辑层面帮助开发落地系统，如数据交互关系、应用形式、交互方式，使得整个系统逻辑更容易理解，比如大家熟知的SOA就属于应用架构。
技术架构：主要解决技术平台选型、如操作系统、中间件、设备、多机房、水平扩展、高可用等问题。
需要注意的是，系统架构首先都是为人服务的，系统的有序度高，应用逻辑合理、业务概念清晰是第一位。现在大家讨论更多的是技术架构，如高并发设计、分布式事务管理等，只是因为这个不需要业务上下文背景，比较好沟通。具体架构设计时，首先要关注业务架构和应用架构。

### 大型网站的架构演进

从一个电商网站开始
以电商网站为例，一个交易类型的网站，一定要具备用户（用户注册、用户管理）、商品（商品展示、商品管理）、交易（下单、支付）功能。最开始的架构应该时这样的：
以上是基于Java技术用单机构建的交易网站。这个地方要注意的是各个功能模块之间是通过JVM内部的方法调用进行交互的，而应用和数据库之间是通过jdbc进行访问。

### 单机负载警告，数据与应用分离

随着网站的开放，访问量不断增大，服务器的负载势必会持续升高，此时将数据库和应用从一台机器分到两台机器。
变化：网站从一台机器变成两台，这个变化对我们影响非常小。单机的情况下，应用采用JDBC的方式和数据库进行连接，现在数据库与应用分离，只需要在配置文件中把数据库的地址从本地改成数据库服务器的ip地址即可。

为什么这么分
从计算机的角度来考虑，一个请求的访问到最终的返回，性能瓶颈只会是：CPU、文件IO、网络IO、内存等因素。如果某个资源消耗过多，通常会造成系统的响应速度较慢，所以增加一台机器，使得数据库的IO和CPU资源独占一台机器从而增加性能。

各个资源消耗的原因

CPU： 主要是上下文切换，每个CPU同时只能执行一个线程，而CPU的调度有抢占式和轮询等。CPU在切换的过程中需要存储当前线程的执行状态并恢复要执行的线程状态。IO、锁等待等场景都会触发上下文切换，当上下文切换过多的时候会造成内核占用比较多的CPU。
文件IO： 比如频繁的日志写入，磁盘本身处理速度比较慢，都会造成IO性能问题。
网络IO： 包括内存泄露、内存溢出、内存不足。
实际不管是应用层的调优，还是硬件的升级，无非就是这几个因素的调整。

### 应用服务器复杂告警，如何让服务器走向集群

应用服务器的压力变大，根据对应用的检测结果，可以针对性能压力大的地方进行优化。可以考虑通过水平扩容及逆行优化，把单机变成集群。
应用服务器从一台变成两台，这两个应用服务器之间没有直接的交互，他们都依赖数据库堆外提供服务，那么这个时候会抛出两个问题：

最终用户对两个应用服务器访问的选择？（对于这个问题，可以采用DNS解决，也可以通过负载均设备来解决）
Session问题？

### 水平和垂直扩容

对于大型的分布式架构而言，我们一直追求一种简单、优雅的方式来应对访问量和数据量的增长。这种方式通常指的是不需要改动软件程序，仅通过硬件升级或者增加机器数就可以解决。即分布式架构下的伸缩设计。

垂直伸缩
通过升级或增加单台机器的硬件来支撑访问量以及数据量的增长。优点是技术难度低，运营和改动成本低。缺点是机器性能有瓶颈，成本大。

水平伸缩
通过增加机器来支撑访问量以及数据量增长的方式，成为水平伸缩，水平伸缩理论上没有瓶颈，但是缺点是技术要求比较高。

### 引入负载均衡设备

服务的路由，基于负载均衡设备来实现。

负载均衡算法
轮询法
将请求顺序轮流分配到后台服务器，均衡的对待每一台服务器，而不关系服务器实际的连接数和当前系统的负载。
缺点：当集群中服务器硬件配置不同、性能差别大时，无法区别对待。

随机法
通过系统随机函数，根据后台服务器里列表的大小值随机选取一台进行访问。随着调用量的增大，其实际效果接近与平均分配流量到每一台服务器，也就是轮询法的效果。
优点：使用简单，不需要额外的配置和算法。
缺点：随机数的特点是在数据量大到一定程度才能保证均衡，所以如果请求量有限的话，可能会达不到负载均衡的要求。

源地址哈希法
根据服务消费者请求客户端IP地址，通过哈希函数计算得到一个哈希值，将这个哈希值和服务器列表的大小进行取模运算，得到的结果便是要访问服务器地址的序号。采用源地址哈希法进行负载均衡，相同的IP客户端，如果服务器列表不变，将映射到同一个后台服务器进行访问。

加权轮询法
不同的后台服务器的配置可能和当前系统的负载并不相同，因此他们的抗压能力也不一样。给配置高、负载低的机器分配更高的权重，使其处理更过的请求，而配置低、负载高的机器分配较低的权重，降低其系统负载，经请求按照顺粗且根据权重分配给后端。

最小连接数法
前面集中请求方式都是通过请求次数的合理分配最大可能你提高服务器的利用率，但是实际上请求次数的均衡并不能代表负载的均衡。
最小连接数法根据后端服务器当前的连接情况，动态的选取其中当前积压连接数量最少的一台服务器来处理当前请求，尽可能的提高后台服务器利用率，将负载合理的分流到每一台服务器。

session问题
在一些场景中，请求需要带有状态特征，引入了session+cookie机制记录每次请求的会话。
当会话开始的时候，给当前会话分配一个唯一的会发标识（sessionid），然后通过cookie把这个标识告诉浏览器，以后每次请求的时候，浏览器都会带上这个会话标识告诉web服务器请求属于哪一个会话。在web服务器上，各个会话有独立的存储，保存在不同的会话中。如过遇到禁用cookie的情况，一般的做法就是把这个会发标识放到URL中。

分布式环境下session的共享
Session复制
服务器实现session复制或者共享：在WEB服务器之间增加会话数据同步，通过同步保证了不同web服务器之间Session数据的一致。一般应用容器都支持Session Replication方式。
存在问题

同步Session数据造成网络宽带的开销。只要Session数据有变化，就需要将数据同步到所有的其他机器，机器越多，通过带来的数据开销就越大。
每台web服务器都要保存多有的session数据，如果整个集群的Session数据很多的话，每台机器用于保存Session数据的内容占用会很严重。
这个方案是靠应用容器完成Session的复制从而解决Session的问题，应用本身不关心这个事情，这个方案不适用集群机器多的场景。
Session集中存放
利用成熟的技术做Session复制，比如12306使用的gemfire， 比如常见的内存数据库redis。
Session数据不保存到本机而是存放在一个集中存储的地方，修改Session也是发生在集中存储的地方。web服务器使用session从集中存储的地方读取。这样保证了不同web服务器读取到的session数据都是一样的。存储Session的具体方式可以是数据库。
存在问题

读写Session数据引入网路操作，存在延时和不稳定。
如果集中存储Session的机器或者集群有问题，会影响到应用。
Session维护在客户端
利用cookie存储，但是客户端存在风险，数据不安全，而且存放的数量比较小，将Session维护在客户端还要对Session进行加密。

### 数据库压力变大，读写分离

随着业务的继续增长，数据量和访问量持续增加，对于读多写少的情况，可以考虑读写分离的方式来优化数据压力。
这个结构的变化会带来两个问题：

数据如何同步
我们希望通过读库来分担主库上读的压力，那么首先需要解决的是怎么复制到读库的问题。数据库系统一般提供了数据库复制的功能，我们可以直接使用数据库自身的机制。例如MySQL的Master+slave复制数据。
应用对数据源如何路由
对于应用来说。增加一个读库对结构变化产生了一定的影响，也就是我们的应用需要更具不同的情况来选择不同的数据库源。
搜索引擎其实是一个读库
搜索引擎可以理解成一个读库。商品存储在数据库中，而网站需要提供用户实时检索的功能，尤其是在商品检索。对于这样的读请求，如果全部走读库，其实性能也会存在一个瓶颈。而使用搜索引擎，不仅能大大提高检索速度，还能减轻读库的压力。搜索引擎最重要的工作是需要根据被所有的数据构建索引，而随着被搜索的数据的变化，索引也需要相应变化。

加速数据读取的利器-缓存及分布式存储
在大型网站中，基本上就是在解决存储和计算的问题，所以存储是一个重要的支撑系统。

分布式文件系统与NoSQL
对于一些图片，大文本，采用分布式文件系统来实现文件存储：淘宝的TFS、google的GFS，还有开源的HDFS。

NoSQL可以存储一些非关系型数据。

数据缓存
大型网站内部都会使用一些数据缓存，主要用于分担数据库的读的压力，魂村系统一般是用来保存和查询键值对的。应用系统中一般会把热点数据放入缓存，二缓存的填充也应该由应用系统完成。如果数据不存在，则从数据库读取数据后放入缓存。

页面缓存
除了数据缓存外，我们还可以对页面做缓存，数据缓存可以加速应用子啊响应请求时的数据读取速度，但是最终展示给用户的还是页面，有些动态产生的页面或者访问量特别高的页面，可以对页面或内容做一些缓存。

### 弥补关系型数据库的不足，引入分布式存储

我们使用的最多的还是关系型数据库，但是在一些场景中关系型数据库不合适，引入分布式存储系统，如：redis、mongoDB、cassandra、HBase等。
根据不同的场景和数据结构类型，选择合适的分布式存储系统可以极大提高性能。分布式系统通过一个集群提供一个高容量、高并发、数据冗余的支持。

读写分离后，数据库又遇瓶颈
通过读写分离以及在某些场景下使用分布式缓存替换关系型数据库的方式，能够降低主库的压力，解决数据存储的问题。但是随着业务的不断发展，我们的主库也遇到平静。

### 专库专用，数据垂直拆分

不同业务的数据从原来的一个数据库拆分到了多个数据库中，那么就需要考虑到如何处理原来单机跨业务的事务。

使用分布式事务。
去掉事务或者不追求强事务的支持。
对数据进行垂直拆分后，解决了把所有业务数据放在一个数据库中的压力问题，并且根据不同的业务特点进行更多的优化。

### 垂直拆分后，遇到瓶颈，数据水平拆分

数据的水平拆分就是把同一个表的数据拆分到两个数据库中，产生数据水平拆分的原因是某个业务的数据表的数据量或者更显量达到了单个数据库的瓶颈，这个时候就可以把表拆分到两个或多个数据库中。

数据库水平拆分与数据垂直拆分的区别是，垂直拆分就把不同的表拆分到不同的数据库，水平拆分是把同一个表拆分到不同的数据库中。

数据水平拆分与读写分离的区别是：读写分离解决是读压力大的问题。

水平拆分带来的影响：

sql路由问题，余姚根据一个条件判断当前请求发送到哪一个数据库中。
主键的处理，不能采用自增ID，需要全局id。
由于用一个业务的数据被拆分到不同的数据库中，因此需要涉及一些查询需要跨连个数据库获取，如果数据量大并且需要分页，比较难处理。

### 应用面临的挑战

随着业务的发展，用用的功能会越来越多，应用也会越来越大，我们需要考虑如何不让应用变大，这就需要把应用拆分，从一个应用变为多个。

服务化：把应用分为三层，最上面的web系统，用于完成不同的业务功能；中间的是一些服务中心，不同的服务中心提供不同的业务服务；最下面的是业务的数据库。

与之前相比有几个重要的变化：

业务功能的访问不仅仅是单机内部的方法调用，还引入了远程的服务调用。
共享代码不再是散落在不同的应用中，这个实现被放在各个服务中心。
数据库的交互放到服务中心，让web应用更加注重与浏览器的交互工作，而不必过多关注业务逻辑的事情。

### 什么是分布式架构

#### 分布式架构的定义

分布式系统是指位于网络计算机上的组件仅通过传递消息来通信和协调目标系统。其中有两个重要因素：

组件是分布在网络上的。
组件之间仅仅通过消息实现通信并协调行动。

#### 分布式架构的意义

升级单机提升性能的性价比越来越低。
单机处理存在瓶颈。
针对稳定性和可用性的要求。

## 10.9 [Dubbo介绍](https://baike.baidu.com/item/Dubbo/18907815?fr=aladdin)

### Dubbo模型

Dubbo是一个高性能、轻量级的开源RPC框架，它由10层模型来构成。整个分层的模型由上到下如下：

![image-20220529211021641](pictures/Dubbo分层模型.png)

也可以把Dubbo理解为三层模型：

* 第1层是Business业务逻辑层，由我们自己来提供接口和实现，还有一些配置信息；
* 第2层是RPC调用的核心层，负责封装和实现整个RPC的调用过程、负载均衡、集群容错、代理等核心功能；
* 第3层是Remoting，是对网络传输、协议和数据转化的一个封装。

### Dubbo核心能力

Dubbo提供如下6个核心能力：

* 面向接口代理的高性能RPC调用
* 智能容错和负载均衡
* 服务的自动注册和发现
* 高度可扩展能力
* 运行期流量调度
* 可视化的服务治理和运维

### Dubbo负载均衡的策略

Dubbo有如下5种负载均衡策略：

1. 加权随机：通过区间的随机算法去获取一个目标服务器，同时这个区间算法可以针对某一个服务器去增加权重，从而去获得更大的一个调用可能性。
2. 最小活跃数：每一个服务提供者对应一个活动数active。初始情况下，所有服务者的活跃数都为0，每次收到请求活跃数加1，完成请求后再将活跃数减1。在服务运行一段时间后，性能好的服务提供者的处理请求的速度更快，因此活跃数下降的也越快，这时服务提供者就能够去优先获得新的请求。
3. 一致性hash：根据同一个客户端请求，通过一致性hash算法，会落到一个目标服务器上，只要请求的信息没变化的情况下，它的请求都会落到同一台目标服务器。
4. 加权轮询：目标服务器按照123123这种方式轮询，而加权可以针对目标服务器的某些性能好的节点去增加权重，从而使得这样的服务器能够获得更多的调用。
5. 最短响应时间的权重随机：会计算目标服务的请求的响应时间，去根据响应时间最短的服务去配置更高的权重，然后进行区间随机算法，从而得到一个目标服务器节点进行访问。

### Dubbo工作原理

工作原理图如下：

![image-20220529212711811](pictures/Dubbo工作原理.png)

1. 服务启动的时候，服务提供者和服务消费者根据配置信息会连接到注册中心，分别向注册中心去订阅和注册服务；
2. 注册中心会根据订阅关系去返回服务提供者的信息到服务消费者，同时服务消费者会把服务提供者的信息缓存到本地。如果信息发生变更，消费者会收到注册中心的推送，去更新本地缓存；
3. 服务消费者会生成代理对象，同时根据负载均衡策略去选择一台目标的服务提供者并且定时向monitor记录接口的调用次数和时间信息；
4. 生成代理对象后，服务消费者通过代理对象发起接口的一个调用；
5. 服务提供者收到请求后，会根据数据进行反序列化，然后通过代理调用具体的接口一个实现。

如上是Dubbo的原理和实现过程。

### Dubbo和SpringCloud的区别

**关注点不同：**

Dubbo是SOA（面向服务架构）时代的产物，它的关注点主要在于服务的远程调用、流量分发、服务治理、流量控制等方面。

SpringCloud是诞生于微服务架构时代，它关注的是微服务生态的解决方案。另外，SpringCloud依托于Spring、SpringBoot的生态。

所以两个框架的目标是不一样的，Dubbo定位于服务治理，SpringCloud是一个微服务解决生态。

**底层技术不同：**

Dubbo底层使用的是netty这样的NIO框架，是基于TCP协议进行传输，通过Hession等序列化的方式去完成RPC的通信。

SpringCloud是基于Http+Rest接口去实现远程通信。相对来说，Http请求会有更大的一些报文，占用的带宽会更多，效率上比Dubbo会更差一些。但是Rest相比RPC来说会更加灵活，服务提供方和服务调用方只需要根据Http协议的契约去完成通信就可以了。

### 主要核心部件

**Remoting:** 网络通信框架，实现了 sync-over-async 和 request-response 消息机制.

**RPC:** 一个[远程过程调用](https://baike.baidu.com/item/远程过程调用)的抽象，支持[负载均衡](https://baike.baidu.com/item/负载均衡)、[容灾](https://baike.baidu.com/item/容灾)和[集群](https://baike.baidu.com/item/集群)功能

**Registry:** 服务目录框架用于服务的注册和服务事件发布和订阅

### 工作原理

![原理图](https://bkimg.cdn.bcebos.com/pic/d01373f082025aaf111c708cfbedab64034f1a4e?x-bce-process=image/resize,m_lfit,w_440,limit_1/format,f_auto)

**Provider**

暴露服务方称之为“[服务提供者](https://baike.baidu.com/item/服务提供者/12725153)”。

**Consumer**

调用[远程服务](https://baike.baidu.com/item/远程服务)方称之为“服务消费者”。

**Registry**

服务注册与发现的中心目录服务称之为“服务注册中心”。

**Monitor**

统计服务的调用次数和调用时间的日志服务称之为“服务监控中心”。

(1) 连通性：

注册中心负责服务地址的注册与查找，相当于[目录服务](https://baike.baidu.com/item/目录服务)，[服务提供者](https://baike.baidu.com/item/服务提供者/12725153)和消费者只在启动时与注册中心交互，注册中心不转发请求，压力较小

监控中心负责统计各服务调用次数，调用时间等，统计先在内存汇总后每分钟一次发送到监控中心服务器，并以报表展示

服务提供者向注册中心注册其提供的服务，并汇报调用时间到监控中心，此时间不包含网络开销

服务消费者向注册中心获取[服务提供者](https://baike.baidu.com/item/服务提供者/12725153)地址列表，并根据负载算法直接调用提供者，同时汇报调用时间到监控中心，此时间包含网络开销

注册中心，服务提供者，服务消费者三者之间均为长连接，监控中心除外

注册中心通过[长连接](https://baike.baidu.com/item/长连接)感知服务提供者的存在，服务提供者宕机，注册中心将立即推送事件通知消费者

注册中心和监控中心全部宕机，不影响已运行的提供者和消费者，消费者在[本地缓存](https://baike.baidu.com/item/本地缓存)了提供者列表

注册中心和监控中心都是可选的，服务消费者可以直连服务提供者

(2) 健壮性：

监控中心宕掉不影响使用，只是丢失部分[采样数据](https://baike.baidu.com/item/采样数据)

数据库宕掉后，注册中心仍能通过[缓存](https://baike.baidu.com/item/缓存)提供服务列表查询，但不能注册新服务

注册中心对等[集群](https://baike.baidu.com/item/集群)，任意一台宕掉后，将自动切换到另一台

注册中心全部宕掉后，[服务提供者](https://baike.baidu.com/item/服务提供者/12725153)和服务消费者仍能通过本地缓存通讯

服务提供者无状态，任意一台宕掉后，不影响使用

服务提供者全部宕掉后，服务消费者应用将无法使用，并无限次重连等待服务提供者恢复

(3) 伸缩性：

注册中心为对等集群，可动态增加机器部署实例，所有客户端将自动发现新的注册中心

服务提供者无状态，可动态增加机器部署实例，注册中心将推送新的服务提供者信息给消费者

### 特性

- 面向接口代理的高性能RPC调用提供高性能的基于代理的远程调用能力，服务以接口为粒度，为开发者屏蔽远程调用底层细节。
- 智能负载均衡内置多种负载均衡策略，智能感知下游节点健康状况，显著减少调用延迟，提高系统吞吐量。
- 服务自动注册与发现支持多种注册中心服务，服务实例上下线实时感知。
- 高度可扩展能力遵循微内核+插件的设计原则，所有核心能力如Protocol、Transport、Serialization被设计为扩展点，平等对待内置实现和第三方实现。
- 运行期流量调度内置条件、脚本等路由策略，通过配置不同的路由规则，轻松实现灰度发布，同机房优先等功能。
- 可视化的服务治理与运维提供丰富服务治理、运维工具：随时查询服务元数据、服务健康状态及调用统计，实时下发路由策略、调整配置参数。



## 10.10 [经验：如何正确的使用开源项目](https://www.infoq.cn/article/how-to-correctly-use-the-open-source-project)



## 10.11 [流量录制与回放技术实践](https://blog.csdn.net/muyimo/article/details/122063319)

**[为什么需要录制线上流量回放](https://baijiahao.baidu.com/s?id=1715687153800992122&wfr=spider&for=pc)**

* 项目大迭代更新，容易漏测，或者有很多没用评估到的地方。

* 如果用线上流量做一次回归测试，可以进一步减少 bug 的风险。
* 大大节省构造测试数据，或者构造测试数据脚本的时间，提高效率。
* 方便回放数据压测

### 3. 技术选型与验证

#### 3.1 技术选型

一开始选型的时候，经验不足，并没有考虑太多因素，只从功能性和知名度两个维度进行了调研。首先功能上一定要能满足我们的需求，比如具备流量过滤功能，这样可以按需录制指定接口。其次，候选项最好有大厂背书，github 上有很多 star。根据这两个要求，选出了如下几个工具：

![img](https://img-blog.csdnimg.cn/img_convert/42a2babfe2efde9d18e25db26bf86339.png)

图1：技术选型

第一个是选型是阿里开源的工具，全称是 [jvm-sandbox-repeater](https://github.com/alibaba/jvm-sandbox-repeater)，这个工具其实是基于 [JVM-Sandbox](https://github.com/alibaba/JVM-Sandbox) 实现的。原理上，工具通过字节码增强的形式，对目标接口进行拦截，以获取接口参数和返回值，效果等价于 AOP 中的环绕通知 (Around advice)。

第二个选型是 GoReplay，基于 Go 语言实现。底层依赖 [pcap](https://blog.csdn.net/cike44/article/details/52699351)库提供流量录制能力。著名的 tcpdump 也依赖于 pcap 库，所以可以把 GoReplay 看成极简版的 tcpdump，因为其支持的协议很单一，只支持录制 http 流量。

第三个选型是 Nginx 的流量镜像模块 [ngx_http_mirror_module](http://nginx.org/en/docs/http/ngx_http_mirror_module.html)，基于这个模块，可以将流量镜像到一台机器上，实现流量录制。

第四个选型是阿里云云效里的子产品——[双引擎回归测试平台](https://help.aliyun.com/document_detail/62635.html)，从名字上可以看出来，这个系统是为回归测试开发的。而我们需求是做压测，所以这个服务里的很多功能我们用不到。

经过比较筛选后，我们选择了 GoReplay 作为流量录制工具。在分析 GoReplay 优缺点之前，先来分析下其他几个工具存在的问题。

1. jvm-sandbox-repeater 这个插件底层基于 JVM-Sandbox 实现，使用时需要把两个项目的代码都加载到目标应用内，对应用运行时环境有侵入。如果两个项目代码存在问题，造成类似 OOM 这种问题，会对目标应用造成很大大的影响。另外因为方向小众，导致 JVM-Sandbox 应用并不是很广泛，社区活跃度较低。因此我们担心出现问题官方无法及时修复，所以这个选型待定。
2. ngx_http_mirror_module 看起来是个不错的选择，出生“名门”。但问题也有一些。首先只能支持 http 流量，而我们以后一定会支持 dubbo 流量录制。其次这个插件要把请求镜像一份出去，势必要消耗机器的 TCP 连接数、网络带宽等资源。考虑到我们的流量录制会持续运行在网关上，所以这些资源消耗一定要考虑。最后，这个模块没法做到对指定接口进行镜像，且镜像功能开关需要修改 nginx 配置实现。线上的配置是不可能，尤其是网关这种核心应用的配置是不能随便改动的。综合这些因素，这个选型也被放弃了。
3. 阿里云的引擎回归测试平台在我们调研时，自身的功能也在打磨，用起来挺麻烦的。其次这个产品属于云效的子产品，不单独出售。另外这个产品主要还是用于回归测试的，与我们的场景存在较大偏差，所以也放弃了。

接着来说一下 GoReplay 的优缺点，先说优点：

- 单体程序，除了 pcap 库，没有其他依赖，也无需配置，所以环境准备很简单
- 本身是个可执行程序，可直接运行，很轻量。只要传入合适的参数就能录制，易使用
- github 上的 star 数较多，知名度较大，且社区活跃
- 支持流量过滤功能、按倍速回放功能、回放时改写接口参数等功能，功能上贴合我们的需求
- 资源消耗小，不侵入业务应用 JVM 运行时环境，对目标应用影响较小

对于以 Java 技术栈为基础的公司来说，GoReplay 由于是 Go 语言开发的，技术栈差异很大，日后的维护和拓展是个大问题。所以单凭这一点，淘汰掉这个选型也是很正常的。但由于其优点也相对突出，综合其他选型的优缺点考虑后，我们最终还是选择了 GoReplay 作为最终的选型。最后大家可能会疑惑，为啥不选择 tcpdump。原因有两点，我们的需求比较少，用 tcpdump 有种大炮打蚊子的感觉。另一方面，tcpdump 给我们的感觉是太复杂了，驾驭不住（流下了没有技术的眼泪😭），因此我们一开始就没怎么考虑过这个选型。

| 选型                             | 语言 | 是否开源 | 优点                                                         | 缺点                                                         |
| :------------------------------- | :--- | :------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| GoReplay                         | Go   | ✅        | 1. 开源项目，代码简单，方便定制 2. 单体持续，依赖少，无需配置，环境准备简单 3. 工具很轻量，易使用 3. 功能相对丰富，能够满足我们所有的需求 4. 自带回放功能，能够直接使用录制数据，无需单独开发 5. 资源消耗少，且不侵入目标应用的 JVM 运行时环境，影响小 6. 提供了插件机制，且插件实现不限制语言，方便拓展 | 1. 应用不够广泛，无大公司背书，成熟度不够 2. 问题比较多，1.2.0 版本官方直接不推荐使用 3. 接上一条，对使用者的要求较高，出问题情况下要能自己读源码解决，官方响应速度一般 4. 社区版只支持 HTTP 协议，不支持二进制协议，且核心逻辑与 HTTP 协议耦合了，拓展较麻烦 5. 只支持命令行启动，没有内置服务，不好进行集成 |
| JVM-Sandbox jvm-sandbox-repeater | Java | ✅        | 1. 通过增强的方式，可以直接对 Java 类方法进行录制，十分强大 2. 功能比较丰富，较为符合需求 3. 对业务代码透明无侵入 | 1. 会对应用运行时环境有一定侵入，如果发生问题，对应用可能会造成影响 2. 工具本身仍然偏向测试回归，所以导致一些功能在我们的场景下没法使用，比如不能使用它的回放功能进行高倍速压测 3. 社区活跃度较低，有停止维护的风险 4. 底层实现确实比较复杂，维护成本也比较高。再次留下了没有技术的眼泪😢 5. 需要搭配其他的辅助系统，整合成本不低 |
| ngx_http_mirror_module           | C    | ✅        | 1. nginx 出品，成熟度可以保证 2. 配置比较简单                | 1. 不方便启停，也不支持过滤 2. 必须和 nginx 搭配只用，因此使用范围也比较受限 |
| 阿里云引擎回归测试平台           | -    | ❌        | -                                                            | -                                                            |

#### 3.2 选型验证

选型完成后，紧接着要进行功能、性能、资源消耗等方面的验证，测试选型是否符合要求。根据我们的需求，做了如下的验证：

1. 录制功能验证，验证流量录制的是否完整，包含请求数量完整性和请求数据准确性。以及在流量较大情况下，资源消耗情况验证
2. 流量过滤功能验证，验证能否过滤指定接口的流量，以及流量的完整性
3. 回放功能验证，验证流量回放是否能如预期工作，回放的请求量是否符合预期
4. 倍速回放验证，验证倍速功能是否符合预期，以及高倍速回放下资源消耗情况

以上几个验证当时在线下都通过了，效果很不错，大家也都挺满意的。可是倍速回放这个功能，在生产环境上进行验证时，回放压力死活上不去，只能压到约 600 的 QPS。之后不管再怎么增压，QPS 始终都在这个水位。我们与业务线同事使用不同的录制数据在线上测试了多轮均不行，开始以为是机器资源出现了瓶颈。可是我们看了 CPU 和内存消耗都非常低，TCP 连接数和带宽也是很富余的，因此资源是不存在瓶颈的。这里也凸显了一个问题，早期我们只对工具做了功能测试，没有做性能测试，导致这个问题没有尽早暴露出来。于是我自己在线下用 nginx 和 tomcat 搭建了一个测试服务，进行了一些性能测试，发现随随便便就能压到几千的 QPS。看到这个结果啼笑皆非，脑裂了😭。后来发现是因为线下的服务的 RT 太短了，与线上差异很大导致的。于是让线程随机睡眠几十到上百毫秒，此时效果和线上很接近。到这里基本上能够大致确定问题范围了，应该是 GoReplay 出现了问题。但是 GoReplay 是 Go 语言写的，大家对 Go 语言都没经验。眼看着问题解决唾手可得，可就是无处下手，很窒息。后来大佬们拍板决定投入时间深入 GoReplay 源码，通过分析源码寻找问题，自此我开始了 Go 语言的学习之路。原计划两周给个初步结论，没想到一周就找到了问题。原来是因为 GoReplay v1.1.0 版本的使用文档与代码实现出现了很大的偏差，导致按照文档操作就是达不到预期效果。具体细节如下：

![img](https://img-blog.csdnimg.cn/img_convert/9ce58842f38b9e1be7be91defa6e1ffc.png)

图2：GoReplay 使用说明

先来看看坑爹的文档是怎么说的，`--output-http-workers` 这个参数表示有多少个协程同时用于发生 http 请求，默认值是0，也就是无限制。再来看看代码（output_http.go）是怎么实现的：

![img](https://img-blog.csdnimg.cn/img_convert/b553ec5e2c9fa33f63161022d42998f2.png)

图3：GoRepaly 协程并发数决策逻辑

文档里说默认 http 发送协程数无限制，结果代码里设置了 10，差异太大了。为什么 10 个协程不够用呢，因为协程需要原地等待响应结果，也就是会被阻塞住，所以10个协程能够打出的 QPS 是有限的。原因找到后，我们明确设定 --output-http-workers 参数值，倍速回放的 QPS 最终验证下来能够达到要求。

这个问题发生后，我们对 GoReplay 产生了很大的怀疑，感觉这个问题比较低级。这样的问题都会出现，那后面是否还会出现有其他问题呢，所以用起来心里发毛。当然，由于这个项目维护的人很少，基本可以认定是个人项目。且该项目经过没有大规模的应用，尤其没有大公司的背书，出现这样的问题也能理解，没必要太苛责。因此后面碰到问题只能见招拆招了，反正代码都有了，直接白盒审计吧。

#### 3.3 总结与反思

先说说选型过程中存在的问题吧。从上面的描述上来看，我在选型和验证过程均犯了一些较为严重的错误，被自己生动的上了一课。在选型阶段，对于知名度，居然认为 star 比较多就算比较有名了，现在想想还是太幼稚了。比起知名度，成熟度其实更重要，稳定坑少下班早🤣。另外，可观测性也一定要考虑，否则查问题时你将体验到什么是无助感。

在验证阶段，功能验证没有太大问题。但性能验证只是象征性的搞了一下，最终在与业务线同事一起验证时翻车了。所以验证期间，性能测试是不能马虎的，一旦相关问题上线后才发现，那就很被动了。

根据这次的技术选型经历做个总结，以后搞技术选型时再翻出来看看。选型维度总结如下：

| 维度     | 说明                                                         |
| :------- | :----------------------------------------------------------- |
| 功能性   | 1. 选型的功能是否能够满足需求，如果不满足，二次开发的成本是怎样的 |
| 成熟度   | 1. 在相关领域内，选型是否经过大范围使用。比如 Java Web 领域，Spring 技术栈基本人尽皆知 2. 一些小众领域的选型可能应用并不是很广泛，那只能自己多去看看 issue，搜索一些踩坑记录，自行评估了 |
| 可观测性 | 1. 内部状态数据是否有观测手段，比如 GoReplay 会把内部状态数据定时打印出来 2. 方不方便接入公司的监控系统也要考虑，毕竟人肉观察太费劲 |

验证总结如下：

1. 根据要求一项一项的去验证选型的功能是否符合预期，可以搞个验证的 checklist 出来，逐项确认
2. 从多个可能的方面对选型进行性能测试，在此过程中注意观察各种资源消耗情况。比如 GoReplay 流量录制、过滤和回放功能都是必须要做性能测试的
3. 对选型的长时间运行的稳定性要进行验证，对验证期间存在的异常情况注意观测和分析
4. 更严格一点，可以做一些故障测试。比如杀进程，断网等

关于选型更详细的实战经验，可以参考李运华大佬的文章：[如何正确的使用开源项目](https://www.infoq.cn/article/how-to-correctly-use-the-open-source-project)。

### 4. 具体实践

当技术选型和验证都完成后，接下来就是要把想法变为现实的时候了。按照现在小步快跑，快速迭代的模式，启动阶段通常我们仅会规划最核心的功能，保证流程走通。接下来再根据需求的优先级进行迭代，逐步完善。接下来，我将在按照项目的迭代过程来进行介绍。

#### 4.1 最小可用系统

4.1.1 需求介绍

| 序号 | 分类 | 需求点               | 说明                                                         |
| :--- | :--- | :------------------- | :----------------------------------------------------------- |
| 1    | 录制 | 流量过滤，按需录制   | 支持按 HTTP 请求路径过滤流量，这样可以录制指定接口的流量     |
| 2    |      | 录制时长可指定       | 可设定录制时长，一般情况下都是录制10分钟，把流量波峰录制下来 |
| 3    |      | 录制任务详情         | 包含录制状态、录制结果统计等信息                             |
| 4    | 回放 | 回放时长可指定       | 支持设定 1 ~ 10 分钟的回放时长                               |
| 5    |      | 回放倍速可指定       | 根据录制时的 QPS，按倍数进行流量放大，最小粒度为 1 倍速      |
| 6    |      | 回放过程允许人为终止 | 在发现被压测应用出现问题时，可人为终止回放过程               |
| 7    |      | 回放任务详情         | 包含回放状态、回放结果统计                                   |

以上就是项目启动阶段的需求列表，这些都是最基本需求。只要完成这些需求，一个最小可用的系统就实现了。

4.1.2 技术方案简介

4.1.2.1 架构图

![img](https://img-blog.csdnimg.cn/img_convert/d0309347414ca1eb7a19c14da04f1a08.png)

图4：压测系统一期架构图

上面的架构图经过编辑，与实际有一定差异，但不影响讲解。需要说明的是，我们的网关服务、压测机以及压测服务都是分别由多台构成，所有网关和压测实例均部署了 GoRepaly 及其控制器。这里为了简化架构图，只画了一台机器。下面对一些核心流程进行介绍。

4.1.2.2 Gor 控制器

在介绍其他内容之前，先说一下 Gor 控制器的用途。用一句话介绍：引入这个中间层的目的是为了将 GoReplay 这个命令行工具与我们的压测系统进行整合。这个模块是我们自己开发，最早使用 shell 编写的（苦不堪言😭），后来用 Go 语言重写了。Gor 控制器主要负责下面一些事情：

1. 掌握 GoRepaly 生杀大权，可以调起和终止 GoReplay 程序
2. 屏蔽掉 GoReplay 使用细节，降低复杂度，提高易用性
3. 回传状态，在 GoReplay 启动前、结束后、其他标志性事件结束后都会向压测系统回传状态
4. 对录制和回放产生数据进行处理与回传
5. 打日志，记录 GoRepaly 输出的状态数据，便于后续排查

GoReplay 本身只提供最基本的功能，可以把其想象成一个只有底盘、轮子、方向盘和发动机等基本配件的汽车，虽然能开起来，但是比较费劲。而我们的 Gor 控制器相当于在其基础上提供了一键启停，转向助力、车联网等增强功能，让其变得更好用。当然这里只是一个近似的比喻，不要纠结合理性哈。知晓控制器的用途后，下面介绍启动和回放的执行过程。

4.1.2.3 录制过程介绍

用户的录制命令首先会发送给压测服务，压测服务原本可以通过 SSH 直接将录制命令发送给 Gor 控制器的，但出于安全考虑必须绕道运维系统。Gor 控制器收到录制命令后，参数验证无误，就会调起 GoReplay。录制结束后，Gor 控制器会将状态回传给压测系统，由压测判定录制任务是否结束。详细的流程如下：

1. 用户设定录制参数，提交录制请求给压测服务
2. 压测服务生成压测任务，并根据用户指定的参数生成录制命令
3. 录制命令经由运维系统下发到具体的机器上
4. Gor 控制器收到录制命令，回传“录制即将开始”的状态给压测服务，随后调起 GoReplay
5. 录制结束，GoReplay 退出，Gor 控制器回传“录制结束”状态给压测服务
6. Gor 控制器回传其他信息给压测系统
7. 压测服务判定录制任务结束后，通知压测机将录制数据读取到本地文件中
8. 录制任务结束

这里说明一下，要想使用 GoReplay 倍速回放功能，必须要将录制数据存储到文件中。然后通过下面的参数设置倍速：

```python
# 三倍速回放
gor --input-file "requests.gor|300%" --output-http "test.com"
```

4.1.2.4 回放过程介绍

回放过程与录制过程基本相似，只不过回放的命令是固定发送给压测机的，具体过程就不赘述了。下面说几个不同点：

1. 给回放流量打上压测标：回放流量要与真实流量区分开，需要一个标记，也就是压测标
2. 按需改写参数：比如把 user-agent 改为 goreplay，或者增加测试账号的 token 信息
3. GoReplay 运行时状态收集：包含 QPS，任务队列积压情况等，这些信息可以帮助了解 GoReplay 的运行状态

4.1.3 不足之处

这个最小可用系统在线上差不多运行了4个月，没有出现过太大的问题，但仍然有一些不足之处。主要有两点：

1. 命令传递的链路略长，增大的出错的概率和排查的难度。比如运维系统的接口偶尔失败，关键还没有日志，一开始根本没法查问题
2. Gor 控制器是用 shell 写的，约 300 行。shell 语法和 Java 差异比较大，代码也不好调试。同时对于复杂的逻辑，比如生成 JSON 字符串，写起来很麻烦，后续维护成本较高

这两点不足一直伴随着我们的开发和运维工作，直到后面进行了一些优化，才算是彻底解决掉了这些问题。

#### 4.2 持续优化

![img](https://img-blog.csdnimg.cn/img_convert/abf6e07de71e4af31693f04eae4a399c.png)

图5：Gor 控制器优化后的架构图

针对前面存在的痛点，我们进行了针对性的改进。重点使用 Go 语言重写了 gor 控制器，新的控制器名称为 gor-server。从名称上可以看出，我们内置了一个 HTTP 服务。基于这个服务，压测服务下发命令终于不用再绕道运维系统了。同时所有的模块都在我们的掌控中，开发和维护的效率明显变高了。

#### 4.4 开花结果，落地新场景

我们的流量录制与回放系统主要的，也是当时唯一的使用场景是做压测。系统稳定后，我们也在考虑还有没有其他的场景可以搞。正好在技术选型阶段试用过 jvm-sandbox-repeater，这个工具主要应用场景是做流量对比测试。对于代码重构这种不影响接口返回值结构的改动，可以通过流量对比测试来验证改动是否有问题。由于大佬们觉得 jvm-sandbox-repeater 和底层的 jvm-sandbox 有点重，技术复杂度也比较高。加之没有资源来开发和维护这两个工具，因此希望我们基于流量录制和回放系统来做这个事情，先把流程跑通。

项目由 QA 团队主导，流量重放与 diff 功能由他们开发，我们则提供底层的录制能力。系统的工作示意图如下：

![img](https://img-blog.csdnimg.cn/img_convert/8039f50d032add840eb4efe45494a1eb.png)

图15：对比测试示意图

我们的录制系统为重放器提供实时的流量数据，重放器拿到数据后立即向预发和线上环境重放。重放后，重放器可以分别拿到两个环境返回的结果，然后再把结果传给比对模块进行后续的比对。最后把比对结果存入到数据库中，比对过程中，用户可以看到哪些请求比对失败了。对于录制模块来说，要注意过滤重放流量。否则会造成接口 QPS 倍增，重放变压测了，喜提故障一枚。

## 10.12 [混沌工程介绍](https://blog.csdn.net/weixin_42526859/article/details/117374795)

[混沌工程之ChaosMesh使用之模拟网络Duplicate包_zuozewei的博客-CSDN博客](https://blog.csdn.net/zuozewei/article/details/121120112)

ChaoBlade 的实现原理
混沌工程之 ChaosBlade-Operator 使用之模拟 POD 丢包场景
Linux 网络故障模拟工具TC
混沌工程之 SpringBoot 集成 ChaosMonkey
混沌工程之 ChaosToolkit K8S 使用之删除 POD 实验
混沌工程之 ChaosMesh 使用之模拟 CPU 使用率
混沌工程之 ChaosMesh 使用之模拟 POD 网络延迟
混沌工程之 ChaosMesh 使用之模拟 POD 网络丢包
混沌工程之 ChaosMesh 使用之模拟网络 Duplicate 包
混沌工具之 ChaosMesh 源码编译安装



### 混沌工程基础介绍

在一个由很多微服务组成的分布式系统中，我们永远难以全面掌握发生什么事件会导致系统局部不可用，甚至全面崩溃。但我们却可以尽可能地在这些不可用的情况发生之前找出系统中的脆弱点。Netflix的工程师团队是根据多年实践经验主动发现系统中脆弱点的一整套方法。这套方法现在已经逐渐演变成计算机科学的一门新兴学科，即“混沌工程”。通过一系列可控的实验和执行实验的原则，混沌工程将揭示出分布式系统中随时发生的各类事件是如何逐步导致系统整体不可用的。

### 混沌工程是什么

混沌工程是一门新兴的技术学科，**它的初衷是通过实验性的方法，让人们建立复杂分布式系统能够在生产中抵御突发事件能力的信心。**

只要你有过在生产环境中实际运行一个分布式系统的经历，你就应该清楚，各种不可预期的突发事件是一定会发生的。分布式系统天生包含大量的交互、依赖点，可能出错的地方数不胜数。硬盘故障，网络不通，流量激增压垮某些组件……我们可以不停地列举下去。这都是每天要面临的常事，任何一次处理不好就有可能导致业务停滞、性能低下，或者其他各种无法预料的异常行为。

在一个复杂的分布式系统中，我们单靠人力并不能够完全阻止这些故障的发生，而应该致力于在这些异常行为被触发之前，尽可能多地识别出会导致这些异常的、在系统中脆弱的、易出故障的环节。当我们识别出这些风险时，就可以有针对性地对系统进行加固、防范，从而避免故障发生时所带来的严重后果。我们能够在不断打造更具弹性[1]系统的同时，建立对运行高可用分布式系统的信心。

混沌工程正是这样一套通过在系统基础设施上进行实验，主动找出系统中的脆弱环节的方法学。这种通过实验验证的方法显然可以为我们打造更具弹性的系统，同时让我们更透彻地掌握系统运行时的各种行为规律。

混沌工程，是一种提高技术架构弹性能力的复杂技术手段。Chaos工程经过实验可以确保系统的可用性。混沌工程旨在将故障扼杀在襁褓之中，也就是在故障造成中断之前将它们识别出来。通过主动制造故障，测试系统在各种压力下的行为，识别并修复故障问题，避免造成严重后果。

它被描述为“**在分布式系统上进行实验的学科，目的是建立对系统承受生产环境中湍流条件能力的信心。**”

> Chaos Engineering is the discipline of experimenting on a systemin
> order to build confidence in the system’s capabilityto withstand
> turbulent conditions in production.

它也可以视为流感疫苗，故意将有害物质注入体内以防止未来疾病，这似乎很疯狂，但这种方法也适用于分布式云系统。混沌工程会将故障注入系统以测试系统对其的响应。这使公司能够为宕机做准备，并在宕机发生之前将其影响降至最低。

如何知道系统是否处于稳定状态呢？通常，团队可以通过单元测试、集成测试和性能测试等手段进行验证。但是，无论这些测试写的多好，我们认为都远远不够，因为错误可以在任何时间发生，尤其是对分布式系统而言，此时就需要引入混沌工程（Chaos Engineering）。

故障演练：目标是沉淀通用的故障模式，以可控成本在线上重放，以持续性的演练和回归方式运营来暴露问题，推动系统、工具、流程、人员能力的不断前进。

### 为什么需要混沌工程

1. 混沌工程与测试的区别

混沌工程、故障注入和故障测试在侧重点和工具集的使用上有一些重叠。举个例子，Netflix的很多混沌工程实验的研究对象都是基于故障注入来引入的。混沌工程和其他测试方法的主要区别在于，混沌工程是发现新信息的实践过程，而故障注入则是基于一个特定的条件、变量的验证方法。

例如，当你希望探究复杂系统会如何应对异常时，会对系统中的服务注入通信故障，如超时、错误等，这是一个典型的故障注入场景。但有时我们希望探究更多其他非故障类的场景，如流量激增、资源竞争条件、拜占庭故障（例如性能差或有异常的节点发出错误的响应、异常的行为、对调用者随机返回不同的响应等）、非计划中的或消息内容非正常组合的处理等。如果一个面向公众用户的网站突然出现流量激增的情况，从而产生了更多的收入，那么我们很难将这种情况称为故障，但我们仍然需要探究清楚系统在这种情况下会如何变现。和故障注入类似，故障测试是通过对预先设想到的可以破坏系统的点进行测试，但是并不能去探究上述这类更广阔领域里的、不可预知的、但很可能发生的事情。

我们可以描述一下测试和实验最重要的区别。在测试中，我们要进行断言：给定一个特定的条件，系统会输出一个特定的结果。一般来说，测试只会产生二元的结果，即验证一个结果是真还是假，从而判定测试是否通过。严格意义上来说，这个实践过程并不能让我们发掘出系统未知的或尚不明确的认知，它仅仅是对已知的系统属性可能的取值进行测验。而实验可以产生新的认知，而且通常还能开辟出一个更广袤的对复杂系统的认知空间。整本书都在探讨这个主题——混沌工程是一种帮助我们获得更多的关于系统的新认知的实验方法。它和已有的功能测试、集成测试等测试已知属性的方法有本质上的区别。

一些混沌工程实验的输入样例：

* 模拟整个云服务区域或整个数据中心的故障。
* 跨多实例删除部分Kafka主题（Topic）来重现生产环境中发生过的问题。
* 挑选一个时间段，针对一部分流量，对其涉及的服务之间的调用注入一些特定的延时。
* 方法级别的混乱（运行时注入）：让方法随机抛出各种异常。
* 代码插入：在目标程序中插入一些指令，使得故障注入在这些指令之前先运行。
* 强迫系统节点间的时间彼此不同步。
* 在驱动程序中执行模拟I/O错误的程序。
* 让一个Elasticsearch集群的CPU超负荷。

混沌工程实验的机会是无限的，可能会根据您的分布式系统的架构和您组织的核心业务价值而有所不同。

2. 实施混沌工程的先决条件

要确定您的组织是否已准备好开始采用Chaos Engineering，您需要回答一个问题：您的系统是否能够适应现实世界中的事件，例如服务故障和网络延迟峰值？

如果您知道答案是“否”，您还有一些工作要做。Chaos Engineering非常适合揭露生产系统中未知的弱点，但如果您确定混沌工程实验会导致系统出现严重问题，那么运行该实验就没有任何意义。先解决这个弱点。然后回到Chaos Engineering，它将发现你不了解的其他弱点，或者它会让你更有信心你的系统实际上是有弹性的。混沌工程的另一个基本要素是可用于确定系统当前状态的监控系统。如果不了解系统的行为，您将无法从实验中得出结论。

### 混沌工程原则

“混乱”一词让我们想起随机性和无序性。然而，这并不意味着混沌工程的实施也是随机和随意的，也不意味着混沌工程师的工作就是引发混乱。相反的是，我们把混沌工程视为一门原则性很强的学科，特别是一门实验性的学科。

在上面的引用中，Dekker观测了分布式系统的整体行为，他也主张从整体上了解复杂系统是如何失效的。我们不应该仅仅着眼于发生故障的组件，而是应该尝试去理解，像组件交互中一些偶发的意外行为，最终是如何导致系统整体滑向一个不安全、不稳定的状态的。

你可以将混沌工程视为一种解决“我们的系统离混乱边缘有多少距离”的经验方法。从另一个角度去思考，“如果我们把混乱注入系统，它会怎么样？”
在这一部分，我们会介绍混沌工程实验的基本设计方法，之后会讨论一些更高级的原则。这些原则建立在真正实施混沌工程的大规模系统之上。在实施混沌工程的过程中，并不是所有高级原则都必须用到。但我们发现，运用的原则越多，你对系统弹性的信心就越充足。

软件系统里并没有类似的传递函数。像很多复杂系统一样，我们无法为软件系统表现出的各种行为建立一个预测模型。如果我们有这样一个模型，可以推导出一次网络延迟骤升会给系统带来什么影响，那就太完美了。但不幸的是，迄今为止我们并没有发现这样一个模型。

因为我们缺乏这样一个理论的预测模型，因此就不得不通过经验方法来了解在不同的情况下我们的系统会如何表现。我们通过在系统上运行各种各样的实验来了解系统的表现。我们尝试给系统制造各种麻烦，看它会发生什么状况。

但是，我们肯定不会给系统不同的随机输入。我们在系统分析之后，期望能够最大化每个实验可以获得的信息。正如科学家通过实验来研究自然现象一样，我们也通过实验来揭示系统的行为。

在开发混沌工程实验时，请牢记以下原则，它们将有助于实验的设计。

 建立稳定状态的假设。

 用多样的现实世界事件做验证。

 在生产环境中进行实验。

 自动化实验以持续运行。

 最小化爆炸半径。

#### 建立稳定状态假设

任何复杂系统都会有许多可变动的部件、许多信号，以及许多形式的输出。我们需要用一个通用的方式来区分系统行为是在预料之内的，还是在预料之外的。我们可以将系统正常运行时的状态定义为系统的“稳定状态”。

很多现有的数据采集框架已经默认采集大量的系统级别指标，所以通常来说，让你的系统有能力抓取业务级别的指标比抓取系统级别的指标更难。然而花精力来采集业务级别的指标是值得的，因为它们才能真实地反映系统的健康状况。这些指标获取的延迟越低越好：那些在月底算出来的业务指标和系统今天的健康状况毫无关系。

在选择指标时，你需要平衡以下几点：
 指标和底层架构的关系。
 收集相关数据需要的工作量。
 指标和系统接下来的行为之间的时间延迟。

如果你还不能直接获得和业务直接相关的指标，则也可以先暂时利用一些系统指标，比如系统吞吐率、错误率、99%以上的延迟等。你选择的指标和自己的业务关系越强，得到的可以采取可执行策略的信号就越强。你可以把这些指标想象成系统的生命特征指标，如脉搏、血压、体温等。同样重要的是，在客户端验证一个服务产生的警报可以提高整体效率，并可以作为对服务器端指标的补充，以构成某一时刻用户体验的完整画面。

#### 用多样的现实世界事件做验证

每个系统，从简单到复杂，只要运行时间足够长，都会受到不可预测的事件和条件的影响。例如，负载的增加、硬件故障、软件缺陷，以及非法数据（有时称为脏数据）的引入。我们无法穷举所有可能的事件或条件，但常见的有以下几类：
 硬件故障。
 功能缺陷。
 状态转换异常（例如发送方和接收方的状态不一致）。
 网络延迟和分区。
 上行或下行输入的大幅波动以及重试风暴。
 资源耗尽。
 服务之间不正常的或者预料之外的组合调用。
 拜占庭故障（例如性能差或有异常的节点发出有错误的响应、异常的行为、对调用者随机地返回不同的响应等）。
 资源竞争条件。
 下游依赖故障。
也许最复杂的情况是上述事件的各类组合导致系统发生异常行为。

要彻底阻止对可用性的各种威胁是不可能的，但是我们可以尽可能地减轻这些威胁。在决定引入哪些事件的时候，我们应当估算这些事件发生的频率和影响范围，权衡引入它们的成本和复杂度。在Netflix，我们选择关闭节点的一方面原因是，节点中断在现实中发生的频率很高，而引入关闭节点事件的成本和难度很低。对于区域故障来说，即使引入一些事件的成本高昂且引入流程复杂，我们还是必须要做，因为区域性故障对用户的影响是巨大的，除非我们有足够的弹性应对它。

文化因素也是一种成本。例如在传统数据中心的文化中，基础设施和系统的健壮性、稳定性高于一切，所以传统数据中心通常会对变更进行严格的流程控制。这种流程控制，和频繁关闭节点的操作是一对天然的矛盾体。随机关闭节点的实验对传统数据中心的文化是一种挑战。随着服务从数据中心迁移到云上，管理基础设施的职责被转移给了云服务提供商，硬件的各类故障都由云服务平台管理，工程部门对硬件故障就越来越习以为常。这种认知实际上在鼓励一种将故障当作可预料的态度，这种态度可以进一步推动混沌工程的引入和实施。虽然硬件故障并不是导致线上事故最常见的原因，但是注入硬件故障是在组织中引入混沌工程并获益的一个较简单的途径。

和硬件故障一样，一些现实世界的事件也可以被直接注入，例如每台机器的负载增加、通信延迟、网络分区、证书失效、时间偏差、数据膨胀等。除此之外的一些事件的注入可能会具有技术或文化上的障碍，所以我们需要寻找其他方法来看一看它们会如何影响生产环境。[1]例如，发布有缺陷的代码。金丝雀发布可以阻止许多显而易见的简单软件缺陷被大规模发布到生产环境中，但其并不能阻止全部的缺陷被发布出去。故意发布有缺陷的代码风险太大，可能会给用户带来严重的影响（参见第7章）。要模拟这类发布所带来的缺陷问题，一种办法是对相应的服务调用注入异常。

#### 在生产环境中进行实验

在我们这个行业里，在生产环境中进行软件验证的想法通常都会被嘲笑。“我们要在生产环境中验证”这句话更像是黑色幽默，它可以被翻译成“我们在发布之前不打算完整地验证这些代码”。

经典测试的一般信条是，寻找软件缺陷要离生产环境越远越好。例如，在单元测试中发现缺陷比在集成测试中发现更好。这里的逻辑是，离生产环境的整个部署越远，就越容易找到缺陷的根本原因并将其彻底修复。如果你曾经分别在单元测试、集成测试和生产环境中调试过问题，上述逻辑的好处就不言而喻了。

但是在混沌工程领域，整个策略却要反过来。在离生产环境越近的地方进行实验越好。理想的实践就是直接在生产环境中进行实验。

在传统的软件测试中，我们是在验证代码逻辑的正确性，是在对函数和方法的行为有良好理解的情况下，写测试来验证它们对不对。换句话说，是在验证代码写得对不对。

而当进行混沌工程的实验时，我们所感兴趣的是整个系统作为一个整体的行为。代码只是整个系统中比较重要的一部分，而除了代码，整个系统还包含很多其他方面，特别是状态、输入，以及第三方系统导致的难以预见的系统行为。

下面来深入了解一下为什么在生产环境中进行实验对混沌工程来说是至关重要的。我们要在生产环境中建立对系统的信心，所以当然需要在生产环境中进行实验。否则，我们就仅仅是在其他并不太关心的环境中建立对系统的信心，这会大大削弱这些实践的价值。

即便你不能在生产环境中进行实验，也要尽可能地在离生产环境最近的环境中进行。越接近生产环境，对实验外部有效性的威胁就越少，对实验结果的信心就越足。

#### 自动化实验以持续运行

手动执行一次性的实验是非常好的第一步。当我们想出寻找故障空间的新方法时，经常从手动的方法开始，小心谨慎地处理每一件事以期建立对实验和对系统的信心。所有当事人都聚集在一起，并向CORE（Critical Operations Response Engineering，Netflix的SRE团队的名称）发出一个警示信息，说明一个新的实验即将开始。

这种谨小慎微的态度有利于：a）正确运行实验，b）确保实验有最小的爆炸半径。在成功执行实验后，下一步就是将这个实验自动化，让其持续运行。

如果一个实验不是自动化的，那么就可以将这个实验废弃。

#### 最小化爆炸半径

我们经常运行本来只会影响一小部分用户的测试，却由于级联故障无意中影响到了更多的用户。在这些情况下，我们不得不立即中断实验。虽然我们绝不想发生这种情况，但随时遏制和停止实验的能力是必备的，这可以避免造成更大的危机。我们的实验通过很多方法来探寻故障会造成的未知的和不可预见的影响，所以关键在于如何让这些薄弱环节曝光出来而不会因意外造成更大规模的故障。我们称之为“最小化爆炸半径”。

能带来最大信心的实验也是风险最大的，是对所有生产流量都有影响的实验。而混沌工程实验应该只承受可以衡量的风险，并采用递进的方式，进行的每一步实验都在前一步的基础之上。这种递进的方式不断增加我们对系统的信心，而不会对用户造成过多不必要的影响。

最小风险的实验只作用于很少的用户。为此在我们验证客户端功能时只向一小部分终端注入故障。这些实验仅限于影响一小部分用户或一小部分流程。它们不能代表全部生产流量，但却是很好的早期指标。例如，如果一个网站无法通过早期实验，那么就没有必要影响其余的大量真实用户。

在自动化实验成功之后（或者在少量的设备验证没有涵盖要测试的功能时），下一步就是运行小规模的扩散实验。这种实验会影响一小部分用户，因为我们允许这些流量遵循正常的路由规则，所以它们最终会在生产服务器上均匀分布。对于此类实验，你需要用定义好的成功指标[1]来过滤所有被影响的用户，以防实验的影响被生产环境的噪声掩盖。[2]小规模扩散实验的优势在于，它不会触及生产环境的阈值，例如断路器的阈值，这样你便可以验证每一个单一请求的超时和预案。这可以验证系统对瞬时异常的弹性。

接下来是进行小规模的集中实验，通过修改路由策略将所有实验覆盖的用户流量导向特定的节点。在这些节点上会做高度集中的故障、延迟等测试。在这里，我们会允许断路器打开，同时将隐藏的资源限制暴露出来。如果我们发现有无效的预案或者奇怪的锁竞争等情况导致服务中断，那么只有实验覆盖的用户会受到影响。这个实验模拟生产环境中的大规模故障，同时可以把负面影响控制到最小，结果却能使我们对系统建立高度的信心。

风险最大但最准确的实验是无自定义路由的大规模实验。在这个实验级别，实验结果应该在主控制台上显示，同时因为断路器和共享资源的限制，实验可能会影响不在实验覆盖范围内的用户。当然，没有什么比让所有生产环境中的用户都参与实验，能给你更多关于系统可以抵御特定故障场景的确定性了。

除了不断扩大实验范围，在实验造成过多危害时及时终止实验也是必不可少的。有些系统设计会使用降级模式来给用户带来较小的影响，这还好，但是在系统完全中断服务的时候，就应该立即终止实验。这可以由之前讨论过的“大红色按钮”来处理。

我们强烈建议实施自动终止实验，尤其是在定期自动执行实验的情况下。关于弄清楚如何构建一个可以实时监控我们感兴趣的指标，并可以随时实施混沌工程实验的系统，这完全依赖于你手上的独特的系统构造。

## 10.13 [混沌工程工具chaos-mesh](https://www.zhihu.com/question/364324836/answer/967459860)

道理大家都懂，但是实际做起来最大的问题在于如何将整个流程自动化，手动搞的效率很低。比如很多时候搭建环境其实才是最麻烦的环节。我们发现整个行业都在说 Chaos Engineering，但是一直缺乏一个好用的开源的工具实践这个理念，所以 ChaosMesh 就诞生在这个背景下，ChaosMesh 的一些设计理念：

1. All-in K8s，使用 K8s 原生的 API 和惯例
2. 对应用无侵入性，不应该有任何领域知识（包括JVM）
3. 尽可能模拟硬件层面的异常，平台需要能够方便的加入新 chaos 类型
4. 可视化，一目了然
5. 开源，社会化的运营

K8s 已经基本是服务编排和调度的事实标准，对我们来说，K8s 对我们的意义是：

1. 应用程序的运行时环境标准化
2. Sidecar 的模式让我们可以对应用没有侵入性的情况下方便的注入错误
3. 更好的复用底层测试集群的物理资源

同时我们希望我们注入的错误是更接近真实世界会出现的问题，例如不仅仅能够模拟底层的[物理机](https://www.zhihu.com/search?q=物理机&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A967459860})断电，甚至能模拟网络的抖动，网络隔离（[脑裂](https://www.zhihu.com/search?q=脑裂&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A967459860})），IO 的抖动（磁盘忽快忽慢，IO 接口不定期返回错误之类的）。



Apache APISIX 已经使用 Chao Mesh 来提升稳定性，下面介绍两个使用场景。

首先简单介绍下 Apache APISIX。Apache APISIX 是一个动态、实时、高性能的开源 API 网关，提供负载均衡、动态上游、灰度发布、服务熔断、身份认证、可观测性等丰富的流量管理功能。

APISIX 是 Apache 基金会下的顶级项目，目前在生产环境中已经通过每日几百亿次请求量的考验。随着社区的发展，Apache APISIX 的功能越来越多，需要与外部组件产生的交互也越来越多，随之而来的不确定性呈指数级增长。在社区中，我们也收到了用户反馈的一些问题，这里举两个例子。

**场景一**

在 Apache APISIX 的配置中心， etcd 与 Apache APISIX 之间出现意外的高网络延迟时，Apache APISIX 能否仍然正常运行进行流量过滤转发？

**场景二**

用户在 issue 反馈，当 etcd 集群中的一个节点失效而集群仍然可以正常运行时，会出现与 Apache APISIX admin API 交互报错的情况。

尽管 Apache APISIX 在 CI 中通过单元 / e2e / fuzz 测试覆盖了大部分情景，然而尚未覆盖到与外部组件的交互。当发生网络波动、硬盘故障、或是进程被杀掉等难以预料的异常行为时，Apache APISIX 能否给出合适的错误信息、是否可以保持或自行恢复到正常的运行状态呢？

**如何在 APISIX 上应用混沌工程**

混沌工程在单纯的注入故障以外，逐渐形成了一套完整的方法论。根据 Principle of Chaos Engineering 的推荐，部署混沌工程实验需要五个步骤：

1. 定义稳态，即找到一个证明正常运行的可量化指标。
2. 做出假设，假设指标在实验组和对照组都始终保持稳定状态。
3. 设计实验，引入运行中可能出现的故障。
4. 验证假设，即通过比较实验组和对照组的结果证伪假设。
5. 修复问题。

接下来以上述两个用户反馈场景为例，依照这五个步骤为大家介绍 Apache APISIX 应用混沌工程的流程。

**场景一**



![img](https://pic1.zhimg.com/50/v2-13cac25727ef5ba63f71c39026641fba_720w.jpg?source=1940ef5c)![img](https://pic1.zhimg.com/80/v2-13cac25727ef5ba63f71c39026641fba_720w.jpg?source=1940ef5c)





用一幅图来描述这个场景。对照上面的五个步骤，首先需要找到衡量 Apache APISIX 正常运行的可量化指标。在测试时最主要的方法是利用 Grafana 对 Apache APISIX 运行指标进行监测，找到可衡量的指标后，在 CI 中就可以从 Prometheus 中单独提取数据进行比较判断，这里使用了路由转发的 Request per Second（RPS）和 etcd 的可[连接性](https://www.zhihu.com/search?q=连接性&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2219797728}) 作为评价指标。另一点就是需要对日志进行分析，对于 Apache APISIX 就是查看 Nginx 的 [error.log](https://www.zhihu.com/search?q=error.log&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2219797728}) 判断是否有报错以及报错是否符合预期。

在对照组也就是引入 Chaos 前进行实验，检测 set/get route 均能成功，etcd 可连接，并记录此时的 RPS。之后，使用 network chaos 添加 5s 的网络延迟 ，再次进行实验，此时 set route 失败，get route 成功，etcd 无法连接，RPS与之前相比无明显变化。实验符合预期。

**场景二**



![img](https://pic1.zhimg.com/50/v2-a32ac45309f5d0fab0a115184c83ad45_720w.jpg?source=1940ef5c)![img](https://pic1.zhimg.com/80/v2-a32ac45309f5d0fab0a115184c83ad45_720w.jpg?source=1940ef5c)



进行同样的对照组实验之后引入 pod-kill chaos，复现了预期的错误。在随机删除集群中少数 [etcd](https://www.zhihu.com/search?q=etcd&search_source=Entity&hybrid_search_source=Entity&hybrid_search_extra={"sourceType"%3A"answer"%2C"sourceId"%3A2219797728}) 节点的情况下，etcd 可连接性表现出时有时无，日志则打印出了大量连接拒绝的报错。更加有趣的是，在删除 etcd 端点列表的第一个或第三个节点时，设置路由正常返回，而只有在删除 etcd 端点列表中的第二个节点时，设置路由会报错 “connection refused”。

排查发现原因在于 Apache APISIX 使用的 etcd lua API 选择端点时并不是随机而是顺序选择，因此新建 etcd client 进行的操作就相当于只绑定在一个 etcd 端点上导致持续性的失败。修复这个问题之后，还为 etcd lua API 添加了健康检查，确保不会在断开连接的 etcd 上进行大量的重复；以及增加了 etcd 集群完全断开连接时的回退检查，避免大量报错冲爆日志。

**未来计划**

1. **借助 e2e 模拟场景进行混沌测试**
   目前在 Apache APISIX 中，仍然主要依靠人来识别系统中可能的脆弱点进行测试修复。尽管在 CI 中测试，无需担心混沌工程的故障半径对生产环境的影响，但同时也无法覆盖生产环境中的复杂而全面的场景。为了覆盖更多的场景，未来社区计划利用现有的 e2e 测试模拟更加完整的场景，进行更大范围、更强随机性的混沌测试。
2. **为更多 Apache APISIX 项目添加混沌测试**
   除了为 Apache APISIX 找到更多可能的脆弱点之外，社区还计划为 Apache APISIX Dashboard 和 Apache APISIX Ingress Controller 等更多项目添加混沌测试。
3. **为 Chaos Mesh 添加功能**
   在部署 Chaos Mesh 时遇见一些暂不支持的功能，包括网络延迟的目标不支持选择 service，网络混沌无法指定容器端口注入等，Apache APISIX 社区未来也会协助 Chaos Mesh 添加相关功能。希望开源社区都会越来越好。

- Apache APISIX Github：[https://github.com/apache/apisix](https://link.zhihu.com/?target=https%3A//github.com/apache/apisix) 
- Apache APISIX 官网：[http://apisix.apache.org/zh](https://link.zhihu.com/?target=http%3A//apisix.apache.org/zh/)

## 10.14 [MockServer](https://www.mock-server.com/)

### 为什么使用MockServer

MockServer允许您通过HTTP或HTTPS模拟任何服务器或服务，例如REST或RPC服务。

这在以下情况下很有用：

- 测试
  - 轻松为 HTTP 依赖项（如 REST 或 RPC 服务）重新创建所有类型的响应，以便轻松、有效地测试应用程序
  - 隔离被测系统，以确保测试可靠运行，并且仅在存在真正的错误时才失败。重要的是，只有被测系统才被测试，而不是它的依赖关系，以避免由于不相关的外部更改（如网络故障或服务器重新启动/重新部署）而导致测试失败。
  - 轻松为每个测试独立设置模拟响应，以确保测试数据与每个测试一起封装。避免在难以管理和维护的测试之间共享数据，并避免测试相互感染的风险
  - 创建测试断言，以验证被测系统已发送的请求
- 解耦开发
  - 在服务可用之前，开始针对服务 API 工作。如果API或服务尚未完全开发，MockServer可以模拟API，允许使用该服务的任何团队在不延迟的情况下开始工作。
  - 在初始开发阶段隔离开发团队，此时API/服务可能非常不稳定和不稳定。使用 MockServer 允许开发工作继续进行，即使外部服务出现故障
- 隔离单个服务
  - 在部署和调试期间，在调试模式下运行单个应用程序或服务或在本地计算机上处理请求子集会很有帮助。使用MockServer，可以轻松地[有选择地将请求转发到](https://www.mock-server.com/mock_server/isolating_single_service.html)在调试模式下运行的本地进程，所有其他请求都可以转发到实际服务，例如在QA或UAT环境中运行



## 10.15 [MockServer简介](https://stornado.github.io/2020/08/26/mockserver-intro/)

服务端测试中，被测服务通常依赖于一系列的外部模块，被测服务与外部模块间通过REST API调用来进行通信。要对被测服务进行系统测试，一般做法是，部署好所有外部依赖模块，由被测服务直接调用。然而有时被调用模块尚未开发完成，或者调用返回不好构造，这将影响被测系统的测试进度。为此我们需要开发桩模块，用来模拟被调用模块的行为。最简单的方式是，对于每个外部模块依赖，都创建一套桩模块。然而这样的话，桩模块服务将非常零散，不便于管理。Mock Server为解决这些问题而生，其提供配置request及相应response方式来实现通用桩服务。本文将专门针对REST API来进行介绍Mock Server的整体结构及应用案例。

![img](https://stornado.github.io/2020/08/26/mockserver-intro/311516-20170912180553860-879452344.png)

### MockServer支持能力

#### 1. 返回“模拟”响应

![Title:Response Action Expectation](https://stornado.github.io/2020/08/26/mockserver-intro/expectation_response_action.png)

#### 2. 执行回调，动态创建响应

![Title:Callback Action Expectation](https://stornado.github.io/2020/08/26/mockserver-intro/expectation_callback_action.png)

#### 3. 返回异常或无效响应

![Title:Error Action Expectation](https://stornado.github.io/2020/08/26/mockserver-intro/expectation_error_action.png)



### MockServer支持匹配规则

#### 请求**属性匹配器**使用以下一个或多个属性匹配请求：

- **method**-[属性匹配器](https://www.mock-server.com/mock_server/creating_expectations.html#request_property_matchers)
- **path**-[属性匹配器](https://www.mock-server.com/mock_server/creating_expectations.html#request_property_matchers)
- **path parameters**-[多个值匹配器的键](https://www.mock-server.com/mock_server/creating_expectations.html#request_key_to_multivalue_matchers)
- **query string parameters**-[多个值匹配器的键](https://www.mock-server.com/mock_server/creating_expectations.html#request_key_to_multivalue_matchers)
- **headers**-[多个值匹配器的键](https://www.mock-server.com/mock_server/creating_expectations.html#request_key_to_multivalue_matchers)
- **cookies**-[单个值匹配器的关键](https://www.mock-server.com/mock_server/creating_expectations.html#request_key_to_value_matchers)
- **body**-[请求体匹配器](https://www.mock-server.com/mock_server/creating_expectations.html#request_body_matchers)
- **secure**- 布尔值，为 true 时 使用 HTTPS。



# 11. Linux

## 11.1 [服务器开发——理解异步I/O](https://zhuanlan.zhihu.com/p/428175475)

### 3、epoll

#### 3.1、poll(select)的限制

Poll函数起源于SVR3，最初局限于流设备，SVR4取消了这种限制。总是来说，poll比select要高效一些，但是，它有可移植性问题，例如，windows就只支持select。

select模型与此类例。内核必须遍历所有监视的描述符，而应用程序也必须遍历所有描述符，检查哪些描述符已经准备好。当描述符成百上千时，会变得非常低效——这是select(poll)模型低效的根源所在。考虑这些情况，2.6以后的内核都引进了epoll模型。

### 3.2、核心数据结构与接口

Epoll模型由3个函数构成，epoll_create、epoll_ctl和epoll_wait。

#### 3.2.1创建epoll实例(Creating a New Epoll Instance)

epoll环境通过epoll_create函数创建：

```text
     #include <sys/epoll.h>
      int epoll_create (int size)
```

调用成功则返回与实例关联的文件描述符，该文件描述符与真实的文件没有任何关系，仅作为接下来调用的函数的句柄。size是给内核的一个提示，告诉内核将要监视的文件描述符的数量，它不是最大值；但是，传递合适的值能够提高系统性能。发生错误时，返回-1。

#### **3.2.2、控制epoll(Controlling Epoll)**

通过epoll_ctl，可以加入文件描述符到epoll环境或从epoll环境移除文件描述符。

```text
#include <sys/epoll.h>
int epoll_ctl (int epfd,
               int op,
               int fd,
               struct epoll_event *event);

struct epoll_event {
        _ _u32 events;  /* events */
        union {
                void *ptr;
                int fd;
                _ _u32 u32;
                _ _u64 u64;
        } data;
};
```

epfd为epoll_create返回的描述符

#### **3.2.3、等待事件(Waiting for Events with Epoll)**

```text
#include <sys/epoll.h>
int epoll_wait (int epfd,
                struct epoll_event *events,
                int maxevents,
                int timeout);
```

等待事件的产生，类似于select()调用。参数events用来从内核得到事件的集合，maxevents告之内核这个events有多大，这个 maxevents的值不能大于创建epoll_create()时的size，参数timeout是超时时间（毫秒，0会立即返回，-1将不确定，也有 说法说是永久阻塞）。该函数返回需要处理的事件数目，如返回0表示已超时。

### 3.4、Level-Triggered与Edge-Triggered Events

epoll有2种工作方式：LT和ET：

LT(level triggered)是缺省的工作方式，并且同时支持block和no-block socket。在这种做法中，内核告诉你一个文件描述符是否就绪了，然后你可以对这个就绪的fd进行IO操作。如果你不作任何操作，内核还是会继续通知你的，所以，这种模式编程出错误可能性要小一点。传统的select/poll都是这种模型的代表。

ET (edge-triggered)是高速工作方式，只支持no-block socket。在这种模式下，当描述符从未就绪变为就绪时，内核通过epoll告诉你。然后它会假设你知道文件描述符已经就绪，并且不会再为那个文件描述符发送更多的就绪通知，直到你做了某些操作导致那个文件描述符不再为就绪状态了(比如，你在发送，接收或者接收请求，或者发送接收的数据少于一定量时导致了一个EWOULDBLOCK 错误）。但是请注意，如果一直不对这个fd作IO操作(从而导致它再次变成未就绪)，内核不会发送更多的通知(only once)，不过在TCP协议中，ET模式的加速效用仍需要更多的benchmark确认。

## 11.2 [linux fuse 阻塞,FUSE原理总结](https://blog.csdn.net/weixin_30356433/article/details/116964336)

Fuse是filesystem in user space，一个用户空间的文件系统框架，允许非特权用户建立功能完备的文件系统，而不需要重新编译内核。fuse模块仅仅提供内核模块的入口，而本身的主要实现代码位于用户空间中。对于读写虚拟文件系统来讲，fuse是个很好的选择。fuse包含包含一个内核模块和一个用户空间守护进程，将大部分的VFS调用都委托一个专用的守护进程来处理。

**工作原理**

Fuse用户空间文件系统与真实的文件系统不同，它的supper block, inode, dentry等都是由内存虚拟而来，具体在物理磁盘上存储的真实文件结构是什么，它不关心，且对真实数据的请求通过驱动和接口一层层传递到用户空间的用户编写的具体实现程序里来，这样就为用户开发自己的文件系统提供了便利，这也就是所谓的“用户空间文件系统”的基本工作理念。

**模块架构**

FUSE分为三大模块：

Ø FUSE内核模块(内核态)

​	FUSE内核模块实现VFS接口(实现fuse文件驱动模块的注册、fuse的(虚拟)设备驱动、提供supper block、dentry、inode的维护)，接收来至后者的请求,传递给LibFUSE，LibFUSE再传递给我们用户程序的接口进行实现操作。

Ø LibFUSE模块(用户态)

​	LibFUSE实现文件系统主要框架、对“用户实现的文件系统操作代码“的封装、mount管理、通过字符设备/dev/fuse与内核模块通信；

Ø 用户程序模块(用户态)

​	用户程序在用户空间实现LibFUSE库封装的文件系统操作；

## 11.3 [说一下用户态和内核态是如何切换的](https://blog.csdn.net/weixin_45785536/article/details/122821842)

### 什么是用户态和内核态

用户态和内核态是操作系统的两种运行状态。

内核态：处于内核态的 CPU 可以访问任意的数据，包括外围设备，比如网卡、硬盘等，处于内核态的 CPU 可以从一个程序切换到另外一个程序，并且占用 CPU 不会发生抢占情况，一般处于特权级 0 的状态我们称之为内核态。
用户态：处于用户态的 CPU 只能受限的访问内存，并且不允许访问外围设备，用户态下的 CPU 不允许独占，也就是说 CPU 能够被其他程序获取。

### 那么为什么要有用户态和内核态呢

这个主要是访问能力的限制的考量，计算机中有一些比较危险的操作，比如设置时钟、内存清理，这些都需要在内核态下完成，如果随意进行这些操作，那你的系统得崩溃多少次。

### 外中断和异常有什么区别

外中断是指由 CPU 执行指令以外的事件引起，如 I/O 完成中断，表示设备输入/输出处理已经完成，处理器能够发送下一个输入/输出请求。此外还有时钟中断、控制台中断等。

而异常时由 CPU 执行指令的内部事件引起，如非法操作码、地址越界、算术溢出等。



## 11.4 [Socket原理](https://blog.csdn.net/weixin_44862085/article/details/112917595)

[socket API的使用+client/server代码演示+封装socket模块](https://blog.csdn.net/crr411422/article/details/130580281)

### **１、什么是Socket**

在计算机通信领域，[socket](https://so.csdn.net/so/search?q=socket&spm=1001.2101.3001.7020) 被翻译为“套接字”，它是计算机之间进行通信的一种约定或一种方式。通过 socket 这种约定，一台计算机可以接收其他计算机的数据，也可以向其他计算机发送数据
　　socket起源于Unix，而Unix/Linux基本哲学之一就是“一切皆文件”，都可以用“打开open –> 读写write/read –> 关闭close”模式来操作。
　　我的理解就是Socket就是该模式的一个实现：即socket是一种特殊的文件，一些socket函数就是对其进行的操作（读/写IO、打开、关闭）。
　　Socket()函数返回一个整型的Socket描述符，随后的连接建立、数据传输等操作都是通过该Socket实现的。

### **２、网络中进程如何通信**

既然Socket主要是用来解决网络通信的，那么我们就来理解网络中进程是如何通信的。

#### 2.1、本地进程间通信

```
    a、消息传递（管道、消息队列、FIFO）
	b、同步（互斥量、条件变量、读写锁、文件和写记录锁、信号量）？【不是很明白】
	c、共享内存（匿名的和具名的，eg:channel）
	d、远程过程调用(RPC)。
```

#### **2.2、网络中进程如何通信**

我们要理解网络中进程如何通信，得解决两个问题：
		a、我们要如何标识一台主机，即怎样确定我们将要通信的进程是在那一台主机上运行。
　　ｂ、我们要如何标识唯一进程，本地通过pid标识，网络中应该怎样标识？
解决办法：
　　ａ、TCP/IP协议族已经帮我们解决了这个问题，网络层的“ip地址”可以唯一标识网络中的主机
　　ｂ、传输层的“协议+端口”可以唯一标识主机中的应用程序（进程），因此，我们利用三元组（ip地址，协议，端口）就可以标识网络的进程了，网络中的进程通信就可以利用这个标志与其它进程进行交互

### ３、Socket怎么通信

现在，我们知道了网络中进程间如何通信，即利用三元组【ip地址，协议，端口】可以进行网络间通信了，那我们应该怎么实现了，因此，我们socket应运而生，它就是利用三元组解决网络通信的一个中间件工具，就目前而言，几乎所有的应用程序都是采用socket，如UNIX BSD的[套接字](https://so.csdn.net/so/search?q=套接字&spm=1001.2101.3001.7020)（socket）和UNIX System V的TLI（已经被淘汰）。

Socket通信的数据传输方式，常用的有两种：
　　ａ、SOCK_STREAM：表示面向连接的数据传输方式。数据可以准确无误地到达另一台计算机，如果损坏或丢失，可以重新发送，但效率相对较慢。常见的 http 协议就使用 SOCK_STREAM 传输数据，因为要确保数据的正确性，否则网页不能正常解析。
　　ｂ、SOCK_DGRAM：表示无连接的数据传输方式。计算机只管传输数据，不作数据校验，如果数据在传输中损坏，或者没有到达另一台计算机，是没有办法补救的。也就是说，数据错了就错了，无法重传。因为 SOCK_DGRAM 所做的校验工作少，所以效率比 SOCK_STREAM 高。
　　例如：QQ 视频聊天和语音聊天就使用 SOCK_DGRAM 传输数据，因为首先要保证通信的效率，尽量减小延迟，而数据的正确性是次要的，即使丢失很小的一部分数据，视频和音频也可以正常解析，最多出现噪点或杂音，不会对通信质量有实质的影响

### ４、TCP/IP协议

##### **4.1、概念**

TCP/IP【TCP（传输控制协议）和IP（网际协议）】提供点对点的链接机制，将数据应该如何封装、定址、传输、路由以及在目的地如何接收，都加以标准化。它将软件通信过程抽象化为四个抽象层，采取协议堆栈的方式，分别实现出不同通信协议。协议族下的各种协议，依其功能不同，被分别归属到这四个层次结构之中，常被视为是简化的七层OSI模型。

它们之间好比送信的线路和驿站的作用，比如要建议送信驿站，必须得了解送信的各个细节。

TCP（Transmission Control Protocol，传输控制协议）是一种面向连接的、可靠的、基于字节流的通信协议，数据在传输前要建立连接，传输完毕后还要断开连接，客户端在收发数据前要使用 connect() 函数和服务器建立连接。建立连接的目的是保证IP地址、端口、物理链路等正确无误，为数据的传输开辟通道。

TCP建立连接时要传输三个数据包，俗称三次握手（Three-way Handshaking）。可以形象的比喻为下面的对话：

```
[Shake 1] 套接字A：“你好，套接字B，我这里有数据要传送给你，建立连接吧。”
[Shake 2] 套接字B：“好的，我这边已准备就绪。”
[Shake 3] 套接字A：“谢谢你受理我的请求。
```

#### 4.2、TCP的粘包问题以及数据的无边界性：

[TCP粘包分析](https://blog.csdn.net/m0_37947204/article/details/80490512)

#### 4.4、TCP数据报结构：![在这里插入图片描述](https://img-blog.csdnimg.cn/img_convert/8de3edefeefb0bdf0cc5d954a42e45f1.png#pic_center)

带阴影的几个字段需要重点说明一下：
　　(1) 序号：Seq（Sequence Number）序号占32位，用来标识从计算机A发送到计算机B的数据包的序号，计算机发送数据时对此进行标记。
　　(2) 确认号：Ack（Acknowledge Number）确认号占32位，客户端和服务器端都可以发送，Ack = Seq + 1。
　　(3) 标志位：每个标志位占用1Bit，共有6个，分别为 URG、ACK、PSH、RST、SYN、FIN，具体含义如下：

```
（1）URG：紧急指针（urgent pointer）有效。
（2）ACK：确认序号有效。
（3）PSH：接收方应该尽快将这个报文交给应用层。
（4）RST：重置连接。
（5）SYN：建立一个新连接。
（6）FIN：断开一个连接。
123456
```

#### 4.5、连接的建立（三次握手）：

使用 connect() 建立连接时，客户端和服务器端会相互发送三个数据包，请看下图：![在这里插入图片描述](https://img-blog.csdnimg.cn/img_convert/096ee1a2b3ecee522233f36af03c448b.png#pic_center)

客户端调用 socket() 函数创建套接字后，因为没有建立连接，所以套接字处于CLOSED状态；服务器端调用 listen() 函数后，套接字进入LISTEN状态，开始监听客户端请求
这时客户端发起请求：
　　1) 当客户端调用 connect() 函数后，TCP协议会组建一个数据包，并设置 SYN 标志位，表示该数据包是用来建立同步连接的。同时生成一个随机数字 1000，填充“序号（Seq）”字段，表示该数据包的序号。完成这些工作，开始向服务器端发送数据包，客户端就进入了SYN-SEND状态。
　　2) 服务器端收到数据包，检测到已经设置了 SYN 标志位，就知道这是客户端发来的建立连接的“请求包”。服务器端也会组建一个数据包，并设置 SYN 和 ACK 标志位，SYN 表示该数据包用来建立连接，ACK 用来确认收到了刚才客户端发送的数据包
　　服务器生成一个随机数 2000，填充“序号（Seq）”字段。2000 和客户端数据包没有关系。
　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　服务器将客户端数据包序号（1000）加1，得到1001，并用这个数字填充“确认号（Ack）”字段。
　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　服务器将数据包发出，进入SYN-RECV状态
　　3) 客户端收到数据包，检测到已经设置了 SYN 和 ACK 标志位，就知道这是服务器发来的“确认包”。客户端会检测“确认号（Ack）”字段，看它的值是否为 1000+1，如果是就说明连接建立成功。
　　接下来，客户端会继续组建数据包，并设置 ACK 标志位，表示客户端正确接收了服务器发来的“确认包”。同时，将刚才服务器发来的数据包序号（2000）加1，得到 2001，并用这个数字来填充“确认号（Ack）”字段。
　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　客户端将数据包发出，进入ESTABLISED状态，表示连接已经成功建立。
　　4) 服务器端收到数据包，检测到已经设置了 ACK 标志位，就知道这是客户端发来的“确认包”。服务器会检测“确认号（Ack）”字段，看它的值是否为 2000+1，如果是就说明连接建立成功，服务器进入ESTABLISED状态。
　　至此，客户端和服务器都进入了ESTABLISED状态，连接建立成功，接下来就可以收发数据了。

#### 4.6、TCP四次握手断开连接

建立连接非常重要，它是数据正确传输的前提；断开连接同样重要，它让计算机释放不再使用的资源。如果连接不能正常断开，不仅会造成数据传输错误，还会导致套接字不能关闭，持续占用资源，如果并发量高，服务器压力堪忧。
断开连接需要四次握手，可以形象的比喻为下面的对话：

```
[Shake 1] 套接字A：“任务处理完毕，我希望断开连接。”
[Shake 2] 套接字B：“哦，是吗？请稍等，我准备一下。”
等待片刻后……
[Shake 3] 套接字B：“我准备好了，可以断开连接了。”
[Shake 4] 套接字A：“好的，谢谢合作。”
```

下图演示了客户端主动断开连接的场景：
![在这里插入图片描述](https://img-blog.csdnimg.cn/img_convert/5053aedc705a6d3a8c830a46bdf7b3e0.png#pic_center)

建立连接后，客户端和服务器都处于ESTABLISED状态。这时，客户端发起断开连接的请求：

客户端调用 close() 函数后，向服务器发送 FIN 数据包，进入FIN_WAIT_1状态。FIN 是 Finish 的缩写，表示完成任务需要断开连接。
服务器收到数据包后，检测到设置了 FIN 标志位，知道要断开连接，于是向客户端发送“确认包”，进入CLOSE_WAIT状态。
注意：服务器收到请求后并不是立即断开连接，而是先向客户端发送“确认包”，告诉它我知道了，我需要准备一下才能断开连接。
客户端收到“确认包”后进入FIN_WAIT_2状态，等待服务器准备完毕后再次发送数据包。
等待片刻后，服务器准备完毕，可以断开连接，于是再主动向客户端发送 FIN 包，告诉它我准备好了，断开连接吧。然后进入LAST_ACK状态。
客户端收到服务器的 FIN 包后，再向服务器发送 ACK 包，告诉它你断开连接吧。然后进入TIME_WAIT状态。
服务器收到客户端的 ACK 包后，就断开连接，关闭套接字，进入CLOSED状态。

#### 4.7、关于 TIME_WAIT 状态的说明

客户端最后一次发送 ACK包后进入 TIME_WAIT 状态，而不是直接进入 CLOSED 状态关闭连接，这是为什么呢？

TCP 是面向连接的传输方式，必须保证数据能够正确到达目标机器，不能丢失或出错，而网络是不稳定的，随时可能会毁坏数据，所以机器A每次向机器B发送数据包后，都要求机器B”确认“，回传ACK包，告诉机器A我收到了，这样机器A才能知道数据传送成功了。如果机器B没有回传ACK包，机器A会重新发送，直到机器B回传ACK包。

客户端最后一次向服务器回传ACK包时，有可能会因为网络问题导致服务器收不到，服务器会再次发送 FIN 包，如果这时客户端完全关闭了连接，那么服务器无论如何也收不到ACK包了，所以客户端需要等待片刻、确认对方收到ACK包后才能进入CLOSED状态。那么，要等待多久呢？

数据包在网络中是有生存时间的，超过这个时间还未到达目标主机就会被丢弃，并通知源主机。这称为报文最大生存时间（MSL，Maximum Segment Lifetime）。TIME_WAIT 要等待 2MSL 才会进入 CLOSED 状态。ACK 包到达服务器需要 MSL 时间，服务器重传 FIN 包也需要 MSL 时间，2MSL 是数据包往返的最大时间，如果 2MSL 后还未收到服务器重传的 FIN 包，就说明服务器已经收到了 ACK 包

#### 4.８.优雅的断开连接–shutdown()

close()/closesocket()和shutdown()的区别
确切地说，close() / closesocket() 用来关闭套接字，将套接字描述符（或句柄）从内存清除，之后再也不能使用该套接字，与C语言中的 fclose() 类似。应用程序关闭套接字后，与该套接字相关的连接和缓存也失去了意义，TCP协议会自动触发关闭连接的操作。

shutdown() 用来关闭连接，而不是套接字，不管调用多少次 shutdown()，套接字依然存在，直到调用 close() / closesocket() 将套接字从内存清除。
调用 close()/closesocket() 关闭套接字时，或调用 shutdown() 关闭输出流时，都会向对方发送 FIN 包。FIN 包表示数据传输完毕，计算机收到 FIN 包就知道不会再有数据传送过来了。

默认情况下，close()/closesocket() 会立即向网络中发送FIN包，不管输出缓冲区中是否还有数据，而shutdown() 会等输出缓冲区中的数据传输完毕再发送FIN包。也就意味着，调用 close()/closesocket() 将丢失输出缓冲区中的数据，而调用 shutdown() 不会

### ５、OSI模型

TCP/IP对OSI的网络模型层进行了划分如下：
![在这里插入图片描述](https://img-blog.csdnimg.cn/img_convert/55791547dfdcf7786a8fd7150ee854d7.png#pic_center)

TCP/IP协议参考模型把所有的TCP/IP系列协议归类到四个抽象层中
　　应用层：TFTP，HTTP，SNMP，FTP，SMTP，DNS，Telnet 等等
　　传输层：TCP，UDP
　　网络层：IP，ICMP，OSPF，EIGRP，IGMP
　　数据链路层：SLIP，CSLIP，PPP，MTU
　　每一抽象层建立在低一层提供的服务上，并且为高一层提供服务，看起来大概是这样子的
　　
![20150615140707753.png](https://img-blog.csdnimg.cn/img_convert/310adccee3de28b3ff5964826d12be03.png#pic_center)

![20150615141705040.png](https://img-blog.csdnimg.cn/img_convert/0552dfe46647398d19bbfb241025aac3.png#pic_center)

### ６、Socket常用函数接口及其原理

图解socket函数：

![20150615150446559.png](https://img-blog.csdnimg.cn/img_convert/ab20eca8909a0dc009161766c2c557a9.png#pic_center)

![20150615150618996.jpeg](https://img-blog.csdnimg.cn/img_convert/b418fd50b366717b6c4a0b70fd3aed6c.png#pic_center)

#### 6.1、使用socket()函数创建套接字

```
int socket(int af, int type, int protocol);
1
```

1. af 为地址族（Address Family），也就是 IP 地址类型，常用的有 AF_INET 和 AF_INET6。AF 是“Address Family”的简写，INET是“Inetnet”的简写。AF_INET 表示 IPv4 地址，例如 127.0.0.1；AF_INET6 表示 IPv6 地址，例如 1030::C9B4:FF12:48AA:1A2B。
   大家需要记住127.0.0.1，它是一个特殊IP地址，表示本机地址，后面的教程会经常用到。
   type 为数据传输方式，常用的有 SOCK_STREAM 和 SOCK_DGRAM
   protocol 表示传输协议，常用的有 IPPROTO_TCP 和 IPPTOTO_UDP，分别表示 TCP 传输协议和 UDP 传输协议

#### 6.2、使用bind()和connect()函数

socket() 函数用来创建套接字，确定套接字的各种属性，然后服务器端要用 bind() 函数将套接字与特定的IP地址和端口绑定起来，只有这样，流经该IP地址和端口的数据才能交给套接字处理；而客户端要用 connect() 函数建立连接

```
int bind(int sock, struct sockaddr *addr, socklen_t addrlen);  
1
```

sock 为 socket 文件描述符，addr 为 sockaddr 结构体变量的指针，addrlen 为 addr 变量的大小，可由 sizeof() 计算得出
下面的代码，将创建的套接字与IP地址 127.0.0.1、端口 1234 绑定：

```
//创建套接字
int serv_sock = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
//创建sockaddr_in结构体变量
struct sockaddr_in serv_addr;
memset(&serv_addr, 0, sizeof(serv_addr));  //每个字节都用0填充
serv_addr.sin_family = AF_INET;  //使用IPv4地址
serv_addr.sin_addr.s_addr = inet_addr("127.0.0.1");  //具体的IP地址
serv_addr.sin_port = htons(1234);  //端口
//将套接字和IP、端口绑定
bind(serv_sock, (struct sockaddr*)&serv_addr, sizeof(serv_addr));
12345678910
```

connect() 函数用来建立连接，它的原型为：

```
int connect(int sock, struct sockaddr *serv_addr, socklen_t addrlen); 
1
```

#### 6.3、使用listen()和accept()函数

于服务器端程序，使用 bind() 绑定套接字后，还需要使用 listen() 函数让套接字进入被动监听状态，再调用 accept() 函数，就可以随时响应客户端的请求了。
通过** listen() 函数**可以让套接字进入被动监听状态，它的原型为：

```
int listen(int sock, int backlog); 
1
```

sock 为需要进入监听状态的套接字，backlog 为请求队列的最大长度。
所谓被动监听，是指当没有客户端请求时，套接字处于“睡眠”状态，只有当接收到客户端请求时，套接字才会被“唤醒”来响应请求。

请求队列
当套接字正在处理客户端请求时，如果有新的请求进来，套接字是没法处理的，只能把它放进缓冲区，待当前请求处理完毕后，再从缓冲区中读取出来处理。如果不断有新的请求进来，它们就按照先后顺序在缓冲区中排队，直到缓冲区满。这个缓冲区，就称为请求队列（Request Queue）。

缓冲区的长度（能存放多少个客户端请求）可以通过 listen() 函数的 backlog 参数指定，但究竟为多少并没有什么标准，可以根据你的需求来定，并发量小的话可以是10或者20。

如果将 backlog 的值设置为 SOMAXCONN，就由系统来决定请求队列长度，这个值一般比较大，可能是几百，或者更多。

当请求队列满时，就不再接收新的请求，对于 Linux，客户端会收到 ECONNREFUSED 错误

注意：listen() 只是让套接字处于监听状态，并没有接收请求。接收请求需要使用 accept() 函数。

当套接字处于监听状态时，可以通过 accept() 函数来接收客户端请求。它的原型为：

```
int accept(int sock, struct sockaddr *addr, socklen_t *addrlen); 
1
```

它的参数与 listen() 和 connect() 是相同的：sock 为服务器端套接字，addr 为 sockaddr_in 结构体变量，addrlen 为参数 addr 的长度，可由 sizeof() 求得。

accept() 返回一个新的套接字来和客户端通信，addr 保存了客户端的IP地址和端口号，而 sock 是服务器端的套接字，大家注意区分。后面和客户端通信时，要使用这个新生成的套接字，而不是原来服务器端的套接字。

最后需要说明的是：listen() 只是让套接字进入监听状态，并没有真正接收客户端请求，listen() 后面的代码会继续执行，直到遇到 accept()。accept() 会阻塞程序执行（后面代码不能被执行），直到有新的请求到来。

#### 6.4、socket数据的接收和发送

Linux下数据的接收和发送
Linux 不区分套接字文件和普通文件，使用 write() 可以向套接字中写入数据，使用 read() 可以从套接字中读取数据。

前面我们说过，两台计算机之间的通信相当于两个套接字之间的通信，在服务器端用 write() 向套接字写入数据，客户端就能收到，然后再使用 read() 从套接字中读取出来，就完成了一次通信。
write() 的原型为：

```
ssize_t write(int fd, const void *buf, size_t nbytes);
1
```

fd 为要写入的文件的描述符，buf 为要写入的数据的缓冲区地址，nbytes 为要写入的数据的字节数。
write() 函数会将缓冲区 buf 中的 nbytes 个字节写入文件 fd，成功则返回写入的字节数，失败则返回 -1。
read() 的原型为：

```
ssize_t read(int fd, void *buf, size_t nbytes);
1
```

fd 为要读取的文件的描述符，buf 为要接收数据的缓冲区地址，nbytes 为要读取的数据的字节数。

read() 函数会从 fd 文件中读取 nbytes 个字节并保存到缓冲区 buf，成功则返回读取到的字节数（但遇到文件结尾则返回0），失败则返回 -1。

#### 6.5、socket缓冲区以及阻塞模式

socket缓冲区
每个 socket 被创建后，都会分配两个缓冲区，输入缓冲区和输出缓冲区。

write()/send() 并不立即向网络中传输数据，而是先将数据写入缓冲区中，再由TCP协议将数据从缓冲区发送到目标机器。一旦将数据写入到缓冲区，函数就可以成功返回，不管它们有没有到达目标机器，也不管它们何时被发送到网络，这些都是TCP协议负责的事情。

TCP协议独立于 write()/send() 函数，数据有可能刚被写入缓冲区就发送到网络，也可能在缓冲区中不断积压，多次写入的数据被一次性发送到网络，这取决于当时的网络情况、当前线程是否空闲等诸多因素，不由程序员控制。

read()/recv() 函数也是如此，也从输入缓冲区中读取数据，而不是直接从网络中读取

![[外链图片转存失败,源站可能有防盗链机制,建议将图片保存下来直接上传(img-m6mKibU0-1611195817872)(https://upload-images.jianshu.io/upload_images/11362584-d4be2dd4bb3a26b8.jpeg?imageMogr2/auto-orient/strip%7CimageView2/2/w/697/format/webp#pic_center)]](https://img-blog.csdnimg.cn/img_convert/1662b03eb231add6305a2b2e486cde02.png#pic_center)

这些I/O缓冲区特性可整理如下：

```
（1）I/O缓冲区在每个TCP套接字中单独存在；
（2）I/O缓冲区在创建套接字时自动生成；
（3）即使关闭套接字也会继续传送输出缓冲区中遗留的数据；
（4）关闭套接字将丢失输入缓冲区中的数据。
1234
```

输入输出缓冲区的默认大小一般都是 8K，可以通过 getsockopt() 函数获取：

```
unsigned optVal;
int optLen = sizeof(int);
getsockopt(servSock, SOL_SOCKET, SO_SNDBUF, (char*)&optVal, &optLen);
printf("Buffer length: %d\n", optVal);
1234
```

阻塞模式
对于TCP套接字（默认情况下），当使用 write()/send() 发送数据时：

1. 首先会检查缓冲区，如果缓冲区的可用空间长度小于要发送的数据，那么 write()/send() 会被阻塞（暂停执行），直到缓冲区中的数据被发送到目标机器，腾出足够的空间，才唤醒 write()/send() 函数继续写入数据。
2. 如果TCP协议正在向网络发送数据，那么输出缓冲区会被锁定，不允许写入，write()/send() 也会被阻塞，直到数据发送完毕缓冲区解锁，write()/send() 才会被唤醒。
3. 如果要写入的数据大于缓冲区的最大长度，那么将分批写入。
4. 直到所有数据被写入缓冲区 write()/send() 才能返回。
   当使用 read()/recv() 读取数据时：
5. 首先会检查缓冲区，如果缓冲区中有数据，那么就读取，否则函数会被阻塞，直到网络上有数据到来。
6. 如果要读取的数据长度小于缓冲区中的数据长度，那么就不能一次性将缓冲区中的所有数据读出，剩余数据将不断积压，直到有 read()/recv() 函数再次读取。
7. 直到读取到数据后 read()/recv() 函数才会返回，否则就一直被阻塞。
   这就是TCP套接字的阻塞模式。所谓阻塞，就是上一步动作没有完成，下一步动作将暂停，直到上一步动作完成后才能继续，以保持同步性。
   TCP套接字默认情况下是阻塞模式



## 11.5 [I/O多路复用](https://blog.csdn.net/Nwk0000000001/article/details/124371544)

select，poll，[epoll](https://so.csdn.net/so/search?q=epoll&spm=1001.2101.3001.7020)都是IO多路复用的机制。所谓I/O多路复用机制，就是说通过一种机制，可以监视多个描述符，一旦某个描述符就绪（一般是读就绪或者写就绪），能够通知程序进行相应的读写操作。但select，poll，epoll本质上都是同步I/O，因为他们都需要在读写事件就绪后自己负责进行读写，也就是说这个读写过程是阻塞的，而异步I/O则无需自己负责进行读写，异步I/O的实现会负责把数据从内核拷贝到用户空间。关于阻塞，非阻塞，同步，异步将在下一篇文章详细说明。

select和poll的实现比较相似，目前也有很多为人诟病的缺点，epoll可以说是select和poll的增强版。
一、select实现
1、使用copy_from_user从用户空间拷贝fd_set到内核空间
2、注册回调函数pollwait
3、遍历所有fd，调用其对应的poll方法（对于socket，这个poll方法是sock_poll，sock_poll根据情况会调用到tcp_poll,udp_poll或者datagram_poll）
4、以tcp_poll为例，其核心实现就是pollwait，也就是上面注册的回调函数。
5、__pollwait的主要工作就是把current（当前进程）挂到设备的等待队列中，不同的设备有不同的等待队列，对于tcp_poll来说，其等待队列是sk->sk_sleep（注意把进程挂到等待队列中并不代表进程已经睡眠了）。在设备收到一条消息（网络设备）或填写完文件数据（磁盘设备）后，会唤醒设备等待队列上睡眠的进程，这时current便被唤醒了。
6、poll方法返回时会返回一个描述读写操作是否就绪的mask掩码，根据这个mask掩码给fd_set赋值。
7、如果遍历完所有的fd，还没有返回一个可读写的mask掩码，则会调用schedule_timeout是调用select的进程（也就是current）进入睡眠。当设备驱动发生自身资源可读写后，会唤醒其等待队列上睡眠的进程。如果超过一定的超时时间（schedule_timeout指定），还是没人唤醒，则调用select的进程会重新被唤醒获得CPU，进而重新遍历fd，判断有没有就绪的fd。
8、把fd_set从内核空间拷贝到用户空间。
总结：
select的几大缺点：
（1）每次调用select，都需要把fd集合从用户态拷贝到内核态，这个开销在fd很多时会很大
（2）同时每次调用select都需要在内核遍历传递进来的所有fd，这个开销在fd很多时也很大
（3）select支持的文件描述符数量太小了，默认是1024

二、poll实现
poll的实现和select非常相似，只是描述fd集合的方式不同，poll使用pollfd结构而不是select的fd_set结构。其他的都差不多。

三、epoll实现
epoll既然是对select和poll的改进，就应该能避免上述的三个缺点。那epoll都是怎么解决的呢？在此之前，我们先看一下epoll和select和poll的调用接口上的不同，select和poll都只提供了一个函数——select或者poll函数。而epoll提供了三个函数，epoll_create,epoll_ctl和epoll_wait，epoll_create是创建一个epoll句柄；epoll_ctl是注册要监听的事件类型；epoll_wait则是等待事件的产生。

对于第一个缺点，epoll的解决方案在epoll_ctl函数中。每次注册新的事件到epoll句柄中时（在epoll_ctl中指定EPOLL_CTL_ADD），会把所有的fd拷贝进内核，而不是在epoll_wait的时候重复拷贝。epoll保证了每个fd在整个过程中只会拷贝一次。

对于第二个缺点，epoll的解决方案不像select或poll一样每次都把current轮流加入fd对应的设备等待队列中，而只在epoll_ctl时把current挂一遍（这一遍必不可少）并为每个fd指定一个回调函数，当设备就绪，唤醒等待队列上的等待者时，就会调用这个回调函数，而这个回调函数会把就绪的fd加入一个就绪链表）。epoll_wait的工作实际上就是在这个就绪链表中查看有没有就绪的fd（利用schedule_timeout()实现睡一会，判断一会的效果，和select实现中的第7步是类似的）。

可以看到，总体和select的实现是类似的，只不过它是创建了一个eppoll_entry结构pwq，只不过pwq->wait的func成员被设置成了回调函数ep_poll_callback（而不是default_wake_function，所以这里并不会有唤醒操作，而只是执行回调函数），private成员被设置成了NULL。最后吧pwq->wait链入到whead中（也就是设备等待队列中）。这样，当设备等待队列中的进程被唤醒时，就会调用ep_poll_callback了。

再梳理一下，当epoll_wait时，它会判断就绪链表中有没有就绪的fd，如果没有，则把current进程加入一个等待队列(file->private_data->wq)中，并在一个while（1）循环中判断就绪队列是否为空，并结合schedule_timeout实现睡一会，判断一会的效果。如果current进程在睡眠中，设备就绪了，就会调用回调函数。在回调函数中，会把就绪的fd放入就绪链表，并唤醒等待队列(file->private_data->wq)中的current进程，这样epoll_wait又能继续执行下去了。
对于第三个缺点，epoll没有这个限制，它所支持的FD上限是最大可以打开文件的数目，这个数字一般远大于2048,举个例子,在1GB内存的机器上大约是10万左右，具体数目可以cat /proc/sys/fs/file-max察看,一般来说这个数目和系统内存关系很大。

总结：
1、select，poll实现需要自己不断轮询所有fd集合，直到设备就绪，期间可能要睡眠和唤醒多次交替。而epoll其实也需要调用epoll_wait不断轮询就绪链表，期间也可能多次睡眠和唤醒交替，但是它是设备就绪时，调用回调函数，把就绪fd放入就绪链表中，并唤醒在epoll_wait中进入睡眠的进程。虽然都要睡眠和交替，但是select和poll在“醒着”的时候要遍历整个fd集合，而epoll在“醒着”的时候只要判断一下就绪链表是否为空就行了，这节省了大量的CPU时间。这就是回调机制带来的性能提升。
2、select，poll每次调用都要把fd集合从用户态往内核态拷贝一次，并且要把current往设备等待队列中挂一次，而epoll只要一次拷贝，而且把current往等待队列上挂也只挂一次（在epoll_wait的开始，注意这里的等待队列并不是设备等待队列，只是一个epoll内部定义的等待队列）。这也能节省不少的开销。



高性能IO模型浅析

服务器端编程经常需要构造高性能的IO模型，常见的IO模型有四种：

（1）同步阻塞IO（Blocking IO）：即传统的IO模型。

（2）同步非阻塞IO（Non-blocking IO）：默认创建的socket都是阻塞的，非阻塞IO要求socket被设置为NONBLOCK。注意这里所说的NIO并非Java的NIO（New IO）库。

（3）IO多路复用（IO Multiplexing）：即经典的Reactor设计模式，有时也称为异步阻塞IO，Java中的Selector和Linux中的epoll都是这种模型。

（4）异步IO（Asynchronous IO）：即经典的Proactor设计模式，也称为异步非阻塞IO。

同步和异步的概念描述的是用户线程与内核的交互方式：同步是指用户线程发起IO请求后需要等待或者轮询内核IO操作完成后才能继续执行；而异步是指用户线程发起IO请求后仍继续执行，当内核IO操作完成后会通知用户线程，或者调用用户线程注册的回调函数。

阻塞和非阻塞的概念描述的是用户线程调用内核IO操作的方式：阻塞是指IO操作需要彻底完成后才返回到用户空间；而非阻塞是指IO操作被调用后立即返回给用户一个状态值，无需等到IO操作彻底完成。

另外，Richard Stevens 在《Unix 网络编程》卷1中提到的基于信号驱动的IO（Signal Driven IO）模型，由于该模型并不常用，本文不作涉及。接下来，我们详细分析四种常见的IO模型的实现原理。为了方便描述，我们统一使用IO的读操作作为示例。

一、同步阻塞IO

同步阻塞IO模型是最简单的IO模型，用户线程在内核进行IO操作时被阻塞。

图1 同步阻塞IO
![http://images.cnitblog.com/blog/405877/201411/142330286789443.png](https://img-blog.csdnimg.cn/98f922ffa15444fea5a67bf6c9d5c735.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHJlZENvYXQ=,size_20,color_FFFFFF,t_70,g_se,x_16)

如图1所示，用户线程通过系统调用read发起IO读操作，由用户空间转到内核空间。内核等到数据包到达后，然后将接收的数据拷贝到用户空间，完成read操作。

用户线程使用同步阻塞IO模型的伪代码描述为：

{

read(socket, buffer);

process(buffer);

}

即用户需要等待read将socket中的数据读取到buffer后，才继续处理接收的数据。整个IO请求的过程中，用户线程是被阻塞的，这导致用户在发起IO请求时，不能做任何事情，对CPU的资源利用率不够。

二、同步非阻塞IO

同步非阻塞IO是在同步阻塞IO的基础上，将socket设置为NONBLOCK。这样做用户线程可以在发起IO请求后可以立即返回。

图2 同步非阻塞IO
![在这里插入图片描述](https://img-blog.csdnimg.cn/429ce35738484df8a098ce0f059f19e4.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHJlZENvYXQ=,size_20,color_FFFFFF,t_70,g_se,x_16)

如图2所示，由于socket是非阻塞的方式，因此用户线程发起IO请求时立即返回。但并未读取到任何数据，用户线程需要不断地发起IO请求，直到数据到达后，才真正读取到数据，继续执行。

用户线程使用同步非阻塞IO模型的伪代码描述为：

{

while(read(socket, buffer) != SUCCESS)

;

process(buffer);

}

即用户需要不断地调用read，尝试读取socket中的数据，直到读取成功后，才继续处理接收的数据。整个IO请求的过程中，虽然用户线程每次发起IO请求后可以立即返回，但是为了等到数据，仍需要不断地轮询、重复请求，消耗了大量的CPU的资源。一般很少直接使用这种模型，而是在其他IO模型中使用非阻塞IO这一特性。

三、IO多路复用

IO多路复用模型是建立在内核提供的多路分离函数select基础之上的，使用select函数可以避免同步非阻塞IO模型中轮询等待的问题。

图3 多路分离函数select
![在这里插入图片描述](https://img-blog.csdnimg.cn/f241232e95ea4cddacf93634ceaf8901.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHJlZENvYXQ=,size_20,color_FFFFFF,t_70,g_se,x_16)

如图3所示，用户首先将需要进行IO操作的socket添加到select中，然后阻塞等待select系统调用返回。当数据到达时，socket被激活，select函数返回。用户线程正式发起read请求，读取数据并继续执行。

从流程上来看，使用select函数进行IO请求和同步阻塞模型没有太大的区别，甚至还多了添加监视socket，以及调用select函数的额外操作，效率更差。但是，使用select以后最大的优势是用户可以在一个线程内同时处理多个socket的IO请求。用户可以注册多个socket，然后不断地调用select读取被激活的socket，即可达到在同一个线程内同时处理多个IO请求的目的。而在同步阻塞模型中，必须通过多线程的方式才能达到这个目的。

用户线程使用select函数的伪代码描述为：

{

select(socket);

while(1) {

sockets = select();

for(socket in sockets) {

if(can_read(socket)) {

read(socket, buffer);

process(buffer);

}

}

}

}

其中while循环前将socket添加到select监视中，然后在while内一直调用select获取被激活的socket，一旦socket可读，便调用read函数将socket中的数据读取出来。

然而，使用select函数的优点并不仅限于此。虽然上述方式允许单线程内处理多个IO请求，但是每个IO请求的过程还是阻塞的（在select函数上阻塞），平均时间甚至比同步阻塞IO模型还要长。如果用户线程只注册自己感兴趣的socket或者IO请求，然后去做自己的事情，等到数据到来时再进行处理，则可以提高CPU的利用率。

IO多路复用模型使用了Reactor设计模式实现了这一机制。
![在这里插入图片描述](https://img-blog.csdnimg.cn/e3081aaabbfa421d84e5b318dad9b5fd.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHJlZENvYXQ=,size_20,color_FFFFFF,t_70,g_se,x_16)

图4 Reactor设计模式

如图4所示，EventHandler抽象类表示IO事件处理器，它拥有IO文件句柄Handle（通过get_handle获取），以及对Handle的操作handle_event（读/写等）。继承于EventHandler的子类可以对事件处理器的行为进行定制。Reactor类用于管理EventHandler（注册、删除等），并使用handle_events实现事件循环，不断调用同步事件多路分离器（一般是内核）的多路分离函数select，只要某个文件句柄被激活（可读/写等），select就返回（阻塞），handle_events就会调用与文件句柄关联的事件处理器的handle_event进行相关操作。

![在这里插入图片描述](https://img-blog.csdnimg.cn/0e66a83097334e01b31144661b33653c.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHJlZENvYXQ=,size_20,color_FFFFFF,t_70,g_se,x_16)

图5 IO多路复用

如图5所示，通过Reactor的方式，可以将用户线程轮询IO操作状态的工作统一交给handle_events事件循环进行处理。用户线程注册事件处理器之后可以继续执行做其他的工作（异步），而Reactor线程负责调用内核的select函数检查socket状态。当有socket被激活时，则通知相应的用户线程（或执行用户线程的回调函数），执行handle_event进行数据读取、处理的工作。由于select函数是阻塞的，因此多路IO复用模型也被称为异步阻塞IO模型。注意，这里的所说的阻塞是指select函数执行时线程被阻塞，而不是指socket。一般在使用IO多路复用模型时，socket都是设置为NONBLOCK的，不过这并不会产生影响，因为用户发起IO请求时，数据已经到达了，用户线程一定不会被阻塞。

用户线程使用IO多路复用模型的伪代码描述为：

void UserEventHandler::handle_event() {

if(can_read(socket)) {

read(socket, buffer);

process(buffer);

}

}

{

Reactor.register(new UserEventHandler(socket));

}

用户需要重写EventHandler的handle_event函数进行读取数据、处理数据的工作，用户线程只需要将自己的EventHandler注册到Reactor即可。Reactor中handle_events事件循环的伪代码大致如下。

Reactor::handle_events() {

while(1) {

sockets = select();

for(socket in sockets) {

get_event_handler(socket).handle_event();

}

}

}

事件循环不断地调用select获取被激活的socket，然后根据获取socket对应的EventHandler，执行器handle_event函数即可。

IO多路复用是最常使用的IO模型，但是其异步程度还不够“彻底”，因为它使用了会阻塞线程的select系统调用。因此IO多路复用只能称为异步阻塞IO，而非真正的异步IO。

四、异步IO

“真正”的异步IO需要操作系统更强的支持。在IO多路复用模型中，事件循环将文件句柄的状态事件通知给用户线程，由用户线程自行读取数据、处理数据。而在异步IO模型中，当用户线程收到通知时，数据已经被内核读取完毕，并放在了用户线程指定的缓冲区内，内核在IO完成后通知用户线程直接使用即可。

异步IO模型使用了Proactor设计模式实现了这一机制。

![在这里插入图片描述](https://img-blog.csdnimg.cn/3836fd9cbe2b4033b142b3cebed8c017.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHJlZENvYXQ=,size_20,color_FFFFFF,t_70,g_se,x_16)

图6 Proactor设计模式

如图6，Proactor模式和Reactor模式在结构上比较相似，不过在用户（Client）使用方式上差别较大。Reactor模式中，用户线程通过向Reactor对象注册感兴趣的事件监听，然后事件触发时调用事件处理函数。而Proactor模式中，用户线程将AsynchronousOperation（读/写等）、Proactor以及操作完成时的CompletionHandler注册到AsynchronousOperationProcessor。AsynchronousOperationProcessor使用Facade模式提供了一组异步操作API（读/写等）供用户使用，当用户线程调用异步API后，便继续执行自己的任务。AsynchronousOperationProcessor 会开启独立的内核线程执行异步操作，实现真正的异步。当异步IO操作完成时，AsynchronousOperationProcessor将用户线程与AsynchronousOperation一起注册的Proactor和CompletionHandler取出，然后将CompletionHandler与IO操作的结果数据一起转发给Proactor，Proactor负责回调每一个异步操作的事件完成处理函数handle_event。虽然Proactor模式中每个异步操作都可以绑定一个Proactor对象，但是一般在操作系统中，Proactor被实现为Singleton模式，以便于集中化分发操作完成事件。
![在这里插入图片描述](https://img-blog.csdnimg.cn/c5dd50f5541342c59dce9aa7bfe2d61b.png?x-oss-process=image/watermark,type_d3F5LXplbmhlaQ,shadow_50,text_Q1NETiBATHJlZENvYXQ=,size_20,color_FFFFFF,t_70,g_se,x_16)

图7 异步IO

如图7所示，异步IO模型中，用户线程直接使用内核提供的异步IO API发起read请求，且发起后立即返回，继续执行用户线程代码。不过此时用户线程已经将调用的AsynchronousOperation和CompletionHandler注册到内核，然后操作系统开启独立的内核线程去处理IO操作。当read请求的数据到达时，由内核负责读取socket中的数据，并写入用户指定的缓冲区中。最后内核将read的数据和用户线程注册的CompletionHandler分发给内部Proactor，Proactor将IO完成的信息通知给用户线程（一般通过调用用户线程注册的完成事件处理函数），完成异步IO。

用户线程使用异步IO模型的伪代码描述为：

void UserCompletionHandler::handle_event(buffer) {

process(buffer);

}

{

aio_read(socket, new UserCompletionHandler);

}

用户需要重写CompletionHandler的handle_event函数进行处理数据的工作，参数buffer表示Proactor已经准备好的数据，用户线程直接调用内核提供的异步IO API，并将重写的CompletionHandler注册即可。

相比于IO多路复用模型，异步IO并不十分常用，不少高性能并发服务程序使用IO多路复用模型+多线程任务处理的架构基本可以满足需求。况且目前操作系统对异步IO的支持并非特别完善，更多的是采用IO多路复用模型模拟异步IO的方式（IO事件触发时不直接通知用户线程，而是将数据读写完毕后放到用户指定的缓冲区中）。Java7之后已经支持了异步IO，感兴趣的读者可以尝试使用。



## 11.6 [LD_PRELOAD预加载so](http://t.zoukankan.com/tongyishu-p-12597441.html)

目前而言，有以下面两种方法可以让LD_PRELOAD失效。

1. 通过静态链接。使用gcc的-static参数可以把libc.so.6静态链入执行程序中。但这也就意味着你的程序不再支持动态链接。
2. 通过设置执行文件的setgid/setuid标志。在有SUID权限的执行文件，系统会忽略LD_PRELOAD环境变量。也就是说，如果你有以root方式运行的程序，最好设置上SUID权限。

在一些UNIX版本上，如果要使用LD_PRELOAD环境变量，需要有root权限。但不管怎么说，这些个方法目前来看并不是一个彻底的解决方案，为了安全，只能禁用LD_PRELOAD。



## 11.7 [应用服务器](https://blog.csdn.net/zzdurkjava/article/details/90054769)

[中间件是什么](https://blog.csdn.net/qq_58281305/article/details/120817107)

常见的应用服务器

**Tomcat：**免费开源，轻量级应用服务器，在中小型系统和并发访问用户不是很多的场合下被普遍使用，是开发和调试JSP 程序的首选。实际上Tomcat 部分是Apache 服务器的扩展，但它是独立运行的，所以当你运行tomcat 时，它实际上作为一个与Apache 独立的进程单独运行的。只实现了JSP/Servlet的相关规范，不支持EJB

 **Jboss：**免费开源，作为Java EE应用服务器，它不但是Servlet容器，而且是EJB容器，从而受到企业级开发人员的欢迎，从而弥补了Tomcat只是一个Servlet容器的缺憾。

 **Websphere：**是IBM的收费平台，价格昂贵，单买一个但CPU的服务器也要十几万。除了web应用服务器还包含大量的工具和其他相关应用开发平台。正常EJB

**weblogic：**收费，包括EJB,JSP,Servlet,JMS等等，全能型的。是商业软件里排名第一的容器（JSP、servlet、EJB等），并提供其他如JAVA编辑等工具，是一个综合的开发及运行环境。

 **jetty：**免费开源,架构比较简单，也是一个可扩展性和非常灵活的应用服务器。Jetty是使用Java语言编写的，它的API以一组JAR包的形式发布。开发人员可以将Jetty容器实例化成一个对象，可以迅速为一些独立运行（stand-alone）的Java应用提供

**Geronimo：**是对Java EE 5标准100%的实现，确切的说是一个基于JAVAEE架构的中间件，WebLogic是用于开发、集成、部署和管理大型分布式Web应用、网络应用和数据库应用的Java应用服务器。将Java的动态功能和Java Enterprise标准的安全性引入大型网络应用的开发、集成、部署和管理之中。

 **Resin:** 收费，是CAUCHO公司的产品，是一个非常流行的application server，对servlet和JSP提供了良好的支持，性能也比较优良，resin自身采用JAVA语言开发。支持负载均衡。resin的速度要比tomcat快3倍，调试方便，resin的报错是十分简洁而明确的，可以让你一眼就可以看出程序错误的类型和位置，resin对于中文的支持要比tomcat好不少

**undertow：**是一个用java编写的、灵活的、高性能的Web服务器，提供基于NIO的阻塞和非阻塞API。Undertow的架构是组合式的，可以通过组合各种小型的目的单一的处理程序来构建Web服务器。所以可以很灵活地的选择完整的Java EE servlet 3.1容器或初级非阻塞程序处理。 

**glassfish：**是Oracle 开发的官方Java EE容器，，也是同时支持Servlet和EJB，支持最新的特性，有自己的web容器，支持集群，支持热部署。

**JRun：**是由Allaire公司开发的JAVA服务器软件，它支持JSP1.1、Servlet2.2规范，目前最新的版本是JRun4，但是它的下载是要付费，它是Micromedia的一个应用服务器，它基于Sun公司的Java2平台企业版(J2EE)。 

**Jonas：**一个开放源代码的J2EE实现，在ObjectWeb协会中开发。整合了Tomcat或Jetty成为它的Web容器，以确保符合Servlet 2.3和JSP 1.2规范。JOnAS服务器依赖或实现以下的Java API：JCA、JDBC、JTA 、JMS、JMX、JNDI、JAAS、JavaMail 。

**ColdFusion：**是一个动态Web服务器，其CFML（ColdFusion Markup Language）是一种程序设计语言，类似现在的JavaServer Page里的JSTL（JSP Standard Tag Lib），从1995年开始开发，其设计思想被一些人认为非常先进，被一些语言所借鉴。

## 11.8 [缺页中断](https://blog.csdn.net/weixin_42323413/article/details/84564457)

##### 2.2 先进先出置换算法（First In First Out, [FIFO](https://so.csdn.net/so/search?q=FIFO&spm=1001.2101.3001.7020))

##### 2.2.1 基本思想

置换最先调入内存的页面，即置换在内存中驻留时间最久的页面。按照进入内存的先后次序排列成队列，从队尾进入，从队首删除。但是该算法会淘汰经常访问的页面，不适应进程实际运行的规律，目前已经很少使用。

##### 2.2.2 算例

仍然以OPT算例为例子。
　　中断次数为9，缺页中断率为9/12*100% = 75%。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181127133631149.png)

##### 2.2.3 Belady异常

一般来说，分配给进程的物理块越多，运行时的缺页次数应该越少，使用FIFO时，可能存在相反情况，分配4个物理块的缺页竟然比3个物理块的缺页次数还多！
　　例如：进程访问顺序为0、2、1、3、0、2、4、0、2、1、3、4。
　　M=3时，缺页中断9次。缺页中断率9/12*100% = 75%。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20181127133416542.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjMyMzQxMw==,size_16,color_FFFFFF,t_70)
　　Ｍ=4时，缺页中断10次。缺页中断率10/12*100% = 83.3%。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20181127133435782.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MjMyMzQxMw==,size_16,color_FFFFFF,t_70)

##### 2.3 最近最久未使用置换算法（Least Recently Used， LRU）

##### 2.3.1 基本思想

置换最近一段时间以来最长时间未访问过的页面。根据程序局部性原理，刚被访问的页面，可能马上又要被访问；而较长时间内没有被访问的页面，可能最近不会被访问。
　　LRU算法普偏地适用于各种类型的程序，但是系统要时时刻刻对各页的访问历史情况加以记录和更新，开销太大，因此LRU算法必须要有硬件的支持。

##### 2.3.2 算例

仍然以OPT算例为例子。
　　中断次数为6，缺页中断率为7/12*100% = 58.3%。

![在这里插入图片描述](https://img-blog.csdnimg.cn/20181127133510742.png)
　　堆栈实现LRU：

系统使用特殊的堆栈来存放内存中每一个页面的页号。每当访问一页时就调整一次，即把被访问页面的页号从栈中移出再压入栈顶。因此，栈顶始终是最新被访问页面的页号，栈底始终是最近最久未被访问的页号。当发生缺页中断时，总是淘汰栈底页号所对应的页面。

## 11.9 [什么是 inode](https://blog.csdn.net/weixin_73348410/article/details/128225564)

[理解 Linux 中的 inodes](https://www.cnblogs.com/wanng/p/linux-inodes.html)

理解inode，要从文件储存说起。

文件储存在硬盘上，硬盘的最小存储单位叫做"扇区"（Sector）。每个扇区储存512字节（相当于 0.5KB）。

操作系统读取硬盘的时候，不会一个个扇区地读取，这样效率太低，而是一次性连续读取多个扇 区，即一次性读取一个"块"（block）。这种由多个扇区组成的"块"，是文件存取的最小单 位。"块"的大小，最常见的是4KB，即连续八个 sector组成一个 block。

文件数据都储存在"块"中，那么很显然，我们还必须找到一个地方储存文件的元信息，比如文件的 创建者、文件的创建日期、文件的大小等等。这种储存文件元信息的区域就叫做inode，中文译名 为"索引节点"。 每一个文件都有对应的inode，里面包含了与该文件有关的一些信息。

Linux 文件系统通过 i 节点把文件的逻辑结构和物理结构转换的工作过程

Linux 通过 inode 节点表将文件的逻辑结构和物理结构进行转换。

        inode 节点是一个 64 字节长的表，表中包含了文件的相关信息，其中有文件的大小、文件所 有者、文件的存取许可方式以及文件的类型等重要信息。在 inode 节点表中最重要的内容是磁盘地址表。在磁盘地址表中有 13 个块号，文件将以块号在磁盘地址表中出现的顺序依次读取相应的块。
## 11.10 [内存、缓存、寄存器 倒排 加载](https://zhuanlan.zhihu.com/p/613081007)

有一个较大的矩阵已经加载到内存里了，通过什么方法可以加快数据访问？

**低效循环嵌套**

低效循环嵌套是适用于多维数组的算法特有的问题。如果沿错误的轴遍历数组，则在程序继续到下一个缓存行之前，将仅使用每个缓存行中的一个元素。

例如，考虑在二维矩阵上迭代的这个 C 代码小片段：

```cpp
double array[SIZE][SIZE]; 
for (int col = 0; col < SIZE; col++) 
	for (int row = 0; row < SIZE; row++) 
		array[row][col] = f(row, col);
```

在外循环的一次迭代中，内循环将访问特定列的每一行中的一个元素。例如，对于 8×8 矩阵：

![img](https://pic3.zhimg.com/80/v2-690ba1a6b300e553479668dd9c4a945a_720w.webp)

对于循环接触的每个元素，都会访问一个新的缓存行。处理器的一级缓存可能不会超过几百个缓存行。如果数组的行数超过几百行，则到达矩阵底部时，矩阵顶部的行可能已经从缓存中逐出。外循环的下一次迭代必须再次重新加载所有缓存行，从而导致每个被触摸的元素都出现缓存未命中。这对循环的性能是完全毁灭性的。

由于从每个获取的缓存行中只使用一个元素，因此不正确的循环嵌套还会在获取未使用的数据时浪费大量内存带宽。

可以通过更改循环的嵌套来解决此问题，以便它们沿行而不是沿列遍历矩阵。

```cpp
double array[SIZE][SIZE]; 
for (int row = 0; row < SIZE; row++) 
	for (int col = 0; col < SIZE; col++) 
		array[row][col] = f(row, col);
```

外循环的每次迭代现在都使用每个缓存行中的所有元素，并且相同的缓存行将不必被以后的迭代重复使用，从而大大减少了缓存未命中和缓存行提取。

![img](https://pic1.zhimg.com/80/v2-6debc9ec05e8bca17016f6cb3160d334_720w.webp)

按顺序访问数据也有助于硬件预取器更好地完成其工作。这将最大限度地利用可用内存带宽。

如果在不改变计算结果的情况下改变循环嵌套是不可能的，另一种方法是转置矩阵。通过更改数据布局，访问模式变得更加规则，更好地利用获取的缓存行并帮助硬件预取器更好地完成工作。不正确的循环嵌套的代价可能远远超过转置矩阵的代价。

不正确的循环嵌套并非二维矩阵所独有。当遍历任何规则的多维数组时，例如，在 3 维矩阵中，也会出现相同的效果。

与 C 相比，Fortran 以相反的方向组织其多维数组，因此如果你使用 Fortran 编程，你可能希望沿列而不是沿行迭代矩阵。

**随机访问模式**

随机内存访问模式通常对高速缓存和内存性能有害。对于随机访问模式，我们并不是指真正的随机访问，而是指通常不是对顺序地址的访问，也不是对相同数据或接近最近使用过的数据的访问。

**缓存的工作原理是期望最近被访问的数据和接近最近被访问的数据的数据更有可能被再次访问**。随机访问模式是不好的，因为它们通常很少包含此类重用，这意味着在缓存中找到访问数据的可能性很低。

随机访问模式通常还会导致低缓存行利用率。可以在不触及缓存行的其余部分的情况下访问缓存行中的各个数据元素。这增加了应用程序相对于它实际使用的数据量的提取率和内存带宽要求。

现代处理器中的硬件预取器还依赖于寻找常规访问模式来确定要预取的数据，从而隐藏内存访问延迟。因此，随机访问模式会使硬件预取器失效。某些类型的不规则访问模式甚至可能欺骗硬件预取器预取无用的数据，从而浪费内存带宽。

当访问接近上次访问地址的地址时，甚至访问主内存也会更快。

随机访问模式可以源自不同的来源：

- 数据结构
- 动态内存分配
- 算法

一些数据结构本质上会导致随机访问模式。

例如，哈希表通常设计为在整个表中随机散布元素以避免冲突。因此，表中的元素查找会导致随机访问模式。

树结构是随机访问模式的另一个常见来源。在树中查找不同的键会导致遍历树中的不同路径，从而导致看似随机的内存访问模式。

用缓存更友好的数据结构替换此类数据结构可以提供性能改进。有关更多信息。

动态内存分配有助于将相关数据对象放置得彼此远离。内存分配器通常会尝试按顺序分配内存区域，但随着应用程序的运行和堆上内存的分配、释放和再次分配，堆可能会变得碎片化。然后分配的内存区域可能会分布在整个堆上。

由于应用程序进行的其他分配，在应用程序运行时增量分配的数据结构也可能分布在地址空间中。

由动态内存分配引起的随机访问模式的一个例子是从新分配的内存区域创建的链表。内存区域可以或多或少地随机分布在地址空间中。因此，当应用程序遍历列表时，它会或多或少地以随机模式导致内存访问。如果在执行期间将元素递增地添加到列表中，则可能会由于内存碎片而进一步恶化问题。

如果由于动态内存分配导致随机内存访问模式成为问题，则可以实现自定义内存分配器，将相关数据放在一起。例如，如果您有一个在执行期间增量分配的数据结构，您可以在应用程序启动时为该数据结构分配一个内存池，然后使用该池中的内存进行增量分配。

最后，某些算法本身可能会导致不规则的访问模式。

许多图形算法都是此类算法的示例。如果将图表示为邻接矩阵，则数据结构本身非常规则。但是，如果你在图中执行深度优先或广度优先搜索，算法将导致对邻接矩阵的不规则访问，因为它遵循图节点之间的边。

修复由算法引起的随机访问模式通常很困难。可能存在具有更好缓存行为的替代算法，但它们在其他方面可能较差。有时可能需要使数据结构适应算法，例如，可以对邻接矩阵进行排序，使连接的节点彼此靠近，从而沿着边缘产生更规则的访问模式。



```cpp
#include <iostream>
#include <vector>

const int SIZE = 10;
const int BLOCK = 2;

void transpose(int dst[SIZE][SIZE], int src[SIZE][SIZE]) {
    int jb, i, j;
    for (jb = 0; jb < SIZE; jb += BLOCK) {
        for (i = 0; i < SIZE; i++) {
            for (j = jb; j < jb + BLOCK && j < SIZE; j++) {
                dst[j][i] = src[i][j];
            }
        }
    }
}

int main() {
    int a[SIZE][SIZE] = {
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {2, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        {3, 3, 3, 3, 3, 3, 3, 3, 3, 3},
        {4, 4, 4, 4, 4, 4, 4, 4, 4, 4},
        {5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
        {6, 6, 6, 6, 6, 6, 6, 6, 6, 6},
        {7, 7, 7, 7, 7, 7, 7, 7, 7, 7},
        {8, 8, 8, 8, 8, 8, 8, 8, 8, 8},
        {9, 9, 9, 9, 9, 9, 9, 9, 9, 9}
    };
    int b[SIZE][SIZE] = {
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1}
    };
    transpose(a, b);
    return 0;
}
```

[CPU架构-Hardware Prefetcher - 2](https://zhuanlan.zhihu.com/p/599040442)

硬件预取器。

## 11.11 [共享内存（Shared Memory）与进程通信](https://blog.csdn.net/crr411422/article/details/131421891)

[ftok()函数深度解析](https://blog.csdn.net/u013485792/article/details/50764224)

一、共享内存概述
        共享内存允许两个或者多个进程共享给定的存储区域。

共享内存的特点：

1）共享内存是进程间共享数据的一种最快的方法。

一个进程向共享内存区域写入了数据，共享这个内存区域的所有进程就可以立刻看到其中的内容。

2）使用共享内存要注意的是多个进程之间对一个给定存储区域访问的互斥。

若一个进程正在向共享内存区写数据，则在它做完这一步操作前，别的进程不应当去读、写这些数据。

共享内存示意图：

![](https://img-blog.csdnimg.cn/0fc57565254e4f3f8d87a433b28873d1.png)

共享内存是进程间通信方式中效率最高的，原因在于进程是直接在物理内存上进行操作，将物理地址映射到用户进程这，所以只要对其地址进行操作，就是直接对物理地址操作。

二、共享内存操作

在ubuntu12.04中共享内存限制如下：

* 共享存储区的最小字节数：1
* 共享存储区的最大字节数：32M
* 共享内存区的最大个数：4096
* 每个进程最多能映射的共享存储区的个数：4096

使用shell命令操作共享内存：

> 查看共享内存：ipcs -m
>
> 删除共享内存：ipcrm -m shmid

1）获得一个共享存储标识符：shmget函数

shmget函数：

```cpp
#include<sys/ipc.h>
#include<sys/shm.h>
int shmget(key_t key, size_t size, int shmflg);
功能：创建一个共享内存
参数：
        key：键值，唯一的键值确定唯一的共享内存
        size：创建的共享内存的大小
        shmflg：共享内存的访问权限
                        一般为IPC_CREAT | 0777

返回值：
        成功：共享内存的id
        失败：-1 
```

2）共享内存映射(attach)：shmat函数

shmat函数：

```cpp
#include<sys/types.h>
#include<sys/shm.h>
void *shmat(int shmid, const void *shmaddr, int shmflg);
功能：映射共享内存
参数：
        shmid：共享内存的id
        shmaddr：映射的地址，设置为NULL为系统自动分配
        shmflg：标志位
                0：共享内存具有可读可写权限
                SHM_RDONLY：只读
返回值：
        成功：映射的地址
        失败：-1
```

3）解除共享内存映射(detach)：shmdt函数

shmdt函数：

```cpp
#include<sys/types.h>
#include<sys/shm.h>
int shmdt(const void *shmaddr);
功能：
        解除共享内存的映射
参数：
        shmaddr：映射的地址，shmat的返回值
返回值：
        成功：0
        失败：-1 
```

4）共享内存控制：shmctl函数

shmctl函数：

```cpp
#include<sys/ipc.h>
#include<sys/shm.h>
int shmctl(int shmid, int cmd, struct shmid_ds *buf);
功能：
        设置或者获取共享内存的属性
参数：
        shmid：共享内存的id
        cmd：执行操作的命令
                IPC_STAT 获取共享内存的属性
                IPC_SET 设置共享内存的属性
                IPC_RMID   删除共享内存
        shmid_ds：共享内存的属性结构体
返回值：
        成功：0
        失败：-1
```

总结：
        共享内存作为一种高效的进程间通信机制，以其独特的优势在多进程环境中发挥着重要的角色。它允许多个进程直接访问同一块内存区域，从而实现了数据的快速共享和交换，显著提高了系统性能。然而，这也带来了数据同步和并发控制的挑战。我们需要结合锁、信号量等同步技术来解决这些问题。

​		总的来说，共享内存是一种强大而灵活的工具，但需要谨慎、有效地使用，以确保程序的正确性和稳定性。

```cpp
#include<sys/ipc.h>
#include<sys/shm.h>
#include<stdio.h>
#include<stdlib.h>

int main()
{
    key_t key;
    if ((key = ftok(".", 100)) == -1) {
        perror("fail to ftok");
        exit(1);
    }
    int shmid;
    if ((shmid = shmget(key, 500, IPC_CREAT | 0666)) == -1) {
        perror("fail to shmget");
        exit(1);
    }

    printf("shmid = %d\n", shmid);
    system("ipcs -m");

    return 0;
}
//
shmid = 44

------ Shared Memory Segments --------
key        shmid      owner      perms      bytes      nattch     status      
0x00000000 13         ben        600        524288     2          dest         
0x00000000 18         ben        600        524288     2          dest         
0x00000000 22         ben        600        4194304    2          dest         
0x00000000 23         ben        600        524288     2          dest         
0x00000000 32         ben        600        524288     2          dest         
0x00000000 35         ben        600        4194304    2          dest         
0x6405068a 44         ben        666        500        0                       


TEST(Test, Test_write)
{
    key_t key;
    if ((key = ftok(".", 100)) == -1) {
        perror("fail to ftok");
        exit(1);
    }
    int shmid;
    if ((shmid = shmget(key, 500, IPC_CREAT | 0666)) == -1) {
        perror("fail to shmget");
        exit(1);
    }

    char* text;
    if ((text = static_cast<char*>(shmat(shmid, NULL, 0))) == (void*) -1) {
        perror("fail to shmat");
        exit(1);
    }
    //写入
    strcpy(text, "hello world");
    if (shmdt(text) == -1) {
        perror("fail to text");
        exit(1);
    }
    system("ipcs -m");
    system("pwd");
}
// 
------ Shared Memory Segments --------
key        shmid      owner      perms      bytes      nattch     status      
0x00000000 13         ben        600        524288     2          dest         
0x00000000 18         ben        600        524288     2          dest         
0x00000000 22         ben        600        4194304    2          dest         
0x00000000 23         ben        600        524288     2          dest         
0x00000000 32         ben        600        524288     2          dest         
0x00000000 35         ben        600        4194304    2          dest         
0x6405068d 43         ben        666        500        0                       

/home/ben/Softwares/JetBrains/CppProjects/gtest_project/cmake-build-debug/test

TEST(Test, Test_read)
{
    //创建共享内存
    key_t key;
    if ((key = ftok(".", 100)) == -1) {
        perror("fail to ftok");
        exit(1);
    }
    int shmid;
    if ((shmid = shmget(key, 500, IPC_CREAT | 0666)) == -1) {
        perror("fail to shmget");
        exit(1);
    }

    //映射
    char* text;
    if ((text = static_cast<char*>(shmat(shmid, NULL, 0))) == (void*) -1) {
        perror("fail to shmat");
        exit(1);
    }
    // system("ipcs -m"); // 这里可以看到nattch为1

    printf("text = %s\n", text);
    //解除映射
    if (shmdt(text) == -1) {
        perror("fail to shmdt");
        exit;
    }
    system("ipcs -m");
    system("pwd");
}
//
------ Shared Memory Segments --------
key        shmid      owner      perms      bytes      nattch     status      
0x00000000 13         ben        600        524288     2          dest         
0x00000000 18         ben        600        524288     2          dest         
0x00000000 22         ben        600        4194304    2          dest         
0x00000000 23         ben        600        524288     2          dest         
0x00000000 32         ben        600        524288     2          dest         
0x00000000 35         ben        600        4194304    2          dest         
0x6405068d 43         ben        666        500        0                       

/home/ben/Softwares/JetBrains/CppProjects/gtest_project/cmake-build-debug/test
text = hello world
    
TEST(Test, Test_rm)
{
    key_t key;
    if ((key = ftok(".", 100)) == -1) {
        perror("fail to ftok");
        exit(1);
    }
    int shmid;
    if ((shmid = shmget(key, 500, IPC_CREAT | 0666)) == -1) {
        perror("fail to shmget");
        exit(1);
    }

    printf("shmid = %d\n", shmid);
    //删除共享内存
    if (shmctl(shmid, IPC_RMID, NULL) == -1) {
        perror("fial to shmid");
        exit(1);
    }
    system("ipcs -m");
}
//
------ Shared Memory Segments --------
key        shmid      owner      perms      bytes      nattch     status      
0x00000000 13         ben        600        524288     2          dest         
0x00000000 18         ben        600        524288     2          dest         
0x00000000 22         ben        600        4194304    2          dest         
0x00000000 23         ben        600        524288     2          dest         
0x00000000 32         ben        600        524288     2          dest         
0x00000000 35         ben        600        4194304    2          dest         

shmid = 43
```



## 11.12 [IPC进程间通信](https://blog.csdn.net/ShiXiAoLaNga/article/details/123648890)

同步：[Linux进程间共享内存通信时如何同步](https://zhuanlan.zhihu.com/p/627763197)

### 1. [IPC](https://so.csdn.net/so/search?q=IPC&spm=1001.2101.3001.7020)定义

IPC技术: 内核进程通信(Inter Process Communication)

### 2. 共享内存

2.1 共享内存定义
     shm(share memory),在主机上指定一块内存作为进程之间的共享内存, 不同的进程之间可以通过一些方式去访问这块内存。

2.2 shm(共享内存 编程模型)

```
创建key                            ftok函数
创建共享内存                   shmget函数
挂载共享内存                   shmat函数
卸载共享内存                   shmdt函数
删除共享内存                   shmctl函数
```

2.3 shm的一些函数原型
        2.3.1 ftok函数

```cpp
//头文件
#include <sys/types.h>
#include <sys/ipc.h>
//函数原型
key_t ftok(const char *pathname, int proj_id);

//返回值: 返回值为一个4byte的整数(返回-1失败)
//0--15  bit: pathname的st_ino属性的低16位
//16--23 bit: pathname的st_dev属性的低8位
//24--31 bit: pro_id的低8位

//参数一: 传入一个路径,一般使用当前路径"."
//参数二: 任意的一个整数,因为要做进程间通信,
//那么另一个进程需要与这个数保持一致才能找到对应的ipcid,一般只使用8个bit,因此取值范围在0--255
```

​        2.3.2 shmget函数
```cpp
//头文件
#include <sys/ipc.h>
#include <sys/shm.h>
//函数原型
int shmget(key_t key, size_t size, int shmflg);

//返回值: 返回一个整数(返回-1失败) 

//参数一: ftok函数的返回值
//参数二: 共享的内存大小(byte)(如果数值为1 -- 4096,实际申请4k(一页))
//参数三: 主要与一些标志有关
// IPC_CREAT  不存在共享内存就创建,否则打开
// IPC_EXCL   不存在共享内存才创建,否则错误
```

​        2.3.3 shmat函数
```cpp
//头文件
#include <sys/types.h>
#include <sys/shm.h>
//函数原型
void *shmat(int shmid, const void *shmaddr, int shmflg);

//返回值: 返回申请的共享内存的首地址(返回-1则挂载失败)

//参数一: shmget函数的返回值
//参数二: 一般为0,表示连接到一个由内核选择的可用地址之上
//参数三: 一般为0
```

​        2.3.4 shmdt函数
```cpp
//头文件
#include <sys/types.h>
#include <sys/shm.h>
//函数原型
int shmdt(const void *shmaddr);

//返回值: 成功返回0,失败返回-1
//参数一: shmat函数的返回值
```

​        2.3.5 shmctl函数
```cpp
//头文件
#include <sys/ipc.h>
#include <sys/shm.h>
//函数原型
int shmctl(int shmid, int cmd, struct shmid_ds *buf);

//返回值: 失败返回-1,
//参数一: shmget函数的返回值
//参数二: 设置调用者对共享内存段的权限
// IPC_STAT: 调用者必须对共享内存具有读权限
// IPC_SET: 对shmid_ds 中的某些值做一些修改
// IPC_RMID: 表示销毁某个段
// IPC_INFO: 返回共享内存限制和参数的信息
// SHM_INFO: 返回一个shm_info的结构体
// SHM_STAT: 返回该内存段在内核数组的索引,该数组记录所有共享内存段的信息
// SHM_LOCK: 防止交换共享内存段
// SHM_UNLOCK: 解除防止交换共享内存段
//参数三: 一个指向shmid_ds的结构体指针
struct shmid_ds 
{   struct ipc_perm shm_perm;/*用户权限*/
    size_t shm_segsz;/* segment (bytes)的大小*/
    time_t shm_atime;/*最后连接时间*/
    time_t shm_dtime;/*最后的分离时间时间*/
    time_t ctime;/*上次更改时间*/
    pid_t  shm_cpid;/*创建器的PID*/
    ......
};
```

2.4 共享内存的优势与缺点

2.4.1 优势
        因为两个进程可以使用一段内存, 这就构成了进程之间的双向通信, 无疑传输速率是很快的, 是最快的进程间通信方式。因为不同的进程之间是直接从这一段内存之中存放、读取数据。而且使用共享内存进行通信对数据是没有什么限制的。并且共享内存的生命周期与系统内核的生命周期是一致的。

2.4.2 缺点
    共享内存并没有提供同步机制，在一个进程对共享内存进行写操作结束之前,另一个进程是不能对这块共享内存进行同步访问的，因为我们通常想要实现的是多个进程对共享内存的同步访问。

2.5 共享内存示例

main_read.cpp

```cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <signal.h>

int* p = NULL;

void hand(int)
{
    //5.卸载功能共享内存
    shmdt(p);
    printf("bye bye!\n");

    exit(0);
}

int main()
{
    signal(SIGINT, hand);
    //1.创建key
    key_t key = ftok(".", 'm');  //参数二在256之内
    if (-1 == key) {
        printf("create key error:%m\n"), exit(-1);
    }
    printf("create key success!\n");
    //2.创建共享内存   内存单位之页   1页 == 4k
    int shmid = shmget(key, 4096, IPC_CREAT | 0666);
    if (-1 == shmid) {
        printf("shmget error:%m\n"), exit(-1);
    }
    printf("shmget success!\n");
    //3.挂载共享内存
    p = (int*) shmat(shmid, NULL, 0);
    if ((int*) -1 == p) {
        printf("shmat error:%m\n"), exit(-1);
    }
    printf("shmat success!\n");
    //4.使用共享内存
    while (1) {
        printf("%d\n", *p);
        sleep(1);
    }

    return 0;
}
//
ben@ben-virtual-machine:~/Softwares/JetBrains/CppProjects/gtest_project/cmake-build-debug/src$ ./main_write 
create key success!
shmget success!
shmat success!
^Cbye bye!
```

main_write.cpp

```cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>
#include <signal.h>

int* p = NULL;

void hand(int)
{
    //5.卸载功能共享内存
    shmdt(p);
    printf("bye bye!\n");

    exit(0);
}

int main()
{
    signal(SIGINT, hand);
    //1.创建key
    key_t key = ftok(".", 'm');  //参数二在256之内
    if (-1 == key) {
        printf("create key error:%m\n"), exit(-1);
    }
    printf("create key success!\n");
    //2.获取共享内存
    int shmid = shmget(key, 4096, IPC_CREAT | 0666);
    if (-1 == shmid) {
        printf("shmget error:%m\n"), exit(-1);
    }
    printf("shmget success!\n");
    //3.挂载共享内存
    p = (int*) shmat(shmid, NULL, 0);
    if (NULL == p) {
        printf("shmat error:%m\n"), exit(-1);
    }
    printf("shmat success!\n");
    //4.使用共享内存
    int n = 0;
    while (1) {
        *p = n++;
        sleep(1);
    }

    return 0;
}
//
ben@ben-virtual-machine:~/Softwares/JetBrains/CppProjects/gtest_project/cmake-build-debug/src$ ./main_read 
create key success!
shmget success!
shmat success!
8
9
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
20
21
22
23
24
25
26
27
28
^Cbye bye!

```

main_rm.cpp

```cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/shm.h>

int main()
{
    //1.创建key
    key_t key = ftok(".", 'm');  //参数二在256之内
    if (-1 == key) {
        printf("create key error:%m\n"), exit(-1);
    }
    printf("create key success!\n");
    //2.获取共享内存
    int shmid = shmget(key, 4096, IPC_CREAT);
    if (-1 == shmid) {
        printf("shmget error:%m\n"), exit(-1);
    }
    printf("shmget success!\n");

    //3.删除共享内存
    shmctl(shmid, IPC_RMID, NULL);

    return 0;
}
//
ben@ben-virtual-machine:~/Softwares/JetBrains/CppProjects/gtest_project/cmake-build-debug/src$ ./main_rm 
create key success!
shmget success!
```

### 3. 消息队列

```shell
ipcs -q # 查看
ipcrm -q qid # 删除
```

3.1 消息队列定义
        msg(message queue), 在主机上指定一个或者多个队列, 一方进程向队列之中放数据, 另一方从队列之中拿东西。

3.2 msg(消息队列 编程模型)
创建key                             ftok函数
创建消息队列                    msgget函数
收发消息                           msgrcv 函数       msgsnd 函数
删除消息队列                    msgctl函数

3.4 消息队列的优势与缺点
        3.4.1 优势
       消息队列提供了一种从进程向另一个进程发送一个数据块的方法。每个数据块都被认为是一个类型，接收进程接收的数据块可以有不同的类型值。可以通过发送消息来避免命名管道的同步和阻塞的问题。消息队列与管道不同的是，消息队列是基于消息的，而管道是基于字节流的，且消息队列的读取不一定是先入先出。 

​		3.4.2 缺点

​       每个数据块有大小限制, 整个操作系统中所有的数据块总大小也有一个限制。

3.5 消息队列演示

main_read.cpp

```cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>

#define TYPE 2
//接收端进程
struct msgBuf {
    long type;
    char buff[20];
};

int main()
{
    struct msgBuf msg;
    msg.type = TYPE;
    //1.创建key
    key_t key = ftok(".", 'q');
    if (-1 == key) {
        printf("ftok error:%m\n"), exit(-1);
    }
    printf("ftok success!\n");
    //2. 创建消息队列
    int msgid = msgget(key, IPC_CREAT | 0666);
    if (-1 == msgid) {
        printf("msgget error:%m\n"), exit(-1);
    }
    printf("msgget success!\n");
    //3. 接收消息
    int r;
    while (1) {
        memset(msg.buff, 0, 20);
        r = msgrcv(msgid, &msg, sizeof(msg), TYPE, IPC_NOWAIT);  // 只接收类型为TYPE的消息，0则接收所有消息
        printf("r:%d msg:%s type:%ld\n", r, msg.buff, msg.type);
        sleep(1);
    }
    return 0;
}

//验证改成如下结构，则read程序会core掉，这样看是通过参数类型匹配type
struct msgBuf {
    std::string typexx;
    char buff[20];
};
```

main_write.cpp

```cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>

//发送端进程
struct msgBuf {
    long type;
    char buff[20];
};

int main()
{
    struct msgBuf msg;
    //1.创建key
    key_t key = ftok(".", 'q');
    if (-1 == key) {
        printf("ftok error:%m\n"), exit(-1);
    }
    printf("ftok success!\n");
    //2. 创建消息队列
    int msgid = msgget(key, IPC_CREAT | 0666);
    if (-1 == msgid) {
        printf("msgget error:%m\n"), exit(-1);
    }
    printf("msgget success!\n");
    //3. 发消息
    int r;
    while (1) {
        printf("请输入消息类型:");
        scanf("%ld", &msg.type);
        printf("请输入消息内容:");
        scanf("%s", msg.buff);
        r = msgsnd(msgid, &msg, sizeof(msg), IPC_NOWAIT); //IPC_NOWAIT非阻塞方式
        printf("r: %d\n", r);
    }

    return 0;
}
```

main_rm.cpp

```cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>

int main()
{
    //1.创建key
    key_t key = ftok(".", 'q');
    if (-1 == key) {
        printf("ftok error:%m\n"), exit(-1);
    }
    printf("ftok success!\n");
    //2. 创建消息队列
    int msgid = msgget(key, IPC_CREAT | 0666);
    if (-1 == msgid) {
        printf("msgget error:%m\n"), exit(-1);
    }
    printf("msgget success!\n");
    //3. 删除当前消息队列
    msgctl(msgid, IPC_RMID, NULL);

    return 0;
}
//
ftok success!
msgget success!
```

### 4. 旗语(信号量)

```shell
ipcs -s # 查看
------ Semaphore Arrays --------
key        semid      owner      perms      nsems     

ipcrm -s semid # 删除
```

4.1 旗语(信号量定义)
sem(semaphore), 让多个进程不可能同时访问一块区域。

**这个信号量个瞬时的值，并没有累积的效果。**

4.2 sem(信号量 编程模型)

```
创建key                          ftok函数
创建旗语(信号量)                   semget函数
初始化旗语(信号量)                 semctl函数
使用旗语(信号量)                   semop函数
删除旗语(信号量)                   semctl函数
```

4.3 sem的一些函数原型
        4.3.1 ftok函数

```cpp
//头文件
#include <sys/types.h>
#include <sys/ipc.h>
//函数原型
key_t ftok(const char *pathname, int proj_id);

//返回值: 返回值为一个4byte的整数(返回-1失败)
//0--15  bit: pathname的st_ino属性的低16位
//16--23 bit: pathname的st_dev属性的低8位
//24--31 bit: pro_id的低8位

//参数一: 传入一个路径,一般使用当前路径"."
//参数二: 任意的一个整数,因为要做进程间通信,
//那么另一个进程需要与这个数保持一致才能找到对应的ipcid,一般只使用8个bit,因此取值范围在0--255
```

​        4.3.2 semget函数

```cpp
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>

int semget(key_t key, int nsems, int semflg);

//返回值: 返回一个整数(-1表示失败)
//参数一: ftok函数返回值
//参数二: 创建信号量的个数(一般创建一个)
//参数三: 主要是一些标志
// IPC_CREAT  不存在共享内存就创建,否则打开
// IPC_EXCL   不存在共享内存才创建,否则错误
```

​        4.3.3 semop函数

```cpp
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>
 
int semop(int semid, struct sembuf *sops, size_t nsops);
 
//返回值: 成功返回0, 失败返回-1
//参数一: semget函数返回值()
//参数二: 信号量的一些信息(用户可更改)
sops[0].sem_num = 0; 信号量的索引
sops[0].sem_op = 0;  加还是减以及加减的值。操作类型，取值可以是正数、负数或0。正数表示增加信号量的值，负数表示减小信号量的值，0 表示等待信号量值变为 0。
sops[0].sem_flg = 0; 一般设置为0
//参数三: 操作的次数
```

​        4.3.4 semctl函数

```cpp
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>
 
int semctl(int semid, int semnum, int cmd, ...);
 
//返回值: 失败返回-1
//参数一: semget函数返回值(信号量的标识id)
//参数二: 信号量的标识,标识使用第几个信号量(从0开始)
//参数三: 一个指向msqid_ds 的结构体指针
// IPC_STAT: 调用者必须对共享内存具有读权限
// IPC_SET: 对semid_ds 中的某些值做一些修改
// IPC_RMID: 表示销毁某个段
// IPC_INFO: 返回共享内存限制和参数的信息
// SEM_INFO: 返回一个seminfo的结构体
// SEM_STAT: 返回该内存段在内核数组的索引,该数组记录所有共享内存段的信息
// GETALL:   获取集合中所有的信号量
// GETNCNT:  返回集合中指定semnum的信号量
// GETPID:   返回指定semnum的sempid的值
// GETVAL:   返回指定semnum的semval的值
// GETZCNT:  返回集合中第一个信号量的semzcnt值
// SETALL:   为集合中所有信号量设置值
// SETVAL:   设置一个信号量的值
//参数四: 缺省参数, union semun类型的指针
 union semun {
               int              val;    /* SETVAL */
               struct semid_ds *buf;    /* Buffer for IPC_STAT, IPC_SET */
               unsigned short  *array;  /* Array for GETALL, SETALL */
               struct seminfo  *__buf;  /* Buffer for IPC_INFO*/
           };
```



4.4 旗语(信号量)的优势与缺点 
        4.4.1 优势
         节省了打开文件以及关闭文件的时间,数据传输速率提高

​		4.4.2 缺点
​     	一但被锁定, 在解锁之前出现程序崩溃等问题, 就会导致锁定的信号量无法恢复, 形成永久性的占用, 使用文件操作的方式则不会出现这种情况, 因为在进程退出的时候, 文件就会被关闭, 在该文件描述符上的锁定就会被自动解除。

4.5 旗语(信号量演示)

main_read.cpp

```cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>

union semun {
    int val;
    struct semid_ds* buf;
    unsigned short* array;
    struct seminfo* __buf;
};

int main()
{
    //1. 创建key
    key_t key = ftok(".", 's');
    if (-1 == key) {
        printf("ftok error:%m\n"), exit(-1);
    }
    printf("ftok success!\n");
    //2. 创建信号量
    int semid = semget(key, 1, IPC_CREAT | 0666);
    if (-1 == semid) {
        printf("semget error:%m\n"), exit(-1);
    }
    printf("semget success!\n");
    //3. 初始化信号量
    union semun u;
    u.val = 5;
    semctl(semid, 0, SETVAL, u);
    //4. 使用旗语
    struct sembuf buf;
    buf.sem_num = 0;
    buf.sem_op = -1;    //减
    buf.sem_flg = 0;

    int n = 0;
    while (1) {
        semop(semid, &buf, 1); //操作一次
        printf("卖出%d辆!\n", ++n);
    }

    return 0;
}
//
ftok success!
semget success!
卖出1辆!
卖出2辆!
卖出3辆!
卖出4辆!
卖出5辆!
卖出6辆!
卖出7辆!
卖出8辆!
卖出9辆!
卖出10辆!
卖出11辆!
卖出12辆!
卖出13辆!
卖出14辆!
卖出15辆!
卖出16辆!
卖出17辆!

```

main_write.cpp

```cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>

int main()
{
    //1. 创建key
    key_t key = ftok(".", 's');
    if (-1 == key) {
        printf("ftok error:%m\n"), exit(-1);
    }
    printf("ftok success!\n");
    //2. 创建信号量
    int semid = semget(key, 1, IPC_CREAT | 0666);
    if (-1 == semid) {
        printf("semget error:%m\n"), exit(-1);
    }
    printf("semget success!\n");

    //3. 使用旗语
    struct sembuf buf;
    buf.sem_num = 0;
    buf.sem_op = 1;     //加
    buf.sem_flg = 0;

    while (1) {
        semop(semid, &buf, 1); //操作一次
        std::cout << "times: " << ++times << std::endl;
        sleep(1);
    }
    return 0;
}
//
ftok success!
semget success!
times: 1
times: 2
times: 3
```

本地测试：

先运行write 10秒，再运行read，则read依然是1秒一个，并没有累积10个。

如果启动两个read进程，则每write一个，只有其中一个read进程能获取信号量。

main_rm.cpp

```cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <signal.h>

int main()
{
    //1. 创建key
    key_t key = ftok(".", 's');
    if (-1 == key) {
        printf("ftok error:%m\n"), exit(-1);
    }
    printf("ftok success!\n");
    //2. 创建信号量
    int semid = semget(key, 1, IPC_CREAT | 0666);
    if (-1 == semid) {
        printf("semget error:%m\n"), exit(-1);
    }
    printf("semget success!\n");

    //3. 删除旗语
    semctl(semid, 0, IPC_RMID, NULL);

    return 0;
}
//
ftok success!
semget success!
```

本地测试`buf.sem_op = 0`（等待信号量值变为 0）时的效果：

```cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <iostream>

union semun {
    int val;
    struct semid_ds* buf;
    unsigned short* array;
    struct seminfo* __buf;
};

int main()
{
    //1. 创建key
    key_t key = ftok(".", 's');
    if (-1 == key) {
        printf("ftok error:%m\n"), exit(-1);
    }
    printf("ftok success!\n");
    //2. 创建信号量
    int semid = semget(key, 1, IPC_CREAT | 0666);
    if (-1 == semid) {
        printf("semget error:%m\n"), exit(-1);
    }
    printf("semget success!\n");

    union semun u;
    u.val = 10;
    semctl(semid, 0, SETVAL, u);
    //3. 使用旗语
    struct sembuf buf;
    buf.sem_num = 0;
    buf.sem_op = 0;     //wait for sem value to be 0
    buf.sem_flg = 0;

    semop(semid, &buf, 1); //操作一次
    std::cout << "sem value is 0" << std::endl;

    int times = 0;
    while (1) {
        semop(semid, &buf, 1); //操作一次
        std::cout << "times: " << ++times << std::endl;
        sleep(1);
    }
    return 0;
}
```

运行该程序，则阻塞在第一行`semop(semid, &buf, 1)`。再启动main_read程序，其中设置信号量初始值`u.val = 5`为5，则在运行到`semctl(semid, 0, SETVAL, u);`时，进程间的信号量就被立马设置为5，同时看到main_read显示如下输出后将信号量减到0就阻塞了：

```shell
ftok success!
semget success!
卖出1辆!
卖出2辆!
卖出3辆!
卖出4辆!
卖出5辆!
```

同时上述程序继续运行并循环输出：

```shell
ftok success!
semget success!
// 阻塞等待信号量变为0
sem value is 0
times: 1
times: 2
times: 3
```



### 5. 管理IPC的ipc命令簇

5.1 ipcs 查看命令

```
-m   查看shm(共享内存)
-q    查看msg(消息队列)
-s    查看sem(信号量)
```

5.2 ipcrm 删除命令

```
-m  删除shm(共享内存)
-q  删除msg(消息队列)
-s  删除sem(信号量)
```

注: shm   msg   sem 都必须先有一个key(key是根据fd来创建的)

## 11.13 [Linux进程间共享内存通信时如何同步](https://zhuanlan.zhihu.com/p/627763197)

在Linux中，进程间的共享内存通信需要通过同步机制来保证数据的正确性和一致性，常用的同步机制包括信号量、互斥锁、条件变量等。

其中，使用信号量来同步进程间的共享内存访问是一种常见的方法。每个共享内存区域可以关联一个或多个信号量，以保护共享内存区域的读写操作。在访问共享内存之前，进程需要获取信号量的使用权，当完成读写操作后，再释放信号量的使用权，以便其他进程可以访问共享内存区域。

### 1、信号量同步

下面是一个简单的示例程序，展示了如何使用信号量来同步共享内存区域的读写操作：

如下程序单独运行有问题，设置了信号量初始值为1，但等待信号量为0，会阻塞在`if (semop(semid, semops, 1) == -1) {`该行。

```cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <string.h>
 
#define SHM_SIZE 1024
#define SEM_KEY 0x123456
 
// 定义联合体，用于信号量操作
union semun {
    int val;
    struct semid_ds *buf;
    unsigned short *array;
};
 
int main() {
    int shmid, semid;
    char *shmaddr;
    struct sembuf semops[2];
    union semun semarg;
 
    // 创建共享内存区域
    shmid = shmget(IPC_PRIVATE, SHM_SIZE, IPC_CREAT | 0666);  // 这里每次用IPC_PRIVATE创建的共享内存会有唯一ID，所以只能用于进程内和线程，无法在进程间使用
    if (shmid == -1) {
        perror("shmget");
        exit(1);
    }
 
    // 将共享内存区域附加到进程地址空间中
    shmaddr = shmat(shmid, NULL, 0);
    if (shmaddr == (char *) -1) {
        perror("shmat");
        exit(1);
    }
 
    // 创建信号量
    semid = semget(SEM_KEY, 1, IPC_CREAT | 0666);
    if (semid == -1) {
        perror("semget");
        exit(1);
    }
 
    // 初始化信号量值为1
    semarg.val = 1;
    if (semctl(semid, 0, SETVAL, semarg) == -1) {
        perror("semctl");
        exit(1);
    }
 
    // 等待信号量
    semops[0].sem_num = 0;
    semops[0].sem_op = 0;
    semops[0].sem_flg = 0;
    if (semop(semid, semops, 1) == -1) {
        perror("semop");
        exit(1);
    }
 
    // 在共享内存中写入数据
    strncpy(shmaddr, "Hello, world!", SHM_SIZE);
 
    // 释放信号量
    semops[0].sem_num = 0;
    semops[0].sem_op = 1;
    semops[0].sem_flg = 0;
    if (semop(semid, semops, 1) == -1) {
        perror("semop");
        exit(1);
    }
 
    // 等待信号量
    semops[0].sem_num = 0;
    semops[0].sem_op = 0;
    semops[0].sem_flg = 0;
    if (semop(semid, semops, 1) == -1) {
        perror("semop");
        exit(1);
    }
 
    // 从共享内存中读取数据
    printf("Received message: %s\n", shmaddr);
 
    // 释放共享内存区域
    if (shmdt(shmaddr) == -1) {
        perror("shmdt");
        exit(1);
    }
 
    // 删除共享内存区域
    if (shmctl(shmid, IPC_RMID, NULL) == -1) {
        perror("shmctl");
        exit(1);
    }
 
    // 删除信号量
    if (semctl(semid, 0, IPC_RMID, semarg) == -1) {
        perror("semctl");
        exit(1);
    }
 
    return 0;
```
在这个示例程序中，使用了System V信号量来同步共享内存的读写操作。程序首先创建一个共享内存区域，并将其附加到进程地址空间中。然后，使用semget()函数创建一个信号量，并将其初始化为1。在写入共享内存数据之前，程序使用semop()函数等待信号量。一旦获取了信号量的使用权，程序就可以在共享内存区域中写入数据。写入数据完成后，程序再次使用semop()函数释放信号量的使用权。在读取共享内存数据时，程序同样需要等待信号量的使用权，读取数据完成后，再次释放信号量的使用权。

需要注意的是，使用信号量来同步共享内存访问时，需要确保每个进程都按照一定的顺序进行读写操作。否则，就可能出现死锁等问题。因此，在设计进程间共享内存通信时，需要仔细考虑数据的读写顺序，并采取合适的同步机制来确保数据的正确性和一致性。

### 2、互斥锁同步

互斥量也是一种常用的同步机制，可以用来实现多个进程之间的共享内存访问。在Linux中，可以使用pthread库中的互斥量来实现进程间共享内存的同步。

下面是一个使用互斥量实现共享内存同步的示例程序：

main_write.cpp

```cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>

#define SHM_SIZE 1024

// 共享内存结构体
typedef struct {
    pthread_mutex_t mutex;
    char data[SHM_SIZE];
} shm_data_t;

int main()
{
    int fd;
    shm_data_t* shm_data;
    pthread_mutexattr_t mutex_attr;
    pthread_mutex_t* mutex;

    // 打开共享内存文件
    if ((fd = shm_open("/my_shm", O_CREAT | O_RDWR, 0666)) == -1) {
        perror("shm_open");
        exit(1);
    }

    // 调整共享内存文件大小
    if (ftruncate(fd, sizeof(shm_data_t)) == -1) {
        perror("ftruncate");
        exit(1);
    }

    // 将共享内存映射到进程地址空间中
    if ((shm_data = static_cast<shm_data_t*>(mmap(NULL, sizeof(shm_data_t), PROT_READ | PROT_WRITE, MAP_SHARED, fd,
                                                  0))) == MAP_FAILED) {
        perror("mmap");
        exit(1);
    }

    // 初始化互斥量属性
    pthread_mutexattr_init(&mutex_attr);
    pthread_mutexattr_setpshared(&mutex_attr, PTHREAD_PROCESS_SHARED);

    // 创建互斥量
    mutex = &(shm_data->mutex);
    pthread_mutex_init(mutex, &mutex_attr);

    // 在共享内存中写入数据
    pthread_mutex_lock(mutex);
    sprintf(shm_data->data, "Hello, world!");
    pthread_mutex_unlock(mutex);  // 注释掉这行，则main_read程序会阻塞在pthread_mutex_lock(mutex);

    // 解除共享内存映射
    if (munmap(shm_data, sizeof(shm_data_t)) == -1) {
        perror("munmap");
        exit(1);
    }

    return 0;
}
```

本地验证：

* 如果注释掉这行：`pthread_mutex_unlock(mutex);`  则`main_read`程序会阻塞在`pthread_mutex_lock(mutex);`
* 如果注释掉这行：`pthread_mutex_unlock(mutex);`，再次运行`main_write`程序，由于对`shm_data->mutex`进行了`pthread_mutex_init`，所以并不会阻塞在`pthread_mutex_lock(mutex);`，而如果注释掉`pthread_mutex_init(mutex, &mutex_attr);`这行，则再次运行`main_write`，也同样会阻塞在`pthread_mutex_lock(mutex);`

main_read.cpp

```cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>

#define SHM_SIZE 1024

// 共享内存结构体
typedef struct {
    pthread_mutex_t mutex;
    char data[SHM_SIZE];
} shm_data_t;

int main()
{
    int fd;
    shm_data_t* shm_data;
    pthread_mutex_t* mutex;

    // 打开共享内存文件
    if ((fd = shm_open("/my_shm", O_RDWR, 0666)) == -1) {
        perror("shm_open");
        exit(1);
    }

    // 将共享内存映射到进程地址空间中
    if ((shm_data = static_cast<shm_data_t*>(mmap(NULL, sizeof(shm_data_t), PROT_READ | PROT_WRITE, MAP_SHARED, fd,
                                                  0))) == MAP_FAILED) {
        perror("mmap");
        exit(1);
    }

    // 创建互斥量
    mutex = &(shm_data->mutex);

    // 在共享内存中读取数据
    pthread_mutex_lock(mutex);
    printf("Received message: %s\n", shm_data->data); // Received message: Hello, world!
    pthread_mutex_unlock(mutex);

    // 解除共享内存映射
    if (munmap(shm_data, sizeof(shm_data_t)) == -1) {
        perror("munmap");
        exit(1);
    }

    return 0;
}
```

main_rm.cpp

```cpp
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>

#define SHM_SIZE 1024

int main() {
    // 删除共享内存文件
    if (shm_unlink("/my_shm") == -1) {
        perror("shm_unlink");
        exit(1);
    }

    return 0;
}
```

说明：

shm_open会在系统的共享内存目录`/dev/shm/`创建文件，而通过`ipcs`命令无法看到。

```shell
ben@ben-virtual-machine:~/Softwares/JetBrains/CppProjects/gtest_project/cmake-build-debug/src$ ll /dev/shm/
total 4
drwxrwxrwt  2 root root   60 Nov 11 22:10 ./
drwxr-xr-x 20 root root 4420 Nov  9 21:49 ../
-rw-rw-r--  1 ben  ben  1064 Nov 11 22:10 my_shm
ben@ben-virtual-machine:~/Softwares/JetBrains/CppProjects/gtest_project/cmake-build-debug/src$ cat /dev/shm/my_shm 
�Hello, world!
```

shm_open需要链接rt：

```cpp
target_link_library(xxx pthread rt)
```

[linux 共享内存 shm_open ，mmap的正确使用](https://blog.csdn.net/modi000/article/details/124164968)



### 3、条件变量同步

在Linux下，可以使用条件变量（Condition Variable）来实现多进程之间的同步。条件变量通常与互斥量（Mutex）结合使用，以便在共享内存区域中对数据进行同步访问。

条件变量是一种线程同步机制，用于等待或者通知某个事件的发生。当某个进程需要等待某个事件发生时，它可以通过调用pthread_cond_wait()函数来阻塞自己，并将互斥量释放。一旦事件发生，其他进程就可以通过调用pthread_cond_signal()或pthread_cond_broadcast()函数来通知等待线程。等待线程接收到通知后，会重新获取互斥量，并继续执行。

在共享内存通信中，可以使用条件变量来实现进程之间的同步。具体操作步骤如下：

初始化互斥量和条件变量。在创建共享内存之前，需要使用pthread_mutexattr_init()和pthread_condattr_init()函数分别初始化互斥量属性和条件变量属性。然后，需要使用pthread_mutexattr_setpshared()和pthread_condattr_setpshared()函数将互斥量属性和条件变量属性设置为PTHREAD_PROCESS_SHARED，以便多个进程可以共享它们。

等待条件变量。在读取共享内存之前，程序可以使用pthread_cond_wait()函数等待条件变量。调用pthread_cond_wait()函数会自动释放互斥量，并阻塞当前进程。一旦其他进程发送信号通知条件变量发生变化，等待线程就会重新获得互斥量，并继续执行。

发送信号通知条件变量变化。在向共享内存中写入数据之后，程序可以使用pthread_cond_signal()或pthread_cond_broadcast()函数发送信号通知条件变量发生变化。调用pthread_cond_signal()函数会发送一个信号通知等待线程条件变量发生变化，而调用pthread_cond_broadcast()函数会向所有等待线程发送信号通知条件变量发生变化。

使用条件变量来同步共享内存访问有以下几点注意事项：

1、程序必须使用互斥量来保护共享内存。在使用条件变量之前，程序必须先获取互斥量的使用权，以便保护共享内存区域中的数据不被多个进程同时访问。

2、程序必须保证条件变量的一致性。多个进程共享同一个条件变量时，必须保证条件变量的一致性。否则，可能会导致多个进程同时访问共享内存区域，导致数据错误或者系统崩溃。

3、程序必须正确使用条件变量。在使用条件变量时，需要正确地使用pthread_cond_wait()、pthread_cond_signal()和pthread_cond_broadcast()函数，否则可能会导致死锁或者其他问题。

4、程序必须正确处理信号。当调用pthread_cond_wait()函数时，程序可能会因为接收到信号而提前返回，此时程序需要正确地处理信号。

下面是一个使用条件变量实现进程间共享内存同步的示例代码：

```cpp
```







## 多线程同步

### 锁

[linux内核中的自旋锁,读写自旋锁,顺序锁,RCU](https://zhuanlan.zhihu.com/p/640309018)



#### [自旋锁](https://localsite.baidu.com/okam/pages/article/index?categoryLv1=教育培训&ch=54&srcid=10005&strategyId=132973381735471&source=natural)

自旋锁的定义：当一个线程尝试去获取某一把锁的时候，如果这个锁已经被另外一个线程占有了，那么此线程就无法获取这把锁，该线程会等待，间隔一段时间后再次尝试获取。这种采用循环加锁,等待锁释放的机制就称为自旋锁（spinlock）

为什么需要自旋锁

由于在多处理器的环境中某些资源的有限性，有时需要互斥访问，这时候就需要引入锁的概念，只有获取到锁的线程才能对临界资源进行访问，由于多线程的核心是CPU的时分片,所以同一时刻只能又一个线程获取到锁。但是那些没有获取到锁的线程该怎么办呢？

通常有两种做法：一种是没有获取到锁的线程就一直等待判断该资源是否已经释放了锁，这种锁叫做自旋锁,它不会引起线程阻塞（本质上是一种忙等待机制，避免线程切换带来的系统开销）。还有一种是，没有获取到锁的线程把自己阻塞起来，重新等待CPU的调度，这种锁称为互斥锁

自旋锁的原理

自旋锁实现的原理比较简单，当那些不能立马获取到锁资源的线程，它们不会像互斥锁那样直接将自己挂起进入阻塞状态，而是等一会（自旋）不判断的去判断锁资源是否被释放了，如果释放了那么就去获取锁资源。这样做避免了那些锁竞争不激烈的情况下，从核心态到用户态的切换，避免了系统上下文切换的开销。

因为自旋锁避免了操作系统进程调度的和线程的切换，所以自旋锁通常适用于在时间比较短的情况下。但是如果长时间上锁的话，自旋锁是非常消耗性能的，因为它阻止了其他线程调度。如果线程持有锁的时间很长，那么其他线程将一直保持旋转状态（不断的去判断锁资源是否被释放了，并没有让出CPU）。



### [Linux 线程同步](https://blog.csdn.net/weixin_45004203/article/details/129682834)

### 互斥锁

在线程里也有这么一把锁：互斥锁（mutex），也叫互斥量，互斥锁是一种简单的加锁的方法来控制对共享资源的访问，以此确保同时仅有一个线程可以访问某项共享资源（可以使用互斥量来保证对任意共享资源的原子访问）。

互斥锁有两种状态：已锁定（locked）和未锁定（unlocked）。任何时候，至多只有一个线程可以锁定该互斥锁。试图对已经锁定的某一互斥锁再次加锁，将可能阻塞线程或者报错失败，具体取决于加锁时使用的方法。

一旦线程锁定互斥锁，随即成为该互斥锁的所有者，只有所有者才能给互斥锁解锁。一般情况下，对每一共享资源（可能由多个相关变量组成）会使用不同的互斥锁，每一线程在访问同一资源时将采用如下协议：
1）访问共享资源时，在临界区域前使用互斥锁进行加锁。
2）在访问完成后释放互斥锁导上的锁。
3）对互斥锁进行加锁后，其他任何试图再次对互斥锁加锁的线程将会被阻塞，直到锁被释放。

### 读写锁

读写锁概述
当有一个线程已经持有互斥锁时，互斥锁将所有试图进入临界区的线程都阻塞住。但是考虑一种情形，当前持有互斥锁的线程只是要读访问共享资源，而同时有其它几个线程也想读取这个共享资源，但是由于互斥锁的排它性，所有其它线程都无法获取锁，也就无法读访问共享资源了，但是实际上多个线程同时读访问共享资源并不会导致数据安全问题。

在对数据的读写操作中，更多的是读操作，写操作较少，例如对数据库数据的读写应用。为了满足当前能够允许多个读出，但只允许一个写入的需求，线程提供了读写锁来实现。

读写锁的特点如下：

如果有其它线程读数据，则允许其它线程执行读操作，但不允许写操作。
如果有其它线程写数据，则其它线程都不允许读、写操作。
写是独占的，写的优先级高。
读写锁分为读锁和写锁，规则如下：
1）如果某线程申请了读锁，其它线程可以再申请读锁，但不能申请写锁。
2）如果某线程申请了写锁，其它线程不能申请读锁，也不能申请写锁。

读写锁应用场景：在对数据的读写操作中，更多的是读操作，写操作较少

### 生产者与消费者模型

线程同步典型的案例为生产者消费者模型，仅仅使用互斥锁也可以实现生产者与消费者模型（可以实现，但是会有很多缺点，改进版本可以看下文条件变量和信号量）。

什么是生产者与消费者模型？

假定有两个线程，一个模拟生产者行为，不断生产产品，一个模拟消费者行为，不断消费产品。所有的产品都存放在一个容器中，这个容器就是一个共享资源，两个线程同时操作这个共享资源（一般称之为汇聚），生产向其中添加产品，消费者从中消费掉产品。

生产者消费者模型中的对象：1、生产者；2、消费者；3、容器
生产者消费者模型中生产者可能会有多个，同理消费者也可能有多个，容器有一个
实现生产者消费者模型过程中会产生的问题：
问题1：数据安全问题——可以使用互斥锁、读写锁解决；
问题2：生产者生产的产品占满了容器，需要等待并通知消费者消费；消费者将容器中产品消费完了，需要等待并通知生产者生产——可以使用条件变量、信号量解决（可以实现，改进版本可以看下文条件变量和信号量）

**总结**：对于问题1，以上的生产者消费者模型可以很好解决，但是对于问题2，以上模型使用 `if else` 代替了条件变量和信号量，虽然也可以实现功能，但是如果容器里面没有数据了，消费者就需要不停地循环和判断，这样浪费计算资源。



### 条件变量

条件变量概述
与互斥锁不同，条件变量是用来等待而不是用来上锁的，条件变量本身不是锁。条件变量用来自动阻塞一个线程，直到某特殊情况发生为止。

通常条件变量和互斥锁同时使用：条件变量变量主要用于阻塞线程，但是它不能保证线程安全，如果要保证线程安全需要与互斥锁配合使用。

条件变量的两个动作：

条件不满，阻塞线程
当条件满足，通知阻塞的线程开始工作（解除阻塞）

### 信号量

信号量概述
信号量也叫信号灯，广泛用于进程或线程间的同步和互斥，信号量本质上是一个非负的整数计数器，它被用来控制对公共资源的访问。编程时可根据操作信号量值的结果判断是否对公共资源具有访问的权限，当信号量值大于 0 时，则可以访问，否则将阻塞。

PV 原语是对信号量的操作，一次 P 操作使信号量减１（占用1个资源），一次 V 操作使信号量加１（释放1个资源）。

信号量主要用于进程或线程间的同步和互斥这两种典型情况。




# 12. CI

## 12.1 [SonarQube简介](https://blog.csdn.net/dingk123/article/details/119728197)

**Sonar可以从以下七个维度检测代码质量。**

1. 不遵循代码标准

sonar可以通过PMD,CheckStyle,Findbugs等等代码规则检测工具规范代码编写。

2. 潜在的缺陷

sonar可以通过PMD,CheckStyle,Findbugs等等代码规则检测工具检测出潜在的缺陷。

3. 糟糕的复杂度分布

文件、类、方法等，如果复杂度过高将难以改变，这会使得开发人员 难以理解它们, 且如果没有自动化的单元测试，对于程序中的任何组件的改变都将可能导致需要全面的回归测试。

4. 重复

显然程序中包含大量复制粘贴的代码是质量低下的，sonar可以展示 源码中重复严重的地方。

5. 注释不足或者过多

没有注释将使代码可读性变差，特别是当不可避免地出现人员变动 时，程序的可读性将大幅下降 而过多的注释又会使得开发人员将精力过多地花费在阅读注释上，亦违背初衷。

6. 缺乏单元测试

sonar可以很方便地统计并展示单元测试覆盖率。

7. 糟糕的设计

通过sonar可以找出循环，展示包与包、类与类之间的相互依赖关系，可以检测自定义的架构规则 通过sonar可以管理第三方的jar包



## 12.2 [Git设置分支保护实现CodeReview卡点](https://blog.csdn.net/crisschan/article/details/100922668?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_title~default-0-100922668-blog-114101732.pc_relevant_default&spm=1001.2101.3001.4242.1&utm_relevant_index=3)



# 13. 安全

## 13.1 [渗透测试与红蓝队对抗的区别](https://blog.csdn.net/tao_405960616/article/details/106136934)

1、渗透测试

渗透测试，是通过模拟黑客攻击行为，评估企业网络资产的状况。通过渗透测试，企业及机构可以了解自身全部的网络资产状态，可以从攻击角度发现系统存在的隐性安全漏洞和网络风险，有助于进一步企业构建网络安全防护体系。渗透测试结束后，企业还可以了解自身网络系统有无合法合规、遵从相关安全条例。渗透测试作为一种全新的安全防护手段，让安全防护从被动转换成主动，正被越来越多企业及机构认可。

在渗透测试前，安全团队需得到企业及机构的授权，才可以开始。同时，需要与企业沟通及确定攻击目标、范围（内网or外网）、规则（时长、能深入到哪个程度）等。以上确定后，便开始渗透，主要分为以下几个步骤：

* 信息收集：收集攻击目标相关信息，如IP、网段、端口、域名、操作系统、应用信息、服务器类型、防护信息等。

* 发现漏洞：收集以上信息后，使用相应的漏洞进行检测，如系统有无及时打补丁、服务器配置有无错误、有无出现开发漏洞等。再对目标漏洞进行探测、分析、制定相应的攻击路径。

* 漏洞利用：对漏洞发动攻击，获得最高权限，取得敏感信息。

入侵结束后，需要清除入侵痕迹，并将整理渗透过程中资产信息、漏洞信息、运用工具等信息，最终形成报告，汇报给甲方。

2、红蓝队对抗

　　渗透测试是每个企业防护基础工作之一，但这紧紧代表企业网络系统正合法合规的运行着。然而企业的业务场景是动态变化的，黑客的攻击手法、0day漏洞更是层出不穷，企业的网络防护系统能否对此进行及时的应急响应呢？

　　企业及机构面临的挑战：

* 0day漏洞（升级漏洞组件、加waf规则等）

* 代码、框架层的业务漏洞

* DDos攻击、APT攻击等复杂多样的攻击

红蓝队对抗便是针对此方面的测试。红蓝队对抗是以蓝队模拟真实攻击，红队负责防御（与国外刚好相反），最终的结果是攻防双方都会有进步。红蓝队对抗能挖掘出渗透测试中所没注意到风险点，并且能持续对抗，不断提升企业系统的安全防御能力。

　　因内部技术人员对自身网络状况比较了解，所以一般红蓝队对抗会选用内部企业人员。

红蓝对抗例子-拉新红队薅羊毛（来源于阿里安全）

　　在模拟攻击过程中，**为了尽可能全面测试整个企业的网络系统，蓝队攻击手法会显得更复杂，而攻击路径的覆盖率更高。蓝队通过黑客视角，自动化的发起大规模、海量节点的实战攻击，以便测试红队在各个业务场景的应急响应能力**。

　　红蓝队对抗与渗透测试都是模拟黑客攻击，但有以下的不同点：

时间：在渗透测试中会制定明确的时间点完成（通常是两星期），而红蓝队对抗并无明确时间，两星期或半年都可以。

技术：红蓝队对抗不单止需要渗透技术，还需要懂得机器学习、自动化等技术。

过程：渗透测试过程是有条不絮的进行。而红队攻击过程中不会全面收集企业资产，也不会进行大规模漏洞扫描。红队攻击的策略主要是依据蓝队防护策略、工具等，拥有不定性。

输出：红蓝队对抗后会出现清晰的脆弱点、攻击路径及解决方案。

目的：渗透测试是为了了解自身网络资产是否存在风险点；而红蓝队对抗更对是了解自身网络资产能否在遭受攻击后能迅速进行应急响应。

关注点：红蓝队对抗更专注的是应用层上的漏洞，而不是信息技术上的漏洞。

3、总结

　　渗透测试和红蓝队对抗都是企业或机构最重要的防护手段，其结果都是为了应对当前不断增加的安全漏洞及复杂多样的网络攻击。只有企业不断经过渗透测试和红蓝对抗，形成漏洞闭环，才能构建强有力的安全防御体系。

## 13.2 [什么是SRE？SRE需要具备什么能力](https://www.jianshu.com/p/4fcbf6563177)

SRE最早是由Google提出的概念，其大概的意思就是：以标准化、自动化、可扩展驱动维护，用软件开发解决运维难题。这个岗位面世的时候，其根本要解决的问题就是打破传统研发人员快速迭代而引发的业务不稳定性，用以保证业务维护侧重的服务质量以及稳定性之间的平衡。

不同公司的SRE定位是不同的，可能某些公司的运维岗位也是SRE，因此，不能以偏概全，国内的SRE基本是以岗位来区分的，比如，有负责网络的SRE，有负责DBA的SRE，有专门负责业务的SRE，还有什么安全SRE等等。就谷歌所提到的SRE的理解来讲，基本都是以服务质量稳定为基线的维护工程师，只是对于SRE的要求是苛刻的，下面是我的个人理解：

- **第一：技能全面，比如网络、操作系统、监控、CICD、研发等，对于研发能力，可能不需要你精通，但是你需要具备可以使用一门语言完成某个功能的设计、开发与迭代。**
- **第二：打破传统运维思想壁垒，以产品角度思维贯穿整个业务架构服务质量为前提的沟通协调能力。**
- **第三：始终以软件工程解决问题为方向的规划之路。**
- **第四：很强的Trouble Shooting与思考、抽象能力，这三个能力在SRE工作当中是至关重要的，是时间与实践积累的最终成果。**



### 制定可交付的SLI/SLO/SLA

SLO 和 SLA 是大家常见的两个名词：**服务等级目标和服务等级协议。**

云计算时代，各大云服务提供商都发布有自己服务的 SLA 条款，比如 Amazon 的 EC2 和 S3 服务都有相应的 SLA 条款。这些大公司的 SLA 看上去如此的高大上，一般是怎么定义出来的呢？

说 SLA 不能不提 SLO，这个是众所周知的，但是还有一个概念知道的人就不多了，那就是 **SLI（Service Level Indicator）**，定义一个可执行的 SLA，好的 SLO 和 SLI 是必不可少的。

另外必须要提到的是，SLI/SLO/SLA主要是为服务指定的，如果没有服务作为依托关系，那这些概念也就失去了原有的意义。

下面是一个 SLA 在制定过程中需要考虑到的一些问题：

例如：设计一个可用率的时候，并不是说达到了”4个9“为标准就足够了，因为我们需要考虑如下问题：

1. 如何定义这个可用率？比如我们以可用率 > 99.9% 为目标，有一个服务部署了 5 个 区域, 那么有一个 区域 挂了，其余的 区域 是可用的，那么可用率被破坏了吗？这个可用率是每一个 区域 的还是所有的 区域 一起计算的？
2. 可用率计算的最小单位是什么？如果 1min 内有 50s 没有达到可用率，那么这一分钟算是 down 还是 up？
3. 可用率的周期是怎么计算的？按照一个月还是一个周？一个周是最近的 7 天还是计算一个自然周？
4. 如何设计与规划 SLI 和 SLO 监控？
5. 如果错误预算即将用完，有什么处理措施？比如减少发布以及配置变更？

### Service

**什么是服务？**

简单说就是一切提供给客户的有用功能都可以称为服务。

服务一般会由服务提供者提供，提供这个有用功能的组织被称为服务提供者，通常是人加上软件，软件的运行需要计算资源，为了能对外提供有用的功能软件可能会有对其他软件系统的依赖。

客户是使用服务提供者提供的服务的人或公司。

#### SLI

SLI 是经过仔细定义的测量指标，它根据不同系统特点确定要测量什么，SLI 的确定是一个非常复杂的过程。

SLI 的确定需要回答以下几个问题：

1. 要测量的指标是什么？

2. 测量时的系统状态？

3. 如何汇总处理测量的指标？

4. 测量指标能否准确描述服务质量？

5. 测量指标的可靠度 (trustworthy)？

6. ##### 常见的测量指标有以下几个方面：

- 性能
  - 响应时间 (latency)
  - 吞吐量 (throughput)
  - 请求量 (qps)
  - 实效性 (freshness)
- 可用性
  - 运行时间 (uptime)
  - 故障时间 / 频率
  - 可靠性
- 质量
  - 准确性 (accuracy)
  - 正确性 (correctness)
  - 完整性 (completeness)
  - 覆盖率 (coverage)
  - 相关性 (relevance)
- 内部指标
  - 队列长度 (queue length)
  - 内存占用 (RAM usage)
- 因素人
  - 响应时间 (time to response)
  - 修复时间 (time to fix)
  - 修复率 (fraction fixed)

下面通过一个例子来说明一下：hotmail 的 downtime SLI

- 错误率 (error rate) 计算的是服务返回给用户的 error 总数
- 如果错误率大于 X%，就算是服务 down 了，开始计算 downtime
- 如果错误率持续超过 Y 分钟，这个 downtime 就会被计算在内
- 间断性的小于 Y 分钟的 downtime 是不被计算在内的。

1. ##### 测量时的系统状态，在什么情况下测量会严重影响测量的结果

- 测量异常 (badly-formed) 请求，还是失败 (fail) 请求还是超时请求 (timeout)
- 测量时的系统负载（是否最大负载）
- 测量的发起位置，服务器端还是客户端
- 测量的时间窗口（仅工作日、还是一周 7 天、是否包括计划内的维护时间段）

1. ##### 如何汇总处理测量的指标？

- 计算的时间区间是什么：是一个滚动时间窗口，还是简单的按照月份计算
- 使用平均值还是百分位值，比如：某服务 X 的 ticket 处理响应时间 SLI 的
- 测量指标：统计所有成功解决请求，从用户创建 ticket 到问题被解决的时间
- 怎么测量：用 ticket 自带的时间戳，统计所有用户创建的 ticket
- 什么情况下的测量：只包括工作时间，不包含法定假日
- 用于 SLI 的数据指标：以一周为滑动窗口，95% 分位的解决时间

1. ##### 测量指标能否准确描述服务质量？

- 性能：时效性、是否有偏差
- 准确性：精度、覆盖率、数据稳定性
- 完整性：数据丢失、无效数据、异常 (outlier) 数据

1. ##### 测量指标的可靠度

- 是否服务提供者和客户都认可
- 是否可被独立验证，比如三方机构
- 客户端还是服务器端测量，取样间隔
- 错误请求是如何计算的

#### SLO

**SLO (服务等级目标)** 指定了服务所提供功能的一种期望状态。SLO 里面应该包含什么呢？所有能够描述服务应该提供什么样功能的信息。

服务提供者用它来指定系统的预期状态；开发人员编写代码来实现；客户依赖于 SLO 进行商业判断。SLO 里没有提到，如果目标达不到会怎么样。

SLO 是用 SLI 来描述的，一般描述为：  比如以下 SLO：

- 每分钟平均 qps > 100k/s
- 99% 访问延迟 < 500ms
- 99% 每分钟带宽 > 200MB/s

**设置 SLO 时的几个最佳实践：**

- 指定计算的时间窗口
- 使用一致的时间窗口 (XX 小时滚动窗口、季度滚动窗口)
- 要有一个免责条款，比如：95% 的时间要能够达到 SLO
- 如果 Service 是第一次设置 SLO，可以遵循以下原则
- 测量系统当前状态
  - 设置预期 (expectations)，而不是保证 (guarantees)
  - 初期的 SLO 不适合作为服务质量的强化工具
- 改进 SLO
  - 设置更低的响应时间、更改的吞吐量等
- 保持一定的安全缓冲
  - 内部用的 SLO 要高于对外宣称的 SLO
- 不要超额完成
  - 定期的 downtime 来使 SLO 不超额完成

设置 SLO 时的目标依赖于系统的不同状态 (conditions)，根据不同状态设置不同的 SLO：**总 SLO = service1.SLO1 *weight1 + service2.SLO2 \*weight2 + …**

为什么要有 SLO，设置 SLO 的好处是什么呢？

- 对于客户而言，是可预期的服务质量，可以简化客户端的系统设计
- 对于服务提供者而言
  - 可预期的服务质量
  - 更好的取舍成本 / 收益
  - 更好的风险控制 (当资源受限的时候)
  - 故障时更快的反应，采取正确措施
  - SLO 设好了，怎么保证能够达到目标呢？
  - 需要一个控制系统来：

监控 / 测量 SLIs  对比检测到的 SLIs 值是否达到目标  如果需要，修正目标或者修正系统以满足目标需要  实施目标的修改或者系统的修改  该控制系统需要重复的执行以上动作，以形成一个标准的反馈环路，不断的衡量和改进 SLO / 服务本身。

我们讨论了目标以及目标是怎么测量的，还讨论了控制机制来达到设置的目标，但是如果因为某些原因，设置的目标达不到该怎么办呢？

也许是因为大量的新增负载；也许是因为底层依赖不能达到标称的 SLO 而影响上次服务的 SLO。这就需要 SLA 出场了。

#### SLA

SLA 是一个涉及 2 方的合约，双方必须都要同意并遵守这个合约。当需要对外提供服务时，SLA 是非常重要的一个服务质量信号，需要产品和法务部门的同时介入。

SLA 用一个简单的公式来描述就是：SLA = SLO + 后果

- SLO 不能满足的一系列动作，可以是部分不能达到
  - 比如：达到响应时间 SLO + 未达到可用性 SLO
- 对动作的具体实施
  - 需要一个通用的货币来奖励 / 惩罚，比如：绩效得分等

SLA 是一个很好的工具，可以用来帮助合理配置资源。一个有明确 SLA 的服务最理想的运行状态是：**增加额外资源来改进系统所带来的收益小于把该资源投给其他服务所带来的收益。**

一个简单的例子就是某服务可用性从 99.9% 提高到 99.99% 所需要的资源和带来的收益之比，是决定该服务是否应该提供 4 个 9 的重要依据。

### 故障复盘的唯一目的是减少故障的发生。有几个我目前认为不错的做法。

**故障复盘需要有文档记录**，包括故障发生的过程，时间线的记录，操作的记录，故障恢复的方法，故障根因的分析，为什么故障会发生的分析。文档应该隐去所有当事人的姓名对公司的所有人公开。很多公司对故障文档设置查看权限，我觉得没什么道理。有些公司的故障复盘甚至[对外也是公开的](https://links.jianshu.com/go?to=https%3A%2F%2Fgithub.com%2Fdanluu%2Fpost-mortems)。

**故障在复盘的时候应该将当事人的名字用代码替代，可以营造更好的讨论氛围。**

**不应该要求所有的故障复盘都产生 Action**。之前一家的公司的故障复盘上，因为必须给领导一个“交待”，所以每次都会产生一些措施来预防相同的故障再次发生，比如增加审批流程之类的。这很扯，让级别很高的领导审批他自己也看不懂的操作，只能让领导更痛苦，也让操作流程变得又臭又长，最后所有人都会忘记这里为什么会有一个审批，但是又没有人敢删掉。你删掉，出了事情你负责。

Blame Free 文化？之前我认为是好的。但是后来发现，**有些不按照流程操作导致的问题确实多少应该 Blame 一下，**比如下线服务的时候没有检查还没有 tcp 连接就直接下线了，或者操作的时候没有做 canary 就全部操作了，这种不理智的行为导致的故障。但是条条框框又不应该太多，不然活都没法干了。

## 13.3 [服务稳定性治理](https://zhuanlan.zhihu.com/p/463919999)

线上稳定性一直是运维工作的核心，从第一个需要系统管理员维护的系统上线开始，运维的工作内容就是维护线上业务的稳定性。从早期系统管理员处理系统硬件和系统软件故障，到应用运维工程师、数据库管理员分别对各自领域的故障进行治理，最后到SRE统一治理平台、微服务等架构，可以发现运维角色在不断衍化，但不变的是运维工程师始终都在处理各种故障。随着技术的发展，线上业务已经呈现分层治理的模式，线上故障的定位也变得越来越复杂。

本章将探讨与线上稳定性相关的话题，通过之前我们团队在云音乐、电商等多个大型业务的实际运维经验，分享我们团队在故障处理领域的实践经验。

### 6.1 SLI/SLO/SLA的制定和落地

从传统运维转变为SRE后，我们对线上业务的稳定性有了全新的定义。在传统运维模式下，业务都是比较简单的架构，业务稳定性主要靠用户反馈或业务主观感受进行评估。现在业务的复杂度已经无法依赖人工方式治理稳定性问题，所以迫切需要一种全新自动化的方式来治理业务稳定性问题。

在计算机领域，如果需要将一种指标进行自动化处理，唯一的途径就是将问题数字化。SRE在处理稳定性时引入了**SLI（Service Level Indicator，服务等级指标）**、**SLO（Service Level Objectives，服务等级目标）**和**SLA（Service Level Agreement，服务等级协议）**用于量化线上稳定性。实践发现数据化的衡量方式对服务稳定性有更精准的评估，而且业务运行的稳定性状态也更加直观和方便管控。

#### 6.1.1 SLI的制定和应用

SLI指服务稳定性的几个关键指标。线上业务很多指标都有监控，但并不是所有指标都是关键的。例如，纯内存运行的Memcached服务，它的磁盘空间指标不是关键指标，有时候系统出现磁盘完全不可写情况，也不影响Memcached服务的正常运行。Memcached服务的Key/Value读写的RT（Round-Trip，延迟指标）对业务稳定性是非常关键的，所以我们在制定与Memcached服务相关的SLI时，会将缓存Key/Value读写的RT列为服务的SLI。

在实际工作中，不同的服务会有不同的SLI，SLI是一个复杂的过程。常见的对外服务SLI指标有以下几个方面。

##### 1. **性能指标**

性能指标主要是延迟（Latency）、吞吐量（Throughput）、处理速率（TPS/QPS）、时效性（Freshness）等。性能指标一般会直接和应用的性能监控数据对接。

##### 2. **可用性指标**

可用性指标主要是可靠性、故障时间/频率、在线时间等。可用性指标一般会基于外部的监控数据，如错误频率可以和APM应用监控数据关联。

##### 3. **质量指标**

质量指标主要是准确性（Accuracy）、正确性（Correctness）、完整性（Completeness）、覆盖率（Coverage）等。质量指标一般会用于衡量大数据、AI等对数据处理结果有要求的业务，一般会依赖事后数据统计监控。

SLI除可以衡量服务的外在表现外，还可以衡量线上业务活动的各个方面。例如，运维团队支持情况如果有SLO，就会有SLI，这个时候的SLI可能会是要求运维团队值班时响应需求的时间和工单处理的时效等。

每个业务的SLI都需要和业务监控数据对应，在实施时注意业务的SLI不要太多，过多的SLI会导致衡量问题困难，所以一般情况下单业务的SLI建议不超过3个。在制定SLI时建议站在用户的角度去提炼核心指标，只提取用户最关心的几个指标。

业务制定的SLI说明了业务对外输出的重要指标承诺，明确的SLI是一个对应衡量对象的优化方向。如果一个线上业务的SLI是延迟，那么开发团队和运维团队就会全力优化所有和延迟相关的环节，如延迟指标危险时，开发团队和运维团队就会推动代码优化、网络设备优化等方案来保障业务SLI满足指标。

通俗地讲SLI是指引SRE团队和业务团队优化业务指标的，SLI代表了业务的核心质量指标，反映了用户对服务的核心质量诉求。

#### 6.1.2 SLO的计算和应用

SLO是团队努力要达成的一个质量指标，一般情况下会是业务团队承诺给用户的一个指标。当我们说一个业务的SLO指标要4个9（99.99%）时，用户就明白该业务团队做出了99.99%的可用性保障，然后整个SRE团队会以这个指标作为衡量工作绩效的标杆并努力达成这个指标。

当业务不满足SLI时，就会触发SLO的计算。以线上Nginx处理能力为例，如果我们定义了一个SLI，要求Nginx的处理速率大于10000 req/s，SLO大于99.99%，当被DDoS攻击时，Nginx的处理速度降低到了9999 req/s，并且持续了30分钟，那么在计算时Nginx的SLO就会被扣除30分钟。如果按天算SLO为1-30/60/24×100%=97.9%，则说明当天Nginx的SLO是不满足用户期望的，需要SRE团队有针对性的改进。

正常情况下SLO会以一个月、一个季度、半年、一年这样的周期进行结算。例如，AWS等云计算平台，一般会以一个月为周期计算，而一个季度、半年、一年这样的周期普遍用于绩效考核、内部服务质量考核等场景。

SLO既可以用于承诺付费用户的场景，又可以用于承诺非付费用户的场景；既可以对公司内部用户应用，又可以对公司外部用户应用。在运作时，SLO只是一个承诺指标，即使真的没有完成，对业务团队来说最坏的情况也就是用户不满意。当然如果有团队的SLO没完成，从公司管理角度说，肯定会影响这个团队的绩效。

从公司管理角度出发，为每个团队的业务设置SLO是非常有意义的，通过设立SLO敦促业务团队努力地达成承诺目标。例如，公司内部的IT客服如果设立了SLO，那么IT客服就会为了SLO大促，努力地改进SLI和提升IT客服响应速度等。

#### 6.1.3 SLA的计算和应用

SLA一般用于付费用户的场景，一般会具有一定的法律效力。当一个服务和用户签订SLA时，往往涉及服务付费、承诺质量和违约责任等内容。

相比SLO是由业务团队自己设立的，SLA很可能是非技术领域的律师或销售人员设立的。大部分情况下SLA在落地上的主要问题是非技术人员为了卖服务，做出的很多和当前业务情况不符合的承诺。

在实际情况中定制SLA必须清晰，要和法律合同一样严谨，否则就会形成争议。例如，我们虚拟一个线上对象存储服务，承诺的SLA是如果线上文件有问题，那么就可以24小时内修复。如果一个用户花了24小时才描述清楚这个线上文件的问题，那么文件存储服务的支持人员是在收到问题后马上修好，还是从用户描述清楚问题后开始计算24小时内修复的承诺呢？

对业务团队来说，在理想的情况下，SLA最好和业务团队的SLO一致，这样会比较符合业务发展的实际情况。如果SLA没有达成，那么公司就会面临违约赔钱的情形。正常情况下SLA是和付费用户签订的，免费用户不需要签订SLA。

SLA/SLO/SLI样例如图6-1所示，SLA是业务团队对付费用户的法律承诺，SLO是业务团队想达成的目标，SLI则是业务团队为满足SLO需要做到的技术指标。



![img](https://pic3.zhimg.com/80/v2-6ee2630be106b929a86866b75071ed36_720w.jpg)图6-1 SLA/SLO/SLI样例



### 6.2 故障预防

在传统运维模式下，对于谁应该为线上业务的稳定性负责这个问题，很多开发团队都会将责任推给运维团队。当线上出现故障时，运维流程、运维操作、运维工程师的响应总能被挑出问题。最常见的情况是线上只要出现故障，事后复盘基本都有与运维团队相关的改进项。运维工程师最直接的感受就是只要线上出现故障，肯定就无法规避责任。

为了防止线上故障让运维团队“背锅”的情况，运维团队也会针对性地梳理线上问题，去优化可以优化的运维点。以历史上某次事故为例，业务代码因为存在某个很隐蔽的bug，所以运行5分钟后出现请求异常，执行发布的配置管理员刚好因为某种原因没有第一时间回滚，那么事后的事故复盘可能会如表6-1所示。



![img](https://pic1.zhimg.com/80/v2-305250bc768e75af04e20d2c841c5350_720w.jpg)表6-1 某事故沟通记录



当然从DevOps或SRE的角度看，改进的点不只是表6-1所示的内容，但如果是传统运维，那么一切就理所当然了。因为线上有太多类似的故障，所以才会有“运维7×24小时待命”的说法。在互联网界，提到运维就会想起地铁上报警电话响起，运维工程师掏出笔记本现场干活的场景。同样是工程师，开发工程师周末可以出去钓鱼，但运维工程师周末去哪里都得背着笔记本式计算机。如果运维工作方式不进行演化，那么这种情况会贯穿整个运维工程师的职业生涯。

对于所有线上故障，其实最好的处理方式是防患于未然，在线上还没有出现问题时就跟进相关运维工作。传统运维团队通常是在业务上线后审查各个模块，看看有什么模块可以优化部署，再推进相关的优化工作。我们团队之前也用过这样的模式，并且在多个项目中工作良好，但随着业务转型微服务、全面上云等技术演化，传统运维模式慢慢地不适用了。

当我们团队第一次全面接手一个大型的电商业务时，在业务初期所有人都不知道电商业务的运作模式。我们以传统的工作模式对接电商业务的运维需求，但是随着电商业务的蓬勃发展，线上模块不断拆分迭代，我们发现传统的工作模式已经不能很好地适应电商业务需求。电商业务模块上线后，等运维团队再去跟进时，却发现电商业务已经有了很大的稳定性风险。在出过几次线上事故后，运维团队和开发团队都意识到了这个问题，并开始探索相关的优化策略。

就是因为这样的事件，通过多次磨合，我们团队慢慢地从传统运维转型为SRE。我们发现预防工作的最佳切入点是在业务架构设计之初。在刚开始设计一个业务时，业务团队如果有意识地邀请运维团队参与架构评审，一般能规避以下几种常见问题。

### **（1）资源容量问题。**

开发团队在设计模块时，一般会对业务发展有初步的预估，能给出请求速率（QPS）、吞吐量等基本业务指标预测，有经验的运维团队能直接根据这些数据评估出具体的资源需求。例如，需要多少特定规格的云主机，新模块上线是否会要求原有模块进行扩容等，而不是等到业务要上线时才发现缺乏机器资源、线上数据库需要扩容等问题。

### **（2）高可用架构和自愈设计问题。**

业务团队在实现某个服务时，往往会关注业务逻辑的实现。例如，新的服务是一个定位服务，开发团队可能关注了很多的算法实现，但是很少考虑部分定位服务器发生宕机的场景，也很少考虑定位服务依赖的缓存服务器发生切换的问题。这些不确定通常会导致当线上真正出现问题时，团队成员不知道怎么做才可以更快恢复线上问题。

相比业务团队关注功能实现，运维团队更关注业务模块的高可用架构和自愈设计。如果线上每个业务模块都有高可用架构和自愈设计，那么运维团队的工作就会轻松很多。所以当SRE团队参与业务架构设计时，肯定会提醒开发团队在业务架构设计方面进行规划。

### **（3）业务降级防护问题。**

很多线上业务的用户量是捉摸不定的，尤其是在大型站点中很多功能是可以被模块化配置的，业务团队可以通过配置就能在业务活动中调用各自的功能模块。在这种情况下，如果有业务模块的容量评估不到位，那么某个普通的推广活动就能撑爆业务容量，进而导致业务故障，而在实际工作中，开发团队很少能想到这么多，即使能想到也会是想通过资源扩容的方式来解决这些问题。

SRE团队在这方面就会有很多的经验和落地思路，能从业务特性和资源情况方面提供技术建议，如建议业务统一接限流模块和建议业务做降级开关等。

除了以上参与业务架构设计评审的办法，运维团队还能利用已有的工具和资源优化业务架构设计。

### **（1）推动技术方案统一。**

当公司中有多个开发团队时，常常会发现总有几个开发团队在重复制造“轮子”。有些公司可能会想通过推技术中台等方式规避这种行为，但更多的情况是“轮子”造得越来越隐蔽，等到发现时已成定局，然后想砍掉几个“轮子”时，就会发现各个开发团队的“轮子”都已经是定制化的。

SRE团队在这方面的推动有比较明显的优势。首先是能较早地发现这样的行为，其次是可以推广内部已有的实现方案，最终在很大程度上提升业务模块的技术统一性。

### **（2）复用原有成熟方案。**

相比开发团队，SRE团队虽然不清楚业务逻辑实现，但对业界的成熟的高可用方案、自愈方案、降级方案有全面的了解和应用。之前我们有遇到过一个团队在项目时间很少的场景下，想实现一个服务发现逻辑的情况，在深入了解业务的实际需求后，SRE团队给出了多个成熟的技术解决方案，如使用DNS域名方式、使用Dubbo RPC框架，或者直接用云平台封装一层负载均衡。最终业务方结合项目进度要求，选择了现成的Dubbo RPC框架，实现了更快速的业务交付。

正常情况下整个团队都应该使用成熟的方案和统一的技术方案，这样无论是迭代周期还是稳定性都更有保障。当整个团队需要实现一些原有技术堆栈无法满足的突破性指标时，一般情况下也是建议统一改进原有模块。例如，团队使用的一个Redis缓存连接库无法满足新业务对缓存Key打散的需求，一般会建议让原来开发Redis缓存连接库的团队进行跟进。一是为了防止多个团队分别维护不同的“轮子”；二是为了提升团队技术线的专一性，让专业的团队做专业的事情往往会更有效率。

### 6.3 抑制不可控因素

俗话说变更是“万恶之源”，运维工程师不喜欢太多变更的主要原因是变更会引入不可控因素，而不可控因素会导致线上业务不稳定。线上每次出现故障都可以认为是不可控因素导致的。运维团队为了控制不可控因素，做了很多的努力，如变更管理、变更控制等。从实践上讲，变更管理等机制的落地很好地降低了线上不可控因素，提升了业务的稳定性。

线上变更操作类似做实验，一次只能调整一个因素，否则就会失去和旧版本的对比。如果线上在同一个时刻有很多不可控因素，那么在执行变更时就有很大概率会失败。在传统运维模式下，因为运维角色的领域区分，很多操作是没有信息同步机制的，所以如果多人同时操作变更，往往会埋下故障隐患。

例如，业务团队好不容易申请到了一个维护窗口，开发工程师、系统管理员、运维工程师、数据库管理员都需要参与，结果在操作时大家各自执行各自的变更，没有同步操作。例如，数据库管理员切数据库主从，系统管理员在关机维修硬件，运维工程师在调整配置发布新的代码，每个角色都觉得自己的操作评估不会出现问题，但出现问题时，根本无法快速定位具体是哪个操作出现的问题。出现问题时如果没有定位问题，往往意味着没办法快速恢复，没办法快速恢复就意味着出现线上故障。

为解决这种多角色变更冲突的问题，传统运维团队会引入内部协调沟通机制，通过调整变更顺序的方式规避冲突。这种内部协调沟通机制虽然在针对某个业务线时运作良好，但当遇到多个业务线相互依赖时，在流程实施上会遇到很多困难。因为每个业务线都有自己的开发节奏，所以如果进行多个业务线的变更顺序同步，沟通成本和时间成本会非常大。

从传统运维团队转变为SRE团队后，对于多业务团队变更协调问题，SRE团队专门做了一些工具和平台进行自动化的协调。相比过去人工协调的方式，SRE团队的工具会依赖不同的数据进行协调控制。在公司内部，越大的项目会越多地依赖第三方业务线，如果没有工具协助管理，那么线上业务的稳定性就会非常脆弱。

和很多工程领域的实践一样，线上业务的稳定性是不能靠运气的。以飞机为例，飞机的每个起飞检查和流程执行都是为了减少不确定性。线上业务的SRE团队也必须搞清楚所有的疑问后才可以执行变更。

那么应该如何减少不可控因素呢？SRE团队可以从以下几个方面推动。

#### 1. **运维检查清单建设**

线上运维执行变更前，会像飞行员一样执行一系列的运维检查清单，正常情况下需要按照清单逐项确认，最终确认没有问题后才会执行变更。很多时候运维检查清单的详细程度会和系统成熟度、团队整体能力等因素挂钩，因为运维检查清单需要长期建设和维护（包括信息的沉淀和平台的集成）。

#### 2. **变更信息自动化协调工具建设**

多团队的变更协调一直是维护的难点，很多公司不同的业务可能有不同的变更系统。因为每个业务对变更的需求是不一样的，如大数据变更有大数据变更的逻辑，业务变更有业务变更的逻辑，PaaS（Platform as a Service）和IaaS（Infrastructure as a Service）有独立的平台变更逻辑等。这些业务在不同的业务代码上是独立的，但在运作上又有相互依赖的情况（如云平台发布时，业务可能无法做到扩容）。从业务稳定性的角度看，不同系统的变更如果有关联，那么就必须要做到关联协调。

SRE团队会对涉及风险、变更因素、影响范围等因素进行抽查，结合SRE团队的人工处理流程逻辑，沉淀到类似变更平台等内部工具系统。

#### 3. **推动业务解耦合**

SRE团队除从传统的运维角度推动外，还有就是推动业务进行架构拆分解耦。SRE团队在这方面主要的工作是对业务模块制定规范和流程，还有就是了解业务特性发现有强耦合的模块。当每个模块的变更不影响调用链路的上下游时，整条业务线多个模块的变更就不会相互受到影响，当然在这方面很重要的工作就是开发团队必须保障模块的API（Application Program Interface）在变更前后是兼容的。

从控制不可控因素角度看，DevOps力推的小迭代、高频率发布模式从某种意义上说也是为了减少不可控因素，因为DevOps的核心是建设CI/CD的过程。

### 6.4 故障演练

说到线上业务故障，故障预案是一个无法规避的问题。在我们团队经历过的故障中，业务团队除了会追问运维团队故障原因，还会追问有没有故障预案。传统运维团队会从自己负责的领域出发，梳理各种故障，制定相应的故障预案。

#### 6.4.1 故障梳理

在传统运维模式下，当线上发生故障时，运维团队经常会发现与故障预案相关的问题。最常见的故障预案问题有以下两类。

##### 1. **不在评估预期内的故障类型**

在线上比较常见的是出现不在评估预期内的故障类型，尤其是当运维团队不清楚业务的实现细节时，更容易出现不在评估预期内的故障类型，也更容易发生故障评估不到位的情况。一旦发生故障评估不到位的情况，就会发现没有现成的故障预案。

##### 2. **故障预案执行有问题**

执行故障预案不只是一个人的事情，通常会需要比较多的团队成员配合执行。当一个比较罕见的故障发生时，很可能因为没有演练原来设计的预案，而发生执行上的偏差。最常见的就是团队成员操作不熟练，某个环节评估不到位，导致预案执行没有得到预期的效果。

这两类问题的发生，在很大程度上和传统运维的工作模式有关系。在传统运维模式下，运维团队和开发团队是独立的团队，两者的绩效考核会有很大的差异。运维团队虽然做了很多故障预案，演练时还需要开发团队的配合，但开发团队的核心绩效不在线上稳定性上，这时运维团队和开发团队在故障演练配合上就会遇到很大的问题。

#### 6.4.2 故障预案

在传统运维团队中，不同角色对线上故障预案的制定会有很大的不同。同样一个数据库服务器宕机问题，数据库管理员的预案可能是切主从数据库，应用配置管理员的预案可能是重启进程，而系统管理员的预案则可能是先把服务器连起来再查问题。3种不同的工作视角，导致3种角色对同一个问题有不同的处理方式。每个运维角色的预案单独看是没有问题的，但当涉及的故障有交叉时，如数据库是自动切换时，将宕机的服务器连起来可能又会触发一次数据库切换。

从SRE团队的角度看，同一个问题的预案一次只能执行一个。前面提到的数据库服务器宕机问题，SRE团队会从业务全局出发，将上面3种处理方式的内在逻辑整合成一个。相比传统运维团队，SRE团队更注重对运维操作的提炼，也因此更加注重自动化工具的建设。线上测试技术的进化和SRE新理念的实践反映到故障演练上，形成了以下两个明显的优势。

##### 1. **故障类型完备度**

SRE团队与传统运维团队相比对业务底层有更深度的了解，SRE团队成员梳理的故障类型更全，也更符合开发团队的执行思路。SRE团队能更全面地看问题，也能让业务团队对业务模块的故障情况有清楚的认知。

##### 2. **故障模拟更加容易**

得益于最近几年“混沌工程”的落地和推广，传统运维团队需要开发团队配合故障触发等因素，慢慢变得不是问题。很多时候SRE团队只要提交申请就可以在特定的时间窗口实现故障触发。

#### 6.4.3 混浊工程

“混浊工程”指的是一种随机在线上注入故障，用于检测线上业务健壮性的工程手段，一开始由Netflix提出并实践，还有开源对应的项目“ChaosMonkey”。这个项目的背景是当一个业务进行微服务化后，线上的调用链路会非常复杂，模块也会非常多。不同模块的开发工程师良莠不齐，单纯靠人工是无法发现系统的薄弱环节的。如果没有发现系统的薄弱环节，那么线上就无从谈起故障预案。

因为“混浊工程”的随机性注入，所以能发现某些人工排查时无法覆盖的故障场景，给线上业务提供了更全面的问题场景。能被“混浊工程”多次考验的业务，通常在实际运行中往往有更好的稳定性表现，所以各大公司纷纷投入人员对“混浊工程”进行研究应用。

可以看到的一个趋势是：“混浊工程”正在加速普及，并对故障预案、故障发现、故障处理等各个环节提出了更高的要求。在传统运维模式中，当线上业务运作时，整个团队最担心的是故障没有预案，或者是有故障预案但没信心执行故障预案。现在有了“混浊工程”在线上担当一个故障触发器的角色，这种随机式的故障注入，要求整个团队对每个组件的每个类型的故障都制定处理预案，而且强迫整个团队能制定预案和能执行预案。故障演练执行表如表6-2所示。



![img](https://pic3.zhimg.com/80/v2-cbeb83fc24372835ae65036c73f01da2_720w.jpg)表6-2 故障演练执行表



只要在业务中多次执行故障演练执行表中的内容，故障梳理、故障预案和故障执行的每个环节都会被整个团队的每个成员牢牢记住。另外这样的“攻防”演习在执行多次后，很多预案其实会被转换成自动化的策略。故障注入和恢复图如图6-2所示。



![img](https://pic2.zhimg.com/80/v2-ce676a8a15d3721d5b23e540f729fcc9_720w.jpg)图6-2 故障注入和恢复图



如图6-2所示，在实际执行时，故障演练最终还是以故障预案平台这样的工具建设为主。因为当前业务云化、容器化、微服务化，所以实际上已经很难单纯依靠人工执行预案，更多的是依赖工具发现问题，然后由SRE团队转化为平台上的各种自动化脚本。

### 6.5 故障自愈

传统运维的日常工作会接触很多的故障自愈。很多人常说的配置一个Keepalived VIP（Virtual IP，虚拟IP）和配置一个MySQL主从等运维操作，从某种意义上都涉及了和故障自愈相关的概念。例如，Keepalived VIP配置好后，挂掉1台节点，Keepalived VIP自动迁移到备节点和MySQL主从挂掉主节点，自动切换到备节点的逻辑。这些在平时没少接触的配置，让运维团队躲过了无数的故障，也充分说明了故障自愈对业务稳定性起着至关重要的作用。

故障自愈不只是上面提及的运维Keepalived VIP、MySQL主从等部署，它代表的是一整套的设计理念和运维想法。从落地方法和实现上说，故障自愈有以下多个层级。

#### **基于主备的设计**

基于主备的设计实现的自愈，主要依赖对服务进行主备节点的设计和部署，重点是通过设计可切换的逻辑，实现单节点故障的快速恢复，比较典型的是开源的Keepalived实现。这个级别的自愈实现容错度较低（很多只支持单节点容错），在部署上会有一些特殊的依赖设计（如Keepalived对网段的要求）。

需要注意的是，主备设计可能会有切换不成功、切换前后配置不一致等通病，当然这些通病都是可以通过运维手段规避优化的。

基于主备的设计实现的自愈，当出现问题时，运维团队一般需要马上跟进问题节点进行修复，否则线上就是单点运行（当然也有做1组多备的场景，这会涉及成本）。

#### **基于负载均衡的设计**

基于负载均衡的设计实现的自愈，主要指的是节点转化成无状态，然后在节点前面套一个负载均衡的设计。最典型的例子是云平台上的负载均衡服务后面挂了一堆的应用节点，这个设计一般能很好地处理多节点故障的场景，在部署上要求网络能互联，同时节点最好是无状态的。

基于负载均衡的设计实现的自愈，通常在出现问题时，运维团队可以很淡定地等待维护窗口进行统一修复。这个方案的主要缺点是需要注意容量管理，不能出现宕机多个节点后，容量不足的情况。

#### **基于平台的设计**

故障演练部分提及的故障预案，在某种意义上也是一种故障自愈的设计实现，因为它满足故障发现、自动处理、服务恢复等环节。更为常见的是云平台上的某些功能，如机器宕机自动恢复、AWS的Auto-Scaling（自动扩缩容）功能等。基于平台的设计实现的自愈，是SRE团队的努力目标，这样的自愈设计可以不依赖业务代码实现。

基于平台的设计实现很好地利用了云计算、K8S、容器等平台功能，在资源和业务数据流向上实现了故障自愈。唯一的问题就是基于平台的设计会强耦合业务运行的平台，如云平台、K8S平台等。单个业务换一个平台运行时，SRE团队往往需要调整相关工具代码进行适配。

#### **基于业务架构的设计**

基于业务架构的设计实现的自愈是最难落地的一种自愈方式，典型的例子是各种分布式系统，如Hadoop等大数据处理平台。这些分布式系统自带自愈设计，而且大部分都涉及CAP （Consistency、Availability、Partition Tolerance）原则，这对开发团队提出了很高的要求。

分布式系统在落地时只能满足CAP原则的部分条件，如满足C、P时，A的能力就会下降。一般情况下如果一个业务内建了故障自愈功能，那么就有一定局限性。大部分分布式系统在设计实现时，会权衡自己的重要需求。

从SRE的角度看，基于业务架构的设计实现的自愈是最受欢迎的，因为这样的设计能降低很多的运维工作量。受CAP原则对分布式系统的约束，分布式系统会有自己的自愈局限，但出现故障时，很可能是重大的故障。

最常见的故障就是分布式系统的自动流量调度导致的“雪崩”场景。例如，A服务实现了跨机房流量调度，触发逻辑是单个机房流量处理延迟大于1秒。当A服务某个机房因为网络问题，处理延迟增加，流量全切另两个机房时，如果容量不够，那么很可能会导致另一个机房的处理延迟大于阈值，进而触发全部流量调度到一个机房的行为。

所有的自愈只是在某种程度上的自愈。例如，双节点Keepalived的VIP迁移，背后意味着容错和容量降低一半。故障自愈是所有SRE团队和开发团队对线上稳定性的最终追求，但这不意味着系统有了自愈功能后就不需要SRE团队和开发团队了。故障自愈设计只是缓冲故障对线上的影响，还需要修理故障点。

### 6.6 业务MTTR

故障自愈涉及的用户比较关心问题，就是故障发生到故障消失的时间。例如，线上有一个应用节点故障，需要考虑负载均衡多久能踢掉问题节点。如果不对负载均衡进行合理调整，那么及时使用健康检测模式也会导致用户感知故障发生。很多模块对故障检测是有时间间隔的，这导致即使再怎么修正配置，故障造成的影响也始终无法消除。这让我们不得不关注平均故障恢复时间和平均故障修复时间。

#### 6.6.1 关于故障修复MTTR

对故障时间长度的控制，在传统运维模式下只是单纯地统计每次故障发生的时间长度，没有对团队做出一些具体性的约束。业界为了更好地评估故障修复需要的时间，引入了MTTR（Mean Time to Repair）概念，MTTR指的是产品从故障状态恢复可用到状态修复产生的平均时间。

虽然传统运维团队对故障有修复预案，但很少会关注MTTR。很少关注MTTR的原因，一方面是传统运维团队关注的重点还是总体的故障损失时间；另一方面是传统运维团队的角色分工，当故障修复中涉及多个团队时，沟通成本会导致传统运维团队无法降低故障修复时间。

实际上MTTR对线上业务有很重要的作用。假设同样一个线上故障，一个MTTR平均修复时间为10秒，另一个平均修复时间是30秒，大部分情况下是1分钟采集一次监控系统指标，这导致故障往往是被忽略的。如果是业务低峰期，发生异常中断影响不大，但当业务高峰期来临时，1秒的不可用，对业务就是很大的损失，甚至损失的是成千上万元的收入（如“双十一”0点时刻发生故障）。这个时候就体现出了MTTR的重要性。

MTTR的出现，让业务团队第一次清楚地看到SRE团队对故障修复的改进方向，SRE团队对于MTTR的追求是永无止境的。SRE团队开发的一些故障自愈工具、平台或流程在本质上都是为了尽量缩短故障修复需要的时间。

为了降低MTTR，我们尝试过很多办法。通过不断实践，**最佳的改进方式是利用故障模拟对各个组件的恢复时间做摸底，然后系统性地整理相关改进项**。MTTR梳理表（部分）如表6-3所示。



![img](https://pic2.zhimg.com/80/v2-bc5fa97f8aabc1348b0c1c82fcbd4549_720w.jpg)表6-3 MTTR梳理表（部分）



如表6-3所示的表单通常会被定期盘点，尤其是当业务遇到的问题和故障恢复时间相关时。需要注意的是改进策略不是随便定的，每个参数的修改都需要经过深思熟虑，结合业务特征进行调整。例如，如果健康检测从5秒调整到3秒，就需要注意时间间隔调整是否会导致额外的健康检测资源消耗。如果业务代码实现不好，很多集群间的健康检测探针就能消耗大量的CPU。

#### 6.6.2 关于故障解决MTTR

关于MTTR（Mean Time to Resolve）的另一个说法是平均故障解决时间，指的是整个团队对某个故障的彻底解决时间。当出现线上故障时，故障修复让业务恢复服务能力是比较容易的，但是要让故障造成的影响彻底消失就很困难。

可能有人会对两个MTTR（Mean Time to Repair和Mean Time to Resolve）感到困惑，以一个模拟场景为例，当一个线上支付业务发生严重的数据库故障时，如果要让服务恢复且提供大部分的服务可能需要15分钟，代价是数据库宕机前1秒的交易数据在服务恢复后是暂时丢失的。但是如果完全让数据库数据达到和故障前一致，可能需要3小时。那业务方会怎么选择呢？

如果选择3小时停服，那么金融业务长时间的修复可能会导致央行介入（央行对金融业务的单次中断时间要求不能超过30分钟），另外长时间的中断会造成社会恐慌。如果选择15分钟恢复服务，让部分功能可用，代价就是部分数据不准确。以SRE团队实际经验看，正常的选择是执行平均故障修复时间优先的运维策略，评估方式有以下几种。

##### **业务中断损失评估**

当一个金融业务出现中断时，如果金融业务的平均故障修复时间和平均故障解决时间都很明确，则能比较容易计算两种时间的金钱损失。

例如，A业务平均每分钟盈利1万元，平均故障修复时间为30分钟，当平均故障解决时间为60分钟时，业务方需要考虑平均故障解决带来的数据修复收益能否大于30万元（真实线上每分钟的资金损失往往不止这个数字）。

##### **数据损失评估**

对线上某些业务来说，数据损失只会引起少量用户投诉，但对支付业务来说，数据损失很可能就是损失真实的钱。从业务角度看，假设现实数据库只丢失1秒的数据，虽然只丢失1秒，但1秒往往至关重要，因为谁也不能保证这1秒内有多少资金转移。

金融行业为了解决数据库丢失1秒数据这样的问题，会在业务层加入多段提交确认，使用昂贵的大型计算机系统，或者记录交易日志的逻辑。一旦发生数据库故障，通常会导致提交订单完全失败，或者需要从应用的交易日志里回捞数据进行数据订正。

对线上数据完整性敏感的业务，如支付、电商等业务，因为对线上数据完整性敏感，所以往往会因为业务复杂度而需要较长的平均故障解决时间。只要业务层能够控制资金风险，就会优先启动服务，宁愿用户只读到旧数据也比用户无法访问页面好。

### 6.7 灾备建设

讲完故障恢复后，我们来看看故障恢复的前提：灾备建设。在所有的运维系统中，灾备建设是故障恢复最终的兜底保障，如果一个业务不做灾备建设，那么迟早会出现大问题。因为计算机系统是一个非常复杂而又脆弱的系统，可能一个震动就会损坏硬件设施，所以只要在计算机上存点重要的资料，就得考虑做灾备建设。

什么样的灾备建设才是合格的、符合自己需求的灾备建设呢？从技术上讲灾备有以下几个等级。

### **（1）同机房灾备。**

很多公司都有实施同机房灾备。例如，同一个机房做一个跨机架的数据库主备、Keepalived主从等。这些集群都有一个共同的特点：它们能容忍机房层面单机或单个机柜的故障，只要不是整个机房挂掉，基本就能保障数据和服务不出现大的问题。

### **（2）同城双活。**

当业务规模达到一定程度时，业务方就会考虑业务中断带来的经济损失，这时就会考虑IDC机房的故障风险，有点经验的运维工程师都知道业务只部署在单个IDC机房是有很大风险的。很多时候IDC机房会遇到电力故障、冷却系统故障、挖断网络光纤等各种奇怪的故障。这些故障发生的原因有些是人为的，如市政施工挖断光纤；有些是天灾，如雷电击穿电力系统；有些则是因为运气不好，如夏天的冷却系统故障。

因为知道业务只部署在单个IDC机房会有太多的不可控因素，所以业务团队会在业务稍有规模时就开始考虑同城双活。如果没有特别的情况，业务团队会通过将业务部署到同一个城市的两个机房的方式，来降低单机房不可用的风险。可能有人会想为什么是同城双机房，而不是异地双机房。这主要是考虑容灾的保障等级已经满足业务需求，更近的机房方便业务团队了解业务的实际情况。

### **（3）异地数据灾备。**

当数据重要到一定程度时，会想到如果IDC机房所在城市遇到全市范围内的灾难，那么是不是就没有数据中心的问题。最常见的全市范围内的灾难就是洪水、地震或战争等大范围影响事件。假设遇到洪水，一个城市的两个IDC机房可能都会被淹没，IDC机房里重要数据可能会丢失，数据丢失就意味着无法做灾后的服务恢复。与金融支付相关的业务，肯定会做异地数据灾备。

### **（4）两地三中心。**

如前面说的异地灾备场景，业务团队会想在异地数据灾备后做更进一步的灾备建设。如遇到洪水后，怎么样才能快速恢复业务呢？这个时候就需要依赖两地三中心这样的概念了。

两地指的是两个城市，三中心指的是三个IDC数据中心。正常情况下，两个城市一般会超过一定距离，如50千米，用于确保类似洪水、台风等大范围的自然灾害不会同时影响所有的机房。

如果业务实现了两地三中心，那么就已经具备了非常大的容灾能力，能抵挡单个城市级别的中断问题（如大范围的电网故障）。业务的灾备建设到了这个级别时往往已经完成了单元化、快速内网流量调度、数据一致性等多种技术改造，初步具备了扩展更多机房的能力。

### **（5）分布式多活。**

理想状态的分布式多活是一种类似科幻片里“Skynet”（天网）形式的系统，只要还有节点存活，业务就还能提供服务。这个等级的灾备系统非常重要，理论上若出现任何故障，系统均能通过切换流量和数据的方式，确保业务继续运行。

相比两地三中心的模式，分布式多活在对数据整体的维护上显然更加用心。在传统的两地三中心模式下，业务做几个数据同步任务就可以解决数据一致性问题。但是当节点多到一定程度时，使用数据同步任务是无法维护节点和数据一致性的，业务会更多地依赖分布式自动化调度的逻辑实现多节点的数据同步或调用。

做到分布式多活的业务往往会在很大范围（如在全国范围或全球范围）内进行IDC数据中心布点，在技术上会考虑更多的问题，如超长距离带来的数据时序一致性和跨区域外网流量调度等问题。

在具体落地灾备建设时，需要注意以下几点细节要求。

**（1）同机房灾备细节要求。**

同机房灾备隐含了一个特殊的前置条件：运维工程师了解服务器真实的物理连接和部署拓扑。如果没有这个特殊的前置条件，那么机房内部署Keepalived可能会发生主备都放置在同一个机架和同一个交换机的场景。缺少这方面的机房信息，会对同机房灾备效果严重打折扣。

**（2）同城双活细节要求。**

当一个业务做同城双活灾备建设时，两个机房的位置选择是非常关键的。做同城双活的业务往往影响力很大，不允许业务有太长时间的中断。但在业务架构设计层还需要全局性的设计，如依赖同一个写数据库等（一个机房放主库，另一个机房放备库）。因此做同城双活灾备建设时两个机房位置会小于50千米，这样网络延迟会小于3毫秒。

同城双活部署的业务对网络链路有延迟要求，根本原因是业务底层拆分不够彻底（只做了应用双活），至少在缓存或数据库层还是相互依赖的。一旦对数据库或缓存有依赖，就意味着应用跨双机房流量和有访问延迟放大的问题。

因为两个机房间有较强的互联需求，所以两个机房间的链路需要高可用和高容量。两个机房间的线路至少是双线，而且要求走不同的ISP链路拓扑。

**（3）两地三中心细节要求。**

做两地三中心的灾备建设需要从数据库底层开始设计，通常的设计是多机房数据库多写，然后互相做数据同步。另外要实现两地三中心灾备建设的正常运行需要业务方对自己的业务模块有清晰的认知和设计，尽量减少应用请求跨机房的操作。因为到了这个级别，已经有非常大的跨机房延迟，所以无法支撑业务方反复调用。

两地三中心另一个比较核心的点是多机房的流量调度。一般情况下用户会随机访问3个数据中心，所以应用需要实现用户和数据间的亲和性调度。

例如，A用户访问1号数据中心，生成相关数据库记录；下一次当A用户访问2号数据中心时，2号数据中心的应用需要将流量调度到1号数据中心。如果不这样处理，那么A用户在两个数据中心做的操作就会失去连续性。当然如果1号数据中心和2号数据中心的数据库能做到强同步，那么就不用考虑这个问题。实际上跨网络的数据库写强同步，在实际业务落地时是非常消耗应用处理能力的，根本无法满足业务方的性能要求。

如果有人进一步分析这几种灾备建设，就会发现大部分应用层的多机房灾备改造不是问题，但是若想将一个数据库或持久缓存做成多机房，就非常考验团队的技术实力了。在工程落地时，会发现最难的点就是数据同步，所以最终能体现各个团队技术水平的就是数据同步方案。

灾备方案与网络特性如表6-4所示，同机房灾备和同城双活是灾备建设的初级阶段，这两种灾备建设方案因为网络延迟较低，所以业务常用的数据库和缓存使用默认的同步技术就可以满足需求（如MySQL、Redis可以直接用软件自带的主从复置方案）。两地三中心是灾备建设的中级阶段，一般会定制专门的数据同步工具，能做到很多业务特有的数据同步方案，包括数据路由、多路复制等功能。分布式多活是灾备建设的高级阶段，这个阶段的主要特征是系统自带数据同步和数据一致性逻辑，一般会应用Raft、Paxos等一致性协议技术实现，这类系统的特点是支持全自动的灾备切换。



![img](https://pic3.zhimg.com/80/v2-45d832c817240eed2a68d45cf2fa20e2_720w.jpg)表6-4 灾备方案与网络特性



### 6.8 总结

线上业务的稳定性治理是一个长期的过程，随着技术的改进和业务的发展会出现新的稳定性技术方案。从SRE工程师的角度看，服务稳定性治理是一个长期的工作，新的团队、新的技术都可能对现有的稳定性治理方案形成挑战。当然需要我们看到的是，对比传统的物理服务器，云原生时代的稳定性治理无论是在预案上还是在技术上都有了明显的变化，这些技术特性的变化就是SRE工程师需要跟进和解决的方面。



## 13.4 [构建持续提升的故障管理能力](https://www.sohu.com/a/449003138_722396)

随着系统架构不断升级，功能持续迭代，系统运行复杂性越来越高，故障的发生不可避免，且发生场景愈发无法预测。从企业角度看，系统故障影响客户体验，降低访问流量，带来交易损失，引发监管问责等；从系统架构角度看，系统故障反映的问题代表系统未来扩展性与局限性；从IT资源角度看，故障（尤其是重复性故障）将占用大量IT人力资源，影响IT价值创造能力；从运维角度看，故障是一个常态化的存在，故障既是业务连续性大敌，也是推动组织架构、人员能力、协同机制、工具平台持续优化的驱动力，对待好故障管理有助于建立学习型的运维组织。本文要解释的故障管理，除了指尽快恢复正常的服务以降低故障影响的相关措施，还尝试探索建立一个闭环的故障管理能力的模式。

**3.3.1.1 从几个故障管理领域的词语开始**

**1、故障**

在ITIL中，故障用Incidnet来描述，即事件，ITIL定义为“服务的意外中断或服务质量的降低”。对这个定义的理解，不同组织略有不同，有些组织只针对服务中断的业务可用性故障，有些组织则细化到与正常运行不一致的事件。我认为故障是驱动团队持续优化，跨组织协同效率提升的有力抓手，是培养学习型运维团队的切入点，在资源有条件的情况下细化到异常情况更好。 **故障管理的关键目标是快速恢复服务或业务，降低故障影响。**

除了一般故障，很多企业还会建立突发或重大故障管理，一般是针对数据中心大面积故障，或重要业务、影响客户交易中断等故障，制定更高优先级的应急协同管理，提前制定危机工作小组，确定相关联络人，沟通计划等。相应的，ITIL将上述故障定义为“灾难”：“对组织造成重大损失或重大损失的突发性意外事件”。本文介绍的故障管理包括一般故障与重大故障。

**2、问题**

很多人把故障与问题混淆，尤其是研发、测试侧的同学。在ITIL中，问题是指造成已知故障的原因或系统潜在风险，问题管理是针对问题解决进行的跟踪管理。问题管理包括问题识别、问题控制、错误控制。问题识别通常来源于生产故障、运行分析、从研发、测试，及外部供应商获知风险信息等。问题控制指问题分析，记录解决方案，问题优先级划分等。错误控制是针对问题的根因的解决，考虑到解决问题的成本，并非所有问题都需要解决，问题的解决需要具体评估，比如有些团队定义超过半年不发生的问题可以考虑关闭。

问题管理故障、风险、变更、知识等管理都有联系，与故障管理的关系十分密切，很多团队的问题主要由故障关联生成。通用的方案是，事件的复盘关联出多个已知或未知问题，问题工单可以作为变更需求来源，在变更流程中可以相应的自动关闭问题，高优先级的问题跟踪纳入到风险管理中。

**3、SLA、SLO、SLI**

在故障管理讲这三个S，重点是希望区分不同故障的对待方式，《谷歌SRE解密》中对这几个词有一些描述：

“我们需利用一些主观判断结合过去的经验来定义一些SLI,SLO,SLA，事先定义好合适的指标有助于在故障发生时帮助SRE进行更好的决策。”

“要求所有SLO都是100%并不现实，过于强调这个会影响创新和部署速度。”

“公开的SLO有助于管理用户的期望值”。

注：SLA（Service Level Agreement ）：服务水平协议，是IT服务提供方和被服务方之间就服务提供中关键的服务目标及双方的责任等有关细节问题而约定的协议；SLO（Service Level Objective）：服务质量目标，服务提供方与服务需求方对服务期望，比如系统可用性是4个9，还是3个9；SLI（Services Level Indicator）：服务质量指标，SLO需要通过一系列SLI技术指标指标细化并量化，比如上面的可用性可能会转化为运行时长，故障时间等，性能的话会转换为响应时长、成功率等。加强运维组织的IT服务管理，可以采用SLA为基础，以SLO为服务质量期望，以SLI为量化指标，来设计自身的服务流程、提供服务形式、绩效评估方法。

**4、时效性分析**

在故障处置过程中，有一些时长可以重点关注一下，比如：MTBF（无故障时长）、MTTI（平均故障发现时长）、MTTK（故障定位时长）、MTTF（平均故障处理时长）、MTTR(平均故障响应时长)，MTTF(平均故障恢复时长)，通过这些时效性分析有助于将故障处理能力数字化，并有针对性的在各个阶段选择优化方案，以不断降低上述时长，提升业务连续性。

### **3.3.1.2 故障管理闭环周期**

故障管理闭环周期可以分为事前、事中、事后三个闭环节点，以下我梳理了一张故障管理生命周期，其中由于事中属于分秒必争的特点，又将事中划分为“故障发现、故障响应、故障定位、故障恢复”。可能也有同学会多了“影响分析，应急处置”节点，考虑到在故障定位过程中会不断的尝试诊断分析、影响评估，在故障响应过程中也有影响分析，所以这里不单列这两项。

![img](https://p6.itc.cn/q_70/images03/20210206/8fd2d65570d940aba78ff70445374292.png)

关于故障管理闭环周期的内容后面将分几节单独细化，本章只做简单介绍。

#### **1、事前：防微杜渐与未雨绸缪**

随着系统架构不断升级，功能持续迭代，系统运行复杂性越来越高，故障的发生不可避免，且发生场景愈发无法预测。在事前环节，可以考虑围绕 **“****发现潜在问题并修复”、“提升故障处置阶段效率”**两个目标。前者可以围绕数据进行架构、容量、性能等评估，利用例行机制跟踪已知问题的解决，利用数据优化监控覆盖面与准确性，利用混沌工程发现复杂性未知问题等。后者可以进行应急自动化工具、运行看板、日志工具等工具建设，优化协同机制的顺畅，提升应急能力等。

#### **2、事中：快速恢复**

事中环节重点是 **在最经济的方式，快速恢复服务可用性/业务连续性**。好的事中处理要有一个完备、在线的协同过程，这个协同过程能够赋能给人，更快速的恢复服务。

- **故障发现**：故障发现的重点关注及时性。良好运维组织的故障发现应该大部分来自监控等自动化手段，甚至对一些确定性很强的故障进行自愈行为。采用机器发现故障，有助于在客户无感知的情况下恢复业务，减少对客户体验的影响。站在故障角度看监控，我认为可以分被动与主动两类，被动监控主要针对已知策略的监控，主动监控是利用自动化模拟、数据分析等手段自动化监控，主动监控将是未来运维组织的努力方向。另外，故障发现还可以通过运行分析、巡检、客户反馈等方式发现，随着系统复杂性不断提高，可以判断未来的故障将以我们无法预知的方式出现，更全面的故障发现方式是自动化发现的有力补充，两手都要抓。
- **故障响应**：相比故障发现、定位、恢复，故障响应环节对协同的顺畅要求更高，通常可以围绕信息触达的快速、信息透明，值班管理及启动应急的有序，预案准确的完备性，信息通报的合理，以及对故障影响初步判断的准确。其中对故障影响初步判断是一个难点，考验运维人员的故障识别能力，不仅要求有基本的应急技能，还要对系统有深刻的理解。另外，在故障响应过程中，系统故障受理人，关联上下游系统运维人员，值班经理等各个角色的作用都尤其重要，需要不断的练习、实战来提升协同顺畅性。
- **故障定位**：故障定位包括：诊断定位与影响分析，通常是整个故障过程中耗时最长的环节。不使用根因定位，而使用诊断定位，是因为需要强调定位一定要建立在快速恢复的基础上，而非寻找问题根因，后者由问题管理负责。理想情况下，更好的监控应该能够更具体的发现触发故障的问题，但实际上这个过程需要不断的优化，所以很多时候需要运维团队不断的假设与执行。这个环节应用到的工具通常有监控、日志、运行看板、应急操作工具等，工具在这个环节的应用主要是为了提升故障定位的效率，由于运维人员主要依靠专家意见与临时运行状态分析来假设问题，随着系统复杂性不断提升，数字化手段的作用将越来越大，给运维平台建设团队带来的挑战是如何将数字化手段结合专家经验，融入到协同机制中，这考验场景的设计水平。
- **故障恢复**：故障恢复环节重点是在定位原因后的执行应急操作，再到故障恢复的过程，由于很多故障是在不断尝试验证解决恢复的动作，所以故障恢复环节与故障定位环节有一定的交叠，或在这两个环节之间不断试错的循环。通常故障恢复中的动作包括我们常说的应急三把斧：“重启、回切、切换”。随着运维由被动向主动转变，运维还要关注架构层面的可靠性、容错性、高可用，推动隔离、限流、降级等配套的非功能能力。

#### **3、事后：不要浪费任务一个故障**

事后环节是对事前与事中环节的复盘，关注 **引发故障根源性问题的解决与故障事中处置效率的提升**。缺少事后环节故障会重复发生，协同会更加低效，IT人力资源会被故障拖住，影响整个IT价值创造。事后分析通常可以包括几个通用的步骤：

- **梳理故障处置过程**：这个步骤最好是客观的反映过程，如果整个过程都在线留痕则最佳，通过梳理故障处置过程可以更加客观的观察处置过程存在的问题。要关注在梳理处置过程中如能保持跨团队在线协作更好，能促进过程的透明性。
- **根因分析**：找出引发这个故障的根源，除了系统程序或硬件层面的问题，还包括流程、组织层面的问题，另外还要关注并发引出的风险等。
- **处置过程优化**：通常包括监控是否及时准确，自动化应急工具是否就绪，日志工具是否就绪，运行数据是否可观察，协同是否顺畅，工作流程是否有效执行，人员技能是否达标，系统是否具备可运维性。
- **建立问题跟踪**：系统故障根因要建立问题单进行跟踪，问题单有专项跟踪机制，问题跟踪是一个难点，需要建立数据驱动，绩效支持的协同方式来确保障高优先级的问题得到及时解决。
- **编写故障报告并发布**：（上述的步骤也可以由报告驱动）针对故障级别，报告有大报告与小报告，报告编写过程中最好能建立信息分享机制，以收集跨团队意见并修订报告，报告完成后最好能公开发布，发布不仅是问题的警戒，还包括处理过程优点的公示。
- **例会**：例会不可或缺，视团队而定建立日、周、月、季例会，复盘重大故障，重复故障，问题解决等。
- **定责**：关于定责，很多人说复盘要对事不对人/团队，我个人观点是要视企业文化而定，很多时候这与组织架构相关，透明公开的定责能激发对生产事件的敬畏之心，同时也可以减少关于“背锅”的负面情绪。当然，考虑到定责带来的负面影响，可以减小定责的公布范围。

**3.3.1.3 故障管理能力增长飞轮**

前面提到了故障管理的前、中、后的闭环，并提出持续优化每个环节的一些优化措施，下一步我们看一下如何推动运维组织 **自驱动**的故障管理能力提升，去有效落实上面的具体举措。我尝试利用飞轮效应思想画一个故障管理的自驱动模型。

![img](https://p2.itc.cn/q_70/images03/20210206/0c1cc4014fa4444fb17ba60e77f5ec83.png)

我先介绍一下飞轮效应，团队能力提升过程中是一个持续推动的过程，你先利用最大力气推动一个沉重的飞轮，飞轮开始慢慢转动，随着一圈一圈的转动，飞轮获得了动能，速度越来越快，飞轮开始不断循环的往复。在多个飞轮的组合中，根据组织现状你可以选择从大飞轮或小飞轮作为切入点，就像你的自行车上坡时你会选择前面牙盘小一些，在平路竞速时你会选择前面牙盘大一些。在飞轮的组合中，每个飞轮更快的转动都能够带动下一个飞轮更快的转动，同样某个飞轮减速了也会影响下一个飞轮的速度。运维故障管理中也一样可以找到这样一个增长飞轮，比如上图的“提高应急处置效率，提升业务连续性”，运维组织可以有更多时间提升组织能力，来“提升故障管理与应急协同机制的可扩展性”，有了机制则可以推动“运维能力的持续提升”等等。我觉得每个运维组织都可以尝试画一个适合自身组织故障能力提升的飞轮，飞轮的方法可以借鉴 吉姆.柯林斯的《飞轮效应》。

**3.3.1.4 从适应性系统看故障管理**

在第2章中我画了一个运维适应性系统的飞轮闭环：“提出需求，（实现需求）带来改变，（改变）引发风险，（解决风险）需要进行改变，改变好了可以（接受更多需求）”。站在整个运维适应性系统的能力提升中，我们可以看到，运维面临业务迭代需求更多且要求更快，商业模式与新技术创新，海量数据的应用，连接因素更加复杂等因素，驱动IT能力的持续提升，带来新技术与新架构模式的引入，运维在新技术选择时机、技术成熟度、架构及数据高可用的评估能力、对存量技术架构的影响，以及新技术附带的选择成本，种种挑战对运维带来的直接挑战就是故障更多，故障处理时效性要求更高。

为了减少故障，提升故障处理过程中的时效性需求，运维组织在故障管理过程中可以考虑从运维体系的组织、流程、平台三个角度来融入体系适应性系统的建设。

**1）组织：**

适应性系统面临环境的复杂性，导致故障处理过程经常面临跨团队协作，尤其是重大故障或危机时，大量不同团队的人涌入ECC值班室，线上IM出现各种信息，各种指令涌向应急执行人员，容易带来混乱，继而影响处理处理效率，良好的组织配置及能力要求有助于建立有序的应急处置。

- 建立故障管理专项或横向岗位： 暂且叫这个岗位为故障经理，有些团队的故障经理偏流程，我认为故障经理需要从一线运维出身，这样的人才能更好的指挥危机处理。这个岗位技术、经验方面有突出的要求，考虑到信息太多所以这个人不一定是技术能力最强的，但他需要具备在应急过程中能够起到指挥协调作用的内在思考与外在沟通的个人魅力，注意是在 应急混乱过程 中的能力。在《架构即未来》对这个角色，有一段描述：“他的三分之一是工程师，三分之一是经理，三分之一是业务经理。虽然很难找到这样一个综合经验、魅力、业务能力的人去履行这个职责，当你找到这个人时，他可能不想要这份工作，因为这是一个无底的压力池。然而，正如一些享受做急诊室医生的快感，一些运维人员享受带领团队渡过危机”。
- 加强技术团队管理岗位的领导作用： 一线技术团队的经理要发挥技术、业务、协调资源、应急经验等，起到应急决策的关键作用，传达行动指令，必要时为应急操作执行人员理清思路，减少外部不必要的信息或指令干扰，协调团队其他技术骨干及跨团队协同的支持，向故障经理、部门领导描述进展、发现、决策等。
- 提升运维工程师/系统管理员能力： 运维专家的能力水平与以下几点相关， “与特定系统不相关的特定技能”、“与系统相关的掌控程度”、“协同机制与工具的应用程度” ，第一、三点是通用能力，第二点是特定的能力。事实上，我觉得在金融行业第二种能力是运维团队要重点扩展的能力，运维的价值一定要围绕在业务上，对业务系统的理解的要求可以视团队的资源情况选择关键点进行切入，详细的观点可以见 《以业务为中心重塑运维岗位能力》 。
- 促进其它条线团队应急人员协同紧迫性： 前面提了多次关于跨条线协同，要在跨条线协同顺畅，关键是让所有运维人员对故障有敬畏心，面对故障能够放下手上所有工作快速进入协查，主动排查自身的可用性，自己是否是根源，或受影响等，并在线反馈信息，接受指令。
- **强化二、三线技术骨干支持协同能力：**除了一线应急处置人员，还需要打造二线运维骨干，三线研发、测试、外部厂商的支持协同能力。通常在线、透明的信息传递对协同有帮助。

**2）流程：**

- **ECC管理流程：**国内金融企业通常都会有一个ECC总控中心（或叫监控管理中心、监控指标中心），是运维团队进行日常运行监控管理、工单处理、应急处置、调度联络、服务台等日常工作的场所。ECC的管理流程主要包括：值班管理流程、ECC工作守则两点，值班管理流程通常又会有一线、二线值班要求，值班经理要求，故障经理要求等职责，ECC工作守则主要是规范在ECC中的行为要求。ECC管理流程的完善是保证应急资源就绪的基础，为运维人员履行应急职责、为监控系统、应急指挥作战等提供可靠的发现环境等提供基础。
- **应急处置协同流程：**好的应急处置协同流程应该是围绕事中处置过程的人、系统、机器、事、工具建立一个在线的连接网络。这个网络需要视不同组织的特点，比如《google sre解密》中提到的一些故障跟踪系统可能就不一定完全适合金融企业，因为google的办公是全球办公的协同，涉及时差等特点。好的应急处置协同流程应该是围绕企业应急管理场景，将专家知识、工具、数据、沟通整合在一起。
- **事后复盘机制：**复盘是为了从故障中学习，找到组织、流程、运维工具、系统架构的不足，持续改进。复盘机制涉及故障报告、日/周/月/季例会、专题分析会议、信息公布等。
- **其它流程：**比如问题跟踪管理流程、整改分析等。

**3）平台：**

围绕“监管控析”的运维平台，在故障管理中涉及的平台建设主要有：

- **监控：**全面覆盖，主动拨测，及时准确报警，集中式的监控报警，多渠道触达相关人员或自动化操作等是监控平台能力的一些要求。
- **日志：**统一的日志平台，方便信息检索、配置监控、历史比对、日志分析是日志平台的要求。
- **自动化操作：**主要从效率与安全角度出发，包括：应急操作的自动化，批量操作，操作风险控制，操作留痕等作用。
- **数据观察：**运维工作的大部分对象是软件、硬件、系统，本身面对的就是一个数字世界，观察数字世界的运行状态，最好的办法就是为数字世界的组成部分建立数字化的在线观察看板，叫数字孪生也不为过。
- **过程管理：**事前、事中、事后涉及过程的管理工具，这块通常需要以实际情况为基础，进行个性化设计，很少有成熟的产品供使用。
- **ITSM：**事件管理、问题管理、服务台管理，以及相互之间的联动。

**3.3.1.5 从数字化角度看故障管理**

**1、协同网络：在线连接机器、系统、人**

故障管理过程是一个多角色，跨团队协同的过程，过程的参与者既包括运维内部组织与员工，也包括运维以外的研发、测试、业务、客服、厂商、监管机构等，以及一切以数字或软件形式存在的机器、系统，将参与者在线化，产生互动连接，将形成一张数字化的协同网络。协同网络将促进员工与组织、员工与客户、人与机器等节点间的互动在线化、透明化，能够有效的加强事前的主动发现问题与解决问题的能力，事中快速响应与快速恢复的能力，事后复盘客观与有效。协同网络将呈现“点线面”的形态，其中，“点”是前提到的各类参与者，“点”与“点”通过协同机制连接成“线”，“线”与“线”互动协作成“面”，“点线面”是一个协同演化，升维变革的过程。

**2、数据智能：数据驱动事前、事中、事后效果**

数据智能实现故障协同网络的在线化，加强参与节点的有效连接。实现故障过程的数据智能包括：一是推动在线协作的线上化，系统运营数据的在线资产化，监管控数据管理工具的就位；二是对线上化、资产化带来的数据进行变现，为事前评估分析，事中应急发现与执行决策，事后复盘分析提供数据支撑；三是基于数据推进运维智能化，实现对未知故障的发现、定位、处置、恢复的决策支持，并结合自动化实现人机协同的模式，将可量化、可衡量、可程序化的工作由机器辅助人处理。

**3、员工赋能：工具与机制赋能**

员工是故障协同网络中核心节点，提升故障应急能力，尤其是临场故障决策，关键的是发挥员工能力。运维组织要从监管控析工具与运维机制两点为员工提供一个全数字化的工作环境，激活跨团队应急协同的参与，重塑运维由被动向主动运营转型。建立全数字化的工作环境，需要从从组织架构上进行优化，优化资源配置，强化信息传导机制，促进协同效率；二是利用在线数据构建更加安全、透明的工作环境，形成员工数字镜像，挖掘优秀员工，辅助员工成长，为应急管理的持续优化赋能；三是为员工提供全在线的“监管控析”工作装备。

**3.3.1.6 小结**

- 不要浪费任务一个故障， 从运维角度看，故障是一个常态化的存在，故障既是业务连续性大敌，也是推动组织架构、人员能力、协同机制、工具平台持续优化的驱动力，对待好故障管理有助于建立学习型的运维组织。
- 故障管理的关键目标是快速恢复服务或业务，降低故障影响。
- 问题是指造成已知故障的原因或系统潜在风险，问题管理是针对问题解决进行的跟踪管理。
- 在事前环节，可以考虑围绕“发现潜在问题并修复”、“提升故障处置阶段效率”两个目标 。
- 事中环节重点是在最经济的方式，快速恢复服务可用性/业务连续性。
- 事后环节是对事前与事中环节的复盘，关注 引发故障根源性问题的解决与故障事中处置效率的提升 。
- 以故障管理飞轮推动运维组织自驱动的故障管理能力提升，有效落实故障管理的具体举措 。
- 将连接、数据、赋能的数字化思维，融入到故障管理。

## 13.5 如何防止渗透攻击

[谈谈TP5防渗透攻击的一些经验_Tim_PHPer的博客-CSDN博客_防渗透攻击](https://blog.csdn.net/tim_phper/article/details/89477199)

服务器系统升级

软件开源组件升级

关闭不使用端口

关闭调试模式、后门接口

特殊字符过滤

登录开启验证码、token校验：限制IP时间段内的错误次数、验证码登录

上传文件类型限制白名单和大小

## 13.6 红蓝攻防演练

应用到线上的生产演练，应急响应，系统健壮，系统可靠性，机制完善性
如果要你做：生产环境、线上运维、应急响应、监控发现、可靠性、
风险来源：
从生产来源：

### 1、新功能上线发布，验证不完善，导致的风险

### 2、突发的大流量导致的系统容量不足

### 3、灾难性、机房不可用

### 4、网络安全攻击导致的

定义问题：
1、模拟功能上线的代码变更问题或配置变更问题
2、构造大流量的访问，如通过一些自动化手段
3、模拟灾难，如机房断电
4、进行SQL注入、xml注入、命令注入等渗透攻击

解决方案：
1、产品上线的一些前置或后置的校验
2、扩容、限流、熔断、降级等手段来解决大流量问题
3、高可用和通过一些容灾方案来解决
4、网络防火墙、漏洞扫描、VPN小网

线下主要从代码层面来说：

### 看单元测试、集成测试、压力测试是不是可以对这些变更有些防范和测出来

# 14. 大数据

[大数据技术原理与应用](https://www.bilibili.com/video/BV1ds41157NS?p=22&vd_source=c9f7d901dfa9fbc04fdbc2cf37bd0212)

## 大数据概念和影响

全样而非抽样

效率而非精确：追求效率，数据是全量的

相关而非因果

## 大数的应用

影视剧拍摄：纸牌屋拍摄

谷歌预测流感：搜索引擎实时收集各类用户查询信息

## 大数据技术的层次

分布式存储

* 分布式数据库BigTable、分布式文件系统GFS、分布式并行处理技术MapReduce

分布式处理

* 批处理计算：MapReduce是批处理计算模式的典型代表，新的Spark

![image-20221119212536379](pictures/大数据计算模式.png)

## 物联网

![image-20221119235615202](pictures/大数据说的是什么点.png)

![image-20221119235922342](pictures/云计算_大数据_物联网关系.png)



![image-20221120113839024](pictures/Hadoop应用现状.png)

## Hadoop项目结构

![image-20221120115323682](pictures/Hadoop项目结构.png)

## HDFS简介

![image-20221120115903395](pictures/HDFS局限性.png)

## HBase简介

![image-20221120150641848](pictures/HBase简介.png)



![image-20221120203600841](pictures/HBase数据模型.png)

## NoSQL概述

![image-20221120205135741](pictures/传统关系数据库的性能缺陷.png)



![image-20221120205939366](pictures/NoSQL集群的缺陷.png)



![image-20221120210252107](pictures/关系型数据库没有发挥作用的特性.png)



![image-20221120211021705](pictures/关系数据库的优势.png)



![image-20221120211112542](pictures/关系数据库的劣势.png)



![image-20221120211209615](pictures/NoSQL优劣势.png)

## 键值数据库和列族数据库

![image-20221120213214301](pictures/键值数据库的特点.png)

![image-20221120211448803](pictures/键值列族数据库.png)

![image-20221120212527167](pictures/列族数据库的特点.png)



![image-20221120212956447](pictures/文档数据库的特点.png)



![image-20221120213118647](pictures/图数据库的特点.png)



![image-20221120213427913](pictures/四种数据库的比较.png)

## 云数据库

![image-20221120213719703](pictures/云数据库-数据模型.png)



![image-20221120220539495](pictures/传统和并行编程模型的区别.png)

## MapReduce模型简介

![image-20221120221005691](pictures/MapReduce和MasterSlave框架.png)

![image-20221120223146515](pictures/MapReduce执行的各个阶段.png)



![image-20221120223228560](pictures/MapReduce的分片.png)



![image-20221120223309305](pictures/Reduce任务的数量.png)

## Shuffle过程详解

![image-20221120223642028](pictures/Shuffle过程简介.png)



![image-20221120224327176](pictures/Map端的Shuffle过程.png)

![image-20221120224634369](pictures/Reduce端的Shuffle过程.png)

## MapReduce应用程序执行过程

![image-20221122004131580](pictures/MapReduce应用程序执行过程.png)

## MapReduce编程实践

![image-20221122005649074](pictures/在Hadoop中执行MapReduce的几种方式.png)

## Spark中基本概念

![image-20221121234909034](pictures/Spark中基本概念.png)

![image-20221121234552882](pictures/Spark中各种概念的相互关系.png)

## Spark运行基本流程

![image-20221121234403901](pictures/Spark运行基本流程.png)

## RDD概念

![image-20221121235131793](pictures/RDD设计背景.png)



![image-20221121235338943](pictures/RDD分布式对象集合.png)



![image-20221121235656469](pictures/RDD概念.png)

![image-20221121235836319](pictures/RDD执行过程.png)

![image-20221121235955869](pictures/RDD执行过程的一个实例.png)

![image-20221122000041811](pictures/RDD优点.png)

## RDD特性

![image-20221122000534400](pictures/RDD特性.png)

## RDD的依赖关系和运行过程

![image-20221122001002911](pictures/RDD窄依赖.png)

![image-20221122001058586](pictures/RDD宽依赖.png)

![image-20221122001857551](pictures/Stage的划分.png)

![image-20221122001937487](pictures/Stage的划分样例.png)

![image-20221122002209020](pictures/ShuffleMapStage.png)

![image-20221122002135799](pictures/ResultStage.png)

![image-20221122002336051](pictures/RDD运行过程.png)

## 编写Storm程序

![image-20221123002217172](pictures/编写Storm程序.png)

## 大数据应用概览

![image-20221123004141261](pictures/大数据应用概览.png)

互联网：推荐系统

生物医学：流行病预测、智慧医疗、生物信息学

物流：智能物流、中国智能物流骨干网——菜鸟

城市管理：智能交通、环保监测、城市规划、安防领域

金融：高频交易、市场情绪分析、信贷风险分析

汽车行业：无人驾驶汽车

零售行业：发现关联购买行为、客户群体细分

餐饮行业：餐饮O2O

电信行业：客户离网分析

能源行业：智能电网

体育娱乐：投拍影视作品、训练球队、预测比赛结果

安全领域：防御网络攻击、预防罪犯

政府领域：选举

## 推荐系统概述

![image-20221123005114150](pictures/推荐系统概述.png)

# 15 [八股文](https://www.bilibili.com/video/BV1TV411K7bb/?spm_id_from=333.337.search-card.all.click&vd_source=c9f7d901dfa9fbc04fdbc2cf37bd0212)

## 1. malloc、free和new、delete的区别

背景：malloc free是C语言中的库函数，new、delete是C++中的操作符

a. new 自动计算所需分配内存大小，malloc需要手动计算

b. new 返回的是对象类型的指针，malloc返回是的void*，再进行类型转换

c. new分配失败会抛出异常，malloc分配失败返回的是null

d. new是free store（自由存储区）上分配内存，malloc堆上分配

e. delete需要对象类型的指针，free是void*类型的指针

malloc是怎么分配空间的？两个系统调用，一个是break（堆区），先从内存池上分配，内存池无法分配再再走break分配。超过128K就走一个mmap分配（文件映射区）。

malloc分配的是物理内存还是虚拟内存？虚拟内存

malloc调用后是否立刻得到物理内存？不会，在初始化对象时才得到物理内存

free怎么知识释放多大的空间？多分配了16字节空间，用于存储大小等信息

free释放内存后，内存还在吗？通过break分配，释放的内存会回去到内存池，通过mmap分配，就会释放。

## 2. 虚函数表、虚函数表指针的创建时机

作用：用来实现多态

虚函数表创建时机：

a. 什么时候生成？编译器编译的时候生成，virtual关键字修饰的函数。

b. 存放在哪里？可执行程序（磁盘），运行状态（内存）

内存模型：

内核空间

栈区

文件映射区

堆区

数据区（静态存储区）：.bss(未初始化的或者初始化为0的全局、静态变量)；.data（初始化的全局、静态变量）

代码区：.text(代码)；.rodata(只读数据段，虚函数表：虚函数地址的数组)

![image-20230828232423181](pictures/内存模型.png)



虚函数表和虚函数表指针的关系：

每个类最多只有一个虚函数表；

类的不同对象，通常虚函数表指针是不一样的，拷贝构造或重载赋值运算符。

虚函数表指针，就是指向虚函数表的指针。



虚函数表指针的创建时机：

1.类对象构造的时候，把类的虚函数表地址赋值缥vptr；

2.没有函数函数，编译器会生成默认的构造函数

3.继承情况下，虚函数表指针赋值过程：

a. 调用基类构造函数，把A的虚函数表的地址赋值给vptr

b. 调用子类构造函数，把B的虚函数表的地址赋值给vptr

![image-20230828231822979](pictures/虚函数表.png)



## 3.什么时候生成默认拷贝构造函数

如果不提供，会触发位拷贝。导致堆上资源、文件句柄、socket重复释放、虚函数表指针丢失等

不得不生成：

a.类成员变量也是一个类，该成员类有默认拷贝构造函数

b.类继承自一个基类，该基类有默认拷贝构造函数

c.类成员中有虚函数

d.类继承自基类，基类中有虚函数

## 4. 进程和线程的区别

进程是资源分配的基本单位，线程是cpu调度的基本单位。

a.并发性

切换效率：进程切换效率低、线程切换效率高

上下文切换

现场运行环境：CPU寄存器、程序计数器、用户空间信息（虚拟内存空间）、内核空间

线程切换：不同进程的线程切换、同一进程的线程切换

b.内存

进程有独立的虚拟地址空间

线程没有独立的地址空间，但有栈、程序计数器、本地存储等独立空间

c.所属关系

一个线程属于一个进程

一个进程可以拥有多个线程

d.健壮性

进程健壮性高

## 5.系统调用的流程

应用程序-》函数库-》系统调用-》内核

系统调用属于软中断，从用户态切换到内核态。

中断号(0x80)、中断处理程序、中断向量表

系统调用的流程：
触发中断：系统调用号写入寄存器eax; int 0x80

切换堆栈：用户态切换到内核态

执行中断程序：0x80 进行系统调用；eax->系统调用号->系统调用表->处理函数；syscall_read

从中断处理程序返回：iret 将返回值返回并从内核态切换到用户态

再问：系统调用是否引起进程或线程切换？

不一定。如果使用阻塞的io且io未就绪，将线程或进程切换；运行态-》阻塞态。

用户态切换内核态，切换堆栈，切换中断上下文。

保存运行现场 cpu存储的是用户态的指令切换成 cpu存储的是内核态的指令。

## 6.页面置换算法

缺页中断->内存已满->磁盘。

目标：尽量减少页面的换入换出的次数。

![image-20230831225553206](pictures/页面置换流程.png)

1.最佳页面转换算法：置换在未来最长时间不访问的页面

2.先进先出置换算法：选择在内存驻留时间最长的页面

3.最近最久未使用置换算法：选择最长时间没有被访问的页面

4.时钟页面转换算法：LRU环形链表

5.最不常用：选择访问次数最少的页面

## 7.TCP和UDP的区别

1.相同点：都是传输层协议，目的是为上层提供服务

2.是否面向连接：

tcp面向连接：三次握手建立连接，四次挥手断开连接；端对端的连接；全双工通信，允许双端同时收发数据

udp面向不连接：无需三次握手、四次挥手；支持一对一、一对多、多对一、多对多

3.数据传输方式：

tcp基于字节流：完整的用户消息可能会被拆分成多个tcp报文进行传输（MTU、MSS）；对于接收方而言，需要处理粘包问题

udp基于报文：udp每次收发都是完整的报文

4.是否可靠

tcp可靠传输：

​	分段，进行发送数据包的大小控制；

​	序列号，tcp报文包含序列号，确保完整接收，丢失重复数据，排序（为什么需要序列号：为后面确认应答机制，为什么在三次握手的时候需要随机序列号：防止历史连接造成干扰）；

​	确认应答机制；

​	校验和，检测报文传输过程中的数据变化；

​	滑动窗口：流量控制，防止包丢失

​	拥塞控制

​	超时重传

udp不可靠传输：

​	不保证消息交付

​	不保证交付顺序

​	不进行拥塞控制

​	不进行流量控制

5.传输效率

tcp效率低：

​	实现可靠传输造成性能损失

​	tcp头20个字节

udp效率高：

​	udp不可靠传输

​	udp头8个字节

6.应用场景

tcp要求数据可靠，对速度要求不高

udp实时性要求高



## 10. malloc如何分配内存，free怎么知道释放多少

1.进程虚拟内存空间分布

2.从低地址往高地址

* 代码段：二进制可执行代码
* 已初始化的数据段：静态常量
* 未初始化的数据段：未初始化的静态变量
* 堆段
  * 从低地址往高地址
  * 动态分配的内存
* 文件映射段：动态库、共享内存等
* 栈段
  * 高地址往低地址
  * 局部变量和函数调用上下文
* 内核空间
  * 32位1G（上面所有空间3G）、64位128T（上面所有空间128T）

3.分配的虚拟内存

4.使用时才会分配真实的物理内存，怎么分配：缺页异常

5.malloc是不是系统调用名？不是，是库函数

6.如何分配：

* 分配的内存大小<128K，通过brk系统调用从堆段分配；
  * 由低到高
  * 优先从内存池分配
  * 减少系统调用次数，减少缺页异常次数
* 分配的内存大小>=128K，通过mmap系统调用从文件映射段分配内存

7.free

* 释放内存后，内存还在吗？

  * brk，归还到内存池

  * mmap，立刻归还操作系统

* 知道释放多少：多申请了16个字节，记录了内存块的描述信息，包括内存块的大小



[进程的虚拟地址空间](https://blog.51cto.com/u_14195159/5687726)

![进程的虚拟地址空间_用户空间](https://s2.51cto.com/images/blog/202209/19094106_6327c8b21af1a65454.png?x-oss-process=image/watermark,size_16,text_QDUxQ1RP5Y2a5a6i,color_FFFFFF,t_30,g_se,x_10,y_10,shadow_20,type_ZmFuZ3poZW5naGVpdGk=/format,webp/resize,m_fixed,w_1184)



[malloc的底层实现原理](https://blog.csdn.net/weixin_43340455/article/details/124570759)

### 基于Linux操作系统malloc申请内存的实现原理

​    malloc在申请内存是在堆中分配内存的，我们在之前的文章说到了很多次堆这个名字，那么究竟堆在内存中长什么样的，下面就是小编画的一个简化图，在32位系统中，寻址空间只有4GB，在Linux系统下0~3GB是用户模式，3~4GB是内核模式。而在用户模式下又分为了代码段、数据段、BSS段、DATA段、堆、栈。各个段所代表的含义在图中已经说明。其中的BSS段存放未初始化的全局变量和局部静态变量。数据段存放已经初始化的全局变量和局部静态变量。至于局部变量存放在栈中。而堆（heap）段位于bss下方，而其中有两个重要的标志：（程序溢出brk）programe break。Linux维护一个break指针，这个指针指向对空间的某个地址。从堆起始地址到break之间的[地址空间](https://so.csdn.net/so/search?q=地址空间&spm=1001.2101.3001.7020)为映射好的，可以供进程访问；**而从break往上，是未映射的地址空间，如果访问这段空间则程序会报错。我们用malloc进行内存分配就是从break往上进行的。**

![img](https://img-blog.csdnimg.cn/3b30a4e79a414255af699c748cbe8bc0.png)

​    进程所面对的虚拟内存地址空间，只有按页映射到物理内存地址，才能真正使用。受物理存储容量限制，整个堆虚拟内存空间不可能全部映射到实际的物理内存，Linux对堆的管理示意图如下：

![img](https://img-blog.csdnimg.cn/cfb78d8f8aaa428292c70d1692110997.png)

 获取了break地址，也就是内存申请的起始地址，下面就是malloc的整体实现方案：

​    **调用malloc 函数**的实质是它有一个将可用的内存块连接为一个长长的列表的所谓空闲链表。 调用 malloc（）函数时，它沿着连接表寻找一个大到足以满足用户请求所需要的内存块。 然后，将该内存块一分为二（一块的大小与用户申请的大小相等，另一块的大小就是剩下来的字节）。 接下来，将分配给用户的那块内存存储区域传给用户，并将剩下的那块（如果有的话）返回到连接表上。

​    **调用 free 函数时**，它将用户释放的内存块连接到空闲链表上。 到最后，空闲链会被切成很多的小内存片段，如果这时用户申请一个大的内存片段， 那么空闲链表上可能没有可以满足用户要求的片段了。于是，malloc（）函数请求延时，并开始在空闲链表上检查各内存片段，对它们进行内存整理，将相邻的小空闲块合并成较大的内存块。

### malloc()和free()的分配算法

​    在程序运行过程中，堆内存从低地址向高地址连续分配，随着内存的释放，会出现不连续的空闲区域，如下图所示（已分配内存和空闲内存相间出现）：

![img](https://img-blog.csdnimg.cn/6dac92df3b374c97863aef0547202cf8.png)

​    带阴影的方框是已被分配的内存，白色方框是空闲内存或已被释放的内存。程序需要内存时，malloc() 首先遍历空闲区域，看是否有大小合适的内存块，如果有，就分配，如果没有，就向操作系统申请（发生系统调用）。为了保证分配给程序的内存的连续性，malloc() 只会在一个空闲区域中分配，而不能将多个空闲区域联合起来。

​    内存块（包括已分配和空闲的）的结构类似于链表，它们之间通过指针连接在一起。在实际应用中，一个内存块的结构如下图所示：

![img](https://img-blog.csdnimg.cn/be040082738d41c38ad8d478c56e85d6.png)

​    next 是指针，指向下一个内存块，used 用来表示当前内存块是否已被使用。这样，整个堆区就会形成如下图所示的链表（类似链表的内存管理方式）：

![img](https://img-blog.csdnimg.cn/543536e22d5544aca4ae28440b5d862c.png)

​    现在假设需要为程序分配100个字节的内存，当搜索到图中第一个空闲区域（大小为200个字节）时，发现满足条件，那么就在这里分配。这时候 malloc() 会把第一个空闲区域拆分成两部分，一部分交给程序使用，剩下的部分任然空闲，如下图所示（为程序分配100个字节的内存）：

![img](https://img-blog.csdnimg.cn/e31a44ce1639445d9f9635a63cc449ad.png)

​    当程序释放掉第二和三个内存块时，就会形成新的空闲区域，free() 会将第二、三、四个连续的空闲区域合并为一个，如下图所示：

 

![img](https://img-blog.csdnimg.cn/0fd4f8080424459d83bfcd8ac1eedeb5.png)

 

​    可以看到，malloc() 和 free() 所做的工作主要是对已有内存块的分拆和合并，并没有频繁地向操作系统申请内存，这大大提高了内存分配的效率。

​    另外，由于单向链表只能向一个方向搜索，在合并或拆分内存块时不方便，所以大部分 malloc() 实现都会在内存块中增加一个 pre 指针指向上一个内存块，构成双向链表，如下图所示：

![img](https://img-blog.csdnimg.cn/e523e138aea84adf8de400c6c70dd55d.png)

 

​    链表是一种经典的堆内存管理方式，经常被用在教学中，很多C语言教程都会提到“栈内存的分配类似于数据结构中的栈，而堆内存的分配却类似于数据结构中的链表”就是源于此。

​    链表式内存管理虽然思路简单，容易理解，但存在很多问题，例如：

- 一旦链表中的 pre 或 next 指针被破坏，整个堆就无法工作，而这些数据恰恰很容易被越界读写所接触到。
- 小的空闲区域往往不容易再次分配，形成很多内存碎片。
- 经常分配和释放内存会造成链表过长，增加遍历的时间。

​    针对链表的缺点，后来人们提出了位图和对象池的管理方式，而现在的 malloc() 往往采用多种方式复合而成，不同大小的内存块往往采用不同的措施，以保证内存分配的安全和效率。



## 11. [栈空间的大小](https://blog.csdn.net/beilizhang/article/details/118694547)

**概念**
在Windows下，栈是向低地址扩展的数据结构，是一块连续的内存区域。栈顶的地址和栈的最大容量是系统预先规定好的，在Windows下，栈的大小是**2MB**。而申请堆空间的大小一般小于2GB。

**栈与堆**
栈的速度快，但是空间小，栈的大小受限于计算机系统中有效的虚拟内存，不灵活。堆获得的空间比较灵活，也比较大，但是速度相对慢。VC中，堆是人为控制的，所以容易产生内存泄漏问题。

栈是向低地址扩展的数据结构，是一块连续的内存区域。而堆空间却不是连续的，原因在于系统用链表来存储空间的内存地址。同时，链表的遍历方向是由低地址向高地址。

**更改栈大小**
link（链接）时用/STACK指定它的大小，或者在.def中使用STACKSIZE指定它的大小

使用控制台命令“EDITBIN”更改exe的栈空间大小

Linux默认栈空间的大小为**8MB**，通过命令ulimit -s来设置



## 12. [堆与栈区别](https://zhuanlan.zhihu.com/p/268370042)

堆与栈实际上是操作系统对进程占用的内存空间的两种管理方式，主要有如下几种区别：
（1）管理方式不同。栈由操作系统自动分配释放，无需我们手动控制；堆的申请和释放工作由程序员控制，容易产生内存泄漏；

（2）空间大小不同。每个进程拥有的栈的大小要远远小于堆的大小。理论上，程序员可申请的堆大小为虚拟内存的大小，进程栈的大小 64bits 的 Windows 默认 1MB，64bits 的 Linux 默认 10MB；

（3）生长方向不同。堆的生长方向向上，内存地址由低到高；栈的生长方向向下，内存地址由高到低。

（4）分配方式不同。堆都是动态分配的，没有静态分配的堆。栈有2种分配方式：静态分配和动态分配。静态分配是由操作系统完成的，比如局部变量的分配。动态分配由alloca函数进行分配，但是栈的动态分配和堆是不同的，他的动态分配是由操作系统进行释放，无需我们手工实现。（如vector是堆内存：[vector内存在堆中](https://blog.csdn.net/qq_43406934/article/details/127553042)，[图解 C++ 中 std::string 的内存布局](https://blog.csdn.net/tjcwt2011/article/details/127729271)）

（5）分配效率不同。栈由操作系统自动分配，会在硬件层级对栈提供支持：分配专门的寄存器存放栈的地址，压栈出栈都有专门的指令执行，这就决定了栈的效率比较高。堆则是由C/C++提供的库函数或运算符来完成申请与管理，实现机制较为复杂，频繁的内存申请容易产生内存碎片。显然，堆的效率比栈要低得多。

（6）存放内容不同。栈存放的内容，函数返回地址、相关参数、局部变量和寄存器内容等。当主函数调用另外一个函数的时候，要对当前函数执行断点进行保存，需要使用栈来实现，首先入栈的是主函数下一条语句的地址，即扩展指针寄存器的内容（EIP），然后是当前栈帧的底部地址，即扩展基址指针寄存器内容（EBP），再然后是被调函数的实参等，一般情况下是按照从右向左的顺序入栈，之后是被调函数的局部变量，注意静态变量是存放在数据段或者BSS段，是不入栈的。出栈的顺序正好相反，最终栈顶指向主函数下一条语句的地址，主程序又从该地址开始执行。堆，一般情况堆顶使用一个字节的空间来存放堆的大小，而堆中具体存放内容是由程序员来填充的。

从以上可以看到，堆和栈相比，由于大量malloc()/free()或new/delete的使用，容易造成大量的内存碎片，并且可能引发用户态和核心态的切换，效率较低。栈相比于堆，在程序中应用较为广泛，最常见的是函数的调用过程由栈来实现，函数返回地址、EBP、实参和局部变量都采用栈的方式存放。虽然栈有众多的好处，但是由于和堆相比不是那么灵活，有时候分配大量的内存空间，主要还是用堆。

无论是堆还是栈，在内存使用时都要防止非法越界，越界导致的非法内存访问可能会摧毁程序的堆、栈数据，轻则导致程序运行处于不确定状态，获取不到预期结果，重则导致程序异常崩溃，这些都是我们编程时与内存打交道时应该注意的问题。



```cpp
int main() {
    std::vector<std::string> strs1 = {"abc", "def"};
    std::cout << &strs1[0] << std::endl;

    std::vector<std::string> strs2(strs1);
    std::cout << &strs2[0] << std::endl;

    std::vector<std::string> strs3(std::move(strs1));  // 指针地址转移给str3
    std::cout << &strs3[0] << std::endl;

    return 0;
}
//
0xa00012850
0xa000228d0
0xa00012850
```



# 16. 量化数学

## 1. 两个大矩阵已经在内存，相乘怎么可以快一点

[大佬是怎么优雅实现矩阵乘法的](https://mp.weixin.qq.com/s/MGkPimWdtqVhdARgG4RPzA)

我想了一会儿，说尽量按行取数据，可以用到缓存。他说另一个矩阵要按列取，我说那是不是可以做下矩阵的转置。他说转置的成本也比较大，然后他提示说存的时候，是不是就可以把另一个矩阵保存的时候就把列转成行，我说这个应该是可以的。
然后他又问要是更大的矩阵，缓存都存不下，还有什么方法吗？我想了一会儿，确实也没啥想法。他说是不是可以按block块来把矩阵分割来乘，我说应该是可以的。他又问那怎么分割块呢？我说一个缓存cacheline一般是按64字节来取，是不是可以按这个大小来分割块的每行数据。他也没太说清楚对还是不对。后来他说我可能在系统开发和设计比较擅长，再这种内存优化，低延迟这些看起来经验比较少，大体是这样的交流吧



## 2. 行情数据推送过来，大体是按时间排序的，但是会有一些乱序的，怎么能够把这些数据排好序落盘，或者说有一部分数据已经落盘了，后续来的数据还需要读前面的文件，怎么可以快点读文件

时序数据库



## 3. 行情数据过来，如何持续在排序排出前五名



# 通用

## 你的缺点是什么

* 不够自信

有些情况，会觉得自己没有足够的经验和知识去处理一些任务。但是正在尝试各种方法来提高自己的自信。

如加入一些行业相关的社交圈，通过交流来获取更多的经验和知识。同时也在学习如何适应和克服自己的不安情绪。这些努力都让我感到更加自信。

* 过于注重细节

有时候会过于注重细节，忽视大局。目前也在解决这个问题：

首先，会更多询问同事和上司的建议，让我想法更多全面。

其次，会对项目做一个整体的规划，防止工作跑偏。

最近也在看了相关书，学习提高自身的全局思维。

相信我会改善缺点，在工作上更进一步。

## 讲一个遇到的具体问题，是怎么分析解决的

分布式构建系统的项目中，在某个产品的编译过程中会有经常偶发的出现cmake进程被拦截住发到主进程后，执行挂死的问题。

* 先通过gdb attach进程查看堆栈信息，但cmake不是debug版本，没看到特别有效的信息，只看在栈顶在执行Linux的select函数，没有更详细的信息。
* 再通过strace跟踪打印进程执行信息，看到不断在循环执行select命令，与gdb看到的信息是一致的。并且里面可以看到具体参数，其中第二个参数里数有1000多个左右文件描述符列表，但这1000个文件描述符是做什么，以为为什么会同时打开这么多文件描述符还需要具体分析。同时由于Linux每个进程默认情况下，最多可以打开1024个文件，最多有1024个文件描述符，所以初步怀疑是这些文件描述符没有及时关闭，导致了cmake进程出现了挂死。
* 再通过/proc/pid/fd可以看到所有打开的文件描述符，看到这1000多个文件描述符对应着编译路径下的一些文件路径。并且这些文件对应的文件内容都是空的。
* 再分析这些文件的来源，我们编译构建的源代码不是直接git clone下载的，用到了一个自研的分布式文件系统会进行文件的上传和下载，再看对于空文件的分支处理有问题，只open了文件，最后没有close文件，导致文件描述符一直没有关闭。
* 最后修改了这块自研代码，空文件分支在open结束后也close文件，再进行多次验证，没再出现cmake进程挂死的问题。



















