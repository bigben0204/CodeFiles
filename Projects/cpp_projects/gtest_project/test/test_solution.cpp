#include "gtest/gtest.h"

using namespace std;

class Solution {
public:
};

class SolutionTest : public testing::Test {
protected:
    Solution solution;
};

TEST_F(SolutionTest, Test1) {
    EXPECT_EQ(1, 1);
}