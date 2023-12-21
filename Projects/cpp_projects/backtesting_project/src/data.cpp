#include <unordered_map>
#include <stdexcept>
#include <vector>
#include <fstream>
#include <iostream>
#include <cmath>
#include "data.h"
#include "utils.h"

class LineImpl : public Line {
public:
    ~LineImpl() noexcept override = default;

    DataName getName() const override {
        return dataName_;
    }

public:
    LineImpl(DataName dataName) : dataName_(dataName) {}

    void addValue(const std::string& value) {
        if (dataName_ == DataName::DATE) {
            forward(timeStrToStamp(value + " 00:00:00"));
        } else {
            forward(std::stod(value));
        }
    }

private:
    DataName dataName_;
};

using LineImplPtr = std::shared_ptr<LineImpl>;

class LinesImpl : public Lines {
public:
    ~LinesImpl() noexcept override = default;

    LinePtr getLine(DataName name) override {
        auto it = nameLineMap_.find(name);
        if (it != nameLineMap_.end()) {
            return (*it).second;
        }
        THROW_RUNTIME_ERROR("getLine not existing name: " + std::to_string(static_cast<int>(name)));
    }

    size_t length() const override {
        if (nameLineMap_.empty()) {
            THROW_RUNTIME_ERROR("empty lines has no length");
        }
        size_t length = nameLineMap_.begin()->second->length();
        return length;
    }

public:
    void addDataName(const std::vector<std::string>& dataNames) {
        static const std::unordered_map<std::string, DataName> str2DataName = {
            {"date", DataName::DATE},
            {"open", DataName::OPEN},
            {"high", DataName::HIGH},
            {"low", DataName::LOW},
            {"close", DataName::CLOSE},
            {"volume", DataName::VOLUME},
            {"openinterest", DataName::OPENINTEREST}
        };

        for (int i = 0; i < dataNames.size(); ++i) {
            auto it = str2DataName.find(dataNames.at(i));
            if (it != str2DataName.end()) {
                DataName dataName = (*it).second;
                dataNames_.push_back(dataName);
                nameLineMap_.emplace(dataName, std::make_unique<LineImpl>(dataName));
            } else {
                THROW_RUNTIME_ERROR("unknow data col name: " + dataNames.at(i));
            }
        }
    }

    void addDataRow(const std::vector<std::string>& dataRow) {
        for (int i = 0; i < dataRow.size(); ++i) {
            const LinePtr& line = getLine(dataNames_.at(i));
            LineImpl& lineImpl = dynamic_cast<LineImpl&>(*line);
            lineImpl.addValue(dataRow.at(i));
        }
    }

    void advance(int size = 1) {
        for (auto& nameLine: nameLineMap_) {
            auto& [name, line] = nameLine;
            line->advance(size);
        }
    }

    void home() {
        for (auto& nameLine: nameLineMap_) {
            auto& [name, line] = nameLine;
            line->home();
        }
    }

    void tick_fill() {
        for (auto& nameLine: nameLineMap_) {
            auto& [name, line] = nameLine;
            tickValueMap_[name] = line->at(0);
        }
    }

    double getTickValue(DataName name) const {
        auto it = tickValueMap_.find(name);
        if (it != tickValueMap_.end()) {
            return (*it).second;
        }
        THROW_RUNTIME_ERROR("getTickValue not existing name: " + std::to_string(static_cast<int>(name)));
    }

private:
    std::vector<DataName> dataNames_;
    std::unordered_map<DataName, LineImplPtr> nameLineMap_;
    std::unordered_map<DataName, double> tickValueMap_;
};

class DataImpl : public Data {
public:
    ~DataImpl() noexcept override = default;

    LinePtr getLine(DataName name) override {
        return lines_.getLine(name);
    }

    double getDatatime(int index) override {
        const LinePtr& lineImpl = getLine(DataName::DATE);
        if (lineImpl->length() <= lineImpl->bufLength()) {
            return lineImpl->at(index);
        }
        return NAN;
    }

    void advance(int size = 1) override {
        lines_.advance(size);

        const LinePtr& lineImpl = getLine(DataName::DATE);
        if (lineImpl->length() <= lineImpl->bufLength()) {
            lines_.tick_fill();
        }
    }

    size_t length() const override {
        return lines_.length();
    }

    void home() override {
        lines_.home();
    }

    double getTickValue(DataName name) override {
        return lines_.getTickValue(name);
    }

    void addDataName(const std::vector<std::string>& dataNames) override {
        lines_.addDataName(dataNames);
    }

    void addDataRow(const std::vector<std::string>& dataRow) override {
        lines_.addDataRow(dataRow);
    }

private:
    LinesImpl lines_;
};

DataPtr DataGenerator::readFromFile(const std::string& csvFile) {
    std::ifstream ifs(csvFile, std::ios::in);
    if (!ifs.is_open()) {
        THROW_RUNTIME_ERROR("failed to open file: " + csvFile);
    }

    auto dataImpl = std::make_unique<DataImpl>();

    std::string line;
    int lineIndex = 0;
    int colCount = 0;
    while (std::getline(ifs, line)) {
        line = trim(line);
        const auto& strs = split<std::vector<std::string>>(line, ",");
        if (++lineIndex == 1) {
            dataImpl->addDataName(strs);
            colCount = strs.size();
        } else {
            if (strs.size() != colCount) {
                THROW_RUNTIME_ERROR("column size not same. Line number: " + std::to_string(lineIndex) + ", " + line);
            }
            dataImpl->addDataRow(strs);
        }
    }

    return dataImpl;
}

DataPtr DataGenerator::readFromApi() {
    THROW_RUNTIME_ERROR("readFromApi is not implemented");
}

DataPtr DataGenerator::createEmpty() {
    return std::make_unique<DataImpl>();
}
