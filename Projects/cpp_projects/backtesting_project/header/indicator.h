#ifndef BACKTESTING_PROJECT_INDICATOR_H
#define BACKTESTING_PROJECT_INDICATOR_H

#include "data.h"

using Period = int;

class Indicator;

using IndicatorPtr = std::shared_ptr<Indicator>;
using Indicators = std::vector<IndicatorPtr>;

class Indicator : public LineBase<double> {
public:
    enum class Type {
        SMA, CROSS_OVER
    };

public:
    virtual ~Indicator() noexcept = default;

    virtual void init() = 0;

    virtual Period getPeriod() const = 0;

    static IndicatorPtr create(Type type, const DataPtr& data, Period period);
};

#endif //BACKTESTING_PROJECT_INDICATOR_H
