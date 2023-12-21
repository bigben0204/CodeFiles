#!/usr/bin/env python3
import argparse
import subprocess


def str_to_bool(input_value):
    if isinstance(input_value, bool):
        return input_value

    if input_value.lower() in ['yes', 'true', 'y', '1']:
        return True
    elif input_value.lower() in ['no', 'false', 'n', '0']:
        return False
    else:
        raise argparse.ArgumentTypeError('Boolean value expected.')


def exec_cmd(cmd):
    """
    开启子进程，执行对应指令，控制台打印执行过程，然后返回子进程执行的状态码和执行返回的数据
    :param cmd: 子进程命令
    :return: 子进程状态码和执行结果
    """
    p = subprocess.Popen(cmd, shell=True, close_fds=True, stdin=subprocess.PIPE, stdout=subprocess.PIPE,
                         stderr=subprocess.PIPE)
    cmd_stdout = []
    while p.poll() is None:
        line = p.stdout.readline().rstrip()
        if not line:
            continue
        cmd_stdout.append(line)
    last_line = p.stdout.read().rstrip()
    if last_line:
        cmd_stdout.append(last_line)
    cmd_return_code = p.wait()
    return cmd_return_code, b'\n'.join(cmd_stdout), p.stderr.read()

