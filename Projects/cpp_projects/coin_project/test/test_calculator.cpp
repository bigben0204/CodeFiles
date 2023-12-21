#include "gtest/gtest.h"
#include "coin_sequence_calculator.h"

class CalculatorTest : public testing::Test {
protected:
    std::unique_ptr<CoinSequenceCalculator> simulationCalculator = CoinSequenceCalculator::create(
        CoinSequenceCalculator::Type::SIMULATION);
    std::unique_ptr<CoinSequenceCalculator> formulaCalculator = CoinSequenceCalculator::create(
        CoinSequenceCalculator::Type::FORMULA);
};

TEST_F(CalculatorTest, Test1) {
    std::string inputSequence = "DDDD";
    std::string expectSequence = "UDDD";
    EXPECT_EQ(expectSequence, simulationCalculator->calBestProbabilitySequence(inputSequence).sequence);
    EXPECT_EQ(expectSequence, formulaCalculator->calBestProbabilitySequence(inputSequence).sequence);
}
