#include "strategy.h"

void Strategy::setData(const Datas& datas) {
    datas_ = datas;
}

void Strategy::setBroker(const BrokerPtr& broker) {
    broker_ = broker;
}

void Strategy::setSizer(const SizerPtr& sizer) {
    sizer_ = sizer;
    sizer_->setBroker(broker_);
}

void Strategy::setAnalyzer(const Analyzers& analyzers) {
    analyzers_ = analyzers;
}

void Strategy::setObserver(const Observers& observers) {
    observers_ = observers;
}

void Strategy::initChild() {
    init();
}

void Strategy::initSelf() {
    initData();
    initIndicators();
}

void Strategy::initIndicators() {
    for (const auto& indicator: indicators_) {
        indicator->init();
        period_ = std::max(period_, indicator->getPeriod());
    }
}

void Strategy::oncePost(double date) {
    at(0) = date;
    auto minPeriodStatus = getMinPeriodStatus();
    if (minPeriodStatus <= 0) {
        next();
    }
    notifyAnalyzers();
    notifyObservers();
}

const Position& Strategy::getPosition(const DataPtr& data/* = nullptr*/) const {
    if (data.get() == nullptr) {
        broker_->isPositioned(datas_[0]);
    }
    return broker_->getPosition(data);
}

Period Strategy::getMinPeriodStatus() const {
    Period minPeriodStatus = std::numeric_limits<Period>::min();
    for (const auto& data: datas_) {
        minPeriodStatus = std::max<Period>(minPeriodStatus, period_ - data->length());
    }
    return minPeriodStatus;
}

bool Strategy::isPositioned(const DataPtr& data/* = nullptr*/) const {
    if (data.get() == nullptr) {
        return broker_->isPositioned(datas_[0]);
    }
    return broker_->isPositioned(data);
}

void Strategy::buy() {
    double size = getSizing(datas_[0]);
    if (size > 0) {
        broker_->buy(datas_[0], size);
    }
}

void Strategy::sell() {
    close();
}

void Strategy::close() {
    double posSize = getPosition(datas_[0]).getSize();
    if (posSize > 0) {
        broker_->sell(datas_[0], posSize);
    }
}

double Strategy::getSizing(const DataPtr& data) const {
    return sizer_->getSizing(data);
}

void Strategy::homeAll() {
    home();
    for (const auto& indicator: indicators_) {
        indicator->home();
    }
}

void Strategy::advanceAll() {
    advance();
    for (const auto& indicator: indicators_) {
        indicator->advance();
    }
}

void Strategy::initData() {
    forward(NAN, datas_[0]->getLine(DataName::CLOSE)->bufLength());
}

void Strategy::notifyAnalyzers() {
    for (const auto& analyzer: analyzers_) {
        analyzer->notify(datas_, broker_->getValue());
    }
}

void Strategy::notifyObservers() {
    for (const auto& observer: observers_) {
        observer->notify(datas_, broker_->getValue());
    }
}

