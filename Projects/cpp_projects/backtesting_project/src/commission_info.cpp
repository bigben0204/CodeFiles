#include <complex>
#include "commission_info.h"

class CommissionInfoImpl : public CommissionInfo {
public:
    ~CommissionInfoImpl() noexcept override = default;

    CommissionInfoImpl(double commission) : commission_(commission) {}

    double getValueSize(double size, double price) override {
        return size * price;
    }

    double getCommission(double size, double price) override {
        return std::abs(size) * commission_ * price;
    }

    double profitAndLoss(double size, double price, double newPrice) override {
        return size * (newPrice - price);
    }

private:
    double commission_;
};

CommissionInfoPtr CommissionInfo::create(double commission) {
    return std::make_shared<CommissionInfoImpl>(commission);
}
