#ifndef BACKTESTING_PROJECT_UTILS_H
#define BACKTESTING_PROJECT_UTILS_H

#include <string>
#include <stdexcept>
#include <cmath>

#define THROW_RUNTIME_ERROR(msg) throw std::runtime_error(std::string(msg))

int timeStrToStamp(const std::string& timeStr, const std::string& fmt = "%d-%d-%d %d:%d:%d");

std::string timeStamp2Str(time_t timeStamp, const std::string& fmt = "%Y-%m-%d %H:%M:%S");

template<typename T>
inline auto split(const std::string& s, const std::string& delimiters = " ") {
    T tokens;
    std::string::size_type lastPos = s.find_first_not_of(delimiters);
    std::string::size_type pos = s.find_first_of(delimiters, lastPos);
    while (pos != std::string::npos || lastPos != std::string::npos) {
        tokens.push_back(s.substr(lastPos, pos - lastPos));
        lastPos = s.find_first_not_of(delimiters, pos);
        pos = s.find_first_of(delimiters, lastPos);
    }
    return tokens;
}

std::string trim(std::string& str, const std::string& chars = " \r\n\t");

template<typename T>
bool equalFloats(T lhs, T rhs) {
    return std::fabs(lhs - rhs) < std::numeric_limits<T>::epsilon();
}

#endif //BACKTESTING_PROJECT_UTILS_H
