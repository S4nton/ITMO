#include "primitives.h"

#include <algorithm>
#include <fstream>
#include <math.h>
#include <vector>

double minDouble = std::numeric_limits<double>::min();
double maxDouble = std::numeric_limits<double>::max();
Rect bigRect(Point(minDouble, minDouble), Point(maxDouble, maxDouble));
bool isChanged = true;

Point::Point(double x, double y)
    : m_x(x)
    , m_y(y)
{
}

double Point::x() const
{
    return m_x;
}

double Point::y() const
{
    return m_y;
}

double Point::distance(const Point & point) const
{
    return std::sqrt((m_x - point.x()) * (m_x - point.x()) + (m_y - point.y()) * (m_y - point.y()));
}

bool Point::operator<(const Point & point) const
{
    return m_x < point.x() || (m_x == point.x() && m_y < point.y());
}

bool Point::operator>(const Point & point) const
{
    return !(*this < point) && !(*this == point);
}

bool Point::operator<=(const Point & point) const
{
    return !(*this > point);
}

bool Point::operator>=(const Point & point) const
{
    return !(*this < point);
}

bool Point::operator==(const Point & point) const
{
    return this->m_x == point.x() && this->m_y == point.y();
}

bool Point::operator!=(const Point & point) const
{
    return !(*this == point);
}

Rect::Rect(const Point & left_bottom, const Point & right_top)
    : left_bottom(left_bottom)
    , right_top(right_top)
{
}

double Rect::xmin() const
{
    return left_bottom.x();
}

double Rect::ymin() const
{
    return left_bottom.y();
}

double Rect::xmax() const
{
    return right_top.x();
}

double Rect::ymax() const
{
    return right_top.y();
}

double Rect::distance(const Point & point) const
{
    if (this->contains(point)) {
        return 0.0;
    }
    double a = std::max(xmin() - point.x(), point.x() - xmax());
    double b = std::max(ymin() - point.y(), point.y() - ymax());
    a = std::max(0.0, a);
    b = std::max(0.0, b);
    return sqrt(a * a + b * b);
}

bool Rect::contains(const Point & point) const
{
    return (point.x() >= this->xmin() && point.x() <= this->xmax()) && (point.y() >= this->ymin() && point.y() <= this->ymax());
}

bool Rect::intersects(const Rect & rect) const
{
    bool inter_left_bottom = rect.contains(left_bottom);
    bool inter_right_top = rect.contains(right_top);
    bool inter_left_top = rect.contains(Point(this->xmin(), this->ymax()));
    bool inter_right_bottom = rect.contains(Point(this->xmax(), this->ymin()));
    return inter_left_bottom || inter_left_top || inter_right_bottom || inter_right_top;
}

// rbtree iterator

rbtree::PointSet::iterator::iterator(const std::shared_ptr<std::set<Point>> & ptr_source, const std::set<Point>::iterator & iter)
    : ptr_source(ptr_source)
    , iter(iter)
{
}

rbtree::PointSet::iterator::reference rbtree::PointSet::iterator::operator*() const
{
    return *iter;
}

rbtree::PointSet::iterator::pointer rbtree::PointSet::iterator::operator->() const
{
    return &*iter;
}
rbtree::PointSet::iterator & rbtree::PointSet::iterator::operator++()
{
    ++iter;
    return *this;
}

rbtree::PointSet::iterator rbtree::PointSet::iterator::operator++(int)
{
    iterator old = *this;
    ++(*this);
    return old;
}

bool rbtree::PointSet::iterator::operator==(const rbtree::PointSet::iterator & other) const
{
    return iter == other.iter;
}

bool rbtree::PointSet::iterator::operator!=(const rbtree::PointSet::iterator & other) const
{
    return !(*this == other);
}

// rbtree

rbtree::PointSet::PointSet()
{
    point_set = std::make_shared<std::set<Point>>();
}

bool rbtree::PointSet::empty() const
{
    return point_set->empty();
}

size_t rbtree::PointSet::size() const
{
    return point_set->size();
}

void rbtree::PointSet::put(const Point & point)
{
    point_set->insert(point);
}

bool rbtree::PointSet::contains(const Point & point) const
{
    auto res_iterator = point_set->find(point);
    return res_iterator != point_set->end();
}

rbtree::PointSet::iterator rbtree::PointSet::begin() const
{
    return iterator(point_set, point_set->begin());
}

rbtree::PointSet::iterator rbtree::PointSet::end() const
{
    return iterator(point_set, point_set->end());
}

std::pair<rbtree::PointSet::iterator, rbtree::PointSet::iterator> rbtree::PointSet::range(const Rect & rect) const
{
    std::shared_ptr<std::set<Point>> res = std::make_shared<std::set<Point>>();
    for (Point const & point : *point_set) {
        if (rect.contains(point)) {
            res->insert(point);
        }
    }
    return {iterator(res, res->begin()), iterator(res, res->end())};
}

