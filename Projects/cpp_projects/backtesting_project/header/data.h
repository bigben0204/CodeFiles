#ifndef BACKTESTING_PROJECT_DATA_H
#define BACKTESTING_PROJECT_DATA_H

#include <memory>
#include <vector>
#include "line_base.h"

enum class DataName {
    DATE, OPEN, HIGH, LOW, CLOSE, VOLUME, OPENINTEREST
};

class Line;
using LinePtr = std::shared_ptr<Line>;

class Line : public LineBase<double> {
public:
    virtual ~Line() noexcept = default;

    virtual DataName getName() const = 0;
};

class Lines {
public:
    virtual ~Lines() noexcept = default;

    virtual LinePtr getLine(DataName name) = 0;

    virtual size_t length() const = 0;
};

class Data;

using DataPtr = std::shared_ptr<Data>;
using Datas = std::vector<DataPtr>;

class Data {
public:
    virtual ~Data() noexcept = default;

    virtual LinePtr getLine(DataName name) = 0;

    virtual double getDatatime(int index) = 0;

    virtual void advance(int size = 1) = 0;

    virtual size_t length() const = 0;

    virtual void home() = 0;

    virtual double getTickValue(DataName name) = 0;

public:
    /*
     * public for unittest, should not be called by user
     */
    virtual void addDataName(const std::vector<std::string>& dataNames) = 0;

    /*
     * public for unittest, should not be called by user
     */
    virtual void addDataRow(const std::vector<std::string>& dataRow) = 0;
};

class DataGenerator {
public:
    static DataPtr readFromFile(const std::string& csvFile);

    static DataPtr readFromApi();

    static DataPtr createEmpty();
};

#endif //BACKTESTING_PROJECT_DATA_H
