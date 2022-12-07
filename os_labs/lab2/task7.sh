#!/bin/bash

ps -e --format="pid" | sed 1d > pids
echo > diffs

for pid in $(cat pids)
do
	rb=$(grep -s "read_bytes:" /proc/$pid/io | awk '{print $2}')
	if [ ! -z rb ]
	then
		cnt[$pid]=$rb
	fi
done

sleep 10

for pid in $(cat pids)
do
	rb=$(grep -s "read_bytes:" /proc/$pid/io | awk '{print $2}')
	cmd=$(cat /proc/$pid/cmdline 2>erorr | tr -d '\0')
	if [ -n rb ]
	then
		diff=$(bc <<< "$rb-${cnt[$pid]}")
		echo $pid ":" $cmd ":" $diff >> diffs
	fi
done

sort -t ':' -nk3 diffs | tail -n 3 > res7



