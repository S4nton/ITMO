#!/bin/bash
date=$(date '+%d.%m.%y_%H:%M:%S')
(mkdir test && echo "catalog test was created successfully" >> report && touch test/$date)
(ping -c 1 http://www.net_nikogo.ru) || (echo "$date :: net_nikogo.ru no answer" >> report)
