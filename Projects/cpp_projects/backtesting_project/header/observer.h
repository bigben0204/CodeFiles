#ifndef BACKTESTING_PROJECT_OBSERVER_H
#define BACKTESTING_PROJECT_OBSERVER_H

#include <memory>
#include <vector>
#include "data.h"

class Observer;

using ObserverPtr = std::shared_ptr<Observer>;

class Observer {
public:
    enum class Type {
        VALUE
    };

public:
    virtual ~Observer() noexcept = default;

    virtual void notify(const Datas& data, double currentValue) = 0;

    virtual void stop() = 0;

    virtual void showInfo() const = 0;

    virtual const std::string& getName() const = 0;

    static ObserverPtr create(Type type);
};

using Observers = std::vector<ObserverPtr>;

#endif //BACKTESTING_PROJECT_OBSERVER_H
