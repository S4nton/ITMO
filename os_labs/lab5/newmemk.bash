#!/bin/bash

i=0
N=$1
K=$2

while [[ "$i" -ne "$K" ]]
do
	./newmem.bash "$N" &
	i=$(($i+1))
	sleep 1s
done
