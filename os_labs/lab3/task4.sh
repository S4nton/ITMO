#!/bin/bash

cpulimit --limit 10 ./plus_one.sh &
bash ./plus_one.sh &
./plus_one.sh &
kill $!
