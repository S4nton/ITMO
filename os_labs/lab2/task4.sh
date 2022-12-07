#!/bin/bash

ps -e --format="pid" | sed 1d > pids
echo > res4
echo > sort_res4

for pid in $(cat pids)
do
	ppid=$(grep -s "PPid" /proc/$pid/status | sed 's/PPid://')
	ser=$(grep -s "sum_exec_runtime" /proc/$pid/sched | sed 's/se.sum_exec_runtime//; s/://')
	nrs=$(grep -s "nr_switches" /proc/$pid/sched | sed 's/nr_switches//; s/://')
	if [ -n "$nrs" ] && [ "$nrs" -ne 0 ];
	then
		art=$(bc <<< "scale=3;$ser/$nrs")
		echo "ProcessID =" $pid ": Parent_ProcessID =" $ppid ": Average_Runnig_Time =" $art >> res4
	fi
done

sort -nk7 res4 > sort_res4
