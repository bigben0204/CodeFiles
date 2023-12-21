#include <sstream>
#include <unordered_map>
#include <iostream>
#include <iomanip>
#include "observer.h"
#include "utils.h"
#include "data.h"

class ObserverValue : public Observer {
public:
    virtual ~ObserverValue() noexcept override = default;

    void notify(const Datas& datas, double currentValue) override {
        dateValues_.emplace_back(datas[0]->getDatatime(0), currentValue);
    }

    void stop() override {
    }

    void showInfo() const override {
        const std::string& dateValueInfoStr = getDateValueInfo();
        std::cout << getName() << ":\n" << dateValueInfoStr << std::endl;
    }

    const std::string& getName() const override {
        static const std::string name = "总价值（Value）";
        return name;
    }

private:
    std::string getDateValueInfo() const {
        std::ostringstream oss;
        for (const auto& dateValue: dateValues_) {
            oss << std::setiosflags(std::ios::fixed) << std::setprecision(6)
                << "Date: " << timeStamp2Str(dateValue.first) << " -- value: " << dateValue.second << "\n";
        }
        return oss.str();
    }

private:
    std::vector<std::pair<double, double>> dateValues_;
};

ObserverPtr Observer::create(Observer::Type type) {
    if (type == Observer::Type::VALUE) {
        return std::make_unique<ObserverValue>();
    }
    THROW_RUNTIME_ERROR("unknown observer type: " + std::to_string(static_cast<int>(type)));
}