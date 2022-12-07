#!/bin/bash

ps -e --format="pid ppid start_time" --sort="start_time" | sed 1d | awk -v my_pid=$$ '{
					if ($1 != my_pid && $2 != my_pid) {
						print $1 " " $3
					}
					}' | tail -n 1
