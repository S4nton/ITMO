#include "searcher.h"

#include <istream>
#include <string>

Searcher::DocIterator::DocIterator(const std::shared_ptr<std::set<Filename>> & ptr_source, const std::set<Filename>::iterator & iter)
    : ptr_source(ptr_source)
    , iter(iter)
{
}

Searcher::DocIterator::reference Searcher::DocIterator::operator*() const
{
    return *iter;
}

Searcher::DocIterator::pointer Searcher::DocIterator::operator->() const
{
    return &*iter;
}
Searcher::DocIterator & Searcher::DocIterator::operator++()
{
    ++iter;
    return *this;
}

Searcher::DocIterator Searcher::DocIterator::operator++(int)
{
    DocIterator old = *this;
    ++(*this);
    return old;
}

bool Searcher::DocIterator::operator==(const Searcher::DocIterator & other) const
{
    return iter == other.iter;
}

bool Searcher::DocIterator::operator!=(const Searcher::DocIterator & other) const
{
    return !(*this == other);
}

Searcher::BadQuery::BadQuery(const std::string & reason, const std::string & query)
    : exception_message("Search query syntax error: " + reason + " query: " + query)
{
}
const char * Searcher::BadQuery::what() const noexcept
{
    return exception_message.c_str();
}

void split_by_words(const std::string & line, std::vector<std::string> & words)
{
    size_t i = 0;
    while (i < line.size()) {
        size_t start_word, end_word;
        while (i < line.size() && !static_cast<bool>(std::isalnum(static_cast<unsigned char>(line[i])))) {
            ++i;
        }
        if (i == line.size()) {
            break;
        }
        start_word = i;
        while (i < line.size() && !isspace(static_cast<unsigned char>(line[i]))) {
            ++i;
        }
        end_word = i - 1;
        while (end_word >= start_word && !static_cast<bool>(std::isalnum(static_cast<unsigned char>(line[end_word])))) {
            --end_word;
        }
        std::string word;
        for (size_t j = start_word; j <= end_word; j++) {
            word += static_cast<char>(std::tolower(static_cast<unsigned char>(line[j])));
        }
        words.push_back(word);
        ++i;
    }
}

void Searcher::reset_file(const Filename & filename)
{
    for (auto & entries : inverted_index) {
        if (entries.second.count(filename) != 0) {
            entries.second.erase(filename);
        }
    }
}

void Searcher::add_document(const Filename & filename, std::istream & strm)
{
    if (files.count(filename) != 0) {
        reset_file(filename);
    }
    else {
        files.insert(filename);
    }
    size_t position = 0;
    std::string str;
    while (std::getline(strm, str)) {
        std::vector<std::string> words;
        split_by_words(str, words);
        for (std::string const & word : words) {
            inverted_index[word][filename].push_back(position);
            ++position;
        }
    }
}

void Searcher::remove_document(const Filename & filename)
{
    reset_file(filename);
    files.erase(filename);
}

void parse_query(const std::string & query, std::vector<std::string> & words, std::vector<std::vector<std::string>> & phrases)
{
    size_t quotes_cnt = 0;
    size_t prefix_quotes_cnt = 0;
    size_t pos = 0;
    bool check_symbols = false;
    for (size_t i = 0; i < query.size(); ++i) {
        if (query[i] == '"') {
            ++quotes_cnt;
        }
        if (static_cast<bool>(std::isalnum(static_cast<unsigned char>(query[i])))) {
            if (!check_symbols) {
                pos = i;
            }
            check_symbols = true;
        }
        if (!check_symbols) {
            if (query[i] == '"') {
                ++prefix_quotes_cnt;
            }
        }
    }
    if (!check_symbols) {
        throw Searcher::BadQuery("no any word", query);
    }
    if (quotes_cnt % 2 != 0) {
        throw Searcher::BadQuery("odd number of quotes", query);
    }

    bool is_phrase = (prefix_quotes_cnt % 2 != 0);
    while (pos < query.size()) {
        std::string str;
        while (pos < query.size() && query[pos] != '"') {
            str += query[pos++];
        }
        if (!is_phrase) {
            split_by_words(str, words);
        }
        else {
            std::vector<std::string> phrase;
            split_by_words(str, phrase);
            phrases.push_back(phrase);
        }
        is_phrase = !is_phrase;
        ++pos;
    }
}

std::set<Searcher::Filename> Searcher::find_words(const std::vector<std::string> & words, const std::set<Filename> & searching_files) const
{
    std::set<Filename> res;
    for (Filename const & file : searching_files) {
        bool contains_all = true;
        for (std::string const & word : words) {
            auto iter = inverted_index.find(word);
            if (iter == inverted_index.end() || iter->second.find(file) == iter->second.end()) {
                contains_all = false;
                break;
            }
        }
        if (contains_all) {
            res.insert(file);
        }
    }
    return res;
}

std::pair<Searcher::DocIterator, Searcher::DocIterator> Searcher::search(const std::string & query) const
{
    std::vector<std::string> words;
    std::vector<std::vector<std::string>> phrases;
    parse_query(query, words, phrases);
    std::set<Filename> res = find_words(words, files);
    for (auto const & phrase : phrases) {
        std::set<Filename> files_with_phrases = find_words(phrase, res);
        res.clear();
        for (Filename const & file : files_with_phrases) {
            for (size_t const start : inverted_index.find(phrase[0])->second.find(file)->second) {
                bool full_phrase = true;
                for (size_t i = 1; i < phrase.size(); ++i) {
                    std::vector<size_t> const & search_in = inverted_index.find(phrase[i])->second.find(file)->second;
                    auto next_pos = std::lower_bound(search_in.begin(), search_in.end(), start + i);
                    if (next_pos == search_in.end() || *next_pos != start + i) {
                        full_phrase = false;
                        break;
                    }
                }
                if (full_phrase) {
                    res.insert(file);
                    break;
                }
            }
        }
    }
    std::shared_ptr<std::set<Filename>> set_ptr = std::make_shared<std::set<Filename>>(res);
    return {DocIterator(set_ptr, set_ptr->begin()), DocIterator(set_ptr, set_ptr->end())};
}