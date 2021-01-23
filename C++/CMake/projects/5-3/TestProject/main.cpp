#include <iostream>
#include "foo.h"

int main() {
    const std::string& helloTo = sayHello("Ben");
    std::cout << helloTo << std::endl;
    return 0;
}
