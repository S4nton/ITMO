#pragma once

#include <iostream>
#include <memory>
#include <optional>
#include <set>
#include <vector>

class Point
{
    double m_x;
    double m_y;

public:
    Point(double x, double y);

    double x() const;
    double y() const;
    double distance(const Point &) const;

    bool operator<(const Point &) const;
    bool operator>(const Point &) const;
    bool operator<=(const Point &) const;
    bool operator>=(const Point &) const;
    bool operator==(const Point &) const;
    bool operator!=(const Point &) const;
};

class Rect
{
    Point left_bottom;
    Point right_top;

public:
    Rect(const Point & left_bottom, const Point & right_top);

    double xmin() const;
    double ymin() const;
    double xmax() const;
    double ymax() const;
    double distance(const Point &) const;

    bool contains(const Point &) const;
    bool intersects(const Rect &) const;
};

namespace rbtree {

class PointSet
{
    std::shared_ptr<std::set<Point>> point_set;

public:
    class iterator
    {
        std::shared_ptr<std::set<Point>> ptr_source;
        std::set<Point>::iterator iter;

    public:
        using value_type = Point;
        using iterator_category = std::forward_iterator_tag;
        using difference_type = std::ptrdiff_t;
        using pointer = const value_type *;
        using reference = const value_type &;

        iterator() = default;

        iterator(const std::shared_ptr<std::set<Point>> &, const std::set<Point>::iterator &);

        reference operator*() const;

        pointer operator->() const;

        iterator & operator++();

        iterator operator++(int);

        bool operator==(const iterator & other) const;

        bool operator!=(const iterator & other) const;
    };

    PointSet();

    bool empty() const;
    std::size_t size() const;
    void put(const Point &);
    bool contains(const Point &) const;

    // second iterator points to an element out of range
    std::pair<iterator, iterator> range(const Rect &) const;
    iterator begin() const;
    iterator end() const;

    std::optional<Point> nearest(const Point &) const;
    // second iterator points to an element out of range
    std::pair<iterator, iterator> nearest(const Point & p, std::size_t k) const;

    friend std::ostream & operator<<(std::ostream & ostrm, const PointSet & pointSet)
    {
        for (const auto point : pointSet) {
            ostrm << &point << "\n";
        }
        return ostrm;
    }
};

} // namespace rbtree

namespace kdtree {

class PointSet
{
    mutable std::shared_ptr<std::vector<Point>> dfs_tree;
    void resizeTree() const;

public:
    class Node
    {
    public:
        Node(const Point & point, size_t h)
            : point(point)
            , h(h)
        {
        }
        const Point point;
        size_t h;
        std::shared_ptr<Node> left;
        std::shared_ptr<Node> right;
    };

    class iterator
    {
        std::shared_ptr<std::vector<Point>> ptr_source;
        std::vector<Point>::iterator iter;

    public:
        using value_type = Point;
        using iterator_category = std::forward_iterator_tag;
        using difference_type = std::ptrdiff_t;
        using pointer = const value_type *;
        using reference = const value_type &;

        iterator() = default;

        iterator(const std::shared_ptr<std::vector<Point>> & ptr_source, const std::vector<Point>::iterator & iter);

        reference operator*() const;

        pointer operator->() const;

        iterator & operator++();

        iterator operator++(int);

        bool operator==(const iterator & other) const;

        bool operator!=(const iterator & other) const;
    };

    PointSet();

    bool empty() const;
    std::size_t size() const;
    void put(const Point &);
    bool contains(const Point &) const;

    std::pair<iterator, iterator> range(const Rect &) const;
    iterator begin() const;
    iterator end() const;

    std::optional<Point> nearest(const Point &) const;
    std::pair<iterator, iterator> nearest(const Point &, std::size_t) const;

    friend std::ostream & operator<<(std::ostream & ostrm, const PointSet & pointSet)
    {
        for (const auto point : pointSet) {
            ostrm << &point << "\n";
        }
        return ostrm;
    }

private:
    std::shared_ptr<Node> root;
};

} // namespace kdtree
