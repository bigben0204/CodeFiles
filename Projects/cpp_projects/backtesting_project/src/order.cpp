#include "order.h"

class OrderImpl : public Order {
public:
    ~OrderImpl() noexcept override = default;

    OrderImpl(const DataPtr& data, double size, Order::Type type)
        : data_(data),
          dataClose_(data->getLine(DataName::CLOSE)),
          type_(type),
          size_(type_ == Order::Type::BUY ? size : -size),
          created_(OrderData(size_, (*dataClose_)[0], (*dataClose_)[0])),
          executed_(OrderData(size_, (*dataClose_)[0], (*dataClose_)[0])) {
    }

    Type getType() const override {
        return type_;
    }

    const DataPtr& getData() const override {
        return data_;
    }

    void setStatus(Order::Status status) override {
        status_ = status;
    }

    OrderData& createdOrderData() override {
        return created_;
    }

    OrderData& executedOrderData() override {
        return executed_;
    }

    void updateExecutedOrderData(double size, double price, const std::string& dateStr) override {
        executed_.setDate(dateStr);
        executed_.setSize(size);
        executed_.setPrice(price);
    }

private:
    DataPtr data_;
    LinePtr dataClose_;
    Status status_ = Order::Status::SUBMITTED;
    Type type_;
    double size_;
    OrderData created_;
    OrderData executed_;
};

OrderPtr Order::create(const DataPtr& data, double size, Order::Type type) {
    return std::make_shared<OrderImpl>(data, size, type);
}
