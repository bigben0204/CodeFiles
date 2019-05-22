# 1. Python爬虫框架Scrapy入门与实践

<https://www.imooc.com/video/17523>

## 1.1. Scrapy是什么？

Scrapy是一套基于Twisted的异步处理框架，是纯python实现的爬虫框架，用户只需要定制开发几个模块就可以轻松的实现一个爬虫，用来抓取网页内容或者各种图片。

![Scrapy框架](pics/Scrapy框架.png)

## 1.2. Scrapy项目实践

以爬取豆瓣top250为例：<https://movie.douban.com/top250>

Scrapy抓取4步走：

* 新建项目
* 明确目标 <https://www.imooc.com/video/17516>
* 制作爬虫
* 存储内容

```python
# spiders/douban_spider.py
# -*- coding: utf-8 -*-
import scrapy

from douban.items import DoubanItem


class DoubanSpiderSpider(scrapy.Spider):
    # 这里是爬虫名字
    name = 'douban_spider'
    # 允许的域名
    allowed_domains = ['movie.douban.com']
    # 入口url，扔到调度器里去
    start_urls = ['https://movie.douban.com/top250']  # 如果是http网址，则无须在settings中配置USER_AGENT

    # 默认解析方法
    def parse(self, response):
        # print(response.text)
        # 循环电影的条目
        movie_list = response.xpath("//div[@class='article']//ol[@class='grid_view']/li")
        for i_item in movie_list:
            # item文件导进来
            douban_item = DoubanItem()

            # 写详细的xpath，进行数据的解析
            douban_item['serial_number'] = i_item.xpath(".//div[@class='item']//em/text()").extract_first()  # 解析到第1个数据
            douban_item['movie_name'] = i_item.xpath(
                ".//div[@class='info']/div[@class='hd']/a/span[1]/text()").extract_first()

            # xpath直接获取的数据不是想要的，需要进行额外数据处理
            content = i_item.xpath(
                ".//div[@class='info']/div[@class='bd']/p[1]/text()").extract()  # 解析到数据，而非第1个，返回类型是list
            douban_item['introduce'] = "".join(content[-1].split())  # 介绍有导演和类型等多行，只获取最后一行电影类型

            douban_item['star'] = i_item.xpath(".//span[@class='rating_num']/text()").extract_first()
            douban_item['evaluate'] = i_item.xpath(".//div[@class='star']/span[4]/text()").extract_first()
            douban_item['description'] = i_item.xpath(".//p[@class='quote']/span/text()").extract_first()

            # 需要将数据yield到pipelines里去进行数据清洗或存储等
            yield douban_item

        # 解析下一面规则，取的后一页的xpath
        next_link = response.xpath("//span[@class='next']/link/@href").extract_first()
        if next_link:
            yield scrapy.Request(f"https://movie.douban.com/top250{next_link}", callback=self.parse)

        # 如下为视频代码：
        # next_link = response.xpath("//span[@class='next']/link/@href").extract()
        # if next_link:
        #     next_link = next_link[0]
        #     yield scrapy.Request("https://movie.douban.com/top250" + next_link, callback=self.parse)

# items.py
# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# https://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class DoubanItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    # 序号
    serial_number = scrapy.Field()
    # 电影名称
    movie_name = scrapy.Field()
    # 电影介绍
    introduce = scrapy.Field()
    # 星级
    star = scrapy.Field()
    # 电影的评论数
    evaluate = scrapy.Field()
    # 电影的描述
    description = scrapy.Field()



# settings.py
# -*- coding: utf-8 -*-

# Scrapy settings for douban project
#
# For simplicity, this file contains only settings considered important or
# commonly used. You can find more settings consulting the documentation:
#
#     https://doc.scrapy.org/en/latest/topics/settings.html
#     https://doc.scrapy.org/en/latest/topics/downloader-middleware.html
#     https://doc.scrapy.org/en/latest/topics/spider-middleware.html

BOT_NAME = 'douban'

SPIDER_MODULES = ['douban.spiders']
NEWSPIDER_MODULE = 'douban.spiders'

# Crawl responsibly by identifying yourself (and your website) on the user-agent
USER_AGENT = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36'

# Obey robots.txt rules
ROBOTSTXT_OBEY = False

# Configure maximum concurrent requests performed by Scrapy (default: 16)
# CONCURRENT_REQUESTS = 32

# Configure a delay for requests for the same website (default: 0)
# See https://doc.scrapy.org/en/latest/topics/settings.html#download-delay
# See also autothrottle settings and docs
DOWNLOAD_DELAY = 0.5
# The download delay setting will honor only one of:
# CONCURRENT_REQUESTS_PER_DOMAIN = 16
# CONCURRENT_REQUESTS_PER_IP = 16

# Disable cookies (enabled by default)
# COOKIES_ENABLED = False

# Disable Telnet Console (enabled by default)
# TELNETCONSOLE_ENABLED = False

# Override the default request headers:
# DEFAULT_REQUEST_HEADERS = {
#   'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
#   'Accept-Language': 'en',
# }

# Enable or disable spider middlewares
# See https://doc.scrapy.org/en/latest/topics/spider-middleware.html
# SPIDER_MIDDLEWARES = {
#    'douban.middlewares.DoubanSpiderMiddleware': 543,
# }

# Enable or disable downloader middlewares
# See https://doc.scrapy.org/en/latest/topics/downloader-middleware.html
# DOWNLOADER_MIDDLEWARES = {
#     # 'douban.middlewares.DoubanDownloaderMiddleware': 543,
#     'douban.middlewares.ProxyMiddleware': 543,
# }

# Enable or disable extensions
# See https://doc.scrapy.org/en/latest/topics/extensions.html
# EXTENSIONS = {
#    'scrapy.extensions.telnet.TelnetConsole': None,
# }

# Configure item pipelines
# See https://doc.scrapy.org/en/latest/topics/item-pipeline.html
# 要开启pipelines选项，否则不会调用pipelines类
# ITEM_PIPELINES = {
#     'douban.pipelines.DoubanPipeline': 300,
# }

# Enable and configure the AutoThrottle extension (disabled by default)
# See https://doc.scrapy.org/en/latest/topics/autothrottle.html
# AUTOTHROTTLE_ENABLED = True
# The initial download delay
# AUTOTHROTTLE_START_DELAY = 5
# The maximum download delay to be set in case of high latencies
# AUTOTHROTTLE_MAX_DELAY = 60
# The average number of requests Scrapy should be sending in parallel to
# each remote server
# AUTOTHROTTLE_TARGET_CONCURRENCY = 1.0
# Enable showing throttling stats for every response received:
# AUTOTHROTTLE_DEBUG = False

# Enable and configure HTTP caching (disabled by default)
# See https://doc.scrapy.org/en/latest/topics/downloader-middleware.html#httpcache-middleware-settings
# HTTPCACHE_ENABLED = True
# HTTPCACHE_EXPIRATION_SECS = 0
# HTTPCACHE_DIR = 'httpcache'
# HTTPCACHE_IGNORE_HTTP_CODES = []
# HTTPCACHE_STORAGE = 'scrapy.extensions.httpcache.FilesystemCacheStorage'

mysql_host = 'localhost'
mysql_port = 3306
mysql_db_name = 'hibernate'
mysql_table_name = 'douban_movie'
mysql_user = 'root'
mysql_password = 'root123'

PROXIES = ['http://222.223.115.30:31387', 'http://116.209.53.5:9999']



# spider_main.py
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from scrapy import cmdline

if __name__ == '__main__':
    cmdline.execute('scrapy crawl douban_spider'.split())
    # cmdline.execute(['scrapy', 'crawl', 'douban_spider'])
```

