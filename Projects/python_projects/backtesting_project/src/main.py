#!/usr/bin/env python3
import os.path
import sys
from datetime import datetime

import backtrader as bt
import pandas as pd
import tushare as ts


def get_data(code, starttime, endtime, data_file):
    df = load_data(data_file)
    if df is None:
        df = ts.get_k_data(code, start=starttime, end=endtime)
        save_data(df, data_file)
    df.index = pd.to_datetime(df.date)
    df['openinterest'] = 0
    # 对df的数据列进行整合
    df = df[['open', 'high', 'low', 'close', 'volume', 'openinterest']]
    return df


def save_data(df, data_file):
    df.to_pickle(data_file)


def load_data(data_file):
    if os.path.exists(data_file):
        return pd.read_pickle(data_file)
    return None


# 2. 构建策略
# 上穿20日线买入，跌穿20日均线卖出
class MyStrategy(bt.Strategy):
    params = (
        ('maperiod', 15),
    )

    def __init__(self):
        self.dataclose = self.data0.close
        self.order = None
        self.buyprice = None
        self.buycomm = None
        self.sma = bt.indicators.MovingAverageSimple(self.data0, period=self.params.maperiod)
        # self.count_next = 0

    # 每个bar都会执行一次，回测的每个日期都会执行一次
    def next(self):
        if not self.position:
            if self.dataclose[0] > self.sma[0]:
                self.buy()
        else:
            if self.dataclose[0] < self.sma[0]:
                self.close()

    def notify_order(self, order):
        if order.status in [order.Submitted, order.Accepted]:
            return
        if order.status in [order.Completed]:
            if order.isbuy():
                self.log('BUY EXECUTED: Price: %.2f, Cost: %.2f, Comm: %.2f' %
                         (order.executed.price,
                          order.executed.value,
                          order.executed.comm
                          ))
                self.buyprice = order.executed.price
                self.buycomm = order.executed.comm
            else:
                self.log('SELL EXECUTED: Price: %.2f, Cost: %.2f, Comm: %.2f' %
                         (order.executed.price,
                          order.executed.value,
                          order.executed.comm
                          ))
            self.bar_executed = len(self)
        elif order.status in [order.Canceled, order.Margin, order.Rejected]:
            self.log('Order Canceled/Margin/Rejected')
        self.order = None

    def notify_trade(self, trade):
        if not trade.isclosed:
            return
        self.log('OPERATION PROFIT, GROSS: %.2f, NET: %.2f' % (trade.pnl, trade.pnlcomm))

    def log(self, txt, dt=None, doprint=False):
        if doprint:
            dt = dt or self.datas[0].datetime.date(0)
            print('%s, %s' % (dt.isoformat(), txt))


def get_date(date_str, date_format="%Y-%m-%d"):
    return datetime.strptime(date_str, date_format)


if __name__ == '__main__':
    data_file = sys.argv[1]
    from_date_str = sys.argv[2]
    to_date_str = sys.argv[3]

    fromdate = get_date(from_date_str)
    todate = get_date(to_date_str)
    code = '600519'

    # 1. 数据加载
    stock_df = get_data(code, from_date_str, to_date_str, data_file)
    print(f'stock_df: {stock_df}')
    stock_df.to_csv(f'{data_file}.csv')
    # 加载并读取数据源
    data = bt.feeds.PandasData(dataname=stock_df, fromdate=fromdate, todate=todate)

    # 3. 策略设置
    cerebro = bt.Cerebro(stdstats=True)  # 创建大脑
    # 将数据加入回测系统
    cerebro.adddata(data, name='maotai')
    # 加入自己的策略
    cerebro.addstrategy(MyStrategy)

    cerebro.addanalyzer(bt.analyzers.SharpeRatio)
    cerebro.addanalyzer(bt.analyzers.DrawDown)  # 回撤

    # 经纪人
    start_cash = 100000
    cerebro.broker.setcash(start_cash)
    # 设置手续费
    cerebro.broker.setcommission(0.0006)

    cerebro.addsizer(bt.sizers.PercentSizer, percents=90)

    # 4. 执行回测
    print(f'初始资金：{start_cash}\n回测时间：{from_date_str}~{to_date_str}')
    res = cerebro.run()[0]
    port_val = cerebro.broker.getvalue()
    print(f'剩余资金：{port_val}\n回测时间：{from_date_str}~{to_date_str}')

    # print('SharpeRatio:', res.analyzers.sharperatio.get_analysis()['sharperatio'])
    # print('DrawDown:', res.analyzers.drawdown.get_analysis()['drawdown'])
    # print('SharpeRatio:', res.analyzers.sharperatio.get_analysis())
    # print('DrawDown:', res.analyzers.drawdown.get_analysis())

    cerebro.plot(style='candle')


