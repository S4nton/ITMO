#!/bin/bash

cat /etc/passwd | awk 'BEGIN{FS=":"} {print $1" "$3}' | sort -nk2
