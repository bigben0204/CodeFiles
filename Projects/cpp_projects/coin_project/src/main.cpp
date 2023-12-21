#include <iostream>
#include "timer.h"
#include "coin_sequence_calculator.h"

void calCoinMaxProbabilitySequence(CoinSequenceCalculator& calculator, const std::string& inputSequence) {
    Timer timer;
    const auto& sequenceInfo = calculator.calBestProbabilitySequence(inputSequence);
    std::cout << "sequence: " << sequenceInfo.sequence << ", max probability: " << sequenceInfo.probability
              << std::endl;
}

int main() {
    const std::string inputSequence = "DDDD";

    auto simulationCalculator = CoinSequenceCalculator::create(CoinSequenceCalculator::Type::SIMULATION);
    calCoinMaxProbabilitySequence(*simulationCalculator, inputSequence);

    std::cout << "--------------------------------------------------------" << std::endl;

    auto formulaCalculator = CoinSequenceCalculator::create(CoinSequenceCalculator::Type::FORMULA);
    calCoinMaxProbabilitySequence(*formulaCalculator, inputSequence);

    return 0;
}