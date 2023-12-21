#include "coin_sequence_calculator.h"

static const char UP_STR = 'U';
static const char DOWN_STR = 'D';

class CoinSequenceCalculatorSimulationImpl : public CoinSequenceCalculator {
public:
    CoinSequenceCalculatorSimulationImpl(int simulateCount) : simulateCount_(simulateCount) {
    }

    virtual ~CoinSequenceCalculatorSimulationImpl() noexcept = default;

    virtual SequenceInfo calBestProbabilitySequence(const std::string& inputSequence) override {
        const auto& allSequence = getAllSequence(inputSequence.size());
        const auto& bestSequenceInfo = getBestSequenceInfo(inputSequence, allSequence, simulateCount_);
        return bestSequenceInfo;
    }

private:
    enum class SimulationResult {
        WIN_A, WIN_B
    };

private:
    std::vector<std::string> getAllSequence(int length) {
        std::string sequence;
        std::vector<std::string> allSequence;
        calAllSequence(length, sequence, allSequence);
        return allSequence;
    }

    void calAllSequence(int length, std::string& sequence, std::vector<std::string>& allSequence) {
        if (sequence.size() == length) {
            allSequence.push_back(sequence);
            return;
        }

        for (const auto& eachSide: {UP_STR, DOWN_STR}) {
            sequence.push_back(eachSide);
            calAllSequence(length, sequence, allSequence);
            sequence.pop_back();
        }
    }

    SequenceInfo getBestSequenceInfo(const std::string& sequenceA, const std::vector<std::string>& allSequence,
                                     int simulateCount) {
        ThreadPool threadPool;
        threadPool.init();
        std::vector<std::future<std::pair<std::string, double>>> allSequenceProbability;
        for (const auto& tmpSequence: allSequence) {
            allSequenceProbability.emplace_back(threadPool.submit(
                std::bind(&CoinSequenceCalculatorSimulationImpl::getSimulationProbability, this, std::placeholders::_1,
                          std::placeholders::_2, std::placeholders::_3), sequenceA, tmpSequence,
                simulateCount));
        }

        std::string sequence;
        double probability = 0;
        for (auto& sequenceProbability: allSequenceProbability) {
            const auto& [tmpSequence, tmpProbability] = sequenceProbability.get();
            if (tmpProbability > probability) {
                probability = tmpProbability;
                sequence = tmpSequence;
            }
        }
        threadPool.shutdown();

        return {sequence, probability};
    }

    std::pair<std::string, double> getSimulationProbability(const std::string& sequenceA, const std::string& sequenceB,
                                                            int count) {
        int winBCount = 0;
        for (int i = 0; i < count; ++i) {
            if (simulateSequence(sequenceA, sequenceB) == SimulationResult::WIN_B) {
                ++winBCount;
            }
        }
        std::cout << "tmpSequence: << " << sequenceB << ", tmpProbability: "
                  << (static_cast<double>(winBCount) / count) << std::endl;
        return {sequenceB, static_cast<double>(winBCount) / count};
    };

    SimulationResult simulateSequence(const std::string& sequenceA, const std::string& sequenceB) {
        std::string simulateSequence;
        while (true) {
            simulateSequence.push_back(getOneCoinSimulation());
            int sequenceLength = sequenceA.size();
            int simulateSeqLength = simulateSequence.size();
            if (simulateSeqLength < sequenceLength) {
                continue;
            }

            if (simulateSequence.compare(simulateSeqLength - sequenceLength, sequenceLength, sequenceA) == 0) {
                return SimulationResult::WIN_A;
            } else if (simulateSequence.compare(simulateSeqLength - sequenceLength, sequenceLength, sequenceB) == 0) {
                return SimulationResult::WIN_B;
            }
        }
    }

    char getOneCoinSimulation() {
        std::random_device rd;
        std::mt19937 gen(rd());
        std::binomial_distribution<> distrib;
        return distrib(gen) == 0 ? UP_STR : DOWN_STR;
    }

private:
    int simulateCount_;
};

class CoinSequenceCalculatorFormulaImpl : public CoinSequenceCalculator {
public:
    CoinSequenceCalculatorFormulaImpl() {
    }

    virtual ~CoinSequenceCalculatorFormulaImpl() noexcept = default;

    virtual SequenceInfo calBestProbabilitySequence(const std::string& inputSequence) override {
        const auto& allSequence = getTwoSequence(inputSequence);
        const auto& bestSequenceInfo = getBestSequenceInfo(inputSequence, allSequence);
        return bestSequenceInfo;
    }

private:
    std::vector<std::string> getTwoSequence(const std::string& inputSequence) {
        const std::string& prefix = inputSequence.substr(0, inputSequence.size() - 1);
        return {UP_STR + prefix, DOWN_STR + prefix};
    }

    SequenceInfo getBestSequenceInfo(const std::string& sequenceA, const std::vector<std::string>& allSequence) {
        std::string sequence;
        double probability = 0;
        for (const auto& tmpSequence: allSequence) {
            double tmpProbability = getProbabilityByFormula(sequenceA, tmpSequence);
            std::cout << "tmpSequence: << " << tmpSequence << ", tmpProbability: " << tmpProbability << std::endl;
            if (tmpProbability > probability) {
                probability = tmpProbability;
                sequence = tmpSequence;
            }
        }
        return {sequence, probability};
    }

    /*
L(10110, 10110) = (10010)2 = 18
L(10110, 01011) = (00001)2 = 1
L(01011, 01011) = (10000)2 = 16
L(01011, 10110) = (01001)2 = 9
那么， 01 串 a 和 b 的胜率之比就是
(L(b, b) – L(b, a)) : (L(a, a) – L(a, b))
     */
    double getProbabilityByFormula(const std::string& seqA, const std::string& seqB) {
        int lbb = getLvalue(seqB, seqB);
        int lba = getLvalue(seqB, seqA);
        int laa = getLvalue(seqA, seqA);
        int lab = getLvalue(seqA, seqB);
        return static_cast<double>(laa - lab) / ((lbb - lba) + (laa - lab));
    }

    int getLvalue(const std::string& lhsSeq, const std::string& rhsSeq) {
        int value = 0;
        int seqLength = lhsSeq.size();
        for (int i = 0; i < lhsSeq.size(); ++i) {
            value <<= 1;
            if (lhsSeq.compare(i, seqLength - i, rhsSeq, 0, seqLength - i) == 0) {
                value += 1;
            }
        }
        return value;
    }
};

std::unique_ptr<CoinSequenceCalculator> CoinSequenceCalculator::create(Type type, int simulationCount) {
    if (type == Type::SIMULATION) {
        return std::make_unique<CoinSequenceCalculatorSimulationImpl>(simulationCount);
    } else if (type == Type::FORMULA) {
        return std::make_unique<CoinSequenceCalculatorFormulaImpl>();
    }
    return std::unique_ptr<CoinSequenceCalculator>();
}