## 1.3. 保存数据 <https://www.imooc.com/video/17519>

1.保存成json：

(base) D:\Program Files\JetBrains\PythonProject\douban\douban\spiders>scrapy crawl douban_spider -o test.json

内容显示为unicode格式。

2.保存为csv：

(base) D:\Program Files\JetBrains\PythonProject\douban\douban\spiders>scrapy crawl douban_spider -o test.csv

直接打开csv文件显示为乱码，使用notepad++修改为UTF-8-BOM格式，再打开可以正常显示。

3.写入MySql <https://www.cnblogs.com/bkwxx/p/10120540.html>

在数据库中先建表，并在设计表中将id修改为自动增长：

```sql
drop table douban_movie
create table douban_movie(
    id int not null,
    serial_number int not null comment '序号',
    movie_name varchar(50) not null comment '电影名称',
    introduce varchar(100) not null comment '电影介绍',
    star varchar(10) not null comment '星级',
    evaluate varchar(50) not null comment '电影的评论数',
    description varchar(500) null comment '电影的描述',
    primary key(id)
)
```

开启settings.py里的ITEM_PIPELINES选项后：

```python
# pipelines.py
# -*- coding: utf-8 -*-
import pymysql


# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://doc.scrapy.org/en/latest/topics/item-pipeline.html
from douban.settings import mysql_host, mysql_port, mysql_user, mysql_password, mysql_db_name, mysql_table_name


class DoubanPipeline(object):
    __SQL = """
    insert into {0}(serial_number, movie_name, introduce, star, evaluate, description)
        values(%s, %s, %s, %s, %s, %s)
    """.format(mysql_table_name)

    def __init__(self) -> None:
        self.__connect = pymysql.connect(host=mysql_host, port=mysql_port, user=mysql_user, passwd=mysql_password,
                                         db=mysql_db_name, charset='utf8')
        self.__cursor = self.__connect.cursor()

    def process_item(self, item, spider):
        # data = dict(item)
        # print(data)
        self.__cursor.execute(self.__SQL, (item['serial_number'], item['movie_name'], item['introduce'], item['star'], item['evaluate'], item['description']))
        self.__connect.commit()
        return item
```

