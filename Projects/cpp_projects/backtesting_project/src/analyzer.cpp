#include <stdexcept>
#include <numeric>
#include <sstream>
#include <unordered_map>
#include "analyzer.h"
#include "utils.h"
#include "data.h"
#include "constant.h"

class AnalyzerDrawDown : public Analyzer {
public:
    virtual ~AnalyzerDrawDown() noexcept override = default;

    void notify(const Datas& datas, double currentValue) override {
        dates_.push_back(datas[0]->getDatatime(0));
        values_.push_back(currentValue);
    }

    const std::string& getName() const override {
        static const std::string name = "最大回撤（DrawDown）";
        return name;
    }

    void stop() override {
        calcMaxDrawDown();
        setAnalysisMap();
    }

    std::string getAnalysis() override {
        return getName() + ": " + getAnalysisMsg();
    }

private:
    void calcMaxDrawDown() {
        double tmpValueMin;
        double tmpValueMinDate;

        double maxDrawDown = DoubleZero;
        std::vector<double> dp(values_.size(), DoubleZero);
        for (int i = values_.size() - 2; i >= 0; --i) {
            if (dp[i + 1] > DoubleZero) {
                dp[i] = dp[i + 1] + values_[i] - values_[i + 1];
            } else {
                dp[i] = values_[i] - values_[i + 1];
                tmpValueMin = values_[i + 1];
                tmpValueMinDate = dates_[i + 1];
            }

            if (dp[i] > maxDrawDown) {
                maxDrawDown = dp[i];
                valueMax_ = values_[i];
                valueMin_ = tmpValueMin;
                valueMaxDate_ = dates_[i];
                valueMinDate_ = tmpValueMinDate;
            }
        }
    }

    void setAnalysisMap() {
        analysisMap_ = {
            {MaxDrawDownRatioStr, (valueMax_ - valueMin_) / valueMax_},
            {MaxValueDownStr, (valueMax_ - valueMin_)},
            {FromDateStr, valueMaxDate_},
            {ToDateStr, valueMinDate_}
        };
    }

    std::string getAnalysisMsg() {
        std::ostringstream oss;
        oss << MaxDrawDownRatioStr << ": " << analysisMap_[MaxDrawDownRatioStr] << ", "
            << MaxValueDownStr << ": " << analysisMap_[MaxValueDownStr] << ", "
            << FromDateStr << ": " << timeStamp2Str(valueMaxDate_) << ", "
            << ToDateStr << ": " << timeStamp2Str(valueMinDate_);
        return oss.str();
    }

private:
    double valueMax_;
    double valueMin_;
    double valueMaxDate_;
    double valueMinDate_;
    std::vector<double> dates_;
    std::vector<double> values_;
};

class AnalyzerTotalReturns : public Analyzer {
public:
    ~AnalyzerTotalReturns() noexcept override = default;

    void notify(const Datas& datas, double currentValue) override {
        if (equalFloats(valueStart_, DoubleZero)) {
            valueStart_ = currentValue;
        } else {
            valueEnd_ = currentValue;
        }
    }

    const std::string& getName() const override {
        static const std::string name = "策略收益（TotalReturn）";
        return name;
    }

    void stop() override {
        analysisMap_ = {{TotalReturnsStr, getTotalReturns()}};
    }

    std::string getAnalysis() override {
        auto totalReturns = analysisMap_[TotalReturnsStr];
        return getName() + ": " + std::to_string(totalReturns * 100) + "%";
    }

public:
    double getTotalReturns() const {
        return (valueEnd_ - valueStart_) / valueStart_;
    }

private:
    double valueStart_ = DoubleZero;
    double valueEnd_ = DoubleZero;
};

class AnalyzerTotalAnnualizedReturns : public Analyzer {
public:
    ~AnalyzerTotalAnnualizedReturns() noexcept override = default;

    void notify(const Datas& datas, double currentValue) override {
        analyzerTotalReturns_.notify(datas, currentValue);
        ++days_;
    }

    const std::string& getName() const override {
        static const std::string name = "策略年化收益（TotalAnnualizedReturns）";
        return name;
    }

    void stop() override {
        analyzerTotalReturns_.stop();
        analysisMap_ = {{TotalAnnualizedReturnsStr, getTotalAnnualizedReturns()}};
    }

