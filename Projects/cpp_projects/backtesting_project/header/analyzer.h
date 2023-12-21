#ifndef BACKTESTING_PROJECT_ANALYZER_H
#define BACKTESTING_PROJECT_ANALYZER_H

#include <memory>
#include <vector>
#include <unordered_map>
#include "data.h"

using KeyValueMap = std::unordered_map<std::string, double>;

class Analyzer;

using AnalyzerPtr = std::shared_ptr<Analyzer>;

class Analyzer {
public:
    enum class Type {
        SHARPE_RATIO, DRAW_DOWN, TOTAL_RETURNS, TOTAL_ANNUALIZED_RETURNS, ALGORITHM_VOLATILITY
    };

public:
    virtual ~Analyzer() noexcept = default;

    virtual void notify(const Datas& datas, double currentValue) = 0;

    virtual const std::string& getName() const = 0;

    virtual std::string getAnalysis() = 0;

    virtual void stop() = 0;

    static AnalyzerPtr create(Type type);

public:
    const KeyValueMap& getAnalysisMap() const;

protected:
    KeyValueMap analysisMap_;
};

using Analyzers = std::vector<AnalyzerPtr>;

#endif //BACKTESTING_PROJECT_ANALYZER_H
