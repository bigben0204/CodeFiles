#include "gtest/gtest.h"
#include "analyzer.h"
#include "constant.h"

using namespace std;

class AnalyzerTest : public testing::Test {
protected:
    AnalyzerPtr analyzerDrawDown = Analyzer::create(Analyzer::Type::DRAW_DOWN);
};

TEST_F(AnalyzerTest, TestDrawDown) {
    DataPtr data = DataGenerator::createEmpty();
    data->addDataName({"date"});

    for (const auto& date: {"2017-01-01", "2017-01-02", "2017-01-03", "2017-01-04", "2017-01-05", "2017-01-06",
                            "2017-01-07", "2017-01-08", "2017-01-09"}) {
        data->addDataRow({date});
    }
    data->home();

    Datas datas = {data};
    for (auto value: {3, 7, 2, 6, 4, 1, 9, 8, 5}) {
        data->advance();
        analyzerDrawDown->notify(datas, value);
    }

    analyzerDrawDown->stop();
    std::cout << analyzerDrawDown->getAnalysis() << std::endl;
    const auto& analysisMap = analyzerDrawDown->getAnalysisMap();

    // 比较结果
    auto maxDrawDownRatio = analysisMap.at(MaxDrawDownRatioStr);
    auto expectMaxDrawDownRatio = static_cast<double >(7 - 1) / 7;
    EXPECT_EQ(maxDrawDownRatio, expectMaxDrawDownRatio);
    EXPECT_EQ(7 - 1, analysisMap.at(MaxValueDownStr));
    EXPECT_EQ("2017-01-02 00:00:00", timeStamp2Str(analysisMap.at(FromDateStr)));
    EXPECT_EQ("2017-01-06 00:00:00", timeStamp2Str(analysisMap.at(ToDateStr)));
}