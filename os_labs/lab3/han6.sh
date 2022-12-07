#!/bin/bash

echo $$ > .pid

ans=1

plus2()
{
	now_op="+"
}
mult2()
{
	now_op="*"
}
term()
{
	now_op="term"
}

trap 'plus2' USR1
trap 'mult2' USR2
trap 'term' SIGTERM

while true
do
	case $now_op in
	"+")
		ans=$(($ans + 2))
		echo $ans
		;;
	"*")
		ans=$(($ans * 2))
		echo $ans
		;;
	"term")
		echo "STOP"
		exit
		;;
	esac
	sleep 2
done
