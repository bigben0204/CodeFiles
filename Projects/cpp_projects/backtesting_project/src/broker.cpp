#include <deque>
#include <unordered_map>
#include "broker.h"
#include "order.h"
#include "commission_info.h"
#include "constant.h"

class BrokerImpl : public Broker {
public:
    ~BrokerImpl() noexcept override = default;

    void setCash(double cash) override {
        value_ = cash_ = cash;
    }

    double getCash() const override {
        return cash_;
    }

    void setCommission(double commission) override {
        commissionInfo_ = CommissionInfo::create(commission);
    }

    const CommissionInfoPtr& getCommissionInfo(const DataPtr& data) override {
        return commissionInfo_;
    }

    double getValue() override {
        double posValue = DoubleZero;
        double unrealized = DoubleZero;
        for (const auto& dataPositionsPair: dataPositionsMap_) {
            const auto& [data, position] = dataPositionsPair;

            auto closePrice = data->getTickValue(DataName::CLOSE);
            double dvalue = commissionInfo_->getValueSize(position.getSize(), closePrice);

            double dunrealized = commissionInfo_->profitAndLoss(position.getSize(), position.getPrice(), closePrice);

            posValue += dvalue;
            unrealized += dunrealized;

            value_ = cash_ + posValue;
            unrealized_ = unrealized;
        }
        return value_;
    }

    void next() override {
        executeOrders();
    }

    bool isPositioned(const DataPtr& data) override {
        return getPosition(data).isPositioned();
    }

    const Position& getPosition(const DataPtr& data) override {
        auto it = dataPositionsMap_.find(data);
        if (it != dataPositionsMap_.end()) {
            return (*it).second;
        }

        return dataPositionsMap_[data];
    }

    void buy(const DataPtr& data, double size) override {
        const auto& order = Order::create(data, size, Order::Type::BUY);
        submit(order);
    }

    void sell(const DataPtr& data, double size) override {
        const auto& order = Order::create(data, size, Order::Type::SELL);
        submit(order);
    }

private:
    void submit(const OrderPtr& order) {
        submittedOrders_.push_back(order);
        orders_.push_back(order);
    }

    void executeOrders() {
        while (!submittedOrders_.empty()) {
            OrderPtr order = submittedOrders_.front();
            submittedOrders_.pop_front();

            bool tryExecuteRet = execute(cash_, order);
            getValue();
        }
    }

    bool execute(double currentCash, const OrderPtr& order) {
        double leftCash = currentCash;

        const auto& data = order->getData();
        Position& position = dataPositionsMap_[data];

        double size = order->executedOrderData().getSize();
        double openPrice = (*data->getLine(DataName::OPEN))[0];

        if (order->getType() == Order::Type::BUY) {
            double openCash = commissionInfo_->getValueSize(size, openPrice);

            leftCash -= openCash;

            double openedComm = commissionInfo_->getCommission(size, openPrice);
            leftCash -= openedComm;

            // 现金不够，无法执行买入
            if (leftCash < DoubleZero) {
                order->setStatus(Order::Status::CANCELED);
                return false;
            }

            // 现金够，执行买入操作
            cash_ = leftCash;
        } else {
            double priceOrig = position.getPrice();
            double pnl = commissionInfo_->profitAndLoss(-size, priceOrig, openPrice);
            double cash = cash_;

            double closeValue = commissionInfo_->getValueSize(-size, priceOrig);
            double closeCash = closeValue;
            cash += closeCash + pnl;

            double closedComm = commissionInfo_->getCommission(size, openPrice);
            cash -= closedComm;
            cash_ = cash;
        }

        position.setAdjbase((*data->getLine(DataName::CLOSE))[0]);
        position.update(size, openPrice, timeStamp2Str(data->getDatatime(0)));
        order->updateExecutedOrderData(size, openPrice, timeStamp2Str(data->getDatatime(0)));
        order->setStatus(Order::Status::COMPLETED);

        return true;
    }

private:
    double cash_;
    double value_;
    double unrealized_;
    CommissionInfoPtr commissionInfo_;
    std::unordered_map<DataPtr, Position> dataPositionsMap_;
    std::deque<OrderPtr> submittedOrders_;
    std::vector<OrderPtr> orders_;
};

BrokerPtr Broker::create() {
    return std::make_unique<BrokerImpl>();
}

void Position::update(double size, double price, const std::string& dateStr) {
    dateStr_ = dateStr;
    priceOrig_ = price_;
    double oldSize = size_;
    std::isnan(size_) ? size_ = size : size_ += size;

    double opened = DoubleZero, closed = DoubleZero;
    if (equalFloats(size_, DoubleZero)) {
        opened = 0;
        closed = size;
        price_ = DoubleZero;
    }
    if (equalFloats(oldSize, DoubleZero)) {
        opened = size;
        closed = 0;
        price_ = price;
    }

    upOpened_ = opened;
    upClosed = closed;
}

void Position::setAdjbase(double adjbase) {
    adjbase_ = adjbase;
}

bool Position::isPositioned() const {
    return !equalFloats(size_, DoubleZero);
}

double Position::getSize() const {
    return size_;
}

double Position::getPrice() const {
    return price_;
}