运行，再查询数据库，可以看到数据插入到douban_moive表中。**由于增加了写数据库操作，耗时增加**。

## 1.4. ip代理中间件编写

<https://www.imooc.com/video/17520>
<https://www.cnblogs.com/cnkai/p/7401526.html>
<https://blog.csdn.net/qq_26877377/article/details/79655746>

```python
# middlewares.py
import random

class ProxyMiddleware(object):
    """
    设置Proxy
    """

    def __init__(self, ip):
        self.__ip = ip

    @classmethod
    def from_crawler(cls, crawler):
        return cls(ip=crawler.settings.get('PROXIES'))

    def process_request(self, request, spider):
        ip = random.choice(self.__ip)
        request.meta['proxy'] = ip

# settings.py
#543是级别，越小级别越高
DOWNLOADER_MIDDLEWARES = {
    # 'douban.middlewares.DoubanDownloaderMiddleware': 543,
    'douban.middlewares.ProxyMiddleware': 543,
}

PROXIES = ['http://222.223.115.30:31387', 'http://116.209.53.5:9999']
```

## 1.5. user_agent中间件的编写

<https://blog.csdn.net/china_python/article/details/78619170>

User Agent中文名为用户代理，简称 UA，它是一个特殊字符串头，使得服务器能够识别客户使用的操作系统及版本、CPU 类型、浏览器及版本、浏览器渲染引擎、浏览器语言、浏览器插件等。

```python
# middlewares.py
class UserAgentMiddleware(object):
    """
    设置User_Agent
    """

    def __init__(self, user_agent):
        self.__user_agent = user_agent

    @classmethod
    def from_crawler(cls, crawler):
        return cls(user_agent=crawler.settings.get('USER_AGENTS'))

    def process_request(self, request, spider):
        request.headers['User_Agent'] = random.choice(self.__user_agent)


# settings.py
DOWNLOADER_MIDDLEWARES = {
    # 'douban.middlewares.DoubanDownloaderMiddleware': 543,
    # 'douban.middlewares.ProxyMiddleware': 543,
    'douban.middlewares.UserAgentMiddleware': 544,
}


USER_AGENTS = [
    'MSIE (MSIE 6.0; X11; Linux; i686) Opera 7.23',
    'Opera/9.20 (Macintosh; Intel Mac OS X; U; en)',
    'Opera/9.0 (Macintosh; PPC Mac OS X; U; en)',
    'iTunes/9.0.3 (Macintosh; U; Intel Mac OS X 10_6_2; en-ca)',
    'Mozilla/4.76 [en_jp] (X11; U; SunOS 5.8 sun4u)',
    'iTunes/4.2 (Macintosh; U; PPC Mac OS X 10.2)',
    'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:5.0) Gecko/20100101 Firefox/5.0',
    'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:9.0) Gecko/20100101 Firefox/9.0',
    'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20120813 Firefox/16.0',
    'Mozilla/4.77 [en] (X11; I; IRIX;64 6.5 IP30)',
    'Mozilla/4.8 [en] (X11; U; SunOS; 5.7 sun4u)'
]
```

## 1.6. Scrapy项目注意事项

注意事项：

* 中间件定义完要在settings文件内启用
* 爬虫文件名和爬虫名称不能相同，spiders目录内不能存在相同爬虫名称的项目文件
* 做一个文明守法的好网民，不要爬取公民的隐私数据，不要给对方的系统带来不必要的麻烦

## 1.7. 部署多个spider项目

<https://www.jianshu.com/p/39f9c5bffbd1>

## 1.8. 爬取代理IP

<https://blog.csdn.net/learn_is_happy/article/details/78893623>

修改后的代码如下：

