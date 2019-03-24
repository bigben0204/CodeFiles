# 1. Python开发简单爬虫

## 1.1. 课程介绍

课程内容：

1. 爬虫简介
2. 简单爬虫架构
3. URL管理器
4. 网页下载器（urllib2）
5. 网页解析器BeautifulSout
6. 完整实例：爬取百度百科Python词条相关的1000个页面数据

## 1.2. 爬虫简介 <https://www.imooc.com/video/10675>

* 爬虫：一段自动抓取互联网信息的程序

## 1.3. 爬虫技术的价值

* 价值：互联网数据，为我所用！

## 1.4. 简单爬虫架构

爬虫调试端 -> 爬虫【URL管理器 -> 网页下载器 -> 网页解析器(**解析出来的有用的URL补充进URL管理器**)】 ->  价值数据

## 1.5. 简单爬虫架构-运行流程

![运行流程](pics/动态运行流程.png)

## 1.6. URL管理器

* URL管理器：管理待抓取URL集合和已爬取URL集合

防止重复爬取、防止循环抓取

![URL管理器](pics/URL管理器.png)

## 1.7. URL管理器(2)

* 实现方式

![URL管理器的实现方式](pics/URL管理器的实现方式.png)

## 1.8. 网页下载器

* 网页下载器：将互联网上URL对应的网页下载到本地的工具

![网页下载器](pics/网页下载器.png)

urllib2：Python官方基础模块

requests：第三方包，有更强的功能

## 1.9. 网页下载器-urllib2

* urllib2下载网页方法1：最简洁方法

url -> urllib2.urlopen(url)

```python
import urllib2

#直接请求
response = urllib2.urlopen('http://www.baidu.com')

#获取状态码，如果是200表示获取成功
print response.getcode()

# 读取内容
cont = response.read()
```

* urllib2下载网页方法2：添加data、http header

url/data/header -> urllib2.Request -> urllib2.urlopen(request)

```python
import urllib2

# 创建Request对象
request = urllib2.Request(url)

# 添加数据
request.add_data('a', '1')
# 添加http的header
request.add_header('User-Agen', 'Mozilla/5.0')

# 发送请求获取结果
request = urllib2.urlopen(request)
```

* urllib2下载网页方法3：添加特殊情景的处理器

HTTPCookieProcessor/ProxyHandler/HTTPSHandler/HTTPRedirectHandler -> opener = urllib2.build_opener(handler) -> urllib2.install_opener(opener) -> urllib2.urlopen(url)/urllib2.urlopen(request)

```python
import urllib2, cookielib

# 创建cookie容器
cj = cookielib.CookieJar()

# 创建一个opener
opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cj))

# 给urllib2安装opener
urllib2.install_opener(opener)

# 使用带有cookie的urllib2访问网页
response = urllib2.urlopen("http://www.baidu.com")
```

## 1.10. Python爬虫urllib2实例代码演示

```python
# testUrllib.py

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import http.cookiejar
import urllib.request

if __name__ == '__main__':
    url = "http://www.baidu.com"

    print("第一种方法")
    response1 = urllib.request.urlopen(url)
    print(response1.getcode())
    print(len(response1.read()))

    print("第二种方法")
    request = urllib.request.Request(url)
    request.add_header("user-agent", "Mozilla/5.0")
    response2 = urllib.request.urlopen(url)
    print(response2.getcode())
    print(len(response2.read()))

    print("第三种方法")
    cj = http.cookiejar.CookieJar()
    opener = urllib.request.build_opener(urllib.request.HTTPCookieProcessor(cj))
    urllib.request.install_opener(opener)
    response3 = urllib.request.urlopen(url)
    print(response3.getcode())
    read = response3.read()  # read方法只能调用一次，后续的调用read将返回""
    print(len(read))
    print(cj)
    print(read)

输出：
第一种方法
200
153035
第二种方法
200
153227
第三种方法
200
153356
<CookieJar[<Cookie BAIDUID=22C30ED0D298B472C3CF563F6EBCE0D7:FG=1 for .baidu.com/>, <Cookie BIDUPSID=22C30ED0D298B472C3CF563F6EBCE0D7 for .baidu.com/>, <Cookie H_PS_PSSID=1421_21092_18560_28721_28557_28697_28585_28625_28606 for .baidu.com/>, <Cookie PSTM=1553426272 for .baidu.com/>, <Cookie delPer=0 for .baidu.com/>, <Cookie BDSVRTM=0 for www.baidu.com/>, <Cookie BD_HOME=0 for www.baidu.com/>]>
b'<!DOCTYPE html>\n<!--STATUS XXX # 省略
```

## 1.11. 网页解析器

* 网页解析器：从网页中提取有价值数据的工具

![网页解析器](pics/网页解析器.png)

* Python几种网页解析器：
1. 正则表达式
2. html.parser
3. BeautifulSoup（可以使用html.parser或lxml来作为解析器）
4. lxml

正则表达式：模糊匹配

其它三种：结构化解析

* 结构化解析-DOM（Document Object Model）树

![网页解析器(2)](pics/网页解析器(2).png)

## 1.12. 网页解析器-Beautiful Soup

* Beautiful Soup
> Python第三方库，用于从HTML或XML中提取数据
* 安装并测试beautifulsoup4
> 安装pip install beautifulsoup4
>
> 测试import bs4

## 1.13. 网页解析器-Beautiful Soup语法

![BeautifulSoup语法](pics/BeautifulSoup语法.png)
![BeautifulSoup语法(2)](pics/BeautifulSoup语法(2).png)