std::optional<Point> rbtree::PointSet::nearest(const Point & point) const
{
    Point nearest_point = *point_set->begin();
    double min_dist = point.distance(nearest_point);
    for (Point const & ps : *point_set) {
        double dist = point.distance(ps);
        if (min_dist > dist) {
            min_dist = dist;
            nearest_point = ps;
        }
    }
    return nearest_point;
}
std::pair<rbtree::PointSet::iterator, rbtree::PointSet::iterator> rbtree::PointSet::nearest(const Point & point, std::size_t k) const
{
    std::shared_ptr<std::set<Point>> res = std::make_shared<std::set<Point>>();
    std::vector<std::pair<double, Point>> distances;
    for (Point const & ps : *point_set) {
        double dist = point.distance(ps);
        distances.push_back({dist, ps});
    }
    std::sort(distances.begin(), distances.end());
    for (size_t i = 0; i < distances.size() && i < k; i++) {
        res->insert(distances[i].second);
    }
    return {iterator(res, res->begin()), iterator(res, res->end())};
}

// kdtree iterator

kdtree::PointSet::iterator::iterator(const std::shared_ptr<std::vector<Point>> & ptr_source, const std::vector<Point>::iterator & iter)
    : ptr_source(ptr_source)
    , iter(iter)
{
}

kdtree::PointSet::iterator::reference kdtree::PointSet::iterator::operator*() const
{
    return *iter;
}

kdtree::PointSet::iterator::pointer kdtree::PointSet::iterator::operator->() const
{
    return &*iter;
}
kdtree::PointSet::iterator & kdtree::PointSet::iterator::operator++()
{
    ++iter;
    return *this;
}

kdtree::PointSet::iterator kdtree::PointSet::iterator::operator++(int)
{
    iterator old = *this;
    ++(*this);
    return old;
}

bool kdtree::PointSet::iterator::operator==(const kdtree::PointSet::iterator & other) const
{
    return iter == other.iter;
}

bool kdtree::PointSet::iterator::operator!=(const kdtree::PointSet::iterator & other) const
{
    return !(*this == other);
}

// kdtree

kdtree::PointSet::PointSet()
{
    dfs_tree = std::make_shared<std::vector<Point>>();
}

std::shared_ptr<kdtree::PointSet::Node> constractTree(std::shared_ptr<std::vector<Point>> & points, size_t height)
{
    auto p = *points;
    if (points->size() == 1) {
        return std::make_shared<kdtree::PointSet::Node>(p[0], height);
    }
    if (height % 2 == 0) {
        sort(points->begin(), points->end(), [](const Point & a, const Point & b) {
            return a.x() < b.x();
        });
    }
    else {
        sort(points->begin(), points->end(), [](const Point & a, const Point & b) {
            return a.y() < b.y();
        });
    }
    std::shared_ptr<std::vector<Point>> leftPoints = std::make_shared<std::vector<Point>>(points->begin(), points->begin() + points->size() / 2);
    std::shared_ptr<std::vector<Point>> rightPoints = std::make_shared<std::vector<Point>>(points->begin() + points->size() / 2, points->begin() + points->size() / 2);
    std::shared_ptr<kdtree::PointSet::Node> node = std::make_shared<kdtree::PointSet::Node>(p[p.size() / 2], height);
    node->left = constractTree(leftPoints, ++height);
    node->right = constractTree(rightPoints, ++height);
    return node;
}

bool kdtree::PointSet::empty() const
{
    return dfs_tree->empty();
}
size_t kdtree::PointSet::size() const
{
    return dfs_tree->size();
}

bool whereToGo(const std::shared_ptr<kdtree::PointSet::Node> & node, const Point & point, const size_t height)
{
    if (height % 2 == 0) {
        return point.x() <= node->point.x();
    }
    else {
        return point.y() <= node->point.y();
    }
}

void put_point(std::shared_ptr<kdtree::PointSet::Node> & node, const Point & point, size_t height)
{
    if (node == nullptr) {
        node = std::make_shared<kdtree::PointSet::Node>(point, 1);
    }
    else if (whereToGo(node, point, height)) {
        put_point(node->left, point, ++height);
    }
    else {
        put_point(node->right, point, ++height);
    }
}

void dfs(const std::shared_ptr<kdtree::PointSet::Node> & node, std::shared_ptr<std::vector<Point>> & res)
{
    res->push_back(node->point);
    if (node->left != nullptr) {
        dfs(node->left, res);
    }
    if (node->right != nullptr) {
        dfs(node->right, res);
    }
}

void kdtree::PointSet::put(const Point & point)
{
    if (dfs_tree->empty()) {
        root = std::make_shared<Node>(point, 1);
        dfs_tree->push_back(point);
        return;
    }
    if (!contains(point)) {
        put_point(root, point, 0);
        dfs_tree->push_back(point);
    }
}

