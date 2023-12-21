#ifndef BACKTESTING_PROJECT_ORDER_H
#define BACKTESTING_PROJECT_ORDER_H

#include <memory>
#include "data.h"

class OrderData {
public:
    OrderData(double size, double price, double close) : size_(size), price_(price), close_(close) {
    }

    void setDate(const std::string& date) {
        date_ = date;
    }

    void setSize(double size) {
        size_ = size;
    }

    void setPrice(double price) {
        price_ = price;
    }

    void setClose(double close) {
        close_ = close;
    }

    const std::string& getDate() const {
        return date_;
    }

    double getSize() const {
        return size_;
    }

    double getPrice() const {
        return price_;
    }

    double getClose() const {
        return close_;
    }

private:
    std::string date_;
    double size_;
    double price_;
    double close_;
};

class Order;

using OrderPtr = std::shared_ptr<Order>;

class Order {
public:
    enum class Type {
        BUY, SELL
    };

    enum class Status {
        SUBMITTED, COMPLETED, CANCELED
    };
public:
    virtual ~Order() noexcept = default;

    virtual Order::Type getType() const = 0;

    virtual const DataPtr& getData() const = 0;

    virtual void setStatus(Order::Status status) = 0;

    virtual OrderData& createdOrderData() = 0;

    virtual OrderData& executedOrderData() = 0;

    virtual void updateExecutedOrderData(double size, double price, const std::string& dateStr) = 0;

    static OrderPtr create(const DataPtr& data, double size, Order::Type type);
};

#endif //BACKTESTING_PROJECT_ORDER_H
