#ifndef BACKTESTING_PROJECT_BACKTEST_H
#define BACKTESTING_PROJECT_BACKTEST_H

#include <memory>
#include "data.h"
#include "strategy.h"
#include "analyzer.h"
#include "broker.h"
#include "sizer.h"
#include "observer.h"

class BackTest {
public:
    virtual ~BackTest() noexcept = default;

    virtual const Analyzers& run() = 0;

    virtual void addData(const DataPtr& data) = 0;

    virtual void addStrategy(const StrategyPtr& strategy) = 0;

    virtual void addSizer(const SizerPtr& sizer) = 0;

    virtual void addAnalyzer(const AnalyzerPtr& analyzer) = 0;

    virtual void addObserver(const ObserverPtr& observer) = 0;

    virtual void showObserverInfo() = 0;

    virtual Broker& getBroker() = 0;

    static std::unique_ptr<BackTest> create();
};

#endif //BACKTESTING_PROJECT_BACKTEST_H