    std::string getAnalysis() override {
        auto totalAnnualizedReturns = analysisMap_[TotalAnnualizedReturnsStr];
        return getName() + ": " + std::to_string(totalAnnualizedReturns * 100) + "%";
    }

public:
    double getTotalAnnualizedReturns() const {
        double totalReturns = analyzerTotalReturns_.getTotalReturns();
        return pow(1 + totalReturns, static_cast<double>(250) / days_) - 1;
    }

private:
    AnalyzerTotalReturns analyzerTotalReturns_;
    int days_ = 0;
};

class AnalyzerAlgorithmVolatility : public Analyzer {
public:
    ~AnalyzerAlgorithmVolatility() noexcept override = default;

    void notify(const Datas& datas, double currentValue) override {
        valueStart_ = valueEnd_;
        valueEnd_ = currentValue;
        ++days_;
        if (days_ >= 2) {
            daysReturns_.push_back(getDayReturns());
        }
    }

    const std::string& getName() const override {
        static const std::string name = "策略波动率（AlgorithmVolatility）";
        return name;
    }

    void stop() override {
        analysisMap_ = {{AlgorithmVolatilityStr, getAlgorithmVolatility()}};
    }


    std::string getAnalysis() override {
        auto algorithmVolatility = analysisMap_[AlgorithmVolatilityStr];
        return getName() + ": " + std::to_string(algorithmVolatility);
    }

public:
    double getAlgorithmVolatility() const {
        double average = std::accumulate(daysReturns_.begin(), daysReturns_.end(), DoubleZero) / days_;

        double delta2Sum = DoubleZero;
        for (const auto& dayReturns: daysReturns_) {
            delta2Sum += pow(dayReturns - average, 2.0);
        }
        return sqrt(static_cast<double>(250) / (days_ - 1) * delta2Sum);
    }

private:
    double getDayReturns() const {
        return (valueEnd_ - valueStart_) / valueStart_ * 100;
    }

private:
    double valueStart_ = DoubleZero;
    double valueEnd_ = DoubleZero;
    int days_ = 0;
    std::vector<double> daysReturns_;
};

class AnalyzerSharpeRatio : public Analyzer {
public:
    virtual ~AnalyzerSharpeRatio() noexcept override = default;

    void notify(const Datas& datas, double currentValue) override {
        analyzerTotalAnnualizedReturns_.notify(datas, currentValue);
        analyzerAlgorithmVolatility_.notify(datas, currentValue);
    }

    const std::string& getName() const override {
        static const std::string name = "夏普比率（SharpeRatio)";
        return name;
    }

    void stop() override {
        analyzerTotalAnnualizedReturns_.stop();
        analyzerAlgorithmVolatility_.stop();
        analysisMap_ = {{SharpeRatioStr, getSharpeRatio()}};
    }

    std::string getAnalysis() override {
        double sharpeRatio = analysisMap_[SharpeRatioStr];
        return getName() + ": " + std::to_string(sharpeRatio);
    }

private:
    double getSharpeRatio() const {
        double totalAnnualizedReturns = analyzerTotalAnnualizedReturns_.getTotalAnnualizedReturns();
        double algorithmVolatility = analyzerAlgorithmVolatility_.getAlgorithmVolatility();
        return (totalAnnualizedReturns - 0.04) / algorithmVolatility;
    }

private:
    AnalyzerTotalAnnualizedReturns analyzerTotalAnnualizedReturns_;
    AnalyzerAlgorithmVolatility analyzerAlgorithmVolatility_;
};

AnalyzerPtr Analyzer::create(Analyzer::Type type) {
    if (type == Analyzer::Type::SHARPE_RATIO) {
        return std::make_unique<AnalyzerSharpeRatio>();
    } else if (type == Analyzer::Type::DRAW_DOWN) {
        return std::make_unique<AnalyzerDrawDown>();
    } else if (type == Analyzer::Type::TOTAL_RETURNS) {
        return std::make_unique<AnalyzerTotalReturns>();
    } else if (type == Analyzer::Type::TOTAL_ANNUALIZED_RETURNS) {
        return std::make_unique<AnalyzerTotalAnnualizedReturns>();
    } else if (type == Analyzer::Type::ALGORITHM_VOLATILITY) {
        return std::make_unique<AnalyzerAlgorithmVolatility>();
    }
    THROW_RUNTIME_ERROR("unknown analyzer type: " + std::to_string(static_cast<int>(type)));
}

const KeyValueMap& Analyzer::getAnalysisMap() const {
    return analysisMap_;
}
