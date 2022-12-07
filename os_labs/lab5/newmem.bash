#!/bin/bash

i=0
N=$1

while true
do
	arr+=(1 2 3 4 5 6 7 8 9 10)
	if [[ "${#arr[@]}" -ge "$N" ]]
	then
		break
	fi
done