* 创建BeautifulSoup对象
```python
from bs4 import BeautifulSoup

# 根据HTML网页字符串创建BeautifulSoup对象
soup = BeautifulSoup(
    html_doc,               # HTML文档字符串
    'html.parser',          # HTML解析器
    from_encoding='utf-8'   # HTML文档的编码
)
```

* 搜索节点（find_all, find）
```python
# 方法：find_all(name, attrs, string)

# 查找所有标签为a的节点
soup.find_all('a')

# 查找所有标签为a，链接符合/view/123.htm形式的节点
soup.find_all('a', href='/view/123.htm')
soup.find_all('a', href=re.compile(r'/view/\d+\.htm'))

# 查找所有标签为div，class为abc，文字为Python的节点
# class_为避免和关键字class冲突，BeautifulSoup加的下划线
soup.find_all('div', class_='abc', string='Python')
```

* 访问节点信息
```python
# 得到节点：<a href='1.html'>Python</a>

# 获取查找到的节点的标签名称
node.name

# 获取查找到的a节点的href属性
node['href]

# 获取查找到的a节点的链接文字
node.get_text()
```

BeautifulSoup使用样例：
```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import re

import bs4

if __name__ == '__main__':
    html_doc = """
    <html><head><title>The Dormouse's story</title></head>
    <body>
    <p class="title"><b>The Dormouse's story</b></p>

    <p class="story">Once upon a time there were three little sisters; and their names were
    <a href="http://example.com/elsie" class="sister" id="link1">Elsie</a>,
    <a href="http://example.com/lacie" class="sister" id="link2">Lacie</a> and
    <a href="http://example.com/tillie" class="sister" id="link3">Tillie</a>;
    and they lived at the bottom of a well.</p>

    <p class="story">...</p>
    """

    # 增加这个参数 from_encoding='utf-8' 会有告警：UserWarning: You provided Unicode markup but also provided a value for from_encoding. Your from_encoding will be ignored.
    soup = bs4.BeautifulSoup(html_doc, 'html.parser')

    print('获取所有链接')
    links = soup.find_all('a')
    for link in links:
        print(link.name, link['href'], link.get_text())

    print('获取lacie的链接')
    link_node_lacie = soup.find('a', href='http://example.com/lacie')
    print(link_node_lacie.name, link_node_lacie['href'], link_node_lacie.get_text())

    print('正则匹配')
    link_node_tillie= soup.find('a', href=re.compile(r'ill'))
    print(link_node_tillie.name, link_node_tillie['href'], link_node_tillie.get_text())

    print('获取P段落文字')
    p_node = soup.find('p', class_='title')
    print(p_node.name, p_node.get_text())

输出：
获取所有链接
a http://example.com/elsie Elsie
a http://example.com/lacie Lacie
a http://example.com/tillie Tillie
获取lacie的链接
a http://example.com/lacie Lacie
正则匹配
a http://example.com/tillie Tillie
获取P段落文字
p The Dormouse's story
```

## 1.14. Python爬虫实例-分析目标

![爬虫实例](pics/爬虫实例.png)

* 目标：百度百科Python词条相关词条网页-标题和简介
* 入口页：<https://baike.baidu.com/item/Python/407313>
* URL格式：
-词条页面URL：/item/%E7%A8%8B%E5%BA%8F%E8%AE%BE%E8%AE%A1%E8%AF%AD%E8%A8%80/2317999
* 数据格式：

-标题：
> `<dd class="lemmaWgt-lemmaTitle-title"><h1>***</h1></dd>`

-简介：
> `<div class="lemma-summary">***</div>`
* 页面编码：UTF-8

## 1.15. 调度程序

```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

from baike_spider import url_manager, html_downloader, html_parser, html_outputer


class SpiderMain(object):
    __MAX_URL_NUM = 1000

    def __init__(self):
        self.urls = url_manager.UrlManager()
        self.downloader = html_downloader.HtmlDownloader()
        self.parser = html_parser.HtmlParser()
        self.outputer = html_outputer.HtmlOutputer()

    def craw(self, root_url):
        count = 1
        self.urls.add_new_url(root_url)
        while self.urls.has_new_url():
            try:
                new_url = self.urls.get_new_url()
                print(f'craw {count} : {new_url}')
                html_cont = self.downloader.download(new_url)
                new_urls, new_data = self.parser.parse(new_url, html_cont)
                self.urls.add_new_urls(new_urls)
                self.outputer.collect_data(new_data)

                if count == self.__MAX_URL_NUM:
                    break

                count += 1
            except Exception as e:
                print(f'craw failed. url={new_url}, error={e}')

        self.outputer.output_html()


if __name__ == '__main__':
    root_url = "https://baike.baidu.com/item/Python/407313"
    obj_spider = SpiderMain()
    obj_spider.craw(root_url)

```

## 1.16. URL管理器
```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-


class UrlManager(object):
    def __init__(self) -> None:
        self.new_urls = set()
        self.old_urls = set()

    def add_new_url(self, url: str) -> None:
        if url is None:
            return
        if url not in self.new_urls and url not in self.old_urls:
            self.new_urls.add(url)

    def add_new_urls(self, urls) -> None:
        if urls is None or len(urls) == 0:
            return
        for url in urls:
            self.add_new_url(url)

    def has_new_url(self) -> bool:
        return len(self.new_urls) != 0

    def get_new_url(self) -> str:
        new_url = self.new_urls.pop()
        self.old_urls.add(new_url)
        return new_url
```

## HTML下载器html_downloader
```python
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import urllib.request


class HtmlDownloader(object):
    def download(self, url):
        if url is None:
            return None

        response = urllib.request.urlopen(url)
        if response.getcode() != 200:
            return None

        return response.read()
```

## HTML解析器html_parser
