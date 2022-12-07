#!/bin/bash

i=0
arr=()
echo "" > report2.log

while true
do
	arr+=(1 2 3 4 5 6 7 8 9 10)
	if [[ $(($i % 100000)) -eq 0 ]]
	then
		echo ${#arr[@]} >> report2.log
	fi
	i=$(($i+1))
done
