#ifndef BACKTESTING_PROJECT_BROKER_H
#define BACKTESTING_PROJECT_BROKER_H

#include <memory>
#include "data.h"
#include "commission_info.h"

class Position {
public:
    void update(double size, double price, const std::string& dateStr);

    void setAdjbase(double adjbase);

    bool isPositioned() const;

    double getSize() const;

    double getPrice() const;

private:
    std::string dateStr_;
    double adjbase_ = 0.0;
    double price_ = 0.0;
    double priceOrig_ = 0.0;
    double size_ = 0.0;
    double upOpened_ = 0.0;
    double upClosed = 0.0;
};

class Broker;
using BrokerPtr = std::shared_ptr<Broker>;

class Broker {
public:
    virtual ~Broker() noexcept = default;

    virtual void next() = 0;

    virtual void setCash(double cash) = 0;

    virtual double getCash() const = 0;

    virtual void setCommission(double commission) = 0;

    virtual const CommissionInfoPtr& getCommissionInfo(const DataPtr& data) = 0;

    virtual double getValue() = 0;

    virtual bool isPositioned(const DataPtr& data) = 0;

    virtual const Position& getPosition(const DataPtr& data) = 0;

    virtual void buy(const DataPtr& data, double size) = 0;

    virtual void sell(const DataPtr& data, double size) = 0;

    static BrokerPtr create();
};

#endif //BACKTESTING_PROJECT_BROKER_H
