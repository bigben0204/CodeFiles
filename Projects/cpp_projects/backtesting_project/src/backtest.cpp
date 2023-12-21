#include <algorithm>
#include <cmath>
#include "backtest.h"
#include "broker.h"

class BackTestImpl : public BackTest {
public:
    ~BackTestImpl() noexcept override = default;

    explicit BackTestImpl() : broker_(Broker::create()) {}

    const Analyzers& run() override {
        initStrategies();
        homeAll();
        runStrategies();
        stopAnalyzers();
        stopObservers();
        return analyzers_;
    }

    void addData(const DataPtr& data) override {
        datas_.push_back(data);
    }

    void addStrategy(const StrategyPtr& strategy) override {
        strategies_.push_back(strategy);
    }

    void addSizer(const SizerPtr& sizer) override {
        sizer_ = sizer;
    }

    void addAnalyzer(const AnalyzerPtr& analyzer) override {
        analyzers_.push_back(analyzer);
    }

    void addObserver(const ObserverPtr& observer) override {
        observers_.push_back(observer);
    }

    void showObserverInfo() override {
        for (const auto& observer : observers_) {
            observer->showInfo();
        }
    }

    Broker& getBroker() override {
        return *broker_;
    }

private:
    void homeAll() {
        for (const auto& data: datas_) {
            data->home();
        }
        for (const auto& strategy: strategies_) {
            strategy->homeAll();
        }
    }

    void initStrategies() {
        std::for_each(strategies_.begin(), strategies_.end(), [&](const StrategyPtr& strategy) {
            strategy->setData(this->datas_);
            strategy->setBroker(broker_);
            strategy->setSizer(sizer_);
            strategy->setAnalyzer(analyzers_);
            strategy->setObserver(observers_);
            strategy->initChild();
            strategy->initSelf();
        });
    }

    void runStrategies() {
        while (true) {
            advanceDatas();
            const auto& nextDates = getDates();
            auto minDate = *std::min_element(nextDates.begin(), nextDates.end());
            if (std::isnan(minDate)) {
                break;
            }

            executeBroker();
            onceStrategies(minDate);
        }
    }

    std::vector<double> getDates() const {
        std::vector<double> dates(datas_.size());
        for (int i = 0; i < datas_.size(); ++i) {
            dates[i] = datas_.at(i)->getDatatime(0);
        }
        return dates;
    }

    void advanceDatas() {
        for (const auto& data: datas_) {
            data->advance();
        }
        for (const auto& strategy: strategies_) {
            strategy->advanceAll();
        }
    }

    void executeBroker() {
        broker_->next();
    }

    void onceStrategies(double date) {
        for (const auto& strategy: strategies_) {
            strategy->oncePost(date);
        }
    }

    void stopAnalyzers() {
        for (const auto& analyzer: analyzers_) {
            analyzer->stop();
        }
    }

    void stopObservers() {
        for (const auto& observer: observers_) {
            observer->stop();
        }
    }

private:
    Datas datas_;
    BrokerPtr broker_;
    SizerPtr sizer_;
    Analyzers analyzers_;
    Strategies strategies_;
    Observers observers_;
};

std::unique_ptr<BackTest> BackTest::create() {
    return std::make_unique<BackTestImpl>();
}
