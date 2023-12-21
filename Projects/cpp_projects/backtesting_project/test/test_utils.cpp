#include "gtest/gtest.h"
#include "utils.h"

TEST(UtilsTest, TestTimeStampConvert) {
    std::string timeStr = "2017-01-05 00:00:00";
    time_t timet = timeStrToStamp(timeStr);
    const std::string& expectTimeStr = timeStamp2Str(timet);
    EXPECT_EQ(expectTimeStr, timeStr);
}

TEST(UtilsTest, TestDoubleCompare) {
    std::string a = "19.334", b = "20.334";
    EXPECT_TRUE(equalFloats(std::stod(a) + 1, std::stod(b)));
}
