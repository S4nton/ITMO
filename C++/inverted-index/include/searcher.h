#pragma once

#include <memory>
#include <set>
#include <string>
#include <unordered_map>
#include <utility>
#include <vector>

class Searcher
{
public:
    using Filename = std::string; // or std::filesystem::path

private:
    std::unordered_map<std::string, std::unordered_map<Filename, std::vector<size_t>>> inverted_index;
    std::set<Filename> files;
    std::set<Filename> find_words(const std::vector<std::string> &, const std::set<Filename> &) const;
    void reset_file(const Filename &);

public:
    // index modification
    void add_document(const Filename & filename, std::istream & strm);

    void remove_document(const Filename & filename);

    // queries
    class DocIterator
    {
        std::shared_ptr<std::set<Filename>> ptr_source;
        std::set<Filename>::iterator iter;

    public:
        using value_type = const Filename;
        using iterator_category = std::forward_iterator_tag;
        using difference_type = std::ptrdiff_t;
        using pointer = value_type *;
        using reference = value_type &;

        DocIterator(const std::shared_ptr<std::set<Filename>> &, const std::set<Filename>::iterator &);

        reference operator*() const;

        pointer operator->() const;

        DocIterator & operator++();

        DocIterator operator++(int);

        bool operator==(const DocIterator & other) const;

        bool operator!=(const DocIterator & other) const;
    };

    class BadQuery : public std::exception
    {
        std::string exception_message;

    public:
        BadQuery(const std::string &, const std::string &);

        const char * what() const noexcept override;
    };

    std::pair<DocIterator, DocIterator> search(const std::string & query) const;
};
