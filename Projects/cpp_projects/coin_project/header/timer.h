#ifndef GTEST_PROJECT_TIMER_H
#define GTEST_PROJECT_TIMER_H

#include <iostream>
#include "chrono"

class Timer {
public:
    ~Timer() {
        std::chrono::steady_clock::time_point end = std::chrono::steady_clock::now();
        std::chrono::duration<double> timeUsed = std::chrono::duration_cast<std::chrono::duration<double >>(
            end - start_);
        std::cout << "Elapse time : " << timeUsed.count() << "s" << std::endl;
    }

private:
    std::chrono::steady_clock::time_point start_ = std::chrono::steady_clock::now();;
};

#endif //GTEST_PROJECT_TIMER_H
