#!/bin/bash

echo "./task1.sh &" | at now +1 minute
tail -f ./report | while read line; do echo $line; done &
