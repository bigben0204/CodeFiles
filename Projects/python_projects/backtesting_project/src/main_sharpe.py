# #!/usr/bin/env python3
# import datetime
# import math
#
# import backtrader as bt
# import backtrader.analyzers as btanalyzers
# import backtrader.indicators as btind
#
#
# # Create a Stratey
# class TestStrategy(bt.Strategy):
#     params = dict(ticker='MSFT')
#
#     def log(self, txt, dt=None):
#         ''' Logging function for this strategy'''
#         dt = dt or self.datas[0].datetime.date(0)
#         print('%s, %s' % (dt.isoformat(), txt))
#
#     def __init__(self):
#         # Keep a reference to the "close" line in the data[0] dataseries
#         self.dataclose = self.datas[0].close
#
#         self.ma50 = btind.SMA(self.data.close, period=50, plotname='50-day MA')
#         self.ma200 = btind.SMA(self.data.close, period=200, plotname='200-day MA')
#
#         self.crossover = btind.CrossOver(self.ma50, self.ma200)
#
#     def next(self):
#         # if we don't own shares of the security
#         # and ma50 crossed above ma200
#         if (self.position.size == 0) & (self.crossover > 0):
#             # buy
#             dollars_to_invest = self.broker.cash
#             # Round down to nearest integer to calculate number of shares.
#             # Assume I can buy at the closing price
#             self.size = math.floor(dollars_to_invest / self.data.close)
#             print(f'Buy {self.size} shares of {self.params.ticker} at ${self.data.close[0]} per share.')
#             self.buy(size=self.size)
#
#         # if we own shares of the security
#         # and ma50 crossed below ma200
#         if (self.position.size > 0) & (self.crossover < 0):
#             print(f'Sell {self.size} shares of {self.params.ticker} at ${self.data.close[0]} per share.')
#             self.close()
#
#
# if __name__ == '__main__':
#     cerebro = bt.Cerebro()
#     cerebro.broker.set_cash(100000.0)
#
#     MSFT = bt.feeds.YahooFinanceData(
#         dataname='MSFT',
#         fromdate=datetime.datetime(2020, 7, 1),
#         todate=datetime.datetime(2020, 7, 31),
#         reverse=False
#     )
#
#     print(f'MSFT data: {MSFT}')
#
#     cerebro.adddata(MSFT)
#
#     cerebro.addstrategy(TestStrategy)
#
#     cerebro.addanalyzer(btanalyzers.SharpeRatio, _name='SharpeRatio')
#
#     strategies = cerebro.run()
#
#     print('Sharpe Ratio:', strategies[0].analyzers.SharpeRatio.get_analysis())
#
#     cerebro.plot(width=28, height=28)[0][0]
