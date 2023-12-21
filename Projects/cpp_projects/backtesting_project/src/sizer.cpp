#include "utils.h"
#include "sizer.h"

class PercentSizer : public Sizer {
public:
    ~PercentSizer() noexcept override = default;

    PercentSizer(double percent) : percent_(percent) {}

    double getSizing(const DataPtr& data) const override {
        const auto& commInfo = broker_->getCommissionInfo(data);
        return getSizing(commInfo, broker_->getCash(), data);
    }

    void setBroker(const BrokerPtr& broker) override {
        broker_ = broker;
    }

private:
    double getSizing(const CommissionInfoPtr& commInfo, double cash, const DataPtr& data) const {
        const auto& position = broker_->getPosition(data);
        double size = 0;
        if (!position.isPositioned()) {
            size = cash / (*data->getLine(DataName::CLOSE))[0] * (percent_ / 100);
        } else {
            size = position.getSize();
        }
        return size;
    }

private:
    BrokerPtr broker_;
    double percent_;
};

SizerPtr Sizer::create(Sizer::Type type, double percent) {
    if (type == Type::PERCENT) {
        return std::make_shared<PercentSizer>(percent);
    }
    THROW_RUNTIME_ERROR("unknown sizer type: " + std::to_string(static_cast<int>(type)));
}
