#!/usr/bin/env python3
import unittest

import pymysql
from pymysql.constants import CLIENT
from utils.utils import exec_cmd


class TestValidation(unittest.TestCase):
    def test1(self):
        # 创建数据库连接对象
        db = pymysql.connect(host='122.51.228.220',
                             port=9030,
                             user='root',
                             password='')
        # 创建游标对象
        cursor  = db.cursor()

        sql = "show databases"
        try:
            cursor.execute(sql)  # 执行sql语句，也可执行数据库命令，如：show tables
            result = cursor.fetchall()  # 所有结果
            print(result)
        except Exception as e:
            db.rollback()
            print("查询失败")
            print(e)
        finally:
            cursor.close()  # 关闭当前游标
            db.close()  # 关闭数据库连接

    def test2(self):
        # 创建数据库连接对象
        db = pymysql.connect(host='122.51.228.220',
                             port=9030,
                             user='root',
                             password='',
                             client_flag=CLIENT.MULTI_STATEMENTS)
        # 创建游标对象
        cursor = db.cursor()

        sql = '''
create database if not exists demo;

use demo;

CREATE TABLE IF NOT EXISTS demo.example_tbl
(
    `user_id` LARGEINT NOT NULL COMMENT "用户id",
    `date` DATE NOT NULL COMMENT "数据灌入日期时间",
    `city` VARCHAR(20) COMMENT "用户所在城市",
    `age` SMALLINT COMMENT "用户年龄",
    `sex` TINYINT COMMENT "用户性别",
    `last_visit_date` DATETIME REPLACE DEFAULT "1970-01-01 00:00:00" COMMENT "用户最后一次访问时间",
    `cost` BIGINT SUM DEFAULT "0" COMMENT "用户总消费",
    `max_dwell_time` INT MAX DEFAULT "0" COMMENT "用户最大停留时间",
    `min_dwell_time` INT MIN DEFAULT "99999" COMMENT "用户最小停留时间"
)
AGGREGATE KEY(`user_id`, `date`, `city`, `age`, `sex`)
DISTRIBUTED BY HASH(`user_id`) BUCKETS 1
PROPERTIES (
    "replication_allocation" = "tag.location.default: 1"
);

truncate table demo.example_tbl;

insert into demo.example_tbl(user_id, date, city, age, sex, last_visit_date, cost, max_dwell_time, min_dwell_time) 
    values(10000,'2017-10-01','北京',20,0,'2017-10-01 06:00:00',20,10,10);
insert into demo.example_tbl(user_id, date, city, age, sex, last_visit_date, cost, max_dwell_time, min_dwell_time) 
    values(10000,'2017-10-01','北京',20,0,'2017-10-01 07:00:00',15,2,2);
insert into demo.example_tbl(user_id, date, city, age, sex, last_visit_date, cost, max_dwell_time, min_dwell_time) 
    values(10001,'2017-10-01','北京',30,1,'2017-10-01 17:05:45',2,22,22);
'''

        try:
            cursor.execute(sql)  # 执行sql语句，也可执行数据库命令，如：show tables
            db.commit()

            cursor.execute('select * from demo.example_tbl order by user_id;')
            result = cursor.fetchall()  # 所有结果
            print(result)
            self.assertTrue(True)
        except Exception as e:
            db.rollback()
            print("查询失败")
            print(e)
            self.assertTrue(False)

        finally:
            cursor.close()  # 关闭当前游标
            db.close()  # 关闭数据库连接


    def test3(self):
        cmd = 'curl  --location-trusted -u root: -T ../conf/test.csv -H "column_separator:," http://192.168.93.1281:8030/api/demo/example_tbl/_stream_load'
        print(exec_cmd(cmd))

if __name__ == '__main__':
    unittest.main()
