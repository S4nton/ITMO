#!/bin/bash

bash task4.sh
echo > res5

awk '
BEGIN {
	ppid_now="0"
	cnt=0
	sum=0
}
{
	if ($7 == ppid_now) {
		cnt++
		sum+=$11
	} else {
		avg=sum/cnt
		print "Average_Runnig_Children_of_ParentID=" ppid_now " is " avg
		ppid_now=$7
		cnt=1
		sum=$11
	}
	print $0
}
END {
	avg=sum/cnt
	print "Average_Running_Children_of_ParentID=" ppid_now " is " avg
}
' sort_res4 > res5