```python
# douban_spider.py
# -*- coding: utf-8 -*-
import scrapy

from douban.custom_settings import CUSTOM_SETTINGS_FOR_DOUBAN
from douban.items import DoubanItem


class DoubanSpiderSpider(scrapy.Spider):
    custom_settings = CUSTOM_SETTINGS_FOR_DOUBAN
    # 这里是爬虫名字
    name = 'douban_spider'
    # 允许的域名
    allowed_domains = ['movie.douban.com']
    # 入口url，扔到调度器里去
    start_urls = ['https://movie.douban.com/top250']  # 如果是http网址，则无须在settings中配置USER_AGENT

    # 默认解析方法
    def parse(self, response):
        # print(response.text)
        # 循环电影的条目
        movie_list = response.xpath("//div[@class='article']//ol[@class='grid_view']/li")
        for i_item in movie_list:
            # item文件导进来
            douban_item = DoubanItem()

            # 写详细的xpath，进行数据的解析
            douban_item['serial_number'] = i_item.xpath(".//div[@class='item']//em/text()").extract_first()  # 解析到第1个数据
            douban_item['movie_name'] = i_item.xpath(
                ".//div[@class='info']/div[@class='hd']/a/span[1]/text()").extract_first()

            # xpath直接获取的数据不是想要的，需要进行额外数据处理
            content = i_item.xpath(
                ".//div[@class='info']/div[@class='bd']/p[1]/text()").extract()  # 解析到数据，而非第1个，返回类型是list
            douban_item['introduce'] = "".join(content[-1].split())  # 介绍有导演和类型等多行，只获取最后一行电影类型

            douban_item['star'] = i_item.xpath(".//span[@class='rating_num']/text()").extract_first()
            douban_item['evaluate'] = i_item.xpath(".//div[@class='star']/span[4]/text()").extract_first()
            douban_item['description'] = i_item.xpath(".//p[@class='quote']/span/text()").extract_first()

            # 需要将数据yield到pipelines里去进行数据清洗或存储等
            yield douban_item

        # 解析下一面规则，取的后一页的xpath
        next_link = response.xpath("//span[@class='next']/link/@href").extract_first()
        if next_link:
            yield scrapy.Request(f"https://movie.douban.com/top250{next_link}", callback=self.parse)

        # 如下为视频代码：
        # next_link = response.xpath("//span[@class='next']/link/@href").extract()
        # if next_link:
        #     next_link = next_link[0]
        #     yield scrapy.Request("https://movie.douban.com/top250" + next_link, callback=self.parse)


# agent_ip_spider.py
# -*- coding: utf-8 -*-
import scrapy

from douban.custom_settings import CUSTOM_SETTINGS_FOR_AGENT_IP
from douban.items import IpInfoItem


def save_html_cont(text):
    with open('tmp.html', 'w', encoding='utf-8') as f:
        f.write(text)


class AgentIpSpiderSpider(scrapy.Spider):
    custom_settings = CUSTOM_SETTINGS_FOR_AGENT_IP
    name = 'agent_ip_spider'
    allowed_domains = ['www.xicidaili.com']
    start_urls = []

    def __init__(self):
        for i in range(1, 2):
            self.start_urls.append('http://www.xicidaili.com/wt/' + str(i))

    def parse(self, response):
        # save_html_cont(response.text)
        ip_item = IpInfoItem()

        tr_list = response.xpath("//tr")
        for tr in tr_list:
            ip = tr.xpath("./td[2]/text()").extract_first()
            port = tr.xpath("./td[3]/text()").extract_first()
            ip_item['ip'] = f"{ip}:{port}"
            yield ip_item

# alive_ip_test.py
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import requests

alive_ip = []


def test_alive(proxy):
    global alive_ip
    for proxies_be in proxy:
        # request中的IP地址需要以下列形式的参数写进函数
        proxies = {"http": proxies_be}

        print("正在测试:{}".format(proxies))
        try:
            r = requests.get("https://movie.douban.com/top250", proxies=proxies, timeout=2)
            if r.status_code == 200:
                print("成功,ip为{}".format(proxies))
                alive_ip.append(proxies_be)
            else:
                print("失败")
        except:
            print("失败")


def out_file(alive_ip=[]):
    with open("alive_ip.txt", "w") as f:
        for ip in alive_ip:
            f.write(str(ip) + "\n")
        print("输出完毕")


def test(filename="blank.txt"):
    with open(filename, "r") as f:
        lines = f.readlines()

        proxys = list(map(lambda x: x.strip(), lines))

        test_alive(proxys)

    out_file(alive_ip)

# settings.py
# -*- coding: utf-8 -*-

# Scrapy settings for douban project
#
# For simplicity, this file contains only settings considered important or
# commonly used. You can find more settings consulting the documentation:
#
#     https://doc.scrapy.org/en/latest/topics/settings.html
#     https://doc.scrapy.org/en/latest/topics/downloader-middleware.html
#     https://doc.scrapy.org/en/latest/topics/spider-middleware.html

BOT_NAME = 'douban'

SPIDER_MODULES = ['douban.spiders']
NEWSPIDER_MODULE = 'douban.spiders'

# Crawl responsibly by identifying yourself (and your website) on the user-agent
USER_AGENT = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36'

# Obey robots.txt rules
ROBOTSTXT_OBEY = False

# Configure maximum concurrent requests performed by Scrapy (default: 16)
# CONCURRENT_REQUESTS = 32

# Configure a delay for requests for the same website (default: 0)
# See https://doc.scrapy.org/en/latest/topics/settings.html#download-delay
# See also autothrottle settings and docs
DOWNLOAD_DELAY = 0.5
# The download delay setting will honor only one of:
# CONCURRENT_REQUESTS_PER_DOMAIN = 16
# CONCURRENT_REQUESTS_PER_IP = 16

# Disable cookies (enabled by default)
# COOKIES_ENABLED = False

# Disable Telnet Console (enabled by default)
# TELNETCONSOLE_ENABLED = False

# Override the default request headers:
# DEFAULT_REQUEST_HEADERS = {
#   'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
#   'Accept-Language': 'en',
# }

# Enable or disable spider middlewares
# See https://doc.scrapy.org/en/latest/topics/spider-middleware.html
# SPIDER_MIDDLEWARES = {
#    'douban.middlewares.DoubanSpiderMiddleware': 543,
# }

# Enable or disable downloader middlewares
# See https://doc.scrapy.org/en/latest/topics/downloader-middleware.html
# DOWNLOADER_MIDDLEWARES = {
#     # 'douban.middlewares.DoubanDownloaderMiddleware': 543,
#     'douban.middlewares.ProxyMiddleware': 543,
#     'douban.middlewares.UserAgentMiddleware': 544,
# }

# Enable or disable extensions
# See https://doc.scrapy.org/en/latest/topics/extensions.html
# EXTENSIONS = {
#    'scrapy.extensions.telnet.TelnetConsole': None,
# }

# Configure item pipelines
# See https://doc.scrapy.org/en/latest/topics/item-pipeline.html
# 要开启pipelines选项，否则不会调用pipelines类
# ITEM_PIPELINES = {
#     'douban.pipelines.DoubanPipeline': 300,
#     'douban.pipelines.IpInfoPipeline': 301,
# }

# Enable and configure the AutoThrottle extension (disabled by default)
# See https://doc.scrapy.org/en/latest/topics/autothrottle.html
# AUTOTHROTTLE_ENABLED = True
# The initial download delay
# AUTOTHROTTLE_START_DELAY = 5
# The maximum download delay to be set in case of high latencies
# AUTOTHROTTLE_MAX_DELAY = 60
# The average number of requests Scrapy should be sending in parallel to
# each remote server
# AUTOTHROTTLE_TARGET_CONCURRENCY = 1.0
# Enable showing throttling stats for every response received:
# AUTOTHROTTLE_DEBUG = False

# Enable and configure HTTP caching (disabled by default)
# See https://doc.scrapy.org/en/latest/topics/downloader-middleware.html#httpcache-middleware-settings
# HTTPCACHE_ENABLED = True
# HTTPCACHE_EXPIRATION_SECS = 0
# HTTPCACHE_DIR = 'httpcache'
# HTTPCACHE_IGNORE_HTTP_CODES = []
# HTTPCACHE_STORAGE = 'scrapy.extensions.httpcache.FilesystemCacheStorage'

mysql_host = 'localhost'
mysql_port = 3306
mysql_db_name = 'hibernate'
mysql_table_name = 'douban_movie'
mysql_user = 'root'
mysql_password = 'root123'

PROXIES = ['110.52.235.139:9999', '175.148.75.246:1133']
USER_AGENTS = [
    'MSIE (MSIE 6.0; X11; Linux; i686) Opera 7.23',
    'Opera/9.20 (Macintosh; Intel Mac OS X; U; en)',
    'Opera/9.0 (Macintosh; PPC Mac OS X; U; en)',
    'iTunes/9.0.3 (Macintosh; U; Intel Mac OS X 10_6_2; en-ca)',
    'Mozilla/4.76 [en_jp] (X11; U; SunOS 5.8 sun4u)',
    'iTunes/4.2 (Macintosh; U; PPC Mac OS X 10.2)',
    'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:5.0) Gecko/20100101 Firefox/5.0',
    'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:9.0) Gecko/20100101 Firefox/9.0',
    'Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20120813 Firefox/16.0',
    'Mozilla/4.77 [en] (X11; I; IRIX;64 6.5 IP30)',
    'Mozilla/4.8 [en] (X11; U; SunOS; 5.7 sun4u)'
]


# custom_settings.py
CUSTOM_SETTINGS_FOR_AGENT_IP = {
    'ITEM_PIPELINES': {
        'douban.pipelines.IpInfoPipeline': 301
    }
}

CUSTOM_SETTINGS_FOR_DOUBAN = {
    'ITEM_PIPELINES': {
        # 'douban.pipelines.DoubanPipeline': 300
    },

    'DOWNLOADER_MIDDLEWARES': {
        # 'douban.middlewares.ProxyMiddleware': 543,
        'douban.middlewares.UserAgentMiddleware': 544
    }
}


# items.py
# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# https://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class DoubanItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    # 序号
    serial_number = scrapy.Field()
    # 电影名称
    movie_name = scrapy.Field()
    # 电影介绍
    introduce = scrapy.Field()
    # 星级
    star = scrapy.Field()
    # 电影的评论数
    evaluate = scrapy.Field()
    # 电影的描述
    description = scrapy.Field()


class IpInfoItem(scrapy.Item):
    ip = scrapy.Field()


# middlewares.py
import random

class ProxyMiddleware(object):
    """
    设置Proxy
    """

    def __init__(self, ip):
        self.__ip = ip

    @classmethod
    def from_crawler(cls, crawler):
        return cls(ip=crawler.settings.get('PROXIES'))

    def process_request(self, request, spider):
        ip = random.choice(self.__ip)
        request.meta['proxy'] = ip


class UserAgentMiddleware(object):
    """
    设置User_Agent
    """

    def __init__(self, user_agent):
        self.__user_agent = user_agent

    @classmethod
    def from_crawler(cls, crawler):
        return cls(user_agent=crawler.settings.get('USER_AGENTS'))

    def process_request(self, request, spider):
        request.headers['User_Agent'] = random.choice(self.__user_agent)


# pipelines.py
# -*- coding: utf-8 -*-
import os

import pymysql

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://doc.scrapy.org/en/latest/topics/item-pipeline.html
from douban.settings import mysql_host, mysql_port, mysql_user, mysql_password, mysql_db_name, mysql_table_name


class DoubanPipeline(object):
    __SQL = """
    insert into {0}(serial_number, movie_name, introduce, star, evaluate, description)
        values(%s, %s, %s, %s, %s, %s)
    """.format(mysql_table_name)

    def __init__(self) -> None:
        self.__connect = pymysql.connect(host=mysql_host, port=mysql_port, user=mysql_user, passwd=mysql_password,
                                         db=mysql_db_name, charset='utf8')
        self.__cursor = self.__connect.cursor()

    def process_item(self, item, spider):
        # data = dict(item)
        # print(data)
        self.__cursor.execute(self.__SQL, (
        item['serial_number'], item['movie_name'], item['introduce'], item['star'], item['evaluate'],
        item['description']))
        self.__connect.commit()
        return item


class IpInfoPipeline(object):
    __TXT_PATH = 'IpInfo.txt'

    def __init__(self) -> None:
        if os.path.exists(self.__TXT_PATH):
            os.remove(self.__TXT_PATH)

    def process_item(self, item, spider):
        with open(self.__TXT_PATH, "a") as f:
            f.write(item['ip'] + '\n')
        return item


# spider_agent_ip_main.py
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from scrapy import cmdline

if __name__ == '__main__':
    cmdline.execute('scrapy crawl agent_ip_spider'.split())


# spider_douban_main.py
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from scrapy import cmdline

if __name__ == '__main__':
    cmdline.execute('scrapy crawl douban_spider'.split())


# ip_test_main.py
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import requests

from douban.alive_ip_test import test

if __name__ == '__main__':
    test("IpInfo.txt")

    # proxy_ip = {"http": "175.148.75.246:1133"}
    # r = requests.get("https://movie.douban.com/top250", proxies=proxy_ip, timeout=2)
    # if r.status_code == 200:
    #     print("成功,ip为{}".format(proxy_ip))
    # else:
    #     print("失败")

```