bool search_point(const std::shared_ptr<kdtree::PointSet::Node> & node, const Point & point, size_t height)
{
    if (node == nullptr) {
        return false;
    }
    if (node->point == point) {
        return true;
    }
    if (whereToGo(node, point, height)) {
        return search_point(node->left, point, ++height);
    }
    else {
        return search_point(node->right, point, ++height);
    }
}

bool kdtree::PointSet::contains(const Point & point) const
{
    return search_point(root, point, 0);
}

void kdtree::PointSet::resizeTree() const
{
    if (isChanged) {
        dfs_tree->clear();
        dfs(root, dfs_tree);
        isChanged = false;
    }
}

kdtree::PointSet::iterator kdtree::PointSet::begin() const
{
    resizeTree();
    return iterator(dfs_tree, dfs_tree->begin());
}

kdtree::PointSet::iterator kdtree::PointSet::end() const
{
    return iterator(dfs_tree, dfs_tree->end());
}

Rect getRectTree(const Point point, const Rect rect, const bool typeOfChild, const size_t height)
{
    if (height % 2 == 0) {
        if (typeOfChild) {
            return Rect(Point(rect.xmin(), rect.ymin()), Point(point.x(), rect.ymax()));
        }
        else {
            return Rect(Point(point.x(), rect.ymin()), Point(rect.xmax(), rect.ymax()));
        }
    }
    else {
        if (typeOfChild) {
            return Rect(Point(rect.xmin(), rect.ymin()), Point(rect.xmax(), point.y()));
        }
        else {
            return Rect(Point(rect.xmin(), point.y()), Point(rect.xmax(), rect.ymax()));
        }
    }
}

void search_points_rect(const std::shared_ptr<kdtree::PointSet::Node> & node, const Rect & rect, std::shared_ptr<std::vector<Point>> & res, Rect rect_tree, size_t height)
{
    if (node == nullptr && !rect.intersects(rect_tree)) {
        return;
    }
    if (rect.contains(node->point)) {
        res->push_back(node->point);
    }
    Rect lRect = getRectTree(node->point, rect_tree, true, height);
    Rect rRect = getRectTree(node->point, rect_tree, false, height);
    if (node->left != nullptr) {
        search_points_rect(node->left, rect, res, lRect, ++height);
    }
    if (node->right != nullptr) {
        search_points_rect(node->right, rect, res, rRect, ++height);
    }
}

std::pair<kdtree::PointSet::iterator, kdtree::PointSet::iterator> kdtree::PointSet::range(const Rect & rect) const
{
    std::shared_ptr<std::vector<Point>> res = std::make_shared<std::vector<Point>>();
    search_points_rect(root, rect, res, bigRect, 0);
    return {iterator(res, res->begin()), iterator(res, res->end())};
}

void search_nearest_points(const std::shared_ptr<kdtree::PointSet::Node> & node, const Point & point, const size_t k, std::set<std::pair<double, Point>> & res, const Rect & rect_tree, size_t height)
{
    if (res.size() < k || (!res.empty() && std::prev(res.end())->first > point.distance(node->point))) {
        if (res.size() == k) {
            res.erase(std::prev(res.end()));
        }
        res.insert({point.distance(node->point), node->point});
    }
    Rect lRect = getRectTree(node->point, rect_tree, true, height);
    Rect rRect = getRectTree(node->point, rect_tree, false, height);
    if (node->left != nullptr && ((height % 2 != 0 && node->point.x() > point.x()) || (height % 2 == 0 && node->point.y() > point.y()) || node->right == nullptr)) {
        search_nearest_points(node->left, point, k, res, lRect, ++height);
        if (node->right != nullptr && (res.size() < k || std::prev(res.end())->first >= rRect.distance(point))) {
            search_nearest_points(node->right, point, k, res, rRect, ++height);
        }
    }
    else if (node->right != nullptr) {
        search_nearest_points(node->right, point, k, res, rRect, ++height);
        if (node->left != nullptr && (res.size() < k || std::prev(res.end())->first >= lRect.distance(point))) {
            search_nearest_points(node->left, point, k, res, lRect, ++height);
        }
    }
}

std::optional<Point> kdtree::PointSet::nearest(const Point & point) const
{
    std::set<std::pair<double, Point>> res;
    search_nearest_points(root, point, 1, res, bigRect, 0);
    return res.begin()->second;
}

std::pair<kdtree::PointSet::iterator, kdtree::PointSet::iterator> kdtree::PointSet::nearest(const Point & point, std::size_t k) const
{
    if (k == 0) {
        return {{}, {}};
    }
    std::set<std::pair<double, Point>> res_set;
    search_nearest_points(root, point, k, res_set, bigRect, 0);
    std::shared_ptr<std::vector<Point>> res = std::make_shared<std::vector<Point>>();
    for (const auto & p : res_set) {
        res->push_back(p.second);
    }
    return {iterator(res, res->begin()), iterator(res, res->end())};
}
