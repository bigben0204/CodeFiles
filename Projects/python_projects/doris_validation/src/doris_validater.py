#!/usr/bin/env python3
import datetime

import pymysql
from pymysql.constants import CLIENT
from utils.utils import exec_cmd


class DorisValidater:
    def __init__(self, args):
        self._args = args
        self._db, self._cursor = self._init(args)

    @staticmethod
    def _init(args):
        try:
            db = pymysql.connect(host=args.ip, port=args.port, user=args.user, password=args.password,
                                 client_flag=CLIENT.MULTI_STATEMENTS)
            cursor = db.cursor()
            return db, cursor
        except Exception as e:
            print(f'failed to connect db: {e}')
            return None, None

    def validate(self):
        if not self._db:
            return False

        try:
            self._execute_sql_by_file('conf/init.sql')

            # insert data
            if not self._validate_insert_data():
                return False

            # curl data
            if not self._validate_curl_data():
                return False

            return True
        finally:
            self._clean()
            self._close()

    def _close(self):
        if self._cursor:
            self._cursor.close()
        if self._db:
            self._db.close()

    def _execute_sql_by_file(self, file_path):
        sql = self._get_init_sql(file_path)
        self._exec_commit_sql(sql)

    def _get_init_sql(self, file_path):
        with open(file_path, encoding='utf-8') as f:
            init_sql = f.read()
            return init_sql

    def _validate_result(self, expect_result, scene):
        try:
            result = self._exec_fetch_sql('select * from demo.example_tbl order by user_id, date;')

            if result == expect_result:
                print(f'doris validate {scene} successfully.')
                return True
            else:
                print(
                    f'doris validate {scene} failed.\nExpected value of: "select * from demo.example_tbl" is:\n {expect_result}\nActual value is:\n {result}')
                return False
        finally:
            self._exec_commit_sql('truncate table demo.example_tbl;')

    def _exec_fetch_sql(self, sql_cmd):
        self._cursor.execute(sql_cmd)
        return self._cursor.fetchall()

    def _exec_commit_sql(self, sql_cmd):
        try:
            self._cursor.execute(sql_cmd)
            self._db.commit()
            return True
        except Exception as e:
            self._db.rollback()
            print(f'failed to exec cmd: {sql_cmd}, error: {e}')
            return False

    def _clean(self):
        if not self._args.clean:
            return True
        return self._exec_commit_sql('drop database demo;')

    def _curl_data(self):
        cmd = f'curl  --location-trusted -u {self._args.user}:{self._args.password} -T conf/test.csv -H "column_separator:," http://{self._args.ip}:8030/api/demo/example_tbl/_stream_load'
        ret_code, stdout, stderr = exec_cmd(cmd)
        if ret_code != 0:
            print(f'failed to curl data, cmd: {cmd}, stdout: {stdout}, stderr: {stderr}')

    def _validate_insert_data(self):
        expect_result = (
            ('10000', datetime.date(2017, 10, 1), '北京', 20, 0, datetime.datetime(2017, 10, 1, 7, 0), 35, 10, 2),
            ('10001', datetime.date(2017, 10, 1), '北京', 30, 1, datetime.datetime(2017, 10, 1, 17, 5, 45), 2, 22,
             22))
        self._execute_sql_by_file('conf/insert.sql')
        return self._validate_result(expect_result, 'insert')

    def _validate_curl_data(self):
        expect_result = (
            ('10000', datetime.date(2017, 10, 1), '北京', 20, 0, datetime.datetime(2017, 10, 1, 7, 0), 35, 10, 2),
            ('10001', datetime.date(2017, 10, 1), '北京', 30, 1, datetime.datetime(2017, 10, 1, 17, 5, 45), 2, 22, 22),
            ('10002', datetime.date(2017, 10, 2), '上海', 20, 1, datetime.datetime(2017, 10, 2, 12, 59, 12), 200, 5, 5),
            ('10003', datetime.date(2017, 10, 2), '广州', 32, 0, datetime.datetime(2017, 10, 2, 11, 20), 30, 11, 11),
            ('10004', datetime.date(2017, 10, 1), '深圳', 35, 0, datetime.datetime(2017, 10, 1, 10, 0, 15), 100, 3, 3),
            ('10004', datetime.date(2017, 10, 3), '深圳', 35, 0, datetime.datetime(2017, 10, 3, 10, 20, 22), 11, 6, 6))
        self._curl_data()
        return self._validate_result(expect_result, 'curl')
