# 1. markdown文件的基本常用编写语法

<https://www.cnblogs.com/liugang-vip/p/6337580.html>

<https://www.appinn.com/markdown/>

## 1.1. 标题

> 使用多个#，如

## 1.2. 列表

无序列表，使用*、+、-，如：

> * 1
> * 2
> * 3

有序列表，使用数字+点，如：

> 1. 列表1
> 2. 列表2
> 3. 列表3

## 1.3. 区块引用

使用>，如：

* 不以结婚为目的的恋爱都叫耍流氓
    > 这是毛主席说的
* 前方高能
    > 注意：这里是为了提醒前面有很刺激的事情发生

可以多级引用，如：
> ## 标题
> * 不以结婚为目的的恋爱都叫耍流氓
>> 这是毛主席说的
> * 前方高能
>> 注意：这里是为了提醒前面有很刺激的事情发生

再如：
> 一级引用
>> 二级引用
>>> 三级引用
>>>> 四级引用
>>>>> 五级引用
>>>>>> 六级引用
>>>>>>> 七级引用

## 1.4. 分割线

由* - _（星号，减号，下划线）至少3个符号表示，如：

***

## 1.5. 链接

### 1.5.1. 行内式：

> [妙语连珠](http://www.baidu.com "百度搜索")是什么意思？

常用的列表链接，如：

> * [列表链接1](http://www.baidu.com)
> * [列表链接2](http://www.baidu.com)

链接带title属性，如：
> [门前大桥下](/home "这是一首诗")，游过一群鸭

我自己测试，只需要写：/ "悬浮提示" 即可，如：
> [门前大桥下](/ "这是一首诗")，游过一群鸭

### 1.5.2. 参数式：

> [name]: http://www.baidu.com "名称"
> [home]: <http://www.baidu.com> "首页"
> [也支持中文]:/home/name "瞎写的"
>
> 这里是[name]，这里是[home]，这里是[也支持中文]
>
> 这就好理解了，就是把链接当成参数，适合多出使用相同链接的场景，注意参数的对应关系，参数定义时，这3种写法都可以：
>
> [foo]: http://example.com/ "Optional Title Here"
> [foo]: http://example.com/ 'Optional Title Here'
> [foo]: http://example.com/ (Optional Title Here)
>
> 还支持这种写法，如果你不想混淆的话：
>
> [foo]: <http://example.com/> "Optional Title Here"

还可以使用[链接内容][链接ID]的方式，如：
> 你也可以选择性地在两个方括号中间加上一个空格：
>
> This is [an example][id] reference-style link.
>
> 接着，在文件的任意处，你可以把这个标记的链接内容定义出> 来：
>
> [id]: http://example.com/  "Optional Title Here"
>
> 链接的定义可以放在文件中的任何一个地方，我比较偏好直接放> 在链接出现段落的后面，你也可以把它放在文件最后面，就像是> 注解一样。
>
> 下面是一个参考式链接的范例：
>
> I get 10 times more traffic from [Google][1] than > from
> [Yahoo][2] or [MSN][3].
>
>   [1]: http://google.com/        "Google"
>   [2]: http://search.yahoo.com/  "Yahoo Search"
>   [3]: http://search.msn.com/    "MSN Search"
>
> 如果改成用链接名称的方式写，或者删除第2组中括号：
>
> I get 10 times more traffic from [Google][] than from
> [Yahoo][] or [MSN][].
>
>   [google]: http://google.com/        "Google"
>   [yahoo]:  http://search.yahoo.com/  "Yahoo Search"
>   [msn]:    http://search.msn.com/    "MSN Search"
>
> 参考式的链接其实重点不在于它比较好写，而是它比较好读，比较一下上面的范例，使用参考式的文章本身只有 81 个字符，但是用行内形式的却会增加到 176 个字元，如果是用纯 HTML 格式来写，会有 234 个字元，在 HTML 格式中，标签比文本还要多。

## 1.6. 图片

### 1.6.1. 行内式：

![我是图片](pic1.png)

### 1.6.2. 参数式

[loadProcess]: pic1.png "类加载过程"

参数式图片，这里是![类加载过程][loadProcess]

[类加载过程]: pic1.png "类加载过程"

参数式图片，这里是![类加载过程]

## 1.7. 代码框

单行使用``，如：

`const std::string s = "abc";`

Use the `printf()` function.

``There is a literal backtick (`) here.``

多行可以使用``````

```c++ 定义常量字符串
const std::string s = "abc";
class ABC
{
public:
    int getI() const
    {
        return i_;
    }
private:
    int i_;
}
```

## 1.8. 表格

从这3种不同写法看，表格的格式不一定要对的非常起，但是为了好看，对齐肯定是最好的，第一种的分割线后面的冒号表示对齐方式，写在左边表示左对齐，右边为右对齐，两边都写表示居中。

第1种：
|name|age|sex|
|:------------------:|:---------|----------:|
|tony|20|男|
|lucy|18|女|

第二种：
表头1|表头2|表头3
:------:|:--------------|---:
cell1|cell2|cell5
cell3|cell4|cell6

第三种：
学号|姓名|分数
-|-|-
小明|男|75
小红|女|79
小李|男|92

更复杂的表格生成，就需要用原生的html语法：
<https://blog.csdn.net/tuxingchen6/article/details/55222951>
<table>
  <tr>
    <th width=10%, bgcolor=yellow >参数</th>
    <th width=40%, bgcolor=yellow>详细解释</th>
    <th width="50%", bgcolor=yellow>备注</th>
  </tr>
  <tr>
    <td bgcolor=#eeeeee> -l </td>
    <td> use a long listing format  </td>
    <td> 以长列表方式显示（显示出文件/文件夹详细信息）  </td>
  </tr>
  <tr>
    <td bgcolor=#00FF00>-t </td>
    <td> sort by modification time </td>
    <td> 按照修改时间排序（默认最近被修改的文件/文件夹排在最前面） </td>
  <tr>
    <td bgcolor=rgb(0,10,0)>-r </td>
    <td> reverse order while sorting </td>
    <td>  逆序排列 </td>
  </tr>
</table>

## 1.9. 强调

*字体倾斜*

_字体倾斜_

**字体加粗**

__字体加粗__

***字体倾斜加粗***

___字体倾斜加粗___

## 1.10. 转义

* \\
* \`
* \~
* \*
* \_
* \-
* \+
* \.
* \!

Markdown 支持以下这些符号前面加上反斜杠来帮助插入普通的符号：

> \   反斜线
>
> `   反引号
>
> \*  星号
>
> _   底线
>
> {}  花括号
>
> []  方括号
>
> ()  括弧
>
> \#   井字号
>
> \+   加号
>
> \-   减号
>
> .   英文句点
>
> !   惊叹号

## 1.11. 删除线

~~请删除我吧~~

## 空格、空行

使用html的语法：
&nbsp;
</br>