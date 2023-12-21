#include "utils.h"
#include <ctime>

int timeStrToStamp(const std::string& timeStr, const std::string& fmt /*= "%d-%d-%d %d:%d:%d"*/) {
    char* cha = (char*) timeStr.c_str();
    tm tm_;
    int year, month, day, hour, minute, second;
    sscanf(cha, fmt.c_str(), &year, &month, &day, &hour, &minute, &second);
    tm_.tm_year = year - 1900;
    tm_.tm_mon = month - 1;
    tm_.tm_mday = day;
    tm_.tm_hour = hour;
    tm_.tm_min = minute;
    tm_.tm_sec = second;
    tm_.tm_isdst = 0;
    time_t t_ = mktime(&tm_);
    return t_;
}

std::string timeStamp2Str(time_t timeStamp, const std::string& fmt /*= "%Y-%m-%d %H:%M:%S"*/) {
    struct tm* timeInfo = nullptr;
    char buffer[80];
    timeInfo = localtime(&timeStamp);
    strftime(buffer, 80, fmt.c_str(), timeInfo);
    return std::string(buffer);
}

std::string trim(std::string& str, const std::string& chars/* = " \r\n\t"*/) {
    if (str.empty()) {
        return str;
    }
    str.erase(0, str.find_first_not_of(chars));
    str.erase(str.find_last_not_of(chars) + 1);
    return str;
}
