#ifndef GTEST_PROJECT_COIN_SEQUENCE_CALCULATOR_H
#define GTEST_PROJECT_COIN_SEQUENCE_CALCULATOR_H

#include <random>
#include <iostream>
#include "thread_pool.h"

struct SequenceInfo {
    std::string sequence;
    double probability;
};

class CoinSequenceCalculator {
public:
    enum class Type {
        SIMULATION, FORMULA
    };

public:
    virtual ~CoinSequenceCalculator() noexcept = default;

    virtual SequenceInfo calBestProbabilitySequence(const std::string& inputSequence) = 0;

    static std::unique_ptr<CoinSequenceCalculator> create(Type type, int simulationCount = 10000);
};

#endif //GTEST_PROJECT_COIN_SEQUENCE_CALCULATOR_H
