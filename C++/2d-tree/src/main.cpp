#include "primitives.h"

#include <iostream>

int main()
{
    //std::cout << "Dream:)";
    kdtree::PointSet p;
    Point a(0, 0), b(1, 1), c(.5, .5);
    p.put(a);
    std::cout << p.size() << "\n";
    p.put(b);
    std::cout << p.size() << "\n";
    p.put(c);
    std::cout << p.size() << "\n";
}
