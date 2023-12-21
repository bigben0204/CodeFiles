#include <cmath>
#include <numeric>
#include "indicator.h"

class IndicatorSMA : public Indicator {
public:
    ~IndicatorSMA() noexcept override = default;

    IndicatorSMA(const DataPtr& data, Period period) : data_(data), period_(period) {}

    void init() override {
        for (int i = 0; i < period_ - 1; ++i) {
            forward(NAN);
        }

        const auto& closeValues = data_->getLine(DataName::CLOSE)->getValues();
        for (int i = 0; i < closeValues.size() - period_ + 1; ++i) {
            auto average = std::accumulate(closeValues.begin() + i, closeValues.begin() + i + period_, 0.0) / period_;
            forward(average);
        }
    }

    Period getPeriod() const override {
        return period_;
    }

private:
    DataPtr data_;
    Period period_;
};

IndicatorPtr Indicator::create(Indicator::Type type, const DataPtr& data, Period period) {
    if (type == Type::SMA) {
        return std::make_unique<IndicatorSMA>(data, period);
    } else if (type == Type::CROSS_OVER) {
        THROW_RUNTIME_ERROR("IndicatorCrossOver is not implemented");
    }
    THROW_RUNTIME_ERROR("unknown indicator type: " + std::to_string(static_cast<int>(type)));
}
