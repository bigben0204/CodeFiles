#ifndef BACKTESTING_PROJECT_COMMISSION_INFO_H
#define BACKTESTING_PROJECT_COMMISSION_INFO_H

#include <memory>

class CommissionInfo;
using CommissionInfoPtr = std::shared_ptr<CommissionInfo>;

class CommissionInfo {
public:
    virtual ~CommissionInfo() noexcept = default;

    virtual double getValueSize(double size, double price) = 0;

    virtual double getCommission(double size, double price) = 0;

    virtual double profitAndLoss(double size, double price, double newPrice) = 0;

    static CommissionInfoPtr create(double commission);
};

#endif //BACKTESTING_PROJECT_COMMISSION_INFO_H
