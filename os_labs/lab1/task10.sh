#!/bin/bash

man bash | grep -E -o -w "([a-zA-Z]{4,})" | sort | uniq -c | sort -rnk1 | head -n 3 | awk '{print $2}'