# 爬取华为商场app

在Request中传递参数：

<https://my.oschina.net/u/2396236/blog/1801840>

```python
# app_spider.py
# -*- coding: utf-8 -*-
from urllib.parse import urljoin

import scrapy

from huaweiapp.items import HuaweiappItem


class AppSpiderSpider(scrapy.Spider):
    name = 'app_spider'
    allowed_domains = ['app.hicloud.com']
    start_urls = []

    __APP_URL_PREFIX = 'http://app.hicloud.com'

    def __init__(self):
        for i in range(1, 108):
            self.start_urls.append('http://app.hicloud.com/topics/' + str(i))

    def parse(self, response):
        # 循环Topic
        topic_list = response.xpath("//div[@class='unit-main nofloat']/div/div[2]")
        for topic_node in topic_list:
            topic_name = topic_node.xpath("./h4/a/text()").extract_first()
            topic_url = urljoin(self.__APP_URL_PREFIX, topic_node.xpath("./h4/a/@href").extract_first())
            if topic_name:
                # print(response.url, topic_name, topic_url)
                # app_topic_item = HuaweiappTopicItem()
                # app_topic_item['topic_name'] = topic_name
                # app_topic_item['topic_url'] = topic_url
                yield scrapy.Request(url=topic_url, meta={'topic_name': topic_name, 'topic_url': topic_url},
                                     callback=self.parse_topic_page)

    def parse_topic_page(self, response):
        topic_name = response.meta['topic_name']
        topic_url = response.meta['topic_url']
        print(topic_name, topic_url)

        app_list = response.xpath("//div[@class='nofloat']/div[@class='item-info corner']")
        for app_node in app_list:
            app_name = app_node.xpath("./ul//a/text()").extract_first()
            app_url = urljoin(self.__APP_URL_PREFIX, app_node.xpath("./ul//a/@href").extract_first())
            # print(app_name, app_url)
            if app_name:
                app_item = HuaweiappItem()
                app_item['topic_name'] = topic_name
                app_item['topic_url'] = topic_url
                app_item['app_name'] = app_name
                app_item['app_url'] = app_url
                yield app_item

# items.py
# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# https://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class HuaweiappItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    topic_name = scrapy.Field()
    topic_url = scrapy.Field()
    app_name = scrapy.Field()
    app_url = scrapy.Field()
    pass


# middlewares.py
import random

from scrapy import signals
from scrapy.downloadermiddlewares.useragent import UserAgentMiddleware

class MyUserAgentMiddleware(UserAgentMiddleware):
    '''
    设置User-Agent
    '''

    def __init__(self, user_agent):
        self.user_agent = user_agent

    @classmethod
    def from_crawler(cls, crawler):
        return cls(
            user_agent=crawler.settings.get('USER_AGENTS_LIST')
        )

    def process_request(self, request, spider):
        agent = random.choice(self.user_agent)
        request.headers['User-Agent'] = agent

# pipelines.py
# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://doc.scrapy.org/en/latest/topics/item-pipeline.html


class HuaweiappPipeline(object):
    def process_item(self, item, spider):
        # print(item)
        return item

# spider_main.py
#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from scrapy import cmdline

if __name__ == '__main__':
    cmdline.execute('scrapy crawl app_spider'.split())

# settings.py
# -*- coding: utf-8 -*-

# Scrapy settings for huaweiapp project
#
# For simplicity, this file contains only settings considered important or
# commonly used. You can find more settings consulting the documentation:
#
#     https://doc.scrapy.org/en/latest/topics/settings.html
#     https://doc.scrapy.org/en/latest/topics/downloader-middleware.html
#     https://doc.scrapy.org/en/latest/topics/spider-middleware.html

BOT_NAME = 'huaweiapp'

SPIDER_MODULES = ['huaweiapp.spiders']
NEWSPIDER_MODULE = 'huaweiapp.spiders'


# Crawl responsibly by identifying yourself (and your website) on the user-agent
USER_AGENT = 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36'

USER_AGENTS_LIST = [
    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; AcooBrowser; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; Acoo Browser; SLCC1; .NET CLR 2.0.50727; Media Center PC 5.0; .NET CLR 3.0.04506)",
    "Mozilla/4.0 (compatible; MSIE 7.0; AOL 9.5; AOLBuild 4337.35; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727)",
    "Mozilla/5.0 (Windows; U; MSIE 9.0; Windows NT 9.0; en-US)",
    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 2.0.50727; Media Center PC 6.0)",
    "Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0; WOW64; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 1.0.3705; .NET CLR 1.1.4322)",
    "Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 5.2; .NET CLR 1.1.4322; .NET CLR 2.0.50727; InfoPath.2; .NET CLR 3.0.04506.30)",
    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN) AppleWebKit/523.15 (KHTML, like Gecko, Safari/419.3) Arora/0.3 (Change: 287 c9dfb30)",
    "Mozilla/5.0 (X11; U; Linux; en-US) AppleWebKit/527+ (KHTML, like Gecko, Safari/419.3) Arora/0.6",
    "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.2pre) Gecko/20070215 K-Ninja/2.1.1",
    "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9) Gecko/20080705 Firefox/3.0 Kapiko/3.0",
    "Mozilla/5.0 (X11; Linux i686; U;) Gecko/20070322 Kazehakase/0.4.5",
    "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.8) Gecko Fedora/1.9.0.8-1.fc10 Kazehakase/0.5.6",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/535.20 (KHTML, like Gecko) Chrome/19.0.1036.7 Safari/535.20",
    "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; fr) Presto/2.9.168 Version/11.52",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER",
    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; LBBROWSER)",
    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E; LBBROWSER)",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 LBBROWSER",
    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)",
    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; QQBrowser/7.0.3698.400)",
    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)",
    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SV1; QQDownload 732; .NET4.0C; .NET4.0E; 360SE)",
    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)",
    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E)",
    "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.89 Safari/537.1",
    "Mozilla/5.0 (iPad; U; CPU OS 4_2_1 like Mac OS X; zh-cn) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8C148 Safari/6533.18.5",
    "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:2.0b13pre) Gecko/20110307 Firefox/4.0b13pre",
    "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:16.0) Gecko/20100101 Firefox/16.0",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11",
    "Mozilla/5.0 (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10"
]

# Obey robots.txt rules
ROBOTSTXT_OBEY = True

# Configure maximum concurrent requests performed by Scrapy (default: 16)
#CONCURRENT_REQUESTS = 32

# Configure a delay for requests for the same website (default: 0)
# See https://doc.scrapy.org/en/latest/topics/settings.html#download-delay
# See also autothrottle settings and docs
#DOWNLOAD_DELAY = 3
# The download delay setting will honor only one of:
#CONCURRENT_REQUESTS_PER_DOMAIN = 16
#CONCURRENT_REQUESTS_PER_IP = 16

# Disable cookies (enabled by default)
#COOKIES_ENABLED = False

# Disable Telnet Console (enabled by default)
#TELNETCONSOLE_ENABLED = False

# Override the default request headers:
#DEFAULT_REQUEST_HEADERS = {
#   'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
#   'Accept-Language': 'en',
#}
# DEFAULT_REQUEST_HEADERS = {
#     'accept-language': 'zh-CN,zh;q=0.8',
#     'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36'
# }

# Enable or disable spider middlewares
# See https://doc.scrapy.org/en/latest/topics/spider-middleware.html
#SPIDER_MIDDLEWARES = {
#    'huaweiapp.middlewares.HuaweiappSpiderMiddleware': 543,
#}

# Enable or disable downloader middlewares
# See https://doc.scrapy.org/en/latest/topics/downloader-middleware.html
DOWNLOADER_MIDDLEWARES = {
   # 'huaweiapp.middlewares.HuaweiappDownloaderMiddleware': 543,
   'huaweiapp.middlewares.MyUserAgentMiddleware': 543,
}

# Enable or disable extensions
# See https://doc.scrapy.org/en/latest/topics/extensions.html
#EXTENSIONS = {
#    'scrapy.extensions.telnet.TelnetConsole': None,
#}

# Configure item pipelines
# See https://doc.scrapy.org/en/latest/topics/item-pipeline.html
# ITEM_PIPELINES = {
#    'huaweiapp.pipelines.HuaweiappPipeline': 300,
# }

# Enable and configure the AutoThrottle extension (disabled by default)
# See https://doc.scrapy.org/en/latest/topics/autothrottle.html
#AUTOTHROTTLE_ENABLED = True
# The initial download delay
#AUTOTHROTTLE_START_DELAY = 5
# The maximum download delay to be set in case of high latencies
#AUTOTHROTTLE_MAX_DELAY = 60
# The average number of requests Scrapy should be sending in parallel to
# each remote server
#AUTOTHROTTLE_TARGET_CONCURRENCY = 1.0
# Enable showing throttling stats for every response received:
#AUTOTHROTTLE_DEBUG = False

# Enable and configure HTTP caching (disabled by default)
# See https://doc.scrapy.org/en/latest/topics/downloader-middleware.html#httpcache-middleware-settings
#HTTPCACHE_ENABLED = True
#HTTPCACHE_EXPIRATION_SECS = 0
#HTTPCACHE_DIR = 'httpcache'
#HTTPCACHE_IGNORE_HTTP_CODES = []
#HTTPCACHE_STORAGE = 'scrapy.extensions.httpcache.FilesystemCacheStorage'
```