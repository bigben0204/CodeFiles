#ifndef BACKTESTING_PROJECT_LINE_BASE_H
#define BACKTESTING_PROJECT_LINE_BASE_H

#include <string>
#include <vector>
#include <cmath>
#include "utils.h"

/*
 * LineBase基类，提供at(0)和at(-1)功能
 */
template<typename T>
class LineBase {
public:
    using Container = std::vector<T>;
public:
    T& operator[](int index) {
        return at(index);
    }

    const T& operator[](int index) const {
        return at(index);
    }

    T& at(int index) {
        checkRange(index);
        return values_.at(index_ + index);
    }

    const T& at(int index) const {
        checkRange(index);
        return values_.at(index_ + index);
    }

    void forward(const T& value = NAN, int size = 1) {
        index_ += size;
        lenCount_ += size;
        for (int i = 0; i < size; ++i) {
            values_.push_back(value);
        }
    }

    size_t length() const {
        return lenCount_;
    }

    size_t bufLength() const {
        return values_.size();
    }

    void advance(int size = 1) {
        index_ += size;
        lenCount_ += size;
    }

    void home() {
        index_ = -1;
        lenCount_ = 0;
    }

    const Container& getValues() const {
        return values_;
    }

private:
    void checkRange(int index) const {
        if (index_ + index > values_.size() - 1) {
            THROW_RUNTIME_ERROR("index should be greater than 0. index: " + std::to_string(index));
        }
        if ((index_ + index) < 0) {
            THROW_RUNTIME_ERROR("past index out of range. index: " + std::to_string(index) + ", current index: " +
                                std::to_string(index_));
        }
    }

protected:
    Container values_;
    int index_ = -1;
    int lenCount_ = 0;
};

#endif //BACKTESTING_PROJECT_LINE_BASE_H
