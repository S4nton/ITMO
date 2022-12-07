#!/bin/bash

ps -e --format="pid" | sed 1d > pids
echo > vmsizes
echo > sort_vmsizes

for pid in $(cat pids)
do
	vmsize=$(grep -s -i "vmsize" /proc/$pid/status | awk '{print $2}')
	echo $pid $vmsize >> vmsizes
done
sort -nk2 vmsizes | tail -n 1 > sort_vmsizes
