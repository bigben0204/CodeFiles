#ifndef BACKTESTING_PROJECT_STRATEGY_H
#define BACKTESTING_PROJECT_STRATEGY_H

#include <memory>
#include "data.h"
#include "broker.h"
#include "indicator.h"
#include "sizer.h"
#include "analyzer.h"
#include "observer.h"

class Strategy;

using StrategyPtr = std::shared_ptr<Strategy>;
using Strategies = std::vector<StrategyPtr>;

class Strategy : public LineBase<double> {
public:
    virtual ~Strategy() noexcept = default;

    virtual void init() = 0;

    virtual void next() = 0;

public:
    void setData(const Datas& datas);

    void setBroker(const BrokerPtr& broker);

    void setSizer(const SizerPtr& sizer);

    void setAnalyzer(const Analyzers& analyzers);

    void setObserver(const Observers& observers);

    void initChild();

    void initSelf();

    void oncePost(double date);

    const Position& getPosition(const DataPtr& data = nullptr) const;

    bool isPositioned(const DataPtr& data = nullptr) const;

    void buy();

    void sell();

    void close();

    void homeAll();

    void advanceAll();

private:
    void initIndicators();

    Period getMinPeriodStatus() const;

    double getSizing(const DataPtr& data) const;

    void initData();

    void notifyAnalyzers();

    void notifyObservers();

protected:
    Datas datas_;
    BrokerPtr broker_;
    SizerPtr sizer_;
    Analyzers analyzers_;
    Observers observers_;
    Indicators indicators_;
    Period period_ = 1;
};

#endif //BACKTESTING_PROJECT_STRATEGY_H
