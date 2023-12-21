#!/usr/bin/env python3
import argparse
import sys

from src.doris_validater import DorisValidater
from utils.utils import str_to_bool


def parse_args(argv):
    parser = argparse.ArgumentParser(description='进行doris安装完成后基本功能验证')
    parser.add_argument('-i', '--ip', type=str, required=True, default='', help='doris ip')
    parser.add_argument('-u', '--user', type=str, required=False, default='root', help='doris user')
    parser.add_argument('-p', '--password', type=str, required=False, default='', help='doris password')
    parser.add_argument('-P', '--port', type=int, required=False, default=9030, help='doris port')
    parser.add_argument('-c', '--clean', type=str_to_bool, required=False, default=True,
                        help='flag to clean test data, default true')
    args = parser.parse_args(argv)
    return args


if __name__ == '__main__':
    args = parse_args(sys.argv[1:])
    doris_validater = DorisValidater(args)
    result = doris_validater.validate()
    exit(0 if result else 1)
