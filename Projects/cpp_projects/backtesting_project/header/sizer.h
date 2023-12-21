#ifndef BACKTESTING_PROJECT_SIZER_H
#define BACKTESTING_PROJECT_SIZER_H

#include <memory>
#include <vector>
#include "data.h"
#include "broker.h"

class Sizer;

using SizerPtr = std::shared_ptr<Sizer>;

class Sizer {
public:
    enum class Type {
        PERCENT
    };

public:
    virtual ~Sizer() noexcept = default;

    virtual double getSizing(const DataPtr& data) const = 0;

    virtual void setBroker(const BrokerPtr& broker) = 0;

    static SizerPtr create(Type type, double percent = 0.0);
};

#endif //BACKTESTING_PROJECT_SIZER_H
