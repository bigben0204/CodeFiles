#include <iostream>
#include <iomanip>
#include <bits/ios_base.h>
#include "data.h"
#include "backtest.h"
#include "strategy.h"
#include "analyzer.h"
#include "sizer.h"
#include "observer.h"

class MyStrategy : public Strategy {
public:
    ~MyStrategy() noexcept override = default;

    void init() override {
        dataClose_ = datas_[0]->getLine(DataName::CLOSE);
        sma_ = Indicator::create(Indicator::Type::SMA, datas_[0], period_);
        indicators_.push_back(sma_);
    }

    void next() override {
        if (!isPositioned()) {
            if ((*dataClose_)[0] > (*sma_)[0]) {
                buy();
            }
        } else {
            if ((*dataClose_)[0] < (*sma_)[0]) {
                close();
            }
        }
    }

private:
    LinePtr dataClose_;
    IndicatorPtr sma_;
    Period period_ = 15;
};

int main(int argc, char* argv[]) {
    try {
        if (argc != 2) {
            std::cerr << "csv file path should be passed in" << std::endl;
            return EXIT_FAILURE;
        }

        // 读取数据
        auto data = DataGenerator::readFromFile(argv[1]);
        auto backtest = BackTest::create();
        backtest->addData(data);

        // 策略设置
        StrategyPtr myStrategy = std::make_unique<MyStrategy>();
        backtest->addStrategy(myStrategy);

        // 指标分析设置
        backtest->addAnalyzer(Analyzer::create(Analyzer::Type::TOTAL_RETURNS));
        backtest->addAnalyzer(Analyzer::create(Analyzer::Type::TOTAL_ANNUALIZED_RETURNS));
        backtest->addAnalyzer(Analyzer::create(Analyzer::Type::ALGORITHM_VOLATILITY));
        backtest->addAnalyzer(Analyzer::create(Analyzer::Type::SHARPE_RATIO));
        backtest->addAnalyzer(Analyzer::create(Analyzer::Type::DRAW_DOWN));

        // 观察指标设置
        backtest->addObserver(Observer::create(Observer::Type::VALUE));

        // 现金手续费设置
        auto startCash = 100000.0;
        backtest->getBroker().setCash(startCash);
        backtest->getBroker().setCommission(0.0006);

        // 购买比例设置
        backtest->addSizer(Sizer::create(Sizer::Type::PERCENT, 90));

        std::cout << std::setiosflags(std::ios::fixed) << std::setprecision(6) << "初始资金：" << startCash << std::endl;
        const auto& analyzers = backtest->run();
        auto value = backtest->getBroker().getValue();
        std::cout << "剩余资金: " << value << std::endl;

        for (const auto& analyzer: analyzers) {
            std::cout << analyzer->getAnalysis() << std::endl;

            // 获取指标map
            // const auto& analysisMap = analyzer->getAnalysisMap();
        }

        backtest->showObserverInfo();

        return EXIT_SUCCESS;
    } catch (std::exception& e) {
        std::cout << "exception occurs: " << e.what() << std::endl;
        return EXIT_FAILURE;
    }
}