#!/bin/bash

echo -n > res1.1

ps --format="pid cmd" U root | sed 1d >> res1.1

wc -l res1.1 | awk '{print $1}' > res1.2

cat res1.1 >> res1.2
