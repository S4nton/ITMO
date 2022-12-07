#!/bin/bash

ans=1
now_op=0
(tail -f pipe) | while true
do
	read LINE
	case "$LINE" in
		"QUIT")
			echo "STOP"
			killall "gen5.sh"
			exit
			;;
		"+")
			now_op=0
			echo "+ num"
			;;
		"*")
			now_op=1
			echo "* num"
			;;
		*)
			if [[ "$LINE" =~ [0-9]+ ]]
			then
				if [[ $now_op -eq 0 ]]
				then
					ans=$(($ans + $LINE))
				else
					ans=$(($ans * $LINE))
				fi
				echo $ans
			else
				echo "Wrong input"
				killall "gen5.sh"
				exit
			fi
			;;
esac
done
